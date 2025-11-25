package com.shatteredpixel.engine.path;

import com.shatteredpixel.engine.geom.BooleanGrid;
import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Main pathfinding class.
 * Builds paths between points using distance maps (Dijkstra/BFS).
 *
 * Migrated from: com.watabou.utils.PathFinder
 * Changes:
 * - Object-oriented API (not static)
 * - Uses DistanceMap internally
 * - Returns PathResult instead of mutating global arrays
 * - Thread-safe (no shared state)
 * - GWT-safe (Java 11)
 */
public class Pathfinder {

    private final int width;
    private final int height;
    private final DistanceMap distanceMap;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a pathfinder for the given dimensions.
     * Reuses a single DistanceMap instance for efficiency.
     */
    public Pathfinder(int width, int height) {
        this.width = width;
        this.height = height;
        this.distanceMap = new DistanceMap(width, height);
    }

    // =========================================================================
    // Path Finding
    // =========================================================================

    /**
     * Find a path from start to goal using 8-way movement.
     *
     * @param start Starting position
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @return PathResult (check isSuccess())
     */
    public PathResult findPath(Point start, Point goal, Predicate<Point> passable) {
        return findPath(start, goal, passable, true);
    }

    /**
     * Find a path from start to goal.
     *
     * @param start Starting position
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @param allowDiagonal Whether to allow diagonal movement
     * @return PathResult (check isSuccess())
     */
    public PathResult findPath(Point start, Point goal, Predicate<Point> passable, boolean allowDiagonal) {
        if (start.equals(goal)) {
            List<Point> path = new ArrayList<>();
            path.add(start);
            return new PathResult(path, start, goal);
        }

        // Build distance map from goal
        distanceMap.build(goal, passable, allowDiagonal);

        // Check if start is reachable
        if (!distanceMap.isReachable(start)) {
            return PathResult.failed(start, goal);
        }

        // Reconstruct path by following downhill gradient
        List<Point> path = new ArrayList<>();
        Point current = start;
        path.add(current);

        while (!current.equals(goal)) {
            Point nextStep = distanceMap.getNextStep(current, allowDiagonal);
            if (nextStep == null) {
                // Should not happen if distance map is correct
                return PathResult.failed(start, goal);
            }
            path.add(nextStep);
            current = nextStep;
        }

        return new PathResult(path, start, goal);
    }

    /**
     * Find a path using a boolean grid for passability.
     */
    public PathResult findPath(Point start, Point goal, BooleanGrid walkable) {
        return findPath(start, goal, p -> walkable.get(p));
    }

    // =========================================================================
    // Single Step Queries
    // =========================================================================

    /**
     * Get the next step from start toward goal (8-way movement).
     * Returns null if no path exists.
     *
     * @param start Starting position
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @return Next step toward goal, or null if unreachable
     */
    public Point getNextStep(Point start, Point goal, Predicate<Point> passable) {
        return getNextStep(start, goal, passable, true);
    }

    /**
     * Get the next step from start toward goal.
     *
     * @param start Starting position
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @param allowDiagonal Whether to allow diagonal movement
     * @return Next step toward goal, or null if unreachable
     */
    public Point getNextStep(Point start, Point goal, Predicate<Point> passable, boolean allowDiagonal) {
        if (start.equals(goal)) {
            return null;
        }

        distanceMap.build(goal, passable, allowDiagonal);

        if (!distanceMap.isReachable(start)) {
            return null;
        }

        return distanceMap.getNextStep(start, allowDiagonal);
    }

    /**
     * Get the next step using a boolean grid for passability.
     */
    public Point getNextStep(Point start, Point goal, BooleanGrid walkable) {
        return getNextStep(start, goal, p -> walkable.get(p));
    }

    // =========================================================================
    // Distance Queries
    // =========================================================================

    /**
     * Get the distance from start to goal.
     * Returns Integer.MAX_VALUE if no path exists.
     *
     * @param start Starting position
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @return Path length, or Integer.MAX_VALUE if unreachable
     */
    public int getDistance(Point start, Point goal, Predicate<Point> passable) {
        distanceMap.build(goal, passable);
        return distanceMap.getDistance(start);
    }

    /**
     * Get the distance using a boolean grid for passability.
     */
    public int getDistance(Point start, Point goal, BooleanGrid walkable) {
        return getDistance(start, goal, p -> walkable.get(p));
    }

    // =========================================================================
    // Utilities
    // =========================================================================

    /**
     * Get the underlying distance map (for advanced usage).
     */
    public DistanceMap getDistanceMap() {
        return distanceMap;
    }

    @Override
    public String toString() {
        return "Pathfinder[" + width + "x" + height + "]";
    }
}
