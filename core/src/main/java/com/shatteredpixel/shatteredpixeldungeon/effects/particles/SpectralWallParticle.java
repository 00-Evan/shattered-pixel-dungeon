/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class SpectralWallParticle extends PixelParticle {

	public static final Emitter.Factory FACTORY = new Emitter.Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			//scale frequency roughly to the size of the bricks used
			int type = 1 + (Dungeon.depth-1)/5;
			type = (int)GameMath.gate(1, type, 5);

			//some regions use fewer particles because they are bigger
			switch (type){
				case 2:
					//3/5 chance for a particle in prison
					if (Random.Int(5) >= 3) return;
					break;
				case 5:
					//2/5 chance for a particle in halls
					if (Random.Int(5) >= 2) return;
					break;
			}

			((SpectralWallParticle)emitter.recycle( SpectralWallParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return false;
		}
	};

	private int type = 0; //1-5 for sewers - demon halls
	private float xScale = 0, yScale = 0;

	public SpectralWallParticle() {
		super();

		lifespan = 3f;

		am = 0.6f;
	}

	public void reset( float x, float y ) {
		revive();

		type = 1 + (Dungeon.depth-1)/5;
		type = (int)GameMath.gate(1, type, 5);

		this.x = x;
		this.y = y;

		left = lifespan;

		switch (type){
			case 1:
				this.x = Math.round(x/7)*7;
				this.y = Math.round(y/4)*4 - 6;
				this.x += Math.round(this.y % 8)/4f - 1;
				color(ColorMath.random(0xD4D4D4, 0xABABAB));
				xScale = 6;
				yScale = 3;
				break;
			case 2:
				this.x = Math.round(x/7)*7;
				this.y = Math.round(y/6)*6 - 6;
				this.x += Math.round(this.y % 8)/4f - 1;
				color(ColorMath.random(0xc4be9c, 0x9c927d));
				xScale = 6;
				yScale = 5;
				break;
			case 3:
				this.y -= 6;
				float colorScale = (this.x%16)/16 + (this.y%16)/16;
				if (colorScale > 1f) colorScale = 2f - colorScale;
				color(ColorMath.interpolate(0xb7b0a5, 0x6a6662, colorScale));
				xScale = 5;
				yScale = 5;
				break;
			case 4:
				this.x = Math.round(x/5)*5;
				this.y = Math.round(y/5)*5 - 6;
				this.x += Math.round(this.y % 16)/5f - 2;
				color(ColorMath.interpolate(0xd0bca3, 0xa38d81));
				xScale = 4;
				yScale = 4;
				break;
			case 5:
				this.x = Math.round(x/4)*4;
				this.y = Math.round((y+8)/16)*16 - 14;
				color(ColorMath.interpolate(0xa2947d, 0x594847));
				xScale = 4;
				yScale = 20;
				break;
		}
	}

	public void update() {
		super.update();
		scale.set((left / lifespan) * xScale, (left / lifespan) * yScale);
	}

}
