/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Base Level Class
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/Level.java
 */

import { Random } from '../utils/Random.js';
import { TerrainType, flags as TerrainFlags } from './Terrain.js';

/**
 * Level Feeling enum
 * Source: Level.java:125-142
 */
export const Feeling = {
    NONE: 'NONE',
    CHASM: 'CHASM',
    WATER: 'WATER',
    GRASS: 'GRASS',
    DARK: 'DARK',
    LARGE: 'LARGE',
    TRAPS: 'TRAPS',
    SECRETS: 'SECRETS'
};

/**
 * Abstract Base Level Class
 * Source: Level.java:123-900+
 */
export class Level {
    constructor() {
        // Dimensions
        // Source: Level.java:144-146
        this.width = 0;
        this.height = 0;
        this.length = 0;

        // Map arrays
        // Source: Level.java:152-168
        this.map = null;        // Terrain tiles (Uint8Array)
        this.visited = null;    // Explored tiles (boolean[])
        this.mapped = null;     // Revealed on minimap (boolean[])

        this.heroFOV = null;    // Current field of view (boolean[])

        // Terrain flag caches (computed from map + terrain flags)
        // Source: Level.java:161-168
        this.passable = null;   // Can be walked through
        this.losBlocking = null;// Blocks line of sight
        this.flamable = null;   // Can burn
        this.secret = null;     // Hidden terrain
        this.solid = null;      // Solid terrain
        this.avoid = null;      // AI avoids
        this.water = null;      // Water terrain
        this.pit = null;        // Pit terrain

        this.openSpace = null;  // Open space for flying

        // Level properties
        // Source: Level.java:172-180
        this.feeling = Feeling.NONE;
        this.entrance = 0;
        this.exit = 0;
        this.transitions = [];
        this.locked = false;

        // Game entities (simplified for dungeon generation)
        // Source: Level.java:182-188
        this.mobs = [];
        this.heaps = new Map();     // Map<pos, Heap>
        this.blobs = new Map();     // Map<class, Blob>
        this.plants = new Map();    // Map<pos, Plant>
        this.traps = new Map();     // Map<pos, Trap>
        this.customTiles = [];
        this.customWalls = [];

        // Items to spawn
        // Source: Level.java:190
        this.itemsToSpawn = [];

        // Visual properties
        // Source: Level.java:195-196
        this.color1 = 0x004400;
        this.color2 = 0x88CC44;
    }

    /**
     * Main level creation method
     * Source: Level.java:215-317
     */
    create(seed) {
        Random.pushGenerator_seed(seed);

        // Determine level feeling (simplified - no item spawning logic)
        // Source: Level.java:256-292
        // 50% chance of getting a level feeling
        // ~7.15% chance for each feeling
        switch (Random.Int_max(14)) {
            case 0:
                this.feeling = Feeling.CHASM;
                break;
            case 1:
                this.feeling = Feeling.WATER;
                break;
            case 2:
                this.feeling = Feeling.GRASS;
                break;
            case 3:
                this.feeling = Feeling.DARK;
                break;
            case 4:
                this.feeling = Feeling.LARGE;
                break;
            case 5:
                this.feeling = Feeling.TRAPS;
                break;
            case 6:
                this.feeling = Feeling.SECRETS;
                break;
            default:
                this.feeling = Feeling.NONE;
        }

        // Build loop - retry until successful
        // Source: Level.java:295-308
        let buildSuccess = false;
        do {
            this.width = this.height = this.length = 0;

            this.transitions = [];

            this.mobs = [];
            this.heaps = new Map();
            this.blobs = new Map();
            this.plants = new Map();
            this.traps = new Map();
            this.customTiles = [];
            this.customWalls = [];

            buildSuccess = this.build();
        } while (!buildSuccess);

        // Post-build processing
        // Source: Level.java:310-316
        this.buildFlagMaps();
        this.cleanWalls();

        Random.popGenerator();

        return this;
    }

    /**
     * Abstract build method - must be overridden by subclasses
     * Source: Level.java (abstract method)
     */
    build() {
        throw new Error('Level.build() must be overridden');
    }

