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

package com.saqfish.spdnet.windows;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.buffs.Combo;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.RedButton;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.Window;
import com.watabou.noosa.Image;

public class WndCombo extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 160;

	private static final int MARGIN  = 2;

	public WndCombo( Combo combo ){
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = MARGIN;
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
		title.hardlight(TITLE_COLOR);
		title.setPos((width-title.width())/2, pos);
		title.maxWidth(width - MARGIN * 2);
		add(title);

		pos = title.bottom() + 3*MARGIN;

		Image icon;
		if (Dungeon.hero.belongings.weapon() != null){
			icon = new ItemSprite(Dungeon.hero.belongings.weapon().image, null);
		} else {
			icon = new ItemSprite(new Item(){ {image = ItemSpriteSheet.WEAPON_HOLDER; }});
		}

		for (Combo.ComboMove move : Combo.ComboMove.values()) {
			Image ic = new Image(icon);

			RedButton moveBtn = new RedButton(move.desc(combo.getComboCount()), 6){
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					combo.useMove(move);
				}
			};
			ic.tint(move.tintColor);
			moveBtn.icon(ic);
			moveBtn.leftJustify = true;
			moveBtn.multiline = true;
			moveBtn.setSize(width, moveBtn.reqHeight());
			moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
			moveBtn.enable(combo.canUseMove(move));
			add(moveBtn);
			pos = moveBtn.bottom() + MARGIN;
		}

		resize(width, (int)pos);

	}


}
