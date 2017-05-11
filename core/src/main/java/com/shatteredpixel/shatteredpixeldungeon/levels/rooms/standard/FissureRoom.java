package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class FissureRoom extends StandardRoom {
	
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{6, 3, 1};
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		if (square() <= 25){
			//just fill in one tile if the room is tiny
			Point p = center();
			Painter.set( level, p.x, p.y, Terrain.CHASM);
			
		} else {
			int smallestDim = Math.min(width(), height());
			int floorW = (int)Math.sqrt(smallestDim);
			//chance for a tile at the edge of the floor to remain a floor tile
			float edgeFloorChance = (float)Math.sqrt(smallestDim) % 1;
			//the wider the floor the more edge chances tend toward 50%
			edgeFloorChance = (edgeFloorChance + (floorW-1)*0.5f) / (float)floorW;
			
			for (int i=top + 2; i <= bottom - 2; i++) {
				for (int j=left + 2; j <= right - 2; j++) {
					int v = Math.min( i - top, bottom - i );
					int h = Math.min( j - left, right - j );
					if (Math.min( v, h ) > floorW
							|| (Math.min( v, h ) == floorW && Random.Float() > edgeFloorChance)) {
						Painter.set( level, j, i, Terrain.CHASM );
					}
				}
			}
		}
	}
	
}
