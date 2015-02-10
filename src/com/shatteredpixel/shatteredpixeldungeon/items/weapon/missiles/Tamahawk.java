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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tamahawk extends MissileWeapon {

	{
		name = "tomahawk";
		image = ItemSpriteSheet.TOMAHAWK;
		
		STR = 17;
		
		MIN = 4;
		MAX = 20;
	}
	
	public Tamahawk() {
		this( 1 );
	}
	
	public Tamahawk( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		Buff.affect( defender, Bleeding.class ).set( damage );
	}	
	
	@Override
	public String desc() {
		return 
			"This throwing axe is not that heavy, but it still " +
			"requires significant strength to be used effectively.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 5, 12 );
		return this;
	}
	
	@Override
	public int price() {
		return 15 * quantity;
	}
}
