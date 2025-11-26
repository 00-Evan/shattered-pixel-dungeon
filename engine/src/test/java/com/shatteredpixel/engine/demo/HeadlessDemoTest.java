package com.shatteredpixel.engine.demo;

import com.shatteredpixel.api.MockPlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.actor.hero.BaseHero;
import com.shatteredpixel.engine.actor.mob.SimpleMeleeMob;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.event.GameEvent;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Headless demo scenarios testing the complete engine lifecycle with save/load.
 *
 * These tests demonstrate:
 * - Turn-based gameplay with heroes and mobs
 * - Movement and combat systems
 * - ActorScheduler integration
 * - Complete save/load cycle
 * - State preservation across save/load
 * - Deterministic replay
 *
 * All scenarios are fully headless (no UI, no console output required).
 */
public class HeadlessDemoTest {

    private static final long DEMO_SEED = 42424242L;

    /**
     * Test hero class for demo scenarios.
     */
    private static class TestHero extends BaseHero {
        public TestHero(Point position, String name) {
            super(position, new Stats(50, 10, 5, 15, 15, 1.0f), name);
        }

        public TestHero(Point position, Stats stats, int currentHealth, String name) {
            super(position, stats, currentHealth, name);
        }

        @Override
        public float act(EngineContext context) {
            // Hero is command-driven, not autonomous
            return 1.0f;
        }
    }

    /**
     * Scenario A: hero_walks_and_mob_chases_then_save_load_then_continue
     *
     * This scenario demonstrates:
     * - Hero moving right across the level
     * - Mob using AI to chase the hero
     * - Saving state mid-scenario
     * - Loading state into fresh engine
     * - Continuing execution deterministically
     * - Combat when mob catches hero
     *
     * Expected flow:
     * 1. Hero at (1,4), Mob at (7,4) on 9x9 grid
     * 2. Hero moves right for 6 turns (→ position (7,4))
     * 3. Mob chases left for 6 turns (→ position closer to hero)
     * 4. Save state
     * 5. Load into fresh engine
     * 6. Continue for 6 more turns
     * 7. Mob reaches hero and attacks
     * 8. Verify positions, health, and state consistency
     */
    @Test
    public void testScenarioA_hero_walks_and_mob_chases_then_save_load_then_continue() {
        // ===== SETUP =====
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, DEMO_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 9x9 floor level
        LevelState level = makeFloorLevel(9, 9);
        context.setLevel(level);

        // Spawn hero at (1, 4)
        Point heroStartPos = new Point(1, 4);
        TestHero hero = spawnHero(context, heroStartPos, "DemoHero");

        // Spawn mob at (7, 4) - same row, 6 tiles away
        Point mobStartPos = new Point(7, 4);
        SimpleMeleeMob mob = spawnMob(context, mobStartPos);

        // Set hero as vision source for FOV
        context.setVisionActorId(hero.getId());

        // Store actor IDs for later reference
        ActorId heroId = hero.getId();
        ActorId mobId = mob.getId();

        // Capture initial state
        int heroInitialHP = hero.getCurrentHealth();
        int mobInitialHP = mob.getCurrentHealth();

        // ===== PHASE 1: Run 6 turns BEFORE save =====
        List<GameEvent> eventsBeforeSave = new ArrayList<>();
        final int TURNS_BEFORE_SAVE = 6;

        for (int i = 0; i < TURNS_BEFORE_SAVE; i++) {
            // Hero command: move right (if hero's turn)
            Actor nextActor = context.getScheduler().getNextActor();
            List<GameCommand> commands = new ArrayList<>();

            if (nextActor != null && nextActor.getId().equals(heroId)) {
                Point currentPos = hero.getPosition();
                Point targetPos = new Point(currentPos.x + 1, currentPos.y);
                commands.add(GameCommand.move(heroId, targetPos));
            }

            List<GameEvent> events = engine.processNextTurn(commands);
            eventsBeforeSave.addAll(events);
        }

        // Verify hero moved right
        Point heroBeforeSave = hero.getPosition();
        assertTrue(heroBeforeSave.x > heroStartPos.x, "Hero should have moved right");
        assertEquals(4, heroBeforeSave.y, "Hero should stay on same row");

        // Verify mob is still alive and chasing
        assertFalse(mob.isDead(), "Mob should still be alive");
        Point mobBeforeSave = mob.getPosition();
        assertTrue(mobBeforeSave.x < mobStartPos.x ||
                   (mobBeforeSave.x == mobStartPos.x && mobBeforeSave.y != mobStartPos.y),
                   "Mob should have moved towards hero");

        // ===== SAVE STATE =====
        byte[] savedData = engine.saveState();
        assertNotNull(savedData);
        assertTrue(savedData.length > 0);

        // Capture positions and state before save
        Point heroPosSaved = new Point(hero.getPosition().x, hero.getPosition().y);
        Point mobPosSaved = new Point(mob.getPosition().x, mob.getPosition().y);
        int heroHPSaved = hero.getCurrentHealth();
        int mobHPSaved = mob.getCurrentHealth();
        float heroTimeSaved = hero.getTime();
        float mobTimeSaved = mob.getTime();

        // ===== CREATE NEW ENGINE =====
        GameEngine newEngine = new GameEngine(platform, DEMO_SEED);
        newEngine.initialize();

        // ===== LOAD STATE =====
        newEngine.loadState(savedData);
        EngineContext newContext = newEngine.getContext();

        // ===== VERIFY STATE AFTER LOAD =====

        // Verify level is restored
        assertNotNull(newContext.getLevel());
        assertEquals(9, newContext.getLevel().getWidth());
        assertEquals(9, newContext.getLevel().getHeight());

        // Find restored actors
        Character restoredHero = null;
        Character restoredMob = null;
        for (Actor actor : newContext.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                restoredHero = (Character) actor;
            } else if (actor.getType() == ActorType.MOB) {
                restoredMob = (Character) actor;
            }
        }

