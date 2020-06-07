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

package net.casiello.pixeldungeonrescue.items.armor.curses;

import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.hero.HeroSubClass;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.particles.LeafParticle;
import net.casiello.pixeldungeonrescue.items.Generator;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.plants.BlandfruitBush;
import net.casiello.pixeldungeonrescue.plants.Plant;
import net.casiello.pixeldungeonrescue.plants.Starflower;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Overgrowth extends Armor.Glyph {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		
		if ( Random.Int( 20 ) == 0) {
			
			Plant.Seed s;
			do{
				s = (Plant.Seed) Generator.random(Generator.Category.SEED);
			} while (s instanceof BlandfruitBush.Seed || s instanceof Starflower.Seed);
			
			Plant p = s.couch(defender.pos, null);
			
			//momentarily revoke warden benefits, otherwise this curse would be incredibly powerful
			if (defender instanceof Hero && ((Hero) defender).subClass == HeroSubClass.WARDEN){
				((Hero) defender).subClass = HeroSubClass.NONE;
				p.activate( defender );
				((Hero) defender).subClass = HeroSubClass.WARDEN;
			} else {
				p.activate( defender );
			}
			
			
			CellEmitter.get( defender.pos ).burst( LeafParticle.LEVEL_SPECIFIC, 10 );
			
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
	
	@Override
	public boolean curse() {
		return true;
	}
}
