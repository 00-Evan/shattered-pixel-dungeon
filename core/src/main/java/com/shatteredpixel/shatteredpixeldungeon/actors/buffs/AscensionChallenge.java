/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.HashMap;

public class AscensionChallenge extends Buff {

	private static HashMap<Class<?extends Mob>, Float> modifiers = new HashMap<>();
	static {
		modifiers.put(Rat.class,            10f);
		modifiers.put(Snake.class,          9f);
		modifiers.put(Gnoll.class,          9f);
		modifiers.put(Swarm.class,          8.5f);
		modifiers.put(Crab.class,           8f);
		modifiers.put(Slime.class,          8f);

		modifiers.put(Skeleton.class,       5f);
		modifiers.put(Thief.class,          5f);
		modifiers.put(DM100.class,          4.5f);
		modifiers.put(Guard.class,          4f);
		modifiers.put(Necromancer.class,    4f);

		modifiers.put(Bat.class,            2.5f);
		modifiers.put(Brute.class,          2.25f);
		modifiers.put(Shaman.class,         2.25f);
		modifiers.put(Spinner.class,        2f);
		modifiers.put(DM200.class,          2f);

		modifiers.put(Ghoul.class,          1.67f);
		modifiers.put(Elemental.class,      1.67f);
		modifiers.put(Warlock.class,        1.5f);
		modifiers.put(Monk.class,           1.5f);
		modifiers.put(Golem.class,          1.33f);

		modifiers.put(RipperDemon.class,    1.2f);
		modifiers.put(Succubus.class,       1.2f);
		modifiers.put(Eye.class,            1.1f);
		modifiers.put(Scorpio.class,        1.1f);
	}

	public static float statModifier(Char ch){
		if (Dungeon.hero.buff(AscensionChallenge.class) == null){
			return 1;
		}

		if (ch instanceof Ratmogrify.TransmogRat){
			ch = ((Ratmogrify.TransmogRat) ch).getOriginal();
		}

		if (ch.buff(AscensionBuffBlocker.class) != null){
			return 1f;
		}

		for (Class<?extends Mob> cls : modifiers.keySet()){
			if (cls.isAssignableFrom(ch.getClass())){
				return modifiers.get(cls);
			}
		}

		return 1;
	}

	//distant mobs get constantly beckoned to the hero at 2+ stacks
	public static void beckonEnemies(){
		if (Dungeon.hero.buff(AscensionChallenge.class) != null
				&& Dungeon.hero.buff(AscensionChallenge.class).stacks >= 2f){
			for (Mob m : Dungeon.level.mobs){
				if (m.alignment == Char.Alignment.ENEMY && m.distance(Dungeon.hero) > 8) {
					m.beckon(Dungeon.hero.pos);
				}
			}
		}
	}

	//mobs move at 2x speed when not hunting/fleeing at 4 stacks or higher
	public static float enemySpeedModifier(Mob m){
		if (Dungeon.hero.buff(AscensionChallenge.class) != null
				&& m.alignment == Char.Alignment.ENEMY
				&& Dungeon.hero.buff(AscensionChallenge.class).stacks >= 4f
				&& m.state != m.HUNTING && m.state != m.FLEEING){
			return 2;
		}

		return 1;
	}

	//hero speed is halved and capped at 1x at 6+ stacks
	public static float modifyHeroSpeed(float speed){
		if (Dungeon.hero.buff(AscensionChallenge.class) != null
				&& Dungeon.hero.buff(AscensionChallenge.class).stacks > 6f){
			return Math.min(speed/2f, 1f);
		}

		return speed;
	}

	public static void processEnemyKill(Char enemy){
		AscensionChallenge chal = Dungeon.hero.buff(AscensionChallenge.class);
		if (chal == null) return;

		if (enemy instanceof Ratmogrify.TransmogRat){
			enemy = ((Ratmogrify.TransmogRat) enemy).getOriginal();
		}

		//only enemies that are boosted count
		if (enemy.buff(AscensionBuffBlocker.class) != null){
			return;
		}

		boolean found = false;
		for (Class<?extends Mob> cls : modifiers.keySet()){
			if (cls.isAssignableFrom(enemy.getClass())){
				found = true;
				break;
			}
		}
		if (!found) return;

		float oldStacks = chal.stacks;
		if (enemy instanceof Ghoul || enemy instanceof RipperDemon){
			chal.stacks -= 0.5f;
		} else {
			chal.stacks -= 1;
		}
		chal.stacks = Math.max(0, chal.stacks);
		if (chal.stacks < 8f && (int)(chal.stacks/2) != (int)(oldStacks/2f)){
			GLog.p(Messages.get(AscensionChallenge.class, "weaken"));
		}
		BuffIndicator.refreshHero();
	}

