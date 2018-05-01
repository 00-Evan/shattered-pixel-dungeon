/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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
		NORMAL, BERSERK, RECOVERING;
	}
	private State state = State.NORMAL;

	private static final float LEVEL_RECOVER_START = 2f;
	private float levelRecovery;
	
	private float power = 0;

	private static final String STATE = "state";
	private static final String LEVEL_RECOVERY = "levelrecovery";
	private static final String POWER = "power";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STATE, state);
		bundle.put(POWER, power);
		if (state == State.RECOVERING) bundle.put(LEVEL_RECOVERY, levelRecovery);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		//pre-0.6.5 saves
		if (bundle.contains("exhaustion")){
			state = State.RECOVERING;
		} else {
			state = bundle.getEnum(STATE, State.class);
		}
		if (bundle.contains(POWER)){
			power = bundle.getFloat(POWER);
		} else {
			power = 1f;
		}
		if (state == State.RECOVERING) levelRecovery = bundle.getFloat(LEVEL_RECOVERY);
	}

	@Override
	public boolean act() {
		if (berserking()){
			if (target.HP <= 0) {
				target.SHLD -=  1 + Math.ceil(target.SHLD * 0.1f);
				if (target.SHLD <= 0) {
					target.SHLD = 0;
					target.die(this);
					if (!target.isAlive()) Dungeon.fail(this.getClass());
				}
			} else {
				state = State.RECOVERING;
				levelRecovery = LEVEL_RECOVER_START;
				BuffIndicator.refreshHero();
				target.SHLD = 0;
				power = 0f;
			}
		} else if (state == State.NORMAL) {
			power -= Math.max(0.1f, power) * 0.1f * Math.pow((target.HP/(float)target.HT), 2);
			
			if (power <= 0){
				detach();
			}
			BuffIndicator.refreshHero();
		}
		spend(TICK);
		return true;
	}

	public int damageFactor(int dmg){
		float bonus = Math.min(1.5f, 1f + (power / 2f));
		return Math.round(dmg * bonus);
	}

	public boolean berserking(){
		if (target.HP == 0 && state == State.NORMAL && power >= 1f){

			WarriorShield shield = target.buff(WarriorShield.class);
			if (shield != null){
				state = State.BERSERK;
				BuffIndicator.refreshHero();
				target.SHLD = shield.maxShield() * 10;

				SpellSprite.show(target, SpellSprite.BERSERK);
				Sample.INSTANCE.play( Assets.SND_CHALLENGE );
				GameScene.flash(0xFF0000);
			}

		}

		return state == State.BERSERK && target.SHLD > 0;
	}
	
	public void damage(int damage){
		if (state == State.RECOVERING) return;
		power = Math.min(1.1f, power + (damage/(float)target.HT)/3f );
		BuffIndicator.refreshHero();
	}

	public void recover(float percent){
		if (levelRecovery > 0){
			levelRecovery -= percent;
			BuffIndicator.refreshHero();
			if (levelRecovery <= 0) {
				state = State.NORMAL;
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
				if (power < 0.5f)       icon.hardlight(1f, 1f, 0.5f - (power));
				else if (power < 1f)    icon.hardlight(1f, 1.5f - power, 0f);
				else                    icon.hardlight(1f, 0f, 0f);
				break;
			case BERSERK:
				icon.hardlight(1f, 0f, 0f);
				break;
			case RECOVERING:
				icon.hardlight(1f - (levelRecovery*0.5f), 1f - (levelRecovery*0.3f), 1f);
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
			case RECOVERING:
				return Messages.get(this, "recovering");
		}
	}

	@Override
	public String desc() {
		float dispDamage = (damageFactor(10000) / 100f) - 100f;
		switch (state){
			case NORMAL: default:
				return Messages.get(this, "angered_desc", Math.floor(power * 100f), dispDamage);
			case BERSERK:
				return Messages.get(this, "berserk_desc");
			case RECOVERING:
				return Messages.get(this, "recovering_desc", levelRecovery);
		}
		
	}
}
