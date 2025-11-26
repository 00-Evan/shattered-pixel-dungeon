package com.shatteredpixel.engine.replay;

import java.util.Objects;

/**
 * Immutable value object representing a deterministic state hash.
 *
 * This captures a 64-bit hash of the complete engine state at a specific
 * point in time. The hash is computed using the FNV-1a-64 algorithm, which:
 * - Is deterministic (same input → same output)
 * - Is platform-independent (stable across JVMs)
 * - Is GWT-safe (no native dependencies)
 * - Has good distribution properties
 *
 * State hashes enable:
 * - Detecting exact turn where desync occurs
 * - Verifying determinism across runs
 * - Comparing replay execution to original
 * - Validating save/load doesn't alter state
 *
 * The hash is computed from the serialized engine state (byte[] from
 * GameEngine.saveState()), ensuring it reflects ALL engine state including:
 * - RNG seed
 * - Level terrain and flags
 * - All actor positions, stats, health
 * - Scheduler state
 *
 * Design principles:
 * - Immutable (thread-safe)
 * - Value-based equality
 * - Human-readable toString()
 * - No engine dependencies
 */
public final class StateHash {

    private static final String DEFAULT_ALGORITHM = "FNV-1a-64";

    private final long value;
    private final String algorithm;

    /**
     * Create a state hash with the default algorithm.
     *
     * @param value 64-bit hash value
     */
    public StateHash(long value) {
        this(value, DEFAULT_ALGORITHM);
    }

    /**
     * Create a state hash with a specific algorithm name.
     *
     * @param value 64-bit hash value
     * @param algorithm Algorithm name (for debugging/metadata)
     */
    public StateHash(long value, String algorithm) {
        this.value = value;
        this.algorithm = algorithm != null ? algorithm : DEFAULT_ALGORITHM;
    }

    /**
     * Get the hash value as a long.
     *
     * @return 64-bit hash
     */
    public long asLong() {
        return value;
    }

    /**
     * Get the algorithm name.
     *
     * @return Algorithm identifier
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Convenience method to create hash from raw bytes.
     * Uses FNV-1a-64 algorithm.
     *
     * @param data Bytes to hash
     * @return StateHash instance
     */
    public static StateHash fromBytes(byte[] data) {
        // Delegate to StateHasher to avoid duplication
        // This is just a convenience wrapper
        if (data == null || data.length == 0) {
            return new StateHash(0L);
        }

        // Compute FNV-1a-64 inline
        final long FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
        final long FNV_PRIME = 0x100000001b3L;

        long hash = FNV_OFFSET_BASIS;
        for (byte b : data) {
            hash ^= (b & 0xff);
            hash *= FNV_PRIME;
        }

        return new StateHash(hash);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StateHash)) return false;
        StateHash other = (StateHash) obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("StateHash{0x%016x, algo=%s}", value, algorithm);
    }

    /**
     * Get a compact hex string representation.
     *
     * @return Hex string (16 characters)
     */
    public String toHexString() {
        return String.format("%016x", value);
    }
}
