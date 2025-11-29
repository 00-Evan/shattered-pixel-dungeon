/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Regular Level - Procedurally generated dungeon level
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/RegularLevel.java
 */

import { Level, Feeling } from './Level.js';
import { Random } from '../utils/Random.js';
import { LoopBuilder } from './builders/LoopBuilder.js';
import { FigureEightBuilder } from './builders/FigureEightBuilder.js';
import { RegularPainter } from './painters/RegularPainter.js';
import { EntranceRoom } from './rooms/EntranceRoom.js';
import { ExitRoom } from './rooms/ExitRoom.js';
import { StandardRoom } from './rooms/StandardRoom.js';

/**
 * Regular Level
 * Source: RegularLevel.java:94-500+
 */
export class RegularLevel extends Level {
    constructor() {
        super();

        this.rooms = [];
        this.builder = null;

        this.roomEntrance = null;
        this.roomExit = null;
    }

    /**
     * Build the level layout
     * Source: RegularLevel.java:104-121
     */
    build() {
        this.builder = this.builder_();

        const initRooms = this.initRooms();
        Random.shuffle(initRooms);

        // Retry until builder succeeds
        // Source: RegularLevel.java:111-117
        do {
            for (const r of initRooms) {
                r.neigbours.length = 0;
                r.connected.clear();
            }
            this.rooms = this.builder.build([...initRooms]);
        } while (this.rooms === null);

        // Paint rooms onto level map
        // Source: RegularLevel.java:119
        return this.painter().paint(this, this.rooms);
    }

    /**
     * Initialize rooms for the level
     * Source: RegularLevel.java:123-165
     */
    initRooms() {
        const initRooms = [];

        // Add entrance and exit
        // Source: RegularLevel.java:125-126
        initRooms.push(this.roomEntrance = EntranceRoom.createEntrance());
        initRooms.push(this.roomExit = ExitRoom.createExit());

        // Calculate number of standard rooms
        // Source: RegularLevel.java:129-132
        let standards = this.standardRooms(this.feeling === Feeling.LARGE);
        if (this.feeling === Feeling.LARGE) {
            standards = Math.ceil(standards * 1.5);
        }

        // Create standard rooms
        // Source: RegularLevel.java:133-140
        for (let i = 0; i < standards; i++) {
            let s;
            do {
                s = StandardRoom.createRoom();
            } while (!s.setSizeCat(standards - i));
            i += s.sizeFactor() - 1;
            initRooms.push(s);
        }

        return initRooms;
    }

    /**
     * Get number of standard rooms
     * Can be overridden by subclasses
     * Source: RegularLevel.java:167-169
     */
    standardRooms(forceMax) {
        return 8; // Default for testing
    }

    /**
     * Get number of special rooms
     * Can be overridden by subclasses
     * Source: RegularLevel.java:171-173
     */
    specialRooms(forceMax) {
        return 0; // Simplified for initial port
    }

    /**
     * Get the builder to use
     * Source: RegularLevel.java:175-188
     */
    builder_() {
        // 50% chance of LoopBuilder or FigureEightBuilder
        // Source: RegularLevel.java:176
        if (Random.Int_max(2) === 0) {
            return new LoopBuilder()
                .setLoopShape(2, Random.Float() * 0.65, Random.Float() * 0.50);
        } else {
            return new FigureEightBuilder()
                .setLoopShape(2, Random.Float() * 0.5 + 0.3, 0);
        }
    }

    /**
     * Get the painter to use
     * Source: RegularLevel.java:190 (abstract method)
     */
    painter() {
        return new RegularPainter();
    }
}
