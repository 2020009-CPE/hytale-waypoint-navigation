require(JavaVersion.current() >= JavaVersion.VERSION_25) {
    "This project requires Java 25 or higher to build. " +
    "Current JVM version is ${JavaVersion.current()}. " +
    "Please install Java 25 and set JAVA_HOME to the Java 25 installation directory."
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.hytale-mods.dev/releases") {
            name = "HytaleModdingReleases"
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "waypoint-navigation"
