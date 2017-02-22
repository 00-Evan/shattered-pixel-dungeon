/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class CustomTiledVisual extends Tilemap implements Bundlable {

	protected static final int SIZE = DungeonTilemap.SIZE;

	public int tileX, tileY;   //x and y coords for texture within a level
	public int tileW = 1, tileH = 1; //width and height in tiles

	public CustomTiledVisual(Object tx) {
		super(tx, new TextureFilm( tx, SIZE, SIZE ) );
	}

	public void pos(int pos) {
		pos( pos%Dungeon.level.width(), pos/Dungeon.level.width() );
	}

	public void pos(int tileX, int tileY){
		this.tileX = tileX;
		this.tileY = tileY;
	}

	public void setRect(int topLeft, int bottomRight){
		setRect( topLeft%Dungeon.level.width(),
				topLeft/Dungeon.level.width(),
				bottomRight%Dungeon.level.width() - topLeft%Dungeon.level.width(),
				bottomRight/Dungeon.level.width() - topLeft/Dungeon.level.width()
		);
	}

	public void setRect(int tileX, int tileY, int tileW, int tileH){
		this.tileX = tileX;
		this.tileY = tileY;
		this.tileW = tileW;
		this.tileH = tileH;
	}

	public CustomTiledVisual create(){
		x = tileX*SIZE;
		y = tileY*SIZE;
		return this;
	}

	//assumes that width and height are already set.
	protected void mapSimpleImage(int txX, int txY){
		int data[] = new int[tileW * tileH];
		int texTileWidth = texture.width/SIZE;
		int x = txX, y = txY;
		for (int i = 0; i < data.length; i++){
			data[i] = x + (texTileWidth*y);

			x++;
			if ((x - txX) == tileW){
				x = txX;
				y++;
			}
		}

		map(data, tileW);
	}

	//x and y here are the coordinates tapped within the tile visual
	public Image image(int tileX, int tileY){
		if (!needsRender(tileX + mapWidth*tileY)){
			return null;
		} else {
			Image img = new Image(texture);
			img.frame(tileset.get(data[tileX + mapWidth * tileY]));
			return img;
		}
	}

	public String name(int tileX, int tileY){
		return null;
	}

	public String desc(int tileX, int tileY){
		return null;
	}

	private static final String TILE_X  = "tileX";
	private static final String TILE_Y  = "tileY";

	private static final String TILE_W  = "tileW";
	private static final String TILE_H  = "tileH";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		tileX = bundle.getInt(TILE_X);
		tileY = bundle.getInt(TILE_Y);

		tileW = bundle.getInt(TILE_W);
		tileH = bundle.getInt(TILE_H);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		bundle.put(TILE_X, tileX);
		bundle.put(TILE_Y, tileY);

		bundle.put(TILE_W, tileW);
		bundle.put(TILE_H, tileH);
	}
}
