package com.shatteredpixel.engine.actor.buff;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.serialization.dto.BuffSnapshot;

/**
 * Abstract base class for all buff implementations.
 *
 * Provides default implementations for:
 * - Duration tracking and expiration logic
 * - No-op lifecycle hooks (onApply/onRemove/onTick)
 * - Snapshot creation using buffType and duration
 *
 * Subclasses only need to:
 * - Implement getBuffType() returning their unique identifier
 * - Override onTick() for per-turn effects (optional)
 * - Override onApply()/onRemove() for initialization/cleanup (optional)
 * - Register a factory in BuffRegistry for deserialization
 *
 * Example:
 * <pre>
 * public class PoisonBuff extends BaseBuff {
 *     public PoisonBuff(int duration) {
 *         super(duration);
 *     }
 *
 *     &#64;Override
 *     public String getBuffType() {
 *         return "poison";
 *     }
 *
 *     &#64;Override
 *     public void onTick(EngineContext context, Actor actor) {
 *         // Apply poison damage
 *         actor.takeDamage(context, 2);
 *     }
 * }
 * </pre>
 */
public abstract class BaseBuff implements Buff {

    /**
     * Remaining duration in turns.
     *
     * Decremented by 1 each turn via reduceDuration().
     * Buff is removed when duration <= 0.
     */
    protected int duration;

    /**
     * Create a buff with specified duration.
     *
     * @param duration Initial duration in turns (must be > 0)
     */
    public BaseBuff(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Buff duration must be positive, got: " + duration);
        }
        this.duration = duration;
    }

    /**
     * Default no-op implementation.
     *
     * Override in subclasses for initialization logic.
     */
    @Override
    public void onApply(EngineContext context, Actor actor) {
        // Default: no-op
    }

    /**
     * Default no-op implementation.
     *
     * Override in subclasses for cleanup logic.
     */
    @Override
    public void onRemove(EngineContext context, Actor actor) {
        // Default: no-op
    }

    /**
     * Default no-op implementation.
     *
     * Override in subclasses for per-turn effects.
     */
    @Override
    public void onTick(EngineContext context, Actor actor) {
        // Default: no-op
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void reduceDuration(int turns) {
        duration -= turns;
    }

    /**
     * Subclasses must implement this to return their unique type identifier.
     *
     * @return Stable buff type identifier (e.g., "poison", "regeneration")
     */
    @Override
    public abstract String getBuffType();

    /**
     * Create a basic snapshot with buffType and duration.
     *
     * Subclasses with additional state should override this method
     * and create custom BuffSnapshot subclasses.
     */
    @Override
    public BuffSnapshot createSnapshot() {
        return new BuffSnapshot(getBuffType(), duration);
    }
}
