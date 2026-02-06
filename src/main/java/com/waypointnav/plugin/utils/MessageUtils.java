package com.waypointnav.plugin.utils;

import javax.annotation.Nonnull;

/**
 * Utility class for creating formatted messages and chat output.
 */
public class MessageUtils {
    
    // Color codes (using standard Minecraft color codes)
    public static final String PREFIX = "§6[§eWaypoint§6]§r ";
    public static final String ERROR = "§c";
    public static final String SUCCESS = "§a";
    public static final String INFO = "§b";
    public static final String WARNING = "§e";
    public static final String HIGHLIGHT = "§6";
    public static final String RESET = "§r";
    
    /**
     * Creates an error message.
     *
     * @param message The error message
     * @return Formatted error message
     */
    @Nonnull
    public static String error(@Nonnull String message) {
        return PREFIX + ERROR + message + RESET;
    }
    
    /**
     * Creates a success message.
     *
     * @param message The success message
     * @return Formatted success message
     */
    @Nonnull
    public static String success(@Nonnull String message) {
        return PREFIX + SUCCESS + message + RESET;
    }
    
    /**
     * Creates an info message.
     *
     * @param message The info message
     * @return Formatted info message
     */
    @Nonnull
    public static String info(@Nonnull String message) {
        return PREFIX + INFO + message + RESET;
    }
    
    /**
     * Creates a warning message.
     *
     * @param message The warning message
     * @return Formatted warning message
     */
    @Nonnull
    public static String warning(@Nonnull String message) {
        return PREFIX + WARNING + message + RESET;
    }
    
    /**
     * Creates a plain message with prefix.
     *
     * @param message The message
     * @return Formatted message
     */
    @Nonnull
    public static String plain(@Nonnull String message) {
        return PREFIX + message;
    }
    
    /**
     * Creates a message without prefix.
     *
     * @param message The message
     * @return The message as-is
     */
    @Nonnull
    public static String raw(@Nonnull String message) {
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
    @Nonnull
    public static String formatCoordinates(double x, double y, double z) {
        return String.format("§e%.1f§7, §e%.1f§7, §e%.1f", x, y, z);
    }
    
    /**
     * Creates a header line for messages.
     *
     * @param title The header title
     * @return Formatted header
     */
    @Nonnull
    public static String header(@Nonnull String title) {
        return HIGHLIGHT + "=== " + title + " ===" + RESET;
    }
    
    /**
     * Creates a list item message.
     *
     * @param index The item index
     * @param content The item content
     * @return Formatted list item
     */
    @Nonnull
    public static String listItem(int index, @Nonnull String content) {
        return INFO + (index + 1) + "§7. " + RESET + content;
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
    @Nonnull
    public static String formatWaypointList(int index, @Nonnull String name, 
                                            double x, double y, double z, 
                                            double distance, boolean completed) {
        String status = completed ? SUCCESS + "✓" : WARNING + "○";
        String coords = formatCoordinates(x, y, z);
        String dist = MathUtils.formatDistance(distance);
        
        return String.format("%s §7%d. %s%s §7[%s] §8(%s)", 
            status, index + 1, RESET, name, coords, dist);
    }
}
