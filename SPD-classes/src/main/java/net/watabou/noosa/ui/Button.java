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

package com.watabou.noosa.ui;

import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.PointerArea;
import com.watabou.utils.Signal;

public class Button extends Component {

	public static float longClick = 1f;
	
	protected PointerArea hotArea;
	
	protected boolean pressed;
	protected float pressTime;
	protected boolean processed;

	@Override
	protected void createChildren() {
		hotArea = new PointerArea( 0, 0, 0, 0 ) {
			@Override
			protected void onPointerDown( PointerEvent event ) {
				pressed = true;
				pressTime = 0;
				processed = false;
				Button.this.onPointerDown();
			}
			@Override
			protected void onPointerUp( PointerEvent event ) {
				pressed = false;
				Button.this.onPointerUp();
			}
			@Override
			protected void onClick( PointerEvent event ) {
				if (!processed) {
					Button.this.onClick();
				}
			}
		};
		add( hotArea );
		
		KeyEvent.addKeyListener( keyListener = new Signal.Listener<KeyEvent>() {
			@Override
			public boolean onSignal ( KeyEvent event ) {
				if ( active && event.pressed && KeyBindings.getActionForKey( event ) == keyAction()){
					onClick();
					return true;
				}
				return false;
			}
		});
	}
	
	private Signal.Listener<KeyEvent> keyListener;
	
	public GameAction keyAction(){
		return null;
	}
	
	@Override
	public void update() {
		super.update();
		
		hotArea.active = visible;
		
		if (pressed) {
			if ((pressTime += Game.elapsed) >= longClick) {
				pressed = false;
				if (onLongClick()) {

					hotArea.reset();
					processed = true;
					onPointerUp();
					
					Game.vibrate( 50 );
				}
			}
		}
	}
	
	protected void onPointerDown() {}
	protected void onPointerUp() {}
	protected void onClick() {}
	protected boolean onLongClick() {
		return false;
	}
	
	@Override
	protected void layout() {
		hotArea.x = x;
		hotArea.y = y;
		hotArea.width = width;
		hotArea.height = height;
	}
	
	@Override
	public synchronized void destroy () {
		super.destroy();
		KeyEvent.removeKeyListener( keyListener );
	}
	
}
