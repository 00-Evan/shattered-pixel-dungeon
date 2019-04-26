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

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Halo;
import com.watabou.utils.PointF;

public class ShieldHalo extends Halo {
	
	private CharSprite target;
	
	private float phase;
	
	public ShieldHalo( CharSprite sprite ) {
		
		//rectangular sprite to circular radius. Pythagorean theorem
		super( (float)Math.sqrt(Math.pow(sprite.width()/2f, 2) + Math.pow(sprite.height()/2f, 2)), 0xBBAACC, 1f );
		
		am = -0.33f;
		aa = +0.33f;
		
		target = sprite;
		
		phase = 1;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (phase < 1) {
			if ((phase -= Game.elapsed) <= 0) {
				killAndErase();
			} else {
				scale.set( (2 - phase) * radius / RADIUS );
				am = phase * (-1);
				aa = phase * (+1);
			}
		}
		
		if (visible = target.visible) {
			PointF p = target.center();
			point( p.x, p.y );
		}
	}
	
	@Override
	public void draw() {
		Blending.setLightMode();
		super.draw();
		Blending.setNormalMode();
	}
	
	public void putOut() {
		phase = 0.999f;
	}
	
}
