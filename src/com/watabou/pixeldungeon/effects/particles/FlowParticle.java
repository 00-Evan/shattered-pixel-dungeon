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
package com.watabou.pixeldungeon.effects.particles;

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class FlowParticle extends PixelParticle {

	public static final Emitter.Factory FACTORY = new Factory() {	
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((FlowParticle)emitter.recycle( FlowParticle.class )).reset( x, y );
		}
	};
	
	public FlowParticle() {
		super();
		
		lifespan = 0.6f;
		acc.set( 0, 32 );
		angularSpeed = Random.Float( -360, +360 );
	}
	
	public void reset( float x, float y ) {
		revive();
		
		left = lifespan;
		
		this.x = x;
		this.y = y;
		
		am = 0;
		size( 0 );
		speed.set( 0 );
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = left / lifespan;
		am = (p < 0.5f ? p : 1 - p) * 0.6f;
		size( (1 - p) * 4 );
	}
	
	public static class Flow extends Group {
		
		private static final float DELAY	= 0.1f;
		
		private int pos;
		
		private float x;
		private float y;
		
		private float delay;
		
		public Flow( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileToWorld( pos );
			x = p.x;
			y = p.y + DungeonTilemap.SIZE - 1;
			
			delay = Random.Float( DELAY );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( DELAY );
					
					((FlowParticle)recycle( FlowParticle.class )).reset( 
						x + Random.Float( DungeonTilemap.SIZE ), y );
				}
			}
		}
	}
}