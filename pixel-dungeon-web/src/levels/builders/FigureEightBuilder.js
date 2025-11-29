/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Figure Eight Builder - Creates two interconnected loops
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/builders/FigureEightBuilder.java
 *
 * NOTE: Simplified implementation - uses LoopBuilder for now
 * Full implementation creates two separate loops connected by a landmark room
 */

import { LoopBuilder } from './LoopBuilder.js';

/**
 * Figure Eight Builder
 * Creates two loops in a figure-8 pattern
 * Source: FigureEightBuilder.java:31-250+
 *
 * SIMPLIFIED: For initial port, uses LoopBuilder logic
 * TODO: Implement full figure-8 algorithm
 */
export class FigureEightBuilder extends LoopBuilder {
    constructor() {
        super();
    }

    /**
     * Set loop shape
     * Source: FigureEightBuilder.java:48-53
     */
    setLoopShape(exponent, intensity, offset) {
        super.setLoopShape(exponent, intensity, offset);
        return this;
    }

    // Simplified: inherits build() from LoopBuilder
    // Full implementation would create two separate loops
}
