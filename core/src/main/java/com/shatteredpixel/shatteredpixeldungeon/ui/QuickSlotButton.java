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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.input.GameAction;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PathFinder;

public class QuickSlotButton extends Button implements WndBag.Listener {
	
	private static QuickSlotButton[] instance = new QuickSlotButton[4];
	private int slotNum;

	private ItemSlot slot;
	
	private static Image crossB;
	private static Image crossM;
	
	private static boolean targeting = false;
	public static Char lastTarget = null;
	
	public QuickSlotButton( int slotNum ) {
		super();
		this.slotNum = slotNum;
		item( select( slotNum ) );
		
		instance[slotNum] = this;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		reset();
	}

	public static void reset() {
		instance = new QuickSlotButton[4];

		lastTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting) {
					int cell = autoAim(lastTarget, select(slotNum));

					if (cell != -1){
						GameScene.handleCell(cell);
					} else {
						//couldn't auto-aim, just target the position and hope for the best.
						GameScene.handleCell( lastTarget.pos );
					}
				} else {
					Item item = select(slotNum);
					if (item.usesTargeting)
						useTargeting();
					item.execute( Dungeon.hero );
				}
			}
			
			@Override
			public GameAction keyAction() {
				return QuickSlotButton.this.keyAction();
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlotButton.this.onLongClick();
			}
			@Override
			protected void onPointerDown() {
				sprite.lightness( 0.7f );
			}
			@Override
			protected void onPointerUp() {
				sprite.resetColor();
			}
		};
		slot.showExtraInfo( false );
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
		
		crossB.x = x + (width - crossB.width) / 2;
		crossB.y = y + (height - crossB.height) / 2;
		PixelScene.align(crossB);
	}

	@Override
	public void update() {
		super.update();
		if (targeting && lastTarget != null && lastTarget.sprite != null){
			crossM.point(lastTarget.sprite.center(crossM));
		}
	}

	@Override
	public GameAction keyAction() {
		switch (slotNum){
			case 0:
				return SPDAction.QUICKSLOT_1;
			case 1:
				return SPDAction.QUICKSLOT_2;
			case 2:
				return SPDAction.QUICKSLOT_3;
			case 3:
				return SPDAction.QUICKSLOT_4;
			default:
				return super.keyAction();
		}
	}
	
	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, Messages.get(this, "select_item") );
	}
	
	@Override
	protected boolean onLongClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, Messages.get(this, "select_item") );
		return true;
	}

	private static Item select(int slotNum){
		return Dungeon.quickslot.getItem( slotNum );
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			Dungeon.quickslot.setSlot( slotNum , item );
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
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
		slot.enable(Dungeon.quickslot.isNonePlaceholder( slotNum ));
	}
	
	private void useTargeting() {

		if (lastTarget != null &&
				Actor.chars().contains( lastTarget ) &&
				lastTarget.isAlive() &&
				Dungeon.level.heroFOV[lastTarget.pos]) {

			targeting = true;
			CharSprite sprite = lastTarget.sprite;
			
			sprite.parent.addToFront( crossM );
			crossM.point(sprite.center(crossM));

			crossB.point(slot.sprite.center(crossB));
			crossB.visible = true;

		} else {

			lastTarget = null;
			targeting = false;

		}

	}

	public static int autoAim(Char target){
		//will use generic projectile logic if no item is specified
		return autoAim(target, new Item());
	}

	//FIXME: this is currently very expensive, should either optimize ballistica or this, or both
	public static int autoAim(Char target, Item item){

		//first try to directly target
		if (item.throwPos(Dungeon.hero, target.pos) == target.pos) {
			return target.pos;
		}

		//Otherwise pick nearby tiles to try and 'angle' the shot, auto-aim basically.
		PathFinder.buildDistanceMap( target.pos, BArray.not( new boolean[Dungeon.level.length()], null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE
					&& item.throwPos(Dungeon.hero, i) == target.pos)
				return i;
		}

		//couldn't find a cell, give up.
		return -1;
	}
	
	public static void refresh() {
		for (int i = 0; i < instance.length; i++) {
			if (instance[i] != null) {
				instance[i].item(select(i));
			}
		}
	}
	
	public static void target( Char target ) {
		if (target != null && target.alignment != Char.Alignment.ALLY) {
			lastTarget = target;
			
			TargetHealthIndicator.instance.target( target );
		}
	}
	
	public static void cancel() {
		if (targeting) {
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}
}
