package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by debenhame on 16/01/2015.
 */
public class QuickSlot {

    /**
     * Slots contain objects which are also in a player's inventory.
     * The one exception to this is when quantity is 0, which can
     * happen for a stackable item that has been 'used up', these are refered to a placeholders.
     */
    //TODO: seriously reconsider using an arraylist here, may make much more sense to just use an array.
    private ArrayList<Item> slots = new ArrayList<Item>();

    public void setSlot(int slot, Item item){
        slots.ensureCapacity(slot+1);
        slots.set(slot, item);
    }

    public Item getItem(int slot){
        return slots.get(slot);
    }

    //TODO: this will currently only get the first occurance of an item, even if it's in multiple slots
    //perhaps do not allow duplicate items in quickslots.
    public int getSlot(Item item) {
        return slots.indexOf(item);
    }

    public Boolean isPlaceholder(int slot){
        return slots.get(slot) != null && slots.get(slot).quantity() == 0;
    }

    public void clearSlot(int slot){
        setSlot(slot, null);
    }

    public void clearItem(Item item){
        for (int i = 0; i < slots.size(); i++)
            if (slots.get(i) == item)
                clearSlot(i);
    }

    public void reset(){
        slots = new ArrayList<Item>();
        slots.add(null);
        slots.add(null);
        slots.add(null);
        slots.add(null);
    }

    public boolean contains(Item item){
        return slots.contains(item);
    }

    public void replaceSimilar(Item item){
        for (int i = 0; i < slots.size(); i++)
            if (slots.get(i) != null && item.isSimilar(slots.get(i)))
                setSlot( i , item );
    }

    public void convertToPlaceholder(Item item){
        Item placeholder = Item.virtual(item.getClass());

        if (placeholder != null && slots.contains(item))
            for (int i = 0; i < slots.size(); i++)
                if (slots.get(i) == item)
                    setSlot( i , placeholder );
    }

    public Item randomNonePlaceholder(){

        ArrayList<Item> result = new ArrayList<Item>();
        for (int i = 0; i < slots.size(); i ++)
            if (slots.get(i) != null && !isPlaceholder(i))
                result.add(slots.get(i));

        return Random.element(result);
    }

    private final String PLACEHOLDERS = "placeholders";
    private final String PLACEMENTS = "placements";

    public void storePlaceholders(Bundle bundle){
        ArrayList<Item> placeholders = new ArrayList<Item>(slots.size());
        boolean[] placements = new boolean[slots.size()];

        for (int i = 0; i < slots.size(); i++)
            if (isPlaceholder(i)) {
                placeholders.add(slots.get(i));
                placements[i] = true;
            }
        bundle.put( PLACEHOLDERS, placeholders );
        bundle.put( PLACEMENTS, placements );
    }

    //placements array is used as order is preserved while bundling, but exact index is not, so if we
    //bundle both the placeholders (which preserves their order) an an array telling us where the placeholders are,
    //we can reconstruct them perfectly.

    public void restorePlaceholders(Bundle bundle){
        Collection<Bundlable> placeholders = bundle.getCollection(PLACEHOLDERS);
        boolean[] placements = bundle.getBooleanArray( PLACEMENTS );

        int i = 0;
        for (Bundlable item : placeholders){
            while (placements[i] == false) i++;
            setSlot( i, (Item)item );
            i++;
        }

    }

} 
