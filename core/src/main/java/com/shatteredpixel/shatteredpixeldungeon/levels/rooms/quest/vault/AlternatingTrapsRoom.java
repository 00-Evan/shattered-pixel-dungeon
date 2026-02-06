package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class AlternatingTrapsRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		int cell;
		boolean alternate = false;
		for (int x = left+1; x <= right-1; x++){

			for (int y = top+1; y <= bottom-1; y++){
				cell = x + y*level.width();

				VaultLevel.VaultFlameTrap.setupTrap(level, cell, alternate ? 2 : 1, 2);
				alternate = !alternate;
			}
		}

	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

}
