/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Base Room Class with Connection Logic
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/rooms/Room.java
 */

import { Rect } from '../../utils/Rect.js';
import { Point } from '../../utils/Point.js';
import { Random } from '../../utils/Random.js';

/**
 * Door types
 * Source: Room.java:444-446
 */
export const DoorType = {
    EMPTY: 'EMPTY',
    TUNNEL: 'TUNNEL',
    WATER: 'WATER',
    REGULAR: 'REGULAR',
    UNLOCKED: 'UNLOCKED',
    HIDDEN: 'HIDDEN',
    BARRICADE: 'BARRICADE',
    LOCKED: 'LOCKED',
    CRYSTAL: 'CRYSTAL',
    WALL: 'WALL'
};

/**
 * Door class
 * Source: Room.java:442-487
 */
export class Door extends Point {
    constructor(x = 0, y = 0) {
        if (typeof x === 'object' && x instanceof Point) {
            super(x.x, x.y);
        } else {
            super(x, y);
        }
        this.type = DoorType.EMPTY;
        this.typeLocked = false;
    }

    lockTypeChanges(lock) {
        this.typeLocked = lock;
    }

    /**
     * Set door type (only if new type is "greater" than current)
     * Source: Room.java:466-470
     */
    set(type) {
        const typeOrder = Object.keys(DoorType);
        if (!this.typeLocked && typeOrder.indexOf(type) > typeOrder.indexOf(this.type)) {
            this.type = type;
        }
    }
}

/**
 * Room direction constants
 * Source: Room.java:168-172
 */
export const RoomDirection = {
    ALL: 0,
    LEFT: 1,
    TOP: 2,
    RIGHT: 3,
    BOTTOM: 4
};

/**
 * Base Room class
 * Extends Rect with graph node capabilities and connection logic
 * Source: Room.java:37-488
 */
export class Room extends Rect {
    constructor(...args) {
        super(...args);

        // Graph.Node interface
        // Source: Room.java:39-43
        this.neigbours = []; // Spelling intentional - matches Java typo
        this.connected = new Map(); // Map<Room, Door>
        this.distance = 0;
        this.price = 1;
    }

    /**
     * Set this room from another, transferring all connections
     * Source: Room.java:53-67
     */
    set_room(other) {
        super.set(other);

        // Transfer neighbors
        for (const r of other.neigbours) {
            this.neigbours.push(r);
            r.neigbours = r.neigbours.filter(n => n !== other);
            r.neigbours.push(this);
        }

        // Transfer connections
        for (const [r, d] of other.connected.entries()) {
            r.connected.delete(other);
            r.connected.set(this, d);
            this.connected.set(r, d);
        }

        return this;
    }

    // ========== Spatial Logic ==========
    // Source: Room.java:69-163

    minWidth() { return -1; }
    maxWidth() { return -1; }
    minHeight() { return -1; }
    maxHeight() { return -1; }

    setSize() {
        return this.setSize_minmax(this.minWidth(), this.maxWidth(), this.minHeight(), this.maxHeight());
    }

    forceSize(w, h) {
        return this.setSize_minmax(w, w, h, h);
    }

    /**
     * Set size with limit
     * Source: Room.java:90-102
     */
    setSizeWithLimit(w, h) {
        if (w < this.minWidth() || h < this.minHeight()) {
            return false;
        } else {
            this.setSize();

            if (this.width() > w || this.height() > h) {
                this.resize(Math.min(this.width(), w) - 1, Math.min(this.height(), h) - 1);
            }

            return true;
        }
    }

    /**
     * Set size within min/max bounds
     * Source: Room.java:104-118
     */
    setSize_minmax(minW, maxW, minH, maxH) {
        if (minW < this.minWidth()
                || maxW > this.maxWidth()
                || minH < this.minHeight()
                || maxH > this.maxHeight()
                || minW > maxW
                || minH > maxH) {
            return false;
        } else {
            // Subtract one because rooms are inclusive to their right and bottom sides
            // Source: Room.java:113-115
            this.resize(Random.NormalIntRange(minW, maxW) - 1,
                       Random.NormalIntRange(minH, maxH) - 1);
            return true;
        }
    }

    /**
     * Get a point n steps inside from a border point
     * Source: Room.java:120-132
     */
    pointInside(from, n) {
        const step = new Point(from);
        if (from.x === this.left) {
            step.offset(+n, 0);
        } else if (from.x === this.right) {
            step.offset(-n, 0);
        } else if (from.y === this.top) {
            step.offset(0, +n);
        } else if (from.y === this.bottom) {
            step.offset(0, -n);
        }
        return step;
    }

