package io.unthrottled.doki.icons.jetbrains

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.ProjectManagerListener
import io.unthrottled.doki.icons.jetbrains.laf.LAFIconReplacementComponent
import io.unthrottled.doki.icons.jetbrains.path.IconPathReplacementComponent
import io.unthrottled.doki.icons.jetbrains.svg.ThemedSVGManager
import io.unthrottled.doki.icons.jetbrains.themes.IconThemeManager
import io.unthrottled.doki.icons.jetbrains.tools.Logging

@Service
class PluginMaster : ProjectManagerListener, Disposable, Logging {
  companion object {
    fun getInstance(): PluginMaster = ApplicationManager.getApplication().getService(PluginMaster::class.java)
  }

  init {
    IconThemeManager.getInstance().init()
    ThemedSVGManager.getInstance().initialize()
    IconPathReplacementComponent.initialize()
    LAFIconReplacementComponent.initialize()
  }

  override fun dispose() {
    IconThemeManager.getInstance().dispose()
    ThemedSVGManager.getInstance().dispose()
    IconPathReplacementComponent.dispose()
    LAFIconReplacementComponent.dispose()
  }
}