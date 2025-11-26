package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.event.GameEvent;

import java.util.List;

/**
 * Utility for replaying recorded game sessions.
 *
 * This class:
 * - Creates a fresh engine with the trace's RNG seed
 * - Applies commands from the trace turn-by-turn
 * - Optionally notifies an observer after each turn
 * - Returns the final engine state
 *
 * The replay should produce identical results to the original session
 * due to deterministic RNG and same command sequence.
 *
 * Usage:
 * <pre>
 * ReplayTrace trace = loadTrace();
 * EngineFactory factory = (seed) -> createEngine(seed);
 *
 * ReplayRunner runner = new ReplayRunner();
 * ReplayResult result = runner.replay(trace, factory, (turn, ctx, eng, events) -> {
 *     // Observe state after each turn
 *     System.out.println("Turn " + turn + ": " + events.size() + " events");
 * });
 * </pre>
 */
public class ReplayRunner {

    /**
     * Replay a recorded session.
     *
     * @param trace The replay trace to execute
     * @param engineFactory Factory to create fresh engine instances
     * @param observer Optional observer to call after each turn (can be null)
     * @return ReplayResult containing final engine state
     */
    public ReplayResult replay(
        ReplayTrace trace,
        EngineFactory engineFactory,
        ReplayObserver observer
    ) {
        if (trace == null) {
            throw new IllegalArgumentException("Trace cannot be null");
        }
        if (engineFactory == null) {
            throw new IllegalArgumentException("Engine factory cannot be null");
        }

        // Create fresh engine with trace's seed
        EngineContextWithEngine pair = engineFactory.create(trace.getSeed());
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Replay each turn
        int turnsExecuted = 0;
        for (ReplayTurnInput turnInput : trace.getTurns()) {
            // Execute turn with recorded commands
            List<GameEvent> events = engine.processNextTurn(turnInput.getCommands());

            // Notify observer if provided
            if (observer != null) {
                observer.afterTurn(turnInput.getTurnIndex(), context, engine, events);
            }

            turnsExecuted++;
        }

        // Return final state
        return new ReplayResult(context, engine, turnsExecuted);
    }

    /**
     * Replay without an observer.
     *
     * @param trace The replay trace to execute
     * @param engineFactory Factory to create fresh engine instances
     * @return ReplayResult containing final engine state
     */
    public ReplayResult replay(ReplayTrace trace, EngineFactory engineFactory) {
        return replay(trace, engineFactory, null);
    }
}
