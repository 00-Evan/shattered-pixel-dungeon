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

public class EyeOfNewt extends Trinket {

	{
		image = ItemSpriteSheet.EYE_OF_NEWT;
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
					Messages.decimalFormat("#.##", 100*(1f-visionRangeMultiplier(buffedLvl()))),
					mindVisionRange(buffedLvl()));
		} else {
			return Messages.get(this, "typical_stats_desc",
					Messages.decimalFormat("#.##", 100*(1f-visionRangeMultiplier(0))),
					mindVisionRange(0));
		}
	}

	public static float visionRangeMultiplier(){
		return visionRangeMultiplier(trinketLevel(EyeOfNewt.class));
	}

	public static float visionRangeMultiplier( int level ){
		if (level < 0){
			return 1;
		} else {
			return 0.875f - 0.125f*level;
		}
	}

	public static int mindVisionRange(){
		return mindVisionRange(trinketLevel(EyeOfNewt.class));
	}

	public static int mindVisionRange( int level ){
		if (level < 0){
			return 0;
		} else {
			return 2+level;
		}
	}

}
