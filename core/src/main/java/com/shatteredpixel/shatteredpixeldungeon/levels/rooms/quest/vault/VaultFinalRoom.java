package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.Point;

public class VaultFinalRoom extends SpecialRoom {

	@Override
	public int minWidth() {
		return 21;
	}

	@Override
	public int maxWidth() {
		return 21;
	}

	@Override
	public int minHeight() {
		return 21;
	}

	@Override
	public int maxHeight(){
		return 21;
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.REGION_DECO_ALT );
		Painter.fill( level, this, 2 , Terrain.EMPTY_SP );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY_SP);
		}
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return false;
	}


}
