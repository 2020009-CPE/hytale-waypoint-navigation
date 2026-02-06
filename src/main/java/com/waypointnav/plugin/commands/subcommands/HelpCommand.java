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
        
        playerRef.sendMessage(Message.raw(MessageUtils.header("Waypoint Navigation - Quick Start Guide")));
        playerRef.sendMessage(Message.raw(""));
        
        // Step-by-step tutorial
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 1: Create your first waypoint")));
        playerRef.sendMessage(Message.raw("  /waypoint add \"Town Square\" 10"));
        playerRef.sendMessage(Message.raw("  (Saves your current location with a 10-block radius)"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 2: Add more waypoints along your route")));
        playerRef.sendMessage(Message.raw("  /waypoint add \"Forest Camp\""));
        playerRef.sendMessage(Message.raw("  /waypoint add \"Mountain Peak\" 15"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 3: View your waypoints")));
        playerRef.sendMessage(Message.raw("  /waypoint list"));
        playerRef.sendMessage(Message.raw("  (Shows all waypoints with distances and completion status)"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 4: Navigate to waypoints")));
        playerRef.sendMessage(Message.raw("  Walk toward the HUD arrow and world markers."));
        playerRef.sendMessage(Message.raw("  Waypoints auto-complete when you reach them."));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.plain("Step 5: Control your navigation")));
        playerRef.sendMessage(Message.raw("  /waypoint next       - Switch to next waypoint"));
        playerRef.sendMessage(Message.raw("  /waypoint skip       - Skip current waypoint"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle     - Turn navigation on/off"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle-hud - Toggle HUD arrow display"));
        playerRef.sendMessage(Message.raw("  /waypoint toggle-world - Toggle world markers"));
        playerRef.sendMessage(Message.raw(""));
        
        playerRef.sendMessage(Message.raw(MessageUtils.header("All Commands")));
        playerRef.sendMessage(Message.raw("  /waypoint add <name> [radius] - Add waypoint at your location"));
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
