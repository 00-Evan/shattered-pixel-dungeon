package com.shatteredpixel.engine.event;

import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.geom.Point;

/**
 * Immutable DTO representing a single event emitted by the engine.
 *
 * Events describe what happened during a tick:
 * - An actor moved
 * - Damage was dealt
 * - A buff was applied
 * - A log message
 *
 * This is designed to be:
 * - Serializable (for replay, networking, logging)
 * - Generic (no SPD-specific logic)
 * - Easy to consume by clients (UI, network, AI observers)
 *
 * All fields are optional except type. The combination of type and fields
 * determines what the event represents.
 */
public final class GameEvent {

    private final EventType type;

    // Optional fields (null if not applicable)
    private final ActorId sourceActorId;
    private final ActorId targetActorId;
    private final Point position;
    private final int amount;
    private final String message;

    /**
     * Full constructor.
     */
    public GameEvent(EventType type, ActorId sourceActorId, ActorId targetActorId,
                    Point position, int amount, String message) {
        this.type = type;
        this.sourceActorId = sourceActorId;
        this.targetActorId = targetActorId;
        this.position = position;
        this.amount = amount;
        this.message = message;
    }

    /**
     * Simple constructor with minimal fields.
     */
    public GameEvent(EventType type) {
        this(type, null, null, null, 0, null);
    }

    // ===== Factory Methods =====

    /**
     * Create an actor moved event.
     */
    public static GameEvent actorMoved(ActorId actorId, Point newPosition) {
        return new GameEvent(EventType.ACTOR_MOVED, actorId, null, newPosition, 0, null);
    }

    /**
     * Create a damage applied event.
     */
    public static GameEvent damageApplied(ActorId sourceActorId, ActorId targetActorId, int amount) {
        return new GameEvent(EventType.DAMAGE_APPLIED, sourceActorId, targetActorId, null, amount, null);
    }

    /**
     * Create a healing applied event.
     */
    public static GameEvent healingApplied(ActorId sourceActorId, ActorId targetActorId, int amount) {
        return new GameEvent(EventType.HEALING_APPLIED, sourceActorId, targetActorId, null, amount, null);
    }

    /**
     * Create an actor died event.
     */
    public static GameEvent actorDied(ActorId actorId) {
        return new GameEvent(EventType.ACTOR_DIED, actorId, null, null, 0, null);
    }

    /**
     * Create a buff applied event.
     */
    public static GameEvent buffApplied(ActorId targetActorId, String buffName) {
        return new GameEvent(EventType.BUFF_APPLIED, null, targetActorId, null, 0, buffName);
    }

    /**
     * Create a buff removed event.
     */
    public static GameEvent buffRemoved(ActorId targetActorId, String buffName) {
        return new GameEvent(EventType.BUFF_REMOVED, null, targetActorId, null, 0, buffName);
    }

    /**
     * Create a turn started event.
     */
    public static GameEvent turnStarted(ActorId actorId) {
        return new GameEvent(EventType.TURN_STARTED, actorId, null, null, 0, null);
    }

    /**
     * Create a turn ended event.
     */
    public static GameEvent turnEnded(ActorId actorId) {
        return new GameEvent(EventType.TURN_ENDED, actorId, null, null, 0, null);
    }

    /**
     * Create a log message event.
     */
    public static GameEvent logMessage(String message) {
        return new GameEvent(EventType.LOG_MESSAGE, null, null, null, 0, message);
    }

    /**
     * Create a system event.
     */
    public static GameEvent system(String message) {
        return new GameEvent(EventType.SYSTEM, null, null, null, 0, message);
    }

    // ===== Getters =====

    public EventType getType() {
        return type;
    }

    public ActorId getSourceActorId() {
        return sourceActorId;
    }

    public ActorId getTargetActorId() {
        return targetActorId;
    }

    public Point getPosition() {
        return position;
    }

    public int getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    // ===== Utility =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameEvent{").append(type);
        if (sourceActorId != null) sb.append(", source=").append(sourceActorId);
        if (targetActorId != null) sb.append(", target=").append(targetActorId);
        if (position != null) sb.append(", pos=").append(position);
        if (amount != 0) sb.append(", amount=").append(amount);
        if (message != null) sb.append(", msg='").append(message).append("'");
        sb.append("}");
        return sb.toString();
    }
}
