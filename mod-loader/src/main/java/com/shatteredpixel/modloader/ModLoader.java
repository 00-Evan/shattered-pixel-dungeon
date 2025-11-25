package com.shatteredpixel.modloader;

import com.shatteredpixel.engine.GameEngine;

/**
 * Mod loading and injection system.
 * Extends engine capabilities without modifying core code.
 * Remains headless (no LibGDX).
 */
public class ModLoader {

    private final GameEngine engine;

    public ModLoader(GameEngine engine) {
        this.engine = engine;
    }

    public void loadMods() {
        System.out.println("ModLoader: Scanning for mods...");
        // Future: Load and inject mods here
    }

    public void applyModHooks() {
        System.out.println("ModLoader: Applying mod hooks to engine...");
        // Future: Hook into engine systems
    }
}
