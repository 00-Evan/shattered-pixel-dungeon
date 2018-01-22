/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingHammer extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.THROWING_HAMMER;
		
		sticky = false;
	}
	
	@Override
	public int min(int lvl) {
		return 8;
	}
	
	@Override
	public int max(int lvl) {
		return 20;
	}
	
	@Override
	public int STRReq(int lvl) {
		return 17;
	}
	
	@Override
	protected float durabilityPerUse() {
		return super.durabilityPerUse()/1.5f;
	}
	
	@Override
	public int price() {
		return 30 * quantity;
	}
}
