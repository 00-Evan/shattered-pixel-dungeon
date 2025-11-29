/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Regular Builder - Base class for regular level builders
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/builders/RegularBuilder.java
 */

import { Builder } from './Builder.js';
import { Random } from '../../utils/Random.js';
import { TunnelRoom } from '../rooms/connection/TunnelRoom.js';

/**
 * Regular Builder
 * Introduces the concept of a main path and branches
 * Source: RegularBuilder.java:35-247
 */
export class RegularBuilder extends Builder {
    constructor() {
        super();

        // Path variance (angle variation)
        // Source: RegularBuilder.java:42
        this.pathVariance = 45;

        // Path length (percentage of rooms on main path)
        // Source: RegularBuilder.java:50-53
        this.pathLength = 0.25;
        this.pathLenJitterChances = [0, 0, 0, 1];

        // Tunnel chances for path and branches
        // Source: RegularBuilder.java:61-62
        this.pathTunnelChances = [2, 2, 1];
        this.branchTunnelChances = [1, 1, 0];

        // Extra connection chance
        // Source: RegularBuilder.java:71
        this.extraConnectionChance = 0.30;

        // Room categories
        // Source: RegularBuilder.java:80-87
        this.entrance = null;
        this.exit = null;
        this.shop = null;

        this.mainPathRooms = [];
        this.multiConnections = [];
        this.singleConnections = [];
    }

    /**
     * Set path variance
     * Source: RegularBuilder.java:44-47
     */
    setPathVariance(variance) {
        this.pathVariance = variance;
        return this;
    }

    /**
     * Set path length
     * Source: RegularBuilder.java:55-59
     */
    setPathLength(len, jitter) {
        this.pathLength = len;
        this.pathLenJitterChances = jitter;
        return this;
    }

    /**
     * Set tunnel length
     * Source: RegularBuilder.java:64-68
     */
    setTunnelLength(path, branch) {
        this.pathTunnelChances = path;
        this.branchTunnelChances = branch;
        return this;
    }

    /**
     * Set extra connection chance
     * Source: RegularBuilder.java:73-76
     */
    setExtraConnectionChance(chance) {
        this.extraConnectionChance = chance;
        return this;
    }

    /**
     * Setup room categories
     * Source: RegularBuilder.java:89-130
     */
    setupRooms(rooms) {
        // Reset all room connections
        // Source: RegularBuilder.java:90-92
        for (const r of rooms) {
            r.setEmpty();
        }

        // Clear categories
        // Source: RegularBuilder.java:94-97
        this.entrance = null;
        this.exit = null;
        this.shop = null;
        this.mainPathRooms = [];
        this.singleConnections = [];
        this.multiConnections = [];

        // Categorize rooms
        // Source: RegularBuilder.java:98-110
        for (const r of rooms) {
            if (r.isEntrance && r.isEntrance()) {
                this.entrance = r;
            } else if (r.isExit && r.isExit()) {
                this.exit = r;
            } else if (r.maxConnections(0) > 1) {  // RoomDirection.ALL = 0
                this.multiConnections.push(r);
            } else if (r.maxConnections(0) === 1) {
                this.singleConnections.push(r);
            }
        }

        // Weight larger rooms to be more likely on main path
        // Source: RegularBuilder.java:112-117
        this.weightRooms(this.multiConnections);
        Random.shuffle(this.multiConnections);

        // Remove duplicates while preserving order
        this.multiConnections = Array.from(new Set(this.multiConnections));

        // Shuffle again
        Random.shuffle(this.multiConnections);

        // Determine number of rooms on main path
        // Source: RegularBuilder.java:119
        let roomsOnMainPath = Math.floor(this.multiConnections.length * this.pathLength) +
            Random.chances(this.pathLenJitterChances);

        // Move rooms from multiConnections to mainPathRooms
        // Source: RegularBuilder.java:121-129
        while (roomsOnMainPath > 0 && this.multiConnections.length > 0) {
            const r = this.multiConnections.shift();
            // Simplified: assume sizeFactor() = 1 for all rooms
            roomsOnMainPath--;
            this.mainPathRooms.push(r);
        }
    }

