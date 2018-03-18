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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class FishingSpear extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.FISHING_SPEAR;
	}
	
	@Override
	public int min(int lvl) {
		return 4;
	}
	
	@Override
	public int max(int lvl) {
		return 10;
	}
	
	@Override
	public int STRReq(int lvl) {
		return 11;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (defender instanceof Piranha){
			damage = Math.max(damage, defender.HP/2);
		}
		return super.proc(attacker, defender, damage);
	}
	
	@Override
	public int price() {
		return 12 * quantity;
	}
	
}
