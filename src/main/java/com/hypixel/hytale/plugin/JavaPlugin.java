package com.hypixel.hytale.plugin;

import java.nio.file.Path;

/**
 * Stub class for Hytale API - JavaPlugin
 * This is a temporary stub until the official Hytale API is released.
 */
public abstract class JavaPlugin {
    private Path dataFolder;
    
    public JavaPlugin(JavaPluginInit init) {
        this.dataFolder = Path.of("plugins/WaypointNavigation");
    }
    
    public abstract void setup();
    
    public Path getDataFolder() {
        return dataFolder;
    }
    
    protected CommandRegistry getCommandRegistry() {
        return new CommandRegistry();
    }
    
    public static class CommandRegistry {
        public void registerCommand(Object command) {
            // Stub method
        }
    }
}
