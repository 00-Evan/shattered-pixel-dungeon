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
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.LinkedHashMap;

public class SPDAction extends GameAction {

	protected SPDAction( String name ){
		super( name );
	}

	//--New references to existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;
	public static final GameAction BACK  = GameAction.BACK;
	//--

	public static final GameAction HERO_INFO   = new SPDAction("hero_info");
	public static final GameAction JOURNAL     = new SPDAction("journal");

	public static final GameAction WAIT        = new SPDAction("wait");
	public static final GameAction SEARCH      = new SPDAction("search");
	public static final GameAction REST        = new SPDAction("rest");

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

	private static final LinkedHashMap<Integer, GameAction> defaultBindings = new LinkedHashMap<>();
	static {
		defaultBindings.put( Input.Keys.ESCAPE,      SPDAction.BACK );
		defaultBindings.put( Input.Keys.BACKSPACE,   SPDAction.BACK );

		defaultBindings.put( Input.Keys.H,           SPDAction.HERO_INFO );
		defaultBindings.put( Input.Keys.J,           SPDAction.JOURNAL );

		defaultBindings.put( Input.Keys.SPACE,       SPDAction.WAIT );
		defaultBindings.put( Input.Keys.S,           SPDAction.SEARCH );
		defaultBindings.put( Input.Keys.Z,           SPDAction.REST );

		defaultBindings.put( Input.Keys.I,           SPDAction.INVENTORY );
		defaultBindings.put( Input.Keys.Q,           SPDAction.QUICKSLOT_1 );
		defaultBindings.put( Input.Keys.W,           SPDAction.QUICKSLOT_2 );
		defaultBindings.put( Input.Keys.E,           SPDAction.QUICKSLOT_3 );
		defaultBindings.put( Input.Keys.R,           SPDAction.QUICKSLOT_4 );

		defaultBindings.put( Input.Keys.A,           SPDAction.TAG_ATTACK );
		defaultBindings.put( Input.Keys.TAB,         SPDAction.TAG_DANGER );
		defaultBindings.put( Input.Keys.D,           SPDAction.TAG_ACTION );
		defaultBindings.put( Input.Keys.ENTER,       SPDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.T,           SPDAction.TAG_RESUME );

		defaultBindings.put( Input.Keys.PLUS,        SPDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.EQUALS,      SPDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.MINUS,       SPDAction.ZOOM_OUT );

		defaultBindings.put( Input.Keys.UP,          SPDAction.N );
		defaultBindings.put( Input.Keys.RIGHT,       SPDAction.E );
		defaultBindings.put( Input.Keys.DOWN,        SPDAction.S );
		defaultBindings.put( Input.Keys.LEFT,        SPDAction.W );

		defaultBindings.put( Input.Keys.NUMPAD_5,    SPDAction.WAIT );
		defaultBindings.put( Input.Keys.NUMPAD_8,    SPDAction.N );
		defaultBindings.put( Input.Keys.NUMPAD_9,    SPDAction.NE );
		defaultBindings.put( Input.Keys.NUMPAD_6,    SPDAction.E );
		defaultBindings.put( Input.Keys.NUMPAD_3,    SPDAction.SE );
		defaultBindings.put( Input.Keys.NUMPAD_2,    SPDAction.S );
		defaultBindings.put( Input.Keys.NUMPAD_1,    SPDAction.SW );
		defaultBindings.put( Input.Keys.NUMPAD_4,    SPDAction.W );
		defaultBindings.put( Input.Keys.NUMPAD_7,    SPDAction.NW );
	}

	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return new LinkedHashMap<>(defaultBindings);
	}

	//hard bindings for android devices
	static {
		KeyBindings.addHardBinding( Input.Keys.BACK, SPDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, SPDAction.INVENTORY );
	}

	//we only save/loads keys which differ from the default configuration.
	private static final String BINDINGS_FILE = "keybinds.dat";

	public static void loadBindings(){

		try {
			Bundle b = FileUtils.bundleFromFile(BINDINGS_FILE);

			Bundle firstKeys = b.getBundle("first_keys");
			Bundle secondKeys = b.getBundle("second_keys");

			LinkedHashMap<Integer, GameAction> defaults = getDefaults();
			LinkedHashMap<Integer, GameAction> custom = new LinkedHashMap<>();

			for (GameAction a : allActions()) {
				if (firstKeys.contains(a.name())) {
					if (firstKeys.getInt(a.name()) == 0){
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					} else {
						custom.put(firstKeys.getInt(a.name()), a);
						defaults.remove(firstKeys.getInt(a.name()));
					}
				}

				//we store any custom second keys in defaults for the moment to preserve order
				//incase the 2nd key is custom but the first one isn't
				if (secondKeys.contains(a.name())) {
					if (secondKeys.getInt(a.name()) == 0){
						int last = 0;
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								last = i;
							}
						}
						defaults.remove(last);
					} else {
						defaults.remove(secondKeys.getInt(a.name()));
						defaults.put(secondKeys.getInt(a.name()), a);
					}
				}

			}

			//now merge them and store
			for( int i : defaults.keySet()){
				if (i != 0) {
					custom.put(i, defaults.get(i));
				}
			}

			KeyBindings.setAllBindings(custom);

		} catch (Exception e){
			KeyBindings.setAllBindings(getDefaults());
		}

	}

	public static void saveBindings(){

		Bundle b = new Bundle();

		Bundle firstKeys = new Bundle();
		Bundle secondKeys = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int firstDef = 0;
			int secondDef = 0;

			for (int i : defaultBindings.keySet()){
				if (defaultBindings.get(i) == a){
					if(firstDef == 0){
						firstDef = i;
					} else {
						secondDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if(firstCur == 0){
						firstCur = i;
					} else {
						secondCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstKeys.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondKeys.put(a.name(), secondCur);
			}

		}

		b.put("first_keys", firstKeys);
		b.put("second_keys", secondKeys);

		try {
			FileUtils.bundleToFile(BINDINGS_FILE, b);
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}

	}

}
