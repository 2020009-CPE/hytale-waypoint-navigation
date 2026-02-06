package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.command.AbstractPlayerCommand;
import com.hypixel.hytale.command.CommandContext;
import com.hypixel.hytale.entity.Player;
import com.hypixel.hytale.entity.PlayerRef;
import com.hypixel.hytale.message.Message;
import com.hypixel.hytale.permission.GameMode;
import com.hypixel.hytale.store.EntityStore;
import com.hypixel.hytale.store.Ref;
import com.hypixel.hytale.store.Store;
import com.hypixel.hytale.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Command to skip the current waypoint and advance to the next.
 * Usage: /waypoint skip
 */
public class SkipCommand extends AbstractPlayerCommand {
    
    public SkipCommand() {
        super("skip", "Skip the current waypoint");
        this.setPermissionGroup(GameMode.Adventure);
    }
    
    @Override
    protected void execute(@Nonnull CommandContext ctx,
                         @Nonnull Store<EntityStore> store,
                         @Nonnull Ref<EntityStore> ref,
                         @Nonnull PlayerRef playerRef,
                         @Nonnull World world) {
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        Player player = store.getComponent(ref, Player.getComponentType());
        UUID playerUuid = player.getUuid();
        
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);
        
        if (playerData == null || playerData.getWaypoints().isEmpty()) {
            player.sendMessage(Message.raw(MessageUtils.error("You have no waypoints!")));
            return;
        }
        
        Waypoint current = playerData.getActiveWaypoint();
        if (current == null) {
            player.sendMessage(Message.raw(MessageUtils.error("No active waypoint!")));
            return;
        }
        
        String currentName = current.getName();
        
        if (playerData.skipCurrentWaypoint()) {
            player.sendMessage(Message.raw(MessageUtils.success("Skipped waypoint: " + currentName)));
            
            Waypoint next = playerData.getActiveWaypoint();
            if (next != null) {
                player.sendMessage(Message.raw(MessageUtils.info("Next waypoint: " + next.getName())));
            } else {
                player.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
            }
            
            // Save
            plugin.getStorage().savePlayerDataAsync(playerData);
        } else {
            // Already at last waypoint
            playerData.completeWaypoint(current.getId());
            player.sendMessage(Message.raw(MessageUtils.success("Skipped last waypoint: " + currentName)));
            player.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
            
            plugin.getStorage().savePlayerDataAsync(playerData);
        }
    }
}
