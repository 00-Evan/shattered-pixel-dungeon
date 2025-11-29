/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Painter - Terrain Stamping Utilities
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/painters/Painter.java
 *
 * 🚨 CRITICAL: This is the bridge between abstract Rooms and concrete Terrain arrays
 * User Warning: "Ensure the logic that 'stamps' the room onto the map (setting the specific tile flags) is precise"
 */

import { Point } from '../../utils/Point.js';
import { Rect } from '../../utils/Rect.js';

/**
 * Abstract Painter class
 * Subclasses implement paint() to fill level terrain
 * Source: Painter.java:32-187
 */
export class Painter {
    /**
     * Paint a level with rooms
     * Source: Painter.java:38
     */
    paint(level, rooms) {
        throw new Error('Painter.paint() must be overridden');
    }

    // ========== Static Terrain Stamping Methods ==========
    // These methods write terrain values to the level map array

    /**
     * Set a single cell by index
     * Source: Painter.java:42-44
     */
    static set(level, ...args) {
        if (args.length === 2) {
            // set(level, cell, value)
            const [cell, value] = args;
            level.map[cell] = value;
        } else if (args.length === 3) {
            if (typeof args[0] === 'number') {
                // set(level, x, y, value)
                const [x, y, value] = args;
                Painter.set(level, x + y * level.width(), value);
            } else {
                // set(level, point, value)
                const [p, value] = args;
                Painter.set(level, p.x, p.y, value);
            }
        }
    }

    /**
     * Fill a rectangular region with a terrain value
     * Source: Painter.java:54-62
     *
     * 🚨 CRITICAL: Cell calculation formula
     * cell = y * width + x
     */
    static fill(level, ...args) {
        if (args[0] instanceof Rect) {
            // fill(level, rect, value)
            // fill(level, rect, margin, value)
            // fill(level, rect, l, t, r, b, value)
            const rect = args[0];

            if (args.length === 2) {
                // fill(level, rect, value)
                Painter.fill_xywh(level, rect.left, rect.top, rect.width(), rect.height(), args[1]);
            } else if (args.length === 3) {
                // fill(level, rect, margin, value)
                const m = args[1];
                const value = args[2];
                Painter.fill_xywh(level, rect.left + m, rect.top + m, rect.width() - m * 2, rect.height() - m * 2, value);
            } else if (args.length === 6) {
                // fill(level, rect, l, t, r, b, value)
                const [l, t, r, b, value] = args.slice(1);
                Painter.fill_xywh(level, rect.left + l, rect.top + t, rect.width() - (l + r), rect.height() - (t + b), value);
            }
        } else {
            // fill(level, x, y, w, h, value)
            Painter.fill_xywh(level, ...args);
        }
    }

    /**
     * Fill by explicit x, y, width, height
     * Source: Painter.java:54-62
     */
    static fill_xywh(level, x, y, w, h, value) {
        const width = level.width;

        let pos = y * width + x;
        for (let i = y; i < y + h; i++, pos += width) {
            level.map.fill(value, pos, pos + w);
        }
    }

    /**
     * Draw a line between two points
     * Source: Painter.java:76-98
     */
    static drawLine(level, from, to, value) {
        let x = from.x;
        let y = from.y;
        let dx = to.x - from.x;
        let dy = to.y - from.y;

        const movingbyX = Math.abs(dx) >= Math.abs(dy);

        // Normalize
        if (movingbyX) {
            dy /= Math.abs(dx);
            dx /= Math.abs(dx);
        } else {
            dx /= Math.abs(dy);
            dy /= Math.abs(dy);
        }

        Painter.set(level, Math.round(x), Math.round(y), value);
        while ((movingbyX && to.x !== x) || (!movingbyX && to.y !== y)) {
            x += dx;
            y += dy;
            Painter.set(level, Math.round(x), Math.round(y), value);
        }
    }

