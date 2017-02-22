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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.opengl.GLES20;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.watabou.glwrap.Texture;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapText.Font;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.Scene;
import com.watabou.noosa.Visual;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.BitmapCache;

import javax.microedition.khronos.opengles.GL10;

public class PixelScene extends Scene {

	// Minimum virtual display size for portrait orientation
	public static final float MIN_WIDTH_P        = 135;
	public static final float MIN_HEIGHT_P        = 225;

	// Minimum virtual display size for landscape orientation
	public static final float MIN_WIDTH_L        = 240;
	public static final float MIN_HEIGHT_L        = 160;

	public static int defaultZoom = 0;
	public static int maxDefaultZoom = 0;
	public static int maxScreenZoom = 0;
	public static float minZoom;
	public static float maxZoom;

	public static Camera uiCamera;

	//stylized pixel font
	public static BitmapText.Font pixelFont;
	//These represent various mipmaps of the same font
	public static BitmapText.Font font1x;
	public static BitmapText.Font font2x;

	@Override
	public void create() {

		super.create();

		GameScene.scene = null;

		float minWidth, minHeight;
		if (ShatteredPixelDungeon.landscape()) {
			minWidth = MIN_WIDTH_L;
			minHeight = MIN_HEIGHT_L;
		} else {
			minWidth = MIN_WIDTH_P;
			minHeight = MIN_HEIGHT_P;
		}

		maxDefaultZoom = (int)Math.min(Game.width/minWidth, Game.height/minHeight);
		maxScreenZoom = (int)Math.min(Game.dispWidth/minWidth, Game.dispHeight/minHeight);
		defaultZoom = ShatteredPixelDungeon.scale();

		if (defaultZoom < Math.ceil( Game.density * 2 ) || defaultZoom > maxDefaultZoom){
			defaultZoom = (int)Math.ceil( Game.density * 2.5 );
			while ((
				Game.width / defaultZoom < minWidth ||
				Game.height / defaultZoom < minHeight
			) && defaultZoom > 1) {
				defaultZoom--;
			}
		}

		minZoom = 1;
		maxZoom = defaultZoom * 2;

		Camera.reset( new PixelCamera( defaultZoom ) );

		float uiZoom = defaultZoom;
		uiCamera = Camera.createFullscreen( uiZoom );
		Camera.add( uiCamera );

		if (pixelFont == null) {

			// 3x5 (6)
			pixelFont = Font.colorMarked(
				BitmapCache.get( Assets.PIXELFONT), 0x00000000, BitmapText.Font.LATIN_FULL );
			pixelFont.baseLine = 6;
			pixelFont.tracking = -1;

			// 9x15 (18)
			font1x = Font.colorMarked(
					BitmapCache.get( Assets.FONT1X), 22, 0x00000000, BitmapText.Font.LATIN_FULL );
			font1x.baseLine = 17;
			font1x.tracking = -2;
			font1x.texture.filter(Texture.LINEAR, Texture.LINEAR);

			//font1x double scaled
			font2x = Font.colorMarked(
					BitmapCache.get( Assets.FONT2X), 44, 0x00000000, BitmapText.Font.LATIN_FULL );
			font2x.baseLine = 38;
			font2x.tracking = -4;
			font2x.texture.filter(Texture.LINEAR, Texture.NEAREST);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		Touchscreen.event.removeAll();
	}

	public static BitmapText.Font font;
	public static float scale;

	public static void chooseFont( float size ) {
		chooseFont( size, defaultZoom );
	}

	public static void chooseFont( float size, float zoom ) {

		float pt = size * zoom;

		if (pt >= 25) {

			font = font2x;
			scale = pt / 38f;

		} else if (pt >= 12) {

			font = font1x;
			scale = pt / 19f;

		} else {
			font = pixelFont;
			scale = 1f;
		}

		scale /= zoom;
	}
	
	public static BitmapText createText( float size ) {
		return createText( null, size );
	}
	
	public static BitmapText createText( String text, float size ) {
		
		chooseFont( size );
		
		BitmapText result = new BitmapText( text, font );
		result.scale.set( scale );
		
		return result;
	}
	
	public static BitmapTextMultiline createMultiline( float size ) {
		return createMultiline( null, size );
	}
	
	public static BitmapTextMultiline createMultiline( String text, float size ) {
		
		chooseFont( size );
		
		BitmapTextMultiline result = new BitmapTextMultiline( text, font );
		result.scale.set( scale );
		
		return result;
	}

	public static RenderedText renderText( int size ) {
		return renderText("", size);
	}

	public static RenderedText renderText( String text, int size ) {
		RenderedText result = new RenderedText( text, size*defaultZoom);
		result.scale.set(1/(float)defaultZoom);
		return result;
	}

	public static RenderedTextMultiline renderMultiline( int size ){
		return renderMultiline("", size);
	}

	public static RenderedTextMultiline renderMultiline( String text, int size ){
		RenderedTextMultiline result = new RenderedTextMultiline( text, size*defaultZoom);
		result.zoom(1/(float)defaultZoom);
		return result;
	}

	/**
	 * These methods align UI elements to device pixels.
	 * e.g. if we have a scale of 3x then valid positions are #.0, #.33, #.67
	 */

	public static float align( float pos ) {
		return Math.round(pos * defaultZoom) / (float)defaultZoom;
	}

	public static float align( Camera camera, float pos ) {
		return Math.round(pos * camera.zoom) / camera.zoom;
	}

	public static void align( Visual v ) {
		v.x = align( v.x );
		v.y = align( v.y );
	}

	public static void align( Component c ){
		c.setPos(align(c.left()), align(c.top()));
	}

	public static boolean noFade = false;
	protected void fadeIn() {
		if (noFade) {
			noFade = false;
		} else {
			fadeIn( 0xFF000000, false );
		}
	}
	
	protected void fadeIn( int color, boolean light ) {
		add( new Fader( color, light ) );
	}
	
	public static void showBadge( Badges.Badge badge ) {
		BadgeBanner banner = BadgeBanner.show( badge.image );
		banner.camera = uiCamera;
		banner.x = align( banner.camera, (banner.camera.width - banner.width) / 2 );
		banner.y = align( banner.camera, (banner.camera.height - banner.height) / 3 );
		Game.scene().add( banner );
	}
	
	protected static class Fader extends ColorBlock {
		
		private static float FADE_TIME = 1f;
		
		private boolean light;
		
		private float time;
		
		public Fader( int color, boolean light ) {
			super( uiCamera.width, uiCamera.height, color );
			
			this.light = light;
			
			camera = uiCamera;
			
			alpha( 1f );
			time = FADE_TIME;
		}
		
		@Override
		public void update() {
			
			super.update();
			
			if ((time -= Game.elapsed) <= 0) {
				alpha( 0f );
				parent.remove( this );
			} else {
				alpha( time / FADE_TIME );
			}
		}
		
		@Override
		public void draw() {
			if (light) {
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
				super.draw();
				GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
			} else {
				super.draw();
			}
		}
	}
	
	private static class PixelCamera extends Camera {
		
		public PixelCamera( float zoom ) {
			super(
				(int)(Game.width - Math.ceil( Game.width / zoom ) * zoom) / 2,
				(int)(Game.height - Math.ceil( Game.height / zoom ) * zoom) / 2,
				(int)Math.ceil( Game.width / zoom ),
				(int)Math.ceil( Game.height / zoom ), zoom );
			fullScreen = true;
		}
		
		@Override
		protected void updateMatrix() {
			float sx = align( this, scroll.x + shakeX );
			float sy = align( this, scroll.y + shakeY );
			
			matrix[0] = +zoom * invW2;
			matrix[5] = -zoom * invH2;
			
			matrix[12] = -1 + x * invW2 - sx * matrix[0];
			matrix[13] = +1 - y * invH2 - sy * matrix[5];
			
		}
	}
}
