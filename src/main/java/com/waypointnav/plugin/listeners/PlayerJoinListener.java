package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import java.util.UUID;

/**
 * Handles player join events to load waypoint data.
 */
public class PlayerJoinListener {
    private final WaypointNavigationPlugin plugin;
    
    public PlayerJoinListener(WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player is added to the world.
     *
     * @param playerRef The player reference
     */
    public void onPlayerJoin(PlayerRef playerRef) {
        UUID playerUuid = playerRef.getUuid();
        
        // Load player data asynchronously
        plugin.getStorage().loadPlayerDataAsync(playerUuid).thenAccept(data -> {
            if (data != null) {
                plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
                // Copy loaded data into player data manager
                PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
                if (playerData != null) {
                    // Merge loaded waypoints
                    data.getWaypoints().forEach(playerData::addWaypoint);
                    playerData.setActiveWaypointIndex(data.getActiveWaypointIndex());
                    playerData.setNavigationEnabled(data.isNavigationEnabled());
                    playerData.setHudEnabled(data.isHudEnabled());
                    playerData.setWorldMarkersEnabled(data.isWorldMarkersEnabled());
                }
            } else {
                // Create new player data
                plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
            }
        });
    }
}
