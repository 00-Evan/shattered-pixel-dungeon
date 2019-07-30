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

import com.badlogic.gdx.InputAdapter;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class InputHandler extends InputAdapter {
	
	// Accumulated touch events
	protected ArrayList<Touchscreen.Touch> touchEvents = new ArrayList<>();
	
	// Accumulated key events
	protected ArrayList<Keys.Key> keyEvents = new ArrayList<>();
	
	@Override
	public boolean keyDown( int keyCode ) {
		
		if (keyCode != Keys.BACK &&
				keyCode != Keys.MENU) {
			return false;
		}
		
		synchronized (keyEvents) {
			keyEvents.add( new Keys.Key(keyCode, true) );
		}
		return true;
	}
	
	@Override
	public boolean keyUp( int keyCode ) {
		
		if (keyCode != Keys.BACK &&
				keyCode != Keys.MENU) {
			return false;
		}
		
		synchronized (keyEvents) {
			keyEvents.add( new Keys.Key(keyCode, false) );
		}
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		synchronized (touchEvents) {
			touchEvents.add(new Touchscreen.Touch(screenX, screenY, pointer, true));
		}
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		synchronized (touchEvents) {
			touchEvents.add(new Touchscreen.Touch(screenX, screenY, pointer, false));
		}
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenX /= (Game.dispWidth / (float)Game.width);
		screenY /= (Game.dispHeight / (float)Game.height);
		synchronized (touchEvents) {
			touchEvents.add(new Touchscreen.Touch(screenX, screenY, pointer, true));
		}
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public void processAllEvents(){
		synchronized (touchEvents) {
			Touchscreen.processTouchEvents( touchEvents );
			touchEvents.clear();
		}
		synchronized (keyEvents) {
			Keys.processKeyEvents( keyEvents );
			keyEvents.clear();
		}
	}
}
