/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;

public class Grim extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {

		if (defender.isImmune(Grim.class)) {
			return damage;
		}

		int level = Math.max( 0, weapon.buffedLvl() );

		//scales from 0 - 50% based on how low hp the enemy is, plus 0-5% per level
		float maxChance = 0.5f + .05f*level;
		maxChance *= procChanceMultiplier(attacker);

		//we defer logic using an actor here so we can know the true final damage
		//see Char.damage
		Buff.affect(defender, GrimTracker.class).maxChance = maxChance;

		if (defender.buff(GrimTracker.class) != null
				&& attacker instanceof Hero
				&& weapon.hasEnchant(Grim.class, attacker)){
			defender.buff(GrimTracker.class).qualifiesForBadge = true;
		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

	public static class GrimTracker extends Buff {

		{
			actPriority = Actor.VFX_PRIO;
		}

		public float maxChance;
		public boolean qualifiesForBadge;

		@Override
		public boolean act() {
			detach();
			return true;
		}
	};

}
