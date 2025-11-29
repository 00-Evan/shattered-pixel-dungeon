package com.shatteredpixel.engine.actor;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.buff.BuffContainer;
import com.shatteredpixel.engine.geom.Point;

/**
 * Abstract base class for all actors (entities in the game world).
 * An actor is anything that:
 * - Exists at a position
 * - Can be updated on engine ticks
 * - Participates in turn-based scheduling
 *
 * Design goals:
 * - Generic and game-agnostic
 * - No references to Hero, Mob, Buff, Dungeon specifics
 * - Supports time-based scheduling
 * - Deterministic behavior
 *
 * Future migration:
 * - Legacy com.shatteredpixel.shatteredpixeldungeon.actors.Actor
 * - Legacy com.shatteredpixel.shatteredpixeldungeon.actors.Char
 */
public abstract class Actor {

    private final ActorId id;
    private final ActorType type;
    private Point position;
    private float time; // Time until this actor's next action
    private final BuffContainer buffContainer; // Manages temporary effects on this actor

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a new actor.
     *
     * @param type Actor type classification
     * @param position Initial position
     */
    protected Actor(ActorType type, Point position) {
        this.id = ActorId.generate();
        this.type = type;
        this.position = position;
        this.time = 0f;
        this.buffContainer = new BuffContainer();
    }

    // =========================================================================
    // Identity
    // =========================================================================

    /**
     * Get this actor's unique identifier.
     */
    public final ActorId getId() {
        return id;
    }

    /**
     * Get this actor's type classification.
     */
    public final ActorType getType() {
        return type;
    }

    /**
     * Get this actor's buff container.
     * Use this to add, remove, or query buffs on this actor.
     */
    public final BuffContainer getBuffContainer() {
        return buffContainer;
    }

    // =========================================================================
    // Position
    // =========================================================================

    /**
     * Get this actor's current position.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set this actor's position.
     * Subclasses may override to add validation or side effects.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Move this actor by an offset.
     */
    public void move(Point offset) {
        this.position = position.add(offset);
    }

    /**
     * Move this actor to a new position.
     */
    public void moveTo(Point newPosition) {
        this.position = newPosition;
    }

    // =========================================================================
    // Scheduling / Timing
    // =========================================================================

    /**
     * Get the time remaining until this actor's next action.
     * Lower values mean the actor acts sooner.
     */
    public float getTime() {
        return time;
    }

    /**
     * Set the time until this actor's next action.
     * Used by the scheduler.
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * Spend time (advance this actor's time counter).
     * Called by the scheduler when other actors act.
     *
     * @param amount Amount of time to spend
     */
    public void spendTime(float amount) {
        this.time += amount;
    }

    // =========================================================================
    // Actions
    // =========================================================================

    /**
     * Perform this actor's action.
     * Called by the scheduler when it's this actor's turn.
     *
     * Subclasses must implement their specific behavior here.
     * This method should:
     * - Perform the actor's action (move, attack, wait, etc.)
     * - Return the time cost of the action
     * - Use EngineContext to access RNG, event bus, game state, etc.
     *
     * @param context Engine context for accessing game systems
     * @return Time cost of the action (added to this actor's time)
     */
    public abstract float act(EngineContext context);

    /**
     * Called when this actor is added to the scheduler.
     * Subclasses can override for initialization logic.
     */
    public void onAdded(EngineContext context) {
        // Default: no-op
    }

    /**
     * Called when this actor is removed from the scheduler.
     * Subclasses can override for cleanup logic.
     */
    public void onRemoved(EngineContext context) {
        // Default: no-op
    }

    // =========================================================================
    // Queries
    // =========================================================================

    /**
     * Check if this actor is at the specified position.
     */
    public boolean isAt(Point p) {
        return position.equals(p);
    }

    /**
     * Get the distance to another actor.
     */
    public int distanceTo(Actor other) {
        return position.manhattanDistance(other.position);
    }

    /**
     * Get the distance to a point.
     */
    public int distanceTo(Point p) {
        return position.manhattanDistance(p);
    }

    /**
     * Check if this actor is adjacent to another actor.
     */
    public boolean isAdjacentTo(Actor other) {
        return position.isAdjacentTo(other.position);
    }

    /**
     * Check if this actor is adjacent to a point.
     */
    public boolean isAdjacentTo(Point p) {
        return position.isAdjacentTo(p);
    }

    // =========================================================================
    // Object overrides
    // =========================================================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Actor)) return false;
        Actor other = (Actor) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + " at " + position + "]";
    }
}
