package com.shatteredpixel.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixed timestep game loop for deterministic simulation.
 * Drives the engine forward in discrete ticks.
 *
 * Design goals:
 * - Fixed timestep (no variable delta time)
 * - Deterministic execution order
 * - No dependence on system time
 * - Supports pause/resume
 *
 * The TickLoop processes game logic in discrete steps.
 * Each tick represents one logical frame of simulation.
 * This is critical for multiplayer and replay systems.
 */
public class TickLoop {

    private final EngineContext context;
    private final List<TickListener> listeners;

    private boolean paused;
    private long tickCount;

    /**
     * Create a new tick loop for the specified engine context.
     */
    public TickLoop(EngineContext context) {
        this.context = context;
        this.listeners = new ArrayList<>();
        this.paused = false;
        this.tickCount = 0;
    }

    /**
     * Register a listener to be notified on each tick.
     */
    public void addTickListener(TickListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregister a tick listener.
     */
    public void removeTickListener(TickListener listener) {
        listeners.remove(listener);
    }

    /**
     * Execute a single simulation tick.
     * This is the main update method called by the platform layer.
     */
    public void tick() {
        if (paused) {
            return;
        }

        // Increment turn counter in game state
        GameState state = context.getGameState();
        state.incrementTurn();
        tickCount++;

        // Notify all listeners in registration order (deterministic)
        for (TickListener listener : listeners) {
            listener.onTick(tickCount);
        }

        // Future: Update all game systems here
        // - Process actor turns
        // - Update buffs/debuffs
        // - Process environmental effects
        // - Check win/loss conditions
    }

    /**
     * Pause the simulation.
     */
    public void pause() {
        this.paused = true;
    }

    /**
     * Resume the simulation.
     */
    public void resume() {
        this.paused = false;
    }

    /**
     * Check if the simulation is paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Get the total number of ticks executed.
     */
    public long getTickCount() {
        return tickCount;
    }

    /**
     * Reset the tick counter (used when starting a new game).
     */
    public void reset() {
        this.tickCount = 0;
        this.paused = false;
    }

    /**
     * Functional interface for tick listeners.
     */
    @FunctionalInterface
    public interface TickListener {
        void onTick(long tickCount);
    }
}
