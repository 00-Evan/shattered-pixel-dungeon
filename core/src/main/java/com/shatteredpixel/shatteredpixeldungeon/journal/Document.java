/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.util.Collection;
import java.util.LinkedHashMap;

public enum Document {
	
	ADVENTURERS_GUIDE(ItemSpriteSheet.GUIDE_PAGE, false),
	ALCHEMY_GUIDE(ItemSpriteSheet.ALCH_PAGE, false),

	INTROS(Icons.STAIRS, true),
	SEWERS_GUARD(ItemSpriteSheet.SEWER_PAGE, true),
	PRISON_WARDEN(ItemSpriteSheet.PRISON_PAGE, true),
	CAVES_EXPLORER(ItemSpriteSheet.CAVES_PAGE, true),
	CITY_WARLOCK(ItemSpriteSheet.CITY_PAGE, true),
	HALLS_KING(ItemSpriteSheet.HALLS_PAGE, true);
	
	Document( int sprite, boolean lore ){
		pageIcon = null;
		pageSprite = sprite;
		loreDocument = lore;
	}

	Document( Icons icon, boolean lore ){
		pageIcon = icon;
		pageSprite = 0;
		loreDocument = lore;
	}

	public static final int NOT_FOUND = 0;
	public static final int FOUND = 1;
	public static final int READ = 2;
	private LinkedHashMap<String, Integer> pagesStates = new LinkedHashMap<>();
	
