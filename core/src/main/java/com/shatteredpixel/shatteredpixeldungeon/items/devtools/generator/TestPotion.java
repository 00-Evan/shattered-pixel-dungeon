package com.shatteredpixel.shatteredpixeldungeon.items.devtools.generator;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestGenerator;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BowFragment;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenHilt;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.BrokenStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.CloakScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.SealShard;
import com.shatteredpixel.shatteredpixeldungeon.items.remains.TornPage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDisarming;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFear;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.AdrenalineDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.BlindingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ChillingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.CleansingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DisplacingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HolyDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.IncendiaryDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.RotDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class TestPotion extends TestGenerator {
    {
        image = ItemSpriteSheet.POTION_HOLDER;
    }

    private int cateSelected = 0;
    private int item_quantity = 1;
    private int selected = 0;
    private boolean multiply = false;


    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_GIVE)) {
            GameScene.show(new SettingsWindow());
        }
    }

    private void createItem(){
        boolean collect = false;
        Item item = Reflection.newInstance(idToItem(selected));
        if(Challenges.isItemBlocked(item)) return;
        if (item != null) {
            if(item.stackable){
                int qu = item_quantity * (multiply?10:1);
                collect = item.quantity(qu).collect();
            }
            else collect = item.collect();
            item.identify();
            if(collect){
                GLog.i(Messages.get(this, "collect_success", item.name()));
            }else{
                item.doDrop(curUser);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("item_quantity", item_quantity);
        bundle.put("selected", selected);
        bundle.put("multiply", multiply);
        bundle.put("cate_selected", cateSelected);
    }

    @Override
    // 从Bundle中恢复数据
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        // 从Bundle中获取item_quantity的值
        item_quantity = bundle.getInt("item_quantity");
        // 从Bundle中获取selected的值
        selected = bundle.getInt("selected");
        // 从Bundle中获取multiply的值
        multiply = bundle.getBoolean("multiply");
        // 从Bundle中获取cateSelected的值
        cateSelected = bundle.getInt("cate_selected");
    }

    private Class<? extends Item> idToItem(int id){
        switch (cateSelected){
            case 0: default: return idToPotion(id);
            case 1: return idToExoticPotion(id);
            case 2: return idToSeed(id);
            case 3: return idToTippedDart(id);
            case 4: return idToScroll(id);
            case 5: return idToExoticScroll(id);
            case 6: return idToStone(id);
            case 7: return idToBomb(id);
            case 8: return idToSpecialPotion(id);
            case 9: return idToSpell(id);
            case 10: return idToFood(id);
            case 11: return idToMisc(id);
            case 12: return idToRemain(id);
        }
    }

    private int idToCategoryImage(int selected){
        switch (selected){
            case 0: return ItemSpriteSheet.POTION_AZURE;
            case 1: return ItemSpriteSheet.EXOTIC_AZURE;
            case 2: return ItemSpriteSheet.SEED_ICECAP;
            case 3: return ItemSpriteSheet.CHILLING_DART;
            case 4: return ItemSpriteSheet.SCROLL_LAGUZ;
            case 5: return ItemSpriteSheet.EXOTIC_LAGUZ;
            case 6: return ItemSpriteSheet.STONE_AUGMENTATION;
            case 7: return ItemSpriteSheet.BOMB;
            case 8: return ItemSpriteSheet.BREW_CAUSTIC;
            case 9: return ItemSpriteSheet.PHASE_SHIFT;
            case 10: return ItemSpriteSheet.RATION;
            case 11: default: return ItemSpriteSheet.CHEST;
            case 12: return ItemSpriteSheet.SEAL_SHARD;
            case 13: return ItemSpriteSheet.BROKEN_STAFF;
        }
    }

    private Class<? extends Potion> idToPotion(int id) {
        switch (id) {
            case 0:
                return PotionOfExperience.class;
            case 1:
                return PotionOfFrost.class;
            case 2:
                return PotionOfHaste.class;
            case 3:
                return PotionOfHealing.class;
            case 4:
                return PotionOfInvisibility.class;
            case 5:
                return PotionOfLevitation.class;
            case 6:
                return PotionOfLiquidFlame.class;
            case 7:
                return PotionOfMindVision.class;
            case 8:
                return PotionOfParalyticGas.class;
            case 9:
                return PotionOfPurity.class;
            case 10:
                return PotionOfStrength.class;
            case 11:
            default:
                return PotionOfToxicGas.class;
        }
    }

    private Class<? extends ExoticPotion> idToExoticPotion(int id) {
        return ExoticPotion.regToExo.get((idToPotion(id)));
    }

    private Class<? extends Plant.Seed> idToSeed(int id) {
        switch (id) {
            case 0:
                return Starflower.Seed.class;
            case 1:
                return Icecap.Seed.class;
            case 2:
                return Swiftthistle.Seed.class;
            case 3:
                return Sungrass.Seed.class;
            case 4:
                return Blindweed.Seed.class;
            case 5:
                return Stormvine.Seed.class;
            case 6:
                return Firebloom.Seed.class;
            case 7:
                return Fadeleaf.Seed.class;
            case 8:
                return Earthroot.Seed.class;
            case 9:
                return Mageroyal.Seed.class;
            case 10:
                return Rotberry.Seed.class;
            case 11:
            default:
                return Sorrowmoss.Seed.class;
        }
    }

    private Class<? extends TippedDart> idToTippedDart(int id){
        switch (id){
            case 0: return HolyDart.class;
            case 1: return ChillingDart.class;
            case 2: return AdrenalineDart.class;
            case 3: return HealingDart.class;
            case 4: return BlindingDart.class;
            case 5: return ShockingDart.class;
            case 6: return IncendiaryDart.class;
            case 7: return DisplacingDart.class;
            case 8: return ParalyticDart.class;
            case 9: return CleansingDart.class;
            case 10: return RotDart.class;
            case 11: default: return PoisonDart.class;
        }
    }

    private Class<? extends Scroll> idToScroll(int id) {
        switch (id) {
            case 0:
                return ScrollOfIdentify.class;
            case 1:
                return ScrollOfLullaby.class;
            case 2:
                return ScrollOfMagicMapping.class;
            case 3:
                return ScrollOfMirrorImage.class;
            case 4:
                return ScrollOfRage.class;
            case 5:
                return ScrollOfRecharging.class;
            case 6:
                return ScrollOfRetribution.class;
            case 7:
                return ScrollOfRemoveCurse.class;
            case 8:
                return ScrollOfTeleportation.class;
            case 9:
                return ScrollOfTerror.class;
            case 10:
                return ScrollOfTransmutation.class;
            case 11:
            default:
                return ScrollOfUpgrade.class;
        }
    }

    private Class<? extends ExoticScroll> idToExoticScroll(int id) {
        return ExoticScroll.regToExo.get(idToScroll(id));
    }

    private Class<? extends Runestone> idToStone(int id) {
        switch (id) {
            case 0:
                return StoneOfIntuition.class;
            case 1:
                return StoneOfFear.class;
            case 2:
                return StoneOfClairvoyance.class;
            case 3:
                return StoneOfFlock.class;
            case 4:
                return StoneOfAggression.class;
            case 5:
                return StoneOfShock.class;
            case 6:
                return StoneOfBlast.class;
            case 7:
                return StoneOfDisarming.class;
            case 8:
                return StoneOfBlink.class;
            case 9:
                return StoneOfDeepSleep.class;
            case 10:
                return StoneOfAugmentation.class;
            case 11:
            default:
                return StoneOfEnchantment.class;
        }
    }

    private Class<? extends Item> idToBomb(int id){
        switch (id){
            case 0: return Bomb.class;
            case 1: return ArcaneBomb.class;
            case 2: return Firebomb.class;
            case 4: return FrostBomb.class;
            case 5: return HolyBomb.class;
            case 6: return Noisemaker.class;
            case 7: return RegrowthBomb.class;
            case 9: return ShrapnelBomb.class;
            case 10: default: return WoollyBomb.class;
        }
    }

    private Class<? extends Potion> idToSpecialPotion(int id){
        switch (id){
            case 0: return BlizzardBrew.class;
            case 1: return CausticBrew.class;
            case 2: return InfernalBrew.class;
            case 3: return ShockingBrew.class;
            case 4: return ElixirOfAquaticRejuvenation.class;
            case 5: return ElixirOfArcaneArmor.class;
            case 6: return ElixirOfDragonsBlood.class;
            case 7: return ElixirOfHoneyedHealing.class;
            case 8: return ElixirOfIcyTouch.class;
            case 9: return ElixirOfMight.class;
            case 10: default:return ElixirOfToxicEssence.class;
        }
    }

    private Class<? extends Spell> idToSpell(int id){
        switch (id){
            case 0: return Alchemize.class;
            case 2: return BeaconOfReturning.class;
            case 3: return CurseInfusion.class;
            case 5: return MagicalInfusion.class;
            case 6: return TelekineticGrab.class;
            case 7: return PhaseShift.class;
            case 8: return ReclaimTrap.class;
            case 9: return Recycle.class;
            case 10: return WildEnergy.class;
            case 11: default:return SummonElemental.class;
        }
    }

    private Class<? extends Food> idToFood(int id){
        switch (id){
            case 0: return Food.class;
            case 1: return SmallRation.class;
            case 2: return Pasty.class;
            case 3: return Blandfruit.class;
            case 4: return MysteryMeat.class;
            case 5: return StewedMeat.class;
            case 6: return FrozenCarpaccio.class;
            case 7: return ChargrilledMeat.class;
            case 8: return Berry.class;
            default:
            case 9: return PhantomMeat.class;

        }
    }

    private Class<? extends Item> idToMisc(int id){
        switch (id){
            case 0: return Torch.class;
            case 1: return GooBlob.class;
            case 2: return MetalShard.class;
            case 3: return Honeypot.class;
            case 4: return Ankh.class;
            case 5: return Waterskin.class;
            case 6: return Stylus.class;
            case 7: default: return KingsCrown.class;
            case 8: return TengusMask.class;
            case 9: return LiquidMetal.class;
            case 10: return ArcaneResin.class;
            case 11: return Embers.class;
            case 12: return CorpseDust.class;
            case 13: return DarkGold.class;
        }
    }

    private Class<? extends Item> idToRemain(int id){
        switch (id){
            case 0: default:return SealShard.class;
            case 1: return BrokenStaff.class;
            case 2: return CloakScrap.class;
            case 3: return BowFragment.class;
            case 4: return BrokenHilt.class;
            case 5: return TornPage.class;

        }
    }

    private int maxIndex(int cate){
        if(cate == 7) return 10;
        if(cate == 9) return 12;
        if(cate == 10) return 9;
        if(cate == 11) return 13;
        if(cate == 12) return 5;
        return 11;
    }

    private int maxCategory(){
        return 13;
    }

    private static ArrayList<Class<? extends Potion>> potionList = new ArrayList<>();
    private static ArrayList<Class<? extends ExoticPotion>> exoticPotionList = new ArrayList<>();
    private static ArrayList<Class<? extends Plant.Seed>> seedList = new ArrayList<>();
    private static ArrayList<Class<? extends Scroll>> scrollList = new ArrayList<>();
    private static ArrayList<Class<? extends ExoticScroll>> exoticScrollList = new ArrayList<>();
    private static ArrayList<Class<? extends Runestone>> stoneList = new ArrayList<>();
    private static ArrayList<Class<? extends TippedDart>> dartList = new ArrayList<>();
    private static ArrayList<Class<? extends Item>> bombList = new ArrayList<>();
    private static ArrayList<Class<? extends Potion>> brewList = new ArrayList<>();
    private static ArrayList<Class<? extends Spell>> spellList = new ArrayList<>();
    private static ArrayList<Class<? extends Food>> foodList = new ArrayList<>();
    private static ArrayList<Class<? extends Item>> miscList = new ArrayList<>();
    private static ArrayList<Class<? extends Item>> remainList = new ArrayList<>();
    private void buildList() {
        if (potionList.isEmpty()) {
            for (int i = 0; i < maxIndex(0)+1; ++i) {
                potionList.add(idToPotion(i));
            }
        }
        if (exoticPotionList.isEmpty()) {
            for (int i = 0; i < maxIndex(1)+1; ++i) {
                exoticPotionList.add(idToExoticPotion(i));
            }
        }

        if (seedList.isEmpty()) {
            for (int i = 0; i < maxIndex(2)+1; ++i) {
                seedList.add(idToSeed(i));
            }
        }

        if(dartList.isEmpty()){
            for(int i=0; i<maxIndex(3)+1; ++i){
                dartList.add(idToTippedDart(i));
            }
        }

        if (scrollList.isEmpty()) {
            for (int i = 0; i < maxIndex(4)+1; ++i) {
                scrollList.add(idToScroll(i));
            }
        }

        if (exoticScrollList.isEmpty()) {
            for (int i = 0; i < maxIndex(5)+1; ++i) {
                exoticScrollList.add(idToExoticScroll(i));
            }
        }

        if (stoneList.isEmpty()) {
            for (int i = 0; i < maxIndex(6)+1; ++i) {
                stoneList.add(idToStone(i));
            }
        }

        if(bombList.isEmpty()){
            for(int i=0; i<maxIndex(7)+1; ++i){
                bombList.add(idToBomb(i));
            }
        }

        if(brewList.isEmpty()){
            for(int i=0; i<maxIndex(8)+1; ++i){
                brewList.add(idToSpecialPotion(i));
            }
        }

        if(spellList.isEmpty()){
            for(int i=0; i<maxIndex(9)+1; ++i){
                spellList.add(idToSpell(i));
            }
        }

        if(foodList.isEmpty()){
            for(int i=0; i<maxIndex(10)+1; ++i){
                foodList.add(idToFood(i));
            }
        }

        if(miscList.isEmpty()){
            for(int i=0; i<maxIndex(11)+1; ++i){
                miscList.add(idToMisc(i));
            }
        }

        if(remainList.isEmpty()){
            for(int i=0; i<maxIndex(12)+1; ++i){
                remainList.add(idToRemain(i));
            }
        }
    }

    private class SettingsWindow extends Window {
        private OptionSlider o_quantity;
        private RenderedTextBlock t_select;
        private CheckBox c_multiply;
        private RedButton b_create;
        private ArrayList<IconButton> buttonList = new ArrayList<>();
        private ArrayList<IconButton> cateButtonList = new ArrayList<>();
        private static final int WIDTH = 120;
        private static final int BTN_SIZE = 16;
        private static final int GAP = 2;
        private static final int TITLE_BTM = 8;

        public SettingsWindow() {
            buildList();

            createCategoryImage();

            createImage();

            t_select = PixelScene.renderTextBlock("", 8);
            t_select.text();
            add(t_select);

            o_quantity = new OptionSlider(Messages.get(this, "quantity"), "1", "10", 1, 10) {
                @Override
                protected void onChange() {
                    item_quantity = getSelectedValue();
                }
            };
            o_quantity.setSelectedValue(item_quantity);
            o_quantity.setRect(0, t_select.bottom() + 2 * GAP, WIDTH, 24);
            add(o_quantity);

            c_multiply = new CheckBox(Messages.get(this, "multiply")){
                @Override
                protected void onClick() {
                    super.onClick();
                    multiply = checked();
                }
            };
            c_multiply.checked(multiply);
            c_multiply.setRect(0, o_quantity.bottom() + GAP, WIDTH, 18);
            add(c_multiply);

            b_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createItem();
                    updateText();
                }
            };
            add(b_create);

            updateText();
        }

        private void layout() {
            t_select.setPos(0, TITLE_BTM +7*GAP + 5*BTN_SIZE + 6);
            o_quantity.setRect(0, t_select.bottom() + 2 * GAP, WIDTH, 24);
            c_multiply.setRect(0, o_quantity.bottom() + GAP, WIDTH/2f - GAP/2f, 16);
            b_create.setRect(WIDTH/2f + GAP/2f, o_quantity.bottom() + GAP, WIDTH/2f - GAP/2f, 16);
            resize(WIDTH, (int) b_create.bottom());
        }

        private void createCategoryImage(){
            float left;
            float top = GAP + TITLE_BTM;
            int placed = 0;
            int length = 13;
            int firstRow = 6;
            int secondRow = 12;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        cateButtonList.get(cateSelected).icon().resetColor();
                        cateSelected = Math.min(j, maxCategory());
                        if(selected > maxIndex(cateSelected)) selected = maxIndex(cateSelected);
                        cateButtonList.get(cateSelected).icon().color(0xFFFF44);
                        updateImage();
                        updateText();

                        super.onClick();
                    }
                };
                Image im =  new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(idToCategoryImage(i)));
                im.scale.set(1.0f);
                btn.icon(im);

                if (i < firstRow) {
                    left = (WIDTH - BTN_SIZE * firstRow) / 2f;
                    btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
                } else if (i < secondRow){
                    left = (WIDTH - BTN_SIZE * firstRow) / 2f;
                    btn.setRect(left + (placed - firstRow) * BTN_SIZE, top + GAP + BTN_SIZE, BTN_SIZE, BTN_SIZE);
                } else {
                    left = (WIDTH - BTN_SIZE * (length - secondRow)) / 2f;
                    btn.setRect(left + (placed - secondRow) * BTN_SIZE, top + GAP * 2 + BTN_SIZE * 2, BTN_SIZE, BTN_SIZE);
                }
                add(btn);
                cateButtonList.add(btn);
                placed++;
            }
        }

        private void createImage() {
            float left;
            float top = TITLE_BTM + 5*GAP + 3*BTN_SIZE + 3;
            int placed = 0;
            int length = maxIndex(cateSelected)+1;
            int firstRow = (length % 2 == 0 ? length / 2 : (length / 2 + 1));
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        selected = Math.min(j, maxIndex(cateSelected));
                        updateText();
                        super.onClick();
                    }
                };
                switch (cateSelected){
                    case 0 :{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(potionList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 1:{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(exoticPotionList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 2:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(seedList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 3:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(dartList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 4:{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(scrollList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    }break;
                    case 5:{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(exoticScrollList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 6:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(stoneList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 7:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(bombList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 8:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(brewList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 9: {
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(spellList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 10: {
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(foodList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 11: default:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(miscList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 12: {
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(remainList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    }
                }

                if (i < firstRow) {
                    left = (WIDTH - BTN_SIZE * firstRow) / 2f;
                    btn.setRect(left + placed * BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
                } else {
                    left = (WIDTH - BTN_SIZE * (length - firstRow)) / 2f;
                    btn.setRect(left + (placed - firstRow) * BTN_SIZE, top + GAP + BTN_SIZE, BTN_SIZE, BTN_SIZE);
                }
                add(btn);
                placed++;
                buttonList.add(btn);
            }
        }

        private void clearImage() {
            for (IconButton button : buttonList.toArray(new IconButton[0])) {
                button.destroy();
            }
        }

        private void updateImage() {
            clearImage();
            createImage();
        }

        private void updateText() {
            Class<? extends Item> item = idToItem(selected);
            String s = Messages.get(item, "name");
            if (item == Pasty.class) {
                s = "馅饼";
            }
            t_select.text(Messages.get(TestPotion.class, "select", s));
            layout();
        }
    }
}
