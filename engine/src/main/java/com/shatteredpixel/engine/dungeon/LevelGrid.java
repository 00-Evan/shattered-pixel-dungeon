package com.shatteredpixel.engine.dungeon;

import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.dungeon.terrain.TileFlags;
import com.shatteredpixel.engine.geom.Point;

/**
 * 2D grid representing a dungeon level layout.
 *
 * Stores terrain type and flags for each tile.
 * Provides queries for passability, transparency, and bounds checking.
 *
 * This is a generic container - no generation logic, FOV, or actor management.
 */
public class LevelGrid {

    private final int width;
    private final int height;
    private final TerrainType[] terrain;
    private final TileFlags[] flags;

    /**
     * Create a new level grid with the specified dimensions.
     * All terrain is initialized to UNKNOWN.
     */
    public LevelGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.terrain = new TerrainType[width * height];
        this.flags = new TileFlags[width * height];

        // Initialize all terrain to UNKNOWN
        for (int i = 0; i < terrain.length; i++) {
            terrain[i] = TerrainType.UNKNOWN;
            flags[i] = new TileFlags();
        }
    }

    // ===== Dimensions =====

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // ===== Bounds Checking =====

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isInBounds(Point p) {
        return isInBounds(p.x, p.y);
    }

    // ===== Terrain Access =====

    public TerrainType getTerrain(int x, int y) {
        if (!isInBounds(x, y)) {
            return TerrainType.UNKNOWN;
        }
        return terrain[x + y * width];
    }

    public TerrainType getTerrain(Point p) {
        return getTerrain(p.x, p.y);
    }

    public void setTerrain(int x, int y, TerrainType type) {
        if (isInBounds(x, y)) {
            terrain[x + y * width] = type;
        }
    }

    public void setTerrain(Point p, TerrainType type) {
        setTerrain(p.x, p.y, type);
    }

    // ===== Flags Access =====

    public TileFlags getFlags(int x, int y) {
        if (!isInBounds(x, y)) {
            return new TileFlags(); // Empty flags for out-of-bounds
        }
        return flags[x + y * width];
    }

    public TileFlags getFlags(Point p) {
        return getFlags(p.x, p.y);
    }

    // ===== Terrain Queries =====

    /**
     * Check if a tile is passable (can be walked on).
     */
    public boolean isPassable(int x, int y) {
        if (!isInBounds(x, y)) {
            return false;
        }
        TerrainType type = getTerrain(x, y);
        return type.isPassable();
    }

    public boolean isPassable(Point p) {
        return isPassable(p.x, p.y);
    }

    /**
     * Check if a tile is transparent (allows vision).
     */
    public boolean isTransparent(int x, int y) {
        if (!isInBounds(x, y)) {
            return false;
        }
        TerrainType type = getTerrain(x, y);
        return type.isTransparent();
    }

    public boolean isTransparent(Point p) {
        return isTransparent(p.x, p.y);
    }

    /**
     * Check if a tile is hazardous (causes damage or negative effects).
     */
    public boolean isHazardous(int x, int y) {
        if (!isInBounds(x, y)) {
            return false;
        }
        TerrainType type = getTerrain(x, y);
        return type.isHazardous();
    }

    public boolean isHazardous(Point p) {
        return isHazardous(p.x, p.y);
    }

    // ===== Utility =====

    /**
     * Fill the entire grid with a single terrain type.
     */
    public void fill(TerrainType type) {
        for (int i = 0; i < terrain.length; i++) {
            terrain[i] = type;
        }
    }

    /**
     * Fill a rectangular region with a terrain type.
     */
    public void fillRect(int x, int y, int w, int h, TerrainType type) {
        for (int iy = y; iy < y + h; iy++) {
            for (int ix = x; ix < x + w; ix++) {
                setTerrain(ix, iy, type);
            }
        }
    }

    /**
     * Clear all tile flags (set to 0).
     */
    public void clearFlags() {
        for (TileFlags flag : flags) {
            flag.clear();
        }
    }

    @Override
    public String toString() {
        return String.format("LevelGrid{%dx%d}", width, height);
    }
}
