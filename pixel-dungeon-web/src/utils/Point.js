/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * 2D Integer Point
 * Source: SPD-classes/src/main/java/com/watabou/utils/Point.java
 */

export class Point {
    constructor(x = 0, y = 0) {
        if (typeof x === 'object' && x instanceof Point) {
            // Copy constructor: Point.java:37-40
            this.x = x.x;
            this.y = x.y;
        } else {
            // Normal constructor: Point.java:32-35
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Set coordinates
     * Source: Point.java:42-52
     */
    set(x, y) {
        if (typeof x === 'object' && x instanceof Point) {
            this.x = x.x;
            this.y = x.y;
        } else {
            this.x = x;
            this.y = y;
        }
        return this;
    }

    /**
     * Create a copy of this point
     * Source: Point.java:54-56
     */
    clone() {
        return new Point(this.x, this.y);
    }

    /**
     * Scale by a factor
     * Source: Point.java:58-62
     */
    scale(f) {
        this.x = Math.floor(this.x * f);
        this.y = Math.floor(this.y * f);
        return this;
    }

    /**
     * Offset by delta
     * Source: Point.java:64-74
     */
    offset(dx, dy) {
        if (typeof dx === 'object' && dx instanceof Point) {
            this.x += dx.x;
            this.y += dx.y;
        } else {
            this.x += dx;
            this.y += dy;
        }
        return this;
    }

    /**
     * Check if point is at origin
     * Source: Point.java:76-78
     */
    isZero() {
        return this.x === 0 && this.y === 0;
    }

    /**
     * Calculate length from origin
     * Source: Point.java:80-82
     */
    length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Calculate distance between two points
     * Source: Point.java:84-88
     */
    static distance(a, b) {
        const dx = a.x - b.x;
        const dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Equality check
     * Source: Point.java:90-98
     */
    equals(obj) {
        if (obj instanceof Point) {
            return obj.x === this.x && obj.y === this.y;
        }
        return false;
    }

    /**
     * String representation
     */
    toString() {
        return `Point(${this.x}, ${this.y})`;
    }
}
