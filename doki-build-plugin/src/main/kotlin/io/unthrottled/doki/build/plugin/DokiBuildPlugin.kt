package io.unthrottled.doki.build.plugin

import io.unthrottled.doki.build.plugin.tasks.BuildPluginXmlTask
import io.unthrottled.doki.build.plugin.tasks.BuildThemesTask
import io.unthrottled.doki.build.plugin.tasks.MultiExecTask
import io.unthrottled.doki.build.plugin.tasks.PatchHTMLTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

enum class IconType {
  ORIGINAL, CUSTOM;

  val lowercase: String
    get() = this.name.lowercase()
  val titlecase: String
    get() = this.lowercase.replaceFirstChar { it.titlecase() }
}

class DokiBuildPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val iconTypeProp = when (project.findProperty("type")) {
      "custom" -> IconType.CUSTOM
      else -> IconType.ORIGINAL
    }
    project.tasks.register<BuildPluginXmlTask>("buildPluginXml") {
      pluginId.set("io.unthrottled.doki.icons")
      pluginName.set("Doki Icons")
      iconType.set(iconTypeProp)
      assetPluginXml.set(project.layout.projectDirectory.file("doki-build-plugin/assets/plugin.xml"))
      resourcePluginXml.set(project.layout.projectDirectory.file("src/main/resources/META-INF/plugin.xml"))
    }
    project.tasks.register<BuildThemesTask>("buildThemes") {
      dependsOn("buildPluginXml")
      if (iconTypeProp == IconType.CUSTOM){
        dependsOn("genCustomIconTemplate")
      }
      iconType.set(iconTypeProp)
      // buildSrc
      dokiPluginAssetDirectory.set(project.layout.projectDirectory.dir("doki-build-plugin/assets"))
      specialUsedIconsMapping.set(dokiPluginAssetDirectory.file("templates/specialUsedIcons.json"))
      // masterTheme
      masterThemesDirectory.set(project.layout.projectDirectory.dir("masterThemes"))
      // root
      resourcesDirectory.set(project.layout.projectDirectory.dir("src/main/resources"))
      iconsDirectory.set(resourcesDirectory.dir("doki/icons"))
      generatedResourcesDirectory.set(resourcesDirectory.dir("doki/generated"))
      resourceMappingFiles.from(project.fileTree(resourcesDirectory).matching { include("*.json") })
      // iconSource
      iconSourceDirectory.set(project.layout.projectDirectory.dir("iconSource"))
      svgIconSourceDirectory.set(iconSourceDirectory.dir("icons"))
      iconPaletteTemplate.set(iconSourceDirectory.file("buildSrc/assets/templates/icon.palette.template.json"))
    }
    project.tasks.register<PatchHTMLTask>("patchHTML") {
      htmlDirectory.set(project.layout.projectDirectory.dir("build/html"))
    }
    project.tasks.register<MultiExecTask>("buildThemeDeps") {
      description = "Builds yarn dependencies needed to build the doki themes."
      mustRunAfter("getRepos")
      val installCMD = "yarn install"
      commandExecMap.put(
        MultiExecTask.OSType.AUTO, listOf(
          "cd doki-build-source",
          installCMD,
          "yarn build",
          "cd ../masterThemes",
          installCMD,
          "yarn generateIconsTemplates",
          "cd ../iconSource",
          installCMD,
          "yarn buildIcons"
        )
      )
      startDirectory.set(project.layout.projectDirectory)
    }
    project.tasks.register<MultiExecTask>("genCustomIconTemplate") {
      description = "Generate icon templates for custom doki themes."
      mustRunAfter("buildPluginXml")
      commandExecMap.put(MultiExecTask.OSType.AUTO, listOf("cd masterThemes", "yarn generateCustomIconsTemplate"))
    }
    val masterThemeDir = project.layout.projectDirectory.dir("masterThemes")
    project.tasks.register<MultiExecTask>("getRepos") {
      description = "Retrieves all repositories doki-theme-icons-jetbrains relies on."
      onlyIf { masterThemeDir.asFile.exists().not() }
      val gitCMD = "git clone"
      val githubBaseURL = "https://github.com/ZimCodes"
      val repoNames = mapOf(
        "doki-master-theme" to "masterThemes",
        "doki-icon-source" to "iconSource",
        "doki-build-source-jvm" to null,
        "doki-build-source" to null
      )
      val commands = repoNames.map { (repoName, folderName) ->
        "$gitCMD $githubBaseURL/$repoName${folderName?.let { " $folderName" } ?: ""}"
      }
      commandExecMap.put(MultiExecTask.OSType.AUTO, commands)
    }
    project.tasks.register<MultiExecTask>("updateRepos") {
      description = "Gets the latest changes made to all repo dependencies from remote repo"
      val pullCMD = "git pull origin main"
      commandExecMap.put(
        MultiExecTask.OSType.AUTO,
        listOf(
          "cd doki-build-source",
          pullCMD,
          "cd ../doki-build-source-jvm",
          pullCMD,
          "cd ../masterThemes",
          pullCMD,
          "cd ../iconSource",
          pullCMD
        )
      )
    }
  }
}