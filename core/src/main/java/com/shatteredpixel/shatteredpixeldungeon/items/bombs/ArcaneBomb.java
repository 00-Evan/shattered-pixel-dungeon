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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GooWarn;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ArcaneBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.ARCANE_BOMB;
	}

	@Override
	public boolean explodesDestructively() {
		return false;
	}

	@Override
	protected int explosionRange() {
		return 2;
	}

	@Override
	protected void onThrow(int cell) {
		super.onThrow(cell);
		if (fuse != null){
			PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), explosionRange() );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					GameScene.add(Blob.seed(i, 3, GooWarn.class));
				}
			}
		}
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
		
		ArrayList<Char> affected = new ArrayList<>();
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), explosionRange() );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level.heroFOV[i]) {
					CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
				}
				Char ch = Actor.findChar(i);
				if (ch != null){
					affected.add(ch);
				}
			}
		}
		
		for (Char ch : affected){
			//pierces armor, and damage in 5x5 instead of 3x3
			int damage = Math.round(Random.NormalIntRange( 4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth() ));
			ch.damage(damage, this);
			if (ch == Dungeon.hero && !ch.isAlive()){
				Badges.validateDeathFromFriendlyMagic();
				Dungeon.fail(this);
			}
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
