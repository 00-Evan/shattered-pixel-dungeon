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

package com.elementalpixel.elementalpixeldungeon.items.potions.exotic;


import com.elementalpixel.elementalpixeldungeon.actors.buffs.AdrenalineSurge;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroSubClass;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;

public class PotionOfAdrenalineSurge extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_ARENSURGE;

		unique = true;
	}
	
	@Override
	public void apply(Hero hero) {
		identify();
		if (curUser.subClass == HeroSubClass.SCIENTIST) {
			Buff.affect(hero, AdrenalineSurge.class).reset(2, 801f); //HAHA big buff
		} else {
			Buff.affect(hero, AdrenalineSurge.class).reset(2, 800f);
		}
	}
	
}
