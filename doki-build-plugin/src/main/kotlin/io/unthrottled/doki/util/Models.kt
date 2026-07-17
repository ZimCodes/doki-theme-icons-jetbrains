package io.unthrottled.doki.build.plugin.util

data class DokiTheme(
  val id: String,
  val name: String,
  val displayName: String,
  val group: String,
  val listName: String,
  val colors: Map<String, String>,
)

data class IconPathMapping(
  val iconName: String,
  val isOddBall: Boolean?,
)
