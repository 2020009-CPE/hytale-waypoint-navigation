package com.hypixel.hytale.server.core.universe;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.hypixel.hytale.server.core.universe.world.World;

/**
 * Stub for Hytale's Universe singleton.
 * Provides access to all online players and worlds.
 */
public class Universe {
    private static final Universe INSTANCE = new Universe();

    public static Universe get() {
        return INSTANCE;
    }

    public Collection<PlayerRef> getPlayers() {
        return Collections.emptyList();
    }

    public World getWorld(UUID worldUuid) {
        return null;
    }
}
