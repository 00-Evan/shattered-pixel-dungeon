/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Regular Painter - Paints regular dungeon levels
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/painters/RegularPainter.java
 */

import { Painter } from './Painter.js';
import { Random } from '../../utils/Random.js';
import { Feeling } from '../Level.js';
import { TerrainType } from '../Terrain.js';
import { DoorType } from '../rooms/Room.js';

/**
 * Regular Painter
 * Paints rooms onto the level map
 * Source: RegularPainter.java:48-300+
 */
export class RegularPainter extends Painter {
    constructor() {
        super();

        // Water fill (simplified - not implemented yet)
        this.waterFill = 0;
        this.waterSmoothness = 0;

        // Grass fill (simplified - not implemented yet)
        this.grassFill = 0;
        this.grassSmoothness = 0;

        // Traps (simplified - not implemented yet)
        this.nTraps = 0;
        this.trapClasses = [];
        this.trapChances = [];
    }

    /**
     * Set water parameters
     * Source: RegularPainter.java:53-57
     */
    setWater(fill, smoothness) {
        this.waterFill = fill;
        this.waterSmoothness = smoothness;
        return this;
    }

    /**
     * Set grass parameters
     * Source: RegularPainter.java:62-66
     */
    setGrass(fill, smoothness) {
        this.grassFill = fill;
        this.grassSmoothness = smoothness;
        return this;
    }

    /**
     * Set trap parameters
     * Source: RegularPainter.java:72-77
     */
    setTraps(num, classes, chances) {
        this.nTraps = num;
        this.trapClasses = classes;
        this.trapChances = chances;
        return this;
    }

    /**
     * Get padding based on level feeling
     * Source: RegularPainter.java:79-81
     */
    padding(level) {
        return level.feeling === Feeling.CHASM ? 2 : 1;
    }

    /**
     * Paint the level with rooms
     * Source: RegularPainter.java:84-169
     */
    paint(level, rooms) {
        // Handle case with no rooms
        if (!rooms || rooms.length === 0) {
            if (level.length === 0) return false;
            rooms = [];
        }

        const padding = this.padding(level);

        // Find leftmost and topmost room bounds
        // Source: RegularPainter.java:91-96
        let leftMost = Number.MAX_SAFE_INTEGER;
        let topMost = Number.MAX_SAFE_INTEGER;

        for (const r of rooms) {
            if (r.left < leftMost) leftMost = r.left;
            if (r.top < topMost) topMost = r.top;
        }

        leftMost -= padding;
        topMost -= padding;

        // Shift all rooms and find rightmost/bottommost bounds
        // Source: RegularPainter.java:101-111
        let rightMost = 0;
        let bottomMost = 0;

        for (const r of rooms) {
            r.shift(-leftMost, -topMost);
            if (r.right > rightMost) rightMost = r.right;
            if (r.bottom > bottomMost) bottomMost = r.bottom;
        }

        rightMost += padding;
        bottomMost += padding;

        // Set level size
        // Source: RegularPainter.java:113
        level.setSize(rightMost + 1, bottomMost + 1);

        // Shuffle rooms for random painting order
        // Source: RegularPainter.java:122
        Random.shuffle(rooms);

        // Place doors and paint each room
        // Source: RegularPainter.java:124-131
        for (const r of rooms) {
            this.placeDoors(r);
            r.paint(level);
        }

        // Paint doors
        // Source: RegularPainter.java:133
        this.paintDoors(level, rooms);

        return true;
    }

    /**
     * Place door objects for a room
     * Source: RegularPainter.java:171-191
     */
    placeDoors(r) {
        for (const [n, door] of r.connected.entries()) {
            door.set(DoorType.REGULAR);
        }
    }

    /**
     * Paint door terrain onto level
     * Source: RegularPainter.java:193-210
     */
    paintDoors(level, rooms) {
        for (const r of rooms) {
            for (const [n, door] of r.connected.entries()) {
                if (door.type === DoorType.REGULAR) {
                    Painter.set(level, door, TerrainType.DOOR);
                } else if (door.type === DoorType.TUNNEL) {
                    Painter.set(level, door, level.tunnelTile());
                } else if (door.type === DoorType.EMPTY) {
                    Painter.set(level, door, TerrainType.EMPTY);
                } else if (door.type === DoorType.LOCKED) {
                    Painter.set(level, door, TerrainType.LOCKED_DOOR);
                } else if (door.type === DoorType.HIDDEN) {
                    Painter.set(level, door, TerrainType.SECRET_DOOR);
                } else if (door.type === DoorType.BARRICADE) {
                    Painter.set(level, door, TerrainType.BARRICADE);
                } else if (door.type === DoorType.UNLOCKED) {
                    Painter.set(level, door, TerrainType.DOOR);
                }
            }
        }
    }
}
