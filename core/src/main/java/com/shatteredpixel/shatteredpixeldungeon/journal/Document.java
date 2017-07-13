/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Document {

	ADVENTURERS_GUIDE;
	
	private LinkedHashMap<String, Boolean> pages = new LinkedHashMap<>();
	
	public Collection<String> pages(){
		return pages.keySet();
	}
	
	public boolean hasPage( String page ){
		return pages.containsKey(page) && pages.get(page);
	}
	
	public String title(){
		return Messages.get( this, name() + ".title");
	}
	
	public String pageTitle( String page ){
		return Messages.get( this, name() + "." + page + ".title");
	}
	
	public String pageBody( String page ){
		return Messages.get( this, name() + "." + page + ".body");
	}
	
	static {
		ADVENTURERS_GUIDE.pages.put("0", false);
		ADVENTURERS_GUIDE.pages.put("1", false);
		ADVENTURERS_GUIDE.pages.put("2", false);
		ADVENTURERS_GUIDE.pages.put("3", false);
		ADVENTURERS_GUIDE.pages.put("4", false);
		ADVENTURERS_GUIDE.pages.put("5", false);
		ADVENTURERS_GUIDE.pages.put("6", false);
		ADVENTURERS_GUIDE.pages.put("7", false);
		ADVENTURERS_GUIDE.pages.put("8", false);
		ADVENTURERS_GUIDE.pages.put("9", false);
		
	}
	
	private static final String DOCUMENTS = "documents";
	
	public static void store( Bundle bundle ){
		
		Bundle docBundle = new Bundle();
		
		for ( Document doc : values()){
			ArrayList<String> pages = new ArrayList<>();
			for (String page : doc.pages()){
				if (doc.pages.get(page)){
					pages.add(page);
				}
			}
			if (!pages.isEmpty()) {
				docBundle.put(doc.name(), pages.toArray(new String[0]));
			}
		}
		
		bundle.put( DOCUMENTS, docBundle );
		
	}
	
	public static void restore( Bundle bundle ){
		
		if (!bundle.contains( DOCUMENTS )){
			return;
		}
		
		Bundle docBundle = bundle.getBundle( DOCUMENTS );
		
		for ( Document doc : values()){
			if (docBundle.contains(doc.name())){
				List<String> pages = Arrays.asList(docBundle.getStringArray(doc.name()));
				for (String page : pages){
					if (doc.pages.containsKey(page)) {
						doc.pages.put(page, true);
					}
				}
			}
		}
	}
	
}
