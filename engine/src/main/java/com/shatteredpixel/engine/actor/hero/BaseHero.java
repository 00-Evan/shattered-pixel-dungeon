package com.shatteredpixel.engine.actor.hero;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Abstract base class for player-controllable hero characters.
 *
 * This is a generic hero type without any SPD-specific logic.
 * Concrete implementations can add:
 * - Subclasses/specializations (warrior, mage, etc.)
 * - Talents/abilities
 * - Inventory/equipment
 * - Experience/leveling mechanics
 *
 * Design principles:
 * - No SPD-specific names or mechanics
 * - Generic lifecycle hooks for death and leveling
 * - Command-driven (not autonomous like mobs)
 * - Acts as foundation for future hero implementations
 *
 * Heroes are typically controlled by:
 * - Player input (converted to GameCommands)
 * - AI for companions/party members
 * - Replay/network commands
 */
public abstract class BaseHero extends Character {

    private final String name; // Hero's name (generic identifier)

    /**
     * Create a new hero with the given position and stats.
     *
     * @param position Initial position in the level
     * @param stats Character stats (health, attack, defense, etc.)
     * @param name Hero's name (for display/logging)
     */
    public BaseHero(Point position, Stats stats, String name) {
        super(ActorType.HERO, position, stats);
        this.name = name;
    }

    /**
     * Create a new hero with stats and specific current health.
     * Useful for loading saved state.
     *
     * @param position Initial position
     * @param stats Character stats
     * @param currentHealth Current health value
     * @param name Hero's name
     */
    public BaseHero(Point position, Stats stats, int currentHealth, String name) {
        super(ActorType.HERO, position, stats, currentHealth);
        this.name = name;
    }

    /**
     * Get the hero's name.
     *
     * @return Hero's name (generic identifier)
     */
    public String getName() {
        return name;
    }

    /**
     * Called when this hero dies (health reaches 0).
     *
     * Subclasses should override to implement death behavior:
     * - Game over logic
     * - Death animations/sounds
     * - Score/statistics tracking
     * - Save file management
     *
     * Default implementation does nothing.
     *
     * @param context Engine context for events/RNG
     */
    @Override
    protected void onDeath(EngineContext context) {
        // TODO: Implement hero death behavior
        // - Emit GAME_OVER event
        // - Save death statistics
        // - Trigger death animation
        // - Clear command queue
    }

    /**
     * Called when this hero levels up.
     *
     * Subclasses should override to implement level-up behavior:
     * - Stat increases
     * - Health restoration
     * - Skill point awards
     * - Achievement tracking
     *
     * Default implementation does nothing.
     *
     * @param context Engine context for events/RNG
     */
    @Override
    protected void onLevelUp(EngineContext context) {
        // TODO: Implement hero level-up behavior
        // - Increase stats based on class/build
        // - Restore health to full
        // - Grant skill points
        // - Emit LEVEL_UP event
        // - Show level-up UI
    }

    /**
     * Heroes are command-driven, not autonomous.
     *
     * The act() method for heroes is typically not called in a traditional
     * turn-based scheduler, as heroes wait for player input (GameCommands).
     *
     * However, for systems that require all actors to have act() behavior
     * (e.g., real-time mode, autonomous companions), subclasses should
     * override this method.
     *
     * Default implementation returns a standard time cost with TODO.
     *
     * @param context Engine context
     * @return Time cost of the action (affects next turn scheduling)
     */
    @Override
    public float act(EngineContext context) {
        // TODO: For now, heroes are command-driven and don't act autonomously
        // Future implementations could add:
        // - Autonomous companion AI
        // - Real-time movement
        // - Idle animations
        return 1.0f; // Standard time cost
    }

    @Override
    public String toString() {
        return String.format("Hero{name='%s', id=%s, pos=%s, %s}",
            name, getId(), getPosition(), getHealth());
    }
}
