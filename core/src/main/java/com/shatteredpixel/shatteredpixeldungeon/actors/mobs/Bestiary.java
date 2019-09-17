/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {
	
	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth ){
		ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
		addRareMobs(depth, mobs);
		swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}
	
	//returns a rotation of standard mobs, unshuffled.
	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
		switch(depth){
			
			// Sewers
			case 1: default:
				//3x rat, 1x snake
				return new ArrayList<>(Arrays.asList(
						Rat.class, Rat.class, Rat.class,
						Snake.class));
			case 2:
				//2x rat, 1x snake, 2x gnoll
				return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
						Snake.class,
						Gnoll.class, Gnoll.class));
			case 3:
				//1x rat, 1x snake, 3x gnoll, 1x swarm, 1x crab
				return new ArrayList<>(Arrays.asList(Rat.class,
						Snake.class,
						Gnoll.class, Gnoll.class, Gnoll.class,
						Swarm.class,
						Crab.class));
			case 4: case 5:
				//1x gnoll, 1x swarm, 2x crab, 2x slime
				return new ArrayList<>(Arrays.asList(Gnoll.class,
						Swarm.class,
						Crab.class, Crab.class,
						Slime.class, Slime.class));
				
			// Prison
			case 6:
				//3x skeleton, 1x thief, 1x swarm
				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Swarm.class));
			case 7:
				//3x skeleton, 1x thief, 1x shaman, 1x guard
				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,
						Shaman.class,
						Guard.class));
			case 8:
				//2x skeleton, 1x thief, 2x shaman, 2x guard, 1x necromancer
				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Guard.class, Guard.class,
						Necromancer.class));
			case 9: case 10:
				//1x skeleton, 1x thief, 2x shaman, 2x guard, 2x necromancer
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						Thief.class,
						Shaman.class, Shaman.class,
						Guard.class, Guard.class,
						Necromancer.class, Necromancer.class));
				
			// Caves
			case 11:
				//5x bat, 1x brute
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class));
			case 12:
				//5x bat, 5x brute, 1x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
						Brute.class, Brute.class, Brute.class, Brute.class, Brute.class,
						Spinner.class));
			case 13:
				//1x bat, 3x brute, 1x shaman, 1x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class, Brute.class, Brute.class,
						Shaman.class,
						Spinner.class));
			case 14: case 15:
				//1x bat, 3x brute, 1x shaman, 4x spinner
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class, Brute.class, Brute.class,
						Shaman.class,
						Spinner.class, Spinner.class, Spinner.class, Spinner.class));
				
			// City
			case 16:
				//5x elemental, 5x warlock, 1x monk
				return new ArrayList<>(Arrays.asList(
						Elemental.class, Elemental.class, Elemental.class, Elemental.class, Elemental.class,
						Warlock.class, Warlock.class, Warlock.class, Warlock.class, Warlock.class,
						Monk.class));
			case 17:
				//2x elemental, 2x warlock, 2x monk
				return new ArrayList<>(Arrays.asList(
						Elemental.class, Elemental.class,
						Warlock.class, Warlock.class,
						Monk.class, Monk.class));
			case 18:
				//1x elemental, 1x warlock, 2x monk, 1x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.class,
						Warlock.class,
						Monk.class, Monk.class,
						Golem.class));
			case 19: case 20:
				//1x elemental, 1x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.class,
						Warlock.class,
						Monk.class, Monk.class,
						Golem.class, Golem.class, Golem.class));
				
			// Halls
			case 21: case 22:
				//3x succubus, 3x evil eye
				return new ArrayList<>(Arrays.asList(
						Succubus.class, Succubus.class, Succubus.class,
						Eye.class, Eye.class, Eye.class));
			case 23:
				//2x succubus, 4x evil eye, 2x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class, Succubus.class,
						Eye.class, Eye.class, Eye.class, Eye.class,
						Scorpio.class, Scorpio.class));
			case 24: case 25: case 26:
				//1x succubus, 2x evil eye, 3x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class, Scorpio.class, Scorpio.class));
		}
		
	}
	
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
		
		switch (depth){
			
			// Sewers
			default:
				return;
			case 4:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;
				
			// Prison
			case 8:
				if (Random.Float() < 0.02f) rotation.add(Bat.class);
				return;
			case 9:
				if (Random.Float() < 0.02f) rotation.add(Bat.class);
				if (Random.Float() < 0.01f) rotation.add(Brute.class);
				return;
				
			// Caves
			case 13:
				if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				return;
			case 14:
				if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				if (Random.Float() < 0.01f) rotation.add(Monk.class);
				return;
				
			// City
			case 19:
				if (Random.Float() < 0.02f) rotation.add(Succubus.class);
				return;
		}
	}
	
	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
		for (int i = 0; i < rotation.size(); i++){
			if (Random.Int( 50 ) == 0) {
				Class<? extends Mob> cl = rotation.get(i);
				if (cl == Rat.class) {
					cl = Albino.class;
				} else if (cl == Slime.class) {
					cl = CausticSlime.class;
				} else if (cl == Thief.class) {
					cl = Bandit.class;
				} else if (cl == Brute.class) {
					cl = Shielded.class;
				} else if (cl == Monk.class) {
					cl = Senior.class;
				} else if (cl == Scorpio.class) {
					cl = Acidic.class;
				}
				rotation.set(i, cl);
			}
		}
	}
}
