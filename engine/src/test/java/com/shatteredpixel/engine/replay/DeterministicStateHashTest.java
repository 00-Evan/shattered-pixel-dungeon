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
 * Comprehensive test suite for deterministic state hashing.
 *
 * These tests verify that:
 * - Same seed + same commands → same state hashes per turn
 * - Direct runs and replay runs produce identical hash sequences
 * - Save/load boundaries don't affect hash sequences
 * - Desyncs can be detected at exact turn via hash comparison
 *
 * All tests use:
 * - StateHasher to compute per-turn hashes
 * - TurnHashSequence to collect hash timelines
 * - ReplayRecorder/ReplayRunner for replay testing
 * - ReplayObserver to hook into turn execution
 *
 * All tests are fully headless (no UI, no console output).
 */
public class DeterministicStateHashTest {

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
     * Scenario 1: simple_walk_hash_sequence_matches
     *
     * Verifies that simple hero movement produces identical hash sequences
     * when executed directly vs replayed.
     *
     * Setup:
     * - 9x3 FLOOR level
     * - Hero at (1, 1)
     * - No mobs
     * - Hero moves right for 5 turns
     *
     * Flow:
     * 1. Direct run: execute turns, hash after each
     * 2. Replay run: use ReplayRecorder + ReplayRunner, hash after each
     * 3. Compare sequences
     *
     * Assertions:
     * - Turn counts match
     * - All turn indices match
     * - All state hashes match
     * - Final positions match
     */
    @Test
    public void testScenario1_simple_walk_hash_sequence_matches() {
        final int TURN_COUNT = 5;

        // ===== DIRECT RUN =====
        EngineFactory factory = this::createSimpleWalkEngine;
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        ActorId heroId = findHeroId(directContext);
        assertNotNull(heroId);

        // Collect commands and hashes
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();
        List<TurnHashRecord> directHashes = new ArrayList<>();

        for (int turn = 0; turn < TURN_COUNT; turn++) {
            Character hero = (Character) directContext.getActor(heroId);
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);

            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(heroId, targetPos)
            );
            commandsPerTurn.add(commands);

            // Execute turn
            directEngine.processNextTurn(commands);

