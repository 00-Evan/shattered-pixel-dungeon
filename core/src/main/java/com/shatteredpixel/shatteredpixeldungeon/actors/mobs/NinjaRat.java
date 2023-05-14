/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NinjaRatSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class NinjaRat extends Mob {

	{
		spriteClass = NinjaRatSprite.class;

		HP = HT = 12;
		defenseSkill = 2;

		maxLvl = 5;

		loot = Shuriken.class;/*掉落物 Shuriken */
		lootChance = 0.1f;/*掉落概率 */
	}

	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos] && Dungeon.hero.armorAbility instanceof Ratmogrify){
			alignment = Alignment.ALLY;
			if (state == SLEEPING) state = WANDERING;
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 6 );
	}


	@Override
	public int attackSkill( Char target ) {
		return 4;
	}


	@Override
	protected boolean canAttack( Char enemy ) {
		return	(super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos);
	}

	/*@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	逃跑代码*/


	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 1);
	}

	private static final String RAT_ALLY = "rat_ally";

	@Override
	public Item createLoot() {
		MissileWeapon drop = (MissileWeapon)super.createLoot();
		//half quantity, rounded up
		drop.quantity(1);
		drop.level(Random.Float() <=0.1f ? 1 : 0);
		return drop;
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (alignment == Alignment.ALLY) bundle.put(RAT_ALLY, true);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(RAT_ALLY)) alignment = Alignment.ALLY;
	}
}



