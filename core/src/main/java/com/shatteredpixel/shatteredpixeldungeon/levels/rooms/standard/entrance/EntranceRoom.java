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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.GuidePage;
import com.shatteredpixel.shatteredpixeldungeon.items.journal.Guidebook;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit.CircleWallEntranceRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class EntranceRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		if (Dungeon.depth <= 2) {
			return false;
		} else {
			return super.canMerge(l, other, p, mergeTerrain);
		}
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		if (Dungeon.depth == 1) {
			return false;
		} else {
			return super.canPlaceTrap(p);
		}
	}

	public void paint(Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		int entrance;
		do {
			entrance = level.pointToCell(random(2));
		} while (level.findMob(entrance) != null);
		Painter.set( level, entrance, Terrain.ENTRANCE );

		if (Dungeon.depth == 1){
			level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.SURFACE));
		} else {
			level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
		}

		placeEarlyGuidePages(level, this);

	}

	public static void placeEarlyGuidePages(Level level, Room r){
		//use a separate generator here so meta progression doesn't affect levelgen
		Random.pushGenerator();

		//places the first guidebook page on floor 1
		if (Dungeon.depth == 1 &&
				(!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_INTRO) || SPDSettings.intro() )){
			int pos;
			do {
				//can't be on bottom row of tiles
				pos = level.pointToCell(new Point( Random.IntRange( r.left + 1, r.right - 1 ),
						Random.IntRange( r.top + 1, r.bottom - 2 )));
			} while (pos == level.entrance() || level.map[pos] == Terrain.REGION_DECO);
			level.drop( new Guidebook(), pos );
			Document.ADVENTURERS_GUIDE.deletePage(Document.GUIDE_INTRO);
		}

		//places the third guidebook page on floor 2
		if (Dungeon.depth == 2 && !Document.ADVENTURERS_GUIDE.isPageFound(Document.GUIDE_SEARCHING)){
			int pos;
			do {
				//can't be on bottom row of tiles
				pos = level.pointToCell(new Point( Random.IntRange( r.left + 1, r.right - 1 ),
						Random.IntRange( r.top + 1, r.bottom - 2 )));
			} while (pos == level.entrance() || level.map[pos] == Terrain.REGION_DECO);
			GuidePage p = new GuidePage();
			p.page(Document.GUIDE_SEARCHING);
			level.drop( p, pos );
		}

		Random.popGenerator();
	}

	private static ArrayList<Class<?extends StandardRoom>> rooms = new ArrayList<>();
	static {
		rooms.add(WaterBridgeEntranceRoom.class);
		rooms.add(RegionDecoPatchEntranceRoom.class);
		rooms.add(RingEntranceRoom.class);
		rooms.add(CircleBasinEntranceRoom.class);

		rooms.add(RegionDecoLineEntranceRoom.class);
		rooms.add(ChasmBridgeEntranceRoom.class);
		rooms.add(PillarsEntranceRoom.class);
		rooms.add(CellBlockEntranceRoom.class);

		rooms.add(CaveEntranceRoom.class);
		rooms.add(RegionDecoBridgeEntranceRoom.class);
		rooms.add(CavesFissureEntranceRoom.class);
		rooms.add(CircleWallEntranceRoom.class);

		rooms.add(HallwayEntranceRoom.class);
		rooms.add(StatuesEntranceRoom.class);
		rooms.add(LibraryHallEntranceRoom.class);
		rooms.add(LibraryRingEntranceRoom.class);

		rooms.add(RegionDecoPatchEntranceRoom.class);
		rooms.add(RuinsEntranceRoom.class);
		rooms.add(ChasmEntranceRoom.class);
		rooms.add(RitualEntranceRoom.class);
	}

	private static float[][] chances = new float[27][];
	static {
		//first 2 floors only use simpler entrance rooms
		chances[1] =  new float[]{4,3,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
		chances[2] =  chances[1];
		chances[3] =  new float[]{4,3,2,1, 0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0};
		chances[5] =  chances[4] = chances[3];

		chances[6] = new float[]{0,0,0,0,  4,3,2,1, 0,0,0,0, 0,0,0,0, 0,0,0,0};
		chances[10] = chances[9] = chances[8] = chances[7] = chances[6];

		chances[11] = new float[]{0,0,0,0, 0,0,0,0, 4,3,2,1, 0,0,0,0, 0,0,0,0};
		chances[15] = chances[14] = chances[13] = chances[12] = chances[11];

		chances[16] = new float[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 4,3,2,1, 0,0,0,0};
		chances[20] = chances[19] = chances[18] = chances[17] = chances[16];

		chances[21] = new float[]{0,0,0,0, 0,0,0,0, 0,0,0,0, 0,0,0,0, 4,3,2,1};
		chances[26] = chances[25] = chances[24] = chances[23] = chances[22] = chances[21];
	}

	public static StandardRoom createEntrance(){
		return Reflection.newInstance(rooms.get(Random.chances(chances[Dungeon.depth])));
	}

}
