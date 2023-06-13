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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ChaserMark extends FlavourBuff implements ActionIndicator.Action {

	private static final String COUNT  = "count";

	public static final float DURATION = 10f;

	{
		type = buffType.POSITIVE;
		actPriority = VFX_PRIO-1;
	}

	public int count ;
	// 단순히 화살이 꽂힌 갯수를 세기 위한 장치
	public void count(){
		count = 3;
	}


	@Override
	public boolean attachTo(Char target) {
		ActionIndicator.setAction(this);
		return super.attachTo(target);
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
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

	@Override
	public int icon() {
		return BuffIndicator.SPIRIT_BUFF;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public String actionName() {
		SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);

		if (bow == null) return null;

		return null;
	}

	@Override
	public Image actionIcon() {

		return new BuffIcon(BuffIndicator.SPIRIT_ICON, true);
	}


	public void doAttack (float dmgBonus , int arrowcounter){
		Hero hero = Dungeon.hero;
		if (hero == null) return;

		SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
		if (bow == null) return;

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.alignment != Char.Alignment.ALLY
					&& mob.buff(ArrowMark.class) != null
					&& Dungeon.level.heroFOV[mob.pos]
					&& !(mob instanceof NPC)) {

				CellEmitter.get(mob.pos).burst(SacrificialParticle.FACTORY, 20);
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
				hero.busy();
				ActionIndicator.clearAction(this);

				if (mob.isAlive() && mob.buff(ArrowMark.class) != null) {
					if (mob.buff(ArrowMark.class).count > 0) {
						//화살개수가 0이 되면 반복이 사라짐
						Sample.INSTANCE.play(Assets.Sounds.BLAST);
						// 증강상태 * (활 레벨 + 영추자 2nd 스킬당 +1f)
						mob.damage(Math.round(arrowcounter*dmgBonus*(bow.buffedLvl() + (hero.pointsInTalent(Talent.CHASER_KILL2)))), this);
					}
					if (mob.isAlive() && mob.buff(ArrowMark.class) != null) {
						mob.buff(ArrowMark.class).count--;
					}
					if (!mob.isAlive()) {
						//적의 위치를 옮기는 장치
						if (hero.hasTalent(Talent.CHASER_UP1)) {
							Buff.affect(hero, Barkskin.class).set(bow.buffedLvl(), 10);
						}
						if (hero.pointsInTalent(Talent.CHASER_UP1) > 1){
							Buff.affect(hero, DrowsyArrowBuff.class);
							if (hero.pointsInTalent(Talent.CHASER_UP1) > 2){
								Buff.affect(hero, Talent.ChaserMarkGrabTracker.class);
							}
						}//1번스킬 2이상 일때 히어로 수면촉 부여
					}
				}

				if (mob.isAlive() && mob.buff(ArrowMark.class) != null && mob.buff(ArrowMark.class).count <= 0) {
					mob.buff(ArrowMark.class).detach();
					count=0;
				}
			}
		}
	}



	@Override
	public void doAction() {

		Hero hero = Dungeon.hero;
		if (hero == null) return;

		SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
		if (bow == null) return;

		SpiritBow.SpiritArrow arrow = bow.knockArrow();
		if (arrow == null) return;


		if (hero.buff(ChaserMark.class) != null) {
			hero.buff(ChaserMark.class).count--;
		}

		if (hero.buff(ChaserMark.class).count > 0) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob.alignment != Char.Alignment.ALLY && mob.buff(ArrowMark.class) != null
						&& Dungeon.level.heroFOV[mob.pos] && !(mob instanceof NPC)) {

					hero.sprite.operate(hero.pos);

					switch (bow.augment) {
						case NONE:
						default:
							doAttack(1f,1);
							break;
						case SPEED:
							doAttack(0.7f,1);
							break;
						case DAMAGE:
							doAttack(1.5f,1);
							break;
					}
				}
				if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos] && mob.buff(ArrowMark.class) == null){
					count = 0;
				}
				if(!(Dungeon.level.heroFOV[mob.pos])){
					count = 0;
				}
			}
		}
		if (hero.buff(ChaserMark.class).count > 0) {
			hero.sprite.operate(hero.pos, new Callback() {
				@Override
				public void call() {
					doAction();
				}
			});

		} else if (hero.buff(ChaserMark.class).count <= 0) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob.alignment != Char.Alignment.ALLY && mob.buff(ArrowMark.class) != null
						&& Dungeon.level.heroFOV[mob.pos] && !(mob instanceof NPC)) {

					int ArrowCount = mob.buff(ArrowMark.class).count;

					switch (bow.augment) {
						case NONE:
						default:
							doAttack(1f, ArrowCount);
							break;
						case SPEED:
							doAttack(0.7f, ArrowCount);
							break;
						case DAMAGE:
							doAttack(1.5f, ArrowCount);
							break;
					}
					if (mob.isAlive() && mob.buff(ArrowMark.class) != null) {
						mob.buff(ArrowMark.class).detach();
					}
				}
			}
			hero.spend(TICK);
			hero.next();
			detach();
		}
	}
}


