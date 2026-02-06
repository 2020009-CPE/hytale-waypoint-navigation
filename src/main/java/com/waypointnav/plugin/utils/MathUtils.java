package com.waypointnav.plugin.utils;
/**
 * Utility class for mathematical calculations used in waypoint navigation.
 */
public class MathUtils {
    
    /**
     * Calculates the Euclidean distance between two 3D points.
     *
     * @param x1 First point X coordinate
     * @param y1 First point Y coordinate
     * @param z1 First point Z coordinate
     * @param x2 Second point X coordinate
     * @param y2 Second point Y coordinate
     * @param z2 Second point Z coordinate
     * @return The distance between the points
     */
    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Calculates the horizontal (XZ plane) distance between two points.
     *
     * @param x1 First point X coordinate
     * @param z1 First point Z coordinate
     * @param x2 Second point X coordinate
     * @param z2 Second point Z coordinate
     * @return The horizontal distance
     */
    public static double horizontalDistance(double x1, double z1, double x2, double z2) {
        double dx = x2 - x1;
        double dz = z2 - z1;
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    /**
     * Calculates the direction vector from source to target.
     *
     * @param srcX Source X coordinate
     * @param srcY Source Y coordinate
     * @param srcZ Source Z coordinate
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @param targetZ Target Z coordinate
     * @return Array containing normalized direction vector [x, y, z]
     */

    public static double[] directionVector(double srcX, double srcY, double srcZ,
                                          double targetX, double targetY, double targetZ) {
        double dx = targetX - srcX;
        double dy = targetY - srcY;
        double dz = targetZ - srcZ;
        
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        
        if (length < 0.0001) {
            return new double[]{0, 0, 0};
        }
        
        return new double[]{dx / length, dy / length, dz / length};
    }
    
    /**
     * Calculates the yaw angle (horizontal rotation) from source to target.
     *
     * @param srcX Source X coordinate
     * @param srcZ Source Z coordinate
     * @param targetX Target X coordinate
     * @param targetZ Target Z coordinate
     * @return The yaw angle in degrees
     */
    public static double calculateYaw(double srcX, double srcZ, double targetX, double targetZ) {
        double dx = targetX - srcX;
        double dz = targetZ - srcZ;
        
        double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90;
        
        if (yaw < 0) {
            yaw += 360;
        }
        
        return yaw;
    }
    
    /**
     * Calculates the pitch angle (vertical rotation) from source to target.
     *
     * @param srcX Source X coordinate
     * @param srcY Source Y coordinate
     * @param srcZ Source Z coordinate
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @param targetZ Target Z coordinate
     * @return The pitch angle in degrees
     */
    public static double calculatePitch(double srcX, double srcY, double srcZ,
                                       double targetX, double targetY, double targetZ) {
        double dx = targetX - srcX;
        double dy = targetY - srcY;
        double dz = targetZ - srcZ;
        
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        
        return -Math.toDegrees(Math.atan2(dy, horizontalDist));
    }
    
    /**
     * Normalizes an angle to be within 0-360 degrees.
     *
     * @param angle The angle to normalize
     * @return The normalized angle
     */
    public static double normalizeAngle(double angle) {
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
    
    /**
     * Calculates the difference between two angles, accounting for wraparound.
     *
     * @param angle1 First angle in degrees
     * @param angle2 Second angle in degrees
     * @return The angular difference (-180 to 180)
     */
    public static double angleDifference(double angle1, double angle2) {
        double diff = angle2 - angle1;
        diff = ((diff + 180) % 360) - 180;
        return diff < -180 ? diff + 360 : diff;
    }
    
    /**
     * Formats a distance value for display.
     *
     * @param distance The distance in blocks
     * @return Formatted distance string
     */

    public static String formatDistance(double distance) {
        if (distance < 1000) {
            return String.format("%.1fm", distance);
        } else {
            return String.format("%.2fkm", distance / 1000.0);
        }
    }
}
