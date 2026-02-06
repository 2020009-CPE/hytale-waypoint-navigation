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
 * Command to toggle HUD display on/off.
 * Usage: /waypoint toggle-hud
 */
public class ToggleHudCommand extends AbstractPlayerCommand {
    
    public ToggleHudCommand() {
        super("toggle-hud", "Toggle HUD display on/off");
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
        
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        
        boolean newState = !playerData.isHudEnabled();
        playerData.setHudEnabled(newState);
        
        // Save
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        String status = newState ? "§aenabled" : "§cdisabled";
        player.sendMessage(Message.raw(MessageUtils.success("HUD display " + status + "!")));
    }
}
