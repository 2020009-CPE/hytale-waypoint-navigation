package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles player join events to load waypoint data.
 */
public class PlayerJoinListener {
    private static final Logger LOGGER = Logger.getLogger(PlayerJoinListener.class.getName());
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
        LOGGER.info(String.format("Player %s joined. Loading waypoint data...", playerUuid));
        
        boolean alwaysEnabled = plugin.getConfigManager().getBoolean("navigation.alwaysEnabled", true);
        
        // Load player data asynchronously
        plugin.getStorage().loadPlayerDataAsync(playerUuid).thenAccept(data -> {
            if (data != null) {
                LOGGER.info(String.format("Loaded %d waypoint(s) for player %s.",
                    data.getWaypoints().size(), playerUuid));
                plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
                // Copy loaded data into player data manager
                PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
                if (playerData != null) {
                    // Merge loaded waypoints
                    data.getWaypoints().forEach(playerData::addWaypoint);
                    playerData.setActiveWaypointIndex(data.getActiveWaypointIndex());
                    playerData.setHudEnabled(data.isHudEnabled());
                    playerData.setWorldMarkersEnabled(data.isWorldMarkersEnabled());
                    
                    // Force navigation enabled if config says always-on
                    if (alwaysEnabled) {
                        playerData.setNavigationEnabled(true);
                    } else {
                        playerData.setNavigationEnabled(data.isNavigationEnabled());
                    }
                    
                    // Recalculate active waypoint based on priority
                    playerData.recalculateActiveWaypoint();
                }
            } else {
                LOGGER.info(String.format("No saved data found for player %s. Creating new profile.", playerUuid));
                // Create new player data (navigation is enabled by default)
                plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
            }
        });
    }
}
