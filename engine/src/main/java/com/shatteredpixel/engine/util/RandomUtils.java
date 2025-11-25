package com.shatteredpixel.engine.util;

import com.shatteredpixel.engine.DeterministicRNG;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Random utility methods for game logic.
 * All methods delegate to DeterministicRNG for reproducible behavior.
 *
 * Migrated from: com.watabou.utils.Random
 * Changes:
 * - Removed generator stack (single deterministic RNG per context)
 * - All methods require explicit RNG parameter
 * - No java.util.Random usage
 * - GWT-safe (Java 11)
 */
public class RandomUtils {

    // =========================================================================
    // Float Methods
    // =========================================================================

    /**
     * Returns a uniformly distributed float in the range [0, 1).
     */
    public static float randomFloat(DeterministicRNG rng) {
        return rng.nextFloat();
    }

    /**
     * Returns a uniformly distributed float in the range [0, max).
     */
    public static float randomFloat(DeterministicRNG rng, float max) {
        return rng.nextFloat() * max;
    }

    /**
     * Returns a uniformly distributed float in the range [min, max).
     */
    public static float randomFloat(DeterministicRNG rng, float min, float max) {
        return min + rng.nextFloat() * (max - min);
    }

    /**
     * Returns a triangularly distributed float in the range [min, max).
     * This makes results more likely as they get closer to the middle of the range.
     */
    public static float normalFloat(DeterministicRNG rng, float min, float max) {
        float range = max - min;
        return min + ((rng.nextFloat() * range + rng.nextFloat() * range) / 2f);
    }

    // =========================================================================
    // Integer Methods
    // =========================================================================

    /**
     * Returns a uniformly distributed int in the range [0, max).
     */
    public static int randomInt(DeterministicRNG rng, int max) {
        return rng.nextInt(max);
    }

    /**
     * Returns a uniformly distributed int in the range [min, max).
     */
    public static int randomInt(DeterministicRNG rng, int min, int max) {
        return min + rng.nextInt(max - min);
    }

    /**
     * Returns a uniformly distributed int in the range [min, max] (inclusive).
     */
    public static int randomIntRange(DeterministicRNG rng, int min, int max) {
        return min + rng.nextInt(max - min + 1);
    }

    /**
     * Returns a triangularly distributed int in the range [min, max].
     * This makes results more likely as they get closer to the middle of the range.
     */
    public static int normalIntRange(DeterministicRNG rng, int min, int max) {
        return min + (int)((rng.nextFloat() + rng.nextFloat()) * (max - min + 1) / 2f);
    }

    /**
     * Returns an inverse triangularly distributed int in the range [min, max].
     * This makes results more likely as they get further from the middle of the range.
     */
    public static int invNormalIntRange(DeterministicRNG rng, int min, int max) {
        float roll1 = rng.nextFloat();
        float roll2 = rng.nextFloat();

        if (Math.abs(roll1 - 0.5f) >= Math.abs(roll2 - 0.5f)) {
            return min + (int)(roll1 * (max - min + 1));
        } else {
            return min + (int)(roll2 * (max - min + 1));
        }
    }

    /**
     * Returns a mostly uniformly distributed long in the range [0, max).
     */
    public static long randomLong(DeterministicRNG rng, long max) {
        // Use multiple nextInt calls to build a long (GWT-safe approach)
        long result = (long)rng.nextInt(Integer.MAX_VALUE) << 32;
        result |= rng.nextInt(Integer.MAX_VALUE);
        if (result < 0) result = -result;
        return result % max;
    }

    // =========================================================================
    // Weighted Selection (chances)
    // =========================================================================

    /**
     * Returns an index from chances array.
     * The probability of each index is proportional to its weight in the array.
     * Negative values are treated as 0.
     *
     * @param chances Weight array
     * @return Selected index, or -1 if sum is 0
     */
    public static int chances(DeterministicRNG rng, float[] chances) {
        int length = chances.length;

        float sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Math.max(0, chances[i]);
        }

        if (sum <= 0) {
            return -1;
        }

        float value = rng.nextFloat() * sum;
        sum = 0;
        for (int i = 0; i < length; i++) {
            sum += Math.max(0, chances[i]);
            if (value < sum) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns a key from the chances map.
     * The probability of each key is proportional to its mapped weight value.
     *
     * @param chances Map of keys to weights
     * @return Selected key, or null if sum is 0
     */
    @SuppressWarnings("unchecked")
    public static <K> K chances(DeterministicRNG rng, HashMap<K, Float> chances) {
        int size = chances.size();

        Object[] values = chances.keySet().toArray();
        float[] probs = new float[size];
        float sum = 0;
        for (int i = 0; i < size; i++) {
            probs[i] = chances.get(values[i]);
            sum += probs[i];
        }

        if (sum <= 0) {
            return null;
        }

        float value = rng.nextFloat() * sum;

        sum = probs[0];
        for (int i = 0; i < size; i++) {
            if (value < sum) {
                return (K)values[i];
            }
            if (i + 1 < size) {
                sum += probs[i + 1];
            }
        }

        return null;
    }

    // =========================================================================
    // Collection Selection
    // =========================================================================

    /**
     * Returns a random index for the given collection.
     */
    public static int randomIndex(DeterministicRNG rng, Collection<?> collection) {
        return rng.nextInt(collection.size());
    }

    /**
     * Returns a random element from the varargs array.
     */
    @SafeVarargs
    public static <T> T oneOf(DeterministicRNG rng, T... array) {
        return array[rng.nextInt(array.length)];
    }

    /**
     * Returns a random element from the array.
     */
    public static <T> T element(DeterministicRNG rng, T[] array) {
        return element(rng, array, array.length);
    }

    /**
     * Returns a random element from the first 'max' elements of the array.
     */
    public static <T> T element(DeterministicRNG rng, T[] array, int max) {
        return array[rng.nextInt(max)];
    }

    /**
     * Returns a random element from the collection.
     * Returns null if collection is empty.
     */
    @SuppressWarnings("unchecked")
    public static <T> T element(DeterministicRNG rng, Collection<? extends T> collection) {
        int size = collection.size();
        return size > 0 ? (T)collection.toArray()[rng.nextInt(size)] : null;
    }

    // =========================================================================
    // Shuffle Methods
    // =========================================================================

    /**
     * Shuffle an array in-place using Fisher-Yates algorithm.
     */
    public static <T> void shuffle(DeterministicRNG rng, T[] array) {
        rng.shuffle(array); // Delegate to DeterministicRNG's shuffle
    }

    /**
     * Shuffle a list in-place.
     */
    public static <T> void shuffle(DeterministicRNG rng, List<T> list) {
        // Convert to array, shuffle, then put back
        @SuppressWarnings("unchecked")
        T[] array = (T[])list.toArray();
        rng.shuffle(array);
        for (int i = 0; i < array.length; i++) {
            list.set(i, array[i]);
        }
    }

    /**
     * Shuffle two arrays in parallel (same permutation applied to both).
     */
    public static <U, V> void shuffle(DeterministicRNG rng, U[] u, V[] v) {
        for (int i = 0; i < u.length - 1; i++) {
            int j = rng.nextInt(i, u.length - 1);
            if (j != i) {
                U ut = u[i];
                u[i] = u[j];
                u[j] = ut;

                V vt = v[i];
                v[i] = v[j];
                v[j] = vt;
            }
        }
    }
}
