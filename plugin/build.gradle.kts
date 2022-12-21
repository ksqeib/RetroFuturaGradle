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
  mavenCentral()
  gradlePluginPortal()
}

dependencies {
  // Apache Commons utilities
  implementation("org.apache.commons:commons-lang3:3.12.0")
  implementation("commons-io:commons-io:2.11.0")
  implementation("commons-codec:commons-codec:1.15")
  // Guava utilities
  implementation("com.google.guava:guava:31.1-jre")
  // Provides a file-downloading task implementation for Gradle
  implementation(
      group = "de.undercouch.download",
      name = "de.undercouch.download.gradle.plugin",
      version = "5.3.0")
  // JSON handling for Minecraft manifests etc.
  implementation("com.google.code.gson:gson:2.10")
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
        implementationClass = "com.gtnewhorizons.retrofuturagradle.RetroFuturaGradlePlugin"
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
