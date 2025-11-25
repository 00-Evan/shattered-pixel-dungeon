package com.shatteredpixel.engine.combat;

import com.shatteredpixel.engine.actor.Character;

/**
 * Immutable DTO describing an attack attempt.
 *
 * Contains all information needed to resolve an attack:
 * - Who is attacking whom
 * - Type of damage
 * - Base damage amount
 * - Optional flags for special attack properties
 *
 * This is designed to be serializable and deterministic.
 * No item/weapon references in this base version - those will be layered later.
 */
public final class AttackRequest {

    private final Character attacker;
    private final Character defender;
    private final DamageType damageType;
    private final int baseDamage;

    // Optional flags (can be expanded in future)
    private final boolean canCritical;
    private final boolean ignoresDefense;
    private final boolean canMiss;

    /**
     * Full constructor with all options.
     */
    public AttackRequest(Character attacker, Character defender, DamageType damageType,
                        int baseDamage, boolean canCritical, boolean ignoresDefense, boolean canMiss) {
        this.attacker = attacker;
        this.defender = defender;
        this.damageType = damageType;
        this.baseDamage = baseDamage;
        this.canCritical = canCritical;
        this.ignoresDefense = ignoresDefense;
        this.canMiss = canMiss;
    }

    /**
     * Simple constructor with defaults (can crit, doesn't ignore defense, can miss).
     */
    public AttackRequest(Character attacker, Character defender, DamageType damageType, int baseDamage) {
        this(attacker, defender, damageType, baseDamage, true, false, true);
    }

    /**
     * Constructor using attacker's base stats for damage.
     */
    public AttackRequest(Character attacker, Character defender, DamageType damageType) {
        this(attacker, defender, damageType, attacker.getStats().getAttackPower(), true, false, true);
    }

    // ===== Getters =====

    public Character getAttacker() {
        return attacker;
    }

    public Character getDefender() {
        return defender;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public boolean canCritical() {
        return canCritical;
    }

    public boolean ignoresDefense() {
        return ignoresDefense;
    }

    public boolean canMiss() {
        return canMiss;
    }

    // ===== Builder-style modifiers (return new instances) =====

    public AttackRequest withBaseDamage(int newBaseDamage) {
        return new AttackRequest(attacker, defender, damageType, newBaseDamage,
                                canCritical, ignoresDefense, canMiss);
    }

    public AttackRequest withCanCritical(boolean newCanCritical) {
        return new AttackRequest(attacker, defender, damageType, baseDamage,
                                newCanCritical, ignoresDefense, canMiss);
    }

    public AttackRequest withIgnoresDefense(boolean newIgnoresDefense) {
        return new AttackRequest(attacker, defender, damageType, baseDamage,
                                canCritical, newIgnoresDefense, canMiss);
    }

    public AttackRequest withCanMiss(boolean newCanMiss) {
        return new AttackRequest(attacker, defender, damageType, baseDamage,
                                canCritical, ignoresDefense, newCanMiss);
    }

    @Override
    public String toString() {
        return String.format("AttackRequest{%s -> %s, %s, dmg=%d, crit=%b, ignoreDef=%b, canMiss=%b}",
            attacker.getId(), defender.getId(), damageType, baseDamage,
            canCritical, ignoresDefense, canMiss);
    }
}
