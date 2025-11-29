package com.shatteredpixel.engine.serialization.dto;

/**
 * Immutable serialization snapshot of a buff's state.
 *
 * Captures all data needed to recreate a buff during deserialization
 * (e.g., after save/load or during replay).
 *
 * Design:
 * - Immutable: All fields final, no setters
 * - Deterministic: Same buff state → same snapshot
 * - GWT-safe: Pure data class, no reflection
 * - Extensible: Subclasses can add buff-specific fields
 *
 * Basic snapshots contain:
 * - buffType: Stable string identifier for buff type
 * - duration: Remaining turns
 *
 * Buffs with additional state (e.g., stacks, intensity) should create
 * custom BuffSnapshot subclasses with additional fields.
 *
 * Serialization flow:
 * 1. BuffContainer.getSnapshot() calls buff.createSnapshot()
 * 2. Snapshot stored in ActorSnapshot
 * 3. ActorSnapshot serialized to byte[] via EngineSnapshot
 * 4. During load, BuffRegistry.createBuff(snapshot) recreates buff
 * 5. Buff added back to actor's BuffContainer
 */
public class BuffSnapshot {

    /**
     * Stable identifier for buff type.
     *
     * Used by BuffRegistry to lookup factory during deserialization.
     * Must match Buff.getBuffType() for the corresponding buff class.
     */
    public final String buffType;

    /**
     * Remaining duration in turns.
     *
     * Restored as buff's initial duration during deserialization.
     */
    public final int duration;

    /**
     * Create a basic buff snapshot.
     *
     * @param buffType Stable buff type identifier (e.g., "poison")
     * @param duration Remaining turns
     */
    public BuffSnapshot(String buffType, int duration) {
        if (buffType == null || buffType.isEmpty()) {
            throw new IllegalArgumentException("Buff type cannot be null or empty");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative, got: " + duration);
        }
        this.buffType = buffType;
        this.duration = duration;
    }

    /**
     * Get buff type identifier.
     *
     * @return Stable buff type string
     */
    public String getBuffType() {
        return buffType;
    }

    /**
     * Get remaining duration.
     *
     * @return Remaining turns
     */
    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BuffSnapshot)) return false;
        BuffSnapshot other = (BuffSnapshot) obj;
        return this.buffType.equals(other.buffType)
            && this.duration == other.duration;
    }

    @Override
    public int hashCode() {
        int result = buffType.hashCode();
        result = 31 * result + duration;
        return result;
    }

    @Override
    public String toString() {
        return "BuffSnapshot{buffType='" + buffType + "', duration=" + duration + "}";
    }
}
