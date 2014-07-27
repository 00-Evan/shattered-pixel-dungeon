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
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.utils.PointF;

public class BadgeBanner extends Image {

	private enum State {
		FADE_IN, STATIC, FADE_OUT
	};
	private State state;
	
	private static final float DEFAULT_SCALE	= 3;
	
	private static final float FADE_IN_TIME		= 0.2f;
	private static final float STATIC_TIME		= 1f;
	private static final float FADE_OUT_TIME	= 1.0f;
	
	private int index;
	private float time;
	
	private static TextureFilm atlas;
	
	private static BadgeBanner current;
	
	private BadgeBanner( int index ) {
		
		super( Assets.BADGES );
		
		if (atlas == null) {
			atlas = new TextureFilm( texture, 16, 16 );
		}
		
		this.index = index;
		
		frame( atlas.get( index ) );
		origin.set( width / 2, height / 2 );
		
		alpha( 0 );
		scale.set( 2 * DEFAULT_SCALE );
		
		state = State.FADE_IN;
		time = FADE_IN_TIME;
		
		Sample.INSTANCE.play( Assets.SND_BADGE );
	}
	
	@Override
	public void update() {
		super.update();
		
		time -= Game.elapsed;
		if (time >= 0) {
			
			switch (state) {
			case FADE_IN:
				float p = time / FADE_IN_TIME;
				scale.set( (1 + p) * DEFAULT_SCALE );
				alpha( 1 - p );
				break;
			case STATIC:
				break;
			case FADE_OUT:
				alpha( time /  FADE_OUT_TIME );
				break;
			}
			
		} else {
			
			switch (state) {
			case FADE_IN:
				time = STATIC_TIME;
				state = State.STATIC;
				scale.set( DEFAULT_SCALE );
				alpha( 1 );
				highlight( this, index );
				break;
			case STATIC:
				time = FADE_OUT_TIME;
				state = State.FADE_OUT;
				break;
			case FADE_OUT:
				killAndErase();
				break;
			}
				
		}
	}
	
	@Override
	public void kill() {
		if (current == this) {
			current = null;
		}
		super.kill();
	}
	
	public static void highlight( Image image, int index ) {
		
		PointF p = new PointF();
		
		switch (index) {
		case 0:
		case 1:
		case 2:
		case 3:
			p.offset( 7, 3 );
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			p.offset( 6, 5 );
			break;
		case 8:
		case 9:
		case 10:
		case 11:
			p.offset( 6, 3 );
			break;
		case 12:
		case 13:
		case 14:
		case 15:
			p.offset( 7, 4 );
			break;
		case 16:
			p.offset( 6, 3 );
			break;
		case 17:
			p.offset( 5, 4 );
			break;
		case 18:
			p.offset( 7, 3 );
			break;
		case 20:
			p.offset( 7, 3 );
			break;
		case 21:
			p.offset( 7, 3 );
			break;
		case 22:
			p.offset( 6, 4 );
			break;
		case 23:
			p.offset( 4, 5 );
			break;
		case 24:
			p.offset( 6, 4 );
			break;
		case 25:
			p.offset( 6, 5 );
			break;
		case 26:
			p.offset( 5, 5 );
			break;
		case 27:
			p.offset( 6, 4 );
			break;
		case 28:
			p.offset( 3, 5 );
			break;
		case 29:
			p.offset( 5, 4 );
			break;
		case 30:
			p.offset( 5, 4 );
			break;
		case 31:
			p.offset( 5, 5 );
			break;
		case 32:
		case 33:
			p.offset( 7, 4 );
			break;
		case 34:
			p.offset( 6, 4 );
			break;
		case 35:
			p.offset( 6, 4 );
			break;
		case 36:
			p.offset( 6, 5 );
			break;
		case 37:
			p.offset( 4, 4 );
			break;
		case 38:
			p.offset( 5, 5 );
			break;
		case 40:
		case 41:
		case 42:
		case 43:
			p.offset( 5, 4 );
			break;
		case 44:
		case 45:
		case 46:
		case 47:
			p.offset( 5, 5 );
			break;
		case 48:
		case 49:
		case 50:
		case 51:
			p.offset( 7, 4 );
			break;
		case 52:
		case 53:
		case 54:
		case 55:
			p.offset( 4, 4 );
			break;
		case 56:
			p.offset( 3, 7 );
			break;
		case 57:
			p.offset( 4, 5 );
			break;
		case 58:
			p.offset( 6, 4 );
			break;
		case 59:
			p.offset( 7, 4 );
			break;
		case 60:
		case 61:
		case 62:
		case 63:
			p.offset( 4, 4 );
			break;
		}
		
		p.x *= image.scale.x; 
		p.y *= image.scale.y;
		p.offset( 
			-image.origin.x * (image.scale.x - 1), 
			-image.origin.y * (image.scale.y - 1) );
		p.offset( image.point() );
		
		Speck star = new Speck();
		star.reset( 0, p.x, p.y, Speck.DISCOVER );
		star.camera = image.camera();
		image.parent.add( star );
	}
	
	public static BadgeBanner show( int image ) {
		if (current != null) {
			current.killAndErase();
		}
		return (current = new BadgeBanner( image ));
	}
	
	public static Image image( int index ) {
		Image image = new Image( Assets.BADGES );
		if (atlas == null) {
			atlas = new TextureFilm( image.texture, 16, 16 );
		}
		image.frame( atlas.get( index ) );
		return image;
	}
}
