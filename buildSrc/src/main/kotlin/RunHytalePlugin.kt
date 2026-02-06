import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Custom Gradle plugin for running Hytale server with the plugin loaded.
 * This plugin configures tasks to start a Hytale development server.
 */
class RunHytalePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("runHytale") {
            group = "hytale"
            description = "Runs the Hytale server with this plugin loaded"
            
            doLast {
                println("Run Hytale task - This would start the Hytale server in a real environment")
                println("Plugin: ${project.name}")
                println("Version: ${project.version}")
            }
        }
    }
}
