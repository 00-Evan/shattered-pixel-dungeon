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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class WndBlacksmith extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 180;

	private static final int GAP  = 2;

	public WndBlacksmith( Blacksmith troll, Hero hero ) {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle titlebar = new IconTitle();
		titlebar.icon( troll.sprite() );
		titlebar.label( Messages.titleCase( troll.name() ) );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );

		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "prompt", Blacksmith.Quest.favor), 6 );
		message.maxWidth( width );
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );

		ArrayList<RedButton> buttons = new ArrayList<>();

		int pickaxeCost = Statistics.questScores[2] >= 2500 ? 0 : 250;
		RedButton pickaxe = new RedButton(Messages.get(this, "pickaxe", pickaxeCost), 6){
			@Override
			protected void onClick() {
				GameScene.show(new WndOptions(
						troll.sprite(),
						Messages.titleCase( troll.name() ),
						Messages.get(WndBlacksmith.class, "pickaxe_verify") + (pickaxeCost == 0 ? "\n\n" + Messages.get(WndBlacksmith.class, "pickaxe_free") : ""),
						Messages.get(WndBlacksmith.class, "pickaxe_yes"),
						Messages.get(WndBlacksmith.class, "pickaxe_no")
				){
					@Override
					protected void onSelect(int index) {
						if (index == 0){
							if (Blacksmith.Quest.pickaxe.doPickUp( Dungeon.hero )) {
								GLog.i( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", Blacksmith.Quest.pickaxe.name()) ));
							} else {
								Dungeon.level.drop( Blacksmith.Quest.pickaxe, Dungeon.hero.pos ).sprite.drop();
							}
							Blacksmith.Quest.favor -= pickaxeCost;
							Blacksmith.Quest.pickaxe = null;
							WndBlacksmith.this.hide();

							if (!Blacksmith.Quest.rewardsAvailable()){
								Notes.remove( Notes.Landmark.TROLL );
							}
						}
					}
				});
			}
		};
		pickaxe.enable(Blacksmith.Quest.pickaxe != null && Blacksmith.Quest.favor >= pickaxeCost);
		buttons.add(pickaxe);

		int reforgecost = 500 + 1000*Blacksmith.Quest.reforges;
		RedButton reforge = new RedButton(Messages.get(this, "reforge", reforgecost), 6){
			@Override
			protected void onClick() {
				GameScene.show(new WndReforge(troll, WndBlacksmith.this));
			}
		};
		reforge.enable(Blacksmith.Quest.favor >= reforgecost);
		buttons.add(reforge);

		int hardenCost = 500 + 1000*Blacksmith.Quest.hardens;
		RedButton harden = new RedButton(Messages.get(this, "harden", hardenCost), 6){
			@Override
			protected void onClick() {
				GameScene.selectItem(new HardenSelector());
			}
		};
		harden.enable(Blacksmith.Quest.favor >= hardenCost);
		buttons.add(harden);

		int upgradeCost = 1000 + 1000*Blacksmith.Quest.upgrades;
		RedButton upgrade = new RedButton(Messages.get(this, "upgrade", upgradeCost), 6){
			@Override
			protected void onClick() {
				GameScene.selectItem(new UpgradeSelector());
			}
		};
		upgrade.enable(Blacksmith.Quest.favor >= upgradeCost);
		buttons.add(upgrade);

		RedButton smith = new RedButton(Messages.get(this, "smith", 2000), 6){
			@Override
			protected void onClick() {
				GameScene.show(new WndOptions(
						troll.sprite(),
						Messages.titleCase( troll.name() ),
						Messages.get(WndBlacksmith.class, "smith_verify"),
						Messages.get(WndBlacksmith.class, "smith_yes"),
						Messages.get(WndBlacksmith.class, "smith_no")
				){
					@Override
					protected void onSelect(int index) {
						if (index == 0){
							Blacksmith.Quest.favor -= 2000;
							Blacksmith.Quest.smiths++;
							WndBlacksmith.this.hide();
							GameScene.show(new WndSmith(troll, hero));
						}
					}
				});
			}
		};
		smith.enable(Blacksmith.Quest.favor >= 2000);
		buttons.add(smith);

		RedButton cashOut = new RedButton(Messages.get(this, "cashout"), 6){
			@Override
			protected void onClick() {
				GameScene.show(new WndOptions(
						troll.sprite(),
						Messages.titleCase( troll.name() ),
						Messages.get(WndBlacksmith.class, "cashout_verify", Blacksmith.Quest.favor),
						Messages.get(WndBlacksmith.class, "cashout_yes"),
						Messages.get(WndBlacksmith.class, "cashout_no")
				){
					@Override
					protected void onSelect(int index) {
						if (index == 0){
							new Gold(Blacksmith.Quest.favor).doPickUp(Dungeon.hero, Dungeon.hero.pos);
							Blacksmith.Quest.favor = 0;
							WndBlacksmith.this.hide();
						}
					}
				});
			}
		};
		cashOut.enable(Blacksmith.Quest.favor > 0);
		buttons.add(cashOut);

		float pos = message.bottom() + 3*GAP;
		for (RedButton b : buttons){
			b.leftJustify = true;
			b.multiline = true;
			b.setSize(width, b.reqHeight());
			b.setRect(0, pos, width, b.reqHeight());
			b.enable(b.active); //so that it's visually reflected
			add(b);
			pos = b.bottom() + GAP;
		}

		resize(width, (int)pos);

	}

	//public so that it can be directly called for pre-v2.2.0 quest completions
	public static class WndReforge extends Window {

		private static final int WIDTH		= 120;

		private static final int BTN_SIZE	= 32;
		private static final float GAP		= 2;
		private static final float BTN_GAP	= 5;

		private ItemButton btnPressed;

		private ItemButton btnItem1;
		private ItemButton btnItem2;
		private RedButton btnReforge;

		public WndReforge( Blacksmith troll, Window wndParent ) {
			super();

			IconTitle titlebar = new IconTitle();
			titlebar.icon( troll.sprite() );
			titlebar.label( Messages.titleCase( troll.name() ) );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
			message.maxWidth( WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			btnItem1 = new ItemButton() {
				@Override
				protected void onClick() {
					btnPressed = btnItem1;
					GameScene.selectItem( itemSelector );
				}
			};
			btnItem1.setRect( (WIDTH - BTN_GAP) / 2 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
			add( btnItem1 );

			btnItem2 = new ItemButton() {
				@Override
				protected void onClick() {
					btnPressed = btnItem2;
					GameScene.selectItem( itemSelector );
				}
			};
			btnItem2.setRect( btnItem1.right() + BTN_GAP, btnItem1.top(), BTN_SIZE, BTN_SIZE );
			add( btnItem2 );

			btnReforge = new RedButton( Messages.get(this, "reforge") ) {
				@Override
				protected void onClick() {

					Item first, second;
					if (btnItem1.item().trueLevel() >= btnItem2.item().trueLevel()) {
						first = btnItem1.item();
						second = btnItem2.item();
					} else {
						first = btnItem2.item();
						second = btnItem1.item();
					}

					Sample.INSTANCE.play( Assets.Sounds.EVOKE );
					ScrollOfUpgrade.upgrade( Dungeon.hero );
					Item.evoke( Dungeon.hero );

					if (second.isEquipped( Dungeon.hero )) {
						((EquipableItem)second).doUnequip( Dungeon.hero, false );
					}
					second.detach( Dungeon.hero.belongings.backpack );

					if (second instanceof Armor){
						BrokenSeal seal = ((Armor) second).checkSeal();
						if (seal != null){
							Dungeon.level.drop( seal, Dungeon.hero.pos );
						}
					}

					//preserves enchant/glyphs if present
					if (first instanceof Weapon && ((Weapon) first).hasGoodEnchant()){
						((Weapon) first).upgrade(true);
					} else if (first instanceof Armor && ((Armor) first).hasGoodGlyph()){
						((Armor) first).upgrade(true);
					} else {
						first.upgrade();
					}
					Badges.validateItemLevelAquired( first );
					Item.updateQuickslot();

					Blacksmith.Quest.favor -= 500 + 1000*Blacksmith.Quest.reforges;
					Blacksmith.Quest.reforges++;

					if (!Blacksmith.Quest.rewardsAvailable()){
						Notes.remove( Notes.Landmark.TROLL );
					}

					hide();
					if (wndParent != null){
						wndParent.hide();
					}
				}
			};
			btnReforge.enable( false );
			btnReforge.setRect( 0, btnItem1.bottom() + BTN_GAP, WIDTH, 20 );
			add( btnReforge );


			resize( WIDTH, (int)btnReforge.bottom() );
		}

		protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

			@Override
			public String textPrompt() {
				return Messages.get(WndReforge.class, "prompt");
			}

			@Override
			public Class<?extends Bag> preferredBag(){
				return Belongings.Backpack.class;
			}

			@Override
			public boolean itemSelectable(Item item) {
				return item.isIdentified() && !item.cursed && item.isUpgradable();
			}

			@Override
			public void onSelect( Item item ) {
				if (item != null && btnPressed.parent != null) {
					btnPressed.item(item);

					Item item1 = btnItem1.item();
					Item item2 = btnItem2.item();

					//need 2 items
					if (item1 == null || item2 == null) {
						btnReforge.enable(false);

					//both of the same type
					} else if (item1.getClass() != item2.getClass()) {
						btnReforge.enable(false);

					//and not the literal same item (unless quantity is >1)
					} else if (item1 == item2 && item1.quantity() == 1) {
						btnReforge.enable(false);

					} else {
						btnReforge.enable(true);
					}
				}
			}
		};

	}

	private class HardenSelector extends WndBag.ItemSelector {

		@Override
		public String textPrompt() {
			return Messages.get(this, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item.isUpgradable()
					&& item.isIdentified() && !item.cursed
					&& ((item instanceof MeleeWeapon && !((Weapon) item).enchantHardened)
					|| (item instanceof Armor && !((Armor) item).glyphHardened));
		}

		@Override
		public void onSelect(Item item) {
			if (item != null) {
				if (item instanceof Weapon){
					((Weapon) item).enchantHardened = true;
				} else if (item instanceof Armor){
					((Armor) item).glyphHardened = true;
				}

				Blacksmith.Quest.favor -= 500 + 1000*Blacksmith.Quest.hardens;
				Blacksmith.Quest.hardens++;

				WndBlacksmith.this.hide();

				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
				Item.evoke( Dungeon.hero );

				if (!Blacksmith.Quest.rewardsAvailable()){
					Notes.remove( Notes.Landmark.TROLL );
				}
			}
		}
	}

	private class UpgradeSelector extends WndBag.ItemSelector {

		@Override
		public String textPrompt() {
			return Messages.get(this, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item.isUpgradable()
					&& item.isIdentified()
					&& !item.cursed
					&& item.level() < 2;
		}

		@Override
		public void onSelect(Item item) {
			if (item != null) {
				item.upgrade();
				int upgradeCost = 1000 + 1000*Blacksmith.Quest.upgrades;
				Blacksmith.Quest.favor -= upgradeCost;
				Blacksmith.Quest.upgrades++;

				WndBlacksmith.this.hide();

				Sample.INSTANCE.play(Assets.Sounds.EVOKE);
				ScrollOfUpgrade.upgrade( Dungeon.hero );
				Item.evoke( Dungeon.hero );

				Badges.validateItemLevelAquired( item );

				if (!Blacksmith.Quest.rewardsAvailable()){
					Notes.remove( Notes.Landmark.TROLL );
				}

				Catalog.countUse(item.getClass());
			}
		}
	}

	public static class WndSmith extends Window {

		private static final int WIDTH      = 120;
		private static final int BTN_SIZE	= 32;
		private static final int BTN_GAP	= 5;
		private static final int GAP		= 2;

		public WndSmith( Blacksmith troll, Hero hero ){
			super();

			IconTitle titlebar = new IconTitle();
			titlebar.icon(troll.sprite());
			titlebar.label(Messages.titleCase(troll.name()));

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "prompt"), 6 );

			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );

			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			if (Blacksmith.Quest.smithRewards == null || Blacksmith.Quest.smithRewards.isEmpty()){
				Blacksmith.Quest.generateRewards(false);
			}

			int count = 0;
			for (Item i : Blacksmith.Quest.smithRewards){
				count++;
				ItemButton btnReward = new ItemButton(){
					@Override
					protected void onClick() {
						GameScene.show(new RewardWindow(troll, hero, item()));
					}
				};
				btnReward.item( i );
				btnReward.setRect( count*(WIDTH - BTN_GAP) / 3 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
				add( btnReward );

			}

			resize(WIDTH, (int)message.bottom() + 2*BTN_GAP + BTN_SIZE);

		}

		@Override
		public void onBackPressed() {
			//do nothing
		}

		private class RewardWindow extends WndInfoItem {

			public RewardWindow( Blacksmith troll, Hero hero, Item item ) {
				super(item);

				RedButton btnConfirm = new RedButton(Messages.get(WndSadGhost.class, "confirm")){
					@Override
					protected void onClick() {
						RewardWindow.this.hide();

						if (item instanceof Weapon && Blacksmith.Quest.smithEnchant != null){
							((Weapon) item).enchant(Blacksmith.Quest.smithEnchant);
						} else if (item instanceof Armor && Blacksmith.Quest.smithGlyph != null){
							((Armor) item).inscribe(Blacksmith.Quest.smithGlyph);
						}

						item.identify(false);
						Sample.INSTANCE.play(Assets.Sounds.EVOKE);
						Item.evoke( Dungeon.hero );
						if (item.doPickUp( Dungeon.hero )) {
							GLog.i( Messages.capitalize(Messages.get(Dungeon.hero, "you_now_have", item.name())) );
						} else {
							Dungeon.level.drop( item, Dungeon.hero.pos ).sprite.drop();
						}
						WndSmith.this.hide();
						Blacksmith.Quest.smithRewards = null;

						if (!Blacksmith.Quest.rewardsAvailable()){
							Notes.remove( Notes.Landmark.TROLL );
						}
					}
				};
				btnConfirm.setRect(0, height+2, width/2-1, 16);
				add(btnConfirm);

				RedButton btnCancel = new RedButton(Messages.get(WndSadGhost.class, "cancel")){
					@Override
					protected void onClick() {
						RewardWindow.this.hide();
					}
				};
				btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
				add(btnCancel);

				resize(width, (int)btnCancel.bottom());
			}
		}

	}

}
