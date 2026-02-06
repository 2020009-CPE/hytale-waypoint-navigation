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
import java.util.UUID;

/**
 * Command to skip all remaining waypoints.
 * Usage: /waypoint skipall
 */
public class SkipAllCommand extends AbstractPlayerCommand {
    
    public SkipAllCommand() {
        super("skipall", "Skip all remaining waypoints");
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
        
        long uncompleted = playerData.getWaypoints().stream()
            .filter(wp -> !wp.isCompleted())
            .count();
        
        if (uncompleted == 0) {
            playerRef.sendMessage(Message.raw(MessageUtils.info("All waypoints are already completed!")));
            return;
        }
        
        playerData.skipAllWaypoints();
        
        // Save
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        playerRef.sendMessage(Message.raw(
            MessageUtils.success("Skipped all remaining waypoints (" + uncompleted + ")!")
        ));
        playerRef.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
    }
}
