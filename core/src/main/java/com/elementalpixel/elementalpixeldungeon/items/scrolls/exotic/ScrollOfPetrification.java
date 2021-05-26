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

package com.elementalpixel.elementalpixeldungeon.items.scrolls.exotic;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Paralysis;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Mob;
import com.elementalpixel.elementalpixeldungeon.effects.Flare;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfPetrification extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_PETRIF;
	}
	
	@Override
	public void doRead() {
		new Flare( 5, 32 ).color( 0xFF0000, true ).show( curUser.sprite, 2f );
		Sample.INSTANCE.play( Assets.Sounds.READ );
		
		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
				Buff.affect( mob, Paralysis.class, Paralysis.DURATION );
			}
		}

		identify();
		
		readAnimation();
	}
}
