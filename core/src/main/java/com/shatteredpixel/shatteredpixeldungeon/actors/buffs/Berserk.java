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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.text.DecimalFormat;

public class Berserk extends Buff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
	}

	private enum State{
		NORMAL, BERSERK, RECOVERING
	}
	private State state = State.NORMAL;

	private static final float LEVEL_RECOVER_START = 5f;
	private float levelRecovery;

	private static final int TURN_RECOVERY_START = 100;
	private int turnRecovery;

	public int powerLossBuffer = 0;
	private float power = 0;

	private static final String STATE = "state";
	private static final String LEVEL_RECOVERY = "levelrecovery";
	private static final String TURN_RECOVERY = "turn_recovery";
	private static final String POWER = "power";
	private static final String POWER_BUFFER = "power_buffer";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STATE, state);
		bundle.put(POWER, power);
		bundle.put(POWER_BUFFER, powerLossBuffer);
		bundle.put(LEVEL_RECOVERY, levelRecovery);
		bundle.put(TURN_RECOVERY, turnRecovery);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		state = bundle.getEnum(STATE, State.class);
		power = bundle.getFloat(POWER);
		powerLossBuffer = bundle.getInt(POWER_BUFFER);
		levelRecovery = bundle.getFloat(LEVEL_RECOVERY);
		turnRecovery = bundle.getInt(TURN_RECOVERY);

		if (power >= 1f && state == State.NORMAL){
			ActionIndicator.setAction(this);
		}
	}

	@Override
	public boolean act() {
		if (state == State.BERSERK){
			ShieldBuff buff = target.buff(WarriorShield.class);
			if (target.shielding() > 0) {
				//lose 2.5% of shielding per turn, but no less than 1
				int dmg = (int)Math.ceil(target.shielding() * 0.025f);
				if (buff != null && buff.shielding() > 0) {
					dmg = buff.absorbDamage(dmg);
				}

				if (dmg > 0){
					//if there is leftover damage, then try to remove from other shielding buffs
					for (ShieldBuff s : target.buffs(ShieldBuff.class)){
						dmg = s.absorbDamage(dmg);
						if (dmg == 0) break;
					}
				}

				if (target.shielding() <= 0){
					state = State.RECOVERING;
					power = 0f;
					BuffIndicator.refreshHero();
					if (!target.isAlive()){
						target.die(this);
						if (!target.isAlive()) Dungeon.fail(this.getClass());
					}
				}

			} else {
				state = State.RECOVERING;
				power = 0f;
				if (!target.isAlive()){
					target.die(this);
					if (!target.isAlive()) Dungeon.fail(this.getClass());
				}

			}
		} else if (state == State.NORMAL) {
			if (powerLossBuffer > 0){
				powerLossBuffer--;
			} else {
				power -= GameMath.gate(0.1f, power, 1f) * 0.067f * Math.pow((target.HP / (float) target.HT), 2);

				if (power < 1f){
					ActionIndicator.clearAction(this);
				}

				if (power <= 0) {
					detach();
				}
			}
		} else if (state == State.RECOVERING && levelRecovery == 0
				&& (target.buff(LockedFloor.class) == null || target.buff(LockedFloor.class).regenOn())){
			turnRecovery--;
			if (turnRecovery <= 0){
				turnRecovery = 0;
				state = State.NORMAL;
			}
		}
		spend(TICK);
		return true;
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}

	public float enchantFactor(float chance){
		return chance + ((Math.min(1f, power) * 0.15f) * ((Hero) target).pointsInTalent(Talent.ENRAGED_CATALYST));
	}

	public float damageFactor(float dmg){
		return dmg * Math.min(1.5f, 1f + (power / 2f));
	}

	public boolean berserking(){
		if (target.HP == 0
				&& state == State.NORMAL
				&& power >= 1f
				&& target.buff(WarriorShield.class) != null
				&& ((Hero)target).hasTalent(Talent.DEATHLESS_FURY)){
			startBerserking();
			ActionIndicator.clearAction(this);
		}

		return state == State.BERSERK && target.shielding() > 0;
	}

	private void startBerserking(){
		state = State.BERSERK;
		SpellSprite.show(target, SpellSprite.BERSERK);
		Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
		GameScene.flash(0xFF0000);

		if (target.HP > 0) {
			turnRecovery = TURN_RECOVERY_START;
			levelRecovery = 0;
		} else {
			levelRecovery = LEVEL_RECOVER_START - ((Hero)target).pointsInTalent(Talent.DEATHLESS_FURY);
			turnRecovery = 0;
		}

		//base multiplier scales at 2/3/4/5/6x at 100/37/20/9/0% HP
		float shieldMultiplier = 2f + 4*(float)Math.pow((1f-(target.HP/(float)target.HT)), 3);

		//Endless rage effect on shield and cooldown
		if (power > 1f){
			shieldMultiplier *= power;
			levelRecovery *= 2f - power;
			turnRecovery *= 2f - power;
		}

		WarriorShield shield = target.buff(WarriorShield.class);
		int shieldAmount = Math.round(shield.maxShield() * shieldMultiplier);
		shield.supercharge(shieldAmount);

		BuffIndicator.refreshHero();
	}
	
	public void damage(int damage){
		if (state != State.NORMAL) return;
		float maxPower = 1f + 0.1667f*((Hero)target).pointsInTalent(Talent.ENDLESS_RAGE);
		power = Math.min(maxPower, power + (damage/(float)target.HT)/3f );
		BuffIndicator.refreshHero(); //show new power immediately
		powerLossBuffer = 3; //2 turns until rage starts dropping
		if (power >= 1f){
			ActionIndicator.setAction(this);
		}
	}

	public void recover(float percent){
		if (state == State.RECOVERING && levelRecovery > 0){
			levelRecovery -= percent;
			if (levelRecovery <= 0) {
				levelRecovery = 0;
				if (turnRecovery == 0){
					state = State.NORMAL;
				}
			}
		}
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action_name");
	}

	@Override
	public Image actionIcon() {
		//TODO, should look into these in general honestly
		return new BuffIcon(BuffIndicator.FURY, true);
	}

	@Override
	public void doAction() {
		WarriorShield shield = target.buff(WarriorShield.class);
		if (shield != null) {
			startBerserking();
			ActionIndicator.clearAction(this);
		} else {
			GLog.w(Messages.get(this, "no_seal"));
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
				if (power < 1f) icon.hardlight(1f, 0.5f, 0f);
				else            icon.hardlight(1f, 0f, 0f);
				break;
			case BERSERK:
				icon.hardlight(1f, 0f, 0f);
				break;
			case RECOVERING:
				icon.hardlight(0, 0, 1f);
				break;
		}
	}
	
	@Override
	public float iconFadePercent() {
		switch (state){
			case NORMAL: default:
				float maxPower = 1f + 0.1667f*((Hero)target).pointsInTalent(Talent.ENDLESS_RAGE);
				return (maxPower - power)/maxPower;
			case BERSERK:
				return 0f;
			case RECOVERING:
				if (levelRecovery > 0) {
					return 1f - levelRecovery/(LEVEL_RECOVER_START-Dungeon.hero.pointsInTalent(Talent.DEATHLESS_FURY));
				} else {
					return 1f - turnRecovery/(float)TURN_RECOVERY_START;
				}
		}
	}

	public String iconTextDisplay(){
		switch (state){
			case NORMAL: case BERSERK: default:
				return (int)(power*100) + "%";
			case RECOVERING:
				if (levelRecovery > 0) {
					return new DecimalFormat("#.#").format(levelRecovery);
				} else {
					return Integer.toString(turnRecovery);
				}
		}
	}

	@Override
	public String name() {
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
		float dispDamage = ((int)damageFactor(10000) / 100f) - 100f;
		switch (state){
			case NORMAL: default:
				return Messages.get(this, "angered_desc", Math.floor(power * 100f), dispDamage);
			case BERSERK:
				return Messages.get(this, "berserk_desc");
			case RECOVERING:
				if (levelRecovery > 0){
					return Messages.get(this, "recovering_desc") + "\n\n" + Messages.get(this, "recovering_desc_levels", levelRecovery);
				} else {
					return Messages.get(this, "recovering_desc") + "\n\n" + Messages.get(this, "recovering_desc_turns", turnRecovery);
				}
		}
		
	}
}
