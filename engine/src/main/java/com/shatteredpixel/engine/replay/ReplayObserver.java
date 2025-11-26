package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.event.GameEvent;

import java.util.List;

/**
 * Functional interface for observing replay execution turn-by-turn.
 *
 * Allows tests and tools to inspect engine state and events after
 * each turn during replay.
 *
 * Common use cases:
 * - Verify state matches expected values
 * - Collect state snapshots for comparison
 * - Log events for debugging
 * - Compute checksums for determinism verification
 *
 * The observer is called AFTER each turn's commands have been executed
 * and events generated.
 */
@FunctionalInterface
public interface ReplayObserver {

    /**
     * Called after each turn during replay.
     *
     * @param turnIndex The turn number (0-based)
     * @param context Current engine context
     * @param engine Current game engine
     * @param eventsThisTurn Events generated during this turn
     */
    void afterTurn(int turnIndex,
                   EngineContext context,
                   GameEngine engine,
                   List<GameEvent> eventsThisTurn);
}
