/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.right.helveticpixeldungeon.items.test.TestTool;
import com.shatteredpixel.shatteredpixeldungeon.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.*;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.*;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.*;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

    WARRIOR(HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR),
    MAGE(HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK),
    ROGUE(HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER),
    HUNTRESS(HeroSubClass.SNIPER, HeroSubClass.WARDEN),
    //DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK ),
    CLERIC(HeroSubClass.PRIEST, HeroSubClass.PALADIN);

    private final HeroSubClass[] subClasses;

    HeroClass(HeroSubClass... subClasses) {
        this.subClasses = subClasses;
    }

    private static void initWarrior(Hero hero) {
        (hero.belongings.weapon = new WornShortsword()).identify();
        ThrowingStone stones = new ThrowingStone();
        stones.quantity(3).collect();
        Dungeon.quickslot.setSlot(0, stones);

        if (hero.belongings.armor != null) {
            hero.belongings.armor.affixSeal(new BrokenSeal());
            Catalog.setSeen(BrokenSeal.class); //as it's not added to the inventory
        }

        new PotionOfHealing().identify();
        new ScrollOfRage().identify();
    }

    private static void initMage(Hero hero) {
        MagesStaff staff;

        staff = new MagesStaff(new WandOfMagicMissile());

        (hero.belongings.weapon = staff).identify();
        hero.belongings.weapon.activate(hero);

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollOfUpgrade().identify();
        new PotionOfLiquidFlame().identify();
    }

    private static void initRogue(Hero hero) {
        (hero.belongings.weapon = new Dagger()).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        (hero.belongings.artifact = cloak).identify();
        hero.belongings.artifact.activate(hero);

        ThrowingKnife knives = new ThrowingKnife();
        knives.quantity(3).collect();

        Dungeon.quickslot.setSlot(0, cloak);
        Dungeon.quickslot.setSlot(1, knives);

        new ScrollOfMagicMapping().identify();
        new PotionOfInvisibility().identify();
    }

    private static void initHuntress(Hero hero) {

        (hero.belongings.weapon = new Gloves()).identify();
        SpiritBow bow = new SpiritBow();
        bow.identify().collect();

        Dungeon.quickslot.setSlot(0, bow);

        new PotionOfMindVision().identify();
        new ScrollOfLullaby().identify();
    }

//    private static void initDuelist(Hero hero) {
//
//        (hero.belongings.weapon = new Rapier()).identify();
//        hero.belongings.weapon.activate(hero);
//
//        ThrowingSpike spikes = new ThrowingSpike();
//        spikes.quantity(2).collect();
//
//        Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
//        Dungeon.quickslot.setSlot(1, spikes);
//
//        new PotionOfStrength().identify();
//        new ScrollOfMirrorImage().identify();
//    }

    private static void initCleric(Hero hero) {

        (hero.belongings.weapon = new Cudgel()).identify();
        hero.belongings.weapon.activate(hero);

        HolyTome tome = new HolyTome();
        (hero.belongings.artifact = tome).identify();
        hero.belongings.artifact.activate(hero);

        Dungeon.quickslot.setSlot(0, tome);

        new PotionOfPurity().identify();
        new ScrollOfRemoveCurse().identify();
    }

    public void initHero(Hero hero) {

        hero.heroClass = this;
        Talent.initClassTalents(hero);

        Item i = new ClothArmor().identify();
        if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor) i;

        i = new Food();
        if (!Challenges.isItemBlocked(i)) i.collect();

        new VelvetPouch().collect();
        Dungeon.LimitedDrops.VELVET_POUCH.drop();

        Waterskin waterskin = new Waterskin();
        waterskin.collect();

        new ScrollOfIdentify().identify();
        if (Dungeon.isChallenged(Challenges.INDEV_MODE)) {
            TestTool.collectTools();
        }

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;

            case CLERIC:
                initCleric(hero);
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
        return switch (this) {
            case WARRIOR -> Badges.Badge.MASTERY_WARRIOR;
            case MAGE -> Badges.Badge.MASTERY_MAGE;
            case ROGUE -> Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS -> Badges.Badge.MASTERY_HUNTRESS;
            //case DUELIST -> Badges.Badge.MASTERY_DUELIST;
            case CLERIC -> Badges.Badge.MASTERY_CLERIC;
        };
    }

    public String title() {
        return Messages.get(HeroClass.class, name());
    }

    public String desc() {
        return Messages.get(HeroClass.class, name() + "_desc");
    }

    public String shortDesc() {
        return Messages.get(HeroClass.class, name() + "_desc_short");
    }

    public HeroSubClass[] subClasses() {
        return subClasses;
    }

    public ArmorAbility[] armorAbilities() {
        return switch (this) {
            case WARRIOR -> new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
            case MAGE -> new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
            case ROGUE -> new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
            case HUNTRESS -> new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
            //case DUELIST -> new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
            case CLERIC -> new ArmorAbility[]{new AscendedForm(), new Trinity(), new PowerOfMany()};
        };
    }

    public String spritesheet() {
        return switch (this) {
            case WARRIOR -> Assets.Sprites.WARRIOR;
            case MAGE -> Assets.Sprites.MAGE;
            case ROGUE -> Assets.Sprites.ROGUE;
            case HUNTRESS -> Assets.Sprites.HUNTRESS;
            case CLERIC -> Assets.Sprites.CLERIC;
        };
    }

    public String splashArt() {
        return switch (this) {
            case WARRIOR -> Assets.Splashes.WARRIOR;
            case MAGE -> Assets.Splashes.MAGE;
            case ROGUE -> Assets.Splashes.ROGUE;
            case HUNTRESS -> Assets.Splashes.HUNTRESS;
            case CLERIC -> Assets.Splashes.CLERIC;
        };
    }

    public boolean isUnlocked() {
        //always unlock on debug builds
        if (DeviceCompat.isDebug()) return true;

        return switch (this) {
            case WARRIOR -> true;
            case MAGE -> Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
            case ROGUE -> Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
            case HUNTRESS -> Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
            case CLERIC -> Badges.isUnlocked(Badges.Badge.UNLOCK_CLERIC);
        };
    }

    public String unlockMsg() {
        return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name() + "_unlock");
    }

}
