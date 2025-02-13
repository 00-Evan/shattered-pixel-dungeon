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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class AquaBrew extends Brew {

	{
		image = ItemSpriteSheet.BREW_AQUA;

		talentChance = 1/(float)Recipe.OUT_QUANTITY;
	}

	@Override
	public void shatter(int cell) {
		GeyserTrap geyser = new GeyserTrap();
		geyser.pos = cell;
		geyser.source = this;

		int userPos = curUser == null ? cell : curUser.pos;
		if (userPos != cell){
			Ballistica aim = new Ballistica(userPos, cell, Ballistica.STOP_TARGET);
			if (aim.path.size() > aim.dist+1) {
				geyser.centerKnockBackDirection = aim.path.get(aim.dist + 1);
			}
		}
		geyser.activate();
	}

	@Override
	public int value() {
		return (int)(60 * (quantity/(float)Recipe.OUT_QUANTITY));
	}

	@Override
	public int energyVal() {
		return (int)(12 * (quantity/(float)Recipe.OUT_QUANTITY));
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		private static final int OUT_QUANTITY = 8;

		{
			inputs =  new Class[]{PotionOfStormClouds.class};
			inQuantity = new int[]{1};

			cost = 8;

			output = AquaBrew.class;
			outQuantity = OUT_QUANTITY;
		}

	}

}
