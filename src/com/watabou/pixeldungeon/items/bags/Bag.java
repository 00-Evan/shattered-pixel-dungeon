/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.items.bags;

import java.util.ArrayList;
import java.util.Iterator;

import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.windows.WndBag;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class Bag extends Item implements Iterable<Item> {

	public static final String AC_OPEN	= "OPEN";
	
	{
		image = 11;
		
		defaultAction = AC_OPEN;
	}
	
	public Char owner;
	
	public ArrayList<Item> items = new ArrayList<Item>();	
	
	public int size = 1;
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_OPEN )) {
			
			GameScene.show( new WndBag( this, null, WndBag.Mode.ALL, null ) );
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {	
			
			owner = container.owner;
			
			for (Item item : container.items.toArray( new Item[0] )) {
				if (grab( item )) {
					item.detachAll( container );
					item.collect( this );
				}
			}
			
			Badges.validateAllBagsBought( this );
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Item detach( Bag container ) {
		owner = null;
		return super.detach( container );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	public void clear() {
		items.clear();
	}
	
	private static final String ITEMS	= "inventory";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ITEMS, items );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		for (Bundlable item : bundle.getCollection( ITEMS )) {
			((Item)item).collect( this );
		};
	}
	
	public boolean contains( Item item ) {
		for (Item i : items) {
			if (i == item) {
				return true;
			} else if (i instanceof Bag && ((Bag)i).contains( item )) {
				return true;
			}
		}
		return false;
	}
	
	public boolean grab( Item item ) {
		return false;
	}

	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		private Iterator<Item> nested = null;
		
		@Override
		public boolean hasNext() {
			if (nested != null) {
				return nested.hasNext() || index < items.size();
			} else {
				return index < items.size();
			}
		}

		@Override
		public Item next() {
			if (nested != null && nested.hasNext()) {
				
				return nested.next();
				
			} else {
				
				nested = null;
				
				Item item = items.get( index++ );
				if (item instanceof Bag) {
					nested = ((Bag)item).iterator();
				}
				
				return item;
			}
		}

		@Override
		public void remove() {
			if (nested != null) {
				nested.remove();
			} else {
				items.remove( index );
			}
		}	
	}
}
