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

public class PetrifiedSeed extends Trinket {

	{
		image = ItemSpriteSheet.PETRIFIED_SEED;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 8(14) -> 10(24) -> 12(36)
		return 6+2*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc",
					Messages.decimalFormat("#.##", 100*stoneInsteadOfSeedChance(buffedLvl())),
					Messages.decimalFormat("#.##", 100*(grassLootMultiplier(buffedLvl())-1f)));
		} else {
			return Messages.get(this, "typical_stats_desc",
					Messages.decimalFormat("#.##", 100*stoneInsteadOfSeedChance(0)),
					Messages.decimalFormat("#.##", 100*(grassLootMultiplier(0)-1f)));
		}
	}

	public static float grassLootMultiplier(){
		return grassLootMultiplier(trinketLevel(PetrifiedSeed.class));
	}

	public static float grassLootMultiplier( int level ){
		if (level <= 0){
			return 1f;
		} else {
			return 1f + .25f*level/3f;
		}
	}

	public static float stoneInsteadOfSeedChance(){
		return stoneInsteadOfSeedChance(trinketLevel(PetrifiedSeed.class));
	}

	//when accounting for boosts, we effectively get:
	//stones: 25/50/75/100%
	//seeds:  75/58/38/25%
	public static float stoneInsteadOfSeedChance( int level ){
		switch (level){
			default:
				return 0;
			case 0:
				return 0.25f;
			case 1:
				return 0.46f;
			case 2:
				return 0.65f;
			case 3:
				return 0.8f;
		}
	}
}
