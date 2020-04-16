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

import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndKeyBindings extends Window {

	private static final int WIDTH = 120;

	private static final int BTN_HEIGHT = 16;

	private static final int COL1_CENTER = WIDTH/4;
	private static final int COL2_CENTER = 5*WIDTH/8;
	private static final int COL3_CENTER = 7*WIDTH/8;

	private Component bindingsList;
	private ArrayList<BindingItem> listItems = new ArrayList<>();

	private LinkedHashMap<Integer, GameAction> changedBindings;

	public WndKeyBindings() {

		changedBindings = KeyBindings.getAllBindings();

		RenderedTextBlock ttlAction = PixelScene.renderTextBlock(Messages.get(this, "ttl_action"), 9);
		ttlAction.setPos( COL1_CENTER - ttlAction.width()/2, (BTN_HEIGHT - ttlAction.height())/2);
		add(ttlAction);

		ColorBlock ttlSep1 = new ColorBlock(1, BTN_HEIGHT, 0xFF222222);
		ttlSep1.x = WIDTH/2;
		add(ttlSep1);

		RenderedTextBlock ttlKey1 = PixelScene.renderTextBlock(Messages.get(this, "ttl_key1"), 9);
		ttlKey1.setPos(COL2_CENTER - ttlKey1.width()/2, (BTN_HEIGHT - ttlKey1.height())/2);
		add(ttlKey1);

		ColorBlock ttlSep2 = new ColorBlock(1, BTN_HEIGHT, 0xFF222222);
		ttlSep2.x = 3*WIDTH/4;
		add(ttlSep2);

		RenderedTextBlock ttlKey2 = PixelScene.renderTextBlock(Messages.get(this, "ttl_key2"), 9);
		ttlKey2.setPos(COL3_CENTER - ttlKey2.width()/2, (BTN_HEIGHT - ttlKey2.height())/2);
		add(ttlKey2);

		ColorBlock ttlSep3 = new ColorBlock(WIDTH, 1, 0xFF222222);
		ttlSep3.y = BTN_HEIGHT;
		add(ttlSep3);

		bindingsList = new Component();

		ScrollPane scrollingList = new ScrollPane(bindingsList){
			@Override
			public void onClick(float x, float y) {
				for (BindingItem i : listItems) {
					if (i.onClick( x, y )) {
						break;
					}
				}
			}
		};

		add(scrollingList);

		int y = 0;
		for (GameAction action : GameAction.allActions()){
			//start at 1. No bindings for NONE
			if (action.code() < 1) continue;

			BindingItem item = new BindingItem(action);
			item.setRect(0, y, WIDTH, BindingItem.HEIGHT);
			bindingsList.add(item);
			listItems.add(item);
			y += item.height();
		}
		bindingsList.setSize(WIDTH, y+1);

		resize(WIDTH, Math.min(BTN_HEIGHT *3 + 3 + BindingItem.HEIGHT*listItems.size(), PixelScene.uiCamera.height-20));

		RedButton btnDefaults = new RedButton(Messages.get(this, "default"), 9){
			@Override
			protected void onClick() {
				changedBindings = SPDAction.getDefaults();
				for (BindingItem i : listItems){
					int key1 = 0;
					int key2 = 0;
					for( int k : changedBindings.keySet()){
						if (changedBindings.get(k) == i.gameAction){
							if (key1 == 0) key1 = k;
							else           key2 = k;
						}
					}
					i.updateBindings(key1, key2);
				}
			}
		};
		btnDefaults.setRect(0, height - BTN_HEIGHT *2 -1, WIDTH, BTN_HEIGHT);
		add(btnDefaults);

		RedButton btnConfirm = new RedButton(Messages.get(this, "confirm"), 9){
			@Override
			protected void onClick() {
				KeyBindings.setAllBindings(changedBindings);
				SPDAction.saveBindings();
				hide();
			}
		};
		btnConfirm.setRect(0, height - BTN_HEIGHT, WIDTH/2, BTN_HEIGHT);
		add(btnConfirm);

		RedButton btnCancel = new RedButton(Messages.get(this, "cancel"), 9){
			@Override
			protected void onClick() {
				hide(); //close and don't save
			}
		};
		btnCancel.setRect(WIDTH/2 + 1, height - BTN_HEIGHT, WIDTH/2 - 1, BTN_HEIGHT);
		add(btnCancel);

		scrollingList.setRect(0, BTN_HEIGHT +1, WIDTH, btnDefaults.top()- BTN_HEIGHT - 1);

	}

	@Override
	public void onBackPressed() {
		//do nothing, avoids accidental back presses which would lose progress.
	}

	private class BindingItem extends Component {

		private static final int HEIGHT = 12;

		private static final int CHANGED = TITLE_COLOR;
		private static final int DEFAULT = 0xFFFFFF;
		private static final int UNBOUND = 0x888888;
		private static final int UNBOUND_CHANGED = 0x888822;

		private GameAction gameAction;
		private int key1;
		private int key2;

		private int origKey1;
		private int origKey2;

		private RenderedTextBlock actionName;
		private RenderedTextBlock key1Name;
		private RenderedTextBlock key2Name;

		private ColorBlock sep1;
		private ColorBlock sep2;
		private ColorBlock sep3;

		public BindingItem( GameAction action ){
			gameAction = action;

			actionName = PixelScene.renderTextBlock(Messages.get(WndKeyBindings.class, action.name()), 6 );
			actionName.setHightlighting(false);
			add(actionName);

			ArrayList<Integer> keys = KeyBindings.getBoundKeysForAction(action);
			origKey1 = key1 = keys.isEmpty() ? 0 : keys.remove(0);
			origKey2 = key2 = keys.isEmpty() ? 0 : keys.remove(0);

			key1Name = PixelScene.renderTextBlock( KeyBindings.getKeyName(key1), 6 );
			if (key1 == 0) key1Name.hardlight(UNBOUND);
			add(key1Name);

			key2Name = PixelScene.renderTextBlock( KeyBindings.getKeyName(key2), 6 );
			if (key2 == 0) key2Name.hardlight(UNBOUND);
			add(key2Name);

			sep1 = new ColorBlock(1, 1, 0xFF222222);
			add(sep1);

			sep2 = new ColorBlock(1, 1, 0xFF222222);
			add(sep2);

			sep3 = new ColorBlock(1, 1, 0xFF222222);
			add(sep3);
		}

		public void updateBindings(int first, int second){
			if (first == 0){
				key1 = second;
				key2 = first;
			} else {
				key1 = first;
				key2 = second;
			}

			key1Name.text(KeyBindings.getKeyName(key1));
			if (key1 != origKey1) key1Name.hardlight( key1 == 0 ? UNBOUND_CHANGED : CHANGED);
			else                  key1Name.hardlight( key1 == 0 ? UNBOUND : DEFAULT);

			key2Name.text(KeyBindings.getKeyName(key2));
			if (key2 != origKey2) key2Name.hardlight( key2 == 0 ? UNBOUND_CHANGED : CHANGED);
			else                  key2Name.hardlight( key2 == 0 ? UNBOUND : DEFAULT);

			layout();
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

		private boolean onClick( float x, float y ){
			if (inside(x, y)){
				//assigning second key
				if (x >= this.x + 3*width()/4 && key1 != 0) {
					ShatteredPixelDungeon.scene().addToFront( new WndChangeBinding(gameAction, this, false, key2, key1));

				//assigning first key
				} else if (x >= this.x + width()/2){
					ShatteredPixelDungeon.scene().addToFront( new WndChangeBinding(gameAction, this, true, key1, key2));

				}
				return true;

			} else {
				return false;
			}
		}

	}

	private class WndChangeBinding extends Window {

		private int curKeyCode;
		private int otherBoundKey;
		private int changedKeyCode = -1;

		private BindingItem changedAction;
		private RenderedTextBlock changedKey;
		private RenderedTextBlock warnErr;

		private RedButton btnConfirm;

		public WndChangeBinding(GameAction action, BindingItem listItem, boolean firstKey, int curKeyCode, int otherBoundKey ){

			this.curKeyCode = curKeyCode;
			this.otherBoundKey = otherBoundKey;

			RenderedTextBlock desc = PixelScene.renderTextBlock( Messages.get(this, firstKey ? "desc_first" : "desc_second",
						Messages.get(WndKeyBindings.class, action.name()),
						KeyBindings.getKeyName(curKeyCode)), 6 );
			desc.maxWidth(WIDTH);
			desc.setRect(0, 0, WIDTH, desc.height());
			add(desc);

			RenderedTextBlock curBind;
			curBind = PixelScene.renderTextBlock(Messages.get(this, "desc_current", KeyBindings.getKeyName(curKeyCode)), 6);
			curBind.maxWidth(WIDTH);
			curBind.setRect((WIDTH - curBind.width())/2, desc.bottom()+6, WIDTH, curBind.height());
			add(curBind);

			changedKey = PixelScene.renderTextBlock(6);
			changedKey.maxWidth(WIDTH);
			changedKey.setRect(0, curBind.bottom()+2, WIDTH, changedKey.height());
			add(changedKey);

			warnErr = PixelScene.renderTextBlock(6);
			warnErr.maxWidth(WIDTH);
			warnErr.setRect(0, changedKey.bottom() + 10, WIDTH, warnErr.height());
			add(warnErr);

			RedButton btnUnbind = new RedButton(Messages.get(this, "unbind"), 9){
				@Override
				protected void onClick() {
					onSignal(new KeyEvent(0, true));
				}
			};
			btnUnbind.setRect(0, warnErr.bottom() + 6, WIDTH, BTN_HEIGHT);
			add(btnUnbind);

			btnConfirm = new RedButton(Messages.get(this, "confirm"), 9){
				@Override
				protected void onClick() {
					if (changedKeyCode != -1){

						changedBindings.remove(changedKeyCode);
						changedBindings.remove(listItem.key1);
						changedBindings.remove(listItem.key2);

						if (firstKey){
							if (changedKeyCode != 0) changedBindings.put(changedKeyCode, action);
							if (listItem.key2 != 0) changedBindings.put(listItem.key2, action);
							listItem.updateBindings(changedKeyCode, listItem.key2);
						} else {
							if (listItem.key1 != 0) changedBindings.put(listItem.key1, action);
							if (changedKeyCode != 0) changedBindings.put(changedKeyCode, action);
							listItem.updateBindings(listItem.key1, changedKeyCode);
						}

						if (changedAction != null){
							if (changedAction.key1 != changedKeyCode){
								changedAction.updateBindings(changedAction.key1, 0);
							} else if (changedAction.key2 != changedKeyCode){
								changedAction.updateBindings(changedAction.key2, 0);
							} else {
								changedAction.updateBindings(0, 0);
							}
						}
					}

					hide();
				}
			};
			btnConfirm.setRect(0, btnUnbind.bottom()+1, WIDTH/2, BTN_HEIGHT);
			btnConfirm.enable(false);
			add(btnConfirm);

			RedButton btnCancel = new RedButton(Messages.get(this, "cancel"), 9){
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(btnConfirm.right()+1, btnUnbind.bottom()+1, WIDTH/2-1, BTN_HEIGHT);
			add(btnCancel);

			resize(WIDTH, (int)btnCancel.bottom());
			KeyBindings.acceptUnbound = true;

		}

		@Override
		public boolean onSignal(KeyEvent event) {
			if (event.pressed){
				changedKey.text(Messages.get(this, "changed_bind", KeyBindings.getKeyName(event.code)));
				changedKey.setPos((WIDTH - changedKey.width())/2, changedKey.top());

				changedKeyCode = event.code;
				changedAction = null;

				if (event.code != 0 && (event.code == curKeyCode || event.code == otherBoundKey)){
					warnErr.text(Messages.get(this, "error"));
					warnErr.hardlight(CharSprite.NEGATIVE);
					btnConfirm.enable(false);

				} else if (event.code != 0 && changedBindings.get(changedKeyCode) != null){
					for (BindingItem i : listItems) {
						if (i.gameAction == changedBindings.get(changedKeyCode)) {
							changedAction = i;
							break;
						}
					}
					warnErr.text(Messages.get(this, "warning", Messages.get(WndKeyBindings.class, changedBindings.get(changedKeyCode).name() )));
					warnErr.hardlight(CharSprite.WARNING);
					btnConfirm.enable(true);

				} else {
					warnErr.text(" ");
					btnConfirm.enable(true);
				}
			}

			return true;
		}

		@Override
		public void destroy() {
			super.destroy();
			KeyBindings.acceptUnbound = false;
		}

	}
}
