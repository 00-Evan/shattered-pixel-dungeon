package com.shatteredpixel.engine.dungeon.fov;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.dungeon.terrain.TileFlags;
import com.shatteredpixel.engine.event.GameEvent;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Integration tests for FOV updates with movement commands.
 *
 * Validates that:
 * - FOV is recomputed after successful MOVE commands
 * - VISIBLE flags are updated based on new actor position
 * - DISCOVERED flags persist (exploration history)
 * - Walls and obstacles block vision correctly
 * - No crashes when vision source is not set
 */
public class FovIntegrationTest {

    private GameEngine engine;
    private EngineContext context;
    private LevelState level;
    private LevelGrid grid;

    /**
     * Mock platform for headless testing.
     */
    private static class MockPlatform implements GamePlatform {
        @Override
        public void initialize() {
            // No-op for testing
        }

        @Override
        public String getPlatformName() {
            return "Test";
        }
    }

    /**
     * Minimal Character implementation for testing.
     */
    private static class TestCharacter extends Character {
        public TestCharacter(Point position, Stats stats) {
            super(ActorType.OTHER, position, stats);
        }

        public TestCharacter(Point position) {
            this(position, new Stats(100));
        }

        @Override
        public float act(EngineContext context) {
            return 1.0f;
        }
    }

    @Before
    public void setUp() {
        // Create engine with mock platform and fixed seed for determinism
        engine = new GameEngine(new MockPlatform(), 12345L);
        engine.initialize();
        context = engine.getContext();

        // Create a 9x9 level filled with FLOOR
        level = new LevelState(1, 9, 9);
        grid = level.getGrid();
        grid.fill(TerrainType.FLOOR);

        // Set level in context
        context.setLevel(level);
    }

