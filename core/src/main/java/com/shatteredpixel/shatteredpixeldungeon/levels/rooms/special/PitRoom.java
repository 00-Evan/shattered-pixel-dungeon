/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class PitRoom extends SpecialRoom {

	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		Door entrance = entrance();
		entrance.set( Door.Type.LOCKED );
		
		Point well = null;
		if (entrance.x == left) {
			well = new Point( right-1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
		} else if (entrance.x == right) {
			well = new Point( left+1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
		} else if (entrance.y == top) {
			well = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, bottom-1 );
		} else if (entrance.y == bottom) {
			well = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, top+1 );
		}
		Painter.set( level, well, Terrain.EMPTY_WELL );
		
		int remains = level.pointToCell(random());
		while (level.map[remains] == Terrain.EMPTY_WELL) {
			remains = level.pointToCell(random());
		}
		
		level.drop( new IronKey( Dungeon.depth ), remains ).type = Heap.Type.SKELETON;
		int loot = Random.Int( 3 );
		if (loot == 0) {
			level.drop( Generator.random( Generator.Category.RING ), remains );
		} else if (loot == 1) {
			level.drop( Generator.random( Generator.Category.ARTIFACT ), remains );
		} else {
			level.drop( Generator.random( Random.oneOf(
				Generator.Category.WEAPON,
				Generator.Category.ARMOR
			) ), remains );
		}
		
		int n = Random.IntRange( 1, 2 );
		for (int i=0; i < n; i++) {
			level.drop( prize( level ), remains );
		}
	}
	
	private static Item prize( Level level ) {
		
		if (Random.Int(2) != 0){
			Item prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}
		
		return Generator.random( Random.oneOf(
			Generator.Category.POTION,
			Generator.Category.SCROLL,
			Generator.Category.FOOD,
			Generator.Category.GOLD
		) );
	}
	
	@Override
	public boolean canPlaceTrap(Point p) {
		//the player is already weak after landing, and will likely need to kite the ghost.
		//having traps here just seems unfair
		return false;
	}
}
