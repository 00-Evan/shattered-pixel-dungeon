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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;

public class Flow extends Armor.Glyph {

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, triggers in Char.speed()
		return damage;
	}

	public static float speedBoost( Char owner, int level ){
		if (level == -1 || !Dungeon.level.water[owner.pos]){
			return 1;
		} else {
			return (2f + 0.5f*level) * genericProcChanceMultiplier(owner);
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLUE;
	}

}
