/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.items.keys;

import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;

public class GoldenKey extends Key {
	
	{
		name = "golden key";
		image = ItemSpriteSheet.GOLDEN_KEY;
	}
	
	public GoldenKey() {
		this( 0 );
	}
	
	public GoldenKey( int depth ) {
		super();
		this.depth = depth;
	}
	
	@Override
	public String info() {
		return 
			"The notches on this golden key are tiny and intricate. " +
			"Maybe it can open some chest lock?";
	}
}
