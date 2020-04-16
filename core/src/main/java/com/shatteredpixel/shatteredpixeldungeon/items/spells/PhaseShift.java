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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PhaseShift extends TargetedSpell {
	
	{
		image = ItemSpriteSheet.PHASE_SHIFT;
	}
	
	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		final Char ch = Actor.findChar(bolt.collisionPos);
		
		if (ch == hero){
			ScrollOfTeleportation.teleportHero(curUser);
		} else if (ch != null) {
			int count = 10;
			int pos;
			do {
				pos = Dungeon.level.randomRespawnCell( hero );
				if (count-- <= 0) {
					break;
				}
			} while (pos == -1);
			
			if (pos == -1 || Dungeon.bossLevel()) {
				
				GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
				
			} else if (ch.properties().contains(Char.Property.IMMOVABLE)) {
				
				GLog.w( Messages.get(this, "tele_fail") );
				
			} else  {
				
				ch.pos = pos;
				if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING){
					((Mob) ch).state = ((Mob) ch).WANDERING;
				}
				ch.sprite.place(ch.pos);
				ch.sprite.visible = Dungeon.level.heroFOV[pos];
				
			}
		}
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 40) / 8f));
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfTeleportation.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = PhaseShift.class;
			outQuantity = 8;
		}
		
	}
	
}
