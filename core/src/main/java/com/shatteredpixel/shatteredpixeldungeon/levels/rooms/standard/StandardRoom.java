/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class StandardRoom extends Room {
	
	public enum SizeCategories{
		
		NORMAL(4, 10, 1),
		LARGE(10, 14, 2),
		GIANT(14, 18, 3);
		
		public final int minDim, maxDim;
		public final int roomValue;
		
		SizeCategories(int min, int max, int val){
			minDim = min;
			maxDim = max;
			roomValue = val;
		}
		
		public int connectionWeight(){
			return roomValue*roomValue;
		}
		
	}
	
	public SizeCategories sizeCat = SizeCategories.NORMAL;
	
	@Override
	public int minWidth() { return sizeCat.minDim; }
	public int maxWidth() { return sizeCat.maxDim; }
	
	@Override
	public int minHeight() { return sizeCat.minDim; }
	public int maxHeight() { return sizeCat.maxDim; }
	
	@Override
	public int minConnections(int direction) {
		if (direction == ALL)   return 1;
		else                    return 0;
	}
	
	@Override
	public int maxConnections(int direction) {
		if (direction == ALL)   return 16;
		else                    return 4;
	}
	
	private static HashMap<Class<?extends StandardRoom>, Float> chances = new LinkedHashMap<>();
	
	static {
		chances.put(EmptyRoom.class, 24f);
		
		chances.put(PlatformRoom.class,     1f);
		chances.put(BurnedRoom.class,       1f);
		chances.put(FissureRoom.class,      1f);
		chances.put(GrassyGraveRoom.class,  1f);
		chances.put(StripedRoom.class,      1f);
		chances.put(StudyRoom.class,        1f);
	}
	
	public static StandardRoom createRoom(){
		try{
			return Random.chances(chances).newInstance();
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
	
}
