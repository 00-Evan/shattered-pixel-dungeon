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

package com.watabou.noosa;

import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.utils.Signal;

public class Scene extends Group {
	
	private Signal.Listener<KeyEvent> keyListener;
	
	public void create() {
		KeyEvent.addKeyListener( keyListener = new Signal.Listener<KeyEvent>() {
			@Override
			public boolean onSignal( KeyEvent event ) {
				if (Game.instance != null && event.pressed) {
					if (KeyBindings.getActionForKey( event ) == GameAction.BACK){
						onBackPressed();
					}
				}
				return false;
			}
		} );
	}
	
	@Override
	public void destroy() {
		KeyEvent.removeKeyListener( keyListener );
		super.destroy();
	}
	
	public void onPause() {
		
	}
	
	public void onResume(){
	
	}
	
	public static boolean landscape(){
		return Game.width > Game.height;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public Camera camera() {
		return Camera.main;
	}
	
	protected void onBackPressed() {
		Game.instance.finish();
	}

}
