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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TouchArea;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class CellSelector extends TouchArea {

	public Listener listener = null;
	
	public boolean enabled;
	
	private float dragThreshold;
	
	public CellSelector( DungeonTilemap map ) {
		super( map );
		camera = map.camera();
		
		dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;
	}
	
	@Override
	protected void onClick( Touch touch ) {
		if (dragging) {
			
			dragging = false;
			
		} else {
			
			PointF p = Camera.main.screenToCamera( (int)touch.current.x, (int)touch.current.y );
			for (Char mob : Dungeon.level.mobs){
				if (mob.sprite != null && mob.sprite.overlapsPoint( p.x, p.y)){
					select( mob.pos );
					return;
				}
			}

			for (Heap heap : Dungeon.level.heaps.values()){
				if (heap.sprite != null && heap.sprite.overlapsPoint( p.x, p.y)){
					select( heap.pos );
					return;
				}
			}
			
			select( ((DungeonTilemap)target).screenToTile(
				(int)touch.current.x,
				(int)touch.current.y,
					true ) );
		}
	}

	private float zoom( float value ) {

		value = GameMath.gate( PixelScene.minZoom, value, PixelScene.maxZoom );
		ShatteredPixelDungeon.zoom((int) (value - PixelScene.defaultZoom));
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
	private Touch another;
	private float startZoom;
	private float startSpan;
	
	@Override
	protected void onTouchDown( Touch t ) {

		if (t != touch && another == null) {
					
			if (!touch.down) {
				touch = t;
				onTouchDown( t );
				return;
			}
			
			pinching = true;
			
			another = t;
			startSpan = PointF.distance( touch.current, another.current );
			startZoom = camera.zoom;

			dragging = false;
		} else if (t != touch) {
			reset();
		}
	}
	
	@Override
	protected void onTouchUp( Touch t ) {
		if (pinching && (t == touch || t == another)) {
			
			pinching = false;
			
			zoom(Math.round( camera.zoom ));
			
			dragging = true;
			if (t == touch) {
				touch = another;
			}
			another = null;
			lastPos.set( touch.current );
		}
	}
	
	private boolean dragging = false;
	private PointF lastPos = new PointF();
	
	@Override
	protected void onDrag( Touch t ) {
		 
		camera.target = null;

		if (pinching) {

			float curSpan = PointF.distance( touch.current, another.current );
			camera.zoom( GameMath.gate(
				PixelScene.minZoom,
				startZoom * curSpan / startSpan,
				PixelScene.maxZoom ) );

		} else {
		
			if (!dragging && PointF.distance( t.current, t.start ) > dragThreshold) {
				
				dragging = true;
				lastPos.set( t.current );
				
			} else if (dragging) {
				camera.scroll.offset( PointF.diff( lastPos, t.current ).invScale( camera.zoom ) );
				lastPos.set( t.current );
			}
		}
		
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

	public interface Listener {
		void onSelect( Integer cell );
		String prompt();
	}
}
