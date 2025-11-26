package com.shatteredpixel.engine;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.event.GameEvent;

import java.util.Collections;
import java.util.List;

/**
 * Core headless game engine - main orchestrator for all game systems.
 * Contains game logic and simulation with no LibGDX or platform dependencies.
 *
 * This class serves as the primary entry point for the engine, coordinating:
 * - EngineContext (dependency container)
 * - GameState (world snapshot)
 * - DeterministicRNG (reproducible random)
 * - EventBus (game events)
 * - TickLoop (simulation driver)
 *
 * Design goals:
 * - Headless and deterministic
 * - Platform-agnostic (uses GamePlatform abstraction)
 * - Serializable for multiplayer/save-load
 * - GWT-compatible (Java 11)
 */
public class GameEngine {

    private final GamePlatform platform;
    private final EngineContext context;
    private boolean initialized;

    /**
     * Create a new game engine with the specified platform and random seed.
     *
     * @param platform Platform abstraction for I/O and rendering
     * @param seed Random seed for deterministic simulation
     */
    public GameEngine(GamePlatform platform, long seed) {
        this.platform = platform;
        this.context = new EngineContext(seed);
        this.initialized = false;
    }

    /**
     * Create a new game engine with the specified platform (generates random seed).
     *
     * @param platform Platform abstraction for I/O and rendering
     */
    public GameEngine(GamePlatform platform) {
        this(platform, System.nanoTime()); // OK to use for initial seed generation
    }

