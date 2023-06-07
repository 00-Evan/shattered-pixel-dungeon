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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TurtleShield;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantTultleSprite;
import com.watabou.utils.Random;

public class GiantTultle extends Mob {

	{
		spriteClass = GiantTultleSprite.class;
		
		HP = HT = 40;
		defenseSkill = 3;
		baseSpeed = 0.9f;
		
		EXP = 4;
		maxLvl = 9;

		loot = new MysteryMeat();
		lootChance = 1f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 4 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 1;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 2);
	}


	public boolean DefensePose;
	private int Cooldown = 0;

	@Override
	protected boolean canAttack( Char enemy ) {

		if ( HP <= 12 | Cooldown == 0){
			DefensePose = true;
			Cooldown = 10;
		}else {
			return super.canAttack(enemy);//canAttack(enemy)
		}
		return  true;
	}

	@Override
	protected boolean act() {
		if (DefensePose && state != HUNTING) {
			DefensePose = false;
			sprite.idle();
		}
		if ( DefensePose ){
			spend(5f);
			DefensePose = false;
		}
		/*if ( DefensePose == false | Cooldown == 0 ) {
			DefensePose = true;
			Cooldown = 10;
			super.act();
		}*/
		if (Cooldown > 0)
			Cooldown --;
			DefensePose = false;
		return super.act();
	}


	@Override
	protected boolean doAttack( Char enemy ) {
		if ( Cooldown > 0 & HP > 12 ) {
			return super.doAttack(enemy);
		}else if ( Cooldown > 0 & HP <= 12 ){
			spend( attackDelay() );
			return true;
		} else if ( Cooldown == 0 | HP <= 12 ){
			((GiantTultleSprite)sprite).defense(enemy.pos );
			spend( attackDelay()*2f );
			Cooldown = 10;
			} return true;
		}



	@Override
	public void damage(int dmg, Object src) {
		if ( DefensePose ) dmg /= 4;
		super.damage(dmg, src);
	}

//战利品掉落
	@Override
	public Item createLoot() {
		Item loot;
		switch(Random.Int(6)){
			case 0: default:
				loot = new MysteryMeat();
				break;
			case 1:
				loot = new TurtleShield();
				break;
			case 2:case 3:case 4:case 5:
				loot = null;
				break;
		}
		return loot;
	}

}
