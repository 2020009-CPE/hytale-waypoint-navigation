package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.command.AbstractPlayerCommand;
import com.hypixel.hytale.command.CommandContext;
import com.hypixel.hytale.command.argument.OptionalArg;
import com.hypixel.hytale.command.argument.RequiredArg;
import com.hypixel.hytale.command.argument.type.ArgTypes;
import com.hypixel.hytale.entity.Player;
import com.hypixel.hytale.entity.PlayerRef;
import com.hypixel.hytale.entity.component.TransformComponent;
import com.hypixel.hytale.math.Vec3d;
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
import com.waypointnav.plugin.waypoint.WaypointType;

import javax.annotation.Nonnull;
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
    protected void execute(@Nonnull CommandContext ctx,
                         @Nonnull Store<EntityStore> store,
                         @Nonnull Ref<EntityStore> ref,
                         @Nonnull PlayerRef playerRef,
                         @Nonnull World world) {
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        Player player = store.getComponent(ref, Player.getComponentType());
        UUID playerUuid = player.getUuid();
        
        String name = nameArg.get(ctx);
        Float radiusFloat = radiusArg.get(ctx);
        double radius = radiusFloat != null ? radiusFloat : 
            plugin.getConfigManager().getDouble("waypoint.defaultRadius", 5.0);
        
        if (radius <= 0) {
            player.sendMessage(Message.raw(MessageUtils.error("Radius must be positive!")));
            return;
        }
        
        // Get player location
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        Vec3d position = transform.getPosition();
        
        // Check waypoint limit
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        int maxWaypoints = plugin.getConfigManager().getInt("waypoint.maxWaypoints", 50);
        if (playerData.getWaypoints().size() >= maxWaypoints) {
            player.sendMessage(Message.raw(
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
        player.sendMessage(Message.raw(MessageUtils.success("Added waypoint: " + name)));
        player.sendMessage(Message.raw(MessageUtils.info(
            "Location: " + MessageUtils.formatCoordinates(position.getX(), position.getY(), position.getZ())
        )));
        player.sendMessage(Message.raw(MessageUtils.info("Radius: " + radius + " blocks")));
    }
}
