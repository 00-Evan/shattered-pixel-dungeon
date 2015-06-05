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
package com.shatteredpixel.shatteredicepixeldungeon.items.armor;

import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;


public class LeatherArmor extends Armor {

	{	
		name = "leather armor";
		image = ItemSpriteSheet.ARMOR_LEATHER;
	}
	
	public LeatherArmor() {
		super( 2 );
	}
	
	@Override
	public String desc() {
		return "Armor made from tanned monster hide. Not as light as cloth armor but provides better protection.";
	}
}
