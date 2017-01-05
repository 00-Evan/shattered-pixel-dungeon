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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;

public class Surprise extends Image {

	private static final float TIME_TO_FADE = 0.8f;

	private float time;

	public Surprise() {
		super(Effects.get(Effects.Type.EXCLAMATION));
		origin.set(width / 2, height / 2);
	}

	public void reset(int p) {
		revive();

		x = (p % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

		time = TIME_TO_FADE;
	}

	public void reset(Visual v) {
		revive();

		point(v.center(this));

		time = TIME_TO_FADE;
	}

	@Override
	public void update() {
		super.update();

		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha(p);
			scale.y = 1 + p/2;
		}
	}

	public static void hit(Char ch) {
		hit(ch, 0);
	}

	public static void hit(Char ch, float angle) {
		if (ch.sprite.parent != null) {
			Surprise s = (Surprise) ch.sprite.parent.recycle(Surprise.class);
			ch.sprite.parent.bringToFront(s);
			s.reset(ch.sprite);
			s.angle = angle;
		}
	}

	public static void hit(int pos) {
		hit(pos, 0);
	}

	public static void hit(int pos, float angle) {
		Group parent = Dungeon.hero.sprite.parent;
		Surprise s = (Surprise) parent.recycle(Surprise.class);
		parent.bringToFront(s);
		s.reset(pos);
		s.angle = angle;
	}
}
