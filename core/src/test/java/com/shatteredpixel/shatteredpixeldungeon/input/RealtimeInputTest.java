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

package com.shatteredpixel.shatteredpixeldungeon.input;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for RealtimeInput state management.
 * These tests verify the enable/disable functionality and movement state handling.
 */
public class RealtimeInputTest {

    @Before
    public void setUp() {
        // Reset to known state before each test
        RealtimeInput.setEnabled(false);
        RealtimeInput.resetMovement();
    }

    @After
    public void tearDown() {
        // Clean up after each test
        RealtimeInput.setEnabled(false);
        RealtimeInput.resetMovement();
    }

    @Test
    public void testDefaultState() {
        // RealtimeInput should start disabled
        assertFalse("Realtime should be disabled by default", RealtimeInput.isEnabled());
    }

    @Test
    public void testEnableRealtimeMode() {
        RealtimeInput.setEnabled(true);
        assertTrue("Realtime should be enabled", RealtimeInput.isEnabled());
    }

    @Test
    public void testDisableRealtimeMode() {
        RealtimeInput.setEnabled(true);
        assertTrue("Realtime should be enabled", RealtimeInput.isEnabled());

        RealtimeInput.setEnabled(false);
        assertFalse("Realtime should be disabled", RealtimeInput.isEnabled());
    }

    @Test
    public void testToggleRealtimeMode() {
        // Test multiple enable/disable cycles
        RealtimeInput.setEnabled(true);
        assertTrue(RealtimeInput.isEnabled());

        RealtimeInput.setEnabled(false);
        assertFalse(RealtimeInput.isEnabled());

        RealtimeInput.setEnabled(true);
        assertTrue(RealtimeInput.isEnabled());
    }

    @Test
    public void testResetMovementClearsAllDirections() {
        // Set all movement flags
        RealtimeInput.moveLeft = true;
        RealtimeInput.moveRight = true;
        RealtimeInput.moveUp = true;
        RealtimeInput.moveDown = true;

        // Reset should clear all flags
        RealtimeInput.resetMovement();

        assertFalse("Move left should be reset", RealtimeInput.moveLeft);
        assertFalse("Move right should be reset", RealtimeInput.moveRight);
        assertFalse("Move up should be reset", RealtimeInput.moveUp);
        assertFalse("Move down should be reset", RealtimeInput.moveDown);
    }

    @Test
    public void testDisablingRealtimeModeResetsMovement() {
        // Enable realtime and set movement flags
        RealtimeInput.setEnabled(true);
        RealtimeInput.moveLeft = true;
        RealtimeInput.moveRight = true;
        RealtimeInput.moveUp = true;
        RealtimeInput.moveDown = true;

        // Disabling should reset movement
        RealtimeInput.setEnabled(false);

        assertFalse("Move left should be reset on disable", RealtimeInput.moveLeft);
        assertFalse("Move right should be reset on disable", RealtimeInput.moveRight);
        assertFalse("Move up should be reset on disable", RealtimeInput.moveUp);
        assertFalse("Move down should be reset on disable", RealtimeInput.moveDown);
    }

    @Test
    public void testMovementStateIndependence() {
        // Test that movement directions can be set independently
        RealtimeInput.moveLeft = true;
        assertTrue("Move left should be true", RealtimeInput.moveLeft);
        assertFalse("Move right should be false", RealtimeInput.moveRight);
        assertFalse("Move up should be false", RealtimeInput.moveUp);
        assertFalse("Move down should be false", RealtimeInput.moveDown);

        RealtimeInput.moveUp = true;
        assertTrue("Move left should still be true", RealtimeInput.moveLeft);
        assertTrue("Move up should be true", RealtimeInput.moveUp);
        assertFalse("Move right should still be false", RealtimeInput.moveRight);
        assertFalse("Move down should still be false", RealtimeInput.moveDown);
    }

    @Test
    public void testDiagonalMovement() {
        // Test that diagonal movement is possible (multiple directions at once)
        RealtimeInput.moveLeft = true;
        RealtimeInput.moveUp = true;

        assertTrue("Move left should be true for diagonal", RealtimeInput.moveLeft);
        assertTrue("Move up should be true for diagonal", RealtimeInput.moveUp);
        assertFalse("Move right should be false", RealtimeInput.moveRight);
        assertFalse("Move down should be false", RealtimeInput.moveDown);
    }

    @Test
    public void testOpposingDirections() {
        // Test that opposing directions can be set (even though it might not make sense in game)
        RealtimeInput.moveLeft = true;
        RealtimeInput.moveRight = true;

        assertTrue("Move left should be settable", RealtimeInput.moveLeft);
        assertTrue("Move right should be settable", RealtimeInput.moveRight);
    }

    @Test
    public void testMovementPersistsWhenEnabled() {
        // Test that movement state persists when realtime mode is enabled
        RealtimeInput.setEnabled(true);
        RealtimeInput.moveLeft = true;

        assertTrue("Realtime should be enabled", RealtimeInput.isEnabled());
        assertTrue("Move left should persist", RealtimeInput.moveLeft);
    }

    @Test
    public void testMultipleResetCalls() {
        // Test that multiple reset calls are safe
        RealtimeInput.moveLeft = true;
        RealtimeInput.resetMovement();
        RealtimeInput.resetMovement();
        RealtimeInput.resetMovement();

        assertFalse("Move left should remain reset", RealtimeInput.moveLeft);
    }

    @Test
    public void testEnableIdempotence() {
        // Test that enabling when already enabled is safe
        RealtimeInput.setEnabled(true);
        RealtimeInput.setEnabled(true);
        assertTrue("Realtime should remain enabled", RealtimeInput.isEnabled());
    }

    @Test
    public void testDisableIdempotence() {
        // Test that disabling when already disabled is safe
        RealtimeInput.setEnabled(false);
        RealtimeInput.setEnabled(false);
        assertFalse("Realtime should remain disabled", RealtimeInput.isEnabled());
    }

    @Test
    public void testMovementStateAfterReenabling() {
        // Test that movement state doesn't persist after disable/enable cycle
        RealtimeInput.setEnabled(true);
        RealtimeInput.moveLeft = true;

        // Disable (should reset movement)
        RealtimeInput.setEnabled(false);

        // Re-enable
        RealtimeInput.setEnabled(true);

        assertFalse("Move left should not persist after disable/enable cycle",
                RealtimeInput.moveLeft);
    }
}
