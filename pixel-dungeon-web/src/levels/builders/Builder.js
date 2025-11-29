/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Abstract Level Builder
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/builders/Builder.java
 *
 * 🚨 CRITICAL: Contains room overlap detection logic
 * User Warning: "Room overlap and map boundaries is the most common point of failure in dungeon ports"
 */

import { Point } from '../../utils/Point.js';
import { PointF } from '../../utils/PointF.js';
import { Rect } from '../../utils/Rect.js';
import { Random } from '../../utils/Random.js';
import { RoomDirection } from '../rooms/Room.js';

/**
 * Abstract Builder class
 * Subclasses implement specific layout algorithms
 * Source: Builder.java:34-258
 */
export class Builder {
    /**
     * Build a connected map from rooms
     * Returns null on failure
     * Source: Builder.java:41
     */
    build(rooms) {
        throw new Error('Builder.build() must be overridden');
    }

    /**
     * Find neighbors for all rooms
     * Rooms are neighbors if they share an edge of length >= 2
     * Source: Builder.java:43-50
     */
    static findNeighbours(rooms) {
        const ra = rooms.slice(); // Copy array
        for (let i = 0; i < ra.length - 1; i++) {
            for (let j = i + 1; j < ra.length; j++) {
                ra[i].addNeigbour(ra[j]);
            }
        }
    }

    /**
     * 🚨 CRITICAL: Find maximum free space from a start point
     * This is the core collision detection algorithm
     * Source: Builder.java:53-141
     *
     * Returns a rectangle representing the maximum free space that doesn't
     * overlap with any rooms in the collision list.
     *
     * Algorithm:
     * 1. Start with a maxSize square centered on start point
     * 2. Find all overlapping rooms
     * 3. For each overlap, shrink the space rectangle to avoid collision
     * 4. Repeat until no collisions remain
     *
     * @param start - Center point for the free space
     * @param collision - List of rooms to avoid
     * @param maxSize - Maximum radius of the free space
     * @returns Rect representing the maximum free space
     */
    static findFreeSpace(start, collision, maxSize) {
        // Initialize space as maxSize square centered on start
        // Source: Builder.java:54
        const space = new Rect(
            start.x - maxSize,
            start.y - maxSize,
            start.x + maxSize,
            start.y + maxSize
        );

        // Shallow copy of collision list
        // Source: Builder.java:57
        const colliding = collision.slice();

        do {
            // Remove empty rooms and non-overlapping rooms
            // Source: Builder.java:61-70
            for (let i = colliding.length - 1; i >= 0; i--) {
                const room = colliding[i];

                // Check if not colliding
                // Source: Builder.java:65-67
                // Rooms don't overlap if:
                // - left edge of one is >= right edge of other, OR
                // - top edge of one is >= bottom edge of other
                if (room.isEmpty()
                        || Math.max(space.left, room.left) >= Math.min(space.right, room.right)
                        || Math.max(space.top, room.top) >= Math.min(space.bottom, room.bottom)) {
                    colliding.splice(i, 1);
                }
            }

            // Find the closest room we're still overlapping with
            // Source: Builder.java:73-105
            let closestRoom = null;
            let closestDiff = Number.MAX_SAFE_INTEGER;
            let inside = true;
            let curDiff = 0;

            for (const curRoom of colliding) {
                curDiff = 0;
                inside = true;

                // Calculate manhattan distance from start to room bounds
                // Source: Builder.java:79-93
                if (start.x <= curRoom.left) {
                    inside = false;
                    curDiff += curRoom.left - start.x;
                } else if (start.x >= curRoom.right) {
                    inside = false;
                    curDiff += start.x - curRoom.right;
                }

                if (start.y <= curRoom.top) {
                    inside = false;
                    curDiff += curRoom.top - start.y;
                } else if (start.y >= curRoom.bottom) {
                    inside = false;
                    curDiff += start.y - curRoom.bottom;
                }

                // If start point is inside a room, return empty space
                // Source: Builder.java:95-98
                if (inside) {
                    space.set(start.x, start.y, start.x, start.y);
                    return space;
                }

                // Track closest room
                // Source: Builder.java:100-103
                if (curDiff < closestDiff) {
                    closestDiff = curDiff;
                    closestRoom = curRoom;
                }
            }

            // Shrink space to avoid closest room
            // Source: Builder.java:107-135
            if (closestRoom !== null) {
                let wDiff, hDiff;

                // Calculate cost of shrinking width vs height
                // Cost = area that would be removed
                // Source: Builder.java:110-122
                wDiff = Number.MAX_SAFE_INTEGER;
                if (closestRoom.left >= start.x) {
                    wDiff = (space.right - closestRoom.left) * (space.height() + 1);
                } else if (closestRoom.right <= start.x) {
                    wDiff = (closestRoom.right - space.left) * (space.height() + 1);
                }

                hDiff = Number.MAX_SAFE_INTEGER;
                if (closestRoom.top >= start.y) {
                    hDiff = (space.bottom - closestRoom.top) * (space.width() + 1);
                } else if (closestRoom.bottom <= start.y) {
                    hDiff = (closestRoom.bottom - space.top) * (space.width() + 1);
                }

                // Reduce by as little as possible to resolve the collision
                // Source: Builder.java:125-131
                if (wDiff < hDiff || (wDiff === hDiff && Random.Int_max(2) === 0)) {
                    // Shrink horizontally
                    if (closestRoom.left >= start.x && closestRoom.left < space.right) {
                        space.right = closestRoom.left;
                    }
                    if (closestRoom.right <= start.x && closestRoom.right > space.left) {
                        space.left = closestRoom.right;
                    }
                } else {
                    // Shrink vertically
                    if (closestRoom.top >= start.y && closestRoom.top < space.bottom) {
                        space.bottom = closestRoom.top;
                    }
                    if (closestRoom.bottom <= start.y && closestRoom.bottom > space.top) {
                        space.top = closestRoom.bottom;
                    }
                }

                colliding.splice(colliding.indexOf(closestRoom), 1);
            } else {
                colliding.length = 0; // Clear array
            }

            // Loop until no longer colliding with any rooms
            // Source: Builder.java:138
        } while (colliding.length > 0);

        return space;
    }

