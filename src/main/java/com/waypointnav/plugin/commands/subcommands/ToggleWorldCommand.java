package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to toggle world markers on/off.
 * Usage: /waypoint toggle-world
 */
public class ToggleWorldCommand {
    private final WaypointNavigationPlugin plugin;
    
    public ToggleWorldCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the toggle-world command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        
        // boolean newState = !playerData.isWorldMarkersEnabled();
        // playerData.setWorldMarkersEnabled(newState);
        
        // // Save
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // String status = newState ? "§aenabled" : "§cdisabled";
        // player.sendMessage(MessageUtils.success("World markers " + status + "!"));
    }
}
