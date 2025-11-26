package com.shatteredpixel.engine.replay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable container for an ordered sequence of per-turn state hashes.
 *
 * This represents the complete hash timeline for a game session,
 * captured either during:
 * - Direct execution (run engine, hash after each turn)
 * - Replay execution (replay trace, hash after each turn)
 * - Save/load execution (run with save/load boundaries)
 *
 * TurnHashSequence enables:
 * - Comparing hash sequences between different runs
 * - Detecting exact turn where desync occurs
 * - Verifying determinism across execution modes
 * - Validating save/load doesn't corrupt state
 *
 * Design principles:
 * - Immutable (defensive copy on construction)
 * - Ordered (preserves turn sequence)
 * - Comparable (equality checks for determinism tests)
 * - Human-readable (toString shows summary)
 *
 * Usage:
 * <pre>
 * // Collect hashes during run
 * List<TurnHashRecord> records = new ArrayList<>();
 * for (int turn = 0; turn < 10; turn++) {
 *     engine.processNextTurn(...);
 *     StateHash hash = StateHasher.hashState(engine);
 *     records.add(new TurnHashRecord(turn, hash));
 * }
 *
 * // Create sequence
 * TurnHashSequence sequence = new TurnHashSequence(records);
 *
 * // Compare with another sequence
 * boolean matches = sequence.equals(otherSequence);
 * </pre>
 */
public final class TurnHashSequence {

    private final List<TurnHashRecord> records;

    /**
     * Create a turn hash sequence from a list of records.
     *
     * @param records List of turn hash records (defensive copy made)
     */
    public TurnHashSequence(List<TurnHashRecord> records) {
        if (records == null) {
            throw new IllegalArgumentException("Records cannot be null");
        }

        // Defensive copy to ensure immutability
        this.records = Collections.unmodifiableList(new ArrayList<>(records));
    }

    /**
     * Get the number of turns in this sequence.
     *
     * @return Turn count
     */
    public int size() {
        return records.size();
    }

    /**
     * Check if this sequence is empty.
     *
     * @return true if no records
     */
    public boolean isEmpty() {
        return records.isEmpty();
    }

    /**
     * Get all turn hash records.
     *
     * @return Immutable list of records
     */
    public List<TurnHashRecord> getRecords() {
        return records;
    }

    /**
     * Get a specific turn hash record by index.
     *
     * @param index Turn index (0-based)
     * @return TurnHashRecord at that index
     * @throws IndexOutOfBoundsException if index out of range
     */
    public TurnHashRecord get(int index) {
        return records.get(index);
    }

    /**
     * Check if all hashes in this sequence match another sequence.
     *
     * This is the primary method for verifying determinism:
     * - Same turn count
     * - Same turn indices
     * - Same state hashes
     *
     * @param other Other sequence to compare
     * @return true if sequences are identical
     */
    public boolean matchesHashes(TurnHashSequence other) {
        if (other == null) {
            return false;
        }

        if (this.size() != other.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            TurnHashRecord thisRecord = this.get(i);
            TurnHashRecord otherRecord = other.get(i);

            if (thisRecord.getTurnIndex() != otherRecord.getTurnIndex()) {
                return false;
            }

            if (!thisRecord.getStateHash().equals(otherRecord.getStateHash())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Find the first turn where hashes differ from another sequence.
     *
     * This helps identify the exact point where a desync occurred.
     *
     * @param other Other sequence to compare
     * @return Turn index where hashes first differ, or -1 if all match
     */
    public int findFirstDifference(TurnHashSequence other) {
        if (other == null) {
            return 0; // Null is different from turn 0
        }

        int maxTurns = Math.min(this.size(), other.size());

        for (int i = 0; i < maxTurns; i++) {
            TurnHashRecord thisRecord = this.get(i);
            TurnHashRecord otherRecord = other.get(i);

            if (!thisRecord.getStateHash().equals(otherRecord.getStateHash())) {
                return i; // First difference at turn i
            }
        }

        // All compared turns match; check if lengths differ
        if (this.size() != other.size()) {
            return maxTurns; // Difference at first extra turn
        }

        return -1; // No differences
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TurnHashSequence)) return false;
        TurnHashSequence other = (TurnHashSequence) obj;
        return this.records.equals(other.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }

    @Override
    public String toString() {
        if (records.isEmpty()) {
            return "TurnHashSequence{empty}";
        }

        TurnHashRecord first = records.get(0);
        TurnHashRecord last = records.get(records.size() - 1);

        return String.format("TurnHashSequence{turns=%d, first=%s, last=%s}",
            records.size(),
            first.getStateHash().toHexString(),
            last.getStateHash().toHexString());
    }

    /**
     * Get a detailed string representation showing all hashes.
     *
     * Useful for debugging when sequences don't match.
     *
     * @return Multi-line string with all turn hashes
     */
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TurnHashSequence{\n");
        for (TurnHashRecord record : records) {
            sb.append("  ").append(record).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
