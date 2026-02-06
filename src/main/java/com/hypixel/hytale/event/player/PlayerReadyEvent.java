package com.hypixel.hytale.event.player;

import com.hypixel.hytale.entity.PlayerRef;

/**
 * Stub class for Hytale API - PlayerReadyEvent
 * This is a temporary stub until the official Hytale API is released.
 */
public class PlayerReadyEvent {
    private PlayerRef playerRef;
    
    public PlayerReadyEvent(PlayerRef playerRef) {
        this.playerRef = playerRef;
    }
    
    public PlayerRef getPlayerRef() {
        return playerRef;
    }
}
