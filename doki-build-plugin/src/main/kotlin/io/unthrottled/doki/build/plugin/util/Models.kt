package io.unthrottled.doki.build.plugin.util

import io.unthrottled.doki.build.jvm.models.HasColors
import io.unthrottled.doki.build.jvm.models.HasId
import io.unthrottled.doki.build.jvm.models.StringDictionary

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

data class IconsAppDefinition(
  override val id: String,
  val overrides: StringDictionary<Any>,
  override val colors: StringDictionary<String>
) : HasId, HasColors
