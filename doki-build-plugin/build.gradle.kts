group = "io.unthrottled.doki.build.plugin"
plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  implementation("io.unthrottled.doki.build.jvm:doki-build-source-jvm:88.0.7")
  implementation("org.jsoup:jsoup:1.21.2")
}

gradlePlugin {
  plugins {
    create("dokiBuildPlugin") {
      id = "io.unthrottled.doki.build.plugin.dokibuildplugin"
      implementationClass = "io.unthrottled.doki.build.plugin.DokiBuildPlugin"
    }
  }
}