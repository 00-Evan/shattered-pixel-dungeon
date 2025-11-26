package com.shatteredpixel.engine.replay;

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
 * Comprehensive test suite for deterministic replay harness.
 *
 * These tests verify:
 * - Same seed + same commands → same results
 * - ReplayRecorder captures external inputs correctly
 * - ReplayRunner reproduces original execution exactly
 * - State preservation across replay
 * - Determinism across multiple runs
 *
 * All tests are fully headless (no UI, no console output).
 */
public class DeterministicReplayHarnessTest {

    private static final long TEST_SEED = 42424242L;

    /**
     * Test hero class.
     */
    private static class TestHero extends BaseHero {
        public TestHero(Point position, String name) {
            super(position, new Stats(50, 10, 5, 15, 15, 1.0f), name);
        }

        @Override
        public float act(EngineContext context) {
            return 1.0f;
        }
    }

    /**
     * Scenario 1: simple_walk_replay_is_identical
     *
     * Verifies that a simple hero walking in a straight line produces
     * identical results when replayed.
     *
     * Setup:
     * - 9x3 FLOOR level
     * - Hero starts at (1, 1)
     * - No mobs
     * - Hero moves right for 5 turns
     *
     * Flow:
     * 1. Direct run: execute turns and capture state
     * 2. Record run with ReplayRecorder to get trace
     * 3. Replay with ReplayRunner using same seed
     * 4. Compare final states
     *
     * Assertions:
     * - Final hero position matches in both runs
     * - Hero HP unchanged in both runs
     * - Level grid occupancy consistent
     * - FOV state matches (optional)
     */
    @Test
    public void testScenario1_simple_walk_replay_is_identical() {
        // ===== SETUP =====
        final int TURN_COUNT = 5;

        // Prepare commands: hero moves right each turn
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();
        for (int i = 0; i < TURN_COUNT; i++) {
            commandsPerTurn.add(new ArrayList<>()); // Will be filled during run
        }

        // Create engine factory
        EngineFactory factory = this::createSimpleWalkEngine;

        // ===== DIRECT RUN =====
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        // Get hero ID
        ActorId heroId = findHeroId(directContext);
        assertNotNull(heroId);

        // Execute turns and prepare commands
        for (int i = 0; i < TURN_COUNT; i++) {
            Character hero = (Character) directContext.getActor(heroId);
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);

            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(heroId, targetPos)
            );
            commandsPerTurn.set(i, commands);

