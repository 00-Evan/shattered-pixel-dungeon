/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Standard Procedural Room
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/standard/StandardRoom.java
 */

import { Room } from './Room.js';
import { Random } from '../../utils/Random.js';
import { TerrainType, flags as TerrainFlags } from '../Terrain.js';
import { Painter } from '../painters/Painter.js';

/**
 * Room size categories
 * Source: StandardRoom.java:36-51
 */
export const SizeCategory = {
    NORMAL: { minDim: 4, maxDim: 10, roomValue: 1 },
    LARGE:  { minDim: 10, maxDim: 14, roomValue: 2 },
    GIANT:  { minDim: 14, maxDim: 18, roomValue: 3 }
};

/**
 * Base class for standard procedural rooms
 * Source: StandardRoom.java:34-194
 */
export class StandardRoom extends Room {
    constructor() {
        super();
        this.sizeCat = null;
        this.setSizeCat();
    }

    /**
     * Probability weights for each size category
     * Source: StandardRoom.java:58-61
     */
    sizeCatProbs() {
        // Always normal by default
        return [1, 0, 0];
    }

    /**
     * Set size category
     * Source: StandardRoom.java:63-90
     */
    setSizeCat(minOrdinal = 0, maxOrdinal = Object.keys(SizeCategory).length - 1) {
        const probs = this.sizeCatProbs();
        const categories = Object.keys(SizeCategory);

        if (probs.length !== categories.length) return false;

        // Zero out probabilities outside the valid range
        for (let i = 0; i < minOrdinal; i++) probs[i] = 0;
        for (let i = maxOrdinal + 1; i < categories.length; i++) probs[i] = 0;

        const ordinal = Random.chances(probs);

        if (ordinal !== -1) {
            this.sizeCat = SizeCategory[categories[ordinal]];
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set size category by max room value
     * Source: StandardRoom.java:68-70
     */
    setSizeCat_maxValue(maxRoomValue) {
        return this.setSizeCat(0, maxRoomValue - 1);
    }

    // ========== Size constraints ==========
    // Source: StandardRoom.java:92-98

    minWidth() { return this.sizeCat.minDim; }
    maxWidth() { return this.sizeCat.maxDim; }
    minHeight() { return this.sizeCat.minDim; }
    maxHeight() { return this.sizeCat.maxDim; }

    /**
     * Size factor for counting purposes
     * Larger rooms count as multiple rooms
     * Source: StandardRoom.java:102-104
     */
    sizeFactor() {
        return this.sizeCat.roomValue;
    }

    /**
     * Mob spawn weight
     * Source: StandardRoom.java:106-111
     */
    mobSpawnWeight() {
        if (this.isEntrance()) {
            return 1; // Entrance rooms don't have higher mob spawns
        }
        return this.sizeFactor();
    }

    /**
     * Connection weight for graph building
     * Source: StandardRoom.java:113-115
     */
    connectionWeight() {
        return this.sizeFactor() * this.sizeFactor();
    }

    /**
     * Can merge with another room?
     * Source: StandardRoom.java:118-121
     */
    canMerge(level, other, p, mergeTerrain) {
        const cell = level.pointToCell(this.pointInside(p, 1));
        return (TerrainFlags.flags[level.map[cell]] & TerrainFlags.SOLID) === 0;
    }

    /**
     * Basic paint implementation
     * Source: EntranceRoom.java:77-100 (used as reference)
     */
    paint(level) {
        // Default empty implementation - subclasses override
        throw new Error('StandardRoom.paint() must be overridden by subclass');
    }

    /**
     * Create a random standard room
     * Source: StandardRoom.java:190-192
     *
     * SIMPLIFIED: For initial port, returns a basic EmptyRoom
     * Full implementation would select from a list of specific room types
     */
    static createRoom() {
        return new EmptyRoom();
    }
}

/**
 * Simple empty room (for initial testing)
 * A basic rectangular room with floor tiles
 */
class EmptyRoom extends StandardRoom {
    constructor() {
        super();
    }

    /**
     * Paint a simple rectangular room
     */
    paint(level) {
        // Fill with floor tiles
        Painter.fill(level, this, TerrainType.EMPTY);
    }
}
