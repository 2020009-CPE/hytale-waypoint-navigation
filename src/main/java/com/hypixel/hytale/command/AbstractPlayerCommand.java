package com.hypixel.hytale.command;

import com.hypixel.hytale.command.argument.OptionalArg;
import com.hypixel.hytale.command.argument.RequiredArg;
import com.hypixel.hytale.entity.PlayerRef;
import com.hypixel.hytale.permission.GameMode;
import com.hypixel.hytale.store.EntityStore;
import com.hypixel.hytale.store.Ref;
import com.hypixel.hytale.store.Store;
import com.hypixel.hytale.world.World;

/**
 * Stub class for Hytale API - AbstractPlayerCommand
 * This is a temporary stub until the official Hytale API is released.
 */
public abstract class AbstractPlayerCommand {
    protected String name;
    protected String description;
    
    public AbstractPlayerCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    protected void setPermissionGroup(GameMode mode) {
        // Stub method
    }
    
    protected <T> RequiredArg<T> withRequiredArg(String name, String description, Object type) {
        return new RequiredArg<>(name, description);
    }
    
    protected <T> OptionalArg<T> withOptionalArg(String name, String description, Object type) {
        return new OptionalArg<>(name, description, null);
    }
    
    protected abstract void execute(CommandContext ctx,
                                   Store<EntityStore> store,
                                   Ref<EntityStore> ref,
                                   PlayerRef playerRef,
                                   World world);
}