    /**
     * Helper: Get all tiles with VISIBLE flag set.
     */
    private Set<Point> getVisibleTiles() {
        Set<Point> visible = new HashSet<>();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Point p = new Point(x, y);
                if (grid.hasFlag(p, TileFlags.VISIBLE)) {
                    visible.add(p);
                }
            }
        }
        return visible;
    }

    /**
     * Helper: Get all tiles with DISCOVERED flag set.
     */
    private Set<Point> getDiscoveredTiles() {
        Set<Point> discovered = new HashSet<>();
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Point p = new Point(x, y);
                if (grid.hasFlag(p, TileFlags.DISCOVERED)) {
                    discovered.add(p);
                }
            }
        }
        return discovered;
    }

    /**
     * Test 1: FOV updated after MOVE in open room.
     *
     * Scenario:
     * - Character at center (4, 4) of 9x9 FLOOR grid
     * - Set as vision source
     * - Move to (5, 4)
     *
     * Expected:
     * - FOV recomputed from new position (5, 4)
     * - VISIBLE tiles centered on (5, 4) within radius 8
     * - DISCOVERED includes all previously visible tiles plus new ones
     * - Tiles far to the left (behind old position) may lose VISIBLE but keep DISCOVERED
     */
    @Test
    public void testFovUpdatedAfterMoveInOpenRoom() {
        // Setup: Character at center
        Point startPos = new Point(4, 4);
        TestCharacter character = new TestCharacter(startPos);
        context.addActor(character);
        context.setVisionActorId(character.getId());
        grid.setOccupied(startPos, true);

        // Compute initial FOV
        FovCalculator fov = new FovCalculator();
        fov.computeAndApply(grid, startPos, 8);

        Set<Point> initialVisible = getVisibleTiles();
        Set<Point> initialDiscovered = getDiscoveredTiles();

        assertTrue("Initial FOV should have visible tiles", initialVisible.size() > 0);
        assertTrue("Initial FOV should include center", initialVisible.contains(startPos));

        // Action: Move right to (5, 4)
        Point targetPos = new Point(5, 4);
        GameCommand moveCommand = GameCommand.moveTo(character.getId(), targetPos);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Get new FOV state
        Set<Point> newVisible = getVisibleTiles();
        Set<Point> newDiscovered = getDiscoveredTiles();

        // Assertions
        assertEquals("Actor should be at new position", targetPos, character.getPosition());
        assertTrue("New FOV should have visible tiles", newVisible.size() > 0);
        assertTrue("New FOV should include new position", newVisible.contains(targetPos));

        // DISCOVERED should be a superset of initial DISCOVERED (exploration history)
        assertTrue("DISCOVERED should include all previously discovered tiles",
            newDiscovered.containsAll(initialDiscovered));

        // New VISIBLE should be centered on new position
        // Check that origin is visible
        assertTrue("New position should be visible", newVisible.contains(targetPos));

        // Tiles to the far left of the old position might no longer be visible
        // but should still be discovered
        Point farLeft = new Point(0, 4);
        if (initialDiscovered.contains(farLeft)) {
            assertTrue("Previously discovered tiles should remain discovered",
                newDiscovered.contains(farLeft));
        }
    }

    /**
     * Test 2: FOV blocked by WALL after movement.
     *
     * Scenario:
     * - Character starts at (2, 4)
     * - Vertical wall at x=5
     * - Move from (2, 4) to (3, 4) (closer to wall)
     *
     * Expected:
     * - Wall itself becomes visible
     * - Tiles behind wall (x >= 6) remain not visible
     * - DISCOVERED accumulates over multiple moves
     */
    @Test
    public void testFovBlockedByWallAfterMovement() {
        // Setup: Character on left side, wall in middle
        Point startPos = new Point(2, 4);
        TestCharacter character = new TestCharacter(startPos);
        context.addActor(character);
        context.setVisionActorId(character.getId());
        grid.setOccupied(startPos, true);

        // Add vertical wall at x=5
        for (int y = 0; y < 9; y++) {
            grid.setTerrain(5, y, TerrainType.WALL);
        }

        // Compute initial FOV
        FovCalculator fov = new FovCalculator();
        fov.computeAndApply(grid, startPos, 8);

        Set<Point> initialVisible = getVisibleTiles();

        // Wall at (5, 4) should be visible initially (within radius)
        assertTrue("Wall should be visible from start position",
            initialVisible.contains(new Point(5, 4)));

        // Behind wall should not be visible
        assertFalse("Behind wall should not be visible",
            initialVisible.contains(new Point(6, 4)));

        // Action: Move closer to wall (2, 4) -> (3, 4)
        Point targetPos = new Point(3, 4);
        GameCommand moveCommand = GameCommand.moveTo(character.getId(), targetPos);
        engine.tick(Collections.singletonList(moveCommand));

        // Get new FOV state
        Set<Point> newVisible = getVisibleTiles();
        Set<Point> newDiscovered = getDiscoveredTiles();

        // Assertions
        assertEquals("Actor should be at new position", targetPos, character.getPosition());

        // Wall should still be visible
        assertTrue("Wall should be visible from new position",
            newVisible.contains(new Point(5, 4)));

        // Behind wall should still not be visible
        assertFalse("Behind wall should not be visible from new position",
            newVisible.contains(new Point(6, 4)));
        assertFalse("Behind wall should not be discovered",
            newDiscovered.contains(new Point(6, 4)));

        // New position should be visible
        assertTrue("New position should be visible", newVisible.contains(targetPos));
    }

    /**
     * Test 3: No vision source available.
     *
     * Scenario:
     * - No vision actor set in EngineContext
     * - Move commands are issued
     *
     * Expected:
     * - No crash
     * - FOV flags remain unchanged (no automatic updates)
     * - Movement still succeeds
     */
    @Test
    public void testNoVisionSourceAvailable() {
        // Setup: Character without setting as vision source
        Point startPos = new Point(4, 4);
        TestCharacter character = new TestCharacter(startPos);
        context.addActor(character);
        grid.setOccupied(startPos, true);

        // Do NOT set vision actor: context.setVisionActorId(character.getId());

        // Manually set some VISIBLE flags to test they don't change
        grid.setFlag(new Point(3, 3), TileFlags.VISIBLE, true);
        grid.setFlag(new Point(3, 4), TileFlags.VISIBLE, true);

        Set<Point> initialVisible = getVisibleTiles();
        assertTrue("Should have manually set visible tiles", initialVisible.size() > 0);

        // Action: Move
        Point targetPos = new Point(5, 4);
        GameCommand moveCommand = GameCommand.moveTo(character.getId(), targetPos);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertNotNull("Events should not be null", events);
        assertEquals("Actor should have moved", targetPos, character.getPosition());

        // FOV flags should be UNCHANGED (no vision source set)
        Set<Point> newVisible = getVisibleTiles();
        assertEquals("VISIBLE flags should not change without vision source",
            initialVisible, newVisible);
    }

    /**
     * Test 4: Different actor moves (not vision source).
     *
     * Scenario:
     * - Two characters: A (vision source) and B (other actor)
     * - B moves, not A
     *
     * Expected:
     * - FOV is NOT recomputed (only updates when vision source moves)
     * - Movement succeeds for B
     * - VISIBLE flags remain unchanged
     */
    @Test
    public void testDifferentActorMoves() {
        // Setup: Two characters, A is vision source
        Point posA = new Point(4, 4);
        Point posB = new Point(6, 4);
        TestCharacter actorA = new TestCharacter(posA);
        TestCharacter actorB = new TestCharacter(posB);

        context.addActor(actorA);
        context.addActor(actorB);
        context.setVisionActorId(actorA.getId()); // A is vision source
        grid.setOccupied(posA, true);
        grid.setOccupied(posB, true);

        // Compute initial FOV from A's position
        FovCalculator fov = new FovCalculator();
        fov.computeAndApply(grid, posA, 8);

        Set<Point> initialVisible = getVisibleTiles();
        assertTrue("Initial FOV should have visible tiles", initialVisible.size() > 0);

        // Action: Move B (not the vision source)
        Point targetPosB = new Point(6, 5);
        GameCommand moveCommand = GameCommand.moveTo(actorB.getId(), targetPosB);
        engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertEquals("Actor B should have moved", targetPosB, actorB.getPosition());
        assertEquals("Actor A should not have moved", posA, actorA.getPosition());

        // FOV should be UNCHANGED (vision source didn't move)
        Set<Point> newVisible = getVisibleTiles();
        assertEquals("VISIBLE flags should not change when non-vision actor moves",
            initialVisible, newVisible);
    }

    /**
     * Test 5: Vision source moves multiple times - DISCOVERED accumulates.
     *
     * Scenario:
     * - Character moves multiple times in different directions
     * - Each move reveals new tiles
     *
     * Expected:
     * - DISCOVERED continually accumulates (never loses tiles)
     * - VISIBLE changes with each move
     */
    @Test
    public void testDiscoveredAccumulatesOverMoves() {
        // Setup: Character at bottom-left
        Point pos1 = new Point(1, 1);
        TestCharacter character = new TestCharacter(pos1);
        context.addActor(character);
        context.setVisionActorId(character.getId());
        grid.setOccupied(pos1, true);

        // Initial FOV
        FovCalculator fov = new FovCalculator();
        fov.computeAndApply(grid, pos1, 8);

        Set<Point> discovered1 = getDiscoveredTiles();
        assertTrue("Should discover tiles from first position", discovered1.size() > 0);

        // Move 1: Right to (4, 1)
        Point pos2 = new Point(4, 1);
        grid.setOccupied(pos1, false);
        GameCommand move1 = GameCommand.moveTo(character.getId(), pos2);
        engine.tick(Collections.singletonList(move1));
        grid.setOccupied(pos2, true);

        Set<Point> discovered2 = getDiscoveredTiles();
        assertTrue("DISCOVERED should include all from first move",
            discovered2.containsAll(discovered1));
        assertTrue("DISCOVERED should grow after move",
            discovered2.size() >= discovered1.size());

        // Move 2: Up to (4, 4)
        Point pos3 = new Point(4, 4);
        grid.setOccupied(pos2, false);
        GameCommand move2 = GameCommand.moveTo(character.getId(), pos3);
        engine.tick(Collections.singletonList(move2));
        grid.setOccupied(pos3, true);

        Set<Point> discovered3 = getDiscoveredTiles();
        assertTrue("DISCOVERED should include all from previous moves",
            discovered3.containsAll(discovered2));

        // Verify old positions are still discovered even if not visible
        assertTrue("First position should remain discovered", discovered3.contains(pos1));
        assertTrue("Second position should remain discovered", discovered3.contains(pos2));
    }

    /**
     * Test 6: FOV with corner wall configuration.
     *
     * Scenario:
     * - L-shaped wall configuration
     * - Character moves around the corner
     *
     * Expected:
     * - Vision correctly handles corner occlusion
     * - New areas become visible as character rounds corner
     */
    @Test
    public void testFovWithCornerWalls() {
        // Setup: Character at (2, 2), L-shaped wall
        Point startPos = new Point(2, 2);
        TestCharacter character = new TestCharacter(startPos);
        context.addActor(character);
        context.setVisionActorId(character.getId());
        grid.setOccupied(startPos, true);

        // Create L-shaped wall:
        // Horizontal wall at y=4, x=[4..8]
        // Vertical wall at x=4, y=[4..8]
        for (int x = 4; x <= 8; x++) {
            grid.setTerrain(x, 4, TerrainType.WALL);
        }
        for (int y = 4; y <= 8; y++) {
            grid.setTerrain(4, y, TerrainType.WALL);
        }

        // Initial FOV
        FovCalculator fov = new FovCalculator();
        fov.computeAndApply(grid, startPos, 8);

        Set<Point> initialVisible = getVisibleTiles();

        // Area beyond the L (e.g., (6, 6)) should not be visible
        assertFalse("Area beyond L-wall should not be visible",
            initialVisible.contains(new Point(6, 6)));

        // Move to (5, 2) - to the right but still above horizontal wall
        Point pos2 = new Point(5, 2);
        grid.setOccupied(startPos, false);
        GameCommand move1 = GameCommand.moveTo(character.getId(), pos2);
        engine.tick(Collections.singletonList(move1));
        grid.setOccupied(pos2, true);

        Set<Point> visible2 = getVisibleTiles();

        // Still shouldn't see beyond wall
        assertFalse("Area beyond L-wall should still not be visible",
            visible2.contains(new Point(6, 6)));

        // Move to (5, 5) - beyond the wall corner
        grid.setOccupied(pos2, false);
        Point pos3 = new Point(5, 5);
        GameCommand move2 = GameCommand.moveTo(character.getId(), pos3);
        engine.tick(Collections.singletonList(move2));
        grid.setOccupied(pos3, true);

        Set<Point> visible3 = getVisibleTiles();

        // Now should be able to see (6, 6) area
        assertTrue("Area beyond corner should now be visible",
            visible3.contains(new Point(6, 6)));
    }
}
