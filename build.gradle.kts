plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
}

group = project.property("maven_group") as String
version = project.property("version") as String

repositories {
    mavenCentral()
}

dependencies {
    // Hytale API (provided by the server at runtime)
    compileOnly(project(":hytale-api-stubs"))

    // JSON processing (shaded into the JAR)
    implementation("com.google.code.gson:gson:2.10.1")

    // Annotations
    compileOnly("org.jetbrains:annotations:26.0.2-1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of((project.property("java_version") as String).toInt())
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
