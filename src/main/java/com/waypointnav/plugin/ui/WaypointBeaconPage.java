package com.waypointnav.plugin.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.waypointnav.plugin.WaypointNavigationPlugin;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Persistent UI overlay that displays a beacon-style indicator for the active waypoint.
 *
 * Shows:
 * - Waypoint name
 * - Directional arrow (left/right/ahead/behind based on player facing)
 * - Distance to waypoint
 * - Waypoint coordinates
 *
 * This replaces the particle-based WorldRenderer approach with a reliable UI overlay
 * that works regardless of distance and graphics settings.
 *
 * Layout file: WaypointNavigation/WaypointBeacon.ui
 */
public class WaypointBeaconPage extends InteractiveCustomUIPage<WaypointBeaconPage.BeaconEventData> {

    private static final Logger LOGGER = Logger.getLogger(WaypointBeaconPage.class.getName());
    public static final String LAYOUT = "WaypointNavigation/WaypointBeacon.ui";

    private final PlayerRef playerRef;

    public WaypointBeaconPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, BeaconEventData.CODEC);
        this.playerRef = playerRef;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store
    ) {
        LOGGER.info(String.format("Building beacon overlay for player %s", playerRef.getUuid()));

        cmd.append(LAYOUT);

        // Populate with current waypoint info
        updateBeaconDisplay(cmd);
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull BeaconEventData data
    ) {
        super.handleDataEvent(ref, store, data);
    }

    /**
     * Refreshes the beacon overlay with current waypoint information.
     * Called periodically by WorldRenderer to keep the display updated.
     */
    public void refresh() {
        UICommandBuilder cmd = new UICommandBuilder();
        updateBeaconDisplay(cmd);
        this.sendUpdate(cmd, new UIEventBuilder(), false);
    }

    /**
     * Updates the beacon display elements with current waypoint data.
     */
    private void updateBeaconDisplay(UICommandBuilder cmd) {
        WaypointNavigationPlugin plugin = WaypointNavigationPlugin.getInstance();
        UUID playerUuid = playerRef.getUuid();
        PlayerWaypointData playerData = plugin.getPlayerDataManager().getPlayerData(playerUuid);

        if (playerData == null) {
            cmd.set("#BeaconName.Text", "No Data");
            cmd.set("#BeaconArrow.Text", "---");
            cmd.set("#BeaconDistance.Text", "---");
            cmd.set("#BeaconCoords.Text", "");
            return;
        }

        Waypoint active = playerData.getActiveWaypoint();
        if (active == null) {
            cmd.set("#BeaconName.Text", "All Complete!");
            cmd.set("#BeaconArrow.Text", "✓");
            cmd.set("#BeaconDistance.Text", "---");
            cmd.set("#BeaconCoords.Text", "");
            return;
        }

        // Waypoint name
        cmd.set("#BeaconName.Text", active.getName());

        // Distance and direction
        Vector3d position = new Vector3d(playerRef.getTransform().getPosition());
        double distance = active.distanceFrom(position.getX(), position.getY(), position.getZ());
        String distStr = MathUtils.formatDistance(distance);
        cmd.set("#BeaconDistance.Text", distStr);

        // Direction arrow based on angle to waypoint
        double targetYaw = MathUtils.calculateYaw(
                position.getX(), position.getZ(),
                active.getX(), active.getZ()
        );
        String arrow = getDirectionArrow(targetYaw);
        cmd.set("#BeaconArrow.Text", arrow);

        // Coordinates
        cmd.set("#BeaconCoords.Text", String.format("(%.0f, %.0f, %.0f)",
                active.getX(), active.getY(), active.getZ()));
    }

    /**
     * Gets a directional arrow string based on the yaw angle to the waypoint.
     *
     * @param yaw The yaw angle in degrees (0-360)
     * @return Arrow characters indicating direction
     */
    private String getDirectionArrow(double yaw) {
        double normalized = MathUtils.normalizeAngle(yaw);

        if (normalized >= 337.5 || normalized < 22.5) {
            return "^ N";
        } else if (normalized < 67.5) {
            return "NE >";
        } else if (normalized < 112.5) {
            return ">> E";
        } else if (normalized < 157.5) {
            return "SE >";
        } else if (normalized < 202.5) {
            return "v S";
        } else if (normalized < 247.5) {
            return "< SW";
        } else if (normalized < 292.5) {
            return "<< W";
        } else {
            return "< NW";
        }
    }

    // ===== Event Data Codec =====

    public static class BeaconEventData {
        public static final BuilderCodec<BeaconEventData> CODEC = BuilderCodec.builder(
                BeaconEventData.class, BeaconEventData::new
        )
        .addField(new KeyedCodec<>("Action", Codec.STRING),
            (e, v) -> e.action = v, e -> e.action)
        .build();

        private String action;

        public BeaconEventData() {}
    }
}
