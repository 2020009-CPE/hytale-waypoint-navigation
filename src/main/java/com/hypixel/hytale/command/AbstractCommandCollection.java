package com.hypixel.hytale.command;

/**
 * Stub class for Hytale API - AbstractCommandCollection
 * This is a temporary stub until the official Hytale API is released.
 */
public abstract class AbstractCommandCollection {
    protected String name;
    protected String description;
    
    public AbstractCommandCollection(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    protected void addSubCommand(Object command) {
        // Stub method
    }
}
