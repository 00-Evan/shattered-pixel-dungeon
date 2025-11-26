package com.shatteredpixel.engine.serialization;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.serialization.dto.EngineSnapshot;

/**
 * Interface for capturing and restoring engine state.
 *
 * This is responsible for converting between live engine state (EngineContext)
 * and portable snapshot DTOs (EngineSnapshot).
 *
 * Responsibilities:
 * - Read from EngineContext, LevelState, actor registry, RNG, scheduler
 * - Build EngineSnapshot from live state
 * - Restore live state from EngineSnapshot
 * - Handle actor reconstruction (instantiate correct concrete classes)
 *
 * This interface is NOT responsible for:
 * - Encoding/decoding to byte[] or String (see EngineSnapshotCodec)
 * - File I/O or networking
 * - Version compatibility (handled by codec layer)
 *
 * Implementations should be:
 * - Deterministic (same state always produces same snapshot)
 * - GWT-safe (no reflection)
 * - Stateless (no internal state between calls)
 *
 * Example usage:
 * <pre>
 * EngineStateSerializer serializer = new DefaultEngineStateSerializer();
 *
 * // Capture
 * EngineSnapshot snapshot = serializer.captureSnapshot(context);
 *
 * // Restore
 * EngineContext newContext = new EngineContext(0);
 * serializer.restoreFromSnapshot(newContext, snapshot);
 * </pre>
 */
public interface EngineStateSerializer {

    /**
     * Capture a snapshot of the current engine state.
     *
     * This reads from:
     * - RNG state (seed)
     * - Level state (terrain, flags)
     * - All actors (position, stats, health, buffs)
     * - Scheduler state (time per actor)
     * - Game state (turn counter, etc.)
     *
     * The returned snapshot is independent of the engine and can be
     * serialized, stored, or transmitted.
     *
     * @param context The engine context to capture
     * @return A complete snapshot of the engine state
     * @throws IllegalArgumentException if context is null or invalid
     */
    EngineSnapshot captureSnapshot(EngineContext context);

    /**
     * Restore engine state from a snapshot.
     *
     * This clears existing state in the context and rebuilds:
     * - RNG (seed)
     * - Level (terrain, flags, grid)
     * - Actors (instantiate concrete classes, set position/stats/health)
     * - Scheduler (register actors with saved time values)
     *
     * Actor reconstruction:
     * - Uses actorKind field to determine concrete class
     * - Instantiates appropriate subclass (Hero, Mob, etc.)
     * - For unknown kinds, may throw or create generic placeholder
     *
     * After restoration, the context should be in the exact same state
     * as when the snapshot was captured.
     *
     * @param context The engine context to restore into (will be cleared)
     * @param snapshot The snapshot to restore from
     * @throws IllegalArgumentException if context or snapshot is null/invalid
     * @throws IllegalStateException if restoration fails (unknown actor kind, etc.)
     */
    void restoreFromSnapshot(EngineContext context, EngineSnapshot snapshot);
}
