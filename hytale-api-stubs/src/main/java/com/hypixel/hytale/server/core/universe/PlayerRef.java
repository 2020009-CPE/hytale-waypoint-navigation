package com.hypixel.hytale.server.core.universe;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.Message;

import java.util.UUID;

public class PlayerRef {
    private static final ComponentType<PlayerRef> COMPONENT_TYPE = new ComponentType<>();

    public static ComponentType<PlayerRef> getComponentType() {
        return COMPONENT_TYPE;
    }

    public UUID getUuid() {
        return null;
    }

    public void sendMessage(Message message) {
    }

    public Transform getTransform() {
        return null;
    }

    public Object getPacketHandler() {
        return null;
    }
}
