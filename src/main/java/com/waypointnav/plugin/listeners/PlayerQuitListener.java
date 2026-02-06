package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import java.util.UUID;

/**
 * Handles player quit events to save waypoint data.
 */
public class PlayerQuitListener {
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
        
        // Get player data
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        if (playerData != null) {
            // Save player data asynchronously
            plugin.getStorage().savePlayerDataAsync(playerData);
        }
    }
}
