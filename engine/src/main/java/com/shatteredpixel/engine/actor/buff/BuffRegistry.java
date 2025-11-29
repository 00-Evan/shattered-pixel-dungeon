package com.shatteredpixel.engine.actor.buff;

import java.util.HashMap;
import java.util.Map;

/**
 * Deterministic registry mapping buff type identifiers to buff factories.
 *
 * This registry is essential for deserialization:
 * - During save, buffs are serialized to BuffSnapshot (buffType + data)
 * - During load, BuffRegistry.createBuff(snapshot) recreates buff instances
 *
 * Design principles:
 * - No reflection (GWT-safe)
 * - Deterministic (same snapshot → same buff)
 * - Static initialization (registered at engine startup)
 * - Explicit registration (no auto-discovery)
 *
 * Usage:
 * <pre>
 * // In static initializer or engine startup:
 * BuffRegistry.register("poison", duration -> new PoisonBuff(duration));
 *
 * // During deserialization:
 * BuffSnapshot snapshot = new BuffSnapshot("poison", 5);
 * Buff buff = BuffRegistry.createBuff(snapshot);
 * </pre>
 *
 * Registration guidelines:
 * - Register all buff types during engine initialization
 * - Use stable, lowercase identifiers (e.g., "poison", not "PoisonBuff")
 * - Factories must be deterministic (same input → same output)
 * - Factories must not use reflection
 */
public final class BuffRegistry {

    /**
     * Factory interface for creating buffs from snapshots.
     *
     * Implementations must:
     * - Be deterministic
     * - Accept duration from snapshot
     * - Create buff with correct initial state
     */
    @FunctionalInterface
    public interface BuffFactory {
        /**
         * Create a buff with specified duration.
         *
         * For basic buffs, this is typically just a constructor call.
         * For complex buffs with additional state, parse it from snapshot.
         *
         * @param duration Initial duration in turns
         * @return New buff instance
         */
        Buff create(int duration);
    }

    /**
     * Map of buffType → factory.
     *
     * Populated via register() calls during engine initialization.
     */
    private static final Map<String, BuffFactory> registry = new HashMap<>();

    /**
     * Private constructor (static utility class).
     */
    private BuffRegistry() {
        throw new UnsupportedOperationException("BuffRegistry is a static utility class");
    }

    /**
     * Register a buff factory for a given type identifier.
     *
     * This must be called during engine initialization for all buff types
     * that need to be serialized/deserialized.
     *
     * @param buffType Stable buff type identifier (e.g., "poison")
     * @param factory Factory function creating buff from duration
     * @throws IllegalArgumentException if buffType is null or already registered
     */
    public static void register(String buffType, BuffFactory factory) {
        if (buffType == null || buffType.isEmpty()) {
            throw new IllegalArgumentException("Buff type cannot be null or empty");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        if (registry.containsKey(buffType)) {
            throw new IllegalArgumentException("Buff type already registered: " + buffType);
        }
        registry.put(buffType, factory);
    }

    /**
     * Create a buff from a snapshot.
     *
     * Looks up factory by buffType and creates buff with snapshot's duration.
     *
     * @param snapshot Buff snapshot to deserialize
     * @return New buff instance
     * @throws IllegalArgumentException if buffType not registered
     */
    public static Buff createBuff(BuffSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot cannot be null");
        }

        String buffType = snapshot.getBuffType();
        BuffFactory factory = registry.get(buffType);

        if (factory == null) {
            throw new IllegalArgumentException(
                "Unknown buff type: " + buffType + ". Did you forget to register it?"
            );
        }

        return factory.create(snapshot.getDuration());
    }

    /**
     * Check if a buff type is registered.
     *
     * Useful for validation during development.
     *
     * @param buffType Buff type to check
     * @return true if registered, false otherwise
     */
    public static boolean isRegistered(String buffType) {
        return registry.containsKey(buffType);
    }

    /**
     * Clear all registrations.
     *
     * WARNING: Only use this in tests! Do NOT call during normal operation.
     * Clears the registry to allow test-specific registrations.
     */
    public static void clearForTesting() {
        registry.clear();
    }

    // Static initializer for built-in buffs
    // Currently empty - buffs will be registered during engine initialization
    // or by test setup code
    static {
        // Built-in buffs registered here (none yet in Step 5.1)
    }
}
