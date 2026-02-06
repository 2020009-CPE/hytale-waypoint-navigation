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

import javax.annotation.Nonnull;
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
            player.sendMessage(Message.raw(MessageUtils.error("You have no waypoints to clear!")));
            return;
        }
        
        int count = playerData.getWaypoints().size();
        playerData.clearWaypoints();
        
        // Save asynchronously
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        player.sendMessage(Message.raw(MessageUtils.success("Cleared " + count + " waypoint(s)!")));
    }
}
