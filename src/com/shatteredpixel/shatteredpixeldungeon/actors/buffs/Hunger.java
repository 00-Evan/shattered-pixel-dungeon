/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Hunger extends Buff implements Hero.Doom {

	private static final float STEP	= 10f;

	public static final float HUNGRY	= 260f;
	public static final float STARVING	= 360f;

	private static final String TXT_HUNGRY		= "You are hungry.";
	private static final String TXT_STARVING	= "You are starving!";
	private static final String TXT_DEATH		= "You starved to death...";

	private float level;

	private static final String LEVEL	= "level";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( LEVEL, level );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
	}

	@Override
	public boolean act() {

		if (Dungeon.level.locked){
			spend(STEP);
			return true;
		}

		if (target.isAlive()) {

			Hero hero = (Hero)target;

			if (isStarving()) {

				if (Random.Float() < 0.3f && (target.HP > 1 || target.paralysed == 0)) {

					hero.damage( 1, this );

				}
			} else {

				float newLevel = level + STEP;
				boolean statusUpdated = false;
				if (newLevel >= STARVING) {

					GLog.n( TXT_STARVING );
					hero.resting = false;
					hero.damage( 1, this );
					statusUpdated = true;

					hero.interrupt();

				} else if (newLevel >= HUNGRY && level < HUNGRY) {

					GLog.w( TXT_HUNGRY );
					statusUpdated = true;

				}
				level = newLevel;

				if (statusUpdated) {
					BuffIndicator.refreshHero();
				}

			}

			float step = ((Hero)target).heroClass == HeroClass.ROGUE ? STEP * 1.2f : STEP;
			spend( target.buff( Shadows.class ) == null ? step : step * 1.5f );

		} else {

			diactivate();

		}

		return true;
	}

	public void satisfy( float energy ) {
		if (((Hero) target).subClass == HeroSubClass.WARLOCK){
			Buff.affect( target, ScrollOfRecharging.Recharging.class, energy/50f);
			return;
		}

		Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
		if (buff != null && buff.isCursed()){
			energy *= 0.67f;
			GLog.n("The cursed horn steals some of the food energy as you eat.");
		}

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			reduceHunger( energy );
	}

	public void consumeSoul( float energy ){

		if (level >= STARVING)
			energy *= 1.33f;
		else if (level < HUNGRY)
			energy *= 0.67f;

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			reduceHunger( energy );
	}

	//directly interacts with hunger, no checks.
	public void reduceHunger( float energy ) {

		level -= energy;
		if (level < 0) {
			level = 0;
		} else if (level > STARVING) {
			level = STARVING;
		}

		BuffIndicator.refreshHero();
	}

	public boolean isStarving() {
		return level >= STARVING;
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
			return "Hungry";
		} else {
			return "Starving";
		}
	}

	@Override
	public String desc() {
		String result;
		if (level < STARVING) {
			result = "You can feel your stomach calling out for food, but it's not too urgent yet.\n\n";
		} else {
			result = "You're so hungry it hurts.\n\n";
		}

		result += "Hunger slowly increases as you spend time in the dungeon, eventually you will begin to starve. " +
				"While starving you will slowly lose health instead of regenerating it.\n" +
				"\n" +
				"Rationing is important! If you have health to spare starving isn't a bad idea if it means there will " +
				"be more food later. Effective rationing can make food last a lot longer!\n\n";

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( ResultDescriptions.HUNGER );
		GLog.n( TXT_DEATH );
	}
}
