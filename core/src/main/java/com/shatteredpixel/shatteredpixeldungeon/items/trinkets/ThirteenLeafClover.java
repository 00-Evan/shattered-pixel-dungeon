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
import com.watabou.utils.Random;

public class ThirteenLeafClover extends Trinket {

	{
		image = ItemSpriteSheet.CLOVER;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 8(14) -> 10(24) -> 12(36)
		return 6+2*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc", Math.round(MAX_CHANCE * 100*alterHeroDamageChance(buffedLvl())), Math.round((1f-MAX_CHANCE) * 100*alterHeroDamageChance(buffedLvl())));
		} else {
			return Messages.get(this, "typical_stats_desc", Math.round(MAX_CHANCE * 100*alterHeroDamageChance(0)), Math.round((1f-MAX_CHANCE) * 100*alterHeroDamageChance(0)));
		}
	}

	public static float alterHeroDamageChance(){
		return alterHeroDamageChance(trinketLevel(ThirteenLeafClover.class));
	}

	public static float alterHeroDamageChance(int level ){
		if (level <= -1){
			return 0;
		} else {
			return 0.25f + 0.25f*level;
		}
	}

	private static float MAX_CHANCE = 0.6f;

	public static int alterDamageRoll(int min, int max){
		if (Random.Float() < MAX_CHANCE){
			return max;
		} else {
			return min;
		}
	}

}