    /**
     * Fill an ellipse
     * Source: Painter.java:100-139
     */
    static fillEllipse(level, ...args) {
        if (args[0] instanceof Rect) {
            const rect = args[0];
            if (args.length === 2) {
                // fillEllipse(level, rect, value)
                Painter.fillEllipse_xywh(level, rect.left, rect.top, rect.width(), rect.height(), args[1]);
            } else if (args.length === 3) {
                // fillEllipse(level, rect, margin, value)
                const m = args[1];
                const value = args[2];
                Painter.fillEllipse_xywh(level, rect.left + m, rect.top + m, rect.width() - m * 2, rect.height() - m * 2, value);
            }
        } else {
            // fillEllipse(level, x, y, w, h, value)
            Painter.fillEllipse_xywh(level, ...args);
        }
    }

    /**
     * Fill ellipse by explicit coordinates
     * Source: Painter.java:108-139
     */
    static fillEllipse_xywh(level, x, y, w, h, value) {
        // Radii
        const radH = h / 2;
        const radW = w / 2;

        // Fill each row of the ellipse from top to bottom
        for (let i = 0; i < h; i++) {
            // Y coordinate for determining ellipse width
            // Always test middle of tile, hence the 0.5 shift
            const rowY = -radH + 0.5 + i;

            // Equation derived from ellipse formula: y^2/radH^2 + x^2/radW^2 = 1
            // Solves for x and then doubles to get width
            let rowW = 2.0 * Math.sqrt((radW * radW) * (1.0 - (rowY * rowY) / (radH * radH)));

            // Round to nearest even or odd number, depending on width
            if (w % 2 === 0) {
                rowW = Math.round(rowW / 2.0) * 2.0;
            } else {
                rowW = Math.floor(rowW / 2.0) * 2.0;
                rowW++;
            }

            const cell = x + (w - rowW) / 2 + ((y + i) * level.width());
            level.map.fill(value, cell, cell + rowW);
        }
    }

    /**
     * Fill a diamond shape
     * Source: Painter.java:141-162
     */
    static fillDiamond(level, ...args) {
        if (args[0] instanceof Rect) {
            const rect = args[0];
            if (args.length === 2) {
                Painter.fillDiamond_xywh(level, rect.left, rect.top, rect.width(), rect.height(), args[1]);
            } else if (args.length === 3) {
                const m = args[1];
                const value = args[2];
                Painter.fillDiamond_xywh(level, rect.left + m, rect.top + m, rect.width() - m * 2, rect.height() - m * 2, value);
            }
        } else {
            Painter.fillDiamond_xywh(level, ...args);
        }
    }

    /**
     * Fill diamond by explicit coordinates
     * Source: Painter.java:149-162
     */
    static fillDiamond_xywh(level, x, y, w, h, value) {
        // Calculate starting width
        let diamondWidth = w - (h - 2 - h % 2);
        // Starting width cannot be smaller than 2 on even width, 3 on odd width
        diamondWidth = Math.max(diamondWidth, w % 2 === 0 ? 2 : 3);

        for (let i = 0; i <= h; i++) {
            Painter.fill(level, x + (w - diamondWidth) / 2, y + i, diamondWidth, h - 2 * i, value);
            diamondWidth += 2;
            if (diamondWidth > w) break;
        }
    }

    /**
     * Draw n steps inside a room from a border point
     * Source: Painter.java:164-186
     */
    static drawInside(level, room, from, n, value) {
        const step = new Point();

        if (from.x === room.left) {
            step.set(+1, 0);
        } else if (from.x === room.right) {
            step.set(-1, 0);
        } else if (from.y === room.top) {
            step.set(0, +1);
        } else if (from.y === room.bottom) {
            step.set(0, -1);
        }

        const p = new Point(from).offset(step);
        for (let i = 0; i < n; i++) {
            if (value !== -1) {
                Painter.set(level, p, value);
            }
            p.offset(step);
        }

        return p;
    }
}
