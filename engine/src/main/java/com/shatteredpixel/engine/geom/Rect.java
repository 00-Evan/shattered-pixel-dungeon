package com.shatteredpixel.engine.geom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Immutable axis-aligned rectangle on an integer grid.
 * Represented by (left, top, right, bottom) where right and bottom are exclusive.
 *
 * Migrated from: com.watabou.utils.Rect
 * Changes:
 * - Made immutable (final fields, returns new instances)
 * - Deterministic center() (no Random usage)
 * - Added proper contains/intersects checks
 * - Added area() method
 * - GWT-safe (Java 11)
 */
public final class Rect {

    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    // =========================================================================
    // Constructors
    // =========================================================================

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Create from position and size.
     */
    public static Rect fromSize(int x, int y, int width, int height) {
        return new Rect(x, y, x + width, y + height);
    }

    /**
     * Create from two points (min and max corners).
     */
    public static Rect fromPoints(Point p1, Point p2) {
        int minX = Math.min(p1.x, p2.x);
        int minY = Math.min(p1.y, p2.y);
        int maxX = Math.max(p1.x, p2.x);
        int maxY = Math.max(p1.y, p2.y);
        return new Rect(minX, minY, maxX + 1, maxY + 1);
    }

    /**
     * Empty rectangle at origin.
     */
    public static Rect empty() {
        return new Rect(0, 0, 0, 0);
    }

    // =========================================================================
    // Properties
    // =========================================================================

    public int width() {
        return right - left;
    }

    public int height() {
        return bottom - top;
    }

    public int area() {
        return width() * height();
    }

    public boolean isEmpty() {
        return right <= left || bottom <= top;
    }

    // =========================================================================
    // Queries
    // =========================================================================

    /**
     * Check if this rectangle contains a point.
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Check if this rectangle contains coordinates.
     */
    public boolean contains(int x, int y) {
        return x >= left && x < right && y >= top && y < bottom;
    }

    /**
     * Check if this rectangle fully contains another rectangle.
     */
    public boolean contains(Rect other) {
        return left <= other.left && right >= other.right &&
               top <= other.top && bottom >= other.bottom;
    }

    /**
     * Check if this rectangle intersects another rectangle.
     */
    public boolean intersects(Rect other) {
        return left < other.right && right > other.left &&
               top < other.bottom && bottom > other.top;
    }

    // =========================================================================
    // Operations (return new instances - immutable)
    // =========================================================================

    /**
     * Get the center point of this rectangle.
     * For even dimensions, returns the lower-left center cell.
     */
    public Point center() {
        return new Point((left + right) / 2, (top + bottom) / 2);
    }

    /**
     * Shrink this rectangle by d cells on all sides.
     */
    public Rect shrink(int d) {
        return new Rect(left + d, top + d, right - d, bottom - d);
    }

    /**
     * Shrink by 1 cell on all sides.
     */
    public Rect shrink() {
        return shrink(1);
    }

    /**
     * Expand this rectangle by d cells on all sides.
     */
    public Rect expand(int d) {
        return new Rect(left - d, top - d, right + d, bottom + d);
    }

    /**
     * Expand by 1 cell on all sides.
     */
    public Rect expand() {
        return expand(1);
    }

    /**
     * Move this rectangle to a new position (preserves size).
     */
    public Rect moveTo(int x, int y) {
        int w = width();
        int h = height();
        return new Rect(x, y, x + w, y + h);
    }

    /**
     * Move this rectangle to a new position.
     */
    public Rect moveTo(Point p) {
        return moveTo(p.x, p.y);
    }

    /**
     * Shift this rectangle by an offset.
     */
    public Rect shift(int dx, int dy) {
        return new Rect(left + dx, top + dy, right + dx, bottom + dy);
    }

    /**
     * Shift this rectangle by a point offset.
     */
    public Rect shift(Point offset) {
        return shift(offset.x, offset.y);
    }

    /**
     * Resize this rectangle (keeps top-left corner fixed).
     */
    public Rect resize(int newWidth, int newHeight) {
        return new Rect(left, top, left + newWidth, top + newHeight);
    }

    /**
     * Scale all coordinates by a factor.
     */
    public Rect scale(int factor) {
        return new Rect(left * factor, top * factor, right * factor, bottom * factor);
    }

    /**
     * Intersection of this rectangle with another.
     * Returns empty rect if no intersection.
     */
    public Rect intersection(Rect other) {
        int l = Math.max(left, other.left);
        int t = Math.max(top, other.top);
        int r = Math.min(right, other.right);
        int b = Math.min(bottom, other.bottom);
        return new Rect(l, t, r, b);
    }

    /**
     * Union of this rectangle with another (smallest rect containing both).
     */
    public Rect union(Rect other) {
        if (isEmpty()) return other;
        if (other.isEmpty()) return this;

        int l = Math.min(left, other.left);
        int t = Math.min(top, other.top);
        int r = Math.max(right, other.right);
        int b = Math.max(bottom, other.bottom);
        return new Rect(l, t, r, b);
    }

    /**
     * Union with a single point (expand to include it).
     */
    public Rect union(Point p) {
        if (isEmpty()) {
            return new Rect(p.x, p.y, p.x + 1, p.y + 1);
        }

        int l = Math.min(left, p.x);
        int t = Math.min(top, p.y);
        int r = Math.max(right, p.x + 1);
        int b = Math.max(bottom, p.y + 1);
        return new Rect(l, t, r, b);
    }

    /**
     * Get all points inside this rectangle.
     */
    public List<Point> getAllPoints() {
        List<Point> points = new ArrayList<>();
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                points.add(new Point(x, y));
            }
        }
        return points;
    }

    /**
     * Get all points on the border of this rectangle.
     */
    public List<Point> getBorderPoints() {
        List<Point> points = new ArrayList<>();
        if (isEmpty()) return points;

        // Top and bottom edges
        for (int x = left; x < right; x++) {
            points.add(new Point(x, top));
            if (height() > 1) {
                points.add(new Point(x, bottom - 1));
            }
        }

        // Left and right edges (skip corners already added)
        for (int y = top + 1; y < bottom - 1; y++) {
            points.add(new Point(left, y));
            if (width() > 1) {
                points.add(new Point(right - 1, y));
            }
        }

        return points;
    }

    // =========================================================================
    // Object overrides
    // =========================================================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Rect)) return false;
        Rect other = (Rect) obj;
        return left == other.left && top == other.top &&
               right == other.right && bottom == other.bottom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, top, right, bottom);
    }

    @Override
    public String toString() {
        return "[(" + left + "," + top + ")-(" + right + "," + bottom + ") " +
               width() + "x" + height() + "]";
    }
}
