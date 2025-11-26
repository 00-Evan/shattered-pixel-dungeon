package com.shatteredpixel.engine;

import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.event.EventCollector;

/**
 * Central context for the game engine.
 * Holds references to all major engine systems and provides access to them.
 * This acts as the dependency injection container for the engine.
 *
 * Design goals:
 * - Single source of truth for engine state
 * - Facilitates testing by allowing component injection
 * - Enables serialization for multiplayer sync
 */
public class EngineContext {

    private final DeterministicRNG rng;
    private final EventBus eventBus;
    private final TickLoop tickLoop;
    private final EventCollector eventCollector;
    private GameState gameState;
    private LevelState currentLevel;

    /**
     * Create a new engine context with the specified seed.
     *
     * @param seed Random seed for deterministic simulation
     */
    public EngineContext(long seed) {
        this.rng = new DeterministicRNG(seed);
        this.eventBus = new EventBus();
        this.tickLoop = new TickLoop(this);
        this.eventCollector = new EventCollector();
        this.gameState = new GameState();
        this.currentLevel = null; // No level loaded initially
    }

    /**
     * Get the deterministic random number generator.
     */
    public DeterministicRNG getRNG() {
        return rng;
    }

    /**
     * Get the event bus for publishing/subscribing to game events.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Get the tick loop that drives the simulation.
     */
    public TickLoop getTickLoop() {
        return tickLoop;
    }

    /**
     * Get the event collector for gathering events during a tick.
     */
    public EventCollector getEventCollector() {
        return eventCollector;
    }

    /**
     * Get the current game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Set a new game state (used for loading saves or sync).
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Get the current level (dungeon/map state).
     */
    public LevelState getLevel() {
        return currentLevel;
    }

    /**
     * Set the current level (used for level transitions, loading, etc.).
     */
    public void setLevel(LevelState level) {
        this.currentLevel = level;
    }

    /**
     * Reset the engine to a fresh state with a new seed.
     */
    public void reset(long newSeed) {
        this.rng.setSeed(newSeed);
        this.gameState = new GameState();
        this.eventBus.clear();
        this.eventCollector.clear();
        this.currentLevel = null;
    }
}
