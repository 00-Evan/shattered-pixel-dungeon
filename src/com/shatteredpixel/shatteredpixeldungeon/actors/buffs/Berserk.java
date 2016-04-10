package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

/**
 * Created by Evan on 20/03/2016.
 */
public class Berserk extends Buff {

	private enum State{
		NORMAL, BERSERK, EXHAUSTED, RECOVERING;
	}
	private State state = State.NORMAL;

	private static final int EXHAUSTION_START = 30;
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
		float percentMissing = 1f - target.HP/(float)target.HT;
		float bonus = 1f + (percentMissing * percentMissing);

		if (state == State.EXHAUSTED) bonus *= (50 - exhaustion) / 50f;

		return Math.round(dmg * bonus);
	}

	public boolean berserking(){
		if (target.HP == 0 && state == State.NORMAL){

			WarriorShield sigil = target.buff(WarriorShield.class);
			if (sigil != null){
				state = State.BERSERK;
				BuffIndicator.refreshHero();
				target.SHLD = sigil.maxShield() * 5;
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
		switch (state){
			case NORMAL: default:
				return BuffIndicator.NONE;
			case BERSERK:
				return BuffIndicator.FURY;
			case EXHAUSTED:
				return BuffIndicator.FURY;
			case RECOVERING:
				return BuffIndicator.FURY;
		}
	}

	@Override
	public String toString() {
		switch (state){
			case NORMAL: default:
				return "";
			case BERSERK:
				return "BERSERK!";
			case EXHAUSTED:
				return "Exhausted";
			case RECOVERING:
				return "Recovering";
		}
	}

	@Override
	public String desc() {
		switch (state){
			case NORMAL: default:
				return "";
			case BERSERK:
				return "Berserking, you're invincible!";
			case EXHAUSTED:
				return "Exhausted! Damage down!";
			case RECOVERING:
				return "Recovering from rage, can't do it again.";
		}
	}
}
