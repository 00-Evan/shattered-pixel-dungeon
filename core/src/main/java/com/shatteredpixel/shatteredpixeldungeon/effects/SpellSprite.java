/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

import java.util.HashMap;

public class SpellSprite extends Image {

	public static final int FOOD		= 0;
	public static final int MAP			= 1;
	public static final int CHARGE		= 2;
	public static final int MASTERY		= 3;
	public static final int BERSERK     = 4;
	
	private static final int SIZE	= 16;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	};
	
	private static final float FADE_IN_TIME		= 0.2f;
	private static final float STATIC_TIME		= 0.8f;
	private static final float FADE_OUT_TIME	= 0.4f;
	
	private static TextureFilm film;
	
	private Char target;
	
	private Phase phase;
	private float duration;
	private float passed;
	
	private static HashMap<Char,SpellSprite> all = new HashMap<Char, SpellSprite>();
	
	public SpellSprite() {
		super( Assets.SPELL_ICONS );
		
		if (film == null) {
			film = new TextureFilm( texture, SIZE );
		}
	}
	
	public void reset( int index ) {
		frame( film.get( index ) );
		origin.set( width / 2, height / 2 );
		
		phase = Phase.FADE_IN;
		
		duration = FADE_IN_TIME;
		passed = 0;
	}
	
	@Override
	public void update() {
		super.update();
		
		x = target.sprite.center().x - SIZE / 2;
		y = target.sprite.y - SIZE;
		
		switch (phase) {
		case FADE_IN:
			alpha( passed / duration );
			scale.set( passed / duration );
			break;
		case STATIC:
			break;
		case FADE_OUT:
			alpha( 1 - passed / duration );
			break;
		}
		
		if ((passed += Game.elapsed) > duration) {
			switch (phase) {
			case FADE_IN:
				phase = Phase.STATIC;
				duration = STATIC_TIME;
				break;
			case STATIC:
				phase = Phase.FADE_OUT;
				duration = FADE_OUT_TIME;
				break;
			case FADE_OUT:
				kill();
				break;
			}
			
			passed = 0;
		}
	}
	
	@Override
	public void kill() {
		super.kill();
		all.remove( target );
	}
	
	public static void show( Char ch, int index ) {
		
		if (!ch.sprite.visible) {
			return;
		}
		
		SpellSprite old = all.get( ch );
		if (old != null) {
			old.kill();
		}
		
		SpellSprite sprite = GameScene.spellSprite();
		sprite.revive();
		sprite.reset( index );
		sprite.target = ch;
		all.put( ch,  sprite );
	}
}
