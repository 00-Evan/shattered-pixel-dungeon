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

import android.opengl.GLES20;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.utils.Rect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class FogOfWar extends Image {

	//first index is visibility type, second is brightness level
	private static final int FOG_COLORS[][] = new int[][]{{
			//visible
			0xAA000000, 0x55000000, //-2 and -1 brightness
			0x00000000, //0 brightness
			0x00000000, 0x00000000 //1 and 2 brightness
			}, {
			//visited
			0xEE000000, 0xDD000000,
			0xCC000000,
			0x99000000, 0x66000000
			}, {
			//mapped
			0xEE442211, 0xDD442211,
			0xCC442211,
			0x99442211, 0x66442211
			}, {
			//invisible
			0xFF000000, 0xFF000000,
			0xFF000000,
			0xFF000000, 0xFF000000
			}};

	private static final int VISIBLE    =   0;
	private static final int VISITED    =   1;
	private static final int MAPPED     =   2;
	private static final int INVISIBLE  =   3;

	private int mapWidth;
	private int mapHeight;
	private int mapLength;
	
	private int pWidth;
	private int pHeight;
	
	private int width2;
	private int height2;

	private volatile Rect updated;
	private Rect updating;

	private static final int PIX_PER_TILE = 2;

	public FogOfWar( int mapWidth, int mapHeight ) {
		
		super();

		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		mapLength = mapHeight * mapWidth;
		
		pWidth = mapWidth * PIX_PER_TILE;
		pHeight = mapHeight * PIX_PER_TILE;
		
		width2 = 1;
		while (width2 < pWidth) {
			width2 <<= 1;
		}
		
		height2 = 1;
		while (height2 < pHeight) {
			height2 <<= 1;
		}
		
		float size = DungeonTilemap.SIZE / PIX_PER_TILE;
		width = width2 * size;
		height = height2 * size;
		
		texture( new FogTexture(width2, height2) );
		
		scale.set(
			DungeonTilemap.SIZE / PIX_PER_TILE,
			DungeonTilemap.SIZE / PIX_PER_TILE);

		updated = new Rect(0, 0, mapWidth, mapHeight);
	}

	public synchronized void updateFog(){
		updated.set( 0, 0, mapWidth, mapHeight );
	}

	public synchronized void updateFogCell( int cell ){
		updateFogArea( cell % mapWidth , cell / mapHeight, 1, 1 );
	}

	public synchronized void updateFogArea(int x, int y, int w, int h){
		updated.union(x, y);
		updated.union(x + w, y + h);
		updated = updated.intersect( new Rect(0, 0, mapWidth, mapHeight) );
	}

	public synchronized void moveToUpdating(){
		updating = new Rect(updated);
		updated.setEmpty();
	}

	private boolean[] visible;
	private boolean[] visited;
	private boolean[] mapped;
	private int brightness;
	
	private void updateTexture( boolean[] visible, boolean[] visited, boolean[] mapped ) {
		this.visible = visible;
		this.visited = visited;
		this.mapped = mapped;
		this.brightness = ShatteredPixelDungeon.brightness() + 2;

		moveToUpdating();

		boolean fullUpdate = updating.height() == mapHeight && updating.width() == mapWidth;

		FogTexture fog = (FogTexture)texture;

		int cell;
		int[] colorArray = new int[PIX_PER_TILE*PIX_PER_TILE];
		for (int i=updating.top; i < updating.bottom; i++) {
			cell = mapWidth * i + updating.left;
			for (int j=updating.left; j < updating.right; j++) {

				if (cell >= Dungeon.level.length()) continue; //do nothing

				if (!Level.discoverable[cell]
						|| (!visible[cell] && !visited[cell] && !mapped[cell])){
					//we skip filling cells here if it isn't a full update
					// because they must already be dark
					if (fullUpdate)
						fillCell(j, i, FOG_COLORS[INVISIBLE][brightness]);
					cell++;
					continue;
				}

				//wall tiles
				if (DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell])){

					//internal wall tiles
					if (cell + mapWidth >= mapLength
							|| DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell + mapWidth])){

						//these tiles need to check both the left and right side, to account for only one half of them being seen
						if (cell % mapWidth != 0){

							//picks the darkest fog between current tile, left, and below-left(if left is a wall).
							if (cell + mapWidth < mapLength && DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell - 1])){
								colorArray[0] = colorArray[2] = FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth - 1), getCellFog(cell - 1)))][brightness];

							} else {
								colorArray[0] = colorArray[2] = FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell - 1))][brightness];
							}

						} else {
							colorArray[0] = colorArray [2] = FOG_COLORS[INVISIBLE][brightness];
						}

						if ((cell+1) % mapWidth != 0){

							//picks the darkest fog between current tile, right, and below-right(if right is a wall).
							if (cell + mapWidth < mapLength && DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell + 1])){
								colorArray[1] = colorArray[3] = FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth + 1), getCellFog(cell + 1)))][brightness];

							} else {
								colorArray[1] = colorArray[3] =
										FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + 1))][brightness];
							}

						} else {
							colorArray[1] = colorArray [3] = FOG_COLORS[INVISIBLE][brightness];
						}

						fillCell(j, i, colorArray);

					//camera-facing wall tiles
					} else {
						fillCell(j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + mapWidth))][brightness]);
					}

					//other tiles
				} else {
					fillCell(j, i, FOG_COLORS[getCellFog(cell)][brightness]);
				}

				cell++;
			}
		}

		if (updating.width() == mapWidth && updating.height() == mapHeight)
			fog.update();
		else
			fog.update(updating.top * PIX_PER_TILE, updating.bottom * PIX_PER_TILE);

	}

	private int getCellFog( int cell ){

		if (visible[cell]) {
			return VISIBLE;
		} else if (visited[cell]) {
			return VISITED;
		} else if (mapped[cell] ) {
			return MAPPED;
		} else {
			return INVISIBLE;
		}
	}

	private void fillCell( int x, int y, int[] colors){
		FogTexture fog = (FogTexture)texture;
		for (int i = 0; i < PIX_PER_TILE; i++){
			fog.pixels.position(((y * PIX_PER_TILE)+i)*width2 + x * PIX_PER_TILE);
			for (int j = 0; j < PIX_PER_TILE; j++) {
				fog.pixels.put(colors[i*PIX_PER_TILE + j]);
			}
		}
	}

	private void fillCell( int x, int y, int color){
		FogTexture fog = (FogTexture)texture;
		for (int i = 0; i < PIX_PER_TILE; i++){
			fog.pixels.position(((y * PIX_PER_TILE)+i)*width2 + x * PIX_PER_TILE);
			for (int j = 0; j < PIX_PER_TILE; j++) {
				fog.pixels.put(color);
			}
		}
	}

	//provides a native intbuffer implementation because android.graphics.bitmap is too slow
	//TODO perhaps should spin this off into something like FastEditTexture in SPD-classes
	private class FogTexture extends SmartTexture {

		private IntBuffer pixels;
		
		public FogTexture(int w, int h) {
			super();
			width = w;
			height = h;
			pixels = ByteBuffer.
					allocateDirect( w * h * 4 ).
					order( ByteOrder.nativeOrder() ).
					asIntBuffer();

			TextureCache.add( FogOfWar.class, this );
		}

		@Override
		protected void generate() {
			int[] ids = new int[1];
			GLES20.glGenTextures( 1, ids, 0 );
			id = ids[0];
		}

		@Override
		public void reload() {
			generate();
			update();
		}

		public void update(){
			bind();
			filter( Texture.LINEAR, Texture.LINEAR );
			wrap( Texture.CLAMP, Texture.CLAMP);
			pixels.position(0);
			GLES20.glTexImage2D(
					GLES20.GL_TEXTURE_2D,
					0,
					GLES20.GL_RGBA,
					width,
					height,
					0,
					GLES20.GL_RGBA,
					GLES20.GL_UNSIGNED_BYTE,
					pixels );
		}

		//allows partially updating the texture
		public void update(int top, int bottom){
			bind();
			filter( Texture.LINEAR, Texture.LINEAR );
			wrap( Texture.CLAMP, Texture.CLAMP);
			pixels.position(top*width);
			GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D,
					0,
					0,
					top,
					width,
					bottom - top,
					GLES20.GL_RGBA,
					GLES20.GL_UNSIGNED_BYTE,
					pixels);
		}

		@Override
		public void delete() {
			super.delete();
		}
	}

	@Override
	protected NoosaScript script() {
		return NoosaScriptNoLighting.get();
	}

	@Override
	public void draw() {

		if (!updated.isEmpty()){
			updateTexture(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
			updating.setEmpty();
		}

		super.draw();
	}
}
