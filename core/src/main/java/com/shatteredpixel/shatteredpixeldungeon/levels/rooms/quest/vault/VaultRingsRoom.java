package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultRingsRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, left+2, top+2, 3, 3, Terrain.WALL);
		Painter.fill(level, right-4, top+2, 3, 3, Terrain.WALL);
		Painter.fill(level, left+2, bottom-4, 3, 3, Terrain.WALL);
		Painter.fill(level, right-4, bottom-4, 3, 3, Terrain.WALL);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		VaultRat rat = new VaultRat();
		do {
			rat.pos = level.pointToCell(random(1));
		} while (level.solid[rat.pos]);
		rat.state = rat.WANDERING;
		level.mobs.add(rat);

		rat.wanderPositions = new int[]{
				level.pointToCell(new Point(left+1, top+1)),
				level.pointToCell(new Point(left+1, top+5)),
				level.pointToCell(new Point(left+1, top+9)),
				level.pointToCell(new Point(left+5, top+1)),
				level.pointToCell(new Point(left+5, top+5)),
				level.pointToCell(new Point(left+5, top+9)),
				level.pointToCell(new Point(left+9, top+1)),
				level.pointToCell(new Point(left+9, top+5)),
				level.pointToCell(new Point(left+9, top+9))
		};
		Random.shuffle(rat.wanderPositions);

	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

}
