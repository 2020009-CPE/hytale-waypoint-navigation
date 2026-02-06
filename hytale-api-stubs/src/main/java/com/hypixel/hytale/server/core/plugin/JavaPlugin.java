package com.hypixel.hytale.server.core.plugin;

import com.hypixel.hytale.server.core.command.CommandRegistry;
import com.hypixel.hytale.server.core.event.EventRegistry;
import java.nio.file.Path;

public abstract class JavaPlugin {

    public JavaPlugin(JavaPluginInit init) {
    }

    protected void setup() {
    }

    public void onDisable() {
    }

    public Path getDataDirectory() {
        return null;
    }

    public CommandRegistry getCommandRegistry() {
        return null;
    }

    public EventRegistry getEventRegistry() {
        return null;
    }
}
