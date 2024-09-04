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
		//6 -> 5(11) -> 7(18) -> 8(26)
		return Math.round(5+1.67f*level());
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc", (int)(100*combatDistributionInverseChance(buffedLvl())));
		} else {
			return Messages.get(this, "typical_stats_desc", (int)(100*combatDistributionInverseChance(0)));
		}
	}

	public static float combatDistributionInverseChance(){
		return combatDistributionInverseChance(trinketLevel(ThirteenLeafClover.class));
	}

	public static float combatDistributionInverseChance( int level ){
		if (level <= -1){
			return 0;
		} else {
			return 0.25f + 0.25f*level;
		}
	}

	public static int invCombatRoll( int min, int max){
		return Random.InvNormalIntRange( min, max );
	}

}
