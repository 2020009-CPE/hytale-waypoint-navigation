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
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import java.util.List;
import java.util.UUID;

/**
 * Command to list all waypoints for a player.
 * Usage: /waypoint list
 */
public class ListCommand extends AbstractPlayerCommand {
    
    public ListCommand() {
        super("list", "List all your waypoints");
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
        
        List<Waypoint> waypoints = playerData.getWaypoints();
        int activeIndex = playerData.getActiveWaypointIndex();
        
        // Get player position from the player's transform
        Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
        
        playerRef.sendMessage(Message.raw(MessageUtils.header("Your Waypoints")));
        
        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);
            double distance = wp.distanceFrom(position.getX(), position.getY(), position.getZ());
            
            String marker = (i == activeIndex) ? "> " : "  ";
            String message = MessageUtils.formatWaypointList(
                i, wp.getName(), 
                wp.getX(), wp.getY(), wp.getZ(),
                distance, wp.isCompleted()
            );
            String priorityStr = " (P:" + wp.getPriority() + ")";
            
            playerRef.sendMessage(Message.raw(marker + message + priorityStr));
        }
        
        int completed = (int) waypoints.stream().filter(Waypoint::isCompleted).count();
        playerRef.sendMessage(Message.raw(MessageUtils.info(
            "Progress: " + completed + "/" + waypoints.size() + " completed"
        )));
    }
}
