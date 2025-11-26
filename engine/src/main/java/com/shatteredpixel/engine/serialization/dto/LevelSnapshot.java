package com.shatteredpixel.engine.serialization.dto;

import java.util.Arrays;

/**
 * Snapshot of a single dungeon level's state.
 *
 * This captures all the data needed to reconstruct a level:
 * - Dimensions (width, height, depth)
 * - Terrain types for each tile (flattened 2D → 1D array)
 * - Tile flags for each tile (flattened 2D → 1D array of bitmasks)
 *
 * Terrain and flags are stored as 1D arrays for simplicity and efficiency:
 * - Index = x + y * width
 * - terrainIds[i] = TerrainType.ordinal() for that tile
 * - tileFlags[i] = int bitmask (DISCOVERED, VISIBLE, OCCUPIED, MARKED)
 *
 * TODO: Future enhancements:
 * - Planted seeds/traps
 * - Door states (open/closed/locked)
 * - Environmental effects (fire, water, gas)
 * - Room/region metadata
 * - Generation seed for regeneration
 */
public class LevelSnapshot {

    public final int depth;
    public final int width;
    public final int height;
    public final int[] terrainIds;  // TerrainType.ordinal() per tile
    public final int[] tileFlags;   // TileFlags bitmask per tile

    /**
     * Create a level snapshot.
     *
     * @param depth Dungeon depth (1 = first floor)
     * @param width Level width in tiles
     * @param height Level height in tiles
     * @param terrainIds Flattened terrain array (TerrainType ordinals)
     * @param tileFlags Flattened flags array (int bitmasks)
     */
    public LevelSnapshot(int depth, int width, int height, int[] terrainIds, int[] tileFlags) {
        this.depth = depth;
        this.width = width;
        this.height = height;
        this.terrainIds = terrainIds != null ? Arrays.copyOf(terrainIds, terrainIds.length) : new int[0];
        this.tileFlags = tileFlags != null ? Arrays.copyOf(tileFlags, tileFlags.length) : new int[0];

        // Validation
        int expectedSize = width * height;
        if (this.terrainIds.length != expectedSize) {
            throw new IllegalArgumentException(
                "terrainIds length (" + this.terrainIds.length + ") " +
                "does not match width*height (" + expectedSize + ")"
            );
        }
        if (this.tileFlags.length != expectedSize) {
            throw new IllegalArgumentException(
                "tileFlags length (" + this.tileFlags.length + ") " +
                "does not match width*height (" + expectedSize + ")"
            );
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LevelSnapshot)) return false;
        LevelSnapshot other = (LevelSnapshot) obj;
        return this.depth == other.depth
            && this.width == other.width
            && this.height == other.height
            && Arrays.equals(this.terrainIds, other.terrainIds)
            && Arrays.equals(this.tileFlags, other.tileFlags);
    }

    @Override
    public int hashCode() {
        int result = depth;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + Arrays.hashCode(terrainIds);
        result = 31 * result + Arrays.hashCode(tileFlags);
        return result;
    }

    @Override
    public String toString() {
        return "LevelSnapshot{" +
            "depth=" + depth +
            ", size=" + width + "x" + height +
            ", tiles=" + terrainIds.length +
            "}";
    }
}
