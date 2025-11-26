package com.shatteredpixel.engine.command;

import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.geom.Point;

/**
 * Immutable DTO representing a single command input to the engine.
 *
 * Commands describe what an actor wants to do:
 * - Move to a position
 * - Attack another actor
 * - Use an ability
 * - Wait/pass turn
 *
 * This is designed to be:
 * - Serializable (for replay, networking, save games)
 * - Deterministic (same command + same state = same result)
 * - Generic (no SPD-specific logic)
 *
 * All fields are optional except type and actorId. The combination of type
 * and fields determines what the command does.
 */
public final class GameCommand {

    private final CommandType type;
    private final ActorId actorId;

    // Optional fields (null if not applicable)
    private final Point targetPosition;
    private final ActorId targetActorId;
    private final String actionId; // Generic identifier for abilities/items/etc.

    /**
     * Full constructor.
     */
    public GameCommand(CommandType type, ActorId actorId, Point targetPosition,
                      ActorId targetActorId, String actionId) {
        this.type = type;
        this.actorId = actorId;
        this.targetPosition = targetPosition;
        this.targetActorId = targetActorId;
        this.actionId = actionId;
    }

    /**
     * Simple constructor with minimal fields.
     */
    public GameCommand(CommandType type, ActorId actorId) {
        this(type, actorId, null, null, null);
    }

    // ===== Factory Methods =====

    /**
     * Create a move command.
     */
    public static GameCommand moveTo(ActorId actorId, Point targetPosition) {
        return new GameCommand(CommandType.MOVE, actorId, targetPosition, null, null);
    }

    /**
     * Create an attack command.
     */
    public static GameCommand attackActor(ActorId attackerId, ActorId targetId) {
        return new GameCommand(CommandType.ATTACK, attackerId, null, targetId, null);
    }

    /**
     * Create a use ability command.
     */
    public static GameCommand useAbility(ActorId actorId, String abilityId) {
        return new GameCommand(CommandType.USE_ABILITY, actorId, null, null, abilityId);
    }

    /**
     * Create a use ability command with target position.
     */
    public static GameCommand useAbility(ActorId actorId, String abilityId, Point targetPosition) {
        return new GameCommand(CommandType.USE_ABILITY, actorId, targetPosition, null, abilityId);
    }

    /**
     * Create a use ability command with target actor.
     */
    public static GameCommand useAbility(ActorId actorId, String abilityId, ActorId targetActorId) {
        return new GameCommand(CommandType.USE_ABILITY, actorId, null, targetActorId, abilityId);
    }

    /**
     * Create a wait/pass turn command.
     */
    public static GameCommand wait(ActorId actorId) {
        return new GameCommand(CommandType.WAIT, actorId);
    }

    /**
     * Create a system command.
     */
    public static GameCommand system(String systemAction) {
        return new GameCommand(CommandType.SYSTEM, null, null, null, systemAction);
    }

    // ===== Getters =====

    public CommandType getType() {
        return type;
    }

    public ActorId getActorId() {
        return actorId;
    }

    public Point getTargetPosition() {
        return targetPosition;
    }

    public ActorId getTargetActorId() {
        return targetActorId;
    }

    public String getActionId() {
        return actionId;
    }

    // ===== Utility =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameCommand{").append(type);
        if (actorId != null) sb.append(", actor=").append(actorId);
        if (targetPosition != null) sb.append(", pos=").append(targetPosition);
        if (targetActorId != null) sb.append(", target=").append(targetActorId);
        if (actionId != null) sb.append(", action=").append(actionId);
        sb.append("}");
        return sb.toString();
    }
}
