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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DemonSpawner extends Mob {

	{
		spriteClass = SpawnerSprite.class;

		HP = HT = 150;
		defenseSkill = 0;

		EXP = 25;
		maxLvl = 29;

		state = PASSIVE;

		loot = PotionOfHealing.class;
		lootChance = 1f;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.MINIBOSS);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 12);
	}

	@Override
	public void beckon(int cell) {
		//do nothing
	}

	private float spawnCooldown = 50;

	@Override
	protected boolean act() {
		spawnCooldown--;
		if (spawnCooldown <= 0){
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				if (Dungeon.level.passable[pos+n] && Actor.findChar( pos+n ) == null) {
					candidates.add( pos+n );
				}
			}

			if (!candidates.isEmpty()) {
				RipperDemon spawn = new RipperDemon();

				spawn.pos = Random.element( candidates );
				spawn.state = spawn.HUNTING;

				Dungeon.level.occupyCell(spawn);

				GameScene.add( spawn );
				if (sprite.visible) {
					Actor.addDelayed(new Pushing(spawn, pos, spawn.pos), -1);
				}

				spawnCooldown += 50;
			}
		}
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 25){
			//takes 25/26/27/28/29/30/31/32/33/34/35 dmg
			// at   25/27/30/34/39/45/52/60/69/79/90 incoming dmg
			dmg = 24 + (int)(Math.sqrt(8*(dmg - 24) + 1) - 1)/2;
		}
		spawnCooldown -= dmg;
		super.damage(dmg, src);
	}

	{
		immunities.add( Paralysis.class );
		immunities.add( Amok.class );
		immunities.add( Sleep.class );
		immunities.add( Terror.class );
		immunities.add( Vertigo.class );
	}
}
