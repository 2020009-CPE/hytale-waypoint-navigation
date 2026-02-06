package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MessageUtils;
import java.util.UUID;

/**
 * Command to toggle world markers on/off.
 * Usage: /waypoint toggle-world
 */
public class ToggleWorldCommand extends AbstractPlayerCommand {
    
    public ToggleWorldCommand() {
        super("toggle-world", "Toggle world markers on/off");
    }
    
    @Override
    protected void execute(CommandContext ctx,
                         Store<EntityStore> store,
                         Ref<EntityStore> ref,
                         PlayerRef playerRef,
                         World world) {
        
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);
        
        boolean newState = !playerData.isWorldMarkersEnabled();
        playerData.setWorldMarkersEnabled(newState);
        
        // Save
        plugin.getStorage().savePlayerDataAsync(playerData);
        
        String status = newState ? "§aenabled" : "§cdisabled";
        playerRef.sendMessage(Message.raw(MessageUtils.success("World markers " + status + "!")));
    }
}
