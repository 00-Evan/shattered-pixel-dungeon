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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndKeyBindings extends Window {

	private static final int WIDTH = 120;

	private static final int TTL_HEIGHT = 16;

	private static final int COL1_CENTER = WIDTH/4;
	private static final int COL2_CENTER = 5*WIDTH/8;
	private static final int COL3_CENTER = 7*WIDTH/8;

	private Component bindingsList;

	public WndKeyBindings() {

		resize(WIDTH, Math.min(340, PixelScene.uiCamera.height-20));

		RenderedTextBlock ttlAction = PixelScene.renderTextBlock(Messages.get(this, "ttl_action"), 9);
		ttlAction.setPos( COL1_CENTER - ttlAction.width()/2, (TTL_HEIGHT - ttlAction.height())/2);
		add(ttlAction);

		ColorBlock ttlSep1 = new ColorBlock(1, TTL_HEIGHT, 0xFF222222);
		ttlSep1.x = WIDTH/2;
		add(ttlSep1);

		RenderedTextBlock ttlKey1 = PixelScene.renderTextBlock(Messages.get(this, "ttl_key1"), 9);
		ttlKey1.setPos(COL2_CENTER - ttlKey1.width()/2, (TTL_HEIGHT - ttlKey1.height())/2);
		add(ttlKey1);

		ColorBlock ttlSep2 = new ColorBlock(1, TTL_HEIGHT, 0xFF222222);
		ttlSep2.x = 3*WIDTH/4;
		add(ttlSep2);

		RenderedTextBlock ttlKey2 = PixelScene.renderTextBlock(Messages.get(this, "ttl_key2"), 9);
		ttlKey2.setPos(COL3_CENTER - ttlKey2.width()/2, (TTL_HEIGHT - ttlKey2.height())/2);
		add(ttlKey2);

		ColorBlock ttlSep3 = new ColorBlock(WIDTH, 1, 0xFF222222);
		ttlSep3.y = TTL_HEIGHT;
		add(ttlSep3);

		bindingsList = new Component();

		ScrollPane scrollingList = new ScrollPane(bindingsList){
			@Override
			public void onClick(float x, float y) {
				//TODO
			}
		};

		add(scrollingList);

		int y = 0;
		for (GameAction action : GameAction.allActions()){
			//start at 3. No bindings for NONE, BACK, and MENU.
			if (action.code() < 3) continue;

			BindingListItem item = new BindingListItem(action);
			item.setRect(0, y, WIDTH, 12);
			bindingsList.add(item);
			y += item.height();
		}
		bindingsList.setSize(WIDTH, y);

		RedButton btnDefaults = new RedButton(Messages.get(this, "default"), 9){
			@Override
			protected void onClick() {
				//TODO reset to default functionality
			}
		};
		btnDefaults.setRect(0, height - TTL_HEIGHT*2 -1, WIDTH, TTL_HEIGHT);
		add(btnDefaults);

		RedButton btnConfirm = new RedButton(Messages.get(this, "confirm"), 9){
			@Override
			protected void onClick() {
				//TODO save changed bindings
				hide();
			}
		};
		btnConfirm.setRect(0, height - TTL_HEIGHT, WIDTH/2 - 1, TTL_HEIGHT);
		add(btnConfirm);

		RedButton btnCancel = new RedButton(Messages.get(this, "cancel"), 9){
			@Override
			protected void onClick() {
				hide(); //close and don't save
			}
		};
		btnCancel.setRect(WIDTH/2, height - TTL_HEIGHT, WIDTH/2, TTL_HEIGHT);
		add(btnCancel);

		scrollingList.setRect(0, TTL_HEIGHT+1, WIDTH, btnDefaults.top()-TTL_HEIGHT-1);

	}

	@Override
	public void onBackPressed() {
		//do nothing, avoids accidental back presses which would lose progress.
	}

	private class BindingListItem extends Component {

		private static final int CHANGED = TITLE_COLOR;
		private static final int DEFAULT = 0xFFFFFF;
		private static final int UNBOUND = 0x888888;

		private GameAction gameAction;
		private int key1;
		private int key2;

		private RenderedTextBlock actionName;
		private RenderedTextBlock key1Name;
		private RenderedTextBlock key2Name;

		private ColorBlock sep1;
		private ColorBlock sep2;
		private ColorBlock sep3;

		public BindingListItem( GameAction action ){
			gameAction = action;

			actionName = PixelScene.renderTextBlock(Messages.get(WndKeyBindings.class, action.name()), 6 );
			actionName.setHightlighting(false);
			add(actionName);

			ArrayList<Integer> keys = KeyBindings.getKeysForAction(action);

			if (keys.size() >= 1){
				key1Name = PixelScene.renderTextBlock( KeyBindings.getKeyName(keys.get(0)), 6 );
				key1 = keys.get(0);
			} else {
				key1Name = PixelScene.renderTextBlock( Messages.get(WndKeyBindings.class, "unbound"), 6 );
				key1Name.hardlight(UNBOUND);
				key1 = 0;
			}
			add(key1Name);

			if (keys.size() >= 2){
				key2Name = PixelScene.renderTextBlock( KeyBindings.getKeyName(keys.get(1)), 6 );
				key2 = keys.get(1);
			} else {
				key2Name = PixelScene.renderTextBlock( Messages.get(WndKeyBindings.class, "unbound"), 6 );
				key2Name.hardlight(UNBOUND);
				key2 = 0;
			}
			add(key2Name);

			sep1 = new ColorBlock(1, 1, 0xFF222222);
			add(sep1);

			sep2 = new ColorBlock(1, 1, 0xFF222222);
			add(sep2);

			sep3 = new ColorBlock(1, 1, 0xFF222222);
			add(sep3);
		}

		@Override
		protected void layout() {
			super.layout();

			actionName.setPos( x, y + (height() - actionName.height())/2);
			key1Name.setPos(x + width()/2 + 2, y + (height() - key1Name.height())/2);
			key2Name.setPos(x + 3*width()/4 + 2, y + (height() - key2Name.height())/2);

			sep1.size(width, 1);
			sep1.x = x;
			sep1.y = bottom();

			sep2.size(1, height);
			sep2.x = key1Name.left()-2;
			sep2.y = y;

			sep3.size(1, height);
			sep3.x = key2Name.left()-2;
			sep3.y = y;
		}

		//onclick opens a new window depending on whether you pressed key 1 or 2.
		//give you the option to change that binding by pressing a key, then lets your confirm/cancel/unbind
		//warns you if a new binding conflicts with an old one
	}
}
