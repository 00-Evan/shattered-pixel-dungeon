package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SupplyRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("PointlessBooleanExpression")
public class Debug {

    //SHOULD ALWAYS BE SET TO FALSE ON OFFICIAL RELEASES.
    public static final boolean DEBUG_MODE = DeviceCompat.isDebug();


    private static final boolean DebuggingStats = false;
    //                                                                  Debug  /  Default
    public static final float Spawn_Multiplier = DebuggingStats ?       .6f     : 1f;
    public static final float Respawn_Multiplier = DebuggingStats ?     0f      : 1f;

    public static final int Starting_Floor = DebuggingStats ?           6       : 1;
    public static final int Starting_HeroLevel = DebuggingStats ?       18      : 1;
    public static final int Starting_Str = DebuggingStats ?             16      : 10;
    public static final int Starting_HP = DebuggingStats ?              2000    : 20;


    private static final boolean ActOnStart = false || DebuggingStats;
    private static final boolean ActOnLoad = false || DebuggingStats;
    private static final ArrayList<Class<?extends Item>> Starting_Items;
    static {
        //Testing items
        Starting_Items = new ArrayList<>(Arrays.asList(

        ));

        if(DebuggingStats || false) {
            Starting_Items.addAll(Arrays.asList(
                PotionOfMindVision.class, PotionOfHaste.class, PotionOfInvisibility.class, ElixirOfFeatherFall.class,
                ScrollOfMagicMapping.class, PhaseShift.class, ScrollOfUpgrade.class,
                StoneOfBlast.class, StoneOfBlink.class, StoneOfShock.class,
                TimekeepersHourglass.class, SupplyRation.class
            ));

            if(Dungeon.isChallenged(Challenges.DARKNESS))
                Starting_Items.add(Torch.class);
        }
    }
    public static void Starting_Bag() {
        if(!Debug.DEBUG_MODE || Starting_Items == null) return;

        for(Class<?extends Item> itemType : Debug.Starting_Items) {
            DebugCollect(itemType);
        }

        //DebugCollect(ClothArmor.class, 0, 1, Swiftness.class);

    }

    public static void StartGame() {
        if(!Debug.DEBUG_MODE || !Debug.ActOnStart) return;
        ClearWaterskin();

        Hero.Polished.Debug_UpdateStats(Starting_HeroLevel);
        Starting_Bag();


    }
    public static void LoadGame() {
        if(!Debug.DEBUG_MODE || !Debug.ActOnLoad) return;

        Hero.Polished.Debug_UpdateStats(Starting_HeroLevel);
        Dungeon.hero.STR = Math.max(Dungeon.hero.STR, Starting_Str);
        //Starting_Bag();

        //DebugCollect(ClothArmor.class, 0, 1, Swiftness.class);

    }


    public static void DebugCollect(Class<?extends Item> itemType) {
        DebugCollect(itemType, 0, 99, null);
    }
    public static void DebugCollect(Class<?extends Item> itemType, int level) {
        DebugCollect(itemType, level, 1, null);
    }
    public static void DebugCollect(Class<?extends Item> itemType, int level, int quantity) {
        DebugCollect(itemType, level, quantity, null);
    }
    public static<T> Item DebugCollect(Class<?extends Item> itemType, int level, int quantity, Class<T> enchant) {
        if(!DEBUG_MODE) return null;
        Item i = Reflection.newInstance(itemType);
        if(i == null) return null;

        i.quantity(i.stackable ? quantity : 1);
        i.identify();
        i.level(level);

        if(enchant != null) {
            if(Weapon.Enchantment.class.isAssignableFrom(enchant) && i instanceof Weapon) {
                ((Weapon) i).enchant( (Weapon.Enchantment) Reflection.newInstance(enchant) );
            }
            if(Armor.Glyph.class.isAssignableFrom(enchant) && i instanceof Armor) {
                ((Armor) i).inscribe( (Armor.Glyph) Reflection.newInstance(enchant) );
            }
        }

        if(i instanceof Wand) {
            ((Wand) i).gainCharge(level);
        }
        if(i instanceof Artifact) {
            ((Artifact) i).Polished_maxCharge();
        }

        i.collect();
        return i;
    }


    private static void ClearWaterskin() {
        if(DebuggingStats) {
            Waterskin waterskin = Dungeon.hero.belongings.getItem(Waterskin.class);
            if(waterskin != null) waterskin.detachAll(Dungeon.hero.belongings.backpack);
        }
    }
}
