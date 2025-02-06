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

public class SaltCube extends Trinket {

	{
		image = ItemSpriteSheet.SALT_CUBE;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 8(14) -> 10(24) -> 12(36)
		return 6+2*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this,
					"stats_desc",
					Messages.decimalFormat("#.##", 100*((1f/hungerGainMultiplier(buffedLvl()))-1f)),
					Messages.decimalFormat("#.##", 100*(1f-healthRegenMultiplier(buffedLvl()))));
		} else {
			return Messages.get(this,
					"typical_stats_desc",
					Messages.decimalFormat("#.##", 100*((1f/hungerGainMultiplier(buffedLvl()))-1f)),
					Messages.decimalFormat("#.##", 100*(1f-healthRegenMultiplier(buffedLvl()))));
		}
	}

	public static float hungerGainMultiplier(){
		return hungerGainMultiplier(trinketLevel(SaltCube.class));
	}

	public static float hungerGainMultiplier( int level ){
		if (level == -1){
			return 1;
		} else {
			return 1f / (1f + 0.25f*(level+1));
		}
	}

	public static float healthRegenMultiplier(){
		return healthRegenMultiplier(trinketLevel(SaltCube.class));
	}

	public static float healthRegenMultiplier( int level ){
		switch (level){
			case -1: default:
				return 1;
			case 0:
				return 0.84f;
			case 1:
				return 0.73f;
			case 2:
				return 0.66f;
			case 3:
				return 0.6f;
		}
	}

}
