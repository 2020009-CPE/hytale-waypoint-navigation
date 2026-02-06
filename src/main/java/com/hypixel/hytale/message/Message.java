package com.hypixel.hytale.message;

/**
 * Stub class for Hytale API - Message
 * This is a temporary stub until the official Hytale API is released.
 */
public class Message {
    private String text;
    
    private Message(String text) {
        this.text = text;
    }
    
    public static Message raw(String text) {
        return new Message(text);
    }
    
    public String getText() {
        return text;
    }
}
