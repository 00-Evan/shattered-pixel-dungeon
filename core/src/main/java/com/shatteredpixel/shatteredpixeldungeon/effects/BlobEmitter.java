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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

public class BlobEmitter extends Emitter {
	
	private Blob blob;
	
	public BlobEmitter( Blob blob ) {
		
		super();
		
		this.blob = blob;
		blob.use( this );
	}

	public RectF bound = new RectF(0, 0, 1, 1);
	
	@Override
	protected void emit( int index ) {
		
		if (blob.volume <= 0) {
			return;
		}

		if (blob.area.isEmpty())
			blob.setupArea();
		
		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;

		int cell;
		for (int i = blob.area.left; i < blob.area.right; i++) {
			for (int j = blob.area.top; j < blob.area.bottom; j++) {
				cell = i + j*Dungeon.level.width();
				if (cell < Dungeon.level.heroFOV.length
						&& (Dungeon.level.heroFOV[cell] || blob.alwaysVisible)
						&& map[cell] > 0) {
					float x = (i + Random.Float(bound.left, bound.right)) * size;
					float y = (j + Random.Float(bound.top, bound.bottom)) * size;
					factory.emit(this, index, x, y);
				}
			}
		}
	}
}
