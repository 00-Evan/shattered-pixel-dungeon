/*
 * Pixel Dungeon Web Port
 * Ported from Shattered Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * Seeded Random Number Generator with Generator Stack
 * Source: SPD-classes/src/main/java/com/watabou/utils/Random.java
 */

export class Random {
    // Stack of random number generators (seeded or unseeded)
    // Top of stack is currently active generator
    static generators = [];

    static {
        this.resetGenerators();
    }

    /**
     * Reset to a single unseeded generator
     */
    static resetGenerators() {
        this.generators = [this._createGenerator()];
    }

    /**
     * Push a new unseeded generator onto the stack
     */
    static pushGenerator() {
        this.generators.push(this._createGenerator());
    }

    /**
     * Push a new seeded generator onto the stack
     * Source: Random.java:51-53
     */
    static pushGenerator_seed(seed) {
        const scrambledSeed = this._scrambleSeed(seed);
        this.generators.push(this._createGenerator(scrambledSeed));
    }

    /**
     * Scrambles a seed to eliminate patterns between similar seeds
     * Algorithm: MX3 by Jon Maiga (jonkagstrom.com), CC0 license
     * Source: Random.java:57-66
     */
    static _scrambleSeed(seed) {
        // Convert to BigInt for 64-bit operations
        let s = BigInt(seed);
        const m = 0xbea225f9eb34556dn; // Magic number from MX3

        s ^= s >> 32n;
        s = (s * m) & 0xffffffffffffffffn;
        s ^= s >> 29n;
        s = (s * m) & 0xffffffffffffffffn;
        s ^= s >> 32n;
        s = (s * m) & 0xffffffffffffffffn;
        s ^= s >> 29n;

        return Number(s & 0xffffffffn); // Return as 32-bit number
    }

    /**
     * Pop a generator from the stack
     * Source: Random.java:68-74
     */
    static popGenerator() {
        if (this.generators.length === 1) {
            console.error('Tried to pop the last random number generator!');
        } else {
            this.generators.pop();
        }
    }

    /**
     * Create a seeded pseudo-random generator using mulberry32
     * (Fast, high-quality 32-bit PRNG)
     */
    static _createGenerator(seed = Math.random() * 0xffffffff) {
        let state = seed | 0;
        return {
            nextFloat: () => {
                state = (state + 0x6d2b79f5) | 0;
                let t = Math.imul(state ^ (state >>> 15), 1 | state);
                t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
                return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
            },
            nextInt: (max) => {
                state = (state + 0x6d2b79f5) | 0;
                let t = Math.imul(state ^ (state >>> 15), 1 | state);
                t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
                const val = ((t ^ (t >>> 14)) >>> 0) / 4294967296;
                return max !== undefined ? Math.floor(val * max) : (val * 0xffffffff) | 0;
            },
            nextLong: () => {
                const high = this.nextInt();
                const low = this.nextInt();
                return (BigInt(high) << 32n) | BigInt(low >>> 0);
            }
        };
    }

    // ========== Float Methods ==========
    // Source: Random.java:76-99

    /**
     * Returns uniformly distributed float in [0, 1)
     */
    static Float(useGeneratorStack = true) {
        const gen = useGeneratorStack
            ? this.generators[this.generators.length - 1]
            : this.generators[0];
        return gen.nextFloat();
    }

    /**
     * Returns uniformly distributed float in [0, max)
     */
    static Float_max(max) {
        return this.Float() * max;
    }

    /**
     * Returns uniformly distributed float in [min, max)
     */
    static Float_range(min, max) {
        return min + this.Float(true) * (max - min);
    }

    /**
     * Returns triangularly distributed float in [min, max)
     * Source: Random.java:97-99
     */
    static NormalFloat(min, max) {
        return min + ((this.Float_max(max - min) + this.Float_max(max - min)) / 2);
    }

    // ========== Int Methods ==========
    // Source: Random.java:102-151

