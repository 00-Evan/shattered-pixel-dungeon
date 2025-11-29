package com.shatteredpixel.engine.actor.buff;

import com.shatteredpixel.api.MockPlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.actor.hero.BaseHero;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.replay.*;
import com.shatteredpixel.engine.serialization.dto.BuffSnapshot;
import com.shatteredpixel.engine.stats.Stats;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive foundation tests for the buff system.
 *
 * Tests Phase 5 Step 5.1 requirements:
 * - Buff add/remove lifecycle
 * - Duration-based ticking and expiration
 * - Serialization (save/load persistence)
 * - Deterministic replay compatibility
 * - BuffSnapshot round-trip
 *
 * Three test scenarios:
 * A) simple_duration_tick - Buff duration decrements each turn until expiration
 * B) save_load_restores_buffs - Buffs persist correctly across save/load
 * C) determinism_hash_test - Replay produces identical state hashes
 */
public class BuffFoundationTest {

    private static final long TEST_SEED = 12345L;

    /**
     * Test buff implementation for foundation tests.
     *
     * Simple buff that tracks onApply/onTick/onRemove calls for verification.
     */
    private static class TestBuff extends BaseBuff {
        // Counters for lifecycle verification
        public int onApplyCount = 0;
        public int onTickCount = 0;
        public int onRemoveCount = 0;

        public TestBuff(int duration) {
            super(duration);
        }

        @Override
        public String getBuffType() {
            return "testbuff";
        }

        @Override
        public void onApply(EngineContext context, com.shatteredpixel.engine.actor.Actor actor) {
            onApplyCount++;
        }

        @Override
        public void onTick(EngineContext context, com.shatteredpixel.engine.actor.Actor actor) {
            onTickCount++;
        }

        @Override
        public void onRemove(EngineContext context, com.shatteredpixel.engine.actor.Actor actor) {
            onRemoveCount++;
        }
    }

    /**
     * Test hero for buff tests.
     */
    private static class TestHero extends BaseHero {
        public TestHero(Point position, String name) {
            super(position, new Stats(50, 10, 5, 15, 15, 1.0f), name);
        }

        @Override
        public float act(EngineContext context) {
            return 1.0f; // Always costs 1 turn
        }
    }

    @BeforeEach
    public void setUp() {
        // Clear and register test buff
        BuffRegistry.clearForTesting();
        BuffRegistry.register("testbuff", duration -> new TestBuff(duration));
    }

    @AfterEach
    public void tearDown() {
        // Clean up registry
        BuffRegistry.clearForTesting();
    }

    // =========================================================================
    // Scenario A: Simple Duration Tick
    // =========================================================================

