/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Loop Builder - Creates a circular loop of rooms
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/builders/LoopBuilder.java
 */

import { RegularBuilder } from './RegularBuilder.js';
import { Builder } from './Builder.js';
import { Random } from '../../utils/Random.js';
import { PointF } from '../../utils/PointF.js';
import { TunnelRoom } from '../rooms/connection/TunnelRoom.js';

/**
 * Loop Builder
 * Creates a circular loop as the main path
 * Source: LoopBuilder.java:31-195
 */
export class LoopBuilder extends RegularBuilder {
    constructor() {
        super();

        // Loop shape parameters
        // Source: LoopBuilder.java:38-47
        this.curveExponent = 0;
        this.curveIntensity = 1;
        this.curveOffset = 0;

        this.loopCenter = null;
    }

    /**
     * Set loop shape
     * Source: LoopBuilder.java:49-54
     */
    setLoopShape(exponent, intensity, offset) {
        this.curveExponent = Math.abs(exponent);
        this.curveIntensity = intensity % 1;
        this.curveOffset = offset % 0.5;
        return this;
    }

    /**
     * Calculate target angle for room placement
     * Source: LoopBuilder.java:56-62
     */
    targetAngle(percentAlong) {
        percentAlong += this.curveOffset;
        return 360 * (
            this.curveIntensity * this.curveEquation(percentAlong) +
            (1 - this.curveIntensity) * percentAlong -
            this.curveOffset
        );
    }

    /**
     * Curve equation for loop shape
     * Source: LoopBuilder.java:64-68
     */
    curveEquation(x) {
        return Math.pow(4, 2 * this.curveExponent) *
            Math.pow((x % 0.5) - 0.25, 2 * this.curveExponent + 1) +
            0.25 + 0.5 * Math.floor(2 * x);
    }

    /**
     * Build the level layout
     * Source: LoopBuilder.java:73-174
     */
    build(rooms) {
        this.setupRooms(rooms);

        if (this.entrance === null) {
            return null;
        }

        // Place entrance at origin
        // Source: LoopBuilder.java:81-82
        this.entrance.setSize();
        this.entrance.setPos(0, 0);

        const startAngle = Random.Float() * 360;

        // Add entrance and exit to main path
        // Source: LoopBuilder.java:86-87
        this.mainPathRooms.unshift(this.entrance);
        if (this.exit !== null) {
            this.mainPathRooms.splice(Math.floor((this.mainPathRooms.length + 1) / 2), 0, this.exit);
        }

        // Create loop with tunnels between rooms
        // Source: LoopBuilder.java:89-104
        const loop = [];
        const pathTunnels = this.pathTunnelChances.slice();

        for (const r of this.mainPathRooms) {
            loop.push(r);

            let tunnels = Random.chances(pathTunnels);
            if (tunnels === -1) {
                pathTunnels.splice(0, pathTunnels.length, ...this.pathTunnelChances);
                tunnels = Random.chances(pathTunnels);
            }
            pathTunnels[tunnels]--;

            for (let j = 0; j < tunnels; j++) {
                loop.push(new TunnelRoom());
            }
        }

        // Place rooms around the loop
        // Source: LoopBuilder.java:106-119
        let prev = this.entrance;
        let targetAngleValue;

        for (let i = 1; i < loop.length; i++) {
            const r = loop[i];
            targetAngleValue = startAngle + this.targetAngle(i / loop.length);

            if (Builder.placeRoom(rooms, prev, r, targetAngleValue) !== -1) {
                prev = r;
                if (!rooms.includes(prev)) {
                    rooms.push(prev);
                }
            } else {
                // Failed to place room
                return null;
            }
        }

        // Connect last room back to entrance
        // Source: LoopBuilder.java:121-132
        while (!prev.connect(this.entrance)) {
            const c = new TunnelRoom();
            if (Builder.placeRoom(loop, prev, c, Builder.angleBetweenRooms(prev, this.entrance)) === -1) {
                return null;
            }
            loop.push(c);
            rooms.push(c);
            prev = c;
        }

        // Place shop (simplified)
        // Source: LoopBuilder.java:134-142
        if (this.shop !== null) {
            let angle;
            let tries = 10;
            do {
                angle = Builder.placeRoom(loop, this.entrance, this.shop, Random.Float() * 360);
                tries--;
            } while (angle === -1 && tries >= 0);
            if (angle === -1) return null;
        }

        // Calculate loop center
        // Source: LoopBuilder.java:144-150
        this.loopCenter = new PointF(0, 0);
        for (const r of loop) {
            this.loopCenter.x += (r.left + r.right) / 2;
            this.loopCenter.y += (r.top + r.bottom) / 2;
        }
        this.loopCenter.x /= loop.length;
        this.loopCenter.y /= loop.length;

        // Create branches
        // Source: LoopBuilder.java:152-160
        const branchable = [...loop];
        const roomsToBranch = [];
        roomsToBranch.push(...this.multiConnections);
        roomsToBranch.push(...this.singleConnections);

        this.weightRooms(branchable);
        if (!this.createBranches(rooms, branchable, roomsToBranch, this.branchTunnelChances)) {
            return null;
        }

        // Find neighbors
        // Source: LoopBuilder.java:162
        Builder.findNeighbours(rooms);

        // Add extra connections
        // Source: LoopBuilder.java:164-171
        for (const r of rooms) {
            for (const n of r.neigbours) {
                if (!n.connected.has(r) && Random.Float() < this.extraConnectionChance) {
                    r.connect(n);
                }
            }
        }

        return rooms;
    }

    /**
     * Random branch angle (prefers pointing toward center)
     * Source: LoopBuilder.java:176-194
     */
    randomBranchAngle(r) {
        if (this.loopCenter === null) {
            return super.randomBranchAngle(r);
        } else {
            // Generate four angles and return the one closest to center
            let toCenter = Builder.angleBetweenPoints(
                new PointF((r.left + r.right) / 2, (r.top + r.bottom) / 2),
                this.loopCenter
            );
            if (toCenter < 0) toCenter += 360;

            let currAngle = Random.Float() * 360;
            for (let i = 0; i < 4; i++) {
                const newAngle = Random.Float() * 360;
                if (Math.abs(toCenter - newAngle) < Math.abs(toCenter - currAngle)) {
                    currAngle = newAngle;
                }
            }
            return currAngle;
        }
    }
}
