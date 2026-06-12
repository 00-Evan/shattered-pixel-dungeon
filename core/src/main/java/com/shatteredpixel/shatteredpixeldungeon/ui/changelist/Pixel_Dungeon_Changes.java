/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.sprites.BatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EyeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MonkSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class Pixel_Dungeon_Changes {

	//just the one, which contains all PD changes
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_PD_Changes(changeInfos);
	}

	public static void add_PD_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("Pixel Dungeon", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.AMULET), "v1.9.X & v1.8.X",
				"_Developer Commentary:_\n" +
				"Shattered Pixel Dungeon is based on the open source code of Pixel Dungeon, which was developed by Watabou from 2012 to 2015.\n" +
				"\n" +
				"This section contain's Watabou's original release notes (with minor edits) for every numbered update to the original Pixel Dungeon.\n" +
						"\n" +
				"Note that the timelines for Pixel Dungeon and Shattered Pixel Dungeon overlap a bit during 2014-2016:\n" +
				"**-** v0.1.0 was based on PD v1.7.1\n"+
				"**-** v0.2.2 added most changes from PD v1.7.2\n"+
				"**-** v0.2.4 added some changes up to PD v1.7.5\n"+
				"**-** v0.3.2 added a few changes up to PD v1.9.1\n"+
				"**-** v0.4.3 added a few changes from PD v1.9.2"
				,
				"**v1.9.2:** (December 1st, 2015)\n" +
				"**-** Catalogus now show the number of currently owned identified potions and scrolls, and you can use them right from there.\n" +
				"**-** Improved tiles, Nothing dramatic here, but the map should look less \"regular\" now.\n" +
				"**-** New Berserker’s perk: \"The more enemies surrounding the Berserker, the faster he performs his attacks.\"\n" +
				"**-** Any wand can be merged with another wand of the same type.\n" +
				"**-** Wand of Flock spawns fewer sheep, but they block line of sight and live longer:\n" +
				"**-** Wand of Avalanche is buffed\n" +
				"**-** Wand of Firebolt is slightly nerfed\n" +
				"**-** Sacrificial mark gets propagated on attack (i.e. mark yourself then attack an enemy)\n" +
				"**-** All enemies on a depth get beckoned when the Hero or an enemy steps into sacrificial fire.\n" +
				"**-** Scroll of Wipe Out can now burn, and it can be stored in the Scroll Holder.\n" +
				"**-** Some typos fixed.\n" +
				"\n" +
				"and more..."
				,
				"**v1.9.1:** (September 4th, 2015)\n" +
				"**-** Added: New armor enchantment - Auto-Repair\n" +
				"**-** Added: New vfx\n" +
				"**-** Changed: Wand of Avalanche \"presses\" affected tiles\n" +
				"**-** Changed: Scroll of Wipe Out turns Shop goods and Mimics into regular heaps\n" +
				"**-** Fixed: Several major and minor bugs\n" +
				"**-** Fixed: Typos"
				,
				"**v1.9.0:** (August 1st, 2015)\n" +
				"Initially, these features were meant to be a part of the previous update, but I was afraid to jam too many of them into a single release.\n" +
				"So it’s more like “1.8.0, Part 2”:\n" +
				"**-** Added: New room - Sacrificial Chamber\n" +
				"**-** Added: Auto-targetting for wands and missile weapons\n" +
				"**-** Added: Read animation\n" +
				"**-** Changed: Degradation system\n" +
				"**-** Changed: Durability of all items is slightly increased\n" +
				"**-** Changed: Wands of Telekinesis are replaced with Wands of Reach with a different effect on monsters\n" +
				"**-** Changed: The way score is calculated\n" +
				"**-** Fixed: Bugs and typos\n" +
				"...and much more...\n" +
				"\n" +
				"About changes in degradation system:\n" +
				"When durability of an item decreases to zero, it doesn’t degrade now. Instead, it keeps its level, but it breaks. Broken items can be used as normal ones, but of course, they perform worse. For fixing them, the same means are used as for restoring durability."
				,
				"**v1.8.0:** (July 7th, 2015)\n" +
				"**-** Added: New quest from the Sad Ghost\n" +
				"**-** Added: New quest from the Old Wandmaker\n" +
				"**-** Added: Tome of Remastery\n" +
				"**-** Changed: Badges screen layout\n" +
				"**-** Changed: Scrollpanes display scroll position\n" +
				"**-** Changed: Improved game log\n" +
				"**-** Changed: Scrolls of Challenge apply Rage debuff.\n" +
				"\n" +
				"and much more..."));

		changes.addButton( new ChangeButton( Icons.get(Icons.CHALLENGE_COLOR), "v1.7.X",
				"**v1.7.5:** (January 22nd, 2015)\n" +
				"**-** Added: New monster type\n" +
				"**-** Added: New sounds\n" +
				"**-** Added: Durability indicators in the inventory window\n" +
				"**-** Changed: Scrolls of Enchantment replace Arcane Styli\n" +
				"**-** Changed: Using a fully charged wand as a melee weapon precipitates degradation\n" +
				"**-** Changed: Both quickslot items are displayed in the Ranking window\n" +
				"**-** Fixed: Several bugs and typos\n" +
				"\n" +
				"I'm going to publish the source code of this version by the end of the week (skipping 1.7.3 and 1.7.4). "
				,
				"**v1.7.4:** (December 10th, 2014)\n" +
				"**2nd Anniversary Version**\n" +
				"\n" +
				"**-** Added: New item - Bomb\n" +
				"**-** Added: New item - Honeypot\n" +
				"**-** Added: New enchantment - Shocking weapon (replaces Wild and Piercing enchantments)\n" +
				"**-** Changed: Upgradable items degrade when used\n" +
				"**-** Changed: Items dropped into chasm can be found on next level\n" +
				"\n" +
				"(Note from Evan: It's not emphasized in this changelog at all, but degradation was a major change. Prior to this update upgrades were permanent, but afterward they would steadily be lost as an item was used. Each upgrade would reset the timer until an upgrade is lost, but higher level items degraded faster.)"
				,
				"**v1.7.3:** (October 18th, 2014)\n" +
				"**-** Added: Better landscape orientation support\n" +
				"**-** Added: Badges synchronization across multiple devices *\n" +
				"**-** Added: New challenge - Forbidden runes\n" +
				"**-** Added: Numerous UI improvements incl. the second quickslot\n" +
				"**-** Changed: Goo’s behaviour & visuals (slightly)\n" +
				"**-** Changed: Wands get autoidentified after a number of usages\n" +
				"**-** Changed: King of Dwarves & Rotting Fist are now immune to vertigo\n" +
				"**-** Changed: Challenges are renamed\n" +
				"**-** Changed: “Sniper’s mark” is replaced with “Zeroed in”\n" +
				"**-** Fixed: Several minor bugs\n" +
				"\n" +
				"and more...\n" +
				"\n" +
				"*This one may be potentially buggy. "
				,
				"**v1.7.2:** (August 11th, 2014)\n" +
				"**-** Added: Immersive mode (switched off by default)\n" +
				"**-** Added: Synchronous mobs movement\n" +
				"**-** Added: Challenges (options for new game+)\n" +
				"**-** Added: New item - weightstone\n" +
				"**-** Changed: Poison mechanics\n" +
				"**-** Changed: Toxic gas (slightly)\n" +
				"**-** Changed: Blindweed is replaced by Dreamweed\n" +
				"**-** Changed: Keys are no longer stackable\n" +
				"**-** Changed: Mirror images are now immune to toxic gas and fire\n" +
				"**-** Changed: Inscriptions increase armor DR\n" +
				"**-** Fixed: Unresponsive error windows\n" +
				"**-** Fixed: Wand charging exploit\n" +
				"\n" +
				"and more...\n" +
				"\n" +
				"Currently implemented challenges look impossible. Maybe they ARE impossible. Feel free to send me your ideas for more adequate ones. "
				,
				"**v1.7.1:** (May 28th, 2014)\n" +
				"(Note from Evan: The source for v1.7.1 was released a bit later on July 27th. At that time Watabou said \"I hope to see a full-fledged mod of Pixel Dungeon one day :)\" )\n" +
				"\n" +
				"This is a really small update. Probably this is the smallest update of Pixel Dungeon ever! It was supposed to be a hotfix, but while working on it I’ve added a couple of new features, so it cannot be counted as a hotfix :). Anyway, here is the list of changes:\n" +
				"\n" +
				"**-** Added: Shake effect\n" +
				"**-** Added: New room type\n" +
				"**-** Changed: Inventory improved\n" +
				"**-** Changed: Class armor usage is restricted\n" +
				"**-** Changed: Leaving the dungeon at night\n" +
				"**-** Changed: Some visual are modified\n" +
				"**-** Fixed: Several bugs\n" +
				"\n" +
				"Usually I add \"and more...\" here, but this time that’s actually all. "
				,
				"**v1.7.0:** (May 15th, 2014)\n" +
				"**-** Added: Now it’s possible to leave the dungeon after obtaining the amulet\n" +
				"**-** Added: New caves tiles\n" +
				"**-** Added: New water textures\n" +
				"**-** Added: New Freerunner’s perk\n" +
				"**-** Changed: The dew vial can be filled in a well of health\n" +
				"**-** Changed: Drinking from a well of awareness reveals all secrets on a level\n" +
				"**-** Changed: Items cannot be dropped into a locked chest\n" +
				"**-** Changed: At least one well of transmutation per game\n" +
				"**-** Changed: Wand of Lightning is buffed\n" +
				"**-** Fixed: Numerous bugs\n" +
				"\n" +
				"and much more... "));

		changes.addButton( new ChangeButton( new Image(new GhostSprite()), "v1.6.X",
				"**v1.6.4:** (March 5th, 2014)\n" +
				"**-** Added: Ring of Power (replaces Ring of Energy)\n" +
				"**-** Added: Ring of Elements (replaces Rings of Cleansing and Resistance)\n" +
				"**-** Added: Ring of Herbalism\n" +
				"**-** Added: Wand holster\n" +
				"**-** Added: Alternative Imp quest\n" +
				"**-** Added: Level feelings\n" +
				"**-** Added: New sounds\n" +
				"**-** Added: Bright mode\n" +
				"**-** Added: Numerous UI improvements\n" +
				"**-** Changed: Boomerang can be enchanted\n" +
				"**-** Changed: Frost damages fire elementals\n" +
				"**-** Changed: Only seeds can be placed into an alchemy pot\n" +
				"**-** Fixed: Lots of bugs\n" +
				"\n" +
				"...and more "
				,
				"**v1.6.3:** (February 20th, 2014)\n" +
				"**-** Added: Journal (now I think I should have named it “Dungeon journal”)\n" +
				"**-** Added: Alternative quest for the troll blacksmith\n" +
				"**-** Added: New glyph - armor of displacement\n" +
				"**-** Added: New glyph - armor of entanglement\n" +
				"**-** Added: Non-rectangular rooms\n" +
				"**-** Added: New fonts\n" +
				"**-** Added: Additional info in rankings\n" +
				"**-** Changed: New level layouts\n" +
				"**-** Changed: Field of view is “round” now\n" +
				"**-** Changed: Inscribed armors are more common now\n" +
				"**-** Changed: Arcane styli are rarer now\n" +
				"**-** Fixed: Several bugs and typos\n" +
				"\n" +
				"...and more... "
				,
				"**v1.6.2:** (January 27th, 2014)\n" +
				"**-** Added: New glyph - armor of stench\n" +
				"**-** Added: New glyph - armor of viscosity\n" +
				"**-** Added: New quest-giving NPC\n" +
				"**-** Added: Dew vial\n" +
				"**-** Changed: New Rankings screen\n" +
				"**-** Changed: Some glyphs are tweaked\n" +
				"**-** Changed: Items dropped atop of shopkeeper’s goods can be picked up for free\n" +
				"**-** Fixed: Loads of bugs\n" +
				"\n" +
				"...and more "
				,
				"**v1.6.1:** (January 3rd, 2014)\n" +
				"Not too many additions and changes here:\n" +
				"\n" +
				"**-** Added: Armor glyphs\n" +
				"**-** Added: New quest-giving NPC\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New visuals\n" +
				"**-** Added: New images for some items\n" +
				"**-** Changed: Burning provides illumination\n" +
				"**-** Changed: Interaction with a mirror images lets you change places with it\n" +
				"**-** Changed: Catalogus now displays all potions and scrolls (incl. unidentified)\n" +
				"**-** Changed: Cloth, leather & chain armors are slightly buffed, plate armor nerfed\n" +
				"**-** Changed: Different aspects of game balance are tweaked\n" +
				"**-** Fixed: Tons of bugs\n" +
				"\n" +
				"...and more... "
				,
				"**v1.6.0:** (December 2nd, 2013)\n" +
				"**-** It’s not beta anymore!\n" +
				"**-** Added: Backstory texts\n" +
				"**-** Added: New NPCs (more in next updates)\n" +
				"**-** Added: New terrain type - chasm\n" +
				"**-** Added: New missile weapon - tomahawk\n" +
				"**-** Added: New enchantment - lucky weapon\n" +
				"**-** Added: New sounds\n" +
				"**-** Added: New visual effects\n" +
				"**-** Added: New perks for Huntress\n" +
				"**-** Changed: Javelin applies Cripple debuff\n" +
				"**-** Changed: Wild and piercing weapons always proc\n" +
				"**-** Changed: Enchantements on degraded weapons work like on +0 weapons\n" +
				"**-** Changed: Freezing traps are replaced with Gripping traps\n" +
				"**-** Fixed: A lot of bugs\n" +
				"\n" +
				"...and more... "));

		changes.addButton( new ChangeButton( new Image(new EyeSprite()), "v0.5.X",
				"**v0.5.4:** (October 24th, 2013)\n" +
				"Pixel Dungeon 0.5.4 released! The focus of this update was mainly on visual aspect of the game, I tried to make it a little more animated. But there are also some new features in a sense of gameplay. For example, now you can find remains of your previous hero, perhaps even with some of his equipment.\n" +
				"\n" +
				"**-** Added: Animated level features\n" +
				"**-** Added: New wands visual effects\n" +
				"**-** Added: New visual effects for gases, fire etc.\n" +
				"**-** Added: Skeletal remains\n" +
				"**-** Added: More variance in rooms decoration\n" +
				"**-** Added: New sounds\n" +
				"**-** Added: New badges\n" +
				"**-** Changed: All wands get identified after the first use\n" +
				"**-** Changed: Potion of Purity is renamed to Potion of Purification and now it can be drunk\n" +
				"**-** Changed: Toxic gas displaces paralytic gas\n" +
				"**-** Fixed: A whole lot of bugs\n" +
				"\n" +
				"and more... "
				,
				"**v0.5.3:** (October 4th, 2013)\n" +
				"Ok, Pixel Dungeon 0.5.3 is released. There is one more class now, it’s a Huntress! This character class is unlockable, you need to slain DM-300 to unlock it (but you don’t need to slain it again, if you did it before). There are also very many small changes in this update and I’m too lazy to list them all :)\n" +
				"\n" +
				"**-** Added: New character class - Huntress\n" +
				"**-** Added: New badges\n" +
				"**-** Added: Plants descriptions\n" +
				"**-** Changed: Rogue’s perks have been changed a little\n" +
				"**-** Changed: Some rooms have been changed a little\n" +
				"**-** Changed: Some effects have been changed a little\n" +
				"**-** Fixed: Bug with blobs (i.e. fire, gases etc)\n" +
				"**-** Fixed: Bug that cause a crash when fighting statues\n" +
				"**-** Fixed: Bug with throwing equipped missile weapons\n" +
				"\n" +
				"and more... "
				,
				"**v0.5.2:** (September 22nd, 2013)\n" +
				"Pixel Dungeon 0.5.2 released! This is a relatively small update, its main feature is Alchemy. Here is the full list of changes:\n" +
				"\n" +
				"**-** Added: Alchemy (find an alchemy pot to transform seeds into potions)\n" +
				"**-** Added: Rare monsters\n" +
				"**-** Added: New weapon enchantments\n" +
				"**-** Added: New missile weapon - boomerang\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Library and laboratory rooms are slightly redesigned\n" +
				"**-** Changed: Excessive strength doesn’t grant damage bonus for missile weapons\n" +
				"**-** Changed: Items can be picked up from a distance with a Wand of Telekinesis\n" +
				"**-** Fixed: Ring of Resistance+5 bug\n" +
				"**-** Fixed: Scroll holder bug"
				,
				"**v0.5.1:** (August 27th, 2013)\n" +
				"Pixel Dungeon 0.5.1 released! Several bugs are fixed there, that was the main focus of this update. Also it includes some new features. Here is the list of changes:\n" +
				"\n" +
				"**-** Added: Scroll holder\n" +
				"**-** Added: Crystal chests room\n" +
				"**-** Added: Scroll of Mirror Image\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Some monsters of the Demon Halls (incl. Yog-Dzewa) are buffed\n" +
				"**-** Changed: Frost extinguishes fire\n" +
				"**-** Fixed: Lots of bugs are fixed (incl. Lloyd’s Beacon bug)\n" +
				"\n" +
				"and more... "
				,
				"**v0.5:** (August 18th, 2013)\n" +
				"Pixel Dungeon 0.5 released! This update introduces new levels of the Demon Halls, new items, monsters and the final boss. Also now you can actually win the game by obtaining the Amulet of Yendor. Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: 5 new levels with new monsters and the final boss\n" +
				"**-** Added: Amulet of Yendor is now obtainable, so you can really win the game\n" +
				"**-** Added: Option to scale up UI on large screen devices\n" +
				"**-** Added: New enchantment - piercing weapon\n" +
				"**-** Added: New items\n" +
				"**-** Added: New plants\n" +
				"**-** Added: New sounds\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New water tiles\n" +
				"**-** Changed: Volume of gases in potions reduced\n" +
				"**-** Changed: King of dwarves is now immune to paralysis\n" +
				"**-** Fixed: Several bugs fixed\n" +
				"\n" +
				"and more... "));

		changes.addButton( new ChangeButton( new Image(new MonkSprite()), "v0.4.X",
				"**v0.4.5:** (July 4th, 2013)\n" +
				"This is another small update, hopefully it will be the last one before the next content update (0.5). Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: New type of items - seeds\n" +
				"**-** Added: Wand of Disintegration\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Ring of Haggler and Ring of Thorns are not need for unlocking “all rings\" badge\n" +
				"**-** Changed: Wands can’t have more than 9 charges now\n" +
				"**-** Changed: The chance of looting a Ring of Thorns is increased\n" +
				"**-** Fixed: Several bugs fixed (incl. critical ones)"
				,
				"**v0.4.4:** (June 26th, 2013)\n" +
				"Pixel Dungeon 0.4.4 released! The main feature of this update is “specializations\". They are like sub-classes, there are 2 of them for each class (i.e. 6 in total). Here is the changelog from Google Play, it’s quite short:\n" +
				"\n" +
				"**-** Added: Specializations (available after defeating the 2nd boss)\n" +
				"**-** Added: New badges\n" +
				"**-** Added: New room \"Flooded vault\"\n" +
				"**-** Changed: Badges visualization is corrected a little\n" +
				"**-** Changed: Some badges criteria\n" +
				"**-** Fixed: Several bugs fixed"
				,
				"**v0.4.3:** (June 18th, 2013)\n" +
				"Pixel Dungeon 0.4.3 released! This update introduces \"Badges\" - a simple achievements system. Here is the list of what’s new from Google Play:\n" +
				"\n" +
				"**-** Added: Badges (achievements)\n" +
				"**-** Added: Freezing traps\n" +
				"**-** Added: Ring of Thorns\n" +
				"**-** Changed: Upon resurrection the current floor is reset only if it’s a boss floor\n" +
				"**-** Changed: Search action always succeeds now\n" +
				"**-** Changed: Reforging preserves enchantment of an upgraded weapon\n" +
				"**-** Changed: Scrolls of Nuclear Blast are renamed to Scrolls of Psionic Blast\n" +
				"**-** Changed: Golems and Animated statues are immune to Psion Blast now\n" +
				"**-** Fixed: Several bugs fixed\n" +
				"\n" +
				"and many more... "
				,
				"**v0.4.2:** (May 29th, 2013)\n" +
				"Pixel Dungeon 0.4.2 released! This is a rather small update without dramatic changes. Here is the list of what’s new from Google Play:\n" +
				"\n" +
				"**-** Added: Potion of Frost\n" +
				"**-** Added: Scroll of Nuclear Blast\n" +
				"**-** Added: Dewdrops\n" +
				"**-** Added: Enemy health indicator (tap danger indicator to activate it)\n" +
				"**-** Added: Number of slain monsters in rankings\n" +
				"**-** Added: Mages can equip and use wands as a melee weapon (in case of need)\n" +
				"**-** Changed: Undead dwarves are immune to Paralysis now\n" +
				"**-** Fixed: Animated statues are not activated by alarms\n" +
				"**-** Fixed: Strictly one item per tile in shops\n" +
				"**-** Fixed: Ring of Resistance+5 and higher\n" +
				"\n" +
				"and more... "
				,
				"**v0.4.1:** (May 21st, 2013)\n" +
				"Pixel Dungeon 0.4.1 released! Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Ring of Resistance\n" +
				"**-** Added: Statue Room\n" +
				"**-** Added: Catalogus (accessible from the character window)\n" +
				"**-** Added: Additional info in rankings (more to come)\n" +
				"**-** Changed: Scroll of Remove Curse also removes Weakness\n" +
				"**-** Changed: Weakness is buffed\n" +
				"**-** Changed: Flying mobs and levitating hero cannot be rooted\n" +
				"**-** Changed: Decelerating weapon is renamed to Chilling weapon\n" +
				"**-** Changed: Wands can be transformed in a Well of Transmutation\n" +
				"**-** Changed: Enchanted weapons are less common now\n" +
				"**-** Fixed: Several bugs fixed\n" +
				"\n" +
				"and more... "
				,
				"**v0.4:** (May 9th, 2013)\n" +
				"Pixel Dungeon 0.4 released! This update brings 5 levels of dwarven city, new monsters and items. Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: 5 new levels (city of dwarves) full of new enemies\n" +
				"**-** Added: Well of Transmutation\n" +
				"**-** Added: In-game sound controls\n" +
				"**-** Added: Resistances and immunities for monsters\n" +
				"**-** Added: Tiles are highlighted when searched\n" +
				"**-** Added: “Resume motion\" button\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Rankings screen is slightly redesigned\n" +
				"**-** Changed: Rogues start with 12 darts\n" +
				"**-** Changed: Mystery Meat can be cooked\n" +
				"**-** Changed: Garden room is redesigned\n" +
				"**-** Fixed: Several bugs fixed\n" +
				"\n" +
				"...and more "));

		changes.addButton( new ChangeButton( new Image(new BatSprite()), "v0.3.X",
				"**v0.3.5:** (April 16th, 2013)\n" +
				"(Note from Evan: I'm pretty sure this was the first version I played!)\n" +
				"\n" +
				"Pixel Dungeon 0.3.5 released! Here is the list of changes:\n" +
				"\n" +
				"**-** Added: In-game music by Cube Code\n" +
				"**-** Added: Rankings (high scores)\n" +
				"**-** Added: New enchantment - Wild weapon\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: All classes were tweaked a little\n" +
				"**-** Changed: All bosses now resist to toxic gas and grim enchantment\n" +
				"**-** Changed: Some special rooms were tweked a little\n" +
				"**-** Fixed: Several major and minor bugs were fixed\n" +
				"\n" +
				"...and more. ",
				"**v0.3.4:** (April 6th, 2013)\n" +
				"Pixel Dungeon for Android 0.3.4 goes live! This update introduces character classes: Warrior, Mage and Rogue. Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Character classes\n" +
				"**-** Added: New special room type - Garden\n" +
				"**-** Added: New special room type - Crypt\n" +
				"**-** Added: New type of trap - Lightning trap\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Now it’s impossible to upgrade a Ring og Haggler\n" +
				"**-** Changed: Unarmed hero inflicts damage based on his Strength\n" +
				"**-** Changed: Now wells really look like wells\n" +
				"**-** Changed: Ring of Rejuvenation is renamed into Ring of Mending\n" +
				"**-** Several bugs fixed\n" +
				"\n" +
				"...and more "
				,
				"**v0.3.3:** (March 21st, 2013)\n" +
				"Pixel Dungeon for Android 0.3.3 released! Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Enchanted weapons\n" +
				"**-** Added: Several new items\n" +
				"**-** Added: New sounds\n" +
				"**-** Changed: Scrolls of Enhancement are renamed to Scrolls of Upgrade\n" +
				"**-** Changed: Landscape orientation is enabled on all devices\n" +
				"**-** Changed: The hero and mobs don’t get slowed down in dense vegetation\n" +
				"**-** Changed: Sleeping monsters less like to notice the hero while he is levitating\n" +
				"**-** Changed: No more crabs on the second level\n" +
				"**-** Changed: Wand of Telekinesis pushes enemies back\n" +
				"**-** Fixed: A lot of bugs and typos...\n" +
				"\n" +
				"...and more\n" +
				"\n" +
				"It took much longer than I planned, sorry about that. The next update will bring many new features (and will take much time to implement too) including classes."
				,
				"(Note from Evan: There is no surviving changelog for **v0.3.2**, it was likely a bugfix update only.)\n" +
				"\n" +
				"**v0.3.1:** (February 28th, 2013)\n" +
				"Pixel Dungeon 0.3.1 released! This update comes with beautiful theme by Cube Code and basic sound effets. Also some serious bugs are fixed including “Sealed-DM-300\" bug and “Starved-to-death-after-resurrection\" bug."
				,
				"**v0.3:** (February 21st, 2013)\n" +
				"Pixel Dungeon 0.3 released! This update brings 5 new cave levels, monsters and items. What else? Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: New attack button (for melee combat)\n" +
				"**-** Added: Ankh - a powerful artefact, that allows you to bring a fallen hero back to life\n" +
				"**-** Changed: Potions of Strength and Scrolls of Enhancement now can be spawned in Laboratory and Library respectively\n" +
				"**-** Changed: Second search at the same place always succeeds\n" +
				"**-** Changed: Regrowth sets “rooted\" debuff on targets in its area of effect\n" +
				"**-** Fixed: a lot of bugs\n" +
				"\n" +
				"and more..."));

		changes.addButton( new ChangeButton( new Image(new SkeletonSprite()), "v0.2.X",
				"**v0.2.4:** (February 5th, 2013)\n" +
				"Pixel Dungeon 0.2.4 released. This update is dedicated to UI improvements. Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Confirmation dialog is displayed when trying to drink an identified harmful potion\n" +
				"**-** Added: Orientation switcher on the main screen for devices with a large enough screen\n" +
				"**-** Added: Loot indicator is displayed when there is an item on the ground at the current location of player\n" +
				"**-** Added: Quickslot auto-targeting\n" +
				"**-** Changed: \"Monsters from the depth\" tend to be less powerful\n" +
				"**-** Changed: Well of Light removes curse from equipped items\n" +
				"**-** Fixed: Several bugs incl. severe ones\n" +
				"\n" +
				"and more...\n" +
				"\n" +
				"If everything will be ok, then the next update will contain a bunch of new levels with new monsters"
				,
				"**v0.2.3:** (January 30th, 2013)\n" +
				"Pixel Dungeon 0.2.3 released! The main feature of this update is the shop on the 6th depth. What else? Here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Shop on the 6th depth\n" +
				"**-** Added: New “trap\" type - alarm\n" +
				"**-** Added: New special room type - healing well\n" +
				"**-** Changed: Now it’s impossible to put out fire in the water while levitating\n" +
				"**-** Changed: Now alarmed monsters are less likely to notice the hero\n" +
				"**-** Fixed: The size of apk-file is reduced\n" +
				"**-** Fixed: Now it’s impossible to use teleportation on Tengu’s depth\n" +
				"**-** Fixed: Crashes on devices with turkish locale"
				,
				"**v0.2.2:** (January 23rd, 2013)\n" +
				"Pixel Dungeon 0.2.2 released! This update is dedicated to items, here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Added: Several new items including rings, wands, potions and ranged weapon\n" +
				"**-** Changed: Areas revealed with Scroll of Magic Mapping are shown in different color\n" +
				"**-** Changed: Inventory window is slightly modified\n" +
				"**-** Changed: Now there can’t be 2 special rooms of the same type on the same level\n" +
				"**-** Changed: Now melee weapon do less damage without enhancement, more with enhancement\n" +
				"**-** Changed: Now enhanced armor gains DR depending on its tier\n" +
				"**-** Fixed: several bugs are fixed\n" +
				"\n" +
				"Actually there are more changes in this update, but the field for recent changes on Google Play is too short and I’m too lazy start enumerating them again :) "
				,
				"**v0.2.1:** (January 17th, 2013)\n" +
				"As I promised, Pixel Dungeon 0.2.1 released! This is a bug-fixing update, here is the list of changes from Google Play:\n" +
				"\n" +
				"**-** Changed: Charging time for enhanced staffs is increased\n" +
				"**-** Changed: Tengu is slightly buffed\n" +
				"**-** Fixed: Several bugs are fixed (including major ones - loss of a key, pinch-to-zoom lock)"
				,
				"**v0.2:** (January 11th, 2013)\n" +
				"I’m glad to announce that Pixel Dungeon 0.2 is now available on Google Play!\n" +
				"\n" +
				"(Note from Evan: the below changes were translated from Russian. This update also included the Prison area.)\n" +
				"\n" +
				"**-** New types of special rooms\n" +
				"**-** New Items\n" +
				"**-** Slightly weakened gnolls and crabs\n" +
				"**-** Swarms of flies and Goo, on the contrary, are made a little stronger\n" +
				"**-** It takes a little longer to go from full satiety to a state of exhaustion\n" +
				"**-** Increased the time it takes to search...\n" +
				"**-** ...but a repeated search greatly increases the likelihood of success\n" +
				"**-** The power of traps increases with the level of the dungeon\n" +
				"**-** Movement can now be interrupted by clicking anywhere on the map\n" +
				"**-** Fixed bugs that sometimes caused the game to slow down or crash\n" +
				"**-** Oh, and one more little thing..."));

		changes.addButton( new ChangeButton( new Image(new RatSprite()), "v0.1.X",
				"**v0.1.4:** (December 19th, 2012)\n" +
				"Pixel Dungeon 0.1.4 released:\n" +
				"\n" +
				"**-** Pinch to zoom\n" +
				"**-** Current zoom level is saved every time you change it\n" +
				"**-** Map panning is less sensitive now (it should be easier to click the map)\n" +
				"**-** Tap-to-move action is interrupted when hero is hit by a monster\n" +
				"**-** The bug that could cause lighting glitches on some HTC devices is fixed (hopefully)\n" +
				"**-** A couple of minor bugs is fixed"
				,
				"**v0.1.3:** (December 13th, 2012)\n" +
				"**-** \"Typical STR requirement\" is displayed for weapons and armor that are not identified yet\n" +
				"**-** Attack speed bonus for excess strength removed\n" +
				"**-** Mobs do more \"centered\" damage\n" +
				"**-** Monsters \"from the depth\" spawn slightly less often\n" +
				"**-** Mystery meat may have no negative effect now\n" +
				"**-** Info window for empty cells\n" +
				"**-** Click outside a window to close it\n" +
				"**-** Now it's easier to click the map without shifting it\n" +
				"**-** Several bugs fixed incl. \"Instant death by caustic ooze\" & \"wand of amok + swarm of flies\"\n" +
				"**-** + some graphics tweaks"
				,
				"**v0.1.2:** (December 10th, 2012)\n" +
				"Pixel Dungeon 0.1.2 released:\n" +
				"\n" +
				"**-** Marsupial rats are nerfed\n" +
				"**-** Penalties for using too heavy weapon and armor are made less severe\n" +
				"**-** Bug causing visual artefacts on some devices is fixed\n" +
				"**-** Bug with Ring of Cleansing is fixed"
				,
				"**v0.1.1:** (December 8th, 2012)\n" +
				"Pixel Dungeon 0.1.1 released. Nothing particularly interesting there, just some bug fixes, but now it should be playable on froyo devices. And that bug with two chars on the same tile, hopefully, is fixed too."
				,
				"**v0.1:** (December 4th 2012)\n" +
				"Finally released!\n\n" +
				"(Note from Evan: In its original release, Pixel Dungeon contained 5 floors going down to Goo. There were no: hero classes, quests, music or sfx, badges, seeds, dewdrops, or thrown weapons (aside from darts). All content that was in the game was in an earlier more basic form too."));

	}

}
