package com.shatteredpixel.engine.actor;

/**
 * Classification of actor types.
 * Used to distinguish different categories of actors without game-specific logic.
 *
 * Design goals:
 * - Generic categories (not tied to SPD specifics)
 * - Extensible for future actor types
 * - No behavior attached (just classification)
 */
public enum ActorType {

    /**
     * Player-controlled character (hero).
     */
    HERO,

    /**
     * Enemy mob (hostile entity).
     */
    MOB,

    /**
     * Non-player character (friendly or neutral).
     */
    NPC,

    /**
     * Projectile (arrow, magic missile, etc.).
     */
    PROJECTILE,

    /**
     * Environmental effect (trap, hazard, plant).
     */
    ENVIRONMENTAL,

    /**
     * Temporary effect or animation (visual only, no gameplay impact).
     */
    EFFECT,

    /**
     * Other/unknown actor type.
     */
    OTHER;

    /**
     * Check if this is a character type (HERO, MOB, or NPC).
     */
    public boolean isCharacter() {
        return this == HERO || this == MOB || this == NPC;
    }

    /**
     * Check if this is typically hostile (MOB).
     */
    public boolean isHostile() {
        return this == MOB;
    }

    /**
     * Check if this is a temporary/ephemeral actor (PROJECTILE, EFFECT).
     */
    public boolean isTemporary() {
        return this == PROJECTILE || this == EFFECT;
    }
}
