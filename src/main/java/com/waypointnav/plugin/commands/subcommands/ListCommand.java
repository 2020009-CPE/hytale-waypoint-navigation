package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.command.AbstractPlayerCommand;
import com.hypixel.hytale.command.CommandContext;
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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * Command to list all waypoints for a player.
 * Usage: /waypoint list
 */
public class ListCommand extends AbstractPlayerCommand {
    
    public ListCommand() {
        super("list", "List all your waypoints");
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
        
        List<Waypoint> waypoints = playerData.getWaypoints();
        int activeIndex = playerData.getActiveWaypointIndex();
        
        // Get player location
        TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
        Vec3d position = transform.getPosition();
        
        player.sendMessage(Message.raw(MessageUtils.header("Your Waypoints")));
        
        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);
            double distance = wp.distanceFrom(position.getX(), position.getY(), position.getZ());
            
            String marker = (i == activeIndex) ? "§a→ " : "  ";
            String message = MessageUtils.formatWaypointList(
                i, wp.getName(), 
                wp.getX(), wp.getY(), wp.getZ(),
                distance, wp.isCompleted()
            );
            
            player.sendMessage(Message.raw(marker + message));
        }
        
        int completed = (int) waypoints.stream().filter(Waypoint::isCompleted).count();
        player.sendMessage(Message.raw(MessageUtils.info(
            "Progress: " + completed + "/" + waypoints.size() + " completed"
        )));
    }
}