    /**
     * Weight rooms (adds room multiple times for higher connection weight)
     * Source: RegularBuilder.java:134-141
     */
    weightRooms(rooms) {
        const toAdd = [];
        for (const r of rooms) {
            // Simplified: assume connectionWeight() = 1 for all rooms
            // In full implementation, StandardRoom has variable weights
        }
        rooms.push(...toAdd);
    }

    /**
     * Create branches from branchable rooms to roomsToBranch
     * Source: RegularBuilder.java:145-241
     */
    createBranches(rooms, branchable, roomsToBranch, connChances) {
        let i = 0;
        let angle;
        let tries;
        let curr;
        const connectingRoomsThisBranch = [];
        let failedBranchAttempts = 0;
        const connectionChances = connChances.slice();

        while (i < roomsToBranch.length) {
            // Fail if too many failed attempts
            // Source: RegularBuilder.java:157-159
            if (failedBranchAttempts > 100) {
                return false;
            }

            const r = roomsToBranch[i];

            connectingRoomsThisBranch.length = 0;

            // Pick random branchable room
            // Source: RegularBuilder.java:165-167
            curr = Random.element(branchable);

            // Determine number of connecting rooms
            // Source: RegularBuilder.java:169-174
            let connectingRooms = Random.chances(connectionChances);
            if (connectingRooms === -1) {
                connectionChances.splice(0, connectionChances.length, ...connChances);
                connectingRooms = Random.chances(connectionChances);
            }
            connectionChances[connectingRooms]--;

            // Place connecting tunnel rooms
            // Source: RegularBuilder.java:176-199
            for (let j = 0; j < connectingRooms; j++) {
                const t = new TunnelRoom();
                tries = 3;

                do {
                    angle = Builder.placeRoom(rooms, curr, t, this.randomBranchAngle(curr));
                    tries--;
                } while (angle === -1 && tries > 0);

                if (angle === -1) {
                    t.clearConnections();
                    for (const c of connectingRoomsThisBranch) {
                        c.clearConnections();
                        const idx = rooms.indexOf(c);
                        if (idx > -1) rooms.splice(idx, 1);
                    }
                    connectingRoomsThisBranch.length = 0;
                    break;
                } else {
                    connectingRoomsThisBranch.push(t);
                    rooms.push(t);
                }

                curr = t;
            }

            // Check if all connecting rooms were placed
            // Source: RegularBuilder.java:201-204
            if (connectingRoomsThisBranch.length !== connectingRooms) {
                failedBranchAttempts++;
                continue;
            }

            // Place the actual branch room
            // Source: RegularBuilder.java:206-222
            tries = 10;

            do {
                angle = Builder.placeRoom(rooms, curr, r, this.randomBranchAngle(curr));
                tries--;
            } while (angle === -1 && tries > 0);

            if (angle === -1) {
                r.clearConnections();
                for (const t of connectingRoomsThisBranch) {
                    t.clearConnections();
                    const idx = rooms.indexOf(t);
                    if (idx > -1) rooms.splice(idx, 1);
                }
                connectingRoomsThisBranch.length = 0;
                failedBranchAttempts++;
                continue;
            }

            // Add connecting rooms to branchable
            // Source: RegularBuilder.java:224-235
            for (let j = 0; j < connectingRoomsThisBranch.length; j++) {
                if (Random.Int_max(3) <= 1) {
                    branchable.push(connectingRoomsThisBranch[j]);
                }
            }
            if (r.maxConnections(0) > 1 && Random.Int_max(3) === 0) {
                branchable.push(r);
            }

            i++;
        }

        return true;
    }

    /**
     * Random branch angle
     * Source: RegularBuilder.java:243-245
     */
    randomBranchAngle(r) {
        return Random.Float() * 360;
    }
}
