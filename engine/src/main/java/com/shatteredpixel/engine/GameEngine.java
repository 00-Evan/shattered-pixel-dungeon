package com.shatteredpixel.engine;

import com.shatteredpixel.api.GamePlatform;

/**
 * Core headless game engine.
 * Contains game logic and simulation.
 * No LibGDX dependencies - completely platform-agnostic.
 */
public class GameEngine {

    private final GamePlatform platform;
    private boolean running;

    public GameEngine(GamePlatform platform) {
        this.platform = platform;
        this.running = false;
    }

    public void initialize() {
        System.out.println("GameEngine initializing on platform: " + platform.getPlatformName());
        platform.initialize();
        this.running = true;
    }

    public void update(float deltaTime) {
        if (!running) {
            throw new IllegalStateException("Engine not initialized");
        }
        // Core game logic goes here (deterministic, headless)
    }

    public boolean isRunning() {
        return running;
    }
}
