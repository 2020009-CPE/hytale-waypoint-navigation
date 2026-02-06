package com.waypointnav.plugin.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.NotificationStyle;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import com.hypixel.hytale.logger.HytaleLogger;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

/**
 * Interactive UI page for the Waypoint Navigation panel.
 *
 * This page provides a visual interface for admins to:
 * - View all waypoints with priority, distance, and completion status
 * - Add new waypoints at the player's current position
 * - Remove individual waypoints by clicking them
 * - Skip current/all waypoints
 * - Clear all waypoints
 *
 * Layout file: Common/UI/Custom/WaypointNavigation/WaypointPanel.ui
 * List item template: Common/UI/Custom/WaypointNavigation/WaypointListItem.ui
 */
public class WaypointPage extends InteractiveCustomUIPage<WaypointPage.WaypointEventData> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static final String LAYOUT = "WaypointNavigation/WaypointPanel.ui";
    public static final String LIST_ITEM = "WaypointNavigation/WaypointListItem.ui";

    private final PlayerRef playerRef;
    private String inputWpName = "";
    private String inputWpPriority = "";
    private String inputWpRadius = "";

    public WaypointPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, WaypointEventData.CODEC);
        this.playerRef = playerRef;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        LOGGER.atInfo().log("Building waypoint UI for player %s", playerRef.getUuid());

        // Load the main panel layout
        cmd.append(LAYOUT);

        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);

        // Get player position for distance calculations
        Vector3d position = new Vector3d(playerRef.getTransform().getPosition());

        // Populate the waypoint list
        populateWaypointList(cmd, evt, playerData, position);

        // Update status and progress labels
        updateStatusLabels(cmd, playerData);

        // === Track text field value changes ===
        evt.addEventBinding(CustomUIEventBindingType.ValueChanged, "#NameInput", EventData.of("@WpName", "#NameInput.Value"), false);
        evt.addEventBinding(CustomUIEventBindingType.ValueChanged, "#PriorityInput", EventData.of("@WpPriority", "#PriorityInput.Value"), false);
        evt.addEventBinding(CustomUIEventBindingType.ValueChanged, "#RadiusInput", EventData.of("@WpRadius", "#RadiusInput.Value"), false);

        // === Bind action button events ===
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#AddHereBtn", EventData.of("Action", "addHere"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#SkipBtn", EventData.of("Action", "skip"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#SkipAllBtn", EventData.of("Action", "skipAll"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#ClearBtn", EventData.of("Action", "clear"), false);
        evt.addEventBinding(CustomUIEventBindingType.Activating, "#CloseBtn", EventData.of("Action", "close"), false);
    }

    /**
     * Populates the waypoint list with dynamic items.
     */
    private void populateWaypointList(UICommandBuilder cmd, UIEventBuilder evt,
                                       PlayerWaypointData playerData, Vector3d position) {
        List<Waypoint> waypoints = playerData.getWaypoints();
        int activeIndex = playerData.getActiveWaypointIndex();

        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);
            double distance = wp.distanceFrom(position.getX(), position.getY(), position.getZ());
            String distStr = MathUtils.formatDistance(distance);

            String status = wp.isCompleted() ? "DONE" : (i == activeIndex ? ">>  " : "    ");
            String label = String.format("%s%d. %s  [P:%d]  %s",
                status, i + 1, wp.getName(), wp.getPriority(), distStr);

            // Append a list item template
            cmd.append("#WaypointList", LIST_ITEM);

            // Set the text on the appended element
            String selector = "#WaypointList[" + i + "]";
            cmd.set(selector + ".Text", label);

            // Bind click event to remove/select this waypoint
            evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                selector,
                EventData.of("Action", "removeWaypoint:" + i),
                false
            );
        }

        if (waypoints.isEmpty()) {
            // Show empty-state message
            cmd.append("#WaypointList", LIST_ITEM);
            cmd.set("#WaypointList[0].Text", "  No waypoints yet. Use the form above to add one.");
        }
    }

    /**
     * Updates the status and progress labels.
     */
    private void updateStatusLabels(UICommandBuilder cmd, PlayerWaypointData playerData) {
        List<Waypoint> waypoints = playerData.getWaypoints();
        int total = waypoints.size();
        int completed = (int) waypoints.stream().filter(Waypoint::isCompleted).count();

        Waypoint active = playerData.getActiveWaypoint();
        String statusText;
        if (active != null) {
            statusText = "Navigating to: " + active.getName() + " (Priority " + active.getPriority() + ")";
        } else if (total > 0) {
            statusText = "All waypoints completed!";
        } else {
            statusText = "No waypoints — add one to start navigation";
        }

        cmd.set("#StatusLabel.Text", statusText);
        cmd.set("#ProgressLabel.Text", completed + "/" + total + " completed");
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull WaypointEventData data
    ) {
        super.handleDataEvent(ref, store, data);

        // Track input field changes
        if (data.wpName != null) {
            this.inputWpName = data.wpName;
        }
        if (data.wpPriority != null) {
            this.inputWpPriority = data.wpPriority;
        }
        if (data.wpRadius != null) {
            this.inputWpRadius = data.wpRadius;
        }

        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getOrCreatePlayerData(playerUuid);

        String action = data.action != null ? data.action : "";

        LOGGER.atInfo().log("Waypoint UI event: action=%s, name=%s",
            data.action, data.wpName);

        if (action.startsWith("removeWaypoint:")) {
            int index = parseIntSafe(action.substring("removeWaypoint:".length()), -1);
            handleRemoveWaypoint(plugin, playerData, index);
        } else switch (action) {
            case "addHere":
                handleAddHere(plugin, playerData);
                break;

            case "skip":
                handleSkip(plugin, playerData);
                break;

            case "skipAll":
                handleSkipAll(plugin, playerData);
                break;

            case "clear":
                handleClear(plugin, playerData);
                break;

            case "close":
                this.close();
                return;

            default:
                LOGGER.atInfo().log("Unknown UI action: %s", data.action);
                return;
        }

        // Refresh the UI after any mutation
        refreshUI(plugin, playerData);
    }

    /**
     * Handles the "Add Here" button — creates a waypoint at the player's position.
     */
    private void handleAddHere(WaypointNavigationPlugin plugin,
                                PlayerWaypointData playerData) {
        String name = (!inputWpName.isEmpty()) ? inputWpName : "Waypoint";
        int priority = parseIntSafe(inputWpPriority, 0);
        double radius = parseDoubleSafe(inputWpRadius,
            plugin.getConfigManager().getDouble("waypoint.defaultRadius", 5.0));

        if (radius <= 0) radius = 5.0;

        int maxWaypoints = plugin.getConfigManager().getInt("waypoint.maxWaypoints", 50);
        if (playerData.getWaypoints().size() >= maxWaypoints) {
            NotificationUtil.sendNotification(
                playerRef.getPacketHandler(),
                Message.raw("Limit Reached"),
                Message.raw("Maximum waypoint limit (" + maxWaypoints + ") reached."),
                NotificationStyle.Warning
            );
            return;
        }

        Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
        Waypoint waypoint = new Waypoint(name, position.getX(), position.getY(), position.getZ(),
                                         WaypointType.USER_DEFINED);
        waypoint.setCollectionRadius(radius);
        waypoint.setPriority(priority);

        playerData.addWaypoint(waypoint);
        plugin.getStorage().savePlayerDataAsync(playerData);

        NotificationUtil.sendNotification(
            playerRef.getPacketHandler(),
            Message.raw("Waypoint Added"),
            Message.raw("Added '" + name + "' (P:" + priority + ", R:" + radius + ")"),
            NotificationStyle.Success
        );
    }

    /**
     * Handles removing a waypoint by list index.
     */
    private void handleRemoveWaypoint(WaypointNavigationPlugin plugin,
                                       PlayerWaypointData playerData,
                                       int index) {
        List<Waypoint> waypoints = playerData.getWaypoints();

        if (index < 0 || index >= waypoints.size()) {
            return;
        }

        Waypoint wp = waypoints.get(index);
        String removedName = wp.getName();
        playerData.removeWaypoint(wp);
        plugin.getStorage().savePlayerDataAsync(playerData);

        NotificationUtil.sendNotification(
            playerRef.getPacketHandler(),
            Message.raw("Waypoint Removed"),
            Message.raw("Removed '" + removedName + "'"),
            NotificationStyle.Default
        );
    }

    /**
     * Handles skipping the current waypoint.
     */
    private void handleSkip(WaypointNavigationPlugin plugin, PlayerWaypointData playerData) {
        Waypoint current = playerData.getActiveWaypoint();
        if (current == null) {
            NotificationUtil.sendNotification(
                playerRef.getPacketHandler(),
                Message.raw("No Active Waypoint"),
                Message.raw("There is no active waypoint to skip."),
                NotificationStyle.Warning
            );
            return;
        }

        String name = current.getName();
        playerData.skipCurrentWaypoint();
        plugin.getStorage().savePlayerDataAsync(playerData);

        NotificationUtil.sendNotification(
            playerRef.getPacketHandler(),
            Message.raw("Waypoint Skipped"),
            Message.raw("Skipped '" + name + "'"),
            NotificationStyle.Success
        );
    }

    /**
     * Handles skipping all remaining waypoints.
     */
    private void handleSkipAll(WaypointNavigationPlugin plugin, PlayerWaypointData playerData) {
        long remaining = playerData.getWaypoints().stream().filter(wp -> !wp.isCompleted()).count();
        if (remaining == 0) {
            NotificationUtil.sendNotification(
                playerRef.getPacketHandler(),
                Message.raw("Already Done"),
                Message.raw("All waypoints are already completed."),
                NotificationStyle.Warning
            );
            return;
        }

        playerData.skipAllWaypoints();
        plugin.getStorage().savePlayerDataAsync(playerData);

        NotificationUtil.sendNotification(
            playerRef.getPacketHandler(),
            Message.raw("All Skipped"),
            Message.raw("Skipped " + remaining + " remaining waypoint(s)."),
            NotificationStyle.Success
        );
    }

    /**
     * Handles clearing all waypoints.
     */
    private void handleClear(WaypointNavigationPlugin plugin, PlayerWaypointData playerData) {
        int count = playerData.getWaypoints().size();
        if (count == 0) {
            NotificationUtil.sendNotification(
                playerRef.getPacketHandler(),
                Message.raw("Nothing to Clear"),
                Message.raw("You have no waypoints."),
                NotificationStyle.Warning
            );
            return;
        }

        playerData.clearWaypoints();
        plugin.getStorage().savePlayerDataAsync(playerData);

        NotificationUtil.sendNotification(
            playerRef.getPacketHandler(),
            Message.raw("Waypoints Cleared"),
            Message.raw("Cleared " + count + " waypoint(s)."),
            NotificationStyle.Success
        );
    }

    /**
     * Refreshes the entire UI by clearing and rebuilding the waypoint list.
     */
    private void refreshUI(WaypointNavigationPlugin plugin, PlayerWaypointData playerData) {
        UICommandBuilder cmd = new UICommandBuilder();
        UIEventBuilder evt = new UIEventBuilder();

        // Clear the list container
        cmd.clear("#WaypointList");

        // Re-populate
        Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
        List<Waypoint> waypoints = playerData.getWaypoints();
        int activeIndex = playerData.getActiveWaypointIndex();

        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);
            double distance = wp.distanceFrom(position.getX(), position.getY(), position.getZ());
            String distStr = MathUtils.formatDistance(distance);

            String status = wp.isCompleted() ? "DONE" : (i == activeIndex ? ">>  " : "    ");
            String label = String.format("%s%d. %s  [P:%d]  %s",
                status, i + 1, wp.getName(), wp.getPriority(), distStr);

            cmd.append("#WaypointList", LIST_ITEM);
            String selector = "#WaypointList[" + i + "]";
            cmd.set(selector + ".Text", label);

            // Re-register remove event binding for dynamically added items
            evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                selector,
                EventData.of("Action", "removeWaypoint:" + i),
                false
            );
        }

        if (waypoints.isEmpty()) {
            cmd.append("#WaypointList", LIST_ITEM);
            cmd.set("#WaypointList[0].Text", "  No waypoints yet. Use the form above to add one.");
        }

        // Update labels
        int total = waypoints.size();
        int completed = (int) waypoints.stream().filter(Waypoint::isCompleted).count();

        Waypoint active = playerData.getActiveWaypoint();
        String statusText;
        if (active != null) {
            statusText = "Navigating to: " + active.getName() + " (Priority " + active.getPriority() + ")";
        } else if (total > 0) {
            statusText = "All waypoints completed!";
        } else {
            statusText = "No waypoints — add one to start navigation";
        }

        cmd.set("#StatusLabel.Text", statusText);
        cmd.set("#ProgressLabel.Text", completed + "/" + total + " completed");

        this.sendUpdate(cmd, evt, false);
    }

    // --- Parsing helpers ---

    private int parseIntSafe(String value, int defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { return defaultValue; }
    }

    private double parseDoubleSafe(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try { return Double.parseDouble(value); } catch (NumberFormatException e) { return defaultValue; }
    }

    // ===== Event Data Codec =====

    public static class WaypointEventData {
        public static final BuilderCodec<WaypointEventData> CODEC = BuilderCodec.builder(
                WaypointEventData.class, WaypointEventData::new
        )
        .addField(new KeyedCodec<>("Action", Codec.STRING),
            (e, v) -> e.action = v, e -> e.action)
        .addField(new KeyedCodec<>("@WpName", Codec.STRING),
            (e, v) -> e.wpName = v, e -> e.wpName)
        .addField(new KeyedCodec<>("@WpPriority", Codec.STRING),
            (e, v) -> e.wpPriority = v, e -> e.wpPriority)
        .addField(new KeyedCodec<>("@WpRadius", Codec.STRING),
            (e, v) -> e.wpRadius = v, e -> e.wpRadius)
        .build();

        private String action;
        private String wpName;
        private String wpPriority;
        private String wpRadius;

        public WaypointEventData() {}
    }
}
