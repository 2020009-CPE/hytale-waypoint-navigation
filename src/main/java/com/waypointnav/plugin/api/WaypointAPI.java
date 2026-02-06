package com.waypointnav.plugin.api;

import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;

import java.util.List;
import java.util.UUID;

/**
 * Public API interface for other plugins to interact with the Waypoint Navigation system.
 * Provides methods to create, manage, and query waypoints programmatically.
 */
public interface WaypointAPI {
    
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

    Waypoint createWaypoint(UUID playerUuid, String name,
                           double x, double y, double z, WaypointType type);
    
    /**
     * Creates a waypoint with custom collection radius.
     *
     * @param playerUuid The player's UUID
     * @param name The waypoint name
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param type The waypoint type
     * @param radius Collection radius
     * @return The created waypoint
     */

    Waypoint createWaypoint(UUID playerUuid, String name,
                           double x, double y, double z, 
                           WaypointType type, double radius);
    
    /**
     * Removes a waypoint for a player.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID to remove
     * @return true if removed successfully
     */
    boolean removeWaypoint(UUID playerUuid, UUID waypointId);
    
    /**
     * Gets all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     * @return List of waypoints
     */

    List<Waypoint> getPlayerWaypoints(UUID playerUuid);
    
    /**
     * Gets the active waypoint for a player.
     *
     * @param playerUuid The player's UUID
     * @return The active waypoint, or null if none
     */

    Waypoint getActiveWaypoint(UUID playerUuid);
    
    /**
     * Sets the active waypoint by index.
     *
     * @param playerUuid The player's UUID
     * @param index The waypoint index
     * @return true if set successfully
     */
    boolean setActiveWaypoint(UUID playerUuid, int index);
    
    /**
     * Advances to the next waypoint.
     *
     * @param playerUuid The player's UUID
     * @return true if advanced successfully
     */
    boolean nextWaypoint(UUID playerUuid);
    
    /**
     * Marks a waypoint as completed.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID
     */
    void completeWaypoint(UUID playerUuid, UUID waypointId);
    
    /**
     * Checks if a waypoint is completed.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID
     * @return true if completed
     */
    boolean isWaypointCompleted(UUID playerUuid, UUID waypointId);
    
    /**
     * Clears all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     */
    void clearWaypoints(UUID playerUuid);
    
    /**
     * Gets player waypoint data.
     *
     * @param playerUuid The player's UUID
     * @return The player's waypoint data, or null if not found
     */

    PlayerWaypointData getPlayerData(UUID playerUuid);
    
    /**
     * Toggles navigation for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether navigation should be enabled
     */
    void setNavigationEnabled(UUID playerUuid, boolean enabled);
    
    /**
     * Toggles HUD display for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether HUD should be enabled
     */
    void setHudEnabled(UUID playerUuid, boolean enabled);
    
    /**
     * Toggles world markers for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether world markers should be enabled
     */
    void setWorldMarkersEnabled(UUID playerUuid, boolean enabled);
}
