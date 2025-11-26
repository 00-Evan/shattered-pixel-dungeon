package com.shatteredpixel.engine.dungeon.fov;

import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.geom.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for Field of View calculation.
 *
 * Validates:
 * - Open rooms show diamond-shaped visibility
 * - Walls block vision
 * - VISIBLE and DISCOVERED flags updated correctly
 * - Different radii produce correct results
 */
public class FovTest {

    private LevelGrid grid;
    private FovCalculator fov;

    @Before
    public void setUp() {
        // Create 7x7 grid for testing
        grid = new LevelGrid(7, 7);
        fov = new FovCalculator();
    }

    /**
     * Test 1: Open room with all FLOOR tiles.
     *
     * Should see a diamond shape within radius.
     */
    @Test
    public void testOpenRoom() {
        // Fill with FLOOR (all transparent)
        grid.fill(TerrainType.FLOOR);

        // Compute FOV from center (3, 3) with radius 3
        Point origin = new Point(3, 3);
        Set<Point> visible = fov.computeVisible(grid, origin, 3);

        // Origin should be visible
        assertTrue("Origin should be visible", visible.contains(origin));

        // Adjacent tiles (distance 1) should be visible
        assertTrue("Left should be visible", visible.contains(new Point(2, 3)));
        assertTrue("Right should be visible", visible.contains(new Point(4, 3)));
        assertTrue("Up should be visible", visible.contains(new Point(3, 2)));
        assertTrue("Down should be visible", visible.contains(new Point(3, 4)));

        // Tiles at distance 2 should be visible
        assertTrue("Distance 2 left", visible.contains(new Point(1, 3)));
        assertTrue("Distance 2 up", visible.contains(new Point(3, 1)));

        // Tiles at distance 3 should be visible
        assertTrue("Distance 3 left", visible.contains(new Point(0, 3)));
        assertTrue("Distance 3 up", visible.contains(new Point(3, 0)));

        // Tiles outside radius (distance > 3) should NOT be visible
        assertFalse("Corner out of range", visible.contains(new Point(0, 0))); // distance 6
        assertFalse("Far corner out of range", visible.contains(new Point(6, 6))); // distance 6
    }

    /**
     * Test 2: Vertical wall blocks vision behind it.
     */
    @Test
    public void testWallBlocksVision() {
        // Fill with FLOOR
        grid.fill(TerrainType.FLOOR);

        // Add vertical wall at x=4
        for (int y = 0; y < 7; y++) {
            grid.setTerrain(4, y, TerrainType.WALL);
        }

        // Compute FOV from (2, 3) with radius 3
        Point origin = new Point(2, 3);
        Set<Point> visible = fov.computeVisible(grid, origin, 3);

        // Wall itself should be visible (you can see the wall)
        assertTrue("Wall should be visible", visible.contains(new Point(4, 3)));

        // Behind wall should NOT be visible
        assertFalse("Behind wall should not be visible", visible.contains(new Point(5, 3)));
        assertFalse("Far behind wall should not be visible", visible.contains(new Point(6, 3)));

        // In front of wall should be visible
        assertTrue("In front of wall", visible.contains(new Point(3, 3)));
        assertTrue("Origin", visible.contains(new Point(2, 3)));
    }

    /**
     * Test 3: Corner walls.
     *
     * Places walls at diagonal positions to test corner visibility.
     */
    @Test
    public void testCornerWalls() {
        // Fill with FLOOR
        grid.fill(TerrainType.FLOOR);

        // Add walls at corners around center
        grid.setTerrain(2, 2, TerrainType.WALL);
        grid.setTerrain(4, 2, TerrainType.WALL);
        grid.setTerrain(2, 4, TerrainType.WALL);
        grid.setTerrain(4, 4, TerrainType.WALL);

        // Compute FOV from center (3, 3) with radius 3
        Point origin = new Point(3, 3);
        Set<Point> visible = fov.computeVisible(grid, origin, 3);

        // Corner walls should be visible
        assertTrue("Top-left corner wall", visible.contains(new Point(2, 2)));
        assertTrue("Top-right corner wall", visible.contains(new Point(4, 2)));
        assertTrue("Bottom-left corner wall", visible.contains(new Point(2, 4)));
        assertTrue("Bottom-right corner wall", visible.contains(new Point(4, 4)));

        // Cardinal directions should still be visible (not blocked by diagonal walls)
        assertTrue("Left clear", visible.contains(new Point(2, 3)));
        assertTrue("Right clear", visible.contains(new Point(4, 3)));
        assertTrue("Up clear", visible.contains(new Point(3, 2)));
        assertTrue("Down clear", visible.contains(new Point(3, 4)));
    }

