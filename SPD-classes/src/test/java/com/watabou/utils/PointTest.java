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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PointTest {

    @Test
    public void testDefaultConstructor() {
        Point p = new Point();
        assertEquals(0, p.x);
        assertEquals(0, p.y);
    }

    @Test
    public void testParameterizedConstructor() {
        Point p = new Point(5, 10);
        assertEquals(5, p.x);
        assertEquals(10, p.y);
    }

    @Test
    public void testCopyConstructor() {
        Point original = new Point(3, 7);
        Point copy = new Point(original);

        assertEquals(original.x, copy.x);
        assertEquals(original.y, copy.y);

        // Verify it's a true copy, not a reference
        copy.x = 99;
        assertEquals(3, original.x); // Original should be unchanged
    }

    @Test
    public void testSet() {
        Point p = new Point(1, 2);
        Point result = p.set(5, 10);

        assertEquals(5, p.x);
        assertEquals(10, p.y);
        assertSame(p, result); // Should return itself for chaining
    }

    @Test
    public void testSetFromPoint() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(5, 10);

        Point result = p1.set(p2);

        assertEquals(5, p1.x);
        assertEquals(10, p1.y);
        assertSame(p1, result); // Should return itself for chaining
    }

    @Test
    public void testClone() {
        Point original = new Point(8, 12);
        Point cloned = original.clone();

        assertEquals(original.x, cloned.x);
        assertEquals(original.y, cloned.y);
        assertNotSame(original, cloned); // Should be different objects
    }

    @Test
    public void testScale() {
        Point p = new Point(4, 8);
        Point result = p.scale(2.0f);

        assertEquals(8, p.x);
        assertEquals(16, p.y);
        assertSame(p, result); // Should return itself for chaining
    }

    @Test
    public void testScaleWithDecimal() {
        Point p = new Point(10, 20);
        p.scale(0.5f);

        assertEquals(5, p.x);
        assertEquals(10, p.y);
    }

    @Test
    public void testOffset() {
        Point p = new Point(3, 4);
        Point result = p.offset(2, 5);

        assertEquals(5, p.x);
        assertEquals(9, p.y);
        assertSame(p, result); // Should return itself for chaining
    }

    @Test
    public void testOffsetNegative() {
        Point p = new Point(10, 10);
        p.offset(-3, -7);

        assertEquals(7, p.x);
        assertEquals(3, p.y);
    }

    @Test
    public void testOffsetWithPoint() {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(2, 5);

        Point result = p1.offset(p2);

        assertEquals(5, p1.x);
        assertEquals(9, p1.y);
        assertSame(p1, result); // Should return itself for chaining
    }

    @Test
    public void testIsZero() {
        Point zero = new Point(0, 0);
        assertTrue(zero.isZero());

        Point nonZero1 = new Point(1, 0);
        assertFalse(nonZero1.isZero());

        Point nonZero2 = new Point(0, 1);
        assertFalse(nonZero2.isZero());

        Point nonZero3 = new Point(5, 5);
        assertFalse(nonZero3.isZero());
    }

    @Test
    public void testLength() {
        Point p1 = new Point(3, 4);
        assertEquals(5.0f, p1.length(), 0.001f); // 3-4-5 triangle

        Point p2 = new Point(0, 0);
        assertEquals(0.0f, p2.length(), 0.001f);

        Point p3 = new Point(5, 0);
        assertEquals(5.0f, p3.length(), 0.001f);
    }

    @Test
    public void testDistance() {
        Point a = new Point(0, 0);
        Point b = new Point(3, 4);

        assertEquals(5.0f, Point.distance(a, b), 0.001f); // 3-4-5 triangle
        assertEquals(5.0f, Point.distance(b, a), 0.001f); // Should be symmetric

        Point c = new Point(5, 5);
        Point d = new Point(5, 5);
        assertEquals(0.0f, Point.distance(c, d), 0.001f); // Same point
    }

    @Test
    public void testEquals() {
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);
        Point p3 = new Point(5, 11);
        Point p4 = new Point(6, 10);

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1)); // Symmetric
        assertFalse(p1.equals(p3)); // Different y
        assertFalse(p1.equals(p4)); // Different x
        assertFalse(p1.equals(null)); // Null check
        assertFalse(p1.equals("not a point")); // Different type
    }

    @Test
    public void testMethodChaining() {
        // Test that methods can be chained together
        Point p = new Point(10, 10);
        Point result = p.set(5, 5).offset(3, 2).scale(2.0f);

        assertEquals(16, p.x); // (5 + 3) * 2
        assertEquals(14, p.y); // (5 + 2) * 2
        assertSame(p, result);
    }
}
