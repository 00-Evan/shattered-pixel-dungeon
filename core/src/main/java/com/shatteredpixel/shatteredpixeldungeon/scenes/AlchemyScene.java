/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndEnergizeItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

import java.io.IOException;
import java.util.ArrayList;

public class AlchemyScene extends PixelScene {

	//max of 3 inputs, and 3 potential recipe outputs
	private static final InputButton[] inputs = new InputButton[3];
	private static final CombineButton[] combines = new CombineButton[3];
	private static final OutputSlot[] outputs = new OutputSlot[3];
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	private Emitter sparkEmitter;
	
	private Emitter lowerBubbles;
	private SkinnedBlock water;

	private Image energyIcon;
	private RenderedTextBlock energyLeft;
	private IconButton energyAdd;

	private static final int BTN_SIZE	= 28;

	{
		inGameScene = true;
	}
	
	@Override
	public void create() {
		super.create();
		
		water = new SkinnedBlock(
				Camera.main.width, Camera.main.height,
				Dungeon.level.waterTex() ){
			
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}
			
			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		water.autoAdjust = true;
		add(water);
		
		Image im = new Image(TextureCache.createGradient(0x66000000, 0x88000000, 0xAA000000, 0xCC000000, 0xFF000000));
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		bubbleEmitter = new Emitter();
		bubbleEmitter.pos(0, 0, Camera.main.width, Camera.main.height);
		bubbleEmitter.autoKill = false;
		add(bubbleEmitter);

		lowerBubbles = new Emitter();
		add(lowerBubbles);
		
		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(Camera.main.width - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);
		
		int w = 50 + Camera.main.width/2;
		int left = (Camera.main.width - w)/2;
		
		int pos = (Camera.main.height - 100)/2;
		
		RenderedTextBlock desc = PixelScene.renderTextBlock(6);
		desc.maxWidth(w);
		desc.text( Messages.get(AlchemyScene.class, "text") );
		desc.setPos(left + (w - desc.width())/2, pos);
		add(desc);
		
		pos += desc.height() + 6;

		NinePatch inputBG = Chrome.get(Chrome.Type.TOAST_TR);
		inputBG.x = left + 6;
		inputBG.y = pos;
		inputBG.size(BTN_SIZE+8, 3*BTN_SIZE + 4 + 8);
		add(inputBG);

		pos += 4;

		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new InputButton();
				inputs[i].setRect(left + 10, pos, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				pos += BTN_SIZE + 2;
			}
		}

		for (int i = 0; i < inputs.length; i++){
			combines[i] = new CombineButton(i);
			combines[i].enable(false);

			outputs[i] = new OutputSlot();
			outputs[i].item(null);

			if (i == 0){
				//first ones are always visible
				combines[i].setRect(left + (w-30)/2f, inputs[1].top()+5, 30, inputs[1].height()-10);
				outputs[i].setRect(left + w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
			} else {
				combines[i].visible = false;
				outputs[i].visible = false;
			}

			add(combines[i]);
			add(outputs[i]);
		}

		smokeEmitter = new Emitter();
		smokeEmitter.pos(outputs[0].left() + (BTN_SIZE-16)/2f, outputs[0].top() + (BTN_SIZE-16)/2f, 16, 16);
		smokeEmitter.autoKill = false;
		add(smokeEmitter);
		
		pos += 10;

		lowerBubbles.pos(0, pos, Camera.main.width, Math.max(0, Camera.main.height-pos));
		lowerBubbles.pour(Speck.factory( Speck.BUBBLE ), 0.1f );
		
		ExitButton btnExit = new ExitButton(){
			@Override
			protected void onClick() {
				Game.switchScene(GameScene.class);
			}
		};
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		IconButton btnGuide = new IconButton( new ItemSprite(ItemSpriteSheet.ALCH_PAGE, null)){
			@Override
			protected void onClick() {
				super.onClick();
				clearSlots();
				updateState();
				AlchemyScene.this.addToFront(new Window(){
				
					{
						WndJournal.AlchemyTab t = new WndJournal.AlchemyTab();
						int w, h;
						if (landscape()) {
							w = WndJournal.WIDTH_L; h = WndJournal.HEIGHT_L;
						} else {
							w = WndJournal.WIDTH_P; h = WndJournal.HEIGHT_P;
						}
						resize(w, h);
						add(t);
						t.setRect(0, 0, w, h);
					}
				
				});
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Document.ALCHEMY_GUIDE.title());
			}
		};
		btnGuide.setRect(0, 0, 20, 20);
		add(btnGuide);

		String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy;
		if (toolkit != null){
			energyText += "+" + toolkit.availableEnergy();
		}

		energyLeft = PixelScene.renderTextBlock(energyText, 9);
		energyLeft.setPos(
				(Camera.main.width - energyLeft.width())/2,
				Camera.main.height - 8 - energyLeft.height()
		);
		energyLeft.hardlight(0x44CCFF);
		add(energyLeft);

