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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;


public class NinjaArmor extends Armor {

	{
		image = ItemSpriteSheet.NINJACLOTHE;

		armorType = ArmorType.metaled;
	}

	public NinjaArmor() {
		super( 4 );
	}


	@Override
	public int DRMin(int lvl) {
		return  0 +    //最小防御为0
				lvl;		//每升1级就加1
	}
;;
	@Override
	public int DRMax(int lvl) {
		return  6 +    //最大防御为6
				Math.round(lvl*3.5f);		//每升1级就加3.5

	}

	@Override
	public int  ArmorEvasion( int lvl ){
		return 10 +
				lvl;
	}
	//额外五点闪避（DefenseSkill）


	@Override
	public void execute( Hero hero, String action ){
		super.execute(hero ,action);

	}

	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker,defender,damage );

		int level = Math.max( 0, buffedLvl() );

		// lvl 0 - 12.5%
		// lvl 1 - 22.2%
		// lvl 2 - 30.0%
		float procChance;
		if(  hasGlyph( Potential.class,defender ) ){
			//lvl 0 6.2%
			//lvl 1 11.7%
			//lvl 2 16.6%
			procChance = (level+1f)/(level+6f) * Glyph.genericProcChanceMultiplier(defender);
			procChance += (level+1f)/(level+16f) * Glyph.genericProcChanceMultiplier(defender);
		}else{
			procChance = (level+1f)/(level+8f) * Glyph.genericProcChanceMultiplier(defender);
		}

		if (Random.Float() < procChance && defender instanceof Hero) {

			float powerMulti = Math.max(1f, procChance);

			int wands = ((Hero) defender).belongings.charge( powerMulti );
			if (wands > 0) {
				defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);
			}
		}

		return damage;
	}

	//合成配方
	/*public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ClothStrip.class, BoneSpike.class, Shuriken.class};
			inQuantity = new int[]{2, 1, 1};

			cost = 6;

			output = NinjaArmor.class;
			outQuantity = 1;
		}
	}*/


}
