/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GeyserTrap extends Trap {

	{
		color = TEAL;
		shape = DIAMOND;
	}

	public int centerKnockBackDirection = -1;
	public Class source = getClass();

	@Override
	public void activate() {
		Splash.at( DungeonTilemap.tileCenterToWorld( pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
		Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);

		Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] == 2 && Random.Int(3) > 0){
				Dungeon.level.setCellToWater(true, i);
				if (fire != null){
					fire.clear(i);
				}
			} else if (PathFinder.distance[i] < 2){
				Dungeon.level.setCellToWater(true, i);
				if (fire != null){
					fire.clear(i);
				}
			}
		}

		for (int i : PathFinder.NEIGHBOURS8){
			Char ch = Actor.findChar(pos + i);
			if (ch != null){
				//trace a ballistica to our target (which will also extend past them)
				Ballistica trajectory = new Ballistica(pos, ch.pos, Ballistica.STOP_TARGET);
				//trim it to just be the part that goes past them
				trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(ch, trajectory, 2, true, true, source);
			}
		}

		Char ch = Actor.findChar(pos);
		if (ch != null){
			int targetpos = -1;
			if (centerKnockBackDirection != -1){
				targetpos = centerKnockBackDirection;
			} else if (ch == Dungeon.hero){
				//if it is the hero, random direction that isn't into a hazard
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int i : PathFinder.NEIGHBOURS8){
					//add as a candidate if both cells on the trajectory are safe
					if (!Dungeon.level.avoid[pos + i] && !Dungeon.level.avoid[pos + i + i]){
						candidates.add(pos + i);
					}
				}
				if (!candidates.isEmpty()){
					targetpos = Random.element(candidates);
				}
			} else {
				//random direction if it isn't the hero
				targetpos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			}
			if (targetpos != -1){
				//trace a ballistica in the direction of our target
				Ballistica trajectory = new Ballistica(pos, targetpos, Ballistica.MAGIC_BOLT);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(ch, trajectory, 2, true, true, source);
			}
		}
	}
}
