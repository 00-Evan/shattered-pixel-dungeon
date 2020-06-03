/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class Slime extends Mob {
	
	{
		spriteClass = SlimeSprite.class;
		
		HP = HT = 20;
		defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;
		
		lootChance = 0.2f; //by default, see rollToDropLoot()
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		if (dmg >= 5){
			//takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
			dmg = 4 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/3 as likely
		// so loot chance looks like: 1/5, 1/15, 1/45, 1/135, etc.
		lootChance *= Math.pow(1/3f, Dungeon.LimitedDrops.SLIME_WEP.count);
		super.rollToDropLoot();
	}
	
	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SLIME_WEP.count++;
		Generator.Category c = Generator.Category.WEP_T2;
		MeleeWeapon w = (MeleeWeapon) Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		w.level(0);
		return w;
	}
}
