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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.CorgSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class Shovel extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.SHOVEL;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		tier = 3;
	}

	public int droppedJewelDummy = 0;

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +
				lvl*(tier+1);
	}//메이스랑 같음

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (ch instanceof Hero && ch.buff(Charger.class) == null && !(Dungeon.hero.heroClass == HeroClass.DUELIST)){
			Buff.affect(ch, Charger.class);
			if(ch.buff(Charger.class) != null) Buff.affect(ch, Charger.class).charges -=3;
		}
	}

	@Override
	public String defaultAction() {
		if (Dungeon.hero != null && !(Dungeon.hero.heroClass == HeroClass.DUELIST)) {
			return AC_ABILITY;
		} else {
			return super.defaultAction();
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && !(Dungeon.hero.heroClass == HeroClass.DUELIST)){
			actions.add(AC_ABILITY);
		}
		return actions;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (hero.buff(MeleeWeapon.Charger.class) != null && hero.heroClass != HeroClass.DUELIST ){
				hero.buff(MeleeWeapon.Charger.class).detach();
			}
			ActionIndicator.updateIcon();
			return true;
		}
		return false;
	}



	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Shovel.ShovelAbility(hero, target,1f, this);
	}

	public static void ShovelAbility(Hero hero, Integer target, float dmgMulti, MeleeWeapon wep){
		if (target == null) {
			return;
		}

		Ballistica jeweldummy = new Ballistica(hero.pos, target, Ballistica.STOP_TARGET );
		Heap h = Dungeon.level.heaps.get(jeweldummy.collisionPos);
		Char enemy = Actor.findChar(target);

		if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
			if (h != null && h.type == Heap.Type.JEWEL_DUMMY
					&& !(hero.rooted || Dungeon.level.distance(hero.pos, target) > wep.reachFactor(hero))){
				//흙더미 로직
				hero.sprite.highjump(hero.pos, target,10,0.2f, new Callback() {
				@Override
				public void call() {
					Sample.INSTANCE.play(Assets.Sounds.HIT_CRUSH);
					Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
					h.destroy();
					Dungeon.level.drop( new Gold(Random.Int(100)), target).sprite.drop();
					hero.sprite.highjump(target, target,10,0.17f, new Callback() {
						@Override
						public void call() {
							hero.pos = target;
							hero.spendAndNext(Actor.TICK);
						}
					});
				}
			});
			return;
			} else {
				GLog.w(Messages.get(wep, "ability_no_target"));
				return;
			}
		}

		hero.belongings.abilityWeapon = wep;
		if (!hero.canAttack(enemy) && (hero.rooted || Dungeon.level.distance(hero.pos, target) > wep.reachFactor(hero))){
			GLog.w(Messages.get(wep, "ability_bad_position"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;


		Ballistica firstjump = new Ballistica(hero.pos, target, Ballistica.STOP_CHARS );

		int firstcell = firstjump.collisionPos;

		hero.busy();
		int start = hero.pos;
		hero.sprite.highjump(hero.pos, firstcell,10,0.2f, new Callback() {
			@Override
			public void call() {
				//한번 더 뛸때 계산
				Ballistica endjump = new Ballistica(start, target, Ballistica.STOP_CHARS );

				int cell = endjump.collisionPos;

				int returnjump = endjump.dist;
				while (Actor.findChar(cell) != null ) {
					try{
						cell = endjump.path.get(returnjump);
						returnjump++;
						//타겟 위치에 적이 있으면 넘어감
					}
					catch (IndexOutOfBoundsException e){
						break;
					}
				}
				final int dest = cell;

				hero.pos = firstcell;
				//데미지 계산식
				if (enemy != null) {
					wep.beforeAbilityUsed(hero);
					if (Dungeon.hero.heroClass == HeroClass.DUELIST) {
						if (hero.attack(enemy, dmgMulti*1.25f, 0, Char.INFINITE_ACCURACY)) {
							Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
							Buff.prolong(hero, ShovelTracker.class, 5f).count++;
							if (!enemy.isAlive()) {
								wep.onAbilityKill(hero);
							}
						}
					}else {
						//결투자를 제외한 영웅의 위력 계산식
						if (hero.attack(enemy, dmgMulti,0, 1f)) {
							if (hero.subClass == HeroSubClass.GLADIATOR){
								Buff.affect( hero, Combo.class ).hit( enemy );
							}
							Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
						}
					}
					Invisibility.dispel();
		    	}
				//두번째 점프 계산

				if (Dungeon.level.solid[start]) ScrollOfTeleportation.teleportChar(hero);

				if (Actor.findChar(dest) == null && Dungeon.level.passable[dest] || (Dungeon.level.avoid[dest] && hero.flying)) {
					hero.sprite.highjump(firstcell, dest,10,0.17f, new Callback() {
						@Override
						public void call() {
							hero.pos = dest;
							Dungeon.level.occupyCell(hero);
							hero.spendAndNext(hero.attackDelay());
							wep.afterAbilityUsed(hero);
						}
					});
				}else{
					hero.sprite.highjump(firstcell, start,10,0.17f, new Callback() {
						@Override
						public void call() {
							hero.pos = start;
							Dungeon.level.occupyCell(hero);
							hero.spendAndNext(hero.attackDelay());
							wep.afterAbilityUsed(hero);
						}
					});
				}

			}
		});
	}

	public static class ShovelTracker extends FlavourBuff {
		//삽의 기본공격을 40% 강화

		{
			type = buffType.POSITIVE;
		}

		public int count ;

		private static final String COUNT  = "count";

		public String desc() {
			return Messages.get(this, "desc", dispTurns() , count);}

		@Override
		public int icon() {
			return BuffIndicator.DUEL_CLEAVE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0f, 1f, 1f);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (5 - visualcooldown()) / 5);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(COUNT, count);

		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			count = bundle.getInt(COUNT);

		}
	}

	public static class JewelDummyDropped extends CounterBuff {{revivePersists = true;}};
}
