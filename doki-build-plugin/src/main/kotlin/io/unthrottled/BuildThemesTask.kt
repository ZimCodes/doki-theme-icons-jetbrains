package io.unthrottled.doki.build.plugin

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.unthrottled.doki.build.jvm.models.AssetTemplateDefinition
import io.unthrottled.doki.build.jvm.models.IconsAppDefinition
import io.unthrottled.doki.build.jvm.models.MasterThemeDefinition
import io.unthrottled.doki.build.jvm.tools.BuildFunctions
import io.unthrottled.doki.build.jvm.tools.BuildFunctions.combineMaps
import io.unthrottled.doki.build.jvm.tools.CommonConstructionFunctions.getAllDokiThemeDefinitions
import io.unthrottled.doki.build.jvm.tools.ConstructableAssetSupplier
import io.unthrottled.doki.build.jvm.tools.ConstructableAssetSupplierFactory
import io.unthrottled.doki.build.jvm.tools.ConstructableTypes
import io.unthrottled.doki.build.jvm.tools.DokiProduct
import io.unthrottled.doki.build.jvm.tools.GroupToNameMapping.getLafNamePrefix
import io.unthrottled.doki.build.jvm.tools.PathTools.cleanDirectory
import io.unthrottled.doki.build.jvm.tools.PathTools.ensureDirectoryExists
import io.unthrottled.doki.build.jvm.tools.PathTools.readJSONFromFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths.get
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

// todo figure out how to share.
data class DokiTheme(
  val id: String,
  val name: String,
  val displayName: String,
  val group: String,
  val listName: String,
  val colors: Map<String, String>,
)

data class IconPathMapping(
  val iconName: String,
  val isOddBall: Boolean?,
)

@CacheableTask
abstract class BuildThemesTask : DefaultTask() {

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val buildSourceAssetDirectory: DirectoryProperty

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val masterThemesDirectory: DirectoryProperty

  @get:Internal
  abstract val resourcesDirectory: DirectoryProperty

  @get:OutputDirectory
  abstract val iconsDirectory: DirectoryProperty

  @get:OutputDirectory
  abstract val generatedResourcesDirectory: DirectoryProperty

  @get:InputFiles
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  abstract val resourceMappingFiles: ConfigurableFileCollection

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  abstract val specialUsedIconsMapping: RegularFileProperty

  @get:Internal
  abstract val iconSourceDirectory: DirectoryProperty

  @get:InputDirectory
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val svgIconSourceDirectory: DirectoryProperty

  @get:InputFile
  @get:PathSensitive(PathSensitivity.NAME_ONLY)
  abstract val iconPaletteTemplate: RegularFileProperty

  init {
    group = "doki"
    description = "Builds all the themes and places them in the proper places"
  }

  @TaskAction
  fun run() {
    val buildSourceAssetDirectory = buildSourceAssetDirectory.get().asFile.toPath()
    val masterThemesDirectory = masterThemesDirectory.get().asFile.toPath()
    val constructableAssetSupplier =
      ConstructableAssetSupplierFactory.createCommonAssetsTemplate(
        buildSourceAssetDirectory,
        masterThemesDirectory
      )

    cleanDirectory(getGenerateResourcesDirectory())

    val jetbrainsIconsThemeDirectory = get(buildSourceAssetDirectory.toString(),"themes")

    val allDokiThemeDefinitions = getAllDokiThemeDefinitions(
      DokiProduct.ICONS,
      jetbrainsIconsThemeDirectory,
      masterThemesDirectory,
      IconsAppDefinition::class.java
    ).collect(Collectors.toList())

    val dokiThemes = allDokiThemeDefinitions
      .map {
        constructDokiTheme(it, constructableAssetSupplier)
      }

    writeThemesAsJson(dokiThemes)

    copyUsedIconsFromIconSource()

    copyIconPaletteFromIconSource()
  }

  private fun copyIconPaletteFromIconSource() {
    if (isCI()) {
      return
    }

    Files.copy(
      iconPaletteTemplate.get().asFile.toPath(),
      get(
        getGenerateResourcesDirectory().toAbsolutePath().toString(),
        "icon.palette.template.json"
      )
    )
  }

