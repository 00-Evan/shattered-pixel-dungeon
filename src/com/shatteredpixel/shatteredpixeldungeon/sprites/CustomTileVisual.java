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
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public class CustomTileVisual extends Image implements Bundlable {

	private static final int TILE_SIZE = 16;

	private String tx;
	private int txX, txY;

	private int tileX, tileY, tileW, tileH;

	public CustomTileVisual(){
		super();
	}

	public CustomTileVisual(String tx, int txX, int txY, int tileW, int tileH){
		super();

		this.tx = tx;
		this.txX = txX;
		this.txY = txY;

		this.tileW = tileW;
		this.tileH = tileH;
	}

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

	private static final String TX  = "tx";
	private static final String TX_X= "txX";
	private static final String TX_Y= "txY";

	private static final String TILE_X  = "tileX";
	private static final String TILE_Y  = "tileY";
	private static final String TILE_W  = "tileW";
	private static final String TILE_H  = "tileH";


	@Override
	public void restoreFromBundle(Bundle bundle) {
		tx = bundle.getString(TX);
		txX = bundle.getInt(TX_X);
		txY = bundle.getInt(TX_Y);

		tileX = bundle.getInt(TILE_X);
		tileY = bundle.getInt(TILE_Y);
		tileW = bundle.getInt(TILE_W);
		tileH = bundle.getInt(TILE_H);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(TX, tx);
		bundle.put(TX_X, txX);
		bundle.put(TX_Y, txY);

		bundle.put(TILE_X, tileX);
		bundle.put(TILE_Y, tileY);
		bundle.put(TILE_W, tileW);
		bundle.put(TILE_H, tileH);
	}
}
