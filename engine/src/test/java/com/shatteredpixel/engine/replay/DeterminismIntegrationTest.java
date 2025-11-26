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
 * Comprehensive end-to-end determinism integration test.
 *
 * This is the FINAL verification step of Phase 4, testing the ENTIRE
 * deterministic pipeline:
 * - Save/load correctness
 * - Replay harness correctness
 * - Turn-by-turn state hashing correctness
 * - AI determinism (SimpleMeleeMob)
 * - Combat determinism (attack resolution)
 * - Scheduler determinism (turn order)
 * - Seed determinism (RNG usage)
 * - Event order determinism
 *
 * Three execution paths are compared:
 * 1. Direct-run (baseline) - straight execution, hash each turn
 * 2. Direct-run with mid-run save/load - save at turn 5, load, continue
 * 3. Replay-run - replay recorded trace, hash each turn
 *
 * All three paths MUST produce:
 * - Identical per-turn state hash sequences
 * - Identical final state (positions, HP, mob death, occupancy)
 *
 * This test proves:
 * - Save/load is invisible at logical state level
 * - Replay produces exact same execution as original
 * - All engine systems are deterministic
 * - No hidden state escapes serialization
 * - Turn-by-turn state hashing works correctly
 *
 * Design:
 * - 100% test-only (no engine modifications)
 * - Non-intrusive (pure observer)
 * - Headless (no UI)
 * - GWT-safe
 * - Comprehensive (tests all Phase 4 features together)
 */
public class DeterminismIntegrationTest {

