package com.shatteredpixel.engine.combat;

/**
 * Broad classification for damage types.
 *
 * This enum provides generic categories without game-specific variants.
 * Concrete game implementations can use these types to implement resistances,
 * weaknesses, and special damage interactions.
 */
public enum DamageType {

    /**
     * Physical damage (e.g., melee, ranged weapons).
     * Typically affected by armor/defense.
     */
    PHYSICAL,

    /**
     * Magic damage (e.g., spells, enchantments).
     * May bypass physical armor but can be resisted by magic resistance.
     */
    MAGIC,

    /**
     * Pure damage (bypasses all resistances).
     * Used for special effects that ignore defenses.
     */
    PURE,

    /**
     * True damage (absolute damage).
     * Similar to pure but may have different semantic meaning in game logic.
     */
    TRUE,

    /**
     * Environmental damage (e.g., traps, hazards, fall damage).
     * May have special interactions with game mechanics.
     */
    ENVIRONMENTAL;

    /**
     * Check if this damage type can be reduced by physical armor.
     */
    public boolean canBeBlockedByArmor() {
        return this == PHYSICAL;
    }

    /**
     * Check if this damage type can be reduced by magic resistance.
     */
    public boolean canBeBlockedByMagicResist() {
        return this == MAGIC;
    }

    /**
     * Check if this damage type bypasses all defenses.
     */
    public boolean bypassesAllDefenses() {
        return this == PURE || this == TRUE;
    }
}
