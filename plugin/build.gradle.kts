import java.util.Collections

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Gradle plugin project to get you started.
 * For more details take a look at the Writing Custom Plugins chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/custom_plugins.html
 */

plugins {
  // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
  id("java-gradle-plugin")
  id("com.diffplug.spotless") version "6.12.0"
}

repositories {
  maven {
    name = "forge"
    url = uri("https://maven.minecraftforge.net")
    mavenContent {
      includeGroup("net.minecraftforge")
      includeGroup("net.minecraftforge.srg2source")
      includeGroup("de.oceanlabs.mcp")
      includeGroup("cpw.mods")
    }
  }
  maven {
    // because Srg2Source needs an eclipse dependency.
    name = "eclipse"
    url = uri("https://repo.eclipse.org/content/groups/eclipse/")
    mavenContent { includeGroup("org.eclipse.jdt") }
  }
  maven {
    name = "mojang"
    url = uri("https://libraries.minecraft.net/")
    mavenContent {
      includeGroup("com.ibm.icu")
      includeGroup("com.mojang")
      includeGroup("com.paulscode")
      includeGroup("org.lwjgl.lwjgl")
      includeGroup("tv.twitch")
      includeGroup("net.minecraft")
    }
  }
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  // Apache Commons utilities
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("commons-io:commons-io:2.11.0")
  implementation("commons-codec:commons-codec:1.15")
  implementation("org.apache.commons:commons-compress:1.22")
  // Guava utilities
  implementation("com.google.guava:guava:31.1-jre")
  // CSV reader, also used by SpecialSource
  implementation("com.opencsv:opencsv:5.7.1")
  // Diffing&Patching
  implementation("com.cloudbees:diff4j:1.1")
  implementation("com.github.abrarsyed.jastyle:jAstyle:1.2")
  implementation("com.github.jponge:lzma-java:1.3")
  implementation("com.nothome:javaxdelta:2.0.1")
  implementation("net.md-5:SpecialSource:1.7.4")
  // "MCP stuff"
  implementation("de.oceanlabs.mcp:mcinjector:3.2-SNAPSHOT")
  implementation("net.minecraftforge.srg2source:Srg2Source:3.2-SNAPSHOT")
  implementation("org.eclipse.jdt:org.eclipse.jdt.core") { version { strictly("3.10.0+") } }
  // Startup classes
  compileOnly("com.mojang:authlib:1.5.16")
  compileOnly("net.minecraft:launchwrapper:1.12")
  // Provides a file-downloading task implementation for Gradle
  implementation(
      group = "de.undercouch.download",
      name = "de.undercouch.download.gradle.plugin",
      version = "5.3.0")
  // JSON handling for Minecraft manifests etc.
  implementation("com.google.code.gson:gson:2.10")
  // Forge utilities (to be merged into the source tree in the future)

  // Use JUnit Jupiter for testing.
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
    vendor.set(JvmVendorSpec.ADOPTIUM)
  }
}

spotless {
  encoding("UTF-8")
  java {
    toggleOffOn()
    importOrder()
    removeUnusedImports()
    palantirJavaFormat("1.1.0")
  }
  kotlinGradle {
    toggleOffOn()
    ktfmt("0.39")
    trimTrailingWhitespace()
    indentWithSpaces(4)
    endWithNewline()
  }
}

gradlePlugin {
  // Define the plugin
  val greeting by
      plugins.creating {
        id = "com.gtnewhorizons.retrofuturagradle"
        implementationClass = "com.gtnewhorizons.retrofuturagradle.UserDevPlugin"
      }
}

java {
  sourceSets {
    // Add the GradleStart tree as sources for IDE support
    create("gradleStart") {
      compileClasspath = configurations.compileClasspath.get()
      java {
        setSrcDirs(Collections.emptyList<String>())
        source(sourceSets.main.get().resources)
      }
    }
  }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by
    tasks.registering(Test::class) {
      testClassesDirs = functionalTestSourceSet.output.classesDirs
      classpath = functionalTestSourceSet.runtimeClasspath
      useJUnitPlatform()
    }

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
  // Run the functional tests as part of `check`
  dependsOn(functionalTest)
}

tasks.named<Test>("test") {
  // Use JUnit Jupiter for unit tests.
  useJUnitPlatform()
}
