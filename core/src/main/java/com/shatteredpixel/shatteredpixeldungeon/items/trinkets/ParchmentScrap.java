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

public class ParchmentScrap extends Trinket {

	{
		image = ItemSpriteSheet.PARCHMENT_SCRAP;
	}

	@Override
	protected int upgradeEnergyCost() {
		//5 -> 10(15) -> 15(30) -> 20(50)
		return 10+5*level();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", (int)enchantChanceMultiplier(buffedLvl()), Messages.decimalFormat("#.##", curseChanceMultiplier(buffedLvl())));
	}

	public static float enchantChanceMultiplier(){
		return enchantChanceMultiplier(trinketLevel(ParchmentScrap.class));
	}

	public static float enchantChanceMultiplier( int level ){
		switch (level){
			default:
				return 1;
			case 0:
				return 2;
			case 1:
				return 4;
			case 2:
				return 7;
			case 3:
				return 10;
		}
	}

	public static float curseChanceMultiplier(){
		return curseChanceMultiplier(trinketLevel(ParchmentScrap.class));
	}

	public static float curseChanceMultiplier( int level ){
		switch (level){
			default:
				return 1;
			case 0:
				return 1.5f;
			case 1:
				return 2f;
			case 2:
				return 1f;
			case 3:
				return 0f;
		}
	}
}
