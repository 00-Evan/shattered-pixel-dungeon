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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class HealingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.HEALING_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		
		//heals 30 hp at base, scaling with enemy HT
		PotionOfHealing.cure( defender );
		Buff.affect( defender, Healing.class ).setHeal((int)(0.5f*defender.HT + 30), 0.25f, 0);
		
		if (attacker.alignment == defender.alignment){
			return 0;
		}
		
		return super.proc(attacker, defender, damage);
	}
	
}