    /**
     * Test 4: computeAndApply() correctly updates VISIBLE and DISCOVERED flags.
     */
    @Test
    public void testComputeAndApplyFlags() {
        // Fill with FLOOR
        grid.fill(TerrainType.FLOOR);

        Point origin = new Point(3, 3);

        // Apply FOV from (3, 3) with radius 2
        fov.computeAndApply(grid, origin, 2);

        // Check VISIBLE and DISCOVERED flags at origin
        assertTrue("Origin visible", grid.getFlags(3, 3).isVisible());
        assertTrue("Origin discovered", grid.getFlags(3, 3).isDiscovered());

        // Check adjacent tiles
        assertTrue("Left visible", grid.getFlags(2, 3).isVisible());
        assertTrue("Left discovered", grid.getFlags(2, 3).isDiscovered());

        assertTrue("Right visible", grid.getFlags(4, 3).isVisible());
        assertTrue("Right discovered", grid.getFlags(4, 3).isDiscovered());

        // Outside radius should not be visible
        assertFalse("Far corner not visible", grid.getFlags(0, 0).isVisible());
        assertFalse("Far corner not discovered", grid.getFlags(0, 0).isDiscovered());

        // Apply FOV again from different position (1, 1)
        Point newOrigin = new Point(1, 1);
        fov.computeAndApply(grid, newOrigin, 2);

        // Old position (3, 3) should no longer be VISIBLE
        assertFalse("Old origin no longer visible", grid.getFlags(3, 3).isVisible());

        // But should still be DISCOVERED (persists)
        assertTrue("Old origin still discovered", grid.getFlags(3, 3).isDiscovered());

        // New position should be VISIBLE
        assertTrue("New origin visible", grid.getFlags(1, 1).isVisible());
        assertTrue("New origin discovered", grid.getFlags(1, 1).isDiscovered());
    }

    /**
     * Test 5: Different radii produce appropriate visibility ranges.
     */
    @Test
    public void testDifferentRadii() {
        grid.fill(TerrainType.FLOOR);
        Point origin = new Point(3, 3);

        // Radius 1 - should see only adjacent tiles
        Set<Point> visible1 = fov.computeVisible(grid, origin, 1);
        assertTrue("Origin visible r1", visible1.contains(new Point(3, 3)));
        assertTrue("Adjacent visible r1", visible1.contains(new Point(2, 3)));
        assertTrue("Adjacent visible r1", visible1.contains(new Point(4, 3)));
        assertFalse("Distance 2 not visible r1", visible1.contains(new Point(1, 3))); // distance 2

        // Radius 2 - should see further
        Set<Point> visible2 = fov.computeVisible(grid, origin, 2);
        assertTrue("Distance 2 visible r2", visible2.contains(new Point(1, 3))); // distance 2
        assertFalse("Distance 3 not visible r2", visible2.contains(new Point(0, 3))); // distance 3

        // Radius 3 - should see even further
        Set<Point> visible3 = fov.computeVisible(grid, origin, 3);
        assertTrue("Distance 3 visible r3", visible3.contains(new Point(0, 3))); // distance 3

        // Larger radius sees more tiles
        assertTrue("Radius 2 > Radius 1", visible2.size() > visible1.size());
        assertTrue("Radius 3 > Radius 2", visible3.size() > visible2.size());
    }

    /**
     * Test 6: Horizontal wall blocks vision.
     */
    @Test
    public void testHorizontalWall() {
        grid.fill(TerrainType.FLOOR);

        // Add horizontal wall at y=2
        for (int x = 0; x < 7; x++) {
            grid.setTerrain(x, 2, TerrainType.WALL);
        }

        // View from below the wall
        Point origin = new Point(3, 4);
        Set<Point> visible = fov.computeVisible(grid, origin, 3);

        // Wall should be visible
        assertTrue("Wall visible", visible.contains(new Point(3, 2)));

        // Above wall should NOT be visible
        assertFalse("Above wall not visible", visible.contains(new Point(3, 1)));
        assertFalse("Far above wall not visible", visible.contains(new Point(3, 0)));

        // Below wall should be visible
        assertTrue("Below wall visible", visible.contains(new Point(3, 3)));
        assertTrue("Origin visible", visible.contains(new Point(3, 4)));
    }
}
