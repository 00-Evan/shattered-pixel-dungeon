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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class RingOfForce extends Ring {

	@Override
	protected RingBuff buff( ) {
		return new Force();
	}

	public static int min(int bonus, int herostr){
		if (bonus < 0) return 0;
		int STR = herostr-8;
		return Math.max(STR/2+bonus, 0);
	}

	public static int max(int bonus, int herostr){
		if (bonus < 0) return 0;
		int STR = herostr-8;
		return Math.max((int)(STR*0.5f*bonus) + STR*2, bonus);
	}

	@Override
	public String desc() {
		String desc = super.desc();
		int str = Dungeon.hero.STR();
		if (levelKnown) {
			desc += "\n\n" + Messages.get(this, "avg_dmg", (min(level(), str) + max(level(), str))/2);
		} else {
			desc += "\n\n" + Messages.get(this, "typical_avg_dmg", (min(1, str) + max(1, str))/2);
		}

		return desc;
	}

	public class Force extends RingBuff {
	}
}

