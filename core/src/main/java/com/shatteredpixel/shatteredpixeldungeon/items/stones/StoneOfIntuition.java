/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.utils.Reflection;

import java.util.HashSet;

public class StoneOfIntuition extends InventoryStone {
	
	
	{
		mode = WndBag.Mode.UNIDED_POTION_OR_SCROLL;
		image = ItemSpriteSheet.STONE_INTUITION;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		GameScene.show( new WndGuess(item));
		
	}
	
	private static Class curGuess = null;
	
	public class WndGuess extends Window {
		
		private static final int WIDTH = 120;
		private static final int BTN_SIZE = 20;
		
		public WndGuess(final Item item){
			
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite(ItemSpriteSheet.STONE_INTUITION, null) );
			titlebar.label( Messages.titleCase(Messages.get(StoneOfIntuition.class, "name")) );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextBlock text = PixelScene.renderTextBlock(6);
			text.text( Messages.get(this, "text") );
			text.setPos(0, titlebar.bottom());
			text.maxWidth( WIDTH );
			add(text);
			
			final RedButton guess = new RedButton(""){
				@Override
				protected void onClick() {
					super.onClick();
					useAnimation();
					if (item.getClass() == curGuess){
						item.identify();
						GLog.p( Messages.get(WndGuess.class, "correct") );
						curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
					} else {
						GLog.n( Messages.get(WndGuess.class, "incorrect") );
					}
					curGuess = null;
					hide();
				}
			};
			guess.visible = false;
			guess.icon( new ItemSprite(item) );
			guess.enable(false);
			guess.setRect(0, 80, WIDTH, 20);
			add(guess);
			
			float left;
			float top = text.bottom() + 5;
			int rows;
			int placed = 0;
			
			HashSet<Class<?extends Item>> unIDed = new HashSet<>();
			final Class<?extends Item>[] all;

			if (item.isIdentified()){
				hide();
				return;
			} else if (item instanceof Potion){
				unIDed.addAll(Potion.getUnknown());
				all = (Class<? extends Item>[]) Generator.Category.POTION.classes.clone();
				if (item instanceof ExoticPotion){
					for (int i = 0; i < all.length; i++){
						all[i] = ExoticPotion.regToExo.get(all[i]);
					}
					HashSet<Class<?extends Item>> exoUID = new HashSet<>();
					for (Class<?extends Item> i : unIDed){
						exoUID.add(ExoticPotion.regToExo.get(i));
					}
					unIDed = exoUID;
				}
			} else if (item instanceof Scroll){
				unIDed.addAll(Scroll.getUnknown());
				all = (Class<? extends Item>[]) Generator.Category.SCROLL.classes.clone();
				if (item instanceof ExoticScroll) {
					for (int i = 0; i < all.length; i++) {
						all[i] = ExoticScroll.regToExo.get(all[i]);
					}
					HashSet<Class<? extends Item>> exoUID = new HashSet<>();
					for (Class<? extends Item> i : unIDed) {
						exoUID.add(ExoticScroll.regToExo.get(i));
					}
					unIDed = exoUID;
				}
			} else {
				hide();
				return;
			}
			
			if (unIDed.size() < 6){
				rows = 1;
				top += BTN_SIZE/2f;
				left = (WIDTH - BTN_SIZE*unIDed.size())/2f;
			} else {
				rows = 2;
				left = (WIDTH - BTN_SIZE*((unIDed.size()+1)/2))/2f;
			}
			
			for (int i = 0; i < all.length; i++){
				if (!unIDed.contains(all[i])) {
					continue;
				}
				
				final int j = i;
				IconButton btn = new IconButton(){
					@Override
					protected void onClick() {
						curGuess = all[j];
						guess.visible = true;
						guess.text( Messages.titleCase(Messages.get(curGuess, "name")) );
						guess.enable(true);
						super.onClick();
					}
				};
				Image im = new Image(Assets.Sprites.ITEM_ICONS);
				im.frame(ItemSpriteSheet.Icons.film.get(Reflection.newInstance(all[i]).icon));
				im.scale.set(2f);
				btn.icon(im);
				btn.setRect(left + placed*BTN_SIZE, top, BTN_SIZE, BTN_SIZE);
				add(btn);
				
				placed++;
				if (rows == 2 && placed == ((unIDed.size()+1)/2)){
					placed = 0;
					if (unIDed.size() % 2 == 1){
						left += BTN_SIZE/2f;
					}
					top += BTN_SIZE;
				}
			}
			
			resize(WIDTH, 100);
			
		}
		
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			new StoneOfIntuition().collect();
		}
	}
}
