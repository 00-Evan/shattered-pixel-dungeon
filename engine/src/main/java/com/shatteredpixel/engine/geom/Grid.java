package com.shatteredpixel.engine.geom;

/**
 * Generic 2D grid with bounds checking.
 * Provides a safe wrapper around a 2D array with coordinate validation.
 *
 * This is a new class (not directly migrated from legacy code).
 * Provides clean, type-safe grid operations for dungeon maps, FOV, pathfinding, etc.
 *
 * Design goals:
 * - Type-safe with generics
 * - Bounds checking on all operations
 * - Immutable dimensions
 * - GWT-safe (Java 11)
 */
public class Grid<T> {

    private final int width;
    private final int height;
    private final T[][] data;
    private final T defaultValue;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a grid with the specified dimensions.
     * All cells initialized to null.
     */
    @SuppressWarnings("unchecked")
    public Grid(int width, int height) {
        this(width, height, null);
    }

    /**
     * Create a grid with the specified dimensions and default value.
     * All cells initialized to the default value.
     */
    @SuppressWarnings("unchecked")
    public Grid(int width, int height, T defaultValue) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }

        this.width = width;
        this.height = height;
        this.defaultValue = defaultValue;
        this.data = (T[][]) new Object[height][width];

        if (defaultValue != null) {
            fill(defaultValue);
        }
    }

    // =========================================================================
    // Properties
    // =========================================================================

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int size() {
        return width * height;
    }

    // =========================================================================
    // Bounds Checking
    // =========================================================================

    /**
     * Check if coordinates are within grid bounds.
     */
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Check if a point is within grid bounds.
     */
    public boolean inBounds(Point p) {
        return inBounds(p.x, p.y);
    }

    // =========================================================================
    // Access Methods
    // =========================================================================

    /**
     * Get the value at the specified coordinates.
     * Returns null if out of bounds.
     */
    public T get(int x, int y) {
        if (!inBounds(x, y)) {
            return null;
        }
        return data[y][x];
    }

    /**
     * Get the value at the specified point.
     */
    public T get(Point p) {
        return get(p.x, p.y);
    }

    /**
     * Set the value at the specified coordinates.
     * Does nothing if out of bounds.
     */
    public void set(int x, int y, T value) {
        if (inBounds(x, y)) {
            data[y][x] = value;
        }
    }

    /**
     * Set the value at the specified point.
     */
    public void set(Point p, T value) {
        set(p.x, p.y, value);
    }

    // =========================================================================
    // Bulk Operations
    // =========================================================================

    /**
     * Fill the entire grid with a value.
     */
    public void fill(T value) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = value;
            }
        }
    }

    /**
     * Fill a rectangular region with a value.
     */
    public void fill(Rect rect, T value) {
        int x1 = Math.max(0, rect.left);
        int y1 = Math.max(0, rect.top);
        int x2 = Math.min(width, rect.right);
        int y2 = Math.min(height, rect.bottom);

        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                data[y][x] = value;
            }
        }
    }

    /**
     * Clear the entire grid (set all cells to default value).
     */
    public void clear() {
        fill(defaultValue);
    }

    /**
     * Count cells matching a specific value.
     */
    public int count(T value) {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                T cell = data[y][x];
                if ((cell == null && value == null) || (cell != null && cell.equals(value))) {
                    count++;
                }
            }
        }
        return count;
    }

    // =========================================================================
    // Copying
    // =========================================================================

    /**
     * Create a deep copy of this grid.
     */
    @SuppressWarnings("unchecked")
    public Grid<T> copy() {
        Grid<T> copy = new Grid<>(width, height, defaultValue);
        for (int y = 0; y < height; y++) {
            System.arraycopy(data[y], 0, copy.data[y], 0, width);
        }
        return copy;
    }

    @Override
    public String toString() {
        return "Grid[" + width + "x" + height + "]";
    }
}
