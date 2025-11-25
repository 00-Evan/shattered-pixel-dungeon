package com.shatteredpixel.engine.combat;

/**
 * Immutable DTO describing the result of an attack.
 *
 * Contains:
 * - Hit result (miss, hit, critical)
 * - Final damage dealt
 * - Whether the defender died
 *
 * Designed to be serializable and deterministic.
 * Can be extended with additional metadata (events, tags, etc.) in future.
 */
public final class AttackResult {

    private final HitResult hitResult;
    private final int finalDamage;
    private final boolean defenderDied;

    public AttackResult(HitResult hitResult, int finalDamage, boolean defenderDied) {
        this.hitResult = hitResult;
        this.finalDamage = finalDamage;
        this.defenderDied = defenderDied;
    }

    public HitResult getHitResult() {
        return hitResult;
    }

    public int getFinalDamage() {
        return finalDamage;
    }

    public boolean defenderDied() {
        return defenderDied;
    }

    /**
     * Check if the attack successfully dealt damage.
     */
    public boolean wasSuccessful() {
        return hitResult.isHit() && finalDamage > 0;
    }

    @Override
    public String toString() {
        return String.format("AttackResult{%s, dmg=%d, died=%b}",
            hitResult, finalDamage, defenderDied);
    }
}
