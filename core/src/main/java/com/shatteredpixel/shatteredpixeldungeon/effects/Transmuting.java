/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.watabou.noosa.ui.Component;

public class Transmuting extends Component {
	private static final int SIZE	= 16;

	private enum Phase {
		FADE_IN, TRANSMUTING, FADE_OUT
	}

	private static final float FADE_IN_TIME		= 0.2f;
	private static final float TRANSMUTING_TIME	= 1.4f;
	private static final float FADE_OUT_TIME	= 0.4f;

	private static final float ALPHA	= 0.6f;

	ItemSprite oldSprite;
	ItemSprite newSprite;

	private Char target;

	private Phase phase;
	private float duration;
	private float passed;

	public Transmuting( Item oldItem, Item newItem ){
		oldSprite = new ItemSprite(oldItem);
		oldSprite.originToCenter();
		add(oldSprite);
		newSprite = new ItemSprite(newItem);
		newSprite.originToCenter();
		add(newSprite);

		oldSprite.alpha(0f);
		newSprite.alpha(0f);

		phase = Phase.FADE_IN;
		duration = FADE_IN_TIME;
		passed = 0;
	}

	@Override
	public void update() {
		super.update();

		oldSprite.x = newSprite.x = target.sprite.center().x - SIZE / 2;
		oldSprite.y = newSprite.y = target.sprite.y - SIZE;

		switch (phase) {
			case FADE_IN:
				oldSprite.alpha( passed / duration * ALPHA );
				oldSprite.scale.set( passed / duration );
				break;
			case TRANSMUTING:
				oldSprite.alpha((TRANSMUTING_TIME - passed)  / duration * ALPHA);
				newSprite.alpha(passed / duration * ALPHA);
				break;
			case FADE_OUT:
				newSprite.alpha( (1 - passed / duration) * ALPHA );
				newSprite.scale.set( 1 + passed / duration );
				break;
		}

		if ((passed += Game.elapsed) > duration) {
			switch (phase) {
				case FADE_IN:
					phase = Phase.TRANSMUTING;
					duration = TRANSMUTING_TIME;
					break;
				case TRANSMUTING:
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

	public static void show( Char ch, Item oldItem, Item newItem ) {

		if (!ch.sprite.visible) {
			return;
		}

		Transmuting sprite = new Transmuting( oldItem, newItem );
		sprite.target = ch;
		ch.sprite.parent.add( sprite );
	}

}
