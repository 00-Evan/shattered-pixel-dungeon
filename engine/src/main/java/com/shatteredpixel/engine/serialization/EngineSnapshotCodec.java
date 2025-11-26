package com.shatteredpixel.engine.serialization;

import com.shatteredpixel.engine.serialization.dto.EngineSnapshot;

/**
 * Interface for encoding/decoding EngineSnapshot to/from raw bytes.
 *
 * This is responsible for converting EngineSnapshot DTOs into a portable
 * binary format that can be:
 * - Saved to files
 * - Transmitted over network
 * - Stored in databases
 * - Cached in memory
 *
 * Responsibilities:
 * - Encode EngineSnapshot to byte[]
 * - Decode byte[] back to EngineSnapshot
 * - Handle version compatibility (optional but recommended)
 * - Ensure deterministic encoding (same snapshot → same bytes)
 *
 * This interface is NOT responsible for:
 * - Converting between EngineContext and EngineSnapshot (see EngineStateSerializer)
 * - File I/O operations
 * - Compression (can be added as separate layer)
 * - Encryption (can be added as separate layer)
 *
 * Implementations should be:
 * - Deterministic (same snapshot always produces same bytes)
 * - GWT-safe (no Java serialization, no reflection)
 * - Efficient (minimize byte[] size)
 * - Versionable (include format version for future compatibility)
 *
 * Example implementation strategies:
 * - Binary format with DataOutputStream/DataInputStream
 * - Custom binary protocol with length prefixes
 * - JSON/MessagePack (if GWT-compatible library available)
 *
 * Example usage:
 * <pre>
 * EngineSnapshotCodec codec = new DefaultEngineSnapshotCodec();
 *
 * // Encode
 * byte[] data = codec.encode(snapshot);
 *
 * // Decode
 * EngineSnapshot restored = codec.decode(data);
 * </pre>
 *
 * TODO: Future enhancements:
 * - Add version parameter to encode() for explicit format control
 * - Add canDecode(byte[]) method to check format before decoding
 * - Add getVersion(byte[]) method to read version without full decode
 */
public interface EngineSnapshotCodec {

    /**
     * Encode an EngineSnapshot to bytes.
     *
     * The encoding should:
     * - Include a format version marker at the beginning
     * - Be deterministic (same snapshot → same bytes)
     * - Be compact (minimize size)
     * - Be self-describing (include lengths, counts, etc.)
     *
     * @param snapshot The snapshot to encode
     * @return Binary representation of the snapshot
     * @throws IllegalArgumentException if snapshot is null or invalid
     * @throws IllegalStateException if encoding fails
     */
    byte[] encode(EngineSnapshot snapshot);

    /**
     * Decode bytes back into an EngineSnapshot.
     *
     * The decoding should:
     * - Read and validate the format version
     * - Reconstruct the complete snapshot
     * - Handle format mismatches gracefully (throw clear exception)
     * - Validate data integrity (array lengths, etc.)
     *
     * @param data The binary data to decode
     * @return Reconstructed EngineSnapshot
     * @throws IllegalArgumentException if data is null or empty
     * @throws IllegalStateException if decoding fails (corrupt data, version mismatch, etc.)
     */
    EngineSnapshot decode(byte[] data);
}
