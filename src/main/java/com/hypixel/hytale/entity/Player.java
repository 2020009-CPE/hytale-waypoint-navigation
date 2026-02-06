package com.hypixel.hytale.entity;

import com.hypixel.hytale.message.Message;
import com.hypixel.hytale.store.ComponentType;

import java.util.UUID;

/**
 * Stub class for Hytale API - Player
 * This is a temporary stub until the official Hytale API is released.
 */
public class Player {
    private UUID uuid;
    
    public Player(UUID uuid) {
        this.uuid = uuid;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public void sendMessage(Message message) {
        // Stub method
    }
    
    public static ComponentType<Player> getComponentType() {
        return new ComponentType<>();
    }
}
