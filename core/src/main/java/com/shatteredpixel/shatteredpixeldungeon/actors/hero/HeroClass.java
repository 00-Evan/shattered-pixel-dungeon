/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.potionyst.Scarecrow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.CorgSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Mjornil;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.WhitePhial;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SuperMushroom;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews.PotionOfDisguise;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews.PotionOfGoldenLiquid;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews.PotionOfGoldlizing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PocketedMobs;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.GreatSaw;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rebellion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shovel;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SurgicalSaw;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WoodSaw;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER , HeroSubClass.LAPIDARIST),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN , HeroSubClass.SOULCHASER ),
	DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK ),
	POTIONYST( HeroSubClass.DR_PLAGUE, HeroSubClass.ALCHEMYST );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case DUELIST:
				initDuelist( hero );
				break;

			case POTIONYST:
				initPotionyst( hero );
				break;
		}

		if (SPDSettings.quickslotWaterskin()) {
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, waterskin);
					break;
				}
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case DUELIST:
				return Badges.Badge.MASTERY_DUELIST;
			case POTIONYST:
				return Badges.Badge.MASTERY_POTIONYST;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();

		(hero.belongings.artifact = new Mjornil()).identify();
		EtherealChains chain = new EtherealChains();
		chain.quantity(1).collect();
		SuperMushroom em = new SuperMushroom();
		em.quantity(4).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();

		StoneOfFlock erer = new StoneOfFlock();
		erer.quantity(100).collect();
		PocketedMobs wewee = new PocketedMobs();
		wewee.quantity(4).collect();
		SummonElemental eeees = new SummonElemental();
		eeees.quantity(4).collect();
		Shovel wx = new Shovel();
		wx.quantity(1).collect();
		wx.quantity(1).identify();
		Bomb rwe = new Bomb();
		rwe.quantity(5).collect();

	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();

		(hero.belongings.artifact = new Mjornil()).identify();
		TengusMask sd = new TengusMask();
		sd.quantity(1).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();
		PotionOfLevitation assd = new PotionOfLevitation();
		assd.quantity(100).collect();
		PotionOfFrost ewew = new PotionOfFrost();
		ewew.quantity(100).collect();
		PotionOfHealing wws = new PotionOfHealing();
		wws.quantity(100).collect();


		ScrollOfIdentify ssss = new ScrollOfIdentify();
		ssss.quantity(100).collect();
		ScrollOfMagicMapping sss = new ScrollOfMagicMapping();
		sss.quantity(100).collect();
		ScrollOfForesight sess = new ScrollOfForesight();
		sess.quantity(100).collect();
		WandOfLivingEarth eri = new WandOfLivingEarth();
		eri.quantity(1).collect();
		PotionOfGoldlizing sdsd = new PotionOfGoldlizing();
		sdsd.quantity(100).collect();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();

		Mjornil ham = new Mjornil();
		ham.quantity(1).collect();
		Shovel wx = new Shovel();
		wx.quantity(1).collect();
		wx.quantity(1).identify();
		TengusMask sd = new TengusMask();
		sd.quantity(1).collect();
		StoneOfEnchantment rrrr = new StoneOfEnchantment();
		rrrr.quantity(100).collect();
		ScrollOfIdentify qwe = new ScrollOfIdentify();
		qwe.quantity(100).collect();
		//
		RingOfMight qqw = new RingOfMight();
		qqw.quantity(1).collect();
		qqw.level(5);
		ScrollOfUpgrade wwa = new ScrollOfUpgrade();
		wwa.quantity(100).collect();
		PotionOfInvisibility ssss = new PotionOfInvisibility();
		ssss.quantity(100).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();
		PotionOfLevitation wwew = new PotionOfLevitation();
		wwew.quantity(100).collect();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();

		(hero.belongings.artifact = new Mjornil()).identify();
     	TengusMask sd = new TengusMask();
		sd.quantity(1).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();
		PotionOfStrength aaaa = new PotionOfStrength();
		aaaa.quantity(100).collect();
		PotionOfInvisibility ssss = new PotionOfInvisibility();
		ssss.quantity(100).collect();
		StoneOfAugmentation rrrr = new StoneOfAugmentation();
		rrrr.quantity(100).collect();
		CorgSeed ss = new CorgSeed();
		ss.quantity(100).collect();
		Rebellion reee = new Rebellion();
		reee.quantity(100).collect();
		reee.quantity(100).identify();
		ScrollOfMagicMapping qwe = new ScrollOfMagicMapping();
		qwe.quantity(100).collect();
		KingsCrown wwdf = new KingsCrown();
		wwdf.quantity(1).collect();
	}

	private static void initDuelist( Hero hero ) {

		(hero.belongings.weapon = new Rapier()).identify();
		hero.belongings.weapon.activate(hero);

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();

		(hero.belongings.artifact = new Mjornil()).identify();
		TengusMask sd = new TengusMask();
		sd.quantity(1).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();
		PotionOfStrength aaaa = new PotionOfStrength();
		aaaa.quantity(100).collect();
		PotionOfInvisibility ssss = new PotionOfInvisibility();
		ssss.quantity(100).collect();
		StoneOfEnchantment rrrr = new StoneOfEnchantment();
		rrrr.quantity(100).collect();
		ScrollOfUpgrade wwa = new ScrollOfUpgrade();
		wwa.quantity(100).collect();
		Rebellion reee = new Rebellion();
		reee.quantity(1).collect();
		reee.quantity(1).identify();
		WoodSaw we = new WoodSaw();
		we.quantity(1).collect();
		we.quantity(1).identify();
		GreatSaw w = new GreatSaw();
		w.quantity(1).collect();
		w.quantity(1).identify();
		Shovel wx = new Shovel();
		wx.quantity(1).collect();
		wx.quantity(1).identify();
	}
	private static void initPotionyst( Hero hero ) {
		WhitePhial phial;

		phial = new WhitePhial(new PotionOfToxicGas());
		phial.identify().collect();

		(hero.belongings.weapon = new SurgicalSaw()).identify();
		hero.belongings.weapon.activate(hero);
		Dungeon.quickslot.setSlot(0, phial);

		new ScrollOfTransmutation().identify();
		new PotionOfToxicGas().identify();

		TengusMask sd = new TengusMask();
		sd.quantity(1).collect();
		PotionOfExperience asd = new PotionOfExperience();
		asd.quantity(100).collect();
		PotionOfStrength aaaa = new PotionOfStrength();
		aaaa.quantity(100).collect();
		PotionOfInvisibility ssss = new PotionOfInvisibility();
		ssss.quantity(100).collect();
		StoneOfEnchantment rrrr = new StoneOfEnchantment();
		rrrr.quantity(100).collect();
		ScrollOfUpgrade wwa = new ScrollOfUpgrade();
		wwa.quantity(100).collect();

		PotionOfLevitation eewe = new PotionOfLevitation();
		eewe.quantity(100).collect();

		KingsCrown wwdf = new KingsCrown();
		wwdf.quantity(1).collect();
		PotionOfGoldenLiquid rrer = new PotionOfGoldenLiquid();
		rrer.quantity(100).identify().collect();
		PotionOfMindVision rere = new PotionOfMindVision();
		rere.quantity(100).identify().collect();
		AlchemistsToolkit wweee = new AlchemistsToolkit();
		wweee.quantity(1).identify().collect();
		LiquidMetal ert = new LiquidMetal();
		ert.quantity(100).identify().collect();

		ThrowingSpike ssd = new ThrowingSpike();
		ssd.quantity(2).collect();
		PotionOfDisguise fgfg = new PotionOfDisguise();
		fgfg.quantity(100).collect();
		PotionOfHealing hhg = new PotionOfHealing();
		hhg.quantity(100).identify().collect();
		DriedRose std = new DriedRose();
		std.quantity(1).collect();
	}

		public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case DUELIST:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
			case POTIONYST:
				return new ArmorAbility[]{new Scarecrow(), new ElementalStrike(), new Feint()};
				// Scarecrow
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case DUELIST:
				return Assets.Sprites.DUELIST;
			case POTIONYST:
				return Assets.Sprites.POTIONYST;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case DUELIST:
				return Assets.Splashes.DUELIST;
			case POTIONYST:
				return Assets.Splashes.POTIONYST;
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;

		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case DUELIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
			case POTIONYST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_POTIONYST);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
