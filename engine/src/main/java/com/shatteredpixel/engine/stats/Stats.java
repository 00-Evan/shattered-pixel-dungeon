package com.shatteredpixel.engine.stats;

/**
 * A data class holding basic numeric attributes for characters.
 * Stats represent the core capabilities of a character that can participate in combat.
 *
 * This class is mutable to allow for stat modifications during gameplay
 * (level ups, equipment changes, temporary buffs, etc.).
 *
 * All fields are primitives for GWT compatibility and easy serialization.
 */
public class Stats {

    private int maxHealth;
    private int attackPower;
    private int defense;
    private int accuracy;
    private int evasion;
    private float speed; // Affects action time costs (1.0 = normal speed)

    /**
     * Create stats with all attributes specified.
     */
    public Stats(int maxHealth, int attackPower, int defense, int accuracy, int evasion, float speed) {
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.speed = speed;
    }

    /**
     * Create basic stats with only max health.
     * Other stats default to 0 (except speed = 1.0).
     */
    public Stats(int maxHealth) {
        this(maxHealth, 0, 0, 0, 0, 1.0f);
    }

    // ===== Getters =====

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getEvasion() {
        return evasion;
    }

    public float getSpeed() {
        return speed;
    }

    // ===== Setters =====

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    // ===== Modification Methods (Additive) =====

    public void modifyMaxHealth(int delta) {
        this.maxHealth += delta;
    }

    public void modifyAttackPower(int delta) {
        this.attackPower += delta;
    }

    public void modifyDefense(int delta) {
        this.defense += delta;
    }

    public void modifyAccuracy(int delta) {
        this.accuracy += delta;
    }

    public void modifyEvasion(int delta) {
        this.evasion += delta;
    }

    public void modifySpeed(float delta) {
        this.speed += delta;
    }

    // ===== Utility =====

    /**
     * Create a deep copy of these stats.
     * Useful for creating stat snapshots or modified copies.
     */
    public Stats copy() {
        return new Stats(maxHealth, attackPower, defense, accuracy, evasion, speed);
    }

    @Override
    public String toString() {
        return String.format("Stats{HP=%d, ATK=%d, DEF=%d, ACC=%d, EVA=%d, SPD=%.2f}",
            maxHealth, attackPower, defense, accuracy, evasion, speed);
    }
}
