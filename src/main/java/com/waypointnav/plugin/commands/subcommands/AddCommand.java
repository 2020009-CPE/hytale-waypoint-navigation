package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
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
 * Command to add a waypoint.
 *
 * Usage:
 *   /waypoint add here <name> [priority] [radius]    — saves player's current position
 *   /waypoint add <name> <x> <y> <z> [priority] [radius] — saves explicit coordinates
 *
 * Priority: lower number = higher priority (default 0).
 * All commands require the "waypoint.admin" permission.
 */
public class AddCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> nameArg;
    private final OptionalArg<String> extraArg1;
    private final OptionalArg<String> extraArg2;
    private final OptionalArg<String> extraArg3;
    private final OptionalArg<String> extraArg4;
    private final OptionalArg<String> extraArg5;
    
    public AddCommand() {
        super("add", "Add a waypoint (use 'here' for current position or specify x y z)");
        
        this.nameArg = withRequiredArg("name_or_here", "Waypoint name or 'here'", ArgTypes.string());
        this.extraArg1 = withOptionalArg("arg1", "Name (if 'here') or X coordinate", ArgTypes.string());
        this.extraArg2 = withOptionalArg("arg2", "Priority/radius (if 'here') or Y coordinate", ArgTypes.string());
        this.extraArg3 = withOptionalArg("arg3", "Radius (if 'here') or Z coordinate", ArgTypes.string());
        this.extraArg4 = withOptionalArg("arg4", "Priority (if coords)", ArgTypes.string());
        this.extraArg5 = withOptionalArg("arg5", "Radius (if coords)", ArgTypes.string());
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
        
        String firstArg = nameArg.get(ctx);
        
        // Check waypoint limit
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        int maxWaypoints = plugin.getConfigManager().getInt("waypoint.maxWaypoints", 50);
        if (playerData.getWaypoints().size() >= maxWaypoints) {
            playerRef.sendMessage(Message.raw(
                MessageUtils.error("Maximum waypoint limit reached (" + maxWaypoints + ")!")
            ));
            return;
        }
        
        double defaultRadius = plugin.getConfigManager().getDouble("waypoint.defaultRadius", 5.0);
        
        if ("here".equalsIgnoreCase(firstArg)) {
            // === HERE mode: /waypoint add here <name> [priority] [radius] ===
            String arg1 = extraArg1.get(ctx);
            if (arg1 == null || arg1.isEmpty()) {
                playerRef.sendMessage(Message.raw(MessageUtils.error("Usage: /waypoint add here <name> [priority] [radius]")));
                return;
            }
            String name = arg1;
            int priority = parseIntOrDefault(extraArg2.get(ctx), 0);
            double radius = parseDoubleOrDefault(extraArg3.get(ctx), defaultRadius);
            
            if (radius <= 0) {
                playerRef.sendMessage(Message.raw(MessageUtils.error("Radius must be positive!")));
                return;
            }
            
            Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
            
            Waypoint waypoint = new Waypoint(name, position.getX(), position.getY(), position.getZ(),
                                             WaypointType.USER_DEFINED);
            waypoint.setCollectionRadius(radius);
            waypoint.setPriority(priority);
            
            playerData.addWaypoint(waypoint);
            plugin.getStorage().savePlayerDataAsync(playerData);
            
            playerRef.sendMessage(Message.raw(MessageUtils.success("Added waypoint: " + name)));
            playerRef.sendMessage(Message.raw(MessageUtils.info(
                "Location: " + MessageUtils.formatCoordinates(position.getX(), position.getY(), position.getZ())
            )));
            playerRef.sendMessage(Message.raw(MessageUtils.info("Priority: " + priority + " | Radius: " + radius + " blocks")));
        } else {
            // === Coordinate mode: /waypoint add <name> <x> <y> <z> [priority] [radius] ===
            String name = firstArg;
            String xStr = extraArg1.get(ctx);
            String yStr = extraArg2.get(ctx);
            String zStr = extraArg3.get(ctx);
            
            if (xStr == null || yStr == null || zStr == null) {
                playerRef.sendMessage(Message.raw(MessageUtils.error(
                    "Usage: /waypoint add <name> <x> <y> <z> [priority] [radius]  OR  /waypoint add here <name> [priority] [radius]")));
                return;
            }
            
            double x, y, z;
            try {
                x = Double.parseDouble(xStr);
                y = Double.parseDouble(yStr);
                z = Double.parseDouble(zStr);
            } catch (NumberFormatException e) {
                playerRef.sendMessage(Message.raw(MessageUtils.error("Invalid coordinates! x, y, z must be numbers.")));
                return;
            }
            
            int priority = parseIntOrDefault(extraArg4.get(ctx), 0);
            double radius = parseDoubleOrDefault(extraArg5.get(ctx), defaultRadius);
            
            if (radius <= 0) {
                playerRef.sendMessage(Message.raw(MessageUtils.error("Radius must be positive!")));
                return;
            }
            
            Waypoint waypoint = new Waypoint(name, x, y, z, WaypointType.BLOCK_COORDINATES);
            waypoint.setCollectionRadius(radius);
            waypoint.setPriority(priority);
            
            playerData.addWaypoint(waypoint);
            plugin.getStorage().savePlayerDataAsync(playerData);
            
            playerRef.sendMessage(Message.raw(MessageUtils.success("Added waypoint: " + name)));
            playerRef.sendMessage(Message.raw(MessageUtils.info(
                "Location: " + MessageUtils.formatCoordinates(x, y, z)
            )));
            playerRef.sendMessage(Message.raw(MessageUtils.info("Priority: " + priority + " | Radius: " + radius + " blocks")));
        }
    }
    
    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { return defaultValue; }
    }
    
    private double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null) return defaultValue;
        try { return Double.parseDouble(value); } catch (NumberFormatException e) { return defaultValue; }
    }
}
