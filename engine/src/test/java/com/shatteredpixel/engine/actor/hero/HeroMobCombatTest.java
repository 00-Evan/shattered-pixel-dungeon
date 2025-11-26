package com.shatteredpixel.engine.actor.hero;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
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
 * Integration tests for hero/mob combat interaction.
 *
 * Validates that:
 * - Heroes can attack mobs via ATTACK commands
 * - Mobs take damage and die correctly
 * - Events are emitted properly (DAMAGE_APPLIED, ACTOR_DIED)
 * - End-to-end combat flow works with command/event pipeline
 */
public class HeroMobCombatTest {

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
            // Default test hero stats: 100 HP, 50 attack
            this(position, new Stats(100, 50, 5, 10, 10, 1.0f), name);
        }

        @Override
        public float act(EngineContext context) {
            // Heroes are command-driven, not autonomous
            return 1.0f;
        }
    }

    @Before
    public void setUp() {
        // Create engine with mock platform and fixed seed for determinism
        engine = new GameEngine(new MockPlatform(), 12345L);
        engine.initialize();
        context = engine.getContext();

        // Create a 5x5 level filled with FLOOR
        level = new LevelState(1, 5, 5);
        grid = level.getGrid();
        grid.fill(TerrainType.FLOOR);

        // Set level in context
        context.setLevel(level);
    }

    /**
     * Test 1: Hero attacks mob and kills it in one hit.
     *
     * Scenario:
     * - Hero with high attack power (50)
     * - SimpleMeleeMob with low health (10 HP)
     * - Single ATTACK command
     *
     * Expected:
     * - Mob dies (isDead() == true)
     * - Mob health reaches 0
     * - DAMAGE_APPLIED event emitted
     * - ACTOR_DIED event emitted
     * - Hero remains alive and unharmed
     */
    @Test
    public void testHeroAttacksMobAndKillsIt() {
        // Setup: Strong hero, weak mob
        Point heroPos = new Point(2, 2);
        Point mobPos = new Point(3, 2);

        // Hero with 100 HP and 50 attack power
        TestHero hero = new TestHero(heroPos, "TestHero");

        // Mob with only 10 HP (easy to kill)
        Stats weakMobStats = new Stats(10, 5, 0, 10, 10, 1.0f);
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos, weakMobStats);

        // Register both actors
        context.addActor(hero);
        context.addActor(mob);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mobPos, true);

        // Verify initial state
        assertTrue("Hero should be alive", hero.isAlive());
        assertTrue("Mob should be alive initially", mob.isAlive());
        assertEquals("Mob should have 10 HP", 10, mob.getCurrentHealth());

        // Action: Hero attacks mob
        GameCommand attackCommand = GameCommand.attack(hero.getId(), mob.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions: Mob should be dead
        assertTrue("Mob should be dead", mob.isDead());
        assertFalse("Mob should not be alive", mob.isAlive());
        assertTrue("Mob health should be <= 0", mob.getCurrentHealth() <= 0);

        // Assertions: Hero should be unchanged
        assertTrue("Hero should still be alive", hero.isAlive());
        assertEquals("Hero health should be unchanged", 100, hero.getCurrentHealth());

        // Check events: DAMAGE_APPLIED
        long damageEvents = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .count();
        assertEquals("Should have one DAMAGE_APPLIED event", 1, damageEvents);

        GameEvent damageEvent = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .findFirst()
            .orElse(null);
        assertNotNull("DAMAGE_APPLIED event should exist", damageEvent);
        assertEquals("Damage event should have hero as source",
            hero.getId(), damageEvent.getSourceActorId());
        assertEquals("Damage event should have mob as target",
            mob.getId(), damageEvent.getTargetActorId());
        assertTrue("Damage should be > 0", damageEvent.getAmount() > 0);

        // Check events: ACTOR_DIED
        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have one ACTOR_DIED event", 1, diedEvents);

        GameEvent diedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .findFirst()
            .orElse(null);
        assertNotNull("ACTOR_DIED event should exist", diedEvent);
        assertEquals("Death event should have mob ID",
            mob.getId(), diedEvent.getSourceActorId());

        // Check events: LOG_MESSAGE mentioning death
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("killed"))
            .count();
        assertTrue("Should have LOG_MESSAGE about death", logEvents >= 1);
    }

    /**
     * Test 2: Hero attacks mob but doesn't kill it (non-lethal damage).
     *
     * Scenario:
     * - Hero with moderate attack power
     * - Mob with enough health to survive
     * - Single ATTACK command
     *
     * Expected:
     * - Mob takes damage but survives
     * - Mob health reduced but > 0
     * - DAMAGE_APPLIED event emitted
     * - NO ACTOR_DIED event
     */
    @Test
    public void testHeroAttacksMobNonLethal() {
        // Setup: Hero and mob with balanced stats
        Point heroPos = new Point(2, 2);
        Point mobPos = new Point(3, 2);

        // Hero with moderate attack (20 attack power)
        Stats heroStats = new Stats(100, 20, 0, 10, 10, 1.0f);
        TestHero hero = new TestHero(heroPos, heroStats, "TestHero");

        // Mob with enough health to survive (50 HP, 5 defense)
        Stats mobStats = new Stats(50, 5, 5, 10, 10, 1.0f);
        SimpleMeleeMob mob = new SimpleMeleeMob(mobPos, mobStats);

        // Register both actors
        context.addActor(hero);
        context.addActor(mob);
        grid.setOccupied(heroPos, true);
        grid.setOccupied(mobPos, true);

        int initialMobHealth = mob.getCurrentHealth();

        // Action: Hero attacks mob
        GameCommand attackCommand = GameCommand.attack(hero.getId(), mob.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions: Mob should be damaged but alive
        assertTrue("Mob should still be alive", mob.isAlive());
        assertFalse("Mob should not be dead", mob.isDead());
        assertTrue("Mob health should be reduced",
            mob.getCurrentHealth() < initialMobHealth);
        assertTrue("Mob health should be > 0", mob.getCurrentHealth() > 0);

        // Check events: DAMAGE_APPLIED
        long damageEvents = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .count();
        assertEquals("Should have one DAMAGE_APPLIED event", 1, damageEvents);

        // Check events: NO ACTOR_DIED
        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have NO ACTOR_DIED events", 0, diedEvents);
    }

    /**
     * Test 3: Multiple heroes and mobs on the same level.
     *
     * Scenario:
     * - Two heroes, two mobs
     * - Hero1 attacks Mob1
     * - Verify correct targeting (no friendly fire)
     *
     * Expected:
     * - Only targeted mob takes damage
     * - Other actors unaffected
     * - Correct events with proper actorIds
     */
    @Test
    public void testMultipleHeroesAndMobs() {
        // Setup: Two heroes and two mobs
        Point hero1Pos = new Point(1, 1);
        Point hero2Pos = new Point(1, 3);
        Point mob1Pos = new Point(2, 1);
        Point mob2Pos = new Point(2, 3);

        TestHero hero1 = new TestHero(hero1Pos, "Hero1");
        TestHero hero2 = new TestHero(hero2Pos, "Hero2");

        Stats weakMobStats = new Stats(10, 5, 0, 10, 10, 1.0f);
        SimpleMeleeMob mob1 = new SimpleMeleeMob(mob1Pos, weakMobStats);
        SimpleMeleeMob mob2 = new SimpleMeleeMob(mob2Pos, weakMobStats.copy());

        // Register all actors
        context.addActor(hero1);
        context.addActor(hero2);
        context.addActor(mob1);
        context.addActor(mob2);

        // Action: Hero1 attacks Mob1
        GameCommand attackCommand = GameCommand.attack(hero1.getId(), mob1.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions: Only Mob1 should be affected
        assertTrue("Mob1 should be dead", mob1.isDead());
        assertTrue("Mob2 should still be alive", mob2.isAlive());
        assertEquals("Mob2 health should be unchanged", 10, mob2.getCurrentHealth());

        assertTrue("Hero1 should be alive", hero1.isAlive());
        assertTrue("Hero2 should be alive", hero2.isAlive());
        assertEquals("Hero1 health unchanged", 100, hero1.getCurrentHealth());
        assertEquals("Hero2 health unchanged", 100, hero2.getCurrentHealth());

        // Verify correct event targeting
        GameEvent damageEvent = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .findFirst()
            .orElse(null);
        assertNotNull("Should have damage event", damageEvent);
        assertEquals("Damage from hero1", hero1.getId(), damageEvent.getSourceActorId());
        assertEquals("Damage to mob1", mob1.getId(), damageEvent.getTargetActorId());

        GameEvent diedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .findFirst()
            .orElse(null);
        assertNotNull("Should have death event", diedEvent);
        assertEquals("Death of mob1", mob1.getId(), diedEvent.getSourceActorId());
    }

    /**
     * Test 4: Verify hero and mob actor types.
     *
     * Expected:
     * - Hero has ActorType.HERO
     * - Mob has ActorType.MOB
     * - Can filter actors by type
     */
    @Test
    public void testActorTypes() {
        // Setup
        TestHero hero = new TestHero(new Point(0, 0), "TestHero");
        SimpleMeleeMob mob = new SimpleMeleeMob(new Point(1, 0));

        context.addActor(hero);
        context.addActor(mob);

        // Assertions: Check actor types
        assertEquals("Hero should have HERO type",
            com.shatteredpixel.engine.actor.ActorType.HERO, hero.getType());
        assertEquals("Mob should have MOB type",
            com.shatteredpixel.engine.actor.ActorType.MOB, mob.getType());

        assertTrue("Hero type should be a character", hero.getType().isCharacter());
        assertTrue("Mob type should be a character", mob.getType().isCharacter());
        assertTrue("Mob type should be hostile", mob.getType().isHostile());
        assertFalse("Hero type should not be hostile", hero.getType().isHostile());

        // Test filtering by type
        long heroCount = context.getActors().values().stream()
            .filter(a -> a.getType() == com.shatteredpixel.engine.actor.ActorType.HERO)
            .count();
        long mobCount = context.getActors().values().stream()
            .filter(a -> a.getType() == com.shatteredpixel.engine.actor.ActorType.MOB)
            .count();

        assertEquals("Should have 1 hero", 1, heroCount);
        assertEquals("Should have 1 mob", 1, mobCount);
    }
}
