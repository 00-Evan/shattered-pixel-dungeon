package com.shatteredpixel.engine.buff;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Character;

import java.util.*;

/**
 * Container that manages multiple buffs attached to a character.
 *
 * Responsibilities:
 * - Add/remove buffs
 * - Query buffs by type/id
 * - Process buff lifecycle (onTurnStart, onTurnEnd)
 * - Automatically remove expired buffs
 *
 * This component is owned by a Character and integrates with the turn system.
 */
public class BuffContainer {

    private final Map<BuffId, Buff> buffs;

    public BuffContainer() {
        this.buffs = new LinkedHashMap<>(); // Preserve insertion order
    }

    // ===== Add/Remove =====

    /**
     * Add a buff to this container and call its onApply hook.
     *
     * @param buff The buff to add
     * @param target The character receiving the buff
     * @param context Engine context
     */
    public void addBuff(Buff buff, Character target, EngineContext context) {
        buffs.put(buff.getId(), buff);
        buff.onApply(target, context);
    }

    /**
     * Remove a buff by ID and call its onRemove hook.
     *
     * @param buffId The buff ID to remove
     * @param target The character losing the buff
     * @param context Engine context
     * @return true if the buff was found and removed
     */
    public boolean removeBuff(BuffId buffId, Character target, EngineContext context) {
        Buff buff = buffs.remove(buffId);
        if (buff != null) {
            buff.onRemove(target, context);
            return true;
        }
        return false;
    }

    /**
     * Remove a specific buff instance and call its onRemove hook.
     */
    public boolean removeBuff(Buff buff, Character target, EngineContext context) {
        return removeBuff(buff.getId(), target, context);
    }

    /**
     * Remove all buffs of a specific type.
     *
     * @param type The buff type to remove
     * @param target The character losing the buffs
     * @param context Engine context
     * @return Number of buffs removed
     */
    public int removeBuffsByType(BuffType type, Character target, EngineContext context) {
        List<Buff> toRemove = new ArrayList<>();
        for (Buff buff : buffs.values()) {
            if (buff.getType() == type) {
                toRemove.add(buff);
            }
        }

        for (Buff buff : toRemove) {
            removeBuff(buff, target, context);
        }

        return toRemove.size();
    }

    /**
     * Remove all buffs.
     */
    public void clear(Character target, EngineContext context) {
        List<Buff> toRemove = new ArrayList<>(buffs.values());
        for (Buff buff : toRemove) {
            removeBuff(buff, target, context);
        }
    }

    // ===== Queries =====

    /**
     * Get a buff by ID.
     */
    public Buff getBuff(BuffId id) {
        return buffs.get(id);
    }

    /**
     * Get all buffs (immutable view).
     */
    public Collection<Buff> getAllBuffs() {
        return Collections.unmodifiableCollection(buffs.values());
    }

    /**
     * Get all buffs of a specific type.
     */
    public List<Buff> getBuffsByType(BuffType type) {
        List<Buff> result = new ArrayList<>();
        for (Buff buff : buffs.values()) {
            if (buff.getType() == type) {
                result.add(buff);
            }
        }
        return result;
    }

    /**
     * Check if this container has any buffs of the given type.
     */
    public boolean hasBuffOfType(BuffType type) {
        for (Buff buff : buffs.values()) {
            if (buff.getType() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Count buffs of a specific type.
     */
    public int countBuffsByType(BuffType type) {
        int count = 0;
        for (Buff buff : buffs.values()) {
            if (buff.getType() == type) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the number of buffs.
     */
    public int size() {
        return buffs.size();
    }

    /**
     * Check if this container is empty.
     */
    public boolean isEmpty() {
        return buffs.isEmpty();
    }

    // ===== Lifecycle Processing =====

    /**
     * Process all buffs at the start of the target's turn.
     * Calls onTurnStart() for each buff.
     *
     * Creates a copy of the buff list to avoid concurrent modification
     * if buffs add/remove other buffs during processing.
     */
    public void onTurnStart(Character target, EngineContext context) {
        // Create a copy to avoid concurrent modification
        List<Buff> buffList = new ArrayList<>(buffs.values());

        for (Buff buff : buffList) {
            buff.onTurnStart(target, context);
        }
    }

    /**
     * Process all buffs at the end of the target's turn.
     * Calls onTurnEnd() for each buff, then removes expired buffs.
     *
     * Creates a copy of the buff list to avoid concurrent modification.
     */
    public void onTurnEnd(Character target, EngineContext context) {
        // Create a copy to avoid concurrent modification
        List<Buff> buffList = new ArrayList<>(buffs.values());

        for (Buff buff : buffList) {
            buff.onTurnEnd(target, context);
        }

        // Remove expired buffs
        removeExpiredBuffs(target, context);
    }

    /**
     * Remove all expired buffs (duration = 0).
     * Called automatically at the end of each turn.
     */
    private void removeExpiredBuffs(Character target, EngineContext context) {
        List<Buff> toRemove = new ArrayList<>();
        for (Buff buff : buffs.values()) {
            if (buff.isExpired()) {
                toRemove.add(buff);
            }
        }

        for (Buff buff : toRemove) {
            removeBuff(buff, target, context);
        }
    }

    // ===== Utility =====

    @Override
    public String toString() {
        return String.format("BuffContainer{%d buffs}", buffs.size());
    }
}
