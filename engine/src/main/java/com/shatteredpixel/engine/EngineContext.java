package com.shatteredpixel.engine;

import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.event.EventCollector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<ActorId, Actor> actors;
    private GameState gameState;
    private LevelState currentLevel;
    private ActorId visionActorId; // Primary actor providing field of view (e.g., player character)

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
        this.actors = new HashMap<>();
        this.gameState = new GameState();
        this.currentLevel = null; // No level loaded initially
        this.visionActorId = null; // No vision source initially
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
     * Get all actors (immutable view).
     */
    public Map<ActorId, Actor> getActors() {
        return Collections.unmodifiableMap(actors);
    }

    /**
     * Add an actor to the registry.
     */
    public void addActor(Actor actor) {
        actors.put(actor.getId(), actor);
    }

    /**
     * Remove an actor from the registry.
     */
    public void removeActor(ActorId id) {
        actors.remove(id);
    }

    /**
     * Get an actor by ID.
     */
    public Actor getActor(ActorId id) {
        return actors.get(id);
    }

    /**
     * Get the primary vision actor ID (actor whose position determines FOV).
     * This is typically the player character or main hero.
     * Can be null if no vision source is set.
     *
     * @return ActorId of the vision source, or null if none
     */
    public ActorId getVisionActorId() {
        return visionActorId;
    }

    /**
     * Set the primary vision actor ID.
     * When this actor moves, FOV will be recomputed from their position.
     *
     * Future enhancements could include:
     * - Multiple vision sources (party members)
     * - Per-actor vision radii
     * - Light sources and darkness
     *
     * @param visionActorId ActorId of the vision source, or null to disable FOV updates
     */
    public void setVisionActorId(ActorId visionActorId) {
        this.visionActorId = visionActorId;
    }

    /**
     * Reset the engine to a fresh state with a new seed.
     */
    public void reset(long newSeed) {
        this.rng.setSeed(newSeed);
        this.gameState = new GameState();
        this.eventBus.clear();
        this.eventCollector.clear();
        this.actors.clear();
        this.currentLevel = null;
        this.visionActorId = null; // Clear vision source on reset
    }
}
