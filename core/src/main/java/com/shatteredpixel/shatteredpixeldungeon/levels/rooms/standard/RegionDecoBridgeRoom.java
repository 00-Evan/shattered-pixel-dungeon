/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

//doesn't look much like a bridge, but can easily use it internally
public class RegionDecoBridgeRoom extends StandardBridgeRoom {

	//can be large because the line breaks the space up
	public float[] sizeCatProbs(){
		return new float[]{2, 1, 0};
	}

	protected int maxBridgeWidth( int roomDimension ) {
		return 1;
	}

	protected int spaceTile(){
		return Terrain.REGION_DECO_ALT;
	}

	@Override
	protected int bridgeTile() {
		return Terrain.EMPTY_SP;
	}
}
