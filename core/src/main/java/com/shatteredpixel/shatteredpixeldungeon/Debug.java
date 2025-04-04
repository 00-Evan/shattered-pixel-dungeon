package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfFeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FishingSpear;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class Debug {

    //SHOULD ALWAYS BE SET TO FALSE ON OFFICIAL RELEASES.
    public static final boolean DEBUG_MODE = DeviceCompat.isDebug();


    public static float Spawn_Multiplier = 1;
    public static float Respawn_Multiplier = 1;

    public static final int Starting_Floor = 1;
    public static final int Starting_HeroLevel = 1;
    public static final int Starting_Str = 10;
    public static final int Starting_HP = 20;

    private static final boolean ActOnStart = false;
    private static final boolean ActOnLoad = false;
    private static final ArrayList<Class<?extends Item>> Starting_Items;
    static {
        Starting_Items = new ArrayList<>(Arrays.asList(
                /*PotionOfHaste.class, PotionOfMindVision.class, PotionOfInvisibility.class, ElixirOfFeatherFall.class,
                ScrollOfMagicMapping.class, ScrollOfTeleportation.class, ScrollOfUpgrade.class,
                TimekeepersHourglass.class*/
        ));
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

        Hero.Polished.Debug_UpdateStats(Starting_HeroLevel);

        Starting_Bag();
    }
    public static void LoadGame() {
        if(!Debug.DEBUG_MODE || !Debug.ActOnLoad) return;

        Hero.Polished.Debug_UpdateStats(Starting_HeroLevel);
        Dungeon.hero.STR = Math.max(Dungeon.hero.STR, Starting_Str);

        Starting_Bag();
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
}
