package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import java.util.UUID;

/**
 * Command to skip the current waypoint and advance to the next.
 * Usage: /waypoint skip
 */
public class SkipCommand extends AbstractPlayerCommand {
    
    public SkipCommand() {
        super("skip", "Skip the current waypoint");
    }
    
    @Override
    protected void execute(CommandContext ctx,
                         Store<EntityStore> store,
                         Ref<EntityStore> ref,
                         PlayerRef playerRef,
                         World world) {
        
        // Permission check
        if (!playerRef.hasPermission("waypoint.admin")) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("You need the 'waypoint.admin' permission!")));
            return;
        }
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        if (playerData == null || playerData.getWaypoints().isEmpty()) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("You have no waypoints!")));
            return;
        }
        
        Waypoint current = playerData.getActiveWaypoint();
        if (current == null) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("No active waypoint!")));
            return;
        }
        
        String currentName = current.getName();
        
        if (playerData.skipCurrentWaypoint()) {
            playerRef.sendMessage(Message.raw(MessageUtils.success("Skipped waypoint: " + currentName)));
            
            Waypoint next = playerData.getActiveWaypoint();
            if (next != null) {
                playerRef.sendMessage(Message.raw(MessageUtils.info("Next waypoint: " + next.getName())));
            } else {
                playerRef.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
            }
            
            // Save
            plugin.getStorage().savePlayerDataAsync(playerData);
        } else {
            // Already at last waypoint
            playerData.completeWaypoint(current.getId());
            playerRef.sendMessage(Message.raw(MessageUtils.success("Skipped last waypoint: " + currentName)));
            playerRef.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
            
            plugin.getStorage().savePlayerDataAsync(playerData);
        }
    }
}
