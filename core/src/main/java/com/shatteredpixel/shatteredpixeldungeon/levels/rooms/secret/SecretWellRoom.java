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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SecretWellRoom extends SecretRoom {
	
	private static final Class<?>[] WATERS =
			{WaterOfAwareness.class, WaterOfHealth.class};
	
	@Override
	public boolean canConnect(Point p) {
		//refuses connections next to corners
		return super.canConnect(p) && ((p.x > left+1 && p.x < right-1) || (p.y > top+1 && p.y < bottom-1));
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Point door = entrance();
		Point well;
		if (door.x == left){
			well = new Point(right-2, door.y);
		} else if (door.x == right){
			well = new Point(left+2, door.y);
		} else if (door.y == top){
			well = new Point(door.x, bottom-2);
		} else {
			well = new Point(door.x, top+2);
		}
		
		Painter.fill(level, well.x-1, well.y-1, 3, 3, Terrain.CHASM);
		Painter.drawLine(level, door, well, Terrain.EMPTY);
		
		Painter.set( level, well, Terrain.WELL );
		
		@SuppressWarnings("unchecked")
		Class<? extends WellWater> waterClass = (Class<? extends WellWater>) Random.element( WATERS );
		
		WellWater.seed(well.x + level.width() * well.y, 1, waterClass, level);
		
		entrance().set( Door.Type.HIDDEN );
	}
}
