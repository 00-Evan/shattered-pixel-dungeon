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

package com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Obfuscation extends Armor.Glyph {

	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x888888 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, triggered in Char.stealth()
		return damage;
	}

	public static float stealthBoost( Char owner, int level ){
		if (level == -1) {
			return 0;
		} else {
			return (1 + level / 3f) * genericProcChanceMultiplier(owner);
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return GREY;
	}

}
