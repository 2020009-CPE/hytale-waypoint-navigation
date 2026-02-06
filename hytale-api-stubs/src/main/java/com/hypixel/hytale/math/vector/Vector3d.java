package com.hypixel.hytale.math.vector;

public class Vector3d {
    private final double x;
    private final double y;
    private final double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}
