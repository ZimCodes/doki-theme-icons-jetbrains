package io.unthrottled.doki.icons.jetbrains.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.ConfigurationException
import io.unthrottled.doki.icons.jetbrains.config.Config
import io.unthrottled.doki.icons.jetbrains.config.IconConfigListener.Companion.TOPIC
import io.unthrottled.doki.icons.jetbrains.themes.DokiTheme
import io.unthrottled.doki.icons.jetbrains.themes.IconThemeManager

object IconSettings {
  const val SETTINGS_ID = "io.unthrottled.doki.icons.jetbrains.settings.ThemeSettings"
  const val ICON_SETTINGS_DISPLAY_NAME = "Doki Theme Icons Settings"

  @JvmStatic
  fun createSettingsModule(): IconSettingsModel {
    val config = Config.getInstance()
    return IconSettingsModel(
      isUIIcons = config.isUIIcons,
      isNamedFileIcons = config.isNamedFileIcons,
      isGlyphIcons = config.isGlyphIcon,
      isNamedFolderIcons = config.isNamedFolderIcons,
      isMyIcons = config.isMyIcons,
      currentThemeId = config.currentThemeId,
      syncWithDokiTheme = config.syncWithDokiTheme,
    )
  }

  @Throws(ConfigurationException::class)
  fun apply(model: IconSettingsModel) {
    with(Config.getInstance()) {
      this.isUIIcons = model.isUIIcons
      this.isNamedFileIcons = model.isNamedFileIcons
      this.isGlyphIcon = model.isGlyphIcons
      this.isNamedFolderIcons = model.isNamedFolderIcons
      this.currentThemeId = model.currentThemeId
      this.syncWithDokiTheme = model.syncWithDokiTheme
      this.isMyIcons = model.isMyIcons
    }
    ApplicationManager.getApplication()
      .messageBus
      .syncPublisher(TOPIC)
      .iconConfigUpdated(model)
  }

  fun getDokiThemeById(id: String? = null): DokiTheme =
    IconThemeManager.getInstance().getThemeById(
      id?.let { id } ?: Config.getInstance().currentThemeId,
    ).orElseGet {
      getDefaultDokiTheme()
    }

  fun getDefaultDokiTheme(): DokiTheme = IconThemeManager.getInstance().defaultTheme

  fun getDokiThemeByName(listName: String?): DokiTheme = IconThemeManager.getInstance().getThemeByListName(
    listName?.let { listName } ?: Config.getInstance().currentThemeId,
  ).orElseGet {
    IconThemeManager.getInstance().defaultTheme
  }


  fun getThemeListNames(): List<String> = IconThemeManager.getInstance().allThemes
    .sortedBy { theme -> theme.listName }
    .map { dokiTheme -> dokiTheme.listName }
}