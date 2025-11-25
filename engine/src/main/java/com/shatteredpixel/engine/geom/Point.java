package com.shatteredpixel.engine.geom;

import java.util.Objects;

/**
 * Immutable 2D integer coordinate.
 * Used for grid positions in dungeons, levels, and spatial calculations.
 *
 * Migrated from: com.watabou.utils.Point
 * Changes:
 * - Made immutable (final fields)
 * - Added proper hashCode() implementation
 * - Added static factory methods and operators
 * - Added cardinal/diagonal direction constants
 * - GWT-safe (Java 11)
 */
public final class Point {

    public final int x;
    public final int y;

    // =========================================================================
    // Cardinal Directions
    // =========================================================================

    public static final Point ZERO = new Point(0, 0);
    public static final Point UP = new Point(0, -1);
    public static final Point DOWN = new Point(0, 1);
    public static final Point LEFT = new Point(-1, 0);
    public static final Point RIGHT = new Point(1, 0);

    // Diagonal directions
    public static final Point UP_LEFT = new Point(-1, -1);
    public static final Point UP_RIGHT = new Point(1, -1);
    public static final Point DOWN_LEFT = new Point(-1, 1);
    public static final Point DOWN_RIGHT = new Point(1, 1);

    // All 8 neighbors (4 cardinal + 4 diagonal)
    public static final Point[] NEIGHBORS_8 = {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    };

    // Cardinal neighbors only (4 directions)
    public static final Point[] NEIGHBORS_4 = {
        UP, DOWN, LEFT, RIGHT
    };

    // =========================================================================
    // Constructors
    // =========================================================================

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
    }

    // =========================================================================
    // Operators
    // =========================================================================

    /**
     * Add another point (vector addition).
     */
    public Point add(Point other) {
        return new Point(x + other.x, y + other.y);
    }

    /**
     * Add x and y offsets.
     */
    public Point add(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }

    /**
     * Subtract another point (vector subtraction).
     */
    public Point subtract(Point other) {
        return new Point(x - other.x, y - other.y);
    }

    /**
     * Subtract x and y offsets.
     */
    public Point subtract(int dx, int dy) {
        return new Point(x - dx, y - dy);
    }

    /**
     * Scale by an integer factor.
     */
    public Point scale(int factor) {
        return new Point(x * factor, y * factor);
    }

    /**
     * Negate (flip signs).
     */
    public Point negate() {
        return new Point(-x, -y);
    }

    // =========================================================================
    // Distance Calculations
    // =========================================================================

    /**
     * Euclidean distance squared (avoids sqrt for performance).
     * Use this for distance comparisons.
     */
    public int distanceSquared(Point other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return dx * dx + dy * dy;
    }

    /**
     * Euclidean distance.
     */
    public float distance(Point other) {
        return (float) Math.sqrt(distanceSquared(other));
    }

    /**
     * Manhattan distance (grid distance, no diagonals).
     */
    public int manhattanDistance(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    /**
     * Chebyshev distance (max of dx, dy - allows diagonals).
     */
    public int chebyshevDistance(Point other) {
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }

    /**
     * Length of this point as a vector (distance from origin).
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Length squared (avoids sqrt).
     */
    public int lengthSquared() {
        return x * x + y * y;
    }

    // =========================================================================
    // Queries
    // =========================================================================

    /**
     * Check if this point is the origin (0, 0).
     */
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /**
     * Check if this point is adjacent to another (including diagonals).
     */
    public boolean isAdjacentTo(Point other) {
        return chebyshevDistance(other) == 1;
    }

    /**
     * Check if this point is cardinally adjacent (no diagonals).
     */
    public boolean isCardinallyAdjacentTo(Point other) {
        return manhattanDistance(other) == 1;
    }

    // =========================================================================
    // Object overrides
    // =========================================================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;
        Point other = (Point) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
