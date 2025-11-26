package com.shatteredpixel.engine.serialization;

import com.shatteredpixel.engine.serialization.dto.ActorSnapshot;
import com.shatteredpixel.engine.serialization.dto.BuffSnapshot;
import com.shatteredpixel.engine.serialization.dto.EngineSnapshot;
import com.shatteredpixel.engine.serialization.dto.LevelSnapshot;
import com.shatteredpixel.engine.serialization.dto.ScheduledActorSnapshot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default binary codec for EngineSnapshot serialization.
 *
 * This implementation uses DataOutputStream/DataInputStream for a simple,
 * deterministic binary format.
 *
 * Format structure:
 * - Version marker (int) - currently 1
 * - RNG seed (long)
 * - Level present flag (boolean)
 * - If level present: depth, width, height, terrain array, flags array
 * - Actor count + actor data
 * - Schedule count + schedule data
 * - Turn counter (long)
 *
 * Design goals:
 * - Deterministic (same snapshot → same bytes)
 * - Compact (uses primitive types, no object overhead)
 * - Versionable (version marker at start)
 * - GWT-safe (uses only standard Java I/O)
 * - Self-describing (includes lengths and counts)
 *
 * TODO: Future enhancements:
 * - Compression (gzip wrapper)
 * - Checksum validation (CRC32)
 * - Multi-version support (migration logic)
 * - Optional fields with bit flags
 */
public class DefaultEngineSnapshotCodec implements EngineSnapshotCodec {

    private static final int FORMAT_VERSION = 1;

    @Override
    public byte[] encode(EngineSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Snapshot cannot be null");
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(baos)) {

            // Write version
            out.writeInt(FORMAT_VERSION);

            // Write RNG seed
            out.writeLong(snapshot.rngSeed);

            // Write level (if present)
            out.writeBoolean(snapshot.level != null);
            if (snapshot.level != null) {
                encodeLevel(out, snapshot.level);
            }

            // Write actors
            out.writeInt(snapshot.actors.size());
            for (ActorSnapshot actor : snapshot.actors) {
                encodeActor(out, actor);
            }

            // Write schedule
            out.writeInt(snapshot.schedule.size());
            for (ScheduledActorSnapshot scheduled : snapshot.schedule) {
                encodeScheduledActor(out, scheduled);
            }

            // Write turn counter
            out.writeLong(snapshot.turnCounter);

            return baos.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to encode snapshot", e);
        }
    }

    private void encodeLevel(DataOutputStream out, LevelSnapshot level) throws IOException {
        out.writeInt(level.depth);
        out.writeInt(level.width);
        out.writeInt(level.height);

        // Write terrain IDs
        for (int id : level.terrainIds) {
            out.writeInt(id);
        }

        // Write tile flags
        for (int flags : level.tileFlags) {
            out.writeInt(flags);
        }
    }

    private void encodeActor(DataOutputStream out, ActorSnapshot actor) throws IOException {
        out.writeLong(actor.actorId);
        out.writeUTF(actor.actorKind);
        out.writeInt(actor.typeOrdinal);
        out.writeInt(actor.x);
        out.writeInt(actor.y);
        out.writeInt(actor.currentHealth);
        out.writeInt(actor.maxHealth);
        out.writeInt(actor.attackPower);
        out.writeInt(actor.defense);
        out.writeInt(actor.accuracy);
        out.writeInt(actor.evasion);
        out.writeFloat(actor.speed);

        // Write buffs (placeholder for now)
        out.writeInt(actor.buffs.size());
        for (BuffSnapshot buff : actor.buffs) {
            encodeBuff(out, buff);
        }
    }

    private void encodeBuff(DataOutputStream out, BuffSnapshot buff) throws IOException {
        out.writeLong(buff.buffId);
        out.writeUTF(buff.buffTypeName);
        out.writeInt(buff.remainingDuration);
    }

    private void encodeScheduledActor(DataOutputStream out, ScheduledActorSnapshot scheduled) throws IOException {
        out.writeLong(scheduled.actorId);
        out.writeFloat(scheduled.time);
    }

    @Override
    public EngineSnapshot decode(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             DataInputStream in = new DataInputStream(bais)) {

            // Read and validate version
            int version = in.readInt();
            if (version != FORMAT_VERSION) {
                throw new IllegalStateException(
                    "Unsupported format version: " + version +
                    " (expected " + FORMAT_VERSION + ")"
                );
            }

            // Read RNG seed
            long rngSeed = in.readLong();

            // Read level (if present)
            LevelSnapshot level = null;
            boolean hasLevel = in.readBoolean();
            if (hasLevel) {
                level = decodeLevel(in);
            }

            // Read actors
            int actorCount = in.readInt();
            List<ActorSnapshot> actors = new ArrayList<>(actorCount);
            for (int i = 0; i < actorCount; i++) {
                actors.add(decodeActor(in));
            }

            // Read schedule
            int scheduleCount = in.readInt();
            List<ScheduledActorSnapshot> schedule = new ArrayList<>(scheduleCount);
            for (int i = 0; i < scheduleCount; i++) {
                schedule.add(decodeScheduledActor(in));
            }

            // Read turn counter
            long turnCounter = in.readLong();

            return new EngineSnapshot(rngSeed, level, actors, schedule, turnCounter);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to decode snapshot", e);
        }
    }

    private LevelSnapshot decodeLevel(DataInputStream in) throws IOException {
        int depth = in.readInt();
        int width = in.readInt();
        int height = in.readInt();

        int size = width * height;
        int[] terrainIds = new int[size];
        int[] tileFlags = new int[size];

        // Read terrain IDs
        for (int i = 0; i < size; i++) {
            terrainIds[i] = in.readInt();
        }

        // Read tile flags
        for (int i = 0; i < size; i++) {
            tileFlags[i] = in.readInt();
        }

        return new LevelSnapshot(depth, width, height, terrainIds, tileFlags);
    }

    private ActorSnapshot decodeActor(DataInputStream in) throws IOException {
        long actorId = in.readLong();
        String actorKind = in.readUTF();
        int typeOrdinal = in.readInt();
        int x = in.readInt();
        int y = in.readInt();
        int currentHealth = in.readInt();
        int maxHealth = in.readInt();
        int attackPower = in.readInt();
        int defense = in.readInt();
        int accuracy = in.readInt();
        int evasion = in.readInt();
        float speed = in.readFloat();

        // Read buffs
        int buffCount = in.readInt();
        List<BuffSnapshot> buffs = new ArrayList<>(buffCount);
        for (int i = 0; i < buffCount; i++) {
            buffs.add(decodeBuff(in));
        }

        return new ActorSnapshot(
            actorId, actorKind, typeOrdinal, x, y,
            currentHealth, maxHealth, attackPower, defense,
            accuracy, evasion, speed, buffs
        );
    }

    private BuffSnapshot decodeBuff(DataInputStream in) throws IOException {
        long buffId = in.readLong();
        String buffTypeName = in.readUTF();
        int remainingDuration = in.readInt();

        return new BuffSnapshot(buffId, buffTypeName, remainingDuration);
    }

    private ScheduledActorSnapshot decodeScheduledActor(DataInputStream in) throws IOException {
        long actorId = in.readLong();
        float time = in.readFloat();

        return new ScheduledActorSnapshot(actorId, time);
    }
}
