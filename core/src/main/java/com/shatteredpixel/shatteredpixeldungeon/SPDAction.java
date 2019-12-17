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

import com.badlogic.gdx.Input;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;

public class SPDAction extends GameAction {

	protected SPDAction( String name ){
		super( name );
	}

	//--New references to existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;

	public static final GameAction BACK  = GameAction.BACK;
	public static final GameAction MENU  = GameAction.MENU;
	//--

	public static final GameAction HERO_INFO   = new SPDAction("hero_info");
	public static final GameAction JOURNAL     = new SPDAction("journal");

	public static final GameAction WAIT        = new SPDAction("wait");
	public static final GameAction SEARCH      = new SPDAction("search");

	public static final GameAction INVENTORY   = new SPDAction("inventory");
	public static final GameAction QUICKSLOT_1 = new SPDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2 = new SPDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3 = new SPDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4 = new SPDAction("quickslot_4");

	public static final GameAction TAG_ATTACK  = new SPDAction("tag_attack");
	public static final GameAction TAG_DANGER  = new SPDAction("tag_danger");
	public static final GameAction TAG_ACTION  = new SPDAction("tag_action");
	public static final GameAction TAG_LOOT    = new SPDAction("tag_loot");
	public static final GameAction TAG_RESUME  = new SPDAction("tag_resume");

	public static final GameAction ZOOM_IN     = new SPDAction("zoom_in");
	public static final GameAction ZOOM_OUT    = new SPDAction("zoom_out");

	public static final GameAction N           = new SPDAction("n");
	public static final GameAction E           = new SPDAction("e");
	public static final GameAction S           = new SPDAction("s");
	public static final GameAction W           = new SPDAction("w");
	public static final GameAction NE          = new SPDAction("ne");
	public static final GameAction SE          = new SPDAction("se");
	public static final GameAction SW          = new SPDAction("sw");
	public static final GameAction NW          = new SPDAction("nw");

	public static void initDefaults() {

		//default key bindings
		KeyBindings.addKeyBinding( Input.Keys.BACK,        SPDAction.BACK );
		KeyBindings.addKeyBinding( Input.Keys.MENU,        SPDAction.MENU );

		KeyBindings.addKeyBinding( Input.Keys.H,           SPDAction.HERO_INFO );
		KeyBindings.addKeyBinding( Input.Keys.J,           SPDAction.JOURNAL );

		KeyBindings.addKeyBinding( Input.Keys.SPACE,       SPDAction.WAIT );
		KeyBindings.addKeyBinding( Input.Keys.S,           SPDAction.SEARCH );

		KeyBindings.addKeyBinding( Input.Keys.I,           SPDAction.INVENTORY );
		KeyBindings.addKeyBinding( Input.Keys.Q,           SPDAction.QUICKSLOT_1 );
		KeyBindings.addKeyBinding( Input.Keys.W,           SPDAction.QUICKSLOT_2 );
		KeyBindings.addKeyBinding( Input.Keys.E,           SPDAction.QUICKSLOT_3 );
		KeyBindings.addKeyBinding( Input.Keys.R,           SPDAction.QUICKSLOT_4 );

		KeyBindings.addKeyBinding( Input.Keys.A,           SPDAction.TAG_ATTACK );
		KeyBindings.addKeyBinding( Input.Keys.TAB,         SPDAction.TAG_DANGER );
		KeyBindings.addKeyBinding( Input.Keys.D,           SPDAction.TAG_ACTION );
		KeyBindings.addKeyBinding( Input.Keys.ENTER,       SPDAction.TAG_LOOT );
		KeyBindings.addKeyBinding( Input.Keys.T,           SPDAction.TAG_RESUME );

		KeyBindings.addKeyBinding( Input.Keys.PLUS,        SPDAction.ZOOM_IN );
		KeyBindings.addKeyBinding( Input.Keys.EQUALS,      SPDAction.ZOOM_IN );
		KeyBindings.addKeyBinding( Input.Keys.MINUS,       SPDAction.ZOOM_OUT );

		KeyBindings.addKeyBinding( Input.Keys.UP,          SPDAction.N );
		KeyBindings.addKeyBinding( Input.Keys.RIGHT,       SPDAction.E );
		KeyBindings.addKeyBinding( Input.Keys.DOWN,        SPDAction.S );
		KeyBindings.addKeyBinding( Input.Keys.LEFT,        SPDAction.W );

		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_5,    SPDAction.WAIT );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_8,    SPDAction.N );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_9,    SPDAction.NE );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_6,    SPDAction.E );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_3,    SPDAction.SE );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_2,    SPDAction.S );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_1,    SPDAction.SW );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_4,    SPDAction.W );
		KeyBindings.addKeyBinding( Input.Keys.NUMPAD_7,    SPDAction.NW );

	}

	//file name? perhaps

	public static void loadBindings(){

	}

	public static void saveBindings(){

	}

}
