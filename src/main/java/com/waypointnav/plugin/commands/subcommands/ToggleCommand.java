package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to toggle navigation on/off.
 * Usage: /waypoint toggle
 */
public class ToggleCommand {
    private final WaypointNavigationPlugin plugin;
    
    public ToggleCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the toggle command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        
        // boolean newState = !playerData.isNavigationEnabled();
        // playerData.setNavigationEnabled(newState);
        
        // // Save
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // String status = newState ? "§aenabled" : "§cdisabled";
        // player.sendMessage(MessageUtils.success("Waypoint navigation " + status + "!"));
    }
}
