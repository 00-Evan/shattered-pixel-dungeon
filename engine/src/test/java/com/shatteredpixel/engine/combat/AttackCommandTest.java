package com.shatteredpixel.engine.combat;

import com.shatteredpixel.api.GamePlatform;
import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.command.GameCommand;
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
 * Tests for ATTACK command processing and combat integration.
 *
 * Validates that:
 * - ATTACK commands are processed correctly with CombatFormula
 * - Damage is applied to defender's health
 * - Death is detected and ACTOR_DIED events are emitted
 * - Misses are handled correctly
 * - Invalid attacks are rejected with appropriate error messages
 * - GameEvents are emitted correctly
 */
public class AttackCommandTest {

    private GameEngine engine;
    private EngineContext context;

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
     * Minimal Character implementation for testing.
     */
    private static class TestCharacter extends Character {
        public TestCharacter(Point position, Stats stats) {
            super(ActorType.OTHER, position, stats);
        }

        @Override
        public float act(EngineContext context) {
            return 1.0f;
        }
    }

    /**
     * Non-Character Actor for testing invalid attacks.
     */
    private static class TestActor extends Actor {
        public TestActor(Point position) {
            super(ActorType.OTHER, position);
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
    }

    /**
     * Test 1: Successful hit with damage.
     *
     * Scenario:
     * - Attacker with 10 attack power
     * - Defender with 50 HP, 0 defense
     * - Attack hits and deals damage
     *
     * Expected:
     * - Defender health reduced by expected amount
     * - DAMAGE_APPLIED event emitted with correct actorIds and damage
     * - No ACTOR_DIED event (damage < maxHealth)
     * - LOG_MESSAGE event describing the hit
     */
    @Test
    public void testSuccessfulHit() {
        // Setup: Attacker with 10 attack, Defender with 50 HP
        Stats attackerStats = new Stats(100, 10, 0, 0, 0, 1.0f);
        Stats defenderStats = new Stats(50, 0, 0, 0, 0, 1.0f);

        TestCharacter attacker = new TestCharacter(new Point(0, 0), attackerStats);
        TestCharacter defender = new TestCharacter(new Point(1, 0), defenderStats);

        context.addActor(attacker);
        context.addActor(defender);

        int initialHealth = defender.getCurrentHealth();

        // Action: Attack
        GameCommand attackCommand = GameCommand.attack(attacker.getId(), defender.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertTrue("Defender health should be reduced",
            defender.getCurrentHealth() < initialHealth);
        assertTrue("Defender should still be alive", defender.isAlive());
        assertFalse("Defender should not be dead", defender.isDead());

        // Check for DAMAGE_APPLIED event
        long damageEvents = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .count();
        assertEquals("Should have one DAMAGE_APPLIED event", 1, damageEvents);

        GameEvent damageEvent = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .findFirst()
            .orElse(null);
        assertNotNull("DAMAGE_APPLIED event should exist", damageEvent);
        assertEquals("Event should have correct attacker ID",
            attacker.getId(), damageEvent.getSourceActorId());
        assertEquals("Event should have correct defender ID",
            defender.getId(), damageEvent.getTargetActorId());
        assertTrue("Damage amount should be > 0", damageEvent.getAmount() > 0);

        // Check NO ACTOR_DIED event
        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have no ACTOR_DIED events", 0, diedEvents);

        // Check for LOG_MESSAGE
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .count();
        assertTrue("Should have at least one LOG_MESSAGE", logEvents >= 1);
    }

    /**
     * Test 2: Lethal hit - defender dies.
     *
     * Scenario:
     * - Attacker with 100 attack power
     * - Defender with 10 HP, 0 defense
     * - Single attack kills defender
     *
     * Expected:
     * - Defender isDead() == true
     * - Defender health <= 0
     * - DAMAGE_APPLIED event emitted
     * - ACTOR_DIED event emitted with correct defenderId
     * - LOG_MESSAGE indicating death
     */
    @Test
    public void testLethalHit() {
        // Setup: Strong attacker, weak defender
        Stats attackerStats = new Stats(100, 100, 0, 0, 0, 1.0f);
        Stats defenderStats = new Stats(10, 0, 0, 0, 0, 1.0f);

        TestCharacter attacker = new TestCharacter(new Point(0, 0), attackerStats);
        TestCharacter defender = new TestCharacter(new Point(1, 0), defenderStats);

        context.addActor(attacker);
        context.addActor(defender);

        // Action: Attack
        GameCommand attackCommand = GameCommand.attack(attacker.getId(), defender.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertTrue("Defender should be dead", defender.isDead());
        assertFalse("Defender should not be alive", defender.isAlive());
        assertTrue("Defender health should be <= 0", defender.getCurrentHealth() <= 0);

        // Check for DAMAGE_APPLIED event
        long damageEvents = events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
            .count();
        assertEquals("Should have one DAMAGE_APPLIED event", 1, damageEvents);

        // Check for ACTOR_DIED event
        long diedEvents = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .count();
        assertEquals("Should have one ACTOR_DIED event", 1, diedEvents);

        GameEvent diedEvent = events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED)
            .findFirst()
            .orElse(null);
        assertNotNull("ACTOR_DIED event should exist", diedEvent);
        assertEquals("ACTOR_DIED should have correct actor ID",
            defender.getId(), diedEvent.getSourceActorId());

        // Check for LOG_MESSAGE mentioning death/killed
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("killed"))
            .count();
        assertTrue("Should have LOG_MESSAGE about death", logEvents >= 1);
    }

