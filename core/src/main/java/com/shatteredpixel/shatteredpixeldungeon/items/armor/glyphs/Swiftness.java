/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Swiftness extends Armor.Glyph {

	private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF00 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		int level = Math.max(0, armor.buffedLvl());
		float procChance = (level+2f)/(level+12f) * procChanceMultiplier(defender);

		if ( attacker.alignment != defender.alignment && Random.Float() < procChance ) {
			float powerMulti = Math.max(1f, procChance);

			Buff.affect( defender, Stamina.class, Math.round(powerMulti * (3+level)));
			//defender.sprite.centerEmitter().start(BloodParticle.BURST, 0.05f, 10);
			//defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
			defender.sprite.centerEmitter().burst(SparkParticle.FACTORY, 10);
		}

		return damage;
	}

	public static float speedBoost( Char owner, int level ){
		/*
		if (level == -1){
			return 1;
		}

		boolean enemyNear = false;
		//for each enemy, check if they are adjacent, or within 2 tiles and an adjacent cell is open
		for (Char ch : Actor.chars()){
			if ( Dungeon.level.distance(ch.pos, owner.pos) <= 2 && owner.alignment != ch.alignment && ch.alignment != Char.Alignment.NEUTRAL){
				if (Dungeon.level.adjacent(ch.pos, owner.pos)){
					enemyNear = true;
					break;
				} else {
					for (int i : PathFinder.NEIGHBOURS8){
						if (Dungeon.level.adjacent(owner.pos+i, ch.pos) && !Dungeon.level.solid[owner.pos+i]){
							enemyNear = true;
							break;
						}
					}
				}
			}
		}
		if (enemyNear){
			return 1;
		} else {
			return (1.2f + 0.04f * level) * genericProcChanceMultiplier(owner);
		}
		 */

		return 1f;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return YELLOW;
	}
}
