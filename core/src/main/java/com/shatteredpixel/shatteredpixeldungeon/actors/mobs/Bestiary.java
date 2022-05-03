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

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.RLPT;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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

	//支离破碎
	private static ArrayList<Class<? extends Mob>> standardMobRotation(int i) {
		if (Dungeon.isChallenged(RLPT)){
			switch (i) {
				case 1:
					//3x rat, 1x snake
					return new ArrayList<>(Arrays.asList(
							Rat.class, Rat.class,
							Rat.class, OGPDZSLS.class, Snake.class,
							Snake.class,Snake.class,Snake.class));
				case 2:
					return new ArrayList<>(Arrays.asList(Rat.class,
							Rat.class, Rat.class, Gnoll.class, Gnoll.class,
							Gnoll.class, OGPDLLS.class, OGPDNQHZ.class, Crab.class));
				case 3:
					return new ArrayList<>(Arrays.asList(Rat.class,
							Rat.class, Rat.class, Gnoll.class, Gnoll.class,
							Gnoll.class, OGPDLLS.class, OGPDNQHZ.class,
							OGPDZSLS.class, Rat.class, Rat.class,
							Snake.class,
							Crab.class));
				case 4:
					return new ArrayList<>(Arrays.asList(Rat.class,
							Gnoll.class, Gnoll.class
							, OGPDLLS.class,
							Snake.class,
							OGPDNQHZ.class, OGPDZSLS.class,
							OGPDLLS.class, Snake.class,
							Slime_Orange.class, Swarm.class, Crab.class));
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					switch (Random.Int(3)) {
						case 0:
							return new ArrayList<>(Arrays.asList(Skeleton.class,
									KagenoNusujin.class, BlackHost.class,
									Thief.class,DM100.class,Necromancer.class));
						case 1:
							return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
									Thief.class,Thief.class,Thief.class,
									DM100.class,Necromancer.class,Necromancer.class,
									DM100.class,SRPDHBLR.class, BlackHost.class));
						case 2:
							return new ArrayList<>(Arrays.asList(
									Bat.class,
									Brute.class,
									Shaman.random(), DM100.class,
									Spinner.class, Spinner.class,
									DM200.class, MolotovHuntsman.class,
									Brute.class,
									MolotovHuntsman.class,MolotovHuntsman.class,FireGhost.class));
					}
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					switch (Random.Int(4)) {
						case 0:
							return new ArrayList<>(Arrays.asList(Skeleton.class,
									KagenoNusujin.class, BlackHost.class,
									Thief.class,DM100.class,Necromancer.class));
						case 1:
							return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
									Thief.class,Thief.class,Thief.class,
									DM100.class,Necromancer.class,Necromancer.class,
									DM100.class,SRPDHBLR.class, BlackHost.class));
						case 2:
							return new ArrayList<>(Arrays.asList(
									Elemental.random(), Elemental.random(), Elemental.random(), Elemental.random(),
									Monk.class,FireGhost.class,FireGhost.class));
						case 3:
							return new ArrayList<>(Arrays.asList(
									Elemental.random(),
									Warlock.class, Warlock.class,
									Monk.class, Monk.class,
									Golem.class, Golem.class, Golem.class,ShieldHuntsman.class));
					}
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
				case 22:
				case 23:
				case 24:
				case 25:
					switch (Random.Int(6)) {
						case 0:
							return new ArrayList<>(Arrays.asList(Skeleton.class,
									KagenoNusujin.class, BlackHost.class,
									Thief.class,DM100.class,Necromancer.class));
						case 1:
							return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
									Thief.class,Thief.class,Fire_Scorpio.class,
									DM201.class,SkullShaman.class,MolotovHuntsman.class,
									DM201.class,SRPDHBLR.class, Ice_Scorpio.class));
						case 2:
							return new ArrayList<>(Arrays.asList(
									Succubus.class, Succubus.class, Succubus.class,
									Succubus.class, Succubus.class,
									Eye.class,ShieldHuntsman.class,Ice_Scorpio.class,Fire_Scorpio.class));
						case 3:
							return new ArrayList<>(Arrays.asList(
									Succubus.class, Succubus.class, Succubus.class,
									Succubus.class, Succubus.class,
									Eye.class,ShieldHuntsman.class,Ice_Scorpio.class,Fire_Scorpio.class,Albino.class));
						case 4:
							return new ArrayList<>(Arrays.asList(
									Succubus.class, Succubus.class, Succubus.class,
									Succubus.class, Succubus.class,
									Eye.class,ShieldHuntsman.class,Ice_Scorpio.class));
						case 5:
							return new ArrayList<>(Arrays.asList(
									Succubus.class,
									Eye.class, Eye.class,
									Scorpio.class, Eye.class, Eye.class,Succubus.class, Succubus.class,
									Scorpio.class, Scorpio.class,Fire_Scorpio.class,Scorpio.class,ShieldHuntsman.class,
									Ice_Scorpio.class,Fire_Scorpio.class));
					}
				default:
					return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
							Rat.class,
							Rat.class, Rat.class, Rat.class,
							Rat.class, Rat.class, Rat.class,
							Rat.class));
			}
		}
		switch (i) {
			//正常刷怪
			case 1:
				//3x rat, 1x snake
				return new ArrayList<>(Arrays.asList(
						Rat.class, Rat.class,
						Rat.class, OGPDZSLS.class, Rat.class,
						Snake.class));
			case 2:
				return new ArrayList<>(Arrays.asList(Rat.class,
						Rat.class, Rat.class, Gnoll.class, Gnoll.class,
						Gnoll.class, OGPDLLS.class, OGPDNQHZ.class));
			case 3:
				return new ArrayList<>(Arrays.asList(Rat.class,
						Rat.class, Rat.class, Gnoll.class, Gnoll.class,
						Gnoll.class, OGPDLLS.class, OGPDNQHZ.class,
						OGPDZSLS.class, Rat.class, Rat.class,
						Snake.class,Crab.class,Crab.class,Swarm.class));
			case 4:
				return new ArrayList<>(Arrays.asList(Rat.class,
						Gnoll.class, Gnoll.class
						, OGPDLLS.class,
						Snake.class,
						OGPDNQHZ.class, OGPDZSLS.class,
						OGPDLLS.class, Snake.class,
						Slime_Orange.class, Swarm.class,Crab.class));
			case 5:
				return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,Slime.class,Slime.class,Swarm.class,Crab.class));
			case 6:
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						BlackHost.class,Guard.class, DM100.class, DM100.class, DM100.class,
						Snake.class));

			case 7:
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						KagenoNusujin.class, BlackHost.class,
						Thief.class,DM100.class,Necromancer.class));
			case 8:
				return new ArrayList<>(Arrays.asList(Skeleton.class,
						 Skeleton.class,
						Thief.class, Shaman.random(),BrownBat.class,BrownBat.class,
						Shaman.random(), Guard.class, SRPDHBLR.class,Necromancer.class,
						Necromancer.class,BrownBat.class));
			case 9:
				return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
						Thief.class,Thief.class,BrownBat.class,
						BrownBat.class,Necromancer.class,Necromancer.class,
						DM100.class,BrownBat.class, BlackHost.class,KagenoNusujin.class));

			case 10:
				return new ArrayList<>(Arrays.asList(Bat.class,
						Brute.class, Brute.class,
						Necromancer.class,Necromancer.class));
			case 11:
				return new ArrayList<>(Arrays.asList(Bat.class,
						Spinner.class, Spinner.class,
						DM200.class, SRPDHBLR.class,
						Brute.class,DM200.class,DM201.class,
						SRPDHBLR.class, FireGhost.class,ColdMagicRat.class,ColdMagicRat.class));
			case 12:
				//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						GnollTrickster.class,
						Shaman.random(), SRPDHBLR.class,
						Spinner.class, Spinner.class,
						DM200.class, SRPDHBLR.class,
						Brute.class,
						SRPDHBLR.class,ColdMagicRat.class,FireGhost.class,ColdMagicRat.class));
			case 13:
				//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						Brute.class,
						MolotovHuntsman.class, Spinner.class,FlameB01.class,
						DM201.class, FlameB01.class,
						Brute.class,FlameB01.class,SRPDHBLR.class,ColdMagicRat.class,RedSwarm.class));

			case 14:
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						MolotovHuntsman.class,
						Spinner.class,
						SRPDHBLR.class, MolotovHuntsman.class,
						Brute.class,SRPDHBLR.class,FireGhost.class,ColdMagicRat.class,RedSwarm.class));
				case 15:
				//1x bat, 1x brute, 2x shaman, 2x spinner, 2x DM-300
				return new ArrayList<>(Arrays.asList(
						Bat.class,
						MolotovHuntsman.class,
						Spinner.class,
						SRPDHBLR.class, MolotovHuntsman.class,
						Brute.class, FlameB01.class,SRPDHBLR.class,FireGhost.class,ColdMagicRat.class,RedSwarm.class));

			// City
			case 16:
				//5x elemental, 5x warlock, 1x monk, 2x silvercrab
				return new ArrayList<>(Arrays.asList(
						Elemental.random(), Elemental.random(), Elemental.random(),
						Monk.class,
						Shaman.random()));
			case 17:
				//2x elemental, 2x warlock, 2x monk, 1x silvercrab
				return new ArrayList<>(Arrays.asList(
						Elemental.random(), Elemental.random(), Elemental.random(), Elemental.random(),
						Monk.class,RedMurderer.class,FireGhost.class,MolotovHuntsman.class));
			case 18:
				//1x elemental, 1x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.random(),RedMurderer.class,
						Warlock.class, Warlock.class,
						Monk.class, Monk.class,
						IceGolem.class, Golem.class, Golem.class,MolotovHuntsman.class,FireGhost.class));
			case 19:
				//1x elemental, 1x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.random(),RedMurderer.class,RedMurderer.class,
						ShieldHuntsman.class, Warlock.class,
						Monk.class, IceGolem.class,IceGolem.class,
						Golem.class, Golem.class, Golem.class,ShieldHuntsman.class));
			case 20:
				//1x elemental, 1x warlock, 2x monk, 3x golem
				return new ArrayList<>(Arrays.asList(
						Elemental.random(),
						Warlock.class, Warlock.class,
						Monk.class, IceGolem.class,
						Golem.class, IceGolem.class, Golem.class,Ice_Scorpio.class));

			case 21:case 22:
				//3x succubus, 3x evil eye
				return new ArrayList<>(Arrays.asList(
						Succubus.class, Succubus.class, Succubus.class,
						Succubus.class, Succubus.class,
						Eye.class,ShieldHuntsman.class,Ice_Scorpio.class));
			case 23:
				//1x: succubus, 2x evil eye, 3x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class, Eye.class, Eye.class,Succubus.class, Succubus.class,
						Scorpio.class, Scorpio.class, Scorpio.class,Fire_Scorpio.class,Ice_Scorpio.class));
			//前半段决战
			case 24:
				//1x succubus, 2x evil eye, 3x scorpio
				return new ArrayList<>(Arrays.asList(
						Succubus.class,
						Eye.class, Eye.class,
						Scorpio.class, Eye.class, Eye.class,Succubus.class, Succubus.class,
						Scorpio.class, Scorpio.class,Fire_Scorpio.class,Scorpio.class,ShieldHuntsman.class,
						Ice_Scorpio.class,Fire_Scorpio.class));
			default:
				return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
						Rat.class,
						Rat.class, Rat.class, Rat.class,
						Rat.class, Rat.class, Rat.class,
						Rat.class));
		}
	}

	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){

		switch (depth){

			// Sewers
			default:
				return;
			case 9:
				if (Random.Float() < 0.025f) rotation.add(Thief.class);
				return;

			// Prison
			case 19:
				if (Random.Float() < 0.025f) rotation.add(Bat.class);
				return;

			// Caves
			case 29:
				if (Random.Float() < 0.025f) rotation.add(Ghoul.class);
				return;

			// City
			case 39:
				if (Random.Float() < 0.025f) rotation.add(Succubus.class);
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
					cl = ArmoredBrute.class;
				} else if (cl == DM200.class) {
					cl = DM201.class;
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
