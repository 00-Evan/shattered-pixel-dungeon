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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {

	protected String inventoryTitle = Messages.get(this, "inv_title");
	protected WndBag.Mode mode = WndBag.Mode.ALL;
	
	@Override
	public void doRead() {
		
		if (!isKnown()) {
			identify();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		
		GameScene.selectItem( itemSelector, mode, inventoryTitle );
	}
	
	private void confirmCancelation() {
		GameScene.show( new WndOptions(new ItemSprite(this),
				Messages.titleCase(name()),
				Messages.get(this, "warning"),
				Messages.get(this, "yes"),
				Messages.get(this, "no") ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
				case 0:
					curUser.spendAndNext( TIME_TO_READ );
					identifiedByUse = false;
					break;
				case 1:
					GameScene.selectItem( itemSelector, mode, inventoryTitle );
					break;
				}
			}
			public void onBackPressed() {}
		} );
	}
	
	protected abstract void onItemSelected( Item item );
	
	protected static boolean identifiedByUse = false;
	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			
			//FIXME this safety check shouldn't be necessary
			//it would be better to eliminate the curItem static variable.
			if (!(curItem instanceof InventoryScroll)){
				return;
			}
			
			if (item != null) {
				
				((InventoryScroll)curItem).onItemSelected( item );
				((InventoryScroll)curItem).readAnimation();
				
				Sample.INSTANCE.play( Assets.Sounds.READ );
				
			} else if (identifiedByUse && !((Scroll)curItem).anonymous) {
				
				((InventoryScroll)curItem).confirmCancelation();
				
			} else if (!((Scroll)curItem).anonymous) {
				
				curItem.collect( curUser.belongings.backpack );
				
			}
		}
	};
}
