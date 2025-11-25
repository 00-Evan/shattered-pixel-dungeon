package com.shatteredpixel.engine.buff;

/**
 * Broad classification for buff/status effects.
 *
 * This enum provides generic categories without game-specific flavors.
 * Concrete buff implementations will specify their type.
 *
 * Categories:
 * - POSITIVE: Beneficial effects (e.g., future: healing, haste, shields)
 * - NEGATIVE: Harmful effects (e.g., future: poison, burning, weakness)
 * - NEUTRAL: Neither beneficial nor harmful (e.g., future: markers, trackers)
 */
public enum BuffType {

    /**
     * Beneficial effects (e.g., future: healing, haste, shields).
     */
    POSITIVE,

    /**
     * Harmful effects (e.g., future: poison, burning, weakness).
     */
    NEGATIVE,

    /**
     * Neither beneficial nor harmful (e.g., future: markers, trackers).
     */
    NEUTRAL;

    /**
     * Check if this buff type is beneficial.
     */
    public boolean isPositive() {
        return this == POSITIVE;
    }

    /**
     * Check if this buff type is harmful.
     */
    public boolean isNegative() {
        return this == NEGATIVE;
    }

    /**
     * Check if this buff type is neutral.
     */
    public boolean isNeutral() {
        return this == NEUTRAL;
    }
}
