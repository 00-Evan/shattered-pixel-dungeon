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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int FADED       = 0x999999;
	public static final int WARNING		= 0xFF8800;
	public static final int ENHANCED	= 0x3399FF;
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;
	
	protected ItemSprite sprite;
	protected Item       item;
	protected BitmapText status;
	protected BitmapText extra;
	protected Image      itemIcon;
	protected BitmapText level;
	
	private static final String TXT_STRENGTH	= ":%d";
	private static final String TXT_TYPICAL_STR	= "%d?";

	private static final String TXT_LEVEL	= "%+d";

	// Special "virtual items"
	public static final Item CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CHEST; }
	};
	public static final Item LOCKED_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.LOCKED_CHEST; }
	};
	public static final Item CRYSTAL_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CRYSTAL_CHEST; }
	};
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; }
	};
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; }
	};
	public static final Item REMAINS = new Item() {
		public int image() { return ItemSpriteSheet.REMAINS; }
	};
	
	public ItemSlot() {
		super();
		sprite.visible(false);
		enable(false);
	}
	
	public ItemSlot( Item item ) {
		this();
		item( item );
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		sprite = new ItemSprite();
		add(sprite);
		
		status = new BitmapText( PixelScene.pixelFont);
		add(status);
		
		extra = new BitmapText( PixelScene.pixelFont);
		add(extra);
		
		level = new BitmapText( PixelScene.pixelFont);
		add(level);
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		sprite.x = x + (width - sprite.width) / 2f;
		sprite.y = y + (height - sprite.height) / 2f;
		PixelScene.align(sprite);
		
		if (status != null) {
			status.measure();
			if (status.width > width){
				status.scale.set(PixelScene.align(0.8f));
			} else {
				status.scale.set(1f);
			}
			status.x = x;
			status.y = y;
			PixelScene.align(status);
		}
		
		if (extra != null) {
			extra.x = x + (width - extra.width());
			extra.y = y;
			PixelScene.align(extra);
		}

		if (itemIcon != null){
			itemIcon.x = x + width - (ItemSpriteSheet.Icons.SIZE + itemIcon.width())/2f;
			itemIcon.y = y + (ItemSpriteSheet.Icons.SIZE - itemIcon.height)/2f;
			PixelScene.align(itemIcon);
		}
		
		if (level != null) {
			level.x = x + (width - level.width());
			level.y = y + (height - level.baseLine() - 1);
			PixelScene.align(level);
		}

	}
	
	public void item( Item item ) {
		if (this.item == item) {
			if (item != null) {
				sprite.frame(item.image());
				sprite.glow(item.glowing());
			}
			updateText();
			return;
		}

		this.item = item;

		if (item == null) {

			enable(false);
			sprite.visible(false);

			updateText();
			
		} else {
			
			enable(true);
			sprite.visible(true);

			sprite.view( item );
			updateText();
		}
	}

	private void updateText(){

		if (itemIcon != null){
			remove(itemIcon);
			itemIcon = null;
		}

		if (item == null){
			status.visible = extra.visible = level.visible = false;
			return;
		} else {
			status.visible = extra.visible = level.visible = true;
		}

		status.text( item.status() );

		if (item.icon != -1 && (item.isIdentified() || (item instanceof Ring && ((Ring) item).isKnown()))){
			extra.text( null );

			itemIcon = new Image(Assets.Sprites.ITEM_ICONS);
			itemIcon.frame(ItemSpriteSheet.Icons.film.get(item.icon));
			add(itemIcon);

		} else if (item instanceof Weapon || item instanceof Armor) {

			if (item.levelKnown){
				int str = item instanceof Weapon ? ((Weapon)item).STRReq() : ((Armor)item).STRReq();
				extra.text( Messages.format( TXT_STRENGTH, str ) );
				if (str > Dungeon.hero.STR()) {
					extra.hardlight( DEGRADED );
				} else {
					extra.resetColor();
				}
			} else {
				int str = item instanceof Weapon ? ((Weapon)item).STRReq(0) : ((Armor)item).STRReq(0);
				extra.text( Messages.format( TXT_TYPICAL_STR, str ) );
				extra.hardlight( WARNING );
			}
			extra.measure();

		} else {

			extra.text( null );

		}

		int trueLvl = item.visiblyUpgraded();
		int buffedLvl = item.buffedVisiblyUpgraded();

		if (trueLvl != 0 || buffedLvl != 0) {
			level.text( Messages.format( TXT_LEVEL, buffedLvl ) );
			level.measure();
			if (trueLvl == buffedLvl || buffedLvl <= 0) {
				level.hardlight(buffedLvl > 0 ? UPGRADED : DEGRADED);
			} else {
				level.hardlight(buffedLvl > trueLvl ? ENHANCED : WARNING);
			}
		} else {
			level.text( null );
		}

		layout();
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;
		sprite.alpha( alpha );
		status.alpha( alpha );
		extra.alpha( alpha );
		level.alpha( alpha );
		if (itemIcon != null) itemIcon.alpha( alpha );
	}

	public void showExtraInfo( boolean show ){

		if (show){
			add(extra);
		} else {
			remove(extra);
		}

	}
}
