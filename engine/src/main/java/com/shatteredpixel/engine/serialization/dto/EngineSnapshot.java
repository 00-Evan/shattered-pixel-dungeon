package com.shatteredpixel.engine.serialization.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Complete snapshot of the engine state at a moment in time.
 *
 * This is the top-level DTO that captures everything needed to save/load
 * or synchronize the engine state:
 * - RNG state (seed for deterministic replay)
 * - Level state (terrain, flags)
 * - All actors (position, stats, health, buffs)
 * - Scheduler state (turn order)
 * - Global turn counter (optional)
 *
 * This snapshot is designed to be:
 * - Portable (can be serialized to bytes, JSON, etc.)
 * - Deterministic (same state always produces same snapshot)
 * - Versionable (can add version field for forward compatibility)
 * - GWT-safe (no reflection, no Java serialization)
 *
 * Use cases:
 * - Save/load game state
 * - Multiplayer state synchronization
 * - Deterministic replay
 * - Undo/redo functionality
 * - Testing and debugging
 *
 * TODO: Future enhancements:
 * - Global quest state
 * - Unlocked badges/achievements
 * - Inventory/equipment state
 * - Known recipes/alchemy
 * - Statistics tracking
 */
public class EngineSnapshot {

    public final long rngSeed;
    public final LevelSnapshot level;
    public final List<ActorSnapshot> actors;
    public final List<ScheduledActorSnapshot> schedule;
    public final long turnCounter;  // Optional: total turns elapsed

    /**
     * Create a complete engine snapshot.
     *
     * @param rngSeed Current RNG seed (mutated by RNG calls)
     * @param level Current level snapshot (null if no level loaded)
     * @param actors List of all actors in the engine
     * @param schedule List of scheduled actors with their time values
     * @param turnCounter Total turns elapsed (optional, can be 0)
     */
    public EngineSnapshot(
        long rngSeed,
        LevelSnapshot level,
        List<ActorSnapshot> actors,
        List<ScheduledActorSnapshot> schedule,
        long turnCounter
    ) {
        this.rngSeed = rngSeed;
        this.level = level; // Can be null
        this.actors = actors != null ? new ArrayList<>(actors) : new ArrayList<>();
        this.schedule = schedule != null ? new ArrayList<>(schedule) : new ArrayList<>();
        this.turnCounter = turnCounter;
    }

    /**
     * Convenience constructor without turn counter.
     */
    public EngineSnapshot(
        long rngSeed,
        LevelSnapshot level,
        List<ActorSnapshot> actors,
        List<ScheduledActorSnapshot> schedule
    ) {
        this(rngSeed, level, actors, schedule, 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EngineSnapshot)) return false;
        EngineSnapshot other = (EngineSnapshot) obj;
        return this.rngSeed == other.rngSeed
            && Objects.equals(this.level, other.level)
            && this.actors.equals(other.actors)
            && this.schedule.equals(other.schedule)
            && this.turnCounter == other.turnCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rngSeed, level, actors, schedule, turnCounter);
    }

    @Override
    public String toString() {
        return "EngineSnapshot{" +
            "seed=" + rngSeed +
            ", level=" + (level != null ? level.depth : "null") +
            ", actors=" + actors.size() +
            ", scheduled=" + schedule.size() +
            ", turns=" + turnCounter +
            "}";
    }
}
