package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultSentry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

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

		Painter.set( level, center(), Terrain.PEDESTAL);

		VaultSentry sentry = new VaultSentry();
		sentry.pos = level.pointToCell(center());

		sentry.scanLength = 4.49f;


		int w = level.width();

		switch (Random.Int(3)){
			case 0:
				sentry.scanWidth = 90f;

				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-1},
						new int[]{sentry.pos-1-w},
						new int[]{sentry.pos-w},
						new int[]{sentry.pos+1-w},
						new int[]{sentry.pos+1},
						new int[]{sentry.pos+1+w},
						new int[]{sentry.pos+w},
						new int[]{sentry.pos+w-1},
				};
			case 1:
				sentry.scanWidth = 45f;
				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-2, sentry.pos+2},
						new int[]{sentry.pos-2-level.width(), sentry.pos+2+level.width()},
						new int[]{sentry.pos-2-2*level.width(), sentry.pos+2+2*level.width()},
						new int[]{sentry.pos-1-2*level.width(), sentry.pos+1+2*level.width()},
						new int[]{sentry.pos-2*level.width(), sentry.pos+2*level.width()},
						new int[]{sentry.pos+1-2*level.width(), sentry.pos-1+2*level.width()},
						new int[]{sentry.pos+2-2*level.width(), sentry.pos-2+2*level.width()},
						new int[]{sentry.pos+2-level.width(), sentry.pos-2+level.width()},
				};
				break;
			case 2:
				sentry.scanWidth = 22.5f;

				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-3, sentry.pos-3*w, sentry.pos+3, sentry.pos+3*w},
						new int[]{sentry.pos-3-1*w, sentry.pos+1-3*w, sentry.pos+3+1*w, sentry.pos-1+3*w},
						new int[]{sentry.pos-3-2*w, sentry.pos+2-3*w, sentry.pos+3+2*w, sentry.pos-2+3*w},
						new int[]{sentry.pos-3-3*w, sentry.pos+3-3*w, sentry.pos+3+3*w, sentry.pos-3+3*w},
						new int[]{sentry.pos-2-3*w, sentry.pos+3-2*w, sentry.pos+2+3*w, sentry.pos-3+2*w},
						new int[]{sentry.pos-1-3*w, sentry.pos+3-1*w, sentry.pos+1+3*w, sentry.pos-3+1*w},
				};
				break;
		}

		level.mobs.add(sentry);

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
