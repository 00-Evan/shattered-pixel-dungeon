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
     * Process the next actor's turn using the ActorScheduler.
     *
     * This method drives turn-based gameplay by:
     * 1. Selecting the next actor from the scheduler
     * 2. Deciding what command that actor will execute
     *    - Heroes: use external command from externalCommands list
     *    - Mobs: use AI decision from decideNextAction()
     * 3. Executing the command via the existing tick() pipeline
     * 4. Advancing the actor's time in the scheduler
     * 5. Removing dead actors from the scheduler
     *
     * Command resolution:
     * - For HERO actors: looks for a matching command in externalCommands by actorId
     * - For MOB actors: calls decideNextAction() to get AI-decided command
     * - If no command available: treats as "wait" (returns empty events)
     *
     * Time management:
     * - Uses fixed time cost of 1.0f per turn for now
     * - Future: variable costs based on action type and actor speed
     *
     * Dead actor cleanup:
     * - Actors that die during their turn are removed from scheduler
     * - Also removed from actor registry for garbage collection
     *
     * @param externalCommands Commands provided externally (typically player input for heroes)
     * @return List of GameEvents produced during this turn
     */
    public List<GameEvent> processNextTurn(List<GameCommand> externalCommands) {
        // Get next actor from scheduler
        com.shatteredpixel.engine.actor.Actor nextActor = context.getScheduler().getNextActor();

        // No actors scheduled → no-op
        if (nextActor == null) {
            return java.util.Collections.emptyList();
        }

        // Verify actor still exists in registry (may have been removed/killed)
        com.shatteredpixel.engine.actor.ActorId actorId = nextActor.getId();
        com.shatteredpixel.engine.actor.Actor actor = context.getActor(actorId);
        if (actor == null) {
            // Actor was removed but still in scheduler - clean up and try next
            context.unregisterActorFromScheduling(actorId);
            return processNextTurn(externalCommands); // Recursive call for next actor
        }

        // Only process Character actors (skip other types for now)
        if (!(actor instanceof com.shatteredpixel.engine.actor.Character)) {
            // Skip non-Character actors, advance their time, and move to next
            actor.spendTime(1.0f);
            return processNextTurn(externalCommands); // Recursive call for next actor
        }

        // Decide what command this actor will execute
        GameCommand command = null;

        if (actor.getType() == com.shatteredpixel.engine.actor.ActorType.HERO) {
            // Hero: look for external command matching this actor's ID
            if (externalCommands != null) {
                for (GameCommand cmd : externalCommands) {
                    if (cmd.getActorId() != null && cmd.getActorId().equals(actorId)) {
                        command = cmd;
                        break;
                    }
                }
            }
            // If no command found, hero "waits" (command remains null)

        } else if (actor.getType() == com.shatteredpixel.engine.actor.ActorType.MOB) {
            // Mob: use AI to decide next action
            if (actor instanceof com.shatteredpixel.engine.actor.mob.SimpleMeleeMob) {
                com.shatteredpixel.engine.actor.mob.SimpleMeleeMob mob =
                    (com.shatteredpixel.engine.actor.mob.SimpleMeleeMob) actor;
                command = mob.decideNextAction(context);
            }
            // If AI returns null, mob "waits" (command remains null)

        } else {
            // Other actor types: just wait (no action)
            // TODO: Implement behavior for NPC, PROJECTILE, etc.
        }

        // Execute the command (if any) via existing pipeline
        List<GameEvent> events;
        if (command != null) {
            events = tick(java.util.Collections.singletonList(command));
        } else {
            // No command (wait) - just clear events and advance tick
            context.getEventCollector().clear();
            context.getTickLoop().tick();
            events = context.getEventCollector().drainAll();
        }

        // Normalize time: subtract minimum time from all actors
        // This keeps time values manageable and prevents overflow
        float minTime = nextActor.getTime();
        if (minTime > 0) {
            for (com.shatteredpixel.engine.actor.Actor a : context.getScheduler().getAllActors()) {
                a.setTime(a.getTime() - minTime);
            }
        }

        // Advance this actor's time by fixed cost
        // TODO: Make time cost configurable (based on action type, actor speed, etc.)
        final float TIME_COST = 1.0f;
        nextActor.spendTime(TIME_COST);

        // Clean up dead actors
        if (actor instanceof com.shatteredpixel.engine.actor.Character) {
            com.shatteredpixel.engine.actor.Character character =
                (com.shatteredpixel.engine.actor.Character) actor;
            if (character.isDead()) {
                // Remove from scheduler
                context.unregisterActorFromScheduling(actorId);
                // Remove from actor registry
                context.removeActor(actorId);
            }
        }

        return events;
    }

    /**
     * Process a single command.
     *
     * IMPLEMENTED:
     * - MOVE: Validates and executes actor movement with collision detection
     * - ATTACK: Resolves combat using CombatFormula, publishes DAMAGE_APPLIED/ACTOR_DIED events
     *
     * TODO: Implement remaining command types:
     * - USE_ABILITY: Look up ability, execute it, publish relevant events
     * - WAIT: Pass turn, publish TURN_ENDED event
     * - SYSTEM: Handle system commands (save, quit, etc.)
     */
    private void processCommand(GameCommand command) {
        switch (command.getType()) {
            case MOVE:
                processMoveCommand(command);
                break;

            case ATTACK:
                processAttackCommand(command);
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
     * Process a MOVE command.
     *
     * Flow:
     * 1. Look up actor from registry
     * 2. Get current level
     * 3. Validate target position (bounds, passability, not occupied)
     * 4. If valid: clear old OCCUPIED, update actor position, set new OCCUPIED, publish ACTOR_MOVED
     * 5. If invalid: publish LOG_MESSAGE with reason
     */
    private void processMoveCommand(GameCommand command) {
        // Validate command has required fields
        if (command.getActorId() == null || command.getTargetPosition() == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("Invalid MOVE command: missing actorId or targetPosition")
            );
            return;
        }

        // Look up actor
        com.shatteredpixel.engine.actor.Actor actor = context.getActor(command.getActorId());
        if (actor == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("MOVE failed: actor " + command.getActorId() + " not found")
            );
            return;
        }

        // Get current level
        com.shatteredpixel.engine.dungeon.LevelState level = context.getLevel();
        if (level == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("MOVE failed: no level loaded")
            );
            return;
        }

        com.shatteredpixel.engine.dungeon.LevelGrid grid = level.getGrid();
        com.shatteredpixel.engine.geom.Point targetPos = command.getTargetPosition();

        // Validate target position
        // 1. Check bounds
        if (!grid.isInBounds(targetPos)) {
            context.getEventCollector().publish(
                GameEvent.logMessage("MOVE failed: target " + targetPos + " out of bounds")
            );
            return;
        }

        // 2. Check passability
        if (!grid.isPassable(targetPos)) {
            context.getEventCollector().publish(
                GameEvent.logMessage("MOVE failed: target " + targetPos + " is not passable")
            );
            return;
        }

        // 3. Check if occupied (don't allow moving into occupied tiles)
        if (grid.isOccupied(targetPos)) {
            context.getEventCollector().publish(
                GameEvent.logMessage("MOVE failed: target " + targetPos + " is occupied")
            );
            return;
        }

        // Execute move
        com.shatteredpixel.engine.geom.Point oldPos = actor.getPosition();

        // Clear old position OCCUPIED flag (if actor was on the grid)
        if (oldPos != null && grid.isInBounds(oldPos)) {
            grid.setOccupied(oldPos, false);
        }

        // Update actor position
        actor.setPosition(targetPos);

        // Set new position OCCUPIED flag
        grid.setOccupied(targetPos, true);

        // Publish ACTOR_MOVED event
        context.getEventCollector().publish(
            GameEvent.actorMoved(command.getActorId(), targetPos)
        );

        // Update FOV if this actor is the vision source
        updateFovIfNeeded(command.getActorId());

        // TODO: Future enhancements:
        // - Check for hazardous terrain and apply damage
        // - Trigger traps
        // - Use pathfinding for multi-step movement
        // - Check line-of-sight for diagonal movement
    }

    /**
     * Update FOV if the given actor is the vision source.
     *
     * This method is called after successful movement to recompute the field of view
     * from the vision actor's new position. It updates VISIBLE and DISCOVERED flags
     * on the level grid.
     *
     * If no vision actor is set, or if the given actor is not the vision source,
     * or if no level is loaded, this method does nothing.
     *
     * @param movedActorId The actor that just moved
     */
    private void updateFovIfNeeded(com.shatteredpixel.engine.actor.ActorId movedActorId) {
        // Check if we have a vision actor configured
        com.shatteredpixel.engine.actor.ActorId visionActorId = context.getVisionActorId();
        if (visionActorId == null) {
            return; // No vision source configured
        }

        // Only update FOV if the moved actor is the vision source
        if (!movedActorId.equals(visionActorId)) {
            return; // Different actor moved, no FOV update needed
        }

        // Get the level
        com.shatteredpixel.engine.dungeon.LevelState level = context.getLevel();
        if (level == null) {
            return; // No level loaded
        }

        // Get the vision actor
        com.shatteredpixel.engine.actor.Actor visionActor = context.getActor(visionActorId);
        if (visionActor == null) {
            return; // Vision actor not found (shouldn't happen, but be safe)
        }

        // Get actor's position
        com.shatteredpixel.engine.geom.Point visionPos = visionActor.getPosition();
        if (visionPos == null) {
            return; // Actor has no position
        }

        // Recompute FOV from the vision actor's position
        // Use fixed radius of 8 tiles for now (will be made configurable later)
        // TODO: Make vision radius configurable per-actor or per-game-mode
        // TODO: Consider light sources, darkness, special vision abilities
        final int VISION_RADIUS = 8;

        com.shatteredpixel.engine.dungeon.LevelGrid grid = level.getGrid();
        com.shatteredpixel.engine.dungeon.fov.FovCalculator fovCalculator =
            new com.shatteredpixel.engine.dungeon.fov.FovCalculator();

        // This clears VISIBLE flags and recomputes them, also updates DISCOVERED
        fovCalculator.computeAndApply(grid, visionPos, VISION_RADIUS);

        // TODO: Future enhancements:
        // - Emit FOV_CHANGED or VISIBILITY_CHANGED events for UI updates
        // - Support multiple vision sources (party members)
        // - Per-actor vision radius based on stats/abilities
        // - Light sources and torch mechanics
        // - Darkness/blindness status effects
    }

    /**
     * Process an ATTACK command.
     *
     * Flow:
     * 1. Validate command has required fields (actorId, targetActorId)
     * 2. Look up attacker and defender from registry
     * 3. Validate both are Characters (combat participants)
     * 4. Validate attacker != defender (can't attack self)
     * 5. Create AttackRequest and resolve using CombatFormula
     * 6. Emit events: DAMAGE_APPLIED (if hit), ACTOR_DIED (if killed), LOG_MESSAGE (summary)
     * 7. On validation failure: emit LOG_MESSAGE, do not apply damage
     */
    private void processAttackCommand(GameCommand command) {
        // Validate command has required fields
        if (command.getActorId() == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: missing actorId")
            );
            return;
        }

        if (command.getTargetActorId() == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: missing targetActorId")
            );
            return;
        }

        // Look up attacker
        com.shatteredpixel.engine.actor.Actor attackerActor = context.getActor(command.getActorId());
        if (attackerActor == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: attacker " + command.getActorId() + " not found")
            );
            return;
        }

        // Look up defender
        com.shatteredpixel.engine.actor.Actor defenderActor = context.getActor(command.getTargetActorId());
        if (defenderActor == null) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: defender " + command.getTargetActorId() + " not found")
            );
            return;
        }

        // Validate attacker is a Character
        if (!(attackerActor instanceof com.shatteredpixel.engine.actor.Character)) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: attacker " + command.getActorId() + " is not a Character")
            );
            return;
        }

        // Validate defender is a Character
        if (!(defenderActor instanceof com.shatteredpixel.engine.actor.Character)) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: defender " + command.getTargetActorId() + " is not a Character")
            );
            return;
        }

        // Validate attacker != defender
        if (command.getActorId().equals(command.getTargetActorId())) {
            context.getEventCollector().publish(
                GameEvent.logMessage("ATTACK failed: attacker and defender are the same")
            );
            return;
        }

        // Cast to Character
        com.shatteredpixel.engine.actor.Character attacker = (com.shatteredpixel.engine.actor.Character) attackerActor;
        com.shatteredpixel.engine.actor.Character defender = (com.shatteredpixel.engine.actor.Character) defenderActor;

        // Create AttackRequest with generic PHYSICAL damage type
        // Uses attacker's attackPower stat as base damage
        com.shatteredpixel.engine.combat.AttackRequest request =
            new com.shatteredpixel.engine.combat.AttackRequest(
                attacker,
                defender,
                com.shatteredpixel.engine.combat.DamageType.PHYSICAL
            );

        // Resolve attack using DefaultCombatFormula
        com.shatteredpixel.engine.combat.CombatFormula formula =
            new com.shatteredpixel.engine.combat.DefaultCombatFormula();
        com.shatteredpixel.engine.combat.AttackResult result =
            formula.resolveAttack(request, context);

        // Emit events based on result
        if (result.getHitResult().isHit()) {
            // Emit DAMAGE_APPLIED event
            context.getEventCollector().publish(
                GameEvent.damageApplied(
                    command.getActorId(),
                    command.getTargetActorId(),
                    result.getFinalDamage()
                )
            );

            // If defender died, emit ACTOR_DIED event
            if (result.defenderDied()) {
                context.getEventCollector().publish(
                    GameEvent.actorDied(command.getTargetActorId())
                );
            }

            // Emit LOG_MESSAGE summary
            String hitType = result.getHitResult().isCritical() ? "critical hit" : "hit";
            String deathNote = result.defenderDied() ? " (killed)" : "";
            context.getEventCollector().publish(
                GameEvent.logMessage(
                    String.format("%s attacks %s: %s for %d damage%s",
                        command.getActorId(),
                        command.getTargetActorId(),
                        hitType,
                        result.getFinalDamage(),
                        deathNote
                    )
                )
            );
        } else {
            // Attack missed
            context.getEventCollector().publish(
                GameEvent.logMessage(
                    String.format("%s attacks %s: miss",
                        command.getActorId(),
                        command.getTargetActorId()
                    )
                )
            );
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