	public boolean findPage( String page ) {
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

	public boolean deletePage( String page ){
		if (pagesStates.containsKey(page) && pagesStates.get(page) != NOT_FOUND){
			pagesStates.put(page, NOT_FOUND);
			Journal.saveNeeded = true;
			return true;
		}
		return false;
	}

	public boolean deletePage( int pageIdx ) {
		return deletePage( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean isPageFound( String page ){
		return pagesStates.containsKey(page) && pagesStates.get(page) > NOT_FOUND;
	}

	public boolean isPageFound( int pageIdx ){
		return isPageFound( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean anyPagesFound(){
		for( Integer val : pagesStates.values()){
			if (val != NOT_FOUND){
				return true;
			}
		}
		return false;
	}

	public boolean allPagesFound(){
		for( Integer val : pagesStates.values()){
			if (val == NOT_FOUND){
				return false;
			}
		}
		return true;
	}

	public boolean readPage( String page ) {
		if (pagesStates.containsKey(page)){
			pagesStates.put(page, READ);
			Journal.saveNeeded = true;
			return true;
		}
		return false;
	}

	public boolean readPage( int pageIdx ) {
		return readPage( pagesStates.keySet().toArray(new String[0])[pageIdx] );
	}

	public boolean isPageRead( String page ){
		return pagesStates.containsKey(page) && pagesStates.get(page) == READ;
	}

	public boolean isPageRead( int pageIdx ){
		return isPageRead( pagesStates.keySet().toArray(new String[0])[pageIdx] );
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
	private Icons pageIcon;
	public Image pageSprite(){
		return pageSprite("");
	}

	public Image pageSprite(String page){
		if (page.isEmpty() || !isPageFound(page) || this != ADVENTURERS_GUIDE){
			if (pageIcon != null){
				return Icons.get(pageIcon);
			} else {
				return new ItemSprite(pageSprite);
			}
		} else {
			//special per-page visuals for guidebook
			switch (page){
				case Document.GUIDE_INTRO: default:
					return new ItemSprite(ItemSpriteSheet.MASTERY);
				case "Examining":
					return Icons.get(Icons.MAGNIFY);
				case "Surprise_Attacks":
					return new ItemSprite( ItemSpriteSheet.ASSASSINS_BLADE );
				case "Identifying":
					return new ItemSprite( new ScrollOfIdentify() );
				case "Food":
					return new ItemSprite( ItemSpriteSheet.PASTY );
				case "Dieing":
					return new ItemSprite( ItemSpriteSheet.TOMB );
				case Document.GUIDE_SEARCHING:
					return Icons.get(Icons.MAGNIFY);
				case "Strength":
					return new ItemSprite( ItemSpriteSheet.GREATAXE );
				case "Upgrades":
					return new ItemSprite( ItemSpriteSheet.RING_EMERALD );
				case "Looting":
					return new ItemSprite( ItemSpriteSheet.CRYSTAL_KEY );
				case "Levelling":
					return Icons.get(Icons.TALENT);
				case "Positioning":
					return new ItemSprite( ItemSpriteSheet.SPIRIT_BOW );
				case "Magic":
					return new ItemSprite( ItemSpriteSheet.WAND_FIREBOLT );
			}
		}
	}

	private boolean loreDocument;
	public boolean isLoreDoc(){
		return loreDocument;
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

	public static final String GUIDE_INTRO          = "Intro";
	public static final String GUIDE_EXAMINING      = "Examining";
	public static final String GUIDE_SURPRISE_ATKS  = "Surprise_Attacks";
	public static final String GUIDE_IDING          = "Identifying";
	public static final String GUIDE_FOOD           = "Food";
	public static final String GUIDE_DIEING         = "Dieing";

	public static final String GUIDE_SEARCHING      = "Searching";

	//pages and default states
	static {
		boolean debug = DeviceCompat.isDebug();
		//hero gets these when guidebook is collected
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_INTRO,          debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_EXAMINING,      debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_SURPRISE_ATKS,  debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_IDING,          debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_FOOD,           debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_DIEING,         debug ? READ : NOT_FOUND);
		//given in sewers
		ADVENTURERS_GUIDE.pagesStates.put(GUIDE_SEARCHING,      debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Strength",           debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Upgrades",           debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Looting",            debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Levelling",          debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Positioning",        debug ? READ : NOT_FOUND);
		ADVENTURERS_GUIDE.pagesStates.put("Magic",              debug ? READ : NOT_FOUND);
		
		//given in sewers
		ALCHEMY_GUIDE.pagesStates.put("Potions",                debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Stones",                 debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Energy_Food",            debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Exotic_Potions",         debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Exotic_Scrolls",         debug ? READ : NOT_FOUND);
		//given in prison
		ALCHEMY_GUIDE.pagesStates.put("Bombs",                  debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Weapons",                debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Catalysts",              debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Brews_Elixirs",          debug ? READ : NOT_FOUND);
		ALCHEMY_GUIDE.pagesStates.put("Spells",                 debug ? READ : NOT_FOUND);

		INTROS.pagesStates.put("Dungeon",                       READ);
		INTROS.pagesStates.put("Sewers",                        debug ? READ : NOT_FOUND);
		INTROS.pagesStates.put("Prison",                        debug ? READ : NOT_FOUND);
		INTROS.pagesStates.put("Caves",                         debug ? READ : NOT_FOUND);
		INTROS.pagesStates.put("City",                          debug ? READ : NOT_FOUND);
		INTROS.pagesStates.put("Halls",                         debug ? READ : NOT_FOUND);

		SEWERS_GUARD.pagesStates.put("new_position",            debug ? READ : NOT_FOUND);
		SEWERS_GUARD.pagesStates.put("dangerous",               debug ? READ : NOT_FOUND);
		SEWERS_GUARD.pagesStates.put("crabs",                   debug ? READ : NOT_FOUND);
		SEWERS_GUARD.pagesStates.put("guild",                   debug ? READ : NOT_FOUND);
		SEWERS_GUARD.pagesStates.put("lost",                    debug ? READ : NOT_FOUND);
		SEWERS_GUARD.pagesStates.put("not_worth",               debug ? READ : NOT_FOUND);

		PRISON_WARDEN.pagesStates.put("journal",                debug ? READ : NOT_FOUND);
		PRISON_WARDEN.pagesStates.put("recruits",               debug ? READ : NOT_FOUND);
		PRISON_WARDEN.pagesStates.put("mines",                  debug ? READ : NOT_FOUND);
		PRISON_WARDEN.pagesStates.put("rotberry",               debug ? READ : NOT_FOUND);
		PRISON_WARDEN.pagesStates.put("no_support",             debug ? READ : NOT_FOUND);
		PRISON_WARDEN.pagesStates.put("letter",                 debug ? READ : NOT_FOUND);

		CAVES_EXPLORER.pagesStates.put("expedition",            debug ? READ : NOT_FOUND);
		CAVES_EXPLORER.pagesStates.put("gold",                  debug ? READ : NOT_FOUND);
		CAVES_EXPLORER.pagesStates.put("troll",                 debug ? READ : NOT_FOUND);
		CAVES_EXPLORER.pagesStates.put("city",                  debug ? READ : NOT_FOUND);
		CAVES_EXPLORER.pagesStates.put("alive",                 debug ? READ : NOT_FOUND);
		CAVES_EXPLORER.pagesStates.put("report",                debug ? READ : NOT_FOUND);

		CITY_WARLOCK.pagesStates.put("old_king",                debug ? READ : NOT_FOUND);
		CITY_WARLOCK.pagesStates.put("resistance",              debug ? READ : NOT_FOUND);
		CITY_WARLOCK.pagesStates.put("failure",                 debug ? READ : NOT_FOUND);
		CITY_WARLOCK.pagesStates.put("more_powerful",           debug ? READ : NOT_FOUND);
		CITY_WARLOCK.pagesStates.put("new_power",               debug ? READ : NOT_FOUND);
		CITY_WARLOCK.pagesStates.put("seen_it",                 debug ? READ : NOT_FOUND);

		HALLS_KING.pagesStates.put("Rejection",                 debug ? READ : NOT_FOUND);
		HALLS_KING.pagesStates.put("amulet",                    debug ? READ : NOT_FOUND);
		HALLS_KING.pagesStates.put("ritual",                    debug ? READ : NOT_FOUND);
		HALLS_KING.pagesStates.put("new_king",                  debug ? READ : NOT_FOUND);
		HALLS_KING.pagesStates.put("thing",                     debug ? READ : NOT_FOUND);
		HALLS_KING.pagesStates.put("attrition",                 debug ? READ : NOT_FOUND);

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

				for (String page : doc.pageNames()) {
					if (pagesBundle.contains(page)) {
						doc.pagesStates.put(page, pagesBundle.getInt(page));
					}
				}
			}
		}
	}
	
}
