/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.effects;

import java.util.ArrayList;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.utils.SparseArray;

public class FloatingText extends BitmapText {

	private static final float LIFESPAN	= 1f;
	private static final float DISTANCE	= DungeonTilemap.SIZE;

	private float timeLeft;
	
	private int key = -1;

	private float cameraZoom = -1;

	private static SparseArray<ArrayList<FloatingText>> stacks = new SparseArray<ArrayList<FloatingText>>();
	
	public FloatingText() {
		super();
		speed.y = - DISTANCE / LIFESPAN;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (timeLeft > 0) {
			if ((timeLeft -= Game.elapsed) <= 0) {
				kill();
			} else {
				float p = timeLeft / LIFESPAN;
				alpha( p > 0.5f ? 1 : p * 2 );
			}
		}
	}
	
	@Override
	public void kill() {
		if (key != -1) {
			stacks.get( key ).remove( this );
			key = -1;
		}
		super.kill();
	}
	
	@Override
	public void destroy() {
		kill();
		super.destroy();
	}
	
	public void reset( float x, float y, String text, int color ) {
		
		revive();

		if (cameraZoom != Camera.main.zoom) {
			cameraZoom = Camera.main.zoom;
			PixelScene.chooseFont( 9, cameraZoom );
			font = PixelScene.font;
			scale.set( PixelScene.scale );
		}

		text( text );
		hardlight( color );
		
		measure();
		this.x = PixelScene.align( x - width() / 2 );
		this.y = y - height();
		
		timeLeft = LIFESPAN;
	}
	
	/* STATIC METHODS */
	
	public static void show( float x, float y, String text, int color ) {
		GameScene.status().reset( x,  y,  text, color );
	}
	
	public static void show( float x, float y, int key, String text, int color ) {
		FloatingText txt = GameScene.status();
		txt.reset( x,  y,  text, color );
		push( txt, key );
	}
	
	private static void push( FloatingText txt, int key ) {
		
		txt.key = key;
		
		ArrayList<FloatingText> stack = stacks.get( key );
		if (stack == null) {
			stack = new ArrayList<FloatingText>();
			stacks.put( key, stack );
		}
		
		if (stack.size() > 0) {
			FloatingText below = txt;
			int aboveIndex = stack.size() - 1;
			while (aboveIndex >= 0) {
				FloatingText above = stack.get( aboveIndex );
				if (above.y + above.height() > below.y) {
					above.y = below.y - above.height();
					
					below = above;
					aboveIndex--;
				} else {
					break;
				}
			}
		}
		
		stack.add( txt );
	}
}
