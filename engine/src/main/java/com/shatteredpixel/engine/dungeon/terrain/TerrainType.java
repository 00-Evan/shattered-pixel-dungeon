package com.shatteredpixel.engine.dungeon.terrain;

/**
 * Generic terrain categories.
 *
 * These represent broad tile types without game-specific semantics.
 * Concrete terrain variants (grass, stone, carpet, etc.) will be
 * layered on top of this foundation.
 */
public enum TerrainType {

    /**
     * Empty floor (passable, transparent).
     */
    FLOOR,

    /**
     * Solid wall (impassable, opaque).
     */
    WALL,

    /**
     * Door (passable when open, opaque when closed).
     * NOTE: Open/closed state should be tracked via TileFlags or separate state.
     */
    DOOR,

    /**
     * Water (passable with restrictions, transparent).
     */
    WATER,

    /**
     * Pit/chasm (impassable for most, hazardous).
     */
    PIT,

    /**
     * Placeholder for traps (passable, transparent, but hazardous).
     */
    TRAP_PLACEHOLDER,

    /**
     * Unknown/uninitialized terrain.
     */
    UNKNOWN;

    /**
     * Check if this terrain type is passable by default.
     * Concrete implementations may add exceptions (e.g., flying over pits).
     */
    public boolean isPassable() {
        switch (this) {
            case FLOOR:
            case DOOR: // Assume open by default
            case WATER:
            case TRAP_PLACEHOLDER:
                return true;
            case WALL:
            case PIT:
            case UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * Check if this terrain type is transparent (allows vision).
     */
    public boolean isTransparent() {
        switch (this) {
            case FLOOR:
            case DOOR: // Assume open by default
            case WATER:
            case PIT:
            case TRAP_PLACEHOLDER:
                return true;
            case WALL:
            case UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * Check if this terrain type is hazardous (causes damage or negative effects).
     */
    public boolean isHazardous() {
        switch (this) {
            case PIT:
            case TRAP_PLACEHOLDER:
                return true;
            case FLOOR:
            case WALL:
            case DOOR:
            case WATER:
            case UNKNOWN:
                return false;
            default:
                return false;
        }
    }
}
