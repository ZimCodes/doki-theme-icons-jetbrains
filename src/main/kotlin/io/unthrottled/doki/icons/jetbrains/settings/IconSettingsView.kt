package io.unthrottled.doki.icons.jetbrains.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.selected
import com.intellij.ui.layout.not
import icons.DokiThemeIconz
import io.unthrottled.doki.icons.jetbrains.Constants
import io.unthrottled.doki.icons.jetbrains.tools.PluginMessageBundle
import org.jetbrains.annotations.NonNls
import javax.swing.JComponent

class IconSettingsView(private val model: IconSettingsModel = IconSettings.createSettingsModule()) :
  SearchableConfigurable,
  DumbAware {
  private lateinit var iconPanel: DialogPanel
  private lateinit var syncWithDokiThemeCheckbox: Cell<JBCheckBox>
  override fun getId(): @NonNls String = IconSettings.SETTINGS_ID
  override fun getDisplayName(): String = IconSettings.ICON_SETTINGS_DISPLAY_NAME

  private fun createIconPanel(): DialogPanel = panel {
    row {
      syncWithDokiThemeCheckbox = checkBox(PluginMessageBundle.message("settings.general.sync.with.theme")).bindSelected(model::syncWithDokiTheme)
    }
    row(PluginMessageBundle.message("settings.general.current.theme")) {
      comboBox(IconSettings.getThemeListNames()).bindItem(
        { IconSettings.getDokiThemeById(model.currentThemeId).listName },
        {
          model.currentThemeId =
            it?.let { IconSettings.getDokiThemeByName(it).id } ?: IconSettings.getDefaultDokiTheme().id
        })
        .enabledIf(syncWithDokiThemeCheckbox.selected.not())
    }
    group(PluginMessageBundle.message("settings.general.icons.settings")) {
      row {
        icon(DokiThemeIconz.PROJECT_GLYPH)
        checkBox(PluginMessageBundle.message("settings.general.icons.ui")).bindSelected(model::isUIIcons)
      }
      row {
        icon(DokiThemeIconz.CURLY_BRACES)
        checkBox(PluginMessageBundle.message("settings.general.icons.folders")).bindSelected(model::isNamedFolderIcons)
      }
      row {
        icon(DokiThemeIconz.CURLY_BRACES)
        checkBox(PluginMessageBundle.message("settings.general.icons.file")).bindSelected(model::isNamedFileIcons)
      }
      row {
        icon(DokiThemeIconz.SOLID_DOKI_GLYPH)
        checkBox(PluginMessageBundle.message("settings.general.icons.glyph")).bindSelected(model::isGlyphIcons)
      }
      row {
        icon(DokiThemeIconz.MAMSNRHBR_CHEHFDE)
        checkBox(PluginMessageBundle.message("settings.general.icons.alex")).bindSelected(model::isMyIcons)
      }
      row {
        browserLink(PluginMessageBundle.message("settings.report.bug"), "${Constants.REPO_URL}/issues")
      }
    }
  }

  override fun createComponent(): JComponent {
    iconPanel = createIconPanel()
    return iconPanel
  }

  override fun isModified(): Boolean = ::iconPanel.isInitialized && iconPanel.isModified()

  override fun apply() {
    iconPanel.apply()
    IconSettings.apply(model)
  }

  override fun reset() {
    if (::iconPanel.isInitialized) {
      iconPanel.reset()
    }
  }

}
