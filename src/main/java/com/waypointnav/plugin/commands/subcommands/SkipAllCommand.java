package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to skip all remaining waypoints.
 * Usage: /waypoint skipall
 */
public class SkipAllCommand {
    private final WaypointNavigationPlugin plugin;
    
    public SkipAllCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the skipall command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        // if (playerData == null || playerData.getWaypoints().isEmpty()) {
        //     player.sendMessage(MessageUtils.error("You have no waypoints!"));
        //     return;
        // }
        
        // long uncompleted = playerData.getWaypoints().stream()
        //     .filter(wp -> !wp.isCompleted())
        //     .count();
        
        // if (uncompleted == 0) {
        //     player.sendMessage(MessageUtils.info("All waypoints are already completed!"));
        //     return;
        // }
        
        // playerData.skipAllWaypoints();
        
        // // Save
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // player.sendMessage(MessageUtils.success("Skipped all remaining waypoints (" + uncompleted + ")!"));
        // player.sendMessage(MessageUtils.success("All waypoints completed!"));
    }
}
