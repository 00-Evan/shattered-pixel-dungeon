package com.shatteredpixel.engine.dungeon;

/**
 * Snapshot-friendly representation of a dungeon level.
 *
 * Holds basic level metadata and can be extended with:
 * - Terrain data
 * - Exploration state
 * - Item/actor references
 * - Room/region information
 *
 * Designed for serialization and save/load.
 */
public class LevelState {

    private int depth;           // Dungeon depth (1 = first floor, etc.)
    private int width;
    private int height;
    private LevelGrid grid;      // The actual level grid

    // Future: Add more state
    // - List<ActorId> actors
    // - List<ItemId> items
    // - Seed for regeneration
    // - Boss defeated flags
    // - etc.

    public LevelState(int depth, int width, int height) {
        this.depth = depth;
        this.width = width;
        this.height = height;
        this.grid = new LevelGrid(width, height);
    }

    // ===== Getters =====

    public int getDepth() {
        return depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LevelGrid getGrid() {
        return grid;
    }

    // ===== Setters =====

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setGrid(LevelGrid grid) {
        this.grid = grid;
        this.width = grid.getWidth();
        this.height = grid.getHeight();
    }

    // ===== Utility =====

    @Override
    public String toString() {
        return String.format("LevelState{depth=%d, %dx%d}", depth, width, height);
    }
}
