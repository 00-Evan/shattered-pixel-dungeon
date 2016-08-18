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
package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class Blob extends Actor {

	{
		actPriority = 1; //take priority over mobs, but not the hero
	}
	
	public int volume = 0;
	
	public int[] cur;
	protected int[] off;
	
	public BlobEmitter emitter;
	
	private static final String CUR		= "cur";
	private static final String START	= "start";
	private static final String LENGTH	= "length";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		if (volume > 0) {
		
			int start;
			for (start=0; start < Dungeon.level.length(); start++) {
				if (cur[start] > 0) {
					break;
				}
			}
			int end;
			for (end=Dungeon.level.length()-1; end > start; end--) {
				if (cur[end] > 0) {
					break;
				}
			}
			
			bundle.put( START, start );
			bundle.put( LENGTH, cur.length );
			bundle.put( CUR, trim( start, end + 1 ) );
			
		}
	}
	
	private int[] trim( int start, int end ) {
		int len = end - start;
		int[] copy = new int[len];
		System.arraycopy( cur, start, copy, 0, len );
		return copy;
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );

		if (bundle.contains(LENGTH)) {
			cur = new int[bundle.getInt(LENGTH)];
		} else {
			//compatability with pre-0.4.2
			cur = new int[1024];
		}
		off = new int[cur.length];
		
		int[] data = bundle.getIntArray( CUR );
		if (data != null) {
			int start = bundle.getInt( START );
			for (int i=0; i < data.length; i++) {
				cur[i + start] = data[i];
				volume += data[i];
			}
		}
	}
	
	@Override
	public boolean act() {
		
		spend( TICK );
		
		if (volume > 0) {

			volume = 0;
			evolve();
			
			int[] tmp = off;
			off = cur;
			cur = tmp;
			
		}
		
		return true;
	}
	
	public void use( BlobEmitter emitter ) {
		this.emitter = emitter;
	}
	
	protected void evolve() {
		
		boolean[] notBlocking = BArray.not( Level.solid, null );
		
		for (int i=1; i < Dungeon.level.height()-1; i++) {
			
			int from = i * Dungeon.level.width() + 1;
			int to = from + Dungeon.level.width() - 2;
			
			for (int pos=from; pos < to; pos++) {
				if (notBlocking[pos]) {
					
					int count = 1;
					int sum = cur[pos];
					
					if (notBlocking[pos-1]) {
						sum += cur[pos-1];
						count++;
					}
					if (notBlocking[pos+1]) {
						sum += cur[pos+1];
						count++;
					}
					if (notBlocking[pos-Dungeon.level.width()]) {
						sum += cur[pos-Dungeon.level.width()];
						count++;
					}
					if (notBlocking[pos+Dungeon.level.width()]) {
						sum += cur[pos+Dungeon.level.width()];
						count++;
					}
					
					int value = sum >= count ? (sum / count) - 1 : 0;
					off[pos] = value;
					
					volume += value;
				} else {
					off[pos] = 0;
				}
			}
		}
	}

	public void seed( Level level, int cell, int amount ) {
		if (cur == null) cur = new int[level.length()];
		if (off == null) off = new int[cur.length];

		cur[cell] += amount;
		volume += amount;
	}
	
	public void clear( int cell ) {
		volume -= cur[cell];
		cur[cell] = 0;
	}

	public void fullyClear(){
		volume = 0;
		cur = new int[Dungeon.level.length()];
		off = new int[Dungeon.level.length()];
	}
	
	public String tileDesc() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static<T extends Blob> T seed( int cell, int amount, Class<T> type ) {
		try {
			
			T gas = (T)Dungeon.level.blobs.get( type );
			if (gas == null) {
				gas = type.newInstance();
				Dungeon.level.blobs.put( type, gas );
			}
			
			gas.seed( Dungeon.level, cell, amount );
			
			return gas;
			
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
}
