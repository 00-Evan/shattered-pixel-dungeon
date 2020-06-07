/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.items.armor.glyphs;

import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Bleeding;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Thorns extends Armor.Glyph {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.buffedLvl());

		// lvl 0 - 16.7%
		// lvl 1 - 28.6%
		// lvl 2 - 37.5%
		if ( Random.Int( level + 6) >= 5) {

			Buff.affect( attacker, Bleeding.class).set( 4 + level );

		}

		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return RED;
	}
}
