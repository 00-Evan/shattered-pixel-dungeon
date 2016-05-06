package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Berserk extends Buff {

	private enum State{
		NORMAL, BERSERK, EXHAUSTED, RECOVERING;
	}
	private State state = State.NORMAL;

	private static final int EXHAUSTION_START = 40;
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

		if (state == State.EXHAUSTED) {
			bonus = (50 - exhaustion) / 50f;
		} else {
			float percentMissing = 1f - target.HP/(float)target.HT;
			bonus = 1f + (float)Math.pow(percentMissing, 3);
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
		switch (state){
			case NORMAL: default:
				return BuffIndicator.ANGERED;
			case BERSERK:
				return BuffIndicator.FURY;
			case EXHAUSTED:
				return BuffIndicator.EXHAUSTED;
			case RECOVERING:
				return BuffIndicator.RECOVERING;
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
		float dispDamage = damageFactor(10000)/100f;
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
