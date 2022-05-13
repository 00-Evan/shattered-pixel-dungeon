/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashMap;

public class BadgeBanner extends Image {

	private enum State {
		FADE_IN, STATIC, FADE_OUT
	}
	private State state;

	public static final float DEFAULT_SCALE	= 3;
	public static final int SIZE = 16;
	
	private static final float FADE_IN_TIME		= 0.25f;
	private static final float STATIC_TIME		= 1f;
	private static final float FADE_OUT_TIME	= 1.75f;
	
	private int index;
	private float time;
	
	private static TextureFilm atlas;
	
	public static ArrayList<BadgeBanner> showing = new ArrayList<>();
	
	private BadgeBanner( int index ) {
		
		super( Assets.Interfaces.BADGES );
		
		if (atlas == null) {
			atlas = new TextureFilm( texture, SIZE, SIZE );
		}
		
		setup(index);
	}
	
	public void setup( int index ){
		this.index = index;
		
		frame( atlas.get( index ) );
		origin.set( width / 2, height / 2 );
		
		alpha( 0 );
		scale.set( 2 * DEFAULT_SCALE );
		
		state = State.FADE_IN;
		time = FADE_IN_TIME;
		
		Sample.INSTANCE.play( Assets.Sounds.BADGE );
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
		showing.remove(this);
		super.kill();
	}

	@Override
	public void destroy() {
		showing.remove(this);
		super.destroy();
	}

	//map to cache highlight positions so we don't have to keep looking at texture pixels
	private static HashMap<Integer, Point> highlightPositions = new HashMap<>();

	//we also hardcode any special cases
	static {
		highlightPositions.put(Badges.Badge.MASTERY_COMBO.image, new Point(3, 7));
	}

	//adds a shine to an appropriate pixel on a badge
	public static void highlight( Image image, int index ) {
		
		PointF p = new PointF();

		if (highlightPositions.containsKey(index)){
			p.x = highlightPositions.get(index).x * image.scale.x;
			p.y = highlightPositions.get(index).y * image.scale.y;
		} else {

			SmartTexture tx = TextureCache.get(Assets.Interfaces.BADGES);

			int size = 16;

			int cols = tx.width / size;
			int row = index / cols;
			int col = index % cols;

			int x = 3;
			int y = 4;
			int bgColor = tx.getPixel(col * size + x, row * size + y);
			int curColor = 0;

			for (x = 3; x <= 12; x++) {
				curColor = tx.getPixel(col * size + x, row * size + y);
				if (curColor != bgColor) break;
			}

			if (curColor == bgColor) {
				y++;
				for (x = 3; x <= 12; x++) {
					curColor = tx.getPixel(col * size + x, row * size + y);
					if (curColor != bgColor) break;
				}
			}

			p.x = x * image.scale.x;
			p.y = y * image.scale.y;

			highlightPositions.put(index, new Point(x, y));
		}

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
		BadgeBanner banner = new BadgeBanner(image);
		showing.add(banner);
		return banner;
	}

	public static boolean isShowingBadges(){
		return !showing.isEmpty();
	}
	
	public static Image image( int index ) {
		Image image = new Image( Assets.Interfaces.BADGES );
		if (atlas == null) {
			atlas = new TextureFilm( image.texture, 16, 16 );
		}
		image.frame( atlas.get( index ) );
		return image;
	}
}
