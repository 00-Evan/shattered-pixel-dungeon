package com.shatteredpixel.engine.actor.mob;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Simple melee mob for testing and demos.
 *
 * This is a minimal concrete mob implementation used only for:
 * - Unit tests
 * - Integration tests
 * - Demos/examples
 *
 * NOT intended as a production mob - just a test fixture.
 *
 * Behavior:
 * - No AI (just waits when act() is called)
 * - Can be attacked and killed
 * - Uses default Stats if none provided
 *
 * Future: When AI is implemented, this could be upgraded to do simple
 * melee attacks on nearby heroes.
 */
public class SimpleMeleeMob extends BaseMob {

    /**
     * Default stats for simple melee mobs.
     * - 20 HP (easy to kill in tests)
     * - 5 attack power (low damage)
     * - 2 defense (minimal armor)
     * - 10 accuracy/evasion (average)
     */
    private static final Stats DEFAULT_STATS = new Stats(
        20,   // maxHealth
        5,    // attackPower
        2,    // defense
        10,   // accuracy
        10,   // evasion
        1.0f  // speed
    );

    /**
     * Create a simple melee mob with default stats.
     *
     * @param position Initial position in the level
     */
    public SimpleMeleeMob(Point position) {
        super(position, DEFAULT_STATS.copy());
    }

    /**
     * Create a simple melee mob with custom stats.
     *
     * @param position Initial position
     * @param stats Custom stats
     */
    public SimpleMeleeMob(Point position, Stats stats) {
        super(position, stats);
    }

    /**
     * Create a simple melee mob with custom stats and health.
     *
     * @param position Initial position
     * @param stats Custom stats
     * @param currentHealth Current health value
     */
    public SimpleMeleeMob(Point position, Stats stats, int currentHealth) {
        super(position, stats, currentHealth);
    }

    /**
     * Stub AI behavior - just waits.
     *
     * TODO: Implement basic melee AI:
     * - Detect nearby heroes (within attack range)
     * - Issue ATTACK command if hero adjacent
     * - Issue MOVE command to approach hero if visible
     * - Wait if no targets
     *
     * For now, this is just a test fixture that returns a fixed time cost.
     *
     * @param context Engine context
     * @return Time cost (1.0 = standard turn)
     */
    @Override
    public float act(EngineContext context) {
        // TODO: Implement AI
        // For now, just wait (do nothing)
        return 1.0f; // Standard time cost for waiting
    }

    /**
     * Called when this mob dies.
     *
     * For SimpleMeleeMob (test fixture), we don't need special death behavior.
     * Production mobs would override this to drop loot, award XP, etc.
     *
     * @param context Engine context
     */
    @Override
    protected void onDeath(EngineContext context) {
        // No special death behavior for test mob
        // Just rely on Character.takeDamage() emitting ACTOR_DIED event
    }

    @Override
    public String toString() {
        return String.format("SimpleMeleeMob{id=%s, pos=%s, %s}",
            getId(), getPosition(), getHealth());
    }
}
