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
import java.util.ArrayList;

public class FogOfWar extends Image {

	//first index is visibility type, second is brightness level
	private static final int FOG_COLORS[][] = new int[][]{{
			//visible
			0x55000000, 0x00000000, //-2 and -1 brightness
			0x00000000, //0 brightness
			0x00000000, 0x00000000 //1 and 2 brightness
			}, {
			//visited
			0xDD000000, 0xBB000000,
			0x99000000,
			0x77000000, 0x55000000
			}, {
			//mapped
			0xDD221108, 0xBB442211,
			0x99663319,
			0x77884411, 0x55AA552A
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

	private volatile ArrayList<Rect> toUpdate;
	private volatile ArrayList<Rect> updating;

	//should be divisible by 2
	private static final int PIX_PER_TILE = 2;

	/*
	TODO currently the center of each fox pixel is aligned with the inside of a cell
	might be possible to create a better fog effect by aligning them with edges of a cell,
	similar to the existing fog effect in vanilla (although probably with more precision)
	the advantage here is that it may be possible to totally eliminate the tile blocking map
	*/
	
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

		toUpdate = new ArrayList<>();
		toUpdate.add(new Rect(0, 0, mapWidth, mapHeight));
	}

	public synchronized void updateFog(){
		toUpdate.clear();
		toUpdate.add(new Rect(0, 0, mapWidth, mapHeight));
	}
	
	public synchronized void updateFog(Rect update){
		for (Rect r : toUpdate.toArray(new Rect[0])){
			if (!r.intersect(update).isEmpty()){
				toUpdate.remove(r);
				toUpdate.add(r.union(update));
				return;
			}
		}
		toUpdate.add(update);
	}

	public synchronized void updateFog( int cell, int radius ){
		Rect update = new Rect(
				(cell % mapWidth) - radius,
				(cell / mapWidth) - radius,
				(cell % mapWidth) - radius + 1 + 2*radius,
				(cell / mapWidth) - radius + 1 + 2*radius);
		update.left = Math.max(0, update.left);
		update.top = Math.max(0, update.top);
		update.right = Math.min(mapWidth, update.right);
		update.bottom = Math.min(mapHeight, update.bottom);
		if (update.isEmpty()) return;
		updateFog( update );
	}

	public synchronized void updateFogArea(int x, int y, int w, int h){
		updateFog(new Rect(x, y, x + w, y + h));
	}

	private synchronized void moveToUpdating(){
		updating = toUpdate;
		toUpdate = new ArrayList<>();
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
		
		boolean fullUpdate = false;
		if (updating.size() == 1){
			Rect update = updating.get(0);
			if (update.height() == mapHeight && update.width() == mapWidth){
				fullUpdate = true;
			}
		}

		FogTexture fog = (FogTexture)texture;

		int cell;
		
		for (Rect update : updating) {
			for (int i = update.top; i <= update.bottom; i++) {
				cell = mapWidth * i + update.left;
				for (int j = update.left; j <= update.right; j++) {
					
					if (cell >= Dungeon.level.length()) continue; //do nothing
					
					if (!Dungeon.level.discoverable[cell]
							|| (!visible[cell] && !visited[cell] && !mapped[cell])) {
						//we skip filling cells here if it isn't a full update
						// because they must already be dark
						if (fullUpdate)
							fillCell(j, i, FOG_COLORS[INVISIBLE][brightness]);
						cell++;
						continue;
					}
					
					//wall tiles
					if (wall(cell)) {
						
						//always dark if nothing is beneath them
						if (cell + mapWidth >= mapLength) {
							fillCell(j, i, FOG_COLORS[INVISIBLE][brightness]);
							
						//internal wall tiles, need to check both the left and right side,
						// to account for only one half of them being seen
						} else if (wall(cell + mapWidth)) {
							
							//left side
							if (cell % mapWidth != 0) {
								
								//picks the darkest fog between current tile, left, and below-left(if left is a wall).
								if (wall(cell - 1)) {
									
									//if below-left is also a wall, then we should be dark no matter what.
									if (wall(cell + mapWidth - 1)) {
										fillLeft(j, i, FOG_COLORS[INVISIBLE][brightness]);
									} else {
										fillLeft(j, i, FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth - 1), getCellFog(cell - 1)))][brightness]);
									}
									
								} else {
									fillLeft(j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell - 1))][brightness]);
								}
								
							} else {
								fillLeft(j, i, FOG_COLORS[INVISIBLE][brightness]);
							}
							
							//right side
							if ((cell + 1) % mapWidth != 0) {
								
								//picks the darkest fog between current tile, right, and below-right(if right is a wall).
								if (wall(cell + 1)) {
									
									//if below-right is also a wall, then we should be dark no matter what.
									if (wall(cell + mapWidth + 1)) {
										fillRight(j, i, FOG_COLORS[INVISIBLE][brightness]);
									} else {
										fillRight(j, i, FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth + 1), getCellFog(cell + 1)))][brightness]);
									}
									
								} else {
									fillRight(j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + 1))][brightness]);
								}
								
							} else {
								fillRight(j, i, FOG_COLORS[INVISIBLE][brightness]);
							}
							
						//camera-facing wall tiles
						//darkest between themselves and the tile below them
						} else {
							fillCell(j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + mapWidth))][brightness]);
						}
						
					//other tiles, just their direct value
					} else {
						fillCell(j, i, FOG_COLORS[getCellFog(cell)][brightness]);
					}
					
					cell++;
				}
			}
			
		}
		
		if (updating.size() == 1 && !fullUpdate){
			fog.update(updating.get(0).top * PIX_PER_TILE, updating.get(0).bottom * PIX_PER_TILE);
		} else {
			fog.update();
		}

	}
	
	private boolean wall(int cell) {
		return DungeonTileSheet.wallStitcheable(Dungeon.level.map[cell]);
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
	
	private void fillLeft( int x, int y, int color){
		FogTexture fog = (FogTexture)texture;
		for (int i = 0; i < PIX_PER_TILE; i++){
			fog.pixels.position(((y * PIX_PER_TILE)+i)*width2 + x * PIX_PER_TILE);
			for (int j = 0; j < PIX_PER_TILE/2; j++) {
				fog.pixels.put(color);
			}
		}
	}
	
	private void fillRight( int x, int y, int color){
		FogTexture fog = (FogTexture)texture;
		for (int i = 0; i < PIX_PER_TILE; i++){
			fog.pixels.position(((y * PIX_PER_TILE)+i)*width2 + x * PIX_PER_TILE + PIX_PER_TILE/2);
			for (int j = PIX_PER_TILE/2; j < PIX_PER_TILE; j++) {
				fog.pixels.put(color);
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

		if (!toUpdate.isEmpty()){
			updateTexture(Dungeon.level.heroFOV, Dungeon.level.visited, Dungeon.level.mapped);
		}

		super.draw();
	}
}
