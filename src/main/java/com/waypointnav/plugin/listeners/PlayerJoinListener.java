package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles player join events to load waypoint data and sync global waypoints.
 */
public class PlayerJoinListener {
    private static final Logger LOGGER = Logger.getLogger(PlayerJoinListener.class.getName());
    private final WaypointNavigationPlugin plugin;
    
    public PlayerJoinListener(WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player is added to the world.
     * Loads saved player data and syncs global waypoints.
     *
     * @param playerRef The player reference
     */
    public void onPlayerJoin(PlayerRef playerRef) {
        UUID playerUuid = playerRef.getUuid();
        LOGGER.info(String.format("Player %s joined. Loading waypoint data...", playerUuid));
        
        boolean alwaysEnabled = plugin.getConfigManager().getBoolean("navigation.alwaysEnabled", true);
        boolean isGlobalScope = !plugin.getConfigManager().getBoolean("playerScope", false);
        
        // Load player data asynchronously
        plugin.getStorage().loadPlayerDataAsync(playerUuid).thenAccept(data -> {
            PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);

            if (data != null) {
                LOGGER.info(String.format("Loaded %d waypoint(s) for player %s.",
                    data.getWaypoints().size(), playerUuid));
                // Merge loaded waypoints
                data.getWaypoints().forEach(playerData::addWaypoint);
                playerData.setActiveWaypointIndex(data.getActiveWaypointIndex());
                playerData.setHudEnabled(data.isHudEnabled());
                playerData.setWorldMarkersEnabled(data.isWorldMarkersEnabled());
                
                if (alwaysEnabled) {
                    playerData.setNavigationEnabled(true);
                } else {
                    playerData.setNavigationEnabled(data.isNavigationEnabled());
                }
            } else {
                LOGGER.info(String.format("No saved data found for player %s. Creating new profile.", playerUuid));
            }
            
            // If global scope and player has no waypoints, sync global waypoints
            if (isGlobalScope && playerData.getWaypoints().isEmpty()) {
                syncGlobalWaypoints(playerData);
            }
            
            // Recalculate active waypoint based on priority
            playerData.recalculateActiveWaypoint();
        });
    }
    
    /**
     * Copies global waypoints to a player's waypoint list.
     * Creates fresh copies so each player has independent completion tracking.
     *
     * @param playerData The player's waypoint data
     */
    private void syncGlobalWaypoints(PlayerWaypointData playerData) {
        List<Waypoint> globalWaypoints = plugin.getWaypointManager().getGlobalWaypoints();
        
        if (globalWaypoints.isEmpty()) {
            LOGGER.info(String.format("No global waypoints to sync for player %s.", playerData.getPlayerUuid()));
            return;
        }
        
        for (Waypoint globalWp : globalWaypoints) {
            // Create a fresh copy for this player (independent completion tracking)
            Waypoint copy = new Waypoint(globalWp.getName(),
                globalWp.getX(), globalWp.getY(), globalWp.getZ(),
                globalWp.getType());
            copy.setCollectionRadius(globalWp.getCollectionRadius());
            copy.setPriority(globalWp.getPriority());
            copy.setWorld(globalWp.getWorld());
            playerData.addWaypoint(copy);
        }
        
        LOGGER.info(String.format("Synced %d global waypoint(s) to player %s.",
            globalWaypoints.size(), playerData.getPlayerUuid()));
    }
}
