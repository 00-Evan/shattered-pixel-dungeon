/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Berserk extends Buff {

	private enum State{
		NORMAL, BERSERK, EXHAUSTED, RECOVERING;
	}
	private State state = State.NORMAL;

	private static final int EXHAUSTION_START = 60;
	private int exhaustion;

	private static final float LEVEL_RECOVER_START = 3f;
	private float levelRecovery;

	private static final String STATE = "state";
	private static final String EXHAUSTION = "exhaustion";
	private static final String LEVEL_RECOVERY = "levelrecovery";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STATE, state);
		if (state == State.EXHAUSTED) bundle.put(EXHAUSTION, exhaustion);
		if (state == State.EXHAUSTED || state == State.RECOVERING) bundle.put(LEVEL_RECOVERY, levelRecovery);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum(STATE, State.class);
		if (state == State.EXHAUSTED) exhaustion = bundle.getInt(EXHAUSTION);
		if (state == State.EXHAUSTED || state == State.RECOVERING) levelRecovery = bundle.getFloat(LEVEL_RECOVERY);
	}

	@Override
	public boolean act() {
		if (berserking()){
			if (target.HP <= 0) {
				target.SHLD -= Math.min(target.SHLD, 2);
				if (target.SHLD == 0) {
					target.die(this);
					if (!target.isAlive()) Dungeon.fail(this.getClass());
				}
			} else {
				state = State.EXHAUSTED;
				exhaustion = EXHAUSTION_START;
				levelRecovery = LEVEL_RECOVER_START;
				BuffIndicator.refreshHero();
				target.SHLD = 0;
			}
		} else if (state == State.EXHAUSTED){
			exhaustion--;
			if (exhaustion == 0){
				state = State.RECOVERING;
				BuffIndicator.refreshHero();
			}
		}
		spend(TICK);
		return true;
	}

	public int damageFactor(int dmg){
		float bonus;

		if (state == State.BERSERK){
			bonus = 2f;
		} else if (state == State.EXHAUSTED) {
			bonus = 1f - ((float)Math.sqrt(exhaustion) / 10f);
		} else {
			float percentMissing = 1f - target.HP/(float)target.HT;
			bonus = 1f + (0.5f * (float)Math.pow(percentMissing, 2));
		}

		return Math.round(dmg * bonus);
	}

	public boolean berserking(){
		if (target.HP == 0 && state == State.NORMAL){

			WarriorShield shield = target.buff(WarriorShield.class);
			if (shield != null){
				state = State.BERSERK;
				BuffIndicator.refreshHero();
				target.SHLD = shield.maxShield() * 5;

				SpellSprite.show(target, SpellSprite.BERSERK);
				Sample.INSTANCE.play( Assets.SND_CHALLENGE );
				GameScene.flash(0xFF0000);
			}

		}

		return state == State.BERSERK;
	}

	public void recover(float percent){
		if (levelRecovery > 0){
			levelRecovery -= percent;
			if (levelRecovery <= 0) {
				state = State.NORMAL;
				BuffIndicator.refreshHero();
				levelRecovery = 0;
			}
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.BERSERK;
	}
	
	@Override
	public void tintIcon(Image icon) {
		switch (state){
			case NORMAL: default:
				icon.hardlight(1f, 0.67f, 0.2f);
				break;
			case BERSERK:
				icon.hardlight(1f, 0.1f, 0.1f);
				break;
			case EXHAUSTED:
				icon.resetColor();
				break;
			case RECOVERING:
				//icon.hardlight(0.12f, 0.20f, 0.55f);
				icon.hardlight(0.35f, 0.45f, 0.75f);
				break;
		}
	}
	
	@Override
	public String toString() {
		switch (state){
			case NORMAL: default:
				return Messages.get(this, "angered");
			case BERSERK:
				return Messages.get(this, "berserk");
			case EXHAUSTED:
				return Messages.get(this, "exhausted");
			case RECOVERING:
				return Messages.get(this, "recovering");
		}
	}

	@Override
	public String desc() {
		float dispDamage = damageFactor(100);
		switch (state){
			case NORMAL: default:
				return Messages.get(this, "angered_desc", dispDamage);
			case BERSERK:
				return Messages.get(this, "berserk_desc");
			case EXHAUSTED:
				return Messages.get(this, "exhausted_desc", exhaustion , dispDamage);
			case RECOVERING:
				return Messages.get(this, "recovering_desc", levelRecovery, dispDamage);
		}
	}
}
