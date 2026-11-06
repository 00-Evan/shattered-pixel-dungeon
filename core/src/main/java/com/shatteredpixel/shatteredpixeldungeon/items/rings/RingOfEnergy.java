/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

public class RingOfEnergy extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_ENERGY;
		buffClass = Energy.class;
	}

	//17.5% more recharge speed per level
	private static final float CHRG_FACTOR = 1.175f;

	public static float rechargeSpeedMultiplier( int bonus ){
		return (float)Math.pow(CHRG_FACTOR, bonus);
	}

	public static float rechargeSpeedMultiplier( Char target ){
		return rechargeSpeedMultiplier(getBuffedBonus(target, Energy.class));
	}

	@Override
	protected RingBuff buff( ) {
		return new Energy();
	}

	public class Energy extends RingBuff {}

	@Override
	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					Messages.decimalFormat("#.##", 100f * (rechargeSpeedMultiplier(soloBuffedBonus()) - 1f)));

			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						Messages.decimalFormat("#.##", 100f * (rechargeSpeedMultiplier(combinedBuffedBonus(Dungeon.hero)) - 1f)));
			}

			return info;
		} else {
			return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 100*(rechargeSpeedMultiplier(1)-1f)));
		}
	}

	@Override
	public String upgradeStat1(int level){
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Messages.decimalFormat("#.##", 100f * (rechargeSpeedMultiplier(level+1)-1f)) + "%";
	}
}
