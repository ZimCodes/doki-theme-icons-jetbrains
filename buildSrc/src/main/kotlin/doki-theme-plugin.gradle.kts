tasks.register<BuildThemes>("buildThemes") {
  // buildSrc
  buildSourceAssetDirectory.set(project.layout.projectDirectory.dir("buildSrc/assets"))
  specialUsedIconsMapping.set(buildSourceAssetDirectory.file("templates/specialUsedIcons.json"))
  // masterTheme
  masterThemesDirectory.set(project.layout.projectDirectory.dir("masterThemes"))
  // root
  resourcesDirectory.set(project.layout.projectDirectory.dir("src/main/resources"))
  iconsDirectory.set(resourcesDirectory.dir("doki/icons"))
  generatedResourcesDirectory.set(resourcesDirectory.dir("doki/generated"))
  resourceMappingFiles.from(fileTree(resourcesDirectory).matching{ include("*.json")})
  // iconSource
  iconSourceDirectory.set(project.layout.projectDirectory.dir("iconSource"))
  svgIconSourceDirectory.set(iconSourceDirectory.dir("icons"))
  iconPaletteTemplate.set(iconSourceDirectory.file("buildSrc/assets/templates/icon.palette.template.json"))
}
tasks.register<PatchHTML>("patchHTML") {
  htmlDirectory.set(project.layout.projectDirectory.dir("build/html"))
}