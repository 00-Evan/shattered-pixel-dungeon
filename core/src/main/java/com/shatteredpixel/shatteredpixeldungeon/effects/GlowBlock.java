/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;

public class GlowBlock extends Gizmo {

	private CharSprite target;

	public GlowBlock(CharSprite target ) {
		super();

		this.target = target;
	}

	@Override
	public void update() {
		super.update();

		//wavers between 0.4f and 0.6f once per second
		target.tint(1.33f, 1.33f, 0.83f, 0.5f + 0.1f*(float)Math.cos(Math.PI*2*Game.timeTotal));

	}

	public void darken() {

		target.resetColor();
		killAndErase();

	}

	public static GlowBlock lighten(CharSprite sprite ) {

		GlowBlock glowBlock = new GlowBlock( sprite );
		if (sprite.parent != null) {
			sprite.parent.add(glowBlock);
		}

		return glowBlock;
	}

}
