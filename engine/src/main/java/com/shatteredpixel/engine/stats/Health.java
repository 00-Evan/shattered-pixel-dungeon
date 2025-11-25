package com.shatteredpixel.engine.stats;

/**
 * Component that manages current and maximum health for a character.
 *
 * Tracks the life state of an entity and provides methods for damage and healing.
 * Health cannot exceed maxHealth or go below 0.
 *
 * This class is mutable and designed for easy serialization.
 */
public class Health {

    private int currentHealth;
    private int maxHealth;

    /**
     * Create a health component starting at full health.
     */
    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * Create a health component with specific current and max values.
     * Current health is clamped between 0 and maxHealth.
     */
    public Health(int currentHealth, int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = Math.max(0, Math.min(currentHealth, maxHealth));
    }

    // ===== Accessors =====

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Set maximum health. If current health exceeds new max, it is clamped down.
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     * Set current health directly (useful for loading saved state).
     * Value is clamped between 0 and maxHealth.
     */
    public void setCurrentHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth));
    }

    // ===== State Queries =====

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    /**
     * Get the percentage of health remaining (0.0 to 1.0).
     */
    public float getHealthPercent() {
        if (maxHealth <= 0) return 0.0f;
        return (float) currentHealth / maxHealth;
    }

    /**
     * Get missing health (maxHealth - currentHealth).
     */
    public int getMissingHealth() {
        return maxHealth - currentHealth;
    }

    // ===== Damage & Healing =====

    /**
     * Apply damage to this health component.
     *
     * @param amount The amount of damage to apply (must be non-negative)
     * @return The actual damage taken (clamped to available health)
     * @throws IllegalArgumentException if amount is negative
     */
    public int takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage amount must be non-negative: " + amount);
        }

        int actualDamage = Math.min(amount, currentHealth);
        currentHealth -= actualDamage;
        return actualDamage;
    }

    /**
     * Heal this health component.
     *
     * @param amount The amount of healing to apply (must be non-negative)
     * @return The actual healing received (clamped to missing health)
     * @throws IllegalArgumentException if amount is negative
     */
    public int heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Heal amount must be non-negative: " + amount);
        }

        int missingHealth = maxHealth - currentHealth;
        int actualHealing = Math.min(amount, missingHealth);
        currentHealth += actualHealing;
        return actualHealing;
    }

    /**
     * Fully restore health to maximum.
     */
    public void fullRestore() {
        currentHealth = maxHealth;
    }

    // ===== Utility =====

    @Override
    public String toString() {
        return String.format("Health{%d/%d (%.0f%%)}",
            currentHealth, maxHealth, getHealthPercent() * 100);
    }
}
