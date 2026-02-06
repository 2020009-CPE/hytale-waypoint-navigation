package com.waypointnav.plugin.waypoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central manager for all waypoints in the system.
 * Handles waypoint creation, deletion, and global waypoint operations.
 *
 * Supports a global waypoint list that is shared across all players.
 * When an admin creates a waypoint, it is added to the global list and
 * automatically synced to all online players.
 */
public class WaypointManager {
    private final Map<UUID, Waypoint> waypoints;
    private final Map<String, List<UUID>> playerWaypoints;

    /** Global waypoint list shared across all players (for guided tours). */
    private final List<Waypoint> globalWaypoints;

    public WaypointManager() {
        this.waypoints = new ConcurrentHashMap<>();
        this.playerWaypoints = new ConcurrentHashMap<>();
        this.globalWaypoints = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Registers a waypoint in the system.
     *
     * @param waypoint The waypoint to register
     */
    public void registerWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.getId(), waypoint);
    }
    
    /**
     * Unregisters a waypoint from the system.
     *
     * @param waypointId The ID of the waypoint to unregister
     * @return true if the waypoint was removed, false otherwise
     */
    public boolean unregisterWaypoint(UUID waypointId) {
        return waypoints.remove(waypointId) != null;
    }
    
    /**
     * Gets a waypoint by its ID.
     *
     * @param id The waypoint ID
     * @return The waypoint, or null if not found
     */

    public Waypoint getWaypoint(UUID id) {
        return waypoints.get(id);
    }
    
    /**
     * Gets all waypoints registered in the system.
     *
     * @return An unmodifiable collection of all waypoints
     */

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

    public Waypoint createWaypoint(UUID playerUuid, String name, 
                                   double x, double y, double z, WaypointType type) {
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

    public List<UUID> getPlayerWaypoints(UUID playerUuid) {
        return playerWaypoints.getOrDefault(playerUuid.toString(), new ArrayList<>());
    }
    
    /**
     * Clears all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     */
    public void clearPlayerWaypoints(UUID playerUuid) {
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
    public boolean removePlayerWaypoint(UUID playerUuid, UUID waypointId) {
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
    private void reorderPlayerWaypoints(UUID playerUuid) {
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

    // ===== Global Waypoint Methods =====

    /**
     * Gets the global waypoint list.
     *
     * @return Unmodifiable list of global waypoints
     */
    public List<Waypoint> getGlobalWaypoints() {
        return Collections.unmodifiableList(globalWaypoints);
    }

    /**
     * Adds a waypoint to the global list.
     *
     * @param waypoint The waypoint to add globally
     */
    public void addGlobalWaypoint(Waypoint waypoint) {
        waypoint.setOrder(globalWaypoints.size());
        globalWaypoints.add(waypoint);
        registerWaypoint(waypoint);
    }

    /**
     * Removes a waypoint from the global list by index.
     *
     * @param index The index to remove
     * @return The removed waypoint, or null if index is invalid
     */
    public Waypoint removeGlobalWaypoint(int index) {
        if (index < 0 || index >= globalWaypoints.size()) {
            return null;
        }
        Waypoint removed = globalWaypoints.remove(index);
        if (removed != null) {
            unregisterWaypoint(removed.getId());
            // Reorder remaining global waypoints
            for (int i = 0; i < globalWaypoints.size(); i++) {
                globalWaypoints.get(i).setOrder(i);
            }
        }
        return removed;
    }

    /**
     * Clears all global waypoints.
     */
    public void clearGlobalWaypoints() {
        for (Waypoint wp : globalWaypoints) {
            unregisterWaypoint(wp.getId());
        }
        globalWaypoints.clear();
    }

    /**
     * Sets the global waypoint list (used when loading from storage).
     *
     * @param waypoints The list of waypoints to set as global
     */
    public void setGlobalWaypoints(List<Waypoint> waypoints) {
        globalWaypoints.clear();
        for (Waypoint wp : waypoints) {
            wp.setOrder(globalWaypoints.size());
            globalWaypoints.add(wp);
            registerWaypoint(wp);
        }
    }
}