    private static final long TEST_SEED = 42424242L;
    private static final int TOTAL_TURNS = 12;
    private static final int SAVE_LOAD_BOUNDARY = 5; // Save/load after turn 5

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
     * INTEGRATION TEST: hero_moves_fights_saves_then_checks_determinism
     *
     * This is the comprehensive end-to-end test for Phase 4.
     *
     * Scenario:
     * - 15×5 FLOOR level
     * - Hero at (2, 2), Mob at (10, 2)
     * - Total 12 turns:
     *   - Turns 0-4: Hero moves right, mob AI chases
     *   - Turn 5: Save/load boundary (in path B only)
     *   - Turns 6-8: Continue moving, become adjacent
     *   - Turns 9-11: Combat until mob dies
     *
     * Execution paths:
     * A) Direct-run (baseline) - 12 turns straight through
     * B) Direct-run with save/load - save after turn 5, load, continue
     * C) Replay-run - replay recorded trace from path A
     *
     * Assertions:
     * - All three paths produce identical hash sequences
     * - No desync detected (findFirstDifference == -1)
     * - Final states match (positions, HP, mob death, occupancy)
     * - No exceptions, no duplicate actors
     */
    @Test
    public void testIntegration_hero_moves_fights_saves_then_checks_determinism() {
        // ===== PATH A: Direct-run (baseline) =====
        System.out.println("Running Path A: Direct-run (baseline)...");
        TurnHashSequence baselineSeq = executeDirectRun();
        EngineContext baselineContext = captureBaselineState();

        // ===== PATH B: Direct-run with mid-run save/load =====
        System.out.println("Running Path B: Direct-run with save/load...");
        TurnHashSequence saveLoadSeq = executeSaveLoadRun();
        EngineContext saveLoadContext = captureSaveLoadState();

        // ===== PATH C: Replay-run =====
        System.out.println("Running Path C: Replay-run...");
        TurnHashSequence replaySeq = executeReplayRun();
        EngineContext replayContext = captureReplayState();

        // ===== VERIFICATION: Hash Sequences =====
        System.out.println("\n=== HASH SEQUENCE COMPARISON ===");

        // 1. Verify sizes
        assertEquals(TOTAL_TURNS, baselineSeq.size(),
            "Baseline should have " + TOTAL_TURNS + " turns");
        assertEquals(TOTAL_TURNS, saveLoadSeq.size(),
            "Save/load should have " + TOTAL_TURNS + " turns");
        assertEquals(TOTAL_TURNS, replaySeq.size(),
            "Replay should have " + TOTAL_TURNS + " turns");

        System.out.println("✓ All sequences have " + TOTAL_TURNS + " turns");

        // 2. Verify no desyncs
        int desyncBaseline_SaveLoad = baselineSeq.findFirstDifference(saveLoadSeq);
        int desyncBaseline_Replay = baselineSeq.findFirstDifference(replaySeq);

        assertEquals(-1, desyncBaseline_SaveLoad,
            "No desync should exist between baseline and save/load");
        assertEquals(-1, desyncBaseline_Replay,
            "No desync should exist between baseline and replay");

        System.out.println("✓ No desyncs detected (findFirstDifference == -1)");

        // 3. Verify complete hash sequence matches
        assertTrue(baselineSeq.matchesHashes(saveLoadSeq),
            "Baseline and save/load hash sequences must match exactly");
        assertTrue(baselineSeq.matchesHashes(replaySeq),
            "Baseline and replay hash sequences must match exactly");

        System.out.println("✓ All hash sequences match exactly");

        // 4. Verify per-turn hashes individually
        for (int turn = 0; turn < TOTAL_TURNS; turn++) {
            TurnHashRecord baselineRecord = baselineSeq.get(turn);
            TurnHashRecord saveLoadRecord = saveLoadSeq.get(turn);
            TurnHashRecord replayRecord = replaySeq.get(turn);

            assertEquals(turn, baselineRecord.getTurnIndex());
            assertEquals(turn, saveLoadRecord.getTurnIndex());
            assertEquals(turn, replayRecord.getTurnIndex());

            assertEquals(baselineRecord.getStateHash(), saveLoadRecord.getStateHash(),
                "Turn " + turn + " hash must match between baseline and save/load");
            assertEquals(baselineRecord.getStateHash(), replayRecord.getStateHash(),
                "Turn " + turn + " hash must match between baseline and replay");

            // Special note for save/load boundary
            if (turn == SAVE_LOAD_BOUNDARY) {
                System.out.println("  Turn " + turn + " (save/load boundary): " +
                    baselineRecord.getStateHash().toHexString() + " ✓");
            }
        }

        System.out.println("✓ All per-turn hashes verified individually");

        // ===== VERIFICATION: Final State =====
        System.out.println("\n=== FINAL STATE COMPARISON ===");

        // Find actors in all three contexts
        ActorId baselineHeroId = findHeroId(baselineContext);
        ActorId saveLoadHeroId = findHeroId(saveLoadContext);
        ActorId replayHeroId = findHeroId(replayContext);

        assertNotNull(baselineHeroId, "Baseline hero should exist");
        assertNotNull(saveLoadHeroId, "Save/load hero should exist");
        assertNotNull(replayHeroId, "Replay hero should exist");

        Character baselineHero = (Character) baselineContext.getActor(baselineHeroId);
        Character saveLoadHero = (Character) saveLoadContext.getActor(saveLoadHeroId);
        Character replayHero = (Character) replayContext.getActor(replayHeroId);

        // 1. Verify hero positions
        Point baselinePos = baselineHero.getPosition();
        Point saveLoadPos = saveLoadHero.getPosition();
        Point replayPos = replayHero.getPosition();

        assertEquals(baselinePos.x, saveLoadPos.x, "Hero X position must match");
        assertEquals(baselinePos.y, saveLoadPos.y, "Hero Y position must match");
        assertEquals(baselinePos.x, replayPos.x, "Hero X position must match");
        assertEquals(baselinePos.y, replayPos.y, "Hero Y position must match");

        System.out.println("✓ Hero final position: (" + baselinePos.x + ", " + baselinePos.y + ")");

        // 2. Verify hero HP
        int baselineHP = baselineHero.getCurrentHealth();
        int saveLoadHP = saveLoadHero.getCurrentHealth();
        int replayHP = replayHero.getCurrentHealth();

        assertEquals(baselineHP, saveLoadHP, "Hero HP must match");
        assertEquals(baselineHP, replayHP, "Hero HP must match");

        System.out.println("✓ Hero final HP: " + baselineHP);

        // 3. Verify mob death status
        ActorId baselineMobId = findMobId(baselineContext);
        ActorId saveLoadMobId = findMobId(saveLoadContext);
        ActorId replayMobId = findMobId(replayContext);

        boolean baselineMobDead = (baselineMobId == null);
        boolean saveLoadMobDead = (saveLoadMobId == null);
        boolean replayMobDead = (replayMobId == null);

        assertEquals(baselineMobDead, saveLoadMobDead, "Mob death status must match");
        assertEquals(baselineMobDead, replayMobDead, "Mob death status must match");

        System.out.println("✓ Mob death status: " + (baselineMobDead ? "DEAD" : "ALIVE"));

        // 4. Verify actor counts
        int baselineActorCount = baselineContext.getActors().size();
        int saveLoadActorCount = saveLoadContext.getActors().size();
        int replayActorCount = replayContext.getActors().size();

        assertEquals(baselineActorCount, saveLoadActorCount, "Actor count must match");
        assertEquals(baselineActorCount, replayActorCount, "Actor count must match");

        System.out.println("✓ Final actor count: " + baselineActorCount);

        // 5. Verify scheduler state
        int baselineSchedulerCount = baselineContext.getScheduler().getActorCount();
        int saveLoadSchedulerCount = saveLoadContext.getScheduler().getActorCount();
        int replaySchedulerCount = replayContext.getScheduler().getActorCount();

        assertEquals(baselineSchedulerCount, saveLoadSchedulerCount, "Scheduler count must match");
        assertEquals(baselineSchedulerCount, replaySchedulerCount, "Scheduler count must match");

        System.out.println("✓ Scheduled actor count: " + baselineSchedulerCount);

        // 6. Verify grid occupancy
        boolean baselineOccupied = baselineContext.getLevel().getGrid().isOccupied(baselinePos);
        boolean saveLoadOccupied = saveLoadContext.getLevel().getGrid().isOccupied(saveLoadPos);
        boolean replayOccupied = replayContext.getLevel().getGrid().isOccupied(replayPos);

        assertTrue(baselineOccupied, "Hero position should be occupied");
        assertTrue(saveLoadOccupied, "Hero position should be occupied");
        assertTrue(replayOccupied, "Hero position should be occupied");

        System.out.println("✓ Grid occupancy consistent");

        // 7. Verify no duplicate actors
        verifyNoDuplicates(baselineContext, "baseline");
        verifyNoDuplicates(saveLoadContext, "save/load");
        verifyNoDuplicates(replayContext, "replay");

        System.out.println("✓ No duplicate actors");

        // ===== SUCCESS =====
        System.out.println("\n=== DETERMINISM INTEGRATION TEST PASSED ===");
        System.out.println("All three execution paths produced identical results:");
        System.out.println("  ✓ Direct-run (baseline)");
        System.out.println("  ✓ Direct-run with save/load at turn " + SAVE_LOAD_BOUNDARY);
        System.out.println("  ✓ Replay-run");
        System.out.println("\nPhase 4 determinism verification: COMPLETE");
    }

    // =========================================================================
    // Command Script Preparation
    // =========================================================================

    /**
     * Build a complete command script for the integration test scenario.
     *
     * This creates a NEW independent list with commands for all TOTAL_TURNS turns.
     * Each turn gets its own ArrayList of commands.
     *
     * Commands are only for the hero (external player input).
     * Mob commands are NOT included - they are generated by AI deterministically.
     *
     * Scenario:
     * - Turns 0-8: MOVE RIGHT (hero walks from (2,2) to (11,2))
     * - Turns 9-11: ATTACK (hero attacks mob until it dies)
     *
     * @param heroId Hero actor ID for this specific engine context
     * @param mobId Mob actor ID for this specific engine context (may be null)
     * @return New independent command script (not shared with other paths)
     */
    private List<List<GameCommand>> buildCommandScriptForScenario(ActorId heroId, ActorId mobId) {
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();

        // Turns 0-8: MOVE RIGHT
        for (int turn = 0; turn < 9; turn++) {
            Point targetPos = new Point(2 + turn + 1, 2); // Start at (2,2), move right
            List<GameCommand> commands = new ArrayList<>();
            commands.add(GameCommand.move(heroId, targetPos));
            commandsPerTurn.add(commands);
        }

        // Turns 9-11: ATTACK
        for (int turn = 9; turn < 12; turn++) {
            List<GameCommand> commands = new ArrayList<>();
            if (mobId != null) {
                commands.add(GameCommand.attack(heroId, mobId));
            }
            commandsPerTurn.add(commands);
        }

        return commandsPerTurn;
    }

    // =========================================================================
    // Path A: Direct-run (baseline)
    // =========================================================================

    private TurnHashSequence executeDirectRun() {
        EngineFactory factory = this::createIntegrationEngine;
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Resolve actor IDs from THIS engine's context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for this path
        List<List<GameCommand>> commandsPerTurn = buildCommandScriptForScenario(heroId, mobId);

        List<TurnHashRecord> hashes = new ArrayList<>();

        for (int turn = 0; turn < TOTAL_TURNS; turn++) {
            List<GameCommand> commands = commandsPerTurn.get(turn);
            engine.processNextTurn(commands);

            StateHash hash = StateHasher.hashState(engine);
            hashes.add(new TurnHashRecord(turn, hash));
        }

        return new TurnHashSequence(hashes);
    }

    private EngineContext captureBaselineState() {
        // Re-run to capture final state
        EngineFactory factory = this::createIntegrationEngine;
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Resolve actor IDs from THIS engine's context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for this path
        List<List<GameCommand>> commandsPerTurn = buildCommandScriptForScenario(heroId, mobId);

        for (int turn = 0; turn < TOTAL_TURNS; turn++) {
            List<GameCommand> commands = commandsPerTurn.get(turn);
            engine.processNextTurn(commands);
        }

        return context;
    }

    // =========================================================================
    // Path B: Direct-run with save/load
    // =========================================================================

    private TurnHashSequence executeSaveLoadRun() {
        // Phase 1: Run turns 0 to SAVE_LOAD_BOUNDARY
        EngineFactory factory = this::createIntegrationEngine;
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Resolve actor IDs from initial engine context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for phase 1 (before save)
        List<List<GameCommand>> commandsBeforeSave = buildCommandScriptForScenario(heroId, mobId);

        List<TurnHashRecord> hashes = new ArrayList<>();

        // Before save
        for (int turn = 0; turn <= SAVE_LOAD_BOUNDARY; turn++) {
            List<GameCommand> commands = commandsBeforeSave.get(turn);
            engine.processNextTurn(commands);

            StateHash hash = StateHasher.hashState(engine);
            hashes.add(new TurnHashRecord(turn, hash));
        }

        // SAVE
        byte[] savedData = engine.saveState();

        // LOAD into fresh engine
        GameEngine loadedEngine = new GameEngine(new MockPlatform(), TEST_SEED);
        loadedEngine.initialize();
        loadedEngine.loadState(savedData);
        EngineContext loadedContext = loadedEngine.getContext();

        // Resolve actor IDs from LOADED engine context
        // (Do not assume IDs persist across save/load)
        ActorId loadedHeroId = findHeroId(loadedContext);
        ActorId loadedMobId = findMobId(loadedContext);

        // Build NEW independent command script for phase 2 (after load) with loaded IDs
        List<List<GameCommand>> commandsAfterLoad = buildCommandScriptForScenario(loadedHeroId, loadedMobId);

        // After load
        for (int turn = SAVE_LOAD_BOUNDARY + 1; turn < TOTAL_TURNS; turn++) {
            List<GameCommand> commands = commandsAfterLoad.get(turn);
            loadedEngine.processNextTurn(commands);

            StateHash hash = StateHasher.hashState(loadedEngine);
            hashes.add(new TurnHashRecord(turn, hash));
        }

        return new TurnHashSequence(hashes);
    }

    private EngineContext captureSaveLoadState() {
        // Re-run to capture final state
        EngineFactory factory = this::createIntegrationEngine;
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();
        GameEngine engine = pair.getEngine();

        // Resolve actor IDs from initial engine context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for phase 1 (before save)
        List<List<GameCommand>> commandsBeforeSave = buildCommandScriptForScenario(heroId, mobId);

        // Before save
        for (int turn = 0; turn <= SAVE_LOAD_BOUNDARY; turn++) {
            List<GameCommand> commands = commandsBeforeSave.get(turn);
            engine.processNextTurn(commands);
        }

        // SAVE and LOAD
        byte[] savedData = engine.saveState();
        GameEngine loadedEngine = new GameEngine(new MockPlatform(), TEST_SEED);
        loadedEngine.initialize();
        loadedEngine.loadState(savedData);
        EngineContext loadedContext = loadedEngine.getContext();

        // Resolve actor IDs from LOADED engine context
        // (Do not assume IDs persist across save/load)
        ActorId loadedHeroId = findHeroId(loadedContext);
        ActorId loadedMobId = findMobId(loadedContext);

        // Build NEW independent command script for phase 2 (after load) with loaded IDs
        List<List<GameCommand>> commandsAfterLoad = buildCommandScriptForScenario(loadedHeroId, loadedMobId);

        // After load
        for (int turn = SAVE_LOAD_BOUNDARY + 1; turn < TOTAL_TURNS; turn++) {
            List<GameCommand> commands = commandsAfterLoad.get(turn);
            loadedEngine.processNextTurn(commands);
        }

        return loadedContext;
    }

    // =========================================================================
    // Path C: Replay-run
    // =========================================================================

    private TurnHashSequence executeReplayRun() {
        EngineFactory factory = this::createIntegrationEngine;

        // Create a fresh engine to resolve actor IDs for recording
        // (This ensures replay uses IDs from its own context, not contaminated by other paths)
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();

        // Resolve actor IDs from THIS engine's context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for replay path
        List<List<GameCommand>> commandsPerTurn = buildCommandScriptForScenario(heroId, mobId);

        // Record the baseline run
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TOTAL_TURNS, commandsPerTurn, factory);

        // Replay it
        List<TurnHashRecord> hashes = new ArrayList<>();
        ReplayObserver observer = (turnIndex, ctx, engine, events) -> {
            StateHash hash = StateHasher.hashState(engine);
            hashes.add(new TurnHashRecord(turnIndex, hash));
        };

        ReplayRunner runner = new ReplayRunner();
        runner.replay(trace, factory, observer);

        return new TurnHashSequence(hashes);
    }

    private EngineContext captureReplayState() {
        EngineFactory factory = this::createIntegrationEngine;

        // Create a fresh engine to resolve actor IDs for recording
        EngineContextWithEngine pair = factory.create(TEST_SEED);
        EngineContext context = pair.getContext();

        // Resolve actor IDs from THIS engine's context
        ActorId heroId = findHeroId(context);
        ActorId mobId = findMobId(context);

        // Build independent command script for replay path
        List<List<GameCommand>> commandsPerTurn = buildCommandScriptForScenario(heroId, mobId);

        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, TOTAL_TURNS, commandsPerTurn, factory);

        ReplayRunner runner = new ReplayRunner();
        ReplayResult result = runner.replay(trace, factory);

        return result.getFinalContext();
    }

    // =========================================================================
    // Engine Creation
    // =========================================================================

    private EngineContextWithEngine createIntegrationEngine(long seed) {
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, seed);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 15x5 FLOOR level
        LevelState level = makeFloorLevel(15, 5);
        context.setLevel(level);

        // Spawn hero at (2, 2)
        TestHero hero = spawnHero(context, new Point(2, 2), "Hero");
        context.setVisionActorId(hero.getId());

        // Spawn mob at (10, 2)
        SimpleMeleeMob mob = spawnMob(context, new Point(10, 2));

        return new EngineContextWithEngine(context, engine);
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    private static LevelState makeFloorLevel(int width, int height) {
        LevelState level = new LevelState(1, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                level.getGrid().setTerrain(x, y, TerrainType.FLOOR);
            }
        }
        return level;
    }

    private static TestHero spawnHero(EngineContext context, Point pos, String name) {
        TestHero hero = new TestHero(pos, name);
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(hero, 0.0f);
        return hero;
    }

    private static SimpleMeleeMob spawnMob(EngineContext context, Point pos) {
        SimpleMeleeMob mob = new SimpleMeleeMob(pos);
        context.addActor(mob);
        context.getLevel().getGrid().setOccupied(pos, true);
        context.registerActorForScheduling(mob, 0.0f);
        return mob;
    }

    private static ActorId findHeroId(EngineContext context) {
        for (Actor actor : context.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                return actor.getId();
            }
        }
        return null;
    }

    private static ActorId findMobId(EngineContext context) {
        for (Actor actor : context.getActors().values()) {
            if (actor.getType() == ActorType.MOB) {
                return actor.getId();
            }
        }
        return null;
    }

    private static void verifyNoDuplicates(EngineContext context, String label) {
        int heroCount = 0;
        int mobCount = 0;

        for (Actor actor : context.getActors().values()) {
            if (actor.getType() == ActorType.HERO) heroCount++;
            if (actor.getType() == ActorType.MOB) mobCount++;
        }

        assertTrue(heroCount <= 1, label + ": Should have at most 1 hero, got " + heroCount);
        assertTrue(mobCount <= 1, label + ": Should have at most 1 mob, got " + mobCount);
    }
}
