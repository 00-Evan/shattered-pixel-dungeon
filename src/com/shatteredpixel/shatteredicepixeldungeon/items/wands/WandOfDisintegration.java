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

import java.util.ArrayList;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.DeathRay;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfDisintegration extends Wand {

	{
		name = "Wand of Disintegration";
		hitChars = false;
	}
	
	@Override
	protected void onZap( int cell ) {
		
		boolean terrainAffected = false;
		
		int level = level();
		
		int maxDistance = distance();
		Ballistica.distance = Math.min( Ballistica.distance, maxDistance );
		
		ArrayList<Char> chars = new ArrayList<Char>();
		
		for (int i=1; i < Ballistica.distance; i++) {
			
			int c = Ballistica.trace[i];
			
			Char ch;
			if ((ch = Actor.findChar( c )) != null) {
				chars.add( ch );
			}
			
			int terr = Dungeon.level.map[c];
			if (terr == Terrain.DOOR || terr == Terrain.BARRICADE) {
				
				Level.set( c, Terrain.EMBERS );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			} else if (terr == Terrain.HIGH_GRASS) {
				
				Level.set( c, Terrain.GRASS );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			}
			
			CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}
		
		int lvl = level + chars.size();
		int dmgMin = lvl;
		int dmgMax = 8 + lvl * lvl / 3;
		for (Char ch : chars) {
			ch.damage( Random.NormalIntRange( dmgMin, dmgMax ), this );
			ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
			ch.sprite.flash();
		}
	}
	
	private int distance() {
		return level() + 4;
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		
		cell = Ballistica.trace[Math.min( Ballistica.distance, distance() ) - 1];
		curUser.sprite.parent.add( new DeathRay( curUser.sprite.center(), DungeonTilemap.tileCenterToWorld( cell ) ) );		
		callback.call();
	}
	
	@Override
	public String desc() {
		return
			"This wand emits a beam of destructive energy, which pierces all creatures in its way. " +
			"The more targets it hits, the more damage it inflicts to each of them.";
	}
}
