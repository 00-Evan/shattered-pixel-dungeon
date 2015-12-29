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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static ResourceBundle strings =
			ResourceBundle.getBundle("com.shatteredpixel.shatteredpixeldungeon.messages.messages");

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

		if (android.os.Build.VERSION.SDK_INT >= 9 && !strings.containsKey(key.toLowerCase())){
			//this is so child classes can inherit properties from their parents.
			//in cases where text is commonly grabbed as a utility from classes that aren't mean to be instantiated
			//(e.g. flavourbuff.dispTurns()) using .class directly is probably smarter to prevent unnecessary recursive calls.
			if (c != null && c.getSuperclass() != null){
				return get(c.getSuperclass(), k, args);
			} else {
				return "!!!NO TEXT FOUND!!!";
			}
		} else {
			try {
				if (args.length > 0) return String.format(Locale.ENGLISH, strings.getString(key.toLowerCase()), args);
				else return strings.getString(key.toLowerCase());
			} catch (MissingResourceException e) {
				//This silly catch block exists because android 2.2 does not support bundle.containsKey.
				//This should only ever trigger on 2.2 devices
				if (c != null && c.getSuperclass() != null){
					return get(c.getSuperclass(), k, args);
				} else {
					return "!!!NO TEXT FOUND!!!";
				}
			}
		}
	}
}

