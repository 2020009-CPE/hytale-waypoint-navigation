package com.hypixel.hytale.math;

/**
 * Stub class for Hytale API - Vec3d
 * This is a temporary stub until the official Hytale API is released.
 */
public class Vec3d {
    private double x;
    private double y;
    private double z;
    
    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getZ() {
        return z;
    }
    
    public double distanceTo(Vec3d other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
