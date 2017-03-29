package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;

public class SpecialRoom extends Room {
	
	@Override
	public int minDimension() {
		return 5;
	}
	
	@Override
	public int maxDimension() {
		return 10;
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
}
