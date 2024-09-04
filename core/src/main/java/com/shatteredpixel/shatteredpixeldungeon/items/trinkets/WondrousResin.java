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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class WondrousResin extends Trinket {

	{
		image = ItemSpriteSheet.WONDROUS_RESIN;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 10(16) -> 15(31) -> 20(51)
		return 10+5*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc",
					Messages.decimalFormat("#.##", 100*positiveCurseEffectChance(buffedLvl())),
					Messages.decimalFormat("#.##", 100*extraCurseEffectChance(buffedLvl())));
		} else {
			return Messages.get(this, "typical_stats_desc",
					Messages.decimalFormat("#.##", 100*positiveCurseEffectChance(0)),
					Messages.decimalFormat("#.##", 100*extraCurseEffectChance(0)));
		}
	}

	//used when bonus curse effects are being created
	public static boolean forcePositive = false;

	public static float positiveCurseEffectChance(){
		if (forcePositive){
			return 1;
		}
		return positiveCurseEffectChance( trinketLevel(WondrousResin.class) );
	}

	public static float positiveCurseEffectChance(int level ){
		if (level >= 0){
			return 0.25f + 0.25f * level;
		} else {
			return 0;
		}
	}

	public static float extraCurseEffectChance(){
		return extraCurseEffectChance( trinketLevel(WondrousResin.class) );
	}

	public static float extraCurseEffectChance( int level ){
		if (level >= 0){
			return 0.125f + 0.125f * level;
		} else {
			return 0;
		}
	}

}
