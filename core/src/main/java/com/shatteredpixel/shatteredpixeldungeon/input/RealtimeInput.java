package com.shatteredpixel.shatteredpixeldungeon.input;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

public final class RealtimeInput {


    private RealtimeInput() {}

    // Enable/disable realtime mode gating in input handler
    private static volatile boolean enabled = false;

    // Movement states
    public static volatile boolean moveLeft  = false;
    public static volatile boolean moveRight = false;
    public static volatile boolean moveUp    = false;
    public static volatile boolean moveDown  = false;

    

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
        if (!enabled) {
            resetMovement();
        }
    }

    public static void resetMovement() {
        moveLeft = moveRight = moveUp = moveDown = false;
    }

    // Wrapper called by the realtime input handler when Space/Action is pressed
    public static void onActionPressed() {
        if (!enabled || Dungeon.hero == null || Dungeon.level == null) return;
        Dungeon.hero.performRealtimeInteraction();
    }
}