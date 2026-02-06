package com.waypointnav.plugin.commands.subcommands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.waypointnav.plugin.ui.WaypointPage;
import com.waypointnav.plugin.utils.MessageUtils;

import javax.annotation.Nonnull;

/**
 * Command to open the visual Waypoint Management UI panel.
 * Usage: /waypoint ui
 * Requires: waypoint.admin permission
 *
 * Opens an interactive panel where admins can:
 * - View all waypoints with priority, distance, and status
 * - Add waypoints at their current position
 * - Remove waypoints by clicking them
 * - Skip, skip all, or clear waypoints
 */
public class UICommand extends AbstractPlayerCommand {

    public UICommand() {
        super("ui", "Open the waypoint management panel");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world
    ) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            playerRef.sendMessage(Message.raw(MessageUtils.error("Could not access player entity.")));
            return;
        }

        WaypointPage page = new WaypointPage(playerRef);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
