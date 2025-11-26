package com.shatteredpixel.engine.movement;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Minimal Character implementation for testing.
 *
 * Does nothing on act() - just returns a default time cost.
 * Used to test movement and collision without requiring full game logic.
 */
public class TestCharacter extends Character {

    public TestCharacter(Point position, Stats stats) {
        super(ActorType.OTHER, position, stats);
    }

    public TestCharacter(Point position) {
        this(position, new Stats(100)); // Default 100 HP
    }

    @Override
    public float act(EngineContext context) {
        // Do nothing - just return default time cost
        return 1.0f;
    }
}
