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

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;

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
		int level = max(0, armor.buffedLvl());
		float procChance = (level+2f)/(level+12f) * procChanceMultiplier(defender);

		if ( attacker.alignment != defender.alignment && Random.Float() < procChance ) {
			float powerMulti = max(1f, procChance);

			float boost = (powerMulti * (3+level));

			Stamina stamina = defender.buff(Stamina.class);
			if(stamina != null) {
				//boost up to maxtotal
				float maxTotal = 1.5f*boost;
				float diff = max(maxTotal - stamina.cooldown(), 0);
				boost = min(diff, boost);
			}

			Buff.affect( defender, Stamina.class, boost);

			//defender.sprite.centerEmitter().start(BloodParticle.BURST, 0.05f, 10);
			//defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
			defender.sprite.centerEmitter().burst(SparkParticle.FACTORY, 10);
		}

		return damage;
	}

	public static float speedBoost( Char owner, int level ){
		//POLISHED: removed

		return 1f;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return YELLOW;
	}
}
