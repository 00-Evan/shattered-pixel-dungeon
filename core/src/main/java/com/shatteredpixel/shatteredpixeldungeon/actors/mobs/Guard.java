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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Guard extends Mob {

	//they can only use their chains once
	private boolean chainsUsed = false;

	{
		spriteClass = GuardSprite.class;

		HP = HT = 40;
		defenseSkill = 10;

		EXP = 6;
		maxLvl = 14;

		loot = null;    //see createloot.
		lootChance = 0.25f;

		properties.add(Property.DEMONIC);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(4, 12);
	}

	@Override
	protected boolean act() {
		Dungeon.level.updateFieldOfView( this, Level.fieldOfView );

		if (state == HUNTING &&
				paralysed <= 0 &&
				enemy != null &&
				enemy.invisible == 0 &&
				Level.fieldOfView[enemy.pos] &&
				Dungeon.level.distance( pos, enemy.pos ) < 5 &&
				!Dungeon.level.adjacent( pos, enemy.pos ) &&
				Random.Int(3) == 0 &&

				chain(enemy.pos)) {

			return false;

		} else {
			return super.act();
		}
	}

	private boolean chain(int target){
		if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
			return false;

		Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

		if (chain.collisionPos != enemy.pos || chain.path.size() < 2 || Level.pit[chain.path.get(1)])
			return false;
		else {
			int newPos = -1;
			for (int i : chain.subPath(1, chain.dist)){
				if (!Level.solid[i] && Actor.findChar(i) == null){
					newPos = i;
					break;
				}
			}

			if (newPos == -1){
				return false;
			} else {
				final int newPosFinal = newPos;
				yell( Messages.get(this, "scorpion") );
				sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback() {
					public void call() {
						Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback(){
							public void call() {
								enemy.pos = newPosFinal;
								Dungeon.level.press(newPosFinal, enemy);
								Cripple.prolong(enemy, Cripple.class, 4f);
								if (enemy == Dungeon.hero) {
									Dungeon.hero.interrupt();
									Dungeon.observe();
									GameScene.updateFog();
								}
							}
						}), -1);
						next();
					}
				}));
			}
		}
		chainsUsed = true;
		return true;
	}

	@Override
	public int attackSkill( Char target ) {
		return 14;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}

	@Override
	protected Item createLoot() {
		//first see if we drop armor, overall chance is 1/8
		if (Random.Int(2) == 0){
			Armor loot;
			do{
				loot = Generator.randomArmor();
				//50% chance of re-rolling tier 4 or 5 items
			} while (loot.tier >= 4 && Random.Int(2) == 0);
			loot.level(0);
			return loot;
		//otherwise, we may drop a health potion. overall chance is 7/(8 * (7 + potions dropped))
		//with 0 potions dropped that simplifies to 1/8
		} else {
			if (Random.Int(7 + Dungeon.limitedDrops.guardHP.count) < 7){
				Dungeon.limitedDrops.guardHP.drop();
				return new PotionOfHealing();
			}
		}

		return null;
	}

	private final String CHAINSUSED = "chainsused";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CHAINSUSED, chainsUsed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		chainsUsed = bundle.getBoolean(CHAINSUSED);
	}
}
