/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.actors.blobs;

import java.util.Arrays;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.utils.BArray;
import com.watabou.utils.Bundle;

public class Blob extends Actor {
	
	public static final int WIDTH	= Level.WIDTH;
	public static final int HEIGHT	= Level.HEIGHT;
	public static final int LENGTH	= Level.LENGTH;
	
	public int volume = 0;
	
	public int[] cur;
	protected int[] off;
	
	public BlobEmitter emitter;
	
	protected Blob() {
		
		cur = new int[LENGTH];
		off = new int[LENGTH];
		
		volume = 0;
	}
	
	private static final String CUR		= "cur";
	private static final String START	= "start";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		
		if (volume > 0) {
		
			int start;
			for (start=0; start < LENGTH; start++) {
				if (cur[start] > 0) {
					break;
				}
			}
			int end;
			for (end=LENGTH-1; end > start; end--) {
				if (cur[end] > 0) {
					break;
				}
			}
			
			bundle.put( START, start );
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
		
		int[] data = bundle.getIntArray( CUR );
		if (data != null) {
			int start = bundle.getInt( START );	
			for (int i=0; i < data.length; i++) {
				cur[i + start] = data[i];
				volume += data[i];
			}
		}
		
		if (Level.resizingNeeded) {
			int[] cur = new int[Level.LENGTH];
			Arrays.fill( cur, 0 );
			
			int loadedMapSize = Level.loadedMapSize;
			for (int i=0; i < loadedMapSize; i++) {
				System.arraycopy( this.cur, i * loadedMapSize, cur, i * Level.WIDTH, loadedMapSize );
			}
			
			this.cur = cur;
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
		
		for (int i=1; i < HEIGHT-1; i++) {
			
			int from = i * WIDTH + 1;
			int to = from + WIDTH - 2;
			
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
					if (notBlocking[pos-WIDTH]) {
						sum += cur[pos-WIDTH];
						count++;
					}
					if (notBlocking[pos+WIDTH]) {
						sum += cur[pos+WIDTH];
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
	
	public void seed( int cell, int amount ) {
		cur[cell] += amount;
		volume += amount;
	}
	
	public void clear( int cell ) {
		volume -= cur[cell];
		cur[cell] = 0;
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
			
			gas.seed( cell, amount );
			
			return gas;
			
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}
}
