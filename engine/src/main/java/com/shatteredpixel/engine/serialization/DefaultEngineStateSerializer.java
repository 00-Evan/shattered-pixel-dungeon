package com.shatteredpixel.engine.serialization;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.actor.Actor;
import com.shatteredpixel.engine.actor.ActorId;
import com.shatteredpixel.engine.actor.ActorType;
import com.shatteredpixel.engine.actor.Character;
import com.shatteredpixel.engine.actor.hero.GenericHero;
import com.shatteredpixel.engine.actor.mob.SimpleMeleeMob;
import com.shatteredpixel.engine.dungeon.LevelGrid;
import com.shatteredpixel.engine.dungeon.LevelState;
import com.shatteredpixel.engine.dungeon.terrain.TerrainType;
import com.shatteredpixel.engine.dungeon.terrain.TileFlags;
import com.shatteredpixel.engine.geom.Point;
import com.shatteredpixel.engine.serialization.dto.ActorSnapshot;
import com.shatteredpixel.engine.serialization.dto.EngineSnapshot;
import com.shatteredpixel.engine.serialization.dto.LevelSnapshot;
import com.shatteredpixel.engine.serialization.dto.ScheduledActorSnapshot;
import com.shatteredpixel.engine.stats.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of EngineStateSerializer.
 *
 * This implementation captures and restores the full engine state including:
 * - RNG seed
 * - Level terrain and flags
 * - All actors (position, stats, health)
 * - Scheduler state (turn order)
 *
 * Actor reconstruction:
 * - "GENERIC_HERO" → GenericHero
 * - "SIMPLE_MELEE_MOB" → SimpleMeleeMob
 * - Unknown kinds throw IllegalStateException
 *
 * TODO: Future enhancements:
 * - Support for more actor types
 * - Buff serialization/restoration
 * - Inventory and equipment
 * - Quest state
 * - Custom actor factories for extensibility
 */
public class DefaultEngineStateSerializer implements EngineStateSerializer {

    private static final String ACTOR_KIND_GENERIC_HERO = "GENERIC_HERO";
    private static final String ACTOR_KIND_SIMPLE_MELEE_MOB = "SIMPLE_MELEE_MOB";

    @Override
    public EngineSnapshot captureSnapshot(EngineContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        // Capture RNG seed
        long rngSeed = context.getRNG().getSeed();

        // Capture level state (if present)
        LevelSnapshot levelSnapshot = null;
        LevelState level = context.getLevel();
        if (level != null) {
            levelSnapshot = captureLevelSnapshot(level);
        }

        // Capture all actors
        List<ActorSnapshot> actorSnapshots = new ArrayList<>();
        Map<ActorId, Actor> actors = context.getActors();
        for (Actor actor : actors.values()) {
            if (actor instanceof Character) {
                actorSnapshots.add(captureActorSnapshot((Character) actor));
            }
            // TODO: Support non-Character actors in the future
        }

        // Capture scheduler state
        List<ScheduledActorSnapshot> scheduleSnapshots = new ArrayList<>();
        List<Actor> scheduledActors = context.getScheduler().getAllActors();
        for (Actor actor : scheduledActors) {
            scheduleSnapshots.add(new ScheduledActorSnapshot(
                actor.getId().getValue(),
                actor.getTime()
            ));
        }

        // Build and return snapshot
        return new EngineSnapshot(
            rngSeed,
            levelSnapshot,
            actorSnapshots,
            scheduleSnapshots
        );
    }

