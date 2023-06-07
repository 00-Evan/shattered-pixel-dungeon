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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyHealing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.sundry.ClothStrip;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.BoneSpike;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;


public class PastorClothe extends Armor {

	{
		image = ItemSpriteSheet.PASTORCLOTHE;

		armorType = ArmorType.clothed;
	}

	public PastorClothe() { super( 2 ); }


	@Override
	public int DRMin(int lvl) {
		return  0 +    //最小防御为0
				lvl;		//每升1级就加1
	}

	@Override
	public int DRMax(int lvl) {
		return  3 +    //最大防御为3
				lvl;		//每升1级就加1
	}


	//HolyHealing回血速度与回血量
	@Override
	public int Holyspeed(int lvl) {
		return  5 +    //
				lvl;		//每升1级就加1
	}
	@Override
	public int Holyadd(int lvl) {
		return   1 +  //
				lvl;		//每升1级就加1
	}


	//快速合成配方
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ClothStrip.class, BoneSpike.class, ScrollOfRemoveCurse.class};
			inQuantity = new int[]{2, 1, 1};

			cost = 6;

			output = PastorClothe.class;
			outQuantity = 1;
		}
	}


	@Override
	public void execute(Hero hero, String action ){
		super.execute(hero ,action);
		if( action.equals( AC_EQUIP )){
			Buff.affect( hero, HolyHealing.class );
		}
	}

	//@Override
	//public void tintIcon(Image icon) {icon.hardlight( 0xffd700);}//修改颜色	金色
}
