package com.waypointnav.plugin.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Handles asynchronous saving and loading of waypoint data to/from JSON files.
 */
public class WaypointStorage {
    private final Path dataFolder;
    private final Gson gson;
    
    public WaypointStorage(@Nonnull Path dataFolder) {
        this.dataFolder = dataFolder.resolve("playerdata");
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
    
    /**
     * Initializes the storage system by creating necessary directories.
     */
    public void initialize() {
        try {
            Files.createDirectories(dataFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves player waypoint data asynchronously.
     *
     * @param playerData The player data to save
     * @return CompletableFuture that completes when save is done
     */
    @Nonnull
    public CompletableFuture<Void> savePlayerDataAsync(@Nonnull PlayerWaypointData playerData) {
        return CompletableFuture.runAsync(() -> savePlayerData(playerData));
    }
    
    /**
     * Saves player waypoint data synchronously.
     *
     * @param playerData The player data to save
     */
    private void savePlayerData(@Nonnull PlayerWaypointData playerData) {
        Path playerFile = dataFolder.resolve(playerData.getPlayerUuid().toString() + ".json");
        
        try {
            // Convert to saveable format
            Map<String, Object> data = new HashMap<>();
            data.put("uuid", playerData.getPlayerUuid().toString());
            data.put("waypoints", playerData.getWaypoints());
            data.put("activeIndex", playerData.getActiveWaypointIndex());
            data.put("completed", playerData.getCompletedWaypoints());
            data.put("navigationEnabled", playerData.isNavigationEnabled());
            data.put("hudEnabled", playerData.isHudEnabled());
            data.put("worldMarkersEnabled", playerData.isWorldMarkersEnabled());
            
            try (Writer writer = Files.newBufferedWriter(playerFile)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads player waypoint data asynchronously.
     *
     * @param playerUuid The player's UUID
     * @return CompletableFuture containing the loaded player data, or null if not found
     */
    @Nonnull
    public CompletableFuture<PlayerWaypointData> loadPlayerDataAsync(@Nonnull UUID playerUuid) {
        return CompletableFuture.supplyAsync(() -> loadPlayerData(playerUuid));
    }
    
    /**
     * Loads player waypoint data synchronously.
     *
     * @param playerUuid The player's UUID
     * @return The loaded player data, or null if not found
     */
    @SuppressWarnings("unchecked")
    private PlayerWaypointData loadPlayerData(@Nonnull UUID playerUuid) {
        Path playerFile = dataFolder.resolve(playerUuid.toString() + ".json");
        
        if (!Files.exists(playerFile)) {
            return null;
        }
        
        try (Reader reader = Files.newBufferedReader(playerFile)) {
            Map<String, Object> data = gson.fromJson(reader, Map.class);
            
            if (data == null) {
                return null;
            }
            
            PlayerWaypointData playerData = new PlayerWaypointData(playerUuid);
            
            // Load waypoints
            Object waypointsObj = data.get("waypoints");
            if (waypointsObj instanceof List) {
                Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
                List<Map<String, Object>> waypointMaps = gson.fromJson(
                    gson.toJsonTree(waypointsObj), listType
                );
                
                for (Map<String, Object> wpMap : waypointMaps) {
                    Waypoint waypoint = deserializeWaypoint(wpMap);
                    if (waypoint != null) {
                        playerData.addWaypoint(waypoint);
                    }
                }
            }
            
            // Load active index
            Object activeIndexObj = data.get("activeIndex");
            if (activeIndexObj instanceof Number) {
                playerData.setActiveWaypointIndex(((Number) activeIndexObj).intValue());
            }
            
            // Load completed waypoints
            Object completedObj = data.get("completed");
            if (completedObj instanceof List) {
                List<String> completedIds = (List<String>) completedObj;
                for (String idStr : completedIds) {
                    try {
                        playerData.completeWaypoint(UUID.fromString(idStr));
                    } catch (IllegalArgumentException e) {
                        // Invalid UUID, skip
                    }
                }
            }
            
            // Load settings
            Object navEnabled = data.get("navigationEnabled");
            if (navEnabled instanceof Boolean) {
                playerData.setNavigationEnabled((Boolean) navEnabled);
            }
            
            Object hudEnabled = data.get("hudEnabled");
            if (hudEnabled instanceof Boolean) {
                playerData.setHudEnabled((Boolean) hudEnabled);
            }
            
            Object worldEnabled = data.get("worldMarkersEnabled");
            if (worldEnabled instanceof Boolean) {
                playerData.setWorldMarkersEnabled((Boolean) worldEnabled);
            }
            
            return playerData;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deserializes a waypoint from a map.
     */
    @SuppressWarnings("unchecked")
    private Waypoint deserializeWaypoint(@Nonnull Map<String, Object> map) {
        try {
            UUID id = UUID.fromString((String) map.get("id"));
            String name = (String) map.get("name");
            double x = ((Number) map.get("x")).doubleValue();
            double y = ((Number) map.get("y")).doubleValue();
            double z = ((Number) map.get("z")).doubleValue();
            double radius = ((Number) map.get("collectionRadius")).doubleValue();
            String typeStr = (String) map.get("type");
            int order = ((Number) map.get("order")).intValue();
            boolean completed = (Boolean) map.getOrDefault("completed", false);
            
            Waypoint waypoint = new Waypoint(
                id, name, x, y, z, radius,
                com.waypointnav.plugin.waypoint.WaypointType.valueOf(typeStr),
                order
            );
            waypoint.setCompleted(completed);
            
            Object worldObj = map.get("world");
            if (worldObj instanceof String) {
                waypoint.setWorld((String) worldObj);
            }
            
            return waypoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Deletes player data file.
     *
     * @param playerUuid The player's UUID
     * @return true if deleted successfully
     */
    public boolean deletePlayerData(@Nonnull UUID playerUuid) {
        Path playerFile = dataFolder.resolve(playerUuid.toString() + ".json");
        try {
            return Files.deleteIfExists(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
