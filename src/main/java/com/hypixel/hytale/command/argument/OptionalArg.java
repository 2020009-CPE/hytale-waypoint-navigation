package com.hypixel.hytale.command.argument;

import com.hypixel.hytale.command.CommandContext;

/**
 * Stub class for Hytale API - OptionalArg
 * This is a temporary stub until the official Hytale API is released.
 */
public class OptionalArg<T> {
    private String name;
    private String description;
    private T defaultValue;
    
    public OptionalArg(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }
    
    public String getName() {
        return name;
    }
    
    public T getDefaultValue() {
        return defaultValue;
    }
    
    public T get(CommandContext ctx) {
        return defaultValue;
    }
}
