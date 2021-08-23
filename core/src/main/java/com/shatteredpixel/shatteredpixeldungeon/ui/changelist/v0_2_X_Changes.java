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
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_2_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "v0.2.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		add_v0_2_4_Changes(changeInfos);
		add_v0_2_3_Changes(changeInfos);
		add_v0_2_2_Changes(changeInfos);
		add_v0_2_1_Changes(changeInfos);
		add_v0_2_0_Changes(changeInfos);
		
		changes = new ChangeInfo(" ", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
	}
	
	public static void add_v0_2_4_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.2.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 23rd, 2015\n" +
				"_-_ 48 days after Shattered v0.2.3\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Honeypot()), "Pixel Dungeon v1.7.5",
				"v1.7.3 - v1.7.5 Source Implemented, with exceptions:\n" +
				"_-_ Degredation not implemented.\n\n" +
				"_-_ Badge syncing not implemented.\n\n" +
				"_-_ Scroll of Weapon Upgrade renamed to Magical Infusion, works on armor.\n\n" +
				"_-_ Scroll of Enchantment not implemented, Arcane stylus has not been removed.\n\n" +
				"_-_ Honey pots now shatter in a new item: shattered honeypot. A bee will defend its shattered pot to the death against anything that gets near.\n\n" +
				"_-_ Bombs have been reworked/nerfed: they explode after a delay, no longer stun, deal more damage at the center of the blast, affect the world (destroy items, blow up other bombs)."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BANDOLIER, null), "New Content",
				"_-_ The huntress has been buffed: starts with Potion of Mind Vision identified, now benefits from strength on melee attacks, and has a chance to reclaim a single used ranged weapon from each defeated enemy.\n\n" +
				"_-_ A new container: The Potion Bandolier! Potions can now shatter from frost, but the bandolier can protect them.\n\n" +
				"_-_ Shops now stock a much greater variety of items, some item prices have been rebalanced.\n\n" +
				"_-_ Added Merchant's Beacon.\n\n" +
				"_-_ Added initials for IDed scrolls/potions."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Going down stairs no longer increases hunger, going up still does.\n\n" +
				"_-_ Many, many bugfixes.\n" +
				"_-_ Some UI improvements.\n" +
				"_-_ Ingame audio quality improved.\n" +
				"_-_ Unstable spellbook buffed.\n" +
				"_-_ Psionic blasts deal less self-damage.\n" +
				"_-_ Potions of liquid flame affect a 3x3 grid."));
	}
	
	public static void add_v0_2_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.2.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released January 6th, 2015\n" +
				"_-_ 64 days after Shattered v0.2.2\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new TimekeepersHourglass()), "Artifact Changes",
				"Added 4 new artifacts:\n" +
				"_-_ Alchemist's Toolkit\n" +
				"_-_ Unstable Spellbook\n" +
				"_-_ Timekeeper's Hourglass\n" +
				"_-_ Dried Rose\n\n" +
				"_-_ Artifacts are now unique over each run\n" +
				"_-_ Artifacts can now be cursed!\n" +
				"_-_ Cloak of Shadows is now exclusive to the rogue\n" +
				"_-_ Smaller Balance Changes and QOL improvements to almost every artifact"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), "Balance Changes",
				"_-_ Health potion farming has been nerfed from all sources\n" +
				"_-_ Freerunner now moves at very high speeds when invisible\n" +
				"_-_ Ring of Force buffed significantly\n" +
				"_-_ Ring of Evasion reworked again\n" +
				"_-_ Improved the effects of some blandfruit types\n" +
				"_-_ Using throwing weapons now cancels stealth"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Implemented a donation system in the Google Play version of Shattered\n\n" +
				"_-_ Significantly increased the stability of the save system\n\n" +
				"_-_ Increased the number of visible rankings to 11 from 6\n\n" +
				"_-_ Improved how the player is interrupted by harmful events"));
	}
	
	public static void add_v0_2_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.2.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 3rd, 2014\n" +
				"_-_ 21 days after Shattered v0.2.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_AUGMENTATION, null), "Pixel Dungeon v1.7.2",
				"Implemented directly from v1.7.2:\n" +
				"_-_ Synchronous Movement\n" +
				"_-_ Challenges\n" +
				"_-_ UI improvements\n" +
				"_-_ Vertigo debuff\n\n" +
				"Implement with changes:\n" +
				"_-_ Weightstone: Increases either speed or damage, at the cost of the other, instead of increasing either speed or accuracy.\n\n" +
				"Not Implemented:\n" +
				"_-_ Key ring and unstackable keys\n" +
				"_-_ Blindweed has not been removed"));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 112, 112, 16, 16), "New Plants",
				"Added two new plants:\n" +
				"_-_ Stormvine, which brews into levitation\n" +
				"_-_ Dreamfoil, which brews into purity\n\n" +
				"_-_ Potion of levitation can now be thrown to make a cloud of confusion gas\n\n" +
				"_-_ Removed gas collision logic, gasses can now stack without limitation."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS, null), "Heroes Remains",
				"Heroes remains have been significantly adjusted to prevent strategies that exploit them, but also to increase their average loot.\n\n" +
				"Remains have additional limitations:\n" +
				"_-_ Heros will no longer drop remains if they have obtained the amulet of yendor, or die 5 or more floors above the deepest floor they have reached\n" +
				"_-_ Class exclusive items can no longer appear in remains\n" +
				"_-_ Items found in remains are now more harshly level-capped\n" +
				"_-_ Remains are not dropped, and cannot be found, when challenges are enabled.\n\n" +
				"However:\n" +
				"_-_ Remains can now contain useful items from the inventory, not just equipped items.\n" +
				"_-_ Remains are now less likely to contain gold."));
	}
	
	public static void add_v0_2_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.2.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 13th, 2014\n" +
				"_-_ 28 days after Shattered v0.2.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.GHOST, 0, 0, 14, 15), "New Sewer Quests",
				"_-_ Removed the dried rose quest (the rose will return...)\n\n" +
				"_-_ Tweaked the mechanics of the fetid rat quest\n\n" +
				"_-_ Added a gnoll trickster quest\n\n" +
				"_-_ Added a great crab quest"));
		
		changes.addButton( new ChangeButton(new Image(Assets.GOO, 43, 3, 14, 11), "Goo Changes",
				"Goo's animations have been overhauled, including a particle effect for the area of its pumped up attack.\n\n" +
				"Goo's arena has been updated to give more room to maneuver, and to be more variable."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null), "Story & Signpost Changes",
				"Most text in the sewers has been overhauled, including descriptions, quest dialogues, signposts, and story scrolls"));
	}
	
	public static void add_v0_2_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.2.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 15th, 2014\n" +
				"_-_ 31 days after Shattered v0.1.1"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new HornOfPlenty()), "Artifacts!",
				"Added artifacts to the game!\n\n" +
				"Artifacts are unique items which offer new gameplay opportunities and grow stronger through unique means.\n\n" +
				"Removed 7 Rings... And Replaced them with 7 Artifacts!\n" +
				"_-_ Ring of Shadows becomes Cloak of Shadows\n" +
				"_-_ Ring of Satiety becomes Horn of Plenty\n" +
				"_-_ Ring of Mending becomes Chalice of Blood\n" +
				"_-_ Ring of Thorns becomes Cape of Thorns\n" +
				"_-_ Ring of Haggler becomes Master Thieves' Armband\n" +
				"_-_ Ring of Naturalism becomes Sandals of Nature"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null), "New Rings!",
				"To replace the lost rings, 6 new rings have been added:\n" +
				"_-_ Ring of Force\n" +
				"_-_ Ring of Furor\n" +
				"_-_ Ring of Might\n" +
				"_-_ Ring of Tenacity\n" +
				"_-_ Ring of Sharpshooting\n" +
				"_-_ Ring of Wealth\n\n" +
				"The 4 remaining rings have also been tweaked or reworked entirely:\n" +
				"_-_ Ring of Accuracy\n" +
				"_-_ Ring of Elements\n" +
				"_-_ Ring of Evasion\n" +
				"_-_ Ring of Haste"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"-Nerfed farming health potions from fly swarms.\n\n" +
				"-Buffed crazed bandit and his drops.\n\n" +
				"-Made Blandfruit more common.\n\n" +
				"-Nerfed Assassin bonus damage slightly, to balance with him having an artifact now.\n\n" +
				"-Added a welcome page!"));
	}
}
