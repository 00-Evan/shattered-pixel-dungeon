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

package net.casiello.pixeldungeonrescue.ui.changelist;

import com.watabou.noosa.Image;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.items.Ankh;
import net.casiello.pixeldungeonrescue.items.food.Blandfruit;
import net.casiello.pixeldungeonrescue.sprites.ItemSprite;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.ui.Icons;
import net.casiello.pixeldungeonrescue.ui.Window;

import java.util.ArrayList;

public class v0_1_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.1.x", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_1_0_Changes(changeInfos);
	}
	
	public static void add_v0_1_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Forked from Shattered Pixel Dungeon v0.8.0b"));

		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Changes",
				"_-_ Warrior is now female\n" +
						 "_-_ Future task: allow sex choice"));
		changes.addButton( new ChangeButton(new Image(Assets.MAGE, 0, 15, 12, 15), "Mage Changes",
				"_-_ Mage is now female\n" +
						 "_-_ Future task: allow sex choice"));
		changes.addButton( new ChangeButton(new Image(Assets.ROGUE, 0, 15, 12, 15), "Rogue Changes",
				"_-_ Rogue is now female\n" +
						 "_-_ Future task: allow sex choice"));
	}
	
}
