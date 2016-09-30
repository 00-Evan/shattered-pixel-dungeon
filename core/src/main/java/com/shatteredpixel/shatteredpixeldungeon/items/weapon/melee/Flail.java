/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Flail extends MeleeWeapon {

	{
		image = ItemSpriteSheet.FLAIL;

		tier = 4;
		DLY = 1.25f; //0.8x speed
		//also cannot surprise attack, see Hero.canSurpriseAttack
	}

	@Override
	public int max(int lvl) {
		return  Math.round(7*(tier+1)) +        //35 base, up from 25
				lvl*Math.round(1.6f*(tier+1));  //+8 per level, up from +5
	}
}
