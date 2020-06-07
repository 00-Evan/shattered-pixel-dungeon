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
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Charm;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.items.armor.Armor.Glyph;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Affection extends Glyph {
	
	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing( 0xFF4488 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.buffedLvl());
		
		// lvl 0 - 15%
		// lvl 1 ~ 19%
		// lvl 2 ~ 23%
		if (Random.Int( level + 20 ) >= 17) {
			
			int duration = Random.IntRange( 8, 12 );

			Buff.affect( attacker, Charm.class, duration ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );

		}
		
		return damage;
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}
