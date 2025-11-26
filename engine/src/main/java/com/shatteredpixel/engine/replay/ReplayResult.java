package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;

/**
 * Result object from replay execution.
 *
 * Contains the final state of the engine after all replay turns
 * have been executed.
 *
 * This allows tests to:
 * - Inspect final actor positions/health
 * - Verify final level state
 * - Compare against expected outcomes
 * - Extract final RNG seed for continued execution
 *
 * Future enhancements:
 * - Per-turn state snapshots
 * - Per-turn event logs
 * - State checksums for each turn
 * - Performance metrics (time per turn, etc.)
 */
public class ReplayResult {

    private final EngineContext finalContext;
    private final GameEngine finalEngine;
    private final int turnsExecuted;

    /**
     * Create a replay result.
     *
     * @param finalContext Final engine context after replay
     * @param finalEngine Final game engine after replay
     * @param turnsExecuted Number of turns executed
     */
    public ReplayResult(EngineContext finalContext, GameEngine finalEngine, int turnsExecuted) {
        if (finalContext == null) {
            throw new IllegalArgumentException("Final context cannot be null");
        }
        if (finalEngine == null) {
            throw new IllegalArgumentException("Final engine cannot be null");
        }
        this.finalContext = finalContext;
        this.finalEngine = finalEngine;
        this.turnsExecuted = turnsExecuted;
    }

    /**
     * Get the final engine context.
     *
     * @return EngineContext after all turns executed
     */
    public EngineContext getFinalContext() {
        return finalContext;
    }

    /**
     * Get the final game engine.
     *
     * @return GameEngine after all turns executed
     */
    public GameEngine getFinalEngine() {
        return finalEngine;
    }

    /**
     * Get the number of turns executed.
     *
     * @return Turn count
     */
    public int getTurnsExecuted() {
        return turnsExecuted;
    }

    @Override
    public String toString() {
        return "ReplayResult{" +
            "turnsExecuted=" + turnsExecuted +
            ", actors=" + finalContext.getActors().size() +
            ", seed=" + finalContext.getRNG().getSeed() +
            "}";
    }
}
