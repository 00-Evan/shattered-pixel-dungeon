/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfFuror extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_FUROR;
	}

	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					Messages.decimalFormat("#.##", 100f * (Math.pow(1.09051f, soloBuffedBonus()) - 1f)));
			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero, Furor.class)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						Messages.decimalFormat("#.##", 100f * (Math.pow(1.09051f, combinedBuffedBonus(Dungeon.hero, Furor.class)) - 1f)));
			}
			return info;
		} else {
			return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 9.051f));
		}
	}

	@Override
	protected RingBuff buff( ) {
		return new Furor();
	}
	
	public static float attackSpeedMultiplier(Char target ){
		return (float)Math.pow(1.09051, getBuffedBonus(target, Furor.class));
	}

	public class Furor extends RingBuff {
	}
}
