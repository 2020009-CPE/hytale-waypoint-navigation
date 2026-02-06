package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.argument.OptionalArg;
import com.hypixel.hytale.server.core.command.system.argument.RequiredArg;
import com.hypixel.hytale.server.core.command.system.argument.type.ArgTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.math.Vec3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.permission.GameMode;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;
import java.util.UUID;

/**
 * Command to add a waypoint at the player's current location.
 * Usage: /waypoint add <name> [radius]
 */
public class AddCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> nameArg;
    private final OptionalArg<Float> radiusArg;
    
    public AddCommand() {
        super("add", "Add a waypoint at your current location");
        this.setPermissionGroup(GameMode.Adventure);
        
        this.nameArg = withRequiredArg("name", "Name of the waypoint", ArgTypes.STRING);
        this.radiusArg = withOptionalArg("radius", "Collection radius in blocks", ArgTypes.FLOAT);
    }
    
    @Override
    protected void execute(CommandContext ctx,
                         Store<EntityStore> store,
                         Ref<EntityStore> ref,
                         PlayerRef playerRef,
                         World world) {
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        
        String name = nameArg.get(ctx);
        Float radiusFloat = radiusArg.get(ctx);
        double radius = radiusFloat != null ? radiusFloat : 
            plugin.getConfigManager().getDouble("waypoint.defaultRadius", 5.0);
        
        if (radius <= 0) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("Radius must be positive!")));
            return;
        }
        
        // Get player location from store
        Vec3d position = store.getPosition(ref);
        
        // Check waypoint limit
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        int maxWaypoints = plugin.getConfigManager().getInt("waypoint.maxWaypoints", 50);
        if (playerData.getWaypoints().size() >= maxWaypoints) {
            playerRef.sendMessage(Message.raw(
                MessageUtils.error("Maximum waypoint limit reached (" + maxWaypoints + ")!")
            ));
            return;
        }
        
        // Create waypoint
        Waypoint waypoint = new Waypoint(name, position.getX(), position.getY(), position.getZ(), 
                                        WaypointType.USER_DEFINED);
        waypoint.setCollectionRadius(radius);
        
        // Add to player data
        playerData.addWaypoint(waypoint);
        
        // Save asynchronously
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        // Confirm to player
        playerRef.sendMessage(Message.raw(MessageUtils.success("Added waypoint: " + name)));
        playerRef.sendMessage(Message.raw(MessageUtils.info(
            "Location: " + MessageUtils.formatCoordinates(position.getX(), position.getY(), position.getZ())
        )));
        playerRef.sendMessage(Message.raw(MessageUtils.info("Radius: " + radius + " blocks")));
    }
}
