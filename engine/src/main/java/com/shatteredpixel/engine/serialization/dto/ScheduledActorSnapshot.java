package com.shatteredpixel.engine.serialization.dto;

/**
 * Snapshot of scheduler state for a single actor.
 *
 * This represents the time value of an actor in the ActorScheduler,
 * which determines when they will take their next turn.
 *
 * The scheduler uses a time-based system where:
 * - Lower time values act sooner
 * - Each action advances the actor's time by an action cost
 * - Time is normalized to prevent overflow
 */
public class ScheduledActorSnapshot {

    public final long actorId;
    public final float time;

    /**
     * Create a scheduled actor snapshot.
     *
     * @param actorId Actor's unique identifier
     * @param time Current time value in the scheduler (lower = acts sooner)
     */
    public ScheduledActorSnapshot(long actorId, float time) {
        this.actorId = actorId;
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScheduledActorSnapshot)) return false;
        ScheduledActorSnapshot other = (ScheduledActorSnapshot) obj;
        return this.actorId == other.actorId
            && Float.compare(this.time, other.time) == 0;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(actorId);
        result = 31 * result + Float.hashCode(time);
        return result;
    }

    @Override
    public String toString() {
        return "ScheduledActorSnapshot{" +
            "actorId=" + actorId +
            ", time=" + time +
            "}";
    }
}
