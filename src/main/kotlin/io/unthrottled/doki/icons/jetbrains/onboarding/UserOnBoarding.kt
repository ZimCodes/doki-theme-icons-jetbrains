package io.unthrottled.doki.icons.jetbrains.onboarding

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import io.unthrottled.doki.icons.jetbrains.Constants.PLUGIN_ID
import io.unthrottled.doki.icons.jetbrains.config.Config
import io.unthrottled.doki.icons.jetbrains.tools.toOptional
import java.util.Optional
import java.util.UUID

object UserOnBoarding {
  fun attemptToPerformNewUpdateActions(project: Project) {
    getNewVersion().ifPresent { newVersion ->
      Config.getInstance().version = newVersion
      StartupManager.getInstance(project)
        .runAfterOpened {
          UpdateNotification.display(project, newVersion)
        }
    }

    val isNewUser = Config.getInstance().userId.isEmpty()
    if (isNewUser) {
      Config.getInstance().userId = UUID.randomUUID().toString()
    }
  }

  private fun getNewVersion() =
    getVersion()
      .filter { it != Config.getInstance().version }

  fun getVersion(): Optional<String> =
    PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID))
      .toOptional()
      .map { it.version }
}
