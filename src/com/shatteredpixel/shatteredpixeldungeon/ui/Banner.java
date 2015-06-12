/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class Banner extends Image {

	private enum State {
		FADE_IN, STATIC, FADE_OUT
	};
	private State state;
	
	private float time;
	
	private int color;
	private float fadeTime;
	private float showTime;
	
	public Banner( Image sample ) {
		super();
		copy( sample );
		alpha( 0 );
	}
	
	public Banner( Object tx ) {
		super( tx );
		alpha( 0 );
	}
	
	public void show( int color, float fadeTime, float showTime ) {
		
		this.color = color;
		this.fadeTime = fadeTime;
		this.showTime = showTime;
		
		state = State.FADE_IN;
		
		time = fadeTime;
	}
	
	public void show( int color, float fadeTime ) {
		show( color, fadeTime, Float.MAX_VALUE );
	}
	
	@Override
	public void update() {
		super.update();
		
		time -= Game.elapsed;
		if (time >= 0) {
			
			float p = time / fadeTime;
			
			switch (state) {
			case FADE_IN:
				tint( color, p );
				alpha( 1 - p );
				break;
			case STATIC:
				break;
			case FADE_OUT:
				alpha( p );
				break;
			}
			
		} else {
			
			switch (state) {
			case FADE_IN:
				time = showTime;
				state = State.STATIC;
				break;
			case STATIC:
				time = fadeTime;
				state = State.FADE_OUT;
				break;
			case FADE_OUT:
				killAndErase();
				break;
			}
				
		}
	}
}
