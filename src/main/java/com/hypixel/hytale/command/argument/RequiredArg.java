package com.hypixel.hytale.command.argument;

import com.hypixel.hytale.command.CommandContext;

/**
 * Stub class for Hytale API - RequiredArg
 * This is a temporary stub until the official Hytale API is released.
 */
public class RequiredArg<T> {
    private String name;
    private String description;
    
    public RequiredArg(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public T get(CommandContext ctx) {
        return null;
    }
}
