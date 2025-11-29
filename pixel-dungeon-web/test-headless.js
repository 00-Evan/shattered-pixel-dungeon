/*
 * Headless Dungeon Generation Test
 * Generates multiple dungeons with different seeds for stress testing
 */

import { RegularLevel } from './src/levels/RegularLevel.js';
import { TerrainType } from './src/levels/Terrain.js';

console.log('=== Pixel Dungeon - Stress Test (5 Generations) ===\n');

// Test seeds
const seeds = [12345, 67890, 99999, 42000, 88888];

let successCount = 0;
let failCount = 0;

for (let runIndex = 0; runIndex < seeds.length; runIndex++) {
    const seed = seeds[runIndex];

    console.log(`\n${'='.repeat(60)}`);
    console.log(`RUN ${runIndex + 1}/5`);
    console.log(`${'='.repeat(60)}\n`);

    try {
        // Create and generate level
        const level = new RegularLevel();

        console.log(`Seed: ${seed}`);
        console.log('Generating...');

        level.create(seed);

        console.log(`Status: ✓ SUCCESS\n`);
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
        console.log(mapString);

        // Count terrain types
        const counts = {};
        for (let i = 0; i < level.map.length; i++) {
            const t = level.map[i];
            counts[t] = (counts[t] || 0) + 1;
        }

        const walls = counts[TerrainType.WALL] || 0;
        const empty = counts[TerrainType.EMPTY] || 0;
        const doors = counts[TerrainType.DOOR] || 0;

        console.log('Terrain Distribution:');
        console.log(`  Walls:  ${walls} (${(walls / level.length * 100).toFixed(1)}%)`);
        console.log(`  Empty:  ${empty} (${(empty / level.length * 100).toFixed(1)}%)`);
        if (doors > 0) {
            console.log(`  Doors:  ${doors}`);
        }

        successCount++;

    } catch (error) {
        console.log(`Status: ✗ FAILED\n`);
        console.error('Error:', error.message);
        console.error('Stack:', error.stack);
        failCount++;
    }

    console.log(`\n${'-'.repeat(60)}\n`);
}

// Final summary
console.log('\n' + '='.repeat(60));
console.log('STRESS TEST SUMMARY');
console.log('='.repeat(60));
console.log(`Total Runs:    ${seeds.length}`);
console.log(`✓ Successful:  ${successCount}`);
console.log(`✗ Failed:      ${failCount}`);
console.log(`Success Rate:  ${(successCount / seeds.length * 100).toFixed(1)}%`);
console.log('='.repeat(60));

if (failCount === 0) {
    console.log('\n✓ All tests passed! Generator is stable.\n');
    process.exit(0);
} else {
    console.log('\n✗ Some tests failed. Review errors above.\n');
    process.exit(1);
}

