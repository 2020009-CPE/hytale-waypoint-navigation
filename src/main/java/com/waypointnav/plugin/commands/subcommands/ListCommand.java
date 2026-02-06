package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Command to list all waypoints for a player.
 * Usage: /waypoint list
 */
public class ListCommand {
    private final WaypointNavigationPlugin plugin;
    
    public ListCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the list command.
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
        
        // List<Waypoint> waypoints = playerData.getWaypoints();
        // int activeIndex = playerData.getActiveWaypointIndex();
        // Location playerLoc = player.getLocation();
        
        // player.sendMessage(MessageUtils.header("Your Waypoints"));
        
        // for (int i = 0; i < waypoints.size(); i++) {
        //     Waypoint wp = waypoints.get(i);
        //     double distance = wp.distanceFrom(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ());
        //     
        //     String marker = (i == activeIndex) ? "§a→ " : "  ";
        //     String message = MessageUtils.formatWaypointList(
        //         i, wp.getName(), 
        //         wp.getX(), wp.getY(), wp.getZ(),
        //         distance, wp.isCompleted()
        //     );
        //     
        //     player.sendMessage(marker + message);
        // }
        
        // int completed = (int) waypoints.stream().filter(Waypoint::isCompleted).count();
        // player.sendMessage(MessageUtils.info(
        //     "Progress: " + completed + "/" + waypoints.size() + " completed"
        // ));
    }
}
