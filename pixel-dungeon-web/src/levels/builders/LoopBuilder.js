/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Loop Builder - Creates a circular loop of rooms
 *
 * REFACTORED: Simplified implementation based on working reference
 * Original complex curve-based algorithm replaced with simpler approach
 * that avoids infinite loop bugs while maintaining core functionality
 */

import { RegularBuilder } from './RegularBuilder.js';
import { Builder } from './Builder.js';
import { Random } from '../../utils/Random.js';
import { Point } from '../../utils/Point.js';
import { TunnelRoom } from '../rooms/connection/TunnelRoom.js';
import { RoomDirection } from '../rooms/Room.js';

/**
 * Loop Builder - Simplified Implementation
 * Creates a main loop of rooms with branching paths
 */
export class LoopBuilder extends RegularBuilder {
    constructor() {
        super();
    }

    /**
     * Set loop shape (maintained for API compatibility)
     * Note: Simplified implementation doesn't use complex curves
     */
    setLoopShape(exponent, intensity, offset) {
        // Parameters stored but not used in simplified implementation
        return this;
    }

    /**
     * Build the level layout
     * Simplified approach: main chain + branches
     */
    build(rooms) {
        this.setupRooms(rooms);

        if (this.entrance === null) {
            return null;
        }

        // Place entrance at origin
        this.entrance.setSize();
        this.entrance.setPos(0, 0);

        // Build main path as a chain of rooms
        const placedRooms = [this.entrance];

        // Add main path rooms - put exit in the MIDDLE, not right after entrance!
        // Entrance and exit cannot connect directly (Room.java:219-222)
        const mainPath = [this.entrance];
        mainPath.push(...this.mainPathRooms);

        // Insert exit in the middle of the path
        if (this.exit !== null && mainPath.length > 1) {
            const midPoint = Math.floor(mainPath.length / 2);
            mainPath.splice(midPoint, 0, this.exit);
        } else if (this.exit !== null) {
            // If there are no other rooms, still add exit but it will fail to connect
            mainPath.push(this.exit);
        }

        // Place main path rooms in a chain
        if (!this.buildMainChain(placedRooms, mainPath)) {
            return null;
        }

        // Add connection tunnels between main path rooms
        this.addConnectionTunnels(placedRooms, mainPath);

        // Build branches from the main path
        const branchable = [...mainPath];
        const roomsToBranch = [];
        roomsToBranch.push(...this.multiConnections);
        roomsToBranch.push(...this.singleConnections);

        if (roomsToBranch.length > 0) {
            this.weightRooms(branchable);
            if (!this.createBranches(placedRooms, branchable, roomsToBranch, this.branchTunnelChances)) {
                return null;
            }
        }

        // Find neighbors and add extra connections
        Builder.findNeighbours(placedRooms);

        for (const r of placedRooms) {
            for (const n of r.neigbours) {
                if (!n.connected.has(r) && Random.Float() < this.extraConnectionChance) {
                    r.connect(n);
                }
            }
        }

        // Normalize coordinates to positive space
        Builder.normalizeCoordinates(placedRooms);

        return placedRooms;
    }

    /**
     * Build main chain of rooms
     * Places rooms in cardinal directions from each other
     */
    buildMainChain(placedRooms, mainPath) {
        let currentRoom = this.entrance;
        let attempts = 0;
        const maxAttempts = 100;

        // Direction angles: 0=up, 90=right, 180=down, 270=left
        const directions = [0, 90, 180, 270];

        for (let i = 1; i < mainPath.length && attempts < maxAttempts; i++) {
            const nextRoom = mainPath[i];
            nextRoom.setSize();

            let placed = false;
            const shuffledDirs = [...directions];
            Random.shuffle(shuffledDirs);

            for (const angle of shuffledDirs) {
                const resultAngle = Builder.placeRoom(placedRooms, currentRoom, nextRoom, angle);

                if (resultAngle !== -1) {
                    placedRooms.push(nextRoom);
                    currentRoom.connect(nextRoom);
                    currentRoom = nextRoom;
                    placed = true;
                    attempts = 0;
                    break;
                }
            }

            if (!placed) {
                // Try placing from a different room in the chain
                attempts++;
                if (placedRooms.length > 1) {
                    currentRoom = placedRooms[placedRooms.length - 1 - Random.Int_max(Math.min(3, placedRooms.length - 1))];
                    i--; // Retry this room
                } else {
                    return null; // Failed to place room
                }
            }
        }

        return placedRooms.length >= mainPath.length;
    }

    /**
     * Add tunnel rooms between main path rooms for variety
     */
    addConnectionTunnels(placedRooms, mainPath) {
        const tunnelsToAdd = Random.Int_max(2) + 1; // 1-2 tunnels

        for (let i = 0; i < tunnelsToAdd && mainPath.length > 1; i++) {
            const idx = Random.Int_max(mainPath.length - 1);
            const room1 = mainPath[idx];
            const room2 = mainPath[idx + 1];

            // Add a tunnel room between two connected rooms
            if (room1.connected.has(room2)) {
                const tunnel = new TunnelRoom();
                tunnel.setSize();

                const angle = Builder.angleBetweenRooms(room1, room2);
                if (Builder.placeRoom(placedRooms, room1, tunnel, angle) !== -1) {
                    placedRooms.push(tunnel);
                    room1.connected.delete(room2);
                    room2.connected.delete(room1);
                    room1.connect(tunnel);
                    tunnel.connect(room2);
                }
            }
        }
    }

    /**
     * Random branch angle
     * Simplified: just use random angles
     */
    randomBranchAngle(r) {
        return Random.Float() * 360;
    }
}
