/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class CustomTileVisual extends Image implements Bundlable {

	protected static final int TILE_SIZE = 16;

	public String name;

	protected String tx;    //string for resource file
	protected int txX, txY; //position(in tiles) within resource file
	//bundleable offsets from the standard texture xy, useful for mapping larger than 1x1 textures to many tiles
	//(e.g. mapping a 3x3 texture to a room, where the corners are walls and the center is the floor)
	protected int ofsX = 0, ofsY = 0;

	public int tileX, tileY;   //x and y coords for texture within a level
	public int tileW = 1, tileH = 1; //width and height in tiles

	public void pos(int pos) {
		pos( pos%Level.WIDTH, pos/Level.WIDTH );
	}

	public void pos(int tileX, int tileY){
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public String desc(){
		return null;
	}

	public CustomTileVisual create() {
		texture(tx);
		frame(texture.uvRect((txX + ofsX) * TILE_SIZE, (txY + ofsY) * TILE_SIZE,
				(txX + ofsX + tileW) * TILE_SIZE, (txY + ofsY + tileH) * TILE_SIZE));

		x = tileX*TILE_SIZE;
		y = tileY*TILE_SIZE;

		return this;
	}

	//returns a number of 1x1 tiles to fill a room, based on a 3x3 texture, not dissimilar to a ninepatch.
	public static ArrayList<CustomTileVisual> CustomTilesForRoom(Room r, Class<?extends CustomTileVisual> c){
		ArrayList<CustomTileVisual> result = new ArrayList<>();

		try {
			for (int x = r.left; x <= r.right; x++) {
				for (int y = r.top; y <= r.bottom; y++) {
					CustomTileVisual vis = c.newInstance();

					if (x == r.right) vis.ofsX = 2;
					else if (x != r.left) vis.ofsX = 1;

					if (y == r.bottom) vis.ofsY = 2;
					else if (y != r.top) vis.ofsY = 1;

					vis.pos(x, y);
					result.add(vis);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Something went wrong while making a bunch of tile visuals for a room!", e);
		}

		return result;
	}

	private static final String TILE_X  = "tileX";
	private static final String TILE_Y  = "tileY";

	private static final String OFS_X  = "ofsX";
	private static final String OFS_Y  = "ofsY";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		tileX = bundle.getInt(TILE_X);
		tileY = bundle.getInt(TILE_Y);

		//if these weren't stored they will default to 0
		ofsX = bundle.getInt(OFS_X);
		ofsY = bundle.getInt(OFS_Y);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(TILE_X, tileX);
		bundle.put(TILE_Y, tileY);

		//don't need to store this in all cases
		if (ofsX != 0 || ofsY != 0){
			bundle.put(OFS_X, ofsX);
			bundle.put(OFS_Y, ofsY);
		}
	}
}
