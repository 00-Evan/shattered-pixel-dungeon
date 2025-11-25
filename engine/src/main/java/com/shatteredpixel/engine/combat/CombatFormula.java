package com.shatteredpixel.engine.combat;

import com.shatteredpixel.engine.EngineContext;

/**
 * Interface defining how combat is resolved.
 *
 * This abstraction allows different implementations of combat formulas:
 * - Default/basic formulas for testing
 * - Game-specific formulas (SPD, mods, etc.)
 * - Difficulty-scaled formulas
 *
 * Implementations should be deterministic (same inputs = same outputs)
 * and use EngineContext for RNG access.
 */
public interface CombatFormula {

    /**
     * Compute whether an attack hits, misses, or crits.
     *
     * Uses attacker accuracy, defender evasion, and RNG to determine outcome.
     *
     * @param request The attack request
     * @param context Engine context for RNG
     * @return The hit result
     */
    HitResult computeHitResult(AttackRequest request, EngineContext context);

    /**
     * Compute final damage for an attack.
     *
     * Applies attacker's attack power, defender's defense, damage type modifiers,
     * critical hit multipliers, etc.
     *
     * @param request The attack request
     * @param hitResult The hit result (for crit multipliers)
     * @param context Engine context
     * @return The final damage amount (>= 0)
     */
    int computeDamage(AttackRequest request, HitResult hitResult, EngineContext context);

    /**
     * Resolve a complete attack.
     *
     * This is a convenience method that:
     * 1. Computes hit result
     * 2. Computes damage (if hit)
     * 3. Applies damage to defender
     * 4. Returns complete result
     *
     * Default implementation combines computeHitResult() and computeDamage(),
     * but can be overridden for more complex attack flows.
     *
     * @param request The attack request
     * @param context Engine context
     * @return The complete attack result
     */
    default AttackResult resolveAttack(AttackRequest request, EngineContext context) {
        // Compute hit result
        HitResult hitResult = computeHitResult(request, context);

        // If missed, no damage
        if (hitResult.isMiss()) {
            return new AttackResult(hitResult, 0, false);
        }

        // Compute damage
        int finalDamage = computeDamage(request, hitResult, context);

        // Apply damage to defender
        request.getDefender().takeDamage(finalDamage, context);

        // Check if defender died
        boolean died = request.getDefender().isDead();

        return new AttackResult(hitResult, finalDamage, died);
    }
}