    /**
     * Test 3: Missed attack.
     *
     * Scenario:
     * - Attacker with very low accuracy (0)
     * - Defender with very high evasion (100)
     * - Attack should miss (or use many attempts until one misses)
     *
     * Expected:
     * - Defender health unchanged
     * - No DAMAGE_APPLIED event
     * - LOG_MESSAGE indicating "miss"
     */
    @Test
    public void testMissedAttack() {
        // Setup: Low accuracy attacker, high evasion defender
        // With accuracy 0 and evasion 100, the difference is -100
        // Hit chance = 1.0 + (-100 * 0.05) = 1.0 - 5.0 = -4.0, clamped to 0.10 (10%)
        // This gives us a 90% miss chance, so we should see misses
        Stats attackerStats = new Stats(100, 10, 0, 0, 100, 1.0f);
        Stats defenderStats = new Stats(50, 0, 0, 0, 100, 1.0f);

        TestCharacter attacker = new TestCharacter(new Point(0, 0), attackerStats);
        TestCharacter defender = new TestCharacter(new Point(1, 0), defenderStats);

        context.addActor(attacker);
        context.addActor(defender);

        // Try multiple attacks to ensure we get at least one miss
        boolean gotMiss = false;
        for (int attempt = 0; attempt < 20; attempt++) {
            int healthBefore = defender.getCurrentHealth();

            GameCommand attackCommand = GameCommand.attack(attacker.getId(), defender.getId());
            List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

            // Check if this was a miss
            long damageEvents = events.stream()
                .filter(e -> e.getType() == EventType.DAMAGE_APPLIED)
                .count();

            if (damageEvents == 0) {
                // This was a miss!
                gotMiss = true;

                // Verify health unchanged
                assertEquals("Health should be unchanged on miss",
                    healthBefore, defender.getCurrentHealth());

                // Check for LOG_MESSAGE about miss
                long logEvents = events.stream()
                    .filter(e -> e.getType() == EventType.LOG_MESSAGE)
                    .filter(e -> e.getMessage().contains("miss"))
                    .count();
                assertTrue("Should have LOG_MESSAGE about miss", logEvents >= 1);

                // Check NO ACTOR_DIED
                long diedEvents = events.stream()
                    .filter(e -> e.getType() == EventType.ACTOR_DIED)
                    .count();
                assertEquals("Should have no ACTOR_DIED on miss", 0, diedEvents);

                break;
            }

            // If defender died, stop testing
            if (defender.isDead()) {
                break;
            }
        }

        assertTrue("Should have gotten at least one miss in 20 attempts", gotMiss);
    }

