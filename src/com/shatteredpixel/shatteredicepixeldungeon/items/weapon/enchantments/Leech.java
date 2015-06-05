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
package com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Leech extends Weapon.Enchantment {

	private static final String TXT_VAMPIRIC	= "Vampiric %s";
	
	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		
		int level = Math.max( 0, weapon.level );
		
		// lvl 0 - 33%
		// lvl 1 - 43%
		// lvl 2 - 50%
		int maxValue = damage * (level + 2) / (level + 6);
		int effValue = Math.min( Random.IntRange( 0, maxValue ), attacker.HT - attacker.HP );
		
		if (effValue > 0) {
		
			attacker.HP += effValue;
			attacker.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 1 );
			attacker.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( effValue ) );
			
			return true;
			
		} else {
			return false;
		}
	}
	
	@Override
	public Glowing glowing() {
		return RED;
	}
	
	@Override
	public String name( String weaponName ) {
		return String.format( TXT_VAMPIRIC, weaponName );
	}

}
