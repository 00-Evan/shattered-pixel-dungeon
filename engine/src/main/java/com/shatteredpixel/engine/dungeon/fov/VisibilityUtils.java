package com.shatteredpixel.engine.dungeon.fov;

import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for visibility and line-of-sight calculations.
 *
 * Provides:
 * - Flag manipulation (VISIBLE, DISCOVERED)
 * - Bresenham line algorithm for LOS checks
 */
public class VisibilityUtils {

    /**
     * Clear all VISIBLE flags in the grid.
     * Call this before computing a new FOV to reset visibility state.
     */
    public static void clearVisible(LevelGrid grid) {
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                grid.getFlags(x, y).setVisible(false);
            }
        }
    }

    /**
     * Mark a tile as visible (currently in FOV).
     */
    public static void markVisible(LevelGrid grid, Point p) {
        if (grid.isInBounds(p)) {
            grid.getFlags(p).setVisible(true);
        }
    }

    /**
     * Mark a tile as discovered (seen at least once).
     * DISCOVERED persists across FOV updates.
     */
    public static void markDiscovered(LevelGrid grid, Point p) {
        if (grid.isInBounds(p)) {
            grid.getFlags(p).setDiscovered(true);
        }
    }

    /**
     * Bresenham line algorithm.
     *
     * Returns all points from start to end (inclusive).
     * Used for line-of-sight checks.
     *
     * @param start Line start point
     * @param end Line end point
     * @return List of all points along the line
     */
    public static List<Point> bresenhamLine(Point start, Point end) {
        List<Point> line = new ArrayList<>();

        int x0 = start.x;
        int y0 = start.y;
        int x1 = end.x;
        int y1 = end.y;

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        int x = x0;
        int y = y0;

        while (true) {
            line.add(new Point(x, y));

            if (x == x1 && y == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }

        return line;
    }
}
