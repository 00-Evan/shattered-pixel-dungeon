package com.shatteredpixel.client.desktop;

import com.shatteredpixel.client.PlatformBridge;
import com.shatteredpixel.engine.GameEngine;

/**
 * Desktop (LWJGL3) launcher.
 * Entry point for desktop builds.
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        System.out.println("=== Shattered Pixel Dungeon - Desktop Launch ===");

        // Create platform implementation
        PlatformBridge platform = new PlatformBridge() {
            @Override
            public String getPlatformName() {
                return "Desktop (LWJGL3)";
            }
        };

        // Create headless engine with platform abstraction
        GameEngine engine = new GameEngine(platform);
        engine.initialize();

        System.out.println("Engine running: " + engine.isRunning());
        System.out.println("Platform supports graphics: " + platform.supportsFeature("graphics"));

        // Future: Launch LibGDX ApplicationListener here
    }
}
