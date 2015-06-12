/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class MasterThievesArmband extends Artifact {

	{
		name = "Master Thieves' Armband";
		image = ItemSpriteSheet.ARTIFACT_ARMBAND;

		level = 0;
		levelCap = 10;

		charge = 0;
	}

	private int exp = 0;

	@Override
	protected ArtifactBuff passiveBuff() {
		return new Thievery();
	}

	@Override
	public String desc() {
		String desc = "This purple velvet armband bears the mark of a master thief. This doesn't belong to you, but " +
				"you doubt it belonged to the person you took it from either.";

		if ( isEquipped (Dungeon.hero) )
			desc += "\n\nWith the armband around your wrist you feel more dexterous and cunning. Every piece of gold " +
					"you find makes you desire others property more. " +
					"You wonder if Pixel Mart accepts the five finger discount...";

		return desc;
	}

	public class Thievery extends ArtifactBuff{
		public void collect(int gold){
			charge += gold/2;
		}

		@Override
		public void detach() {
			charge *= 0.95;
			super.detach();
		}

		public boolean steal(int value){
			if (value <= charge){
				charge -= value;
				exp += value;
			} else {
				float chance = stealChance(value);
				if (Random.Float() > chance)
					return false;
				else {
					if (chance <= 1)
						charge = 0;
					else
						//removes the charge it took you to reach 100%
						charge -= charge/chance;
					exp += value;
				}
			}
			while(exp >= 600 && level < levelCap) {
				exp -= 600;
				upgrade();
			}
			return true;
		}

		public float stealChance(int value){
				//get lvl*100 gold or lvl*5% item value of free charge, whichever is less.
				int chargeBonus = Math.min(level*100, (value*level)/20);

				return (((float)charge + chargeBonus)/value);
		}
	}
}