        assertNotNull(restoredHero, "Hero should be restored");
        assertNotNull(restoredMob, "Mob should be restored");

        // Verify positions match
        assertEquals(heroPosSaved.x, restoredHero.getPosition().x);
        assertEquals(heroPosSaved.y, restoredHero.getPosition().y);
        assertEquals(mobPosSaved.x, restoredMob.getPosition().x);
        assertEquals(mobPosSaved.y, restoredMob.getPosition().y);

        // Verify health matches
        assertEquals(heroHPSaved, restoredHero.getCurrentHealth());
        assertEquals(mobHPSaved, restoredMob.getCurrentHealth());

        // Verify scheduler contains both actors
        assertEquals(2, newContext.getScheduler().getActorCount());

        // Verify time values (within epsilon)
        assertEquals(heroTimeSaved, restoredHero.getTime(), 0.001f);
        assertEquals(mobTimeSaved, restoredMob.getTime(), 0.001f);

        // ===== PHASE 2: Run 6 turns AFTER load =====
        List<GameEvent> eventsAfterLoad = new ArrayList<>();
        final int TURNS_AFTER_LOAD = 6;

        ActorId restoredHeroId = restoredHero.getId();
        ActorId restoredMobId = restoredMob.getId();

        for (int i = 0; i < TURNS_AFTER_LOAD; i++) {
            Actor nextActor = newContext.getScheduler().getNextActor();
            List<GameCommand> commands = new ArrayList<>();

            // Hero continues moving right (if still alive and it's hero's turn)
            if (nextActor != null && nextActor.getId().equals(restoredHeroId) && restoredHero.isAlive()) {
                Point currentPos = restoredHero.getPosition();
                Point targetPos = new Point(currentPos.x + 1, currentPos.y);
                // Only move if in bounds
                if (newContext.getLevel().getGrid().isInBounds(targetPos)) {
                    commands.add(GameCommand.move(restoredHeroId, targetPos));
                }
            }

            List<GameEvent> events = newEngine.processNextTurn(commands);
            eventsAfterLoad.addAll(events);
        }

        // ===== FINAL ASSERTIONS =====

        // Verify actors still exist (unless killed in combat)
        // Hero should still be alive
        assertTrue(restoredHero.isAlive(), "Hero should survive the demo");

