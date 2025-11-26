package com.shatteredpixel.engine.actor;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.hero.BaseHero;
import com.shatteredpixel.engine.actor.mob.SimpleMeleeMob;
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
 * Integration tests for ActorScheduler with turn-based gameplay.
 *
 * Validates that:
 * - Actors are scheduled and take turns in correct order
 * - Heroes are driven by external commands
 * - Mobs are driven by AI (decideNextAction)
 * - Dead actors are removed from scheduler
 * - Multiple actors can coexist and interact
 */
public class ActorSchedulerIntegrationTest {

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
            // Default: 50 HP, 5 attack
            this(position, new Stats(50, 5, 0, 10, 10, 1.0f), name);
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
     * Test (A): Hero waits while mob approaches and attacks.
     *
     * Scenario:
     * - Hero at (1, 3), Mob at (7, 3) (distance = 6)
     * - Hero provided with no external commands (waits each turn)
     * - Mob AI moves closer each turn, then attacks
     *
     * Expected:
     * - Turns alternate between hero and mob
     * - Mob gradually approaches hero
     * - When adjacent, mob attacks hero
     * - Hero health decreases after attack
     */
    @Test
    public void testSingleHeroWaitsAndMobApproachesAndAttacks() {
        // Setup: Hero and mob distant
        Point heroPos = new Point(1, 3);
        Point mobPos = new Point(7, 3);

        TestHero hero = new TestHero(heroPos, "TestHero");
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);

