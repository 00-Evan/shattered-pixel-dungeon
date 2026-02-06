package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultSentry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class VaultCrossRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Painter.fill( level, this, 4, 1, 4, 1, Terrain.EMPTY );
		Painter.fill( level, this, 1, 4, 1, 4, Terrain.EMPTY );

		Painter.set( level, center(), Terrain.PEDESTAL);

		//TODO only shapes for sides with doors?

		VaultSentry sentry = new VaultSentry();
		sentry.pos = level.pointToCell(center());

		sentry.scanLength = 4;
		sentry.scanWidth = 90;

		sentry.scanDirs = new int[][]{
				new int[]{sentry.pos-1},
				new int[]{sentry.pos-level.width()},
				new int[]{sentry.pos+1},
				new int[]{sentry.pos+level.width()},
		};

		level.mobs.add(sentry);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) <= 1 || Math.abs(c.y - p.y) <= 1) && super.canConnect(p);
	}
}
