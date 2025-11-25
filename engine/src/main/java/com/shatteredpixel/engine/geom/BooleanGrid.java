package com.shatteredpixel.engine.geom;

/**
 * Specialized 2D boolean grid for dungeon maps, visibility, pathfinding masks, etc.
 * More memory-efficient than Grid<Boolean> and provides boolean-specific operations.
 *
 * Migrated concepts from: com.watabou.utils.BArray
 * Changes:
 * - Proper 2D grid wrapper instead of 1D array utilities
 * - Bounds checking
 * - Boolean logic operations (and, or, not)
 * - GWT-safe (Java 11)
 */
public class BooleanGrid {

    private final int width;
    private final int height;
    private final boolean[][] data;

    // =========================================================================
    // Constructors
    // =========================================================================

    /**
     * Create a boolean grid with all cells initialized to false.
     */
    public BooleanGrid(int width, int height) {
        this(width, height, false);
    }

    /**
     * Create a boolean grid with all cells initialized to the specified value.
     */
    public BooleanGrid(int width, int height, boolean initialValue) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }

        this.width = width;
        this.height = height;
        this.data = new boolean[height][width];

        if (initialValue) {
            fill(true);
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

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean inBounds(Point p) {
        return inBounds(p.x, p.y);
    }

    // =========================================================================
    // Access Methods
    // =========================================================================

    /**
     * Get the value at the specified coordinates.
     * Returns false if out of bounds.
     */
    public boolean get(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }
        return data[y][x];
    }

    public boolean get(Point p) {
        return get(p.x, p.y);
    }

    /**
     * Set the value at the specified coordinates.
     * Does nothing if out of bounds.
     */
    public void set(int x, int y, boolean value) {
        if (inBounds(x, y)) {
            data[y][x] = value;
        }
    }

    public void set(Point p, boolean value) {
        set(p.x, p.y, value);
    }

    // =========================================================================
    // Bulk Operations
    // =========================================================================

    /**
     * Fill the entire grid with a value.
     */
    public void fill(boolean value) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = value;
            }
        }
    }

    /**
     * Fill a rectangular region with a value.
     */
    public void fill(Rect rect, boolean value) {
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
     * Clear the grid (set all to false).
     */
    public void clear() {
        fill(false);
    }

    /**
     * Set all to true.
     */
    public void setAll() {
        fill(true);
    }

    // =========================================================================
    // Boolean Logic Operations
    // =========================================================================

    /**
     * Logical AND with another grid (this = this AND other).
     * Grids must be same size.
     */
    public void and(BooleanGrid other) {
        if (width != other.width || height != other.height) {
            throw new IllegalArgumentException("Grids must be same size for AND operation");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = data[y][x] && other.data[y][x];
            }
        }
    }

    /**
     * Logical OR with another grid (this = this OR other).
     * Grids must be same size.
     */
    public void or(BooleanGrid other) {
        if (width != other.width || height != other.height) {
            throw new IllegalArgumentException("Grids must be same size for OR operation");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = data[y][x] || other.data[y][x];
            }
        }
    }

    /**
     * Logical NOT (invert all values).
     */
    public void not() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = !data[y][x];
            }
        }
    }

    // =========================================================================
    // Counting
    // =========================================================================

    /**
     * Count true cells.
     */
    public int countTrue() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (data[y][x]) count++;
            }
        }
        return count;
    }

    /**
     * Count false cells.
     */
    public int countFalse() {
        return size() - countTrue();
    }

    /**
     * Check if all cells are true.
     */
    public boolean allTrue() {
        return countTrue() == size();
    }

    /**
     * Check if all cells are false.
     */
    public boolean allFalse() {
        return countTrue() == 0;
    }

    // =========================================================================
    // Copying
    // =========================================================================

    /**
     * Create a deep copy of this grid.
     */
    public BooleanGrid copy() {
        BooleanGrid copy = new BooleanGrid(width, height);
        for (int y = 0; y < height; y++) {
            System.arraycopy(data[y], 0, copy.data[y], 0, width);
        }
        return copy;
    }

    @Override
    public String toString() {
        return "BooleanGrid[" + width + "x" + height + ", " + countTrue() + " true]";
    }
}
