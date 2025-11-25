package com.shatteredpixel.engine.util;

/**
 * Deterministic math utility functions for game logic.
 * All functions are pure (no side effects, no randomness).
 *
 * Migrated from: com.watabou.utils.GameMath
 * Changes:
 * - Removed speed() (platform-dependent via Game.elapsed)
 * - Added common game math functions
 * - GWT-safe (Java 11)
 */
public class MathUtils {

    /**
     * Clamp a value to the range [min, max].
     * If value < min, returns min.
     * If value > max, returns max.
     * Otherwise, returns value.
     *
     * Also known as "gate" in legacy code.
     */
    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Clamp an int value to the range [min, max].
     */
    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Clamp a long value to the range [min, max].
     */
    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    /**
     * Linear interpolation between a and b by factor t.
     * When t = 0, returns a.
     * When t = 1, returns b.
     * t is not clamped, so extrapolation is possible.
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Inverse linear interpolation.
     * Returns the t value that would produce 'value' in lerp(a, b, t).
     * Returns 0 if a == b (to avoid division by zero).
     */
    public static float inverseLerp(float a, float b, float value) {
        if (a == b) return 0;
        return (value - a) / (b - a);
    }

    /**
     * Remap a value from one range to another.
     * Example: remap(5, 0, 10, 0, 100) returns 50.
     */
    public static float remap(float value, float fromMin, float fromMax, float toMin, float toMax) {
        float t = inverseLerp(fromMin, fromMax, value);
        return lerp(toMin, toMax, t);
    }

    /**
     * Check if two floats are approximately equal within epsilon.
     */
    public static boolean approximately(float a, float b, float epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    /**
     * Check if two floats are approximately equal (default epsilon = 0.0001f).
     */
    public static boolean approximately(float a, float b) {
        return approximately(a, b, 0.0001f);
    }

    /**
     * Sign function: returns -1 for negative, 0 for zero, +1 for positive.
     */
    public static int sign(float value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }

    /**
     * Sign function for int.
     */
    public static int sign(int value) {
        if (value < 0) return -1;
        if (value > 0) return 1;
        return 0;
    }
}
