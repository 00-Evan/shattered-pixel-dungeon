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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Alchemy extends Blob {

	protected int pos;
	
	@Override
	protected void evolve() {
		int cell;
		for (int i=area.top-1; i <= area.bottom; i++) {
			for (int j = area.left-1; j <= area.right; j++) {
				cell = j + i* Dungeon.level.width();
				if (Dungeon.level.insideMap(cell)) {
					off[cell] = cur[cell];

					//for pre-v1.1.0 saves, drops 1/4 the pot's old energy contents in crystals
					if (off[cell] >= 4){
						int n;
						do {
							n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
						} while (!Dungeon.level.passable[n] && !Dungeon.level.avoid[n]);
						Dungeon.level.drop( new EnergyCrystal((int)Math.ceil(off[cell]/4f)), n ).sprite.drop( cell );
						off[cell] = 1;
					}

					volume += off[cell];
					if (off[cell] > 0 && Dungeon.level.heroFOV[cell]){
						Notes.add( Notes.Landmark.ALCHEMY );
					}
				}
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.BUBBLE ), 0.33f, 0 );
	}

}
