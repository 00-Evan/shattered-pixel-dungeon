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
package com.shatteredpixel.shatteredpixeldungeon.messages;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/*
	Simple wrapper class for java resource bundles.

	The core idea here is that each string resource's key is a combination of the class definition and a local value.
	An object or static method would usually call this with an object/class reference (usually its own) and a local key.
	This means that an object can just ask for "name" rather than, say, "items.weapon.enchantments.death.name"
 */
public class Messages {

	private static String[] prop_files = new String[]{
			"com.shatteredpixel.shatteredpixeldungeon.messages.actors.actors",
			"com.shatteredpixel.shatteredpixeldungeon.messages.items.items",
			"com.shatteredpixel.shatteredpixeldungeon.messages.levels.levels",
			"com.shatteredpixel.shatteredpixeldungeon.messages.plants.plants",
			"com.shatteredpixel.shatteredpixeldungeon.messages.scenes.scenes",
			"com.shatteredpixel.shatteredpixeldungeon.messages.ui.ui",
			"com.shatteredpixel.shatteredpixeldungeon.messages.windows.windows",
			"com.shatteredpixel.shatteredpixeldungeon.messages.misc.misc"
	};

	/*
		use hashmap for two reasons. Firstly because android 2.2 doesn't support resourcebundle.containskey(),
		secondly so I can read in and combine multiple properties files,
		resulting in a more clean structure for organizing all the strings, instead of one big file.

		..Yes R.string would do this for me, but that's not multiplatform
	 */
	private static HashMap<String, String> strings;

	static{
		setup(Locale.getDefault().getLanguage());
	}

	public static void setup( String region ){
		strings = new HashMap<>();
		Locale locale = new Locale(region); //TODO:load in locale from a preference, use default only if none set.

		for (String file : prop_files) {
			ResourceBundle bundle = ResourceBundle.getBundle( file, locale);
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				strings.put(key, bundle.getString(key));
			}
		}
	}

	public static String get(String key, Object...args){
		return get(null, key, args);
	}

	//stuffing static variables with results from this means the app needs to restart for locale changes to take effect.
	//so be careful with where you're calling this, never assign its result to a static value (including enum variables)
	public static String get(Object o, String k, Object...args){
		return get(o.getClass(), k, args);
	}

	public static String get(Class c, String k, Object...args){
		String key;
		if (c != null){
			key = c.getName().replace("com.shatteredpixel.shatteredpixeldungeon.", "");
			key += "." + k;
		} else
			key = k;

		if (!strings.containsKey(key.toLowerCase())){
			//this is so child classes can inherit properties from their parents.
			//in cases where text is commonly grabbed as a utility from classes that aren't mean to be instantiated
			//(e.g. flavourbuff.dispTurns()) using .class directly is probably smarter to prevent unnecessary recursive calls.
			if (c != null && c.getSuperclass() != null){
				return get(c.getSuperclass(), k, args);
			} else {
				return "!!!NO TEXT FOUND!!!";
			}
		} else {
			if (args.length > 0) return String.format(Locale.ENGLISH, strings.get(key.toLowerCase()), args);
			else return strings.get(key.toLowerCase());
		}
	}
}

