plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("runHytale") {
            id = "run-hytale"
            implementationClass = "RunHytalePlugin"
        }
    }
}
