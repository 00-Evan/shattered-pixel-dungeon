package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class VaultCircleRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 2, Terrain.EMPTY );

		Painter.fill( level, this, 4, 1, 4, 1, Terrain.EMPTY );
		Painter.fill( level, this, 1, 4, 1, 4, Terrain.EMPTY );

		Painter.set( level, center(), Terrain.STATUE);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 4, Terrain.EMPTY);
		}
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}


}
