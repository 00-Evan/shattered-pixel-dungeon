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
package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

import java.util.Iterator;

public class AlchemyPot {

	private static final String TXT_SELECT_SEED	= "Select a seed to throw";
	private static final String TXT_POT	        = "Alchemy Pot";
	private static final String TXT_FRUIT	    = "Cook a Blandfruit";
	private static final String TXT_POTION	    = "Brew a Potion";
	private static final String TXT_OPTIONS	    =
			"Do you want to cook a Blandfruit with a seed, or brew a Potion from seeds?";
	
	public static Hero hero;
	public static int pos;

	public static boolean foundFruit;
	public static Item curItem = null;
	
	public static void operate( Hero hero, int pos ) {
		
		AlchemyPot.hero = hero;
		AlchemyPot.pos = pos;

		Iterator<Item> items = hero.belongings.iterator();
		foundFruit = false;
		Heap heap = Dungeon.level.heaps.get( pos );

		if (heap == null)
			while (items.hasNext() && !foundFruit){
				curItem = items.next();
				if (curItem instanceof Blandfruit && ((Blandfruit) curItem).potionAttrib == null){
						GameScene.show(
								new WndOptions(TXT_POT, TXT_OPTIONS, TXT_FRUIT, TXT_POTION) {
									@Override
									protected void onSelect(int index) {
										if (index == 0) {
											curItem.cast( AlchemyPot.hero, AlchemyPot.pos );
										} else
											GameScene.selectItem(itemSelector, WndBag.Mode.SEED, TXT_SELECT_SEED);
									}
								}
					);
					foundFruit = true;
				}
			}

		if (!foundFruit)
			GameScene.selectItem(itemSelector, WndBag.Mode.SEED, TXT_SELECT_SEED);
	}
	
	private static final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				item.cast( hero, pos );
			}
		}
	};
}
