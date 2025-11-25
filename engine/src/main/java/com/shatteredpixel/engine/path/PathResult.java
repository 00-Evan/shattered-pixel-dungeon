package com.shatteredpixel.engine.path;

import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable result of a pathfinding operation.
 * Represents a sequence of points from start to goal.
 *
 * Design goals:
 * - Immutable (thread-safe)
 * - Provides path analysis (length, contains checks)
 * - Supports both forward and reverse iteration
 */
public final class PathResult {

    private final List<Point> points;
    private final Point start;
    private final Point goal;
    private final boolean success;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a successful path result.
     */
    public PathResult(List<Point> points, Point start, Point goal) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Path must contain at least one point");
        }
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
        this.start = start;
        this.goal = goal;
        this.success = true;
    }

    /**
     * Create a failed path result (no path found).
     */
    public static PathResult failed(Point start, Point goal) {
        return new PathResult(start, goal);
    }

    private PathResult(Point start, Point goal) {
        this.points = Collections.emptyList();
        this.start = start;
        this.goal = goal;
        this.success = false;
    }

    // =========================================================================
    // Properties
    // =========================================================================

    /**
     * Check if a path was successfully found.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Get the starting point.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Get the goal point.
     */
    public Point getGoal() {
        return goal;
    }

    /**
     * Get all points in the path (immutable).
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Get the number of steps in the path.
     * Returns 0 if path failed.
     */
    public int getLength() {
        return points.size();
    }

    /**
     * Check if the path contains a specific point.
     */
    public boolean contains(Point p) {
        return points.contains(p);
    }

    // =========================================================================
    // Access
    // =========================================================================

    /**
     * Get the point at the specified index.
     * Index 0 is the start, last index is the goal.
     */
    public Point get(int index) {
        if (!success) {
            throw new IllegalStateException("Cannot get point from failed path");
        }
        return points.get(index);
    }

    /**
     * Get the first step from start toward goal.
     * Returns null if path failed or is empty.
     */
    public Point getFirstStep() {
        return success && points.size() > 1 ? points.get(1) : null;
    }

    /**
     * Get the last step before reaching goal.
     * Returns null if path failed or has only one point.
     */
    public Point getLastStep() {
        return success && points.size() > 1 ? points.get(points.size() - 2) : null;
    }

    @Override
    public String toString() {
        if (!success) {
            return "PathResult[FAILED: " + start + " -> " + goal + "]";
        }
        return "PathResult[" + start + " -> " + goal + ", length=" + getLength() + "]";
    }
}
