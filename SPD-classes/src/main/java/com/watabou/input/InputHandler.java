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

package com.watabou.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.watabou.noosa.Game;
import com.watabou.utils.PointF;

public class InputHandler extends InputAdapter {
	
	public InputHandler( Input input ){
		input.setInputProcessor( this );
		input.setCatchKey( Input.Keys.BACK, true);
		input.setCatchKey( Input.Keys.MENU, true);
	}
	
	public void processAllEvents(){
		PointerEvent.processPointerEvents();
		KeyEvent.processKeyEvents();
		ScrollEvent.processScrollEvents();
	}
	
	// *********************
	// *** Pointer Input ***
	// *********************
	
	@Override
	public synchronized boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, true));
		return true;
	}
	
	@Override
	public synchronized boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, false));
		return true;
	}
	
	@Override
	public synchronized boolean touchDragged(int screenX, int screenY, int pointer) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, true));
		return true;
	}
	
	//TODO tracking this should probably be in PointerEvent
	private static PointF pointerHoverPos = new PointF();
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		pointerHoverPos.x = screenX;
		pointerHoverPos.y = screenY;
		return true;
	}
	
	// *****************
	// *** Key Input ***
	// *****************
	
	@Override
	public synchronized boolean keyDown( int keyCode ) {
		if (KeyBindings.isKeyBound( keyCode )) {
			KeyEvent.addKeyEvent( new KeyEvent( keyCode, true ) );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public synchronized boolean keyUp( int keyCode ) {
		if (KeyBindings.isKeyBound( keyCode )) {
			KeyEvent.addKeyEvent( new KeyEvent( keyCode, false ) );
			return true;
		} else {
			return false;
		}
	}
	
	// ********************
	// *** Scroll Input ***
	// ********************
	
	@Override
	public boolean scrolled(int amount) {
		ScrollEvent.addScrollEvent( new ScrollEvent(pointerHoverPos, amount));
		return true;
	}
}
