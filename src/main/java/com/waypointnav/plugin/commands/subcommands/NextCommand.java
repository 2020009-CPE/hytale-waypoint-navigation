package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;

/**
 * Command to advance to the next waypoint without marking current as completed.
 * Usage: /waypoint next
 */
public class NextCommand {
    private final WaypointNavigationPlugin plugin;
    
    public NextCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the next command.
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
        
        // if (playerData.nextWaypoint()) {
        //     Waypoint nextWaypoint = playerData.getActiveWaypoint();
        //     if (nextWaypoint != null) {
        //         player.sendMessage(MessageUtils.success("Switched to next waypoint: " + nextWaypoint.getName()));
        //         
        //         Location wpLoc = new Location(player.getWorld(), 
        //             nextWaypoint.getX(), nextWaypoint.getY(), nextWaypoint.getZ());
        //         double distance = player.getLocation().distance(wpLoc);
        //         
        //         player.sendMessage(MessageUtils.info(
        //             "Distance: " + MathUtils.formatDistance(distance)
        //         ));
        //     }
        //     
        //     // Save
        //     plugin.getStorage().savePlayerDataAsync(playerData);
        // } else {
        //     player.sendMessage(MessageUtils.error("You are already at the last waypoint!"));
        // }
    }
}
