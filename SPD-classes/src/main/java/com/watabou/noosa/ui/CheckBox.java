/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.watabou.noosa.ui;

public class CheckBox extends Button {

	protected boolean checked;
	
	public boolean checked() {
		return checked;
	}
	
	public void checked( boolean value ) {
		if (checked != value) {
			checked = value;
			updateState();
		}
	}
	
	protected void updateState() {
		
	}
	
	@Override
	protected void onClick() {
		checked( !checked );
		onChange();
	}
	
	protected void onChange() {
	}
}
