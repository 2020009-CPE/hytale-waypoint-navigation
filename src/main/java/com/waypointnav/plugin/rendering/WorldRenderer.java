package com.waypointnav.plugin.rendering;

import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.utils.MathUtils;
import com.waypointnav.plugin.waypoint.Waypoint;

import javax.annotation.Nonnull;

/**
 * Handles rendering of world-space markers for waypoints.
 * Creates 3D particle effects and visual indicators in the game world.
 * 
 * Note: Specific Hytale particle APIs are not yet documented.
 * This class provides the logic framework ready for integration with Hytale's
 * particle system when available.
 */
public class WorldRenderer {
    private int tickCounter = 0;
    
    /**
     * Updates world markers for a player. Should be called every tick.
     *
     * @param playerData The player's waypoint data
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @param playerZ Player's Z position
     */
    public void update(@Nonnull PlayerWaypointData playerData,
                      double playerX, double playerY, double playerZ) {
        
        tickCounter++;
        
        if (!playerData.isWorldMarkersEnabled()) {
            return;
        }
        
        Waypoint active = playerData.getActiveWaypoint();
        if (active == null) {
            return;
        }
        
        // Update every 5 ticks (4 times per second) to reduce particle spam
        if (tickCounter % 5 == 0) {
            renderWaypointMarker(active, playerX, playerY, playerZ);
        }
    }
    
    /**
     * Renders a marker for a waypoint in the world.
     * Placeholder for Hytale particle API integration.
     *
     * @param waypoint The waypoint to mark
     * @param playerX Player's X position
     * @param playerY Player's Y position
     * @param playerZ Player's Z position
     */
    private void renderWaypointMarker(@Nonnull Waypoint waypoint,
                                      double playerX, double playerY, double playerZ) {
        double distance = waypoint.distanceFrom(playerX, playerY, playerZ);
        
        // Don't render if too far away (performance optimization)
        if (distance > 500) {
            return;
        }
        
        // Calculate direction vector
        double[] direction = MathUtils.directionVector(
            playerX, playerY + 1.6, playerZ, // Player eye level
            waypoint.getX(), waypoint.getY(), waypoint.getZ()
        );
        
        // Render particles in a line toward the waypoint
        renderParticleLine(
            playerX, playerY + 1.6, playerZ,
            direction[0], direction[1], direction[2],
            distance
        );
        
        // Render marker at waypoint location
        renderWaypointBeacon(waypoint.getX(), waypoint.getY(), waypoint.getZ());
    }
    
    /**
     * Renders a line of particles pointing toward the waypoint.
     * Placeholder for Hytale particle API integration.
     *
     * @param startX Start X position
     * @param startY Start Y position
     * @param startZ Start Z position
     * @param dirX Direction X component (normalized)
     * @param dirY Direction Y component (normalized)
     * @param dirZ Direction Z component (normalized)
     * @param distance Distance to waypoint
     */
    private void renderParticleLine(double startX, double startY, double startZ,
                                   double dirX, double dirY, double dirZ,
                                   double distance) {
        // Spawn particles in a line from player toward waypoint
        // Particle spacing should be about 2 blocks
        // Limit to first 20 blocks to avoid clutter
        
        int particleCount = Math.min(10, (int)(distance / 2));
        
        for (int i = 1; i <= particleCount; i++) {
            double step = i * 2.0; // 2 blocks apart
            double x = startX + dirX * step;
            double y = startY + dirY * step;
            double z = startZ + dirZ * step;
            
            // Use Hytale's particle system to spawn particle at (x, y, z)
            // Use different particle types based on distance:
            // - Close: Green particles
            // - Medium: Yellow particles
            // - Far: Red particles
            spawnParticle(x, y, z, getParticleType(distance));
        }
    }
    
    /**
     * Renders a beacon effect at the waypoint location.
     * Placeholder for Hytale particle API integration.
     *
     * @param x Waypoint X position
     * @param y Waypoint Y position
     * @param z Waypoint Z position
     */
    private void renderWaypointBeacon(double x, double y, double z) {
        // Create a vertical beam of particles at waypoint location
        // Spiral or circular pattern works well
        
        double angle = (tickCounter * 10) % 360;
        double radians = Math.toRadians(angle);
        double radius = 1.0;
        
        // Create circular particle effect
        for (int i = 0; i < 8; i++) {
            double offsetAngle = radians + (i * Math.PI / 4);
            double offsetX = Math.cos(offsetAngle) * radius;
            double offsetZ = Math.sin(offsetAngle) * radius;
            
            spawnParticle(x + offsetX, y, z + offsetZ, "FLAME");
        }
        
        // Vertical beam
        for (int i = 0; i < 5; i++) {
            double offsetY = y + (i * 0.5);
            spawnParticle(x, offsetY, z, "END_ROD");
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
    private void spawnParticle(double x, double y, double z, @Nonnull String particleType) {
        // Use Hytale's particle system:
        // world.spawnParticle(particleType, x, y, z, count, offsetX, offsetY, offsetZ, speed);
    }
    
    /**
     * Gets the particle type based on distance.
     *
     * @param distance Distance in blocks
     * @return Particle type name
     */
    @Nonnull
    private String getParticleType(double distance) {
        if (distance < 50) {
            return "HAPPY_VILLAGER"; // Green
        } else if (distance < 200) {
            return "FLAME"; // Orange/Yellow
        } else {
            return "LAVA"; // Red
        }
    }
    
    /**
     * Clears all world markers.
     */
    public void clear() {
        // Particles naturally despawn, so nothing to clear
        tickCounter = 0;
    }
}
