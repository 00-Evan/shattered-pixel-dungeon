/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

public class WndTradeItem extends Window {
	
	private static final float GAP		= 2;
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 16;
	
	private static final String TXT_SALE		= "FOR SALE: %s - %dg";
	private static final String TXT_BUY			= "Buy for %dg";
	private static final String TXT_STEAL		= "Steal with %d%% chance";
	private static final String TXT_SELL		= "Sell for %dg";
	private static final String TXT_SELL_1		= "Sell 1 for %dg";
	private static final String TXT_SELL_ALL	= "Sell all for %dg";
	private static final String TXT_CANCEL		= "Never mind";
	
	private static final String TXT_SOLD	= "You've sold your %s for %dg";
	private static final String TXT_BOUGHT	= "You've bought %s for %dg";
	private static final String TXT_STOLE	= "You've stolen the %s";
	
	private WndBag owner;
	
	public WndTradeItem( final Item item, WndBag owner ) {
		
		super();
		
		this.owner = owner;
		
		float pos = createDescription( item, false );
		
		if (item.quantity() == 1) {
			
			RedButton btnSell = new RedButton( Utils.format( TXT_SELL, item.price() ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell );
			
			pos = btnSell.bottom();
			
		} else {
			
			int priceAll= item.price();
			RedButton btnSell1 = new RedButton( Utils.format( TXT_SELL_1, priceAll / item.quantity() ) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Utils.format( TXT_SELL_ALL, priceAll ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnSellAll );
			
			pos = btnSellAll.bottom();
			
		}
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	public WndTradeItem( final Heap heap, boolean canBuy ) {
		
		super();
		
		Item item = heap.peek();
		
		float pos = createDescription( item, true );
		
		final int price = price( item );
		
		if (canBuy) {
			
			RedButton btnBuy = new RedButton( Utils.format( TXT_BUY, price ) ) {
				@Override
				protected void onClick() {
					hide();
					buy( heap );
				}
			};
			btnBuy.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			btnBuy.enable( price <= Dungeon.gold );
			add( btnBuy );

			RedButton btnCancel = new RedButton( TXT_CANCEL ) {
				@Override
				protected void onClick() {
					hide();
				}
			};

			final MasterThievesArmband.Thievery thievery = Dungeon.hero.buff(MasterThievesArmband.Thievery.class);
			if (thievery != null) {
				final float chance = thievery.stealChance(price);
				RedButton btnSteal = new RedButton(Utils.format(TXT_STEAL, Math.min(100, (int)(chance*100)))) {
					@Override
					protected void onClick() {
						if(thievery.steal(price)){
							Hero hero = Dungeon.hero;
							Item item = heap.pickUp();
							GLog.i( TXT_STOLE, item.name());
							hide();

							if (!item.doPickUp( hero )) {
								Dungeon.level.drop( item, heap.pos ).sprite.drop();
							}
						} else {
							for (Mob mob : Dungeon.level.mobs){
								if (mob instanceof Shopkeeper) {
									mob.yell(((Shopkeeper) mob).TXT_THIEF);
									((Shopkeeper) mob).flee();
									break;
								}
							}
							hide();
						}
					}
				};
				btnSteal.setRect(0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT);
				add(btnSteal);

				btnCancel.setRect( 0, btnSteal.bottom() + GAP, WIDTH, BTN_HEIGHT );
			} else
				btnCancel.setRect( 0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT );

			add( btnCancel );
			
			resize( WIDTH, (int)btnCancel.bottom() );
			
		} else {
			
			resize( WIDTH, (int)pos );
			
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		if (owner != null) {
			owner.hide();
			Shopkeeper.sell();
		}
	}
	
	private float createDescription( Item item, boolean forSale ) {
		
		// Title
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item ) );
		titlebar.label( forSale ?
			Utils.format( TXT_SALE, item.toString(), price( item ) ) :
			Utils.capitalize( item.toString() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		// Upgraded / degraded
		if (item.levelKnown) {
			if (item.level() < 0) {
				titlebar.color( ItemSlot.DEGRADED );
			} else if (item.level() > 0) {
				titlebar.color( ItemSlot.UPGRADED );
			}
		}
		
		// Description
		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		add( info );
		
		return info.y + info.height();
	}
	
	private void sell( Item item ) {
		
		Hero hero = Dungeon.hero;
		
		if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false )) {
			return;
		}
		item.detachAll( hero.belongings.backpack );
		
		int price = item.price();
		
		new Gold( price ).doPickUp( hero );
		GLog.i( TXT_SOLD, item.name(), price );
	}
	
	private void sellOne( Item item ) {
		
		if (item.quantity() <= 1) {
			sell( item );
		} else {
			
			Hero hero = Dungeon.hero;
			
			item = item.detach( hero.belongings.backpack );
			int price = item.price();
			
			new Gold( price ).doPickUp( hero );
			GLog.i( TXT_SOLD, item.name(), price );
		}
	}
	
	private int price( Item item ) {
		int price = item.price() * 5 * (Dungeon.depth / 5 + 1);
		return price;
	}
	
	private void buy( Heap heap ) {
		
		Hero hero = Dungeon.hero;
		Item item = heap.pickUp();
		
		int price = price( item );
		Dungeon.gold -= price;
		
		GLog.i( TXT_BOUGHT, item.name(), price );
		
		if (!item.doPickUp( hero )) {
			Dungeon.level.drop( item, heap.pos ).sprite.drop();
		}
	}
}