    /**
     * Constant for angle conversions
     * Source: Builder.java:143
     */
    static A = 180 / Math.PI;

    /**
     * Calculate angle between room centers
     * 0 degrees is straight up
     * Source: Builder.java:146-150
     */
    static angleBetweenRooms(from, to) {
        const fromCenter = new PointF(
            (from.left + from.right) / 2,
            (from.top + from.bottom) / 2
        );
        const toCenter = new PointF(
            (to.left + to.right) / 2,
            (to.top + to.bottom) / 2
        );
        return Builder.angleBetweenPoints(fromCenter, toCenter);
    }

    /**
     * Calculate angle between two points
     * Source: Builder.java:152-158
     */
    static angleBetweenPoints(from, to) {
        const m = (to.y - from.y) / (to.x - from.x);
        let angle = Builder.A * (Math.atan(m) + Math.PI / 2.0);
        if (from.x > to.x) angle -= 180;
        return angle;
    }

    /**
     * 🚨 CRITICAL: Place a room at a specific angle from another room
     * This uses findFreeSpace to ensure no overlaps
     * Source: Builder.java:164-257
     *
     * @param collision - List of placed rooms to avoid
     * @param prev - Previous room to place from
     * @param next - Room to place
     * @param angle - Target angle (0-360, 0 is up)
     * @returns Actual angle between rooms, or -1 on failure
     */
    static placeRoom(collision, prev, next, angle) {
        // Wrap angle to [0, 360)
        // Source: Builder.java:166-170
        angle = angle % 360;
        if (angle < 0) {
            angle += 360;
        }

        const prevCenter = new PointF(
            (prev.left + prev.right) / 2,
            (prev.top + prev.bottom) / 2
        );

        // Calculate line using y = mx + b
        // Source: Builder.java:175-176
        const m = Math.tan(angle / Builder.A + Math.PI / 2.0);
        const b = prevCenter.y - m * prevCenter.x;

        // Find the point along prev room where the line exits
        // Source: Builder.java:179-197
        let start;
        let direction;

        if (Math.abs(m) >= 1) {
            // Line is more vertical than horizontal
            if (angle < 90 || angle > 270) {
                direction = RoomDirection.TOP;
                start = new Point(Math.round((prev.top - b) / m), prev.top);
            } else {
                direction = RoomDirection.BOTTOM;
                start = new Point(Math.round((prev.bottom - b) / m), prev.bottom);
            }
        } else {
            // Line is more horizontal than vertical
            if (angle < 180) {
                direction = RoomDirection.RIGHT;
                start = new Point(prev.right, Math.round(m * prev.right + b));
            } else {
                direction = RoomDirection.LEFT;
                start = new Point(prev.left, Math.round(m * prev.left + b));
            }
        }

        // Cap to valid connection point
        // Source: Builder.java:200-204
        if (direction === RoomDirection.TOP || direction === RoomDirection.BOTTOM) {
            start.x = Math.max(prev.left + 1, Math.min(start.x, prev.right - 1));
        } else {
            start.y = Math.max(prev.top + 1, Math.min(start.y, prev.bottom - 1));
        }

        // 🚨 CRITICAL: Space checking using findFreeSpace
        // Source: Builder.java:207-210
        const space = Builder.findFreeSpace(
            start,
            collision,
            Math.max(next.maxWidth(), next.maxHeight())
        );

        if (!next.setSizeWithLimit(space.width() + 1, space.height() + 1)) {
            return -1;
        }

        // Find ideal center using line equation and known dimensions
        // Source: Builder.java:213-234
        const targetCenter = new PointF();

        if (direction === RoomDirection.TOP) {
            targetCenter.y = prev.top - (next.height() - 1) / 2;
            targetCenter.x = (targetCenter.y - b) / m;
            next.setPos(
                Math.round(targetCenter.x - (next.width() - 1) / 2),
                prev.top - (next.height() - 1)
            );
        } else if (direction === RoomDirection.BOTTOM) {
            targetCenter.y = prev.bottom + (next.height() - 1) / 2;
            targetCenter.x = (targetCenter.y - b) / m;
            next.setPos(
                Math.round(targetCenter.x - (next.width() - 1) / 2),
                prev.bottom
            );
        } else if (direction === RoomDirection.RIGHT) {
            targetCenter.x = prev.right + (next.width() - 1) / 2;
            targetCenter.y = m * targetCenter.x + b;
            next.setPos(
                prev.right,
                Math.round(targetCenter.y - (next.height() - 1) / 2)
            );
        } else if (direction === RoomDirection.LEFT) {
            targetCenter.x = prev.left - (next.width() - 1) / 2;
            targetCenter.y = m * targetCenter.x + b;
            next.setPos(
                prev.left - (next.width() - 1),
                Math.round(targetCenter.y - (next.height() - 1) / 2)
            );
        }

        // Perform connection bounds and target checking
        // Move room if necessary to stay within space bounds
        // Source: Builder.java:237-249
        if (direction === RoomDirection.TOP || direction === RoomDirection.BOTTOM) {
            if (next.right < prev.left + 2) next.shift(prev.left + 2 - next.right, 0);
            else if (next.left > prev.right - 2) next.shift(prev.right - 2 - next.left, 0);

            if (next.right > space.right) next.shift(space.right - next.right, 0);
            else if (next.left < space.left) next.shift(space.left - next.left, 0);
        } else {
            if (next.bottom < prev.top + 2) next.shift(0, prev.top + 2 - next.bottom);
            else if (next.top > prev.bottom - 2) next.shift(0, prev.bottom - 2 - next.top);

            if (next.bottom > space.bottom) next.shift(0, space.bottom - next.bottom);
            else if (next.top < space.top) next.shift(0, space.top - next.top);
        }

        // Attempt to connect, return angle if successful
        // Source: Builder.java:252-256
        if (next.connect(prev)) {
            return Builder.angleBetweenRooms(prev, next);
        } else {
            return -1;
        }
    }

    /**
     * Normalize room coordinates to positive space
     * Shifts all rooms so minimum coordinates are at (padding, padding)
     *
     * @param rooms - List of rooms to normalize
     * @param padding - Minimum offset from (0,0), defaults to 1
     */
    static normalizeCoordinates(rooms, padding = 1) {
        if (!rooms || rooms.length === 0) {
            return;
        }

        // Find minimum X and Y coordinates
        let minX = Number.MAX_SAFE_INTEGER;
        let minY = Number.MAX_SAFE_INTEGER;

        for (const room of rooms) {
            if (room.left < minX) minX = room.left;
            if (room.top < minY) minY = room.top;
        }

        // Calculate shift amounts
        const shiftX = minX < padding ? padding - minX : 0;
        const shiftY = minY < padding ? padding - minY : 0;

        // Apply shifts if needed
        if (shiftX !== 0 || shiftY !== 0) {
            for (const room of rooms) {
                room.shift(shiftX, shiftY);
            }
        }
    }
}
