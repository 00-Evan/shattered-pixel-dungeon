package com.shatteredpixel.engine.command;

/**
 * Generic command categories.
 *
 * These are broad classifications, not specific game actions.
 * Concrete command implementations will use these types for routing.
 */
public enum CommandType {

    /**
     * Movement commands (walk, teleport, etc.).
     */
    MOVE,

    /**
     * Attack/combat commands.
     */
    ATTACK,

    /**
     * Use ability/skill/special action.
     */
    USE_ABILITY,

    /**
     * Wait/pass turn.
     */
    WAIT,

    /**
     * System commands (save, quit, etc.).
     */
    SYSTEM,

    /**
     * Debug/cheat commands.
     */
    DEBUG;
}
