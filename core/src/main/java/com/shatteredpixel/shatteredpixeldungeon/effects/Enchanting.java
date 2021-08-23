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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.Game;

public class Enchanting extends ItemSprite {
	private static final int SIZE	= 16;

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private static final float FADE_IN_TIME		= 0.2f;
	private static final float STATIC_TIME		= 1.0f;
	private static final float FADE_OUT_TIME	= 0.4f;

	private static final float ALPHA	= 0.6f;

	private int color;

	private Char target;

	private Phase phase;
	private float duration;
	private float passed;

	public Enchanting( Item item ) {
		super( item.image(), null );
		originToCenter();

		color = item.glowing().color;

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
				alpha( passed / duration * ALPHA );
				scale.set( passed / duration );
				break;
			case STATIC:
				tint( color, passed / duration * 0.8f );
				break;
			case FADE_OUT:
				alpha( (1 - passed / duration) * ALPHA );
				scale.set( 1 + passed / duration );
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

	public static void show( Char ch, Item item ) {

		if (!ch.sprite.visible) {
			return;
		}

		Enchanting sprite = new Enchanting( item );
		sprite.target = ch;
		ch.sprite.parent.add( sprite );
	}
}