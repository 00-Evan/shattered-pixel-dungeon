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

package net.casiello.pixeldungeonrescue.items.stones;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Actor;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Paralysis;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Lightning;
import net.casiello.pixeldungeonrescue.effects.particles.EnergyParticle;
import net.casiello.pixeldungeonrescue.effects.particles.SparkParticle;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class StoneOfShock extends Runestone {
	
	{
		image = ItemSpriteSheet.STONE_SHOCK;
	}
	
	@Override
	protected void activate(int cell) {
		
		Sample.INSTANCE.play( Assets.SND_LIGHTNING );
		
		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		int hits = 0;
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Char n = Actor.findChar(i);
				if (n != null) {
					arcs.add(new Lightning.Arc(cell, n.sprite.center()));
					Buff.prolong(n, Paralysis.class, 1f);
					hits++;
				}
			}
		}
		
		CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
		
		if (hits > 0) {
			curUser.sprite.parent.addToFront( new Lightning( arcs, null ) );
			curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
			Sample.INSTANCE.play( Assets.SND_LIGHTNING );
			
			curUser.belongings.charge(1f + hits);
		}
	
	}
}
