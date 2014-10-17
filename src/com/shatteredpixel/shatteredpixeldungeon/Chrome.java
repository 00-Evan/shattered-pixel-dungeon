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
package com.shatteredpixel.shatteredpixeldungeon;

import com.watabou.noosa.NinePatch;

public class Chrome {

	public enum  Type {
		TOAST,
		TOAST_TR,
		WINDOW,
		BUTTON,
		TAG,
		GEM,
		SCROLL,
		TAB_SET,
		TAB_SELECTED,
		TAB_UNSELECTED
	};
	
	public static NinePatch get( Type type ) {
		switch (type) {
		case WINDOW:
			return new NinePatch( Assets.CHROME, 0, 0, 22, 22, 7 );
		case TOAST:
			return new NinePatch( Assets.CHROME, 22, 0, 18, 18, 5 );
		case TOAST_TR:
			return new NinePatch( Assets.CHROME, 40, 0, 18, 18, 5 );
		case BUTTON:
			return new NinePatch( Assets.CHROME, 58, 0, 4, 4, 1 );
		case TAG:
			return new NinePatch( Assets.CHROME, 22, 18, 16, 14, 3 );
		case GEM:
			return new NinePatch( Assets.CHROME, 0, 32, 32, 32, 13 );
		case SCROLL:
			return new NinePatch( Assets.CHROME, 32, 32, 32, 32, 5, 11, 5, 11 );
		case TAB_SET:
			return new NinePatch( Assets.CHROME, 64, 0, 22, 22, 7, 7, 7, 7 );
		case TAB_SELECTED:
			return new NinePatch( Assets.CHROME, 64, 22, 10, 14, 4, 7, 4, 6 );
		case TAB_UNSELECTED:
			return new NinePatch( Assets.CHROME, 74, 22, 10, 14, 4, 7, 4, 6 );
		default:
			return null;
		}
	}
}
