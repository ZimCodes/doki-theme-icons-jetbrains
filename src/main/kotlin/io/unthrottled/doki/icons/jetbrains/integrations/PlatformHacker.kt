package io.unthrottled.doki.icons.jetbrains.integrations

import io.unthrottled.doki.icons.jetbrains.path.IconPathReplacementComponent
import io.unthrottled.doki.icons.jetbrains.tools.Logging
import io.unthrottled.doki.icons.jetbrains.tools.logger
import io.unthrottled.doki.icons.jetbrains.tools.runSafely
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

object PlatformHacker : Logging {
  fun init() {
    IconPathReplacementComponent.installComponents()
    hackPlatform()
  }

  private fun hackPlatform() {
    fixEXPUIButton()
    fixEXPUIDefaultButton()
    fixEXPUIRunWidget()
  }

  private fun fixEXPUIRunWidget() {
    val fixName = "fixRunWidget"
    val uiFQ = "com.intellij.execution.ui.RunState"
    fixEXPUI(
      uiFQ,
      "com.intellij.execution.ui.RunWidgetButtonLook",
      "paintIcon",
      fixName
    )
    fixEXPUI(
      uiFQ,
      "com.intellij.execution.ui.RedesignedRunConfigurationSelector",
      "update",
      fixName
    )
  }

  private fun fixEXPUIButton() = fixEXPUI(
    "com.intellij.openapi.wm.impl.SideStack",
    "com.intellij.openapi.wm.impl.SquareStripeButtonLook",
    "paintIcon",
    "fixButton"
  )

  private fun fixEXPUIDefaultButton() = fixEXPUI(
    "com.intellij.ide.ui.laf.darcula.ui.DarculaMenuSeparatorUI",
    "com.intellij.ide.ui.laf.darcula.ui.DarculaOptionButtonUI",
    "paintArrow",
    "fixDefaultButton"
  )

  private fun fixEXPUI(
    uiFQ: String,
    uiChildFQ: String,
    iconMethod: String,
    fixName: String = ::fixEXPUI.name
  ) {
    runSafely({
      val cp = ClassPool(true)
      cp.insertClassPath(
        ClassClassPath(
          Class.forName(uiFQ),
        ),
      )
      val ctClass = cp[uiChildFQ]
      ctClass.getDeclaredMethods(iconMethod).forEach { doPaintText ->
        doPaintText.instrument(
          object : ExprEditor() {
            override fun edit(m: MethodCall?) {
              if (m?.methodName == "toStrokeIcon") {
                m.replace($$"{ $_ = $1; }")
              }
            }
          },
        )
      }
      ctClass.toClass()
    }) {
      logger().warn("Unable to hack '$fixName' for raisins", it)
    }
  }
}
