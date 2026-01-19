package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultLongRoom extends StandardRoom {

	private boolean wide = Random.Int(2) == 0;

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 0, 1};
	}

	@Override
	public int minWidth() {
		return wide ? 21 : 11;
	}

	@Override
	public int maxWidth() {
		return minWidth();
	}

	@Override
	public int minHeight() {
		return wide ? 11: 21;
	}

	@Override
	public int maxHeight() {
		return minHeight();
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, this, 4, Terrain.WALL);

		if (wide){
			Painter.fill(level, this, 8, 4, 8, 4, Terrain.EMPTY);
		} else {
			Painter.fill(level, this, 4, 8, 4, 8, Terrain.EMPTY);
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

}
