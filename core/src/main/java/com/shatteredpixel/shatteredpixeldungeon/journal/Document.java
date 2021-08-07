/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.Collection;
import java.util.LinkedHashMap;

public enum Document {
	
	ADVENTURERS_GUIDE(ItemSpriteSheet.GUIDE_PAGE),
	ALCHEMY_GUIDE(ItemSpriteSheet.ALCH_PAGE);
	
	Document( int sprite ){
		pageSprite = sprite;
	}

	private static final int NOT_FOUND = 0;
	private static final int FOUND = 1;
	private static final int READ = 2;
	private LinkedHashMap<String, Integer> pagesStates = new LinkedHashMap<>();
	
	public boolean findPage(String page ) {
		if (pagesStates.containsKey(page) && pagesStates.get(page) == NOT_FOUND){
			pagesStates.put(page, FOUND);
			Journal.saveNeeded = true;
			return true;
		}
		return false;
	}

	public boolean findPage( int pageIdx ) {
		return findPage( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean pageFound( String page ){
		return pagesStates.containsKey(page) && pagesStates.get(page) > NOT_FOUND;
	}

	public boolean pageFound( int pageIdx ){
		return pageFound( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean readPage( String page ) {
		if (pagesStates.containsKey(page) && pagesStates.get(page) == FOUND){
			pagesStates.put(page, READ);
			Journal.saveNeeded = true;
			return true;
		}
		return false;
	}

	public boolean readPage( int pageIdx ) {
		return readPage( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean pageRead( String page ){
		return pagesStates.containsKey(page) && pagesStates.get(page) == READ;
	}

	public boolean pageRead( int pageIdx ){
		return pageRead( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public Collection<String> pageNames(){
		return pagesStates.keySet();
	}

	public int pageIdx(String name){
		int i = 0;
		for( String page : pagesStates.keySet()){
			if (page.equals(name)){
				return i;
			}
			i++;
		}
		return -1;
	}

	private int pageSprite;
	public int pageSprite(){
		return pageSprite;
	}
	
	public String title(){
		return Messages.get( this, name() + ".title");
	}
	
	public String pageTitle( String page ){
		return Messages.get( this, name() + "." + page + ".title");
	}
	
	public String pageTitle( int pageIdx ){
		return pageTitle( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}
	
	public String pageBody( String page ){
		return Messages.get( this, name() + "." + page + ".body");
	}
	
	public String pageBody( int pageIdx ){
		return pageBody( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public static final String GUIDE_INTRO_PAGE = "Intro";
	public static final String GUIDE_SEARCH_PAGE = "Examining_and_Searching";
	
	static {
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_INTRO_PAGE, 	DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Identifying", 		DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_SEARCH_PAGE, 	DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Strength", 		    DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Food", 			    DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Levelling", 		    DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Surprise_Attacks",   DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Dieing", 			DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Looting", 		    DeviceCompat.isDebug() ? 2 : 0);
		ADVENTURERS_GUIDE.pagesStates.put("Magic", 			    DeviceCompat.isDebug() ? 2 : 0);
		
		//sewers
		ALCHEMY_GUIDE.pagesStates.put("Potions",              DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Stones",               DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Energy_Food",          DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Bombs",                DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Weapons",              DeviceCompat.isDebug() ? 2 : 0);
		
		//prison
		ALCHEMY_GUIDE.pagesStates.put("Exotic_Potions",       DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Exotic_Scrolls",       DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Catalysts",            DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Brews_Elixirs",        DeviceCompat.isDebug() ? 2 : 0);
		ALCHEMY_GUIDE.pagesStates.put("Spells",               DeviceCompat.isDebug() ? 2 : 0);
	}
	
	private static final String DOCUMENTS = "documents";
	
	public static void store( Bundle bundle ){
		
		Bundle docsBundle = new Bundle();
		
		for ( Document doc : values()){
			Bundle pagesBundle = new Bundle();
			boolean empty = true;
			for (String page : doc.pageNames()){
				if (doc.pagesStates.get(page) != NOT_FOUND){
					pagesBundle.put(page, doc.pagesStates.get(page));
					empty = false;
				}
			}
			if (!empty){
				docsBundle.put(doc.name(), pagesBundle);
			}
		}
		
		bundle.put( DOCUMENTS, docsBundle );
		
	}
	
	public static void restore( Bundle bundle ){
		
		if (!bundle.contains( DOCUMENTS )){
			return;
		}
		
		Bundle docsBundle = bundle.getBundle( DOCUMENTS );
		
		for ( Document doc : values()){
			if (docsBundle.contains(doc.name())){
				Bundle pagesBundle = docsBundle.getBundle(doc.name());

				//compatibility with pre-0.9.4 saves
				if (pagesBundle.isNull()) {
					for (String page : docsBundle.getStringArray(doc.name())){
						if (doc.pagesStates.containsKey(page)) {
							doc.pagesStates.put(page, READ);
						}
					}

				} else {
					for (String page : doc.pageNames()) {
						if (pagesBundle.contains(page)) {
							doc.pagesStates.put(page, pagesBundle.getInt(page));
						}
					}
				}
			}
		}
	}
	
}
