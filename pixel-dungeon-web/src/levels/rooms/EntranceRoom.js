/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Entrance Room
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/standard/entrance/EntranceRoom.java
 */

import { StandardRoom } from './StandardRoom.js';
import { DoorType } from './Room.js';
import { TerrainType } from '../Terrain.js';
import { Random } from '../../utils/Random.js';

/**
 * Level entrance room
 * Source: EntranceRoom.java:42-189
 */
export class EntranceRoom extends StandardRoom {
    /**
     * Minimum dimensions (at least 5x5)
     * Source: EntranceRoom.java:45-52
     */
    minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    /**
     * Mark as entrance
     * Source: EntranceRoom.java:55-57
     */
    isEntrance() {
        return true;
    }

    /**
     * Can merge? (restricted on early floors)
     * Source: EntranceRoom.java:60-66
     *
     * Note: Dungeon.depth will need to be passed in or accessed globally
     */
    canMerge(level, other, p, mergeTerrain) {
        if (level.depth <= 2) {
            return false;
        } else {
            return super.canMerge(level, other, p, mergeTerrain);
        }
    }

    /**
     * Can place trap? (no traps on floor 1)
     * Source: EntranceRoom.java:69-75
     */
    canPlaceTrap(p) {
        // TODO: Need access to Dungeon.depth
        // if (Dungeon.depth === 1) return false;
        return super.canPlaceTrap(p);
    }

    /**
     * Paint the entrance room
     * Source: EntranceRoom.java:77-100
     */
    paint(level) {
        // This will be implemented fully when Painter is ported
        // Painter.fill(level, this, Terrain.WALL);
        // Painter.fill(level, this, 1, Terrain.EMPTY);

        // Set all doors to REGULAR type
        for (const door of this.connected.values()) {
            if (door) door.set(DoorType.REGULAR);
        }

        // Place entrance tile
        let entrance;
        do {
            entrance = level.pointToCell(this.random(2));
        } while (level.findMob && level.findMob(entrance) != null);

        // Painter.set(level, entrance, Terrain.ENTRANCE);

        // Add level transition (will be implemented with Level class)
        // if (Dungeon.depth === 1) {
        //     level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.SURFACE));
        // } else {
        //     level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
        // }
    }

    /**
     * Factory method to create random entrance room
     * Source: EntranceRoom.java:184-186
     *
     * TODO: Implement when room type system is ready
     */
    static createEntrance() {
        // For now, return basic entrance
        return new EntranceRoom();
    }
}
