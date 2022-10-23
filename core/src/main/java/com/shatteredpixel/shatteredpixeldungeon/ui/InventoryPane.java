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

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;

public class InventoryPane extends Component {

	private NinePatch bg;
	private NinePatch bg2; //2 backgrounds to reduce transparency

	//used to prevent clicks through the BG normally, or to cancel selectors if they're enabled
	private PointerArea blocker;

	private Signal.Listener<KeyEvent> keyBlocker;

	private static InventoryPane instance;

	private ArrayList<InventorySlot> equipped;
	private ArrayList<InventorySlot> bagItems;

	private Image gold;
	private BitmapText goldTxt;
	private Image energy;
	private BitmapText energyTxt;
	private RenderedTextBlock promptTxt;

	private ArrayList<BagButton> bags;

	public static final int WIDTH = 187;
	public static final int HEIGHT = 82;

	private static final int SLOT_WIDTH = 17;
	private static final int SLOT_HEIGHT = 24;

	private WndBag.ItemSelector selector;

	public static Bag lastBag;

	private boolean lastEnabled = true;

	private static Image crossB;
	private static Image crossM;

	private static boolean targeting = false;
	private static InventorySlot targetingSlot = null;
	public static Char lastTarget = null;

	public InventoryPane(){
		super();
		instance = this;
	}

	@Override
	public synchronized void destroy() {
		KeyEvent.removeKeyListener(keyBlocker);
		super.destroy();
		if (instance == this) instance = null;
	}

	@Override
	protected void createChildren() {

		bg = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg);

