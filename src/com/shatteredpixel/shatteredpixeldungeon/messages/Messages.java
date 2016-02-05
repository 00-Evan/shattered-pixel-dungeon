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

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

/*
	Simple wrapper class for java resource bundles.

	The core idea here is that each string resource's key is a combination of the class definition and a local value.
	An object or static method would usually call this with an object/class reference (usually its own) and a local key.
	This means that an object can just ask for "name" rather than, say, "items.weapon.enchantments.death.name"
 */
public class Messages {

	public enum Languages {
		ENGLISH("english",      "", Status.REVIEWED),
		RUSSIAN("русский",      "ru", Status.REVIEWED),
		KOREAN("한국어",         "ko", Status.REVIEWED),

		CHINESE("中文",          "zh", Status.UNREVIEWED), //Simplified
		PORTUGUESE("português", "pt", Status.UNREVIEWED), //Brazillian

		GERMAN("deutsch",       "de", Status.INCOMPLETE),
		POLISH("polski",        "pl", Status.INCOMPLETE),
		SPANISH("español",      "es", Status.INCOMPLETE),
		FRENCH("français",      "fr", Status.INCOMPLETE);

		private String name;
		private String code;
		private Status status;

		Languages(String name, String code, Status status){
			this.name = name;
			this.code = code;
			this.status = status;
		}

		public String nativeName(){
			return name;
		}

		public String code(){
			return code;
		}

		public Status status(){
			return status;
		}

		public static Languages matchLocale(Locale locale){
			return matchCode(locale.getLanguage());
		}

		public static Languages matchCode(String code){
			for (Languages lang : Languages.values()){
				if (lang.code().equals(code))
					return lang;
			}
			return ENGLISH;
		}

		public enum Status{
			//below 60% complete languages are not added.
			INCOMPLETE, //60-99% complete
			UNREVIEWED, //100% complete
			REVIEWED    //100% reviewed
		}

	}


	/*
		use hashmap for two reasons. Firstly because android 2.2 doesn't support resourcebundle.containskey(),
		secondly so I can read in and combine multiple properties files,
		resulting in a more clean structure for organizing all the strings, instead of one big file.

		..Yes R.string would do this for me, but that's not multiplatform
	 */
	private static HashMap<String, String> strings;
	private static Languages lang;

	public static Languages lang(){
		return lang;
	}



	/**
	 * Setup Methods
	 */

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

	static{
		setup(ShatteredPixelDungeon.language());
	}

	public static void setup( Languages lang ){
		strings = new HashMap<>();
		Messages.lang = lang;
		Locale locale = new Locale(lang.code());

		for (String file : prop_files) {
			ResourceBundle bundle = ResourceBundle.getBundle( file, locale);
			Enumeration<String> keys = bundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = bundle.getString(key);

				//android 2.2 doesn't use UTF-8 by default, need to force it.
				if (android.os.Build.VERSION.SDK_INT == 8) {
					try {
						value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
					} catch (Exception e) {}
				}

				strings.put(key, value);
			}
		}
	}



	/**
	 * Resource grabbing methods
	 */

	public static String get(String key, Object...args){
		return get(null, key, args);
	}

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
			if (args.length > 0) return format(strings.get(key.toLowerCase()), args);
			else return strings.get(key.toLowerCase());
		}
	}



	/**
	 * String Utility Methods
	 */

	public static String format( String format, Object...args ) {
		return String.format( Locale.ENGLISH, format, args );
	}

	public static String capitalize( String str ){
		return Character.toTitleCase( str.charAt( 0 ) ) + str.substring( 1 );
	}

	//Words which should not be capitalized in title case, mostly prepositions which appear ingame
	//This list is not comprehensive!
	private static final HashSet<String> noCaps = new HashSet<>(
			Arrays.asList(new String[]{
					//English
					"a", "of",
					//French
					"à", "avec", "de", "du", "des",
					//Portugese
					"a", "de", "des", "da", "das", "do", "dos",
					//German
					"der", "des",
					//Russian
					"с", //Not an english c, looks just like it though.
			})
	);

	public static String titleCase( String str ){
		String result = "";
		//split by any unicode space character
		for (String word : str.split("(?<=\\p{Zs})")){
			if (noCaps.contains(word.trim().toLowerCase())){
				result += word;
			} else {
				result += capitalize(word);
			}
		}
		//first character is always capitalized.
		return capitalize(result);
	}
}
