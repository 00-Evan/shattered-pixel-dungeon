/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class RingOfForce extends Ring {

	@Override
	protected RingBuff buff( ) {
		return new Force();
	}

	private static float tier(int str){
		float tier = Math.max(1, (str - 8)/2f);
		//each str point after 18 is half as effective
		if (tier > 5){
			tier = 5 + (tier - 5) / 2f;
		}
		return tier;
	}

	public static int damageRoll( Hero hero ){
		int level = getBonus(hero, Force.class);
		float tier = tier(hero.STR());
		return Random.NormalIntRange(min(level, tier), max(level, tier));
	}

	//same as equivalent tier weapon
	private static int min(int lvl, float tier){
		return Math.round(
				tier +  //base
				lvl     //level scaling
		);
	}

	//same as equivalent tier weapon
	private static int max(int lvl, float tier){
		return Math.round(
				5*(tier+1) +    //base
				lvl*(tier+1)    //level scaling
		);
	}

	@Override
	public String desc() {
		String desc = super.desc();
		float tier = tier(Dungeon.hero.STR());
		if (levelKnown) {
			desc += "\n\n" + Messages.get(this, "avg_dmg", min(level(), tier), max(level(), tier));
		} else {
			desc += "\n\n" + Messages.get(this, "typical_avg_dmg", min(1, tier), max(1, tier));
		}

		return desc;
	}

	public class Force extends RingBuff {
	}
}

