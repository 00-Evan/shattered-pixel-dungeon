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
package com.watabou.pixeldungeon.items.weapon.enchantments;

import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.weapon.Weapon;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Poison extends Weapon.Enchantment {

	private static final String TXT_VENOMOUS	= "Venomous %s";
	
	private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0x4400AA );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max( 0, weapon.level );
		
		if (Random.Int( level + 3 ) >= 2) {
			
			Buff.affect( defender, com.watabou.pixeldungeon.actors.buffs.Poison.class ).
				set( com.watabou.pixeldungeon.actors.buffs.Poison.durationFactor( defender ) * (level + 1) );
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Glowing glowing() {
		return PURPLE;
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_VENOMOUS, weaponName );
	}

}