    /**
     * Test that buff duration decrements each turn and buff expires correctly.
     *
     * Scenario:
     * 1. Create Hero at (5, 5)
     * 2. Add TestBuff(duration=3)
     * 3. Run 3 turns
     * 4. Verify duration decrements: 3 → 2 → 1 → 0 (expired)
     */
    @Test
    public void testScenario_simple_duration_tick() {
        System.out.println("\n=== SCENARIO A: Simple Duration Tick ===");

        // Create engine and level
        GameEngine engine = new GameEngine(new MockPlatform(), TEST_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();
        context.setLevel(createTestLevel(10, 10));

        // Create and add hero
        TestHero hero = new TestHero(new Point(5, 5), "Hero");
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(hero.getPosition(), true);
        context.registerActorForScheduling(hero, 0.0f);

        // Add buff with duration=3
        TestBuff buff = new TestBuff(3);
        hero.getBuffContainer().addBuff(buff, context, hero);

        System.out.println("Initial state: Buff duration = " + buff.getDuration());
        assertEquals(3, buff.getDuration(), "Initial duration should be 3");
        assertEquals(1, buff.onApplyCount, "onApply should be called once");
        assertEquals(1, hero.getBuffContainer().getBuffCount(), "Hero should have 1 buff");
        assertFalse(buff.isExpired(), "Buff should not be expired initially");

        // Turn 0 → Turn 1: Duration 3 → 2
        engine.processNextTurn(Collections.emptyList());
        System.out.println("After turn 1: Buff duration = " + buff.getDuration());
        assertEquals(2, buff.getDuration(), "Duration should be 2 after turn 1");
        assertEquals(1, buff.onTickCount, "onTick should be called once");
        assertEquals(1, hero.getBuffContainer().getBuffCount(), "Buff should still be active");
        assertFalse(buff.isExpired(), "Buff should not be expired");

        // Turn 1 → Turn 2: Duration 2 → 1
        engine.processNextTurn(Collections.emptyList());
        System.out.println("After turn 2: Buff duration = " + buff.getDuration());
        assertEquals(1, buff.getDuration(), "Duration should be 1 after turn 2");
        assertEquals(2, buff.onTickCount, "onTick should be called twice");
        assertEquals(1, hero.getBuffContainer().getBuffCount(), "Buff should still be active");
        assertFalse(buff.isExpired(), "Buff should not be expired");

        // Turn 2 → Turn 3: Duration 1 → 0 (expired and removed)
        engine.processNextTurn(Collections.emptyList());
        System.out.println("After turn 3: Buff duration = " + buff.getDuration() + ", expired = " + buff.isExpired());
        assertEquals(0, buff.getDuration(), "Duration should be 0 after turn 3");
        assertEquals(3, buff.onTickCount, "onTick should be called three times");
        assertEquals(1, buff.onRemoveCount, "onRemove should be called once");
        assertEquals(0, hero.getBuffContainer().getBuffCount(), "Buff should be removed");
        assertTrue(buff.isExpired(), "Buff should be expired");

        System.out.println("✓ Buff duration ticked correctly and expired");
    }

    // =========================================================================
    // Scenario B: Save/Load Restores Buffs
    // =========================================================================

    /**
     * Test that buffs persist correctly across save/load operations.
     *
     * Scenario:
     * 1. Create Hero
     * 2. Add TestBuff(duration=5)
     * 3. Run 2 turns → duration=3
     * 4. Save engine state
     * 5. Load into a new engine
     * 6. Run 1 more turn → duration=2
     * 7. Verify buff exists with correct duration
     * 8. Verify hash matches replay run
     */
    @Test
    public void testScenario_save_load_restores_buffs() {
        System.out.println("\n=== SCENARIO B: Save/Load Restores Buffs ===");

        final int INITIAL_DURATION = 5;
        final int TURNS_BEFORE_SAVE = 2;
        final int EXPECTED_DURATION_AFTER_SAVE = INITIAL_DURATION - TURNS_BEFORE_SAVE; // 3

        // Create engine and level
        GameEngine engine = new GameEngine(new MockPlatform(), TEST_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();
        context.setLevel(createTestLevel(10, 10));

        // Create and add hero
        TestHero hero = new TestHero(new Point(5, 5), "Hero");
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(hero.getPosition(), true);
        context.registerActorForScheduling(hero, 0.0f);

        // Add buff
        TestBuff buff = new TestBuff(INITIAL_DURATION);
        hero.getBuffContainer().addBuff(buff, context, hero);
        System.out.println("Initial buff duration: " + buff.getDuration());

        // Run 2 turns
        for (int i = 0; i < TURNS_BEFORE_SAVE; i++) {
            engine.processNextTurn(Collections.emptyList());
        }

        System.out.println("After " + TURNS_BEFORE_SAVE + " turns: Buff duration = " + buff.getDuration());
        assertEquals(EXPECTED_DURATION_AFTER_SAVE, buff.getDuration(),
            "Duration should be " + EXPECTED_DURATION_AFTER_SAVE + " before save");

        // SAVE
        byte[] savedData = engine.saveState();
        System.out.println("Saved engine state");

        // LOAD into new engine
        GameEngine loadedEngine = new GameEngine(new MockPlatform(), TEST_SEED);
        loadedEngine.initialize();
        loadedEngine.loadState(savedData);
        EngineContext loadedContext = loadedEngine.getContext();
        System.out.println("Loaded engine state");

        // Find hero in loaded context
        Character loadedHero = null;
        for (com.shatteredpixel.engine.actor.Actor actor : loadedContext.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                loadedHero = (Character) actor;
                break;
            }
        }

        assertNotNull(loadedHero, "Hero should exist in loaded context");

        // Verify buff was restored
        assertEquals(1, loadedHero.getBuffContainer().getBuffCount(),
            "Loaded hero should have 1 buff");

        Buff loadedBuff = loadedHero.getBuffContainer().getBuffs().get(0);
        assertEquals("testbuff", loadedBuff.getBuffType(), "Buff type should match");
        assertEquals(EXPECTED_DURATION_AFTER_SAVE, loadedBuff.getDuration(),
            "Loaded buff duration should be " + EXPECTED_DURATION_AFTER_SAVE);

        System.out.println("Loaded buff duration: " + loadedBuff.getDuration());

        // Run 1 more turn
        loadedEngine.processNextTurn(Collections.emptyList());
        System.out.println("After 1 more turn: Buff duration = " + loadedBuff.getDuration());
        assertEquals(EXPECTED_DURATION_AFTER_SAVE - 1, loadedBuff.getDuration(),
            "Duration should decrement after load");

        System.out.println("✓ Buffs persisted correctly across save/load");
    }

    // =========================================================================
    // Scenario C: Determinism Hash Test
    // =========================================================================

    /**
     * Test that buffs are deterministic and produce identical hashes in replay.
     *
     * Scenario:
     * 1. Hero with TestBuff(duration=4)
     * 2. Run 4 turns to expiration
     * 3. Capture TurnHashSequence via:
     *    - direct-run
     *    - replay-run
     * 4. All hashes must match exactly per turn
     */
    @Test
    public void testScenario_determinism_hash_test() {
        System.out.println("\n=== SCENARIO C: Determinism Hash Test ===");

        final int BUFF_DURATION = 4;
        final int TOTAL_TURNS = 4;

        // ===== DIRECT RUN =====
        System.out.println("Running direct-run...");
        TurnHashSequence directSeq = runBuffScenarioWithHashing(BUFF_DURATION, TOTAL_TURNS);

        // ===== REPLAY RUN =====
        System.out.println("Running replay-run...");
        TurnHashSequence replaySeq = runBuffScenarioReplay(BUFF_DURATION, TOTAL_TURNS);

        // ===== VERIFICATION =====
        System.out.println("\nVerifying hash sequences...");

        assertEquals(TOTAL_TURNS, directSeq.size(), "Direct run should have " + TOTAL_TURNS + " hashes");
        assertEquals(TOTAL_TURNS, replaySeq.size(), "Replay run should have " + TOTAL_TURNS + " hashes");

        // Check for desyncs
        int desync = directSeq.findFirstDifference(replaySeq);
        assertEquals(-1, desync, "No desync should exist between direct and replay");

        // Verify all hashes match
        assertTrue(directSeq.matchesHashes(replaySeq),
            "Direct and replay hash sequences must match exactly");

        // Print per-turn verification
        for (int turn = 0; turn < TOTAL_TURNS; turn++) {
            TurnHashRecord directRecord = directSeq.get(turn);
            TurnHashRecord replayRecord = replaySeq.get(turn);
            assertEquals(directRecord.getStateHash(), replayRecord.getStateHash(),
                "Turn " + turn + " hash must match");
            System.out.println("  Turn " + turn + ": " + directRecord.getStateHash().toHexString() + " ✓");
        }

        System.out.println("✓ Determinism verified: Direct and replay produce identical hashes");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    private LevelState createTestLevel(int width, int height) {
        LevelState level = new LevelState(1, width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                level.getGrid().setTerrain(x, y, TerrainType.FLOOR);
            }
        }
        return level;
    }

    private TurnHashSequence runBuffScenarioWithHashing(int buffDuration, int totalTurns) {
        GameEngine engine = new GameEngine(new MockPlatform(), TEST_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();
        context.setLevel(createTestLevel(10, 10));

        TestHero hero = new TestHero(new Point(5, 5), "Hero");
        context.addActor(hero);
        context.getLevel().getGrid().setOccupied(hero.getPosition(), true);
        context.registerActorForScheduling(hero, 0.0f);

        // Add buff
        TestBuff buff = new TestBuff(buffDuration);
        hero.getBuffContainer().addBuff(buff, context, hero);

        // Run turns and collect hashes
        List<TurnHashRecord> hashes = new ArrayList<>();
        for (int turn = 0; turn < totalTurns; turn++) {
            engine.processNextTurn(Collections.emptyList());
            StateHash hash = StateHasher.hashState(engine);
            hashes.add(new TurnHashRecord(turn, hash));
        }

        return new TurnHashSequence(hashes);
    }

    private TurnHashSequence runBuffScenarioReplay(int buffDuration, int totalTurns) {
        EngineFactory factory = seed -> {
            GameEngine engine = new GameEngine(new MockPlatform(), seed);
            engine.initialize();
            EngineContext context = engine.getContext();
            context.setLevel(createTestLevel(10, 10));

            TestHero hero = new TestHero(new Point(5, 5), "Hero");
            context.addActor(hero);
            context.getLevel().getGrid().setOccupied(hero.getPosition(), true);
            context.registerActorForScheduling(hero, 0.0f);

            // Add buff
            TestBuff buff = new TestBuff(buffDuration);
            hero.getBuffContainer().addBuff(buff, context, hero);

            return new EngineContextWithEngine(context, engine);
        };

        // Prepare empty command list (no external commands)
        List<List<GameCommand>> commandsPerTurn = new ArrayList<>();
        for (int i = 0; i < totalTurns; i++) {
            commandsPerTurn.add(Collections.emptyList());
        }

        // Record
        ReplayRecorder recorder = new ReplayRecorder();
        ReplayTrace trace = recorder.recordRun(TEST_SEED, totalTurns, commandsPerTurn, factory);

        // Replay with observer
        List<TurnHashRecord> hashes = new ArrayList<>();
        ReplayObserver observer = (turnIndex, context, engine, events) -> {
            StateHash hash = StateHasher.hashState(engine);
            hashes.add(new TurnHashRecord(turnIndex, hash));
        };

        ReplayRunner runner = new ReplayRunner();
        runner.replay(trace, factory, observer);

        return new TurnHashSequence(hashes);
    }
}
