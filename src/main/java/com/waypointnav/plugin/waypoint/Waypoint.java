package com.waypointnav.plugin.waypoint;

import java.util.UUID;

/**
 * Represents a single waypoint in the navigation system.
 * Contains all information needed to track and navigate to a specific location.
 */
public class Waypoint {
    private final UUID id;
    private String name;
    private double x;
    private double y;
    private double z;
    private double collectionRadius;
    private WaypointType type;
    private int order;
    private boolean completed;
    private String world;
    
    /**
     * Creates a new waypoint with the specified properties.
     *
     * @param id The unique identifier for this waypoint
     * @param name The display name of the waypoint
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param collectionRadius The radius within which the waypoint is considered reached
     * @param type The type of waypoint
     * @param order The order in the waypoint sequence
     */
    public Waypoint(UUID id, String name, double x, double y, double z, 
                    double collectionRadius, WaypointType type, int order) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.collectionRadius = collectionRadius;
        this.type = type;
        this.order = order;
        this.completed = false;
        this.world = "world"; // Default world
    }
    
    /**
     * Creates a new waypoint with default collection radius.
     *
     * @param name The display name
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param type The waypoint type
     */
    public Waypoint(String name, double x, double y, double z, WaypointType type) {
        this(UUID.randomUUID(), name, x, y, z, 5.0, type, 0);
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getCollectionRadius() { return collectionRadius; }
    public WaypointType getType() { return type; }
    public int getOrder() { return order; }
    public boolean isCompleted() { return completed; }
    public String getWorld() { return world; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }
    public void setCollectionRadius(double radius) { this.collectionRadius = radius; }
    public void setType(WaypointType type) { this.type = type; }
    public void setOrder(int order) { this.order = order; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setWorld(String world) { this.world = world; }
    
    /**
     * Checks if a position is within the collection radius of this waypoint.
     *
     * @param px Player X coordinate
     * @param py Player Y coordinate
     * @param pz Player Z coordinate
     * @return true if the position is within collection radius
     */
    public boolean isWithinRadius(double px, double py, double pz) {
        double dx = x - px;
        double dy = y - py;
        double dz = z - pz;
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        return distance <= collectionRadius;
    }
    
    /**
     * Calculates the distance from a given position to this waypoint.
     *
     * @param px Player X coordinate
     * @param py Player Y coordinate
     * @param pz Player Z coordinate
     * @return The distance in blocks
     */
    public double distanceFrom(double px, double py, double pz) {
        double dx = x - px;
        double dy = y - py;
        double dz = z - pz;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.1f, %.1f, %.1f) [%s]", 
            name, x, y, z, type.getDisplayName());
    }
}
