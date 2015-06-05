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
package com.shatteredpixel.shatteredicepixeldungeon.items.wands;

import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredicepixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredicepixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfTelekinesis extends Wand {

	private static final String TXT_YOU_NOW_HAVE	= "You have magically transported %s into your backpack"; 
	
	{
		name = "Wand of Telekinesis";
		hitChars = false;
	}
	
	@Override
	protected void onZap( int cell ) {
		
		boolean mapUpdated = false;
		
		int maxDistance = level() + 4;
		Ballistica.distance = Math.min( Ballistica.distance, maxDistance );
		
		Char ch;
		Heap heap = null;
		
		for (int i=1; i < Ballistica.distance; i++) {
			
			int c = Ballistica.trace[i];
			
			int before = Dungeon.level.map[c];
			
			if ((ch = Actor.findChar( c )) != null) {

				if (i == Ballistica.distance-1) {
					
					ch.damage( maxDistance-1 - i, this );
					
				} else {
					
					int next = Ballistica.trace[i + 1];
					if ((Level.passable[next] || Level.avoid[next]) && Actor.findChar( next ) == null) {
						
						Actor.addDelayed( new Pushing( ch, ch.pos, next ), -1 );
						
						ch.pos = next;
						Actor.freeCell( next );

						if (ch instanceof Shopkeeper)
							ch.damage( 0, this );

						// FIXME
						if (ch instanceof Mob) {
							Dungeon.level.mobPress( (Mob)ch );
						} else {
							Dungeon.level.press( ch.pos, ch );
						}
						
					} else {
						
						ch.damage( maxDistance-1 - i, this );
						
					}
				}
			}
			
			if (heap == null && (heap = Dungeon.level.heaps.get( c )) != null) {
				switch (heap.type) {
				case HEAP:
					transport( heap );
					break;
				case CHEST:
					open( heap );
					break;
				default:
				}
			}
			
			Dungeon.level.press( c, null );
			if (before == Terrain.OPEN_DOOR && Actor.findChar( c ) == null) {
				
				Level.set( c, Terrain.DOOR );
				GameScene.updateMap( c );
				
			} else if (Level.water[c]) {
				
				GameScene.ripple( c );
				
			}
			
			if (!mapUpdated && Dungeon.level.map[c] != before) {
				mapUpdated = true;
			}
		}
		
		if (mapUpdated) {
			Dungeon.observe();
		}
	}
	
	private void transport( Heap heap ) {
		Item item = heap.pickUp();
		if (item.doPickUp( curUser )) {
			
			if (item instanceof Dewdrop) {

			} else {

				if ((item instanceof ScrollOfUpgrade && ((ScrollOfUpgrade)item).isKnown()) ||
					(item instanceof PotionOfStrength && ((PotionOfStrength)item).isKnown())) {
					GLog.p( TXT_YOU_NOW_HAVE, item.name() );
				} else {
					GLog.i( TXT_YOU_NOW_HAVE, item.name() );
				}
			}

		} else {
			Dungeon.level.drop( item, curUser.pos ).sprite.drop();
		}
	}
	
	private void open( Heap heap ) {
		heap.type = Type.HEAP;
		heap.sprite.link();
		heap.sprite.drop();
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.force( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"Waves of magic force from this wand will affect all cells on their way triggering traps, trampling high vegetation, " +
			"opening closed doors and closing open ones. They also push back monsters.";
	}
}
