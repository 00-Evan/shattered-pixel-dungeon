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

package com.saqfish.spdnet.ui.changelist;

import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.sprites.ItemSprite;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

//not actually a button, but functions as one.
public class ChangeButton extends Component {
	
	protected Image icon;
	protected String title;
	protected String message;
	
	public ChangeButton( Image icon, String title, String message){
		super();
		
		this.icon = icon;
		add(this.icon);
		
		this.title = Messages.titleCase(title);
		this.message = message;
		
		layout();
	}
	
	public ChangeButton(Item item, String message ){
		this( new ItemSprite(item), item.name(), message);
	}
	
	protected void onClick() {
		ShatteredPixelDungeon.scene().add(new ChangesWindow(new Image(icon), title, message));
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		icon.x = x + (width - icon.width()) / 2f;
		icon.y = y + (height - icon.height()) / 2f;
		PixelScene.align(icon);
	}
}
