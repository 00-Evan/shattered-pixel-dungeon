package com.shatteredpixel.engine.actor.mob;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Abstract base class for hostile mob characters.
 *
 * This is a generic mob type without any SPD-specific logic.
 * Concrete implementations can add:
 * - Specific mob types (melee, ranged, caster, boss)
 * - AI behaviors (chase, flee, patrol, ambush)
 * - Special abilities/attacks
 * - Drop tables (items, gold)
 * - Experience values
 *
 * Design principles:
 * - No SPD-specific names or mechanics (no "Rat", "Gnoll", etc.)
 * - Generic AI hooks for future behavior
 * - AI-driven (autonomous, not command-driven like heroes)
 * - Acts as foundation for future mob implementations
 *
 * Mobs are typically controlled by:
 * - AI logic in act() method
 * - Behavior trees or state machines
 * - Scripted encounters
 */
public abstract class BaseMob extends Character {

    /**
     * Create a new mob with the given position and stats.
     *
     * @param position Initial position in the level
     * @param stats Character stats (health, attack, defense, etc.)
     */
    public BaseMob(Point position, Stats stats) {
        super(ActorType.MOB, position, stats);
    }

    /**
     * Create a new mob with stats and specific current health.
     * Useful for loading saved state or wounded spawns.
     *
     * @param position Initial position
     * @param stats Character stats
     * @param currentHealth Current health value
     */
    public BaseMob(Point position, Stats stats, int currentHealth) {
        super(ActorType.MOB, position, stats, currentHealth);
    }

    /**
     * Called when this mob dies (health reaches 0).
     *
     * Subclasses should override to implement death behavior:
     * - Drop items/gold
     * - Grant experience to killer
     * - Trigger death animation/sound
     * - Update quest counters
     * - Remove from scheduler
     *
     * Default implementation does nothing.
     *
     * @param context Engine context for events/RNG
     */
    @Override
    protected void onDeath(EngineContext context) {
        // TODO: Implement mob death behavior
        // - Emit ACTOR_DIED event (already done in Character.takeDamage)
        // - Drop loot based on loot table
        // - Award experience to killer
        // - Trigger death effects (blood splatter, corpse, etc.)
        // - Update statistics/achievements
    }

    /**
     * Choose the next action for this mob to perform.
     *
     * This is the core AI decision method. Subclasses should override to
     * implement mob-specific behavior:
     * - Chase hero if in range
     * - Patrol area
     * - Flee if low health
     * - Use special abilities
     * - Wait/idle if no targets
     *
     * The return value could be:
     * - An enum representing the chosen action (MOVE, ATTACK, WAIT, etc.)
     * - A GameCommand to be executed
     * - Null if no action chosen (wait)
     *
     * For this base implementation, this is a placeholder.
     * Future implementations will define a standard action representation.
     *
     * @param context Engine context for RNG/game state
     * @return Chosen action (future: GameCommand or action enum)
     */
    protected Object chooseAction(EngineContext context) {
        // TODO: Define standard action representation
        // - Return GameCommand directly?
        // - Return action enum (MOVE, ATTACK, WAIT)?
        // - Return behavior tree result?

        // For now, placeholder return
        return null; // No action chosen (wait)
    }

    /**
     * Mob acts autonomously based on AI.
     *
     * The act() method is called by the turn scheduler when it's this mob's
     * turn to act. Subclasses should:
     * 1. Choose an action via chooseAction()
     * 2. Execute the action (move, attack, use ability, wait)
     * 3. Return time cost of the action
     *
     * Default implementation is a stub that just waits.
     * Subclasses MUST override this to provide meaningful AI behavior.
     *
     * @param context Engine context
     * @return Time cost of the action (affects next turn scheduling)
     */
    @Override
    public abstract float act(EngineContext context);

    @Override
    public String toString() {
        return String.format("Mob{type=%s, id=%s, pos=%s, %s}",
            getClass().getSimpleName(), getId(), getPosition(), getHealth());
    }
}
