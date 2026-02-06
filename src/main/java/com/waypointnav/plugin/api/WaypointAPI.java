package com.waypointnav.plugin.api;

import com.waypointnav.plugin.player.PlayerWaypointData;
import com.waypointnav.plugin.waypoint.Waypoint;
import com.waypointnav.plugin.waypoint.WaypointType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    @Nonnull
    Waypoint createWaypoint(@Nonnull UUID playerUuid, @Nonnull String name,
                           double x, double y, double z, @Nonnull WaypointType type);
    
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
    @Nonnull
    Waypoint createWaypoint(@Nonnull UUID playerUuid, @Nonnull String name,
                           double x, double y, double z, 
                           @Nonnull WaypointType type, double radius);
    
    /**
     * Removes a waypoint for a player.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID to remove
     * @return true if removed successfully
     */
    boolean removeWaypoint(@Nonnull UUID playerUuid, @Nonnull UUID waypointId);
    
    /**
     * Gets all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     * @return List of waypoints
     */
    @Nonnull
    List<Waypoint> getPlayerWaypoints(@Nonnull UUID playerUuid);
    
    /**
     * Gets the active waypoint for a player.
     *
     * @param playerUuid The player's UUID
     * @return The active waypoint, or null if none
     */
    @Nullable
    Waypoint getActiveWaypoint(@Nonnull UUID playerUuid);
    
    /**
     * Sets the active waypoint by index.
     *
     * @param playerUuid The player's UUID
     * @param index The waypoint index
     * @return true if set successfully
     */
    boolean setActiveWaypoint(@Nonnull UUID playerUuid, int index);
    
    /**
     * Advances to the next waypoint.
     *
     * @param playerUuid The player's UUID
     * @return true if advanced successfully
     */
    boolean nextWaypoint(@Nonnull UUID playerUuid);
    
    /**
     * Marks a waypoint as completed.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID
     */
    void completeWaypoint(@Nonnull UUID playerUuid, @Nonnull UUID waypointId);
    
    /**
     * Checks if a waypoint is completed.
     *
     * @param playerUuid The player's UUID
     * @param waypointId The waypoint ID
     * @return true if completed
     */
    boolean isWaypointCompleted(@Nonnull UUID playerUuid, @Nonnull UUID waypointId);
    
    /**
     * Clears all waypoints for a player.
     *
     * @param playerUuid The player's UUID
     */
    void clearWaypoints(@Nonnull UUID playerUuid);
    
    /**
     * Gets player waypoint data.
     *
     * @param playerUuid The player's UUID
     * @return The player's waypoint data, or null if not found
     */
    @Nullable
    PlayerWaypointData getPlayerData(@Nonnull UUID playerUuid);
    
    /**
     * Toggles navigation for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether navigation should be enabled
     */
    void setNavigationEnabled(@Nonnull UUID playerUuid, boolean enabled);
    
    /**
     * Toggles HUD display for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether HUD should be enabled
     */
    void setHudEnabled(@Nonnull UUID playerUuid, boolean enabled);
    
    /**
     * Toggles world markers for a player.
     *
     * @param playerUuid The player's UUID
     * @param enabled Whether world markers should be enabled
     */
    void setWorldMarkersEnabled(@Nonnull UUID playerUuid, boolean enabled);
}
