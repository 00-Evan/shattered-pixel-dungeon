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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Random;

public abstract class TippedDart extends Dart {
	
	{
		bones = true;
	}
	
	@Override
	public int STRReq(int lvl) {
		return 11;
	}
	
	@Override
	protected void rangedHit(Char enemy) {
		if (enemy.isAlive())
			Buff.affect(enemy, PinCushion.class).stick(new Dart());
		else
			Dungeon.level.drop( new Dart(), enemy.pos).sprite.drop();
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 3, 5 );
		return this;
	}
	
	@Override
	public int price() {
		return quantity * 6;
	}
}
