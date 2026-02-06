package com.waypointnav.plugin.utils;
/**
 * Utility class for creating formatted messages and chat output.
 *
 * Note: Hytale does not support Minecraft-style color codes (§ or &amp; prefixes).
 * All messages use plain text formatting. When Hytale's text styling API becomes
 * available, these constants can be updated to use the native formatting system.
 */
public class MessageUtils {
    
    // Plain text prefixes (Hytale does not support § or & color codes)
    public static final String PREFIX = "[Waypoint] ";
    public static final String ERROR = "[ERROR] ";
    public static final String SUCCESS = "";
    public static final String INFO = "";
    public static final String WARNING = "[!] ";
    public static final String HIGHLIGHT = "";
    public static final String RESET = "";
    
    /**
     * Creates an error message.
     *
     * @param message The error message
     * @return Formatted error message
     */

    public static String error(String message) {
        return PREFIX + ERROR + message + RESET;
    }
    
    /**
     * Creates a success message.
     *
     * @param message The success message
     * @return Formatted success message
     */

    public static String success(String message) {
        return PREFIX + SUCCESS + message + RESET;
    }
    
    /**
     * Creates an info message.
     *
     * @param message The info message
     * @return Formatted info message
     */

    public static String info(String message) {
        return PREFIX + INFO + message + RESET;
    }
    
    /**
     * Creates a warning message.
     *
     * @param message The warning message
     * @return Formatted warning message
     */

    public static String warning(String message) {
        return PREFIX + WARNING + message + RESET;
    }
    
    /**
     * Creates a plain message with prefix.
     *
     * @param message The message
     * @return Formatted message
     */

    public static String plain(String message) {
        return PREFIX + message;
    }
    
    /**
     * Creates a message without prefix.
     *
     * @param message The message
     * @return The message as-is
     */

    public static String raw(String message) {
        return message;
    }
    
    /**
     * Formats coordinates for display.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return Formatted coordinates string
     */

    public static String formatCoordinates(double x, double y, double z) {
        return String.format("%.1f, %.1f, %.1f", x, y, z);
    }
    
    /**
     * Creates a header line for messages.
     *
     * @param title The header title
     * @return Formatted header
     */

    public static String header(String title) {
        return HIGHLIGHT + "=== " + title + " ===" + RESET;
    }
    
    /**
     * Creates a list item message.
     *
     * @param index The item index
     * @param content The item content
     * @return Formatted list item
     */

    public static String listItem(int index, String content) {
        return (index + 1) + ". " + content;
    }
    
    /**
     * Formats a waypoint for display in a list.
     *
     * @param index The waypoint index
     * @param name The waypoint name
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param distance Distance to waypoint
     * @param completed Whether the waypoint is completed
     * @return Formatted waypoint string
     */

    public static String formatWaypointList(int index, String name, 
                                            double x, double y, double z, 
                                            double distance, boolean completed) {
        String status = completed ? SUCCESS + "✓" : WARNING + "○";
        String coords = formatCoordinates(x, y, z);
        String dist = MathUtils.formatDistance(distance);
        
        return String.format("%s %d. %s [%s] (%s)", 
            status, index + 1, name, coords, dist);
    }
}
