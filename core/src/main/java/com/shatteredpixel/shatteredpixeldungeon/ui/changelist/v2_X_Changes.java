/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BruteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TormentedSpiritSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class v2_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v2_2_Changes(changeInfos);
		add_v2_1_Changes(changeInfos);
		add_v2_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		if (DeviceCompat.isiOS() && Gdx.app.getVersion() < 11){
			changes.addButton( new ChangeButton(Icons.WARNING.get(), "32-bit iOS support",
					"v2.3 will be the last version of Shattered Pixel Dungeon that will supports 32-bit iOS devices. As a result, future Shattered updates will require iOS 11+, up from 9+.\n\n" +
					"Players on iOS 9 and 10 will be able to continue playing Shattered Pixel Dungeon v2.3, and can check the news section for information on future versions."));
		}

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Overview",
				"The next update will be v2.3.0, and will add two new variants to the new caves quest!\n" +
				"\n" +
				"The new caves quest ended up taking quite a lot longer than intended, so I'm a bit hesitant to commit to an ETA for v2.3.0 currently. I should hopefully be able to announce something later in November or early December. Please keep in mind that while I always try to keep to the ETAs I provide, they are just estimates. If you don't hear from me by the ETA, it means I'm still busy with the update!"));

		changes.addButton( new ChangeButton(new Image(new BruteSprite()), "Gnoll Encampment",
				"The first of the upcoming caves quest variants is an unstable gnoll encampment. The gnolls have taken to trying to mine out some gold themselves, but are preferring to use earth-moving magic with little care for cave stability. Expect a lot of closed in walls and fallen rocks, as well as gnolls that'll be able to use those rocks against you."));

		changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_CAVES, 32, 64, 16, 16), "Mushroom Forest",
				"The second upcoming variant is a mushroom forest. A large mycellium network has grown in this area, creating lots of hostile plant life. The abundance of tall vegetation isn't just pretty tough, you'll want to use it (or the cave walls themselves) to keep out of sight from the powerful, yet stationary, mushroom sentries."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Smaller Changes/Fixes",
				"I also expect v2.3.0 will include followup changes to the initial new quest variant, as well as the usual assortment of other smaller improvements, fixes, and additions. I'd like for v2.3.0 to be fairly quick, so the other changes will probably be lighter than usual."));

	}

	public static void add_v2_2_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("v2.2.1", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton( new Image(new BlacksmithSprite()), "Blacksmith Reward Changes",
				"Overall I'm fairly happy with the level of challenge and reward the new quest offers, but from looking at gameplay data the rewards are a bit overtuned currently.\n\n" +
				"I certainly don't plan to reduce the blacksmith rewards back to being similar to before this new quest, but for now I am making some initial changes to get them into a better place balance-wise. This will make it easier to fine-tune the overall quest difficulty and rewards in v2.3.0.\n\n" +
				"_-_ Cost for second reforge/harden up to 1500 from 1000\n" +
				"_-_ Upgrade now caps at +2, down from +3\n" +
				"_-_ Chance for smith to produce a +0 item up to 30%, from 20%\n" +
				"_-_ Chance for smith to produce a +1 item down to 45%, from 55%"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ The Crystal Spire now awards 20 exp when it is defeated\n\n" +
				"_-_ Increased the amount of toxic gas that the rot heart can produce\n\n" +
				"_-_ New quest bosses now interrupt the hero when they are telegraphing an attack\n\n" +
				"_-_ Updated translations and translator credits"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Caused by v2.2.0:_\n" +
				"_-_ Various rare crash bugs in new quest\n" +
				"_-_ Hardening not being preserved when transmuting weapons\n" +
				"_-_ Items rarely spawning on new quest entrance\n" +
				"_-_ Hero getting teleported to quest entrance if game was loaded while they were inside a crystal spike\n" +
				"_-_ Various minor visual bugs in new quest\n" +
				"\n" +
				"_Existed Prior to v2.2.0:_\n" +
				"_-_ System gestures in iOS sometimes registering as taps within the game\n" +
				"_-_ Endure ability not working properly when used twice quickly\n" +
				"_-_ Melee damage of Mage's Staff benefiting from talents that boost wand levels\n" +
				"_-_ Various blink effects allowing movement over magical fire\n" +
				"_-_ Some game actions being possible while meditating\n" +
				"_-_ Various minor visual and textual errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 18th, 2023\n" +
				"_-_ 138 days after Shattered v2.1.0\n\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_CAVES, 192, 64, 16, 16), "New Blacksmith Quest!",
				"_Shattered Pixel Dungeon's blacksmith quest has been completely redone!_\n" +
				"\n" +
				"_The quest now takes place in a new sublevel, with one entirely new environment, and two more on the way in v2.3!_ Each environment has its own set of hazards, level generation logic, and unique enemies.\n" +
				"\n" +
				"_The quest area features a new mining mechanic._ In addition to digging out gold, you can tunnel through walls to create new routes and evade hazards.\n" +
				"\n" +
				"_The quest rewards are also massively expanded._ The better you do on the quest, the more favor you'll earn in exchange for blacksmithing services. The old reforge option is still available, but there are several new options too."));

		changes.addButton( new ChangeButton(Icons.AUDIO.get(), "New Music!",
				"_Shattered Pixel Dungeon's soundtrack has been massively expanded!_ The game's soundtrack runtime has doubled in total, with almost 20 minutes of new audio by the game's composer: Kristjan Thomas Haaristo.\n" +
				"\n" +
				"_The existing region tracks have been expanded._ Each track now has three total segments, instead of two. All of these segments play in a semi-random pattern, increasing variety for the game's more frequently heard music.\n" +
				"\n" +
				"_Each region also has a new 'intense' track._ These tracks play while completing region quests and also during the ascension challenge.\n" +
				"\n" +
				"_Lastly, four 'finale' tracks have been added._ Three of these tracks play when you are about to defeat the game's three later bosses, and one plays right before securing victory (either grabbing the amulet or reaching the end of ascension)."));

		changes.addButton( new ChangeButton(new Image(new WandmakerSprite()), "Prison Quest Changes",
				"The game's second quest has received a bunch of adjustments to make it more engaging.\n" +
				"\n" +
				"_Corpse Dust Quest:_ The mass grave room now always spawns at least a bit away from the entrance, and wraith spawning is more consistent.\n" +
				"\n" +
				"_Elemental Embers Quest:_ The newborn elemental has been reworked:\n" +
				"_-_ HP up to 60 from 30, attacking power reduced, no longer ignites on-hit\n" +
				"_-_ Now shoots an avoidable fireball in a 3x3 area\n" +
				"_-_ The summon elemental spell is unchanged\n" +
				"\n" +
				"_Rotberry Quest:_ Overhauled the rot garden room:\n" +
				"_-_ Rot lashers are now much stronger, but take 1 turn to notice an adjacent enemy before attacking\n" +
				"_-_ Room layout is now much more chaotic, with more grass and crumbling walls\n" +
				"_-_ There is now a guaranteed safe path to the rot heart"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.CHALLENGE_ON.get(), "Hostile Champions",
				"I've decided to make some changes to the hostile champions challenge, to better balance the difficulty of each of the champion types, and address some common feedback about difficulty spikes:\n" +
				"\n" +
				"_- Projecting Champions_ now have +3 attack range, instead of infinite range\n" +
				"_- Blazing Champions_ now cannot spread fire onto water tiles\n" +
				"_- Growing Champions_ now gain stats 25% more slowly\n" +
				"_- Blessed Champions_ now have 4x accuracy and evasion, up from 3x\n" +
				"_- Giant Champions_ now take 80% reduced damage, up from 75%\n" +
				"_- Antimagic Champions_ now take 50% reduced damage, up from 25%"));

		changes.addButton( new ChangeButton(Icons.TALENT.get(), "T2 Potion and Scroll Talents",
				"The T2 potion and scroll talents have been reworked to trigger on a more broad range of items, and have had their names and effects changed as a result:\n" +
				"\n" +
				"_-_ Warrior, Huntress, and Duelist talents now trigger on any potion, not just potions of healing. Their effects are doubled when using potions of strength or experience.\n" +
				"_-_ Mage and Rogue talents now trigger on any scroll, not just scrolls of upgrade. Their effects are doubled when using scrolls of upgrade or transmutation.\n" +
				"\n" +
				"_-_ Warrior effect reduced to 50/75% shield, from 67/100%\n" +
				"_-_ Mage effect changed to +2 levels on the next 2/3 wand zaps\n" +
				"_-_ Rogue effect changed to 3/5 turns of invisibility\n" +
				"_-_ Huntress effect reduced to 1/2 turns of rooting and 4/6 grass, from 2/3 root and 5/8 grass\n" +
				"_-_ Duelist effect unchanged\n" +
				"\n" +
				"_-_ The Mage now also has a new T3 talent: 'desperate power', as the above change effectively moves the Empowering Scrolls talent to T2."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ Shopkeepers now warn the player once before fleeing when they are damaged\n" +
				"_-_ The sad ghost now always spawns at the end of the level it appears in and does not leave the exit room\n" +
				"_-_ Bosses from the sad ghost's quest now tend to wander toward the hero\n" +
				"_-_ Crystal path rooms have been redesigned to give the player more choice over the items they get\n" +
				"_-_ Increased the value of lower-exp enemies in sacrifice rooms. This is most noticeable in early floors, especially when sacrificing rats.\n" +
				"_-_ Equipping two of the same ring now shows their combined effect in their descriptions\n" +
				"_-_ Added a new Language: Belarusian!",

				"_UI/VFX:_\n" +
				"_-_ There is now a Google Play achievements button in the badges screen, if Google Play Games is enabled\n" +
				"_-_ Indicators now lower into empty space below them after the hero takes an action, not whenever new indicators appear\n" +
				"_-_ The game's tutorial is now skipped if there is existing gameplay data (e.g. via Google Play Games sync)\n" +
				"_-_ Heroes other than the duelist can now quickslot weapons if they get the swift equip talent.",

				"_Misc.:_\n" +
				"_-_ Overhauled retreating enemy AI. Retreating enemies should get stuck far less often, and terrified enemies are unable to approach the hero.\n" +
				"_-_ Substantially reduced the forbidden runes challenge's effect on levelgen\n" +
				"_-_ Healing effects still do not stack, but now combine more effectively\n" +
				"_-_ Added dev commentary for v1.1.0\n" +
				"_-_ The Google Play version of Shattered now uses Google Play Games v2, which requires Android 4.4+\n" +
				"_-_ Removed support for Android Instant Apps\n" +
				"_-_ Updated some links in the game's credits"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Some items being incorrectly consumed when the game is closed while they are being used\n" +
				"_-_ Mage's Staff not being affected by the degrade debuff\n" +
				"_-_ Further characters sometimes rendering on top of closer large characters\n" +
				"\n" +
				"_Items:_\n" +
				"_-_ Dwarf King's Crown rarely triggering the effect of the runic transference talent\n" +
				"_-_ Exploit where multiplicity curse could be used to skip some of Dwarf King's second phase\n" +
				"_-_ Various errors with class armor conversion and Warrior's broken seal\n" +
				"_-_ Ring of Force incorrectly displaying +99.99% when at +7, instead of +100%\n" +
				"_-_ Living Earth and Transfusion wands granting their self-buffs when shooting NPCs\n" +
				"_-_ Several obscure issues with noisemakers\n" +
				"_-_ Trap effects from reclaim trap spell not scaling with ascension challenge\n" +
				"_-_ Horn of Plenty occasionally having the wrong visuals for its charge state",

				"_Allies & Enemies:_\n" +
				"_-_ Damage caused to Yog's fists not correctly adding time to boss regen limit\n" +
				"_-_ Ambitious Imp sometimes calling out to the hero when not visible\n" +
				"_-_ Phantom Piranhas rapidly teleporting when corrupted\n" +
				"_-_ DM-300 fight sometimes not having a safe route to a power pylon\n" +
				"_-_ Mimics not dropping loot when they are killed via chasm while hiding\n" +
				"_-_ Tengu very rarely throwing bombs ontop of each other\n" +
				"_-_ Piranhas throwing themselves off chasms in very rare cases\n" +
				"_-_ The Rogue's body replacement talent not triggering effects like chasms and traps\n" +
				"_-_ Followup strike buff rarely not clearing when enemies die",

				"_Misc.:_\n" +
				"_-_ Various rare crash issues\n" +
				"_-_ Various minor visual and textual errors\n" +
				"_-_ Various rare cases where levelgen could differ between two runs on the same seed\n" +
				"_-_ Into Darkness and Barren Land challenges affecting levelgen\n" +
				"_-_ Items in Imp's shop not being affected by dungeon seed\n" +
				"_-_ Gold ore appearing on the back face of walls in regular caves levels\n" +
				"_-_ Starflower plant VFX triggering even when out of the hero's FOV\n" +
				"_-_ Storm Clouds not correctly clearing fire or harming fiery enemies\n" +
				"_-_ Cases where pushing effects could cause pitfalls to trigger early\n" +
				"_-_ Combining diagonal direction keys on desktop causing rare errors\n" +
				"_-_ Cases where default keybindings could override custom ones\n" +
				"_-_ Crashes caused by text input windows for controller users"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DIRK), "Weapon Ability Buffs",
				"Weapon abilities are overall in a much better place now, but there are still a couple that need a boost:\n" +
				"\n" +
				"_- Sneak_ ability (Dagger, Dirk, and Assassin's blade) reworked. Now lets the Duelist instantly blink for 6/5/4 tiles of distance and grants 1 turn of invisibility, instead of granting 10/8/6 turns of invisibility.\n" +
				"\n" +
				"_- Harvest_ ability (Sickle and War Scythe) bleeding amount increased to 110%/90% of damage, from 100%/80% of damage.\n" +
				"\n" +
				"_- Brawler's Stance_ ability (Ring of Force) charge use down to 1/6 per attack, from 1/4 per attack."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KATANA), "Weapon & Curse Nerfs",
				"Relatively mild changes here, a slight nerf to a new weapon and nerfs to the two most harmful curses (i.e. they are less detrimental now).\n" +
				"\n" +
				"_- Katana_ blocking down to 0-3 from 0-4\n" +
				"\n" +
				"_- Sacrificial_ curse bleeding amount reduced by ~25%\n" +
				"_- Sacrificial_ curse no longer always deals a minimum of 1 bleeding when it triggers\n" +
				"\n" +
				"_- Corrosion_ curse turns of ooze down to 10, from 20."));

		changes.addButton( new ChangeButton(Icons.get(Icons.STAIRS), "Ascension Challenge",
				"The combination of enemy stat scaling adjustments and the switch to boosting HP instead of reducing damage taken has made the earlier floors in the ascension challenge a little harder than intended, so I'm scaling things back:\n" +
				"\n" +
				"_-_ Reverted the v2.1.0 increase to enemy stats in the caves and prison during the ascension challenge."));

	}

	public static void add_v2_1_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 2nd, 2023\n" +
				"_-_ 83 days after Shattered v2.0.0\n\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAR_SCYTHE), "New Weapons!",
				"Three new weapons have been added to the game!\n" +
				"\n" +
				"_The Katana_ is a tier-4 defensive weapon that was designed to respond to the common feedback point of players wanting a higher tier weapon with the rapier's ability. Just like with her rapier, the Duelist can _lunge_ at enemies with a katana, dealing bonus damage.\n" +
				"\n" +
				"_The Sickle_ and _War Scythe_ are T2 and T5 weapons that trade in some accuracy for increased base damage. The Duelist can use the _harvest_ ability with these weapons, which deals a large amount of bleeding instead of direct damage, but costs 2 charges."));

		changes.addButton( new ChangeButton(new Image(new TormentedSpiritSprite()), "New Exotic Enemies",
				"An exotic variant has been added for wraiths and piranhas!\n" +
				"\n" +
				"_Tormented Spirits_ replace 1 in every 100 wraiths, and have higher stats along with a unique interaction. Using a scroll of remove curse on these spirits will save them from their curse, peacefully defeating them and giving you an uncursed equipment reward!\n" +
				"\n" +
				"_Phantom Piranhas_ replace 1 in every 50 piranhas, and can teleport either to attack or retreat whenever they take damage. They're harder to kill, but give valuable phantom meat as a drop, instead of regular mystery meat. They're also a reference to the phantom fish quest from the original Pixel Dungeon!"));

		changes.addButton( new ChangeButton(new Image(new ShopkeeperSprite()), "Shop Interface Improvements",
				"_A new UI has been added when interacting with shopkeepers._ This UI lets you talk with them and buyback the 3 most recently sold items!\n" +
				"\n" +
				"I plan to add more dialogue and region-specific shopkeepers in the future, but for now there is a bit of dialogue that varies based on the region and the hero you're playing as."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.STAIRS.get(), "Ascension Changes",
				"I've made several changes to the Ascension challenge to make it more fun, mesh better with some items/effects, and to smooth out its difficulty:\n\n" +
				"_-_ Enemies now gain increased max HP during ascent, instead of damage resistance\n\n" +
				"_-_ Enemies now grant exp to the hero up to level 30 while ascending\n" +
				"_-_ Even at level 30, on-exp-gain effects will now still trigger as long as the amulet's curse is being weakened\n\n" +
				"_-_ Increased enemy stat scaling in the earlier floors of the caves and prison"));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.PRECISE_ASSAULT), "Precise Assault",
				"The _Lightweight Charge_ talent has been completely replaced with a new talent: _Precise Assault_. This new talent grants a big accuracy boost for 1 attack after using a weapon ability, encouraging interweaving special and regular attacks.\n\n" +
				"Lightweight Charge is removed as it was originally designed much earlier in the Duelist's development process. This was before mechanics like Swift Equip, the Champion's Dual wielding, or Unencumbered Spirit existed. Those mechanics do a much better job of encouraging using abilities on lower tier weapons."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ Enemy misses now interrupt the hero, just like hits\n" +
				"_-_ Boss regen limits are now less permissive, especially with badder bosses enabled\n" +
				"_-_ Certain Weapons and Wands are no longer slightly more likely to spawn than others\n" +
				"_-_ Players are now less likely to find multiple of the same weapon/wand/ring\n" +
				"_-_ Slightly improved the final loot from crystal path rooms\n" +
				"_-_ The toolbar's dimming behaviour is now less visually disruptive\n" +
				"_-_ Added visual buffs for some Duelist and Huntress talents\n" +
				"_-_ Shattered Supporters on Google Play now have access to 10 HoH pages, up from 4",

				"_Other:_\n" +
				"_-_ Water effects now damage fiery enemies\n" +
				"_-_ Enemies no longer randomly spawn on plants or traps\n" +
				"_-_ Adjusted Gladiator's combo window to be consistent with Monk's ability window\n" +
				"_-_ Adjusted tutorialization relating to surprise attacks\n" +
				"_-_ Improved the icon for the locked floor status\n" +
				"_-_ Magical fire can now burn items placed next to it by the player",

				"_v2.1.1:_\n" +
				"_-_ Internal code changes to support content coming in v2.2.0\n" +
				"_-_ Barkskin can now be applied from multiple sources at once\n" +
				"_-_ Rooted debuff now more consistently prevents movement abilities\n" +
				"_-_ Various movement effects now take levitation into account\n" +
				"\n" +
				"_v2.1.2:_\n" +
				"_-_ Teleportation effects now the cleanse the rooted debuff\n" +
				"_-_ The Sandals of Nature have new vfx, and can now target visible tiles through walls.\n" +
				"_-_ Armors now include their tier in the description\n" +
				"\n" +
				"_v2.1.3:_\n" +
				"_-_ Heroes remains can now appear in more cases, but always contain 10 gold in challenge or seeded runs\n" +
				"_-_ Added a setting for screen shake intensity"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Various rare crash errors\n" +
				"_-_ Scrolls of Transmutation triggering the swift equip talent\n" +
				"_-_ Evil Eyes meleeing at a distance in rare cases\n" +
				"\n" +
				"_Items:_\n" +
				"_-_ Rare rounding errors when determining speed of augmented weapons\n" +
				"_-_ Ethereal chains clearing invisibility before they activate instead of after\n" +
				"_-_ Some spells not clearing invisibility\n" +
				"_-_ Grim enchantment interacting incorrectly with some damage resistance effects\n" +
				"_-_ Kinetic enchantment building damage from ally kills\n" +
				"_-_ Artifact -> Ring transmutation generating a purely random ring\n" +
				"_-_ Wand of Lightning not spreading correctly to flying enemies over water\n" +
				"_-_ Errors when transmuting an equipped artifact into a ring\n" +
				"_-_ Displacing darts not properly granting vision of teleported enemies in some cases\n" +
				"_-_ Empty item spots on the floor 20 shop",

				"_Effects:_\n" +
				"_-_ Some Duelist abilities not clearing invisibility\n" +
				"_-_ Cases where the player could input actions during the Sniper's flurry ability\n" +
				"_-_ Speedy Stealth talent requiring existing momentum to work\n" +
				"_-_ Rare errors caused by triggering many pitfall traps at once\n" +
				"_-_ Projectile traps firing at incorrect targets in very specific cases\n" +
				"_-_ Lethal Haste talent triggering on ally kills\n" +
				"_-_ Very rare cases where the Duelist could be debuffed by attacking her own afterimage\n" +
				"_-_ Monk's focus ability rarely triggering on magical attacks\n" +
				"_-_ Chill and ring of elements interacting incorrectly in some cases\n" +
				"_-_ Protective Shadows talent not triggering if unlocked while invisible\n" +
				"_-_ Some effects resolving before beacon of returning teleportation applies itself",

				"_Allies & Enemies:_\n" +
				"_-_ Necromancers not properly tracking their enemy while summoning\n" +
				"_-_ Giant necromancers teleporting their skeletons into enclosed spaces\n" +
				"_-_ Ally swapping causing errors in rare cases\n" +
				"_-_ Transmogrify armor ability incorrectly interacting with the multiplicity glyph\n" +
				"_-_ Rat King blocking Goo's pump-up attack\n" +
				"_-_ Enemy item drops being visible out of FOV for one frame\n" +
				"_-_ Tengu smoke bombs visually destroying each other\n" +
				"\n" +
				"_UI/VFX:_\n" +
				"_-_ Various minor visual/textual errors\n" +
				"_-_ Particle FX failing to appear after loading during the 4th boss fight\n" +
				"_-_ Action indicator not updating properly from Speedy Stealth talent\n" +
				"_-_ Incorrect error messages when the game tries to report crashes on desktop\n" +
				"_-_ Invalid move actions (e.g. into unknown areas) causing self-trample to activate",

				"_v2.1.1_:\n" +
				"_-_ Specific exploits involving moving between floors on a partial turn\n" +
				"_-_ Enemy AI quirks which caused them to not switch away from inaccessible targets in some cases\n" +
				"_-_ Various cases where the hero would not lose a debuff after becoming immune to it\n" +
				"_-_ Hidden mimics not triggering some talents that only occur against enemies\n" +
				"_-_ Challenge ability having a range of 4 tiles, instead of 5 as stated\n" +
				"_-_ Shield battery talent being usable with an empty wand\n" +
				"_-_ Elemental strike not working correctly with a friendly weapon\n" +
				"_-_ Ally AI errors when told to attack while repositioning\n" +
				"_-_ Visual bugs when multiple arcane bombs were used at once\n" +
				"_-_ Various minor textual errors\n" +
				"\n" +
				"_v2.1.2:_\n" +
				"_-_ Rare visual errors with the lunge ability",

				"_v2.1.3:_\n" +
				"_-_ Various rare crash and hang bugs\n" +
				"_-_ Weaker healing effects overiding stronger ones\n" +
				"_-_ Divine inspiration potion not being cancellable if it was already identified\n" +
				"_-_ Very rare cases where tapping a location wouldn't cause the hero to move\n" +
				"_-_ Armored brutes blocking more damage than intended\n" +
				"\n" +
				"_v2.1.4:_\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Specific exploits that allowed noisemakers to alert enemies as they were defused\n" +
				"_-_ Regen-disabling effects not affecting the warrior's shield\n" +
				"_-_ Counter Ability talent incorrectly making all abilities cost 0 charge when upgraded to +2\n" +
				"_-_ Visual bugs when hiding mimics were magically slept"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Whip(),
				"The whip is getting a quick and simple buff, as it is performing a bit worse than its T3 counterparts:\n\n" +
				"_- Whip_ base damage increased to 3-15 from 3-12"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAR_HAMMER), "Weapon Abilities",
				"Several of the Duelist's weapon abilities have not been performing as well as I'd hoped, even after the buffs in v2.0.1. It seems that I've overestimated how much the drawbacks I added have held back many of these abilities. My hope with these changes is to better balance other abilities versus the simple-yet-effective Cleave.\n" +
				"\n" +
				"Firstly, some abilities need relatively little help:\n" +
				"_- Sneak_ invis duration up to 10/8/6 from 8/6/4\n" +
				"_- Combo strike_ damage boost up to 45/40/35%, from 40/35/30%\n" +
				"_- Spike damage_ bonus up to 45/30% from 40/30%\n" +
				"_- Defensive Stance_ evasion boost up to 3x from 2x, duration down to 5 turns from 6\n" +
				"_- Lash_ is now guaranteed to hit the closest target\n" +
				"_- Block_ duration up to 8/6 from 5/4\n" +
				"_- Runic Slash_ enchant bonus up to +300% from +250%",

				"And some abilities need more substantial buffs:\n" +
				"_- Sword Dance_ now gives +25% ACC, up from -20%, duration down to 5 turns from 6\n" +
				"_- Spin_ now guarantees a hit at all levels, instead of just at 3 spins\n" +
				"_- Retribution_ damage bonus up to +50% from +35%\n" +
				"\n" +
				"_Heavy Blow:_\n" +
				"_-_ Now always hits, but costs 2 charges if it is not surprise attacking\n" +
				"_-_ Damage boost reduced to 50-30%, from 70-50%\n" +
				"_-_ Now applies a new 'daze' debuff instead of vulnerable or weakness. Daze halves accuracy and evasion for 5 turns.\n" +
				"\n" +
				"_Charged Shot:_\n" +
				"_-_ Now grants +4 dart tip uses, up from +2\n" +
				"_-_ Now triggers on-hit effects in a 5x5 AOE\n" +
				"_-_ Harmful on-hit effects now only apply to enemies in the AOE, and positive effects only apply to allies."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 2), "Duelist Subclasses",
				"The Duelist's subclasses have not had as many problems as her weapon abilities, so the changes here are more incremental. I've been more generous with the Monk, in anticipation that the weapon ability changes will be more beneficial to the Champion:\n\n" +
				"_Champion:_\n" +
				"_- Combined Lethality_ HP threshold up to 13/27/40 from 10/20/30\n\n" +
				"_Monk:_\n" +
				"_- Unencumbered Spirit_ energy boost up to 40/80/120% from 33/67/100%. Boost at +3 when unarmed removed\n" +
				"_- Combined Energy_ now refunds 50% of energy spent, up from 33%\n" +
				"_Monastic Vigor:_\n" +
				"_-_ Flurry enchantment power up to 100% from 75%\n" +
				"_-_ Dash range boost up to +3 from +2\n" +
				"_-_ Dragon Kick damage boost up to +50% from +33%"));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "Duelist Armor Abilities",
				"Armor abilities are also getting more minor changes, targeted at specific talents and the elemental strike ability:\n\n" +
				"_- Elemental Strike_ AOE effect base power increased by 20-33%, varying by enchantment\n\n" +
				"_- Expose Weakness_ turns of vulnerable increased to 2/4/6/8 from 1/2/3/4\n\n" +
				"_- Invigorating Victory_ base heal increased to 5/10/15/20 from 3/6/9/12"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 4), "Duelist Nerfs",
				"The Duelist is also receiving a few very specific nerfs in v2.1.0:\n\n" +
				"_- Meditate_ now grants the monk 8 turns of recharging, down from 10.\n" +
				"_- Meditate_ recharging now begins after meditation ends, instead of when it starts.\n\n" +
				"_- Elimination Match_ charge cost reduction down to 16/30/40/50% from 20/36/50/60%"));

	}

	public static void add_v2_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released March 13th, 2023\n" +
				"_-_ 160 days after Shattered v1.4.0\n" +
				"_-_ 413 days after Shattered v1.0.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 1), HeroClass.DUELIST.title(),
				"_Shattered Pixel Dungeon has a new playable character!!_\n" +
				"\n" +
				"The Duelist is an entire new hero class, _with a unique ability for every weapon in the game!_ She can be unlocked by earning a simple badge for equipping a T2 or higher weapon without a strength penalty. However, for existing players who already have a win she is unlocked by default!\n" +
				"\n" +
				"I want the Duelist to provide more interesting gameplay options for melee weapon focused builds, that have previously been fairly plain and simple compared to builds using other items."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 4), "Duelist Subclasses",
				"_The Duelist has two subclasses, with 3 more talents each!_\n" +
				"\n" +
				"_The Champion_ is a master of melee weapons who can equip a secondary weapon in addition to her primary one!\n" +
				"\n" +
				"_The Monk_ is a master of physical technique. As she defeats enemies, she gains energy which can be used on a variety of defensive and utlity-focused abilities."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "Duelist Armor Abilities",
				"_The Duelist has three lategame armor abilities, with 3 more talents each!_\n" +
				"\n" +
				"_Challenge_ compels an enemy to fight the Duelist while all other enemies are temporarily frozen in time.\n" +
				"\n" +
				"_Elemental Strike_ produces a special effect that is based on the Duelist's weapon enchantment.\n" +
				"\n" +
				"_Feint_ allows the Duelist to sidestep an enemy's attack while they are focused on her afterimage."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), "Ascension Challenge",
				"I'm making a few adjustments to the ascension challenge to slightly increase the difficulty of some regions, while also reducing the pressure to kill enemies a little bit:\n" +
				"\n" +
				"_-_ Enemies to kill per floor reduced to 2 from 2.5. Thresholds for all amulet debuff effects adjusted to compensate\n" +
				"\n" +
				"_- Ripper demon_ spawn rate increased if player is ascending\n" +
				"\n" +
				"_- Monk & Warlock_ stat boost up to 1.5x from 1.33x\n" +
				"_- Elemental & Ghoul_ stat boost up to 1.67x from 1.5x\n" +
				"\n" +
				"_- Crab & Slime_ stat boost up to 8x from 6x\n" +
				"_- Swarm_ stat boost up to 8.5x from 6.5x\n" +
				"_- Gnoll & Snake_ stat boost up to 9x from 7x\n" +
				"_- Rat_ stat boost up to 10x from 8x"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ The game now remembers if the player removes the waterskin from their quickslot right after starting a run\n" +
				"_-_ The damage warning vfx now always interrupts the hero, regardless of any other factors\n" +
				"_-_ The deadly misstep badge can now also be unlocked with disintegration traps\n" +
				"_-_ Added metamorphosis effects to the two remaining talents that previously couldn't be gained by metamorphosis\n" +
				"_-_ Desktop users can now toggle fullscreen with right-alt + enter\n" +
				"_-_ Added a setting to enable/disable playing music in background on desktop\n" +
				"_-_ Added a 5th games in progress slot for the Duelist",

				"_Translations:_\n" +
				"_-_ Added a new Language: _Vietnamese!_\n" +
				"_-_ Removed the following Languages due to low usage and lack of maintenance: Finnish, Galician, Basque, Esperanto.\n" +
				"\n" +
				"_iOS:_\n" +
				"_-_ Improved gesture handling, should reduce/eliminate touch delays\n" +
				"_-_ Removed unused audio files, reducing download size by about 15%\n" +
				"\n" +
				"_Effects:_\n" +
				"_-_ Backup barrier now triggers before wand zaps fully resolve\n" +
				"_-_ The chasm warning screen now also appears when levitation is about to end.",

				"_Hero, Allies, & Enemies:_\n" +
				"_-_ The hero can now also self-trample plants, in addition to grass\n" +
				"_-_ Ripper demons will now try to leap even if their destination is blocked by terrain\n" +
				"_-_ Red Sentry attacks can now be dodged or blocked, but are very accurate.\n" +
				"_-_ Knockback effects now round up after being halved vs. bosses\n" +
				"\n" +
				"_Levelgen:_\n" +
				"_-_ Adjusted the layout of sacrifice rooms to provide some cover from ranged enemies\n" +
				"_-_ Secret rooms now never affect the generation of items in other rooms\n" +
				"_-_ Items and Enemies can no longer spawn on the Wandmaker quest ritual marker.",

				"_Items:_\n" +
				"_-_ Several artifacts now cancel invisibility when used\n" +
				"_-_ Items no longer spawn on pitfall traps\n" +
				"_-_ Ritual candles now light if they are placed correctly\n" +
				"_-_ Item selectors now always open the main backpack if their preferred bag isn't present\n" +
				"\n" +
				"_Misc:_\n" +
				"_-_ Updated the icons for several talents\n" +
				"_-_ Healing no longer interrupts resting when HP is already full\n" +
				"_-_ Updated various code libraries",

				"_v2.0.1:_\n" +
				"_-_ Quickslot contents can now automatically swap when equipped items are swapped.\n" +
				"_-_ The changes screen now supports more text for a single entry.\n" +
				"_-_ Attacking an enemy now properly sets them as the auto-targeting target in all cases\n" +
				"_-_ Levitation now prevents damage from floor electricity during the DM-300 fight\n" +
				"_-_ Removed support for save data prior to v1.2.3\n" +
				"\n" +
				"_v2.0.2:_\n" +
				"_-_ Overhauled visuals for the action indicator. Some info has been moved from buff icons to this new indicator.\n" +
				"_-_ Rankings now attempt to show some basic information if loading full game data fails.\n" +
				"_-_ The changes scene now shows a warning if the user is not viewing it in English.\n" +
				"_-_ Liquid metal value increased to 1 from 0.5."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Various rare crash and freeze errors\n" +
				"_-_ Softlocks caused by the warden using fadeleaf just as they start a boss fight\n" +
				"_-_ Particle effects failing to appear in a bunch of rare cases\n" +
				"_-_ AOE from gladiator's crush move invalidating Dwarf King's 'no weapons' badge\n" +
				"_-_ Magic resistance being extremely effective against Grim traps at low HP\n" +
				"_-_ Allies spawned by some armor abilities getting boosted stats during ascension\n" +
				"_-_ One upgrade being lost when transferring class armor with a warrior's seal attached\n" +
				"_-_ Transmuting a dried rose deleting any items held by the ghost\n" +
				"_-_ Rare cases of hero stacking onto enemies when trying to swap positions with an ally\n" +
				"_-_ Directable allies being easily distracted after being told to move\n" +
				"_-_ Several on-kill effects incorrectly triggering when ghouls get downed",

				"_Effects:_\n" +
				"_-_ Lethal momentum not triggering on kills made via enchantment\n" +
				"_-_ Teleportation effects not being blocked by magic immunity\n" +
				"_-_ Barkskin not reducing damage from things like bombs or the chalice of blood\n" +
				"_-_ Some armor abilities not checking if targets are out of vision\n" +
				"_-_ Magical fire not clearing regular fire if they are on the same tile\n" +
				"_-_ Gladiator being able to riposte enemies who charmed him\n" +
				"_-_ Iron Stomach talent cancelling fall damage in rare cases\n" +
				"_-_ Time freeze causing various odd behaviour when triggering plants and traps\n" +
				"_-_ Rare cases of earthroot armor and hold fast working after movement\n" +
				"_-_ Volley ability not triggering lethal momentum",

				"_Items:_\n" +
				"_-_ Darts being lost in rare cases when tipped darts have bonus durability\n" +
				"_-_ Alchemist's Toolkit not triggering the enhanced rings talent\n" +
				"_-_ Wand of fireblast rarely shooting great distances\n" +
				"_-_ Wand of lightning rarely taking credit for hero deaths not caused by it\n" +
				"_-_ Horn of plenty benefiting from artifact recharging much more than intended\n" +
				"_-_ Shurikens still getting an instant attack after waiting\n" +
				"_-_ Transmutation not turning artifacts into rings if all artifacts have been spawned\n" +
				"_-_ Magic immunity not blocking use of shield battery, cursed artifact effects, or wand recharging\n" +
				"_-_ Cursed items still blocking equipment slots when lost via ankh revive\n" +
				"_-_ Antimagic not reducing damage from enchantments\n" +
				"_-_ Rare cases where cloak of shadows wouldn't spend a charge on activation\n" +
				"_-_ Disarming traps rarely teleporting weapons into chests or graves\n" +
				"_-_ Blacksmith failing to take his pickaxe back in rare cases\n" +
				"_-_ Various rare errors with blacksmith reforging and resin boosted wands",

				"_Allies & Enemies:_\n" +
				"_-_ DM-300 not using abilities in its first phase in specific situations\n" +
				"_-_ DM-201s rarely lobbing grenades when they shouldn't\n" +
				"_-_ DM-300's rockfall attack very rarely having no delay\n" +
				"_-_ Tengu rarely throwing bombs into walls\n" +
				"_-_ Soiled fist being able to see through shrouding fog\n" +
				"_-_ Rare cases where the Imp's shop could appear without completing his quest\n" +
				"_-_ Gladiator not gaining combo from attacking hiding mimics\n" +
				"_-_ Demon spawners rapidly spawning ripper demons in very specific cases\n" +
				"_-_ Fly swarms often not splitting during ascension challenge\n" +
				"_-_ Rare cases where enemies couldn't be surprise attacked when in combat with allies\n" +
				"_-_ Various rare errors with shock elemental electricity damage\n" +
				"_-_ Evil eyes only resisting some disintegration effects\n" +
				"_-_ Several rare issues with spinner web shooting\n" +
				"_-_ Very rare cases where surprise attacks on mimics would fail\n" +
				"_-_ Very rare pathfinding bugs with flying enemies",

				"_UI/VFX:_\n" +
				"_-_ Various minor audiovisual errors\n" +
				"_-_ Various minor textual errors\n" +
				"_-_ Items rarely disappearing when hotkeys are used to close the inventory\n" +
				"_-_ Number display errors when device language is set to Arabic\n" +
				"_-_ 'i' being incorrectly uppercased to 'I' in Turkish\n" +
				"_-_ Auras from champion enemies being visible in the fog of war for one frame\n" +
				"_-_ Very rare cases where Goo attack particles behaved incorrectly\n" +
				"_-_ VFX rarely not appearing on characters as they are spawned by multiplicity\n" +
				"_-_ Damage warn vfx not accounting for hunger ignoring shielding\n" +
				"_-_ Cases where very fast heroes would cause landmarks to not be recorded\n" +
				"_-_ No error message being given when the mage uses elemental blast without a staff",

				"_v2.0.1:_\n" +
				"_-_ Various UI bugs caused by pressing multiple buttons simultaneously\n" +
				"_-_ Noisemakers being visually defusable after trigger but not exploding\n" +
				"_-_ Noisemakers being collectable in some cases after triggering\n" +
				"_-_ Damage/Stun from blastwave knockback applying to downed ghouls\n" +
				"_-_ Projecting champions with ranged attacks refusing to use melee in some cases\n" +
				"_-_ Life Link sometimes persisting for longer than intended during Dwarf King fight\n" +
				"_-_ Various rare UI bugs\n" +
				"\n" +
				"_v2.0.2:_\n" +
				"_-_ Various bugs with controller input and simultaneous button presses\n" +
				"_-_ Hero being able to leave floor 5 after it locks in rare cases\n" +
				"_-_ Various minor rounding errors in alchemy produce values\n" +
				"_-_ Spirit bow encumbrance calculations always using a strength requirement of 10\n" +
				"_-_ Downed ghoul visuals sometimes staying visible in the fog of war"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_TOPAZ), "Item Buffs",
				"I'm making several buffs to various items that could be performing a little better:\n" +
				"\n" +
				"_- Pickaxe_ can now benefit from upgrades enchantments and augmentation, if you feel like using it for fun.\n" +
				"\n" +
				"_- Ring of Energy_ now also applies a recharging boost to hero armor abilities. All the boosts it gives are now standardized to +15%.\n" +
				"_- Ring of Arcana_ enchantment boost up to +17.5% per level, from +15%\n" +
				"\n" +
				"_- Glyph of Repulsion_ now only knocks back enemies who are adjacent to the hero. This should make it slightly better versus ranged enemies.\n" +
				"_- Glyph of Flow_ now grants +50% movespeed in water per level, up from +25%\n" +
				"\n" +
				"_- Horn of Plenty_ now gains 2 levels from being fed a pasty, up from 1.5\n" +
				"_- Horn of Plenty_ now gains 4 levels from being fed a meat pie, up from 3"));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 6), "Hero Buffs",
				"A couple buffs to the two worst performing armor abilities/talents:\n" +
				"\n" +
				"_- Shrug it Off_ now just directly boosts the damage resistance from endure to 60/68/74/80%, instead of reducing max damage taken\n" +
				"\n" +
				"_- Wild Magic_ now spends 0.5 wand charges at base. Conserved magic can reduce this to 0.1\n" +
				"\n" +
				"I expect the next update will include more hero balance changes, after the dust settles from the Duelist."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ROUND_SHIELD), "Major Item Nerfs",
				"I'm also making several significant nerfs to items with standout winrates this update:\n" +
				"\n" +
				"_- Round Shield_ blocking per level reduced to 0-1 from 0-2, base damage increased to 3-12 from 3-10\n" +
				"_- Greatshield_ blocking per level reduced to 0-2 from 0-3, base damage increased to 5-18 from 5-15\n" +
				"\n" +
				"_Wand of Corruption:_\n" +
				"_-_ Corrupted enemies now die over 100 turns, down from 200\n" +
				"_-_ Doomed bosses now take +67% damage, down from +100%\n" +
				"_-_ Battlemage corruption on-hit base proc rate reduced to 1/6 from 1/4\n" +
				"\n" +
				"_Wand of Regrowth:_\n" +
				"_-_ Base charge limit increased to 20 from 8\n" +
				"_-_ Charge limit scaling substantially reduced at wand levels 4 to 9.\n" +
				"\n" +
				"_Chalice of Blood:_\n" +
				"_-_ Prick damage increased by 5 at all levels\n" +
				"_-_ Now grants 1.15x-5x healing, down from 0x-10x\n" +
				"\n" +
				"_- Ethereal chains_ charge from gaining exp reduced by 40%"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_RUBY), "Smaller Item Nerfs",
				"There are also some more minor nerfs to items that are just doing a bit too well:\n" +
				"\n" +
				"_- Ring of Furor_ attack speed boost per level down to 9.05% from 10.5%\n" +
				"_- Ring of Evasion_ dodge boost per level down to 12.5% from 15%\n" +
				"\n" +
				"_- Blocking_ enchantment now grants 2+item level shielding, down from max HP/10\n" +
				"\n" +
				"_- Timekeeper's Hourglass_ sand bag cost up to 30 from 20\n" +
				"_- Alchemist's Toolkit_ now requires 6 energy per level, up from 5\n" +
				"\n" +
				"_- Wand of Fireblast_ base damage reduced to 1-2 from 1-6 when spending 1 charge, and 2-8 from 2-12 when spending 2 charges. This is to offset the relatively high amount of DOT the wand deals at low levels."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.ROGUE, 6), "Hero Nerfs",
				"T1 Talents:\n" +
				"_-_ Warrior's T1 ID talent renamed from _Armsmaster's Intuition_ to _Veteran's Intuition_. The talent now focuses more on armor, to contrast with the Duelist.\n" +
				"\n" +
				"Armor Abilities:\n" +
				"_- Heroic Energy_ lightly nerfed to 12/23/32/40% charge speed boost, from 13/24/34/43%\n" +
				"\n" +
				"_- Double Jump_ charge cost reduction down to 16/30/41/50%, from 20/36/50/60%\n" +
				"\n" +
				"_- Smoke Bomb_ range down to 6 from 10\n" +
				"_- Shadow Step_ charge cost reduction down to 16/30/41/50%, from 20/36/50/60%"));

	}

}
