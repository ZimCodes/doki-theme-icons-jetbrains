package io.unthrottled.doki.icons.jetbrains.path

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.intellij.openapi.util.IconLoader
import io.unthrottled.doki.icons.jetbrains.config.Config
import io.unthrottled.doki.icons.jetbrains.config.IconConfigListener
import io.unthrottled.doki.icons.jetbrains.config.IconSettingsModel
import io.unthrottled.doki.icons.jetbrains.tools.updateToolbars

data class IconReplacementPack(
  val iconPatcher: DokiIconPathPatcher,
  val iconSettingsExtractor: (IconSettingsModel) -> Boolean,
  val iconConfigExtractor: (Config) -> Boolean,
)

object IconPathReplacementComponent : IconConfigListener {
  private val iconInstallPacs =
    listOf(
      IconReplacementPack(
        DokiIconPathPatcher("alex-icons.path.mappings.json"),
        { it.isMyIcons },
        { it.isMyIcons },
      ),
      IconReplacementPack(
        DokiIconPathPatcher("ui-icons.path.mappings.json"),
        { it.isUIIcons },
        { it.isUIIcons },
      ),
      IconReplacementPack(
        DokiIconPathPatcher("node.path.mappings.json"),
        { it.isUIIcons },
        { it.isUIIcons },
      ),
      IconReplacementPack(
        DokiIconPathPatcher("file-icons.path.mappings.json"),
        { it.isUIIcons },
        { it.isUIIcons },
      ),
      IconReplacementPack(
        DokiIconPathPatcher("glyph-icons.path.mappings.json"),
        { it.isGlyphIcons },
        { it.isGlyphIcon },
      ),
    )

  private val connection = ApplicationManager.getApplication().messageBus.connect()

  fun initialize() {
    this.connection.subscribe(IconConfigListener.TOPIC, this)
    installComponents()
  }

  fun installComponents() {
    ExperimentalUIFixer.fixExperimentalUI()

    iconInstallPacs.forEach { pak ->
      IconLoader.removePathPatcher(pak.iconPatcher)
      if (pak.iconConfigExtractor(Config.instance)) {
        IconLoader.installPathPatcher(pak.iconPatcher)
      }
    }
  }

  fun dispose() {
    connection.dispose()
    iconInstallPacs.forEach { pak ->
      IconLoader.removePathPatcher(pak.iconPatcher)
    }
  }

  override fun iconConfigUpdated(
    previousState: IconSettingsModel,
    newState: IconSettingsModel,
  ) {
    val hasChanged =
      iconInstallPacs.any {
        it.iconSettingsExtractor(previousState) != it.iconSettingsExtractor(newState)
      }
    if (!hasChanged) {
      return
    }

    iconInstallPacs.forEach {
      IconLoader.removePathPatcher(it.iconPatcher)
    }

    iconInstallPacs.forEach { pak ->
      val newIconPatchState = pak.iconSettingsExtractor(newState)
      if (newIconPatchState) {
        IconLoader.installPathPatcher(pak.iconPatcher)
      }
    }

    refresh()
  }

  private fun refresh() {
    ExperimentalUIFixer.fixExperimentalUI()
    val app = ApplicationManager.getApplication()
    app.invokeLater(
      {
        if (app.isDisposed) return@invokeLater
        try {
          app.runWriteAction {
            FileTypeManagerEx.getInstanceEx().fireFileTypesChanged()
          }
        } catch (t: Throwable) {
          // no-op: avoid interfering with modal dialogs
        }
        try {
          app.runWriteAction { updateToolbars() }
        } catch (t: Throwable) {
          // no-op: avoid interfering with modal dialogs
        }
      },
      com.intellij.openapi.application.ModalityState.nonModal(),
    )
  }
}
