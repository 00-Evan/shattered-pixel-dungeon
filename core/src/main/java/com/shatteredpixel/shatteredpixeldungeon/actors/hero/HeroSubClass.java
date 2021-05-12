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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public enum HeroSubClass {

	NONE,
	
	GLADIATOR,
	BERSERKER,
	
	WARLOCK,
	BATTLEMAGE,
	
	ASSASSIN,
	FREERUNNER,
	
	SNIPER,
	WARDEN;
	
	public String title() {
		return Messages.get(this, name());
	}
	
	public String desc() {
		return Messages.get(this, name()+"_desc");
	}

	//FIXME shouldn't hardcode these, probably want to just have a BuffIcon class
	public Image icon(){
		switch (this){
			case GLADIATOR: default:
				return new Image(Assets.Interfaces.BUFFS_LARGE, 16, 16, 16, 16);
			case BERSERKER:
				return new Image(Assets.Interfaces.BUFFS_LARGE, 32, 16, 16, 16);

			case WARLOCK:
				return new Image(Assets.Interfaces.BUFFS_LARGE, 64, 32, 16, 16);
			case BATTLEMAGE:
				Image im = new Image(Assets.Interfaces.BUFFS_LARGE, 32, 48, 16, 16);
				im.hardlight(1f, 1f, 0f);
				return im;

			case ASSASSIN:
				im = new Image(Assets.Interfaces.BUFFS_LARGE, 160, 32, 16, 16);
				im.hardlight(1f, 0f, 0f);
				return im;
			case FREERUNNER:
				im = new Image(Assets.Interfaces.BUFFS_LARGE, 48, 48, 16, 16);
				im.hardlight(1f, 1f, 0f);
				return im;

			case SNIPER:
				return new Image(Assets.Interfaces.BUFFS_LARGE, 176, 16, 16, 16);
			case WARDEN:
				return new Image(Assets.Interfaces.BUFFS_LARGE, 208, 0, 16, 16);
		}
	}

}
