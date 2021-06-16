package com.elementalpixel.elementalpixeldungeon.items;

import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.GamesInProgress;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Belongings;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroAction;
import com.elementalpixel.elementalpixeldungeon.items.armor.PlateArmor;
import com.elementalpixel.elementalpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.elementalpixel.elementalpixeldungeon.items.artifacts.HornOfPlenty;
import com.elementalpixel.elementalpixeldungeon.items.bags.MagicalHolster;
import com.elementalpixel.elementalpixeldungeon.items.bags.PotionBandolier;
import com.elementalpixel.elementalpixeldungeon.items.bags.ScrollHolder;
import com.elementalpixel.elementalpixeldungeon.items.bags.VelvetPouch;
import com.elementalpixel.elementalpixeldungeon.items.food.Food;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfExperience;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfFrost;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfHaste;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfHealing;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfInvisibility;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLevitation;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfMindVision;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfParalyticGas;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfPurity;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfStrength;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfToxicGas;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfRage;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfTerror;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.Glaive;
import com.elementalpixel.elementalpixeldungeon.levels.Level;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.plants.Blindweed;
import com.elementalpixel.elementalpixeldungeon.plants.Dreamfoil;
import com.elementalpixel.elementalpixeldungeon.plants.Earthroot;
import com.elementalpixel.elementalpixeldungeon.plants.Fadeleaf;
import com.elementalpixel.elementalpixeldungeon.plants.Firebloom;
import com.elementalpixel.elementalpixeldungeon.plants.Rotberry;
import com.elementalpixel.elementalpixeldungeon.plants.Sorrowmoss;
import com.elementalpixel.elementalpixeldungeon.plants.Starflower;
import com.elementalpixel.elementalpixeldungeon.plants.Stormvine;
import com.elementalpixel.elementalpixeldungeon.plants.Sungrass;
import com.elementalpixel.elementalpixeldungeon.plants.Swiftthistle;
import com.elementalpixel.elementalpixeldungeon.scenes.InterlevelScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.ui.Icons;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.ArrayList;


public class AddItems extends Item {
    String AC_ADD = "add";
    String AC_REMOVE = "remove";

    {
        image = ItemSpriteSheet.DEWDROP;

        unique = true;
        stackable = false;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_ADD);
        actions.add(AC_REMOVE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_ADD)) {

            //BAGS
            new VelvetPouch().collect();
            Dungeon.LimitedDrops.VELVET_POUCH.drop();

            new ScrollHolder().collect();
            Dungeon.LimitedDrops.SCROLL_HOLDER.drop();

            new MagicalHolster().collect();
            Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();

            new PotionBandolier().collect();
            Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

            //POTIONS
            new PotionOfExperience().quantity(29).identify().collect();
            new PotionOfLiquidFlame().quantity(10).identify().collect();
            new PotionOfToxicGas().quantity(10).identify().collect();
            new PotionOfParalyticGas().quantity(10).identify().collect();
            new PotionOfLevitation().quantity(10).identify().collect();
            new PotionOfStrength().quantity(20).identify().collect();
            new PotionOfHealing().quantity(20).identify().collect();
            new PotionOfFrost().quantity(15).identify().collect();
            new PotionOfHaste().quantity(15).identify().collect();
            new PotionOfInvisibility().quantity(15).identify().collect();
            new PotionOfLevitation().quantity(15).identify().collect();
            new PotionOfMindVision().quantity(15).identify().collect();
            new PotionOfPurity().quantity(15).identify().collect();



            //SCROLLS
            new ScrollOfIdentify().quantity(20).identify().collect();
            new ScrollOfMagicMapping().quantity(50).identify().collect();
            new ScrollOfUpgrade().quantity(50).identify().collect();
            new ScrollOfTeleportation().quantity(10).identify().collect();
            new ScrollOfLullaby().quantity(10).identify().collect();
            new ScrollOfMirrorImage().quantity(15).identify().collect();
            new ScrollOfRage().quantity(10).identify().collect();
            new ScrollOfRecharging().quantity(15).identify().collect();
            new ScrollOfRemoveCurse().quantity(10).identify().collect();
            new ScrollOfRetribution().quantity(15).identify().collect();
            new ScrollOfTerror().quantity(10).identify().collect();
            new ScrollOfTransmutation().quantity(15).identify().collect();

            //MISC
            new TomeOfMastery().collect();

            new Amulet().collect();
            new BrokenAmulet().collect();
            new teleport().collect();
            new ArmorKit().collect();

            new HornOfPlenty().identify().collect();
            new AlchemistsToolkit().identify().collect();

            new Glaive().upgrade(30).identify().collect();
            new PlateArmor().upgrade(30).identify().collect();

            new Food().quantity(15).collect();

            //SEEDS
            new Sungrass.Seed().quantity(20).collect();
            new Fadeleaf.Seed().quantity(20).collect();
            new Earthroot.Seed().quantity(20).collect();
            new Starflower.Seed().quantity(20).collect();
            new Rotberry.Seed().quantity(20).collect();
            new Blindweed.Seed().quantity(20).collect();
            new Rotberry.Seed().quantity(20).collect();
            new Dreamfoil.Seed().quantity(20).collect();
            new Firebloom.Seed().quantity(20).collect();
            new Sorrowmoss.Seed().quantity(20).collect();
            new Stormvine.Seed().quantity(20).collect();
            new Swiftthistle.Seed().quantity(20).collect();

            //FRAGMENTS
        } else if (action.equals(AC_REMOVE)) {
            curItem.detach(hero.belongings.backpack);
            GLog.h(Messages.get(AddItems.class, "remove"));
        }
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (super.doPickUp( hero )) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

}


