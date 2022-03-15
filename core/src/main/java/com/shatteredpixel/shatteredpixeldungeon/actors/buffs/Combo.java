/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Combo extends Buff implements ActionIndicator.Action {
	
	private int count = 0;
	private float comboTime = 0f;
	private float initialComboTime = 5f;

	@Override
	public int icon() {
		return BuffIndicator.COMBO;
	}
	
	@Override
	public void tintIcon(Image icon) {
		ComboMove move = getHighestMove();
		if (move != null){
			icon.hardlight(move.tintColor & 0x00FFFFFF);
		} else {
			icon.resetColor();
		}
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (initialComboTime - comboTime)/ initialComboTime);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)comboTime);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void hit( Char enemy ) {

		count++;
		comboTime = 5f;

		if (!enemy.isAlive() || (enemy.buff(Corruption.class) != null && enemy.HP == enemy.HT)){
			comboTime = Math.max(comboTime, 15*((Hero)target).pointsInTalent(Talent.CLEAVE));
		}

		initialComboTime = comboTime;

		if ((getHighestMove() != null)) {

			ActionIndicator.setAction( this );
			Badges.validateMasteryCombo( count );

			GLog.p( Messages.get(this, "combo", count) );
			
		}

		BuffIndicator.refreshHero(); //refresh the buff visually on-hit

	}

	public void addTime( float time ){
		comboTime += time;
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}

	@Override
	public boolean act() {
		comboTime-=TICK;
		spend(TICK);
		if (comboTime <= 0) {
			detach();
		}
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", count, dispTurns(comboTime));
	}

	private static final String COUNT = "count";
	private static final String TIME  = "combotime";
	private static final String INITIAL_TIME  = "initialComboTime";

	private static final String CLOBBER_USED = "clobber_used";
	private static final String PARRY_USED   = "parry_used";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
		bundle.put(TIME, comboTime);
		bundle.put(INITIAL_TIME, initialComboTime);

		bundle.put(CLOBBER_USED, clobberUsed);
		bundle.put(PARRY_USED, parryUsed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );
		comboTime = bundle.getFloat( TIME );

		initialComboTime = bundle.getFloat( INITIAL_TIME );

		clobberUsed = bundle.getBoolean(CLOBBER_USED);
		parryUsed = bundle.getBoolean(PARRY_USED);

		if (getHighestMove() != null) ActionIndicator.setAction(this);
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action_name");
	}

	@Override
	public Image actionIcon() {
		Image icon;
		if (((Hero)target).belongings.weapon() != null){
			icon = new ItemSprite(((Hero)target).belongings.weapon().image, null);
		} else {
			icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
		}

		icon.tint(getHighestMove().tintColor);
		return icon;
	}

	@Override
	public void doAction() {
		GameScene.show(new WndCombo(this));
	}

	public enum ComboMove {
		CLOBBER(2, 0xFF00FF00),
		SLAM   (4, 0xFFCCFF00),
		PARRY  (6, 0xFFFFFF00),
		CRUSH  (8, 0xFFFFCC00),
		FURY   (10, 0xFFFF0000);

		public int comboReq, tintColor;

		ComboMove(int comboReq, int tintColor){
			this.comboReq = comboReq;
			this.tintColor = tintColor;
		}

		public String desc(int count){
			switch (this){
				default:
					return Messages.get(this, name()+"_desc");
				case SLAM:
					return Messages.get(this, name()+"_desc", count*20);
				case CRUSH:
					return Messages.get(this, name()+"_desc", count*25);
			}

		}

	}

	private boolean clobberUsed = false;
	private boolean parryUsed = false;

	public ComboMove getHighestMove(){
		ComboMove best = null;
		for (ComboMove move : ComboMove.values()){
			if (count >= move.comboReq){
				best = move;
			}
		}
		return best;
	}

	public int getComboCount(){
		return count;
	}

	public boolean canUseMove(ComboMove move){
		if (move == ComboMove.CLOBBER && clobberUsed)   return false;
		if (move == ComboMove.PARRY && parryUsed)       return false;
		return move.comboReq <= count;
	}

	public void useMove(ComboMove move){
		if (move == ComboMove.PARRY){
			parryUsed = true;
			comboTime = 5f;
			Invisibility.dispel();
			Buff.affect(target, ParryTracker.class, Actor.TICK);
			((Hero)target).spendAndNext(Actor.TICK);
			Dungeon.hero.busy();
		} else {
			moveBeingUsed = move;
			GameScene.selectCell(listener);
		}
	}

	public static class ParryTracker extends FlavourBuff{
		{ actPriority = HERO_PRIO+1;}

		public boolean parried;

		@Override
		public void detach() {
			if (!parried && target.buff(Combo.class) != null) target.buff(Combo.class).detach();
			super.detach();
		}
	}

	public static class RiposteTracker extends Buff{
		{ actPriority = VFX_PRIO;}

		public Char enemy;

		@Override
		public boolean act() {
			if (target.buff(Combo.class) != null) {
				moveBeingUsed = ComboMove.PARRY;
				target.sprite.attack(enemy.pos, new Callback() {
					@Override
					public void call() {
						target.buff(Combo.class).doAttack(enemy);
						next();
					}
				});
				detach();
				return false;
			} else {
				detach();
				return true;
			}
		}
	}

	private static ComboMove moveBeingUsed;

	private void doAttack(final Char enemy) {

		AttackIndicator.target(enemy);

		boolean wasAlly = enemy.alignment == target.alignment;
		Hero hero = (Hero) target;

		float dmgMulti = 1f;
		int dmgBonus = 0;

		//variance in damage dealt
		switch (moveBeingUsed) {
			case CLOBBER:
				dmgMulti = 0;
				break;
			case SLAM:
				dmgBonus = Math.round(target.drRoll() * count / 5f);
				break;
			case CRUSH:
				dmgMulti = 0.25f * count;
				break;
			case FURY:
				dmgMulti = 0.6f;
				break;
		}

		if (hero.attack(enemy, dmgMulti, dmgBonus, Char.INFINITE_ACCURACY)){
			//special on-hit effects
			switch (moveBeingUsed) {
				case CLOBBER:
					hit(enemy);
					//trace a ballistica to our target (which will also extend past them
					Ballistica trajectory = new Ballistica(target.pos, enemy.pos, Ballistica.STOP_TARGET);
					//trim it to just be the part that goes past them
					trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
					//knock them back along that ballistica, ensuring they don't fall into a pit
					int dist = 2;
					if (enemy.isAlive() && count >= 7 && hero.pointsInTalent(Talent.ENHANCED_COMBO) >= 1) {
						dist++;
						Buff.prolong(enemy, Vertigo.class, 3);
					} else if (!enemy.flying) {
						while (dist > trajectory.dist ||
								(dist > 0 && Dungeon.level.pit[trajectory.path.get(dist)])) {
							dist--;
						}
					}
					WandOfBlastWave.throwChar(enemy, trajectory, dist, true, false);
					break;
				case PARRY:
					hit(enemy);
					break;
				case CRUSH:
					WandOfBlastWave.BlastWave.blast(enemy.pos);
					PathFinder.buildDistanceMap(target.pos, BArray.not(Dungeon.level.solid, null), 3);
					for (Char ch : Actor.chars()) {
						if (ch != enemy && ch.alignment == Char.Alignment.ENEMY
								&& PathFinder.distance[ch.pos] < Integer.MAX_VALUE) {
							int aoeHit = Math.round(target.damageRoll() * 0.25f * count);
							aoeHit /= 2;
							aoeHit -= ch.drRoll();
							if (ch.buff(Vulnerable.class) != null) aoeHit *= 1.33f;
							ch.damage(aoeHit, target);
							ch.sprite.bloodBurstA(target.sprite.center(), aoeHit);
							ch.sprite.flash();

							if (!ch.isAlive()) {
								if (hero.hasTalent(Talent.LETHAL_DEFENSE) && hero.buff(BrokenSeal.WarriorShield.class) != null) {
									BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
									shield.supercharge(Math.round(shield.maxShield() * hero.pointsInTalent(Talent.LETHAL_DEFENSE) / 3f));
								}
							}
						}
					}
					break;
				default:
					//nothing
					break;
			}
		}

		Invisibility.dispel();

		//Post-attack behaviour
		switch(moveBeingUsed){
			case CLOBBER:
				clobberUsed = true;
				if (getHighestMove() == null) ActionIndicator.clearAction(Combo.this);
				hero.spendAndNext(hero.attackDelay());
				break;

			case PARRY:
				//do nothing
				break;

			case FURY:
				count--;
				//fury attacks as many times as you have combo count
				if (count > 0 && enemy.isAlive() && hero.canAttack(enemy) &&
						(wasAlly || enemy.alignment != target.alignment)){
					target.sprite.attack(enemy.pos, new Callback() {
						@Override
						public void call() {
							doAttack(enemy);
						}
					});
				} else {
					detach();
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					ActionIndicator.clearAction(Combo.this);
					hero.spendAndNext(hero.attackDelay());
				}
				break;

			default:
				detach();
				ActionIndicator.clearAction(Combo.this);
				hero.spendAndNext(hero.attackDelay());
				break;
		}

		if (!enemy.isAlive() || (!wasAlly && enemy.alignment == target.alignment)) {
			if (hero.hasTalent(Talent.LETHAL_DEFENSE) && hero.buff(BrokenSeal.WarriorShield.class) != null){
				BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
				shield.supercharge(Math.round(shield.maxShield() * hero.pointsInTalent(Talent.LETHAL_DEFENSE)/3f));
			}
		}

	}

	private CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| enemy == target
					|| !Dungeon.level.heroFOV[cell]
					|| target.isCharmedBy( enemy )) {
				GLog.w(Messages.get(Combo.class, "bad_target"));

			} else if (!((Hero)target).canAttack(enemy)){
				if (((Hero) target).pointsInTalent(Talent.ENHANCED_COMBO) < 3
					|| Dungeon.level.distance(target.pos, enemy.pos) > 1 + target.buff(Combo.class).count/3){
					GLog.w(Messages.get(Combo.class, "bad_target"));
				} else {
					Ballistica c = new Ballistica(target.pos, enemy.pos, Ballistica.PROJECTILE);
					if (c.collisionPos == enemy.pos){
						final int leapPos = c.path.get(c.dist-1);
						if (!Dungeon.level.passable[leapPos]){
							GLog.w(Messages.get(Combo.class, "bad_target"));
						} else {
							Dungeon.hero.busy();
							target.sprite.jump(target.pos, leapPos, new Callback() {
								@Override
								public void call() {
									target.move(leapPos);
									Dungeon.level.occupyCell(target);
									Dungeon.observe();
									GameScene.updateFog();
									target.sprite.attack(cell, new Callback() {
										@Override
										public void call() {
											doAttack(enemy);
										}
									});
								}
							});
						}
					} else {
						GLog.w(Messages.get(Combo.class, "bad_target"));
					}
				}

			} else {
				Dungeon.hero.busy();
				target.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		@Override
		public String prompt() {
			return Messages.get(Combo.class, "prompt");
		}
	};
}
