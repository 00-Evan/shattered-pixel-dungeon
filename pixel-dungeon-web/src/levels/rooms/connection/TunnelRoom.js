/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Tunnel Room - Simple L-shaped hallways
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/connection/TunnelRoom.java
 */

import { ConnectionRoom } from './ConnectionRoom.js';
import { DoorType } from '../Room.js';
import { Point } from '../../../utils/Point.js';
import { PointF } from '../../../utils/PointF.js';
import { Rect } from '../../../utils/Rect.js';
import { Random } from '../../../utils/Random.js';

/**
 * GameMath utility (simplified)
 * Source: com/watabou/utils/GameMath.java
 */
class GameMath {
    static gate(min, value, max) {
        return Math.max(min, Math.min(value, max));
    }
}

/**
 * Painter utility (simplified for TunnelRoom)
 * Source: levels/painters/Painter.java
 */
class Painter {
    /**
     * Draw a line of floor tiles between two points
     * Source: Painter.java (drawLine methods)
     */
    static drawLine(level, from, to, tile) {
        const dx = to.x - from.x;
        const dy = to.y - from.y;
        const steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (let i = 0; i <= steps; i++) {
            const x = Math.round(from.x + (dx * i) / steps);
            const y = Math.round(from.y + (dy * i) / steps);
            const cell = level.cell(x, y);
            if (level.insideMap(cell)) {
                level.map[cell] = tile;
            }
        }
    }

    /**
     * Set a single tile
     * Source: Painter.java (set methods)
     */
    static set(level, point, tile) {
        const cell = level.cell(point.x, point.y);
        if (level.insideMap(cell)) {
            level.map[cell] = tile;
        }
    }
}

/**
 * Tunnel Room - Draws straight-line tunnels from each door to a central point
 * Source: TunnelRoom.java:33-121
 */
export class TunnelRoom extends ConnectionRoom {
    /**
     * Paint tunnels connecting all doors
     * Source: TunnelRoom.java:35-94
     */
    paint(level) {
        const floor = level.tunnelTile();

        const c = this.getConnectionSpace();

        // Draw tunnels from each door to the center
        // Source: TunnelRoom.java:41-77
        for (const [room, door] of this.connected.entries()) {
            let start;
            let mid;
            let end;

            // Start just inside the door
            // Source: TunnelRoom.java:47-51
            start = new Point(door);
            if (start.x === this.left) {
                start.x++;
            } else if (start.y === this.top) {
                start.y++;
            } else if (start.x === this.right) {
                start.x--;
            } else if (start.y === this.bottom) {
                start.y--;
            }

            let rightShift;
            let downShift;

            // Calculate shift needed to reach connection space
            // Source: TunnelRoom.java:56-62
            if (start.x < c.left) {
                rightShift = c.left - start.x;
            } else if (start.x > c.right) {
                rightShift = c.right - start.x;
            } else {
                rightShift = 0;
            }

            if (start.y < c.top) {
                downShift = c.top - start.y;
            } else if (start.y > c.bottom) {
                downShift = c.bottom - start.y;
            } else {
                downShift = 0;
            }

            // Always goes inward first (creates L-shape)
            // Source: TunnelRoom.java:64-73
            if (door.x === this.left || door.x === this.right) {
                mid = new Point(start.x + rightShift, start.y);
                end = new Point(mid.x, mid.y + downShift);
            } else {
                mid = new Point(start.x, start.y + downShift);
                end = new Point(mid.x + rightShift, mid.y);
            }

            Painter.drawLine(level, start, mid, floor);
            Painter.drawLine(level, mid, end, floor);
        }

        // Fill in an extra diagonal tile at center randomly
        // for larger rooms with many connections
        // Source: TunnelRoom.java:79-89
        if (this.width() >= 7 && this.height() >= 7 &&
            this.connected.size >= 4 && c.square() === 0) {
            const p = new Point(c.left, c.top);
            p.x += Random.Int_max(2) === 0 ? 1 : -1;
            p.y += Random.Int_max(2) === 0 ? 1 : -1;
            // Prevent filling a tile outside the room
            p.x = GameMath.gate(this.left + 1, p.x, this.right - 1);
            p.y = GameMath.gate(this.top + 1, p.y, this.bottom - 1);
            Painter.set(level, p, floor);
        }

        // Set all doors to TUNNEL type
        // Source: TunnelRoom.java:91-93
        for (const [room, door] of this.connected.entries()) {
            door.set(DoorType.TUNNEL);
        }
    }

    /**
     * Returns the space which all doors must connect to
     * Usually 1 cell, but can be more
     * Source: TunnelRoom.java:96-102
     */
    getConnectionSpace() {
        const c = this.getDoorCenter();
        return new Rect(c.x, c.y, c.x, c.y);
    }

    /**
     * Returns a point equidistant from all doors
     * Source: TunnelRoom.java:104-120
     */
    getDoorCenter() {
        const doorCenter = new PointF(0, 0);

        // Sum all door positions
        // Source: TunnelRoom.java:108-111
        for (const [room, door] of this.connected.entries()) {
            doorCenter.x += door.x;
            doorCenter.y += door.y;
        }

        // Average and randomize fractional parts
        // Source: TunnelRoom.java:113-118
        const c = new Point(
            Math.floor(doorCenter.x / this.connected.size),
            Math.floor(doorCenter.y / this.connected.size)
        );

        if (Random.Float() < doorCenter.x % 1) c.x++;
        if (Random.Float() < doorCenter.y % 1) c.y++;

        c.x = GameMath.gate(this.left + 1, c.x, this.right - 1);
        c.y = GameMath.gate(this.top + 1, c.y, this.bottom - 1);

        return c;
    }
}
