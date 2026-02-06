package com.hypixel.hytale.server.core.event;

import java.util.function.Consumer;

public class EventRegistry {
    public <E> void registerGlobal(Class<E> eventClass, Consumer<E> handler) {
    }
}
