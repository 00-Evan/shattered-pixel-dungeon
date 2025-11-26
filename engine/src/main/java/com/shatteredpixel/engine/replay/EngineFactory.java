package com.shatteredpixel.engine.replay;

/**
 * Functional interface for creating EngineContext + GameEngine instances.
 *
 * Used by replay system to create fresh engine instances with specific RNG seeds.
 *
 * Implementations should:
 * - Create a new EngineContext with the provided seed
 * - Create a new GameEngine with the context
 * - Initialize the engine
 * - Set up any required level/actor state
 * - Return both wrapped in EngineContextWithEngine
 *
 * This abstraction allows tests to control engine creation while
 * keeping replay logic generic.
 */
@FunctionalInterface
public interface EngineFactory {

    /**
     * Create a new engine instance with the given RNG seed.
     *
     * @param seed Deterministic RNG seed
     * @return Wrapper containing both context and engine
     */
    EngineContextWithEngine create(long seed);
}
