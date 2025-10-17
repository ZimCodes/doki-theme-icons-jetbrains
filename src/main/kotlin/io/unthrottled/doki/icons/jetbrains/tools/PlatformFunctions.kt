package io.unthrottled.doki.icons.jetbrains.tools

import com.intellij.openapi.diagnostic.Logger

val logger = Logger.getInstance("io.unthrottled.doki.icons.jetbrains.tools.PlatformFunctions")

fun updateToolbars() {
  val actionToolbarClass = runSafelyWithResult({
    Class.forName("com.intellij.openapi.actionSystem.impl.ActionToolbarImpl")
  }) {
    logger.warn("Error getting toolbar ", it)
    null
  } ?: return

  try {
    // Prefer zero-arg variant if available
    val zeroArg = actionToolbarClass.methods.firstOrNull {
      it.name == "updateAllToolbarsImmediately" && it.parameterCount == 0
    }
    if (zeroArg != null) {
      zeroArg.isAccessible = true
      zeroArg.invoke(null)
      return
    }

    // Fallback to single-arg (boolean) variant used in newer IDEs
    val oneArg = actionToolbarClass.methods.firstOrNull {
      it.name == "updateAllToolbarsImmediately" && it.parameterCount == 1
    }
    if (oneArg != null) {
      val paramType = oneArg.parameterTypes[0]
      val arg: Any = when {
        paramType == java.lang.Boolean.TYPE || paramType == java.lang.Boolean::class.java -> true
        else -> {
          logger.warn("Unsupported parameter type for updateAllToolbarsImmediately: ${paramType.name}")
          return
        }
      }
      oneArg.isAccessible = true
      oneArg.invoke(null, arg)
      return
    }

    logger.warn("Could not find updateAllToolbarsImmediately method on ActionToolbarImpl")
  } catch (t: Throwable) {
    logger.warn("Failed to invoke updateAllToolbarsImmediately", t)
  }
}

