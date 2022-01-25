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
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class InventoryPane extends Component {

	private NinePatch bg;
	private NinePatch bg2; //2 backgrounds to reduce transparency

	private static InventoryPane instance;

	private ArrayList<InventorySlot> equipped;
	private ArrayList<InventorySlot> bagItems;

	private Image gold;
	private BitmapText goldTxt;
	private Image energy;
	private BitmapText energyTxt;

	private ArrayList<BagButton> bags;

	public static final int WIDTH = 187;
	public static final int HEIGHT = 82;

	private static final int SLOT_WIDTH = 17;
	private static final int SLOT_HEIGHT = 24;

	public static Bag lastBag;

	private boolean lastEnabled = true;

	public InventoryPane(){
		super();
		instance = this;
	}

	@Override
	public synchronized void destroy() {
		super.destroy();
		if (instance == this) instance = null;
	}

	@Override
	protected void createChildren() {

		//TODO capture inputs tapped on BG

		bg = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg);

		bg2 = Chrome.get(Chrome.Type.TOAST_TR);
		add(bg2);

		//blocks touches from going through BG into game scene
		add (new PointerArea(bg));

		equipped = new ArrayList<>();
		for (int i = 0; i < 5; i++){
			InventorySlot btn = new InventorySlot(null){
				@Override
				protected void onClick() {
					if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
						updateInventory();
					} else {
						Game.scene().addToFront(new WndUseItem( null, item ) );
					}
				}
			};
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

		bagItems = new ArrayList<>();
		for (int i = 0; i < 20; i++){
			InventorySlot btn = new InventorySlot(null){
				@Override
				protected void onClick() {
					if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){
						updateInventory();
					} else {
						Game.scene().addToFront(new WndUseItem( null, item ) );
					}
				}
			};
			bagItems.add(btn);
			add(btn);
		}

		bags = new ArrayList<>();
		for (int i = 0; i < 5; i++){
			BagButton btn = new BagButton(null);
			bags.add(btn);
			add(btn);
		}

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

	public static void refresh(){
		if (instance != null) instance.updateInventory();
	}

	public void updateInventory(){
		Belongings stuff = Dungeon.hero.belongings;

		if (lastBag == null || !stuff.contains(lastBag)){
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

		goldTxt.text(Integer.toString(Dungeon.gold));
		goldTxt.measure();

		energyTxt.text(Integer.toString(Dungeon.energy));
		energyTxt.measure();
		energyTxt.visible = energy.visible = Dungeon.energy > 0;

		ArrayList<Bag> inventBags = stuff.getBags();
		for (int i = 0; i < bags.size(); i++){
			if (inventBags.size() > i){
				bags.get(i).bag(inventBags.get(i));
			} else {
				bags.get(i).bag(null);
			}
		}

		for (InventorySlot b : equipped){
			b.enable(lastEnabled && !(b.item() instanceof WndBag.Placeholder));
		}
		for (InventorySlot b : bagItems){
			b.enable(lastEnabled && b.item() != null);
		}
		for (BagButton b : bags){
			b.enable(lastEnabled);
		}

		layout();
	}

	@Override
	public synchronized void update() {
		super.update();

		if (lastEnabled != (Dungeon.hero.ready || !Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready || !Dungeon.hero.isAlive());

			for (InventorySlot b : equipped){
				b.enable(lastEnabled && !(b.item() instanceof WndBag.Placeholder));
			}
			for (InventorySlot b : bagItems){
				b.enable(lastEnabled && b.item() != null);
			}
			for (BagButton b : bags){
				b.enable(lastEnabled);
			}
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

	private class BagButton extends IconButton {

		private static final int ACTIVE		= 0x9953564D;
		private static final int INACTIVE	= 0x9942443D;

		private ColorBlock bgTop;
		private ColorBlock bgBottom;

		private Bag bag;

		public BagButton( Bag bag ){
			super( bagIcon(bag) );
			this.bag = bag;
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

		@Override
		protected void onClick() {
			super.onClick();
			lastBag = bag;
			refresh();
		}

	}

}
