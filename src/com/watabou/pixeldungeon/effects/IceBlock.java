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
package com.watabou.pixeldungeon.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.sprites.CharSprite;

public class IceBlock extends Gizmo {
	
	private float phase; 
	
	private CharSprite target;
	
	public IceBlock( CharSprite target ) {
		super();

		this.target = target;
		phase = 0;
	}
	
	@Override
	public void update() {
		super.update();

		if ((phase += Game.elapsed * 2) < 1) {
			target.tint( 0.83f, 1.17f, 1.33f, phase * 0.6f );
		} else {
			target.tint( 0.83f, 1.17f, 1.33f, 0.6f );
		}
	}
	
	public void melt() {

		target.resetColor();
		killAndErase();

		if (visible) {
			Splash.at( target.center(), 0xFFB2D6FF, 5 );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}
	}
	
	public static IceBlock freeze( CharSprite sprite ) {
		
		IceBlock iceBlock = new IceBlock( sprite );
		sprite.parent.add( iceBlock );
		
		return iceBlock;
	}
}
