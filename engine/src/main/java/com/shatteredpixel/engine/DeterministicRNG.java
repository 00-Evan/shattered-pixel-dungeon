package com.shatteredpixel.engine;

/**
 * Deterministic random number generator for the game engine.
 * Uses XORShift algorithm for fast, deterministic random number generation.
 *
 * Design goals:
 * - Deterministic: same seed always produces same sequence
 * - No dependence on Math.random() or system time
 * - GWT-compatible (Java 11)
 * - Fast performance for game logic
 * - Serializable for multiplayer sync
 *
 * This is critical for:
 * - Multiplayer simulation (all clients see same results)
 * - Replay systems
 * - Testing and debugging
 */
public class DeterministicRNG {

    private long seed;

    /**
     * Create a new RNG with the specified seed.
     */
    public DeterministicRNG(long seed) {
        this.seed = seed;
    }

    /**
     * Set a new seed (resets the sequence).
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }

    /**
     * Get the current seed state.
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Generate the next random long using XORShift64.
     */
    private long nextLong() {
        seed ^= (seed << 21);
        seed ^= (seed >>> 35);
        seed ^= (seed << 4);
        return seed;
    }

    /**
     * Generate a random integer in the range [0, bound).
     *
     * @param bound Upper bound (exclusive)
     * @return Random integer in [0, bound)
     */
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        // Use rejection sampling to avoid modulo bias
        long value = nextLong() & 0x7FFFFFFFL; // Positive long
        return (int) (value % bound);
    }

    /**
     * Generate a random integer in the range [min, max].
     *
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return Random integer in [min, max]
     */
    public int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return min + nextInt(max - min + 1);
    }

    /**
     * Generate a random float in the range [0.0, 1.0).
     */
    public float nextFloat() {
        return (nextLong() & 0x7FFFFFFFL) / (float) 0x80000000L;
    }

    /**
     * Generate a random boolean with 50% probability.
     */
    public boolean nextBoolean() {
        return (nextLong() & 1) == 0;
    }

    /**
     * Roll a dice: return true with probability (numerator / denominator).
     * Example: roll(1, 6) returns true ~16.7% of the time.
     */
    public boolean roll(int numerator, int denominator) {
        if (numerator <= 0) return false;
        if (numerator >= denominator) return true;
        return nextInt(denominator) < numerator;
    }

    /**
     * Shuffle an array in-place using Fisher-Yates algorithm.
     */
    public <T> void shuffle(T[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = nextInt(i + 1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
