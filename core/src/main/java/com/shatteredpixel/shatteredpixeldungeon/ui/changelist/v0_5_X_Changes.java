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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v0_5_X_Changes {
	
	//just the one update this time
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_5_0_Changes(changeInfos);
	}
	
	public static void add_v0_5_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 8th, 2017\n" +
				"_-_ 233 days after Shattered v0.4.0\n" +
				"_-_ 115 days after Shattered v0.4.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton( Icons.get(Icons.DEPTH), "New Dungeon Visual Style!",
				"_-_ Walls and some terrain now have depth\n" +
				"_-_ Characters & items are raised & cast shadows\n" +
				"_-_ Added a visible tile grid in the settings menu"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Quarterstaff()), "Equipment Balance Changes",
				"_-_ Quarterstaff armor bonus increased from 2 to 3\n\n" +
				"_-_ Wand of Frost damage against chilled enemies reduced from -7.5% per turn of chill to -10%\n\n" +
				"_-_ Wand of Transfusion self-damage reduced from 15% max hp to 10% max hp per zap\n\n" +
				"_-_ Dried Rose charges 20% faster and the ghost hero is stronger, especially at low levels"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Stylus()), "Glyph Balance Changes",
				"_-_ Glyph of Entanglement activates less often but grants significantly more herbal armor\n\n" +
				"_-_ Glyph of Stone armor bonus reduced from 2+level to 0+level\n\n" +
				"_-_ Glyph of Antimagic magical damage resist reduced from 50% of armor to 33% of armor\n\n" +
				"_-_ Glyph of Viscosity damage rate increased from 10% of deferred damage to 15%"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"_-_ Added new Language: Esperanto\n" +
				"_-_ Added new Language: Indonesian\n"));
	}
	
}
