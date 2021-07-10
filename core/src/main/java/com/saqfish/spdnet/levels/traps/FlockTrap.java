/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.levels.traps;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.mobs.npcs.Sheep;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class FlockTrap extends Trap {

	{
		color = WHITE;
		shape = WAVES;
	}


	@Override
	public void activate() {
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			Trap t;
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level.insideMap(i)
						&& Actor.findChar(i) == null
						&& !(Dungeon.level.pit[i])) {
					Sheep sheep = new Sheep();
					sheep.lifespan = Random.NormalIntRange( 4, 8 );
					sheep.pos = i;
					GameScene.add(sheep);
					CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
					//before the tile is pressed, directly trigger traps to avoid sfx spam
					if ((t = Dungeon.level.traps.get(i)) != null && t.active){
						t.disarm();
						t.reveal();
						t.activate();
					}
					Dungeon.level.occupyCell(sheep);
					Sample.INSTANCE.play(Assets.Sounds.PUFF);
					Sample.INSTANCE.play(Assets.Sounds.SHEEP);
				}
			}
		}
	}

}