		bg2 = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg2);

		blocker = new PointerArea(0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height){
			@Override
			protected void onClick(PointerEvent event) {
				if (selector != null && !bg.overlapsScreenPoint((int)event.current.x, (int)event.current.y)){
					//any windows opened as a consequence of this should be centered on the inventory
					GameScene.centerNextWndOnInvPane();
					selector.onSelect(null);
					selector = null;
					updateInventory();
				}
			}
		};
		blocker.target = bg; //targets bg when there is no selector, otherwise targets itself
		add (blocker);

		keyBlocker = new Signal.Listener<KeyEvent>(){
			@Override
			public boolean onSignal(KeyEvent keyEvent) {
				if (keyEvent.pressed && isSelecting() && InventoryPane.this.visible
						&& KeyBindings.getActionForKey(keyEvent) != SPDAction.BAG_1
						&& KeyBindings.getActionForKey(keyEvent) != SPDAction.BAG_2
						&& KeyBindings.getActionForKey(keyEvent) != SPDAction.BAG_3
						&& KeyBindings.getActionForKey(keyEvent) != SPDAction.BAG_4
						&& KeyBindings.getActionForKey(keyEvent) != SPDAction.BAG_5){
					//any windows opened as a consequence of this should be centered on the inventory
					GameScene.centerNextWndOnInvPane();
					selector.onSelect(null);
					selector = null;
					updateInventory();
					return true;
				}
				return false;
			}
		};

		equipped = new ArrayList<>();
		for (int i = 0; i < 5; i++){
			InventorySlot btn = new InventoryPaneSlot(null);
			equipped.add(btn);
			add(btn);
		}

		gold = Icons.get(Icons.COIN_SML);
		add(gold);
		goldTxt = new BitmapText(PixelScene.pixelFont);
		goldTxt.hardlight(Window.TITLE_COLOR);
		add(goldTxt);

		energy = Icons.get(Icons.ENERGY_SML);
		add(energy);
		energyTxt = new BitmapText(PixelScene.pixelFont);
		energyTxt.hardlight(0x44CCFF);
		add(energyTxt);

		promptTxt = PixelScene.renderTextBlock(6);
		promptTxt.hardlight(Window.TITLE_COLOR);
		add(promptTxt);

		bagItems = new ArrayList<>();
		for (int i = 0; i < 20; i++){
			InventorySlot btn = new InventoryPaneSlot(null);
			bagItems.add(btn);
			add(btn);
		}

		bags = new ArrayList<>();
		for (int i = 0; i < 5; i++){
			BagButton btn = new BagButton(null, i+1);
			bags.add(btn);
			add(btn);
		}

		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );

		crossM = new Image();
		crossM.copy( crossB );

		lastEnabled = true;
		updateInventory();

		width = WIDTH;
		height = HEIGHT;
	}

	@Override
	protected void layout() {
		width = WIDTH;
		height = HEIGHT;

		bg.x = bg2.x = x;
		bg.y = bg2.y = y;
		bg.size(width, height);
		bg2.size(width, height);

		float left = x+4;
		for (InventorySlot i : equipped){
			i.setRect(left, y+4, SLOT_WIDTH, SLOT_HEIGHT);
			left = i.right()+1;
		}

		promptTxt.maxWidth((int) (width - (left - x) - bg.marginRight()));
		if (promptTxt.height() > 10){
			promptTxt.setPos(left, y + 2 + (12 - promptTxt.height()) / 2);
		} else {
			promptTxt.setPos(left, y + 4 + (10 - promptTxt.height()) / 2);
		}

		goldTxt.x = left;
		goldTxt.y = y+5.5f;
		PixelScene.align(goldTxt);

		gold.x = goldTxt.x + goldTxt.width() + 1;
		gold.y = goldTxt.y;

		energyTxt.x = gold.x + gold.width() + 2;
		energyTxt.y = y+5.5f;
		PixelScene.align(energyTxt);

		energy.x = energyTxt.x + energyTxt.width() + 1;
		energy.y = energyTxt.y;

		for (BagButton b : bags){
			b.setRect(left, y + 14, SLOT_WIDTH, 14);
			left = b.right()+1;
		}

		left = x+4;
		float top = y+4+SLOT_HEIGHT+1;
		for (InventorySlot b : bagItems){
			b.setRect(left, top, SLOT_WIDTH, SLOT_HEIGHT);
			left = b.right()+1;
			if (left - x > width - 17){
				left = x+4;
				top += SLOT_HEIGHT+1;
			}
		}

		super.layout();
	}
	
	public void alpha( float value ){
		bg.alpha( value );
		bg2.alpha( value );
		
		for (InventorySlot slot : equipped){
			slot.alpha( value );
		}
		for (InventorySlot slot : bagItems){
			slot.alpha( value );
		}
		
		gold.alpha(value);
		goldTxt.alpha(value);
		energy.alpha(value);
		energyTxt.alpha(value);

		for (BagButton bag : bags){
			bag.alpha( value );
		}
	}

	public static void refresh(){
		if (instance != null) instance.updateInventory();
	}

	public void updateInventory(){
		if (selector == null){
			blocker.target = bg;
			KeyEvent.removeKeyListener(keyBlocker);
		} else {
			blocker.target = blocker;
			KeyEvent.addKeyListener(keyBlocker);
		}

		Belongings stuff = Dungeon.hero.belongings;

		if (lastBag == null || !stuff.getBags().contains(lastBag)){
			lastBag = stuff.backpack;
		}

		equipped.get(0).item(stuff.weapon == null ? new WndBag.Placeholder( ItemSpriteSheet.WEAPON_HOLDER ) : stuff.weapon);
		equipped.get(1).item(stuff.armor == null ? new WndBag.Placeholder( ItemSpriteSheet.ARMOR_HOLDER ) : stuff.armor);
		equipped.get(2).item(stuff.artifact == null ? new WndBag.Placeholder( ItemSpriteSheet.ARTIFACT_HOLDER ) : stuff.artifact);
		equipped.get(3).item(stuff.misc == null ? new WndBag.Placeholder( ItemSpriteSheet.SOMETHING ) : stuff.misc);
		equipped.get(4).item(stuff.ring == null ? new WndBag.Placeholder( ItemSpriteSheet.RING_HOLDER ) : stuff.ring);

		ArrayList<Item> items = lastBag.items;
		int j = 0;
		for (int i = 0; i < 20; i++){
			if (i == 0 && lastBag != stuff.backpack){
				bagItems.get(i).item(lastBag);
				continue;
			}
			if (items.size() > j){
				if (items.get(j) instanceof Bag){
					j++;
					i--;
					continue;
				}
				bagItems.get(i).item(items.get(j));
				j++;
			} else {
				bagItems.get(i).item(null);
			}
		}

		if (selector == null) {
			promptTxt.visible = false;

			goldTxt.text(Integer.toString(Dungeon.gold));
			goldTxt.measure();
			goldTxt.visible = gold.visible = true;

			energyTxt.text(Integer.toString(Dungeon.energy));
			energyTxt.measure();
			energyTxt.visible = energy.visible = Dungeon.energy > 0;
		} else {
			promptTxt.text(selector.textPrompt());
			promptTxt.visible = true;

			goldTxt.visible = gold.visible = false;
			energyTxt.visible = energy.visible = false;
		}

		ArrayList<Bag> inventBags = stuff.getBags();
		for (int i = 0; i < bags.size(); i++){
			if (inventBags.size() > i){
				bags.get(i).bag(inventBags.get(i));
			} else {
				bags.get(i).bag(null);
			}
		}

		boolean lostInvent = Dungeon.hero.buff(LostInventory.class) != null;
		for (InventorySlot b : equipped){
			b.enable(lastEnabled
					&& !(b.item() instanceof WndBag.Placeholder)
					&& (selector == null || selector.itemSelectable(b.item()))
					&& (!lostInvent || b.item().keptThoughLostInvent));
		}
		for (InventorySlot b : bagItems){
			b.enable(lastEnabled
					&& b.item() != null
					&& (selector == null || selector.itemSelectable(b.item()))
					&& (!lostInvent || b.item().keptThoughLostInvent));
		}
		for (BagButton b : bags){
			b.enable(lastEnabled);
		}

		goldTxt.alpha( lastEnabled ? 1f : 0.3f );
		gold.alpha( lastEnabled ? 1f : 0.3f );
		energyTxt.alpha( lastEnabled ? 1f : 0.3f );
		energy.alpha( lastEnabled ? 1f : 0.3f );

		layout();
	}

	public void setSelector(WndBag.ItemSelector selector){
		this.selector = selector;
		if (selector.preferredBag() == Belongings.Backpack.class){
			lastBag = Dungeon.hero.belongings.backpack;
		} else if (selector.preferredBag() != null) {
			Bag preferred = Dungeon.hero.belongings.getItem(selector.preferredBag());
			if (preferred != null) lastBag = preferred;
		}
		updateInventory();
	}

	public boolean isSelecting(){
		return selector != null;
	}

	public static void useTargeting(){
		if (instance != null &&
				instance.visible &&
				lastTarget != null &&
				Actor.chars().contains( lastTarget ) &&
				lastTarget.isAlive() &&
				lastTarget.alignment != Char.Alignment.ALLY &&
				Dungeon.level.heroFOV[lastTarget.pos]) {

			targeting = true;
			CharSprite sprite = lastTarget.sprite;

			if (sprite.parent != null) {
				sprite.parent.addToFront(crossM);
				crossM.point(sprite.center(crossM));
			}

			crossB.point(targetingSlot.sprite.center(crossB));
			crossB.visible = true;

		} else {

			lastTarget = null;
			targeting = false;

		}
	}

	public static void cancelTargeting(){
		if (targeting){
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}

	@Override
	public synchronized void update() {
		super.update();

		if (lastEnabled != (Dungeon.hero.ready || !Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready || !Dungeon.hero.isAlive());

			boolean lostInvent = Dungeon.hero.buff(LostInventory.class) != null;
			for (InventorySlot b : equipped){
				b.enable(lastEnabled
						&& !(b.item() instanceof WndBag.Placeholder)
						&& (selector == null || selector.itemSelectable(b.item()))
						&& (!lostInvent || b.item().keptThoughLostInvent));
			}
			for (InventorySlot b : bagItems){
				b.enable(lastEnabled
						&& b.item() != null
						&& (selector == null || selector.itemSelectable(b.item()))
						&& (!lostInvent || b.item().keptThoughLostInvent));
			}
			for (BagButton b : bags){
				b.enable(lastEnabled);
			}

			goldTxt.alpha( lastEnabled ? 1f : 0.3f );
			gold.alpha( lastEnabled ? 1f : 0.3f );
			energyTxt.alpha( lastEnabled ? 1f : 0.3f );
			energy.alpha( lastEnabled ? 1f : 0.3f );
		}

	}

	private Image bagIcon(Bag bag ) {
		if (bag instanceof VelvetPouch) {
			return Icons.get( Icons.SEED_POUCH );
		} else if (bag instanceof ScrollHolder) {
			return Icons.get( Icons.SCROLL_HOLDER );
		} else if (bag instanceof MagicalHolster) {
			return Icons.get( Icons.WAND_HOLSTER );
		} else if (bag instanceof PotionBandolier) {
			return Icons.get( Icons.POTION_BANDOLIER );
		} else {
			return Icons.get( Icons.BACKPACK );
		}
	}

	private class InventoryPaneSlot extends InventorySlot {

		private InventoryPaneSlot( Item item ){
			super(item);
		}

		@Override
		protected void onClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (targeting){
				if (targetingSlot == this){
					int cell = QuickSlotButton.autoAim(lastTarget, item());

					if (cell != -1){
						GameScene.handleCell(cell);
					} else {
						//couldn't auto-aim, just target the position and hope for the best.
						GameScene.handleCell( lastTarget.pos );
					}
					return;
				} else {
					cancelTargeting();
				}
			}

			//any windows opened as a consequence of this button should be centered on the inventory
			GameScene.centerNextWndOnInvPane();
			if (selector != null) {
				WndBag.ItemSelector activating = selector;
				selector = null;
				activating.onSelect( item );
				updateInventory();
			} else {
				targetingSlot = this;
				GameScene.show(new WndUseItem( null, item ));
			}
		}

		@Override
		protected void onMiddleClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (!Dungeon.hero.isAlive() || !Dungeon.hero.ready){
				return;
			}

			if (targeting){
				if (targetingSlot == this){
					onClick();
				}
				return;
			}

			if (selector == null && item.defaultAction != null){
				item.execute(Dungeon.hero);
				if (item.usesTargeting) {
					targetingSlot = this;
					InventoryPane.useTargeting();
				}
			} else {
				onClick();
			}
		}

		@Override
		protected void onRightClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
				updateInventory();
				return;
			}

			if (!Dungeon.hero.isAlive() || !Dungeon.hero.ready){
				return;
			}

			if (targeting){
				//do nothing
				return;
			}

			if (selector == null){
				targetingSlot = this;
				RightClickMenu r = new RightClickMenu(item);
				parent.addToFront(r);
				r.camera = camera();
				PointF mousePos = PointerEvent.currentHoverPos();
				mousePos = camera.screenToCamera((int)mousePos.x, (int)mousePos.y);
				r.setPos(mousePos.x-3, mousePos.y-3);
			} else {
				//do nothing
			}
		}
	}

	private class BagButton extends IconButton {

		private static final int ACTIVE		= 0x9953564D;
		private static final int INACTIVE	= 0x9942443D;

		private ColorBlock bgTop;
		private ColorBlock bgBottom;

		private Bag bag;
		private int index;

		public BagButton( Bag bag, int index ){
			super( bagIcon(bag) );
			this.bag = bag;
			this.index = index;
			visible = active = bag != null;
		}

		public void bag( Bag bag ){
			this.bag = bag;
			icon(bagIcon(bag));
			visible = active = bag != null;

			if (lastBag == bag){
				bgTop.texture(TextureCache.createSolid(ACTIVE));
				bgBottom.texture(TextureCache.createSolid(ACTIVE));
			} else {
				bgTop.texture(TextureCache.createSolid(INACTIVE));
				bgBottom.texture(TextureCache.createSolid(INACTIVE));
			}
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			bgTop = new ColorBlock(1, 1, ACTIVE);
			add(bgTop);

			bgBottom = new ColorBlock(1, 1, ACTIVE);
			add(bgBottom);
		}

		@Override
		protected void layout() {
			super.layout();

			bgTop.size(width-2, 1);
			bgTop.y = y;
			bgTop.x = x+1;

			bgBottom.size(width, height-1);
			bgBottom.y = y+1;
			bgBottom.x = x;
		}
		
		public void alpha( float value ){
			bgTop.alpha(value);
			bgBottom.alpha(value);
			icon.alpha(value);
		}

		@Override
		protected void onClick() {
			super.onClick();
			lastBag = bag;
			refresh();
		}

		@Override
		public GameAction keyAction() {
			switch (index){
				case 1: default:
					return SPDAction.BAG_1;
				case 2:
					return SPDAction.BAG_2;
				case 3:
					return SPDAction.BAG_3;
				case 4:
					return SPDAction.BAG_4;
				case 5:
					return SPDAction.BAG_5;
			}
		}

		@Override
		public GameAction secondaryTooltipAction() {
			return SPDAction.INVENTORY_SELECTOR;
		}

		@Override
		protected String hoverText() {
			if (bag != null) {
				return Messages.titleCase(bag.name());
			} else {
				return null;
			}
		}
	}

}
