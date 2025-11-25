package com.shatteredpixel.engine.actor;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.geom.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Turn-based scheduling system for actors.
 * Manages which actor acts next based on time/speed.
 *
 * Algorithm:
 * - Each actor has a "time" value representing when they act next
 * - Lower time = acts sooner
 * - After an actor acts, their time increases by the action's cost
 * - Scheduler always selects the actor with lowest time
 *
 * Design goals:
 * - Deterministic turn order (stable sort)
 * - Supports variable action speeds
 * - No game-specific logic
 * - Efficient actor lookup and scheduling
 *
 * Future migration:
 * - Replaces legacy com.shatteredpixel.shatteredpixeldungeon.actors.Actor static scheduling
 */
public class ActorScheduler {

    private final Map<ActorId, Actor> actors;
    private boolean dirty; // True if actor list needs re-sorting

    // =========================================================================
    // Constructors
    // =========================================================================

    public ActorScheduler() {
        this.actors = new HashMap<>();
        this.dirty = false;
    }

    // =========================================================================
    // Actor Management
    // =========================================================================

    /**
     * Add an actor to the scheduler.
     * The actor will participate in turn-based updates.
     */
    public void addActor(Actor actor, EngineContext context) {
        actors.put(actor.getId(), actor);
        actor.onAdded(context);
        dirty = true;
    }

    /**
     * Remove an actor from the scheduler.
     * The actor will no longer receive updates.
     */
    public void removeActor(ActorId actorId, EngineContext context) {
        Actor actor = actors.remove(actorId);
        if (actor != null) {
            actor.onRemoved(context);
            dirty = true;
        }
    }

    /**
     * Remove an actor by reference.
     */
    public void removeActor(Actor actor, EngineContext context) {
        removeActor(actor.getId(), context);
    }

    /**
     * Check if an actor is in the scheduler.
     */
    public boolean hasActor(ActorId actorId) {
        return actors.containsKey(actorId);
    }

    /**
     * Get an actor by ID.
     * Returns null if not found.
     */
    public Actor getActor(ActorId actorId) {
        return actors.get(actorId);
    }

    /**
     * Get all actors (unordered).
     */
    public List<Actor> getAllActors() {
        return new ArrayList<>(actors.values());
    }

    /**
     * Get the number of actors in the scheduler.
     */
    public int getActorCount() {
        return actors.size();
    }

    /**
     * Clear all actors from the scheduler.
     */
    public void clear(EngineContext context) {
        for (Actor actor : actors.values()) {
            actor.onRemoved(context);
        }
        actors.clear();
        dirty = false;
    }

    // =========================================================================
    // Scheduling
    // =========================================================================

    /**
     * Get the next actor that should act.
     * Returns the actor with the lowest time value.
     * Returns null if no actors are scheduled.
     */
    public Actor getNextActor() {
        if (actors.isEmpty()) {
            return null;
        }

        // Find actor with minimum time
        Actor next = null;
        float minTime = Float.MAX_VALUE;

        for (Actor actor : actors.values()) {
            if (actor.getTime() < minTime) {
                minTime = actor.getTime();
                next = actor;
            }
        }

        return next;
    }

    /**
     * Process the next actor's turn.
     * Returns true if an actor acted, false if no actors available.
     *
     * @param context Engine context for the actor's act() method
     * @return True if an actor performed an action
     */
    public boolean processTurn(EngineContext context) {
        Actor next = getNextActor();
        if (next == null) {
            return false;
        }

        // Normalize time: subtract minimum time from all actors
        // This keeps time values manageable and prevents overflow
        float minTime = next.getTime();
        if (minTime > 0) {
            for (Actor actor : actors.values()) {
                actor.setTime(actor.getTime() - minTime);
            }
        }

        // Let the actor act
        float actionCost = next.act(context);

        // Add action cost to actor's time
        next.spendTime(actionCost);

        return true;
    }

    /**
     * Process turns until a specific actor acts, or max iterations reached.
     * Useful for "wait until hero acts" logic.
     *
     * @param targetActorId Actor to wait for
     * @param maxIterations Maximum turns to process (prevents infinite loops)
     * @param context Engine context
     * @return True if target actor acted, false if max iterations reached
     */
    public boolean processUntil(ActorId targetActorId, int maxIterations, EngineContext context) {
        for (int i = 0; i < maxIterations; i++) {
            Actor next = getNextActor();
            if (next == null) {
                return false;
            }

            if (next.getId().equals(targetActorId)) {
                processTurn(context);
                return true;
            }

            processTurn(context);
        }
        return false;
    }

    // =========================================================================
    // Queries
    // =========================================================================

    /**
     * Get all actors sorted by their next action time.
     * First actor in list acts soonest.
     */
    public List<Actor> getActorsByTime() {
        List<Actor> sorted = new ArrayList<>(actors.values());
        sorted.sort(Comparator.comparing(Actor::getTime));
        return sorted;
    }

    /**
     * Get all actors of a specific type.
     */
    public List<Actor> getActorsByType(ActorType type) {
        List<Actor> result = new ArrayList<>();
        for (Actor actor : actors.values()) {
            if (actor.getType() == type) {
                result.add(actor);
            }
        }
        return result;
    }

    /**
     * Get all actors at a specific position.
     */
    public List<Actor> getActorsAt(Point position) {
        List<Actor> result = new ArrayList<>();
        for (Actor actor : actors.values()) {
            if (actor.getPosition().equals(position)) {
                result.add(actor);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ActorScheduler[" + actors.size() + " actors]";
    }
}
