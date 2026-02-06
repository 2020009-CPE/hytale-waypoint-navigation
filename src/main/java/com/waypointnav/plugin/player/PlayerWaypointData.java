package com.waypointnav.plugin.player;

import com.waypointnav.plugin.waypoint.Waypoint;

import java.util.*;

/**
 * Stores waypoint navigation data for a specific player.
 * Tracks waypoint progress, active waypoint, and player-specific settings.
 */
public class PlayerWaypointData {
    private final UUID playerUuid;
    private final List<Waypoint> waypoints;
    private final Set<UUID> completedWaypoints;
    private int activeWaypointIndex;
    
    // Player settings
    private boolean navigationEnabled;
    private boolean hudEnabled;
    private boolean worldMarkersEnabled;
    
    /**
     * Creates new player waypoint data.
     *
     * @param playerUuid The player's UUID
     */
    public PlayerWaypointData(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.waypoints = new ArrayList<>();
        this.completedWaypoints = new HashSet<>();
        this.activeWaypointIndex = 0;
        this.navigationEnabled = true;
        this.hudEnabled = true;
        this.worldMarkersEnabled = true;
    }
    
    /**
     * Gets the player's UUID.
     *
     * @return The player UUID
     */

    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    /**
     * Gets all waypoints for this player.
     *
     * @return Unmodifiable list of waypoints
     */

    public List<Waypoint> getWaypoints() {
        return Collections.unmodifiableList(waypoints);
    }
    
    /**
     * Adds a waypoint to the player's list.
     *
     * @param waypoint The waypoint to add
     */
    public void addWaypoint(Waypoint waypoint) {
        waypoint.setOrder(waypoints.size());
        waypoints.add(waypoint);
    }
    
    /**
     * Removes a waypoint from the player's list.
     *
     * @param waypoint The waypoint to remove
     * @return true if removed successfully
     */
    public boolean removeWaypoint(Waypoint waypoint) {
        boolean removed = waypoints.remove(waypoint);
        if (removed) {
            completedWaypoints.remove(waypoint.getId());
            // Reorder waypoints
            for (int i = 0; i < waypoints.size(); i++) {
                waypoints.get(i).setOrder(i);
            }
            // Adjust active index if needed
            if (activeWaypointIndex >= waypoints.size() && !waypoints.isEmpty()) {
                activeWaypointIndex = waypoints.size() - 1;
            }
        }
        return removed;
    }
    
    /**
     * Clears all waypoints for this player.
     */
    public void clearWaypoints() {
        waypoints.clear();
        completedWaypoints.clear();
        activeWaypointIndex = 0;
    }
    
    /**
     * Gets the currently active waypoint.
     *
     * @return The active waypoint, or null if none
     */

    public Waypoint getActiveWaypoint() {
        if (waypoints.isEmpty() || activeWaypointIndex >= waypoints.size()) {
            return null;
        }
        return waypoints.get(activeWaypointIndex);
    }
    
    /**
     * Gets the index of the active waypoint.
     *
     * @return The active waypoint index
     */
    public int getActiveWaypointIndex() {
        return activeWaypointIndex;
    }
    
    /**
     * Sets the active waypoint by index.
     *
     * @param index The index to set
     */
    public void setActiveWaypointIndex(int index) {
        if (index >= 0 && index < waypoints.size()) {
            this.activeWaypointIndex = index;
        }
    }
    
    /**
     * Advances to the next waypoint.
     *
     * @return true if advanced, false if already at last waypoint
     */
    public boolean nextWaypoint() {
        if (activeWaypointIndex < waypoints.size() - 1) {
            activeWaypointIndex++;
            return true;
        }
        return false;
    }
    
    /**
     * Marks a waypoint as completed.
     *
     * @param waypointId The waypoint ID
     */
    public void completeWaypoint(UUID waypointId) {
        completedWaypoints.add(waypointId);
        
        // Find and mark waypoint as completed
        waypoints.stream()
            .filter(wp -> wp.getId().equals(waypointId))
            .findFirst()
            .ifPresent(wp -> wp.setCompleted(true));
    }
    
    /**
     * Checks if a waypoint is completed.
     *
     * @param waypointId The waypoint ID
     * @return true if completed
     */
    public boolean isWaypointCompleted(UUID waypointId) {
        return completedWaypoints.contains(waypointId);
    }
    
    /**
     * Gets all completed waypoint IDs.
     *
     * @return Unmodifiable set of completed waypoint IDs
     */

    public Set<UUID> getCompletedWaypoints() {
        return Collections.unmodifiableSet(completedWaypoints);
    }
    
    /**
     * Skips the current waypoint and advances to the next.
     *
     * @return true if skipped successfully
     */
    public boolean skipCurrentWaypoint() {
        if (waypoints.isEmpty()) {
            return false;
        }
        
        Waypoint current = getActiveWaypoint();
        if (current != null) {
            completeWaypoint(current.getId());
        }
        
        return nextWaypoint();
    }
    
    /**
     * Skips all remaining waypoints.
     */
    public void skipAllWaypoints() {
        waypoints.forEach(wp -> {
            completeWaypoint(wp.getId());
            wp.setCompleted(true);
        });
        activeWaypointIndex = waypoints.size() - 1;
    }
    
    // Settings getters and setters
    public boolean isNavigationEnabled() { return navigationEnabled; }
    public void setNavigationEnabled(boolean enabled) { this.navigationEnabled = enabled; }
    
    public boolean isHudEnabled() { return hudEnabled; }
    public void setHudEnabled(boolean enabled) { this.hudEnabled = enabled; }
    
    public boolean isWorldMarkersEnabled() { return worldMarkersEnabled; }
    public void setWorldMarkersEnabled(boolean enabled) { this.worldMarkersEnabled = enabled; }
}
