package com.shatteredpixel.engine.actor.buff;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.serialization.dto.BuffSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Container managing all buffs attached to an actor.
 *
 * Each Actor has exactly one BuffContainer instance that:
 * - Stores all active buffs
 * - Triggers buff lifecycle hooks (onApply/onTick/onRemove)
 * - Manages duration reduction and expiration
 * - Provides serialization via snapshots
 *
 * Buff lifecycle in container:
 * 1. addBuff() → buff added to list, onApply() called
 * 2. tickAll() called each turn:
 *    a. onTick() called for each buff
 *    b. reduceDuration(1) called
 *    c. Expired buffs removed, onRemove() called
 * 3. removeBuff() → buff removed from list, onRemove() called (manual removal)
 *
 * Serialization:
 * - getSnapshot() → BuffSnapshot[] for all active buffs
 * - loadFromSnapshot() → recreates buffs from snapshots via BuffRegistry
 *
 * Integration points:
 * - Actor owns one BuffContainer instance
 * - ActorScheduler calls tickAll() during turn processing
 * - ActorSnapshot includes buff snapshots
 */
public class BuffContainer {

    /**
     * List of active buffs.
     *
     * Maintained in insertion order for determinism.
     */
    private final List<Buff> buffs;

    /**
     * Create an empty buff container.
     */
    public BuffContainer() {
        this.buffs = new ArrayList<>();
    }

    /**
     * Add a buff to this container and apply it to the actor.
     *
     * The buff's onApply() hook is called immediately.
     *
     * @param buff Buff to add
     * @param context Current engine context
     * @param actor Actor receiving this buff
     */
    public void addBuff(Buff buff, EngineContext context, Actor actor) {
        if (buff == null) {
            throw new IllegalArgumentException("Cannot add null buff");
        }
        buffs.add(buff);
        buff.onApply(context, actor);
    }

    /**
     * Remove a buff from this container.
     *
     * The buff's onRemove() hook is called if buff was present.
     *
     * @param buff Buff to remove
     * @param context Current engine context
     * @param actor Actor losing this buff
     * @return true if buff was removed, false if not present
     */
    public boolean removeBuff(Buff buff, EngineContext context, Actor actor) {
        if (buffs.remove(buff)) {
            buff.onRemove(context, actor);
            return true;
        }
        return false;
    }

    /**
     * Tick all buffs and remove expired ones.
     *
     * Called once per turn by ActorScheduler during turn processing.
     *
     * For each buff:
     * 1. Call onTick() for per-turn effects
     * 2. Call reduceDuration(1) to decrement duration
     * 3. If expired, call onRemove() and remove from list
     *
     * Uses iterator to allow safe removal during iteration.
     *
     * @param context Current engine context
     * @param actor Actor with these buffs
     */
    public void tickAll(EngineContext context, Actor actor) {
        Iterator<Buff> iterator = buffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();

            // Trigger per-turn effect
            buff.onTick(context, actor);

            // Reduce duration
            buff.reduceDuration(1);

            // Remove if expired
            if (buff.isExpired()) {
                buff.onRemove(context, actor);
                iterator.remove();
            }
        }
    }

    /**
     * Get read-only view of active buffs.
     *
     * @return Unmodifiable list of buffs (in insertion order)
     */
    public List<Buff> getBuffs() {
        return Collections.unmodifiableList(buffs);
    }

    /**
     * Get count of active buffs.
     *
     * @return Number of buffs currently active
     */
    public int getBuffCount() {
        return buffs.size();
    }

    /**
     * Check if actor has any buffs of specified type.
     *
     * @param buffType Buff type identifier to check
     * @return true if at least one buff of that type exists
     */
    public boolean hasBuff(String buffType) {
        for (Buff buff : buffs) {
            if (buff.getBuffType().equals(buffType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get first buff of specified type, if any.
     *
     * @param buffType Buff type identifier to find
     * @return First matching buff, or null if none found
     */
    public Buff getBuff(String buffType) {
        for (Buff buff : buffs) {
            if (buff.getBuffType().equals(buffType)) {
                return buff;
            }
        }
        return null;
    }

    /**
     * Create snapshot array for serialization.
     *
     * Calls createSnapshot() on each buff to capture state.
     *
     * @return Array of buff snapshots (empty if no buffs)
     */
    public BuffSnapshot[] getSnapshot() {
        BuffSnapshot[] snapshots = new BuffSnapshot[buffs.size()];
        for (int i = 0; i < buffs.size(); i++) {
            snapshots[i] = buffs.get(i).createSnapshot();
        }
        return snapshots;
    }

    /**
     * Load buffs from snapshot array during deserialization.
     *
     * Clears existing buffs and recreates them from snapshots via BuffRegistry.
     * Calls onApply() for each recreated buff.
     *
     * @param snapshots Array of buff snapshots (null = no buffs)
     * @param context Current engine context
     * @param actor Actor receiving these buffs
     */
    public void loadFromSnapshot(BuffSnapshot[] snapshots, EngineContext context, Actor actor) {
        buffs.clear();

        if (snapshots != null) {
            for (BuffSnapshot snapshot : snapshots) {
                Buff buff = BuffRegistry.createBuff(snapshot);
                buffs.add(buff);
                buff.onApply(context, actor);
            }
        }
    }
}
