package io.unthrottled.doki.icons.jetbrains.listeners

import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.ui.LafManager
import com.intellij.openapi.project.ProjectManager
import io.unthrottled.doki.icons.jetbrains.config.IconConfigListener
import io.unthrottled.doki.icons.jetbrains.settings.IconSettingsModel

class ProjectRefreshListener : IconConfigListener {
  override fun iconConfigUpdated(
    newState: IconSettingsModel,
  ) {
    LafManager.getInstance().updateUI()
    refreshProjects()
  }

  private fun refreshProjects() {
    ProjectManager.getInstance().openProjects
      .mapNotNull { ProjectView.getInstance(it) }
      .forEach {
          projectView ->
        projectView.refresh()
        projectView.currentProjectViewPane?.updateFromRoot(true)
      }
  }
}
