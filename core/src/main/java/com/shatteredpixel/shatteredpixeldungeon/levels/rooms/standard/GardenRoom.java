/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class GardenRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{3, 1, 0};
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.GRASS );
		Painter.fill( level, this, 2, Terrain.HIGH_GRASS );
		
		if (Math.min(width(), height()) >= 7){
			Painter.fill( level, this, 3, Terrain.GRASS );
		}
		
		Point center = center();
		
		//place at least 2 plants for rooms with at least 9 in one dimensions
		if (Math.max(width(), height()) >= 9){
			
			//place 4 plants for very large rooms
			if (Math.min(width(), height()) >= 11) {
				Painter.drawLine(level, new Point(left+2, center.y), new Point(right-2, center.y), Terrain.HIGH_GRASS);
				Painter.drawLine(level, new Point(center.x, top+2), new Point(center.x, bottom-2), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y+1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y+1)));
			
			//place 2 plants otherwise
			//left/right
			} else if (width() > height() || (width() == height() && Random.Int(2) == 0)){
				Painter.drawLine(level, new Point(center.x, top+2), new Point(center.x, bottom-2), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x-1, center.y)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x+1, center.y)));
			
			//top/bottom
			} else {
				Painter.drawLine(level, new Point(left+2, center.y), new Point(right-2, center.y), Terrain.HIGH_GRASS);
				level.plant( randomSeed(), level.pointToCell(new Point(center.x, center.y-1)));
				level.plant( randomSeed(), level.pointToCell(new Point(center.x, center.y+1)));
			
			}
			
		//place just one plant for smaller sized rooms
		} else {
			level.plant( randomSeed(), level.pointToCell(center));
		}
		
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		super.paint(level);
	}
	
	//TODO: sungrass, blandfruit, and starflower seeds can grow here, perhaps that's too good.
	private static Plant.Seed randomSeed(){
		return (Plant.Seed) Generator.random(Generator.Category.SEED);
	}
}
