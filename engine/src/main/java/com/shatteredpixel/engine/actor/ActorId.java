package com.shatteredpixel.engine.actor;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Immutable unique identifier for actors.
 * Used to reference actors across systems without holding direct references.
 *
 * Design goals:
 * - Immutable and thread-safe
 * - Suitable for use as HashMap keys
 * - Serialization-friendly
 * - Unique ID generation
 */
public final class ActorId {

    private static final AtomicLong nextId = new AtomicLong(1);

    private final long id;

    // =========================================================================
    // Constructors
    // =========================================================================

    private ActorId(long id) {
        this.id = id;
    }

    /**
     * Generate a new unique actor ID.
     * Thread-safe and deterministic within a single engine instance.
     */
    public static ActorId generate() {
        return new ActorId(nextId.getAndIncrement());
    }

    /**
     * Create an ActorId from a specific ID value.
     * Used for deserialization or testing.
     */
    public static ActorId of(long id) {
        return new ActorId(id);
    }

    /**
     * Reset the ID generator (for testing or new game sessions).
     * NOT thread-safe - should only be called during initialization.
     */
    public static void resetGenerator() {
        nextId.set(1);
    }

    // =========================================================================
    // Properties
    // =========================================================================

    /**
     * Get the numeric ID value.
     */
    public long getValue() {
        return id;
    }

    // =========================================================================
    // Object overrides
    // =========================================================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ActorId)) return false;
        ActorId other = (ActorId) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ActorId[" + id + "]";
    }
}
