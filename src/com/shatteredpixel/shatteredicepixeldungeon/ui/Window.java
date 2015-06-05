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
package com.shatteredpixel.shatteredicepixeldungeon.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.watabou.input.Keys;
import com.watabou.input.Keys.Key;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.TouchArea;
import com.shatteredpixel.shatteredicepixeldungeon.Chrome;
import com.shatteredpixel.shatteredicepixeldungeon.effects.ShadowBox;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.PixelScene;
import com.watabou.utils.Signal;

public class Window extends Group implements Signal.Listener<Key> {

	protected int width;
	protected int height;
	
	protected TouchArea blocker;
    protected ShadowBox shadow;
    protected NinePatch chrome;
	
	public static final int TITLE_COLOR = 0xFFFF44;
    public static final int SHPX_COLOR = 0x33BB33;
	
	public Window() {
		this( 0, 0, Chrome.get( Chrome.Type.WINDOW ) );
	}
	
	public Window( int width, int height ) {
		this( width, height, Chrome.get( Chrome.Type.WINDOW ) );
	}
			
	public Window( int width, int height, NinePatch chrome ) {
		super();
		
		blocker = new TouchArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
			@Override
			protected void onClick( Touch touch ) {
				if (!Window.this.chrome.overlapsScreenPoint( 
					(int)touch.current.x, 
					(int)touch.current.y )) {
					
					onBackPressed();
				}
			}
		};
		blocker.camera = PixelScene.uiCamera;
		add( blocker );
		
		this.chrome = chrome;

		this.width = width;
		this.height = height;

        shadow = new ShadowBox();
        shadow.am = 0.5f;
        shadow.camera = PixelScene.uiCamera.visible ?
                PixelScene.uiCamera : Camera.main;
        add( shadow );

		chrome.x = -chrome.marginLeft();
		chrome.y = -chrome.marginTop();
		chrome.size( 
			width - chrome.x + chrome.marginRight(),
			height - chrome.y + chrome.marginBottom() );
		add( chrome );
		
		camera = new Camera( 0, 0, 
			(int)chrome.width, 
			(int)chrome.height, 
			PixelScene.defaultZoom );
		camera.x = (int)(Game.width - camera.width * camera.zoom) / 2;
		camera.y = (int)(Game.height - camera.height * camera.zoom) / 2;
		camera.scroll.set( chrome.x, chrome.y );
		Camera.add( camera );

		shadow.boxRect(
				camera.x / camera.zoom,
				camera.y / camera.zoom,
				chrome.width(), chrome.height );

		Keys.event.add( this );
	}
	
	public void resize( int w, int h ) {
		this.width = w;
		this.height = h;
		
		chrome.size( 
			width + chrome.marginHor(),
			height + chrome.marginVer() );
		
		camera.resize( (int)chrome.width, (int)chrome.height );
		camera.x = (int)(Game.width - camera.screenWidth()) / 2;
		camera.y = (int)(Game.height - camera.screenHeight()) / 2;

		shadow.boxRect( camera.x / camera.zoom, camera.y / camera.zoom, chrome.width(), chrome.height );
	}
	
	public void hide() {
		parent.erase( this );
		destroy();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		Camera.remove( camera );
		Keys.event.remove( this );
	}

	@Override
	public void onSignal( Key key ) {
		if (key.pressed) {
			switch (key.code) {
			case Keys.BACK:
				onBackPressed();			
				break;
			case Keys.MENU:
				onMenuPressed();			
				break;
			}
		}
		
		Keys.event.cancel();
	}
	
	public void onBackPressed() {
		hide();
	}
	
	public void onMenuPressed() {
	}
	
	protected static class Highlighter {
		
		private static final Pattern HIGHLIGHTER	= Pattern.compile( "_(.*?)_" );
		private static final Pattern STRIPPER		= Pattern.compile( "[ \n]" );
		
		public String text;
		
		public boolean[] mask;
		
		public Highlighter( String text ) {
			
			String stripped = STRIPPER.matcher( text ).replaceAll( "" );
			mask = new boolean[stripped.length()];
			
			Matcher m = HIGHLIGHTER.matcher( stripped );
			
			int pos = 0;
			int lastMatch = 0;
			
			while (m.find()) {
				pos += (m.start() - lastMatch);
				int groupLen = m.group( 1 ).length();
				for (int i=pos; i < pos + groupLen; i++) {
					mask[i] = true;
				}
				pos += groupLen;
				lastMatch = m.end();
			}
			
			m.reset( text );
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				m.appendReplacement( sb, m.group( 1 ) );
			}
			m.appendTail( sb );
			
			this.text = sb.toString();
		}
		
		public boolean[] inverted() {
			boolean[] result = new boolean[mask.length];
			for (int i=0; i < result.length; i++) {
				result[i] = !mask[i];
			}
			return result;
		}
		
		public boolean isHighlighted() {
			for (int i=0; i < mask.length; i++) {
				if (mask[i]) {
					return true;
				}
			}
			return false;
		}
	}
}
