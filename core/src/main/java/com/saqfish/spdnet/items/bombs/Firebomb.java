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

package com.saqfish.spdnet.items.bombs;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.blobs.Blob;
import com.saqfish.spdnet.actors.blobs.Fire;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.particles.FlameParticle;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class Firebomb extends Bomb {
	
	{
		image = ItemSpriteSheet.FIRE_BOMB;
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level.pit[i])
					GameScene.add(Blob.seed(i, 2, Fire.class));
				else
					GameScene.add(Blob.seed(i, 10, Fire.class));
				CellEmitter.get(i).burst(FlameParticle.FACTORY, 5);
			}
		}
		Sample.INSTANCE.play(Assets.Sounds.BURNING);
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
