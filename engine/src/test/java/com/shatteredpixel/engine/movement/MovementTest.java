package com.shatteredpixel.engine.movement;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.event.EventType;
import com.shatteredpixel.engine.event.GameEvent;
import com.shatteredpixel.engine.geom.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for movement and collision detection.
 *
 * Validates that:
 * - MOVE commands update actor positions correctly
 * - Invalid moves are rejected with appropriate events
 * - OCCUPIED flags are updated consistently
 * - GameEvents are emitted correctly
 */
public class MovementTest {

    private GameEngine engine;
    private EngineContext context;
    private LevelState level;
    private LevelGrid grid;

    /**
     * Mock platform for headless testing.
     * Provides minimal GamePlatform implementation without any UI dependencies.
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

    @Before
    public void setUp() {
        // Create engine with mock platform and fixed seed for determinism
        engine = new GameEngine(new MockPlatform(), 12345L);
        engine.initialize();

        // Get context from engine
        context = engine.getContext();

        // Create a 5x5 level filled with FLOOR
        level = new LevelState(1, 5, 5);
        grid = level.getGrid();
        grid.fill(TerrainType.FLOOR);

        // Set level in context
        context.setLevel(level);
    }

    /**
     * Test 1: Valid movement on empty floor.
     *
     * Scenario:
     * - Actor at (2, 2) moves to (3, 2)
     * - Both positions are passable FLOOR
     * - No obstacles
     *
     * Expected:
     * - Actor position updated to (3, 2)
     * - OCCUPIED flag cleared at (2, 2)
     * - OCCUPIED flag set at (3, 2)
     * - One ACTOR_MOVED event emitted
     */
    @Test
    public void testValidMovement() {
        // Setup: Create actor at (2, 2)
        Point startPos = new Point(2, 2);
        TestCharacter actor = new TestCharacter(startPos);
        context.addActor(actor);
        grid.setOccupied(startPos, true);

        // Action: Move to (3, 2)
        Point targetPos = new Point(3, 2);
        GameCommand moveCommand = GameCommand.moveTo(actor.getId(), targetPos);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertEquals("Actor position should be updated", targetPos, actor.getPosition());
        assertFalse("Old position should not be occupied", grid.isOccupied(startPos));
        assertTrue("New position should be occupied", grid.isOccupied(targetPos));

        // Check events
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .count();
        assertEquals("Should have one ACTOR_MOVED event", 1, movedEvents);

        GameEvent movedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .findFirst()
            .orElse(null);
        assertNotNull("ACTOR_MOVED event should exist", movedEvent);
        assertEquals("Event should have correct actor ID", actor.getId(), movedEvent.getSourceActorId());
        assertEquals("Event should have correct position", targetPos, movedEvent.getPosition());
    }

    /**
     * Test 2: Movement into WALL (not passable).
     *
     * Scenario:
     * - Actor at (2, 2) tries to move to (3, 2)
     * - Target (3, 2) is a WALL
     *
     * Expected:
     * - Actor stays at (2, 2)
     * - OCCUPIED flags unchanged
     * - LOG_MESSAGE event with "not passable"
     * - No ACTOR_MOVED event
     */
    @Test
    public void testMovementIntoWall() {
        // Setup: Actor at (2, 2), wall at (3, 2)
        Point startPos = new Point(2, 2);
        Point wallPos = new Point(3, 2);
        TestCharacter actor = new TestCharacter(startPos);
        context.addActor(actor);
        grid.setOccupied(startPos, true);
        grid.setTerrain(wallPos, TerrainType.WALL);

        // Action: Try to move into wall
        GameCommand moveCommand = GameCommand.moveTo(actor.getId(), wallPos);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertEquals("Actor should not move", startPos, actor.getPosition());
        assertTrue("Original position should still be occupied", grid.isOccupied(startPos));
        assertFalse("Wall position should not be occupied", grid.isOccupied(wallPos));

        // Check for LOG_MESSAGE event
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("not passable"))
            .count();
        assertTrue("Should have a LOG_MESSAGE about not passable", logEvents >= 1);

