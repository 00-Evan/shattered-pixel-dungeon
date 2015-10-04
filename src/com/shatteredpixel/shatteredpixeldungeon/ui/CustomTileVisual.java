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
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class CustomTileVisual extends Image implements Bundlable {

	protected static final int TILE_SIZE = 16;

	protected String tx;    //string for resource file
	protected int txX, txY; //position(in tiles) within resource file

	protected int tileX, tileY;   //x and y coords for texture within a level
	protected int tileW, tileH; //width and height in tiles

	public void pos(int pos) {
		pos( pos%Level.WIDTH, pos/Level.WIDTH );
	}

	public void pos(int tileX, int tileY){
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public CustomTileVisual create() {
		texture(tx);
		frame(texture.uvRect(txX * TILE_SIZE, txY * TILE_SIZE, (txX + tileW) * TILE_SIZE, (txY + tileH) * TILE_SIZE));

		x = tileX*TILE_SIZE;
		y = tileY*TILE_SIZE;

		return this;
	}

	private static final String TILE_X  = "tileX";
	private static final String TILE_Y  = "tileY";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		tileX = bundle.getInt(TILE_X);
		tileY = bundle.getInt(TILE_Y);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(TILE_X, tileX);
		bundle.put(TILE_Y, tileY);
	}
}
