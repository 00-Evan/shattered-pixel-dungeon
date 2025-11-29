/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * 2D Rectangle
 * Source: SPD-classes/src/main/java/com/watabou/utils/Rect.java
 */

import { Point } from './Point.js';
import { Random } from './Random.js';

export class Rect {
    constructor(left = 0, top = 0, right = 0, bottom = 0) {
        if (typeof left === 'object' && left instanceof Rect) {
            // Copy constructor: Rect.java:37-39
            this.left = left.left;
            this.top = left.top;
            this.right = left.right;
            this.bottom = left.bottom;
        } else {
            // Normal constructor: Rect.java:41-46
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    /**
     * Calculate width
     * Source: Rect.java:48-50
     */
    width() {
        return this.right - this.left;
    }

    /**
     * Calculate height
     * Source: Rect.java:52-54
     */
    height() {
        return this.bottom - this.top;
    }

    /**
     * Calculate area
     * Source: Rect.java:56-58
     */
    square() {
        return this.width() * this.height();
    }

    /**
     * Set rectangle bounds
     * Source: Rect.java:60-70
     */
    set(left, top, right, bottom) {
        if (typeof left === 'object' && left instanceof Rect) {
            this.left = left.left;
            this.top = left.top;
            this.right = left.right;
            this.bottom = left.bottom;
        } else {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
        return this;
    }

    /**
     * Set position (keeping size constant)
     * Source: Rect.java:72-74
     */
    setPos(x, y) {
        return this.set(x, y, x + (this.right - this.left), y + (this.bottom - this.top));
    }

    /**
     * Shift rectangle by offset
     * Source: Rect.java:76-78
     */
    shift(x, y) {
        return this.set(this.left + x, this.top + y, this.right + x, this.bottom + y);
    }

    /**
     * Resize rectangle
     * Source: Rect.java:80-82
     */
    resize(w, h) {
        return this.set(this.left, this.top, this.left + w, this.top + h);
    }

    /**
     * Check if rectangle is empty
     * Source: Rect.java:84-86
     */
    isEmpty() {
        return this.right <= this.left || this.bottom <= this.top;
    }

    /**
     * Set rectangle to empty
     * Source: Rect.java:88-91
     */
    setEmpty() {
        this.left = this.right = this.top = this.bottom = 0;
        return this;
    }

    /**
     * Calculate intersection with another rectangle
     * Source: Rect.java:93-100
     */
    intersect(other) {
        const result = new Rect();
        result.left = Math.max(this.left, other.left);
        result.right = Math.min(this.right, other.right);
        result.top = Math.max(this.top, other.top);
        result.bottom = Math.min(this.bottom, other.bottom);
        return result;
    }

    /**
     * Calculate union with another rectangle
     * Source: Rect.java:102-109
     */
    union(other) {
        if (typeof other === 'object' && other instanceof Rect) {
            const result = new Rect();
            result.left = Math.min(this.left, other.left);
            result.right = Math.max(this.right, other.right);
            result.top = Math.min(this.top, other.top);
            result.bottom = Math.max(this.bottom, other.bottom);
            return result;
        } else if (typeof other === 'object' && other instanceof Point) {
            // Union with Point: Rect.java:129-131
            return this.union_xy(other.x, other.y);
        } else {
            // Union with x, y coordinates: Rect.java:111-127
            const x = other;
            const y = arguments[1];
            return this.union_xy(x, y);
        }
    }

    /**
     * Union with x, y coordinates
     * Source: Rect.java:111-127
     */
    union_xy(x, y) {
        if (this.isEmpty()) {
            return this.set(x, y, x + 1, y + 1);
        } else {
            if (x < this.left) {
                this.left = x;
            } else if (x >= this.right) {
                this.right = x + 1;
            }
            if (y < this.top) {
                this.top = y;
            } else if (y >= this.bottom) {
                this.bottom = y + 1;
            }
            return this;
        }
    }

    /**
     * Check if point is inside rectangle
     * Source: Rect.java:133-135
     */
    inside(p) {
        return p.x >= this.left && p.x < this.right && p.y >= this.top && p.y < this.bottom;
    }

    /**
     * Get center point (with random offset for even dimensions)
     * Source: Rect.java:137-141
     *
     * ⚠️ CRITICAL: This uses Random.Int for even-dimension randomization
     * Line 139-140 in Rect.java adds random offset when dimensions are even
     */
    center() {
        return new Point(
            Math.floor((this.left + this.right) / 2) + (((this.right - this.left) % 2) === 0 ? Random.Int_max(2) : 0),
            Math.floor((this.top + this.bottom) / 2) + (((this.bottom - this.top) % 2) === 0 ? Random.Int_max(2) : 0)
        );
    }

    /**
     * Shrink rectangle by distance d
     * Source: Rect.java:143-149
     */
    shrink(d = 1) {
        return new Rect(this.left + d, this.top + d, this.right - d, this.bottom - d);
    }

    /**
     * Scale rectangle by factor d
     * Source: Rect.java:151-153
     */
    scale(d) {
        return new Rect(this.left * d, this.top * d, this.right * d, this.bottom * d);
    }

    /**
     * Get all points within rectangle
     * Source: Rect.java:155-161
     */
    getPoints() {
        const points = [];
        for (let i = this.left; i <= this.right; i++) {
            for (let j = this.top; j <= this.bottom; j++) {
                points.push(new Point(i, j));
            }
        }
        return points;
    }

    /**
     * String representation
     */
    toString() {
        return `Rect(${this.left}, ${this.top}, ${this.right}, ${this.bottom})`;
    }
}
