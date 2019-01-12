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

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class Chains extends Group {

	private static final double A = 180 / Math.PI;

	private float spent = 0f;
	private float duration;

	private Callback callback;

	private Image[] chains;
	private int numChains;
	private float distance;
	private float rotation = 0;

	private PointF from, to;

	public Chains(int from, int to, Callback callback){
		this(DungeonTilemap.tileCenterToWorld(from),
				DungeonTilemap.tileCenterToWorld(to),
				callback);
	}

	public Chains(PointF from, PointF to, Callback callback){
		super();

		this.callback = callback;

		this.from = from;
		this.to = to;

		float dx = to.x - from.x;
		float dy = to.y - from.y;
		distance = (float)Math.hypot(dx, dy);


		duration = distance/300f + 0.1f;

		rotation = (float)(Math.atan2( dy, dx ) * A) + 90f;

		numChains = Math.round(distance/6f)+1;

		chains = new Image[numChains];
		for (int i = 0; i < chains.length; i++){
			chains[i] = new Image(Effects.get(Effects.Type.CHAIN));
			chains[i].angle = rotation;
			chains[i].origin.set( chains[i].width()/ 2, chains[i].height() );
			add(chains[i]);
		}
	}

	@Override
	public void update() {
		if ((spent += Game.elapsed) > duration) {

			killAndErase();
			if (callback != null) {
				callback.call();
			}

		} else {
			float dx = to.x - from.x;
			float dy = to.y - from.y;
			for (int i = 0; i < chains.length; i++) {
				chains[i].center(new PointF(
						from.x + ((dx * (i / (float)chains.length)) * (spent/duration)),
						from.y + ((dy * (i / (float)chains.length)) * (spent/duration))
				));
			}
		}
	}

}
