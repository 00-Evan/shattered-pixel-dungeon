package com.shatteredpixel.engine.dungeon.terrain;

/**
 * Flags for tile properties beyond terrain type.
 *
 * Uses a bitmask for efficient storage and serialization.
 * Can be extended with additional flags as needed.
 */
public class TileFlags {

    // Flag bits
    public static final int DISCOVERED = 1 << 0;  // Tile has been seen before
    public static final int VISIBLE = 1 << 1;     // Tile is currently visible
    public static final int OCCUPIED = 1 << 2;    // Tile is occupied by an actor
    public static final int MARKED = 1 << 3;      // Tile is marked (for UI, pathfinding, etc.)

    private int flags;

    public TileFlags() {
        this.flags = 0;
    }

    public TileFlags(int flags) {
        this.flags = flags;
    }

    // ===== Flag Checks =====

    public boolean isDiscovered() {
        return (flags & DISCOVERED) != 0;
    }

    public boolean isVisible() {
        return (flags & VISIBLE) != 0;
    }

    public boolean isOccupied() {
        return (flags & OCCUPIED) != 0;
    }

    public boolean isMarked() {
        return (flags & MARKED) != 0;
    }

    public boolean hasFlag(int flag) {
        return (flags & flag) != 0;
    }

    // ===== Flag Setters =====

    public void setDiscovered(boolean value) {
        if (value) {
            flags |= DISCOVERED;
        } else {
            flags &= ~DISCOVERED;
        }
    }

    public void setVisible(boolean value) {
        if (value) {
            flags |= VISIBLE;
        } else {
            flags &= ~VISIBLE;
        }
    }

    public void setOccupied(boolean value) {
        if (value) {
            flags |= OCCUPIED;
        } else {
            flags &= ~OCCUPIED;
        }
    }

    public void setMarked(boolean value) {
        if (value) {
            flags |= MARKED;
        } else {
            flags &= ~MARKED;
        }
    }

    public void setFlag(int flag, boolean value) {
        if (value) {
            flags |= flag;
        } else {
            flags &= ~flag;
        }
    }

    // ===== Utility =====

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void clear() {
        flags = 0;
    }

    public TileFlags copy() {
        return new TileFlags(flags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TileFlags{");
        if (isDiscovered()) sb.append("D");
        if (isVisible()) sb.append("V");
        if (isOccupied()) sb.append("O");
        if (isMarked()) sb.append("M");
        sb.append("}");
        return sb.toString();
    }
}
