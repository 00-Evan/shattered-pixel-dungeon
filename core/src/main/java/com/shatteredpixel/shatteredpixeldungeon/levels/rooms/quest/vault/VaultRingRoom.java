package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultRingRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, this, 4, Terrain.WALL);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		VaultRat rat = new VaultRat();
		do {
			rat.pos = level.pointToCell(random(1));
		} while (level.solid[rat.pos]);

		if (Random.Int(2) == 0) {
			rat.wanderPositions = new int[]{
					level.pointToCell(new Point(left+2, top+2)),
					level.pointToCell(new Point(right-2, top+2)),
					level.pointToCell(new Point(right-2, bottom-2)),
					level.pointToCell(new Point(left+2, bottom-2))
			};
		} else {
			rat.wanderPositions = new int[]{
					level.pointToCell(new Point(left+2, bottom-2)),
					level.pointToCell(new Point(right-2, bottom-2)),
					level.pointToCell(new Point(right-2, top+2)),
					level.pointToCell(new Point(left+2, top+2))
			};
		}
		rat.wanderPosIdx = Random.Int(4);
		rat.state = rat.WANDERING;
		level.mobs.add(rat);
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
}
