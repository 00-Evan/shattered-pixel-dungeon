/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredicepixeldungeon.levels.features;

import com.shatteredpixel.shatteredicepixeldungeon.Challenges;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredicepixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredicepixeldungeon.items.Generator;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredicepixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		
		Level.set( pos, Terrain.GRASS );
		GameScene.updateMap( pos );

        if (!Dungeon.isChallenged( Challenges.NO_HERBALISM )) {
            int naturalismLevel = 0;

            if (ch != null) {
                SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
                if (naturalism != null) {
                    if (!naturalism.isCursed()) {
                        naturalismLevel = naturalism.level() + 1;
                        naturalism.charge();
                    } else {
                        naturalismLevel = -1;
                    }
                }
            }

            if (naturalismLevel >= 0) {
                // Seed
                if (Random.Int(18 - ((int) (naturalismLevel * 3.34))) == 0) {
                    Item seed = Generator.random(Generator.Category.SEED);

                    if (seed instanceof BlandfruitBush.Seed) {
                        if (Random.Int(15) - Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
                            level.drop(seed, pos).sprite.drop();
                            Dungeon.limitedDrops.blandfruitSeed.count++;
                        }
                    } else
                        level.drop(seed, pos).sprite.drop();
                }

                // Dew
                if (Random.Int(6 - naturalismLevel) == 0) {
                    level.drop(new Dewdrop(), pos).sprite.drop();
                }
            }
        }

		int leaves = 4;
		
		// Barkskin
		if (ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
			Buff.affect( ch, Barkskin.class ).level( ch.HT / 3 );
			leaves = 8;
		}
		
		CellEmitter.get( pos ).burst( LeafParticle.LEVEL_SPECIFIC, leaves );
		Dungeon.observe();
	}
}
