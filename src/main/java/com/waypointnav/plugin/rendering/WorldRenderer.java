package com.waypointnav.plugin.rendering;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.ui.WaypointBeaconPage;
import com.waypointnav.plugin.waypoint.Waypoint;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Handles rendering of world-space markers for waypoints.
 *
 * Instead of using particles (which require an unavailable particle API),
 * this renderer uses a UI-based beacon overlay ({@link WaypointBeaconPage})
 * that displays a persistent on-screen indicator showing the active waypoint's
 * name, direction, distance, and coordinates.
 *
 * The beacon overlay is automatically opened when a player has an active waypoint
 * and closed when navigation is disabled or all waypoints are completed.
 * It refreshes periodically to keep direction and distance up-to-date.
 */
public class WorldRenderer {
    private static final Logger LOGGER = Logger.getLogger(WorldRenderer.class.getName());
    private int tickCounter = 0;

    private static final int RENDER_INTERVAL = 10; // Ticks between beacon UI updates

    /** Active beacon overlay pages per player UUID. */
    private final Map<UUID, WaypointBeaconPage> activeBeacons = new ConcurrentHashMap<>();

    /**
     * Updates world markers for a player. Should be called every tick.
     *
     * @param playerData The player's waypoint data
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @param playerZ Player's Z position
     */
    public void update(PlayerWaypointData playerData,
                      double playerX, double playerY, double playerZ) {

        tickCounter++;

        if (!playerData.isWorldMarkersEnabled()) {
            closeBeacon(playerData.getPlayerUuid());
            return;
        }

        Waypoint active = playerData.getActiveWaypoint();
        if (active == null) {
            closeBeacon(playerData.getPlayerUuid());
            return;
        }

        // Refresh the beacon overlay periodically
        if (tickCounter % RENDER_INTERVAL == 0) {
            refreshBeacon(playerData.getPlayerUuid());
        }
    }

    /**
     * Opens a beacon overlay for a player. Call this when a player has an active
     * waypoint and the beacon should be shown.
     *
     * @param playerRef The player reference
     * @param ref Entity reference
     * @param store Entity store
     */
    public void openBeacon(PlayerRef playerRef, Ref<EntityStore> ref, Store<EntityStore> store) {
        UUID playerUuid = playerRef.getUuid();

        if (activeBeacons.containsKey(playerUuid)) {
            LOGGER.fine(String.format("[DEBUG] Beacon already open for player %s", playerUuid));
            return;
        }

        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            LOGGER.warning(String.format("Could not open beacon for player %s — Player component not found.", playerUuid));
            return;
        }

        WaypointBeaconPage beaconPage = new WaypointBeaconPage(playerRef);
        player.getPageManager().openCustomPage(ref, store, beaconPage);
        activeBeacons.put(playerUuid, beaconPage);

        LOGGER.info(String.format("Opened waypoint beacon overlay for player %s", playerUuid));
    }

    /**
     * Refreshes the beacon overlay for a player.
     *
     * @param playerUuid The player's UUID
     */
    private void refreshBeacon(UUID playerUuid) {
        WaypointBeaconPage beacon = activeBeacons.get(playerUuid);
        if (beacon != null) {
            beacon.refresh();
        }
    }

    /**
     * Closes the beacon overlay for a player.
     *
     * @param playerUuid The player's UUID
     */
    public void closeBeacon(UUID playerUuid) {
        WaypointBeaconPage beacon = activeBeacons.remove(playerUuid);
        if (beacon != null) {
            beacon.close();
            LOGGER.info(String.format("Closed waypoint beacon overlay for player %s", playerUuid));
        }
    }

    /**
     * Checks if a player has an active beacon overlay.
     *
     * @param playerUuid The player's UUID
     * @return true if the player has an active beacon
     */
    public boolean hasBeacon(UUID playerUuid) {
        return activeBeacons.containsKey(playerUuid);
    }

    /**
     * Clears all world markers and beacon overlays.
     */
    public void clear() {
        LOGGER.fine("[DEBUG] Clearing all world markers and beacon overlays.");
        activeBeacons.values().forEach(WaypointBeaconPage::close);
        activeBeacons.clear();
        tickCounter = 0;
    }
}