    private LevelSnapshot captureLevelSnapshot(LevelState level) {
        int depth = level.getDepth();
        int width = level.getWidth();
        int height = level.getHeight();
        LevelGrid grid = level.getGrid();

        // Flatten 2D terrain/flags into 1D arrays
        int size = width * height;
        int[] terrainIds = new int[size];
        int[] tileFlags = new int[size];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = x + y * width;
                terrainIds[index] = grid.getTerrain(x, y).ordinal();
                tileFlags[index] = grid.getFlags(x, y).getFlags();
            }
        }

        return new LevelSnapshot(depth, width, height, terrainIds, tileFlags);
    }

    private ActorSnapshot captureActorSnapshot(Character character) {
        // Extract actor ID
        long actorId = character.getId().getValue();

        // Determine actor kind
        String actorKind;
        if (character instanceof SimpleMeleeMob) {
            actorKind = ACTOR_KIND_SIMPLE_MELEE_MOB;
        } else if (character.getType() == ActorType.HERO) {
            actorKind = ACTOR_KIND_GENERIC_HERO;
        } else {
            actorKind = "UNKNOWN";
        }

        // Extract position
        Point pos = character.getPosition();
        int x = (pos != null) ? pos.x : -1;
        int y = (pos != null) ? pos.y : -1;

        // Extract stats
        Stats stats = character.getStats();
        int currentHealth = character.getCurrentHealth();

        // Extract buffs from BuffContainer
        BuffSnapshot[] buffSnapshots = character.getBuffContainer().getSnapshot();
        List<BuffSnapshot> buffList = new ArrayList<>();
        for (BuffSnapshot snap : buffSnapshots) {
            buffList.add(snap);
        }

        return new ActorSnapshot(
            actorId,
            actorKind,
            character.getType().ordinal(),
            x,
            y,
            currentHealth,
            stats.getMaxHealth(),
            stats.getAttackPower(),
            stats.getDefense(),
            stats.getAccuracy(),
            stats.getEvasion(),
            stats.getSpeed(),
            buffList
        );
    }

    @Override
    public void restoreFromSnapshot(EngineContext context, EngineSnapshot snapshot) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot cannot be null");
        }

        // Clear existing state
        clearContext(context);

        // Restore RNG seed
        context.getRNG().setSeed(snapshot.rngSeed);

        // Restore level (if present)
        if (snapshot.level != null) {
            LevelState level = restoreLevelState(snapshot.level);
            context.setLevel(level);
        }

        // Restore actors
        for (ActorSnapshot actorSnapshot : snapshot.actors) {
            Character actor = restoreActor(actorSnapshot);
            context.addActor(actor);

            // Set grid occupied flag if actor has position
            if (snapshot.level != null && actor.getPosition() != null) {
                LevelGrid grid = context.getLevel().getGrid();
                if (grid.isInBounds(actor.getPosition())) {
                    grid.setOccupied(actor.getPosition(), true);
                }
            }

            // Restore buffs now that actor is in context
            if (actorSnapshot.buffs != null && !actorSnapshot.buffs.isEmpty()) {
                BuffSnapshot[] buffSnapshotArray = actorSnapshot.buffs.toArray(new BuffSnapshot[0]);
                actor.getBuffContainer().loadFromSnapshot(buffSnapshotArray, context, actor);
            }
        }

        // Restore scheduler state
        for (ScheduledActorSnapshot scheduleSnapshot : snapshot.schedule) {
            ActorId actorId = new ActorId(scheduleSnapshot.actorId);
            Actor actor = context.getActor(actorId);
            if (actor != null) {
                context.registerActorForScheduling(actor, scheduleSnapshot.time);
            }
            // If actor not found, skip (already removed or invalid state)
        }
    }

    private void clearContext(EngineContext context) {
        // Clear actors
        List<ActorId> actorIds = new ArrayList<>(context.getActors().keySet());
        for (ActorId id : actorIds) {
            context.removeActor(id);
        }

        // Clear scheduler
        context.getScheduler().clear(context);

        // Clear level
        context.setLevel(null);

        // Clear event collector
        context.getEventCollector().clear();
    }

    private LevelState restoreLevelState(LevelSnapshot snapshot) {
        LevelState level = new LevelState(snapshot.depth, snapshot.width, snapshot.height);
        LevelGrid grid = level.getGrid();

        // Restore terrain and flags from flattened arrays
        for (int y = 0; y < snapshot.height; y++) {
            for (int x = 0; x < snapshot.width; x++) {
                int index = x + y * snapshot.width;

                // Restore terrain
                TerrainType terrain = TerrainType.values()[snapshot.terrainIds[index]];
                grid.setTerrain(x, y, terrain);

                // Restore flags
                TileFlags flags = new TileFlags(snapshot.tileFlags[index]);
                grid.getFlags(x, y).setFlags(flags.getFlags());
            }
        }

        return level;
    }

    private Character restoreActor(ActorSnapshot snapshot) {
        // Reconstruct position
        Point position = (snapshot.x >= 0 && snapshot.y >= 0)
            ? new Point(snapshot.x, snapshot.y)
            : null;

        // Reconstruct stats
        Stats stats = new Stats(
            snapshot.maxHealth,
            snapshot.attackPower,
            snapshot.defense,
            snapshot.accuracy,
            snapshot.evasion,
            snapshot.speed
        );

        // Instantiate appropriate concrete class based on actor kind
        Character actor;
        switch (snapshot.actorKind) {
            case ACTOR_KIND_GENERIC_HERO:
                actor = new GenericHero(position, stats, snapshot.currentHealth, "Hero");
                break;

            case ACTOR_KIND_SIMPLE_MELEE_MOB:
                actor = new SimpleMeleeMob(position, stats, snapshot.currentHealth);
                break;

            default:
                throw new IllegalStateException(
                    "Unknown actor kind: " + snapshot.actorKind +
                    ". Cannot restore actor with ID " + snapshot.actorId
                );
        }

        // Set actor ID to match snapshot
        // Note: ActorId is auto-generated in Actor constructor, so we need to
        // work around this. For now, we'll accept the new ID.
        // TODO: Add setId() or constructor that accepts ActorId for proper restoration

        // Buffs are restored after actor is added to context (see restoreFromSnapshot)

        return actor;
    }
}
