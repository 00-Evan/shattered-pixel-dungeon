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

import com.badlogic.gdx.graphics.Pixmap;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class FogOfWar extends Image {

	//first index is visibility type, second is brightness level
	private static final int FOG_COLORS[][] = new int[][]{{
			//visible
			0x00000000, //-1 brightness
			0x00000000, //0  brightness
			0x00000000, //1  brightness
			}, {
			//visited
			0xCC000000,
			0x99000000,
			0x55000000
			}, {
			//mapped
			0xCC112244,
			0x99193366,
			0x55224488
			}, {
			//invisible
			0xFF000000,
			0xFF000000,
			0xFF000000
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

		//TODO might be nice to compartmentalize the pixmap access and modification into texture/texturecache
		Pixmap px = new Pixmap(width2, height2, Pixmap.Format.RGBA8888);
		px.setBlending(Pixmap.Blending.None);
		px.setColor(0x000000FF);
		px.fill();
		SmartTexture tx = new SmartTexture(px, Texture.LINEAR, Texture.CLAMP, false);
		TextureCache.add(FogOfWar.class, tx);
		texture( tx );
		
		scale.set( size, size );

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
		this.brightness = SPDSettings.brightness() + 1;

		moveToUpdating();
		
		boolean fullUpdate = false;
		if (updating.size() == 1){
			Rect update = updating.get(0);
			if (update.height() == mapHeight && update.width() == mapWidth){
				fullUpdate = true;
			}
		}

		Pixmap fog = texture.bitmap;

		int cell;
		
		for (Rect update : updating) {
			for (int i = update.top; i < update.bottom; i++) {
				cell = mapWidth * i + update.left;
				for (int j = update.left; j < update.right; j++) {
					
					if (cell >= Dungeon.level.length()) continue; //do nothing
					
					if (!Dungeon.level.discoverable[cell]
							|| (!visible[cell] && !visited[cell] && !mapped[cell])) {
						//we skip filling cells here if it isn't a full update
						// because they must already be dark
						if (fullUpdate)
							fillCell(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
						cell++;
						continue;
					}
					
					//wall tiles
					if (wall(cell)) {
						
						//always dark if nothing is beneath them
						if (cell + mapWidth >= mapLength) {
							fillCell(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
							
						//internal wall tiles, need to check both the left and right side,
						// to account for only one half of them being seen
						} else if (wall(cell + mapWidth)) {
							
							//left side
							if (cell % mapWidth != 0) {
								
								//picks the darkest fog between current tile, left, and below-left(if left is a wall).
								if (wall(cell - 1)) {
									
									//if below-left is also a wall, then we should be dark no matter what.
									if (wall(cell + mapWidth - 1)) {
										fillLeft(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
									} else {
										fillLeft(fog, j, i, FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth - 1), getCellFog(cell - 1)))][brightness]);
									}
									
								} else {
									fillLeft(fog, j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell - 1))][brightness]);
								}
								
							} else {
								fillLeft(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
							}
							
							//right side
							if ((cell + 1) % mapWidth != 0) {
								
								//picks the darkest fog between current tile, right, and below-right(if right is a wall).
								if (wall(cell + 1)) {
									
									//if below-right is also a wall, then we should be dark no matter what.
									if (wall(cell + mapWidth + 1)) {
										fillRight(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
									} else {
										fillRight(fog, j, i, FOG_COLORS[Math.max(getCellFog(cell), Math.max(getCellFog(cell + mapWidth + 1), getCellFog(cell + 1)))][brightness]);
									}
									
								} else {
									fillRight(fog, j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + 1))][brightness]);
								}
								
							} else {
								fillRight(fog, j, i, FOG_COLORS[INVISIBLE][brightness]);
							}
							
						//camera-facing wall tiles
						//darkest between themselves and the tile below them
						} else {
							fillCell(fog, j, i, FOG_COLORS[Math.max(getCellFog(cell), getCellFog(cell + mapWidth))][brightness]);
						}
						
					//other tiles, just their direct value
					} else {
						fillCell(fog, j, i, FOG_COLORS[getCellFog(cell)][brightness]);
					}
					
					cell++;
				}
			}
			
		}
		
		texture.bitmap(fog);

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
	
	private void fillLeft( Pixmap fog, int x, int y, int color){
		fog.setColor((color << 8) | (color >>> 24));
		fog.fillRectangle(x * PIX_PER_TILE, y*PIX_PER_TILE, PIX_PER_TILE/2, PIX_PER_TILE);
	}
	
	private void fillRight( Pixmap fog, int x, int y, int color){
		fog.setColor((color << 8) | (color >>> 24));
		fog.fillRectangle(x * PIX_PER_TILE + PIX_PER_TILE/2, y*PIX_PER_TILE, PIX_PER_TILE/2, PIX_PER_TILE);
	}

	private void fillCell( Pixmap fog, int x, int y, int color){
		fog.setColor((color << 8) | (color >>> 24));
		fog.fillRectangle(x * PIX_PER_TILE, y*PIX_PER_TILE, PIX_PER_TILE, PIX_PER_TILE);
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
	
	@Override
	public void destroy() {
		super.destroy();
		if (texture != null){
			TextureCache.remove(FogOfWar.class);
		}
	}
}
