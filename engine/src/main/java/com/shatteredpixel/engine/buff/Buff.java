package com.shatteredpixel.engine.buff;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Character;

/**
 * Abstract base class for buffs/status effects.
 *
 * A buff represents a temporary or permanent modifier attached to a character.
 * Buffs have lifecycle hooks (onApply, onRemove, onTurnStart, onTurnEnd) and
 * can optionally have a duration.
 *
 * This is a generic framework - concrete buff implementations will provide
 * actual game effects (damage over time, stat modifiers, etc.).
 *
 * Design principles:
 * - Generic and game-agnostic (no specific buff types)
 * - Deterministic and GWT-safe
 * - Simple fields for serialization
 * - No global state
 *
 * Duration conventions:
 * - -1: Permanent (never expires)
 * - 0: Expired (will be removed)
 * - >0: Remaining turns
 */
public abstract class Buff {

    private final BuffId id;
    private final BuffType type;

    // Duration in turns (-1 = permanent, 0 = expired, >0 = remaining turns)
    private int duration;

    /**
     * Create a new buff with specified type and duration.
     *
     * @param type The buff type classification
     * @param duration Duration in turns (-1 for permanent)
     */
    protected Buff(BuffType type, int duration) {
        this.id = BuffId.generate();
        this.type = type;
        this.duration = duration;
    }

    /**
     * Create a permanent buff (duration = -1).
     */
    protected Buff(BuffType type) {
        this(type, -1);
    }

    // ===== Identity =====

    public BuffId getId() {
        return id;
    }

    public BuffType getType() {
        return type;
    }

    // ===== Duration Management =====

    /**
     * Get remaining duration in turns.
     * -1 = permanent, 0 = expired, >0 = remaining turns.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set duration directly (useful for loading saved state).
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Check if this buff is permanent (never expires).
     */
    public boolean isPermanent() {
        return duration == -1;
    }

    /**
     * Check if this buff has expired (duration reached 0).
     */
    public boolean isExpired() {
        return duration == 0;
    }

    /**
     * Decrease duration by the given amount.
     * Does nothing for permanent buffs.
     *
     * @param amount Amount to decrease (typically 1 per turn)
     */
    public void tickDuration(int amount) {
        if (duration > 0) {
            duration = Math.max(0, duration - amount);
        }
    }

    // ===== Lifecycle Hooks =====

    /**
     * Called when this buff is applied to a character.
     * Override to implement initial effects (e.g., stat modifiers, visual effects).
     *
     * Default implementation does nothing.
     *
     * @param target The character receiving this buff
     * @param context Engine context for RNG, events, etc.
     */
    public void onApply(Character target, EngineContext context) {
        // Default: no effect
    }

    /**
     * Called when this buff is removed from a character.
     * Override to implement cleanup (e.g., revert stat modifiers, visual effects).
     *
     * Default implementation does nothing.
     *
     * @param target The character losing this buff
     * @param context Engine context
     */
    public void onRemove(Character target, EngineContext context) {
        // Default: no effect
    }

    /**
     * Called at the start of the target's turn.
     * Override to implement turn-start effects (e.g., regeneration, damage ticks).
     *
     * Default implementation does nothing.
     *
     * @param target The character with this buff
     * @param context Engine context
     */
    public void onTurnStart(Character target, EngineContext context) {
        // Default: no effect
    }

    /**
     * Called at the end of the target's turn.
     * Override to implement turn-end effects (e.g., duration decrease, end-of-turn damage).
     *
     * Default implementation ticks duration for temporary buffs.
     *
     * @param target The character with this buff
     * @param context Engine context
     */
    public void onTurnEnd(Character target, EngineContext context) {
        // Default: tick duration for temporary buffs
        if (!isPermanent()) {
            tickDuration(1);
        }
    }

    // ===== Utility =====

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Buff)) return false;
        Buff other = (Buff) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        String durationStr = isPermanent() ? "∞" : (isExpired() ? "EXPIRED" : String.valueOf(duration));
        return String.format("%s{id=%s, type=%s, duration=%s}",
            getClass().getSimpleName(), id, type, durationStr);
    }
}
