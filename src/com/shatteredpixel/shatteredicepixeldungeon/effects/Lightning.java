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
package com.shatteredpixel.shatteredicepixeldungeon.effects;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Lightning extends Group {

	private static final float DURATION = 0.3f;
	
	private float life;
	
	private int length;
	private float[] cx;
	private float[] cy;
	
	private Image[] arcsS;
	private Image[] arcsE;
	
	private Callback callback;
	
	public Lightning( int[] cells, int length, Callback callback ) {
		
		super();
		
		this.callback = callback;
		
		Image proto = Effects.get( Effects.Type.LIGHTNING );
		float ox = 0;
		float oy = proto.height / 2;
		
		this.length = length;
		cx = new float[length];
		cy = new float[length];
		
		for (int i=0; i < length; i++) {
			int c = cells[i];
			cx[i] = (c % Level.WIDTH + 0.5f) * DungeonTilemap.SIZE;
			cy[i] = (c / Level.WIDTH + 0.5f) * DungeonTilemap.SIZE;
		}
		
		arcsS = new Image[length - 1];
		arcsE = new Image[length - 1];
		for (int i=0; i < length - 1; i++) {
			
			Image arc = arcsS[i] = new Image( proto );
			
			arc.x = cx[i] - arc.origin.x;
			arc.y = cy[i] - arc.origin.y;
			arc.origin.set( ox, oy );
			add( arc );
			
			arc = arcsE[i] = new Image( proto );
			arc.origin.set( ox, oy );
			add( arc );
		}
		
		life = DURATION;
		
		Sample.INSTANCE.play( Assets.SND_LIGHTNING );
	}
	
	private static final double A = 180 / Math.PI;
	
	@Override
	public void update() {
		super.update();
		
		if ((life -= Game.elapsed) < 0) {
			
			killAndErase();
			if (callback != null) {
				callback.call();
			}
			
		} else {
			
			float alpha = life / DURATION;
			
			for (int i=0; i < length - 1; i++) {
				
				float sx = cx[i];
				float sy = cy[i];
				float ex = cx[i+1];
				float ey = cy[i+1];
				
				float x2 = (sx + ex) / 2 + Random.Float( -4, +4 );
				float y2 = (sy + ey) / 2 + Random.Float( -4, +4 );
				
				float dx = x2 - sx;
				float dy = y2 - sy;
				Image arc = arcsS[i];
				arc.am = alpha;
				arc.angle = (float)(Math.atan2( dy, dx ) * A);
				arc.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc.width;
				
				dx = ex - x2;
				dy = ey - y2;
				arc = arcsE[i];
				arc.am = alpha;
				arc.angle = (float)(Math.atan2( dy, dx ) * A);
				arc.scale.x = (float)Math.sqrt( dx * dx + dy * dy ) / arc.width;
				arc.x = x2 - arc.origin.x;
				arc.y = y2 - arc.origin.x;
			}
		}
	}
	
	@Override
	public void draw() {
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
		super.draw();
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}
}
