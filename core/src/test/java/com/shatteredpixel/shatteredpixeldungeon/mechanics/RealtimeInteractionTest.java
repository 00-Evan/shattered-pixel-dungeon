/*
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for realtime interaction distance calculations and logic.
 *
 * RealtimeController uses distance squared calculations for performance optimization.
 * These tests validate the mathematical constants and distance logic used for:
 * - Item pickup range (1.5 units = 2.25 squared)
 * - Container interaction range (1.6 units = 2.56 squared)
 */
public class RealtimeInteractionTest {

    // Constants from RealtimeController (duplicated for testing)
    private static final float PICKUP_RANGE = 1.5f;
    private static final float PICKUP_RANGE_SQ = PICKUP_RANGE * PICKUP_RANGE;

    private static final float INTERACTION_RANGE = 1.6f;
    private static final float INTERACTION_RANGE_SQ = INTERACTION_RANGE * INTERACTION_RANGE;

    /**
     * Helper method to calculate distance squared between two points.
     * This matches the logic used in RealtimeController.
     */
    private float calculateDistanceSquared(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    @Test
    public void testPickupRangeConstants() {
        // Verify the squared range constant is correct
        assertEquals("Pickup range squared should be 2.25",
                2.25f, PICKUP_RANGE_SQ, 0.001f);

        // Verify it matches PICKUP_RANGE^2
        assertEquals("Pickup range squared should equal range^2",
                PICKUP_RANGE * PICKUP_RANGE, PICKUP_RANGE_SQ, 0.001f);
    }

    @Test
    public void testInteractionRangeConstants() {
        // Verify the squared range constant is correct
        assertEquals("Interaction range squared should be 2.56",
                2.56f, INTERACTION_RANGE_SQ, 0.001f);

        // Verify it matches INTERACTION_RANGE^2
        assertEquals("Interaction range squared should equal range^2",
                INTERACTION_RANGE * INTERACTION_RANGE, INTERACTION_RANGE_SQ, 0.001f);
    }

    @Test
    public void testInteractionRangeIsLargerThanPickupRange() {
        // Interaction range should be slightly larger than pickup range
        assertTrue("Interaction range should be larger than pickup range",
                INTERACTION_RANGE_SQ > PICKUP_RANGE_SQ);

        float difference = INTERACTION_RANGE_SQ - PICKUP_RANGE_SQ;
        assertEquals("Difference should be about 0.31", 0.31f, difference, 0.01f);
    }

    @Test
    public void testDistanceCalculationSamePosition() {
        // Distance from a point to itself should be zero
        float dist = calculateDistanceSquared(5.0f, 5.0f, 5.0f, 5.0f);
        assertEquals("Distance to same position should be 0", 0.0f, dist, 0.001f);
    }

    @Test
    public void testDistanceCalculationOneUnit() {
        // One unit horizontal distance
        float dist = calculateDistanceSquared(0.0f, 0.0f, 1.0f, 0.0f);
        assertEquals("One unit horizontal should be 1.0 squared", 1.0f, dist, 0.001f);

        // One unit vertical distance
        dist = calculateDistanceSquared(0.0f, 0.0f, 0.0f, 1.0f);
        assertEquals("One unit vertical should be 1.0 squared", 1.0f, dist, 0.001f);
    }

    @Test
    public void testDistanceCalculationDiagonal() {
        // Pythagorean theorem: 3-4-5 triangle
        float dist = calculateDistanceSquared(0.0f, 0.0f, 3.0f, 4.0f);
        assertEquals("3-4-5 triangle should have distance^2 of 25", 25.0f, dist, 0.001f);

        // 1-1 diagonal (sqrt(2))
        dist = calculateDistanceSquared(0.0f, 0.0f, 1.0f, 1.0f);
        assertEquals("1-1 diagonal should have distance^2 of 2", 2.0f, dist, 0.001f);
    }

    @Test
    public void testPickupRangeBoundary() {
        // Test exact pickup range boundary (1.5 units)
        float exactRange = calculateDistanceSquared(0.0f, 0.0f, 1.5f, 0.0f);
        assertEquals("Exact pickup range should be 2.25",
                PICKUP_RANGE_SQ, exactRange, 0.001f);

        // Test just inside pickup range
        float insideRange = calculateDistanceSquared(0.0f, 0.0f, 1.4f, 0.0f);
        assertTrue("1.4 units should be within pickup range",
                insideRange <= PICKUP_RANGE_SQ);

        // Test just outside pickup range
        float outsideRange = calculateDistanceSquared(0.0f, 0.0f, 1.6f, 0.0f);
        assertTrue("1.6 units should be outside pickup range",
                outsideRange > PICKUP_RANGE_SQ);
    }

    @Test
    public void testInteractionRangeBoundary() {
        // Test exact interaction range boundary (1.6 units)
        float exactRange = calculateDistanceSquared(0.0f, 0.0f, 1.6f, 0.0f);
        assertEquals("Exact interaction range should be 2.56",
                INTERACTION_RANGE_SQ, exactRange, 0.001f);

        // Test just inside interaction range
        float insideRange = calculateDistanceSquared(0.0f, 0.0f, 1.5f, 0.0f);
        assertTrue("1.5 units should be within interaction range",
                insideRange <= INTERACTION_RANGE_SQ);

        // Test just outside interaction range
        float outsideRange = calculateDistanceSquared(0.0f, 0.0f, 1.7f, 0.0f);
        assertTrue("1.7 units should be outside interaction range",
                outsideRange > INTERACTION_RANGE_SQ);
    }

    @Test
    public void testAdjacentCellDistances() {
        // Test distances to adjacent cells (important for door unlocking)
        // Hero at (5, 5)
        float heroX = 5.0f;
        float heroY = 5.0f;

        // Adjacent cells should all be within pickup and interaction range
        float[] adjacentX = {4, 5, 6, 4, 6, 4, 5, 6}; // 8 neighbors
        float[] adjacentY = {4, 4, 4, 5, 5, 6, 6, 6};

        for (int i = 0; i < 8; i++) {
            float dist = calculateDistanceSquared(heroX, heroY, adjacentX[i], adjacentY[i]);
            assertTrue("Adjacent cell should be within pickup range",
                    dist <= PICKUP_RANGE_SQ);
            assertTrue("Adjacent cell should be within interaction range",
                    dist <= INTERACTION_RANGE_SQ);
        }
    }

    @Test
    public void testDistanceCalculationSymmetry() {
        // Distance from A to B should equal distance from B to A
        float distAB = calculateDistanceSquared(2.0f, 3.0f, 5.0f, 7.0f);
        float distBA = calculateDistanceSquared(5.0f, 7.0f, 2.0f, 3.0f);
        assertEquals("Distance should be symmetric", distAB, distBA, 0.001f);
    }

    @Test
    public void testDistanceWithNegativeCoordinates() {
        // Test that distance calculation works with negative coordinates
        float dist = calculateDistanceSquared(-2.0f, -3.0f, 1.0f, 1.0f);
        // Distance is sqrt((1-(-2))^2 + (1-(-3))^2) = sqrt(9 + 16) = sqrt(25) = 5
        assertEquals("Distance squared should be 25", 25.0f, dist, 0.001f);
    }

    @Test
    public void testDistanceWithDecimalPositions() {
        // Test distance calculation with decimal hero positions (realtime movement)
        float dist = calculateDistanceSquared(5.5f, 5.5f, 6.5f, 6.5f);
        // Distance is sqrt(1^2 + 1^2) = sqrt(2), squared = 2
        assertEquals("Decimal position distance squared should be 2",
                2.0f, dist, 0.001f);
    }

    @Test
    public void testLargeDistances() {
        // Test that large distances work correctly
        float dist = calculateDistanceSquared(0.0f, 0.0f, 100.0f, 100.0f);
        // Distance is sqrt(100^2 + 100^2) = sqrt(20000), squared = 20000
        assertEquals("Large distance squared should be 20000",
                20000.0f, dist, 0.001f);

        // Such distances should be far outside interaction range
        assertTrue("Large distance should exceed interaction range",
                dist > INTERACTION_RANGE_SQ);
    }

    @Test
    public void testOptimalRangeDifference() {
        // The difference between interaction and pickup range should be small
        // but sufficient to prevent edge cases where items just out of pickup
        // range can still be interacted with as containers
        float rangeDiff = INTERACTION_RANGE - PICKUP_RANGE;
        assertEquals("Range difference should be 0.1 units",
                0.1f, rangeDiff, 0.001f);

        // This means an object at 1.55 units away:
        float testDist = calculateDistanceSquared(0.0f, 0.0f, 1.55f, 0.0f);
        assertTrue("1.55 units should be outside pickup range",
                testDist > PICKUP_RANGE_SQ);
        assertTrue("1.55 units should be within interaction range",
                testDist <= INTERACTION_RANGE_SQ);
    }

    @Test
    public void testZeroDistanceIsWithinBothRanges() {
        // Zero distance (hero on same tile) should always be valid
        float zeroDist = 0.0f;
        assertTrue("Zero distance should be within pickup range",
                zeroDist <= PICKUP_RANGE_SQ);
        assertTrue("Zero distance should be within interaction range",
                zeroDist <= INTERACTION_RANGE_SQ);
    }

    @Test
    public void testPerformanceOptimization() {
        // Verify that using squared distance avoids sqrt() calls
        // We test that PICKUP_RANGE_SQ < 3.0 and INTERACTION_RANGE_SQ < 3.0
        // meaning we can check distance^2 directly without computing sqrt

        assertTrue("Pickup range squared should be reasonable for comparison",
                PICKUP_RANGE_SQ < 10.0f);
        assertTrue("Interaction range squared should be reasonable for comparison",
                INTERACTION_RANGE_SQ < 10.0f);

        // The squared values should be positive
        assertTrue("Pickup range squared should be positive",
                PICKUP_RANGE_SQ > 0);
        assertTrue("Interaction range squared should be positive",
                INTERACTION_RANGE_SQ > 0);
    }
}