    /**
     * Set level dimensions and initialize arrays
     * Source: Level.java:319-345
     */
    setSize(w, h) {
        this.width = w;
        this.height = h;
        this.length = w * h;

        // Initialize map with walls (or chasms for CHASM feeling)
        // Source: Level.java:325-326
        const fillTile = this.feeling === Feeling.CHASM ? TerrainType.CHASM : TerrainType.WALL;
        this.map = new Uint8Array(this.length);
        this.map.fill(fillTile);

        // Initialize boolean arrays
        // Source: Level.java:328-342
        this.visited = new Array(this.length).fill(false);
        this.mapped = new Array(this.length).fill(false);

        this.heroFOV = new Array(this.length).fill(false);

        this.passable = new Array(this.length).fill(false);
        this.losBlocking = new Array(this.length).fill(false);
        this.flamable = new Array(this.length).fill(false);
        this.secret = new Array(this.length).fill(false);
        this.solid = new Array(this.length).fill(false);
        this.avoid = new Array(this.length).fill(false);
        this.water = new Array(this.length).fill(false);
        this.pit = new Array(this.length).fill(false);

        this.openSpace = new Array(this.length).fill(false);
    }

    /**
     * Convert point coordinates to cell index
     * Source: Level.java (common utility)
     */
    pointToCell(point) {
        return point.x + point.y * this.width;
    }

    /**
     * Build terrain flag maps from map array
     * Source: Level.java:829-871
     */
    buildFlagMaps() {
        // Update flag arrays based on terrain
        // Source: Level.java:831-841
        for (let i = 0; i < this.length; i++) {
            const terrainFlags = TerrainFlags[this.map[i]];

            this.passable[i] = (terrainFlags & 0x01) !== 0;     // PASSABLE
            this.losBlocking[i] = (terrainFlags & 0x02) !== 0;  // LOS_BLOCKING
            this.flamable[i] = (terrainFlags & 0x04) !== 0;     // FLAMABLE
            this.secret[i] = (terrainFlags & 0x08) !== 0;       // SECRET
            this.solid[i] = (terrainFlags & 0x10) !== 0;        // SOLID
            this.avoid[i] = (terrainFlags & 0x20) !== 0;        // AVOID
            this.water[i] = (terrainFlags & 0x40) !== 0;        // LIQUID
            this.pit[i] = (terrainFlags & 0x80) !== 0;          // PIT
        }

        // Mark edges as non-passable
        // Source: Level.java:847-860
        const lastRow = this.length - this.width;
        for (let i = 0; i < this.width; i++) {
            this.passable[i] = false;
            this.passable[lastRow + i] = false;
        }
        for (let i = 0; i < this.length; i += this.width) {
            this.passable[i] = false;
            this.passable[i + this.width - 1] = false;
        }
    }

    /**
     * Clean up walls (remove unnecessary wall tiles)
     * Simplified version - just a placeholder for now
     * Source: Level.java:873-900+
     */
    cleanWalls() {
        // For now, do nothing - this is mainly for visual cleanup
        // Full implementation involves discovering certain walls based on neighbors
    }

    /**
     * Get cell index from x,y coordinates
     * Source: Level.java (implicit pattern, e.g., "x + y * width")
     */
    cell(x, y) {
        return x + y * this.width;
    }

    /**
     * Get x coordinate from cell index
     */
    cellX(cell) {
        return cell % this.width;
    }

    /**
     * Get y coordinate from cell index
     */
    cellY(cell) {
        return Math.floor(cell / this.width);
    }

    /**
     * Check if cell is within level bounds
     */
    insideMap(cell) {
        return cell >= 0 && cell < this.length;
    }

    /**
     * Get tunnel tile type (used by ConnectionRooms)
     * Can be overridden by subclasses
     * Source: Level.java (various implementations)
     */
    tunnelTile() {
        return TerrainType.EMPTY;
    }

    /**
     * Get width
     */
    width_() {
        return this.width;
    }

    /**
     * Get height
     */
    height_() {
        return this.height;
    }

    /**
     * Get length
     */
    length_() {
        return this.length;
    }
}
