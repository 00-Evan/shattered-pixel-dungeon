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

package com.watabou.input;

//FIXME this is describing actions defined in the core module, it should probably be moved there.
public enum KeyAction {
	BACK,
	MENU,
	
	HERO_INFO,
	JOURNAL,
	
	WAIT,
	SEARCH,
	
	INVENTORY,
	QUICKSLOT_1,
	QUICKSLOT_2,
	QUICKSLOT_3,
	QUICKSLOT_4,
	
	TAG_ATTACK,
	TAG_DANGER,
	TAG_ACTION,
	TAG_LOOT,
	TAG_RESUME,
	
	ZOOM_IN,
	ZOOM_OUT,
	ZOOM_DEFAULT,
	
	N, NE, E, SE, S, SW, W, NW
}
