/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.utils.Bundle;

import java.io.IOException;
import java.util.HashMap;

public class GamesInProgress {

	private static HashMap<HeroClass, Info> state = new HashMap<HeroClass, Info>();
	
	public static Info check( HeroClass cl ) {
		
		if (state.containsKey( cl )) {
			
			return state.get( cl );
			
		} else {
			
			Info info;
			try {
				
				Bundle bundle = Dungeon.gameBundle( Dungeon.gameFile( cl ) );
				info = new Info();
				Dungeon.preview( info, bundle );

			} catch (IOException e) {
				info = null;
			}
			
			state.put( cl, info );
			return info;
			
		}
	}

	public static void set( HeroClass cl, int depth, int level, boolean challenges ) {
		Info info = new Info();
		info.depth = depth;
		info.level = level;
		info.challenges = challenges;
		state.put( cl, info );
	}
	
	public static void setUnknown( HeroClass cl ) {
		state.remove( cl );
	}
	
	public static void delete( HeroClass cl ) {
		state.put( cl, null );
	}
	
	public static class Info {
		public int depth;
		public int level;
		public boolean challenges;
	}
}
