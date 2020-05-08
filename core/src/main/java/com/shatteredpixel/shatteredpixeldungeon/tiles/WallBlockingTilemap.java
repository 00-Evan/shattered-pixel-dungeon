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

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewHallsBossLevel;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;


public class WallBlockingTilemap extends Tilemap {

	public static final int SIZE = 16;

	private static final int CLEARED        = -2;
	private static final int BLOCK_NONE     = -1;
	private static final int BLOCK_RIGHT    = 0;
	private static final int BLOCK_LEFT     = 1;
	private static final int BLOCK_ALL      = 2;
	private static final int BLOCK_BELOW    = 3;

	public WallBlockingTilemap() {
		super(Assets.Environment.WALL_BLOCKING, new TextureFilm( Assets.Environment.WALL_BLOCKING, SIZE, SIZE ) );
		map( new int[Dungeon.level.length()], Dungeon.level.width());
	}

	@Override
	public synchronized void updateMap() {
		super.updateMap();
		data = new int[size]; //clears all values, including cleared tiles
		
		for (int cell = 0; cell < data.length; cell++) {
			//force all top/bottom row, and none-discoverable cells to cleared
			if (!Dungeon.level.discoverable[cell]
					|| (cell - mapWidth) <= 0
					|| (cell + mapWidth) >= size){
				data[cell] = CLEARED;
			} else {
				updateMapCell(cell);
			}
		}
	}

	private int curr;
	
	@Override
	public synchronized void updateMapCell(int cell) {

		//FIXME this is to address the wall blocking looking odd on the new yog floor.
		// The true solution is to improve the fog of war so the blockers aren't necessary.
		if (Dungeon.level instanceof NewHallsBossLevel){
			data[cell] = CLEARED;
			super.updateMapCell(cell);
			return;
		}
		
		//TODO should doors be considered? currently the blocking is a bit permissive around doors

		//non-wall tiles
		if (!wall(cell)) {

			//clear empty floor tiles and cells which are visible
			if (!fogHidden(cell) || !wall(cell + mapWidth)) {
				curr = CLEARED;

			//block wall overhang if:
			//- There are cells 2x below
			//- The cell below is a wall and visible
			//- All of left, below-left, right, below-right is either a wall or hidden
			} else if ( !fogHidden(cell + mapWidth)
					&& (fogHidden(cell - 1) || wall(cell - 1))
					&& (fogHidden(cell + 1) || wall(cell + 1))
					&& (fogHidden(cell - 1 + mapWidth) || wall(cell - 1 + mapWidth))
					&& (fogHidden(cell + 1 + mapWidth) || wall(cell + 1 + mapWidth))) {
				curr = BLOCK_BELOW;

			} else {
				curr = BLOCK_NONE;
			}

		//wall tiles
		} else {

			//camera-facing wall tiles
			if (!wall(cell + mapWidth)) {

				//Block a camera-facing wall if:
				//- the cell above, above-left, or above-right is not a wall, visible, and has a wall below
				//- none of the remaining 5 neighbour cells are both not a wall and visible
				
				//if all 3 above are wall we can shortcut and just clear the cell
				if (wall(cell - 1 - mapWidth) && wall(cell - mapWidth) && wall(cell + 1 - mapWidth)){
					curr = CLEARED;
					
				} else if ((!wall(cell - 1 - mapWidth) && !fogHidden(cell - 1 - mapWidth) && wall(cell - 1)) ||
						(!wall(cell - mapWidth) && !fogHidden(cell - mapWidth)) ||
						(!wall(cell + 1 - mapWidth) && !fogHidden(cell + 1 - mapWidth) && wall(cell+1))){
					
					if ( !fogHidden( cell + mapWidth) ||
							(!wall(cell - 1) && !fogHidden(cell - 1)) ||
							(!wall(cell - 1 + mapWidth) && !fogHidden(cell - 1 + mapWidth)) ||
							(!wall(cell + 1) && !fogHidden(cell + 1)) ||
							(!wall(cell + 1 + mapWidth) && !fogHidden(cell + 1 + mapWidth))){
						curr = CLEARED;
					} else {
						curr = BLOCK_ALL;
					}
					
				} else {
					curr = BLOCK_NONE;
				}

			//internal wall tiles
			} else {
				
				//Block the side of an internal wall if:
				//- the cell above, below, or the cell itself is visible
				//and all of the following are NOT true:
				//- the cell has no neighbours on that side
				//- the top-side neighbour is visible and the side neighbour isn't a wall.
				//- the side neighbour is both not a wall and visible
				//- the bottom-side neighbour is both not a wall and visible

				curr = BLOCK_NONE;
				
				if (!fogHidden(cell - mapWidth)
						|| !fogHidden(cell)
						|| !fogHidden(cell + mapWidth)) {
					
					//right side
					if ( ((cell + 1) % mapWidth == 0) ||
							(!wall(cell + 1) && !fogHidden(cell + 1 - mapWidth)) ||
							(!wall(cell + 1) && !fogHidden(cell + 1)) ||
							(!wall(cell + 1 + mapWidth) && !fogHidden(cell + 1 + mapWidth))
							){
						//do nothing
					} else {
						curr += 1;
					}
					
					//left side
					if ( (cell  % mapWidth == 0) ||
							(!wall(cell - 1) && !fogHidden(cell - 1 - mapWidth)) ||
							(!wall(cell - 1) && !fogHidden(cell - 1)) ||
							(!wall(cell - 1 + mapWidth) && !fogHidden(cell - 1 + mapWidth))
							){
						//do nothing
					} else {
						curr += 2;
					}
					
					if (curr == BLOCK_NONE) {
						curr = CLEARED;
					}
				}

			}

		}

		if (data[cell] != curr){
			data[cell] = curr;
			super.updateMapCell(cell);
		}
	}

	private boolean fogHidden(int cell){
		if (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]) {
			return true;
		} else if (wall(cell) && cell + mapWidth < size && !wall(cell + mapWidth) &&
				!Dungeon.level.visited[cell + mapWidth] && !Dungeon.level.mapped[cell + mapWidth]) {
			return true;
		}
		return false;
	}

	private boolean wall(int cell) {
		return DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell]);
	}

	private boolean door(int cell) {
		return DungeonTileSheet.doorTile(Dungeon.level.map[cell]);
	}
	
	public synchronized void updateArea(int cell, int radius){
		int l = cell%mapWidth - radius;
		int t = cell/mapWidth - radius;
		int r = cell%mapWidth + radius;
		int b = cell/mapWidth + radius;
		updateArea(
				Math.max(0, l),
				Math.max(0, t),
				Math.min(mapWidth-1, r - l),
				Math.min(mapHeight-1, b - t)
		);
	}

	public synchronized void updateArea(int x, int y, int w, int h) {
		int cell;
		for (int i = x; i <= x+w; i++){
			for (int j = y; j <= y+h; j++){
				cell = i + j*mapWidth;
				if (cell < data.length && data[cell] != CLEARED)
					updateMapCell(cell);
			}
		}
	}
	
}
