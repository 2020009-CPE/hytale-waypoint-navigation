package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.permission.GameMode;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import java.util.UUID;

/**
 * Command to clear all waypoints for a player.
 * Usage: /waypoint clear
 */
public class ClearCommand extends AbstractPlayerCommand {
    
    public ClearCommand() {
        super("clear", "Clear all your waypoints");
        this.setPermissionGroup(GameMode.Adventure);
    }
    
    @Override
    protected void execute(CommandContext ctx,
                         Store<EntityStore> store,
                         Ref<EntityStore> ref,
                         PlayerRef playerRef,
                         World world) {
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        if (playerData == null || playerData.getWaypoints().isEmpty()) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("You have no waypoints to clear!")));
            return;
        }
        
        int count = playerData.getWaypoints().size();
        playerData.clearWaypoints();
        
        // Save asynchronously
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        playerRef.sendMessage(Message.raw(MessageUtils.success("Cleared " + count + " waypoint(s)!")));
    }
}
