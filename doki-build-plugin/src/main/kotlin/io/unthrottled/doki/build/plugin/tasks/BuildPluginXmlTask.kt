package io.unthrottled.doki.build.plugin.tasks

import groovy.util.Node
import groovy.util.NodeList
import io.unthrottled.doki.build.jvm.tools.parseXml
import io.unthrottled.doki.build.jvm.tools.writeXmlToFile
import io.unthrottled.doki.build.plugin.IconType
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

abstract class BuildPluginXmlTask : DefaultTask() {
  @get:Input
  abstract val iconType: Property<IconType>

  @get:Input
  abstract val pluginId: Property<String>

  @get:Input
  abstract val pluginName: Property<String>

  @get:InputFile
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val assetPluginXml: RegularFileProperty

  @get:OutputFile
  abstract val resourcePluginXml: RegularFileProperty

  init {
    group = "doki"
    description = "Generates a plugin.xml file and place it in resources folder"
  }

  @TaskAction
  fun run() {
    val myIconType = iconType.get()
    val pluginXml: Node = parseXml(assetPluginXml.get().asFile.toPath())
    applyPluginXmlId(pluginXml, myIconType)
    applyPluginXmlName(pluginXml, myIconType)
    writeXmlToFile(resourcePluginXml.get().asFile.toPath(), pluginXml)
  }

  private fun getNode(pluginXml: Node, xmlTag: String): Node {
    val idNodeList = pluginXml[xmlTag] as NodeList
    return idNodeList[0] as Node
  }

  private fun applyPluginXmlId(pluginXml: Node, iconType: IconType) {
    getNode(pluginXml, "id").setValue("${pluginId.get()}.${iconType.lowercase}")
  }

  private fun getPluginName(iconType: IconType) = when (iconType) {
    IconType.ORIGINAL -> ""
    else -> " ${iconType.titlecase}"
  }

  private fun applyPluginXmlName(pluginXml: Node, iconType: IconType) {
    getNode(pluginXml, "name").setValue("${pluginName.get()}${getPluginName(iconType)}")
  }

}