	//used for internal calculations like corruption, not actual exp gain
	public static int AscensionExp(Mob m){
		if (Dungeon.hero.buff(AscensionChallenge.class) == null){
			return m.EXP;
		}

		if (m instanceof Ratmogrify.TransmogRat){
			m = ((Ratmogrify.TransmogRat) m).getOriginal();
		}

		if (m.buff(AscensionBuffBlocker.class) != null){
			return m.EXP;
		}

		if (m instanceof RipperDemon){
			return 10; //reduced due to their numbers
		} else if (m instanceof Ghoul){
			return 7; //half of 13, rounded up
		} else {
			for (Class<?extends Mob> cls : modifiers.keySet()){
				if (cls.isAssignableFrom(m.getClass())){
					return Math.max(13, m.EXP); //same exp as an eye
				}
			}
		}
		return m.EXP;
	}

	{
		revivePersists = true;
	}

	private float stacks = 0;
	private float damageInc = 0;

	public void onLevelSwitch(){
		if (Dungeon.depth < Statistics.highestAscent){
			Statistics.highestAscent = Dungeon.depth;
			if (Dungeon.bossLevel()){
				Dungeon.hero.buff(Hunger.class).satisfy(Hunger.STARVING);
				Buff.affect(Dungeon.hero, Healing.class).setHeal(Dungeon.hero.HT, 0, 20);
			} else {
				stacks += 2f;

				//clears any existing mobs from the level and adds one initial one
				//this helps balance difficulty between levels with lots of mobs left, and ones with few
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (!mob.reset()) {
						Dungeon.level.mobs.remove( mob );
					}
				}
				Dungeon.level.spawnMob(12);

			}
		}
		if (Statistics.highestAscent < 20){
			for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
				if (m instanceof Shopkeeper){
					((Shopkeeper) m).flee();
				}
			}
		}

	}

	public void saySwitch(){
		if (Dungeon.bossLevel()){
			GLog.p(Messages.get(this, "break"));
		} else {
			if (Dungeon.depth == 1){
				GLog.n(Messages.get(this, "almost"));
			} else if (stacks >= 8f){
				GLog.n(Messages.get(this, "damage"));
			} else if (stacks >= 6f){
				GLog.n(Messages.get(this, "slow"));
			} else if (stacks >= 4f){
				GLog.n(Messages.get(this, "haste"));
			} else if (stacks >= 2f){
				GLog.n(Messages.get(this, "beckon"));
			}
			if (stacks > 8 || stacks > 4 && Dungeon.depth > 20){
				GLog.h(Messages.get(this, "weaken_info"));
			}
		}
	}

	@Override
	public boolean act() {

		beckonEnemies();

		//hero starts progressively taking damage over time at 8+ stacks
		if (stacks >= 8 && !Dungeon.bossLevel()){
			damageInc += (stacks-4)/4f;
			if (damageInc >= 1){
				target.damage((int)damageInc, this);
				damageInc -= (int)damageInc;

				if (target == Dungeon.hero && !target.isAlive()){
					Badges.validateDeathFromFriendlyMagic();
					GLog.n(Messages.get(this, "on_kill"));
					Dungeon.fail(Amulet.class);
				}
			}
		} else {
			damageInc = 0;
		}

		spend(TICK);
		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.AMULET;
	}

	@Override
	public void tintIcon(Image icon) {
		if (stacks < 2){
			icon.hardlight(0.5f, 1, 0);
		} else if (stacks < 4) {
			icon.hardlight(1, 1, 0);
		} else if (stacks < 6){
			icon.hardlight(1, 0.67f, 0);
		} else if (stacks < 8){
			icon.hardlight(1, 0.33f, 0);
		} else {
			icon.hardlight(1, 0, 0);
		}
	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		desc += "\n";
		if (stacks < 2){

			desc += "\n" + Messages.get(this, "desc_clear");

		} else {

			if (stacks >= 2)    desc += "\n" + Messages.get(this, "desc_beckon");
			if (stacks >= 4)    desc += "\n" + Messages.get(this, "desc_haste");
			if (stacks >= 6)    desc += "\n" + Messages.get(this, "desc_slow");
			if (stacks >= 8)    desc += "\n" + Messages.get(this, "desc_damage");

		}

		return desc;
	}

	public static final String STACKS = "enemy_stacks";
	public static final String DAMAGE = "damage_inc";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STACKS, stacks);
		bundle.put(DAMAGE, damageInc);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		stacks = bundle.getFloat(STACKS);
		damageInc = bundle.getFloat(DAMAGE);
	}

	//chars with this buff are not boosted by the ascension challenge
	public static class AscensionBuffBlocker extends Buff{};
}
