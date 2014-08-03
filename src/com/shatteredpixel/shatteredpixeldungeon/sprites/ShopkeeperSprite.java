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
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.PixelParticle;
import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class ShopkeeperSprite extends MobSprite {
	
	private PixelParticle coin;
	
	public ShopkeeperSprite() {
		super();
		
		texture( Assets.KEEPER );
		TextureFilm film = new TextureFilm( texture, 14, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( film, 1, 1, 1, 1, 1, 0, 0, 0, 0 );
		
		run = idle.clone();
		die = idle.clone();
		attack = idle.clone();
		
		idle();
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		
		if (visible && anim == idle) {
			if (coin == null) {
				coin = new PixelParticle();
				parent.add( coin );
			}
			coin.reset( x + (flipHorizontal ? 0 : 13), y + 7, 0xFFFF00, 1, 0.5f );
			coin.speed.y = -40;
			coin.acc.y = +160;
		}
	}
}
