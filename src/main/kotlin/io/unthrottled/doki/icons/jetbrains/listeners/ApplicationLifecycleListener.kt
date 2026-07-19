package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.ide.AppLifecycleListener
import com.intellij.openapi.project.DumbAware
import io.unthrottled.doki.icons.jetbrains.PluginMaster

class ApplicationLifecycleListener : AppLifecycleListener, DumbAware {
  override fun appClosing() {
    PluginMaster.getInstance().dispose()
  }
}
