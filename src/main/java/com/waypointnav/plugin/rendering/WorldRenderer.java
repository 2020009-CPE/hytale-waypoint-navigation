package com.waypointnav.plugin.rendering;

import com.hypixel.hytale.logger.HytaleLogger;
import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.waypoint.Waypoint;
/**
 * Handles rendering of world-space markers for waypoints.
 * Creates 3D particle effects and visual indicators in the game world.
 *
 * Particles are rendered frequently and in high counts to ensure they are
 * clearly visible. A tall vertical beacon column marks the waypoint location,
 * and a dense particle trail guides players from their position toward it.
 */
public class WorldRenderer {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private int tickCounter = 0;

    // Rendering constants — tuned for maximum visibility
    private static final int BEACON_HEIGHT = 20;        // Tall vertical column (blocks)
    private static final int BEACON_RING_POINTS = 16;   // Points per ring for circular base
    private static final double BEACON_RADIUS = 1.5;    // Ring radius at base
    private static final int TRAIL_MAX_PARTICLES = 20;  // Max particles in direction trail
    private static final double TRAIL_SPACING = 1.5;    // Blocks between trail particles
    private static final int RENDER_INTERVAL = 2;       // Ticks between updates (10 times/sec)
    private static final double MAX_RENDER_DISTANCE = 500; // Max render distance in blocks

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
            LOGGER.fine("[DEBUG] World markers disabled for player, skipping render.");
            return;
        }

        Waypoint active = playerData.getActiveWaypoint();
        if (active == null) {
            LOGGER.fine("[DEBUG] No active waypoint for player, skipping world marker render.");
            return;
        }

        // Render every RENDER_INTERVAL ticks (10 times/second for high visibility)
        if (tickCounter % RENDER_INTERVAL == 0) {
            renderWaypointMarker(active, playerX, playerY, playerZ);
        }
    }

    /**
     * Renders a marker for a waypoint in the world.
     *
     * @param waypoint The waypoint to mark
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @param playerZ Player's Z position
     */
    private void renderWaypointMarker(Waypoint waypoint,
                                      double playerX, double playerY, double playerZ) {
        double distance = waypoint.distanceFrom(playerX, playerY, playerZ);

        // Don't render if too far away (performance optimization)
        if (distance > MAX_RENDER_DISTANCE) {
            LOGGER.fine("[DEBUG] Waypoint '%s' is %.1f blocks away (>%.0f), skipping world marker.",
                waypoint.getName(), distance, MAX_RENDER_DISTANCE);
            return;
        }

        LOGGER.fine("[DEBUG] Rendering world marker for waypoint '%s' at (%.1f, %.1f, %.1f), distance: %.1f",
            waypoint.getName(), waypoint.getX(), waypoint.getY(), waypoint.getZ(), distance);

        // Calculate direction vector from player eye level to waypoint
        double[] direction = MathUtils.directionVector(
            playerX, playerY + 1.6, playerZ,
            waypoint.getX(), waypoint.getY(), waypoint.getZ()
        );

        // 1) Dense particle trail from player toward waypoint
        renderParticleTrail(
            playerX, playerY + 1.6, playerZ,
            direction[0], direction[1], direction[2],
            distance
        );

        // 2) Tall beacon column at the waypoint location
        renderBeaconColumn(waypoint.getX(), waypoint.getY(), waypoint.getZ(), distance);

        // 3) Rotating ring at the base of the beacon
        renderBeaconRing(waypoint.getX(), waypoint.getY(), waypoint.getZ());
    }

    /**
     * Renders a dense line of particles from the player toward the waypoint.
     * Uses close spacing (1.5 blocks) and up to 20 particles for clear directionality.
     */
    private void renderParticleTrail(double startX, double startY, double startZ,
                                     double dirX, double dirY, double dirZ,
                                     double distance) {
        int particleCount = Math.min(TRAIL_MAX_PARTICLES, (int)(distance / TRAIL_SPACING));

        String particleType = getDistanceParticleType(distance);

        for (int i = 1; i <= particleCount; i++) {
            double step = i * TRAIL_SPACING;
            double x = startX + dirX * step;
            double y = startY + dirY * step;
            double z = startZ + dirZ * step;

            spawnParticle(x, y, z, particleType);
            // Spawn a second offset particle for extra density
            spawnParticle(x, y + 0.3, z, particleType);
        }
    }

    /**
     * Renders a tall vertical beacon column at the waypoint.
     * The column is BEACON_HEIGHT blocks tall, using bright particles every 0.5 blocks
     * so it is visible from far away.
     */
    private void renderBeaconColumn(double x, double y, double z, double distance) {
        String columnType = getDistanceParticleType(distance);

        for (int i = 0; i < BEACON_HEIGHT * 2; i++) {
            double offsetY = i * 0.5;
            spawnParticle(x, y + offsetY, z, columnType);
        }

        // Additional bright "END_ROD" particles along the column for glow effect
        for (int i = 0; i < BEACON_HEIGHT; i++) {
            spawnParticle(x, y + i, z, "END_ROD");
        }
    }

    /**
     * Renders a rotating ring of particles at the waypoint base for visibility.
     */
    private void renderBeaconRing(double x, double y, double z) {
        double angle = (tickCounter * 15) % 360;
        double radians = Math.toRadians(angle);

        for (int i = 0; i < BEACON_RING_POINTS; i++) {
            double offsetAngle = radians + (i * 2 * Math.PI / BEACON_RING_POINTS);
            double offsetX = Math.cos(offsetAngle) * BEACON_RADIUS;
            double offsetZ = Math.sin(offsetAngle) * BEACON_RADIUS;

            spawnParticle(x + offsetX, y, z + offsetZ, "FLAME");
            spawnParticle(x + offsetX, y + 0.5, z + offsetZ, "FLAME");
        }
    }

    /**
     * Spawns a particle at the specified location.
     * Placeholder for Hytale particle API integration.
     *
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param particleType Type of particle to spawn
     */
    private void spawnParticle(double x, double y, double z, String particleType) {
        // TODO: Use Hytale's particle system when API is available:
        // world.spawnParticle(particleType, x, y, z, count, offsetX, offsetY, offsetZ, speed);
        LOGGER.fine("[DEBUG] Particle spawn requested: type=%s at (%.2f, %.2f, %.2f) - awaiting Hytale particle API",
            particleType, x, y, z);
    }

    /**
     * Gets the particle type based on distance — distance-based color coding.
     *
     * @param distance Distance in blocks
     * @return Particle type name
     */
    private String getDistanceParticleType(double distance) {
        if (distance < 50) {
            return "HAPPY_VILLAGER"; // Green — very close
        } else if (distance < 200) {
            return "FLAME"; // Orange/Yellow — medium
        } else {
            return "LAVA"; // Red — far
        }
    }

    /**
     * Clears all world markers.
     */
    public void clear() {
        LOGGER.fine("[DEBUG] Clearing all world markers, resetting tick counter.");
        tickCounter = 0;
    }
}
