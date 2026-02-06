package com.hypixel.hytale.event.player;

import com.hypixel.hytale.entity.PlayerRef;

/**
 * Stub class for Hytale API - PlayerDisconnectEvent
 * This is a temporary stub until the official Hytale API is released.
 */
public class PlayerDisconnectEvent {
    private PlayerRef playerRef;
    
    public PlayerDisconnectEvent(PlayerRef playerRef) {
        this.playerRef = playerRef;
    }
    
    public PlayerRef getPlayerRef() {
        return playerRef;
    }
}
