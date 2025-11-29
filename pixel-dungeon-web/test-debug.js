/*
 * Detailed debugging test
 */

import { Random } from './src/utils/Random.js';
import { EntranceRoom } from './src/levels/rooms/EntranceRoom.js';
import { ExitRoom } from './src/levels/rooms/ExitRoom.js';
import { StandardRoom } from './src/levels/rooms/StandardRoom.js';
import { LoopBuilder } from './src/levels/builders/LoopBuilder.js';
import { RegularLevel } from './src/levels/RegularLevel.js';

console.log('=== Detailed Level Generation Debug ===\n');

Random.pushGenerator_seed(12345);

console.log('Test 1: Create rooms manually');
const entrance = EntranceRoom.createEntrance();
const exit = ExitRoom.createExit();
const standard = StandardRoom.createRoom();

console.log(`  Entrance: ${entrance.width()}x${entrance.height()}`);
console.log(`  Exit: ${exit.width()}x${exit.height()}`);
console.log(`  Standard: ${standard.width()}x${standard.height()}`);

console.log('\nTest 2: Test builder with minimal rooms');
const builder = new LoopBuilder();
const rooms = [entrance, exit, standard];

console.log(`  Calling builder.build()...`);
let buildAttempts = 0;
const maxBuildAttempts = 5;

let result = null;
while (buildAttempts < maxBuildAttempts && result === null) {
    buildAttempts++;
    console.log(`  Build attempt ${buildAttempts}...`);
    result = builder.build([...rooms]);

    if (result === null) {
        console.log(`    Build failed, retrying...`);
        // Reset rooms
        for (const r of rooms) {
            r.neigbours = [];
            r.connected = new Map();
        }
    } else {
        console.log(`    Build succeeded! ${result.length} rooms placed`);
    }
}

if (result === null) {
    console.log(`\n✗ Builder failed after ${maxBuildAttempts} attempts`);
    console.log('This suggests a fundamental issue with room placement');
} else {
    console.log(`\n✓ Builder succeeded`);
    console.log(`  Total rooms: ${result.length}`);
    console.log(`  Rooms: ${result.map(r => `${r.width()}x${r.height()}`).join(', ')}`);
}

Random.popGenerator();

console.log('\n=== Debug Complete ===');