    /**
     * Test 4: Invalid ATTACK - missing actorId.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about missing actorId
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testMissingActorId() {
        // Setup: Create a defender
        TestCharacter defender = new TestCharacter(new Point(1, 0),
            new Stats(50, 0, 0, 0, 0, 1.0f));
        context.addActor(defender);

        // Action: Attack with null actorId
        GameCommand attackCommand = new GameCommand(
            com.shatteredpixel.engine.command.CommandType.ATTACK,
            null, // null actorId
            defender.getId(),
            null,
            null,
            null
        );
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about missing actorId
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("missing actorId"))
            .count();
        assertTrue("Should have LOG_MESSAGE about missing actorId", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());

        // Defender health unchanged
        assertEquals("Defender health should be unchanged", 50, defender.getCurrentHealth());
    }

    /**
     * Test 5: Invalid ATTACK - missing targetActorId.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about missing targetActorId
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testMissingTargetActorId() {
        // Setup: Create an attacker
        TestCharacter attacker = new TestCharacter(new Point(0, 0),
            new Stats(100, 10, 0, 0, 0, 1.0f));
        context.addActor(attacker);

        // Action: Attack with null targetActorId
        GameCommand attackCommand = new GameCommand(
            com.shatteredpixel.engine.command.CommandType.ATTACK,
            attacker.getId(),
            null, // null targetActorId
            null,
            null,
            null
        );
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about missing targetActorId
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("missing targetActorId"))
            .count();
        assertTrue("Should have LOG_MESSAGE about missing targetActorId", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());
    }

    /**
     * Test 6: Invalid ATTACK - attacker not found.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about attacker not found
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testAttackerNotFound() {
        // Setup: Create a defender only
        TestCharacter defender = new TestCharacter(new Point(1, 0),
            new Stats(50, 0, 0, 0, 0, 1.0f));
        context.addActor(defender);

        // Action: Attack with non-existent attacker
        ActorId fakeAttackerId = ActorId.generate();
        GameCommand attackCommand = GameCommand.attack(fakeAttackerId, defender.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about attacker not found
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("attacker") && e.getMessage().contains("not found"))
            .count();
        assertTrue("Should have LOG_MESSAGE about attacker not found", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());

        // Defender health unchanged
        assertEquals("Defender health should be unchanged", 50, defender.getCurrentHealth());
    }

    /**
     * Test 7: Invalid ATTACK - defender not found.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about defender not found
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testDefenderNotFound() {
        // Setup: Create an attacker only
        TestCharacter attacker = new TestCharacter(new Point(0, 0),
            new Stats(100, 10, 0, 0, 0, 1.0f));
        context.addActor(attacker);

        // Action: Attack non-existent defender
        ActorId fakeDefenderId = ActorId.generate();
        GameCommand attackCommand = GameCommand.attack(attacker.getId(), fakeDefenderId);
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about defender not found
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("defender") && e.getMessage().contains("not found"))
            .count();
        assertTrue("Should have LOG_MESSAGE about defender not found", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());
    }

    /**
     * Test 8: Invalid ATTACK - attacker is not a Character.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about attacker not being a Character
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testAttackerNotCharacter() {
        // Setup: Non-Character attacker, Character defender
        TestActor attacker = new TestActor(new Point(0, 0));
        TestCharacter defender = new TestCharacter(new Point(1, 0),
            new Stats(50, 0, 0, 0, 0, 1.0f));

        context.addActor(attacker);
        context.addActor(defender);

        // Action: Try to attack
        GameCommand attackCommand = GameCommand.attack(attacker.getId(), defender.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about attacker not a Character
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("attacker") && e.getMessage().contains("not a Character"))
            .count();
        assertTrue("Should have LOG_MESSAGE about attacker not a Character", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());

        // Defender health unchanged
        assertEquals("Defender health should be unchanged", 50, defender.getCurrentHealth());
    }

    /**
     * Test 9: Invalid ATTACK - defender is not a Character.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about defender not being a Character
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testDefenderNotCharacter() {
        // Setup: Character attacker, non-Character defender
        TestCharacter attacker = new TestCharacter(new Point(0, 0),
            new Stats(100, 10, 0, 0, 0, 1.0f));
        TestActor defender = new TestActor(new Point(1, 0));

        context.addActor(attacker);
        context.addActor(defender);

        // Action: Try to attack
        GameCommand attackCommand = GameCommand.attack(attacker.getId(), defender.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about defender not a Character
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("defender") && e.getMessage().contains("not a Character"))
            .count();
        assertTrue("Should have LOG_MESSAGE about defender not a Character", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());
    }

    /**
     * Test 10: Invalid ATTACK - attacker and defender are the same.
     *
     * Expected:
     * - No crash
     * - LOG_MESSAGE about attacker and defender being the same
     * - No DAMAGE_APPLIED or ACTOR_DIED events
     */
    @Test
    public void testSelfAttack() {
        // Setup: Single character
        TestCharacter character = new TestCharacter(new Point(0, 0),
            new Stats(50, 10, 0, 0, 0, 1.0f));
        context.addActor(character);

        int initialHealth = character.getCurrentHealth();

        // Action: Try to attack self
        GameCommand attackCommand = GameCommand.attack(character.getId(), character.getId());
        List<GameEvent> events = engine.tick(Collections.singletonList(attackCommand));

        // Assertions
        assertNotNull("Events should not be null", events);

        // Check for LOG_MESSAGE about same attacker/defender
        long logEvents = events.stream()
            .filter(e -> e.getType() == EventType.LOG_MESSAGE)
            .filter(e -> e.getMessage().contains("same"))
            .count();
        assertTrue("Should have LOG_MESSAGE about same attacker/defender", logEvents >= 1);

        // No damage or death events
        assertEquals("Should have no DAMAGE_APPLIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.DAMAGE_APPLIED).count());
        assertEquals("Should have no ACTOR_DIED", 0, events.stream()
            .filter(e -> e.getType() == EventType.ACTOR_DIED).count());

        // Health unchanged
        assertEquals("Health should be unchanged", initialHealth, character.getCurrentHealth());
    }
}
