package com.shatteredpixel.engine.actor.mob;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.command.GameCommand;
import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.stats.Stats;

/**
 * Simple melee mob for testing and demos.
 *
 * This is a minimal concrete mob implementation used only for:
 * - Unit tests
 * - Integration tests
 * - Demos/examples
 *
 * NOT intended as a production mob - just a test fixture.
 *
 * Behavior:
 * - Simple AI: moves towards nearest hero, attacks when adjacent
 * - Can be attacked and killed
 * - Uses default Stats if none provided
 * - Naive Manhattan movement (no pathfinding)
 * - Global awareness (no FOV/LOS restrictions yet)
 */
public class SimpleMeleeMob extends BaseMob {

    /**
     * Default stats for simple melee mobs.
     * - 20 HP (easy to kill in tests)
     * - 5 attack power (low damage)
     * - 2 defense (minimal armor)
     * - 10 accuracy/evasion (average)
     */
    private static final Stats DEFAULT_STATS = new Stats(
        20,   // maxHealth
        5,    // attackPower
        2,    // defense
        10,   // accuracy
        10,   // evasion
        1.0f  // speed
    );

    /**
     * Create a simple melee mob with default stats.
     *
     * @param position Initial position in the level
     */
    public SimpleMeleeMob(Point position) {
        super(position, DEFAULT_STATS.copy());
    }

    /**
     * Create a simple melee mob with custom stats.
     *
     * @param position Initial position
     * @param stats Custom stats
     */
    public SimpleMeleeMob(Point position, Stats stats) {
        super(position, stats);
    }

    /**
     * Create a simple melee mob with custom stats and health.
     *
     * @param position Initial position
     * @param stats Custom stats
     * @param currentHealth Current health value
     */
    public SimpleMeleeMob(Point position, Stats stats, int currentHealth) {
        super(position, stats, currentHealth);
    }

    /**
     * Decide the next action for this mob based on the current game state.
     *
     * AI logic (simple and deterministic):
     * 1. Find all heroes in the game
     * 2. If no heroes exist → return null (wait)
     * 3. Find nearest hero by Manhattan distance
     * 4. If adjacent to nearest hero (distance == 1) → ATTACK
     * 5. Otherwise → MOVE one step closer (naive Manhattan movement)
     * 6. If movement is blocked → return null (wait)
     *
     * Movement strategy:
     * - Prefers horizontal movement first, then vertical
     * - Validates: in bounds, passable, not occupied
     * - No pathfinding (just naive one-step approach)
     * - No FOV/LOS checks (global awareness for now)
     *
     * This method only decides an action; it does NOT execute it.
     * The caller must pass the returned GameCommand to GameEngine.tick().
     *
     * @param context Engine context providing access to actors, level, etc.
     * @return GameCommand to execute (MOVE or ATTACK), or null if no action
     */
    public GameCommand decideNextAction(EngineContext context) {
        // Get mob's current position
        Point mobPos = getPosition();
        if (mobPos == null) {
            return null; // Can't act without a position
        }

        // Find all heroes in the game
        Actor nearestHero = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (Actor actor : context.getActors().values()) {
            // Only consider HERO actors
            if (actor.getType() != ActorType.HERO) {
                continue;
            }

            // Skip actors without valid positions
            Point heroPos = actor.getPosition();
            if (heroPos == null) {
                continue;
            }

            // Calculate Manhattan distance
            int distance = Math.abs(mobPos.x - heroPos.x) + Math.abs(mobPos.y - heroPos.y);

            // Track nearest hero
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestHero = actor;
            }
        }

        // No heroes found → wait
        if (nearestHero == null) {
            return null;
        }

        Point heroPos = nearestHero.getPosition();

        // Adjacent to hero (distance == 1) → ATTACK
        if (nearestDistance == 1) {
            return GameCommand.attack(getId(), nearestHero.getId());
        }

        // Not adjacent → MOVE towards hero
        // Calculate direction (prefer horizontal first, then vertical)
        int dx = heroPos.x - mobPos.x;
        int dy = heroPos.y - mobPos.y;

        // Normalize to single step (-1, 0, or +1)
        int stepX = 0;
        int stepY = 0;

        if (dx != 0) {
            // Move horizontally first
            stepX = dx > 0 ? 1 : -1;
        } else if (dy != 0) {
            // Move vertically if no horizontal distance
            stepY = dy > 0 ? 1 : -1;
        }

        // Calculate target position
        Point targetPos = new Point(mobPos.x + stepX, mobPos.y + stepY);

        // Validate movement
        LevelState level = context.getLevel();
        if (level == null) {
            return null; // No level loaded
        }

        LevelGrid grid = level.getGrid();

        // Check bounds
        if (!grid.isInBounds(targetPos)) {
            return null; // Out of bounds
        }

        // Check passable
        if (!grid.isPassable(targetPos)) {
            return null; // Blocked by wall/terrain
        }

        // Check not occupied
        if (grid.isOccupied(targetPos)) {
            return null; // Blocked by another actor
        }

        // Valid move → issue MOVE command
        return GameCommand.moveTo(getId(), targetPos);
    }

    /**
     * Mob acts autonomously using AI.
     *
     * For SimpleMeleeMob, the AI decision logic is in decideNextAction().
     * However, this act() method is still a stub because we don't have a
     * turn scheduler integrated yet.
     *
     * For now, tests and future scheduler code should call decideNextAction()
     * and pass the resulting GameCommand to GameEngine.tick() externally.
     *
     * Future integration:
     * - Turn scheduler will call act() when it's this mob's turn
     * - act() will call decideNextAction() to get a command
     * - act() will somehow execute that command (design TBD)
     *
     * @param context Engine context
     * @return Time cost (1.0 = standard turn)
     */
    @Override
    public float act(EngineContext context) {
        // AI decision is in decideNextAction()
        // For now, just return time cost (scheduler not integrated yet)
        return 1.0f; // Standard time cost for waiting
    }

    /**
     * Called when this mob dies.
     *
     * For SimpleMeleeMob (test fixture), we don't need special death behavior.
     * Production mobs would override this to drop loot, award XP, etc.
     *
     * @param context Engine context
     */
    @Override
    protected void onDeath(EngineContext context) {
        // No special death behavior for test mob
        // Just rely on Character.takeDamage() emitting ACTOR_DIED event
    }

    @Override
    public String toString() {
        return String.format("SimpleMeleeMob{id=%s, pos=%s, %s}",
            getId(), getPosition(), getHealth());
    }
}