    /**
     * Width and height are increased by 1 because rooms are inclusive
     * Source: Room.java:134-143
     */
    width() {
        return super.width() + 1;
    }

    height() {
        return super.height() + 1;
    }

    /**
     * Random point inside room
     * Source: Room.java:145-152
     */
    random(m = 1) {
        return new Point(
            Random.IntRange(this.left + m, this.right - m + 1),
            Random.IntRange(this.top + m, this.bottom - m + 1)
        );
    }

    /**
     * Point is inside if within 1-tile perimeter
     * Source: Room.java:154-157
     */
    inside(p) {
        return p.x > this.left && p.y > this.top && p.x < this.right && p.y < this.bottom;
    }

    /**
     * Center with randomization for odd dimensions
     * Source: Room.java:159-163
     *
     * 🚨 CRITICAL: Uses Random.Int for odd-dimension randomization
     */
    center() {
        return new Point(
            Math.floor((this.left + this.right) / 2) + (((this.right - this.left) % 2) === 1 ? Random.Int_max(2) : 0),
            Math.floor((this.top + this.bottom) / 2) + (((this.bottom - this.top) % 2) === 1 ? Random.Int_max(2) : 0)
        );
    }

    // ========== Connection Logic ==========
    // Source: Room.java:166-290

    /**
     * Minimum connections for a direction
     * Source: Room.java:174-177
     */
    minConnections(direction) {
        if (direction === RoomDirection.ALL) return 1;
        else return 0;
    }

    /**
     * Current connections for a direction
     * Source: Room.java:179-194
     */
    curConnections(direction) {
        if (direction === RoomDirection.ALL) {
            return this.connected.size;
        } else {
            let total = 0;
            for (const r of this.connected.keys()) {
                const i = this.intersect(r);
                if (direction === RoomDirection.LEFT && i.width() === 0 && i.left === this.left) total++;
                else if (direction === RoomDirection.TOP && i.height() === 0 && i.top === this.top) total++;
                else if (direction === RoomDirection.RIGHT && i.width() === 0 && i.right === this.right) total++;
                else if (direction === RoomDirection.BOTTOM && i.height() === 0 && i.bottom === this.bottom) total++;
            }
            return total;
        }
    }

    /**
     * Remaining connections available
     * Source: Room.java:196-199
     */
    remConnections(direction) {
        if (this.curConnections(RoomDirection.ALL) >= this.maxConnections(RoomDirection.ALL)) return 0;
        else return this.maxConnections(direction) - this.curConnections(direction);
    }

    /**
     * Maximum connections for a direction
     * Source: Room.java:201-204
     */
    maxConnections(direction) {
        if (direction === RoomDirection.ALL) return 16;
        else return 4;
    }

    /**
     * Can connect at specific point?
     * Point must be along exactly one edge, no corners
     * Source: Room.java:207-210
     */
    canConnect_point(p) {
        return (p.x === this.left || p.x === this.right) !== (p.y === this.top || p.y === this.bottom);
    }

    /**
     * Can connect in direction?
     * Source: Room.java:213-215
     */
    canConnect_direction(direction) {
        return this.remConnections(direction) > 0;
    }

    /**
     * Can connect to another room?
     * Source: Room.java:218-245
     */
    canConnect_room(r) {
        // Entrance and exit rooms cannot directly connect
        // Source: Room.java:219-222
        if (this.isExit() && r.isEntrance() || this.isEntrance() && r.isExit()) {
            return false;
        }

        const i = this.intersect(r);

        // Must have at least one valid connection point
        let foundPoint = false;
        for (const p of i.getPoints()) {
            if (this.canConnect_point(p) && r.canConnect_point(p)) {
                foundPoint = true;
                break;
            }
        }
        if (!foundPoint) return false;

        // Check directional connection limits
        if (i.width() === 0 && i.left === this.left)
            return this.canConnect_direction(RoomDirection.LEFT) && r.canConnect_direction(RoomDirection.RIGHT);
        else if (i.height() === 0 && i.top === this.top)
            return this.canConnect_direction(RoomDirection.TOP) && r.canConnect_direction(RoomDirection.BOTTOM);
        else if (i.width() === 0 && i.right === this.right)
            return this.canConnect_direction(RoomDirection.RIGHT) && r.canConnect_direction(RoomDirection.LEFT);
        else if (i.height() === 0 && i.bottom === this.bottom)
            return this.canConnect_direction(RoomDirection.BOTTOM) && r.canConnect_direction(RoomDirection.TOP);
        else
            return false;
    }

