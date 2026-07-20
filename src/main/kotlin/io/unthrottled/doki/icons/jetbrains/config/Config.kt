package io.unthrottled.doki.icons.jetbrains.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.SerializablePersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import io.unthrottled.doki.icons.jetbrains.themes.IconThemeManager

@Service
@State(
  name = "doki-theme-icon-config",
  storages = [Storage("doki-theme-icons.xml")],
)
class Config : SerializablePersistentStateComponent<Config.StateConfig>(StateConfig()) {
  companion object {
    @JvmStatic
    fun getInstance(): Config = ApplicationManager.getApplication().getService(Config::class.java)
  }

  var currentThemeId: String
    get() = state.currentThemeId
    set(value) {
      updateState {
        it.copy(currentThemeId = value)
      }
    }
  var isUIIcons: Boolean
    get() = state.isUIIcons
    set(value) {
      updateState {
        it.copy(isUIIcons = value)
      }
    }
  var isNamedFileIcons: Boolean
    get() = state.isNamedFileIcons
    set(value) {
      updateState {
        it.copy(isNamedFileIcons = value)
      }
    }
  var isGlyphIcon: Boolean
    get() = state.isGlyphIcon
    set(value) {
      updateState {
        it.copy(isGlyphIcon = value)
      }
    }
  var isNamedFolderIcons: Boolean
    get() = state.isNamedFolderIcons
    set(value) {
      updateState {
        it.copy(isNamedFolderIcons = value)
      }
    }
  var isMyIcons: Boolean
    get() = state.isMyIcons
    set(value) {
      updateState {
        it.copy(isMyIcons = value)
      }
    }
  var syncWithDokiTheme: Boolean
    get() = state.syncWithDokiTheme
    set(value) {
      updateState {
        it.copy(syncWithDokiTheme = value)
      }
    }

  data class StateConfig(
    var currentThemeId: String = IconThemeManager.DEFAULT_THEME_ID,
    var isUIIcons: Boolean = true,
    var isNamedFileIcons: Boolean = true,
    var isGlyphIcon: Boolean = true,
    var isNamedFolderIcons: Boolean = true,
    var isMyIcons: Boolean = true,
    var syncWithDokiTheme: Boolean = false,
  )
}
