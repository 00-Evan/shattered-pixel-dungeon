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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_3_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.3.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_3_5_Changes(changeInfos);
		add_v0_3_4_Changes(changeInfos);
		add_v0_3_3_Changes(changeInfos);
		add_v0_3_2_Changes(changeInfos);
		add_v0_3_1_Changes(changeInfos);
		add_v0_3_0_Changes(changeInfos);
	}
	
	public static void add_v0_3_5_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 1st, 2016\n" +
				"_-_ 81 days after Shattered v0.3.4\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Rework!",
				"Warrior Rework:\n" +
				"_-_ Starting STR down to 10, from 11\n" +
				"_-_ Short sword dmg down to 1-10, from 1-12\n" +
				"_-_ Short sword can no longer be reforged\n" +
				"_-_ Now IDs potions of health, not STR\n" +
				"_-_ Now starts with a unique seal for armor\n" +
				"_-_ Seal grants shielding ontop of health\n" +
				"_-_ Seal allows for one upgrade transfer"));
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Warrior Subclass Rework!",
				"Berserker Rework:\n" +
				"_-_ Bonus damage now scales with lost HP, instead of a flat 50% at 50% hp\n" +
				"_-_ Berserker can now endure through death for a short time, with caveats\n" +
				"\n" +
				"Gladiator Rework:\n" +
				"_-_ Combo no longer grants bonus damage\n" +
				"_-_ Combo is now easier to stack\n" +
				"_-_ Combo now unlocks special finisher moves"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Balance Tweaks:\n" +
				"_-_ Spears can now reach enemies 1 tile away\n" +
				"_-_ Wand of Blast Wave now pushes bosses less\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Can now examine multiple things in one tile\n" +
				"_-_ Pixelated font now available for cyrillic languages\n" +
				"_-_ Added Hungarian language"));
	}
	
	public static void add_v0_3_4_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 10th, 2016\n" +
				"_-_ 54 days after Shattered v0.3.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "Translations!",
				"Shattered Pixel Dungeon now supports multiple languages, thanks to a new community translation project!\n\n" +
				"The Following languages are supported at release:\n" +
				"_-_ English\n" +
				"_-_ Russian\n" +
				"_-_ Korean\n" +
				"_-_ Chinese\n" +
				"_-_ Portugese\n" +
				"_-_ German\n" +
				"_-_ French\n" +
				"_-_ Italian\n" +
				"_-_ Polish\n" +
				"_-_ Spanish"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Completely redesigned the text rendering system to support none-english characters\n\n" +
				"New text system renders using either the default system font, or the original pixelated game font. None-latin languages must use system font.\n\n" +
				"Balance Changes:\n" +
				"_-_ Hunger now builds ~10% slower\n" +
				"_-_ Sad Ghost no longer gives tier 1 loot\n" +
				"_-_ Sad Ghost gives tier 4/5 loot less often\n" +
				"_-_ Burning now deals less damage at low HP\n" +
				"_-_ Weakness no longer discharges wands\n" +
				"_-_ Rockfall traps rebalanced"));
	}
	
	public static void add_v0_3_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released December 18th, 2015\n" +
				"_-_ 44 days after Shattered v0.3.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Google Play Games",
				"Added support for Google Play Games in the Google Play version:\n\n" +
				"- Badges can now sync across devices\n" +
				"- Five Google Play Achievements added\n" +
				"- Rankings sync will come in future\n\n" +
				"Shattered remains a 100% offline game if Google Play Games is not enabled"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Gameplay Changes:\n" +
				"- Tengu's maze is now different each time\n" +
				"- Items no longer auto-pickup when enemies are near\n" +
				"\n" +
				"Fixes:\n" +
				"- Fixed several bugs with prison enemies\n" +
				"- Fixed some landscape window size issues\n" +
				"- Fixed other minor bugs\n" +
				"\n" +
				"Misc:\n" +
				"- Added support for reverse landscape\n" +
				"- Added a small holiday treat ;)\n" +
				"- Thieves now disappear when they get away"));
	}
	
	public static void add_v0_3_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 4th, 2015\n" +
				"_-_ 79 days after Shattered v0.3.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TENGU, 0, 0, 14, 16), "Prison Rework",
				"_-_ Tengu boss fight completely redone\n" +
				"_-_ Corpse dust quest overhauled\n" +
				"_-_ Rotberry quest overhauled\n" +
				"_-_ NEW elemental embers quest\n" +
				"_-_ NEW prison mob: insane prison guards!\n" +
				"_-_ Thieves can escape with a stolen item\n" +
				"_-_ Gnoll shaman attack speed increased"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY, null), "Balance Changes",
				"_-_ Mastery Book now always at floor 10, even when unlocked\n" +
				"_-_ Hunger damage now increases with hero level, starts lower\n" +
				"\n" +
				"Sewers rebalance: \n" +
				"_-_ Marsupial rat dmg and evasion reduced\n" +
				"_-_ Gnoll scout accuracy reduced\n" +
				"_-_ Sewer crabs less likely to spawn on floor 3, grant more exp\n" +
				"_-_ Fly swarms rebalanced, moved to the sewers\n" +
				"_-_ Great Crab HP reduced \n" +
				"_-_ Goo fight rebalanced \n" +
				" \n" +
				"Base Class Changes: \n" +
				"_-_ Mage's staff base damage increased \n" +
				"_-_ Huntress now starts with 20 hp \n" +
				"_-_ Huntress no longer heals more from dew \n" +
				"_-_ Rogue's cloak of shadows now drains less while invisible\n" +
				" \n" +
				"Subclass Changes: \n" +
				"_-_ Berserker now starts raging at 50% hp (up from 40%) \n" +
				"_-_ Warden now heals 2 extra HP from dew \n" +
				"_-_ Warlock completely overhauled"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Visual improvements from 1.9.1 source\n" +
				"_-_ Improved golden UI for donators\n" +
				"_-_ Fixed 'white line' graphical artifacts\n" +
				"_-_ Floor locking now pauses all passive effects, not just hunger\n" +
				"_-_ Cursed chains now only cripple, do not root\n" +
				"_-_ Warping trap rebalanced, much less harsh\n" +
				"_-_ Various other bugfixes"));
	}
	
	public static void add_v0_3_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 17th, 2015\n" +
				"_-_ 83 days after Shattered v0.3.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 112, 96, 16, 16), "Trap Overhaul",
				"_-_ Over 20 new traps + tweaks to existing ones\n" +
				"_-_ Trap visuals overhauled\n" +
				"_-_ Traps now get trickier deeper in the dungeon\n" +
				"_-_ Trap room reworked to make use of new traps"));
		
		changes.addButton( new ChangeButton(new Image(Assets.MENU, 15, 0, 16, 15), "Interface Improvements",
				"_-_ Adjusted display scaling\n" +
				"_-_ Search and Examine merged into one button (double tap to search)\n" +
				"_-_ New max of 4 Quickslots!\n" +
				"_-_ Multiple toolbar modes for large display and landscape users\n" +
				"_-_ Ability to flip toolbar and indicators (left-handed mode)\n" +
				"_-_ Better settings menu\n" +
				"_-_ Graphics settings are now accessible ingame\n" +
				"_-_ More consistent text rendering\n" +
				"_-_ Recent changes can now be viewed from the title screen\n" +
				"_-_ Added a health bar for bosses"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Balance changes:\n" +
				"_-_ Ethereal chains now gain less charge the more charges they have\n" +
				"_-_ Staff of regrowth grants more herbal healing\n" +
				"_-_ Monks now disarm less randomly, but not less frequently\n" +
				"_-_ Invisibility potion effect now lasts for 20 turns, up from 15\n\n" +
				"QOL improvements:\n" +
				"_-_ Quickslots now autotarget enemies\n" +
				"_-_ Resting now works while hungry & at max HP\n" +
				"_-_ Dew drops no longer collect when at full health with no dew vial\n" +
				"_-_ Items now stay visible in the fog of war\n" +
				"_-_ Added a visual hint to weak floor rooms\n" +
				"_-_ Many bugfixes"));
	}
	
	public static void add_v0_3_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.3.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 26th, 2015\n" +
				"_-_ 253 days after Shattered v0.2.0\n" +
				"_-_ 92 days after Shattered v0.2.4\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.MAGE, 0, 15, 12, 15), "Mage Rework!",
				"_-_ No longer starts with knuckledusters or a wand\n" +
				"_-_ Can no longer equip wands\n" +
				"_-_ Now starts with a unique mages staff, empowered with magic missile to start.\n\n" +
				"_-_ Battlemage reworked, staff now deals bonus effects when used as a melee weapon.\n\n" +
				"_-_ Warlock reworked, gains more health and fullness from gaining exp, but food no longer restores hunger."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_DISINTEGRATION, null), "Wand Rework!",
				"Removed Wands:\n" +
				"Flock, Blink, Teleportation, Avalanche\n" +
				"\n" +
				"Reworked Wands:\n" +
				"Magic Missile, Lightning, Disintegration,\n" +
				"Fireblast (was Firebolt), Venom (was Poison),\n" +
				"Frost (was Slowing), Corruption (was Amok),\n" +
				"Blast Wave (was Telekinesis), Regrowth\n" +
				"\n" +
				"New Wands:\n" +
				"Prismatic Light, Transfusion\n" +
				"\n" +
				"_-_ Wand types are now known by default.\n" +
				"_-_ Wands now each have unique sprites.\n" +
				"_-_ Wands now cap at 10 charges instead of 9\n" +
				"_-_ Wands now recharge faster the more charges are missing.\n" +
				"_-_ Self-targeting with wands is no longer possible.\n" +
				"_-_ Wand recharge effects now give charge over time.\n" +
				"_-_ Wands can now be cursed!"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"New Artifacts:\n" +
				"_-_ Ethereal Chains (replaces wand of blink)\n" +
				"_-_ Lloyd's Beacon (replaces wand of teleportation)\n" +
				"\n" +
				"Misc. Balance changes:\n" +
				"_-_ Blessed Ankhs now revive at 1/4hp, but also grant initiative.\n" +
				"_-_ Alchemist's Toolkit removed (will be reworked)\n" +
				"_-_ Chalice of blood nerfed, now regens less hp at high levels.\n" +
				"_-_ Cape of Thorns buffed, now absorbs all damage, but only deflects adjacent attacks.\n" +
				"_-_ Sandals of nature adjusted, now give more seeds, less dew.\n" +
				"_-_ Hunger no longer increases while fighting bosses.\n" +
				"_-_ Floor 1 now spawns 10 rats, exactly enough to level up.\n" +
				"_-_ Scrolls of recharging and mirror image now more common.\n" +
				"_-_ Mimics are now less common, stronger, & give better loot.\n" +
				"\n" +
				"UI tweaks:\n" +
				"_-_ New app icon!\n" +
				"_-_ Shading added to main game interface\n" +
				"_-_ Buffs now have descriptions, tap their icons!\n" +
				"_-_ Visual indicator added for surprising enemies"));
	}
}
