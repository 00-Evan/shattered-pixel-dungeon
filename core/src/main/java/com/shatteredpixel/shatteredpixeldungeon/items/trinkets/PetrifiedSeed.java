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
		return 1 + level(); //TODO
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", (int)(100*stoneInsteadOfSeedChance(buffedLvl())), (int)Math.round(100*(grassLootMultiplier(buffedLvl())-1f)));
	}

	public static float grassLootMultiplier(){
		return grassLootMultiplier(trinketLevel(PetrifiedSeed.class));
	}

	public static float grassLootMultiplier( int level ){
		return 1f + .4f*level/3f;
	}

	public static float stoneInsteadOfSeedChance(){
		return stoneInsteadOfSeedChance(trinketLevel(PetrifiedSeed.class));
	}

	public static float stoneInsteadOfSeedChance( int level ){
		if (level == -1){
			return 0f;
		} else {
			return 0.35f + .05f*level;
		}
	}
}
