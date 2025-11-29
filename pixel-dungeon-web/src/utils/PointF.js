/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * 2D Floating Point
 * Source: SPD-classes/src/main/java/com/watabou/utils/PointF.java
 */

export class PointF {
    constructor(x = 0, y = 0) {
        if (typeof x === 'object' && x instanceof PointF) {
            // Copy constructor
            this.x = x.x;
            this.y = x.y;
        } else {
            this.x = x;
            this.y = y;
        }
    }

    set(x, y) {
        if (typeof x === 'object' && x instanceof PointF) {
            this.x = x.x;
            this.y = x.y;
        } else {
            this.x = x;
            this.y = y;
        }
        return this;
    }

    clone() {
        return new PointF(this.x, this.y);
    }

    scale(f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    offset(dx, dy) {
        if (typeof dx === 'object' && dx instanceof PointF) {
            this.x += dx.x;
            this.y += dx.y;
        } else {
            this.x += dx;
            this.y += dy;
        }
        return this;
    }

    length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    static distance(a, b) {
        const dx = a.x - b.x;
        const dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    equals(obj) {
        if (obj instanceof PointF) {
            return obj.x === this.x && obj.y === this.y;
        }
        return false;
    }

    toString() {
        return `PointF(${this.x}, ${this.y})`;
    }
}
