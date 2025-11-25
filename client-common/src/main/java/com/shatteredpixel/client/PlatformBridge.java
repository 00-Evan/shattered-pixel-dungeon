package com.shatteredpixel.client;

import com.shatteredpixel.api.GamePlatform;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/**
 * Bridge between headless engine and LibGDX.
 * This is the ONLY place where engine APIs meet LibGDX.
 */
public abstract class PlatformBridge implements GamePlatform {

    @Override
    public void initialize() {
        System.out.println("PlatformBridge: Initializing LibGDX on " + getPlatformName());
        if (Gdx.app != null) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        }
    }

    @Override
    public boolean supportsFeature(String featureName) {
        // Default implementation - can be overridden by platform-specific clients
        switch (featureName) {
            case "graphics": return true;
            case "audio": return true;
            default: return false;
        }
    }

    /**
     * Platform-specific clients must implement this
     */
    @Override
    public abstract String getPlatformName();
}
