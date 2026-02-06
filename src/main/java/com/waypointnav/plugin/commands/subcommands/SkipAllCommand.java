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
 * Command to skip all remaining waypoints.
 * Usage: /waypoint skipall
 */
public class SkipAllCommand extends AbstractPlayerCommand {
    
    public SkipAllCommand() {
        super("skipall", "Skip all remaining waypoints");
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
        
        long uncompleted = playerData.getWaypoints().stream()
            .filter(wp -> !wp.isCompleted())
            .count();
        
        if (uncompleted == 0) {
            player.sendMessage(Message.raw(MessageUtils.info("All waypoints are already completed!")));
            return;
        }
        
        playerData.skipAllWaypoints();
        
        // Save
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        player.sendMessage(Message.raw(
            MessageUtils.success("Skipped all remaining waypoints (" + uncompleted + ")!")
        ));
        player.sendMessage(Message.raw(MessageUtils.success("All waypoints completed!")));
    }
}
