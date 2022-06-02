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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.*;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

import java.util.HashMap;

public class AscensionChallenge extends Buff{

	{
		revivePersists = true;
	}

	private static HashMap<Class<?extends Mob>, Float> modifiers = new HashMap<>();
	static {
		modifiers.put(Rat.class,            10f);
		modifiers.put(Snake.class,          8f);
		modifiers.put(Gnoll.class,          8f);
		modifiers.put(Swarm.class,          7f);
		modifiers.put(Crab.class,           6f);
		modifiers.put(Slime.class,          6f);

		modifiers.put(Skeleton.class,       4.5f);
		modifiers.put(Thief.class,          4.5f);
		modifiers.put(DM100.class,          4f);
		modifiers.put(Guard.class,          3.5f);
		modifiers.put(Necromancer.class,    3.5f);

		modifiers.put(Bat.class,            2.5f);
		modifiers.put(Brute.class,          2.25f);
		modifiers.put(Shaman.class,         2.25f);
		modifiers.put(Spinner.class,        2f);
		modifiers.put(DM200.class,          2f);

		modifiers.put(Ghoul.class,          1.67f);
		modifiers.put(Elemental.class,      1.5f);
		modifiers.put(Warlock.class,        1.33f);
		modifiers.put(Monk.class,           1.33f);
		modifiers.put(Golem.class,          1.25f);

		modifiers.put(RipperDemon.class,    1.2f);
		modifiers.put(Succubus.class,       1.2f);
		modifiers.put(Eye.class,            1f);
		modifiers.put(Scorpio.class,        1f);
	}

	public static float statModifier(Char ch){
		if (Dungeon.hero.buff(AscensionChallenge.class) == null){
			return 1;
		}

		for (Class<?extends Mob> cls : modifiers.keySet()){
			if (ch.getClass().isAssignableFrom(cls)){
				return modifiers.get(cls);
			}
		}

		return 1;
	}

	//TODO lots of impl still to do here

	//for Exp: treat all enemies with multiplier as needing 14 EXP (except ghouls/rippers, which are 7/10)

	//For damage scaling effects: Treat as if floor 26 (bombs, toxic gas, corrosion, electricity, sac fire(?)
	//  Burning, ooze,

	//for allies/enemies with depth scaling effects, treat as if floor 26
	// How though?

	@Override
	public int icon() {
		return BuffIndicator.AMULET;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 1, 0);
	}


}
