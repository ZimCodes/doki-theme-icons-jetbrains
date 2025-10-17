package io.unthrottled.doki.icons.jetbrains.themes

import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfo
import javax.swing.UIManager

fun UIManager.LookAndFeelInfo.getId(): String =
  when (this) {
    is UIThemeLookAndFeelInfo -> this.id
    else -> this.name
  }
