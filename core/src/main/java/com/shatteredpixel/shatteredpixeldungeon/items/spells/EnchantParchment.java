/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 *  Shattered Pixel Dungeon
 *  Copyright (C) 2014-2022 Evan Debenham
 *
 * Summoning Pixel Dungeon
 * Copyright (C) 2019-2022 TrashboxBobylev
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class EnchantParchment extends InventorySpell {


    @Override
    protected boolean usableOnItem(Item item) {
        if (glyph == null && enchantment == null){
            return ((item instanceof Armor && ((Armor) item).glyph != null) || (item instanceof Weapon && ((Weapon) item).enchantment != null));
        }
        else if (glyph != null){
            return item instanceof Armor;
        }
        else {
            return ScrollOfEnchantment.enchantable(item);
        }
    }

    {
        image = ItemSpriteSheet.ENCHANT;
        stackable = true;
    }

    @Override
    public boolean isSimilar(Item item) {
        return super.isSimilar(item) && (
                (glyph == ((EnchantParchment)(item)).glyph && enchantment == null) ||
                (enchantment == ((EnchantParchment)(item)).enchantment && glyph == null));
    }

    protected Armor.Glyph glyph;
    protected Weapon.Enchantment enchantment;

    @Override
    public ItemSprite.Glowing glowing() {
        if (glyph != null){ return glyph.glowing();}
        if (enchantment != null) {return enchantment.glowing();}
        return super.glowing();
    }

    @Override
    protected void onItemSelected(Item item) {
        if (item instanceof Armor){
            if (glyph == null) {
                glyph = ((Armor) item).glyph;
                ((Armor) item).inscribe(null);
                GLog.w(Messages.get(EnchantParchment.class, "glyph_obtained", glyph.name()));
                curItem.collect();
            } else {
                ((Armor) item).inscribe(glyph);
                GLog.p(Messages.get(EnchantParchment.class, "glyph_used", glyph.name()));
                glyph = null;
            }
        }
        if (item instanceof Weapon){
            if (enchantment == null) {
                enchantment = ((Weapon) item).enchantment;
                ((Weapon) item).enchant(null);
                GLog.w(Messages.get(EnchantParchment.class, "ench_obtained", enchantment.name()));
                curItem.collect();
            } else {
                ((Weapon) item).enchant(enchantment);
                GLog.p(Messages.get(EnchantParchment.class, "ench_used", enchantment.name()));
                glyph = null;
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        Dungeon.hero.sprite.emitter().burst(ElmoParticle.FACTORY, 10);
    }

    @Override
    public String name() {
        if (glyph != null) return super.name() + " (" + (glyph.name().toUpperCase()) + ")";
        if (enchantment != null) return super.name() + " (" + (enchantment.name().toUpperCase()) + ")";
        return super.name();
    }

    private static final String GLYPH = "glyph";
    private static final String ENCHANTMENT = "ench";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GLYPH, glyph);
        bundle.put(ENCHANTMENT, enchantment);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        glyph = (Armor.Glyph)bundle.get(GLYPH);
        enchantment = (Weapon.Enchantment)bundle.get(ENCHANTMENT);
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return Math.round(quantity * (70));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Stylus.class, ArcaneCatalyst.class};
            inQuantity = new int[]{1, 1};

            cost = 12;

            output = EnchantParchment.class;
            outQuantity = 1;
        }

    }
}
