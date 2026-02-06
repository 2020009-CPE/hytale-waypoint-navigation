package com.hypixel.hytale.component;

public interface Store<T> {
    <C> C getComponent(Ref<T> ref, ComponentType<C> type);
}
