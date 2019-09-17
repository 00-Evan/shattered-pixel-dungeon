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

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeSprite;
import com.watabou.utils.Random;

public class Slime extends Mob {
	
	{
		spriteClass = SlimeSprite.class;
		
		HP = HT = 25;
		defenseSkill = 5;
		
		EXP = 4;
		maxLvl = 9;
		
		lootChance = 0.1f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public void damage(int dmg, Object src) {
		super.damage(Math.min(dmg, 6), src);
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		return super.defenseProc(enemy, Math.min(damage, 6));
	}
	
	@Override
	protected Item createLoot() {
		try {
			Generator.Category c = Generator.Category.WEP_T2;
			MeleeWeapon w = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			w.level(0);
			return w;
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
}