    /**
     * Returns uniformly distributed int in [-2^31, 2^31)
     */
    static Int(useGeneratorStack = true) {
        const gen = useGeneratorStack
            ? this.generators[this.generators.length - 1]
            : this.generators[0];
        return gen.nextInt();
    }

    /**
     * Returns uniformly distributed int in [0, max)
     */
    static Int_max(max, useGeneratorStack = true) {
        if (max <= 0) return 0;
        const gen = useGeneratorStack
            ? this.generators[this.generators.length - 1]
            : this.generators[0];
        return gen.nextInt(max);
    }

    /**
     * Returns uniformly distributed int in [min, max)
     */
    static Int_range(min, max) {
        return min + this.Int_max(max - min, true);
    }

    /**
     * Returns uniformly distributed int in [min, max]
     * Source: Random.java:132-134
     */
    static IntRange(min, max) {
        return min + this.Int_max(max - min + 1, true);
    }

    /**
     * Returns triangularly distributed int in [min, max]
     * More likely near the middle of the range
     * Source: Random.java:138-140
     */
    static NormalIntRange(min, max) {
        return min + Math.floor(((this.Float() + this.Float()) * (max - min + 1)) / 2);
    }

    /**
     * Returns inverse triangularly distributed int in [min, max]
     * More likely at the extremes of the range
     * Source: Random.java:144-151
     */
    static InvNormalIntRange(min, max) {
        const roll1 = this.Float();
        const roll2 = this.Float();
        if (Math.abs(roll1 - 0.5) >= Math.abs(roll2 - 0.5)) {
            return min + Math.floor(roll1 * (max - min + 1));
        } else {
            return min + Math.floor(roll2 * (max - min + 1));
        }
    }

    // ========== Long Methods ==========
    // Source: Random.java:154-171

    /**
     * Returns uniformly distributed long in [-2^63, 2^63)
     */
    static Long(useGeneratorStack = true) {
        const gen = useGeneratorStack
            ? this.generators[this.generators.length - 1]
            : this.generators[0];
        return gen.nextLong();
    }

    /**
     * Returns mostly uniformly distributed long in [0, max)
     * Source: Random.java:166-171
     */
    static Long_max(max) {
        let result = this.Long(true);
        if (result < 0n) result += 9223372036854775807n; // Long.MAX_VALUE
        return result % BigInt(max);
    }

    // ========== Weighted Random Selection ==========
    // Source: Random.java:175-229

    /**
     * Returns an index based on weighted chances
     * Negative values are treated as 0
     * Source: Random.java:175-198
     */
    static chances(chances) {
        const length = chances.length;

        let sum = 0;
        for (let i = 0; i < length; i++) {
            sum += Math.max(0, chances[i]);
        }

        if (sum <= 0) return -1;

        const value = this.Float_max(sum);
        sum = 0;
        for (let i = 0; i < length; i++) {
            sum += Math.max(0, chances[i]);
            if (value < sum) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns a key from a Map based on weighted values
     * Source: Random.java:202-229
     */
    static chances_map(chancesMap) {
        const entries = Array.from(chancesMap.entries());
        const size = entries.length;

        const probs = new Array(size);
        let sum = 0;
        for (let i = 0; i < size; i++) {
            probs[i] = entries[i][1];
            sum += probs[i];
        }

        if (sum <= 0) return null;

        const value = this.Float_max(sum);

        sum = probs[0];
        for (let i = 0; i < size; i++) {
            if (value < sum) {
                return entries[i][0];
            }
            sum += probs[i + 1];
        }

        return null;
    }

    // ========== Collection Utilities ==========
    // Source: Random.java:231-284

    /**
     * Returns random element from array
     */
    static element(array, max = array.length) {
        return array[this.Int_max(max, true)];
    }

    /**
     * Shuffle an array in place
     * Source: Random.java:260-269
     */
    static shuffle(array) {
        for (let i = 0; i < array.length - 1; i++) {
            const j = this.Int_range(i, array.length);
            if (j !== i) {
                const temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
    }
}
