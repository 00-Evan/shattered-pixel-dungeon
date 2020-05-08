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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

public abstract class GooBossRoom extends StandardRoom {
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}
	
	public static GooBossRoom randomGooRoom(){
		switch (Random.Int(4)){
			case 0: default:
				return new DiamondGooRoom();
			case 1:
				return new WalledGooRoom();
			case 2:
				return new ThinPillarsGooRoom();
			case 3:
				return new ThickPillarsGooRoom();
		}
	}
	
	protected void setupGooNest( Level level ){
		GooNest nest = new GooNest();
		nest.setRect(left + width()/2 - 2, top + height()/2 - 2, 4 + width()%2, 4 + height()%2);
		
		level.customTiles.add(nest);
	}
	
	public static class GooNest extends CustomTilemap {
		
		{
			texture = Assets.Environment.SEWER_BOSS;
		}
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			for (int x = 0; x < tileW; x++){
				for (int y = 0; y < tileH; y++){
					
					//corners
					if ((x == 0 || x == tileW-1) && (y == 0 || y == tileH-1)){
						data[x + tileW*y] = -1;
					
					//adjacent to corners
					} else if ((x == 1 && y == 0) || (x == 0 && y == 1)){
						data[x + tileW*y] = 0;
						
					} else if ((x == tileW-2 && y == 0) || (x == tileW-1 && y == 1)){
						data[x + tileW*y] = 1;
						
					} else if ((x == 1 && y == tileH-1) || (x == 0 && y == tileH-2)){
						data[x + tileW*y] = 2;
					
					} else if ((x == tileW-2 && y == tileH-1) || (x == tileW-1 && y == tileH-2)) {
						data[x + tileW*y] = 3;
					
					//sides
					} else if (x == 0){
						data[x + tileW*y] = 4;
					
					} else if (y == 0){
						data[x + tileW*y] = 5;
					
					} else if (x == tileW-1){
						data[x + tileW*y] = 6;
					
					} else if (y == tileH-1){
						data[x + tileW*y] = 7;
					
					//inside
					} else {
						data[x + tileW*y] = 8;
					}
					
				}
			}
			v.map( data, tileW );
			return v;
		}
		
		@Override
		public Image image(int tileX, int tileY) {
			return null;
		}
	}
}
