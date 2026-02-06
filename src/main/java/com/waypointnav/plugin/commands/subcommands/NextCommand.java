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
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.utils.MessageUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Command to advance to the next waypoint without marking current as completed.
 * Usage: /waypoint next
 */
public class NextCommand extends AbstractPlayerCommand {
    
    public NextCommand() {
        super("next", "Switch to the next waypoint");
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
        
        if (playerData.nextWaypoint()) {
            Waypoint nextWaypoint = playerData.getActiveWaypoint();
            if (nextWaypoint != null) {
                player.sendMessage(Message.raw(
                    MessageUtils.success("Switched to next waypoint: " + nextWaypoint.getName())
                ));
                
                TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());
                Vec3d position = transform.getPosition();
                double distance = nextWaypoint.distanceFrom(position.getX(), position.getY(), position.getZ());
                
                player.sendMessage(Message.raw(MessageUtils.info(
                    "Distance: " + MathUtils.formatDistance(distance)
                )));
            }
            
            // Save
            plugin.getStorage().savePlayerDataAsync(playerData);
        } else {
            player.sendMessage(Message.raw(
                MessageUtils.error("You are already at the last waypoint!")
            ));
        }
    }
}
