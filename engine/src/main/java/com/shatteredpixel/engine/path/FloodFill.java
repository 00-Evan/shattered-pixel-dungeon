package com.shatteredpixel.engine.path;

import com.shatteredpixel.engine.geom.BooleanGrid;
import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Flood fill algorithm for finding reachable areas.
 * Used for movement range calculation, area effects, targeting, etc.
 *
 * This is a new class (not directly from legacy).
 * Provides clean API for area/range queries commonly needed in roguelikes.
 *
 * Design goals:
 * - Simple BFS-based flood fill
 * - Supports maximum distance limits
 * - Supports custom passability checks
 * - GWT-safe (Java 11)
 */
public class FloodFill {

    /**
     * Find all points reachable from start within maxSteps.
     *
     * @param start Starting position
     * @param maxSteps Maximum number of steps allowed
     * @param passable Predicate that returns true if a point is walkable
     * @param allowDiagonal Whether to allow diagonal movement
     * @return Set of all reachable points (including start)
     */
    public static Set<Point> getReachableArea(Point start, int maxSteps,
                                               Predicate<Point> passable,
                                               boolean allowDiagonal) {
        Set<Point> reachable = new HashSet<>();
        if (!passable.test(start)) {
            return reachable;
        }

        Point[] directions = allowDiagonal ? Point.NEIGHBORS_8 : Point.NEIGHBORS_4;

        // BFS with distance tracking
        List<Point> queue = new ArrayList<>();
        Set<Point> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        reachable.add(start);

        int currentDistance = 0;
        int currentLevelSize = 1;
        int nextLevelSize = 0;

        while (!queue.isEmpty() && currentDistance < maxSteps) {
            Point current = queue.remove(0);
            currentLevelSize--;

            for (Point dir : directions) {
                Point neighbor = current.add(dir);

                if (!visited.contains(neighbor) && passable.test(neighbor)) {
                    visited.add(neighbor);
                    reachable.add(neighbor);
                    queue.add(neighbor);
                    nextLevelSize++;
                }
            }

            // Move to next distance level
            if (currentLevelSize == 0) {
                currentDistance++;
                currentLevelSize = nextLevelSize;
                nextLevelSize = 0;
            }
        }

        return reachable;
    }

    /**
     * Find all points reachable from start within maxSteps (8-way movement).
     */
    public static Set<Point> getReachableArea(Point start, int maxSteps, Predicate<Point> passable) {
        return getReachableArea(start, maxSteps, passable, true);
    }

    /**
     * Find all points reachable from start within maxSteps using a boolean grid.
     */
    public static Set<Point> getReachableArea(Point start, int maxSteps, BooleanGrid walkable) {
        return getReachableArea(start, maxSteps, p -> walkable.get(p), true);
    }

    /**
     * Fill a boolean grid with reachable points from start.
     * Sets result[x][y] = true for all reachable points.
     *
     * @param start Starting position
     * @param maxSteps Maximum number of steps allowed
     * @param passable Predicate that returns true if a point is walkable
     * @param result Output grid (must match dimensions)
     * @param allowDiagonal Whether to allow diagonal movement
     */
    public static void fillReachableGrid(Point start, int maxSteps,
                                         Predicate<Point> passable,
                                         BooleanGrid result,
                                         boolean allowDiagonal) {
        result.clear();
        Set<Point> reachable = getReachableArea(start, maxSteps, passable, allowDiagonal);
        for (Point p : reachable) {
            result.set(p, true);
        }
    }

    /**
     * Fill a boolean grid with reachable points (8-way movement).
     */
    public static void fillReachableGrid(Point start, int maxSteps,
                                         Predicate<Point> passable,
                                         BooleanGrid result) {
        fillReachableGrid(start, maxSteps, passable, result, true);
    }
}
