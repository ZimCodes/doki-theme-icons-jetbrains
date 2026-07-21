package io.unthrottled.doki.icons.jetbrains.integrations

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import io.unthrottled.doki.icons.jetbrains.tools.Logging

const val DOKI_THEME_COMMUNITY_PLUGIN_ID = "io.acari.DDLCTheme"
private const val DOKI_THEME_ULTIMATE_PLUGIN_ID = "io.unthrottled.DokiTheme"

object PluginService : Logging {
  fun isDokiThemeInstalled(): Boolean =
    PluginManagerCore.isPluginInstalled(
      PluginId.getId(DOKI_THEME_COMMUNITY_PLUGIN_ID),
    ) ||
      PluginManagerCore.isPluginInstalled(
        PluginId.getId(DOKI_THEME_ULTIMATE_PLUGIN_ID),
      )
}
