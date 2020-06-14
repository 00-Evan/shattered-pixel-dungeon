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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
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
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Combo extends Buff implements ActionIndicator.Action {
	
	private int count = 0;
	private float comboTime = 0f;
	private int misses = 0;
	
	@Override
	public int icon() {
		return BuffIndicator.COMBO;
	}
	
	@Override
	public void tintIcon(Image icon) {
		if (count >= 10)    icon.hardlight(1f, 0f, 0f);
		else if (count >= 8)icon.hardlight(1f, 0.8f, 0f);
		else if (count >= 6)icon.hardlight(1f, 1f, 0f);
		else if (count >= 4)icon.hardlight(0.8f, 1f, 0f);
		else if (count >= 2)icon.hardlight(0f, 1f, 0f);
		else                icon.resetColor();
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (4 - comboTime)/4f);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void hit( Char enemy ) {

		count++;
		comboTime = 4f;
		misses = 0;
		
		if (count >= 2) {

			ActionIndicator.setAction( this );
			Badges.validateMasteryCombo( count );

			GLog.p( Messages.get(this, "combo", count) );
			
		}

		BuffIndicator.refreshHero(); //refresh the buff visually on-hit

	}

	public void miss( Char enemy ){
		misses++;
		comboTime = 4f;
		if (misses >= 2){
			detach();
		}
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
		String desc = Messages.get(this, "desc");

		if (count >= 10)    desc += "\n\n" + Messages.get(this, "fury_desc");
		else if (count >= 8)desc += "\n\n" + Messages.get(this, "crush_desc");
		else if (count >= 6)desc += "\n\n" + Messages.get(this, "slam_desc");
		else if (count >= 4)desc += "\n\n" + Messages.get(this, "cleave_desc");
		else if (count >= 2)desc += "\n\n" + Messages.get(this, "clobber_desc");

		return desc;
	}

	private static final String COUNT = "count";
	private static final String TIME  = "combotime";
	private static final String MISSES= "misses";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
		bundle.put(TIME, comboTime);
		bundle.put(MISSES, misses);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );
		if (count >= 2) ActionIndicator.setAction(this);
		comboTime = bundle.getFloat( TIME );
		misses = bundle.getInt( MISSES );
	}

	@Override
	public Image getIcon() {
		Image icon;
		if (((Hero)target).belongings.weapon != null){
			icon = new ItemSprite(((Hero)target).belongings.weapon.image, null);
		} else {
			icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
		}

		if (count >= 10)    icon.tint(0xFFFF0000);
		else if (count >= 8)icon.tint(0xFFFFCC00);
		else if (count >= 6)icon.tint(0xFFFFFF00);
		else if (count >= 4)icon.tint(0xFFCCFF00);
		else                icon.tint(0xFF00FF00);

		return icon;
	}

	@Override
	public void doAction() {
		GameScene.selectCell(finisher);
	}

	private enum finisherType{
		CLOBBER, CLEAVE, SLAM, CRUSH, FURY
	}

	private CellSelector.Listener finisher = new CellSelector.Listener() {

		private finisherType type;

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
						if (count >= 10)    type = finisherType.FURY;
						else if (count >= 8)type = finisherType.CRUSH;
						else if (count >= 6)type = finisherType.SLAM;
						else if (count >= 4)type = finisherType.CLEAVE;
						else                type = finisherType.CLOBBER;
						doAttack(enemy);
					}
				});
			}
		}

		private void doAttack(final Char enemy){

			AttackIndicator.target(enemy);

			if (enemy.defenseSkill(target) >= Char.INFINITE_EVASION){
				enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				detach();
				ActionIndicator.clearAction(Combo.this);
				((Hero)target).spendAndNext(((Hero)target).attackDelay());
				return;
			} else if (enemy.isInvulnerable(target.getClass())){
				enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable") );
				Sample.INSTANCE.play(Assets.Sounds.MISS);
				detach();
				ActionIndicator.clearAction(Combo.this);
				((Hero)target).spendAndNext(((Hero)target).attackDelay());
				return;
			}

			int dmg = target.damageRoll();

			//variance in damage dealt
			switch(type){
				case CLOBBER:
					dmg = Math.round(dmg*0.6f);
					break;
				case CLEAVE:
					dmg = Math.round(dmg*1.5f);
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
					dmg = Math.round(dmg*2.5f);
					break;
				case FURY:
					dmg = Math.round(dmg*0.6f);
					break;
			}
			
			dmg = enemy.defenseProc(target, dmg);
			dmg -= enemy.drRoll();
			
			if ( enemy.buff( Vulnerable.class ) != null){
				dmg *= 1.33f;
			}
			
			dmg = target.attackProc(enemy, dmg);
			boolean wasAlly = enemy.alignment == target.alignment;
			enemy.damage( dmg, this );

			//special effects
			switch (type){
				case CLOBBER:
					if (enemy.isAlive()){
						if (!enemy.properties().contains(Char.Property.IMMOVABLE)){
							for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
								int ofs = PathFinder.NEIGHBOURS8[i];
								if (enemy.pos - target.pos == ofs) {
									int newPos = enemy.pos + ofs;
									if ((Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos])
											&& Actor.findChar( newPos ) == null
											&& (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[newPos])) {

										Actor.addDelayed( new Pushing( enemy, enemy.pos, newPos ), -1 );

										enemy.pos = newPos;
										Dungeon.level.occupyCell(enemy );

									}
									break;
								}
							}
						}
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

			if (target.buff(FireImbue.class) != null)
				target.buff(FireImbue.class).proc(enemy);
			if (target.buff(EarthImbue.class) != null)
				target.buff(EarthImbue.class).proc(enemy);
			if (target.buff(FrostImbue.class) != null)
				target.buff(FrostImbue.class).proc(enemy);

			target.hitSound(Random.Float(0.87f, 1.15f));
			if (type != finisherType.FURY) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			enemy.sprite.bloodBurstA( target.sprite.center(), dmg );
			enemy.sprite.flash();

			if (!enemy.isAlive()){
				GLog.i( Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())) );
			}

			Hero hero = (Hero)target;

			//Post-attack behaviour
			switch(type){
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
					if (count > 0 && enemy.isAlive()){
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
			if (count >= 10)    return Messages.get(Combo.class, "fury_prompt");
			else if (count >= 8)return Messages.get(Combo.class, "crush_prompt");
			else if (count >= 6)return Messages.get(Combo.class, "slam_prompt");
			else if (count >= 4)return Messages.get(Combo.class, "cleave_prompt");
			else                return Messages.get(Combo.class, "clobber_prompt");
		}
	};
}
