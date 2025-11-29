/*
 * Headless Dungeon Generation Test
 * Generates a dungeon and displays it as ASCII art
 */

import { RegularLevel } from './src/levels/RegularLevel.js';
import { TerrainType } from './src/levels/Terrain.js';

console.log('=== Pixel Dungeon - Headless Debug Test ===\n');

try {
    // Create and generate level
    const level = new RegularLevel();
    const seed = 12345; // Fixed seed for reproducibility

    console.log(`Generating with seed: ${seed}`);
    level.create(seed);

    console.log('✓ Level generated successfully!\n');
    console.log(`Dimensions: ${level.width} × ${level.height} (${level.length} cells)`);
    console.log(`Rooms: ${level.rooms ? level.rooms.length : 'N/A'}`);
    console.log(`Feeling: ${level.feeling || 'NONE'}\n`);

    // Build ASCII map
    let mapString = '';

    for (let y = 0; y < level.height; y++) {
        for (let x = 0; x < level.width; x++) {
            const cell = x + y * level.width;
            const terrain = level.map[cell];

            // Check if this is entrance or exit
            let char = ' ';

            if (level.entrance === cell) {
                char = 'E';
            } else if (level.exit === cell) {
                char = 'X';
            } else {
                // Map terrain to characters
                switch (terrain) {
                    case TerrainType.WALL:
                        char = '#';
                        break;
                    case TerrainType.EMPTY:
                    case TerrainType.EMPTY_SP:
                    case TerrainType.EMPTY_DECO:
                        char = '.';
                        break;
                    case TerrainType.DOOR:
                    case TerrainType.OPEN_DOOR:
                    case TerrainType.LOCKED_DOOR:
                        char = '+';
                        break;
                    case TerrainType.WATER:
                        char = '~';
                        break;
                    case TerrainType.GRASS:
                    case TerrainType.HIGH_GRASS:
                    case TerrainType.FURROWED_GRASS:
                        char = ',';
                        break;
                    case TerrainType.CHASM:
                        char = ' ';
                        break;
                    case TerrainType.ENTRANCE:
                        char = 'E';
                        break;
                    case TerrainType.EXIT:
                        char = 'X';
                        break;
                    default:
                        char = '?';
                        break;
                }
            }

            mapString += char;
        }
        mapString += '\n';
    }

    // Display map
    console.log('Map:');
    console.log('═'.repeat(level.width + 2));
    console.log(mapString);
    console.log('═'.repeat(level.width + 2));

    // Stats
    console.log('\nLegend:');
    console.log('  # = Wall');
    console.log('  . = Floor/Empty');
    console.log('  + = Door');
    console.log('  E = Entrance');
    console.log('  X = Exit');

    // Count terrain types
    const counts = {};
    for (let i = 0; i < level.map.length; i++) {
        const t = level.map[i];
        counts[t] = (counts[t] || 0) + 1;
    }

    console.log('\nTerrain Distribution:');
    const walls = counts[TerrainType.WALL] || 0;
    const empty = counts[TerrainType.EMPTY] || 0;
    const doors = counts[TerrainType.DOOR] || 0;

    console.log(`  Walls:  ${walls} (${(walls / level.length * 100).toFixed(1)}%)`);
    console.log(`  Empty:  ${empty} (${(empty / level.length * 100).toFixed(1)}%)`);
    if (doors > 0) {
        console.log(`  Doors:  ${doors}`);
    }

    console.log('\n✓ Test Passed!\n');

} catch (error) {
    console.error('✗ Level generation failed:', error);
    console.error(error.stack);
    process.exit(1);
}
