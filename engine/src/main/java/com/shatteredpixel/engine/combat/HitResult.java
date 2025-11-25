package com.shatteredpixel.engine.combat;

/**
 * Result of a hit chance calculation.
 *
 * Represents whether an attack successfully hit, missed, or critically hit.
 */
public enum HitResult {

    /**
     * The attack missed the target.
     */
    MISS,

    /**
     * The attack hit the target normally.
     */
    HIT,

    /**
     * The attack critically hit the target (often for increased damage).
     */
    CRITICAL;

    /**
     * Check if the attack successfully hit (HIT or CRITICAL).
     */
    public boolean isHit() {
        return this == HIT || this == CRITICAL;
    }

    /**
     * Check if the attack missed.
     */
    public boolean isMiss() {
        return this == MISS;
    }

    /**
     * Check if the attack critically hit.
     */
    public boolean isCritical() {
        return this == CRITICAL;
    }

    /**
     * Get damage multiplier for this hit result.
     * MISS = 0.0, HIT = 1.0, CRITICAL = 2.0 (default, can be overridden).
     */
    public float getDamageMultiplier() {
        switch (this) {
            case MISS:
                return 0.0f;
            case HIT:
                return 1.0f;
            case CRITICAL:
                return 2.0f;
            default:
                return 1.0f;
        }
    }
}
