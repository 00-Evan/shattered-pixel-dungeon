package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.GameEngine;

/**
 * Static utility for computing deterministic state hashes from engine state.
 *
 * This class provides methods to:
 * - Hash complete engine state via GameEngine.saveState()
 * - Hash raw byte arrays
 * - Produce stable, deterministic 64-bit hashes
 *
 * Hash algorithm: FNV-1a-64
 * - Fast, simple, deterministic
 * - Good distribution properties
 * - Platform-independent
 * - GWT-safe (no native code)
 * - Stable across JVM versions
 *
 * The hash is computed from the serialized state byte[], which includes:
 * - RNG seed
 * - Level terrain and flags
 * - All actors (position, stats, health, buffs)
 * - Scheduler state (time values)
 * - Turn counter
 *
 * This ensures the hash reflects ALL engine state, making it suitable for:
 * - Detecting desyncs (different hash → different state)
 * - Verifying determinism (same hash → same state)
 * - Comparing runs (direct vs replay)
 * - Validating save/load (no state corruption)
 *
 * Design principles:
 * - Non-intrusive (uses public API only)
 * - Stateless (pure functions)
 * - No engine modifications required
 * - No side effects
 *
 * Usage:
 * <pre>
 * StateHash hash = StateHasher.hashState(engine);
 * System.out.println("State hash: " + hash.toHexString());
 * </pre>
 */
public final class StateHasher {

    // FNV-1a-64 constants
    private static final long FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
    private static final long FNV_PRIME = 0x100000001b3L;

    // Private constructor - utility class
    private StateHasher() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Compute deterministic hash of complete engine state.
     *
     * This uses GameEngine.saveState() to get a canonical byte[]
     * representation of the state, then hashes it with FNV-1a-64.
     *
     * The hash is stable and deterministic:
     * - Same engine state → same hash
     * - Different engine state → (very likely) different hash
     * - Platform-independent
     * - Reproducible across runs
     *
     * @param engine Game engine instance
     * @return StateHash representing complete state
     * @throws IllegalArgumentException if engine is null
     */
    public static StateHash hashState(GameEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Engine cannot be null");
        }

        // Get canonical byte representation via serialization
        byte[] stateBytes = engine.saveState();

        // Hash the bytes
        return hashBytes(stateBytes);
    }

    /**
     * Compute FNV-1a-64 hash of raw byte array.
     *
     * This is a lower-level method that can be used to hash
     * any byte array deterministically.
     *
     * FNV-1a-64 algorithm:
     * 1. Start with offset basis (0xcbf29ce484222325L)
     * 2. For each byte:
     *    - XOR hash with byte
     *    - Multiply hash by FNV prime (0x100000001b3L)
     * 3. Return final hash
     *
     * @param data Bytes to hash
     * @return StateHash of the data
     * @throws IllegalArgumentException if data is null
     */
    public static StateHash hashBytes(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        long hash = fnv1a64(data);
        return new StateHash(hash);
    }

    /**
     * FNV-1a-64 hash function implementation.
     *
     * This is a simple, fast, deterministic hash with good distribution.
     * It processes bytes sequentially: XOR then multiply.
     *
     * Properties:
     * - Deterministic (same input → same output)
     * - Platform-independent (pure Java, no native code)
     * - GWT-safe (no reflection, no serialization)
     * - Good avalanche (small input change → large hash change)
     *
     * @param data Bytes to hash
     * @return 64-bit FNV-1a hash
     */
    private static long fnv1a64(byte[] data) {
        long hash = FNV_OFFSET_BASIS;

        for (byte b : data) {
            // XOR with byte (promote to unsigned via & 0xff)
            hash ^= (b & 0xff);

            // Multiply by FNV prime
            hash *= FNV_PRIME;
        }

        return hash;
    }
}
