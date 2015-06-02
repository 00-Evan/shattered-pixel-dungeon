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
package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.watabou.noosa.Camera;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LightningTrap extends Trap {

	{
		name = "lightning trap";
		image = 5;
	}

	@Override
	public void activate() {

		Char ch = Actor.findChar( pos );

		if (ch != null) {
			ch.damage( Math.max( 1, Random.Int( ch.HP / 3, 2 * ch.HP / 3 ) ), LIGHTNING );
			if (ch == Dungeon.hero) {

				Camera.main.shake( 2, 0.3f );

				if (!ch.isAlive()) {
					Dungeon.fail( Utils.format( ResultDescriptions.TRAP, name ) );
					GLog.n( "You were killed by a discharge of a lightning trap..." );
				}
			}

			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			arcs.add(new Lightning.Arc(pos - Level.WIDTH, pos + Level.WIDTH));
			arcs.add(new Lightning.Arc(pos - 1, pos + 1));

			ch.sprite.parent.add( new Lightning( arcs, null ) );
		}

		CellEmitter.center( pos ).burst( SparkParticle.FACTORY, Random.IntRange( 3, 4 ) );
	}

	//FIXME: this is bad, handle when you rework resistances, make into a category
	public static final Electricity LIGHTNING = new Electricity();
	public static class Electricity {
	}
}
