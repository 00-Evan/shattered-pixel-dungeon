package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class VaultQuadrantsRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		Painter.drawInside( level, this, new Point(left, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(right, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, top), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, bottom), 3, Terrain.WALL);

		Painter.set( level, c, Terrain.STATUE);

		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return c.x != p.x && c.y != p.y && super.canConnect(p);
	}

}
