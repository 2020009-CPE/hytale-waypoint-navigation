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
    // Hytale API (version will be provided by the run-hytale plugin)
    compileOnly("com.hypixel.hytale:hytale-api:+")
    
    // JSON processing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Annotations
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
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
