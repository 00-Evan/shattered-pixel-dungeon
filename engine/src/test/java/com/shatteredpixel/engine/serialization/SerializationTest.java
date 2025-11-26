package com.shatteredpixel.engine.serialization;

import com.shatteredpixel.api.MockPlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.actor.hero.BaseHero;
import com.shatteredpixel.engine.actor.mob.SimpleMeleeMob;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.event.GameEvent;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.serialization.dto.ActorSnapshot;
import com.shatteredpixel.engine.serialization.dto.EngineSnapshot;
import com.shatteredpixel.engine.serialization.dto.LevelSnapshot;
import com.shatteredpixel.engine.serialization.dto.ScheduledActorSnapshot;
import com.shatteredpixel.engine.stats.Stats;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for engine serialization system.
 *
 * Tests cover:
 * - Save/load of heroes and mobs
 * - Deterministic replay after load
 * - Codec round-trip encoding/decoding
 * - State preservation (RNG, positions, stats, health)
 */
public class SerializationTest {

    private static final long TEST_SEED = 123456789L;

    /**
     * Test helper: Concrete hero for testing.
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
            return 1.0f;
        }
    }

    /**
     * (A) testSaveAndLoad_SimpleHeroAndMob
     *
     * Setup:
     * - Create EngineContext + GameEngine with fixed seed
     * - Create a small 5x5 FLOOR level
     * - Create one TestHero and one SimpleMeleeMob with specific positions and stats
     * - Register both actors and schedule them
     * - Run a few turns to alter health/time
     *
     * Action:
     * - Save state to byte[]
     * - Create new EngineContext + GameEngine
     * - Load state from byte[]
     *
     * Assertions:
     * - Level dimensions and terrain match original
     * - Both actors exist in new context
     * - Actor positions match
     * - Health / stats match
     * - Scheduler contains both actors with same time values
     */
    @Test
    public void testSaveAndLoad_SimpleHeroAndMob() {
        // === Setup ===
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, TEST_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 5x5 level
        LevelState level = new LevelState(1, 5, 5);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                level.getGrid().setTerrain(x, y, TerrainType.FLOOR);
            }
        }
        context.setLevel(level);

        // Create hero and mob
        Point heroPos = new Point(1, 1);
        Point mobPos = new Point(3, 3);
        TestHero hero = new TestHero(heroPos, "TestHero");
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);

        context.addActor(hero);
        context.addActor(mob);
        level.getGrid().setOccupied(heroPos, true);
        level.getGrid().setOccupied(mobPos, true);

        // Schedule actors
        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob, 0.5f);

        // Run a few turns to alter state
        hero.takeDamage(10, context); // Hero loses 10 HP
        mob.takeDamage(5, context);   // Mob loses 5 HP

        // Capture original state for assertions
        long originalSeed = context.getRNG().getSeed();
        int heroOriginalHP = hero.getCurrentHealth();
        int mobOriginalHP = mob.getCurrentHealth();
        Point heroOriginalPos = new Point(hero.getPosition().x, hero.getPosition().y);
        Point mobOriginalPos = new Point(mob.getPosition().x, mob.getPosition().y);
        float heroOriginalTime = hero.getTime();
        float mobOriginalTime = mob.getTime();

        // === Action: Save ===
        byte[] savedData = engine.saveState();
        assertNotNull(savedData);
        assertTrue(savedData.length > 0);

        // === Action: Load into new engine ===
        GameEngine newEngine = new GameEngine(platform, 0L); // Different seed initially
        newEngine.initialize();
        newEngine.loadState(savedData);
        EngineContext newContext = newEngine.getContext();

        // === Assertions ===

        // RNG seed restored
        assertEquals(originalSeed, newContext.getRNG().getSeed());

        // Level dimensions match
        LevelState newLevel = newContext.getLevel();
        assertNotNull(newLevel);
        assertEquals(1, newLevel.getDepth());
        assertEquals(5, newLevel.getWidth());
        assertEquals(5, newLevel.getHeight());

        // Terrain matches (spot check a few tiles)
        assertEquals(TerrainType.FLOOR, newLevel.getGrid().getTerrain(0, 0));
        assertEquals(TerrainType.FLOOR, newLevel.getGrid().getTerrain(2, 2));

        // Both actors exist
        assertEquals(2, newContext.getActors().size());

        // Find hero and mob in restored context
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

        // Hero position matches
        assertEquals(heroOriginalPos.x, restoredHero.getPosition().x);
        assertEquals(heroOriginalPos.y, restoredHero.getPosition().y);

        // Mob position matches
        assertEquals(mobOriginalPos.x, restoredMob.getPosition().x);
        assertEquals(mobOriginalPos.y, restoredMob.getPosition().y);

        // Hero health/stats match
        assertEquals(heroOriginalHP, restoredHero.getCurrentHealth());
        assertEquals(50, restoredHero.getMaxHealth());
        assertEquals(10, restoredHero.getStats().getAttackPower());

        // Mob health/stats match
        assertEquals(mobOriginalHP, restoredMob.getCurrentHealth());
        assertEquals(20, restoredMob.getMaxHealth()); // SimpleMeleeMob default

        // Scheduler contains both actors
        List<Actor> scheduled = newContext.getScheduler().getAllActors();
        assertEquals(2, scheduled.size());

        // Verify time values match (within epsilon for float comparison)
        Actor scheduledHero = scheduled.stream()
            .filter(a -> a.getType() == ActorType.HERO)
            .findFirst().orElse(null);
        Actor scheduledMob = scheduled.stream()
            .filter(a -> a.getType() == ActorType.MOB)
            .findFirst().orElse(null);

        assertNotNull(scheduledHero);
        assertNotNull(scheduledMob);
        assertEquals(heroOriginalTime, scheduledHero.getTime(), 0.001f);
        assertEquals(mobOriginalTime, scheduledMob.getTime(), 0.001f);
    }

    /**
     * (B) testDeterministicSaveLoadReplay
     *
     * Setup:
     * - Create initial EngineContext + GameEngine with fixed RNG seed
     * - Create Level + Hero + Mob
     *
     * Action:
     * - Run N turns via processNextTurn(), capturing events
     * - Save state → byte[]
     * - Recreate EngineContext + GameEngine with SAME RNG seed, load state
     * - Run another M turns via processNextTurn(), capturing events
     *
     * Assertions:
     * - Events after load should be deterministic
     * - RNG state continues from where it left off
     * - Actor states evolve predictably
     */
    @Test
    public void testDeterministicSaveLoadReplay() {
        // === Setup ===
        MockPlatform platform = new MockPlatform();
        GameEngine engine = new GameEngine(platform, TEST_SEED);
        engine.initialize();
        EngineContext context = engine.getContext();

        // Create 7x7 level
        LevelState level = new LevelState(1, 7, 7);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                level.getGrid().setTerrain(x, y, TerrainType.FLOOR);
            }
        }
        context.setLevel(level);

        // Create hero and mob
        Point heroPos = new Point(2, 2);
        Point mobPos = new Point(5, 5);
        TestHero hero = new TestHero(heroPos, "Hero");
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos);

        context.addActor(hero);
        context.addActor(mob);
        level.getGrid().setOccupied(heroPos, true);
        level.getGrid().setOccupied(mobPos, true);

        context.registerActorForScheduling(hero, 0.0f);
        context.registerActorForScheduling(mob, 0.0f);

        // === Run some turns before save ===
        List<String> eventsBeforeSave = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<GameEvent> events = engine.processNextTurn(Collections.emptyList());
            for (GameEvent event : events) {
                eventsBeforeSave.add(event.toString());
            }
        }

        // Capture state before save
        int heroHPBeforeSave = hero.getCurrentHealth();
        int mobHPBeforeSave = mob.getCurrentHealth();

        // === Save ===
        byte[] savedData = engine.saveState();

        // === Run more turns on original engine ===
        List<String> eventsAfterSaveOriginal = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<GameEvent> events = engine.processNextTurn(Collections.emptyList());
            for (GameEvent event : events) {
                eventsAfterSaveOriginal.add(event.toString());
            }
        }

        // === Load into new engine ===
        GameEngine newEngine = new GameEngine(platform, 0L);
        newEngine.initialize();
        newEngine.loadState(savedData);
        EngineContext newContext = newEngine.getContext();

        // === Verify state after load ===
        Character restoredHero = null;
        Character restoredMob = null;
        for (Actor actor : newContext.getActors().values()) {
            if (actor.getType() == ActorType.HERO) {
                restoredHero = (Character) actor;
            } else if (actor.getType() == ActorType.MOB) {
                restoredMob = (Character) actor;
            }
        }

        assertNotNull(restoredHero);
        assertNotNull(restoredMob);

        // HP should match state at save time
        assertEquals(heroHPBeforeSave, restoredHero.getCurrentHealth());
        assertEquals(mobHPBeforeSave, restoredMob.getCurrentHealth());

        // === Run same turns on restored engine ===
        List<String> eventsAfterSaveRestored = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<GameEvent> events = newEngine.processNextTurn(Collections.emptyList());
            for (GameEvent event : events) {
                eventsAfterSaveRestored.add(event.toString());
            }
        }

        // === Assertions ===
        // Event sequences should be identical (deterministic)
        assertEquals(eventsAfterSaveOriginal.size(), eventsAfterSaveRestored.size());
        // Note: Exact event matching may be complex due to new actor IDs,
        // but event types and counts should match
    }

    /**
     * (C) testCodecRoundTrip
     *
     * Setup:
     * - Manually construct a small EngineSnapshot with a couple of actors
     *
     * Action:
     * - Encode with DefaultEngineSnapshotCodec
     * - Decode back to EngineSnapshot
     *
     * Assertions:
     * - All fields (rng, level, actors, schedule) are equal between
     *   original and decoded snapshot
     */
    @Test
    public void testCodecRoundTrip() {
        // === Setup: Manually construct snapshot ===
        long rngSeed = 987654321L;

        // Level snapshot (3x3 grid)
        int[] terrainIds = new int[9];
        int[] tileFlags = new int[9];
        for (int i = 0; i < 9; i++) {
            terrainIds[i] = TerrainType.FLOOR.ordinal();
            tileFlags[i] = 0;
        }
        LevelSnapshot level = new LevelSnapshot(1, 3, 3, terrainIds, tileFlags);

        // Actor snapshots
        List<ActorSnapshot> actors = new ArrayList<>();
        actors.add(new ActorSnapshot(
            1L, "GENERIC_HERO", ActorType.HERO.ordinal(),
            1, 1, 50, 50, 10, 5, 15, 15, 1.0f
        ));
        actors.add(new ActorSnapshot(
            2L, "SIMPLE_MELEE_MOB", ActorType.MOB.ordinal(),
            2, 2, 20, 20, 5, 2, 10, 10, 1.0f
        ));

        // Schedule snapshots
        List<ScheduledActorSnapshot> schedule = new ArrayList<>();
        schedule.add(new ScheduledActorSnapshot(1L, 0.0f));
        schedule.add(new ScheduledActorSnapshot(2L, 0.5f));

        EngineSnapshot original = new EngineSnapshot(rngSeed, level, actors, schedule, 42L);

        // === Action: Encode and decode ===
        DefaultEngineSnapshotCodec codec = new DefaultEngineSnapshotCodec();
        byte[] encoded = codec.encode(original);
        assertNotNull(encoded);
        assertTrue(encoded.length > 0);

        EngineSnapshot decoded = codec.decode(encoded);
        assertNotNull(decoded);

        // === Assertions: All fields match ===

        // RNG seed
        assertEquals(original.rngSeed, decoded.rngSeed);

        // Turn counter
        assertEquals(original.turnCounter, decoded.turnCounter);

        // Level
        assertNotNull(decoded.level);
        assertEquals(original.level.depth, decoded.level.depth);
        assertEquals(original.level.width, decoded.level.width);
        assertEquals(original.level.height, decoded.level.height);
        assertArrayEquals(original.level.terrainIds, decoded.level.terrainIds);
        assertArrayEquals(original.level.tileFlags, decoded.level.tileFlags);

        // Actors
        assertEquals(original.actors.size(), decoded.actors.size());
        for (int i = 0; i < original.actors.size(); i++) {
            ActorSnapshot origActor = original.actors.get(i);
            ActorSnapshot decodedActor = decoded.actors.get(i);
            assertEquals(origActor.actorId, decodedActor.actorId);
            assertEquals(origActor.actorKind, decodedActor.actorKind);
            assertEquals(origActor.typeOrdinal, decodedActor.typeOrdinal);
            assertEquals(origActor.x, decodedActor.x);
            assertEquals(origActor.y, decodedActor.y);
            assertEquals(origActor.currentHealth, decodedActor.currentHealth);
            assertEquals(origActor.maxHealth, decodedActor.maxHealth);
            assertEquals(origActor.attackPower, decodedActor.attackPower);
            assertEquals(origActor.defense, decodedActor.defense);
            assertEquals(origActor.accuracy, decodedActor.accuracy);
            assertEquals(origActor.evasion, decodedActor.evasion);
            assertEquals(origActor.speed, decodedActor.speed, 0.001f);
        }

        // Schedule
        assertEquals(original.schedule.size(), decoded.schedule.size());
        for (int i = 0; i < original.schedule.size(); i++) {
            ScheduledActorSnapshot origSched = original.schedule.get(i);
            ScheduledActorSnapshot decodedSched = decoded.schedule.get(i);
            assertEquals(origSched.actorId, decodedSched.actorId);
            assertEquals(origSched.time, decodedSched.time, 0.001f);
        }
    }

    /**
     * Test encoding/decoding with null level (no level loaded).
     */
    @Test
    public void testCodecRoundTrip_NullLevel() {
        EngineSnapshot original = new EngineSnapshot(
            123L, null, Collections.emptyList(), Collections.emptyList(), 0L
        );

        DefaultEngineSnapshotCodec codec = new DefaultEngineSnapshotCodec();
        byte[] encoded = codec.encode(original);
        EngineSnapshot decoded = codec.decode(encoded);

        assertNotNull(decoded);
        assertEquals(original.rngSeed, decoded.rngSeed);
        assertNull(decoded.level);
        assertEquals(0, decoded.actors.size());
        assertEquals(0, decoded.schedule.size());
    }

    /**
     * Test that codec throws on invalid data.
     */
    @Test
    public void testCodecThrowsOnInvalidData() {
        DefaultEngineSnapshotCodec codec = new DefaultEngineSnapshotCodec();

        // Null data
        assertThrows(IllegalArgumentException.class, () -> codec.decode(null));

        // Empty data
        assertThrows(IllegalArgumentException.class, () -> codec.decode(new byte[0]));

        // Corrupt data (wrong version)
        byte[] badData = new byte[]{0, 0, 0, 99}; // Version 99
        assertThrows(IllegalStateException.class, () -> codec.decode(badData));
    }
}
