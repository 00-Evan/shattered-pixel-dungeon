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
package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.items.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TrapSprite;

public class ExplosiveTrap extends Trap {

	{
		name = "Explosive trap";
		color = TrapSprite.ORANGE;
		shape = TrapSprite.DIAMOND;
	}

	@Override
	public void activate() {
		new Bomb().explode(pos);
	}

	@Override
	public String desc() {
		return "This trap contains some powdered explosive and a trigger mechanism. " +
				"Activating it will cause an explosion in the immediate area.";
	}
}
