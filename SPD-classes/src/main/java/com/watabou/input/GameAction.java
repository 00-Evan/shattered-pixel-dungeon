/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import java.util.ArrayList;

//This is similar to an enum, but we don't use that because subclasses should be able to add actions
public class GameAction {

	private static final ArrayList<GameAction> ALL_ACTIONS = new ArrayList<>();

	private int code;
	private String name;

	protected GameAction( String name ){
		code = ALL_ACTIONS.size();
		this.name = name;

		ALL_ACTIONS.add(this);
	}

	public int code(){
		return code;
	}

	public String name(){
		return name;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GameAction && ((GameAction) o).code == code;
	}

	public static final GameAction NONE = new GameAction( "none" );
	public static final GameAction BACK = new GameAction( "back" );

	public static ArrayList<GameAction> allActions(){
		return new ArrayList<>(ALL_ACTIONS);
	}

	public static int totalActions(){
		return ALL_ACTIONS.size();
	}

}
