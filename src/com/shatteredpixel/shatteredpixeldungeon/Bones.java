/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";
	
	private static int depth = -1;
	private static Item item;
	
	public static void leave() {
		
		item = pickItem(Dungeon.hero);

		
		depth = Dungeon.depth;
		
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, depth );
		bundle.put( ITEM, item );
		
		try {
			OutputStream output = Game.instance.openFileOutput( BONES_FILE, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
		} catch (IOException e) {

		}
	}

    private static Item pickItem(Hero hero){
        Item item = null;
        if (Random.Int(2) == 0) {
            switch (Random.Int(5)) {
                case 0:
                    item = hero.belongings.weapon;
                    break;
                case 1:
                    item = hero.belongings.armor;
                    break;
                case 2:
                    item = hero.belongings.misc1;
                    break;
                case 3:
                    item = hero.belongings.misc2;
                    break;
                case 4:
                    item = QuickSlot.getItem();
                    break;
            }
            if (item != null && !item.bones)
                return pickItem(hero);
        } else {

            Iterator<Item> iterator = hero.belongings.backpack.iterator();
            Item curItem;
            ArrayList<Item> items = new ArrayList<Item>();
            while (iterator.hasNext()){
                curItem = iterator.next();
                if (curItem.bones && !(curItem instanceof EquipableItem))
                    items.add(curItem);
            }

            if (!items.isEmpty()) {
                item = Random.element(items);
                if (item.stackable){
                    item.quantity((int)Math.sqrt(item.quantity()));
                }
            }
        }
        if (item == null) {
            if (Dungeon.gold > 0) {
                item = new Gold( Random.NormalIntRange( 1, Dungeon.gold ) );
            } else {
                item = new Gold( 1 );
            }
        }
        return item;
    }
	
	public static Item get() {
		if (depth == -1) {
			
			try {
				InputStream input = Game.instance.openFileInput( BONES_FILE ) ;
				Bundle bundle = Bundle.read( input );
				input.close();
				
				depth = bundle.getInt( LEVEL );
				item = (Item)bundle.get( ITEM );
				
				return get();
				
			} catch (Exception e) {
				return null;
			}
			
		} else {
			if (depth == Dungeon.depth) {
				Game.instance.deleteFile( BONES_FILE );
				depth = 0;
				
				if (item.isUpgradable()) {
					item.cursed = true;
					item.cursedKnown = true;
					if (item.isUpgradable()) {
						int lvl = (Dungeon.depth - 1) * 3 / 5 + 1;
						if (lvl < item.level) {
							item.degrade( item.level - lvl );
						}
						item.levelKnown = false;
					}
				}
				
				item.syncVisuals();
				
				return item;
			} else {
				return null;
			}
		}
	}
}
