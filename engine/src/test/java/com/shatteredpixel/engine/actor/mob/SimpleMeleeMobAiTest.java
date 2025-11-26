package com.shatteredpixel.engine.actor.mob;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.hero.BaseHero;
import com.shatteredpixel.engine.command.CommandType;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.event.EventType;
import com.shatteredpixel.engine.event.GameEvent;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for SimpleMeleeMob AI decision-making.
 *
 * Validates that:
 * - Mob decides to ATTACK when adjacent to hero
 * - Mob decides to MOVE towards distant hero
 * - Mob waits (returns null) when no heroes present
 * - Mob waits when movement is blocked
 * - End-to-end: mob's AI command actually executes via GameEngine
 */
public class SimpleMeleeMobAiTest {

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
     * Minimal concrete hero implementation for testing.
     */
    private static class TestHero extends BaseHero {
        public TestHero(Point position, Stats stats, String name) {
            super(position, stats, name);
        }

        public TestHero(Point position, String name) {
            // Default: 100 HP, 10 attack (weak for this test)
            this(position, new Stats(100, 10, 0, 10, 10, 1.0f), name);
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

        // Create a 7x7 level filled with FLOOR
        level = new LevelState(1, 7, 7);
        grid = level.getGrid();
        grid.fill(TerrainType.FLOOR);

        // Set level in context
        context.setLevel(level);
    }

    /**
     * Test (A): decideNextAction returns ATTACK when adjacent to hero.
     *
     * Scenario:
     * - SimpleMeleeMob at (2, 2)
     * - TestHero at (3, 2) (adjacent, distance = 1)
     *
     * Expected:
     * - Command is ATTACK
     * - Attacker is mob
     * - Target is hero
     */
    @Test
    public void testDecideNextAction_AttackWhenAdjacent() {
        // Setup: Mob and hero adjacent
        Point mobPos = new Point(2, 2);
        Point heroPos = new Point(3, 2);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions
        assertNotNull("Command should not be null", cmd);
        assertEquals("Command type should be ATTACK",
            CommandType.ATTACK, cmd.getType());
        assertEquals("Attacker should be mob",
            mob.getId(), cmd.getActorId());
        assertEquals("Target should be hero",
            hero.getId(), cmd.getTargetActorId());
        assertNull("Target position should be null for ATTACK",
            cmd.getTargetPosition());
    }

    /**
     * Test (B): decideNextAction returns MOVE towards distant hero.
     *
     * Scenario:
     * - SimpleMeleeMob at (1, 1)
     * - TestHero at (5, 1) (distance = 4, same row)
     *
     * Expected:
     * - Command is MOVE
     * - Target position is (2, 1) (one step towards hero)
     * - Horizontal movement preferred
     */
    @Test
    public void testDecideNextAction_MoveTowardsHero() {
        // Setup: Mob and hero distant (same row)
        Point mobPos = new Point(1, 1);
        Point heroPos = new Point(5, 1);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions
        assertNotNull("Command should not be null", cmd);
        assertEquals("Command type should be MOVE",
            CommandType.MOVE, cmd.getType());
        assertEquals("Actor should be mob",
            mob.getId(), cmd.getActorId());

        Point targetPos = cmd.getTargetPosition();
        assertNotNull("Target position should not be null", targetPos);

        // Should move one step towards hero (horizontally)
        assertEquals("Should move right towards hero",
            new Point(2, 1), targetPos);

        // Verify target is valid
        assertTrue("Target should be in bounds", grid.isInBounds(targetPos));
        assertTrue("Target should be passable", grid.isPassable(targetPos));
        assertFalse("Target should not be occupied", grid.isOccupied(targetPos));
    }

    /**
     * Test (B2): decideNextAction with diagonal movement.
     *
     * Scenario:
     * - Mob at (1, 1), Hero at (4, 3) (diagonal)
     *
     * Expected:
     * - Moves horizontally first (implementation preference)
     * - Target is (2, 1)
     */
    @Test
    public void testDecideNextAction_MoveDiagonal() {
        // Setup: Mob and hero diagonal
        Point mobPos = new Point(1, 1);
        Point heroPos = new Point(4, 3);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions
        assertNotNull("Command should not be null", cmd);
        assertEquals("Command type should be MOVE", CommandType.MOVE, cmd.getType());

        Point targetPos = cmd.getTargetPosition();
        assertNotNull("Target position should not be null", targetPos);

        // Horizontal movement preferred (dx != 0 takes priority)
        assertEquals("Should move horizontally first",
            new Point(2, 1), targetPos);
    }

