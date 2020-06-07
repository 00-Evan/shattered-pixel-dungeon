/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.levels.rooms.special;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.Statistics;
import net.casiello.pixeldungeonrescue.actors.mobs.DemonSpawner;
import net.casiello.pixeldungeonrescue.levels.Level;
import net.casiello.pixeldungeonrescue.levels.Terrain;
import net.casiello.pixeldungeonrescue.levels.painters.Painter;
import net.casiello.pixeldungeonrescue.levels.rooms.Room;
import net.casiello.pixeldungeonrescue.levels.rooms.standard.EntranceRoom;
import net.casiello.pixeldungeonrescue.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class DemonSpawnerRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		int cx = c.x;
		int cy = c.y;

		Door door = entrance();
		door.set(Door.Type.UNLOCKED);

		DemonSpawner spawner = new DemonSpawner();
		spawner.pos = cx + cy * level.width();
		level.mobs.add( spawner );

		CustomFloor vis = new CustomFloor();
		vis.setRect(left+1, top+1, width()-2, height()-2);
		level.customTiles.add(vis);

	}

	@Override
	public boolean connect(Room room) {
		//cannot connect to entrance, otherwise works normally
		if (room instanceof EntranceRoom) return false;
		else                              return super.connect(room);
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	public static class CustomFloor extends CustomTilemap {

		{
			texture = Assets.HALLS_SP;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int cell = tileX + tileY * Dungeon.level.width();
			int[] map = Dungeon.level.map;
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i % tileW == 0){
					cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
				}

				if (Dungeon.level.findMob(cell) instanceof DemonSpawner){
					data[i-1] = 5 + 4*8;
					data[i] = 6 + 4*8;
					data[i+1] = 7 + 4*8;
					i++;
					cell++;
				} else if (map[cell] == Terrain.EMPTY_DECO) {
					if (Statistics.amuletObtained){
						data[i] = 31;
					} else {
						data[i] = 27;
					}
				} else {
					data[i] = 19;
				}

				cell++;
			}
			v.map( data, tileW );
			return v;
		}

	}
}
