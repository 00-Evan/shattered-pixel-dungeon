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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Constants;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.GameAction;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;

	private PickedUpItem pickedUp;

	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}

	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}

	@Override
	protected void createChildren() {

		btnQuick = new QuickslotTool[Constants.MAX_QUICKSLOTS];

		for (int i = 0; i < btnQuick.length; i++) {
			btnQuick[i] = new QuickslotTool(64, 0, 22, 24, i);
			if (i < SPDSettings.quickslots()) {
				add(btnQuick[i]);
			}
		}

		add(btnWait = new Toolbar.Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				examining = false;
				Dungeon.hero.rest(false);
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.WAIT;
			}

			protected boolean onLongClick() {
				examining = false;
				Dungeon.hero.rest(true);
				return true;
			}
		});

		add(new Button(){
			@Override
			protected void onClick() {
				examining = false;
				Dungeon.hero.rest(true);
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.REST;
			}
		});

		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (!examining) {
					GameScene.selectCell(informer);
					examining = true;
				} else {
					informer.onSelect(null);
					Dungeon.hero.search(true);
				}
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.SEARCH;
			}

			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});



		add(btnInventory = new Tool(0, 0, 24, 26) {
			private GoldIndicator gold;

			@Override
			protected void onClick() {
				GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null));
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.INVENTORY;
			}

			@Override
			protected boolean onLongClick() {
				WndJournal.last_index = 3; //catalog page
				GameScene.show(new WndJournal());
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add(gold);
			}

			@Override
			protected void layout() {
				super.layout();
				gold.fill(this);
			}
		});

		add(pickedUp = new PickedUpItem());
	}

	@Override
	protected void layout() {
		final int maxHorizontalQuickslots = PixelScene.landscape() ? 8 : 4;

		for (int i = 0; i < btnQuick.length; i++) {
			if (i < SPDSettings.quickslots()) {
				if (btnQuick[i] == null) btnQuick[i] = new QuickslotTool(64, 0, 24, 24, i);
				add(btnQuick[i]);
			} else {
				remove(btnQuick[i]);
			}
		}

		for(int i = 0; i < Constants.MAX_QUICKSLOTS; i++) {
			//FIXME doesn't work for portrait mode and no longer dynamically resizes.
			if (i == 0 && !SPDSettings.flipToolbar() ||
					i == Math.min(SPDSettings.quickslots(), maxHorizontalQuickslots)-1 && SPDSettings.flipToolbar()) {
				btnQuick[i].border(0, 2);
				btnQuick[i].frame(106, 0, 19, 24);
			} else if (i == 0 && SPDSettings.flipToolbar() ||
					i == Math.min(SPDSettings.quickslots(), maxHorizontalQuickslots)-1 && !SPDSettings.flipToolbar()) {
				btnQuick[i].border(2, 1);
				btnQuick[i].frame(86, 0, 20, 24);
			} else {
				btnQuick[i].border(2, 2);
				btnQuick[i].frame(64, 0, 22, 24);
			}

		}

		float right = width;
		float startX, startY;
		switch(Mode.valueOf(SPDSettings.toolbarMode())){
			case SPLIT:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				startX = btnInventory.left() - btnQuick[0].width();
				for (int i = 0; i < maxHorizontalQuickslots; i++) {
					QuickslotTool tool = btnQuick[i];
					tool.setPos(startX, y);
					if (i + 1 < btnQuick.length) {
						startX = btnQuick[i].left() - btnQuick[i+1].width();
					}
				}

				startY = 40;
				for (int i = maxHorizontalQuickslots; i < btnQuick.length; i++) {
					QuickslotTool tool = btnQuick[i];
					tool.setPos(width - (tool.width() + 2), startY);
					if (i + 1 < btnQuick.length) {
						startY = btnQuick[i].bottom();
					}
				}

				//center the quickslots if they
				if (btnQuick[btnQuick.length-1].left() < btnSearch.right()){
					float diff = Math.round(btnSearch.right() - btnQuick[btnQuick.length-1].left())/2;
					for( int i = 0; i < Constants.MAX_QUICKSLOTS; i++){
						btnQuick[i].setPos( btnQuick[i].left()+diff, btnQuick[i].top() );
					}
				}
				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				float toolbarWidth = btnWait.width() + btnSearch.width() + btnInventory.width();
				for(Button slot : btnQuick){
					if (slot.visible) toolbarWidth += slot.width();
				}
				right = (width + toolbarWidth)/2;

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);

				startX = btnInventory.left() - btnQuick[0].width();
				for (int i = 0; i < btnQuick.length; i++) {
					QuickslotTool tool = btnQuick[i];
					tool.setPos(startX, y + 2);
					if (i + 1 < btnQuick.length) {
						startX = btnQuick[i].left() - btnQuick[i+1].width();
					}
				}

				startY = 40;
				for (int i = maxHorizontalQuickslots; i < btnQuick.length; i++) {
					QuickslotTool tool = btnQuick[i];
					tool.setPos(width - (tool.width() + 2), startY);
					if (i + 1 < btnQuick.length) {
						startY = btnQuick[i].bottom();
					}
				}

				if (btnQuick[btnQuick.length-1].left() < 0) {
					float diff = -Math.round(btnQuick[3].left())/2;
					for (QuickslotTool quickslotTool : btnQuick) {
						quickslotTool.setPos(quickslotTool.left() + diff, quickslotTool.top());
					}
				}
				break;
		}
		right = width;

		if (SPDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnSearch.setPos( (right - btnSearch.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);

			for (QuickslotTool tool : btnQuick) {
				tool.setPos( right - tool.right(), y+2);
			}
		}
	}

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}

	@Override
	public void update() {
		super.update();

		if (lastEnabled != (Dungeon.hero.ready && Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready && Dungeon.hero.isAlive());

			for (Gizmo tool : members.toArray(new Gizmo[0])) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}

		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}

	public void pickup(Item item, int cell ) {
		pickedUp.reset( item,
				cell,
				btnInventory.centerX(),
				btnInventory.centerY());
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			instance.examining = false;
			GameScene.examineCell( cell );
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};

	private static class Tool extends Button {

		private static final int BGCOLOR = 0x7B8073;

		private Image base;

		public Tool( int x, int y, int width, int height ) {
			super();

			hotArea.blockWhenInactive = true;
			frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			base = new Image( Assets.Interfaces.TOOLBAR );
			add( base );
		}

		@Override
		public void update() {
			super.update();
			if (SPDSettings.ClassUI()) {
				base.texture = TextureCache.get(Assets.Interfaces.TOOLBARDRAK);
			} else {
				base.texture = TextureCache.get(Assets.Interfaces.TOOLBAR);
			}
		}

		@Override
		protected void layout() {
			super.layout();

			base.x = x;
			base.y = y;
		}

		@Override
		protected void onPointerDown() {
			base.brightness( 1.4f );
		}

		@Override
		protected void onPointerUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}

		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}

	private static class QuickslotTool extends Tool {

		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;

		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}

		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + borderLeft, y + 2, width - borderLeft-borderRight, height - 4 );
		}

		@Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
	}

	public static class PickedUpItem extends ItemSprite {

		private static final float DURATION = 0.5f;

		private float startScale;
		private float startX, startY;
		private float endX, endY;
		private float left;

		public PickedUpItem() {
			super();

			originToCenter();

			active =
					visible =
							false;
		}

		public void reset(Item item, int cell, float endX, float endY ) {
			view( item );

			active =
					visible =
							true;

			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);

			x = this.startX = start.x - ItemSprite.SIZE / 2;
			y = this.startY = start.y - ItemSprite.SIZE / 2;

			this.endX = endX - ItemSprite.SIZE / 2;
			this.endY = endY - ItemSprite.SIZE / 2;
			left = DURATION;

			scale.set( startScale = Camera.main.zoom / camera().zoom );

		}

		@Override
		public void update() {
			super.update();

			if ((left -= Game.elapsed) <= 0) {

				visible =
						active =
								false;
				killEmitters();

			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );

				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}
