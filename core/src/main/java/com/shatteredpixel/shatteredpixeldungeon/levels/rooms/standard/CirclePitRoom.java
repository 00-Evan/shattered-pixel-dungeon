package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

public class CirclePitRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(8, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(8, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 2, 1};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Painter.fillEllipse( level, this, 1 , Terrain.EMPTY );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			if (door.x == left || door.x == right){
				Painter.drawInside(level, this, door, width()/2, Terrain.EMPTY);
			} else {
				Painter.drawInside(level, this, door, height()/2, Terrain.EMPTY);
			}
		}

		Painter.fillEllipse( level, this, 3 , Terrain.CHASM );
	}
}
