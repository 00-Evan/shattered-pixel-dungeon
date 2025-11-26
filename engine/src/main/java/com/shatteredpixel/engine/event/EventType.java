package com.shatteredpixel.engine.event;

/**
 * Generic event categories.
 *
 * These represent observable state changes in the game engine.
 * Events are produced by the engine and consumed by clients (UI, networking, logging, etc.).
 */
public enum EventType {

    /**
     * An actor moved to a new position.
     */
    ACTOR_MOVED,

    /**
     * Damage was applied to an actor.
     */
    DAMAGE_APPLIED,

    /**
     * Healing was applied to an actor.
     */
    HEALING_APPLIED,

    /**
     * An actor died.
     */
    ACTOR_DIED,

    /**
     * A buff was applied to an actor.
     */
    BUFF_APPLIED,

    /**
     * A buff was removed from an actor.
     */
    BUFF_REMOVED,

    /**
     * An actor's turn started.
     */
    TURN_STARTED,

    /**
     * An actor's turn ended.
     */
    TURN_ENDED,

    /**
     * A log message or notification.
     */
    LOG_MESSAGE,

    /**
     * System event (game over, level transition, etc.).
     */
    SYSTEM;
}
