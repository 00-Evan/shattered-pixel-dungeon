/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.watabou.noosa.Game;
import com.watabou.utils.PointF;

public class InputHandler extends InputAdapter {

	private InputMultiplexer multiplexer;

	public InputHandler( Input input ){
		//An input multiplexer, with additional coord tweaks for power saver mode
		multiplexer = new InputMultiplexer(){
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				screenX /= (Game.dispWidth / (float)Game.width);
				screenY /= (Game.dispHeight / (float)Game.height);
				return super.touchDown(screenX, screenY, pointer, button);
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				screenX /= (Game.dispWidth / (float)Game.width);
				screenY /= (Game.dispHeight / (float)Game.height);
				return super.touchDragged(screenX, screenY, pointer);
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				screenX /= (Game.dispWidth / (float)Game.width);
				screenY /= (Game.dispHeight / (float)Game.height);
				return super.touchUp(screenX, screenY, pointer, button);
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				screenX /= (Game.dispWidth / (float)Game.width);
				screenY /= (Game.dispHeight / (float)Game.height);
				return super.mouseMoved(screenX, screenY);
			}
		};
		input.setInputProcessor(multiplexer);
		addInputProcessor(this);
		input.setCatchKey( Input.Keys.BACK, true);
		input.setCatchKey( Input.Keys.MENU, true);
	}

	public void addInputProcessor(InputProcessor processor){
		multiplexer.addProcessor(0, processor);
	}

	public void removeInputProcessor(InputProcessor processor){
		multiplexer.removeProcessor(processor);
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
		Gdx.input.setOnscreenKeyboardVisible(false); //in-game events never need keyboard, so hide it
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, true));
		return true;
	}
	
	@Override
	public synchronized boolean touchUp(int screenX, int screenY, int pointer, int button) {
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, false));
		return true;
	}
	
	@Override
	public synchronized boolean touchDragged(int screenX, int screenY, int pointer) {
		PointerEvent.addPointerEvent(new PointerEvent(screenX, screenY, pointer, true));
		return true;
	}
	
	//TODO tracking this should probably be in PointerEvent
	private static PointF pointerHoverPos = new PointF();
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
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
	public boolean scrolled(float amountX, float amountY) {
		ScrollEvent.addScrollEvent( new ScrollEvent(pointerHoverPos, amountY));
		return true;
	}
}