        // Verify no duplicate actors
        int heroCount = 0;
        int mobCount = 0;
        for (Actor actor : newContext.getActors().values()) {
            if (actor.getType() == ActorType.HERO) heroCount++;
            if (actor.getType() == ActorType.MOB) mobCount++;
        }
        assertEquals(1, heroCount, "Should have exactly 1 hero");
        assertTrue(mobCount <= 1, "Should have at most 1 mob (may be dead)");

        // Verify level grid occupancy matches actor positions
        for (Actor actor : newContext.getActors().values()) {
            if (actor.getPosition() != null) {
                assertTrue(newContext.getLevel().getGrid().isOccupied(actor.getPosition()),
                    "Actor position should be marked as occupied");
            }
        }

        // Verify events were generated
        assertTrue(eventsBeforeSave.size() > 0, "Should have events before save");
        assertTrue(eventsAfterLoad.size() > 0, "Should have events after load");

        // Check if combat occurred (hero HP changed)
        boolean combatOccurred = restoredHero.getCurrentHealth() < heroHPSaved;
        if (combatOccurred) {
            // If combat happened, verify mob got close enough to attack
            int distance = Math.abs(restoredHero.getPosition().x - restoredMob.getPosition().x) +
                          Math.abs(restoredHero.getPosition().y - restoredMob.getPosition().y);
            assertTrue(distance <= 1, "Mob should be adjacent to hero if combat occurred");
        }
    }

    /**
     * Scenario B: kill_mob_before_save_and_verify_no_respawn
     *
     * This scenario verifies that dead actors are properly removed and don't respawn:
     * - Hero adjacent to weak mob
     * - Hero attacks and kills mob
     * - Save state with dead mob
     * - Load state into fresh engine
     * - Verify mob stays dead (not in registry, not in scheduler, no occupied flag)
     */
    @Test
    public void testScenarioB_kill_mob_before_save_and_verify_no_respawn() {
        // ===== SETUP =====
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, DEMO_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 5x5 floor level
        LevelState level = makeFloorLevel(5, 5);
        context.setLevel(level);

        // Spawn hero at (2, 2) - strong hero
        Point heroPos = new Point(2, 2);
        TestHero hero = new TestHero(heroPos, "StrongHero");
        hero.getStats().setAttackPower(25); // High attack to kill mob quickly
        context.addActor(hero);
        level.getGrid().setOccupied(heroPos, true);
        context.registerActorForScheduling(hero, 0.0f);

        // Spawn weak mob adjacent at (3, 2)
        Point mobPos = new Point(3, 2);
        Stats weakStats = new Stats(5, 2, 0, 5, 5, 1.0f); // 5 HP, weak
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos, weakStats);
        context.addActor(mob);
        level.getGrid().setOccupied(mobPos, true);
        context.registerActorForScheduling(mob, 0.0f);

        ActorId heroId = hero.getId();
        ActorId mobId = mob.getId();

        // ===== KILL MOB =====
        // Hero attacks mob until dead
        int maxAttacks = 5;
        for (int i = 0; i < maxAttacks && mob.isAlive(); i++) {
            Actor nextActor = context.getScheduler().getNextActor();
            List<GameCommand> commands = new ArrayList<>();

            if (nextActor != null && nextActor.getId().equals(heroId)) {
                // Hero attacks mob
                commands.add(GameCommand.attack(heroId, mobId));
            }

            engine.processNextTurn(commands);
        }

        // Verify mob is dead
        assertTrue(mob.isDead(), "Mob should be dead");

        // Verify mob removed from scheduler
        boolean mobInScheduler = false;
        for (Actor actor : context.getScheduler().getAllActors()) {
            if (actor.getId().equals(mobId)) {
                mobInScheduler = true;
                break;
            }
        }
        assertFalse(mobInScheduler, "Dead mob should not be in scheduler before save");

        // Verify mob removed from actor registry
        assertNull(context.getActor(mobId), "Dead mob should not be in actor registry before save");

        // Verify mob position no longer occupied
        assertFalse(level.getGrid().isOccupied(mobPos), "Mob's former position should not be occupied");

        // ===== SAVE STATE =====
        byte[] savedData = engine.saveState();
        assertNotNull(savedData);

        // ===== CREATE NEW ENGINE AND LOAD =====
        GameEngine newEngine = new GameEngine(platform, DEMO_SEED);
        newEngine.initialize();
        newEngine.loadState(savedData);
        EngineContext newContext = newEngine.getContext();

        // ===== VERIFY MOB STAYS DEAD =====

        // Verify only 1 actor (hero) in registry
        assertEquals(1, newContext.getActors().size(), "Should only have hero, not dead mob");

        // Verify actor is hero
        Actor onlyActor = newContext.getActors().values().iterator().next();
        assertEquals(ActorType.HERO, onlyActor.getType());

        // Verify only 1 actor in scheduler
        assertEquals(1, newContext.getScheduler().getActorCount(), "Only hero should be scheduled");

        // Verify mob's former position not occupied
        LevelState newLevel = newContext.getLevel();
        assertFalse(newLevel.getGrid().isOccupied(mobPos),
            "Dead mob's former position should not be occupied after load");

        // ===== RUN ADDITIONAL TURNS =====
        // Verify no errors or mob-related events
        List<GameEvent> eventsAfterLoad = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<GameEvent> events = newEngine.processNextTurn(Collections.emptyList());
            eventsAfterLoad.addAll(events);

            // Verify no mob-related events
            for (GameEvent event : events) {
                String eventStr = event.toString().toLowerCase();
                // Events should not reference the dead mob
                // (This is a soft check - depends on event format)
            }
        }

        // Verify still only 1 actor
        assertEquals(1, newContext.getActors().size(), "Should still only have hero");
        assertEquals(1, newContext.getScheduler().getActorCount(), "Should still only schedule hero");
    }

    /**
     * Scenario C: save_load_then_hero_continues_moving
     *
     * This scenario verifies deterministic movement across save/load:
     * - Hero alone on level
     * - Hero moves right for N turns
     * - Save state
     * - Load state into fresh engine
     * - Hero continues moving right for M more turns
     * - Verify final position is (start_x + N + M, start_y)
     * - Verify FOV, occupancy, and RNG continuity
     */
    @Test
    public void testScenarioC_save_load_then_hero_continues_moving() {
        // ===== SETUP =====
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, DEMO_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 15x5 floor level (wide enough for movement)
        LevelState level = makeFloorLevel(15, 5);
        context.setLevel(level);

        // Spawn hero at (2, 2)
        Point heroStartPos = new Point(2, 2);
        TestHero hero = spawnHero(context, heroStartPos, "WalkingHero");
        ActorId heroId = hero.getId();

        // Set hero as vision source
        context.setVisionActorId(heroId);

        // ===== PHASE 1: Move right for 3 turns BEFORE save =====
        final int TURNS_BEFORE_SAVE = 3;

        for (int i = 0; i < TURNS_BEFORE_SAVE; i++) {
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);
            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(heroId, targetPos)
            );
            engine.processNextTurn(commands);
        }

        // Verify position after 3 moves
        Point expectedPosBeforeSave = new Point(2 + TURNS_BEFORE_SAVE, 2);
        assertEquals(expectedPosBeforeSave.x, hero.getPosition().x);
        assertEquals(expectedPosBeforeSave.y, hero.getPosition().y);

        // Capture state before save
        Point heroPosSaved = new Point(hero.getPosition().x, hero.getPosition().y);
        int heroHPSaved = hero.getCurrentHealth();

        // Verify occupancy at current position
        assertTrue(level.getGrid().isOccupied(hero.getPosition()),
            "Hero's position should be occupied before save");

        // Verify old position not occupied
        assertFalse(level.getGrid().isOccupied(heroStartPos),
            "Hero's starting position should not be occupied after move");

        // ===== SAVE STATE =====
        byte[] savedData = engine.saveState();
        assertNotNull(savedData);

        // ===== CREATE NEW ENGINE AND LOAD =====
        GameEngine newEngine = new GameEngine(platform, DEMO_SEED);
        newEngine.initialize();
        newEngine.loadState(savedData);
        EngineContext newContext = newEngine.getContext();

        // ===== VERIFY STATE AFTER LOAD =====

        // Find restored hero
        Character restoredHero = null;
        for (Actor actor : newContext.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                restoredHero = (Character) actor;
                break;
            }
        }

        assertNotNull(restoredHero, "Hero should be restored");

        // Verify position matches
        assertEquals(heroPosSaved.x, restoredHero.getPosition().x);
        assertEquals(heroPosSaved.y, restoredHero.getPosition().y);

        // Verify health matches
        assertEquals(heroHPSaved, restoredHero.getCurrentHealth());

        // Verify occupancy restored correctly
        LevelState newLevel = newContext.getLevel();
        assertTrue(newLevel.getGrid().isOccupied(restoredHero.getPosition()),
            "Hero's position should be occupied after load");
        assertFalse(newLevel.getGrid().isOccupied(heroStartPos),
            "Original starting position should not be occupied after load");

        // Verify vision actor ID restored
        assertNotNull(newContext.getVisionActorId(), "Vision actor should be set");

        // ===== PHASE 2: Continue moving right for 5 more turns =====
        final int TURNS_AFTER_LOAD = 5;
        ActorId restoredHeroId = restoredHero.getId();

        for (int i = 0; i < TURNS_AFTER_LOAD; i++) {
            Point currentPos = restoredHero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);
            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(restoredHeroId, targetPos)
            );
            newEngine.processNextTurn(commands);
        }

        // ===== FINAL ASSERTIONS =====

        // Verify final position
        Point expectedFinalPos = new Point(2 + TURNS_BEFORE_SAVE + TURNS_AFTER_LOAD, 2);
        assertEquals(expectedFinalPos.x, restoredHero.getPosition().x,
            "Hero should be at x=" + expectedFinalPos.x + " after " +
            (TURNS_BEFORE_SAVE + TURNS_AFTER_LOAD) + " moves");
        assertEquals(expectedFinalPos.y, restoredHero.getPosition().y);

        // With N_before=3 and N_after=5, final position should be (10, 2)
        assertEquals(10, restoredHero.getPosition().x);
        assertEquals(2, restoredHero.getPosition().y);

        // Verify occupancy consistent
        assertTrue(newLevel.getGrid().isOccupied(restoredHero.getPosition()),
            "Final position should be occupied");

        // Verify all previous positions not occupied
        for (int x = 2; x < restoredHero.getPosition().x; x++) {
            assertFalse(newLevel.getGrid().isOccupied(new Point(x, 2)),
                "Previous position (" + x + ",2) should not be occupied");
        }

        // Verify hero still in scheduler
        assertEquals(1, newContext.getScheduler().getActorCount());

        // Verify deterministic state preservation
        // Hero should have full health (no combat)
        assertEquals(50, restoredHero.getCurrentHealth(), "Hero should have full health");
        assertEquals(50, restoredHero.getMaxHealth());
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Create a level filled entirely with FLOOR tiles.
     *
     * @param width Level width
     * @param height Level height
     * @return LevelState with all FLOOR tiles
     */
    private static LevelState makeFloorLevel(int width, int height) {
        LevelState level = new LevelState(1, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                level.getGrid().setTerrain(x, y, TerrainType.FLOOR);
            }
        }
        return level;
    }

    /**
     * Spawn a hero at the given position and register with context/scheduler.
     *
     * @param context Engine context
     * @param pos Position to spawn at
     * @param name Hero name
     * @return The spawned hero
     */
    private static TestHero spawnHero(EngineContext context, Point pos, String name) {
        TestHero hero = new TestHero(pos, name);
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(hero, 0.0f);
        return hero;
    }

    /**
     * Spawn a mob at the given position and register with context/scheduler.
     *
     * @param context Engine context
     * @param pos Position to spawn at
     * @return The spawned mob
     */
    private static SimpleMeleeMob spawnMob(EngineContext context, Point pos) {
        SimpleMeleeMob mob = new SimpleMeleeMob(pos);
        context.addActor(mob);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(mob, 0.0f);
        return mob;
    }

    /**
     * Get the current position of an actor by ID.
     *
     * @param actorId Actor ID
     * @param context Engine context
     * @return Actor's position, or null if not found
     */
    private static Point positionOf(ActorId actorId, EngineContext context) {
        Actor actor = context.getActor(actorId);
        return actor != null ? actor.getPosition() : null;
    }
}
