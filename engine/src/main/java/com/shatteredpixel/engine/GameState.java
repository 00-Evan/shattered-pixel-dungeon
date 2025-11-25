package com.shatteredpixel.engine;

/**
 * Represents the complete state of the game world.
 * This includes dungeon layout, hero state, items, mobs, etc.
 *
 * Design goals:
 * - Serializable for save/load and multiplayer sync
 * - Immutable where possible (use builder pattern for updates)
 * - No platform-specific dependencies (headless)
 * - Deterministic - same inputs produce same state transitions
 *
 * Future: This will hold all migrated game data from :core
 * (Dungeon, Hero, Level, Items, Mobs, etc.)
 */
public class GameState {

    private int depth;
    private long turnCount;
    private boolean gameOver;

    /**
     * Create a new empty game state.
     */
    public GameState() {
        this.depth = 1;
        this.turnCount = 0;
        this.gameOver = false;
    }

    /**
     * Get the current dungeon depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Set the dungeon depth.
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Get the total turn count (for deterministic simulation).
     */
    public long getTurnCount() {
        return turnCount;
    }

    /**
     * Increment the turn counter.
     */
    public void incrementTurn() {
        this.turnCount++;
    }

    /**
     * Check if the game is over.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Mark the game as over.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Create a snapshot of the current state for serialization.
     * Future: This will serialize the entire game world.
     */
    public byte[] serialize() {
        // TODO: Implement proper serialization during migration
        return new byte[0];
    }

    /**
     * Restore state from a serialized snapshot.
     * Future: This will deserialize the entire game world.
     */
    public static GameState deserialize(byte[] data) {
        // TODO: Implement proper deserialization during migration
        return new GameState();
    }
}
