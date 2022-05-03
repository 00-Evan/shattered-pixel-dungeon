//
// Decompiled by Jadx - 756ms
//
package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nxhy;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MerchantsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NxhyShopRoom extends SpecialRoom {
    private ArrayList<Item> itemsToSpawn;

    public NxhyShopRoom() {
    }

    public int minWidth() {
        return Math.max(8, (int) (Math.sqrt((double) itemCount()) + 3.0d));
    }

    public int minHeight() {
        return Math.max(8, (int) (Math.sqrt((double) itemCount()) + 3.0d));
    }

    public int itemCount() {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        return this.itemsToSpawn.size();
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        placeShopkeeper(level);
        placeItems(level);
        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
    }

    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());
        Mob nxhy = new Nxhy();
        nxhy.pos = pos;
        level.mobs.add(nxhy);
    }

    protected void placeItems(Level level) {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == this.top) {
            itemPlacement.y++;
        } else if (itemPlacement.y == this.bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == this.left) {
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }
        Iterator<Item> it = this.itemsToSpawn.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (itemPlacement.x == this.left + 1 && itemPlacement.y != this.top + 1) {
                itemPlacement.y--;
            } else if (itemPlacement.y == this.top + 1 && itemPlacement.x != this.right - 1) {
                itemPlacement.x++;
            } else if (itemPlacement.x != this.right - 1 || itemPlacement.y == this.bottom - 1) {
                itemPlacement.x--;
            } else {
                itemPlacement.y++;
            }
            int cell = level.pointToCell(itemPlacement);
            if (level.heaps.get(cell) != null) {
                while (true) {
                    cell = level.pointToCell(random());
                    if (level.heaps.get(cell) == null && level.findMob(cell) == null) {
                        break;
                    }
                }
            }
            level.drop( item, cell ).type = Heap.Type.FOR_SALE;
        }
    }

    protected static ArrayList<Item> generateItems() {
        Item rare;
        Item item;
        ArrayList<Item> itemsToSpawn2 = new ArrayList<>();
        int i = Dungeon.depth;
        Item w = Generator.random(Generator.wepTiers[4]);
        //itemsToSpawn2.add(Generator.random(Generator.misTiers[4]).quantity(2).identify());
        itemsToSpawn2.add(new LeatherArmor().identify());
        w.cursed = true;
        w.level(0);
        w.identify();
        itemsToSpawn2.add(w);
        itemsToSpawn2.add(TippedDart.randomTipped(2));
        itemsToSpawn2.add(new MerchantsBeacon());
        itemsToSpawn2.add(ChooseBag(Dungeon.hero.belongings));
        itemsToSpawn2.add(new PotionOfHealing());
        itemsToSpawn2.add(new ScrollOfTransmutation());
        //itemsToSpawn2.add(new DriedRose());
        itemsToSpawn2.add(Generator.randomUsingDefaults(Generator.Category.POTION));
        itemsToSpawn2.add(Generator.randomUsingDefaults(Generator.Category.WAND));
        itemsToSpawn2.add(Generator.randomUsingDefaults(Generator.Category.WAND));
        itemsToSpawn2.add(new ScrollOfIdentify());
        itemsToSpawn2.add(new ScrollOfRemoveCurse());
            switch (Random.Int(5)) {
                case 0:
                case 1:
                case 2:
                default:
                    itemsToSpawn2.add(new Food());
                    break;
                case 3:
                case 4:
                    itemsToSpawn2.add(new YellowSunBooks());
                    break;
                case 5:
                    itemsToSpawn2.add(new MeatPie());
                    break;
            }
        itemsToSpawn2.add(new ScrollOfMagicMapping());
        for (int i2 = 0; i2 < 2; i2++) {
            if (Random.Int(2) == 0) {
                item = Generator.randomUsingDefaults(Generator.Category.POTION);
            } else {
                item = Generator.randomUsingDefaults(Generator.Category.SCROLL);
            }
            itemsToSpawn2.add(item);
        }
        itemsToSpawn2.add(new SmallRation());
        itemsToSpawn2.add(new SmallRation());
        int Int = Random.Int(4);
        if (Int == 0) {
            itemsToSpawn2.add(new Bomb());
        } else if (Int == 1 || Int == 2) {
            itemsToSpawn2.add(new Bomb.DoubleBomb());
        } else if (Int == 3) {
            itemsToSpawn2.add(new Honeypot());
        }
        itemsToSpawn2.add(new Ankh());
        itemsToSpawn2.add(new StoneOfAugmentation());
        TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null) {
            int bags = 0;
            int i3 = Dungeon.depth;
            if (i3 == 6) {
                bags = (int) Math.ceil((double) (((float) (5 - hourglass.sandBags)) * 0.2f));
            } else if (i3 == 11) {
                bags = (int) Math.ceil((double) (((float) (3 - hourglass.sandBags)) * 0.25f));
            } else if (i3 == 16) {
                bags = (int) Math.ceil((double) (((float) (5 - hourglass.sandBags)) * 0.5f));
            } else if (i3 == 20 || i3 == 21) {
                bags = (int) Math.ceil((double) (((float) (5 - hourglass.sandBags)) * 0.8f));
            }
            for (int i4 = 1; i4 <= bags; i4++) {
                itemsToSpawn2.add(new TimekeepersHourglass.sandBag());
                hourglass.sandBags++;
            }
        }
        int Int2 = Random.Int(10);
        if (Int2 == 0) {
            rare = Generator.random(Generator.Category.WAND);
            rare.level(0);
        } else if (Int2 == 1) {
            rare = Generator.random(Generator.Category.RING);
            rare.level(0);
        } else if (Int2 != 2) {
            rare = new Stylus();
        } else {
            rare = Generator.random(Generator.Category.ARTIFACT);
        }
        rare.cursed = false;
        rare.cursedKnown = true;
        itemsToSpawn2.add(rare);
        if (itemsToSpawn2.size() <= 63) {
            Random.shuffle(itemsToSpawn2);
            return itemsToSpawn2;
        }
        throw new RuntimeException("Shop attempted to carry more than 63 items!");
    }


    protected static Bag ChooseBag(Belongings pack) {
        HashMap<Bag, Integer> bags = new HashMap<>();
        if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) {
            bags.put(new VelvetPouch(), 1);
        }
        if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) {
            bags.put(new ScrollHolder(), 0);
        }
        if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) {
            bags.put(new PotionBandolier(), 0);
        }
        if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) {
            bags.put(new MagicalHolster(), 0);
        }
        if (bags.isEmpty()) {
            return null;
        }
        Iterator it = pack.backpack.items.iterator();
        while (it.hasNext()) {
            Item item = (Item) it.next();
            for (Bag bag : bags.keySet()) {
                if (bag.canHold(item)) {
                    bags.put(bag, Integer.valueOf(bags.get(bag).intValue() + 1));
                }
            }
        }
        Bag bestBag = null;
        for (Bag bag2 : bags.keySet()) {
            if (bestBag == null) {
                bestBag = bag2;
            } else if (bags.get(bag2).intValue() > bags.get(bestBag).intValue()) {
                bestBag = bag2;
            }
        }
        if (bestBag instanceof VelvetPouch) {
            Dungeon.LimitedDrops.VELVET_POUCH.drop();
        } else if (bestBag instanceof ScrollHolder) {
            Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
        } else if (bestBag instanceof PotionBandolier) {
            Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
        } else if (bestBag instanceof MagicalHolster) {
            Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
        }
        return bestBag;
    }
}
