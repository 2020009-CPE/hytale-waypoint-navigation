package com.hypixel.hytale.server.core.universe.world;

/**
 * Stub for Hytale's World class.
 * Provides world-level operations including thread-safe task execution.
 */
public class World {

    /**
     * Executes a task on the world's thread.
     * Used for thread-safe operations within the world context.
     *
     * @param task The task to execute
     */
    public void execute(Runnable task) {
        // Stub: in the real server, this queues the task for the world thread
    }
}
