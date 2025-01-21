/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v3_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v3_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.WARNING), "Older Device Support",
				"Unfortunately I will have to make some changes in 2025 that will remove support for very old versions of iOS, Android, and Java:\n" +
				"\n" +
				"**iOS 11:** Support for this version of iOS is being dropped after v3.0 (this update). There were no Apple devices which got iOS 11 but not 12, so this shouldn't actually affect any users.\n" +
				"\n" +
				"**Android 4.0-4.4:** Due to updates to Shattered's game library (libGDX) and Google Play's billing library, I will sadly have to drop support for these older Android versions toward the end of 2025. These Android versions should still at least get v3.1 and v3.2 though.\n" +
				"\n" +
				"**Java 8-10:** This only affects users who play the old desktop JAR distribution, which requires a separate Java install. Support for the Java 8, 9, and 10 targets has been depreciated, at some point in the future Shattered will require Java 11 instead. I'll likely make this change at the same time as the Android change."));
	}

	public static void add_v3_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v3.0-BETA", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("BETA-2.0", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"Hey folks, thanks for your patience as things took a bit longer than expected to get started back up in the new year!\n\n" +
				"I've now implemented the Paladin and hope to be implementing both outstanding armor abilities in the next couple weeks, along with other smaller changes.\n\n" +
				"Here's what we have left before release, roughly in the order I plan to do it:\n" +
				"**-** Address any bugs or serious balance issues as they come up\n" +
				"**-** Visual improvements to title screen, hero select, and games in progress\n" +
				"**-** Trinity armor ability\n" +
				"**-** Power of Many armor ability\n" +
				"**-** Final visual polish for the Cleric before release"));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "The Paladin!",
				"After a bit of a long wait, **the Cleric's second subclass has been implemented!**\n\n" +
				"**The Paladin** is focused on melee spell combat and defensive power. Their effects most strongly synergize with weapons and armor."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by BETA:**\n" +
				"**-** Shared upgrades granting MUCH more bonus damage than intended\n" +
				"**-** Exploits where talent spells could still be cast after being metamorphed\n" +
				"**-** Minor text errors\n\n" +
				"**Existed Prior to BETA:**\n" +
				"**-** Exotic crystals trinket not applying to monster drops in many cases\n" +
				"**-** Rare errors when cancelling scroll of enchantment on armor\n" +
				"**-** Multiplicity glyph not working correctly with ghouls in some cases\n" +
				"**-** Geomancer rockfall attack being cleared on save/load"));

		changes = new ChangeInfo("BETA-1.4", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHAOTIC_CENSER), "Trinket & Weapon Ability Buffs",
				"Overall trinket balance is in a much better place since v2.5, but there are still some that can do with being more powerful or fun to use:\n" +
				"\n" +
				"**- Chaotic Censer** now only spawns gasses when enemies are present, and gives a warning one moment before the gas is spewed.\n" +
				"**- 13 Leaf Clover** slightly redesigned, now has a 15% chance to set damage to max and 10% chance to set damage to min per level. This results in ~10% more average damage at +3.\n" +
				"**- Mimic Tooth** ebony mimics now have normal mimic stats, down from +25%, but still deal full damage on surprise attack.\n" +
				"**- Trap Mechanism** now also makes 10% of a level's traps spawn visible per level.\n" +
				"**- Shard of Oblivion** now prevents ID effects such as scroll of ID and wells of knowledge. Items are instead set to be ready to be IDed by the shard.",

				"The Duelist continues to do better following all the changes in v2.X updates. A couple of weapon abilities are still lagging behind though, and so I'm giving them a little help:\n" +
				"\n" +
				"**- Combo Strike** ability damage boost buffed by 25% for Gauntlets. 33% for Sai, 50% for Gloves.\n" +
				"**- Charged Shot** knockback +1, base bonus damage on untipped darts +1."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MOSSY_CLUMP), "Trinket & Talent Nerfs",
				"Despite nerfs in v2.5 the Mossy Clump is still far and away the strongest trinket. For now I'm going to experiment with adjusting the ratios on the floors it grants.\n" +
				"\n" +
				"**- Mossy Clump** now generates grassy floors 1/3 of the time and water floors 2/3 of the time, instead of 1/2 each. This will usually result in one fewer grassy floor over a whole run.",

				"Thrown weapons aren't an especially popular category of item to upgrade, and I would like to make more extensive changes to them in the future, but for the moment I'm making a targeted adjustment to the Shared Upgrades talent. The bonus damage it provided wasn't tied to the tier of thrown weapon used, which made T2 thrown weapons disproportionally powerful for the Sniper.\n" +
				"\n" +
				"**- Shared Upgrades**  now grants 2.5%/5%/7.5% bonus damage per upgrade per tier, instead of a flat 10%/20%/30% bonus damage per upgrade. Functionally, this means -50% bonus dmg for T2 thrown weapons, -25% for T3, no changes for T4, and +25% for T5."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Holy weapon and ward now work without a weapon or armor. Mainly this is to prevent antisynergy with the ring of force.\n\n" +
				"**-** Updated Translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by BETA:**\n" +
				"**-** Character visual effects (burning, illuminated, etc.) persisting after death in various cases\n" +
				"\n" +
				"**Existed Prior to BETA:**\n" +
				"**-** Exploits involving juggling salt cube to get slower hunger with full regen\n" +
				"**-** Disarming traps not teleporting the hero's weapon in some cases where they should"));

		changes = new ChangeInfo("BETA 1.1 to 1.3", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Turned off auto-targeting in cases where it was almost always wrong (Bless spell, Wand of Warding, etc.)\n\n" +
				"**-** Camera panning to enemies now respects the 'camera follow intensity' setting.\n\n" +
				"**-** Holy Tome and Cloak of Shadows can no longer be transmuted."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Existed Prior to BETA:**\n" +
				"**-** Rare cases where bomb AOE could be influenced by nearby potions shattering\n" +
				"**-** Artifact uniqueness being affected by runs from prior game versions\n" +
				"**-** Rare cases where game win scene wouldn't trigger immediately\n" +
				"**-** Minor text errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		/*changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released ..., ...\n" +
				"_-_ ... days after Shattered v2.5.0\n\n" +
				"Dev commentary will be added here in the future."));*/

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Beta Info",
				"Hey Beta testers! The Beta for v3.0.0 is in an earlier state than a beta normally is, so expect it to last for a little while. Here's roughly what needs to be done before release:\n" +
				"\n" +
				"For the Cleric themselves:\n" +
				"**-** Implement the **Paladin Subclass**, and **Trinity and Power of Many** armor abilities.\n" +
				"**-** Make refinements and balance adjustments as needed based on feedback and analytics data.\n" +
				"**-** Finalize Cleric visuals, currently many in-game visuals are WIP.\n" +
				"\n" +
				"And for other things:\n" +
				"**-** Make various miscellaneous bugfixes and small tweaks, I've got a list to get through.\n" +
				"**-** Further interface improvements to the games in progress scene\n" +
				"**-** Visual improvements to art in the title screen and hero select.\n\n" +
				"I'm taking a bit of a break over the holidays, so expect these items to get getting cleared early in the new year."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 1), "The Cleric!",
				"**Shattered Pixel Dungeon has another new hero, making for six total!!**\n" +
				"\n" +
				"The Cleric is an entire new hero class **with a variety of unique spells that they learn via talents!** They can be unlocked by removing the curse on any piece of equipment, but existing players who already have a win will automatically unlock them!\n" +
				"\n" +
				"Much like how the Duelist can use a bunch of weapon abilities, I want the Cleric to be an ability-centric hero focused on Magic. Unlike the Duelist, these abilities are tied into the hero and their talents, instead of equipment items.\n" +
				"\n" +
				"**Note that while the Cleric is playable, they are not yet fully implemented!** Some visuals are placeholders and not all subclasses and armor abilities are implement."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 4), "Cleric Subclasses",
				"**The Cleric has two subclasses, each with their own emphasis!**\n" +
				"\n" +
				"**The Priest** is focused on ranged spell combat, effects that synergize with magical items like wands and artifacts.\n" +
				"\n" +
				"**The Paladin** is focused on melee spell combat and defensive power. Their effects most strongly synergize with weapons and armor."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 5), "Cleric Armor Abilities",
				"**The Cleric has three lategame armor abilities as well!**\n" +
				"\n" +
				"**Ascended Form** grants the Cleric access to new spells, extra attack ranged, and shielding whenever they cast spells.\n" +
				"\n" +
				"**Currently The other two abilities are not implemented, look out for them soon!**"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.DISPLAY.get(), "Interface Changes",
				"You can now have up to six runs in progress, and the UI has been adjusted to accommodate for this.\n" +
				"\n" +
				"**I also plan to add a bit more information to runs in progress and make some other interface changes before release**"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**Highlights:**\n" +
				"**-** Enemies are now more willing to switch targets if their target is retreating and they are being attacked by something else\n" +
				"**-** Enemies are now more consistently drawn to the hero's position if they are attacked from out of visible range\n" +
				"**-** Fixed Reclaim trap spell exploits, and allowed it to be dropped while charged\n" +
				"**-** Improved visual contrast for symbols on runestones\n" +
				"\n" +
				"**Characters:**\n" +
				"**-** Crazed bandits now have their own description\n" +
				"\n" +
				"**Effects:**\n" +
				"**-** Blast Wave no longer knocks back characters if they are killed over a pit\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Adjusted icons for Sucker Punch and Followup Strike\n" +
				"**-** Camera no longer re-centers on hero when adding custom notes\n" +
				"**-** Improved the game's monochrome Android icon\n" +
				"**-** Improved text clarity in a few cases\n" +
				"**-** Updated internal code libraries"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Highlights:**\n" +
				"**-** Various cases where closing the game inside of the alchemy scene could result in lost progress\n" +
				"**-** Runestones working while hero is magic immune\n" +
				"\n" +
				"**Items:**\n" +
				"**-** Cursed armband not updating player gold display on desktop after stealing gold\n" +
				"**-** Very specific cases where disarming traps could make items unattainable\n" +
				"**-** Icecap incorrectly being blocked by high grass\n" +
				"**-** Stone of detect magic incorrectly being usable on thrown weapons\n" +
				"\n" +
				"**Effects:**\n" +
				"**-** Monk abilities which ignore armor using the incorrect damage icon\n" +
				"**-** Damage type not showing when hitting downed crystal guardians\n" +
				"**-** Very rare cases where spawned high grass could interfere with the floor 20 shop\n" +
				"**-** Certain effects not having on-death or rankings messages\n" +
				"**-** Specific cases where cursed wand effects would forget they were spawned by wondrous resin\n" +
				"**-** Duration of greater haste not being preserved on save/load\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Various minor textual errors\n" +
				"**-** Various rare crash errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

	}

}
