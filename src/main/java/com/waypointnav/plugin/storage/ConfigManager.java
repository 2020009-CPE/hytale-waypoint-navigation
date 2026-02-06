package com.waypointnav.plugin.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages plugin configuration loading and saving.
 */
public class ConfigManager {
    private final Path configPath;
    private final Gson gson;
    private Map<String, Object> config;
    
    public ConfigManager(Path dataFolder) {
        this.configPath = dataFolder.resolve("config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.config = new HashMap<>();
    }
    
    /**
     * Loads configuration from file or creates default if not exists.
     */
    public void load() {
        try {
            if (Files.exists(configPath)) {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    Map<String, Object> loaded = gson.fromJson(reader, Map.class);
                    if (loaded != null) {
                        config = loaded;
                    }
                }
            } else {
                createDefaultConfig();
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
            createDefaultConfig();
        }
    }
    
    /**
     * Saves configuration to file.
     */
    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                gson.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates default configuration values.
     */
    private void createDefaultConfig() {
        config = new HashMap<>();
        
        // HUD settings
        Map<String, Object> hudSettings = new HashMap<>();
        hudSettings.put("enabled", true);
        hudSettings.put("showDistance", true);
        hudSettings.put("showCoordinates", true);
        hudSettings.put("arrowColor", "YELLOW");
        config.put("hud", hudSettings);
        
        // World marker settings
        Map<String, Object> worldSettings = new HashMap<>();
        worldSettings.put("enabled", true);
        worldSettings.put("particleType", "FLAME");
        worldSettings.put("particleCount", 20);
        worldSettings.put("updateInterval", 2); // ticks
        config.put("world", worldSettings);
        
        // Waypoint settings
        Map<String, Object> waypointSettings = new HashMap<>();
        waypointSettings.put("defaultRadius", 5.0);
        waypointSettings.put("autoProgress", true);
        waypointSettings.put("playSound", true);
        waypointSettings.put("maxWaypoints", 50);
        config.put("waypoint", waypointSettings);
        
        // Player scope settings
        config.put("playerScope", true); // true = per-player, false = global
        
        // Navigation settings
        Map<String, Object> navSettings = new HashMap<>();
        navSettings.put("alwaysEnabled", true);
        config.put("navigation", navSettings);
        
        // Auto-save settings
        Map<String, Object> saveSettings = new HashMap<>();
        saveSettings.put("enabled", true);
        saveSettings.put("interval", 300); // seconds
        config.put("autoSave", saveSettings);
    }
    
    /**
     * Gets a configuration value.
     *
     * @param path The config path (e.g., "hud.enabled")
     * @param defaultValue The default value if not found
     * @return The configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String path, T defaultValue) {
        String[] parts = path.split("\\.");
        Object current = config;
        
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
                if (current == null) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
        
        try {
            return (T) current;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
    
    /**
     * Sets a configuration value.
     *
     * @param path The config path (e.g., "hud.enabled")
     * @param value The value to set
     */
    @SuppressWarnings("unchecked")
    public void set(String path, Object value) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = config;
        
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            Object next = current.get(part);
            
            if (!(next instanceof Map)) {
                next = new HashMap<String, Object>();
                current.put(part, next);
            }
            
            current = (Map<String, Object>) next;
        }
        
        current.put(parts[parts.length - 1], value);
    }
    
    /**
     * Gets a boolean configuration value.
     *
     * @param path The config path
     * @param defaultValue The default value
     * @return The boolean value
     */
    public boolean getBoolean(String path, boolean defaultValue) {
        return get(path, defaultValue);
    }
    
    /**
     * Gets an integer configuration value.
     *
     * @param path The config path
     * @param defaultValue The default value
     * @return The integer value
     */
    public int getInt(String path, int defaultValue) {
        Object value = get(path, defaultValue);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    /**
     * Gets a double configuration value.
     *
     * @param path The config path
     * @param defaultValue The default value
     * @return The double value
     */
    public double getDouble(String path, double defaultValue) {
        Object value = get(path, defaultValue);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    /**
     * Gets a string configuration value.
     *
     * @param path The config path
     * @param defaultValue The default value
     * @return The string value
     */

    public String getString(String path, String defaultValue) {
        return get(path, defaultValue);
    }
}
