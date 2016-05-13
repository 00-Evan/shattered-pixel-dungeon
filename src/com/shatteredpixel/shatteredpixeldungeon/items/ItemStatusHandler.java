/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items;

import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ItemStatusHandler<T extends Item> {

	private Class<? extends T>[] items;
	private HashMap<Class<? extends T>, String> itemLabels;
	private HashMap<String, Integer> labelImages;
	private HashSet<Class<? extends T>> known;

	public ItemStatusHandler( Class<? extends T>[] items, HashMap<String, Integer> labelImages ) {

		this.items = items;

		this.itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(labelImages);
		known = new HashSet<Class<? extends T>>();

		ArrayList<String> labelsLeft = new ArrayList<String>( labelImages.keySet() );

		for (int i=0; i < items.length; i++) {

			Class<? extends T> item = items[i];

			int index = Random.Int( labelsLeft.size() );

			itemLabels.put( item, labelsLeft.get( index ) );
			labelsLeft.remove( index );

		}
	}

	public ItemStatusHandler( Class<? extends T>[] items, HashMap<String, Integer> labelImages, Bundle bundle ) {

		this.items = items;

		this.itemLabels = new HashMap<>();
		this.labelImages = new HashMap<>(labelImages);
		known = new HashSet<>();

		ArrayList<String> allLabels = new ArrayList<String>( labelImages.keySet() );

		restore(bundle, allLabels);
	}

	private static final String PFX_LABEL	= "_label";
	private static final String PFX_KNOWN	= "_known";
	
	public void save( Bundle bundle ) {
		for (int i=0; i < items.length; i++) {
			String itemName = items[i].toString();
			bundle.put( itemName + PFX_LABEL, itemLabels.get( items[i] ) );
			bundle.put( itemName + PFX_KNOWN, known.contains( items[i] ) );
		}
	}

	private void restore( Bundle bundle, ArrayList<String> labelsLeft  ) {

		for (int i=0; i < items.length; i++) {

			Class<? extends T> item = items[i];
			String itemName = item.toString();

			if (bundle.contains( itemName + PFX_LABEL )) {

				String label = bundle.getString( itemName + PFX_LABEL );
				itemLabels.put( item, label );
				labelsLeft.remove( label );

				if (bundle.getBoolean( itemName + PFX_KNOWN )) {
					known.add( item );
				}

			} else {

				int index = Random.Int( labelsLeft.size() );

				itemLabels.put( item, labelsLeft.get( index ) );
				labelsLeft.remove( index );

				if (bundle.contains( itemName + PFX_KNOWN ) && bundle.getBoolean( itemName + PFX_KNOWN )) {
					known.add( item );
				}
			}
		}
	}
	
	public int image( T item ) {
		return labelImages.get(label(item));
	}
	
	public String label( T item ) {
		return itemLabels.get(item.getClass());
	}
	
	public boolean isKnown( T item ) {
		return known.contains( item.getClass() );
	}
	
	@SuppressWarnings("unchecked")
	public void know( T item ) {
		known.add( (Class<? extends T>)item.getClass() );
		
		if (known.size() == items.length - 1) {
			for (int i=0; i < items.length; i++) {
				if (!known.contains( items[i] )) {
					known.add( items[i] );
					break;
				}
			}
		}
	}
	
	public HashSet<Class<? extends T>> known() {
		return known;
	}
	
	public HashSet<Class<? extends T>> unknown() {
		HashSet<Class<? extends T>> result = new HashSet<Class<? extends T>>();
		for (Class<? extends T> i : items) {
			if (!known.contains( i )) {
				result.add( i );
			}
		}
		return result;
	}
}