    /**
     * Test (C): decideNextAction returns null when no heroes present.
     *
     * Scenario:
     * - Only mob on the level, no heroes
     *
     * Expected:
     * - Returns null (wait)
     */
    @Test
    public void testDecideNextAction_NoHeroes() {
        // Setup: Only mob, no heroes
        Point mobPos = new Point(3, 3);
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);

        context.addActor(mob);
        grid.setOccupied(mobPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions
        assertNull("Command should be null (no heroes to target)", cmd);
    }

    /**
     * Test (D): End-to-end - mob attacks hero via AI command.
     *
     * Scenario:
     * - Mob adjacent to hero
     * - Mob has high attack (50), hero has low HP (10)
     * - AI decides ATTACK, command executes, hero dies
     *
     * Expected:
     * - Mob's decideNextAction returns ATTACK
     * - Executing the command kills hero
     * - Events: DAMAGE_APPLIED, ACTOR_DIED
     */
    @Test
    public void testEndToEnd_MobAttacksHeroViaCommand() {
        // Setup: Strong mob, weak hero, adjacent
        Point mobPos = new Point(2, 2);
        Point heroPos = new Point(3, 2);

        // Mob with high attack (50)
        Stats strongMobStats = new Stats(50, 50, 0, 10, 10, 1.0f);
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos, strongMobStats);

        // Hero with low HP (10)
        Stats weakHeroStats = new Stats(10, 5, 0, 10, 10, 1.0f);
        TestHero hero = new TestHero(heroPos, weakHeroStats, "WeakHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Verify initial state
        assertTrue("Hero should be alive initially", hero.isAlive());

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Verify decision
        assertNotNull("Mob should decide an action", cmd);
        assertEquals("Should decide to ATTACK", CommandType.ATTACK, cmd.getType());
        assertEquals("Should attack the hero", hero.getId(), cmd.getTargetActorId());

        // Execute the command via GameEngine
        List<GameEvent> events = engine.tick(Collections.singletonList(cmd));

        // Assertions: Hero should be dead
        assertTrue("Hero should be dead after attack", hero.isDead());
        assertFalse("Hero should not be alive", hero.isAlive());
        assertTrue("Hero health should be <= 0", hero.getCurrentHealth() <= 0);

        // Verify events
        long damageEvents = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .count();
        assertEquals("Should have DAMAGE_APPLIED event", 1, damageEvents);

        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have ACTOR_DIED event", 1, diedEvents);

        // Verify event details
        GameEvent damageEvent = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .findFirst()
            .orElse(null);
        assertNotNull("DAMAGE_APPLIED event should exist", damageEvent);
        assertEquals("Damage from mob", mob.getId(), damageEvent.getSourceActorId());
        assertEquals("Damage to hero", hero.getId(), damageEvent.getTargetActorId());

        GameEvent diedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .findFirst()
            .orElse(null);
        assertNotNull("ACTOR_DIED event should exist", diedEvent);
        assertEquals("Hero died", hero.getId(), diedEvent.getSourceActorId());
    }

    /**
     * Test (E): Move blocked by wall - mob returns null.
     *
     * Scenario:
     * - Mob at (2, 2), Hero at (5, 2)
     * - Wall at (3, 2) blocking direct path
     *
     * Expected:
     * - Mob tries to move towards hero
     * - Target (3, 2) is not passable (wall)
     * - Returns null (can't move)
     */
    @Test
    public void testDecideNextAction_MoveBlockedByWall() {
        // Setup: Mob, hero, wall between them
        Point mobPos = new Point(2, 2);
        Point heroPos = new Point(5, 2);
        Point wallPos = new Point(3, 2);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Add wall blocking path
        grid.setTerrain(wallPos, TerrainType.WALL);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions: Can't move through wall
        assertNull("Command should be null (blocked by wall)", cmd);
    }

