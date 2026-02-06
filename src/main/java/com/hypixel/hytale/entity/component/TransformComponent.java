package com.hypixel.hytale.entity.component;

import com.hypixel.hytale.math.Vec3d;
import com.hypixel.hytale.store.ComponentType;

/**
 * Stub class for Hytale API - TransformComponent
 * This is a temporary stub until the official Hytale API is released.
 */
public class TransformComponent {
    private Vec3d position;
    
    public TransformComponent() {
        this.position = new Vec3d(0, 0, 0);
    }
    
    public Vec3d getPosition() {
        return position;
    }
    
    public void setPosition(Vec3d position) {
        this.position = position;
    }
    
    public static ComponentType<TransformComponent> getComponentType() {
        return new ComponentType<>();
    }
}