    /**
     * Can merge with another room?
     * Source: Room.java:247-249
     */
    canMerge(level, other, p, mergeTerrain) {
        return false;
    }

    /**
     * Merge with another room
     * Source: Room.java:252-254
     */
    merge(level, other, merge, mergeTerrain) {
        // Will be implemented when Painter is ported
        // Painter.fill(level, merge, mergeTerrain);
    }

    /**
     * Add as neighbor
     * Rooms must share an edge of length >= 2
     * Source: Room.java:256-268
     */
    addNeigbour(other) {
        if (this.neigbours.includes(other)) {
            return true;
        }

        const i = this.intersect(other);
        if ((i.width() === 0 && i.height() >= 2) ||
            (i.height() === 0 && i.width() >= 2)) {
            this.neigbours.push(other);
            other.neigbours.push(this);
            return true;
        }
        return false;
    }

    /**
     * Connect to another room
     * Source: Room.java:270-279
     */
    connect(room) {
        if ((this.neigbours.includes(room) || this.addNeigbour(room))
                && !this.connected.has(room) && this.canConnect_room(room)) {
            this.connected.set(room, null);
            room.connected.set(this, null);
            return true;
        }
        return false;
    }

    /**
     * Clear all connections
     * Source: Room.java:281-290
     */
    clearConnections() {
        for (const r of this.neigbours) {
            r.neigbours = r.neigbours.filter(n => n !== this);
        }
        this.neigbours = [];

        for (const r of this.connected.keys()) {
            r.connected.delete(this);
        }
        this.connected.clear();
    }

    /**
     * Is this room an entrance?
     * Source: Room.java:292-294
     */
    isEntrance() {
        return false;
    }

    /**
     * Is this room an exit?
     * Source: Room.java:296-298
     */
    isExit() {
        return false;
    }

    // ========== Painter Logic ==========
    // Source: Room.java:300-382

    /**
     * Abstract paint method - must be overridden
     * Source: Room.java:302
     */
    paint(level) {
        throw new Error('Room.paint() must be overridden');
    }

    /**
     * Can place water at point?
     * Source: Room.java:305-307
     */
    canPlaceWater(p) {
        return true;
    }

    /**
     * Get all water-placeable points
     * Source: Room.java:309-318
     */
    waterPlaceablePoints() {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                const p = new Point(i, j);
                if (this.canPlaceWater(p)) points.push(p);
            }
        }
        return points;
    }

    /**
     * Can place grass at point?
     * Source: Room.java:321-323
     */
    canPlaceGrass(p) {
        return true;
    }

    /**
     * Get all grass-placeable points
     * Source: Room.java:325-334
     */
    grassPlaceablePoints() {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                const p = new Point(i, j);
                if (this.canPlaceGrass(p)) points.push(p);
            }
        }
        return points;
    }

    /**
     * Can place trap at point?
     * Source: Room.java:337-339
     */
    canPlaceTrap(p) {
        return true;
    }

    /**
     * Get all trap-placeable points
     * Source: Room.java:341-350
     */
    trapPlaceablePoints() {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                const p = new Point(i, j);
                if (this.canPlaceTrap(p)) points.push(p);
            }
        }
        return points;
    }

    /**
     * Can place item at point?
     * Source: Room.java:353-355
     */
    canPlaceItem(p, level) {
        return this.inside(p);
    }

    /**
     * Get all item-placeable points
     * Source: Room.java:357-366
     */
    itemPlaceablePoints(level) {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                const p = new Point(i, j);
                if (this.canPlaceItem(p, level)) points.push(p);
            }
        }
        return points;
    }

    /**
     * Can place character at point?
     * Source: Room.java:369-371
     */
    canPlaceCharacter(p, level) {
        return this.inside(p);
    }

    /**
     * Get all character-placeable points
     * Source: Room.java:373-382
     */
    charPlaceablePoints(level) {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                const p = new Point(i, j);
                if (this.canPlaceCharacter(p, level)) points.push(p);
            }
        }
        return points;
    }

    // ========== Graph.Node Interface ==========
    // Source: Room.java:385-419

    /**
     * Get connected rooms (edges in graph)
     * Only includes unlocked/open doors
     * Source: Room.java:408-419
     */
    edges() {
        const edges = [];
        for (const [r, d] of this.connected.entries()) {
            // For path building, ignore locked, blocked, or hidden doors
            if (!d || d.type === DoorType.EMPTY || d.type === DoorType.TUNNEL
                    || d.type === DoorType.UNLOCKED || d.type === DoorType.REGULAR) {
                edges.push(r);
            }
        }
        return edges;
    }
}
