package com.shatteredpixel.engine.replay;

import java.util.Objects;

/**
 * Immutable value object capturing the state hash for a single turn.
 *
 * This records:
 * - Turn index (0-based)
 * - State hash after processing this turn
 * - Optional: cumulative command count (for debugging)
 *
 * A sequence of TurnHashRecord entries forms a complete hash timeline
 * for a game session, allowing:
 * - Per-turn state comparison between runs
 * - Exact desync detection (first turn where hashes differ)
 * - Determinism verification
 * - Replay validation
 *
 * Design principles:
 * - Immutable (thread-safe)
 * - Value-based equality
 * - Lightweight (just metadata + hash)
 * - Human-readable toString()
 */
public final class TurnHashRecord {

    private final int turnIndex;
    private final StateHash stateHash;
    private final int totalCommandsSoFar;

    /**
     * Create a turn hash record with command count.
     *
     * @param turnIndex Turn number (0-based)
     * @param stateHash State hash after this turn
     * @param totalCommandsSoFar Cumulative commands executed (for debugging)
     */
    public TurnHashRecord(int turnIndex, StateHash stateHash, int totalCommandsSoFar) {
        if (stateHash == null) {
            throw new IllegalArgumentException("State hash cannot be null");
        }
        if (turnIndex < 0) {
            throw new IllegalArgumentException("Turn index cannot be negative");
        }

        this.turnIndex = turnIndex;
        this.stateHash = stateHash;
        this.totalCommandsSoFar = totalCommandsSoFar;
    }

    /**
     * Create a turn hash record without command count.
     *
     * @param turnIndex Turn number (0-based)
     * @param stateHash State hash after this turn
     */
    public TurnHashRecord(int turnIndex, StateHash stateHash) {
        this(turnIndex, stateHash, 0);
    }

    /**
     * Get the turn index.
     *
     * @return Turn number (0-based)
     */
    public int getTurnIndex() {
        return turnIndex;
    }

    /**
     * Get the state hash for this turn.
     *
     * @return StateHash instance
     */
    public StateHash getStateHash() {
        return stateHash;
    }

    /**
     * Get the cumulative command count.
     *
     * @return Total external commands executed up to this turn
     */
    public int getTotalCommandsSoFar() {
        return totalCommandsSoFar;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TurnHashRecord)) return false;
        TurnHashRecord other = (TurnHashRecord) obj;
        return this.turnIndex == other.turnIndex
            && this.stateHash.equals(other.stateHash)
            && this.totalCommandsSoFar == other.totalCommandsSoFar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnIndex, stateHash, totalCommandsSoFar);
    }

    @Override
    public String toString() {
        return String.format("TurnHashRecord{turn=%d, hash=%s, cmds=%d}",
            turnIndex, stateHash.toHexString(), totalCommandsSoFar);
    }
}