        context.addActor(hero);
        context.addActor(mob);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mobPos, true);

        // Register both with scheduler (initial time = 0)
        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob, 0.0f);

        int initialHeroHealth = hero.getCurrentHealth();
        Point initialMobPos = mob.getPosition();

        // Process several turns (hero waits, mob approaches)
        int turnsProcessed = 0;
        int maxTurns = 20;

        for (int i = 0; i < maxTurns; i++) {
            // No hero commands (hero waits)
            List<GameEvent> events = engine.processNextTurn(Collections.emptyList());
            turnsProcessed++;

            // If hero died, stop
            if (hero.isDead()) {
                break;
            }

            // Check if mob attacked
            long attackEvents = events.stream()
                .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
                .filter(e -> e.getTargetActorId() != null && e.getTargetActorId().equals(hero.getId()))
                .count();

            if (attackEvents > 0) {
                // Mob attacked! Verify conditions
                assertTrue("Hero should have taken damage",
                    hero.getCurrentHealth() < initialHeroHealth);

                // Mob should be adjacent to hero
                Point mobCurrentPos = mob.getPosition();
                int distance = Math.abs(mobCurrentPos.x - heroPos.x) +
                              Math.abs(mobCurrentPos.y - heroPos.y);
                assertEquals("Mob should be adjacent when attacking", 1, distance);

                // Test passed - mob approached and attacked
                return;
            }
        }

        // After max turns, mob should have moved closer or attacked
        Point finalMobPos = mob.getPosition();
        int initialDistance = Math.abs(initialMobPos.x - heroPos.x) +
                             Math.abs(initialMobPos.y - heroPos.y);
        int finalDistance = Math.abs(finalMobPos.x - heroPos.x) +
                           Math.abs(finalMobPos.y - heroPos.y);

        assertTrue("Mob should have moved closer to hero",
            finalDistance < initialDistance);
        assertTrue("Should have processed multiple turns", turnsProcessed > 0);
    }

    /**
     * Test (B): Dead actor removed from scheduler.
     *
     * Scenario:
     * - Hero and mob adjacent
     * - Hero has high attack (50), mob has low HP (10)
     * - Hero attacks and kills mob
     *
     * Expected:
     * - Mob dies (isDead() == true)
     * - ACTOR_DIED event emitted
     * - Mob removed from actor registry
     * - Subsequent turns don't include mob
     */
    @Test
    public void testDeadActorRemovedFromScheduler() {
        // Setup: Strong hero, weak mob, adjacent
        Point heroPos = new Point(3, 3);
        Point mobPos = new Point(4, 3);

        Stats strongHeroStats = new Stats(100, 50, 0, 10, 10, 1.0f);
        TestHero hero = new TestHero(heroPos, strongHeroStats, "StrongHero");

        Stats weakMobStats = new Stats(10, 5, 0, 10, 10, 1.0f);
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos, weakMobStats);

        context.addActor(hero);
        context.addActor(mob);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mobPos, true);

        // Register both with scheduler
        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob, 0.0f);

        // Get initial scheduler state
        int initialActorCount = context.getScheduler().getActorCount();
        assertEquals("Should have 2 actors initially", 2, initialActorCount);

        // Hero's turn: attack mob
        GameCommand attackCommand = GameCommand.attack(hero.getId(), mob.getId());
        List<GameEvent> events = engine.processNextTurn(
            Collections.singletonList(attackCommand)
        );

        // Assertions: Mob should be dead
        assertTrue("Mob should be dead", mob.isDead());

        // Check ACTOR_DIED event
        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have ACTOR_DIED event", 1, diedEvents);

        // Mob should be removed from actor registry
        assertNull("Mob should be removed from registry",
            context.getActor(mob.getId()));

        // Mob should be removed from scheduler
        assertFalse("Mob should not be in scheduler",
            context.getScheduler().hasActor(mob.getId()));
        assertEquals("Scheduler should have 1 actor (hero only)", 1,
            context.getScheduler().getActorCount());

        // Process several more turns - mob should never act again
        for (int i = 0; i < 5; i++) {
            Actor nextActor = context.getScheduler().getNextActor();
            assertNotNull("Should have next actor (hero)", nextActor);
            assertEquals("Only hero should be scheduled",
                hero.getId(), nextActor.getId());

            // Process turn
            engine.processNextTurn(Collections.emptyList());
        }
    }

    /**
     * Test (C): Multiple mobs take turns.
     *
     * Scenario:
     * - One hero, two mobs at different positions
     * - All registered with scheduler
     * - Hero waits (no commands)
     *
     * Expected:
     * - All actors get turns (hero and both mobs)
     * - Both mobs move towards hero via AI
     * - Turn order is fair (no actor starved)
     */
    @Test
    public void testMultipleMobsTakeTurns() {
        // Setup: Hero and two mobs
        Point heroPos = new Point(4, 4);
        Point mob1Pos = new Point(1, 4);
        Point mob2Pos = new Point(7, 4);

        TestHero hero = new TestHero(heroPos, "TestHero");
        SimpleMeleeMob mob1 = new SimpleMeleeMob(mob1Pos);
        SimpleMeleeMob mob2 = new SimpleMeleeMob(mob2Pos);

        context.addActor(hero);
        context.addActor(mob1);
        context.addActor(mob2);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mob1Pos, true);
        grid.setOccupied(mob2Pos, true);

        // Register all with scheduler
        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob1, 0.0f);
        context.registerActorForScheduling(mob2, 0.0f);

        assertEquals("Should have 3 actors", 3,
            context.getScheduler().getActorCount());

        // Track which actors got turns
        java.util.Set<ActorId> actorsThatActed = new java.util.HashSet<>();

        // Process 10 turns
        for (int i = 0; i < 10; i++) {
            Actor nextActor = context.getScheduler().getNextActor();
            assertNotNull("Should have next actor", nextActor);
            actorsThatActed.add(nextActor.getId());

            // Process turn (hero waits, mobs use AI)
            engine.processNextTurn(Collections.emptyList());
        }

        // Verify all actors got at least one turn
        assertTrue("Hero should have acted",
            actorsThatActed.contains(hero.getId()));
        assertTrue("Mob1 should have acted",
            actorsThatActed.contains(mob1.getId()));
        assertTrue("Mob2 should have acted",
            actorsThatActed.contains(mob2.getId()));

        // Both mobs should have moved closer to hero
        Point finalMob1Pos = mob1.getPosition();
        Point finalMob2Pos = mob2.getPosition();

        int initialDist1 = Math.abs(mob1Pos.x - heroPos.x);
        int finalDist1 = Math.abs(finalMob1Pos.x - heroPos.x);
        assertTrue("Mob1 should have moved closer",
            finalDist1 < initialDist1);

        int initialDist2 = Math.abs(mob2Pos.x - heroPos.x);
        int finalDist2 = Math.abs(finalMob2Pos.x - heroPos.x);
        assertTrue("Mob2 should have moved closer",
            finalDist2 < initialDist2);
    }

    /**
     * Test (D): No scheduled actors.
     *
     * Scenario:
     * - Empty scheduler (no actors registered)
     *
     * Expected:
     * - processNextTurn returns empty list
     * - No crashes or exceptions
     */
    @Test
    public void testNoScheduledActors() {
        // Setup: Empty scheduler (don't register any actors)
        assertEquals("Scheduler should be empty", 0,
            context.getScheduler().getActorCount());

        // Action: Process turn with empty scheduler
        List<GameEvent> events = engine.processNextTurn(Collections.emptyList());

        // Assertions
        assertNotNull("Events should not be null", events);
        assertEquals("Events should be empty", 0, events.size());

        // Should not throw exceptions
        // Process multiple times to verify stability
        for (int i = 0; i < 5; i++) {
            events = engine.processNextTurn(Collections.emptyList());
            assertNotNull("Events should not be null", events);
            assertEquals("Events should be empty", 0, events.size());
        }
    }

    /**
     * Test: Hero acts on their turn with external command.
     *
     * Scenario:
     * - Hero and mob registered
     * - On hero's turn, provide MOVE command
     *
     * Expected:
     * - Hero executes MOVE command
     * - Hero position updated
     * - ACTOR_MOVED event emitted
     */
    @Test
    public void testHeroExecutesExternalCommand() {
        // Setup: Hero and mob
        Point heroPos = new Point(2, 2);
        Point mobPos = new Point(5, 2);

        TestHero hero = new TestHero(heroPos, "TestHero");
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);

        context.addActor(hero);
        context.addActor(mob);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mobPos, true);

        // Register with scheduler
        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob, 0.0f);

        // First turn should be hero (both start at time 0, deterministic order)
        Actor nextActor = context.getScheduler().getNextActor();

        // Find which actor acts first and process accordingly
        if (nextActor.getId().equals(hero.getId())) {
            // Hero's turn: provide MOVE command
            Point targetPos = new Point(3, 2);
            GameCommand moveCommand = GameCommand.moveTo(hero.getId(), targetPos);
            List<GameEvent> events = engine.processNextTurn(
                Collections.singletonList(moveCommand)
            );

            // Verify hero moved
            assertEquals("Hero should be at new position",
                targetPos, hero.getPosition());

            // Check ACTOR_MOVED event
            long movedEvents = events.stream()
                .filter(e -> e.getType() == EventType.ACTOR_MOVED)
                .filter(e -> e.getSourceActorId().equals(hero.getId()))
                .count();
            assertEquals("Should have ACTOR_MOVED event for hero", 1, movedEvents);
        } else {
            // Mob acted first, process again for hero
            engine.processNextTurn(Collections.emptyList()); // Mob's turn

            // Now hero's turn
            Point targetPos = new Point(3, 2);
            GameCommand moveCommand = GameCommand.moveTo(hero.getId(), targetPos);
            List<GameEvent> events = engine.processNextTurn(
                Collections.singletonList(moveCommand)
            );

            // Verify hero moved
            assertEquals("Hero should be at new position",
                targetPos, hero.getPosition());
        }
    }

    /**
     * Test: Time normalization works correctly.
     *
     * Scenario:
     * - Process many turns to ensure time values don't overflow
     *
     * Expected:
     * - Time values stay reasonable (normalized)
     * - Actors continue to take turns correctly
     */
    @Test
    public void testTimeNormalization() {
        // Setup: Simple hero
        Point heroPos = new Point(4, 4);
        TestHero hero = new TestHero(heroPos, "TestHero");

        context.addActor(hero);
        grid.setOccupied(heroPos, true);
        context.registerActorForScheduling(hero, 0.0f);

        // Process many turns
        for (int i = 0; i < 100; i++) {
            engine.processNextTurn(Collections.emptyList());

            // Verify time stays reasonable (not overflowing to huge values)
            float heroTime = hero.getTime();
            assertTrue("Hero time should be reasonable (< 1000)",
                heroTime < 1000.0f);
        }

        // Hero should still be acting normally
        Actor nextActor = context.getScheduler().getNextActor();
        assertEquals("Hero should still be next", hero.getId(), nextActor.getId());
    }
}
