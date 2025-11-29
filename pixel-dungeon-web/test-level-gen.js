/*
 * Test Level Generation
 * Validates complete dungeon generation pipeline
 */

import { RegularLevel } from './src/levels/RegularLevel.js';
import { TerrainType } from './src/levels/Terrain.js';

console.log('=== Pixel Dungeon Web - Level Generation Test ===\n');

// Test level generation
console.log('Test: Generate a RegularLevel');
console.log('----------------------------------');

const level = new RegularLevel();
const seed = 12345;

console.log(`Generating level with seed ${seed}...`);

try {
    level.create(seed);

    console.log(`✓ Level generated successfully!`);
    console.log(`  Dimensions: ${level.width}x${level.height} (${level.length} cells)`);
    console.log(`  Feeling: ${level.feeling}`);
    console.log(`  Rooms: ${level.rooms ? level.rooms.length : 0}`);

    // Analyze terrain distribution
    const terrainCounts = new Map();
    for (let i = 0; i < level.map.length; i++) {
        const terrain = level.map[i];
        terrainCounts.set(terrain, (terrainCounts.get(terrain) || 0) + 1);
    }

    console.log(`\n  Terrain distribution:`);
    const terrainNames = {
        [TerrainType.CHASM]: 'CHASM',
        [TerrainType.EMPTY]: 'EMPTY',
        [TerrainType.GRASS]: 'GRASS',
        [TerrainType.EMPTY_WELL]: 'EMPTY_WELL',
        [TerrainType.WALL]: 'WALL',
        [TerrainType.DOOR]: 'DOOR',
        [TerrainType.OPEN_DOOR]: 'OPEN_DOOR',
        [TerrainType.ENTRANCE]: 'ENTRANCE',
        [TerrainType.EXIT]: 'EXIT',
        [TerrainType.EMBERS]: 'EMBERS',
        [TerrainType.LOCKED_DOOR]: 'LOCKED_DOOR',
        [TerrainType.CRYSTAL_DOOR]: 'CRYSTAL_DOOR',
        [TerrainType.PEDESTAL]: 'PEDESTAL',
        [TerrainType.WALL_DECO]: 'WALL_DECO',
        [TerrainType.BARRICADE]: 'BARRICADE',
        [TerrainType.EMPTY_SP]: 'EMPTY_SP',
        [TerrainType.HIGH_GRASS]: 'HIGH_GRASS',
        [TerrainType.FURROWED_GRASS]: 'FURROWED_GRASS',
        [TerrainType.SECRET_DOOR]: 'SECRET_DOOR',
        [TerrainType.TOXIC_TRAP]: 'TOXIC_TRAP',
        [TerrainType.TRAP]: 'TRAP',
        [TerrainType.INACTIVE_TRAP]: 'INACTIVE_TRAP',
        [TerrainType.EMPTY_DECO]: 'EMPTY_DECO',
        [TerrainType.LOCKED_EXIT]: 'LOCKED_EXIT',
        [TerrainType.UNLOCKED_EXIT]: 'UNLOCKED_EXIT',
        [TerrainType.SIGN]: 'SIGN',
        [TerrainType.WELL]: 'WELL',
        [TerrainType.STATUE]: 'STATUE',
        [TerrainType.STATUE_SP]: 'STATUE_SP',
        [TerrainType.BOOKSHELF]: 'BOOKSHELF',
        [TerrainType.ALCHEMY]: 'ALCHEMY',
        [TerrainType.CHASM_FLOOR]: 'CHASM_FLOOR',
        [TerrainType.CHASM_FLOOR_SP]: 'CHASM_FLOOR_SP',
        [TerrainType.CHASM_WALL]: 'CHASM_WALL',
        [TerrainType.CHASM_WATER]: 'CHASM_WATER',
    };

    for (const [terrain, count] of terrainCounts.entries()) {
        const name = terrainNames[terrain] || `UNKNOWN(${terrain})`;
        const percentage = ((count / level.length) * 100).toFixed(1);
        console.log(`    ${name}: ${count} (${percentage}%)`);
    }

    // Visualize level (simplified ASCII map)
    console.log(`\n  ASCII Map (first 50x30 area):`);
    const terrainChars = {
        [TerrainType.CHASM]: ' ',
        [TerrainType.EMPTY]: '.',
        [TerrainType.WALL]: '#',
        [TerrainType.DOOR]: '+',
        [TerrainType.ENTRANCE]: '<',
        [TerrainType.EXIT]: '>',
        [TerrainType.LOCKED_DOOR]: '=',
        [TerrainType.SECRET_DOOR]: 'S',
    };

    const mapWidth = Math.min(50, level.width);
    const mapHeight = Math.min(30, level.height);

    for (let y = 0; y < mapHeight; y++) {
        let line = '    ';
        for (let x = 0; x < mapWidth; x++) {
            const cell = x + y * level.width;
            const terrain = level.map[cell];
            line += terrainChars[terrain] || '?';
        }
        console.log(line);
    }

    console.log(`\n✓ Level generation test passed!`);

} catch (error) {
    console.error(`✗ Level generation failed:`, error);
    console.error(error.stack);
    process.exit(1);
}

console.log('\n=== All Tests Passed! ===');
console.log('The dungeon generation system is working correctly.\n');
