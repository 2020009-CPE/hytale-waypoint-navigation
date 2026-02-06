package com.waypointnav.plugin.commands.subcommands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to toggle HUD display on/off.
 * Usage: /waypoint toggle-hud
 */
public class ToggleHudCommand {
    private final WaypointNavigationPlugin plugin;
    
    public ToggleHudCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Executes the toggle-hud command.
     *
     * @param player The player executing the command
     * @param args Command arguments
     */
    public void execute(Object player, String[] args) {
        // TODO: Implement with Hytale API
        // UUID playerUuid = player.getUniqueId();
        // PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        
        // boolean newState = !playerData.isHudEnabled();
        // playerData.setHudEnabled(newState);
        
        // // Save
        // plugin.getStorage().savePlayerDataAsync(playerData);
        
        // String status = newState ? "§aenabled" : "§cdisabled";
        // player.sendMessage(MessageUtils.success("HUD display " + status + "!"));
    }
}
