package com.shatteredpixel.engine.combat;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.stats.Stats;
import com.shatteredpixel.engine.util.RandomUtils;

/**
 * Default implementation of CombatFormula with simple, easy-to-understand formulas.
 *
 * This is a PLACEHOLDER implementation intended as a baseline. Real game logic
 * should provide more sophisticated formulas with proper balancing.
 *
 * Formula details:
 *
 * HIT CHANCE:
 * - Base hit chance = 100%
 * - Modified by: (attacker.accuracy - defender.evasion)
 * - Each point of difference = 5% hit chance change
 * - Clamped to [10%, 95%] to avoid guaranteed hits/misses
 * - Critical chance = 10% of successful hits (if enabled)
 *
 * DAMAGE:
 * - Base damage from AttackRequest
 * - Modified by attacker's attackPower stat
 * - Reduced by defender's defense stat (if not ignored)
 * - Defense reduces damage by: (defense / 2) flat reduction
 * - Critical hits deal 2x damage
 * - Minimum damage = 1 (if hit)
 *
 * These formulas are deterministic and use DeterministicRNG from context.
 */
public class DefaultCombatFormula implements CombatFormula {

    // Constants for tuning
    private static final float BASE_HIT_CHANCE = 1.0f; // 100%
    private static final float HIT_CHANCE_PER_ACCURACY_DIFF = 0.05f; // 5% per point
    private static final float MIN_HIT_CHANCE = 0.10f; // 10%
    private static final float MAX_HIT_CHANCE = 0.95f; // 95%
    private static final float CRIT_CHANCE = 0.10f; // 10% of hits

    @Override
    public HitResult computeHitResult(AttackRequest request, EngineContext context) {
        // If attack can't miss, auto-hit
        if (!request.canMiss()) {
            return request.canCritical() && rollCritical(context) ? HitResult.CRITICAL : HitResult.HIT;
        }

        // Get stats
        Stats attackerStats = request.getAttacker().getStats();
        Stats defenderStats = request.getDefender().getStats();

        // Compute hit chance: base + (accuracy - evasion) * modifier
        int accuracyDiff = attackerStats.getAccuracy() - defenderStats.getEvasion();
        float hitChance = BASE_HIT_CHANCE + (accuracyDiff * HIT_CHANCE_PER_ACCURACY_DIFF);

        // Clamp to [MIN, MAX]
        hitChance = Math.max(MIN_HIT_CHANCE, Math.min(MAX_HIT_CHANCE, hitChance));

        // Roll hit
        if (!RandomUtils.chance(context.getRng(), hitChance)) {
            return HitResult.MISS;
        }

        // Check for critical (if enabled)
        if (request.canCritical() && rollCritical(context)) {
            return HitResult.CRITICAL;
        }

        return HitResult.HIT;
    }

    @Override
    public int computeDamage(AttackRequest request, HitResult hitResult, EngineContext context) {
        // If missed, no damage
        if (hitResult.isMiss()) {
            return 0;
        }

        // Start with base damage
        int damage = request.getBaseDamage();

        // Add attacker's attack power
        Stats attackerStats = request.getAttacker().getStats();
        damage += attackerStats.getAttackPower();

        // Apply defense reduction (if not ignored)
        if (!request.ignoresDefense() && request.getDamageType().canBeBlockedByArmor()) {
            Stats defenderStats = request.getDefender().getStats();
            int defense = defenderStats.getDefense();

            // Simple flat reduction: defense / 2
            // This is a PLACEHOLDER formula - real implementations should use
            // more sophisticated defense curves (e.g., diminishing returns)
            int defenseReduction = defense / 2;
            damage -= defenseReduction;
        }

        // Apply crit multiplier
        if (hitResult.isCritical()) {
            damage = (int) (damage * hitResult.getDamageMultiplier());
        }

        // Minimum 1 damage if hit landed
        return Math.max(1, damage);
    }

    /**
     * Roll for critical hit.
     *
     * @return true if critical hit
     */
    private boolean rollCritical(EngineContext context) {
        return RandomUtils.chance(context.getRng(), CRIT_CHANCE);
    }
}
