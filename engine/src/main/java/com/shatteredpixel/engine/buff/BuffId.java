package com.shatteredpixel.engine.buff;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Immutable unique identifier for a buff instance.
 *
 * Similar to ActorId, BuffId provides a unique identity for each buff instance
 * to support efficient lookup, removal, and serialization.
 *
 * Thread-safe ID generation using AtomicLong.
 */
public final class BuffId {

    private static final AtomicLong nextId = new AtomicLong(1);

    private final long id;

    private BuffId(long id) {
        this.id = id;
    }

    /**
     * Generate a new unique BuffId.
     */
    public static BuffId generate() {
        return new BuffId(nextId.getAndIncrement());
    }

    /**
     * Restore a BuffId from a saved long value (for deserialization).
     */
    public static BuffId fromLong(long id) {
        return new BuffId(id);
    }

    /**
     * Get the underlying long value (for serialization).
     */
    public long toLong() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BuffId)) return false;
        BuffId other = (BuffId) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "BuffId{" + id + "}";
    }
}
