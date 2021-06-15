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

package com.elementalpixel.elementalpixeldungeon.ui.changelist;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.scenes.ChangesScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSprite;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.sprites.MobSprite;
import com.elementalpixel.elementalpixeldungeon.ui.Icons;
import com.elementalpixel.elementalpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_1_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.1.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_1_0_Changes(changeInfos);
	}
	

	public static void add_v0_1_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo("v0.1a", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RATION, null), "Food Changes",
				"_-_ Eating gives you minor buffs\n")
		);

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.ROGUE, 0, 90, 12, 15),  "New Class",
				"_-_ Added new class - Alchemist:\n" +
						"Starts with alchemist dagger and alchemist flask\n\n" +
						"Subclasses:\n\n" +
						"- _Elementalist_ heals himself while affected by fire or toxic gas. He's also immune to Paralysis and Vertigo\n\n" +
						"- _Scientist_ crafts 2 potions instead of 1. Effects of most potions are stronger."
				) );

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET, null), "Amulet changes",
				"_-_ Added Broken Amulet of Yendor\n" +
						"_-_ Added 4 new chapters of each element after Yog\n\n" +
						"_-_ In the end of each chapter, there will be new boss which will drop fragment of its element. You have to acquire all fragments to fix the Broken Amulet of Yendor"
				)
		);
		

	}
	
}
