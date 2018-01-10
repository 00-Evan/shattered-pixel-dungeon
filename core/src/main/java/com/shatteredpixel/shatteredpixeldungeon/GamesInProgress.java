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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GamesInProgress {
	
	//null means we have loaded info and it is empty, no entry means unknown.
	private static HashMap<Integer, Info> slotStates = new HashMap<>();
	public static int curSlot;
	
	private static final String GAME_FOLDER = "game%d";
	private static final String GAME_FILE	= "game.dat";
	private static final String DEPTH_FILE	= "depth%d.dat";
	
	public static boolean gameExists( int slot ){
		return FileUtils.dirExists(Messages.format(GAME_FOLDER, slot));
	}
	
	public static File gameFolder( int slot ){
		return FileUtils.getDir(Messages.format(GAME_FOLDER, slot));
	}
	
	public static File gameFile( int slot ){
		return FileUtils.getFile(gameFolder( slot ), GAME_FILE);
	}
	
	public static File depthFile( int slot, int depth ) {
		return FileUtils.getFile( gameFolder(slot), Messages.format(DEPTH_FILE, depth));
	}
	
	public static Info check( int slot ) {
		
		if (slotStates.containsKey( slot )) {
			
			return slotStates.get( slot );
			
		} else if (!gameExists( slot )) {
			
			slotStates.put(slot, null);
			return null;
			
		} else {
			
			Info info;
			try {
				
				Bundle bundle = FileUtils.bundleFromFile(gameFile(slot));
				info = new Info();
				Dungeon.preview(info, bundle);
				
				//saves from before 0.4.3c are not supported
				if (info.version < ShatteredPixelDungeon.v0_4_3c) {
					info = null;
					
				}

			} catch (IOException e) {
				info = null;
			}
			
			slotStates.put( slot, info );
			return info;
			
		}
	}

	public static void set( int slot, int depth, int challenges,
	                        int level, HeroClass heroClass, HeroSubClass subClass) {
		Info info = new Info();
		info.depth = depth;
		info.challenges = challenges;
		
		info.level = level;
		info.heroClass = heroClass;
		info.subClass = subClass;
		
		slotStates.put( slot, info );
	}
	
	public static void setUnknown( int slot ) {
		slotStates.remove( slot );
	}
	
	public static void delete( int slot ) {
		slotStates.put( slot, null );
	}
	
	public static class Info {
		public int depth;
		public int version;
		public int challenges;
		
		public int level;
		public HeroClass heroClass;
		public HeroSubClass subClass;
	}
}
