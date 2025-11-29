/*
 * Simple test to debug level generation issues
 */

import { Random } from './src/utils/Random.js';
import { EntranceRoom } from './src/levels/rooms/EntranceRoom.js';
import { ExitRoom } from './src/levels/rooms/ExitRoom.js';
import { StandardRoom } from './src/levels/rooms/StandardRoom.js';
import { LoopBuilder } from './src/levels/builders/LoopBuilder.js';

console.log('=== Simple Level Generation Debug ===\n');

Random.pushGenerator_seed(12345);

console.log('Step 1: Create entrance room');
const entrance = EntranceRoom.createEntrance();
console.log(`  Entrance created: ${entrance}`);
console.log(`  Entrance setSize result:`, entrance.setSize());
console.log(`  Entrance dimensions: ${entrance.width()}x${entrance.height()}`);

console.log('\nStep 2: Create exit room');
const exit = ExitRoom.createExit();
console.log(`  Exit created: ${exit}`);
console.log(`  Exit setSize result:`, exit.setSize());
console.log(`  Exit dimensions: ${exit.width()}x${exit.height()}`);

console.log('\nStep 3: Create standard room');
const standard = StandardRoom.createRoom();
console.log(`  Standard created: ${standard}`);
console.log(`  Standard setSizeCat result:`, standard.setSizeCat(2));
console.log(`  Standard dimensions: ${standard.width()}x${standard.height()}`);

console.log('\nStep 4: Create builder and test room placement');
const builder = new LoopBuilder();
const rooms = [entrance, exit, standard];

console.log(`  Created builder`);
console.log(`  Rooms to build: ${rooms.length}`);

console.log('\nStep 5: Test builder.setupRooms');
try {
    builder.setupRooms(rooms);
    console.log(`  setupRooms succeeded`);
    console.log(`  Entrance: ${builder.entrance}`);
    console.log(`  Exit: ${builder.exit}`);
    console.log(`  Main path rooms: ${builder.mainPathRooms.length}`);
} catch (error) {
    console.error(`  setupRooms failed:`, error.message);
    console.error(error.stack);
}

console.log('\n✓ Simple test completed');

Random.popGenerator();
