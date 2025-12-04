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

package com.watabou.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameMathTest {

    @Test
    public void testGateWithinRange() {
        // Value within range should be returned unchanged
        float result = GameMath.gate(0.0f, 5.0f, 10.0f);
        assertEquals(5.0f, result, 0.001f);
    }

    @Test
    public void testGateBelowMinimum() {
        // Value below minimum should be clamped to minimum
        float result = GameMath.gate(5.0f, 3.0f, 10.0f);
        assertEquals(5.0f, result, 0.001f);
    }

    @Test
    public void testGateAboveMaximum() {
        // Value above maximum should be clamped to maximum
        float result = GameMath.gate(0.0f, 15.0f, 10.0f);
        assertEquals(10.0f, result, 0.001f);
    }

    @Test
    public void testGateAtMinimum() {
        // Value exactly at minimum should be returned
        float result = GameMath.gate(5.0f, 5.0f, 10.0f);
        assertEquals(5.0f, result, 0.001f);
    }

    @Test
    public void testGateAtMaximum() {
        // Value exactly at maximum should be returned
        float result = GameMath.gate(0.0f, 10.0f, 10.0f);
        assertEquals(10.0f, result, 0.001f);
    }

    @Test
    public void testGateWithNegativeValues() {
        // Should work with negative ranges
        float result1 = GameMath.gate(-10.0f, -5.0f, 0.0f);
        assertEquals(-5.0f, result1, 0.001f);

        float result2 = GameMath.gate(-10.0f, -15.0f, 0.0f);
        assertEquals(-10.0f, result2, 0.001f);

        float result3 = GameMath.gate(-10.0f, 5.0f, 0.0f);
        assertEquals(0.0f, result3, 0.001f);
    }

    @Test
    public void testGateWithZeroRange() {
        // When min equals max, should always return that value
        float result = GameMath.gate(5.0f, 10.0f, 5.0f);
        assertEquals(5.0f, result, 0.001f);
    }

    @Test
    public void testGateWithDecimals() {
        // Should work correctly with decimal values
        float result1 = GameMath.gate(0.5f, 0.75f, 1.5f);
        assertEquals(0.75f, result1, 0.001f);

        float result2 = GameMath.gate(0.5f, 0.25f, 1.5f);
        assertEquals(0.5f, result2, 0.001f);

        float result3 = GameMath.gate(0.5f, 2.0f, 1.5f);
        assertEquals(1.5f, result3, 0.001f);
    }
}
