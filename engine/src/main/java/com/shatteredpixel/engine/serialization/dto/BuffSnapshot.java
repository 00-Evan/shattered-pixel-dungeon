package com.shatteredpixel.engine.serialization.dto;

/**
 * Snapshot of a single buff's state.
 *
 * This is a placeholder skeleton for future buff implementation.
 * Currently contains only minimal data for identifying buffs.
 *
 * TODO: Expand when concrete buff system is implemented:
 * - Buff-specific data (strength, stacks, etc.)
 * - Source actor reference
 * - Tick-based vs duration-based tracking
 * - Conditional/reactive buff logic state
 */
public class BuffSnapshot {

    public final long buffId;
    public final String buffTypeName;
    public final int remainingDuration;

    /**
     * Create a buff snapshot.
     *
     * @param buffId Unique identifier for this buff instance
     * @param buffTypeName Type name (e.g., "POISON", "STRENGTH", "REGENERATION")
     * @param remainingDuration Remaining turns/ticks (-1 for permanent)
     */
    public BuffSnapshot(long buffId, String buffTypeName, int remainingDuration) {
        this.buffId = buffId;
        this.buffTypeName = buffTypeName;
        this.remainingDuration = remainingDuration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BuffSnapshot)) return false;
        BuffSnapshot other = (BuffSnapshot) obj;
        return this.buffId == other.buffId
            && this.buffTypeName.equals(other.buffTypeName)
            && this.remainingDuration == other.remainingDuration;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(buffId);
        result = 31 * result + buffTypeName.hashCode();
        result = 31 * result + remainingDuration;
        return result;
    }

    @Override
    public String toString() {
        return "BuffSnapshot{" +
            "id=" + buffId +
            ", type=" + buffTypeName +
            ", duration=" + remainingDuration +
            "}";
    }
}
