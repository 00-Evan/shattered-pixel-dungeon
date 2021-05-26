/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.elementalpixel.elementalpixeldungeon.items;


import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.scenes.GameScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.elementalpixel.elementalpixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class BrokenAmulet extends Item {

    private static final String AC_FIX = "FIX";
    protected WndBag.Mode mode = WndBag.Mode.FRAGMENT;

    {
        image = ItemSpriteSheet.AMULET;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_FIX);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_FIX)) {
            GameScene.selectItem(itemSelector, mode, Messages.get(this, "fragment"));
        }
    }

    @Override
    public boolean doPickUp(Hero hero) {
        if (super.doPickUp(hero)) {
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

    private static int fragmentCount = 0;

    public static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            Hero hero = Dungeon.hero;
            curUser.spend(1);

            fragmentCount++;

            if (fragmentCount == 4) {
                item.detach(hero.belongings.backpack);
                GLog.h(Messages.get(BrokenAmulet.class, "fix"));
                ((BrokenAmulet) curItem).detach(hero.belongings.backpack);

                new Amulet().collect();
            } else if (fragmentCount < 4) {
                try {
                    item.detach(hero.belongings.backpack);
                } catch (NullPointerException e) {

                }

            }
        }
    };
}


