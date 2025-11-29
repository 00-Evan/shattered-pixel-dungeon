package com.shatteredpixel.engine.actor.buff;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.serialization.dto.BuffSnapshot;

/**
 * Core interface for all buffs (temporary effects) on actors.
 *
 * Buffs are duration-based effects that can modify actor behavior,
 * apply damage over time, heal over time, or provide temporary stat changes.
 *
 * Design principles:
 * - Deterministic: Same seed + same commands → same buff results
 * - Serializable: Full state captured in BuffSnapshot
 * - GWT-safe: No reflection, pure data structures
 * - Turn-based: Duration decrements each turn via tickAll()
 *
 * Lifecycle:
 * 1. Buff created and added to actor's BuffContainer
 * 2. onApply() called when buff is first added
 * 3. onTick() called each turn before duration reduction
 * 4. reduceDuration() called to decrement duration
 * 5. onRemove() called when buff expires or is removed
 *
 * Subclasses must:
 * - Implement getBuffType() returning stable string identifier
 * - Register their factory in BuffRegistry for deserialization
 * - Override onTick() for per-turn effects (optional)
 * - Override onApply()/onRemove() for initialization/cleanup (optional)
 */
public interface Buff {

    /**
     * Called when this buff is first applied to an actor.
     *
     * Use this for initialization logic (e.g., applying initial stat changes).
     *
     * @param context Current engine context
     * @param actor Actor receiving this buff
     */
    void onApply(EngineContext context, Actor actor);

    /**
     * Called when this buff is removed from an actor.
     *
     * Use this for cleanup logic (e.g., removing stat changes).
     *
     * @param context Current engine context
     * @param actor Actor losing this buff
     */
    void onRemove(EngineContext context, Actor actor);

    /**
     * Called each turn before duration reduction.
     *
     * Use this for per-turn effects (e.g., poison damage, healing).
     *
     * @param context Current engine context
     * @param actor Actor with this buff
     */
    void onTick(EngineContext context, Actor actor);

    /**
     * Check if this buff has expired.
     *
     * A buff is expired when duration <= 0.
     *
     * @return true if buff should be removed
     */
    boolean isExpired();

    /**
     * Get remaining duration in turns.
     *
     * @return Number of turns remaining (0 = expired this turn)
     */
    int getDuration();

    /**
     * Reduce buff duration by specified number of turns.
     *
     * Called automatically by BuffContainer.tickAll().
     *
     * @param turns Number of turns to reduce (typically 1)
     */
    void reduceDuration(int turns);

    /**
     * Get stable string identifier for this buff type.
     *
     * This identifier is used for:
     * - Serialization (stored in BuffSnapshot)
     * - Deserialization (lookup in BuffRegistry)
     * - Buff stacking/replacement logic
     *
     * Must be:
     * - Stable across versions
     * - Unique per buff type
     * - Lowercase with no spaces (e.g., "poison", "regeneration")
     *
     * @return Stable buff type identifier
     */
    String getBuffType();

    /**
     * Create a serialization snapshot of this buff's state.
     *
     * The snapshot captures all data needed to recreate this buff
     * during deserialization (e.g., after save/load).
     *
     * @return Immutable snapshot of buff state
     */
    BuffSnapshot createSnapshot();
}
