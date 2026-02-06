package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;

/**
 * Command to skip the current waypoint and advance to the next.
 * Usage: /waypoint skip
 */
public class SkipCommand {
    private final WaypointNavigationPlugin plugin;
    
    public SkipCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the skip command.
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
        
        // Waypoint current = playerData.getActiveWaypoint();
        // if (current == null) {
        //     player.sendMessage(MessageUtils.error("No active waypoint!"));
        //     return;
        // }
        
        // String currentName = current.getName();
        
        // if (playerData.skipCurrentWaypoint()) {
        //     player.sendMessage(MessageUtils.success("Skipped waypoint: " + currentName));
        //     
        //     Waypoint next = playerData.getActiveWaypoint();
        //     if (next != null) {
        //         player.sendMessage(MessageUtils.info("Next waypoint: " + next.getName()));
        //     } else {
        //         player.sendMessage(MessageUtils.success("All waypoints completed!"));
        //     }
        //     
        //     // Save
        //     plugin.getStorage().savePlayerDataAsync(playerData);
        // } else {
        //     // Already at last waypoint
        //     playerData.completeWaypoint(current.getId());
        //     player.sendMessage(MessageUtils.success("Skipped last waypoint: " + currentName));
        //     player.sendMessage(MessageUtils.success("All waypoints completed!"));
        //     
        //     plugin.getStorage().savePlayerDataAsync(playerData);
        // }
    }
}
