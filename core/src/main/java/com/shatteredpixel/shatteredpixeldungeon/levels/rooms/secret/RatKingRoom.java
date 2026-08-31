/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class RatKingRoom extends SecretRoom {
	
	@Override
	public boolean canConnect(Room r) {
		//never connects at the entrance
		return !(r instanceof SewerBossEntranceRoom) && super.canConnect(r);
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return false;
	}

	//force internal size to 5x5 for consistency of layout
	@Override
	public int minWidth() { return 7; }
	public int maxWidth() { return 7; }
	public int minHeight() { return 7; }
	public int maxHeight() { return 7; }
	
	public void paint(Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		Painter.fill( level, this, 2, Terrain.EMPTY_SP );
		
		Door entrance = entrance();
		entrance.set( Door.Type.HIDDEN );
		int door = level.pointToCell(entrance);

		int center = level.pointToCell(center());
		int w = level.width();

		int[] statuePositions = new int[]{
				center - 2 - 2*w,
				center - 2*w,
				center + 2 - 2*w,
				center + 2,
				center + 2 + 2*w,
				center + 2*w,
				center - 2 + 2*w,
				center -2,
		};

		for (int cell : statuePositions){
			if (level.distance(door, cell) >= 2) {
				Painter.set(level, cell, Terrain.CUSTOM_DECO);
			}
		}

		Carpet c = new Carpet();
		c.setRect(left+2, top+2, width()-4, height()-4);
		level.customTiles.add(c);

		RatKingRoomDeco deco = new RatKingRoomDeco();
		deco.setRect(left+1, top+1, width()-2, height()-2);
		level.customTiles.add(deco);

		RatKingStatues statues = new RatKingStatues();
		statues.setRect(left+1, top, width()-2, height());
		level.customTerrain.add(statues);

		StatueOverhang overhang = new StatueOverhang();
		overhang.setRect(left+1, top, width()-2, height());
		level.customWalls.add(overhang);

		RatKing king = new RatKing();
		king.pos = center;
		level.mobs.add( king );

		for (Point p : getPoints()){
			int cell = level.pointToCell(p);
			if (cell != center && (level.map[cell] == Terrain.EMPTY || level.map[cell] == Terrain.EMPTY_SP)){
				level.drop( new Gold( Random.IntRange( 5, 20 ) ), cell );
			}
		}
	}

	public static class RatKingRoomDeco extends CustomTilemap {

		{
			texture = Assets.Environment.RAT_KING_ROOM;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int i = 0;
			for (int y = 0; y < tileH; y++){
				int cell = tileX + (tileY+y)*Dungeon.level.width();
				for (int x = 0; x < tileW; x++){
					if (x == 2 && y == 2){
						//center, pillow
						data[i] = 3;
					} else if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO){
						//statue
						data[i] = 0;
					} else {
						data[i] = -1;
					}
					cell++;
					i++;
				}
			}
			v.map( data, tileW );
			return v;
		}

		@Override
		public Image image(int tileX, int tileY) {
			int cell = this.tileX+tileX + (this.tileY + tileY)*Dungeon.level.width();
			if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO){
				//custom visual for rat king statue examine
				//TODO should make a method for this if we have to do it with any frequency
				Image img = new Image(texture);
				img.frame(64, 0, 16, 16);
				return img;
			} else {
				return super.image(tileX, tileY);
			}
		}

		@Override
		public String name(int tileX, int tileY) {
			int cell = this.tileX+tileX + (this.tileY + tileY)*Dungeon.level.width();
			if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO){
				return Messages.get(this, "statue_name");
			} else if (tileX == 2 && tileY == 2){
				return Messages.get(this, "pillow_name");
			} else {
				return super.name(tileX, tileY);
			}
		}

		@Override
		public String desc(int tileX, int tileY) {
			int cell = this.tileX+tileX + (this.tileY + tileY)*Dungeon.level.width();
			if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO){
				return Messages.get(this, "statue_desc");
			} else if (tileX == 2 && tileY == 2){
				return Messages.get(this, "pillow_desc");
			} else {
				return super.desc(tileX, tileY);
			}
		}
	}

	public static class RatKingStatues extends CustomTilemap {

		{
			texture = Assets.Environment.RAT_KING_ROOM;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int i = 0;
			for (int y = 0; y < tileH; y++){
				int cell = tileX + (tileY+y)*Dungeon.level.width();
				for (int x = 0; x < tileW; x++){
					if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO){
						//statue
						data[i] = 1;
					} else {
						data[i] = -1;
					}
					cell++;
					i++;
				}
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class StatueOverhang extends CustomTilemap {

		{
			texture = Assets.Environment.RAT_KING_ROOM;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int i = 0;
			for (int y = 0; y < tileH; y++){
				int cell = tileX + (tileY+y)*Dungeon.level.width();
				for (int x = 0; x < tileW; x++){
					if (Dungeon.level.map[cell + Dungeon.level.width()] == Terrain.CUSTOM_DECO){
						//statue overhang
						data[i] = 2;
					} else {
						data[i] = -1;
					}
					cell++;
					i++;
				}
			}
			v.map( data, tileW );
			return v;
		}

	}
}
