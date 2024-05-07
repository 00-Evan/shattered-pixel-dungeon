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
		//5 -> 10(15) -> 15(30) -> 20(50)
		return 10+5*level();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc",
				Messages.decimalFormat("#.##", 100*positiveCurseEffectChance(buffedLvl())),
				Messages.decimalFormat("#.##", 100*extraCurseEffectChance(buffedLvl())));
	}

	//TODO currently this trims most rare/v.rare wand effects entirely. Need to improve variety there
	// certain effects might also be extremely good with no negatives

	public static float positiveCurseEffectChance(){
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
