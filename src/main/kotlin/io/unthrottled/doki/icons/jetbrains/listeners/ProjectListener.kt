package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.openapi.project.ProjectManagerListener
import io.unthrottled.doki.icons.jetbrains.PluginMaster
import io.unthrottled.doki.icons.jetbrains.tools.Logging

internal class ProjectListener :
  ProjectManagerListener, Logging {
  init {
    PluginMaster.getInstance()
  }
}