        // Check NO ACTOR_MOVED event
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .count();
        assertEquals("Should have no ACTOR_MOVED events", 0, movedEvents);
    }

    /**
     * Test 3: Movement out of bounds.
     *
     * Scenario:
     * - Actor at (2, 2) tries to move to (-1, 2)
     * - Target is outside the 5x5 grid
     *
     * Expected:
     * - Actor stays at (2, 2)
     * - OCCUPIED flags unchanged
     * - LOG_MESSAGE event with "out of bounds"
     * - No ACTOR_MOVED event
     */
    @Test
    public void testMovementOutOfBounds() {
        // Setup: Actor at (2, 2)
        Point startPos = new Point(2, 2);
        TestCharacter actor = new TestCharacter(startPos);
        context.addActor(actor);
        grid.setOccupied(startPos, true);

        // Action: Try to move out of bounds
        Point outOfBounds = new Point(-1, 2);
        GameCommand moveCommand = GameCommand.moveTo(actor.getId(), outOfBounds);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertEquals("Actor should not move", startPos, actor.getPosition());
        assertTrue("Original position should still be occupied", grid.isOccupied(startPos));

        // Check for LOG_MESSAGE about out of bounds
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("out of bounds"))
            .count();
        assertTrue("Should have LOG_MESSAGE about out of bounds", logEvents >= 1);

        // No ACTOR_MOVED
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .count();
        assertEquals("Should have no ACTOR_MOVED events", 0, movedEvents);
    }

    /**
     * Test 4: Movement into occupied tile.
     *
     * Scenario:
     * - Actor A at (2, 2)
     * - Actor B at (3, 2)
     * - A tries to move into B's position
     *
     * Expected:
     * - Actor A stays at (2, 2)
     * - Actor B stays at (3, 2)
     * - Both OCCUPIED flags unchanged
     * - LOG_MESSAGE event with "occupied"
     * - No ACTOR_MOVED event for A
     */
    @Test
    public void testMovementIntoOccupiedTile() {
        // Setup: Two actors - A at (2, 2), B at (3, 2)
        Point posA = new Point(2, 2);
        Point posB = new Point(3, 2);
        TestCharacter actorA = new TestCharacter(posA);
        TestCharacter actorB = new TestCharacter(posB);
        context.addActor(actorA);
        context.addActor(actorB);
        grid.setOccupied(posA, true);
        grid.setOccupied(posB, true);

        // Action: Try to move A into B's position
        GameCommand moveCommand = GameCommand.moveTo(actorA.getId(), posB);
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions
        assertEquals("Actor A should not move", posA, actorA.getPosition());
        assertEquals("Actor B should not move", posB, actorB.getPosition());
        assertTrue("Position A should still be occupied", grid.isOccupied(posA));
        assertTrue("Position B should still be occupied", grid.isOccupied(posB));

        // Check for LOG_MESSAGE about occupied
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("occupied"))
            .count();
        assertTrue("Should have LOG_MESSAGE about occupied", logEvents >= 1);

        // No ACTOR_MOVED for A
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .filter(e -> e.getSourceActorId().equals(actorA.getId()))
            .count();
        assertEquals("Should have no ACTOR_MOVED for actor A", 0, movedEvents);
    }

    /**
     * Test 5: Missing actor.
     *
     * Scenario:
     * - MOVE command references a non-existent ActorId
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE event with "not found"
     * - No ACTOR_MOVED event
     */
    @Test
    public void testMissingActor() {
        // Action: Try to move a non-existent actor
        ActorId fakeId = ActorId.generate();
        GameCommand moveCommand = GameCommand.moveTo(fakeId, new Point(3, 2));
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions - should not crash
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about actor not found
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("not found"))
            .count();
        assertTrue("Should have LOG_MESSAGE about actor not found", logEvents >= 1);

        // No ACTOR_MOVED
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .count();
        assertEquals("Should have no ACTOR_MOVED events", 0, movedEvents);
    }

    /**
     * Test 6: No level loaded.
     *
     * Scenario:
     * - Context has no LevelState set
     * - MOVE command is issued
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE event with "no level"
     * - Actor position unchanged
     */
    @Test
    public void testNoLevelLoaded() {
        // Setup: Clear the level
        context.setLevel(null);

        // Create an actor (but it's not on any level)
        Point pos = new Point(2, 2);
        TestCharacter actor = new TestCharacter(pos);
        context.addActor(actor);

        // Action: Try to move
        GameCommand moveCommand = GameCommand.moveTo(actor.getId(), new Point(3, 2));
        List<GameEvent> events = engine.tick(Collections.singletonList(moveCommand));

        // Assertions - should not crash
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about no level
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("no level"))
            .count();
        assertTrue("Should have LOG_MESSAGE about no level", logEvents >= 1);

        // Actor position unchanged
        assertEquals("Actor position should not change", pos, actor.getPosition());
    }
}