    /**
     * Initialize the engine and start a new game.
     * This sets up all game systems and creates the initial game state.
     */
    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("Engine already initialized. Call reset() first.");
        }

        System.out.println("GameEngine initializing on platform: " + platform.getPlatformName());
        platform.initialize();

        // Set up tick loop listener for core game updates
        context.getTickLoop().addTickListener(this::onTick);

        // Subscribe to game events (example)
        context.getEventBus().subscribe(GameOverEvent.class, this::onGameOver);

        // Initialize game state (future: generate dungeon, create hero, etc.)
        GameState state = context.getGameState();
        state.setDepth(1);

        // Publish engine started event
        context.getEventBus().publish(new EngineStartedEvent(platform.getPlatformName()));

        this.initialized = true;
        System.out.println("GameEngine initialized. Seed: " + context.getRNG().getSeed());
    }

    /**
     * Execute a single simulation tick.
     * This advances the game state by one frame.
     *
     * Called by the platform layer (e.g., LibGDX render loop).
     */
    public void tick() {
        if (!initialized) {
            throw new IllegalStateException("Engine not initialized. Call initialize() first.");
        }

        context.getTickLoop().tick();
    }

    /**
     * Execute a single simulation tick with command input.
     * This is the command/event pipeline entry point.
     *
     * Flow:
     * 1. Clear previous events from EventCollector
     * 2. Process each command in order
     * 3. Advance tick count
     * 4. Return all events generated during this tick
     *
     * This method is designed for:
     * - Deterministic replay (same commands + same state = same events)
     * - Multiplayer sync (commands from all players processed together)
     * - Testing (inject commands, observe events)
     *
     * @param commands List of commands to process this tick
     * @return List of events generated this tick
     */
    public List<GameEvent> tick(List<GameCommand> commands) {
        if (!initialized) {
            throw new IllegalStateException("Engine not initialized. Call initialize() first.");
        }

        // Clear previous events
        context.getEventCollector().clear();

        // Process commands (placeholder implementation for now)
        if (commands != null) {
            for (GameCommand command : commands) {
                processCommand(command);
            }
        }

        // Advance tick
        context.getTickLoop().tick();

        // Return all events generated this tick
        return context.getEventCollector().drainAll();
    }

    /**
     * Process a single command (placeholder implementation).
     *
     * TODO: Implement full command processing:
     * - MOVE: Move actor to target position, check collision, publish ACTOR_MOVED event
     * - ATTACK: Resolve combat using CombatFormula, publish DAMAGE_APPLIED/ACTOR_DIED events
     * - USE_ABILITY: Look up ability, execute it, publish relevant events
     * - WAIT: Pass turn, publish TURN_ENDED event
     * - SYSTEM: Handle system commands (save, quit, etc.)
     *
     * For now, this creates placeholder events to demonstrate the pipeline.
     */
    private void processCommand(GameCommand command) {
        switch (command.getType()) {
            case MOVE:
                // TODO: Implement movement logic
                // - Look up actor by actorId
                // - Check if target position is valid (pathfinding, collision)
                // - Update actor position
                // - Publish ACTOR_MOVED event
                if (command.getTargetPosition() != null) {
                    context.getEventCollector().publish(
                        GameEvent.actorMoved(command.getActorId(), command.getTargetPosition())
                    );
                }
                break;

            case ATTACK:
                // TODO: Implement attack logic
                // - Look up attacker and target by actorId
                // - Create AttackRequest
                // - Resolve using CombatFormula
                // - Publish DAMAGE_APPLIED event
                // - If target died, publish ACTOR_DIED event
                if (command.getTargetActorId() != null) {
                    context.getEventCollector().publish(
                        GameEvent.logMessage("Attack placeholder: " + command.getActorId() + " -> " + command.getTargetActorId())
                    );
                }
                break;

            case USE_ABILITY:
                // TODO: Implement ability logic
                // - Look up actor by actorId
                // - Look up ability by actionId
                // - Execute ability
                // - Publish relevant events (damage, healing, buffs, etc.)
                context.getEventCollector().publish(
                    GameEvent.logMessage("Ability placeholder: " + command.getActionId())
                );
                break;

            case WAIT:
                // TODO: Implement wait logic
                // - Look up actor by actorId
                // - Pass turn (advance actor's time)
                // - Publish TURN_ENDED event
                context.getEventCollector().publish(
                    GameEvent.turnEnded(command.getActorId())
                );
                break;

            case SYSTEM:
                // TODO: Implement system commands
                // - Handle save, load, quit, etc.
                context.getEventCollector().publish(
                    GameEvent.system("System command: " + command.getActionId())
                );
                break;

            case DEBUG:
                // TODO: Implement debug commands
                // - Teleport, spawn items, give XP, etc.
                context.getEventCollector().publish(
                    GameEvent.system("Debug command: " + command.getActionId())
                );
                break;
        }
    }

    /**
     * Internal tick handler called by TickLoop.
     * This is where core game logic will be processed.
     */
    private void onTick(long tickCount) {
        GameState state = context.getGameState();

        // Future: Process game logic here
        // - Update actors (hero, mobs)
        // - Process buffs/debuffs
        // - Handle environmental effects
        // - Check win/loss conditions

        // Example: Check game over condition (placeholder)
        if (state.getTurnCount() % 100 == 0) {
            System.out.println("Turn " + state.getTurnCount() + " - Depth " + state.getDepth());
        }
    }

    /**
     * Handle game over event.
     */
    private void onGameOver(GameOverEvent event) {
        System.out.println("Game Over: " + event.reason);
        context.getGameState().setGameOver(true);
        context.getTickLoop().pause();
    }

    /**
     * Pause the simulation.
     */
    public void pause() {
        context.getTickLoop().pause();
    }

    /**
     * Resume the simulation.
     */
    public void resume() {
        context.getTickLoop().resume();
    }

    /**
     * Check if the simulation is paused.
     */
    public boolean isPaused() {
        return context.getTickLoop().isPaused();
    }

    /**
     * Check if the engine is running.
     */
    public boolean isRunning() {
        return initialized && !context.getGameState().isGameOver();
    }

    /**
     * Serialize the current game state to bytes.
     * Used for save files and multiplayer synchronization.
     *
     * @return Serialized game state
     */
    public byte[] saveState() {
        if (!initialized) {
            throw new IllegalStateException("Engine not initialized. Nothing to save.");
        }

        // Future: Serialize entire game world
        // For now, just delegate to GameState
        return context.getGameState().serialize();
    }

    /**
     * Load a game state from serialized bytes.
     * Used for loading save files and multiplayer synchronization.
     *
     * @param data Serialized game state
     */
    public void loadState(byte[] data) {
        if (!initialized) {
            throw new IllegalStateException("Engine not initialized. Call initialize() first.");
        }

        // Future: Deserialize entire game world
        GameState loadedState = GameState.deserialize(data);
        context.setGameState(loadedState);

        System.out.println("GameState loaded. Depth: " + loadedState.getDepth());
    }

    /**
     * Reset the engine to a fresh state with a new seed.
     * Clears all game state and reinitializes systems.
     *
     * @param newSeed New random seed
     */
    public void reset(long newSeed) {
        context.reset(newSeed);
        context.getTickLoop().reset();
        this.initialized = false;

        System.out.println("GameEngine reset with new seed: " + newSeed);
    }

    /**
     * Get the engine context (for advanced use cases).
     * Provides access to all engine subsystems.
     */
    public EngineContext getContext() {
        return context;
    }

    /**
     * Get the platform abstraction.
     */
    public GamePlatform getPlatform() {
        return platform;
    }

    /**
     * Get the current game state.
     */
    public GameState getGameState() {
        return context.getGameState();
    }

    // =========================================================================
    // Event Classes (minimal examples for wiring demonstration)
    // =========================================================================

    /**
     * Event published when the engine starts.
     */
    public static class EngineStartedEvent {
        public final String platformName;

        public EngineStartedEvent(String platformName) {
            this.platformName = platformName;
        }
    }

    /**
     * Event published when the game is over.
     */
    public static class GameOverEvent {
        public final String reason;

        public GameOverEvent(String reason) {
            this.reason = reason;
        }
    }
}
