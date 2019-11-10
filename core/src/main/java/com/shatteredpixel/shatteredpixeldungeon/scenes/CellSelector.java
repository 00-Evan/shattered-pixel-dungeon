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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.input.KeyAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.input.PointerEvent;
import com.watabou.input.ScrollEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ScrollArea;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

public class CellSelector extends ScrollArea {

	public Listener listener = null;
	
	public boolean enabled;
	
	private float dragThreshold;
	
	public CellSelector( DungeonTilemap map ) {
		super( map );
		camera = map.camera();
		
		dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;
		
		mouseZoom = camera.zoom;
		KeyEvent.addKeyListener( keyListener );
	}
	
	private float mouseZoom;
	
	@Override
	protected void onScroll( ScrollEvent event ) {
		float diff = event.amount/10f;
		
		//scale zoom difference so zooming is consistent
		diff /= ((camera.zoom+1)/camera.zoom)-1;
		diff = Math.min(1, diff);
		mouseZoom = GameMath.gate( PixelScene.minZoom, mouseZoom - diff, PixelScene.maxZoom );
		
		zoom( Math.round(mouseZoom) );
	}
	
	@Override
	protected void onClick( PointerEvent event ) {
		if (dragging) {
			
			dragging = false;
			
		} else {
			
			PointF p = Camera.main.screenToCamera( (int) event.current.x, (int) event.current.y );
			for (Char mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob.sprite != null && mob.sprite.overlapsPoint( p.x, p.y)){
					select( mob.pos );
					return;
				}
			}

			for (Heap heap : Dungeon.level.heaps.valueList()){
				if (heap.sprite != null && heap.sprite.overlapsPoint( p.x, p.y)){
					select( heap.pos );
					return;
				}
			}
			
			select( ((DungeonTilemap)target).screenToTile(
				(int) event.current.x,
				(int) event.current.y,
					true ) );
		}
	}

	private float zoom( float value ) {

		value = GameMath.gate( PixelScene.minZoom, value, PixelScene.maxZoom );
		SPDSettings.zoom((int) (value - PixelScene.defaultZoom));
		camera.zoom( value );

		//Resets character sprite positions with the new camera zoom
		//This is important as characters are centered on a 16x16 tile, but may have any sprite size
		//This can lead to none-whole coordinate, which need to be aligned with the zoom
		for (Char c : Actor.chars()){
			if (c.sprite != null && !c.sprite.isMoving){
				c.sprite.point(c.sprite.worldToCamera(c.pos));
			}
		}

		return value;
	}
	
	public void select( int cell ) {
		if (enabled && listener != null && cell != -1) {
			
			listener.onSelect( cell );
			GameScene.ready();
			
		} else {
			
			GameScene.cancel();
			
		}
	}
	
	private boolean pinching = false;
	private PointerEvent another;
	private float startZoom;
	private float startSpan;
	
	@Override
	protected void onPointerDown( PointerEvent event ) {

		if (event != curEvent && another == null) {
					
			if (!curEvent.down) {
				curEvent = event;
				onPointerDown( event );
				return;
			}
			
			pinching = true;
			
			another = event;
			startSpan = PointF.distance( curEvent.current, another.current );
			startZoom = camera.zoom;

			dragging = false;
		} else if (event != curEvent) {
			reset();
		}
	}
	
	@Override
	protected void onPointerUp( PointerEvent event ) {
		if (pinching && (event == curEvent || event == another)) {
			
			pinching = false;
			
			zoom(Math.round( camera.zoom ));
			
			dragging = true;
			if (event == curEvent) {
				curEvent = another;
			}
			another = null;
			lastPos.set( curEvent.current );
		}
	}
	
	private boolean dragging = false;
	private PointF lastPos = new PointF();
	
	@Override
	protected void onDrag( PointerEvent event ) {

		if (pinching) {

			float curSpan = PointF.distance( curEvent.current, another.current );
			float zoom = (startZoom * curSpan / startSpan);
			camera.zoom( GameMath.gate(
				PixelScene.minZoom,
					zoom - (zoom % 0.1f),
				PixelScene.maxZoom ) );

		} else {
		
			if (!dragging && PointF.distance( event.current, event.start ) > dragThreshold) {
				
				dragging = true;
				lastPos.set( event.current );
				
			} else if (dragging) {
				camera.shift( PointF.diff( lastPos, event.current ).invScale( camera.zoom ) );
				lastPos.set( event.current );
			}
		}
		
	}
	
	private KeyAction heldAction = null;
	private int heldTurns = 0;
	
	private Signal.Listener<KeyEvent> keyListener = new Signal.Listener<KeyEvent>() {
		@Override
		public boolean onSignal(KeyEvent event) {
			KeyAction action = KeyBindings.getBinding( event );
			if (!event.pressed){
				
				if (heldAction != null && heldAction == action) {
					resetKeyHold();
					return true;
				} else {
					switch (action){
						case ZOOM_IN:
							zoom( camera.zoom+1 );
							return true;
						case ZOOM_OUT:
							zoom( camera.zoom-1 );
							return true;
						case ZOOM_DEFAULT:
							zoom( PixelScene.defaultZoom );
							return true;
					}
				}
			} else if (moveFromKey(action)) {
				heldAction = action;
				return true;
			}
			
			return false;
		}
	};
	
	private boolean moveFromKey(KeyAction event){
		boolean moved = true;
		int cell = Dungeon.hero.pos;
		//TODO implement game actions, instead of using keys directly
		switch (event){
			case N:
				cell += -Dungeon.level.width();
				break;
			case NE:
				cell += +1-Dungeon.level.width();
				break;
			case E:
				cell += +1;
				break;
			case SE:
				cell += +1+Dungeon.level.width();
				break;
			case S:
				cell += +Dungeon.level.width();
				break;
			case SW:
				cell += -1+Dungeon.level.width();
				break;
			case W:
				cell += -1;
				break;
			case NW:
				cell += -1-Dungeon.level.width();
				break;
			default:
				moved = false;
		}
		
		if (moved){
			//each step when keyboard moving takes 0.15s, 0.125s, 0.1s, 0.1s, ...
			// this is to make it easier to move 1 or 2 steps without overshooting
			CharSprite.setMoveInterval( CharSprite.DEFAULT_MOVE_INTERVAL +
			                            Math.max(0, 0.05f - heldTurns *0.025f));
			select(cell);
		}
		
		return moved;
	}
	
	public void processKeyHold(){
		if (heldAction != null){
			enabled = true;
			heldTurns++;
			moveFromKey(heldAction);
		}
	}
	
	public void resetKeyHold(){
		heldAction = null;
		heldTurns = 0;
		CharSprite.setMoveInterval( CharSprite.DEFAULT_MOVE_INTERVAL );
	}
	
	public void cancel() {
		
		if (listener != null) {
			listener.onSelect( null );
		}
		
		GameScene.ready();
	}

	@Override
	public void reset() {
		super.reset();
		another = null;
		if (pinching){
			pinching = false;

			zoom( Math.round( camera.zoom ) );
		}
	}

	public void enable(boolean value){
		if (enabled != value){
			enabled = value;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		KeyEvent.removeKeyListener( keyListener );
	}
	
	public interface Listener {
		void onSelect( Integer cell );
		String prompt();
	}
}
