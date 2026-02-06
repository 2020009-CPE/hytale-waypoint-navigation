package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import java.util.UUID;

/**
 * Command to advance to the next waypoint without marking current as completed.
 * Usage: /waypoint next
 */
public class NextCommand extends AbstractPlayerCommand {
    
    public NextCommand() {
        super("next", "Switch to the next waypoint");
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
        
        if (playerData.nextWaypoint()) {
            Waypoint nextWaypoint = playerData.getActiveWaypoint();
            if (nextWaypoint != null) {
                playerRef.sendMessage(Message.raw(
                    MessageUtils.success("Switched to next waypoint: " + nextWaypoint.getName())
                ));
                
                Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
                double distance = nextWaypoint.distanceFrom(position.getX(), position.getY(), position.getZ());
                
                playerRef.sendMessage(Message.raw(MessageUtils.info(
                    "Distance: " + MathUtils.formatDistance(distance)
                )));
            }
            
            // Save
            plugin.getStorage().savePlayerDataAsync(playerData);
        } else {
            playerRef.sendMessage(Message.raw(
                MessageUtils.error("You are already at the last waypoint!")
            ));
        }
    }
}
