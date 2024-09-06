/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class TrinketCatalyst extends Item {

	{
		image = ItemSpriteSheet.TRINKET_CATA;

		unique = true;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		if (super.doPickUp(hero, pos)){
			if (!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_ALCHEMY)){
				GameScene.flashForDocument(Document.ADVENTURERS_GUIDE, Document.GUIDE_ALCHEMY);
			}
			return true;
		} else {
			return false;
		}
	}

	private ArrayList<Trinket> rolledTrinkets = new ArrayList<>();

	public boolean hasRolledTrinkets(){
		return !rolledTrinkets.isEmpty();
	}

	private static final String ROLLED_TRINKETS = "rolled_trinkets";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (!rolledTrinkets.isEmpty()){
			bundle.put(ROLLED_TRINKETS, rolledTrinkets);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		rolledTrinkets.clear();
		if (bundle.contains(ROLLED_TRINKETS)){
			rolledTrinkets.addAll((Collection<Trinket>) ((Collection<?>)bundle.getCollection( ROLLED_TRINKETS )));
		}
	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.size() == 1 && ingredients.get(0) instanceof TrinketCatalyst;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 6;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			//we silently re-add the catalyst so that we can clear it when a trinket is selected
			//this way player isn't totally screwed if they quit the game while selecting
			TrinketCatalyst newCata = (TrinketCatalyst) ingredients.get(0).duplicate();
			newCata.collect();

			ingredients.get(0).quantity(0);

			ShatteredPixelDungeon.scene().addToFront(new WndTrinket(newCata));
			try {
				Dungeon.saveAll(); //do a save here as pausing alch scene doesn't otherwise save
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return null;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new Trinket.PlaceHolder();
		}
	}

	public static class RandomTrinket extends Item {

		{
			image = ItemSpriteSheet.TRINKET_HOLDER;
		}

	}

	public static class WndTrinket extends Window {

		private static final int WIDTH		= 120;
		private static final int BTN_SIZE	= 24;
		private static final int BTN_GAP	= 4;
		private static final int GAP		= 2;

		private static final int NUM_TRINKETS = 4; //last one is a random choice

		public WndTrinket( TrinketCatalyst cata ){

			IconTitle titlebar = new IconTitle();
			titlebar.icon(new ItemSprite(cata));
			titlebar.label(Messages.titleCase(cata.name()));
			titlebar.setRect(0, 0, WIDTH, 0);
			add( titlebar );

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(TrinketCatalyst.class, "window_text"), 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			//roll new trinkets if trinkets were not already rolled
			while (cata.rolledTrinkets.size() < NUM_TRINKETS-1){
				cata.rolledTrinkets.add((Trinket) Generator.random(Generator.Category.TRINKET));
			}

			for (int i = 0; i < NUM_TRINKETS; i++){
				ItemButton btnReward = new ItemButton() {
					@Override
					protected void onClick() {
						ShatteredPixelDungeon.scene().addToFront(new RewardWindow(item()));
					}
				};
				if (i == NUM_TRINKETS-1){
					btnReward.item(new RandomTrinket());
				} else {
					btnReward.item(cata.rolledTrinkets.get(i));
				}
				btnReward.setRect( (i+1)*(WIDTH - BTN_GAP) / NUM_TRINKETS - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
				add( btnReward );

			}

			resize(WIDTH, (int)(message.top() + message.height() + 2*BTN_GAP + BTN_SIZE));

		}

		@Override
		public void onBackPressed() {
			//do nothing
		}

		private class RewardWindow extends WndInfoItem {

			public RewardWindow( Item item ) {
				super(item);

				RedButton btnConfirm = new RedButton(Messages.get(WndSadGhost.class, "confirm")){
					@Override
					protected void onClick() {
						RewardWindow.this.hide();
						WndTrinket.this.hide();

						Item result = item;
						if (result instanceof RandomTrinket){
							result = Generator.random(Generator.Category.TRINKET);
						}

						TrinketCatalyst cata = Dungeon.hero.belongings.getItem(TrinketCatalyst.class);

						if (cata != null) {
							cata.detach(Dungeon.hero.belongings.backpack);
							Catalog.countUse(cata.getClass());
							result.identify();
							if (ShatteredPixelDungeon.scene() instanceof AlchemyScene) {
								((AlchemyScene) ShatteredPixelDungeon.scene()).craftItem(null, result);
							} else {
								Sample.INSTANCE.play( Assets.Sounds.PUFF );

								if (result.doPickUp(Dungeon.hero)){
									GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", item.name())) );
								} else {
									Dungeon.level.drop(result, Dungeon.hero.pos);
								}

								Statistics.itemsCrafted++;
								Badges.validateItemsCrafted();

								try {
									Dungeon.saveAll();
								} catch (IOException e) {
									ShatteredPixelDungeon.reportException(e);
								}
							}
						}
					}
				};
				btnConfirm.setRect(0, height+2, width/2-1, 16);
				add(btnConfirm);

				RedButton btnCancel = new RedButton(Messages.get(WndSadGhost.class, "cancel")){
					@Override
					protected void onClick() {
						hide();
					}
				};
				btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
				add(btnCancel);

				resize(width, (int)btnCancel.bottom());
			}
		}

	}
}
