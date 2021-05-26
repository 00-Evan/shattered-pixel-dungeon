/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.elementalpixel.elementalpixeldungeon.items.potions.brews;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Blob;
import com.elementalpixel.elementalpixeldungeon.actors.blobs.Inferno;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroSubClass;
import com.elementalpixel.elementalpixeldungeon.items.potions.AlchemicalCatalyst;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class InfernalBrew extends Brew {
	
	{
		image = ItemSpriteSheet.BREW_INFERNAL;
	}
	
	@Override
	public void shatter(int cell) {
		
		if (Dungeon.level.heroFOV[cell]) {
			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}
		if (curUser.subClass == HeroSubClass.SCIENTIST) {
			GameScene.add( Blob.seed( cell, 1400, Inferno.class ) );
		} else {
			GameScene.add( Blob.seed( cell, 1000, Inferno.class ) );
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (30 + 40);
	}
	
	public static class Recipe extends com.elementalpixel.elementalpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfLiquidFlame.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = InfernalBrew.class;
			outQuantity = 1;
		}
		
	}
}
