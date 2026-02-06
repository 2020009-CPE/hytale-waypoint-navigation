plugins {
    id("java-library")
    id("hytale-mod") version "0.+"
    id("com.gradleup.shadow") version "9.3.1"
}

group = project.property("maven_group") as String
version = project.property("version") as String

repositories {
    mavenCentral()
    maven("https://maven.hytale-mods.dev/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {
    // JSON processing (shaded into the JAR)
    implementation("com.google.code.gson:gson:2.10.1")

    // Annotations
    compileOnly("org.jetbrains:annotations:26.0.2-1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("WaypointNavigation")

        // Relocate dependencies to avoid conflicts
        relocate("com.google.gson", "com.waypointnav.libs.gson")
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        val replaceProperties = mapOf(
            "version" to project.version
        )

        filesMatching("manifest.json") {
            expand(replaceProperties)
        }

        inputs.properties(replaceProperties)
    }
}
