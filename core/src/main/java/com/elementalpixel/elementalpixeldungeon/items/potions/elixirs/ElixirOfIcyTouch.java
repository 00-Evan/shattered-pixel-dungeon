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

package com.elementalpixel.elementalpixeldungeon.items.potions.elixirs;


import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.FrostImbue;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroSubClass;
import com.elementalpixel.elementalpixeldungeon.effects.particles.SnowParticle;
import com.elementalpixel.elementalpixeldungeon.items.potions.AlchemicalCatalyst;
import com.elementalpixel.elementalpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;

public class ElixirOfIcyTouch extends Elixir {
	
	{
		image = ItemSpriteSheet.ELIXIR_ICY;
	}
	
	@Override
	public void apply(Hero hero) {
		if (curUser.subClass == HeroSubClass.SCIENTIST) {
			Buff.affect(hero, FrostImbue.class, FrostImbue.DURATION * 1.4f);
			hero.sprite.emitter().burst(SnowParticle.FACTORY, 8);
		} else {
			Buff.affect(hero, FrostImbue.class, FrostImbue.DURATION);
			hero.sprite.emitter().burst(SnowParticle.FACTORY, 5);
		}
	}
	
	@Override
	protected int splashColor() {
		return 0xFF18C3E6;
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (50 + 40);
	}
	
	public static class Recipe extends com.elementalpixel.elementalpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfSnapFreeze.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = ElixirOfIcyTouch.class;
			outQuantity = 1;
		}
		
	}
}
