package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.entity.PlayerRef;
import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.player.PlayerDisconnectEvent;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handles player quit events to save waypoint data.
 */
public class PlayerQuitListener {
    private final WaypointNavigationPlugin plugin;
    
    public PlayerQuitListener(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player quits the server.
     *
     * @param event The player disconnect event
     */
    @EventHandler
    public void onPlayerDisconnect(@Nonnull PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        UUID playerUuid = playerRef.getUuid();
        
        // Get player data
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        if (playerData != null) {
            // Save player data asynchronously
            plugin.getStorage().savePlayerDataAsync(playerData);
        }
    }
}
