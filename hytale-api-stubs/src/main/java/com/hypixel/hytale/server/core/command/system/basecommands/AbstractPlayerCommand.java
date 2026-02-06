package com.hypixel.hytale.server.core.command.system.basecommands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgType;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public abstract class AbstractPlayerCommand {

    public AbstractPlayerCommand(String name, String description) {
    }

    protected abstract void execute(CommandContext ctx,
                                    Store<EntityStore> store,
                                    Ref<EntityStore> ref,
                                    PlayerRef playerRef,
                                    World world);

    protected <T> RequiredArg<T> withRequiredArg(String name, String description, ArgType<T> type) {
        return new RequiredArg<>();
    }

    protected <T> OptionalArg<T> withOptionalArg(String name, String description, ArgType<T> type) {
        return new OptionalArg<>();
    }
}
