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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;


public class Hunger extends Buff implements Hero.Doom {

	private static final float STEP	= 10f;

	public static final float HUNGRY	= 300f;
	public static final float STARVING	= 450f;
	public static final float STARVINGR	= 20f;
	public static final float DEWEFFECT	= 20f;

	private float level;
	private float partialDamage;

	private static final String LEVEL			= "level";
	private static final String PARTIALDAMAGE 	= "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( LEVEL, level );
		bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	@Override
	public boolean act() {
		float f = 10.0f;
		if (Dungeon.level.locked || Dungeon.depth == 0 || this.target.buff(WellFed.class) != null) {
			spend(10.0f);
			return true;
		}
		if (!this.target.isAlive() || !(this.target instanceof Hero)) {
			diactivate();
		} else {
			Hero hero = (Hero) this.target;
			if (Dungeon.isChallenged(1024) && isStarving()) {
				GLog.b(Messages.get(this, "warning", new Object[0]), new Object[0]);
				float f2 = this.partialDamage + ((((float) this.target.HT) * 20.0f) / 80.0f);
				this.partialDamage = f2;
				if (f2 > 1.0f) {
					this.target.damage((int) this.partialDamage, this);
					float f3 = this.partialDamage;
					this.partialDamage = f3 - ((float) ((int) f3));
				}
			} else if (isStarving()) {
				float f4 = this.partialDamage + ((((float) this.target.HT) * 10.0f) / 1000.0f);
				this.partialDamage = f4;
				if (f4 > 1.0f) {
					this.target.damage((int) this.partialDamage, this);
					float f5 = this.partialDamage;
					this.partialDamage = f5 - ((float) ((int) f5));
				}
			} else {
				float f6 = this.level;
				float newLevel = 10.0f + f6;
				if (newLevel >= 450.0f) {
					GLog.n(Messages.get(this, "onstarving", new Object[0]), new Object[0]);
					hero.resting = false;
					hero.damage(1, this);
					hero.interrupt();
				} else if (newLevel >= 300.0f && f6 < 300.0f) {
					GLog.w(Messages.get(this, "onhungry", new Object[0]), new Object[0]);
				}
				this.level = newLevel;
			}
			if (this.target.buff(Shadows.class) != null) {
				f = 15.0f;
			}
			spend(f);
		}
		return true;
	}

	public void satisfy( float energy ) {

		Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
		if (buff != null && buff.isCursed()){
			energy *= 0.67f;
			GLog.n( Messages.get(this, "cursedhorn") );
		}

		affectHunger( energy, false );
	}

	public void affectHunger(float energy ){
		affectHunger( energy, false );
	}

	public void affectHunger(float energy, boolean overrideLimits ) {

		if (energy < 0 && target.buff(WellFed.class) != null){
			target.buff(WellFed.class).left += energy;
			BuffIndicator.refreshHero();
			return;
		}

		level -= energy;
		if (level < 0 && !overrideLimits) {
			level = 0;
		} else if (level > STARVING) {
			float excess = level - STARVING;
			level = STARVING;
			partialDamage += excess * (target.HT/1000f);
		}

		BuffIndicator.refreshHero();
	}

	public boolean isStarving() {
		return level >= STARVING;
	}

	public int hunger() {
		return (int)Math.ceil(level);
	}

	@Override
	public int icon() {
		if (level < HUNGRY) {
			return BuffIndicator.NONE;
		} else if (level < STARVING) {
			return BuffIndicator.HUNGER;
		} else {
			return BuffIndicator.STARVATION;
		}
	}

	@Override
	public String toString() {
		if (level < STARVING) {
			return Messages.get(this, "hungry");
		} else {
			return Messages.get(this, "starving");
		}
	}

	@Override
	public String desc() {
		String result;
		if (level < STARVING) {
			result = Messages.get(this, "desc_intro_hungry");
		} else {
			result = Messages.get(this, "desc_intro_starving");
		}

		result += Messages.get(this, "desc");

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( getClass() );
		GLog.n( Messages.get(this, "ondeath") );
	}
}
