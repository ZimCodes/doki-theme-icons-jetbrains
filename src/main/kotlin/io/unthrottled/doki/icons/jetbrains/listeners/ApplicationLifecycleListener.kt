package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.icons.jetbrains.PluginMaster

class ApplicationLifecycleListener : AppLifecycleListener, DumbAware {
  override fun appFrameCreated(commandLineArgs: MutableList<String>) {
    PluginMaster.instance.initializePlugin()
  }

  override fun appClosing() {
    PluginMaster.instance.dispose()
  }
}
