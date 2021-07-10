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

package com.saqfish.spdnet.items.weapon.curses;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Invisibility;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.items.weapon.Weapon;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Annoying extends Weapon.Enchantment {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {

		float procChance = 1/20f * procChanceMultiplier(attacker);
		if (Random.Float() < procChance) {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				mob.beckon(attacker.pos);
			}
			attacker.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
			Sample.INSTANCE.play(Assets.Sounds.MIMIC);
			Invisibility.dispel();
			GLog.n(Messages.get(this, "msg_" + (Random.Int(5)+1)));
		}

		return damage;
	}

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

}