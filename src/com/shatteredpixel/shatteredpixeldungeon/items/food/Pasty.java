/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.Calendar;

public class Pasty extends Food {

	//TODO: implement fun stuff for other holidays
	//TODO: probably should externalize this if I want to add any more festive stuff.
	private enum Holiday{
		NONE,
		EASTER, //TBD
		HALLOWEEN,//TBD
		XMAS //3rd week of december through first week of january
	}

	private static Holiday holiday;

	static{
		final Calendar calendar = Calendar.getInstance();
		holiday = Holiday.NONE;
		//we add 1 to turn it into standard calendar months (1-12 instead of 0-11)
		switch(calendar.get(Calendar.MONTH)+1){
			case 1:
				if (calendar.get(Calendar.WEEK_OF_MONTH) == 1){
					holiday = Holiday.XMAS;
				}
				break;
			case 12:
				if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3){
					holiday = Holiday.XMAS;
				}
				break;
		}
	}

	{

		switch(holiday){
			case NONE:
				name = Messages.get(this, "pasty");
				image = ItemSpriteSheet.PASTY;
			break;
			case XMAS:
				name = Messages.get(this, "cane");
				image = ItemSpriteSheet.CANDY_CANE;
				break;
		}

		energy = Hunger.STARVING;
		hornValue = 5;

		bones = true;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_EAT)){
			switch(holiday){
				case NONE: default:
					break; //do nothing extra
				case XMAS:
					Buff.affect( hero, Recharging.class, 2f ); //half of a charge
					ScrollOfRecharging.charge( hero );
					break;
			}
		}
	}

	@Override
	public String info() {
		switch(holiday){
			case NONE: default:
				return Messages.get(this, "pasty_desc");
			case XMAS:
				return Messages.get(this, "cane_desc");
		}
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}
}
