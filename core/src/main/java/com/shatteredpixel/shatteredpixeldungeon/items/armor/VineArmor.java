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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


public class VineArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_MAIL;

		armorType = ArmorType.empty;
	}

	public VineArmor() {
		super( 3 );
	}

	public static boolean AlwaysFire(Armor armor){
		if(hero.belongings.armor() == armor){
			return true;
		}
		// 如果条件不满足，则返回false
		return false;
	}

	@Override
	public int DRMin(int lvl) {
		return  1 +    //最小防御为1
				lvl;		//每升1级就加1
	}


	@Override
	public int DRMax(int lvl) {
		return  5 +    //最大防御为5
				Math.round(lvl*3.5f);		//三阶半护甲成长
	}



}
