/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
		WINDOW_SILVER,
		RED_BUTTON,
		GREY_BUTTON,
		GREY_BUTTON_TR,
		TAG,
		GEM,
		SCROLL,
		TAB_SET,
		TAB_SELECTED,
		TAB_UNSELECTED
	}
	
	public static NinePatch get( Type type ) {
		String Asset = Assets.Interfaces.CHROME;
		switch (type) {
		case WINDOW:
			return new NinePatch( Asset, 0, 0, 20, 20, 6 );
		case WINDOW_SILVER:
			return new NinePatch( Asset, 86, 0, 22, 22, 7 );
		case TOAST:
			return new NinePatch( Asset, 22, 0, 18, 18, 5 );
		case TOAST_TR:
			return new NinePatch( Asset, 40, 0, 18, 18, 5 );
		case RED_BUTTON:
			return new NinePatch( Asset, 58, 0, 6, 6, 2 );
		case GREY_BUTTON:
			return new NinePatch( Asset, 58, 6, 6, 6, 2 );
		case TAG:
			return new NinePatch( Asset, 22, 18, 16, 14, 3 );
		case GEM:
			return new NinePatch( Asset, 0, 32, 32, 32, 13 );
		case GREY_BUTTON_TR:
			return new NinePatch( Asset, 53, 20, 9, 9, 5 );
		case SCROLL:
			return new NinePatch( Asset, 32, 32, 32, 32, 5, 11, 5, 11 );
		case TAB_SET:
			return new NinePatch( Asset, 64, 0, 20, 20, 6 );
		case TAB_SELECTED:
			return new NinePatch( Asset, 65, 22, 8, 13, 3, 7, 3, 5 );
		case TAB_UNSELECTED:
			return new NinePatch( Asset, 75, 22, 8, 13, 3, 7, 3, 5 );
		default:
			return null;
		}
	}
}
