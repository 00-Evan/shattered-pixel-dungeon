/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.actors.mobs;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Ooze;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.potions.PotionOfExperience;
import com.saqfish.spdnet.sprites.AcidicSprite;

public class Acidic extends Scorpio {

	{
		spriteClass = AcidicSprite.class;
		
		properties.add(Property.ACIDIC);

		loot = new PotionOfExperience();
		lootChance = 1f;
	}
	@Override
	public int attackProc(Char enemy, int damage) {
		Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
		return super.attackProc(enemy, damage);
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {
		if (Dungeon.level.adjacent(pos, enemy.pos)){
			Buff.affect(enemy, Ooze.class).set( Ooze.DURATION );
		}
		return super.defenseProc( enemy, damage );
	}

	@Override
	protected Item createLoot() {
		return new PotionOfExperience();
	}
}
