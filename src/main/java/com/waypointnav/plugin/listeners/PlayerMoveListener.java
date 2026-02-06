package com.waypointnav.plugin.listeners;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.waypoint.Waypoint;

import java.util.logging.Logger;

/**
 * Handles player movement to check waypoint completion.
 *
 * Note: Since Hytale may not have a direct PlayerMoveEvent, this functionality
 * should be implemented using a tick system or World.execute() to periodically
 * check player positions against active waypoints.
 */
public class PlayerMoveListener {
    private static final Logger LOGGER = Logger.getLogger(PlayerMoveListener.class.getName());
    private final WaypointNavigationPlugin plugin;
    
    public PlayerMoveListener(WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Checks if a player has reached their active waypoint.
     * This would be called from a tick handler or periodic task.
     *
     * @param playerUuid The player's UUID
     * @param x Player's X position
     * @param y Player's Y position
     * @param z Player's Z position
     */
    public void checkWaypointReached(java.util.UUID playerUuid, 
                                    double x, double y, double z) {
        // Get player data
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        if (playerData == null || !playerData.isNavigationEnabled()) {
            return;
        }
        
        // Get active waypoint
        Waypoint activeWaypoint = playerData.getActiveWaypoint();
        if (activeWaypoint == null || activeWaypoint.isCompleted()) {
            return;
        }
        
        // Check if player is within collection radius
        if (activeWaypoint.isWithinRadius(x, y, z)) {
            LOGGER.info(String.format("Player %s reached waypoint '%s' (within radius %.1f).",
                playerUuid, activeWaypoint.getName(), activeWaypoint.getCollectionRadius()));
            handleWaypointReached(playerData, activeWaypoint);
        }
    }
    
    /**
     * Handles waypoint completion.
     *
     * @param playerData The player's waypoint data
     * @param waypoint The completed waypoint
     */
    private void handleWaypointReached(PlayerWaypointData playerData,
                                       Waypoint waypoint) {
        // Mark as completed
        playerData.completeWaypoint(waypoint.getId());
        waypoint.setCompleted(true);
        LOGGER.info(String.format("Waypoint '%s' (id=%s) marked as completed.", waypoint.getName(), waypoint.getId()));
        
        // Auto-progress to next waypoint if enabled
        if (plugin.getConfigManager().getBoolean("waypoint.autoProgress", true)) {
            boolean advanced = playerData.nextWaypoint();
            LOGGER.info(String.format("Auto-progress enabled. Advanced to next waypoint: %s", advanced));
        }
        
        // Save player data
        plugin.getStorage().savePlayerDataAsync(playerData);
    }
}
