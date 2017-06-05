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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import android.graphics.Bitmap;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class ItemSprite extends MovieClip {

	public static final int SIZE	= 16;
	
	private static final float DROP_INTERVAL = 0.4f;
	
	public Heap heap;
	
	private Glowing glowing;
	//FIXME: a lot of this emitter functionality isn't very well implemented.
	//right now I want to ship 0.3.0, but should refactor in the future.
	protected Emitter emitter;
	private float phase;
	private boolean glowUp;
	
	private float dropInterval;

	//the amount the sprite is raised from flat when viewed in a raised perspective
	protected float perspectiveRaise    = 5 / 16f; //5 pixels

	//the width and height of the shadow are a percentage of sprite size
	//offset is the number of pixels the shadow is moved down or up (handy for some animations)
	protected boolean renderShadow  = false;
	protected float shadowWidth     = 1f;
	protected float shadowHeight    = 0.25f;
	protected float shadowOffset    = 0.5f;
	
	public ItemSprite() {
		this( ItemSpriteSheet.SOMETHING, null );
	}
	
	public ItemSprite( Item item ) {
		super(Assets.ITEMS);

		view (item);
	}
	
	public ItemSprite( int image, Glowing glowing ) {
		super( Assets.ITEMS );
		
		view(image, glowing);
	}
	
	public void originToCenter() {
		origin.set(width / 2, height / 2);
	}
	
	public void link() {
		link(heap);
	}
	
	public void link( Heap heap ) {
		this.heap = heap;
		view( heap.image(), heap.glowing() );
		renderShadow = true;
		place(heap.pos);
	}
	
	@Override
	public void revive() {
		super.revive();
		
		speed.set( 0 );
		acc.set( 0 );
		dropInterval = 0;
		
		heap = null;
		if (emitter != null) {
			emitter.killAndErase();
			emitter = null;
		}
	}

	public void visible(boolean value){
		this.visible = value;
		if (emitter != null && !visible){
			emitter.killAndErase();
			emitter = null;
		}
	}
	
	public PointF worldToCamera( int cell ) {
		final int csize = DungeonTilemap.SIZE;
		
		return new PointF(
			cell % Dungeon.level.width() * csize + (csize - width()) * 0.5f,
			cell / Dungeon.level.width() * csize + (csize - height()) - csize * perspectiveRaise
		);
	}
	
	public void place( int p ) {
		if (Dungeon.level != null) {
			point(worldToCamera(p));
			shadowOffset = 0.5f;
		}
	}
	
	public void drop() {

		if (heap.isEmpty()) {
			return;
		} else if (heap.size() == 1){
			// normally this would happen for any heap, however this is not applied to heaps greater than 1 in size
			// in order to preserve an amusing visual bug/feature that used to trigger for heaps with size > 1
			// where as long as the player continually taps, the heap sails up into the air.
			place(heap.pos);
		}
			
		dropInterval = DROP_INTERVAL;
		
		speed.set( 0, -100 );
		acc.set(0, -speed.y / DROP_INTERVAL * 2);
		
		if (visible && heap != null && heap.peek() instanceof Gold) {
			CellEmitter.center( heap.pos ).burst( Speck.factory( Speck.COIN ), 5 );
			Sample.INSTANCE.play( Assets.SND_GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
		}
	}
	
	public void drop( int from ) {

		if (heap.pos == from) {
			drop();
		} else {
			
			float px = x;
			float py = y;
			drop();
			
			place(from);
	
			speed.offset((px - x) / DROP_INTERVAL, (py - y) / DROP_INTERVAL);
		}
	}

	public ItemSprite view(Item item){
		view(item.image(), item.glowing());
		Emitter emitter = item.emitter();
		if (emitter != null && parent != null) {
			emitter.pos( this );
			parent.add( emitter );
			this.emitter = emitter;
		}
		return this;
	}
	
	public ItemSprite view( int image, Glowing glowing ) {
		if (this.emitter != null) this.emitter.killAndErase();
		emitter = null;
		frame( image );
		if ((this.glowing = glowing) == null) {
			resetColor();
		}
		return this;
	}

	public void frame( int image ){
		frame( ItemSpriteSheet.film.get( image ));

		float height = ItemSpriteSheet.film.height( image );
		//adds extra raise to very short items, so they are visible
		if (height < 8f){
			perspectiveRaise =  (5 + 8 - height) / 16f;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (emitter != null) emitter.killAndErase();
		emitter = null;
	}

	private float[] shadowMatrix = new float[16];

	@Override
	protected void updateMatrix() {
		super.updateMatrix();
		Matrix.copy(matrix, shadowMatrix);
		Matrix.translate(shadowMatrix,
				(width() * (1f - shadowWidth)) / 2f,
				(height() * (1f - shadowHeight)) + shadowOffset);
		Matrix.scale(shadowMatrix, shadowWidth, shadowHeight);
	}

	@Override
	public void draw() {
		if (texture == null || (!dirty && buffer == null))
			return;

		if (renderShadow) {
			if (dirty) {
				verticesBuffer.position(0);
				verticesBuffer.put(vertices);
				if (buffer == null)
					buffer = new Vertexbuffer(verticesBuffer);
				else
					buffer.updateVertices(verticesBuffer);
				dirty = false;
			}

			NoosaScript script = script();

			texture.bind();

			script.camera(camera());

			updateMatrix();

			script.uModel.valueM4(shadowMatrix);
			script.lighting(
					0, 0, 0, am * .6f,
					0, 0, 0, aa * .6f);

			script.drawQuad(buffer);
		}

		super.draw();

	}

	@Override
	public void update() {
		super.update();

		visible = (heap == null || heap.seen);

		if (dropInterval > 0){
			shadowOffset -= speed.y * Game.elapsed * 0.8f;

			if ((dropInterval -= Game.elapsed) <= 0){

				speed.set(0);
				acc.set(0);
				shadowOffset = 0.25f;
				place(heap.pos);

				if (visible) {
					boolean water = Level.water[heap.pos];

					if (water) {
						GameScene.ripple(heap.pos);
					} else {
						int cell = Dungeon.level.map[heap.pos];
						water = (cell == Terrain.WELL || cell == Terrain.ALCHEMY);
					}

					if (!(heap.peek() instanceof Gold)) {
						Sample.INSTANCE.play(water ? Assets.SND_WATER : Assets.SND_STEP, 0.8f, 0.8f, 1.2f);
					}
				}
			}
		}

		if (visible && glowing != null) {
			if (glowUp && (phase += Game.elapsed) > glowing.period) {
				
				glowUp = false;
				phase = glowing.period;
				
			} else if (!glowUp && (phase -= Game.elapsed) < 0) {
				
				glowUp = true;
				phase = 0;
				
			}
			
			float value = phase / glowing.period * 0.6f;
			
			rm = gm = bm = 1 - value;
			ra = glowing.red * value;
			ga = glowing.green * value;
			ba = glowing.blue * value;
		}
	}

	public static int pick( int index, int x, int y ) {
		Bitmap bmp = TextureCache.get( Assets.ITEMS ).bitmap;
		int rows = bmp.getWidth() / SIZE;
		int row = index / rows;
		int col = index % rows;
		return bmp.getPixel( col * SIZE + x, row * SIZE + y );
	}
	
	public static class Glowing {
		
		public int color;
		public float red;
		public float green;
		public float blue;
		public float period;
		
		public Glowing( int color ) {
			this( color, 1f );
		}
		
		public Glowing( int color, float period ) {

			this.color = color;

			red = (color >> 16) / 255f;
			green = ((color >> 8) & 0xFF) / 255f;
			blue = (color & 0xFF) / 255f;
			
			this.period = period;
		}
	}
}
