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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

//This room type uses the patch system to fill itself in in some manner
//it's still up to the specific room to implement paint, but utility methods are provided
public abstract class PatchRoom extends StandardRoom {
	
	protected boolean[] patch;
	
	protected void setupPatch(Level level, float fill, int clustering, boolean ensurePath){
		
		if (ensurePath){
			PathFinder.setMapSize(width()-2, height()-2);
			boolean valid;
			do {
				patch = Patch.generate(width()-2, height()-2, fill, clustering, true);
				int startPoint = 0;
				for (Door door : connected.values()) {
					if (door.x == left) {
						startPoint = xyToPatchCoords(door.x + 1, door.y);
						patch[xyToPatchCoords(door.x + 1, door.y)] = false;
						patch[xyToPatchCoords(door.x + 2, door.y)] = false;
					} else if (door.x == right) {
						startPoint = xyToPatchCoords(door.x - 1, door.y);
						patch[xyToPatchCoords(door.x - 1, door.y)] = false;
						patch[xyToPatchCoords(door.x - 2, door.y)] = false;
					} else if (door.y == top) {
						startPoint = xyToPatchCoords(door.x, door.y + 1);
						patch[xyToPatchCoords(door.x, door.y + 1)] = false;
						patch[xyToPatchCoords(door.x, door.y + 2)] = false;
					} else if (door.y == bottom) {
						startPoint = xyToPatchCoords(door.x, door.y - 1);
						patch[xyToPatchCoords(door.x, door.y - 1)] = false;
						patch[xyToPatchCoords(door.x, door.y - 2)] = false;
					}
				}
				
				PathFinder.buildDistanceMap(startPoint, BArray.not(patch, null));
				
				valid = true;
				for (int i = 0; i < patch.length; i++){
					if (!patch[i] && PathFinder.distance[i] == Integer.MAX_VALUE){
						valid = false;
						break;
					}
				}
			} while (!valid);
			PathFinder.setMapSize(level.width(), level.height());
		} else {
			patch = Patch.generate(width()-2, height()-2, fill, clustering, true);
		}
	}
	
	//removes all diagonal-only adjacent filled patch areas, handy for making things look cleaner
	//note that this will reduce the fill rate very slightly
	protected void cleanDiagonalEdges(){
		if (patch == null) return;
		
		int pWidth = width()-2;
		
		for (int i = 0; i < patch.length - pWidth; i++){
			if (!patch[i]) continue;
			
			//we don't need to check above because we are either at the top
			// or have already dealt with those tiles
			
			//down-left
			if (i % pWidth != 0){
				if (patch[i - 1 + pWidth] && !(patch[i - 1] || patch[i + pWidth])){
					patch[i - 1 + pWidth] = false;
				}
			}
			
			//down-right
			if ((i + 1) % pWidth != 0){
				if (patch[i + 1 + pWidth] && !(patch[i + 1] || patch[i + pWidth])){
					patch[i + 1 + pWidth] = false;
				}
			}
			
		}
	}
	
	protected int xyToPatchCoords(int x, int y){
		return (x-left-1) + ((y-top-1) * (width()-2));
	}
}
