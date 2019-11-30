/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RipperSprite;
import com.watabou.utils.Random;

public class RipperDemon extends Mob {

	{
		spriteClass = RipperSprite.class;

		HP = HT = 60;
		defenseSkill = 22;

		EXP = 9; //for corrupting
		maxLvl = -2;

		baseSpeed = 2f;
	}

	@Override
	public float spawningWeight() {
		return 0;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 25 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 35;
	}

	@Override
	protected float attackDelay() {
		return super.attackDelay()*0.5f;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
}
