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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuickBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.input.ControllerHandler;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;
	private SlotSwapTool btnSwap;
	
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
	public synchronized void destroy() {
		super.destroy();
		if (instance == this) instance = null;
	}

	@Override
	protected void createChildren() {

		add(btnSwap = new SlotSwapTool(128, 0, 21, 23));

		btnQuick = new QuickslotTool[QuickSlot.SIZE];
		for (int i = 0; i < btnQuick.length; i++){
			add( btnQuick[i] = new QuickslotTool(64, 0, 22, 24, i) );
		}

		//hidden button for quickslot selector keybind
		add(new Button(){
			@Override
			protected void onClick() {
				if (QuickSlotButton.targetingSlot != -1){
					int cell = QuickSlotButton.autoAim(QuickSlotButton.lastTarget, Dungeon.quickslot.getItem(QuickSlotButton.targetingSlot));

					if (cell != -1){
						GameScene.handleCell(cell);
					} else {
						//couldn't auto-aim, just target the position and hope for the best.
						GameScene.handleCell( QuickSlotButton.lastTarget.pos );
					}
					return;
				}

				if (Dungeon.hero.ready && !GameScene.cancel()) {

					String[] slotNames = new String[6];
					Image[] slotIcons = new Image[6];
					for (int i = 0; i < 6; i++){
						Item item = Dungeon.quickslot.getItem(i);

						if (item != null && !Dungeon.quickslot.isPlaceholder(i) &&
								(Dungeon.hero.buff(LostInventory.class) == null || item.keptThoughLostInvent)){
							slotNames[i] = Messages.titleCase(item.name());
							slotIcons[i] = new ItemSprite(item);
						} else {
							slotNames[i] = Messages.get(Toolbar.class, "quickslot_assign");
							slotIcons[i] = new ItemSprite(ItemSpriteSheet.SOMETHING);
						}
					}

					String info = "";
					if (ControllerHandler.controllerActive){
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_select") + "\n";
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.RIGHT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_assign") + "\n";
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "quickslot_cancel");
					} else {
						info += Messages.get(WndKeyBindings.class, SPDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "quickslot_select") + "\n";
						info += Messages.get(WndKeyBindings.class, SPDAction.RIGHT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "quickslot_assign") + "\n";
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "quickslot_cancel");
					}

					Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "quickslot_prompt"), info, slotNames, slotIcons) {
						@Override
						public void onSelect(int idx, boolean alt) {
							Item item = Dungeon.quickslot.getItem(idx);

							if (item == null || Dungeon.quickslot.isPlaceholder(idx)
									|| (Dungeon.hero.buff(LostInventory.class) != null && !item.keptThoughLostInvent)
									|| alt){
								//TODO would be nice to use a radial menu for this too
								// Also a bunch of code could be moved out of here into subclasses of RadialMenu
								GameScene.selectItem(new WndBag.ItemSelector() {
									@Override
									public String textPrompt() {
										return Messages.get(QuickSlotButton.class, "select_item");
									}

									@Override
									public boolean itemSelectable(Item item) {
										return item.defaultAction != null;
									}

									@Override
									public void onSelect(Item item) {
										if (item != null) {
											Dungeon.quickslot.setSlot( idx , item );
											QuickSlotButton.refresh();
										}
									}
								});
							} else {

								item.execute(Dungeon.hero);
								if (item.usesTargeting) {
									QuickSlotButton.useTargeting(idx);
								}
							}
							super.onSelect(idx, alt);
						}
					});
				}
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return SPDAction.QUICKSLOT_SELECTOR;
				else				return null;
			}
		});
		
		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(false);
				}
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.WAIT;
			}

			@Override
			public GameAction secondaryTooltipAction() {
				return SPDAction.WAIT_OR_PICKUP;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "wait"));
			}

			protected boolean onLongClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
				return true;
			}
		});

		//hidden button for rest keybind
		add(new Button(){
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return SPDAction.REST;
				else				return null;
			}
		});

		//hidden button for wait / pickup keybind
		add(new Button(){
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					Dungeon.hero.waitOrPickup = true;
					if ((Dungeon.level.heaps.get(Dungeon.hero.pos) != null || Dungeon.hero.isStandingOnTrampleableGrass())
						&& Dungeon.hero.handle(Dungeon.hero.pos)){
						//trigger hold fast here, even if the hero didn't specifically wait
						if (Dungeon.hero.hasTalent(Talent.HOLD_FAST)){
							Buff.affect(Dungeon.hero, HoldFast.class);
						}
						Dungeon.hero.next();
					} else {
						examining = false;
						Dungeon.hero.rest(false);
					}
				}
			}

			protected boolean onLongClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
				return true;
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return SPDAction.WAIT_OR_PICKUP;
				else				return null;
			}
		});
		
		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready) {
					if (!examining && !GameScene.cancel()) {
						GameScene.selectCell(informer);
						examining = true;
					} else if (examining) {
						informer.onSelect(null);
						Dungeon.hero.search(true);
					}
				}
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.EXAMINE;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "examine"));
			}
			
			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});
		
		add(btnInventory = new Tool(0, 0, 24, 26) {
			private CurrencyIndicator ind;

			private Image arrow;

			@Override
			protected void onClick() {
				if (Dungeon.hero.ready || !Dungeon.hero.isAlive()) {
					if (SPDSettings.interfaceSize() == 2) {
						GameScene.toggleInvPane();
					} else {
						if (!GameScene.cancel()) {
							GameScene.show(new WndBag(Dungeon.hero.belongings.backpack));
						}
					}
				}
			}
			
			@Override
			public GameAction keyAction() {
				return SPDAction.INVENTORY;
			}

			@Override
			public GameAction secondaryTooltipAction() {
				return SPDAction.INVENTORY_SELECTOR;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "inventory"));
			}
			
			@Override
			protected boolean onLongClick() {
				GameScene.show(new WndQuickBag(null));
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				arrow = Icons.get(Icons.COMPASS);
				arrow.originToCenter();
				arrow.visible = SPDSettings.interfaceSize() == 2;
				arrow.tint(0x3D2E18, 1f);
				add(arrow);

				ind = new CurrencyIndicator();
				add(ind);
			}

			@Override
			protected void layout() {
				super.layout();
				ind.fill(this);

				arrow.x = left() + (width - arrow.width())/2;
				arrow.y = bottom()-arrow.height-1;
				arrow.angle = bottom() == camera().height ? 0 : 180;
			}
		});

		//hidden button for inventory selector keybind
		add(new Button(){
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					ArrayList<Bag> bags = Dungeon.hero.belongings.getBags();
					String[] names = new String[bags.size()];
					Image[] images = new Image[bags.size()];
					for (int i = 0; i < bags.size(); i++){
						names[i] = Messages.titleCase(bags.get(i).name());
						images[i] = new ItemSprite(bags.get(i));
					}
					String info = "";
					if (ControllerHandler.controllerActive){
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "container_select") + "\n";
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "container_cancel");
					} else {
						info += Messages.get(WndKeyBindings.class, SPDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "container_select") + "\n";
						info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "container_cancel");
					}

					Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "container_prompt"), info, names, images){
						@Override
						public void onSelect(int idx, boolean alt) {
							super.onSelect(idx, alt);
							Bag bag = bags.get(idx);
							ArrayList<Item> items = (ArrayList<Item>) bag.items.clone();

							for(Item i : bag.items){
								if (i instanceof Bag) items.remove(i);
								if (Dungeon.hero.buff(LostInventory.class) != null && !i.keptThoughLostInvent) items.remove(i);
							}

							if (idx == 0){
								Belongings b = Dungeon.hero.belongings;
								if (b.ring() != null) items.add(0, b.ring());
								if (b.misc() != null) items.add(0, b.misc());
								if (b.artifact() != null) items.add(0, b.artifact());
								if (b.armor() != null) items.add(0, b.armor());
								if (b.weapon() != null) items.add(0, b.weapon());
							}

							if (items.size() == 0){
								GameScene.show(new WndMessage(Messages.get(Toolbar.class, "container_empty")));
								return;
							}

							String[] itemNames = new String[items.size()];
							Image[] itemIcons = new Image[items.size()];
							for (int i = 0; i < items.size(); i++){
								itemNames[i] = Messages.titleCase(items.get(i).name());
								itemIcons[i] = new ItemSprite(items.get(i));
							}

							String info = "";
							if (ControllerHandler.controllerActive){
								info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.LEFT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "item_select") + "\n";
								info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.RIGHT_CLICK, true)) + ": " + Messages.get(Toolbar.class, "item_use") + "\n";
								info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, true)) + ": " + Messages.get(Toolbar.class, "item_cancel");
							} else {
								info += Messages.get(WndKeyBindings.class, SPDAction.LEFT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "item_select") + "\n";
								info += Messages.get(WndKeyBindings.class, SPDAction.RIGHT_CLICK.name()) + ": " + Messages.get(Toolbar.class, "item_use") + "\n";
								info += KeyBindings.getKeyName(KeyBindings.getFirstKeyForAction(GameAction.BACK, false)) + ": " + Messages.get(Toolbar.class, "item_cancel");
							}

							Game.scene().addToFront(new RadialMenu(Messages.get(Toolbar.class, "item_prompt"), info, itemNames, itemIcons){
								@Override
								public void onSelect(int idx, boolean alt) {
									super.onSelect(idx, alt);
									Item item = items.get(idx);
									if (alt && item.defaultAction != null) {
										item.execute(Dungeon.hero);
									} else {
										Game.scene().addToFront(new WndUseItem(null, item));
									}
								}
							});
						}
					});
				}
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return SPDAction.INVENTORY_SELECTOR;
				else				return null;
			}
		});

		add(pickedUp = new PickedUpItem());
	}
	
	@Override
	protected void layout() {

		float right = width;

		int quickslotsToShow = 4;
		if (PixelScene.uiCamera.width > 152) quickslotsToShow ++;
		if (PixelScene.uiCamera.width > 170) quickslotsToShow ++;

		int startingSlot;
		if (SPDSettings.quickSwapper() && quickslotsToShow < 6){
			quickslotsToShow = 3;
			startingSlot = swappedQuickslots ? 3 : 0;
			btnSwap.visible = true;
			btnSwap.active = lastEnabled;
		} else {
			startingSlot = 0;
			btnSwap.visible = btnSwap.active = false;
			btnSwap.setPos(0, PixelScene.uiCamera.height);
		}
		int endingSlot = startingSlot+quickslotsToShow-1;

		for (int i = 0; i < btnQuick.length; i++){
			btnQuick[i].visible = i >= startingSlot && i <= endingSlot;
			btnQuick[i].enable(btnQuick[i].visible && lastEnabled);
			if (i < startingSlot || i > endingSlot){
				btnQuick[i].setPos(btnQuick[i].left(), PixelScene.uiCamera.height);
			}
		}

		if (SPDSettings.interfaceSize() > 0){
			btnInventory.setPos(right - btnInventory.width(), y);
			btnWait.setPos(btnInventory.left() - btnWait.width(), y);
			btnSearch.setPos(btnWait.left() - btnSearch.width(), y);

			right = btnSearch.left();
			for(int i = endingSlot; i >= startingSlot; i--) {
				if (i == endingSlot){
					btnQuick[i].border(0, 2);
					btnQuick[i].frame(106, 0, 19, 24);
				} else if (i == 0){
					btnQuick[i].border(2, 1);
					btnQuick[i].frame(86, 0, 20, 24);
				} else {
					btnQuick[i].border(0, 1);
					btnQuick[i].frame(88, 0, 18, 24);
				}
				btnQuick[i].setPos(right-btnQuick[i].width(), y+2);
				right = btnQuick[i].left();
			}

			//swap button never appears on larger interface sizes

			return;
		}

		for(int i = startingSlot; i <= endingSlot; i++) {
			if (i == startingSlot && !SPDSettings.flipToolbar() ||
				i == endingSlot && SPDSettings.flipToolbar()){
				btnQuick[i].border(0, 2);
				btnQuick[i].frame(106, 0, 19, 24);
			} else if (i == startingSlot && SPDSettings.flipToolbar() ||
					i == endingSlot && !SPDSettings.flipToolbar()){
				btnQuick[i].border(2, 1);
				btnQuick[i].frame(86, 0, 20, 24);
			} else {
				btnQuick[i].border(0, 1);
				btnQuick[i].frame(88, 0, 18, 24);
			}
		}

		float shift = 0;
		switch(Mode.valueOf(SPDSettings.toolbarMode())){
			case SPLIT:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				float left = 0;

				btnQuick[startingSlot].setPos(btnInventory.left() - btnQuick[startingSlot].width(), y + 2);
				for (int i = startingSlot+1; i <= endingSlot; i++) {
					btnQuick[i].setPos(btnQuick[i-1].left() - btnQuick[i].width(), y + 2);
					shift = btnSearch.right() - btnQuick[i].left();
				}

				if (btnSwap.visible){
					btnSwap.setPos(btnQuick[endingSlot].left() - (btnSwap.width()-2), y+3);
					shift = btnSearch.right() - btnSwap.left();
				}

				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				float toolbarWidth = btnWait.width() + btnSearch.width() + btnInventory.width();
				for(Button slot : btnQuick){
					if (slot.visible) toolbarWidth += slot.width();
				}
				if (btnSwap.visible) toolbarWidth += btnSwap.width()-2;
				right = (width + toolbarWidth)/2;

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
				btnInventory.setPos(btnSearch.left() - btnInventory.width(), y);

				btnQuick[startingSlot].setPos(btnInventory.left() - btnQuick[startingSlot].width(), y + 2);
				for (int i = startingSlot+1; i <= endingSlot; i++) {
					btnQuick[i].setPos(btnQuick[i-1].left() - btnQuick[i].width(), y + 2);
					shift = -btnQuick[i].left();
				}

				if (btnSwap.visible){
					btnSwap.setPos(btnQuick[endingSlot].left() - (btnSwap.width()-2), y+3);
					shift = -btnSwap.left();
				}
				
				break;
		}

		if (shift > 0){
			shift /= 2; //we want to center;
			for (int i = startingSlot; i <= endingSlot; i++) {
				btnQuick[i].setPos(btnQuick[i].left()+shift,  btnQuick[i].top());
			}
			if (btnSwap.visible){
				btnSwap.setPos(btnSwap.left()+shift, btnSwap.top());
			}
		}

		right = width;

		if (SPDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnSearch.setPos( (right - btnSearch.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);

			for(int i = startingSlot; i <= endingSlot; i++) {
				btnQuick[i].setPos( right - btnQuick[i].right(), y+2);
			}

			if (btnSwap.visible){
				btnSwap.setPos( right - btnSwap.right(), y+3);
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

	public void alpha( float value ){
		btnWait.alpha( value );
		btnSearch.alpha( value );
		btnInventory.alpha( value );
		for (QuickslotTool tool : btnQuick){
			tool.alpha(value);
		}
		btnSwap.alpha( value );
	}

	public void pickup( Item item, int cell ) {
		pickedUp.reset( item,
			cell,
			btnInventory.centerX(),
			btnInventory.centerY());
	}
	
	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (instance != null) {
				instance.examining = false;
				GameScene.examineCell(cell);
			}
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

			hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
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
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}

		public void alpha( float value ){
			base.alpha(value);
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
			slot.setRect( x, y, width, height );
			slot.slotMargins(borderLeft, 2, borderRight, 2);
		}

		@Override
		public void alpha(float value) {
			super.alpha(value);
			slot.alpha(value);
		}

		@Override
		public void enable( boolean value ) {
			super.enable( value && visible );
			slot.enable( value && visible );
		}
	}

	public static boolean swappedQuickslots = false;
	public static SlotSwapTool SWAP_INSTANCE;

	public static class SlotSwapTool extends Tool {

		private Image[] icons = new Image[4];
		private Item[] items = new Item[4];

		public SlotSwapTool(int x, int y, int width, int height) {
			super(x, y, width, height);
			SWAP_INSTANCE = this;
			updateVisuals();
		}

		@Override
		public synchronized void destroy() {
			super.destroy();
			if (SWAP_INSTANCE == this) SWAP_INSTANCE = null;
		}

		@Override
		protected void onClick() {
			super.onClick();
			swappedQuickslots = !swappedQuickslots;
			updateLayout();
			updateVisuals();
		}

		public void updateVisuals(){
			if (icons[0] == null){
				icons[0] = Icons.get(Icons.CHANGES);
				icons[0].scale.set(PixelScene.align(0.45f));
				add(icons[0]);
			}

			int slot;
			int slotDir;
			if (SPDSettings.flipToolbar()){
				slot = swappedQuickslots ? 0 : 3;
				slotDir = +1;
			} else {
				slot = swappedQuickslots ? 2 : 5;
				slotDir = -1;
			}

			for (int i = 1; i < 4; i++){
				if (items[i] == Dungeon.quickslot.getItem(slot)){
					slot += slotDir;
					continue;
				} else {
					items[i] = Dungeon.quickslot.getItem(slot);
				}
				if (icons[i] != null){
					icons[i].killAndErase();
					icons[i] = null;
				}
				if (items[i] != null){
					icons[i] = new ItemSprite(items[i]);
					icons[i].scale.set(PixelScene.align(0.45f));
					if (Dungeon.quickslot.isPlaceholder(slot)) icons[i].alpha(0.29f);
					add(icons[i]);
				}
				slot += slotDir;
			}

			icons[0].x = x + 2 + (8 - icons[0].width())/2;
			icons[0].y = y + 2 + (9 - icons[0].height())/2;
			PixelScene.align(icons[0]);

			if (icons[1] != null){
				icons[1].x = x + 11 + (8 - icons[1].width())/2;
				icons[1].y = y + 2 + (9 - icons[1].height())/2;
				PixelScene.align(icons[1]);
			}

			if (icons[2] != null){
				icons[2].x = x + 2 + (8 - icons[2].width())/2;
				icons[2].y = y + 12 + (9 - icons[2].height())/2;
				PixelScene.align(icons[2]);
			}

			if (icons[3] != null){
				icons[3].x = x + 11 + (8 - icons[3].width())/2;
				icons[3].y = y + 12 + (9 - icons[3].height())/2;
				PixelScene.align(icons[3]);
			}
		}

		@Override
		protected void layout() {
			super.layout();
			updateVisuals();
		}

		@Override
		public void alpha(float value) {
			super.alpha(value);
			for (Image im : icons){
				if (im != null) im.alpha(value);
			}
		}

		@Override
		public void enable(boolean value) {
			super.enable(value);
			for (Image ic : icons){
				if (ic != null && ic.alpha() >= 0.3f){
					ic.alpha( value ? 1 : 0.3f);
				}
			}
		}

		//private

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
		
		public void reset( Item item, int cell, float endX, float endY ) {
			view( item );
			
			active =
			visible =
				true;
			
			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);
			
			x = this.startX = start.x - width() / 2;
			y = this.startY = start.y - width() / 2;
			
			this.endX = endX - width() / 2;
			this.endY = endY - width() / 2;
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
				if (emitter != null) emitter.on = false;
				
			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );
				
				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}
