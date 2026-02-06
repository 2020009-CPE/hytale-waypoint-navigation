package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.waypointnav.plugin.utils.MessageUtils;

/**
 * Command to display a step-by-step tutorial for the waypoint navigation system.
 * Usage: /waypoint help
 * Requires: waypoint.admin permission
 */
public class HelpCommand extends AbstractPlayerCommand {
    
    public HelpCommand() {
        super("help", "Show step-by-step usage guide");
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
        
        playerRef.sendMessage(Message.raw(MessageUtils.header("Waypoint Navigation - Admin Guide")));
        playerRef.sendMessage(Message.raw(""));
        playerRef.sendMessage(Message.raw(MessageUtils.plain("All commands require 'waypoint.admin' permission.")));
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Navigation is always enabled automatically for all players.")));
        playerRef.sendMessage(Message.raw(""));
        
        // How it works
        playerRef.sendMessage(Message.raw(MessageUtils.header("How Navigation Works")));
        playerRef.sendMessage(Message.raw("  1. Player joins the server -> data is loaded."));
        playerRef.sendMessage(Message.raw("  2. Navigation is automatically enabled."));
        playerRef.sendMessage(Message.raw("  3. Arrow targets the highest-priority incomplete waypoint."));
        playerRef.sendMessage(Message.raw("  4. When the player enters the waypoint radius:"));
        playerRef.sendMessage(Message.raw("     -> Waypoint is completed."));
        playerRef.sendMessage(Message.raw("     -> Arrow switches to next priority waypoint."));
        playerRef.sendMessage(Message.raw("  5. When no waypoints remain, visuals are hidden."));
        playerRef.sendMessage(Message.raw(""));
        
        // Step-by-step admin tutorial
        playerRef.sendMessage(Message.raw(MessageUtils.header("Step-by-Step Admin Tutorial")));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 1: Add a waypoint at your position")));
        playerRef.sendMessage(Message.raw("  /waypoint add here \"Town Square\" 0 10"));
        playerRef.sendMessage(Message.raw("  (Name: Town Square, Priority: 0, Radius: 10 blocks)"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 2: Add waypoints at specific coordinates")));
        playerRef.sendMessage(Message.raw("  /waypoint add \"Forest Camp\" 100 64 200 1"));
        playerRef.sendMessage(Message.raw("  (Name, X Y Z, Priority 1 = visited after priority 0)"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 3: View all waypoints")));
        playerRef.sendMessage(Message.raw("  /waypoint list"));
        playerRef.sendMessage(Message.raw("  (Shows priority, distance, and completion status)"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 4: Navigate!")));
        playerRef.sendMessage(Message.raw("  Follow the tall particle beacon and HUD arrow."));
        playerRef.sendMessage(Message.raw("  Walk within the radius to auto-complete each waypoint."));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.header("All Commands (Admin Only)")));
        playerRef.sendMessage(Message.raw("  /waypoint add here <name> [priority] [radius]"));
        playerRef.sendMessage(Message.raw("  /waypoint add <name> <x> <y> <z> [priority] [radius]"));
        playerRef.sendMessage(Message.raw("  /waypoint remove <index>      - Remove waypoint by number"));
        playerRef.sendMessage(Message.raw("  /waypoint clear               - Remove all waypoints"));
        playerRef.sendMessage(Message.raw("  /waypoint list                - Show all waypoints"));
        playerRef.sendMessage(Message.raw("  /waypoint next                - Go to next waypoint"));
        playerRef.sendMessage(Message.raw("  /waypoint skip                - Skip current waypoint"));
        playerRef.sendMessage(Message.raw("  /waypoint skipall             - Skip all waypoints"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle              - Toggle navigation on/off"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle-hud          - Toggle HUD display"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle-world        - Toggle world markers"));
        playerRef.sendMessage(Message.raw("  /waypoint help                - Show this guide"));
    }
}
