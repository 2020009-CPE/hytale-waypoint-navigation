package com.hypixel.hytale.server.core.entity.entities.player.pages;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public abstract class InteractiveCustomUIPage<T> {

    public InteractiveCustomUIPage(PlayerRef playerRef, CustomPageLifetime lifetime, BuilderCodec<T> codec) {
    }

    public abstract void build(Ref<EntityStore> ref, UICommandBuilder cmd, UIEventBuilder evt, Store<EntityStore> store);

    public abstract void handleDataEvent(Ref<EntityStore> ref, Store<EntityStore> store, T data);

    public void close() {
    }

    public void update(boolean flag, UICommandBuilder cmd) {
    }
}
