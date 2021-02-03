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
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndCombo;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Combo extends Buff implements ActionIndicator.Action {
	
	private int count = 0;
	private float comboTime = 0f;
	
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
		return Math.max(0, (5 - comboTime)/5f);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void hit( Char enemy ) {

		count++;
		comboTime = 5f;

		//TODO this won't count a kill on an enemy that gets corruped by corrupting I think?
		if (!enemy.isAlive() || enemy.buff(Corruption.class) != null){
			comboTime = Math.max(comboTime, 10*((Hero)target).pointsInTalent(Talent.CLEAVE));
		}

		if ((getHighestMove() != null)) {

			ActionIndicator.setAction( this );
			Badges.validateMasteryCombo( count );

			GLog.p( Messages.get(this, "combo", count) );
			
		}

		BuffIndicator.refreshHero(); //refresh the buff visually on-hit

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
		return Messages.get(this, "desc");
	}

	private static final String COUNT = "count";
	private static final String TIME  = "combotime";
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
		bundle.put(TIME, comboTime);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );
		if (getHighestMove() != null) ActionIndicator.setAction(this);
		comboTime = bundle.getFloat( TIME );
	}

	@Override
	public Image getIcon() {
		Image icon;
		if (((Hero)target).belongings.weapon != null){
			icon = new ItemSprite(((Hero)target).belongings.weapon.image, null);
		} else {
			icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
		}

		icon.tint(getHighestMove().tintColor);
		return icon;
	}

	@Override
	public void doAction() {
		GameScene.show(new WndCombo(this));
		//GameScene.selectCell(finisher);
	}

	public enum ComboMove {
		//TODO rework these moves
		CLOBBER(2, 0xFF00FF00),
		CLEAVE(4, 0xFFCCFF00),
		SLAM(6, 0xFFFFFF00),
		CRUSH(8, 0xFFFFCC00),
		FURY(10, 0xFFFF0000);

		public int comboReq, tintColor;

		ComboMove(int comboReq, int tintColor){
			this.comboReq = comboReq;
			this.tintColor = tintColor;
		}

		public String desc(){
			return Messages.get(this, name()+"_desc");
		}


	}

	public ComboMove getHighestMove(){
		ComboMove best = null;
		for (ComboMove move : ComboMove.values()){
			if (count >= move.comboReq){
				best = move;
			}
		}
		return best;
	}

	public boolean canUseMove(ComboMove move){
		return move.comboReq <= count;
	}

	public void useMove(ComboMove move){
		moveBeingUsed = move;
		GameScene.selectCell(listener);
	}

	private static ComboMove moveBeingUsed;

	private CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;
			final Char enemy = Actor.findChar( cell );
			if (enemy == null
					|| !Dungeon.level.heroFOV[cell]
					|| !((Hero)target).canAttack(enemy)
					|| target.isCharmedBy( enemy )){
				GLog.w( Messages.get(Combo.class, "bad_target") );
			} else {
				target.sprite.attack(cell, new Callback() {
					@Override
					public void call() {
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			boolean wasAlly = enemy.alignment == target.alignment;

			if (enemy.defenseSkill(target) >= Char.INFINITE_EVASION){
				enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
				Sample.INSTANCE.play(Assets.Sounds.MISS);

			} else if (enemy.isInvulnerable(target.getClass())){
				enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable") );
				Sample.INSTANCE.play(Assets.Sounds.MISS);

			} else {

				int dmg = target.damageRoll();

				//variance in damage dealt
				switch (moveBeingUsed) {
					case CLOBBER:
						dmg = Math.round(dmg * 0.6f);
						break;
					case CLEAVE:
						dmg = Math.round(dmg * 1.5f);
						break;
					case SLAM:
						dmg += target.drRoll();
						break;
					case CRUSH:
						//rolls 4 times, takes the highest roll
						for (int i = 1; i < 4; i++) {
							int dmgReroll = target.damageRoll();
							if (dmgReroll > dmg) dmg = dmgReroll;
						}
						dmg = Math.round(dmg * 2.5f);
						break;
					case FURY:
						dmg = Math.round(dmg * 0.6f);
						break;
				}

				dmg = enemy.defenseProc(target, dmg);
				dmg -= enemy.drRoll();

				if (enemy.buff(Vulnerable.class) != null) {
					dmg *= 1.33f;
				}

				dmg = target.attackProc(enemy, dmg);
				enemy.damage(dmg, target);

				//special effects
				switch (moveBeingUsed) {
					case CLOBBER:
						if (enemy.isAlive()) {
							//trace a ballistica to our target (which will also extend past them
							Ballistica trajectory = new Ballistica(target.pos, enemy.pos, Ballistica.STOP_TARGET);
							//trim it to just be the part that goes past them
							trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
							//knock them back along that ballistica
							WandOfBlastWave.throwChar(enemy, trajectory, 2, true, false);
							Buff.prolong(enemy, Vertigo.class, Random.NormalIntRange(1, 4));
						}
						break;
					case SLAM:
						BrokenSeal.WarriorShield shield = Buff.affect(target, BrokenSeal.WarriorShield.class);
						if (shield != null) {
							shield.supercharge(dmg / 2);
						}
						break;
					default:
						//nothing
						break;
				}

				if (target.buff(FireImbue.class) != null)   target.buff(FireImbue.class).proc(enemy);
				if (target.buff(FrostImbue.class) != null)  target.buff(FrostImbue.class).proc(enemy);

				target.hitSound(Random.Float(0.87f, 1.15f));
				if (moveBeingUsed != ComboMove.FURY) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				enemy.sprite.bloodBurstA(target.sprite.center(), dmg);
				enemy.sprite.flash();

				if (!enemy.isAlive()) {
					GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())));
				}

			}

			Hero hero = (Hero)target;

			//Post-attack behaviour
			switch(moveBeingUsed){
				case CLEAVE:
					//combo isn't reset, but rather increments with a cleave kill, and grants more time.
					//this includes corrupting kills (which is why we check alignment
					if (!enemy.isAlive() || (!wasAlly && enemy.alignment == target.alignment)) {
						hit( enemy );
						comboTime = 12f;
					} else {
						detach();
						ActionIndicator.clearAction(Combo.this);
					}
					hero.spendAndNext(hero.attackDelay());
					break;

				case FURY:
					count--;
					//fury attacks as many times as you have combo count
					if (count > 0 && enemy.isAlive() && (wasAlly || enemy.alignment != target.alignment)){
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

		}

		@Override
		public String prompt() {
			return Messages.get(Combo.class, "prompt");
		}
	};
}