    /**
     * Test (E2): Move blocked by occupied tile - mob returns null.
     *
     * Scenario:
     * - Mob at (2, 2), Hero at (5, 2)
     * - Another mob at (3, 2) blocking direct path
     *
     * Expected:
     * - Returns null (target occupied)
     */
    @Test
    public void testDecideNextAction_MoveBlockedByOccupied() {
        // Setup: Mob, hero, another mob blocking path
        Point mobPos = new Point(2, 2);
        Point heroPos = new Point(5, 2);
        Point blockerPos = new Point(3, 2);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");
        SimpleMeleeMob blocker = new SimpleMeleeMob(blockerPos);

        context.addActor(mob);
        context.addActor(hero);
        context.addActor(blocker);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(blockerPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions: Can't move into occupied tile
        assertNull("Command should be null (blocked by another actor)", cmd);
    }

    /**
     * Test: Multiple heroes - mob targets nearest.
     *
     * Scenario:
     * - Mob at (3, 3)
     * - Hero1 at (5, 3) (distance = 2)
     * - Hero2 at (3, 6) (distance = 3)
     *
     * Expected:
     * - Mob moves towards Hero1 (nearest)
     */
    @Test
    public void testDecideNextAction_MultipleHeroes_TargetsNearest() {
        // Setup: Mob and two heroes
        Point mobPos = new Point(3, 3);
        Point hero1Pos = new Point(5, 3); // distance = 2
        Point hero2Pos = new Point(3, 6); // distance = 3

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero1 = new TestHero(hero1Pos, "Hero1");
        TestHero hero2 = new TestHero(hero2Pos, "Hero2");

        context.addActor(mob);
        context.addActor(hero1);
        context.addActor(hero2);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(hero1Pos, true);
        grid.setOccupied(hero2Pos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions: Should move towards Hero1 (nearest)
        assertNotNull("Command should not be null", cmd);
        assertEquals("Command type should be MOVE", CommandType.MOVE, cmd.getType());

        Point targetPos = cmd.getTargetPosition();
        assertNotNull("Target position should not be null", targetPos);

        // Should move towards Hero1 (right)
        assertEquals("Should move towards nearest hero (Hero1)",
            new Point(4, 3), targetPos);
    }

    /**
     * Test: Vertical movement when no horizontal distance.
     *
     * Scenario:
     * - Mob at (3, 3), Hero at (3, 6) (same column, vertical distance only)
     *
     * Expected:
     * - Moves vertically (down towards hero)
     */
    @Test
    public void testDecideNextAction_VerticalMovement() {
        // Setup: Mob and hero in same column
        Point mobPos = new Point(3, 3);
        Point heroPos = new Point(3, 6);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions: Should move vertically (down)
        assertNotNull("Command should not be null", cmd);
        assertEquals("Command type should be MOVE", CommandType.MOVE, cmd.getType());

        Point targetPos = cmd.getTargetPosition();
        assertNotNull("Target position should not be null", targetPos);

        // Should move down (y + 1)
        assertEquals("Should move vertically towards hero",
            new Point(3, 4), targetPos);
    }

    /**
     * Test: Mob with no position returns null.
     *
     * Scenario:
     * - Mob has null position
     *
     * Expected:
     * - Returns null (can't act without position)
     */
    @Test
    public void testDecideNextAction_MobNoPosition() {
        // Setup: Mob with no position (don't set position)
        SimpleMeleeMob mob = new SimpleMeleeMob(new Point(0, 0));
        mob.setPosition(null); // Clear position

        TestHero hero = new TestHero(new Point(3, 3), "TestHero");
        context.addActor(mob);
        context.addActor(hero);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Assertions
        assertNull("Command should be null (mob has no position)", cmd);
    }

    /**
     * Test: End-to-end - mob moves towards hero via AI command.
     *
     * Scenario:
     * - Mob at (1, 1), Hero at (4, 1)
     * - AI decides MOVE
     * - Command executes, mob position updated
     *
     * Expected:
     * - Mob's decideNextAction returns MOVE
     * - Executing the command moves mob to (2, 1)
     * - ACTOR_MOVED event emitted
     */
    @Test
    public void testEndToEnd_MobMovesViaCommand() {
        // Setup: Mob and hero distant
        Point mobPos = new Point(1, 1);
        Point heroPos = new Point(4, 1);

        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(mob);
        context.addActor(hero);
        grid.setOccupied(mobPos, true);
        grid.setOccupied(heroPos, true);

        // Action: Mob decides next action
        GameCommand cmd = mob.decideNextAction(context);

        // Verify decision
        assertNotNull("Mob should decide an action", cmd);
        assertEquals("Should decide to MOVE", CommandType.MOVE, cmd.getType());
        assertEquals("Should be (2, 1)", new Point(2, 1), cmd.getTargetPosition());

        // Execute the command via GameEngine
        List<GameEvent> events = engine.tick(Collections.singletonList(cmd));

        // Assertions: Mob should have moved
        assertEquals("Mob should be at new position",
            new Point(2, 1), mob.getPosition());

        // Verify ACTOR_MOVED event
        long movedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .count();
        assertEquals("Should have ACTOR_MOVED event", 1, movedEvents);

        GameEvent movedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_MOVED)
            .findFirst()
            .orElse(null);
        assertNotNull("ACTOR_MOVED event should exist", movedEvent);
        assertEquals("Moved actor is mob", mob.getId(), movedEvent.getSourceActorId());
        assertEquals("New position is (2, 1)", new Point(2, 1), movedEvent.getPosition());
    }
}
