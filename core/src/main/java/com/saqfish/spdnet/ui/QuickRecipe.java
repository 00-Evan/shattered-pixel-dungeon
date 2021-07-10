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

package com.saqfish.spdnet.ui;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.items.Generator;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.Recipe;
import com.saqfish.spdnet.items.bombs.Bomb;
import com.saqfish.spdnet.items.food.Blandfruit;
import com.saqfish.spdnet.items.food.Food;
import com.saqfish.spdnet.items.food.MeatPie;
import com.saqfish.spdnet.items.food.MysteryMeat;
import com.saqfish.spdnet.items.food.Pasty;
import com.saqfish.spdnet.items.food.StewedMeat;
import com.saqfish.spdnet.items.potions.AlchemicalCatalyst;
import com.saqfish.spdnet.items.potions.Potion;
import com.saqfish.spdnet.items.potions.brews.BlizzardBrew;
import com.saqfish.spdnet.items.potions.brews.CausticBrew;
import com.saqfish.spdnet.items.potions.brews.InfernalBrew;
import com.saqfish.spdnet.items.potions.brews.ShockingBrew;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfArcaneArmor;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfDragonsBlood;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfIcyTouch;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfMight;
import com.saqfish.spdnet.items.potions.elixirs.ElixirOfToxicEssence;
import com.saqfish.spdnet.items.potions.exotic.ExoticPotion;
import com.saqfish.spdnet.items.scrolls.Scroll;
import com.saqfish.spdnet.items.scrolls.exotic.ExoticScroll;
import com.saqfish.spdnet.items.spells.Alchemize;
import com.saqfish.spdnet.items.spells.AquaBlast;
import com.saqfish.spdnet.items.spells.ArcaneCatalyst;
import com.saqfish.spdnet.items.spells.BeaconOfReturning;
import com.saqfish.spdnet.items.spells.CurseInfusion;
import com.saqfish.spdnet.items.spells.FeatherFall;
import com.saqfish.spdnet.items.spells.MagicalInfusion;
import com.saqfish.spdnet.items.spells.MagicalPorter;
import com.saqfish.spdnet.items.spells.PhaseShift;
import com.saqfish.spdnet.items.spells.ReclaimTrap;
import com.saqfish.spdnet.items.spells.Recycle;
import com.saqfish.spdnet.items.spells.WildEnergy;
import com.saqfish.spdnet.items.stones.Runestone;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.plants.Plant;
import com.saqfish.spdnet.scenes.AlchemyScene;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.windows.WndBag;
import com.saqfish.spdnet.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickRecipe extends Component {
	
	private ArrayList<Item> ingredients;
	
	private ArrayList<ItemSlot> inputs;
	private QuickRecipe.arrow arrow;
	private ItemSlot output;
	
	public QuickRecipe(Recipe.SimpleRecipe r){
		this(r, r.getIngredients(), r.sampleOutput(null));
	}
	
	public QuickRecipe(Recipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		for (final Item in : inputs) {
			anonymize(in);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				{
					hotArea.blockLevel = PointerArea.NEVER_BLOCK;
				}

				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(in));
				}
			};
			
			ArrayList<Item> similar = Dungeon.hero.belongings.getAllSimilar(in);
			int quantity = 0;
			for (Item sim : similar) {
				//if we are looking for a specific item, it must be IDed
				if (sim.getClass() != in.getClass() || sim.isIdentified()) quantity += sim.quantity();
			}
			
			if (quantity < in.quantity()) {
				curr.sprite.alpha(0.3f);
				hasInputs = false;
			}
			curr.showExtraInfo(false);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0) {
			arrow = new arrow(Icons.get(Icons.ARROW), cost);
			arrow.hardlightText(0x00CCFF);
		} else {
			arrow = new arrow(Icons.get(Icons.ARROW));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(ShatteredPixelDungeon.scene() instanceof AlchemyScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (!hasInputs){
			this.output.sprite.alpha(0.3f);
		}
		this.output.showExtraInfo(false);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;
		
		for (ItemSlot item : inputs){
			item.setRect(x + width, y, 16, 16);
			width += 16;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class arrow extends IconButton {
		
		BitmapText text;
		
		public arrow(){
			super();
		}
		
		public arrow( Image icon ){
			super( icon );
		}
		
		public arrow( Image icon, int count ){
			super( icon );
			hotArea.blockLevel = PointerArea.NEVER_BLOCK;

			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((AlchemyScene)ShatteredPixelDungeon.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickRecipe> getRecipes( int pageIdx ){
		ArrayList<QuickRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				result.add(new QuickRecipe( new Potion.SeedToPotion(), new ArrayList<>(Arrays.asList(new Plant.Seed.PlaceHolder().quantity(3))), new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
					@Override
					public String name() {
						return Messages.get(Potion.SeedToPotion.class, "name");
					}

					@Override
					public String info() {
						return "";
					}
				}));
				return result;
			case 1:
				Recipe r = new Scroll.ScrollToStone();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					if (!scroll.isKnown()) scroll.anonymize();
					ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(scroll));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 2:
				result.add(new QuickRecipe( new StewedMeat.oneMeat() ));
				result.add(new QuickRecipe( new StewedMeat.twoMeat() ));
				result.add(new QuickRecipe( new StewedMeat.threeMeat() ));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new MeatPie.Recipe(),
						new ArrayList<Item>(Arrays.asList(new Pasty(), new Food(), new MysteryMeat.PlaceHolder())),
						new MeatPie()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new Blandfruit.CookFruit(),
						new ArrayList<>(Arrays.asList(new Blandfruit(), new Plant.Seed.PlaceHolder())),
						new Blandfruit(){

							public String name(){
								return Messages.get(Blandfruit.class, "cooked");
							}
							
							@Override
							public String info() {
								return "";
							}
						}));
				return result;
			case 3:
				r = new Bomb.EnhanceBomb();
				int i = 0;
				for (Class<?> cls : Bomb.EnhanceBomb.validIngredients.keySet()){
					if (i == 2){
						result.add(null);
						i = 0;
					}
					Item item = (Item) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(new Bomb(), item));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					i++;
				}
				return result;
			case 4:
				r = new ExoticPotion.PotionToExotic();
				for (Class<?> cls : Generator.Category.POTION.classes){
					Potion pot = (Potion) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(pot, new Plant.Seed.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 5:
				r = new ExoticScroll.ScrollToExotic();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(scroll, new Runestone.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 6:
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Plant.Seed.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Runestone.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Runestone.PlaceHolder())), new ArcaneCatalyst()));
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Plant.Seed.PlaceHolder())), new ArcaneCatalyst()));
				return result;
			case 7:
				result.add(new QuickRecipe(new CausticBrew.Recipe()));
				result.add(new QuickRecipe(new InfernalBrew.Recipe()));
				result.add(new QuickRecipe(new BlizzardBrew.Recipe()));
				result.add(new QuickRecipe(new ShockingBrew.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ElixirOfHoneyedHealing.Recipe()));
				result.add(new QuickRecipe(new ElixirOfMight.Recipe()));
				result.add(new QuickRecipe(new ElixirOfAquaticRejuvenation.Recipe()));
				result.add(new QuickRecipe(new ElixirOfDragonsBlood.Recipe()));
				result.add(new QuickRecipe(new ElixirOfIcyTouch.Recipe()));
				result.add(new QuickRecipe(new ElixirOfToxicEssence.Recipe()));
				result.add(new QuickRecipe(new ElixirOfArcaneArmor.Recipe()));
				return result;
			case 8:
				result.add(new QuickRecipe(new MagicalPorter.Recipe()));
				result.add(new QuickRecipe(new PhaseShift.Recipe()));
				result.add(new QuickRecipe(new WildEnergy.Recipe()));
				result.add(new QuickRecipe(new BeaconOfReturning.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new AquaBlast.Recipe()));
				result.add(new QuickRecipe(new FeatherFall.Recipe()));
				result.add(new QuickRecipe(new ReclaimTrap.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new CurseInfusion.Recipe()));
				result.add(new QuickRecipe(new MagicalInfusion.Recipe()));
				result.add(new QuickRecipe(new Alchemize.Recipe()));
				result.add(new QuickRecipe(new Recycle.Recipe()));
				return result;
		}
	}
	
}
