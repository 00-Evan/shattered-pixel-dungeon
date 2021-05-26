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


import com.elementalpixel.elementalpixeldungeon.sprites.ItemSprite;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.ui.Icons;
import com.elementalpixel.elementalpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v0_1_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.1.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_1_0_Changes(changeInfos);
	}
	

	public static void add_v0_1_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 5th, 2014\n" +
				"_-_ 69 days after Pixel Dungeon v1.7.1\n" +
				"_-_ 9 days after v1.7.1 source release\n" +
				"\n" +
				"v0.1.0 and v0.1.1 were extremely early Shattered updates that were only distributed via the Pixel Dungeon Subreddit. At this early stage of development Shattered was basically the same game as Pixel Dungeon v1.7.1.\n" +
				"\n" +
				"I started playing Pixel Dungeon in mid 2013. I loved the game but was frustrated with the balance of some items. When Pixel Dungeon went open source I decided to make Shattered as a balance modification. I called it Shattered as 'Shattered Pixel' was an old trade name I had, and the mod was going to 'shatter' Pixel Dungeon's balance.\n" +
				"\n" +
				"At this stage I didn't have any plans to add new content, I thought I was just going to spend a couple months rebalancing the game and that was it!"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RATION, null), "Food Changes",
				"_-_ Eating gives you minor buffs according to your class:\n")
		);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET, null), "Amulet changes",
				"_-_ Added Broken Amulet of Yendor")
		);
		

	}
	
}
