/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

//essentially a RedButton version of ItemSlot
public class ItemButton extends Component {

	protected NinePatch bg;
	protected ItemSlot slot;

	@Override
	protected void createChildren() {
		super.createChildren();

		bg = Chrome.get(Chrome.Type.RED_BUTTON);
		add(bg);

		slot = new ItemSlot() {
			@Override
			protected void onPointerDown() {
				bg.brightness(1.2f);
				Sample.INSTANCE.play(Assets.Sounds.CLICK);
			}

			@Override
			protected void onPointerUp() {
				bg.resetColor();
			}

			@Override
			protected void onClick() {
				ItemButton.this.onClick();
			}

			@Override
			protected boolean onLongClick() {
				return ItemButton.this.onLongClick();
			}
		};
		slot.enable(true);
		add(slot);
	}

	protected void onClick() {}

	protected boolean onLongClick(){
		return false;
	}

	@Override
	protected void layout() {
		super.layout();

		bg.x = x;
		bg.y = y;
		bg.size( width, height );

		slot.setRect(x, y, width, height);
		if (width() >= 24 || height >= 24) {
			slot.setMargins(2, 2, 2, 2);
		} else {
			slot.setMargins(1, 1, 1, 1);
		}
	}

	public Item item(){
		return slot.item;
	}

	public void item( Item item ) {
		slot.item( item );
	}

	public void clear(){
		slot.clear();
	}

	public ItemSlot slot(){
		return slot;
	}

}
