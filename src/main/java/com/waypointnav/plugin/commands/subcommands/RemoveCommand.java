package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
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
        
        this.indexArg = withRequiredArg("index", "Index of the waypoint to remove", ArgTypes.INTEGER);
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
            playerRef.sendMessage(Message.raw(MessageUtils.error("You have no waypoints!")));
            return;
        }
        
        int index = indexArg.get(ctx) - 1; // Convert to 0-based index
        List<Waypoint> waypoints = playerData.getWaypoints();
        
        if (index < 0 || index >= waypoints.size()) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("Invalid waypoint index!")));
            return;
        }
        
        Waypoint waypoint = waypoints.get(index);
        playerData.removeWaypoint(waypoint);
        
        // Save asynchronously
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        playerRef.sendMessage(Message.raw(MessageUtils.success("Removed waypoint: " + waypoint.getName())));
    }
}
