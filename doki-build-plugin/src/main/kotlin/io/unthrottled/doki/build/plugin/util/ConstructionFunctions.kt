package io.unthrottled.doki.build.plugin.util

import io.unthrottled.doki.build.jvm.models.HasId
import io.unthrottled.doki.build.jvm.models.MasterThemeDefinition
import io.unthrottled.doki.build.jvm.tools.CommonConstructionFunctions.gson
import io.unthrottled.doki.build.jvm.tools.DokiProduct
import io.unthrottled.doki.build.plugin.IconType
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

object ConstructionFunctions {
  fun <T : HasId> getAllDokiThemeDefinitions(
    dokiProduct: DokiProduct,
    iconType: IconType,
    productBuildSourceDirectory: Path,
    masterThemeDirectory: Path,
    clazz: Class<T>
  ): Stream<Triple<Path, MasterThemeDefinition, T>> {
    val allProductDefinitions =
      getAllProductDefinitions(iconType, productBuildSourceDirectory, dokiProduct, clazz)
    val masterThemeDefinitionPath = Paths.get(masterThemeDirectory.toString(), "definitions")
    return Files.walk(masterThemeDefinitionPath)
      .filter { !Files.isDirectory(it) }
      .filter { isDefinitionType(it.fileName.toString(), iconType, "master") }
      .map { it to Files.newInputStream(it) }
      .map {
        val masterThemePath = it.first.toString()
        val masterFileDefinition = masterThemePath.substringAfter("$masterThemeDefinitionPath")
        val productDefinitionDefinitionPath =
          Paths.get(productBuildSourceDirectory.toString(), masterFileDefinition)
        val masterThemeDefinition = gson.fromJson(
          InputStreamReader(it.second, StandardCharsets.UTF_8),
          MasterThemeDefinition::class.java
        )
        val productDefinition =
          allProductDefinitions[masterThemeDefinition.id] ?: throw IllegalArgumentException(
            """
            Master Theme ${masterThemeDefinition.displayName} is missing the ${dokiProduct.prettyName} definition file!
            """.trimIndent()
          )
        Triple(productDefinitionDefinitionPath, masterThemeDefinition, productDefinition)
      }
  }

  private fun <T : HasId> getAllProductDefinitions(
    iconType: IconType,
    productBuildSourceDirectory: Path,
    dokiProduct: DokiProduct,
    clazz: Class<T>
  ): Map<String, T> {
    return Files.walk(productBuildSourceDirectory)
      .filter { !Files.isDirectory(it) }
      .filter { isDefinitionType(it.fileName.toString(), iconType, dokiProduct.value) }
      .map { Files.newInputStream(it) }
      .map {
        gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          clazz
        )
      }.collect(
        Collectors.toMap(
          { it.id },
          { it }
        )
      )
  }

  private fun isDefinitionType(
    fileName: String,
    iconType: IconType,
    definitionType: String,
  ): Boolean {
    val definitionSuffix = "definition.json"
    return when (iconType) {
      IconType.CUSTOM -> fileName.endsWith("${iconType.lowercase}.$definitionType.$definitionSuffix")
      else -> !IconType.entries.stream()
        .anyMatch { fileName.endsWith(".${it.lowercase}.$definitionType.$definitionSuffix") }
    }
  }
}