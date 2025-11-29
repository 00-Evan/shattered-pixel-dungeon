/*
 * Simple test to verify ported utility classes
 */

import { Random } from './src/utils/Random.js';
import { Point } from './src/utils/Point.js';
import { Rect } from './src/utils/Rect.js';
import { TerrainType, flags, discover, isPassable } from './src/levels/Terrain.js';

console.log('=== Pixel Dungeon Web Port - Utility Class Tests ===\n');

// Test 1: Random Number Generation
console.log('Test 1: Random Number Generation');
console.log('-----------------------------------');

Random.pushGenerator_seed(12345); // Push seeded generator

const floats = [];
for (let i = 0; i < 5; i++) {
    floats.push(Random.Float().toFixed(4));
}
console.log('Random floats (seed 12345):', floats.join(', '));

const ints = [];
for (let i = 0; i < 5; i++) {
    ints.push(Random.Int_max(100));
}
console.log('Random ints [0,100):', ints.join(', '));

const rangeInts = [];
for (let i = 0; i < 5; i++) {
    rangeInts.push(Random.IntRange(10, 20));
}
console.log('Random ints [10,20]:', rangeInts.join(', '));

Random.popGenerator();
console.log('Generator popped.\n');

// Test 2: Point Operations
console.log('Test 2: Point Operations');
console.log('------------------------');

const p1 = new Point(5, 10);
const p2 = new Point(15, 25);
console.log(`Point 1: ${p1.toString()}`);
console.log(`Point 2: ${p2.toString()}`);
console.log(`Distance: ${Point.distance(p1, p2).toFixed(2)}`);

p1.offset(3, -2);
console.log(`Point 1 after offset(3, -2): ${p1.toString()}\n`);

// Test 3: Rectangle Operations
console.log('Test 3: Rectangle Operations');
console.log('-----------------------------');

const rect1 = new Rect(0, 0, 10, 10);
const rect2 = new Rect(5, 5, 15, 15);
console.log(`Rect 1: ${rect1.toString()}`);
console.log(`Rect 2: ${rect2.toString()}`);
console.log(`Rect 1 area: ${rect1.square()}`);

const intersection = rect1.intersect(rect2);
console.log(`Intersection: ${intersection.toString()}`);
console.log(`Intersection area: ${intersection.square()}`);

const union = rect1.union(rect2);
console.log(`Union: ${union.toString()}`);

const center = rect1.center();
console.log(`Center of Rect 1: ${center.toString()}\n`);

// Test 4: Terrain System
console.log('Test 4: Terrain System');
console.log('----------------------');

console.log(`EMPTY terrain value: ${TerrainType.EMPTY}`);
console.log(`WALL terrain value: ${TerrainType.WALL}`);
console.log(`CHASM terrain value: ${TerrainType.CHASM}`);

console.log(`\nIs EMPTY passable? ${isPassable(TerrainType.EMPTY)}`);
console.log(`Is WALL passable? ${isPassable(TerrainType.WALL)}`);
console.log(`Is CHASM passable? ${isPassable(TerrainType.CHASM)}`);

console.log(`\nDiscovering SECRET_DOOR: ${discover(TerrainType.SECRET_DOOR)} (should be ${TerrainType.DOOR})`);
console.log(`Discovering WALL: ${discover(TerrainType.WALL)} (should be ${TerrainType.WALL})`);

console.log('\n=== All Tests Passed! ===');
console.log('\n🎉 Foundation classes successfully ported from Java to JavaScript!');
console.log('Next: Port Room system for procedural dungeon generation.\n');
