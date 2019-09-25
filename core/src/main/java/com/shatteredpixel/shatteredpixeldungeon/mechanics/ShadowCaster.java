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

package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;

//based on: http://www.roguebasin.com/index.php?title=FOV_using_recursive_shadowcasting
public final class ShadowCaster {

	public static final int MAX_DISTANCE = 12;
	
	//max length of rows as FOV moves out, for each FOV distance
	//This is used to make the overall FOV circular, instead of square
	public static int[][] rounding;
	static {
		rounding = new int[MAX_DISTANCE+1][];
		for (int i=1; i <= MAX_DISTANCE; i++) {
			rounding[i] = new int[i+1];
			for (int j=1; j <= i; j++) {
				//testing the middle of a cell, so we use i + 0.5
				rounding[i][j] = (int)Math.min(
						j,
						Math.round( (i + 0.5) * Math.cos( Math.asin( j / (i + 0.5) ))));
			}
		}
	}
	
	public static void castShadow( int x, int y, boolean[] fieldOfView, boolean[] blocking, int distance ) {
		
		if (distance >= MAX_DISTANCE){
			distance = MAX_DISTANCE;
		}

		BArray.setFalse(fieldOfView);

		//set source cell to true
		fieldOfView[y * Dungeon.level.width() + x] = true;
		
		//scans octants, clockwise
		try {
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, +1, -1, false);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, -1, +1, true);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, +1, +1, true);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, +1, +1, false);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, -1, +1, false);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, +1, -1, true);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, -1, -1, true);
			scanOctant(distance, fieldOfView, blocking, 1, x, y, 0.0, 1.0, -1, -1, false);
		} catch (Exception e){
			ShatteredPixelDungeon.reportException(e);
			BArray.setFalse(fieldOfView);
		}

	}
	
	//scans a single 45 degree octant of the FOV.
	//This can add up to a whole FOV by mirroring in X(mX), Y(mY), and X=Y(mXY)
	private static void scanOctant(int distance, boolean[] fov, boolean[] blocking, int row,
	                               int x, int y, double lSlope, double rSlope,
	                               int mX, int mY, boolean mXY){
		
		//if we have negative space to traverse, just quit.
		if (rSlope < lSlope) return;
		
		boolean inBlocking = false;
		int start, end;
		int col;
		
		//calculations are offset by 0.5 because FOV is coming from the center of the source cell
		
		//for each row, starting with the current one
		for (; row <= distance; row++){
			
			//we offset by slightly less than 0.5 to account for slopes just touching a cell
			if (lSlope == 0)    start = 0;
			else                start = (int)Math.floor((row - 0.5) * lSlope + 0.499);
			
			if (rSlope == 1)    end = rounding[distance][row];
			else                end = Math.min( rounding[distance][row],
			                                    (int)Math.ceil((row + 0.5) * rSlope - 0.499));
			
			//coordinates of source
			int cell = x + y*Dungeon.level.width();
			
			//plus coordinates of current cell (including mirroring in x, y, and x=y)
			if (mXY)    cell += mX*start*Dungeon.level.width() + mY*row;
			else        cell += mX*start + mY*row*Dungeon.level.width();
			
			//for each column in this row, which
			for (col = start; col <= end; col++){
				
				fov[cell] = true;
				
				if (blocking[cell]){
					if (!inBlocking){
						inBlocking = true;
						
						//start a new scan, 1 row deeper, ending at the left side of current cell
						if (col != start){
							scanOctant(distance, fov, blocking, row+1, x, y, lSlope,
									//change in x over change in y
									(col - 0.5) / (row + 0.5),
									mX, mY, mXY);
						}
					}
				
				} else {
					if (inBlocking){
						inBlocking = false;
						
						//restrict current scan to the left side of current cell for future rows
						
						//change in x over change in y
						lSlope = (col - 0.5) / (row - 0.5);
					}
				}
				
				if (!mXY)   cell += mX;
				else        cell += mX*Dungeon.level.width();
				
			}
			
			//if the row ends in a blocking cell, this scan is finished.
			if (inBlocking) return;
		}
	}
}
