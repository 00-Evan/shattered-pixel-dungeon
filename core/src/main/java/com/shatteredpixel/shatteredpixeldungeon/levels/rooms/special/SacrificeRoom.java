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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;

public class SacrificeRoom extends SpecialRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.CHASM );

		Point c = center();
		Door door = entrance();
		if (door.x == left || door.x == right) {
			Point p = Painter.drawInside( level, this, door, Math.abs( door.x - c.x ) - 2, Terrain.EMPTY_SP );
			for (; p.y != c.y; p.y += p.y < c.y ? +1 : -1) {
				Painter.set( level, p, Terrain.EMPTY_SP );
			}
		} else {
			Point p = Painter.drawInside( level, this, door, Math.abs( door.y - c.y ) - 2, Terrain.EMPTY_SP );
			for (; p.x != c.x; p.x += p.x < c.x ? +1 : -1) {
				Painter.set( level, p, Terrain.EMPTY_SP );
			}
		}

		Painter.fill( level, c.x - 1, c.y - 1, 3, 3, Terrain.EMBERS );
		Painter.set( level, c, Terrain.PEDESTAL );

		Blob.seed( level.pointToCell(c), 6 + Dungeon.depth * 4, SacrificialFire.class, level ).setPrize(prize(level));

		door.set( Door.Type.EMPTY );
	}

	public static Item prize( Level level ) {

		//1 floor set higher than normal
		Weapon prize = Generator.randomWeapon( (Dungeon.depth / 5) + 1);

		if (Challenges.isItemBlocked(prize)){
			return new Gold().random();
		}

		//if it isn't already cursed, give it a free upgrade
		if (!prize.cursed){
			prize.upgrade();
			//curse the weapon, unless it has a glyph
			if (!prize.hasGoodEnchant()){
				prize.enchant(Weapon.Enchantment.randomCurse());
			}
		}
		prize.cursed = prize.cursedKnown = true;

		return prize;
	}

}
