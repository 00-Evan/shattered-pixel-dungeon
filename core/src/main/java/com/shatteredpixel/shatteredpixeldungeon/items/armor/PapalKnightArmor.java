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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;


import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyHealing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


public class PapalKnightArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_SCALE;

		armorType = ArmorType.metaled;
	}

	public PapalKnightArmor() {
		super( 3 );
	}


	@Override
	public int DRMin(int lvl) {
		return  1 +    //最小防御为0
				lvl;		//每升1级就加1
	}

	@Override
	public int DRMax(int lvl) {
		return  4 +    //最大防御为3
				3*lvl;		//每升1级就加1
	}


	//HolyHealing回血速度与回血量
	@Override
	public int Holyspeed(int lvl) {
		return  8 +    //
				lvl;		//每升1级就加1
	}
	@Override
	public int Holyadd(int lvl) {
		return   3 +  //
				lvl;		//每升1级就加1
	}



	@Override
	public void execute(Hero hero, String action ){
		super.execute(hero ,action);
		if( action.equals( AC_EQUIP )){
			Buff.affect( hero, HolyHealing.class );
			Buff.affect( hero, HolyLight.class );
		}
	}


}
