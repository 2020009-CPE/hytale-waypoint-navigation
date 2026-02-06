package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Command to remove a waypoint by index.
 * Usage: /waypoint remove <index>
 */
public class RemoveCommand {
    private final WaypointNavigationPlugin plugin;
    
    public RemoveCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the remove command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // if (args.length < 2) {
        //     player.sendMessage(MessageUtils.error("Usage: /waypoint remove <index>"));
        //     return;
        // }
        
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        // if (playerData == null || playerData.getWaypoints().isEmpty()) {
        //     player.sendMessage(MessageUtils.error("You have no waypoints!"));
        //     return;
        // }
        
        // try {
        //     int index = Integer.parseInt(args[1]) - 1; // Convert to 0-based index
        //     List<Waypoint> waypoints = playerData.getWaypoints();
        //     
        //     if (index < 0 || index >= waypoints.size()) {
        //         player.sendMessage(MessageUtils.error("Invalid waypoint index!"));
        //         return;
        //     }
        //     
        //     Waypoint waypoint = waypoints.get(index);
        //     playerData.removeWaypoint(waypoint);
        //     
        //     // Save asynchronously
        //     plugin.getStorage().savePlayerDataAsync(playerData);
        //     
        //     player.sendMessage(MessageUtils.success("Removed waypoint: " + waypoint.getName()));
        //     
        // } catch (NumberFormatException e) {
        //     player.sendMessage(MessageUtils.error("Invalid index!"));
        // }
    }
}
