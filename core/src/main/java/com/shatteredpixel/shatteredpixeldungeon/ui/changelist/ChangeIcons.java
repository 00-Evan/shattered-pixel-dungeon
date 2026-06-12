/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;

//separate sprite assets for icons in the changelist, so that old visuals are preserved here after changes
public enum ChangeIcons {

	PD_AMULET,
	PD_CHALLENGES,
	PD_GHOST,
	PD_EYE,
	PD_MONK,
	PD_BAT,
	PD_SKELETON,
	PD_RAT,

	V010_SHPX,
	V010_EARTHROOT,
	V010_POTION_SILVER,
	V010_SCROLL_BERKANAN,

	V011_BLANDFRUIT,
	V011_ANKH,
	V011_SCROLL_LAGUZ,

	V020_HORN,
	V020_RING_DIAMOND,
	V020_MISC,

	V021_SHPX,
	V021_GOO,
	V021_SIGNPOST,

	V022_WEIGHTSTONE,
	V022_DREAMFOIL,
	V022_REMAINS,

	V023_HOURGLASS,
	V023_POTION_CRIMSON,

	V024_HONEYPOT,
	V024_BANDOLIER,

	V030_MAGE_CLOTH,
	V030_DISINTEGRATION,

	V031_GRIM_TRAP,
	V031_MENU_BUTTON,

	V032_TENGU,
	V032_MASTERY,

	V033_PLAYGAMES,

	V034_TRANSLATIONS,

	V035_WARRIOR_CLOTH,
	V035_WARRIOR_HEROARM,

	V040_LONGSWORD,
	V040_STYLUS,

	V041_PLATE,
	V041_RUNICBLADE,

	V042_GLAIVE,

	V043_FLAIL,

	V050_STAIRS,
	V050_QUARTERSTAFF,

	V074_SHPX,

	V081_MISC,

	;


	private static int texW = 128/16;
	int w, h;

	ChangeIcons(){
		w = h = 16;
	}

	ChangeIcons(int w, int h){
		this.w = w;
		this.h = h;
	}

	public Image get(){
		return get(this);
	}

	public static Image get( ChangeIcons type ) {
		Image icon = new Image( Assets.Interfaces.CHANGE_ICONS );
		int x = type.ordinal()%texW;
		int y = type.ordinal()/texW;
		icon.frame(16*x, 16*y, type.w, type.h);
		return icon;
	}

}
