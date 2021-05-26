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
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Ooze;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroSubClass;
import com.elementalpixel.elementalpixeldungeon.effects.Splash;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfToxicGas;
import com.elementalpixel.elementalpixeldungeon.items.quest.GooBlob;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class CausticBrew extends Brew {
	
	{
		image = ItemSpriteSheet.BREW_CAUSTIC;
	}
	
	@Override
	public void shatter(int cell) {
		
		if (Dungeon.level.heroFOV[cell]) {
			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
		}
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 3 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Splash.at( i, 0x000000, 5);
				Char ch = Actor.findChar(i);
				
				if (ch != null){
					if (curUser.subClass == HeroSubClass.SCIENTIST) {
						Buff.affect(ch, Ooze.class).set( Ooze.DURATION * 1.4f);
					} else {
						Buff.affect(ch, Ooze.class).set( Ooze.DURATION );
					}
				}
			}
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (30 + 50);
	}
	
	public static class Recipe extends com.elementalpixel.elementalpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfToxicGas.class, GooBlob.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = CausticBrew.class;
			outQuantity = 1;
		}
		
	}
}
