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
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;

public class NewbornElemental extends Elemental {

	{
		name = "newborn fire elemental";
		HT = 65;
		HP = HT/2; //32

		defenseSkill = 12;

		EXP = 4;

		loot = null; //TODO Elemental embers
		lootChance = 1f;
	}

	@Override
	public int damageRoll() {
		return super.damageRoll()/2;
	}

	@Override
	public void add(Buff buff) {
		if (buff instanceof Frost || buff instanceof Chill) {
			die(buff);
		} else {
			super.add(buff);
		}
	}

	@Override
	public String description() {
		return "young fire elemental"; //TODO
	}
}
