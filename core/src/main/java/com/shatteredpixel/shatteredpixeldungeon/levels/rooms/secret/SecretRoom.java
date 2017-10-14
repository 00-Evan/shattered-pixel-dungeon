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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.scenes.StartScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class SecretRoom extends SpecialRoom {
	
	
	private static final ArrayList<Class<? extends SecretRoom>> ALL_SECRETS = new ArrayList<>( Arrays.asList(
			SecretGardenRoom.class, SecretLaboratoryRoom.class, SecretLibraryRoom.class,
			SecretLarderRoom.class, SecretWellRoom.class, SecretRunestoneRoom.class,
			SecretArtilleryRoom.class, SecretChestChasmRoom.class, SecretHoneypotRoom.class,
			SecretHoardRoom.class, SecretMazeRoom.class, SecretSummoningRoom.class));
	
	public static ArrayList<Class<? extends SecretRoom>> runSecrets = new ArrayList<>();

	//this is the number of secret rooms per region (whole value),
	// plus the chance for an extra secret room (fractional value)
	private static float[] baseRegionSecrets = new float[]{1.4f, 1.8f, 2.2f, 2.6f, 3.0f};
	private static int[] regionSecretsThisRun = new int[5];
	
	public static void initForRun(){
		
		float[] regionChances = baseRegionSecrets.clone();
		
		if (StartScene.curClass == HeroClass.ROGUE){
			for (int i = 0; i < regionChances.length; i++){
				regionChances[i] += 0.6f;
			}
		}
		
		for (int i = 0; i < regionSecretsThisRun.length; i++){
			regionSecretsThisRun[i] = (int)regionChances[i];
			if (Random.Float() < regionChances[i] % 1f){
				regionSecretsThisRun[i]++;
			}
		}
		
		runSecrets = new ArrayList<>(ALL_SECRETS);
		Random.shuffle(runSecrets);
		
	}
	
	public static int secretsForFloor(int depth){
		if (depth == 1) return 0;
		
		int region = depth/5;
		int floor = depth%5;
		
		int floorsLeft = 5 - floor;
		
		float secrets;
		if (floorsLeft == 0) {
			secrets = regionSecretsThisRun[region];
		} else {
			secrets = regionSecretsThisRun[region] / floorsLeft;
			if (Random.Float() < secrets % 1f){
				secrets = (float)Math.ceil(secrets);
			} else {
				secrets = (float)Math.floor(secrets);
			}
		}
		
		regionSecretsThisRun[region] -= (int)secrets;
		return (int)secrets;
	}
	
	public static SecretRoom createRoom(){
		
		SecretRoom r = null;
		int index = runSecrets.size();
		for (int i = 0; i < 4; i++){
			int newidx = Random.Int( runSecrets.size() );
			if (newidx < index) index = newidx;
		}
		try {
			r = runSecrets.get( index ).newInstance();
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
		}
		
		runSecrets.add(runSecrets.remove(index));
		
		return r;
	}
	
	private static final String ROOMS	= "secret_rooms";
	private static final String REGIONS	= "region_secrets";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		runSecrets.clear();
		if (bundle.contains( ROOMS )) {
			for (Class<? extends SecretRoom> type : bundle.getClassArray(ROOMS)) {
				if (type != null) runSecrets.add(type);
			}
			regionSecretsThisRun = bundle.getIntArray(REGIONS);
		} else {
			initForRun();
			ShatteredPixelDungeon.reportException(new Exception("secrets array didn't exist!"));
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		bundle.put( ROOMS, runSecrets.toArray(new Class[0]) );
		bundle.put( REGIONS, regionSecretsThisRun );
	}

}
