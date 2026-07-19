package io.unthrottled.doki.icons.jetbrains.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.ComboBox
import io.unthrottled.doki.icons.jetbrains.config.Config
import io.unthrottled.doki.icons.jetbrains.config.IconConfigListener.Companion.TOPIC
import io.unthrottled.doki.icons.jetbrains.themes.DokiTheme
import io.unthrottled.doki.icons.jetbrains.themes.IconThemeManager
import org.jetbrains.annotations.UnknownNullability
import java.util.*
import javax.swing.DefaultComboBoxModel


object IconSettings {
  const val SETTINGS_ID = "io.unthrottled.doki.icons.jetbrains.settings.ThemeSettings"
  const val ICON_SETTINGS_DISPLAY_NAME = "Doki Theme Icons Settings"

  @JvmStatic
  fun createSettingsModule(): IconSettingsModel =
    IconSettingsModel(
      isUIIcons = Config.getInstance().isUIIcons,
      isNamedFileIcons = Config.getInstance().isNamedFileIcons,
      isGlyphIcons = Config.getInstance().isGlyphIcon,
      isNamedFolderIcons = Config.getInstance().isNamedFolderIcons,
      isMyIcons = Config.getInstance().isMyIcons,
      currentThemeId = getDokiTheme().id,
      currentThemeName = getDokiTheme().listName,
      syncWithDokiTheme = Config.getInstance().syncWithDokiTheme,
    )

  @Throws(ConfigurationException::class)
  fun apply(model: IconSettingsModel) {
    with(Config.getInstance()) {
      this.isUIIcons = model.isUIIcons
      this.isNamedFileIcons = model.isNamedFileIcons
      this.isGlyphIcon = model.isGlyphIcons
      this.isNamedFolderIcons = model.isNamedFolderIcons
      this.currentThemeId = model.currentThemeId
      this.currentThemeName = getDokiTheme().listName
      this.syncWithDokiTheme = model.syncWithDokiTheme
      this.isMyIcons = model.isMyIcons
    }

    ApplicationManager.getApplication()
      .messageBus
      .syncPublisher(TOPIC)
      .iconConfigUpdated(model)
  }

  fun getDokiTheme(id: String? = null): DokiTheme =
    IconThemeManager.getInstance().getThemeById(
      id?.let { id } ?: Config.getInstance().currentThemeId,
    ).orElseGet {
      IconThemeManager.getInstance().defaultTheme
    }

  fun getThemeList() = IconThemeManager.getInstance().allThemes
    .sortedBy { theme -> theme.listName }
}