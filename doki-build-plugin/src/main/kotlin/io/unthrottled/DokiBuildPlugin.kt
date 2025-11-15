package io.unthrottled.doki.build.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class DokiBuildPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register<BuildThemesTask>("buildThemes") {
      // buildSrc
      buildSourceAssetDirectory.set(project.layout.projectDirectory.dir("doki-build-plugin/assets"))
      specialUsedIconsMapping.set(buildSourceAssetDirectory.file("templates/specialUsedIcons.json"))
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
  }
}