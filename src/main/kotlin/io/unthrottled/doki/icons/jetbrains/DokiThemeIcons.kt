package io.unthrottled.doki.icons.jetbrains

import com.intellij.ide.plugins.PluginDetailsService
import com.intellij.openapi.extensions.PluginId
import io.unthrottled.doki.icons.jetbrains.tools.toOptional
import java.util.Optional

object DokiThemeIcons {

  fun getVersion(): Optional<String> =
    // NOTE: API provides info about installed plugins -> https://github.com/JetBrains/intellij-community/blob/master/platform/core-impl/src/com/intellij/ide/plugins/PluginDetails.kt
    PluginDetailsService.getInstance().findDetails(PluginId.getId(Constants.PLUGIN_ID)).toOptional().map { it.version }
}
