/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;


public class WallBlockingTilemap extends Tilemap {

	public static final int SIZE = 16;

	private static final int BLOCK_NONE     = 0;
	private static final int BLOCK_RIGHT    = 1;
	private static final int BLOCK_LEFT     = 2;
	private static final int BLOCK_ALL      = 3;

	public WallBlockingTilemap() {
		super("wall_blocking.png", new TextureFilm( "wall_blocking.png", SIZE, SIZE ) );
		map( new int[Dungeon.level.length()], Dungeon.level.width());
	}

	@Override
	public synchronized void updateMap() {
		super.updateMap();
		for (int i = 0; i < data.length; i++)
			updateMapCell(i);
	}

	@Override
	public synchronized void updateMapCell(int cell) {
		int prev = data[cell];
		int curr;

		//no point in blocking tiles that are already obscured by fog
		if (fogHidden(cell) && fogHidden(cell - mapWidth)){
			curr = BLOCK_NONE;

		} else if (DungeonTileSheet.wallStitcheable.contains(Dungeon.level.map[cell])) {
			if (cell + mapWidth < Dungeon.level.map.length) {
				if (!DungeonTileSheet.wallStitcheable.contains(Dungeon.level.map[cell + mapWidth])) {
					if (fogHidden(cell + mapWidth)) {
						curr = BLOCK_ALL;
					} else {
						curr = BLOCK_NONE;
					}
				} else {
					curr = BLOCK_NONE;

					if ((cell + 1) % mapWidth != 0) {

						if (!DungeonTileSheet.wallStitcheable.contains(Dungeon.level.map[cell + 1])){
							if (fogHidden(cell + 1)) {
								curr += 1;
							}
						} else {
							if (fogHidden(cell + 1 + mapWidth)){
								curr += 1;
							}
						}

					}

					if (cell % mapWidth != 0) {

						if (!DungeonTileSheet.wallStitcheable.contains(Dungeon.level.map[cell - 1])){
							if (fogHidden(cell - 1)) {
								curr += 2;
							}
						} else {
							if (fogHidden(cell - 1 + mapWidth)){
								curr += 2;
							}
						}

					}

				}

			} else {
				curr = BLOCK_NONE;

				if ((cell + 1) % mapWidth != 0 && fogHidden(cell + 1)) {
					curr += 1;
				}

				if (cell % mapWidth != 0 && fogHidden(cell - 1)) {
					curr += 2;
				}
			}

		} else {
			curr = BLOCK_NONE;
		}

		if (prev != curr){
			data[cell] = curr;
			super.updateMapCell(cell);
		}
	}

	private boolean fogHidden(int cell){
		if (cell < 0 || cell >= Dungeon.level.length()) return false;
		return (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]);
	}

	public synchronized void updateArea(int x, int y, int w, int h) {
		int cell;
		for (int i = x; i <= x+w; i++){
			for (int j = y; j <= y+h; j++){
				cell = i + j*mapWidth;
				if (cell < data.length)
					updateMapCell(cell);
			}
		}
	}

	@Override
	protected boolean needsRender(int pos) {
		return data[pos] != BLOCK_NONE;
	}
}
