package com.shatteredpixel.api;

/**
 * Platform abstraction interface.
 * Implementations will be provided by client modules.
 * Must remain GWT-compatible (Java 11).
 */
public interface GamePlatform {

    /**
     * Get platform name (e.g., "Desktop", "Android", "Web")
     */
    String getPlatformName();

    /**
     * Platform-specific initialization
     */
    void initialize();

    /**
     * Check if platform supports a specific feature
     */
    boolean supportsFeature(String featureName);
}
