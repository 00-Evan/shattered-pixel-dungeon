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

package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.net.actor.Player;
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.sprites.HeroSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.ItemSlot;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.windows.WndBag;
import com.saqfish.spdnet.windows.WndInfoItem;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

public class WndInfoPlayer extends NetWindow {

	private final static int ITEM_HEIGHT = 23;
	private final static int ITEM_WIDTH = 23;
	private final static int VGAP = 3;
	private final static int HGAP = 4;

	private Image image;
	private RenderedTextBlock name;
	private RenderedTextBlock depth;
	private ColorBlock sep;
	private ItemsList items;

	public WndInfoPlayer( Receive.Player player ) {
		layout(player.nick, player.playerClass, player.depth, player.items);
	}

	public WndInfoPlayer( Player player ) {
		layout(player.nick(), player.playerClass(), player.depth(), player.items());
	}

	private void layout(String nick, int playerClass, int pdepth, Receive.NetItems netItems) {
		int x = 0;
		int y = 0;

		image = HeroSprite.avatar(WndPlayerList.playerClassToHeroClass(playerClass), 0);
		add( image );
		image.x = 0;
		image.y = 0;

		name = PixelScene.renderTextBlock( nick, 9 );
		add( name );
		name.setPos(x + image.width + VGAP,
				image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);

		depth = PixelScene.renderTextBlock( String.valueOf(pdepth), 9 );
		add( depth );

		sep = new ColorBlock(1, 1, 0xFF000000);
		add(sep);
		sep.size(width, 1);
		sep.y = image.height() + HGAP;

		items = new ItemsList(netItems);
		add( items );
		items.setPos(0, sep.y+(2*HGAP));

		resize((int)items.width(), (int)items.bottom());
	}

	@Override
	public synchronized void update() {
		super.update();
		sep.size(width, 1);
		depth.setPos(width - depth.width() - (VGAP+2), HGAP);
	}

	private class ItemsList extends Component {
		ItemSlot weaponSlot;
		ItemSlot armorSlot;
		ItemSlot artifactSlot;
		ItemSlot miscSlot;
		ItemSlot ringSlot;


		public ItemsList(Receive.NetItems items) {
			super();

			weaponSlot = itemSlot(items == null ? null : items.weapon, ItemSpriteSheet.WEAPON_HOLDER);
			armorSlot = itemSlot(items == null ? null : items.armor, ItemSpriteSheet.ARMOR_HOLDER);
			artifactSlot = itemSlot(items == null ? null : items.artifact, ItemSpriteSheet.ARTIFACT_HOLDER);
			miscSlot = itemSlot(items == null ? null : items.misc, ItemSpriteSheet.SOMETHING);
			ringSlot = itemSlot(items == null ? null : items.ring, ItemSpriteSheet.RING_HOLDER);
		}

		@Override
		protected void layout() {
			super.layout();

			weaponSlot.setRect(x, y, ITEM_WIDTH, ITEM_HEIGHT);
			add(weaponSlot);
			x += ITEM_WIDTH;

			armorSlot.setRect(x, y, ITEM_WIDTH, ITEM_HEIGHT);
			add(armorSlot);
			x += ITEM_WIDTH;

			artifactSlot.setRect(x, y, ITEM_WIDTH, ITEM_HEIGHT);
			add(artifactSlot);
			x += ITEM_WIDTH;

			miscSlot.setRect(x, y, ITEM_WIDTH, ITEM_HEIGHT);
			add(miscSlot);
			x += ITEM_WIDTH;

			ringSlot.setRect(x, y, ITEM_WIDTH, ITEM_HEIGHT);
			add(ringSlot);
			x += ITEM_WIDTH;

			width = x;
			height = ITEM_HEIGHT;
		}

		private ItemSlot itemSlot (Receive.NetItem item, int placeHolder){
			ItemSlot slot;
			try{
				Class<?> k = Reflection.forNameUnhandled(addPkgName(item.className));
				Item a = (Item)Reflection.newInstance(k);
				a.level(item.level);
				a.levelKnown = true;
				slot = new ItemSlot(a){
					@Override
					protected void onClick() {
						super.onClick();
						Game.scene().addToFront(new WndInfoItem(item));
					}
				};
			}catch(Exception ignored){
				WndBag.Placeholder p = new WndBag.Placeholder(placeHolder);
				slot = new ItemSlot(p);
			}
			return slot;
		}

		private String addPkgName(String c){
			return Game.pkgName+".items."+c;
		}
	}
}
