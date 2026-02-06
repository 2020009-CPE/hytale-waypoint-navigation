package com.waypointnav.plugin.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.waypointnav.plugin.commands.subcommands.*;

/**
 * Main waypoint command handler that dispatches to subcommands.
 */
public class WaypointCommand extends AbstractCommandCollection {
    
    public WaypointCommand() {
        super("waypoint", "Waypoint navigation commands");
        
        // Register all subcommands
        addSubCommand(new AddCommand());
        addSubCommand(new RemoveCommand());
        addSubCommand(new ClearCommand());
        addSubCommand(new ListCommand());
        addSubCommand(new NextCommand());
        addSubCommand(new SkipCommand());
        addSubCommand(new SkipAllCommand());
        addSubCommand(new ToggleCommand());
        addSubCommand(new ToggleHudCommand());
        addSubCommand(new ToggleWorldCommand());
    }
}
