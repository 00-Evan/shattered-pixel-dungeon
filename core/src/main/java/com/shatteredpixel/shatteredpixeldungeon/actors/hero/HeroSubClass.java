/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Game;

public enum HeroSubClass {

	NONE(HeroIcon.NONE),

	BERSERKER(HeroIcon.BERSERKER),
	GLADIATOR(HeroIcon.GLADIATOR),

	BATTLEMAGE(HeroIcon.BATTLEMAGE),
	WARLOCK(HeroIcon.WARLOCK),
	
	ASSASSIN(HeroIcon.ASSASSIN),
	FREERUNNER(HeroIcon.FREERUNNER),
	
	SNIPER(HeroIcon.SNIPER),
	WARDEN(HeroIcon.WARDEN);

	int icon;

	HeroSubClass(int icon){
		this.icon = icon;
	}
	
	public String title() {
		return Messages.get(this, name());
	}

	public String shortDesc() {
		return Messages.get(this, name()+"_short_desc");
	}

	public String desc() {
		//Include the staff effect description in the battlemage's desc if possible
		if (this == BATTLEMAGE){
			String desc = Messages.get(this, name() + "_desc");
			if (Game.scene() instanceof GameScene){
				MagesStaff staff = Dungeon.hero.belongings.getItem(MagesStaff.class);
				if (staff != null && staff.wandClass() != null){
					desc += "\n\n" + Messages.get(staff.wandClass(), "bmage_desc");
					desc = desc.replaceAll("_", "");
				}
			}
			return desc;
		} else {
			return Messages.get(this, name() + "_desc");
		}
	}

	public int icon(){
		return icon;
	}

}
