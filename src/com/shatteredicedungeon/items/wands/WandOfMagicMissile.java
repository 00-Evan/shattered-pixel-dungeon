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
package com.shatteredicedungeon.items.wands;

import com.shatteredicedungeon.actors.Actor;
import com.shatteredicedungeon.actors.Char;
import com.shatteredicedungeon.actors.buffs.Buff;
import com.shatteredicedungeon.effects.SpellSprite;
import com.shatteredicedungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredicedungeon.items.weapon.melee.MagesStaff;
import com.shatteredicedungeon.mechanics.Ballistica;
import com.shatteredicedungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class WandOfMagicMissile extends Wand {

	{
		name = "Wand of Magic Missile";
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
	}
	
	@Override
	protected void onZap( Ballistica bolt ) {
				
		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null) {
			
			int level = level();

			ch.damage(Random.NormalIntRange(4 , 6 + level * 2), this);

			ch.sprite.burst(0xFFFFFFFF, level / 2 + 2);

		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//gain 1 turn of recharging buff per level of the wand.
		if (level > 0) {
			Buff.prolong( attacker, ScrollOfRecharging.Recharging.class, (float)staff.level);
			SpellSprite.show(attacker, SpellSprite.CHARGE);
		}
	}
	
	protected int initialCharges() {
		return 3;
	}
	
	@Override
	public String desc() {
		return
			"This wand launches missiles of pure magical energy, dealing moderate damage to a target creature.";
	}
}
