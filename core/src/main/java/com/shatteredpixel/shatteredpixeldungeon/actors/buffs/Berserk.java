/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal.WarriorShield;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

public class Berserk extends Buff implements ActionIndicator.Action {

	{
		type = buffType.POSITIVE;
		revivePersists = true;

		actPriority=HERO_PRIO+1;
	}

	private enum State{
		RECOVERING, READY, ANGRY, PREPARING, RAMPAGING, UNDYING
	}
	private State state = State.READY;

	private void switchState(State state) {
		this.state = state;
		BuffIndicator.refreshHero();

		if(state == State.ANGRY)
			ActionIndicator.setAction(this);
		else
			ActionIndicator.clearAction(this);
	}

	private static final int ANGER_START = 5;
	private static final int PREPARATION_START = 3;
	private static final int RAMPAGE_START = 8;
	private static final int COOLDOWN_START = 100;

	private int ticksLeft;
	private int cooldown;
	private int rageKills = 0;

	private static final String STATE = "state";
	private static final String TICKS_LEFT = "ticks_left";
	private static final String COOLDOWN = "cooldown";
	private static final String RAGE_KILLS = "rage_kills";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STATE, state);
		bundle.put(TICKS_LEFT, ticksLeft);
		bundle.put(COOLDOWN, cooldown);
		bundle.put(RAGE_KILLS, rageKills);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		switchState(bundle.getEnum(STATE, State.class));
		ticksLeft = bundle.getInt(TICKS_LEFT);
		cooldown = bundle.getInt(COOLDOWN);
		rageKills = bundle.getInt(RAGE_KILLS);
	}


	@Override
	public boolean act() {
		spend(TICK);

		if(state == State.RECOVERING) {
			if(Regeneration.regenOn()) cooldown--;

			if(cooldown <= 0) switchState(State.READY);
		}
		else if(state == State.READY) {
			//do nothing
		}
		else if(state == State.ANGRY) {
			ticksLeft--;

			if(ticksLeft <= 0)
				switchState(State.READY);
		}
		else if(state == State.PREPARING) {
			ticksLeft--;

			if(ticksLeft <= 0)
				startRampage();
		}
		else if(state == State.RAMPAGING || state == State.UNDYING) {
			ticksLeft--;

			if(ticksLeft <= 0)
				stopRampage();
		}

		return true;
	}

	private float durationLeft() {
		return ticksLeft-1 + cooldown();
	}

	private void alignWithHero() {
		timeToNow();
		spend(target.cooldown()+TICK);
	}

	private int rageDuration() {
		int points = ((Hero) target).pointsInTalent(Talent.UNDYING_RAGE);
		if (points == 0) return 0;

		else return 1 + 2 * points;
	}


	private float shieldFactor() {
		float min = .15f;
		float missingHP = (float)(target.HT - target.HP) / target.HT;
		missingHP = GameMath.gate(0, missingHP/(1f-min), 1);

		return 1f + missingHP;
	}

	public float resistanceFactor(){
		int points = ((Hero)target).pointsInTalent(Talent.LAST_STAND);

		if(facingEnemies() >= 3) {
			switch (points) {
				case 1: return 0.83f;
				case 2: return 0.72f;
				case 3: return 0.64f;

				case 0: default: return 1f;
			}
		}
		else return 1f;
	}

	public float damageFactor(){
		int points = ((Hero)target).pointsInTalent(Talent.LAST_STAND);
		float lastStand = -1*(resistanceFactor()-1);

		return (state == State.RAMPAGING || state == State.UNDYING) ? 1.4f+lastStand : 1f;
	}

	public float accuracyFactor(){
		//Hero gets +acc while preparing, but NOT the +dmg
		return (state == State.RAMPAGING || state == State.UNDYING || state == State.PREPARING) ? 2f : 1f;
	}

	public float enchantBoost(boolean glyph){
		int points = ((Hero)target).pointsInTalent(Talent.ENRAGED_CATALYST);
		float boost = points * (glyph ? 0.5f : 0.2f);
		//15-100% based on missing HP
		boost *= 0.15f + 0.85f * (shieldFactor()-1);

		return (state == State.RAMPAGING || state == State.UNDYING) ? boost : 1f;
	}

	private int facingEnemies() {
		Hero hero = (Hero)target;

		int enemies = 0;
		for(Mob mob : hero.getVisibleEnemies()) {
			if(mob.distance(target) <= 8 && mob.isTargeting(target))
				enemies++;
		}

		return enemies;
	}


	public void damage(int damage){
		if(state == State.READY || state == State.ANGRY) {
			switchState(State.ANGRY);
			ticksLeft = ANGER_START;
			alignWithHero();
		}
	}

	private void prepareRampage() {
		switchState(State.PREPARING);
		ticksLeft = PREPARATION_START;
		alignWithHero();

		for (Mob mob : Dungeon.level.mobs) {
			if (mob.paralysed <= 0
					&& Dungeon.level.distance(target.pos, mob.pos) <= 8
					&& mob.alignment != target.alignment
			) {
				mob.beckon(target.pos);
			}
		}

		target.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
	}

	public void onHit() {
		if(state == State.PREPARING)
			startRampage();
	}

	private void startRampage(){
		switchState(State.RAMPAGING);
		ticksLeft = RAMPAGE_START;
		alignWithHero();

		WarriorShield shield = target.buff(WarriorShield.class);
		//WILL CHANGE THIS AFTER WARRIOR REWORK MERGE
		int shieldAmount = Math.round(3*shield.Polished_reworkShield() * shieldFactor());
		shield.supercharge(shieldAmount);

		Sample.INSTANCE.play(Assets.Sounds.BURNING, 2f, 0.75f);
		GameScene.flash(0xFF8000);
		SpellSprite.show(target, SpellSprite.BERSERK);
		target.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(shieldAmount), FloatingText.SHIELDING );
	}

	public void continueRampage(){
		if(state == State.RAMPAGING) {
			ticksLeft += 2;
		}
		else if(state == State.UNDYING) {
			ticksLeft++;
			rageKills++;
		}
	}

	private void stopRampage() {
		if(state == State.UNDYING) {
			Buff.affect(target, Berserk.UndyingRecovery.class);

			int healing = Math.min(Math.round(rageHeal() * target.HT), target.HT - target.HP);
			target.HP += healing;
			rageKills = 0;

			target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(healing), FloatingText.HEALING);
			Sample.INSTANCE.play(Assets.Sounds.DRINK);
		}

		WarriorShield shield = target.buff(WarriorShield.class);
		if(shield != null)
			shield.clearShield();

		switchState(State.RECOVERING);
		cooldown = COOLDOWN_START;

		GameScene.flash(0x80FFFFFF);
		Sample.INSTANCE.play(Assets.Sounds.DEGRADE);
		Dungeon.hero.interrupt();
	}


	public boolean raging(){
		if (target.HP <= 0
				&& target.buff(UndyingRecovery.class) == null
				&& target.buff(WarriorShield.class) != null
				&& ((Hero)target).hasTalent(Talent.UNDYING_RAGE)){

			target.HP = 1;
			if(state != State.UNDYING)
				startRage();
		}

		return state == State.UNDYING;
	}

	private void startRage(){
		switchState(State.UNDYING);
		ticksLeft = rageDuration();
		alignWithHero();

		for (Mob mob : Dungeon.level.mobs) {
			if (mob.paralysed <= 0
				&& Dungeon.level.distance(target.pos, mob.pos) <= 6
				&& mob.alignment != target.alignment
			) {
				mob.beckon(target.pos);
			}
		}

		target.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
		Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );

		GameScene.flash(0xFF0000);
		SpellSprite.show(target, SpellSprite.BERSERK);
		Dungeon.hero.interrupt();
	}

	private float rageHeal() {
		int points = ((Hero) target).pointsInTalent(Talent.UNDYING_RAGE);

		//caps at 7 kills
		float max = points * 0.1f;
		return Math.min(points * (0.025f + 0.0125f*rageKills), max);
	}


	@Override
	public String actionName() {
		return Messages.get(this, "action_name");
	}

	@Override
	public int actionIcon() {
		return HeroIcon.BERSERK;
	}

	@Override
	public Visual secondaryVisual() {
		BitmapText txt = new BitmapText(PixelScene.pixelFont);
		txt.text(Messages.decimalFormat("#.#", shieldFactor()) + "x");
		txt.hardlight(CharSprite.POSITIVE);
		txt.measure();
		return txt;
	}

	@Override
	public int indicatorColor() {
		return 0xC04000;
	}

	@Override
	public void doAction() {
		WarriorShield shield = target.buff(WarriorShield.class);
		if (shield != null && shield.maxShield() > 0) {
			prepareRampage();
		} else {
			GLog.w(Messages.get(this, "no_seal"));
		}
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}


	@Override
	public int icon() {
		return BuffIndicator.BERSERK;
	}
	
	@Override
	public void tintIcon(Image icon) {
		switch (state){
			case RECOVERING:
				icon.hardlight(0, 0.5f, 1f);
				break;
			case READY: default:
				icon.hardlight(0.5f, 1.0f, 0f);
				break;
			case ANGRY:
				icon.hardlight(1f, 1.0f, 0f);
				break;
			case PREPARING:
				icon.hardlight(1f, 0.75f, 0f);
				break;
			case RAMPAGING:
				icon.hardlight(1f, 0.5f, 0f);
				break;
			case UNDYING:
				icon.hardlight(1f, 0f, 0f);
				break;
		}
	}
	
	@Override
	public float iconFadePercent() {
		switch (state) {
			case RECOVERING:
				return (float)cooldown / COOLDOWN_START;
			case READY: default:
				return 0f;
			case ANGRY:
				return 1f - durationLeft() / ANGER_START;
			case PREPARING:
				return 1f - durationLeft() / PREPARATION_START;
			case RAMPAGING:
				return 1f - durationLeft() / RAMPAGE_START;
			case UNDYING:
				return 1f - durationLeft() / rageDuration();
		}
	}

	public String iconTextDisplay(){
		if(state == State.RECOVERING)
			return Integer.toString(cooldown);
		else if(state == State.READY)
			return "";
		else
			return Integer.toString(ticksLeft);
	}

	@Override
	public String name() {
		switch (state){
			case RECOVERING:
				return Messages.get(this, "recovering");
			case READY: default:
				return Messages.get(this, "ready");
			case ANGRY:
				return Messages.get(this, "angry");
			case PREPARING:
				return Messages.get(this, "preparing");
			case RAMPAGING:
				return Messages.get(this, "rampaging");
			case UNDYING:
				return Messages.get(this, "undying");
		}
	}

	@Override
	public String desc() {
		switch (state){
			case RECOVERING:
				return Messages.get(this, "recovering_desc", cooldown);
			case READY: default:
				return Messages.get(this, "ready_desc");
			case ANGRY:
				return Messages.get(this, "angry_desc", dispTurns(durationLeft()));
			case PREPARING:
				return Messages.get(this, "preparing_desc", dispTurns(durationLeft()));
			case RAMPAGING:
				return Messages.get(this, "rampaging_desc", dispTurns(durationLeft()));
			case UNDYING:
				return Messages.get(this, "undying_desc", dispTurns(durationLeft()));
		}
	}


	public static class UndyingRecovery extends FlavourBuff {
		{
			type = buffType.NEUTRAL;
			revivePersists = true;
		}
		private static final float LEVEL_RECOVER = 1.5f;
		private float levelRecovery = 0;

		public void recover(float percent){
			levelRecovery += percent;

			if(levelRecovery >= LEVEL_RECOVER) {
				detach();
			}
		}

		@Override
		public boolean act() {
			spend( TICK );
			return true;
		}


		private static final String LEVEL_RECOVERY = "level_recovery";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEVEL_RECOVERY, levelRecovery);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			levelRecovery = bundle.getFloat(LEVEL_RECOVERY);
		}


		@Override
		public int icon() {
			return BuffIndicator.BERSERK;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0f, 0.0f, 1f);
		}

		@Override
		public float iconFadePercent() {
			return 1f - levelRecovery/LEVEL_RECOVER;
		}

		public String iconTextDisplay(){
			return (int)(100 * (levelRecovery/LEVEL_RECOVER)) + "%";
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", levelRecovery);
		}
	}

	public static class LastStandIndicator extends FlavourBuff {
		{
			type = buffType.POSITIVE;
			revivePersists = true;

			actPriority = HERO_PRIO-1;
		}

		boolean active = false;

		@Override
		public boolean act() {
			Berserk berserk = target.buff(Berserk.class);
			if(berserk != null) active = berserk.facingEnemies() >= 3 && ((Hero)target).hasTalent(Talent.LAST_STAND);

			spend(target.cooldown());
			return true;
		}

		@Override
		public int icon() {
			return active ? BuffIndicator.LAST_STAND : BuffIndicator.NONE;
		}

		@Override
		public String iconTextDisplay() {
			return "";
		}


		private static final String ACTIVE = "active";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ACTIVE, active);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			active = bundle.getBoolean(ACTIVE);
		}
	}
}
