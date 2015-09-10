/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;

public class CheckBox extends RedButton {

	private boolean checked = false;
	
	public CheckBox( String label ) {
		super( label );
		
		icon( Icons.get( Icons.UNCHECKED ) );
	}

	@Override
	protected void layout() {
		super.layout();
		
		float margin = (height - text.baseLine()) / 2;
		
		text.x = x + margin;
		text.y = y + margin;

		margin = (height - icon.height) / 2;

		icon.x = x + width - margin - icon.width;
		icon.y = y + margin;
	}
	
	public boolean checked() {
		return checked;
	}
	
	public void checked( boolean value ) {
		if (checked != value) {
			checked = value;
			icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
		}
	}
	
	@Override
	protected void onClick() {
		super.onClick();
		checked( !checked );
	}
}
