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

import com.badlogic.gdx.Input;

//FIXME this is describing actions defined in the core module, it should probably be moved there.
public class KeyAction {

	public static final int NONE        = 0;

	public static final int BACK        = 1;
	public static final int MENU        = 2;

	public static final int HERO_INFO   = 3;
	public static final int JOURNAL     = 4;

	public static final int WAIT        = 5;
	public static final int SEARCH      = 6;

	public static final int INVENTORY   = 7;
	public static final int QUICKSLOT_1 = 8;
	public static final int QUICKSLOT_2 = 9;
	public static final int QUICKSLOT_3 = 10;
	public static final int QUICKSLOT_4 = 11;

	public static final int TAG_ATTACK  = 12;
	public static final int TAG_DANGER  = 13;
	public static final int TAG_ACTION  = 14;
	public static final int TAG_LOOT    = 15;
	public static final int TAG_RESUME  = 16;

	public static final int ZOOM_IN     = 17;
	public static final int ZOOM_OUT    = 18;
	public static final int ZOOM_DEFAULT= 19;

	public static final int N           = 20;
	public static final int NE          = 21;
	public static final int E           = 22;
	public static final int SE          = 23;
	public static final int S           = 24;
	public static final int SW          = 25;
	public static final int W           = 26;
	public static final int NW          = 27;

	public static void initialize() {

		KeyBindings.addName(NONE,           "none");

		KeyBindings.addName(BACK,           "back");
		KeyBindings.addName(MENU,           "menu");

		KeyBindings.addName(HERO_INFO,      "hero_info");
		KeyBindings.addName(JOURNAL,        "journal");

		KeyBindings.addName(WAIT,           "wait");
		KeyBindings.addName(SEARCH,         "search");

		KeyBindings.addName(INVENTORY,      "inventory");
		KeyBindings.addName(QUICKSLOT_1,    "quickslot_1");
		KeyBindings.addName(QUICKSLOT_2,    "quickslot_2");
		KeyBindings.addName(QUICKSLOT_3,    "quickslot_3");
		KeyBindings.addName(QUICKSLOT_4,    "quickslot_4");

		KeyBindings.addName(TAG_ATTACK,     "tag_attack");
		KeyBindings.addName(TAG_DANGER,     "tag_danger");
		KeyBindings.addName(TAG_ACTION,     "tag_action");
		KeyBindings.addName(TAG_LOOT,       "tag_loot");
		KeyBindings.addName(TAG_RESUME,     "tag_resume");

		KeyBindings.addName(ZOOM_IN,        "zoom_in");
		KeyBindings.addName(ZOOM_OUT,       "zoom_out");
		KeyBindings.addName(ZOOM_DEFAULT,   "zoom_default");

		KeyBindings.addName(N,              "none");
		KeyBindings.addName(NE,             "none");
		KeyBindings.addName(E,              "none");
		KeyBindings.addName(SE,             "none");
		KeyBindings.addName(S,              "none");
		KeyBindings.addName(SW,             "none");
		KeyBindings.addName(W,              "none");
		KeyBindings.addName(NW,             "none");
		
		//default key bindings
		KeyBindings.addBinding( Input.Keys.BACK, KeyAction.BACK );
		KeyBindings.addBinding( Input.Keys.MENU, KeyAction.MENU );

		KeyBindings.addBinding( Input.Keys.H, KeyAction.HERO_INFO );
		KeyBindings.addBinding( Input.Keys.J, KeyAction.JOURNAL );

		KeyBindings.addBinding( Input.Keys.NUMPAD_5, KeyAction.WAIT );
		KeyBindings.addBinding( Input.Keys.SPACE,    KeyAction.WAIT );
		KeyBindings.addBinding( Input.Keys.S,        KeyAction.SEARCH );

		KeyBindings.addBinding( Input.Keys.I,  KeyAction.INVENTORY );
		KeyBindings.addBinding( Input.Keys.Q,  KeyAction.QUICKSLOT_1 );
		KeyBindings.addBinding( Input.Keys.W,  KeyAction.QUICKSLOT_2 );
		KeyBindings.addBinding( Input.Keys.E,  KeyAction.QUICKSLOT_3 );
		KeyBindings.addBinding( Input.Keys.R,  KeyAction.QUICKSLOT_4 );

		KeyBindings.addBinding( Input.Keys.A,     KeyAction.TAG_ATTACK );
		KeyBindings.addBinding( Input.Keys.TAB,   KeyAction.TAG_DANGER );
		KeyBindings.addBinding( Input.Keys.D,     KeyAction.TAG_ACTION );
		KeyBindings.addBinding( Input.Keys.ENTER, KeyAction.TAG_LOOT );
		KeyBindings.addBinding( Input.Keys.T,     KeyAction.TAG_RESUME );

		KeyBindings.addBinding( Input.Keys.PLUS,   KeyAction.ZOOM_IN );
		KeyBindings.addBinding( Input.Keys.EQUALS, KeyAction.ZOOM_IN );
		KeyBindings.addBinding( Input.Keys.MINUS,  KeyAction.ZOOM_OUT );
		KeyBindings.addBinding( Input.Keys.SLASH,  KeyAction.ZOOM_DEFAULT );

		KeyBindings.addBinding( Input.Keys.UP,    KeyAction.N );
		KeyBindings.addBinding( Input.Keys.RIGHT, KeyAction.E );
		KeyBindings.addBinding( Input.Keys.DOWN,  KeyAction.S );
		KeyBindings.addBinding( Input.Keys.LEFT,  KeyAction.W );
		KeyBindings.addBinding( Input.Keys.NUMPAD_8,  KeyAction.N );
		KeyBindings.addBinding( Input.Keys.NUMPAD_9,  KeyAction.NE );
		KeyBindings.addBinding( Input.Keys.NUMPAD_6,  KeyAction.E );
		KeyBindings.addBinding( Input.Keys.NUMPAD_3,  KeyAction.SE );
		KeyBindings.addBinding( Input.Keys.NUMPAD_2,  KeyAction.S );
		KeyBindings.addBinding( Input.Keys.NUMPAD_1,  KeyAction.SW );
		KeyBindings.addBinding( Input.Keys.NUMPAD_4,  KeyAction.W );
		KeyBindings.addBinding( Input.Keys.NUMPAD_7,  KeyAction.NW );
		
	}
}
