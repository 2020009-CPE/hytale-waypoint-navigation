package com.waypointnav.plugin.waypoint;

/**
 * Enum representing the different types of waypoints that can be created.
 * Each type defines a specific method of determining waypoint completion.
 */
public enum WaypointType {
    /**
     * Simple coordinate-based waypoint.
     * Completed when player reaches specified coordinates within radius.
     */
    BLOCK_COORDINATES("Block Coordinates"),
    
    /**
     * NPC entity-based waypoint.
     * Completed when player reaches specified NPC entity.
     */
    NPC_ENTITY("NPC Entity"),
    
    /**
     * Specific block type waypoint.
     * Completed when player reaches a block of specified type.
     */
    SPECIFIC_BLOCK("Specific Block"),
    
    /**
     * Custom objective waypoint.
     * Completed based on custom conditions (extensible).
     */
    CUSTOM_OBJECTIVE("Custom Objective"),
    
    /**
     * User-defined waypoint.
     * Basic waypoint created by player with custom name and position.
     */
    USER_DEFINED("User Defined");
    
    private final String displayName;
    
    WaypointType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the human-readable display name of this waypoint type.
     *
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
