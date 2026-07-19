package io.unthrottled.doki.icons.jetbrains.settings

data class IconSettingsModel(
  var isUIIcons: Boolean,
  var isNamedFileIcons: Boolean,
  var isGlyphIcons: Boolean,
  var isNamedFolderIcons: Boolean,
  var isMyIcons: Boolean,
  var currentThemeId: String,
  var currentThemeName: String,
  var syncWithDokiTheme: Boolean,
)
