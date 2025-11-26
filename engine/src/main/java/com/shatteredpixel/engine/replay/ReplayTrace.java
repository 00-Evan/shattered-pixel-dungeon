package com.shatteredpixel.engine.replay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable container representing a complete replay trace.
 *
 * A replay trace captures everything needed to deterministically replay
 * a game session:
 * - RNG seed (for deterministic randomness)
 * - Start turn index (usually 0)
 * - Ordered sequence of external commands per turn
 *
 * This is the "script" that can be used to:
 * - Replay a game session exactly
 * - Verify determinism (same inputs → same outputs)
 * - Debug gameplay issues
 * - Share replays with others
 * - Test save/load boundaries
 *
 * The trace contains ONLY external inputs (player commands).
 * AI decisions are NOT recorded - they are deterministically
 * regenerated from the RNG seed and game state during replay.
 *
 * Future enhancements:
 * - Add metadata (game version, timestamp, player name)
 * - Add checksums for validation
 * - Support for compressed traces
 * - Binary serialization format
 */
public class ReplayTrace {

    private final long seed;
    private final int startTurnIndex;
    private final List<ReplayTurnInput> turns;

    /**
     * Create a replay trace.
     *
     * @param seed RNG seed used for this replay
     * @param startTurnIndex Starting turn index (usually 0)
     * @param turns Ordered list of turn inputs (defensive copy made)
     */
    public ReplayTrace(long seed, int startTurnIndex, List<ReplayTurnInput> turns) {
        this.seed = seed;
        this.startTurnIndex = startTurnIndex;
        // Defensive copy to ensure immutability
        this.turns = turns != null
            ? Collections.unmodifiableList(new ArrayList<>(turns))
            : Collections.emptyList();
    }

    /**
     * Get the RNG seed.
     *
     * @return Deterministic RNG seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Get the starting turn index.
     *
     * @return Start turn (usually 0)
     */
    public int getStartTurnIndex() {
        return startTurnIndex;
    }

    /**
     * Get all turn inputs.
     *
     * @return Immutable list of turn inputs
     */
    public List<ReplayTurnInput> getTurns() {
        return turns;
    }

    /**
     * Get the number of turns in this trace.
     *
     * @return Turn count
     */
    public int getTurnCount() {
        return turns.size();
    }

    /**
     * Get the total number of commands across all turns.
     *
     * @return Total command count
     */
    public int getTotalCommandCount() {
        int count = 0;
        for (ReplayTurnInput turn : turns) {
            count += turn.getCommands().size();
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReplayTrace)) return false;
        ReplayTrace other = (ReplayTrace) obj;
        return this.seed == other.seed
            && this.startTurnIndex == other.startTurnIndex
            && this.turns.equals(other.turns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed, startTurnIndex, turns);
    }

    @Override
    public String toString() {
        return "ReplayTrace{" +
            "seed=" + seed +
            ", startTurn=" + startTurnIndex +
            ", turns=" + turns.size() +
            ", totalCommands=" + getTotalCommandCount() +
            "}";
    }
}
