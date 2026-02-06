package com.waypointnav.plugin.waypoint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central manager for all waypoints in the system.
 * Handles waypoint creation, deletion, and global waypoint operations.
 */
public class WaypointManager {
    private final Map<UUID, Waypoint> waypoints;
    private final Map<String, List<UUID>> playerWaypoints;
    
    public WaypointManager() {
        this.waypoints = new ConcurrentHashMap<>();
        this.playerWaypoints = new ConcurrentHashMap<>();
    }
    
    /**
     * Registers a waypoint in the system.
     *
     * @param waypoint The waypoint to register
     */
    public void registerWaypoint(@Nonnull Waypoint waypoint) {
        waypoints.put(waypoint.getId(), waypoint);
    }
    
    /**
     * Unregisters a waypoint from the system.
     *
     * @param waypointId The ID of the waypoint to unregister
     * @return true if the waypoint was removed, false otherwise
     */
    public boolean unregisterWaypoint(@Nonnull UUID waypointId) {
        return waypoints.remove(waypointId) != null;
    }
    
    /**
     * Gets a waypoint by its ID.
     *
     * @param id The waypoint ID
     * @return The waypoint, or null if not found
     */
    @Nullable
    public Waypoint getWaypoint(@Nonnull UUID id) {
        return waypoints.get(id);
    }
    
    /**
     * Gets all waypoints registered in the system.
     *
     * @return An unmodifiable collection of all waypoints
     */
    @Nonnull
    public Collection<Waypoint> getAllWaypoints() {
        return Collections.unmodifiableCollection(waypoints.values());
    }
    
    /**
     * Creates a new waypoint for a player.
     *
     * @param playerUuid The player's UUID
     * @param name The waypoint name
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param type The waypoint type
     * @return The created waypoint
     */
    @Nonnull
    public Waypoint createWaypoint(@Nonnull UUID playerUuid, @Nonnull String name, 
                                   double x, double y, double z, @Nonnull WaypointType type) {
        Waypoint waypoint = new Waypoint(name, x, y, z, type);
        
        List<UUID> playerWaypointList = playerWaypoints.computeIfAbsent(
            playerUuid.toString(), k -> new ArrayList<>()
        );
        
        waypoint.setOrder(playerWaypointList.size());
        registerWaypoint(waypoint);
        playerWaypointList.add(waypoint.getId());
        
        return waypoint;
    }
    
    /**
     * Gets all waypoints for a specific player.
     *
     * @param playerUuid The player's UUID
     * @return List of waypoint IDs for the player
     */
    @Nonnull
    public List<UUID> getPlayerWaypoints(@Nonnull UUID playerUuid) {
        return playerWaypoints.getOrDefault(playerUuid.toString(), new ArrayList<>());
    }
    
    /**
     * Clears all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     */
    public void clearPlayerWaypoints(@Nonnull UUID playerUuid) {
        List<UUID> waypointIds = playerWaypoints.remove(playerUuid.toString());
        if (waypointIds != null) {
            waypointIds.forEach(waypoints::remove);
        }
    }
    
    /**
     * Removes a specific waypoint from a player's list.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID to remove
     * @return true if removed successfully
     */
    public boolean removePlayerWaypoint(@Nonnull UUID playerUuid, @Nonnull UUID waypointId) {
        List<UUID> waypointIds = playerWaypoints.get(playerUuid.toString());
        if (waypointIds != null && waypointIds.remove(waypointId)) {
            waypoints.remove(waypointId);
            // Reorder remaining waypoints
            reorderPlayerWaypoints(playerUuid);
            return true;
        }
        return false;
    }
    
    /**
     * Reorders waypoints for a player based on their current list order.
     *
     * @param playerUuid The player's UUID
     */
    private void reorderPlayerWaypoints(@Nonnull UUID playerUuid) {
        List<UUID> waypointIds = playerWaypoints.get(playerUuid.toString());
        if (waypointIds != null) {
            for (int i = 0; i < waypointIds.size(); i++) {
                Waypoint wp = waypoints.get(waypointIds.get(i));
                if (wp != null) {
                    wp.setOrder(i);
                }
            }
        }
    }
}
