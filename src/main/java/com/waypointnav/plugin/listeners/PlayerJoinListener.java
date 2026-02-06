package com.waypointnav.plugin.listeners;

import com.hypixel.hytale.entity.PlayerRef;
import com.hypixel.hytale.event.EventHandler;
import com.hypixel.hytale.event.player.PlayerReadyEvent;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handles player join events to load waypoint data.
 */
public class PlayerJoinListener {
    private final WaypointNavigationPlugin plugin;
    
    public PlayerJoinListener(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Called when a player joins the server.
     *
     * @param event The player ready event
     */
    @EventHandler
    public void onPlayerReady(@Nonnull PlayerReadyEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
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
