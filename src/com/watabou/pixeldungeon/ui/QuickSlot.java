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
package com.watabou.pixeldungeon.ui;

import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.windows.WndBag;

public class QuickSlot extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";
	
	private static QuickSlot instance;
	
	private Item itemInSlot;
	private ItemSlot slot;
	
	private Image crossB;
	private Image crossM;
	
	private boolean targeting = false;
	private Item lastItem = null;
	private Char lastTarget= null;
	
	public QuickSlot() {
		super();
		item( select() );
		
		instance = this;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		instance = null;
		
		lastItem = null;
		lastTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					GameScene.handleCell( lastTarget.pos );
				} else {
					Item item = select();
					if (item == lastItem) {
						useTargeting();
					} else {
						lastItem = item;
					}
					item.execute( Dungeon.hero );
				}
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlot.this.onLongClick();
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );
		
		crossM = new Image();
		crossM.copy( crossB );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill( this );
		
		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
	}
	
	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
	}
	
	@Override
	protected boolean onLongClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static Item select() {
		if (Dungeon.quickslot instanceof Item) {
			
			return (Item)Dungeon.quickslot;
			
		} else if (Dungeon.quickslot != null) {
			
			Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)Dungeon.quickslot );			
			return item != null ? item : Item.virtual( (Class<? extends Item>)Dungeon.quickslot );
			
		} else {
			
			return null;
			
		}
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			Dungeon.quickslot = item.stackable ? item.getClass() : item;
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
		itemInSlot = item;
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable( 
			itemInSlot != null && 
			itemInSlot.quantity() > 0 && 
			(Dungeon.hero.belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.hero )));
	}
	
	private void useTargeting() {
		
		targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.visible[lastTarget.pos];
		
		if (targeting) {
			if (Actor.all().contains( lastTarget )) {
				lastTarget.sprite.parent.add( crossM );
				crossM.point( DungeonTilemap.tileToWorld( lastTarget.pos ) );
				crossB.visible = true;
			} else {
				lastTarget = null;
			}
		}
	}
	
	public static void refresh() {
		if (instance != null) {
			instance.item( select() );
		}
	}
	
	public static void target( Item item, Char target ) {
		if (item == instance.lastItem && target != Dungeon.hero) {
			instance.lastTarget = target;
			
			HealthIndicator.instance.target( target );
		}
	}
	
	public static void cancel() {
		if (instance != null && instance.targeting) {
			instance.crossB.visible = false;
			instance.crossM.remove();
			instance.targeting = false;
		}
	}
}
