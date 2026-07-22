package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.icons.jetbrains.PluginMaster
import io.unthrottled.doki.icons.jetbrains.integrations.PlatformHacker

class ApplicationLifecycleListener : AppLifecycleListener, DumbAware {
  override fun appFrameCreated(commandLineArgs: MutableList<String>) {
    PlatformHacker.init()
  }

  override fun appClosing() {
    PluginMaster.getInstance().dispose()
  }
}
