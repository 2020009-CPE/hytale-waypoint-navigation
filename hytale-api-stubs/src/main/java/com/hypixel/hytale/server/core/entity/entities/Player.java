package com.hypixel.hytale.server.core.entity.entities;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;

public class Player {
    private static final ComponentType<Player> COMPONENT_TYPE = new ComponentType<>();

    public static ComponentType<Player> getComponentType() {
        return COMPONENT_TYPE;
    }

    public PageManager getPageManager() {
        return null;
    }
}
