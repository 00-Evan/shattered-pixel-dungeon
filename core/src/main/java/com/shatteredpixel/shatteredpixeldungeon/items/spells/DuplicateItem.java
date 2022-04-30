package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;

public class DuplicateItem extends Spell{
    {
        image = ItemSpriteSheet.DUPLICATE_ITEM;
    }

    protected boolean usableOnItem(Item item) {
        return item instanceof MissileWeapon ||
                (item instanceof Potion && !(item instanceof PotionOfStrength || item instanceof PotionOfMastery || item instanceof ElixirOfMight)) ||
                (item instanceof Scroll && !(item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment)) ||
                item instanceof Plant.Seed ||
                item instanceof Runestone;
    }

    protected void onItemSelected(Item item) {
        int num = 1;
        if(item instanceof Runestone){
            num = 2;
        }else if(item instanceof MissileWeapon && (item.trueLevel() < 2 || item.isIdentified())){
            if(((MissileWeapon) item).tier < 4){
                num = 2;
            }
        }else if(item instanceof Plant.Seed){
            num = 3;
        }
        int slot = Dungeon.quickslot.getSlot(item);
        if (slot != -3 && slot != -1
                && item.defaultAction != null
                && !Dungeon.quickslot.isNonePlaceholder(slot)
                && Dungeon.hero.belongings.contains(item)) {
            Dungeon.quickslot.setSlot(slot, item);
        }
        if (item.isIdentified()){
            Catalog.setSeen(item.getClass());
        }
        item.duplicate(item, num);
        Transmuting.show(curUser, item, item);
        //curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
        GLog.p( Messages.get(this, "morph") );
        curItem.detach(Dungeon.hero.belongings.backpack);
    }

    @Override
    protected void onCast(Hero hero) {
        GameScene.selectItem( itemSelector );
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(DuplicateItem.class, "inv_title");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
        public void onSelect(Item item) {
            onItemSelected(item);
        }
    };

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return Math.round(quantity * (100 / 2f));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScrollOfTransmutation.class, ScrollOfMirrorImage.class};
            inQuantity = new int[]{1, 1};

            cost = 4;//can't duplicate the ingredients infinetly

            output = DuplicateItem.class;
            outQuantity = 2;
        }

    }
}
