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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.List;

public class Lightning extends Group {

	private static final float DURATION = 0.3f;
	
	private float life;

	private List<Arc> arcs;
	
	private Callback callback;

	public Lightning(int from, int to, Callback callback){
		this(Arrays.asList(new Arc(from, to)), callback);
	}

	public Lightning(PointF from, int to, Callback callback){
		this(Arrays.asList(new Arc(from, to)), callback);
	}

	public Lightning(int from, PointF to, Callback callback){
		this(Arrays.asList(new Arc(from, to)), callback);
	}

	public Lightning(PointF from, PointF to, Callback callback){
		this(Arrays.asList(new Arc(from, to)), callback);
	}
	
	public Lightning( List<Arc> arcs, Callback callback ) {
		
		super();

		this.arcs = arcs;
		for (Arc arc : this.arcs)
			add(arc);

		this.callback = callback;
		
		life = DURATION;
	}
	
	private static final double A = 180 / Math.PI;
	
	@Override
	public void update() {
		if ((life -= Game.elapsed) < 0) {
			
			killAndErase();
			if (callback != null) {
				callback.call();
			}
			
		} else {
			
			float alpha = life / DURATION;
			
			for (Arc arc : arcs) {
				arc.alpha(alpha);
			}

			super.update();
		}
	}
	
	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}

	//A lightning object is meant to be loaded up with arcs.
	//these act as a means of easily expressing lighting between two points.
	public static class Arc extends Group {

		private Image arc1, arc2;

		//starting and ending x/y values
		private PointF start, end;

		public Arc(int from, int to){
			this( DungeonTilemap.tileCenterToWorld(from),
					DungeonTilemap.tileCenterToWorld(to));
		}

		public Arc(PointF from, int to){
			this( from, DungeonTilemap.tileCenterToWorld(to));
		}

		public Arc(int from, PointF to){
			this( DungeonTilemap.tileCenterToWorld(from), to);
		}

		public Arc(PointF from, PointF to){
			start = from;
			end = to;

			arc1 = new Image(Effects.get(Effects.Type.LIGHTNING));
			arc1.x = start.x - arc1.origin.x;
			arc1.y = start.y - arc1.origin.y;
			arc1.origin.set( 0, arc1.height()/2 );
			add( arc1 );

			arc2 = new Image(Effects.get(Effects.Type.LIGHTNING));
			arc2.origin.set( 0, arc2.height()/2 );
			add( arc2 );

			update();
		}

		public void alpha(float alpha) {
			arc1.am = arc2.am = alpha;
		}

		@Override
		public void update() {
			float x2 = (start.x + end.x) / 2 + Random.Float( -4, +4 );
			float y2 = (start.y + end.y) / 2 + Random.Float( -4, +4 );

			float dx = x2 - start.x;
			float dy = y2 - start.y;
			arc1.angle = (float)(Math.atan2( dy, dx ) * A);
			arc1.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc1.width;

			dx = end.x - x2;
			dy = end.y - y2;
			arc2.angle = (float)(Math.atan2( dy, dx ) * A);
			arc2.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc2.width;
			arc2.x = x2 - arc2.origin.x;
			arc2.y = y2 - arc2.origin.x;
		}
	}
}
