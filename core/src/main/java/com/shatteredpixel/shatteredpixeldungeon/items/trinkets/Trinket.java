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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public abstract class Trinket extends Item {

	{
		levelKnown = true;

		unique = true;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	protected abstract int upgradeEnergyCost();

	protected static int trinketLevel(Class<? extends Trinket> trinketType ){
		if (Dungeon.hero == null || Dungeon.hero.belongings == null){
			return -1;
		}

		Trinket trinket = Dungeon.hero.belongings.getItem(trinketType);

		if (trinket != null){
			return trinket.buffedLvl();
		} else {
			return -1;
		}
	}

	public static class PlaceHolder extends Trinket {

		{
			image = ItemSpriteSheet.TRINKET_HOLDER;
		}

		@Override
		protected int upgradeEnergyCost() {
			return 0;
		}

		@Override
		public boolean isSimilar(Item item) {
			return item instanceof Trinket;
		}

		@Override
		public String info() {
				return "";
			}

	}

	public static class UpgradeTrinket extends Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.size() == 1 && ingredients.get(0) instanceof Trinket && ingredients.get(0).level() < 3;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return ((Trinket)ingredients.get(0)).upgradeEnergyCost();
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			Item result = ingredients.get(0).duplicate();
			ingredients.get(0).quantity(0);
			result.upgrade();
			return result;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return ingredients.get(0).duplicate().upgrade();
		}
	}
}