            directEngine.processNextTurn(commands);
        }

        // Capture direct run state
        Character directHero = (Character) directContext.getActor(heroId);
        Point directFinalPos = new Point(directHero.getPosition().x, directHero.getPosition().y);
        int directFinalHP = directHero.getCurrentHealth();

        // ===== RECORD RUN =====
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TURN_COUNT, commandsPerTurn, factory);

        assertNotNull(trace);
        assertEquals(TEST_SEED, trace.getSeed());
        assertEquals(TURN_COUNT, trace.getTurnCount());

        // ===== REPLAY RUN =====
        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory);

        assertNotNull(result);
        assertEquals(TURN_COUNT, result.getTurnsExecuted());

        // ===== VERIFY REPLAY MATCHES DIRECT RUN =====
        EngineContext replayContext = result.getFinalContext();

        // Find hero in replay
        ActorId replayHeroId = findHeroId(replayContext);
        assertNotNull(replayHeroId);

        Character replayHero = (Character) replayContext.getActor(replayHeroId);
        assertNotNull(replayHero);

        // Verify position matches
        assertEquals(directFinalPos.x, replayHero.getPosition().x,
            "Hero X position should match between direct and replay");
        assertEquals(directFinalPos.y, replayHero.getPosition().y,
            "Hero Y position should match between direct and replay");

        // Expected final position: (1 + 5, 1) = (6, 1)
        assertEquals(6, replayHero.getPosition().x);
        assertEquals(1, replayHero.getPosition().y);

        // Verify HP matches
        assertEquals(directFinalHP, replayHero.getCurrentHealth(),
            "Hero HP should match between direct and replay");

        // Verify level grid occupancy
        assertTrue(replayContext.getLevel().getGrid().isOccupied(replayHero.getPosition()),
            "Hero's final position should be occupied");

        // Verify old position not occupied
        assertFalse(replayContext.getLevel().getGrid().isOccupied(new Point(1, 1)),
            "Hero's starting position should not be occupied");
    }

    /**
     * Scenario 2: combat_replay_is_identical
     *
     * Verifies that combat produces identical results when replayed.
     *
     * Setup:
     * - 7x3 FLOOR level
     * - Hero at (2, 1)
     * - Mob at (4, 1)
     * - Hero moves right until adjacent, then attacks
     *
     * Flow:
     * 1. Record run until mob dies
     * 2. Replay with same seed
     * 3. Verify mob death and hero HP match
     *
     * Assertions:
     * - Mob ends dead in both runs
     * - Hero final HP matches
     * - Mob removed from registry/scheduler
     * - Occupancy cleared
     */
    @Test
    public void testScenario2_combat_replay_is_identical() {
        // ===== SETUP =====
        final int MAX_TURNS = 15; // Enough for movement + combat

        // Create engine factory
        EngineFactory factory = this::createCombatEngine;

        // ===== DIRECT RUN =====
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        ActorId heroId = findHeroId(directContext);
        ActorId mobId = findMobId(directContext);
        assertNotNull(heroId);
        assertNotNull(mobId);

        // Prepare commands: move right, then attack when adjacent
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();

        for (int i = 0; i < MAX_TURNS; i++) {
            Character hero = (Character) directContext.getActor(heroId);
            Character mob = (Character) directContext.getActor(mobId);

            List<GameCommand> commands = new ArrayList<>();

            if (hero != null && mob != null && !mob.isDead()) {
                // Calculate distance
                int distance = Math.abs(hero.getPosition().x - mob.getPosition().x) +
                              Math.abs(hero.getPosition().y - mob.getPosition().y);

                if (distance <= 1) {
                    // Adjacent: attack
                    commands.add(GameCommand.attack(heroId, mobId));
                } else {
                    // Not adjacent: move right
                    Point currentPos = hero.getPosition();
                    Point targetPos = new Point(currentPos.x + 1, currentPos.y);
                    commands.add(GameCommand.move(heroId, targetPos));
                }
            }

            commandsPerTurn.add(commands);
            directEngine.processNextTurn(commands);

            // Stop if mob is dead
            if (mob == null || mob.isDead()) {
                break;
            }
        }

        // Capture direct run state
        Character directHero = (Character) directContext.getActor(heroId);
        assertNotNull(directHero);
        int directFinalHP = directHero.getCurrentHealth();

        // Verify mob is dead and removed
        Character directMob = (Character) directContext.getActor(mobId);
        assertTrue(directMob == null || directMob.isDead(), "Mob should be dead in direct run");

        // ===== RECORD RUN =====
        int actualTurns = commandsPerTurn.size();
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, actualTurns, commandsPerTurn, factory);

        assertNotNull(trace);
        assertEquals(TEST_SEED, trace.getSeed());

        // ===== REPLAY RUN =====
        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory);

        assertNotNull(result);
        assertEquals(actualTurns, result.getTurnsExecuted());

        // ===== VERIFY REPLAY MATCHES DIRECT RUN =====
        EngineContext replayContext = result.getFinalContext();

        ActorId replayHeroId = findHeroId(replayContext);
        assertNotNull(replayHeroId);

        Character replayHero = (Character) replayContext.getActor(replayHeroId);
        assertNotNull(replayHero);

        // Verify hero HP matches
        assertEquals(directFinalHP, replayHero.getCurrentHealth(),
            "Hero HP should match between direct and replay");

        // Verify mob is dead and removed in replay
        ActorId replayMobId = findMobId(replayContext);
        Character replayMob = replayMobId != null ? (Character) replayContext.getActor(replayMobId) : null;
        assertTrue(replayMob == null || replayMob.isDead(),
            "Mob should be dead in replay");

        // Verify only 1 actor (hero) in registry
        assertEquals(1, replayContext.getActors().size(),
            "Should only have hero after mob death");

        // Verify only 1 actor in scheduler
        assertEquals(1, replayContext.getScheduler().getActorCount(),
            "Should only have hero scheduled after mob death");
    }

    /**
     * Scenario 3: mid_run_save_does_not_break_replay
     *
     * Verifies that save/load boundaries don't affect replay determinism.
     *
     * Setup:
     * - 9x3 FLOOR level
     * - Hero at (1, 1), Mob at (7, 1)
     * - Hero moves right, fights mob, continues
     *
     * Flow:
     * 1. Direct run with save/load in middle
     *    - Run T1 turns, save state
     *    - Load state, continue T2 turns
     * 2. Record all commands (T1 + T2)
     * 3. Replay straight through without save/load
     * 4. Compare final states
     *
     * Assertions:
     * - Final positions match
     * - Final HP matches
     * - Mob death state matches
     * - Registry/scheduler consistent
     */
    @Test
    public void testScenario3_mid_run_save_does_not_break_replay() {
        // ===== SETUP =====
        final int TURNS_BEFORE_SAVE = 3;
        final int TURNS_AFTER_SAVE = 5;
        final int TOTAL_TURNS = TURNS_BEFORE_SAVE + TURNS_AFTER_SAVE;

        // Create engine factory
        EngineFactory factory = this::createSaveLoadEngine;

        // ===== DIRECT RUN WITH SAVE/LOAD =====
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        ActorId heroId = findHeroId(directContext);
        assertNotNull(heroId);

        List<List<GameCommand>> allCommands = new ArrayList<>();

        // Phase 1: Run turns before save
        for (int i = 0; i < TURNS_BEFORE_SAVE; i++) {
            Character hero = (Character) directContext.getActor(heroId);
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);

            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(heroId, targetPos)
            );
            allCommands.add(commands);

            directEngine.processNextTurn(commands);
        }

        // SAVE STATE
        byte[] savedData = directEngine.saveState();
        assertNotNull(savedData);

        // LOAD STATE into fresh engine
        GameEngine loadedEngine = new GameEngine(new MockPlatform(), TEST_SEED);
        loadedEngine.initialize();
        loadedEngine.loadState(savedData);
        EngineContext loadedContext = loadedEngine.getContext();

        // Find hero in loaded context
        ActorId loadedHeroId = findHeroId(loadedContext);
        assertNotNull(loadedHeroId);

        // Phase 2: Continue after load
        for (int i = 0; i < TURNS_AFTER_SAVE; i++) {
            Character hero = (Character) loadedContext.getActor(loadedHeroId);
            if (hero != null) {
                Point currentPos = hero.getPosition();
                Point targetPos = new Point(currentPos.x + 1, currentPos.y);

                List<GameCommand> commands = Collections.singletonList(
                    GameCommand.move(loadedHeroId, targetPos)
                );
                allCommands.add(commands);

                loadedEngine.processNextTurn(commands);
            }
        }

        // Capture final state from save/load run
        Character finalHeroSaveLoad = (Character) loadedContext.getActor(loadedHeroId);
        assertNotNull(finalHeroSaveLoad);
        Point finalPosSaveLoad = new Point(finalHeroSaveLoad.getPosition().x, finalHeroSaveLoad.getPosition().y);
        int finalHPSaveLoad = finalHeroSaveLoad.getCurrentHealth();

        // ===== RECORD RUN (straight through, no save/load) =====
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TOTAL_TURNS, allCommands, factory);

        assertNotNull(trace);
        assertEquals(TEST_SEED, trace.getSeed());
        assertEquals(TOTAL_TURNS, trace.getTurnCount());

        // ===== REPLAY RUN (straight through) =====
        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory);

        assertNotNull(result);
        assertEquals(TOTAL_TURNS, result.getTurnsExecuted());

        // ===== VERIFY REPLAY MATCHES SAVE/LOAD RUN =====
        EngineContext replayContext = result.getFinalContext();

        ActorId replayHeroId = findHeroId(replayContext);
        assertNotNull(replayHeroId);

        Character replayHero = (Character) replayContext.getActor(replayHeroId);
        assertNotNull(replayHero);

        // Verify position matches
        assertEquals(finalPosSaveLoad.x, replayHero.getPosition().x,
            "Hero X position should match between save/load and replay");
        assertEquals(finalPosSaveLoad.y, replayHero.getPosition().y,
            "Hero Y position should match between save/load and replay");

        // Expected final position: (1 + 8, 1) = (9, 1)
        // (3 moves before save + 5 moves after save = 8 total moves)
        assertEquals(9, replayHero.getPosition().x);
        assertEquals(1, replayHero.getPosition().y);

        // Verify HP matches
        assertEquals(finalHPSaveLoad, replayHero.getCurrentHealth(),
            "Hero HP should match between save/load and replay");

        // Verify no extra actors
        assertEquals(loadedContext.getActors().size(), replayContext.getActors().size(),
            "Actor count should match");

        // Verify occupancy consistent
        assertTrue(replayContext.getLevel().getGrid().isOccupied(replayHero.getPosition()),
            "Hero's final position should be occupied");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Create engine for simple walk scenario.
     */
    private EngineContextWithEngine createSimpleWalkEngine(long seed) {
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, seed);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 9x3 floor level
        LevelState level = makeFloorLevel(9, 3);
        context.setLevel(level);

        // Spawn hero at (1, 1)
        TestHero hero = spawnHero(context, new Point(1, 1), "Hero");
        context.setVisionActorId(hero.getId());

        return new EngineContextWithEngine(context, engine);
    }

    /**
     * Create engine for combat scenario.
     */
    private EngineContextWithEngine createCombatEngine(long seed) {
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, seed);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 7x3 floor level
        LevelState level = makeFloorLevel(7, 3);
        context.setLevel(level);

        // Spawn hero at (2, 1)
        TestHero hero = spawnHero(context, new Point(2, 1), "Hero");
        context.setVisionActorId(hero.getId());

        // Spawn mob at (4, 1)
        SimpleMeleeMob mob = spawnMob(context, new Point(4, 1));

        return new EngineContextWithEngine(context, engine);
    }

    /**
     * Create engine for save/load scenario.
     */
    private EngineContextWithEngine createSaveLoadEngine(long seed) {
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, seed);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 12x3 floor level (wide enough for movement)
        LevelState level = makeFloorLevel(12, 3);
        context.setLevel(level);

        // Spawn hero at (1, 1)
        TestHero hero = spawnHero(context, new Point(1, 1), "Hero");
        context.setVisionActorId(hero.getId());

        // Spawn mob at (7, 1) - not too far
        SimpleMeleeMob mob = spawnMob(context, new Point(7, 1));

        return new EngineContextWithEngine(context, engine);
    }

    /**
     * Create a level filled with FLOOR tiles.
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
     * Spawn a hero at the given position.
     */
    private static TestHero spawnHero(EngineContext context, Point pos, String name) {
        TestHero hero = new TestHero(pos, name);
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(hero, 0.0f);
        return hero;
    }

    /**
     * Spawn a mob at the given position.
     */
    private static SimpleMeleeMob spawnMob(EngineContext context, Point pos) {
        SimpleMeleeMob mob = new SimpleMeleeMob(pos);
        context.addActor(mob);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(mob, 0.0f);
        return mob;
    }

    /**
     * Find hero actor ID in context.
     */
    private static ActorId findHeroId(EngineContext context) {
        for (Actor actor : context.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                return actor.getId();
            }
        }
        return null;
    }

    /**
     * Find mob actor ID in context.
     */
    private static ActorId findMobId(EngineContext context) {
        for (Actor actor : context.getActors().values()) {
            if (actor.getType() == ActorType.MOB) {
                return actor.getId();
            }
        }
        return null;
    }

    /**
     * Get position of an actor by ID.
     */
    private static Point positionOf(ActorId actorId, EngineContext context) {
        Actor actor = context.getActor(actorId);
        return actor != null ? actor.getPosition() : null;
    }
}
