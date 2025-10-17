package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.ide.plugins.DynamicPluginListener
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.openapi.application.ApplicationManager
import io.unthrottled.doki.icons.jetbrains.Constants.PLUGIN_ID
import io.unthrottled.doki.icons.jetbrains.PluginMaster
import io.unthrottled.doki.icons.jetbrains.tools.Logging

class IDEPluginInstallListener : DynamicPluginListener, Logging {
  override fun beforePluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
  }

  override fun beforePluginUnload(
    pluginDescriptor: IdeaPluginDescriptor,
    isUpdate: Boolean,
  ) {
  }

  override fun pluginLoaded(pluginDescriptor: IdeaPluginDescriptor) {
    if (pluginDescriptor.pluginId.idString == PLUGIN_ID) {
      ApplicationManager.getApplication().invokeLater(
        { PluginMaster.instance.initializePlugin() },
        com.intellij.openapi.application.ModalityState.nonModal(),
      )
    }
  }

  override fun pluginUnloaded(
    pluginDescriptor: IdeaPluginDescriptor,
    isUpdate: Boolean,
  ) {
  }
}
