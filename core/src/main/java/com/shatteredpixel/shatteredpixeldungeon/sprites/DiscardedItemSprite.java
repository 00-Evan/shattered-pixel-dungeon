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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;

public class DiscardedItemSprite extends ItemSprite {
	
	@Override
	public void drop() {
		scale.set( 1 );
		am = 1;
		if (emitter != null) emitter.killAndErase();

		origin.set( width/2, height - DungeonTilemap.SIZE/2);
		angularSpeed = 720;
	}
	
	@Override
	public void update() {
		
		super.update();
		
		scale.set( scale.x -= Game.elapsed );
		y += 12 * Game.elapsed;
		if ((am -= Game.elapsed) <= 0) {
			remove();
		}
	}
}
