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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.Trinity;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
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

		changes = new ChangeInfo("BETA-2.4", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"With Trinity implemented the beta for v3.0 is finally getting close to wrapping up!\n\n" +
				"There's still a bit more to do though:\n" +
				"**-** Address any bugs or serious balance issues as they come up\n" +
				"**-** Power of Many armor ability (I expect to get this done faster than Trinity)\n" +
				"**-** Final visual polish for the Cleric before release"));

		changes.addButton(new ChangeButton(new HeroIcon(new Trinity()), "Trinity!",
				"**The Cleric's second armor ability is now available!**\n\n" +
				"**Trinity** acts like three abilities in one! Each ability lets you reproduce the effect of an enchant/glyph, thrown weapon/wand, or ring/artifact that you've previously identified. New spells are used to assign effects, and Trinity itself activates them.\n\n" +
				"Note that Trinity depends on internal tracking code that was added in BETA-2.2, if you're playing a run from before then you may not be able to select every thing you have identified."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Internal adjustments to hunger and regeneration effects, they should now be more responsive when hunger or regen rate changes.\n\n" +
				"**-** Added developer commentary for v2.0.0\n\n" +
				"**-** Updated Translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by BETA:**\n" +
				"**-** Cleric spells incorrectly interacting with autotargeting in some cases\n" +
				"**-** iOS launch screen using the old title graphic\n" +
				"**-** Various minor layout/text errors"));

		changes = new ChangeInfo("BETA-2.2 & 2.3", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "Visual and UI Improvements!",
				"**Major visual improvements have been made to the game's title screen and hero select!**\n\n" +
				"Shattered's title graphic has been totally redrawn! The new title graphic, by Aleksandar Komitov, improves on text quality and style while trying to stay true to the original title. There is now a landscape and portrait variant of this title as well.\n\n" +
				"The game's hero splash arts have been improved as well! After so many years, Aleksandar has revised some of the game's hero splash arts to bring them up to his current standards.\n" +
				"**-** The Huntress and Rogue have recieved major changes\n" +
				"**-** The Mage has recieved moderate changes\n" +
				"**-** The Duelist and Cleric have received only tiny tweaks to some face details.\n" +
				"**-** The Warrior's splash remains unchanged for the moment, but improvements to it are coming soon as well!"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Added info about how recently runs were played to the games in progress screen, and added a sorting setting\n\n" +
				"**-** Smite no longer guarantees a hit if the Paladin is encumbered\n" +
				"**-** Added a buff icon for Illuminated\n\n" +
				"**-** Traps triggered by time freeze ending now always resolve after other effects/actions (e.g. teleportation, item pickup)\n\n" +
				"**-** Updated Translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by BETA:**\n" +
				"**-** Smite dealing damage to freshly corrupted enemies\n" +
				"**-** Rare crash errors\n" +
				"**-** Minor text errors\n\n" +
				"**Existed Prior to BETA:**" +
				"**-** Rare cases where multiple ascension wins could be recorded for one run\n" +
				"**-** Unstable spell never triggering scroll effects that could apply in or out of combat\n" +
				"**-** Cursed wand effects potentially applying levitation to immovable characters\n" +
				"**-** Ripper demons sometimes losing their target early"));

		changes = new ChangeInfo("BETA-2.0 & 2.1", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "The Paladin!",
				"After a bit of a long wait, **the Cleric's second subclass has been implemented!**\n\n" +
				"**The Paladin** is focused on melee spell combat and defensive power. Their effects most strongly synergize with weapons and armor."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Updated Translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Existed Prior to BETA:**\n" +
				"**-** Exotic crystals trinket not applying to monster drops in many cases\n" +
				"**-** Rare errors when cancelling scroll of enchantment on armor\n" +
				"**-** Multiplicity glyph not working correctly with ghouls in some cases\n" +
				"**-** Geomancer rockfall attack being cleared on save/load"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Existed Prior to BETA:**\n" +
				"**-** Various issues with system gestures registering as game inputs on Android and iOS\n" +
				"**-** Corrosion gas from chaotic censer always starting at 1 damage\n" +
				"**-** Retreating characters failing to retreat through crowded area in some cases\n" +
				"**-** Pacifist badge unlocking when it shouldn't in rare cases\n" +
				"**-** Rat King's description sometimes being incorrect in landmarks"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		/*changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released ..., ...\n" +
				"_-_ ... days after Shattered v2.5.0\n\n" +
				"Dev commentary will be added here in the future."));*/

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
				"**-** Turned off auto-targeting in cases where it was almost always wrong (Bless spell, Wand of Warding, etc.)\n" +
				"**-** Enemies are now more willing to switch targets if their target is retreating and they are being attacked by something else\n" +
				"**-** Enemies are now more consistently drawn to the hero's position if they are attacked from out of visible range\n" +
				"**-** Fixed Reclaim trap spell exploits, and allowed it to be dropped while charged\n" +
				"**-** Improved visual contrast for symbols on runestones\n" +
				"\n" +
				"**Characters:**\n" +
				"**-** Crazed bandits now have their own description",

				"**Effects:**\n" +
				"**-** Blast Wave no longer knocks back characters if they are killed over a pit\n" +
				"**-** Cloak of Shadows (and new Holy Tome) can no longer be transmuted.\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Adjusted icons for Sucker Punch and Followup Strike\n" +
				"**-** Camera no longer re-centers on hero when adding custom notes\n" +
				"**-** Camera panning to enemies now respects the 'camera follow intensity' setting.\n" +
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
				"**-** Rare cases where bomb AOE could be influenced by nearby potions shattering\n" +
				"**-** Exploits involving juggling salt cube to get slower hunger with full regen\n" +
				"**-** Cursed armband not updating player gold display on desktop after stealing gold\n" +
				"**-** Very specific cases where disarming traps could make items unattainable\n" +
				"**-** Icecap incorrectly being blocked by high grass\n" +
				"**-** Stone of detect magic incorrectly being usable on thrown weapons\n" +
				"**-** Artifact uniqueness being affected by runs from prior game versions",

				"**Effects:**\n" +
				"**-** Monk abilities which ignore armor using the incorrect damage icon\n" +
				"**-** Damage type not showing when hitting downed crystal guardians\n" +
				"**-** Very rare cases where spawned high grass could interfere with the floor 20 shop\n" +
				"**-** Certain effects not having on-death or rankings messages\n" +
				"**-** Specific cases where cursed wand effects would forget they were spawned by wondrous resin\n" +
				"**-** Duration of greater haste not being preserved on save/load\n" +
				"**-** Disarming traps not teleporting the hero's weapon in some cases where they should\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Rare cases where game win scene wouldn't trigger immediately\n" +
				"**-** Various minor textual errors\n" +
				"**-** Various rare crash errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHAOTIC_CENSER), "Trinket Buffs",
				"Overall trinket balance is in a much better place since v2.5, but there are still some that can do with being more powerful or fun to use:\n" +
				"\n" +
				"**- Chaotic Censer** now only spawns gasses when enemies are present, and gives a warning one moment before the gas is spewed.\n" +
				"**- 13 Leaf Clover** slightly redesigned, now has a 15% chance to set damage to max and 10% chance to set damage to min per level. This results in ~10% more average damage at +3.\n" +
				"**- Mimic Tooth** ebony mimics now have normal mimic stats, down from +25%, but still deal full damage on surprise attack.\n" +
				"**- Trap Mechanism** now also makes 10% of a level's traps spawn visible per level.\n" +
				"**- Shard of Oblivion** now prevents ID effects such as scroll of ID and wells of knowledge. Items are instead set to be ready to be IDed by the shard."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), "Weapon Ability Buffs",
				"The Duelist continues to do better following all the changes in v2.X updates. A couple of weapon abilities are still lagging behind though, and so I'm giving them a little help:\n" +
				"\n" +
				"**- Combo Strike** ability damage boost buffed by 25% for Gauntlets. 33% for Sai, 50% for Gloves.\n" +
				"**- Charged Shot** knockback +1, base bonus damage on untipped darts +1."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MOSSY_CLUMP), "Trinket Nerfs",
				"Despite nerfs in v2.5 the Mossy Clump is still far and away the strongest trinket. For now I'm going to experiment with adjusting the ratios on the floors it grants.\n" +
				"\n" +
				"**- Mossy Clump** now generates grassy floors 1/3 of the time and water floors 2/3 of the time, instead of 1/2 each. This will usually result in one fewer grassy floor over a whole run."));

		changes.addButton(new ChangeButton(new TalentIcon(Talent.SHARED_UPGRADES), "Shared Upgrades Nerf",
				"Thrown weapons aren't an especially popular category of item to upgrade, and I would like to make more extensive changes to them in the future, but for the moment I'm making a targeted adjustment to the Shared Upgrades talent. The bonus damage it provided wasn't tied to the tier of thrown weapon used, which made T2 thrown weapons disproportionally powerful for the Sniper.\n" +
				"\n" +
				"**- Shared Upgrades** now grants 2.5%/5%/7.5% bonus damage per upgrade per tier, instead of a flat 10%/20%/30% bonus damage per upgrade. Functionally, this means -50% bonus dmg for T2 thrown weapons, -25% for T3, no changes for T4, and +25% for T5."));

	}

}
