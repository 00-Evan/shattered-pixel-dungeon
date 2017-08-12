/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

import android.opengl.GLES20;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

import javax.microedition.khronos.opengles.GL10;

//FIXME the healthbar added here is a quick fix.
// The game should have a much more flexible health bar system which works for any character
// However I want a ghost HP bar to get into 0.6.1, so this will have to do for now.
public class GhostSprite extends MobSprite {
	
	private HealthBar hpBar;
	
	public GhostSprite() {
		super();
		
		texture( Assets.GHOST );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1 );

		attack = new Animation( 10, false );
		attack.frames( frames, 0, 2, 3 );

		die = new Animation( 8, false );
		die.frames( frames, 0, 4, 5, 6, 7 );
		
		play( idle );
	}
	
	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof DriedRose.GhostHero){
			final Char finalCH = ch;
			hpBar = new HealthBar(){
				@Override
				public synchronized void update() {
					super.update();
					hpBar.setRect(finalCH.sprite.x, finalCH.sprite.y-3, finalCH.sprite.width, hpBar.height());
					hpBar.level( finalCH );
					visible = finalCH.sprite.visible;
				}
			};
			((GameScene)ShatteredPixelDungeon.scene()).ghostHP.add(hpBar);
		}
	}
	
	@Override
	public void draw() {
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
		super.draw();
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}
	
	@Override
	public void die() {
		super.die();
		if (hpBar != null) hpBar.killAndErase();
		emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
		emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
	}
	
	@Override
	public int blood() {
		return 0xFFFFFF;
	}
}
