package com.waypointnav.plugin.utils;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Utility class for broadcasting waypoint events and progress to all online players.
 *
 * Used in global waypoint mode to keep all players informed about:
 * - When a player reaches a waypoint
 * - When new waypoints are added/removed
 * - Overall tour progress
 */
public class BroadcastUtils {
    private static final Logger LOGGER = Logger.getLogger(BroadcastUtils.class.getName());

    /**
     * Broadcasts a chat message to all online players.
     *
     * @param message The message to broadcast
     */
    public static void broadcastMessage(String message) {
        Universe.get().getPlayers().forEach(playerRef -> {
            UUID worldUuid = playerRef.getWorldUuid();
            if (worldUuid != null) {
                World world = Universe.get().getWorld(worldUuid);
                if (world != null) {
                    world.execute(() -> playerRef.sendMessage(Message.raw(message)));
                }
            }
        });
    }

    /**
     * Broadcasts a title and subtitle to all online players.
     * Uses the Hytale EventTitleUtil for prominent on-screen display.
     *
     * @param title The main title text
     * @param subtitle The subtitle text
     * @param playSound Whether to play a notification sound
     */
    public static void broadcastTitle(String title, String subtitle, boolean playSound) {
        Universe.get().getPlayers().forEach(playerRef -> {
            UUID worldUuid = playerRef.getWorldUuid();
            if (worldUuid != null) {
                World world = Universe.get().getWorld(worldUuid);
                if (world != null) {
                    world.execute(() ->
                        EventTitleUtil.showEventTitleToPlayer(
                            playerRef, Message.raw(title), Message.raw(subtitle), playSound
                        )
                    );
                }
            }
        });
    }

    /**
     * Broadcasts that a player has reached a waypoint.
     * Shows both a title to the player and a chat message to everyone.
     *
     * @param playerName The name of the player who reached the waypoint
     * @param waypointName The name of the reached waypoint
     * @param completed Number of completed waypoints
     * @param total Total number of waypoints
     */
    public static void broadcastWaypointReached(String playerName, String waypointName,
                                                 int completed, int total) {
        String chatMsg = MessageUtils.plain(
            playerName + " reached waypoint '" + waypointName + "' (" + completed + "/" + total + ")"
        );
        broadcastMessage(chatMsg);

        LOGGER.info(String.format("Broadcast: %s reached waypoint '%s' (%d/%d)",
            playerName, waypointName, completed, total));
    }

    /**
     * Broadcasts that a new waypoint has been added globally.
     *
     * @param waypointName The name of the new waypoint
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public static void broadcastWaypointAdded(String waypointName, double x, double y, double z) {
        String chatMsg = MessageUtils.plain(
            "New waypoint added: '" + waypointName + "' at " + MessageUtils.formatCoordinates(x, y, z)
        );
        broadcastMessage(chatMsg);

        broadcastTitle(
            "New Waypoint",
            waypointName + " - " + MessageUtils.formatCoordinates(x, y, z),
            true
        );
    }

    /**
     * Broadcasts that waypoints have been cleared globally.
     *
     * @param count The number of waypoints cleared
     */
    public static void broadcastWaypointsCleared(int count) {
        String chatMsg = MessageUtils.plain(
            "All " + count + " waypoint(s) have been cleared by an admin."
        );
        broadcastMessage(chatMsg);

        broadcastTitle("Waypoints Cleared", count + " waypoint(s) removed", true);
    }

    /**
     * Broadcasts that a waypoint has been removed globally.
     *
     * @param waypointName The name of the removed waypoint
     */
    public static void broadcastWaypointRemoved(String waypointName) {
        String chatMsg = MessageUtils.plain(
            "Waypoint '" + waypointName + "' has been removed by an admin."
        );
        broadcastMessage(chatMsg);
    }

    /**
     * Broadcasts that all waypoints have been completed (tour finished).
     *
     * @param playerName The name of the player who completed the tour
     */
    public static void broadcastTourComplete(String playerName) {
        broadcastTitle(
            "Tour Complete!",
            playerName + " has completed all waypoints!",
            true
        );
    }
}
