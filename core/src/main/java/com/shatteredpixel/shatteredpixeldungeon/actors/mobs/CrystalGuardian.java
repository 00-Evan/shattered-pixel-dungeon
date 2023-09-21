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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalGuardianSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CrystalGuardian extends Mob{

	{
		spriteClass = CrystalGuardianSprite.class;

		HP = HT = 100;
		defenseSkill = 14;

		EXP = 10;
		maxLvl = -2;

		SLEEPING = new Sleeping();
		state = SLEEPING;

		properties.add(Property.INORGANIC);
		properties.add(Property.MINIBOSS);
	}

	@Override
	protected boolean act() {
		if (sprite instanceof CrystalGuardianSprite) ((CrystalGuardianSprite) sprite).endCrumple();
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10, 20 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 20;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 10);
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean isAlive() {
		if (HP <= 0){
			HP = 1;
			if (sprite != null) ((CrystalGuardianSprite)sprite).crumple();
			spend(20f-cooldown());
			Buff.affect(this, Healing.class).setHeal(100, 0, 5);
		}
		return super.isAlive();
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
	public void move(int step, boolean travelling) {
		super.move(step, travelling);
		if (Dungeon.level.map[pos] == Terrain.MINE_CRYSTAL){
			Level.set(pos, Terrain.EMPTY);
			GameScene.updateMap(pos);
			if (Dungeon.level.heroFOV[pos]){
				Splash.at(pos, 0xFFFFFF, 5);
				Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			}
		}
	}

	@Override
	public boolean[] modifyPassable(boolean[] passable) {
		//if we are hunting, and our current target is not reachable otherwise, then we can step on crystals
		if (state == HUNTING){
			PathFinder.buildDistanceMap(target, passable);

			if (PathFinder.distance[pos] == Integer.MAX_VALUE) {
				for (int i = 0; i < Dungeon.level.length(); i++) {
					passable[i] = passable[i] || Dungeon.level.map[i] == Terrain.MINE_CRYSTAL;
				}
			}
		}
		return passable;
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
