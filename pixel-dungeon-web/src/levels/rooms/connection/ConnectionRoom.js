/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Connection Room (Hallways)
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/connection/ConnectionRoom.java
 */

import { Room, RoomDirection } from '../Room.js';

/**
 * Base class for connection rooms (hallways between main rooms)
 * Source: ConnectionRoom.java:31-84
 */
export class ConnectionRoom extends Room {
    /**
     * Minimum width
     * Source: ConnectionRoom.java:34
     */
    minWidth() {
        return 3;
    }

    /**
     * Maximum width
     * Source: ConnectionRoom.java:35
     */
    maxWidth() {
        return 10;
    }

    /**
     * Minimum height
     * Source: ConnectionRoom.java:38
     */
    minHeight() {
        return 3;
    }

    /**
     * Maximum height
     * Source: ConnectionRoom.java:39
     */
    maxHeight() {
        return 10;
    }

    /**
     * Minimum connections required
     * Connection rooms need at least 2 connections
     * Source: ConnectionRoom.java:42-45
     */
    minConnections(direction) {
        if (direction === RoomDirection.ALL) {
            return 2;
        } else {
            return 0;
        }
    }
}
