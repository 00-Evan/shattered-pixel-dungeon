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
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class QuickSlot {

	/**
	 * Slots contain objects which are also in a player's inventory. The one exception to this is when quantity is 0,
	 * which can happen for a stackable item that has been 'used up', these are refered to a placeholders.
	 */

	//note that the current max size is coded at 4, due to UI constraints, but it could be much much bigger with no issue.
	public static int SIZE = 4;
	private Item[] slots = new Item[SIZE];


	//direct array interaction methods, everything should build from these methods.
	public void setSlot(int slot, Item item){
		clearItem(item); //we don't want to allow the same item in multiple slots.
		slots[slot] = item;
	}

	public void clearSlot(int slot){
		slots[slot] = null;
	}

	public void reset(){
		slots = new Item[SIZE];
	}

	public Item getItem(int slot){
		return slots[slot];
	}


	//utility methods, for easier use of the internal array.
	public int getSlot(Item item) {
		for (int i = 0; i < SIZE; i++)
			if (getItem(i) == item)
				return i;
		return -1;
	}

	public Boolean isPlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() == 0;
	}

	public Boolean isNonePlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() > 0;
	}

	public void clearItem(Item item){
		if (contains(item))
			clearSlot(getSlot(item));
	}

	public boolean contains(Item item){
		return getSlot(item) != -1;
	}

	public void replaceSimilar(Item item){
		for (int i = 0; i < SIZE; i++)
			if (getItem(i) != null && item.isSimilar(getItem(i)))
				setSlot( i , item );
	}

	public void convertToPlaceholder(Item item){
		Item placeholder = Item.virtual(item.getClass());

		if (placeholder != null && contains(item))
			for (int i = 0; i < SIZE; i++)
				if (getItem(i) == item)
					setSlot( i , placeholder );
	}

	public Item randomNonePlaceholder(){

		ArrayList<Item> result = new ArrayList<Item>();
		for (int i = 0; i < SIZE; i ++)
		if (getItem(i) != null && !isPlaceholder(i))
				result.add(getItem(i));

		return Random.element(result);
	}

	private final String PLACEHOLDERS = "placeholders";
	private final String PLACEMENTS = "placements";

	/**
	 * Placements array is used as order is preserved while bundling, but exact index is not, so if we
	 * bundle both the placeholders (which preserves their order) and an array telling us where the placeholders are,
	 * we can reconstruct them perfectly.
	 */

	public void storePlaceholders(Bundle bundle){
		ArrayList<Item> placeholders = new ArrayList<Item>(SIZE);
		boolean[] placements = new boolean[SIZE];

		for (int i = 0; i < SIZE; i++)
			if (isPlaceholder(i)) {
				placeholders.add(getItem(i));
				placements[i] = true;
			}
		bundle.put( PLACEHOLDERS, placeholders );
		bundle.put( PLACEMENTS, placements );
	}

	public void restorePlaceholders(Bundle bundle){
		Collection<Bundlable> placeholders = bundle.getCollection(PLACEHOLDERS);
		boolean[] placements = bundle.getBooleanArray( PLACEMENTS );

		int i = 0;
		for (Bundlable item : placeholders){
			while (!placements[i]) i++;
			setSlot( i, (Item)item );
			i++;
		}

	}

}
