package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.command.GameCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for recording game sessions into replay traces.
 *
 * This class:
 * - Creates an engine with a deterministic RNG seed
 * - Executes turns while applying external commands
 * - Captures the command sequence into a ReplayTrace
 *
 * The resulting ReplayTrace can be used to:
 * - Replay the exact same session
 * - Verify determinism
 * - Debug issues
 * - Share replays
 *
 * Usage:
 * <pre>
 * EngineFactory factory = (seed) -> createEngine(seed);
 * List<List<GameCommand>> commandsPerTurn = prepareCommands();
 *
 * ReplayRecorder recorder = new ReplayRecorder();
 * ReplayTrace trace = recorder.recordRun(
 *     42L,              // seed
 *     0,                // start turn
 *     10,               // turn count
 *     commandsPerTurn,  // commands
 *     factory           // engine factory
 * );
 * </pre>
 */
public class ReplayRecorder {

    /**
     * Record a game session into a replay trace.
     *
     * @param seed Deterministic RNG seed
     * @param startTurnIndex Starting turn index (usually 0)
     * @param turnCount Number of turns to record
     * @param externalCommandsPerTurn Commands for each turn (indexed by turn)
     * @param engineFactory Factory to create engine instances
     * @return ReplayTrace containing the recorded session
     */
    public ReplayTrace recordRun(
        long seed,
        int startTurnIndex,
        int turnCount,
        List<List<GameCommand>> externalCommandsPerTurn,
        EngineFactory engineFactory
    ) {
        if (engineFactory == null) {
            throw new IllegalArgumentException("Engine factory cannot be null");
        }
        if (turnCount < 0) {
            throw new IllegalArgumentException("Turn count cannot be negative");
        }
        if (externalCommandsPerTurn == null) {
            throw new IllegalArgumentException("External commands cannot be null");
        }

        // Create engine with deterministic seed
        EngineContextWithEngine pair = engineFactory.create(seed);
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Record each turn
        List<ReplayTurnInput> turnInputs = new ArrayList<>();

        for (int i = 0; i < turnCount; i++) {
            int turnIndex = startTurnIndex + i;

            // Get commands for this turn (or empty list if not provided)
            List<GameCommand> commandsThisTurn = (i < externalCommandsPerTurn.size())
                ? externalCommandsPerTurn.get(i)
                : new ArrayList<>();

            // Record the input for this turn
            ReplayTurnInput turnInput = new ReplayTurnInput(turnIndex, commandsThisTurn);
            turnInputs.add(turnInput);

            // Execute the turn
            engine.processNextTurn(commandsThisTurn);
        }

        // Build and return trace
        return new ReplayTrace(seed, startTurnIndex, turnInputs);
    }

    /**
     * Convenience method for recording from turn 0.
     *
     * @param seed Deterministic RNG seed
     * @param turnCount Number of turns to record
     * @param externalCommandsPerTurn Commands for each turn
     * @param engineFactory Factory to create engine instances
     * @return ReplayTrace containing the recorded session
     */
    public ReplayTrace recordRun(
        long seed,
        int turnCount,
        List<List<GameCommand>> externalCommandsPerTurn,
        EngineFactory engineFactory
    ) {
        return recordRun(seed, 0, turnCount, externalCommandsPerTurn, engineFactory);
    }
}
