package com.hypixel.hytale.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Stub class for Hytale API - JavaPlugin
 * This is a temporary stub until the official Hytale API is released.
 */
public abstract class JavaPlugin {
    private Path dataFolder;
    
    public JavaPlugin(JavaPluginInit init) {
        // Default to plugins directory - subclasses can customize via getDataFolder override
        this.dataFolder = Paths.get("plugins", this.getClass().getSimpleName());
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