  private fun isCI(): Boolean {
    return System.getenv("IS_CI") == "true"
  }

  private fun copyUsedIconsFromIconSource() {
    if (isCI()) {
      return
    }
    val iconsDirectory = iconsDirectory.get().asFile.toPath()
    ensureDirectoryExists(iconsDirectory)
    cleanDirectory(iconsDirectory)

    val allUsedIcons = resourceMappingFiles.map { it.toPath() }.flatMap {
      readJSONFromFile(it,
      object: TypeToken<List<IconPathMapping>>(){}
      )
    }.filter { it.isOddBall != true }
      .map { it.iconName }
      .toMutableSet()

    allUsedIcons.addAll(
      readJSONFromFile(
        specialUsedIconsMapping.get().asFile.toPath(),
        object : TypeToken<List<String>>() {}
      )
    )

    val stagingIconDirectory = get("staging")
    val copiedIcons = Files.walk(svgIconSourceDirectory.get().asFile.toPath())
      .filter {
        allUsedIcons.contains(it.fileName.toString()) &&
          it.contains(stagingIconDirectory).not()
      }
      .map { dokiIconPath ->
        Files.copy(
          dokiIconPath,
          get(
            iconsDirectory.toString(),
            dokiIconPath.fileName.toString()
          ),
          StandardCopyOption.REPLACE_EXISTING
        )
        dokiIconPath.fileName.toString()
      }
      .collect(Collectors.toList())

    val diff =
      allUsedIcons.toMutableSet()
        .subtract(copiedIcons)

    if (diff.isNotEmpty()) {
      throw RuntimeException(
        """Hey Silly, you missed these icons "${
          diff.joinToString(", ")
        }"."""
      )
    }
  }

  private fun writeThemesAsJson(dokiThemes: List<DokiTheme>) {
    val directoryToPutStuffIn =
      ensureDirectoryExists(
        getGenerateResourcesDirectory()
      )

    val dokiThemesPath = get(directoryToPutStuffIn.toString(), "doki-theme-definitions.json")

    Files.newBufferedWriter(dokiThemesPath, StandardOpenOption.CREATE_NEW)
      .use { writer ->
        GsonBuilder().create().toJson(dokiThemes, writer)
      }
  }

  private fun constructDokiTheme(
    it: Triple<Path, MasterThemeDefinition, IconsAppDefinition>,
    constructableAssetSupplier: ConstructableAssetSupplier
  ): DokiTheme {
    val (
      _,
      masterThemeDefinition,
      appDefinition
    ) = it
    return DokiTheme(
      id = masterThemeDefinition.id,
      name = masterThemeDefinition.name,
      displayName = masterThemeDefinition.displayName,
      group = masterThemeDefinition.group,
      listName = "${getLafNamePrefix(masterThemeDefinition.group)}${masterThemeDefinition.name}",
      colors = resolveColors(
        masterThemeDefinition,
        appDefinition,
        constructableAssetSupplier
      ),
    )
  }


  private fun resolveColors(
    masterThemeDefinition: MasterThemeDefinition,
    iconsAppDefinition: IconsAppDefinition,
    constructableAssetSupplier: ConstructableAssetSupplier
  ): MutableMap<String, String> {
    val templateName = if (masterThemeDefinition.dark) "dark" else "light"
    return constructableAssetSupplier.getConstructableAsset(
      ConstructableTypes.Color
    ).map { colorAsset ->
      BuildFunctions.resolveTemplateWithCombini(
        AssetTemplateDefinition(
          colors = combineMaps(
            masterThemeDefinition.colors,
            iconsAppDefinition.colors,
          ),
          name = "app color template",
          extends = templateName,
        ),
        colorAsset.definitions,
        { it.colors!! },
        { it.extends },
        { parent, child ->
          combineMaps(parent, child)
        }
      )
    }.map {
      it.toMutableMap()
    }
      .orElseGet {
        masterThemeDefinition.colors.toMutableMap()
      }
  }

  private fun getGenerateResourcesDirectory(): Path = generatedResourcesDirectory.get().asFile.toPath()
}
