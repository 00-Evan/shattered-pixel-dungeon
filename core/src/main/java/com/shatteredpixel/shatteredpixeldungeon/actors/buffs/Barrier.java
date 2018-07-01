/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;

//FIXME managing shield buffs is starting to get cumbersome, should come up with a way to have them interact easily
public class Barrier extends Buff {
	
	private int level = 1;
	
	//TODO icon and description for phase 2
	
	@Override
	public boolean act() {
		
		if (target.SHLD == level){
			target.SHLD -= 1;
		}
		
		level -= 1;
		
		if (target.SHLD <= level) {
			target.SHLD = level;
		}
		
		if (level <= 0){
			detach();
		}
		
		spend( TICK );
		
		return true;
	}
	
	public void set( int shielding ){
		if (level < shielding){
			level = shielding;
			if (target.SHLD < level){
				target.SHLD = level;
			}
		}
	}
	
	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.SHIELDED);
		else target.sprite.remove(CharSprite.State.SHIELDED);
	}
	
	private static final String LEVEL = "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getInt( LEVEL );
	}
}
