package com.shatteredpixel.engine.actor.hero;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Generic concrete hero implementation for serialization/testing.
 *
 * This is a minimal hero class used for:
 * - Deserialization (restoring heroes from save files)
 * - Testing
 * - Placeholder before concrete hero classes are created
 *
 * This class has no special behavior - just implements the abstract
 * methods from BaseHero with minimal logic.
 *
 * In production, specific hero classes (Warrior, Mage, etc.) would
 * be used instead, with their own serialization logic.
 */
public class GenericHero extends BaseHero {

    /**
     * Create a generic hero with position, stats, and name.
     *
     * @param position Initial position
     * @param stats Character stats
     * @param name Hero's name
     */
    public GenericHero(Point position, Stats stats, String name) {
        super(position, stats, name);
    }

    /**
     * Create a generic hero with specific current health.
     * Used for deserialization/restoration.
     *
     * @param position Initial position
     * @param stats Character stats
     * @param currentHealth Current health value
     * @param name Hero's name
     */
    public GenericHero(Point position, Stats stats, int currentHealth, String name) {
        super(position, stats, currentHealth, name);
    }

    @Override
    public float act(EngineContext context) {
        // Generic hero just waits - actual behavior driven by external commands
        return 1.0f;
    }
}
