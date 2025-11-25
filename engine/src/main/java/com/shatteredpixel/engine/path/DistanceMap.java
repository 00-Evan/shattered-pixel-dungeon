package com.shatteredpixel.engine.path;

import com.shatteredpixel.engine.geom.BooleanGrid;
import com.shatteredpixel.engine.geom.Grid;
import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Dijkstra/BFS distance map builder.
 * Computes shortest path distances from goal(s) to all reachable cells.
 *
 * Migrated from: com.watabou.utils.PathFinder.buildDistanceMap
 * Changes:
 * - Object-oriented (not static/global)
 * - Uses Point and Grid instead of 1D arrays
 * - Thread-safe (no global state)
 * - Supports custom passability predicates
 * - GWT-safe (Java 11)
 *
 * Algorithm: Breadth-first search (BFS) / Dijkstra with uniform cost.
 * All edges have cost 1 (including diagonals for simplicity).
 */
public class DistanceMap {

    private final int width;
    private final int height;
    private final Grid<Integer> distances;

    private static final int UNREACHABLE = Integer.MAX_VALUE;

    // Direction offsets for 8-way movement (cardinal + diagonal)
    private static final Point[] DIRECTIONS_8 = Point.NEIGHBORS_8;

    // Direction offsets for 4-way movement (cardinal only)
    private static final Point[] DIRECTIONS_4 = Point.NEIGHBORS_4;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a distance map for the given dimensions.
     */
    public DistanceMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.distances = new Grid<>(width, height, UNREACHABLE);
    }

    // =========================================================================
    // Distance Map Building
    // =========================================================================

    /**
     * Build distance map from a single goal using 8-way movement.
     *
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     */
    public void build(Point goal, Predicate<Point> passable) {
        List<Point> goals = new ArrayList<>();
        goals.add(goal);
        build(goals, passable, true);
    }

    /**
     * Build distance map from a single goal.
     *
     * @param goal Goal position
     * @param passable Predicate that returns true if a point is walkable
     * @param allowDiagonal Whether to allow diagonal movement
     */
    public void build(Point goal, Predicate<Point> passable, boolean allowDiagonal) {
        List<Point> goals = new ArrayList<>();
        goals.add(goal);
        build(goals, passable, allowDiagonal);
    }

    /**
     * Build distance map from multiple goals using 8-way movement.
     *
     * @param goals List of goal positions
     * @param passable Predicate that returns true if a point is walkable
     */
    public void build(List<Point> goals, Predicate<Point> passable) {
        build(goals, passable, true);
    }

    /**
     * Build distance map from multiple goals.
     * Uses BFS to compute shortest distances to all reachable cells.
     *
     * @param goals List of goal positions
     * @param passable Predicate that returns true if a point is walkable
     * @param allowDiagonal Whether to allow diagonal movement
     */
    public void build(List<Point> goals, Predicate<Point> passable, boolean allowDiagonal) {
        // Clear previous distances
        distances.fill(UNREACHABLE);

        if (goals.isEmpty()) {
            return;
        }

        // Choose direction set
        Point[] directions = allowDiagonal ? DIRECTIONS_8 : DIRECTIONS_4;

        // BFS queue (simple array-based queue for performance)
        int[] queueX = new int[width * height];
        int[] queueY = new int[width * height];
        int head = 0;
        int tail = 0;

        // Initialize with all goals
        for (Point goal : goals) {
            if (inBounds(goal)) {
                distances.set(goal, 0);
                queueX[tail] = goal.x;
                queueY[tail] = goal.y;
                tail++;
            }
        }

        // BFS flood fill
        while (head < tail) {
            int x = queueX[head];
            int y = queueY[head];
            head++;

            Point current = new Point(x, y);
            int currentDist = distances.get(current);
            int nextDist = currentDist + 1;

            // Check all neighbors
            for (Point dir : directions) {
                Point neighbor = current.add(dir);

                if (inBounds(neighbor) && passable.test(neighbor)) {
                    Integer neighborDist = distances.get(neighbor);

                    if (neighborDist == null || neighborDist > nextDist) {
                        distances.set(neighbor, nextDist);
                        queueX[tail] = neighbor.x;
                        queueY[tail] = neighbor.y;
                        tail++;
                    }
                }
            }
        }
    }

    /**
     * Build distance map from a single goal using a boolean grid for passability.
     */
    public void build(Point goal, BooleanGrid walkable) {
        build(goal, p -> walkable.get(p));
    }

    /**
     * Build distance map from multiple goals using a boolean grid for passability.
     */
    public void build(List<Point> goals, BooleanGrid walkable) {
        build(goals, p -> walkable.get(p));
    }

    // =========================================================================
    // Queries
    // =========================================================================

    /**
     * Get the distance from goals to the specified point.
     * Returns UNREACHABLE if point is not reachable.
     */
    public int getDistance(Point p) {
        Integer dist = distances.get(p);
        return dist != null ? dist : UNREACHABLE;
    }

    /**
     * Get the distance from goals to the specified coordinates.
     */
    public int getDistance(int x, int y) {
        return getDistance(new Point(x, y));
    }

    /**
     * Check if a point is reachable from any goal.
     */
    public boolean isReachable(Point p) {
        return getDistance(p) < UNREACHABLE;
    }

    /**
     * Get all reachable points within a maximum distance.
     */
    public List<Point> getReachablePoints(int maxDistance) {
        List<Point> result = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point p = new Point(x, y);
                int dist = getDistance(p);
                if (dist <= maxDistance) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * Get the next step from 'from' toward the nearest goal.
     * Returns null if no valid step exists.
     */
    public Point getNextStep(Point from, boolean allowDiagonal) {
        int currentDist = getDistance(from);
        if (currentDist == UNREACHABLE || currentDist == 0) {
            return null; // Already at goal or unreachable
        }

        Point[] directions = allowDiagonal ? DIRECTIONS_8 : DIRECTIONS_4;
        Point bestStep = null;
        int bestDist = currentDist;

        for (Point dir : directions) {
            Point neighbor = from.add(dir);
            int neighborDist = getDistance(neighbor);

            if (neighborDist < bestDist) {
                bestDist = neighborDist;
                bestStep = neighbor;
            }
        }

        return bestStep;
    }

    // =========================================================================
    // Utilities
    // =========================================================================

    private boolean inBounds(Point p) {
        return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
    }

    /**
     * Get the distance grid (for advanced usage).
     */
    public Grid<Integer> getDistanceGrid() {
        return distances;
    }

    @Override
    public String toString() {
        return "DistanceMap[" + width + "x" + height + "]";
    }
}
