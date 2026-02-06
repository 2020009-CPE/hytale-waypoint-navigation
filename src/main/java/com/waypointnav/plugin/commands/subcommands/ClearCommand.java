package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to clear all waypoints for a player.
 * Usage: /waypoint clear
 */
public class ClearCommand {
    private final WaypointNavigationPlugin plugin;
    
    public ClearCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the clear command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        // if (playerData == null || playerData.getWaypoints().isEmpty()) {
        //     player.sendMessage(MessageUtils.error("You have no waypoints to clear!"));
        //     return;
        // }
        
        // int count = playerData.getWaypoints().size();
        // playerData.clearWaypoints();
        
        // // Save asynchronously
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // player.sendMessage(MessageUtils.success("Cleared " + count + " waypoint(s)!"));
    }
}
