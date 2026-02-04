package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class VaultEnemyCenterRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill( level, this, 2 , Terrain.WALL );
		Painter.fill( level, this, 3 , Terrain.EMPTY );
		Painter.drawLine( level, new Point(left+1, top+3), new Point(right-1, top+3), Terrain.EMPTY);
		Painter.drawLine( level, new Point(left+1, bottom-3), new Point(right-1, bottom-3), Terrain.EMPTY);
		Painter.drawLine( level, new Point(left+3, top+1), new Point(left+3, bottom-1), Terrain.EMPTY);
		Painter.drawLine( level, new Point(right-3, top+1), new Point(right-3, bottom-1), Terrain.EMPTY);
		//TODO maybe better without corner pillars? they sorta just bait you...
		// Need to think a little more about layout here

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		VaultRat rat = new VaultRat();
		do {
			rat.pos = level.pointToCell(center());
		} while (level.solid[rat.pos]);
		rat.state = rat.WANDERING;
		level.mobs.add(rat);

	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
}
