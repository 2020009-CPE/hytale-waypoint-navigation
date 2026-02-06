package com.waypointnav.plugin.player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages player waypoint data for all online players.
 * Provides fast access to player-specific waypoint information.
 */
public class PlayerDataManager {
    private final Map<UUID, PlayerWaypointData> playerData;
    
    public PlayerDataManager() {
        this.playerData = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets or creates player waypoint data.
     *
     * @param playerUuid The player's UUID
     * @return The player's waypoint data
     */

    public PlayerWaypointData getOrCreatePlayerData(UUID playerUuid) {
        return playerData.computeIfAbsent(playerUuid, PlayerWaypointData::new);
    }
    
    /**
     * Gets player waypoint data if it exists.
     *
     * @param playerUuid The player's UUID
     * @return The player's waypoint data, or null if not found
     */

    public PlayerWaypointData getPlayerData(UUID playerUuid) {
        return playerData.get(playerUuid);
    }
    
    /**
     * Checks if player data exists.
     *
     * @param playerUuid The player's UUID
     * @return true if data exists
     */
    public boolean hasPlayerData(UUID playerUuid) {
        return playerData.containsKey(playerUuid);
    }
    
    /**
     * Removes player data.
     *
     * @param playerUuid The player's UUID
     * @return The removed data, or null if none existed
     */

    public PlayerWaypointData removePlayerData(UUID playerUuid) {
        return playerData.remove(playerUuid);
    }
    
    /**
     * Clears all player data.
     */
    public void clearAll() {
        playerData.clear();
    }
    
    /**
     * Gets the number of players with data.
     *
     * @return The count of players
     */
    public int getPlayerCount() {
        return playerData.size();
    }
}
