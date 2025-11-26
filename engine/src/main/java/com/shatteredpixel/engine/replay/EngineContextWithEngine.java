package com.shatteredpixel.engine.replay;

import com.shatteredpixel.engine.EngineContext;
import com.shatteredpixel.engine.GameEngine;

/**
 * Simple holder pairing an EngineContext with its GameEngine.
 *
 * Used by replay system to pass both objects together through
 * factory methods and replay operations.
 *
 * This avoids the need for complex tuple types or repeated
 * parameter passing.
 */
public class EngineContextWithEngine {

    private final EngineContext context;
    private final GameEngine engine;

    /**
     * Create a context+engine pair.
     *
     * @param context The engine context
     * @param engine The game engine
     */
    public EngineContextWithEngine(EngineContext context, GameEngine engine) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (engine == null) {
            throw new IllegalArgumentException("Engine cannot be null");
        }
        this.context = context;
        this.engine = engine;
    }

    /**
     * Get the engine context.
     *
     * @return EngineContext instance
     */
    public EngineContext getContext() {
        return context;
    }

    /**
     * Get the game engine.
     *
     * @return GameEngine instance
     */
    public GameEngine getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        return "EngineContextWithEngine{" +
            "context=" + context +
            ", engine=" + engine +
            "}";
    }
}
