package com.hypixel.hytale.command.argument.type;

import com.hypixel.hytale.command.argument.OptionalArg;
import com.hypixel.hytale.command.argument.RequiredArg;

/**
 * Stub class for Hytale API - ArgTypes
 * This is a temporary stub until the official Hytale API is released.
 */
public class ArgTypes {
    public static final Object STRING = new Object();
    public static final Object INTEGER = new Object();
    public static final Object FLOAT = new Object();
    public static final Object DOUBLE = new Object();
    public static final Object BOOLEAN = new Object();
    
    public static <T> RequiredArg<T> withRequiredArg(String name, String description, Object type) {
        return new RequiredArg<>(name, description);
    }
    
    public static <T> OptionalArg<T> withOptionalArg(String name, String description, Object type) {
        return new OptionalArg<>(name, description, null);
    }
}
