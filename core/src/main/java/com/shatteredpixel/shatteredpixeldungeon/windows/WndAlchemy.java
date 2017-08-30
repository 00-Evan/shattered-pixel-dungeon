/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WndAlchemy extends Window {
	
	private WndBlacksmith.ItemButton[] inputs = new WndBlacksmith.ItemButton[3];
	private ItemSlot output;
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	
	private RedButton btnCombine;
	
	private static final int WIDTH_P = 116;
	private static final int WIDTH_L = 160;
	
	private static final int BTN_SIZE	= 28;
	
	public WndAlchemy(){
		
		int w = WIDTH_P;
		
		int h = 0;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon(DungeonTerrainTilemap.tile(0, Terrain.ALCHEMY));
		titlebar.label( Messages.get(this, "title") );
		titlebar.setRect( 0, 0, w, 0 );
		add( titlebar );
		
		h += titlebar.height() + 2;
		
		RenderedTextMultiline desc = PixelScene.renderMultiline(6);
		desc.text( Messages.get(this, "text") );
		desc.setPos(0, h);
		desc.maxWidth(w);
		add(desc);
		
		h += desc.height() + 6;
		
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = new WndBlacksmith.ItemButton(){
				@Override
				protected void onClick() {
					super.onClick();
					if (item != null){
						if (!item.collect()){
							Dungeon.level.drop(item, Dungeon.hero.pos);
						}
						item = null;
						slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
					}
					GameScene.selectItem( itemSelector, WndBag.Mode.ALCHEMY, Messages.get(WndAlchemy.class, "select") );
				}
			};
			inputs[i].setRect(15, h, BTN_SIZE, BTN_SIZE);
			add(inputs[i]);
			h += BTN_SIZE + 2;
		}
		
		Image arrow = Icons.get(Icons.RESUME);
		arrow.hardlight(0, 0, 0);
		arrow.x = (w - arrow.width)/2f;
		arrow.y = inputs[1].top() + (inputs[1].height() - arrow.height)/2f;
		PixelScene.align(arrow);
		add(arrow);
		
		output = new ItemSlot(){
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null){
					GameScene.show(new WndInfoItem(item));
				}
			}
		};
		output.setRect(w - BTN_SIZE - 15, inputs[1].top(), BTN_SIZE, BTN_SIZE);
		
		ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
		outputBG.x = output.left();
		outputBG.y = output.top();
		add(outputBG);
		
		add(output);
		output.visible = false;
		
		bubbleEmitter = new Emitter();
		smokeEmitter = new Emitter();
		bubbleEmitter.pos(outputBG.x + (BTN_SIZE-16)/2f, outputBG.y + (BTN_SIZE-16)/2f, 16, 16);
		smokeEmitter.pos(bubbleEmitter.x, bubbleEmitter.y, bubbleEmitter.width, bubbleEmitter.height);
		bubbleEmitter.autoKill = false;
		smokeEmitter.autoKill = false;
		add(bubbleEmitter);
		add(smokeEmitter);
		
		h += 4;
		
		float btnWidth = (w-14)/2f;
		
		btnCombine = new RedButton(Messages.get(this, "combine")){
			@Override
			protected void onClick() {
				super.onClick();
				combine();
			}
		};
		btnCombine.setRect(5, h, btnWidth, 18);
		PixelScene.align(btnCombine);
		btnCombine.enable(false);
		add(btnCombine);
		
		RedButton btnCancel = new RedButton(Messages.get(this, "cancel")){
			@Override
			protected void onClick() {
				super.onClick();
				onBackPressed();
			}
		};
		btnCancel.setRect(w - 5 - btnWidth, h, btnWidth, 18);
		PixelScene.align(btnCancel);
		add(btnCancel);
		
		h += btnCancel.height();
		
		resize(w, h);
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i].item == null){
						inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
						break;
					}
				}
			}
			updateState();
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item;
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		//potion creation
		if (filterInput(Plant.Seed.class).size() == 3){
			output.item(new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER));
			output.visible = true;
			btnCombine.enable(true);
			
		//blandfruit cooking
		} else if (filterInput(Blandfruit.class).size() == 1 && filterInput(Plant.Seed.class).size() == 1){
			output.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
			output.visible = true;
			btnCombine.enable(true);
			
		} else {
			btnCombine.enable(false);
			output.visible = false;
		}
	}

	private void combine(){
		ArrayList<Plant.Seed> seeds = filterInput(Plant.Seed.class);
		ArrayList<Blandfruit> fruits = filterInput(Blandfruit.class);
		
		Item result = null;
		
		//potion creation
		if (seeds.size() == 3){
			
			if (Random.Int( 3 ) == 0) {
				
				result = Generator.random( Generator.Category.POTION );
				
			} else {
				
				Class<? extends Item> itemClass = Random.element(seeds).alchemyClass;
				try {
					result = itemClass.newInstance();
				} catch (Exception e) {
					ShatteredPixelDungeon.reportException(e);
					result = Generator.random( Generator.Category.POTION );
				}
				
			}
			
			while (result instanceof PotionOfHealing
					&& Random.Int(10) < Dungeon.LimitedDrops.COOKING_HP.count) {
				result = Generator.random(Generator.Category.POTION);
			}
			
			if (result instanceof PotionOfHealing) {
				Dungeon.LimitedDrops.COOKING_HP.count++;
			}
			
			Statistics.potionsCooked++;
			Badges.validatePotionsCooked();
			
		//blandfruit cooking
		} else if (fruits.size() == 1 && seeds.size() == 1) {
			result = fruits.get(0);
			((Blandfruit)result).cook(seeds.get(0));
		}
		
		if (result != null){
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.2f, 10 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.SND_PUFF );
			
			output.item(result);
			if (!result.collect()){
				Dungeon.level.drop(result, Dungeon.hero.pos);
			}
			
			for (int i = 0; i < inputs.length; i++){
				inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
				inputs[i].item = null;
			}
			
			btnCombine.enable(false);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		for (int i = 0; i < inputs.length; i++) {
			if (inputs[i].item != null){
				if (!inputs[i].item.collect()){
					Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
				}
			}
		}
		super.onBackPressed();
	}
}
