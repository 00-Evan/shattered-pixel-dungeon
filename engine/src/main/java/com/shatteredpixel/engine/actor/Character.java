package com.shatteredpixel.engine.actor;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Health;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Abstract base class for actors that have health, stats, and can participate
 * in combat-like interactions.
 *
 * Character extends Actor to add:
 * - Health tracking (current/max HP)
 * - Stats (attack, defense, accuracy, evasion, speed)
 * - Combat-related methods (take damage, heal, death)
 *
 * This is the foundation for future concrete implementations like Hero, Mob, NPC.
 *
 * Design principles:
 * - No game-specific logic (no inventory, AI, skills, concrete damage formulas)
 * - No references to items, buffs, dungeon, or terrain
 * - Deterministic and GWT-safe
 * - Designed for future serialization and multiplayer sync
 */
public abstract class Character extends Actor {

    private final Stats stats;
    private final Health health;

    /**
     * Create a new character with the given stats.
     * Health is initialized to maxHealth from stats.
     */
    public Character(ActorType type, Point position, Stats stats) {
        super(type, position);
        this.stats = stats;
        this.health = new Health(stats.getMaxHealth());
    }

    /**
     * Create a new character with stats and specific current health.
     * Useful for loading saved state.
     */
    public Character(ActorType type, Point position, Stats stats, int currentHealth) {
        super(type, position);
        this.stats = stats;
        this.health = new Health(currentHealth, stats.getMaxHealth());
    }

    // ===== Stats Access =====

    public Stats getStats() {
        return stats;
    }

    public Health getHealth() {
        return health;
    }

    public int getCurrentHealth() {
        return health.getCurrentHealth();
    }

    public int getMaxHealth() {
        return stats.getMaxHealth();
    }

    public boolean isAlive() {
        return health.isAlive();
    }

    public boolean isDead() {
        return health.isDead();
    }

    // ===== Combat Methods =====

    /**
     * Apply damage to this character.
     * This is a basic damage application with no formulas or resistances yet.
     *
     * Future implementations will add:
     * - Defense/armor calculations
     * - Damage type resistances
     * - Critical hits
     * - Damage events for logging/UI
     *
     * @param amount Raw damage amount
     * @param context Engine context for events/RNG
     * @return Actual damage taken
     */
    public int takeDamage(int amount, EngineContext context) {
        int actualDamage = health.takeDamage(amount);

        // Check for death
        if (health.isDead()) {
            onDeath(context);
        }

        return actualDamage;
    }

    /**
     * Heal this character.
     *
     * Future implementations will add:
     * - Healing over time (HoT)
     * - Healing events for logging/UI
     * - Heal effectiveness modifiers
     *
     * @param amount Healing amount
     * @param context Engine context
     * @return Actual healing received
     */
    public int heal(int amount, EngineContext context) {
        return health.heal(amount);
    }

    /**
     * Fully restore this character's health.
     */
    public void fullRestore() {
        health.fullRestore();
    }

    // ===== Lifecycle Hooks =====

    /**
     * Called when this character dies (health reaches 0).
     * Subclasses should override to implement death behavior
     * (drop items, give XP, play animation, etc.).
     *
     * Default implementation does nothing.
     *
     * Future implementations will:
     * - Fire death events
     * - Drop items/gold
     * - Award XP to killer
     * - Play death animation/sound
     * - Remove from scheduler
     */
    protected void onDeath(EngineContext context) {
        // TODO: Death behavior for concrete character types
    }

    /**
     * Called when this character levels up.
     * Subclasses should override to implement level-up behavior.
     *
     * Default implementation does nothing.
     *
     * Future implementations will:
     * - Increase stats
     * - Restore health
     * - Fire level-up events
     * - Show level-up UI
     */
    protected void onLevelUp(EngineContext context) {
        // TODO: Level-up behavior for concrete character types
    }

    // ===== Abstract Methods =====

    /**
     * Character subclasses must implement act() to define their behavior.
     *
     * For combat characters, this typically involves:
     * - AI decision making (for enemies)
     * - Input processing (for player)
     * - Movement and attacks
     *
     * @param context Engine context with RNG, events, game state
     * @return Time cost of the action (affects next turn scheduling)
     */
    @Override
    public abstract float act(EngineContext context);

    // ===== Utility =====

    @Override
    public String toString() {
        return String.format("%s{id=%s, pos=%s, %s, %s}",
            getClass().getSimpleName(),
            getId(),
            getPosition(),
            health,
            stats);
    }
}
