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

package com.elementalpixel.elementalpixeldungeon.items.food;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Challenges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.Statistics;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.BlobImmunity;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Haste;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Hunger;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Invisibility;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Recharging;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Talent;
import com.elementalpixel.elementalpixeldungeon.effects.SpellSprite;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

	public static final float TIME_TO_EAT	= 3f;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Hunger.HUNGRY;
	
	{
		stackable = true;
		image = ItemSpriteSheet.RATION;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );
			
			satisfy(hero);
			GLog.i( Messages.get(this, "eat_msg") );
			
			hero.sprite.operate( hero.pos );
			hero.busy();
			SpellSprite.show( hero, SpellSprite.FOOD );
			Sample.INSTANCE.play( Assets.Sounds.EAT );
			
			hero.spend( eatingTime() );

			Talent.onFoodEaten(hero, energy, this);
			
			Statistics.foodEaten++;
			Badges.validateFoodEaten();
			
		}
	}

	protected float eatingTime(){
		if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
			|| Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
			|| Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
			|| Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)){
			return TIME_TO_EAT - 2;
		} else {
			return TIME_TO_EAT;
		}
	}
	
	protected void satisfy( Hero hero ){
		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			Buff.affect(hero, Hunger.class).satisfy(energy/3f);
		} else {
			Buff.affect(hero, Hunger.class).satisfy(energy);
		}

		switch (hero.heroClass) {
			case WARRIOR:
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + (hero.HT) / 5, hero.HT );
				}
				break;
			case MAGE:
				Buff.affect(hero, Recharging.class, Recharging.DURATION - 25f);
				break;
			case ROGUE:
				Buff.affect(hero, Invisibility.class, Invisibility.DURATION - 17f);
				break;
			case HUNTRESS:
				Buff.affect(hero, Haste.class, Haste.DURATION - 16f);
				break;
			case ALCHEMIST:
				Buff.affect(hero, BlobImmunity.class, BlobImmunity.DURATION - 13f);
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 10 * quantity;
	}
}