		energyIcon = new ItemSprite( toolkit != null ? ItemSpriteSheet.ARTIFACT_TOOLKIT : ItemSpriteSheet.ENERGY);
		energyIcon.x = energyLeft.left() - energyIcon.width();
		energyIcon.y = energyLeft.top() - (energyIcon.height() - energyLeft.height())/2;
		align(energyIcon);
		add(energyIcon);

		energyAdd = new IconButton(Icons.get(Icons.PLUS)){
			@Override
			protected void onClick() {
				WndEnergizeItem.openItemSelector();
			}
		};
		energyAdd.setRect(energyLeft.right(), energyLeft.top() - (16 - energyLeft.height())/2, 16, 16);
		align(energyAdd);
		add(energyAdd);

		sparkEmitter = new Emitter();
		sparkEmitter.pos(energyLeft.left(), energyLeft.top(), energyLeft.width(), energyLeft.height());
		sparkEmitter.autoKill = false;
		add(sparkEmitter);
		
		fadeIn();
		
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
	}
	
	@Override
	public void update() {
		super.update();
		water.offset( 0, -5 * Game.elapsed );
	}
	
	@Override
	protected void onBackPressed() {
		Game.switchScene(GameScene.class);
	}
	
	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(AlchemyScene.class, "select");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Recipe.usableInRecipe(item);
		}

		@Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item() == null) {
							if (item instanceof LiquidMetal){
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
					updateState();
				}
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item();
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState(){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		ArrayList<Recipe> recipes = Recipe.findRecipes(ingredients);

		//disables / hides unneeded buttons
		for (int i = recipes.size(); i < combines.length; i++){
			combines[i].enable(false);
			outputs[i].item(null);

			if (i != 0){
				combines[i].visible = false;
				outputs[i].visible = false;
			}
		}

		if (recipes.isEmpty()){
			combines[0].setPos(combines[0].left(), inputs[1].top()+5);
			outputs[0].setPos(outputs[0].left(), inputs[1].top());
			return;
		}

		//positions active buttons
		float gap = recipes.size() == 2 ? 6 : 2;

		float height = inputs[2].bottom() - inputs[0].top();
		height -= recipes.size()*BTN_SIZE + (recipes.size()-1)*gap;
		float top = inputs[0].top() + height/2;

		//positions and enables active buttons
		for (int i = 0; i < recipes.size(); i++){

			Recipe recipe = recipes.get(i);

			int cost = recipe.cost(ingredients);

			outputs[i].visible = true;
			outputs[i].setRect(outputs[0].left(), top, BTN_SIZE, BTN_SIZE);
			outputs[i].item(recipe.sampleOutput(ingredients));
			top += BTN_SIZE+gap;

			int availableEnergy = Dungeon.energy;
			if (toolkit != null){
				availableEnergy += toolkit.availableEnergy();
			}

			combines[i].visible = true;
			combines[i].setRect(combines[0].left(), outputs[i].top()+5, 30, 20);
			combines[i].enable(cost <= availableEnergy, cost);

		}
		
	}
	
	private void combine( int slot ){
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		if (ingredients.isEmpty()) return;

		ArrayList<Recipe> recipes = Recipe.findRecipes(ingredients);
		if (recipes.size() <= slot) return;

		Recipe recipe = recipes.get(slot);
		
		Item result = null;
		
		if (recipe != null){
			int cost = recipe.cost(ingredients);
			if (toolkit != null){
				cost = toolkit.consumeEnergy(cost);
			}
			Dungeon.energy -= cost;

			String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy;
			if (toolkit != null){
				energyText += "+" + toolkit.availableEnergy();
			}
			energyLeft.text(energyText);
			energyLeft.setPos(
					(Camera.main.width - energyLeft.width())/2,
					Camera.main.height - 8 - energyLeft.height()
			);

			energyIcon.x = energyLeft.left() - energyIcon.width();
			align(energyIcon);

			energyAdd.setPos(energyLeft.right(), energyAdd.top());
			align(energyAdd);
			
			result = recipe.brew(ingredients);
		}
		
		if (result != null){
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.01f, 100 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );

			int resultQuantity = result.quantity();
			if (!result.collect()){
				Dungeon.level.drop(result, Dungeon.hero.pos);
			}

			Statistics.itemsCrafted++;
			Badges.validateItemsCrafted();
			
			try {
				Dungeon.saveAll();
			} catch (IOException e) {
				ShatteredPixelDungeon.reportException(e);
			}
			
			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item() != null) {
						Item item = inputs[i].item();
						if (item.quantity() <= 0) {
							inputs[i].item(null);
						} else {
							inputs[i].slot.updateText();
						}
					}
				}
			}
			
			updateState();
			//we reset the quantity in case the result was merged into another stack in the backpack
			result.quantity(resultQuantity);
			outputs[0].item(result);
		}
		
	}
	
	public void populate(ArrayList<Item> toFind, Belongings inventory){
		clearSlots();
		
		int curslot = 0;
		for (Item finding : toFind){
			int needed = finding.quantity();
			ArrayList<Item> found = inventory.getAllSimilar(finding);
			while (!found.isEmpty() && needed > 0){
				Item detached;
				if (finding instanceof LiquidMetal) {
					detached = found.get(0).detachAll(inventory.backpack);
				} else {
					detached = found.get(0).detach(inventory.backpack);
				}
				inputs[curslot].item(detached);
				curslot++;
				needed -= detached.quantity();
				if (detached == found.get(0)) {
					found.remove(0);
				}
			}
		}
		updateState();
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			clearSlots();
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = null;
			}
		}
		
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
		super.destroy();
	}
	
	public void clearSlots(){
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] != null && inputs[i].item() != null) {
					Item item = inputs[i].item();
					if (!item.collect()) {
						Dungeon.level.drop(item, Dungeon.hero.pos);
					}
					inputs[i].item(null);
				}
			}
		}
	}

	public void createEnergy(){
		String energyText = Messages.get(AlchemyScene.class, "energy") + " " + Dungeon.energy;
		if (toolkit != null){
			energyText += "+" + toolkit.availableEnergy();
		}
		energyLeft.text(energyText);
		energyLeft.setPos(
				(Camera.main.width - energyLeft.width())/2,
				Camera.main.height - 8 - energyLeft.height()
		);

		energyIcon.x = energyLeft.left() - energyIcon.width();
		align(energyIcon);

		energyAdd.setPos(energyLeft.right(), energyAdd.top());
		align(energyAdd);

		bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.01f, 100 );
		sparkEmitter.burst(SparkParticle.FACTORY, 20);
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );

		updateState();
	}
	
	private class InputButton extends Component {
		
		protected NinePatch bg;
		protected ItemSlot slot;
		
		private Item item = null;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			bg = Chrome.get( Chrome.Type.RED_BUTTON);
			add( bg );
			
			slot = new ItemSlot() {
				@Override
				protected void onPointerDown() {
					bg.brightness( 1.2f );
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
				@Override
				protected void onPointerUp() {
					bg.resetColor();
				}
				@Override
				protected void onClick() {
					super.onClick();
					Item item = InputButton.this.item;
					if (item != null) {
						if (!item.collect()) {
							Dungeon.level.drop(item, Dungeon.hero.pos);
						}
						InputButton.this.item(null);
						updateState();
					}
					AlchemyScene.this.addToFront(WndBag.getBag( itemSelector ));
				}

				@Override
				protected boolean onLongClick() {
					Item item = InputButton.this.item;
					if (item != null){
						AlchemyScene.this.addToFront(new WndInfoItem(item));
						return true;
					}
					return false;
				}
			};
			slot.enable(true);
			add( slot );
		}

		@Override
		protected void layout() {
			super.layout();
			
			bg.x = x;
			bg.y = y;
			bg.size( width, height );
			
			slot.setRect( x + 2, y + 2, width - 4, height - 4 );
		}

		public Item item(){
			return item;
		}

		public void item( Item item ) {
			if (item == null){
				this.item = null;
				slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
			} else {
				slot.item(this.item = item);
			}
		}
	}

	private class CombineButton extends Component {

		protected int slot;

		protected RedButton button;
		protected RenderedTextBlock costText;

		private CombineButton(int slot){
			super();

			this.slot = slot;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			button = new RedButton(""){
				@Override
				protected void onClick() {
					super.onClick();
					combine(slot);
				}
			};
			button.icon(Icons.get(Icons.ARROW));
			add(button);

			costText = PixelScene.renderTextBlock(6);
			add(costText);
		}

		@Override
		protected void layout() {
			super.layout();

			button.setRect(x, y, width(), height());

			costText.setPos(
					left() + (width() - costText.width())/2,
					top() - costText.height()
			);
		}

		public void enable( boolean enabled ){
			enable(enabled, 0);
		}

		public void enable( boolean enabled, int cost ){
			button.enable(enabled);
			if (enabled) {
				button.icon().tint(1, 1, 0, 1);
				button.alpha(1f);
				costText.hardlight(0x44CCFF);
			} else {
				button.icon().color(0, 0, 0);
				button.alpha(0.6f);
				costText.hardlight(0xFF0000);
			}

			if (cost == 0){
				costText.visible = false;
			} else {
				costText.visible = true;
				costText.text(Messages.get(AlchemyScene.class, "energy") + " " + cost);
			}

			layout();
		}

	}

	private class OutputSlot extends Component {

		protected NinePatch bg;
		protected ItemSlot slot;

		@Override
		protected void createChildren() {

			bg = Chrome.get(Chrome.Type.TOAST_TR);
			add(bg);

			slot = new ItemSlot() {
				@Override
				protected void onClick() {
					super.onClick();
					if (visible && item != null && item.trueName() != null){
						AlchemyScene.this.addToFront(new WndInfoItem(item));
					}
				}
			};
			slot.item(null);
			add( slot );
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x;
			bg.y = y;
			bg.size(width(), height());

			slot.setRect(x+2, y+2, width()-4, height()-4);
		}

		public void item( Item item ) {
			slot.item(item);
		}
	}

	private static AlchemistsToolkit toolkit;

	public static void assignToolkit( AlchemistsToolkit toolkit ){
		AlchemyScene.toolkit = toolkit;
	}

	public static void clearToolkit(){
		AlchemyScene.toolkit = null;
	}

}
