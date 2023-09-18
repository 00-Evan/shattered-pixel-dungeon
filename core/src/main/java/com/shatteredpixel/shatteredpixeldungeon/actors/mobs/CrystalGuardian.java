/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalGuardianSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CrystalGuardian extends Mob{

	{
		HP = HT = 1;
		spriteClass = CrystalGuardianSprite.class;

		SLEEPING = new Sleeping();
		state = SLEEPING;
	}

	public CrystalGuardian(){
		super();
		switch (Random.Int(3)){
			case 0: default:
				spriteClass = CrystalGuardianSprite.Blue.class;
				break;
			case 1:
				spriteClass = CrystalGuardianSprite.Green.class;
				break;
			case 2:
				spriteClass = CrystalGuardianSprite.Red.class;
				break;
		}
	}

	@Override
	public float spawningWeight() {
		return 0;
	}

	@Override
	public float speed() {
		if (Dungeon.level.openSpace[pos]) {
			return super.speed();
		} else {
			return super.speed()/4f;
		}
	}

	@Override
	public void beckon(int cell) {
		super.beckon(cell);

		//If we are still penned into our starting area, break out of it
		PathFinder.buildDistanceMap(cell, Dungeon.level.passable);
		if (PathFinder.distance[pos] == Integer.MAX_VALUE){
			boolean[] passable = Dungeon.level.passable.clone();
			for (int i = 0; i < Dungeon.level.length(); i++){
				passable[i] = passable[i] || Dungeon.level.map[i] == Terrain.MINE_CRYSTAL;
			}
			PathFinder.Path p = PathFinder.find(pos, cell, passable);
			if (p != null) {
				for (int i : p) {
					if (Dungeon.level.map[i] == Terrain.MINE_CRYSTAL) {
						Level.set(i, Terrain.EMPTY);
						GameScene.updateMap(i);
					}
				}
			}
		}
	}

	protected class Sleeping extends Mob.Sleeping{

		@Override
		protected void awaken(boolean enemyInFOV) {
			if (enemyInFOV){
				//do not wake up if we see an enemy we can't actually reach
				PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.passable);
				if (PathFinder.distance[pos] == Integer.MAX_VALUE){
					return;
				}
			}
			super.awaken(enemyInFOV);
		}
	}

	public static final String SPRITE = "sprite";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPRITE, spriteClass);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		spriteClass = bundle.getClass(SPRITE);
	}
}
