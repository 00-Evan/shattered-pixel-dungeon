/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

public class ScrollOfEnchantment extends ExoticScroll {
	
	{
		initials = 11;
	}
	
	@Override
	public void doRead() {
		setKnown();
		
		GameScene.selectItem( itemSelector, WndBag.Mode.ENCHANTABLE, "TODO" );
	}
	
	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(final Item item) {
			
			if (item instanceof Weapon){
				
				final Weapon.Enchantment enchants[] = new Weapon.Enchantment[3];
				
				enchants[0] = Weapon.Enchantment.randomCommon();
				enchants[1] = Weapon.Enchantment.randomUncommon();
				do {
					enchants[2] = Weapon.Enchantment.random();
				} while (enchants[2].getClass() == enchants[0].getClass() ||
						enchants[1].getClass() == enchants[0].getClass());
				
				GameScene.show(new WndOptions("title", "body",
						enchants[0].name(),
						enchants[1].name(),
						enchants[2].name(),
						"cancel"){
					
					@Override
					protected void onSelect(int index) {
						if (index < 3) {
							((Weapon) item).enchant(enchants[index]);
							//TODO text
							Enchanting.show(curUser, item);
						}
					}
				});
			
			} else {
				
				final Armor.Glyph glyphs[] = new Armor.Glyph[3];
				
				glyphs[0] = Armor.Glyph.randomCommon();
				glyphs[1] = Armor.Glyph.randomUncommon();
				do {
					glyphs[2] = Armor.Glyph.random();
				} while (glyphs[2].getClass() == glyphs[0].getClass() ||
						glyphs[1].getClass() == glyphs[0].getClass());
				
				GameScene.show(new WndOptions("title", "body",
						glyphs[0].name(),
						glyphs[1].name(),
						glyphs[2].name(),
						"cancel"){
					
					@Override
					protected void onSelect(int index) {
						if (index < 3) {
							((Armor) item).inscribe(glyphs[index]);
							//TODO text
							Enchanting.show(curUser, item);
						}
					}
				});
			}
		}
	};
}
