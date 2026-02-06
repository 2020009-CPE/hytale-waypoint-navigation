plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
    id("run-hytale")
}

group = project.property("maven_group") as String
version = project.property("version") as String

repositories {
    mavenCentral()
    maven("https://repo.hypixel.net/repository/Hytale/")
}

dependencies {
    // Hytale API - Using local stub classes until official API is released
    // The stub classes are located in src/main/java/com/hypixel/hytale/
    // When the official Hytale API is available, remove the stubs and uncomment:
    // compileOnly("com.hypixel.hytale:hytale-api:+")
    
    // JSON processing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Annotations (JSR-305 for @Nonnull, @Nullable, etc.)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(project.property("java_version") as String))
    }
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("WaypointNavigation")
        
        // Include Hytale API stubs in the JAR until the official API is released.
        // When the official Hytale API is available, the stubs can be removed and
        // the compileOnly dependency uncommented above.
        
        // Relocate dependencies to avoid conflicts
        relocate("com.google.gson", "com.waypointnav.libs.gson")
    }
    
    build {
        dependsOn(shadowJar)
    }
    
    processResources {
        inputs.property("version", project.version)
        
        filesMatching("manifest.json") {
            expand("version" to project.version)
        }
    }
}
