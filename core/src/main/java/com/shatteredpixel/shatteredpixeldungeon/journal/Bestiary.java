/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredBrute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bandit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Brute;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CausticSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalGuardian;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalSpire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalWisp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.EbonyMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollSapper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomPiranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RipperDemon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SpectralNecromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.TormentedSpirit;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public enum Bestiary {


	REGIONAL,
	BOSSES,
	UNIVERSAL,
	RARE,
	QUEST,
	NEUTRAL,
	ALLY; //the journal tracks kills with allies, or quest completions, or shop purchases (eventually?)

	//while this is all internally reported as kills, it's effectively 1 = seen for allies/NPCs
	private LinkedHashMap<Class<?>, Integer> killCount = new LinkedHashMap<>();

	//should only be used when initializing
	private void addMobs( Class<?>... mobs ){
		for (Class<?> mob : mobs){
			killCount.put(mob, 0);
		}
	}

	public Collection<Class<?>> mobs(){
		return killCount.keySet();
	}

	public String title(){
		return Messages.get(this, name() + ".title");
	}

	public int totalMobs(){
		return killCount.size();
	}

	public int totalSeen(){
		int seenTotal = 0;
		for (int count : killCount.values()){
			if (count > 0) seenTotal++;
		}
		return seenTotal;
	}

	static {

		REGIONAL.addMobs(Rat.class, Snake.class, Gnoll.class, Swarm.class, Crab.class, Slime.class,
				Skeleton.class, Thief.class, DM100.class, Guard.class, Necromancer.class,
				Bat.class, Brute.class, Shaman.RedShaman.class, Shaman.BlueShaman.class, Shaman.PurpleShaman.class, Spinner.class, DM200.class,
				Ghoul.class, Elemental.FireElemental.class, Elemental.FrostElemental.class, Elemental.ShockElemental.class, Warlock.class, Monk.class, Golem.class,
				RipperDemon.class, DemonSpawner.class, Succubus.class, Eye.class, Scorpio.class);

		BOSSES.addMobs(Goo.class,
				Tengu.class,
				Pylon.class, DM300.class,
				DwarfKing.class,
				YogDzewa.Larva.class, YogFist.BurningFist.class, YogFist.SoiledFist.class, YogFist.RottingFist.class, YogFist.RustedFist.class,YogFist.BrightFist.class, YogFist.DarkFist.class, YogDzewa.class);

		UNIVERSAL.addMobs(Wraith.class, Piranha.class, Mimic.class, GoldenMimic.class, Statue.class, GuardianTrap.Guardian.class, SentryRoom.Sentry.class);

		RARE.addMobs(Albino.class, CausticSlime.class,
				Bandit.class, SpectralNecromancer.class,
				ArmoredBrute.class, DM201.class,
				Elemental.ChaosElemental.class, Senior.class,
				Acidic.class,
				TormentedSpirit.class, PhantomPiranha.class, CrystalMimic.class, EbonyMimic.class, ArmoredStatue.class);

		QUEST.addMobs(FetidRat.class, GnollTrickster.class, GreatCrab.class,
				Elemental.NewbornFireElemental.class, RotLasher.class, RotHeart.class,
				CrystalWisp.class, CrystalGuardian.class, CrystalSpire.class, GnollGuard.class, GnollSapper.class, GnollGeomancer.class);

		NEUTRAL.addMobs(Ghost.class, Shopkeeper.class, Wandmaker.class, Blacksmith.class, Imp.class, Sheep.class, RatKing.class, Bee.class);

		ALLY.addMobs(MirrorImage.class, PrismaticImage.class,
				DriedRose.GhostHero.class,
				WandOfWarding.Ward.class, WandOfLivingEarth.EarthGuardian.class, WandOfRegrowth.Lotus.class,
				ShadowClone.ShadowAlly.class, SmokeBomb.NinjaLog.class, SpiritHawk.HawkAlly.class);

	}

	public static int killCount(Class<?> mobClass) {
		for (Bestiary cat : values()) {
			if (cat.killCount.containsKey(mobClass)) {
				return cat.killCount.get(mobClass);
			}
		}
		return 0;
	}

	public static boolean isSeen(Class<?> mobClass){
		return killCount(mobClass) > 0;
	}

	//some mobs have different internal classes when they are minions, so need to convert here
	private static final HashMap<Class<?>, Class<?>> minionConversions = new HashMap<>();
	static {
		minionConversions.put(CorpseDust.DustWraith.class,      Wraith.class);

		minionConversions.put(Necromancer.NecroSkeleton.class,  Skeleton.class);

		minionConversions.put(DwarfKing.DKGhoul.class,          Ghoul.class);
		minionConversions.put(DwarfKing.DKWarlock.class,        Warlock.class);
		minionConversions.put(DwarfKing.DKMonk.class,           Monk.class);
		minionConversions.put(DwarfKing.DKGolem.class,          Golem.class);

		minionConversions.put(YogDzewa.YogRipper.class,         RipperDemon.class);
		minionConversions.put(YogDzewa.YogEye.class,            Eye.class);
		minionConversions.put(YogDzewa.YogScorpio.class,        Scorpio.class);
	}

	public static boolean setSeen(Class<?> mobClass){
		if (minionConversions.containsKey(mobClass)){
			mobClass = minionConversions.get(mobClass);
		}
		for (Bestiary cat : values()) {
			if (cat.killCount.containsKey(mobClass)) {
				if (cat.killCount.get(mobClass) == 0){
					cat.killCount.put(mobClass, 1);
					Journal.saveNeeded = true;
				}
			}
		}
		return false;
	}

	public static void trackKill(Class<?> mobClass){
		if (minionConversions.containsKey(mobClass)){
			mobClass = minionConversions.get(mobClass);
		}
		for (Bestiary cat : values()) {
			if (cat.killCount.containsKey(mobClass)) {
				if (cat.killCount.get(mobClass) != Integer.MAX_VALUE){
					cat.killCount.put(mobClass, cat.killCount.get(mobClass)+1);
					Journal.saveNeeded = true;
				}
			}
		}
	}

	private static final String BESTIARY_CLASSES = "bestiary_classes";
	private static final String BESTIARY_KILLS = "bestiary_kills";

	public static void store( Bundle bundle ){
		Bundle bestiaryBundle = new Bundle();

		ArrayList<Class<?>> classes = new ArrayList<>();
		ArrayList<Integer> kills = new ArrayList<>();

		for (Bestiary cat : values()) {
			for (Class<?> mob : cat.mobs()) {
				if (cat.killCount.get(mob) > 0){
					classes.add(mob);
					kills.add(cat.killCount.get(mob));
				}
			}
		}

		int[] killsToStore = new int[kills.size()];
		for (int i = 0; i < killsToStore.length; i++){
			killsToStore[i] = kills.get(i);
		}

		bundle.put( BESTIARY_CLASSES, classes.toArray(new Class[0]) );
		bundle.put( BESTIARY_KILLS, killsToStore );

	}

	public static void restore( Bundle bundle ){

		if (bundle.contains(BESTIARY_CLASSES)){
			Class<?>[] classes = bundle.getClassArray(BESTIARY_CLASSES);
			int[] kills = bundle.getIntArray(BESTIARY_KILLS);

			for (int i = 0; i < classes.length; i++){
				for (Bestiary cat : values()){
					if (cat.killCount.containsKey(classes[i])){
						cat.killCount.put(classes[i], kills[i]);
					}
				}
			}
		}

	}

}