            // Compute and record hash
            StateHash hash = StateHasher.hashState(directEngine);
            directHashes.add(new TurnHashRecord(turn, hash));
        }

        TurnHashSequence directSequence = new TurnHashSequence(directHashes);

        // Capture final state
        Character directHero = (Character) directContext.getActor(heroId);
        Point directFinalPos = new Point(directHero.getPosition().x, directHero.getPosition().y);

        // ===== REPLAY RUN =====
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TURN_COUNT, commandsPerTurn, factory);

        List<TurnHashRecord> replayHashes = new ArrayList<>();
        ReplayObserver observer = (turnIndex, context, engine, events) -> {
            StateHash hash = StateHasher.hashState(engine);
            replayHashes.add(new TurnHashRecord(turnIndex, hash));
        };

        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory, observer);

        TurnHashSequence replaySequence = new TurnHashSequence(replayHashes);

        // ===== VERIFY SEQUENCES MATCH =====
        assertEquals(directSequence.size(), replaySequence.size(),
            "Turn count should match");

        assertTrue(directSequence.matchesHashes(replaySequence),
            "Hash sequences should match exactly");

        // Verify no differences
        int firstDiff = directSequence.findFirstDifference(replaySequence);
        assertEquals(-1, firstDiff,
            "Should be no differences between sequences");

        // Verify each turn individually
        for (int i = 0; i < TURN_COUNT; i++) {
            TurnHashRecord directRecord = directSequence.get(i);
            TurnHashRecord replayRecord = replaySequence.get(i);

            assertEquals(i, directRecord.getTurnIndex());
            assertEquals(i, replayRecord.getTurnIndex());
            assertEquals(directRecord.getStateHash(), replayRecord.getStateHash(),
                "Hash at turn " + i + " should match");
        }

        // Verify final position as sanity check
        ActorId replayHeroId = findHeroId(result.getFinalContext());
        Character replayHero = (Character) result.getFinalContext().getActor(replayHeroId);

        assertEquals(directFinalPos.x, replayHero.getPosition().x);
        assertEquals(directFinalPos.y, replayHero.getPosition().y);

        // Expected final position: (1 + 5, 1) = (6, 1)
        assertEquals(6, replayHero.getPosition().x);
        assertEquals(1, replayHero.getPosition().y);
    }

    /**
     * Scenario 2: combat_hash_sequence_matches
     *
     * Verifies that combat produces identical hash sequences when
     * executed directly vs replayed.
     *
     * Setup:
     * - 7x3 FLOOR level
     * - Hero at (2, 1), Mob at (4, 1)
     * - Hero moves right until adjacent, then attacks until mob dies
     *
     * Flow:
     * 1. Direct run: movement + combat, hash after each turn
     * 2. Replay run: replay trace, hash after each turn
     * 3. Compare sequences
     *
     * Assertions:
     * - Turn counts match
     * - All state hashes match per turn
     * - Mob death state matches
     * - Final hero HP matches
     */
    @Test
    public void testScenario2_combat_hash_sequence_matches() {
        final int MAX_TURNS = 15;

        // ===== DIRECT RUN =====
        EngineFactory factory = this::createCombatEngine;
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        ActorId heroId = findHeroId(directContext);
        ActorId mobId = findMobId(directContext);
        assertNotNull(heroId);
        assertNotNull(mobId);

        // Collect commands and hashes
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();
        List<TurnHashRecord> directHashes = new ArrayList<>();

        for (int turn = 0; turn < MAX_TURNS; turn++) {
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

            // Execute turn
            directEngine.processNextTurn(commands);

            // Compute and record hash
            StateHash hash = StateHasher.hashState(directEngine);
            directHashes.add(new TurnHashRecord(turn, hash));

            // Stop if mob is dead
            if (mob == null || mob.isDead()) {
                break;
            }
        }

        TurnHashSequence directSequence = new TurnHashSequence(directHashes);

        // Capture final state
        Character directHero = (Character) directContext.getActor(heroId);
        int directFinalHP = directHero.getCurrentHealth();

        // ===== REPLAY RUN =====
        int actualTurns = commandsPerTurn.size();
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, actualTurns, commandsPerTurn, factory);

        List<TurnHashRecord> replayHashes = new ArrayList<>();
        ReplayObserver observer = (turnIndex, context, engine, events) -> {
            StateHash hash = StateHasher.hashState(engine);
            replayHashes.add(new TurnHashRecord(turnIndex, hash));
        };

        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory, observer);

        TurnHashSequence replaySequence = new TurnHashSequence(replayHashes);

        // ===== VERIFY SEQUENCES MATCH =====
        assertEquals(directSequence.size(), replaySequence.size(),
            "Turn count should match");

        assertTrue(directSequence.matchesHashes(replaySequence),
            "Hash sequences should match exactly");

        // Verify no differences
        int firstDiff = directSequence.findFirstDifference(replaySequence);
        assertEquals(-1, firstDiff,
            "Should be no differences between sequences");

        // Verify final state
        ActorId replayHeroId = findHeroId(result.getFinalContext());
        Character replayHero = (Character) result.getFinalContext().getActor(replayHeroId);

        assertEquals(directFinalHP, replayHero.getCurrentHealth(),
            "Final HP should match");

        // Verify mob is dead in both
        ActorId replayMobId = findMobId(result.getFinalContext());
        assertTrue(replayMobId == null,
            "Mob should be dead and removed");
    }

    /**
     * Scenario 3: save_load_boundary_does_not_change_hash_sequence
     *
     * Verifies that save/load boundaries don't affect hash sequences.
     *
     * Setup:
     * - 12x3 FLOOR level
     * - Hero at (1, 1), Mob at (7, 1)
     * - Hero moves right across multiple turns
     *
     * Flow:
     * 1. Direct run with save/load:
     *    - Run 3 turns, hash each
     *    - Save state
     *    - Load into fresh engine
     *    - Continue 5 more turns, hash each
     * 2. Replay run (straight through, no save/load):
     *    - Replay all 8 turns
     *    - Hash each turn
     * 3. Compare sequences
     *
     * Assertions:
     * - Turn counts match (8 total)
     * - All state hashes match
     * - Save/load boundary is invisible
     * - Final positions match
     */
    @Test
    public void testScenario3_save_load_boundary_does_not_change_hash_sequence() {
        final int TURNS_BEFORE_SAVE = 3;
        final int TURNS_AFTER_SAVE = 5;
        final int TOTAL_TURNS = TURNS_BEFORE_SAVE + TURNS_AFTER_SAVE;

        // ===== DIRECT RUN WITH SAVE/LOAD =====
        EngineFactory factory = this::createSaveLoadEngine;
        EngineContextWithEngine directPair = factory.create(TEST_SEED);
        EngineContext directContext = directPair.getContext();
        GameEngine directEngine = directPair.getEngine();

        ActorId heroId = findHeroId(directContext);
        assertNotNull(heroId);

        List<List<GameCommand>> allCommands = new ArrayList<>();
        List<TurnHashRecord> saveLoadHashes = new ArrayList<>();

        // Phase 1: Turns before save
        for (int turn = 0; turn < TURNS_BEFORE_SAVE; turn++) {
            Character hero = (Character) directContext.getActor(heroId);
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);

            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(heroId, targetPos)
            );
            allCommands.add(commands);

            // Execute turn
            directEngine.processNextTurn(commands);

            // Compute and record hash
            StateHash hash = StateHasher.hashState(directEngine);
            saveLoadHashes.add(new TurnHashRecord(turn, hash));
        }

        // SAVE STATE
        byte[] savedData = directEngine.saveState();
        assertNotNull(savedData);

        // LOAD STATE into fresh engine
        GameEngine loadedEngine = new GameEngine(new MockPlatform(), TEST_SEED);
        loadedEngine.initialize();
        loadedEngine.loadState(savedData);
        EngineContext loadedContext = loadedEngine.getContext();

        ActorId loadedHeroId = findHeroId(loadedContext);
        assertNotNull(loadedHeroId);

        // Phase 2: Turns after load
        for (int turn = TURNS_BEFORE_SAVE; turn < TOTAL_TURNS; turn++) {
            Character hero = (Character) loadedContext.getActor(loadedHeroId);
            Point currentPos = hero.getPosition();
            Point targetPos = new Point(currentPos.x + 1, currentPos.y);

            List<GameCommand> commands = Collections.singletonList(
                GameCommand.move(loadedHeroId, targetPos)
            );
            allCommands.add(commands);

            // Execute turn
            loadedEngine.processNextTurn(commands);

            // Compute and record hash
            StateHash hash = StateHasher.hashState(loadedEngine);
            saveLoadHashes.add(new TurnHashRecord(turn, hash));
        }

        TurnHashSequence saveLoadSequence = new TurnHashSequence(saveLoadHashes);

        // Capture final state
        Character finalHeroSaveLoad = (Character) loadedContext.getActor(loadedHeroId);
        Point finalPosSaveLoad = new Point(finalHeroSaveLoad.getPosition().x, finalHeroSaveLoad.getPosition().y);

        // ===== REPLAY RUN (straight through, no save/load) =====
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TOTAL_TURNS, allCommands, factory);

        List<TurnHashRecord> replayHashes = new ArrayList<>();
        ReplayObserver observer = (turnIndex, context, engine, events) -> {
            StateHash hash = StateHasher.hashState(engine);
            replayHashes.add(new TurnHashRecord(turnIndex, hash));
        };

        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory, observer);

        TurnHashSequence replaySequence = new TurnHashSequence(replayHashes);

        // ===== VERIFY SEQUENCES MATCH =====
        assertEquals(TOTAL_TURNS, saveLoadSequence.size(),
            "Save/load sequence should have " + TOTAL_TURNS + " turns");
        assertEquals(TOTAL_TURNS, replaySequence.size(),
            "Replay sequence should have " + TOTAL_TURNS + " turns");

        assertTrue(saveLoadSequence.matchesHashes(replaySequence),
            "Hash sequences should match exactly despite save/load boundary");

        // Verify no differences
        int firstDiff = saveLoadSequence.findFirstDifference(replaySequence);
        assertEquals(-1, firstDiff,
            "Should be no differences between sequences");

        // Verify each turn individually
        for (int i = 0; i < TOTAL_TURNS; i++) {
            TurnHashRecord saveLoadRecord = saveLoadSequence.get(i);
            TurnHashRecord replayRecord = replaySequence.get(i);

            assertEquals(i, saveLoadRecord.getTurnIndex());
            assertEquals(i, replayRecord.getTurnIndex());
            assertEquals(saveLoadRecord.getStateHash(), replayRecord.getStateHash(),
                "Hash at turn " + i + " should match (save/load at turn " + TURNS_BEFORE_SAVE + ")");
        }

        // Verify final position
        ActorId replayHeroId = findHeroId(result.getFinalContext());
        Character replayHero = (Character) result.getFinalContext().getActor(replayHeroId);

        assertEquals(finalPosSaveLoad.x, replayHero.getPosition().x);
        assertEquals(finalPosSaveLoad.y, replayHero.getPosition().y);

        // Expected final position: (1 + 8, 1) = (9, 1)
        assertEquals(9, replayHero.getPosition().x);
        assertEquals(1, replayHero.getPosition().y);
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

        // Create 12x3 floor level
        LevelState level = makeFloorLevel(12, 3);
        context.setLevel(level);

        // Spawn hero at (1, 1)
        TestHero hero = spawnHero(context, new Point(1, 1), "Hero");
        context.setVisionActorId(hero.getId());

        // Spawn mob at (7, 1)
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
