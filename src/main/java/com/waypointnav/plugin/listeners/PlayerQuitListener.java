package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Handles player quit events to save waypoint data.
 */
public class PlayerQuitListener {
    private static final Logger LOGGER = Logger.getLogger(PlayerQuitListener.class.getName());
    private final WaypointNavigationPlugin plugin;
    
    public PlayerQuitListener(WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player is removed from the world.
     *
     * @param playerRef The player reference
     */
    public void onPlayerQuit(PlayerRef playerRef) {
        UUID playerUuid = playerRef.getUuid();
        LOGGER.info(String.format("Player %s quit. Saving waypoint data...", playerUuid));
        
        // Get player data
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        if (playerData != null) {
            LOGGER.info(String.format("Saving %d waypoint(s) for player %s.",
                playerData.getWaypoints().size(), playerUuid));
            // Save player data asynchronously
            plugin.getStorage().savePlayerDataAsync(playerData);
        } else {
            LOGGER.info(String.format("No waypoint data found for player %s, nothing to save.", playerUuid));
        }
    }
}
