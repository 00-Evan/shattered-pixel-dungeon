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
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v0_4_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.4.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_4_3_Changes(changeInfos);
		add_v0_4_2_Changes(changeInfos);
		add_v0_4_1_Changes(changeInfos);
		add_v0_4_0_Changes(changeInfos);
	}
	
	public static void add_v0_4_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.4.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 16th, 2016\n" +
				"_-_ 37 days after Shattered v0.4.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Added rankings and hall of heroes sync via Google Play Games, for the Google Play version of Shattered.\n\n" +
				"_-_ Added Power Saver mode in settings\n" +
				"_-_ Download size reduced by ~25%\n" +
				"_-_ Game now supports small screen devices\n" +
				"_-_ Performance improvements\n" +
				"_-_ Improved variety of level visuals"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FLAIL, null), "Balance Changes",
				"_-_ Flail max damage increased by ~15%\n" +
				"_-_ Wand of Frost damage reduction increased from 5% per turn of chill to 7.5%\n" +
				"_-_ Ring of Furor speed bonus reduced by ~15% for slow weapons, ~0% for fast weapons\n" +
				"_-_ Reduced sacrificial curse bleed by ~33%\n" +
				"_-_ Reworked glyph of brimstone, now grants shielding instead of healing\n" +
				"_-_ Reworked glyph of stone, now reduces speed in doorways\n" +
				"_-_ Thrown potions now trigger traps and plants"));
	}
	
	public static void add_v0_4_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.4.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 9th, 2016\n" +
				"_-_ 46 days after Shattered v0.4.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Many general performance improvements\n" +
				"_-_ Game now uses 2 CPU cores, up from 1\n" +
				"_-_ Reduced hitching on many devices\n" +
				"_-_ Framerate improvements for older devices\n" +
				"_-_ Game size reduced by ~10%"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Glaive()), "Balance Changes",
				"_-_ Spear and Glaive damage reduced\n" +
				"_-_ Runic blade damage reduced\n" +
				"_-_ Grim enchant now procs more often\n" +
				"_-_ Glyph of stone adds more weight\n" +
				"_-_ Glyph of potential procs less often\n" +
				"_-_ Wand of Fireblast less dangerous to caster\n" +
				"_-_ Wand of Pris. Light reveal area reduced\n" +
				"_-_ Ring of Wealth slightly more effective\n" +
				"_-_ Ring of Sharpshooting gives more accuracy"));
	}
	
	public static void add_v0_4_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.4.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released July 25th, 2016\n" +
				"_-_ 35 days after Shattered v0.4.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new PlateArmor()), "Item Changes pt.1",
				"Armor and Enemy Balance Changes:\n" +
				"_-_ Armor now has a min damage block value\n" +
				"_-_ Armor gains more blocking from upgrades\n" +
				"_-_ Prison+ enemy damage increased\n" +
				"_-_ Evil Eyes reworked\n" +
				"_-_ Brimstone glyph healing reduced\n" +
				"\n" +
				"Class Balance Changes:\n" +
				"_-_ Mage's Staff melee damage increased\n" +
				"_-_ Mage's Staff can now preserve one upgrade\n" +
				"_-_ Cloak of Shadows buffed at lower levels\n" +
				"_-_ Some Battlemage effects changed\n" +
				"\n" +
				"Wand Balance Changes:\n" +
				"_-_ All wands damage adjusted/increased\n" +
				"_-_ Upgraded wands appear slightly less often\n" +
				"_-_ Wand of Lightning bonus damage reduced\n" +
				"_-_ Wand of Fireblast uses fewer charges\n" +
				"_-_ Wand of Venom damage increases over time\n" +
				"_-_ Wand of Prismatic Light bonus damage reduced\n" +
				"_-_ Corrupted enemies live longer & no longer attack eachother\n" +
				"_-_ Wands in the holster now charge faster"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new RunicBlade()), "Item Changes pt.2",
				"Ring Balance Changes:\n" +
				"_-_ Ring of Force weaker at 18+ strength, stronger otherwise\n" +
				"_-_ Ring of Tenacity reduces more damage\n" +
				"_-_ Ring of Wealth secret rewards adjusted\n" +
				"_-_ Ring of Evasion now works consistently\n" +
				"\n" +
				"Artifact Balance Changes:\n" +
				"_-_ Dried Rose charges faster, ghost HP up\n" +
				"_-_ Horn of Plenty now charges on exp gain\n" +
				"_-_ Master Thieves Armband levels faster\n" +
				"_-_ Sandals of Nature level faster\n" +
				"_-_ Hourglass uses fewer charges at a time\n" +
				"_-_ Horn of Plenty adjusted, now stronger\n" +
				"\n" +
				"Weapon Balance Changes:\n" +
				"_-_ Lucky enchant deals max dmg more often\n" +
				"_-_ Dazzling enchant now cripples & blinds\n"+
				"_-_ Flail now can't surprise attack, damage increased\n" +
				"_-_ Extra reach weapons no longer penetrate\n" +
				"_-_ Runic blade damage decreased"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Added a new journal button with key display\n" +
				"_-_ Keys now exist in the journal, not inventory\n" +
				"_-_ Improved donator menu button visuals\n" +
				"_-_ Increased the efficiency of data storage\n" +
				"_-_ Chasms now deal less damage, but bleed\n" +
				"_-_ Many shop prices adjusted\n" +
				"_-_ Pirahna rooms no longer give cursed gear"));
	}
	
	public static void add_v0_4_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.4.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 20th, 2016\n" +
				"_-_ 391 days after Shattered v0.3.0\n" +
				"_-_ 50 days after Shattered v0.3.5\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Longsword()), "Equipment Overhaul!",
				"_-_ 13 new weapons, 12 rebalanced weapons\n" +
				"\n" +
				"Equipment Balance:\n" +
				"_-_ Tier 2-4 weapons do more base damage\n" +
				"_-_ All weapons gain more dmg from upgrades\n" +
				"_-_ Upgrades now remove enchants less often\n" +
				"_-_ Upgrades reduce str requirements less\n" +
				"_-_ All armors require 1 more str\n" +
				"_-_ Encumbered characters can't sneak attack\n" +
				"\n" +
				"Droprate Changes:\n" +
				"_-_ Powerful equipment less common early\n" +
				"_-_ +3 and +2 equipment less common\n" +
				"_-_ Equipment curses more common\n" +
				"_-_ Tier 1 equipment no longer drops\n" +
				"_-_ Arcane styli slightly more common\n" +
				"_-_ Better item drops on floors 22-24"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Stylus()), "Curse, Enchant, & Glyph Overhaul!",
				"_-_ 3 new enchants, 10 rebalanced enchants\n" +
				"_-_ 8 new glyphs, 5 rebalanced glyphs\n" +
				"_-_ 12 new curse effects\n" +
				"\n" +
				"Equipment Curses:\n" +
				"_-_ Curses now give negative effects\n" +
				"_-_ Curses no longer give negative levels\n" +
				"_-_ Upgrades now weaken curses\n" +
				"_-_ Remove curse scrolls now affect 1 item"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Class Balance:\n" +
				"_-_ Huntress now starts with knuckleduster\n" +
				"_-_ Assassin sneak bonus damage reduced\n" +
				"_-_ Fixed a bug where berserker was immune when enraged\n" +
				"_-_ Gladiator's clobber now inflicts vertigo, not stun\n" +
				"\n" +
				"Enemy Balance:\n" +
				"_-_ Tengu damage increased\n" +
				"_-_ Prison Guard health and DR increased\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Scrolls of upgrade no longer burn\n" +
				"_-_ Potions of strength no longer freeze\n" +
				"_-_ Translation updates\n" +
				"_-_ Various bugfixes"));
	}
}
