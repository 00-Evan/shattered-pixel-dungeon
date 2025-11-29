/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Terrain Types and Flags
 * Source: core/src/main/java/com/shatteredpixel/shatteredpixeldungeon/levels/Terrain.java
 */

/**
 * Terrain tile types
 * Source: Terrain.java:26-68
 */
export const TerrainType = {
    CHASM: 0,
    EMPTY: 1,
    GRASS: 2,
    EMPTY_WELL: 3,
    WALL: 4,
    DOOR: 5,
    OPEN_DOOR: 6,
    ENTRANCE: 7,
    ENTRANCE_SP: 37,
    EXIT: 8,
    EMBERS: 9,
    LOCKED_DOOR: 10,
    CRYSTAL_DOOR: 31,
    PEDESTAL: 11,
    WALL_DECO: 12,
    BARRICADE: 13,
    EMPTY_SP: 14,
    HIGH_GRASS: 15,
    FURROWED_GRASS: 30,

    SECRET_DOOR: 16,
    SECRET_TRAP: 17,
    TRAP: 18,
    INACTIVE_TRAP: 19,

    EMPTY_DECO: 20,
    LOCKED_EXIT: 21,
    UNLOCKED_EXIT: 22,
    WELL: 24,
    BOOKSHELF: 27,
    ALCHEMY: 28,

    CUSTOM_DECO_EMPTY: 32,
    CUSTOM_DECO: 23,
    STATUE: 25,
    STATUE_SP: 26,

    REGION_DECO: 33,
    REGION_DECO_ALT: 34,
    MINE_CRYSTAL: 35,
    MINE_BOULDER: 36,

    WATER: 29
};

/**
 * Terrain property flags
 * Source: Terrain.java:71-78
 */
export const TerrainFlags = {
    PASSABLE: 0x01,
    LOS_BLOCKING: 0x02,
    FLAMABLE: 0x04,
    SECRET: 0x08,
    SOLID: 0x10,
    AVOID: 0x20,
    LIQUID: 0x40,
    PIT: 0x80
};

/**
 * Terrain flags lookup table
 * Each terrain type maps to a combination of flags
 * Source: Terrain.java:80-125
 */
export const flags = new Array(256).fill(0);

// Initialize flags for each terrain type
// Source: Terrain.java:82-124
flags[TerrainType.CHASM] = TerrainFlags.AVOID | TerrainFlags.PIT;
flags[TerrainType.EMPTY] = TerrainFlags.PASSABLE;
flags[TerrainType.GRASS] = TerrainFlags.PASSABLE | TerrainFlags.FLAMABLE;
flags[TerrainType.EMPTY_WELL] = TerrainFlags.PASSABLE;
flags[TerrainType.WATER] = TerrainFlags.PASSABLE | TerrainFlags.LIQUID;
flags[TerrainType.WALL] = TerrainFlags.LOS_BLOCKING | TerrainFlags.SOLID;
flags[TerrainType.DOOR] = TerrainFlags.PASSABLE | TerrainFlags.LOS_BLOCKING | TerrainFlags.FLAMABLE | TerrainFlags.SOLID;
flags[TerrainType.OPEN_DOOR] = TerrainFlags.PASSABLE | TerrainFlags.FLAMABLE;
flags[TerrainType.ENTRANCE] = TerrainFlags.PASSABLE;
flags[TerrainType.ENTRANCE_SP] = flags[TerrainType.ENTRANCE];
flags[TerrainType.EXIT] = TerrainFlags.PASSABLE;
flags[TerrainType.EMBERS] = TerrainFlags.PASSABLE;
flags[TerrainType.LOCKED_DOOR] = TerrainFlags.LOS_BLOCKING | TerrainFlags.SOLID;
flags[TerrainType.CRYSTAL_DOOR] = TerrainFlags.SOLID;
flags[TerrainType.PEDESTAL] = TerrainFlags.PASSABLE;
flags[TerrainType.WALL_DECO] = flags[TerrainType.WALL];
flags[TerrainType.BARRICADE] = TerrainFlags.FLAMABLE | TerrainFlags.SOLID | TerrainFlags.LOS_BLOCKING;
flags[TerrainType.EMPTY_SP] = flags[TerrainType.EMPTY];
flags[TerrainType.HIGH_GRASS] = TerrainFlags.PASSABLE | TerrainFlags.LOS_BLOCKING | TerrainFlags.FLAMABLE;
flags[TerrainType.FURROWED_GRASS] = flags[TerrainType.HIGH_GRASS];

flags[TerrainType.SECRET_DOOR] = flags[TerrainType.WALL] | TerrainFlags.SECRET;
flags[TerrainType.SECRET_TRAP] = flags[TerrainType.EMPTY] | TerrainFlags.SECRET;
flags[TerrainType.TRAP] = TerrainFlags.AVOID;
flags[TerrainType.INACTIVE_TRAP] = flags[TerrainType.EMPTY];

flags[TerrainType.EMPTY_DECO] = flags[TerrainType.EMPTY];
flags[TerrainType.LOCKED_EXIT] = TerrainFlags.SOLID;
flags[TerrainType.UNLOCKED_EXIT] = TerrainFlags.PASSABLE;
flags[TerrainType.WELL] = TerrainFlags.AVOID;
flags[TerrainType.BOOKSHELF] = flags[TerrainType.BARRICADE];
flags[TerrainType.ALCHEMY] = TerrainFlags.SOLID;

flags[TerrainType.CUSTOM_DECO_EMPTY] = flags[TerrainType.EMPTY];
flags[TerrainType.CUSTOM_DECO] = TerrainFlags.SOLID;
flags[TerrainType.STATUE] = TerrainFlags.SOLID;
flags[TerrainType.STATUE_SP] = flags[TerrainType.STATUE];

flags[TerrainType.REGION_DECO] = flags[TerrainType.STATUE];
flags[TerrainType.REGION_DECO_ALT] = flags[TerrainType.STATUE_SP];
flags[TerrainType.MINE_CRYSTAL] = TerrainFlags.SOLID;
flags[TerrainType.MINE_BOULDER] = TerrainFlags.SOLID;

/**
 * Discover hidden terrain (reveal secrets)
 * Source: Terrain.java:127-136
 */
export function discover(terr) {
    switch (terr) {
        case TerrainType.SECRET_DOOR:
            return TerrainType.DOOR;
        case TerrainType.SECRET_TRAP:
            return TerrainType.TRAP;
        default:
            return terr;
    }
}

/**
 * Check if terrain has a specific flag
 */
export function hasFlag(terr, flag) {
    return (flags[terr] & flag) !== 0;
}

/**
 * Check if terrain is passable
 */
export function isPassable(terr) {
    return hasFlag(terr, TerrainFlags.PASSABLE);
}

/**
 * Check if terrain blocks line of sight
 */
export function losBlocking(terr) {
    return hasFlag(terr, TerrainFlags.LOS_BLOCKING);
}

/**
 * Check if terrain is solid
 */
export function isSolid(terr) {
    return hasFlag(terr, TerrainFlags.SOLID);
}

/**
 * Check if terrain should be avoided
 */
export function isAvoid(terr) {
    return hasFlag(terr, TerrainFlags.AVOID);
}
