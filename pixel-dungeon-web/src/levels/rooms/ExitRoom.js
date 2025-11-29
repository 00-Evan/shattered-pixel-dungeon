/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Exit Room
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/standard/exit/ExitRoom.java
 */

import { StandardRoom } from './StandardRoom.js';
import { DoorType } from './Room.js';
import { TerrainType } from '../Terrain.js';

/**
 * Level exit room
 * Source: ExitRoom.java:37-125
 */
export class ExitRoom extends StandardRoom {
    /**
     * Minimum dimensions (at least 5x5)
     * Source: ExitRoom.java:40-47
     */
    minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    /**
     * Mark as exit
     * Source: ExitRoom.java:50-52
     */
    isExit() {
        return true;
    }

    /**
     * Paint the exit room
     * Source: ExitRoom.java:54-66
     */
    paint(level) {
        // This will be implemented fully when Painter is ported
        // Painter.fill(level, this, Terrain.WALL);
        // Painter.fill(level, this, 1, Terrain.EMPTY);

        // Set all doors to REGULAR type
        for (const door of this.connected.values()) {
            if (door) door.set(DoorType.REGULAR);
        }

        // Place exit tile
        const exit = level.pointToCell(this.random(2));
        // Painter.set(level, exit, Terrain.EXIT);

        // Add level transition (will be implemented with Level class)
        // level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
    }

    /**
     * Can place character? (not on exit tile)
     * Source: ExitRoom.java:69-71
     */
    canPlaceCharacter(p, level) {
        return super.canPlaceCharacter(p, level) && level.pointToCell(p) !== level.exit();
    }

    /**
     * Factory method to create random exit room
     * Source: ExitRoom.java:121-123
     *
     * TODO: Implement when room type system is ready
     */
    static createExit() {
        // For now, return basic exit
        return new ExitRoom();
    }
}
