package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;

import javax.annotation.Nonnull;

/**
 * Command to add a waypoint at the player's current location.
 * Usage: /waypoint add <name> [radius]
 */
public class AddCommand {
    private final WaypointNavigationPlugin plugin;
    
    public AddCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the add command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // if (args.length < 2) {
        //     player.sendMessage(MessageUtils.error("Usage: /waypoint add <name> [radius]"));
        //     return;
        // }
        
        // String name = args[1];
        // double radius = plugin.getConfigManager().getDouble("waypoint.defaultRadius", 5.0);
        
        // Parse custom radius if provided
        // if (args.length >= 3) {
        //     try {
        //         radius = Double.parseDouble(args[2]);
        //         if (radius <= 0) {
        //             player.sendMessage(MessageUtils.error("Radius must be positive!"));
        //             return;
        //         }
        //     } catch (NumberFormatException e) {
        //         player.sendMessage(MessageUtils.error("Invalid radius!"));
        //         return;
        //     }
        // }
        
        // Get player location
        // Location loc = player.getLocation();
        // UUID playerUuid = player.getUniqueId();
        
        // Check waypoint limit
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        // int maxWaypoints = plugin.getConfigManager().getInt("waypoint.maxWaypoints", 50);
        // if (playerData.getWaypoints().size() >= maxWaypoints) {
        //     player.sendMessage(MessageUtils.error("Maximum waypoint limit reached (" + maxWaypoints + ")!"));
        //     return;
        // }
        
        // Create waypoint
        // Waypoint waypoint = new Waypoint(name, loc.getX(), loc.getY(), loc.getZ(), WaypointType.USER_DEFINED);
        // waypoint.setCollectionRadius(radius);
        
        // Add to player data
        // playerData.addWaypoint(waypoint);
        
        // Save asynchronously
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // Confirm to player
        // player.sendMessage(MessageUtils.success("Added waypoint: " + name));
        // player.sendMessage(MessageUtils.info(
        //     "Location: " + MessageUtils.formatCoordinates(loc.getX(), loc.getY(), loc.getZ())
        // ));
        // player.sendMessage(MessageUtils.info("Radius: " + radius + " blocks"));
    }
}
