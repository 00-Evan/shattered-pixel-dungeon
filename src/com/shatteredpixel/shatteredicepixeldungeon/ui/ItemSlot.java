/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.ui;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredicepixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredicepixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Button;

public class ItemSlot extends Button {

	public static final int DEGRADED	= 0xFF4444;
	public static final int UPGRADED	= 0x44FF44;
	public static final int FADED       = 0x999999;
	public static final int WARNING		= 0xFF8800;
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;
	
	protected ItemSprite icon;
	protected BitmapText topLeft;
	protected BitmapText topRight;
	protected BitmapText bottomRight;
	
	private static final String TXT_STRENGTH	= ":%d";
	private static final String TXT_TYPICAL_STR	= "%d?";
    private static final String TXT_KEY_DEPTH	= "\u007F%d";

	private static final String TXT_LEVEL	= "%+d";
    private static final String TXT_CURSED    = "";//"-";

    // Special "virtual items"
	public static final Item CHEST = new Item() {
		public int image() { return ItemSpriteSheet.CHEST; };
	};
	public static final Item LOCKED_CHEST = new Item() {
		public int image() { return ItemSpriteSheet.LOCKED_CHEST; };
	};
    public static final Item CRYSTAL_CHEST = new Item() {
        public int image() { return ItemSpriteSheet.CRYSTAL_CHEST; };
    };
	public static final Item TOMB = new Item() {
		public int image() { return ItemSpriteSheet.TOMB; };
	};
	public static final Item SKELETON = new Item() {
		public int image() { return ItemSpriteSheet.BONES; };
	};
    public static final Item REMAINS = new Item() {
        public int image() { return ItemSpriteSheet.REMAINS; };
    };
	
	public ItemSlot() {
		super();
	}
	
	public ItemSlot( Item item ) {
		this();
		item( item );
	}
		
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		icon = new ItemSprite();
		add( icon );
		
		topLeft = new BitmapText( PixelScene.font1x );
		add( topLeft );
		
		topRight = new BitmapText( PixelScene.font1x );
		add( topRight );
		
		bottomRight = new BitmapText( PixelScene.font1x );
		add( bottomRight );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width) / 2;
		icon.y = y + (height - icon.height) / 2;
		
		if (topLeft != null) {
			topLeft.x = x;
			topLeft.y = y;
		}
		
		if (topRight != null) {
			topRight.x = x + (width - topRight.width());
			topRight.y = y;
		}
		
		if (bottomRight != null) {
			bottomRight.x = x + (width - bottomRight.width());
			bottomRight.y = y + (height - bottomRight.height());
		}
	}
	
	public void item( Item item ) {
		if (item == null) {
			
			active = false;
			icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = false;
			
		} else {
			
			active = true;
			icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = true;
			
			icon.view( item.image(), item.glowing() );
			
			topLeft.text( item.status()  );
			
			boolean isArmor = item instanceof Armor;
			boolean isWeapon = item instanceof Weapon;
			if (isArmor || isWeapon) {
				
				if (item.levelKnown || (isWeapon && !(item instanceof MeleeWeapon))) {
					
					int str = isArmor ? ((Armor)item).STR : ((Weapon)item).STR;
					topRight.text( Utils.format( TXT_STRENGTH, str ) );
					if (str > Dungeon.hero.STR()) {
						topRight.hardlight( DEGRADED );
					} else {
						topRight.resetColor();
					}
					
				} else {
					
					topRight.text( Utils.format( TXT_TYPICAL_STR, isArmor ? 
						((Armor)item).typicalSTR() : 
						((MeleeWeapon)item).typicalSTR() ) );
					topRight.hardlight( WARNING );
					
				}
				topRight.measure();

			} else if (item instanceof Key && !(item instanceof SkeletonKey)) {
                topRight.text(Utils.format(TXT_KEY_DEPTH, ((Key) item).depth));
                topRight.measure();
            } else {
				
				topRight.text( null );
				
			}
	
			int level = item.visiblyUpgraded(); 

			if (level != 0) {
                bottomRight.text( item.levelKnown ? Utils.format( TXT_LEVEL, level ) : TXT_CURSED );
                bottomRight.measure();
				bottomRight.hardlight( level > 0 ? UPGRADED : DEGRADED );
			} else if (item instanceof Scroll || item instanceof Potion) {
				if (item instanceof Scroll) bottomRight.text(((Scroll) item).initials());
				else bottomRight.text(((Potion) item).initials());

				bottomRight.measure();

				if (item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion
						|| item instanceof PotionOfStrength || item instanceof PotionOfMight)
					bottomRight.hardlight( UPGRADED );
				else
					bottomRight.hardlight( FADED );

			} else {
				bottomRight.text( null );
			}
			
			layout();
		}
	}
	
	public void enable( boolean value ) {
		
		active = value;
		
		float alpha = value ? ENABLED : DISABLED;
		icon.alpha( alpha );
		topLeft.alpha( alpha );
		topRight.alpha( alpha );
		bottomRight.alpha( alpha );
	}

    public void showParams( boolean TL, boolean TR, boolean BR ) {
        if (TL) add( topLeft );
        else remove( topLeft );

        if (TR) add( topRight );
        else remove( topRight );

        if (BR) add( bottomRight );
        else remove( bottomRight );
    }
}
