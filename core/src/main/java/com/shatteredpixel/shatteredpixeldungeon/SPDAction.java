/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.watabou.input.ControllerHandler;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
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

	public static final GameAction LEFT_CLICK   = GameAction.LEFT_CLICK;
	public static final GameAction RIGHT_CLICK  = GameAction.RIGHT_CLICK;
	public static final GameAction MIDDLE_CLICK = GameAction.MIDDLE_CLICK;
	//--

	public static final GameAction N            = new SPDAction("n");
	public static final GameAction W            = new SPDAction("w");
	public static final GameAction S            = new SPDAction("s");
	public static final GameAction E            = new SPDAction("e");
	public static final GameAction NW           = new SPDAction("nw");
	public static final GameAction NE           = new SPDAction("ne");
	public static final GameAction SW           = new SPDAction("sw");
	public static final GameAction SE           = new SPDAction("se");
	public static final GameAction WAIT_OR_PICKUP   = new SPDAction("wait_or_pickup");

	public static final GameAction INVENTORY    = new SPDAction("inventory");
	public static final GameAction INVENTORY_SELECTOR   = new SPDAction("inventory_selector");
	public static final GameAction QUICKSLOT_SELECTOR   = new SPDAction("quickslot_selector");
	public static final GameAction QUICKSLOT_1  = new SPDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2  = new SPDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3  = new SPDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4  = new SPDAction("quickslot_4");
	public static final GameAction QUICKSLOT_5  = new SPDAction("quickslot_5");
	public static final GameAction QUICKSLOT_6  = new SPDAction("quickslot_6");

	public static final GameAction BAG_1        = new SPDAction("bag_1");
	public static final GameAction BAG_2        = new SPDAction("bag_2");
	public static final GameAction BAG_3        = new SPDAction("bag_3");
	public static final GameAction BAG_4        = new SPDAction("bag_4");
	public static final GameAction BAG_5        = new SPDAction("bag_5");

	public static final GameAction EXAMINE      = new SPDAction("examine");
	public static final GameAction WAIT         = new SPDAction("wait");
	public static final GameAction REST         = new SPDAction("rest");

	public static final GameAction TAG_ATTACK   = new SPDAction("tag_attack");
	public static final GameAction TAG_ACTION   = new SPDAction("tag_action");
	public static final GameAction TAG_LOOT     = new SPDAction("tag_loot");
	public static final GameAction TAG_RESUME   = new SPDAction("tag_resume");

	public static final GameAction CYCLE        = new SPDAction("cycle");

	public static final GameAction HERO_INFO    = new SPDAction("hero_info");
	public static final GameAction JOURNAL      = new SPDAction("journal");

	public static final GameAction ZOOM_IN      = new SPDAction("zoom_in");
	public static final GameAction ZOOM_OUT     = new SPDAction("zoom_out");

	private static final LinkedHashMap<Integer, GameAction> defaultBindings = new LinkedHashMap<>();
	static {
		defaultBindings.put( Input.Keys.ESCAPE,         SPDAction.BACK );
		defaultBindings.put( Input.Keys.BACKSPACE,      SPDAction.BACK );

		defaultBindings.put( Input.Keys.W,              SPDAction.N );
		defaultBindings.put( Input.Keys.A,              SPDAction.W );
		defaultBindings.put( Input.Keys.S,              SPDAction.S );
		defaultBindings.put( Input.Keys.D,              SPDAction.E );
		defaultBindings.put( Input.Keys.SPACE,          SPDAction.WAIT_OR_PICKUP);

		defaultBindings.put( Input.Keys.UP,             SPDAction.N );
		defaultBindings.put( Input.Keys.LEFT,           SPDAction.W );
		defaultBindings.put( Input.Keys.DOWN,           SPDAction.S );
		defaultBindings.put( Input.Keys.RIGHT,          SPDAction.E );

		defaultBindings.put( Input.Keys.NUMPAD_8,       SPDAction.N );
		defaultBindings.put( Input.Keys.NUMPAD_4,       SPDAction.W );
		defaultBindings.put( Input.Keys.NUMPAD_2,       SPDAction.S );
		defaultBindings.put( Input.Keys.NUMPAD_6,       SPDAction.E );
		defaultBindings.put( Input.Keys.NUMPAD_7,       SPDAction.NW );
		defaultBindings.put( Input.Keys.NUMPAD_9,       SPDAction.NE );
		defaultBindings.put( Input.Keys.NUMPAD_1,       SPDAction.SW );
		defaultBindings.put( Input.Keys.NUMPAD_3,       SPDAction.SE );
		defaultBindings.put( Input.Keys.NUMPAD_5,       SPDAction.WAIT_OR_PICKUP );

		defaultBindings.put( Input.Keys.F,              SPDAction.INVENTORY );
		defaultBindings.put( Input.Keys.I,              SPDAction.INVENTORY );
		defaultBindings.put( Input.Keys.NUM_1,          SPDAction.QUICKSLOT_1 );
		defaultBindings.put( Input.Keys.NUM_2,          SPDAction.QUICKSLOT_2 );
		defaultBindings.put( Input.Keys.NUM_3,          SPDAction.QUICKSLOT_3 );
		defaultBindings.put( Input.Keys.NUM_4,          SPDAction.QUICKSLOT_4 );
		defaultBindings.put( Input.Keys.NUM_5,          SPDAction.QUICKSLOT_5 );
		defaultBindings.put( Input.Keys.NUM_6,          SPDAction.QUICKSLOT_6 );

		defaultBindings.put( Input.Keys.F1,             SPDAction.BAG_1 );
		defaultBindings.put( Input.Keys.F2,             SPDAction.BAG_2 );
		defaultBindings.put( Input.Keys.F3,             SPDAction.BAG_3 );
		defaultBindings.put( Input.Keys.F4,             SPDAction.BAG_4 );
		defaultBindings.put( Input.Keys.F5,             SPDAction.BAG_5 );

		defaultBindings.put( Input.Keys.E,              SPDAction.EXAMINE );
		defaultBindings.put( Input.Keys.Z,              SPDAction.REST );

		defaultBindings.put( Input.Keys.Q,              SPDAction.TAG_ATTACK );
		defaultBindings.put( Input.Keys.TAB,            SPDAction.CYCLE);
		defaultBindings.put( Input.Keys.X,              SPDAction.TAG_ACTION );
		defaultBindings.put( Input.Keys.C,              SPDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.ENTER,          SPDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.R,              SPDAction.TAG_RESUME );

		defaultBindings.put( Input.Keys.H,              SPDAction.HERO_INFO );
		defaultBindings.put( Input.Keys.J,              SPDAction.JOURNAL );

		defaultBindings.put( Input.Keys.PLUS,           SPDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.EQUALS,         SPDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.MINUS,          SPDAction.ZOOM_OUT );
	}

	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return new LinkedHashMap<>(defaultBindings);
	}

	private static final LinkedHashMap<Integer, GameAction> defaultControllerBindings = new LinkedHashMap<>();
	static {
		defaultControllerBindings.put( Input.Keys.BUTTON_START,     SPDAction.BACK );
		defaultControllerBindings.put( Input.Keys.BUTTON_SELECT,    SPDAction.JOURNAL );

		defaultControllerBindings.put( Input.Keys.BUTTON_R2,        SPDAction.LEFT_CLICK );
		defaultControllerBindings.put( Input.Keys.BUTTON_THUMBR,    SPDAction.LEFT_CLICK );
		defaultControllerBindings.put( Input.Keys.BUTTON_L2,        SPDAction.RIGHT_CLICK );

		defaultControllerBindings.put( Input.Keys.DPAD_UP+1000,     SPDAction.TAG_ACTION );
		defaultControllerBindings.put( Input.Keys.DPAD_LEFT+1000,   SPDAction.TAG_LOOT );
		defaultControllerBindings.put( Input.Keys.DPAD_DOWN+1000,   SPDAction.TAG_RESUME );
		defaultControllerBindings.put( Input.Keys.DPAD_RIGHT+1000,  SPDAction.CYCLE );

		defaultControllerBindings.put( Input.Keys.BUTTON_THUMBL,    SPDAction.WAIT_OR_PICKUP );

		defaultControllerBindings.put( Input.Keys.BUTTON_R1,        SPDAction.ZOOM_IN );
		defaultControllerBindings.put( Input.Keys.BUTTON_L1,        SPDAction.ZOOM_OUT );

		defaultControllerBindings.put( Input.Keys.BUTTON_A,         SPDAction.TAG_ATTACK );
		defaultControllerBindings.put( Input.Keys.BUTTON_B,         SPDAction.EXAMINE );
		defaultControllerBindings.put( Input.Keys.BUTTON_X,         SPDAction.QUICKSLOT_SELECTOR );
		defaultControllerBindings.put( Input.Keys.BUTTON_Y,         SPDAction.INVENTORY_SELECTOR );
	}

	public static LinkedHashMap<Integer, GameAction> getControllerDefaults() {
		return new LinkedHashMap<>(defaultControllerBindings);
	}

	static {
		//hard bindings for android devices
		KeyBindings.addHardBinding( Input.Keys.BACK, SPDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, SPDAction.INVENTORY );

		//hard bindings for desktop fullscreen toggle
		//not bound to specific game actions, see PixelScene
		//Note that user-entered bindings can override these individually, and that's fine.
		KeyBindings.addHardBinding( Input.Keys.ALT_RIGHT, SPDAction.NONE );
		KeyBindings.addHardBinding( Input.Keys.ENTER, SPDAction.NONE );
	}

	//we only save/loads keys which differ from the default configuration.
	private static final String BINDINGS_FILE = "keybinds.dat";

	public static void loadBindings(){

		if (!KeyBindings.getAllBindings().isEmpty()){
			return;
		}

		try {
			Bundle b = FileUtils.bundleFromFile(BINDINGS_FILE);

			Bundle firstKeys = b.getBundle("first_keys");
			Bundle secondKeys = b.getBundle("second_keys");
			Bundle thirdKeys = b.getBundle("third_keys");

			LinkedHashMap<Integer, GameAction> defaults = getDefaults();
			LinkedHashMap<Integer, GameAction> merged = new LinkedHashMap<>();

			for (GameAction a : allActions()) {
				if (firstKeys.contains(a.name()) && KeyEvent.isKeyboardKey(firstKeys.getInt(a.name()))) {
					if (firstKeys.getInt(a.name()) == 0){
						continue; //we have no keys assigned to this action, move to the next one
					} else {
						merged.put(firstKeys.getInt(a.name()), a);
						//remove whatever the first default key was for this action, if any
						for (int i : defaults.keySet()) {
							if (defaults.get(i) == a) {
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the first one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

				if (secondKeys.contains(a.name()) && KeyEvent.isKeyboardKey(secondKeys.getInt(a.name()))) {
					if (secondKeys.getInt(a.name()) == 0){
						continue; //we have no more keys assigned to this action, move to the next one
					} else {
						merged.put(secondKeys.getInt(a.name()), a);
						//remove whatever the second default key was for this action, if any
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the next one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

				if (thirdKeys.contains(a.name()) && KeyEvent.isKeyboardKey(thirdKeys.getInt(a.name()))) {
					if (thirdKeys.getInt(a.name()) == 0){
						continue; //we have no more keys assigned to this action, move to the next one
					} else {
						merged.put(thirdKeys.getInt(a.name()), a);
						//remove whatever the third default key was for this action, if any
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the next one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

			}

			KeyBindings.setAllBindings(merged);

			defaults = getControllerDefaults();
			merged.clear();

			Bundle firstButtons = b.getBundle("first_keys_controller");
			Bundle secondButtons = b.getBundle("second_keys_controller");
			Bundle thirdButtons = b.getBundle("third_keys_controller");

			for (GameAction a : allActions()) {
				if (firstButtons.contains(a.name()) && ControllerHandler.icControllerKey(firstButtons.getInt(a.name()))) {
					if (firstButtons.getInt(a.name()) == 0){
						continue; //we have no keys assigned to this action, move to the next one
					} else {
						merged.put(firstButtons.getInt(a.name()), a);
						//remove whatever the first default button was for this action, if any
						for (int i : defaults.keySet()) {
							if (defaults.get(i) == a) {
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the first one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

				if (secondButtons.contains(a.name()) && ControllerHandler.icControllerKey(secondButtons.getInt(a.name()))) {
					if (secondButtons.getInt(a.name()) == 0){
						continue; //we have no more keys assigned to this action, move to the next one
					} else {
						merged.put(secondButtons.getInt(a.name()), a);
						//remove whatever the second default button was for this action, if any
						for (int i : defaults.keySet()) {
							if (defaults.get(i) == a) {
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the next one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

				if (thirdButtons.contains(a.name()) && ControllerHandler.icControllerKey(thirdButtons.getInt(a.name()))) {
					if (thirdButtons.getInt(a.name()) == 0){
						continue; //we have no more keys assigned to this action, move to the next one
					} else {
						merged.put(thirdButtons.getInt(a.name()), a);
						//remove whatever the third default button was for this action, if any
						for (int i : defaults.keySet()) {
							if (defaults.get(i) == a) {
								defaults.remove(i);
								break;
							}
						}
					}
				} else {
					//if we have no custom key here, find the next one from defaults and merge it
					for (int i : defaults.keySet()){
						if (defaults.get(i) == a){
							merged.put(i, defaults.remove(i));
							break;
						}
					}
				}

			}

			KeyBindings.setAllControllerBindings(merged);

		} catch (Exception e){
			KeyBindings.setAllBindings(getDefaults());
			KeyBindings.setAllControllerBindings(getControllerDefaults());
		}

	}

	public static void saveBindings(){

		Bundle b = new Bundle();

		Bundle firstKeys = new Bundle();
		Bundle secondKeys = new Bundle();
		Bundle thirdKeys = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int thirdCur = 0;
			int firstDef = 0;
			int secondDef = 0;
			int thirdDef = 0;

			for (int i : defaultBindings.keySet()){
				if (defaultBindings.get(i) == a){
					if (firstDef == 0) {
						firstDef = i;
					} else if (secondDef == 0) {
						secondDef = i;
					} else {
						thirdDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if (firstCur == 0) {
						firstCur = i;
					} else if (secondCur == 0) {
						secondCur = i;
					} else {
						thirdCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstKeys.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondKeys.put(a.name(), secondCur);
			}
			if (thirdCur != thirdDef){
				thirdKeys.put(a.name(), thirdCur);
			}

		}

		b.put("first_keys", firstKeys);
		b.put("second_keys", secondKeys);
		b.put("third_keys", thirdKeys);

		Bundle firstButtons = new Bundle();
		Bundle secondButtons = new Bundle();
		Bundle thirdButtons = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int thirdCur = 0;
			int firstDef = 0;
			int secondDef = 0;
			int thirdDef = 0;

			for (int i : defaultControllerBindings.keySet()){
				if (defaultControllerBindings.get(i) == a){
					if (firstDef == 0) {
						firstDef = i;
					} else if (secondDef == 0) {
						secondDef = i;
					} else {
						thirdDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllControllerBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if (firstCur == 0) {
						firstCur = i;
					} else if (secondCur == 0) {
						secondCur = i;
					} else {
						thirdCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstButtons.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondButtons.put(a.name(), secondCur);
			}
			if (thirdCur != thirdDef){
				thirdButtons.put(a.name(), thirdCur);
			}

		}

		b.put("first_keys_controller", firstButtons);
		b.put("second_keys_controller", secondButtons);
		b.put("third_keys_controller", thirdButtons);

		try {
			FileUtils.bundleToFile(BINDINGS_FILE, b);
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}

	}

}
