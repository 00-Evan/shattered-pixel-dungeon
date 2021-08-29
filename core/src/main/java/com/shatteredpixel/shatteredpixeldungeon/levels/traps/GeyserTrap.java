package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
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

	@Override
	public void activate() {
		Splash.at( DungeonTilemap.tileCenterToWorld( pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
		Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);

		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Dungeon.level.setCellToWater(true, i);
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
				WandOfBlastWave.throwChar(ch, trajectory, 2, true);
			}
		}

		Char ch = Actor.findChar(pos);
		if (ch != null){
			int targetpos = -1;
			if (ch == Dungeon.hero){
				//if it is the hero, random direction that isn't into a hazard
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int i : PathFinder.NEIGHBOURS8){
					//add as a candidate if both cells on the trajectory are safe
					if (!Dungeon.level.avoid[pos + i] && !Dungeon.level.avoid[pos + i + i]){
						candidates.add(pos + i + i);
					}
				}
				if (!candidates.isEmpty()){
					targetpos = Random.element(candidates);
				}
			} else {
				//random direction if it isn't the hero
				targetpos = pos + 2*PathFinder.NEIGHBOURS8[Random.Int(8)];
			}
			if (targetpos != -1){
				//trace a ballistica in the direction of our target
				Ballistica trajectory = new Ballistica(pos, targetpos, Ballistica.PROJECTILE);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(ch, trajectory, 2, true);
			}
		}
	}
}
