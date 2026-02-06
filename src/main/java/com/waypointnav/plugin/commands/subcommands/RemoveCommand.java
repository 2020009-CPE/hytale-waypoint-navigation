package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.command.AbstractPlayerCommand;
import com.hypixel.hytale.command.CommandContext;
import com.hypixel.hytale.command.argument.RequiredArg;
import com.hypixel.hytale.command.argument.type.ArgTypes;
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
import java.util.List;
import java.util.UUID;

/**
 * Command to remove a waypoint by index.
 * Usage: /waypoint remove <index>
 */
public class RemoveCommand extends AbstractPlayerCommand {
    private final RequiredArg<Integer> indexArg;
    
    public RemoveCommand() {
        super("remove", "Remove a waypoint by index");
        this.setPermissionGroup(GameMode.Adventure);
        
        this.indexArg = withRequiredArg("index", "Index of the waypoint to remove", ArgTypes.INTEGER);
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
        
        int index = indexArg.get(ctx) - 1; // Convert to 0-based index
        List<Waypoint> waypoints = playerData.getWaypoints();
        
        if (index < 0 || index >= waypoints.size()) {
            player.sendMessage(Message.raw(MessageUtils.error("Invalid waypoint index!")));
            return;
        }
        
        Waypoint waypoint = waypoints.get(index);
        playerData.removeWaypoint(waypoint);
        
        // Save asynchronously
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        player.sendMessage(Message.raw(MessageUtils.success("Removed waypoint: " + waypoint.getName())));
    }
}
