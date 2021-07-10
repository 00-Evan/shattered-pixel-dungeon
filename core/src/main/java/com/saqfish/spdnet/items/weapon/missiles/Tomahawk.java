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

package com.saqfish.spdnet.items.weapon.missiles;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Bleeding;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;

public class Tomahawk extends MissileWeapon {

	{
		image = ItemSpriteSheet.TOMAHAWK;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 0.9f;

		tier = 4;
		baseUses = 5;
	}

	@Override
	public int min(int lvl) {
		return  Math.round(1.5f * tier) +   //6 base, down from 8
				2 * lvl;                    //scaling unchanged
	}
	
	@Override
	public int max(int lvl) {
		return  Math.round(3.75f * tier) +  //15 base, down from 20
				(tier)*lvl;                 //scaling unchanged
	}
	
	@Override
	public int proc( Char attacker, Char defender, int damage ) {
		Buff.affect( defender, Bleeding.class ).set( Math.round(damage*0.6f) );
		return super.proc( attacker, defender, damage );
	}
}
