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
	
	public enum SizeCategory {
		
		NORMAL(4, 10, 1),
		LARGE(10, 14, 2),
		GIANT(14, 18, 3);
		
		public final int minDim, maxDim;
		public final int roomValue;
		
		SizeCategory(int min, int max, int val){
			minDim = min;
			maxDim = max;
			roomValue = val;
		}
		
		public int connectionWeight(){
			return roomValue*roomValue;
		}
		
	}
	
	public SizeCategory sizeCat;
	{ setSizeCat(); }
	
	//Note that if a room wishes to allow itself to be forced to a certain size category,
	//but would (effectively) never roll that size category, consider using Float.MIN_VALUE
	public float[] sizeCatProbs(){
		//always normal by default
		return new float[]{1, 0, 0};
	}
	
	public boolean setSizeCat(){
		return setSizeCat(0, SizeCategory.values().length-1);
	}
	
	//assumes room value is always ordinal+1
	public boolean setSizeCat( int maxRoomValue ){
		return setSizeCat(0, maxRoomValue-1);
	}
	
	//returns false if size cannot be set
	public boolean setSizeCat( int minOrdinal, int maxOrdinal ) {
		float[] probs = sizeCatProbs();
		SizeCategory[] categories = SizeCategory.values();
		
		if (probs.length != categories.length) return false;
		
		for (int i = 0; i < minOrdinal; i++)                    probs[i] = 0;
		for (int i = maxOrdinal+1; i < categories.length; i++)  probs[i] = 0;
		
		int ordinal = Random.chances(probs);
		
		if (ordinal != -1){
			sizeCat = categories[ordinal];
			return true;
		} else {
			return false;
		}
	}
	
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
		//currently effective average room size value is 1.15916
		chances.put(EmptyRoom.class,        24f);//average value: 1.0000, +.00000 overall
		
		chances.put(RingRoom.class,         8f); //average value: 1.5000, +.10000 overall
		
		chances.put(GardenRoom.class,       1f); //average value: 1.2500, +.00625 overall
		chances.put(AquariumRoom.class,     1f); //average value: 1.2500, +.00625 overall
		chances.put(PlatformRoom.class,     1f); //average value: 1.5000, +.01250 overall
		chances.put(BurnedRoom.class,       1f); //average value: 1.2000, +.00500 overall
		chances.put(FissureRoom.class,      1f); //average value: 1.5000, +.01250 overall
		chances.put(GrassyGraveRoom.class,  1f); //average value: 1.0000, +.00000 overall
		chances.put(StripedRoom.class,      1f); //average value: 1.3333, +.00833 overall
		chances.put(StudyRoom.class,        1f); //average value: 1.3333, +.00833 overall
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
