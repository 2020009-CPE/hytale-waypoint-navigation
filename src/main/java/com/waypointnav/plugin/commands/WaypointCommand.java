package com.waypointnav.plugin.commands;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.commands.subcommands.*;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Main waypoint command handler that dispatches to subcommands.
 * 
 * TODO: Extend Hytale's CommandBase when API is available.
 * This is a placeholder showing the required structure.
 */
public class WaypointCommand {
    private final WaypointNavigationPlugin plugin;
    private final Map<String, Object> subcommands;
    
    public WaypointCommand(@Nonnull WaypointNavigationPlugin plugin) {
        this.plugin = plugin;
        this.subcommands = new HashMap<>();
        
        // Register all subcommands
        registerSubcommands();
    }
    
    /**
     * Registers all subcommands.
     */
    private void registerSubcommands() {
        subcommands.put("add", new AddCommand(plugin));
        subcommands.put("remove", new RemoveCommand(plugin));
        subcommands.put("clear", new ClearCommand(plugin));
        subcommands.put("list", new ListCommand(plugin));
        subcommands.put("next", new NextCommand(plugin));
        subcommands.put("skip", new SkipCommand(plugin));
        subcommands.put("skipall", new SkipAllCommand(plugin));
        subcommands.put("toggle", new ToggleCommand(plugin));
        subcommands.put("toggle-hud", new ToggleHudCommand(plugin));
        subcommands.put("toggle-world", new ToggleWorldCommand(plugin));
    }
    
    /**
     * Executes the command.
     * TODO: Override executeSync from Hytale's CommandBase
     *
     * @param ctx The command context
     */
    // @Override
    // protected void executeSync(@Nonnull CommandContext ctx) {
    public void execute(Object ctx) {
        // TODO: Extract args from context using Hytale API
        // String[] args = ctx.getArgs();
        // Player player = ctx.getPlayer();
        
        // if (args.length == 0) {
        //     sendHelp(player);
        //     return;
        // }
        
        // String subcommandName = args[0].toLowerCase();
        // Object subcommand = subcommands.get(subcommandName);
        
        // if (subcommand == null) {
        //     player.sendMessage(MessageUtils.error("Unknown subcommand: " + subcommandName));
        //     sendHelp(player);
        //     return;
        // }
        
        // Execute subcommand
        // TODO: Call subcommand execute method
    }
    
    /**
     * Sends help message to player.
     *
     * @param player The player
     */
    private void sendHelp(Object player) {
        // TODO: Send formatted help message using Hytale API
        // player.sendMessage(MessageUtils.header("Waypoint Navigation Commands"));
        // player.sendMessage(MessageUtils.info("/waypoint add <name> - Add waypoint at current location"));
        // player.sendMessage(MessageUtils.info("/waypoint remove <index> - Remove a waypoint"));
        // player.sendMessage(MessageUtils.info("/waypoint clear - Clear all waypoints"));
        // player.sendMessage(MessageUtils.info("/waypoint list - List all waypoints"));
        // player.sendMessage(MessageUtils.info("/waypoint next - Go to next waypoint"));
        // player.sendMessage(MessageUtils.info("/waypoint skip - Skip current waypoint"));
        // player.sendMessage(MessageUtils.info("/waypoint skipall - Skip all waypoints"));
        // player.sendMessage(MessageUtils.info("/waypoint toggle - Toggle navigation on/off"));
        // player.sendMessage(MessageUtils.info("/waypoint toggle-hud - Toggle HUD display"));
        // player.sendMessage(MessageUtils.info("/waypoint toggle-world - Toggle world markers"));
    }
    
    /**
     * Registers this command with the command system.
     * TODO: Use Hytale's command registration system
     */
    public void register() {
        // TODO: Register with Hytale's command registry
        // plugin.getCommandRegistry().registerCommand(this);
    }
}
