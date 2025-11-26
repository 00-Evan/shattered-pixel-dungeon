package com.shatteredpixel.engine.dungeon.fov;

import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.geom.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Field of View calculator.
 *
 * Computes which tiles are visible from a given origin point,
 * based on terrain transparency and distance.
 *
 * Algorithm: Bresenham line-based FOV
 * - For each tile within radius
 * - Cast a line from origin to tile
 * - If line is not blocked by opaque tiles, tile is visible
 *
 * This is simple and correct, though not the most efficient.
 * Future: Could optimize with shadow-casting or recursive shadowcasting.
 */
public class FovCalculator {

    /**
     * Compute visible tiles from origin within radius.
     *
     * Uses diamond/Manhattan distance (|dx| + |dy| <= radius).
     * Checks line-of-sight using Bresenham line algorithm.
     *
     * @param grid The level grid
     * @param origin The viewpoint
     * @param radius Maximum vision distance (Manhattan distance)
     * @return Set of visible tile positions
     */
    public Set<Point> computeVisible(LevelGrid grid, Point origin, int radius) {
        Set<Point> visible = new HashSet<>();

        // Origin is always visible (if in bounds)
        if (grid.isInBounds(origin)) {
            visible.add(origin);
        }

        // Check all tiles within radius (diamond shape)
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                // Skip origin (already added)
                if (dx == 0 && dy == 0) continue;

                // Use diamond/Manhattan distance
                if (Math.abs(dx) + Math.abs(dy) > radius) continue;

                Point target = new Point(origin.x + dx, origin.y + dy);

                // Check if target is in bounds
                if (!grid.isInBounds(target)) continue;

                // Check if line of sight is clear
                if (hasLineOfSight(grid, origin, target)) {
                    visible.add(target);
                }
            }
        }

        return visible;
    }

    /**
     * Compute FOV and apply to grid flags.
     *
     * Flow:
     * 1. Clear all VISIBLE flags
     * 2. Compute visible tiles
     * 3. Set VISIBLE for tiles in current FOV
     * 4. Set DISCOVERED for any newly seen tiles
     *
     * VISIBLE is temporary (current FOV only).
     * DISCOVERED persists (tiles seen at least once).
     *
     * @param grid The level grid
     * @param origin The viewpoint
     * @param radius Maximum vision distance
     */
    public void computeAndApply(LevelGrid grid, Point origin, int radius) {
        // Clear all VISIBLE flags
        VisibilityUtils.clearVisible(grid);

        // Compute visible tiles
        Set<Point> visible = computeVisible(grid, origin, radius);

        // Apply flags
        for (Point p : visible) {
            VisibilityUtils.markVisible(grid, p);
            VisibilityUtils.markDiscovered(grid, p);
        }
    }

    /**
     * Check if there is line of sight from origin to target.
     *
     * Uses Bresenham line algorithm to trace the path.
     * Intermediate opaque tiles block LOS.
     * Target being opaque does NOT block LOS to the target itself
     * (you can see the wall, just not through it).
     *
     * @param grid The level grid
     * @param origin Start point
     * @param target End point
     * @return true if LOS is clear
     */
    private boolean hasLineOfSight(LevelGrid grid, Point origin, Point target) {
        // Get all points along the line
        List<Point> line = VisibilityUtils.bresenhamLine(origin, target);

        // Check all points except origin and target
        // (We can see the target even if it's opaque, just not through it)
        for (int i = 1; i < line.size() - 1; i++) {
            Point p = line.get(i);

            // If any intermediate point is opaque, no LOS
            if (!grid.isTransparent(p)) {
                return false;
            }
        }

        return true;
    }
}
