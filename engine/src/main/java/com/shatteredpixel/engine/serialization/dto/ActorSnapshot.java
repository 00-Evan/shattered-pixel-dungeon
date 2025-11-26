package com.shatteredpixel.engine.serialization.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Snapshot of a single actor's state.
 *
 * This captures all the data needed to reconstruct an actor:
 * - Identity (ID, type, kind)
 * - Position on the level grid
 * - Combat stats (health, attack, defense, etc.)
 * - Active buffs (placeholder for now)
 *
 * The actorKind field is used to determine which concrete actor class
 * to instantiate during deserialization (e.g., "HERO", "SIMPLE_MELEE_MOB").
 *
 * TODO: Future enhancements:
 * - Inventory items
 * - Equipment slots
 * - Quest state
 * - Faction/alignment
 * - AI state/memory
 */
public class ActorSnapshot {

    public final long actorId;
    public final String actorKind;      // e.g., "HERO", "SIMPLE_MELEE_MOB", "GENERIC"
    public final int typeOrdinal;       // ActorType ordinal (HERO=0, MOB=1, NPC=2, etc.)
    public final int x;                 // Position X (-1 if no position)
    public final int y;                 // Position Y (-1 if no position)
    public final int currentHealth;
    public final int maxHealth;
    public final int attackPower;
    public final int defense;
    public final int accuracy;
    public final int evasion;
    public final float speed;
    public final List<BuffSnapshot> buffs; // Currently placeholder, will be empty for now

    /**
     * Create an actor snapshot with all fields specified.
     *
     * @param actorId Unique actor identifier
     * @param actorKind Kind string for reconstruction (e.g., "HERO", "SIMPLE_MELEE_MOB")
     * @param typeOrdinal ActorType ordinal value
     * @param x Position X coordinate (-1 for no position)
     * @param y Position Y coordinate (-1 for no position)
     * @param currentHealth Current health points
     * @param maxHealth Maximum health points
     * @param attackPower Attack power stat
     * @param defense Defense stat
     * @param accuracy Accuracy stat
     * @param evasion Evasion stat
     * @param speed Speed multiplier
     * @param buffs List of active buffs (empty for now)
     */
    public ActorSnapshot(
        long actorId,
        String actorKind,
        int typeOrdinal,
        int x,
        int y,
        int currentHealth,
        int maxHealth,
        int attackPower,
        int defense,
        int accuracy,
        int evasion,
        float speed,
        List<BuffSnapshot> buffs
    ) {
        this.actorId = actorId;
        this.actorKind = actorKind;
        this.typeOrdinal = typeOrdinal;
        this.x = x;
        this.y = y;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.speed = speed;
        this.buffs = buffs != null ? new ArrayList<>(buffs) : new ArrayList<>();
    }

    /**
     * Convenience constructor without buffs (uses empty list).
     */
    public ActorSnapshot(
        long actorId,
        String actorKind,
        int typeOrdinal,
        int x,
        int y,
        int currentHealth,
        int maxHealth,
        int attackPower,
        int defense,
        int accuracy,
        int evasion,
        float speed
    ) {
        this(actorId, actorKind, typeOrdinal, x, y, currentHealth, maxHealth,
            attackPower, defense, accuracy, evasion, speed, Collections.emptyList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ActorSnapshot)) return false;
        ActorSnapshot other = (ActorSnapshot) obj;
        return this.actorId == other.actorId
            && this.actorKind.equals(other.actorKind)
            && this.typeOrdinal == other.typeOrdinal
            && this.x == other.x
            && this.y == other.y
            && this.currentHealth == other.currentHealth
            && this.maxHealth == other.maxHealth
            && this.attackPower == other.attackPower
            && this.defense == other.defense
            && this.accuracy == other.accuracy
            && this.evasion == other.evasion
            && Float.compare(this.speed, other.speed) == 0
            && this.buffs.equals(other.buffs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, actorKind, typeOrdinal, x, y,
            currentHealth, maxHealth, attackPower, defense, accuracy,
            evasion, speed, buffs);
    }

    @Override
    public String toString() {
        return "ActorSnapshot{" +
            "id=" + actorId +
            ", kind=" + actorKind +
            ", type=" + typeOrdinal +
            ", pos=(" + x + "," + y + ")" +
            ", hp=" + currentHealth + "/" + maxHealth +
            ", atk=" + attackPower +
            ", def=" + defense +
            ", acc=" + accuracy +
            ", eva=" + evasion +
            ", spd=" + speed +
            ", buffs=" + buffs.size() +
            "}";
    }
}
