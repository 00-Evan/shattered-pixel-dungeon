/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v1_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v1_3_Changes(changeInfos);
		add_v1_2_Changes(changeInfos);
		add_v1_1_Changes(changeInfos);
		add_v1_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Overview",
			"The next Shattered update will be v1.4.0! After focusing on game porting and multiple smaller additions, v1.4.0 will finally focus on new core game content! I expect v1.4.0 to take about 2 months to complete, as I want to focus primarily on the core content and not overload the update too much with other smaller additions.\n\n" +
			"I should have some more info on v1.4.0 to share in a blog post some time in August."));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY_LAND), "Desktop UI Improvements",
			"...Before v1.4.0 though, I would like to add a few interface improvements for desktp users in some v1.3 patches. In particular I still haven't added more detailed enemy info to the main game screen. I expect to be adding these things along with the usual bugfixes in patches for v1.3."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE), "Blacksmith Quest",
			"The main content in v1.4.0 is going to be a total rework to the blacksmith's quest in the caves! Currently the quest is pretty simple, and I'd like to expand it to continue the trend of quests getting progressively more involved. Expect more mining for rare ore and gems, with some greater rewards if you perform well!"));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Side Content",
			"I plan to also have one significant piece of side content in v1.4.0. I haven't yet locked this in, so there are a few possibilities. Expect the side content to be on the scale of a single new item, challenge, or similar."));

		changes.addButton( new ChangeButton(Icons.get(Icons.TALENT), "Mechanics Tweaks",
			"v1.4.0 will also have some tweaks, as usual. In particular I plan to focus on hero talents and some pieces of equipment. Shattered's overall balance is better than it ever has been, but there is always room for more incremental improvements."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET), "What About a New Hero?",
			"Lastly, while this won't be coming in v1.4.0, I am making some progress on adding a new hero in 2022! Expect to hear more on this after v1.4.0 releases!"));

	}

	public static void add_v1_3_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v1.3", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("v1.3.1", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Wards and Sentries are now immune to sleep, vertigo and fear\n" +
				"_-_ Updated translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by v1.3.0):\n" +
				"_-_ Runs ended by debuffs not appearing in rankings\n" +
				"_-_ Bugs with new hero armor transferring ability\n" +
				"_-_ Text for daily run dates not being language-neutral"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ v1.3.0 Released July 6th, 2022\n" +
				"_-_ 105 days after Shattered v1.2.0\n" +
				"Expect dev commentary here in the future."));

		Image ic = Icons.get(Icons.SEED);
		ic.hardlight(1f, 1.5f, 0.67f);
		changes.addButton( new ChangeButton(ic, "Seeded Runs!",
				"_It's now possible to enter a custom seed when starting a new game!_\n\n" +
				"Seeds are used to determine dungeon generation, and two runs with the same seed and game version will produce the exact same dungeon to play though.\n\n" +
				"If you don't enter a custom seed, the game will use a random one to generate a random dungeon, just like it did prior to this update.\n\n" +
				"Note that only players who have won at least once can enter custom seeds, and games with custom seeds are not eligible to appear in rankings."));

		ic = Icons.get(Icons.CALENDAR);
		ic.hardlight(0.5f, 1f, 2f);
		changes.addButton( new ChangeButton(ic, "Daily Runs!",
				"_Every day there is a specific seeded run that's available to all players!_\n\n" +
				"The daily run makes it easy to compete again friends or other folks on the internet, without having to coordinate and share a specific seed.\n\n" +
				"The game does keep track of your previous daily scores, but at the moment there is no leaderboard. You must win at least once to have access to daily runs."));

		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.HIGH_SCORE_2.image ), "New Score System!",
				"_The game's scoring system has been overhauled to go along with seeded runs and dailies!_\n\n" +
				"The score system now factors in a bunch of new criteria like exploration, performance during boss fights, quest completions, and enabled challenges. This should make score a much better measure of player performance.\n\n" +
				"A score breakdown page has also been added to the rankings screen. This page even works for old games, and retroactively applies the challenge bonus!"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AMULET), "Harder Ascension Route!",
				"_A bunch of adjustments have been made to the ascension route to make it a proper challenge!_\n\n" +
				"Enemies will get much stronger as you ascend, and it's impossible to teleport back up or flee and avoid all combat. Expect to have to work a little bit more for an ascension win!"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "UI/UX Improvements!",
				"_Several UI and UX Improvements have been made for desktop and mobile users_\n\n" +
				"_-_ The settings menu has been adjusted with a few new and rearranged options.\n" +
				"_-_ Added radial menus for controller users, and redid default controller bindings.\n" +
				"_-_ Added a quickslot swapper option for mobile portrait users.\n" +
				"_-_ Keyboard and controller key bindings now have separate windows\n" +
				"_-_ Added a few new key/button bindings actions"));

		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.BOSS_CHALLENGE_5.image ), "New Badges!",
				"_14 new badges have been added to the game!_\n\n" +
				"_-_ Five of these badges are 'high score' badges, meant to tie into the new score system.\n" +
				"_-_ Another five of these badges are 'boss challenge' badges, which each require you to defeat a boss in a particular way.\n" +
				"_-_ Four new 'cause of death' badges have also been added, which should be a little trickier than the existing ones.\n\n" +
				"Several of these badges are on the harder end, in particular the final high score and boss challenge badge should be a real challenge, even for veteran players."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SHORTSWORD, new ItemSprite.Glowing(0x000000)), "Curse Redesigns",
				"_Three of the most annoying curses have been replaced or redesigned, and two more have been adjusted._\n\n" +
				"_- Fragile_ has been replaced by _explosive,_ which builds power and then explodes!\n" +
				"_- Wayward_ has been redesigned to sometimes apply an accuracy reducing debuff, instead of always reducing accuracy.\n" +
				"_- Exhausting_ has been replaced by _dazzling,_ which can blind both the attacker and defender.\n\n" +
				"_Anti-entropy_ and _sacrifice_ have also been nerfed (i.e. made less harsh), look at the nerfs section for more details on that."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_ODAL), Messages.get(ScrollOfMetamorphosis.class, "name"),
				"The scroll of metamorphosis has been adjusted to allow more of the game's talents to work with its effect.\n\n" +
				"Several talents that were previously exempt from being chosen by the scroll now have alternative effects that let them be used by any hero.\n\n" +
				"These alternative effects only appear when getting these talents via metamorphosis."));

		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.MONSTERS_SLAIN_5.image ), "Badge Changes",
				"I'm making several changes to existing badges, based on feedback and data from Steam players:\n\n" +
				"_-_ Several gold tier badges have been bumped up to platinum tier to better reflect their difficulty\n" +
				"_-_ The 'grim reaper' badge has been bumped up to gold tier, from silver\n\n" +
				"_-_ The master and grandmaster 'monsters hunter' and 'treasure hunter' badges have been made more difficult\n" +
				"_-_ The alchemist badges have been rebalanced to start out easier and end up harder\n\n" +
				"_-_ The 'dungeoneer' badges have been made easier to unlock with wins or games played. The master and grandmaster versions still require a lot of games played though."));

		changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.TARGETED, true), "Buff and Spell Icons",
				"Several buffs have been given icons when they didn't have any, or have had their icons adjusted to prevent icon duplication. This should improve buff clarity in a few cases, and ensure that two active buffs can never have the exact same icon (recolored icons are still present though).\n\n" +
				"A few new overhead spell effects have been added as well."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Updated translations, translator credits, and added a new language: Dutch!\n" +
				"_-_ Made the Huntress a bit easier to unlock again\n" +
				"_-_ Dreamfoil has been renamed to Mageroyal, to better fit its lack of sleeping functionality since 1.2\n" +
				"_-_ Updated various code dependencies\n" +
				"_-_ Made major internal changes in prep for quest improvements in v1.4\n" +
				"_-_ Added a slight delay to chasm jump confirmation window, to prevent mistaps\n" +
				"_-_ Progress is now shown for badges that need to be unlocked with multiple heroes\n" +
				"_-_ Multiple unlocked badges can now be shown at once\n" +
				"_-_ Various minor tweaks to item and level generation to support seeded runs\n" +
				"_-_ Keys now appear on top of other items in pit rooms\n" +
				"_-_ Large floors now spawn two torches with the 'into darkness' challenge enabled\n" +
				"_-_ Blazing champions no longer explode if they are killed by chasms\n" +
				"_-_ Red sentries no longer fire on players with lost inventories"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed\n" +
				"_-_ Various minor textual and visual bugs\n" +
				"_-_ Final boss's summons being slightly weaker than intended when badder bosses is enabled\n" +
				"_-_ Great crab not blocking right after loading a save\n" +
				"_-_ Exploits that could force DM-300 to dig outside of its arena\n" +
				"_-_ Various 'cause of death' badges not being awarded if death occurred with an ankh.\n" +
				"_-_ Wraiths from spectral necromancers not always dying when the necromancer dies\n" +
				"_-_ The mystical charge talent giving more charge than intended\n" +
				"_-_ Ring of might HP bonus not applying in specific cases\n" +
				"_-_ Stones of blink disappearing if they fail to teleport\n" +
				"_-_ Beacon of returning not working at all in boss arenas\n" +
				"_-_ Earthen guardian not being immune to poison, gas, and bleed\n" +
				"_-_ Transmogrified enemies awarding exp when the effect ends"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN), "Armor Ability Buffs",
				"_- Endure_ damage bonus increased to 1/2 of damage taken from 1/3\n\n" +
				"_- Wild Magic_ base wand boost and max boost increased by 1\n" +
				"_- Fire Everything_ now has a 25% chance per point to let a wand be usable 3 times\n" +
				"_- Conserved Magic_ no longer lets a wand be usable 3 times, now grants a chance for wild magic to take 0 turns instead\n\n" +
				"_- Elemental power_ boost per point up to 25%, from 20%\n" +
				"_- Reactive Barrier_ shielding per point up to 2.5, from 2, and max targets now increases by 1 per point.\n\n" +
				"_- Shadow Clone_ now costs 35 energy, down from 50. Initial HP down to 80 from 100\n" +
				"_- Shadow Blade_ damage per point up to 8%, from 7.5%\n" +
				"_- Cloned Armor_ armor per point down to 12%, from 15%\n\n" +
				"_- Eagle Eye_ now grants 9 and 10 vision range at 3 and 4 points\n" +
				"_- Go for the Eyes_ now cripples at ranks 3 and 4\n" +
				"_- Swift Spirit_ now grants 2/4/6/8 dodges, up from 2/3/4/5"));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.WAND_PRESERVATION), Talent.WAND_PRESERVATION.title(),
				"Only one isolated talent change in this update:\n\n" +
				"_- Wand Preservation_ chance to preserve at +1 reverted to 67% from 50%, still grants 1 arcane resin if it fails to preserve"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY), "Crystal Path Rooms",
				"Loot from crystal path rooms (the rooms with a sequence of 3 crystal doors) has been buffed to make their value closer to other crystal key rooms."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CLEANSING_DART), "Alchemy Item Buffs",
				"I'm giving woolly bomb's a big buff to help make them more distinct from other sheep-spawning items:\n\n" +
				"_- Woolly Bombs_ now summon sheep for 200 turns, or 20 turns during boss fights, up from 12-16 turns. However, sheep no longer totally prevent bosses from summoning minions.\n\n" +
				"I've given some alternative functions to three darts that would previously only help allies:\n\n" +
				"_- Holy Dart_ turns of bless reverted to 30 from 100, now heavily damages undead or demonic enemies, instead of blessing them\n\n" +
				"_- Adrenaline Dart_ turns of adrenaline reverted to 10 from 30, now cripples enemies for 5 turns, instead of giving them adrenaline\n\n" +
				"_- Cleansing Dart_ now clears positive buffs from enemies, and causes them to start wandering (note that they may immediately re-aggro if they are close enough)"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN), "Armor Ability Nerfs",
				"Along with several armor ability buffs, two have received energy cost nerfs, mainly to reduce the uptime of positioning/escape abilities. Some compensation buffs have been given for the energy cost change as well:\n\n" +
				"_- Heroic Leap_ energy cost up to 35 from 25\n" +
				"_- Body Slam_ now adds 1-4 base damage per point in talent\n" +
				"_- Impact Wave_ now applies vulnerable for 5 turns, up from 3\n" +
				"_- Double jump_ energy cost reduction increased by 20%\n\n" +
				"_- Smoke Bomb_ energy cost up to 50 from 35\n" +
				"_- Smoke Bomb_ max range up to 10 from 6"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_LEATHER, new ItemSprite.Glowing(0x000000)), "Curse Nerfs",
				"Two of the most harsh curses have been nerfed (i.e. made better for the player):\n\n" +
				"_- Anti-Entropy_ now spreads less fire to the player, and freezes all adjacent tiles instead of just the enemy.\n\n" +
				"_- Sacrifice_ now more heavily scales on current HP, bleeding for a bit more at high health, and very little at medium to low health."));

		changes.addButton( new ChangeButton(new Image(new ElementalSprite.Fire()), "Floor 16 Adjustments",
				"Floor 16's spawn rates have been adjusted:\n\n" +
				"Ghouls up to 60% from 40%\n" +
				"Elementals down to 20% from 40%\n" +
				"Warlocks unchanged at 20%\n\n" +
				"This is to help smooth over a slight difficulty spike on that floor."));

	}

	public static void add_v1_2_Changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v1.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ v1.2.0 Released March 23rd, 2022\n" +
				"_-_ 103 days after Shattered v1.1.0\n" +
				"Expect dev commentary here in the future."));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY_LAND), "Desktop Enhancements and Steam Release!",
				"_Shattered Pixel Dungeon has received a bunch of new features in preparation for its release on Steam!_\n\n" +
				"These features include:\n" +
				"_-_ A new main UI for larger displays, which places the inventory in the main game screen\n" +
				"_-_ Full controller support, including button bindings and an analog stick cursor.\n" +
				"_-_ Better keyboard controls, including combining keys to move diagonally.\n" +
				"_-_ Better mouse support, including hover tooltips and right-click menus.\n" +
				"_-_ Two additional quickslots on the new UI, and on mobile UI if there is enough space.\n" +
				"_-_ Integration with Steamworks for achievements and cloud sync.\n\n" +
				"Users on mobile devices will be able to benefit from most of these features as well! (some feature require a large enough display)"));

		changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16, 16 ), "Special Rooms Additions!",
				"_Six new special rooms have been added!_\n\n" +
				"Two of these rooms (and one existing room) use new crystal doors, which let you see through them before you find a crystal key to unlock them.\n\n" +
				"Three of these rooms include new terrain hazards, which will require the right tools to get past.\n\n" +
				"The final new room is the sacrificial room from the original Pixel Dungeon! It returns with a few tweaks to its mechanics and loot (sorry, no scroll of wipe out)."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ARMBAND), "Armband Rework!",
				"_The Master Thieves' Armband has been reworked!_\n\n" +
				"This rework focuses on giving the armband usefulness outside of shops. You can now use it to steal from enemies as well as shopkeepers, and it gains charge as you gain exp, instead of when you collect gold."));

		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.MONSTERS_SLAIN_5.image), "New Badges!",
				"_Badges now have names, and 8 new badges have been added!_\n\n" +
				"These new badges are all part of the existing series badges (e.g. defeat X enemies), and primarily exist around the gold badge level.\n\n" +
				"The 'games played' badges have also been adjusted to unlock either on a large number of games played, or a smaller number of games won."));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "New Boss Music!",
				"_Each of the game's five bosses now have their own music track!_\n\n" +
				"Just as before, these tracks are all composed by Kristjan Harristo, check the about scene for more details on them.\n\n" +
				"All of the boss tracks take cues from the region tracks, but add enough to be more than simple remixes."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Reduced cases of multiple rooms that require a solution potion per floor\n" +
				"_-_ Reduced the huntress unlock requirement\n" +
				"_-_ Adjusted the secrets level feeling to be less harsh\n\n" +
				"_-_ Added a 'new game' and 'menu' button after game over\n" +
				"_-_ Made the blinking behaviour of the journal button easier to notice\n" +
				"_-_ quickslot placement is now preserved when items are transmuted\n" +
				"_-_ Improved the depth display to include icons for level feelings\n" +
				"_-_ Added an icon next to depth display showing enabled challenges\n" +
				"_-_ Made surprise attack VFX a bit more obvious\n" +
				"_-_ Improved the resilience of the game's save system\n\n" +
				"_-_ Added a new language: Galician!\n" +
				"_-_ Removed the Catalan translation as it was below 70% complete"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 1",
				"Fixed:\n" +
				"_-_ Various rare cases of save corruption on Android\n" +
				"_-_ Various minor textual and visual errors\n\n" +
				"_-_ Various rare cases where the hero could perform two actions at once\n" +
				"_-_ Rare cases where the Freerunner could gain momentum while freerunninng\n" +
				"_-_ Gladiator's parry move not cancelling invisibility or time freeze\n" +
				"_-_ Assassinate killing enemies right after they were corrupted by a corrupting weapon\n" +
				"_-_ Player being able to self-target with assassinate ability\n" +
				"_-_ Exploits involving corruption and the 13th armor ability\n\n" +
				"_-_ Various rare cases where giant enemies could enter enclosed spaces\n" +
				"_-_ On-hit effects still triggering when the great crab blocks\n" +
				"_-_ Corruption debuff affecting smoke bomb decoy\n" +
				"_-_ Character mind vision persisting after a character dies\n" +
				"_-_ Dwarf King not being targeted by wands or thrown weapons while on his throne"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 2",
				"Fixed:\n" +
				"_-_ Pharmacophobia challenge incorrectly blocking some alchemy recipes\n" +
				"_-_ Unidentified wands being usable in alchemy\n" +
				"_-_ Wild energy spell not cancelling invisibility or time freeze\n" +
				"_-_ Various rare bugs with the timekeeper's hourglass\n" +
				"_-_ Various bugs with the potion of dragon's breath\n" +
				"_-_ Artifact recharging not charging the horn of plenty in some cases when it should\n" +
				"_-_ Some items rarely not being consumed when they should be\n" +
				"_-_ Arcane catalysts not being able to be turned into energy\n" +
				"_-_ Fog of War not properly updating when warp beacon is used\n" +
				"_-_ Very rare cases where dried rose becomes unusable"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 3",
				"Fixed:\n" +
				"_-_ Rare cases where lullaby scrolls were generated by the Unstable Spellbook\n" +
				"_-_ Ring of might health boost not being affected by lost inventory debuff\n" +
				"_-_ Items that spawn identified counting as being IDed by the player\n" +
				"_-_ Some sources of artifact recharging affecting cursed artifacts\n" +
				"_-_ Blacksmith not refusing to work with cursed items in specific cases\n" +
				"_-_ An exploit where unblessed ankhs could be used with a lost inventory\n\n" +
				"_-_ Layout issues with the loot indicator\n" +
				"_-_ Floor 5 entrance rooms sometimes being smaller than intended\n" +
				"_-_ Red flash effects stacking on each other in some cases\n" +
				"_-_ Game forgetting previous window size when maximized and restarted\n" +
				"_-_ Cases where ghoul sprites could become glitched\n" +
				"_-_ Cases where heroic energy talent would use the wrong name/icon\n" +
				"_-_ Curse status of quickslot items not showing in rankings"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FIRE_BOMB), "Recipe Cost Reductions",
				"I've made a bunch of cost adjustments to alchemy recipes to help counteract energy becoming more expensive after v1.1.0:\n\n" +
				"_- Bomb Recipe_ energy costs down across the board\n\n" +
				"_- Infernal, Blizzard, and Caustic Brew_ energy costs down by 1\n\n" +
				"_- Telekinetic Grab_ energy cost down to 2 from 4, liquid metal cost reduced to 10 from 15\n" +
				"_- Phase Shift_ energy cost down to 4 from 6\n" +
				"_- Wild Energy_ energy cost down to 4 from 6\n" +
				"_- Beacon of Returning_ energy cost down to 6 from 8\n" +
				"_- Summon Elemental_ energy cost down to 6 from 8\n" +
				"_- Alchemize_ energy cost down to 2 from 3"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AQUA_BLAST), "Alchemy Buffs",
				"Several recipes have also been buffed, in addition to the cost reductions:\n\n" +
				"_- Scroll of Foresight_ duration up to 400 from 250\n" +
				"_- Scroll of Dread_ now grants 1/2 exp for defeated enemies\n" +
				"_- Potion of Shrouding Fog_ gas quantity increased by 50%\n\n" +
				"_-_ Items and effects which create water now douse fire\n\n" +
				"_- Caustic Brew_ damage per turn increased by 1\n" +
				"_- Infernal and Blizzard Brew_ now start their gas in a 3x3 AOE\n" +
				"_- Shocking Brew_ AOE up to 7x7 from 5x5\n\n" +
				"_- Phase Shift_ now stuns whatever it teleports\n" +
				"_- Summon Elemental_ quantity up to 5 from 3, elemental's stats now scale with depth, and elementals can be re-summoned\n" +
				"_- Aqua Blast_ now acts like a geyser trap, quantity down to 8 from 12\n" +
				"_- Reclaim Trap_ quantity up to 4 from 3\n" +
				"_- Curse Infusion_ now boosts highly levelled gear by more than +1, quantity up to 4 from 3.\n" +
				"_- Recycle_ quantity up to 12 from 8, cost up to 8 from 6"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ROT_DART), "Dart Buffs",
				"While they don't tie into v1.1.0's energy changes in particular, I am also handing out several buffs to tipped darts:\n\n" +
				"_- Rot Dart_ uses increased to 5 from 1\n" +
				"_- Adrenaline Dart_ duration up to 30 from 10\n" +
				"_- Shocking Dart_ damage now slightly scales with depth\n" +
				"_- Poison Dart_ damage scaling increased\n" +
				"_- Sleep Dart_ is now _Cleansing Dart_, makes allies immune to debuffs for several turns\n" +
				"_- Holy Dart_ duration up to 100 from 30\n" +
				"_- Displacing Dart_ now much more consistently teleports enemies away"));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.LIGHT_CLOAK.icon()), "Talent Buffs",
				"I'm handing out a few buffs to help better balance the Mage's T2 talents and the Rogue's class-based T3 talents. I'm also making one bugfix that counts as a buff:\n\n" +
				"_- Energizing Upgrade_ charge boost up to 4/6, from 3/5\n" +
				"_- Wand Preservation_ chance at +1 reduced to 50%, but now grants 1 arcane resin if it fails to preserve\n" +
				"_- Wand Preservation_ max uses up to 5 from 3\n" +
				"_- Empowering Scrolls_ now grants +3 on the next 1/2/3 wand zaps\n\n" +
				"_- Light Cloak_ charging rate boosted to 25%/50%/75%, from 17%/33%/50%\n\n" +
				"_- Shared Upgrades_ bugfixed to give the bonus damage stated in the description, instead of slightly less."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_INFUSE), "Alchemy Nerfs",
				"v1.2.0 is mostly about alchemy buffs, but a few alchemy items have had their power reduced as well:\n\n" +
				"_- Magical Infusion_ energy cost up to 4 from 3\n" +
				"_- Holy Bomb_ bonus damage reduced to 50% from 67%\n" +
				"_- Goo Blob and Metal Shard_ energy value reduced to 3\n" +
				"_- Alchemize_ quantity in shops reduced by 1\n\n" +
				"While not a direct alchemy item nerf, I've also made some of the final bosses' fists less susceptible to certain mechanics:\n" +
				"_- Soiled Fist_ is now immune to burning, but the grass it generates still burns\n" +
				"_- Burning Fist_ is now immune to freezing, but it can still be chilled\n" +
				"_- Rotting and Rusted Fists_ now take less damage from retribution, grim, and psionic blast"));

		changes.addButton( new ChangeButton( new Image(Assets.Environment.TERRAIN_FEATURES, 112, 112, 16, 16), "Dreamfoil",
				"Dreamfoil has always had great utility as a debuff-cleanser, and with the recent addition of stones of deep sleep its enemy sleeping functionality was feeling a bit unnecessary:\n\n" +
				"_- Dreamfoil_ no longer puts enemies into magical sleep\n\n" +
				"Sleep darts (made from dreamfoil) have also been changed into cleansing darts to go along with this change. These darts will make an ally temporarily immune to harmful effects."));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.SHIELD_BATTERY.icon()), "Talent Nerfs",
				"I'm making a few talent nerfs to better balance the Mage's T2 talents, and to pull in the power of the Berserker a little:\n\n" +
				"_- Shield Battery_ shielding per charge down to 4%/6%, from 5%/7.5%\n\n" +
				"_- Endless Rage_ max rage boost reduced to 10%/20%/30% from 15%/30%/45%\n" +
				"_- Enraged Catalyst_ proc rate boost reduced to 15%/30%/45% from 17%/33%/50%"));

	}

	public static void add_v1_1_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v1.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ v1.1.0 released December 10th, 2021\n" +
				"_-_ 115 days after Shattered v1.0.0\n" +
				"Expect dev commentary here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ENERGY), "Alchemical Energy Overhaul",
				"_The role of Alchemical Energy in the alchemy system has been totally overhauled!_\n\n" +
				"Energy is now a resource that the player carries with themselves, like gold. The game also generates much less energy for free, but more can be created by scrapping consumable items.\n\n" +
				"Many recipes have been adjusted to compensate for this. Exotic potions and scrolls now require energy instead of seeds/stones, and several of them have been buffed or totally redesigned (see buffs and changes sections for more details).\n\n" +
				"Other recipes have received relatively minor changes for now (mostly energy cost tweaks), but I'll likely be giving them more attention soon in future updates.\n\n" +
				"This repositions energy as the primary driving force for alchemy, and should make the system both more flexible and better at recycling consumables the player doesn't want into ones that they do."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SUMMON_ELE), "New and Reworked Spells",
				"While this update mostly focused changes on exotic potions and scrolls, there are _two new spells, and one totally redesigned spell:_\n\n" +
				"_Summon Elemental_ requires fresh embers and an arcane catalyst. It can be used to summon a friendly elemental to fight for you, and can even be powered up with other items!\n\n" +
				"_Telekinetic Grab_ requires some liquid metal and an arcane catalyst. It can be used to grab items remotely, even thrown items that are stuck to an enemy!\n\n" +
				"_Alchemize_ has been totally redesigned. It now only requires an arcane catalyst, and is used to convert items into gold or alchemical energy on the go. I'm really hoping this spell helps with inventory management.\n\n" +
				"Because of the redesign to alchemize, the merchant's beacon and magical porter are made mostly redundant and have been removed from the game. Shops now sell a few uses of alchemize instead."));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "more new music!",
				"_The game now has a music track for each of the five dungeon regions!_\n\n" +
				"Just like the remastered tracks from v1.0.0, they are all composed by Kristjan Harristo, check the about scene for more details on them.\n\n" +
				"Each of these tracks use a similar variable looping method to the sewers track, to try and reduce repetitiveness.\n\n" +
				"There have also been some small tweaks made to the existing sewers and title theme tracks."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.EXOTIC_ISAZ), "Exotic Reworks",
				"Several exotic potions and scrolls have been redesigned to be more powerful and worth using:\n\n" +
				"_- Potion of Holy Furor_ is now _Potion of Divine Inspiration_, which gives bonus talent points.\n" +
				"_- Potion of Adrenaline Surge_ is now _Potion of Mastery_, which reduces the strength requirement of one item by 2.\n\n" +
				"_- Scroll of Petrification_ is now _Scroll of Dread_, which causes enemies to flee the dungeon entirely.\n" +
				"_- Scroll of Affection_ is now _Scroll of Siren's Song_, which permanently makes an enemy into an ally.\n" +
				"_- Scroll of Confusion_ is now _Scroll of Challenge_, which attracts enemies but creates an arena where you take reduced damage.\n" +
				"_- Scroll of Polymorph_ is now _Scroll of Metamorphosis_, which lets you swap out a talent to one from another class." ));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc") + " 1",
				"_-_ Item drops and special room spawns are now more consistent. Getting loads of the same item is now much less likely.\n" +
				"_-_ Items present on boss floors are now preserved if the hero is revived from an unblessed ankh\n" +
				"_-_ Teleport mechanics now work on boss levels\n" +
				"_-_ Traps that teleport no longer work on items in chests or similar containers\n" +
				"_-_ Rewards from piranha and trap rooms now always appear in chests\n\n" +

				"_-_ Tipped darts can now be transmuted and recycled\n" +
				"_-_ Thrown weapons no longer stick to allies\n" +
				"_-_ Liquid metal production from upgraded thrown weapons now caps at +3"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc") + " 2",
				"_-_ Updated game icons on Android and Desktop platforms\n" +
				"_-_ Tabs in rankings and hero info windows now use icons, not text\n" +
				"_-_ 'potions cooked' badge and stats are now 'items crafted'\n\n" +

				"_-_ Newborn elementals no longer have a ranged attack\n\n" +

				"Various small improvements for iOS Devices:\n" +
				"_-_ Game can now run at higher framerates than 60\n" +
				"_-_ Ingame UI elements now move inward if notched devices are used in landscape\n" +
				"_-_ There is now an option to override silent mode\n\n" +

				"_-_ Updated translations and translator credits"));

		//TODO condense to two bugfix entries
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 1",
				"Fixed:\n" +
				"_-_ Various minor/rare visual and textual errors\n" +
				"_-_ Cases where pausing/resuming the game at precise moments would cancel animations or attacks\n" +
				"_-_ Endure damage reduction applying after some specific other damage-reducing effects\n" +
				"_-_ Unblessed ankh resurrection windows disappearing in some cases\n" +
				"_-_ Lucky enchantment rarely not trigger in some cases\n" +
				"_-_ Artifacts spawning upgraded from golden mimics\n" +
				"_-_ Unblessed ankh revival cancelling corpse dust curse\n" +
				"_-_ Unstable spellbook letting the player select unidentified scrolls\n" +
				"_-_ Desktop version not working correctly with FreeBSD\n" +
				"_-_ Liquid metal being usable on darts\n" +
				"_-_ Teleportation working on immovable characters in some cases\n" +
				"_-_ Various quirks with thrown weapon durability\n" +
				"_-_ Rare cases where ghouls would get many extra turns when reviving\n" +
				"_-_ Magical infusion not preserving curses on armor\n" +
				"_-_ Vertigo and teleportation effects rarely interfering\n" +
				"_-_ Layout issues in the hero info window with long buff names"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 2",
				"Fixed:\n" +
				"_-_ Cursed wands being usable to create arcane resin\n" +
				"_-_ Unblessed ankh revival rarely causing crashes or placing the player on hazards\n" +
				"_-_ Some glyphs not working for armored statues or the ghost hero\n" +
				"_-_ Various oddities with inferno gas logic\n" +
				"_-_ Spirit bow having negative damage values in rare cases\n" +
				"_-_ Artifact recharging buff working on cursed artifacts\n" +
				"_-_ Scrolls of upgrade revealing whether unidentified rings/wands were cursed\n" +
				"_-_ Ring of Might not updating hero health total in rare cases\n" +
				"_-_ Specific cases where darts would not recognize an equipped crossbow\n" +
				"_-_ Cap on regrowth wand being affect by level boosts\n" +
				"_-_ Some on-hit effects not triggering on ghost or armored statues\n" +
				"_-_ Rare errors when gateway traps teleported multiple things at once\n" +
				"_-_ Various rare errors when multiple inputs were given in the same frame\n" +
				"_-_ Fog of War errors in Tengu's arena\n" +
				"_-_ Rare errors with sheep spawning items and traps"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes") + " 3",
				"Fixed:\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor textual and visual errors\n" +
				"_-_ Gateway traps rarely teleporting immovable characters\n" +
				"_-_ Monks never losing focus if attacked out of hero vision range\n" +
				"_-_ Wild magic continuing to activate if the hero dies during it\n" +
				"_-_ Specific cases where guidebook windows could be stacked\n" +
				"_-_ Remove curse stating nothing was cleansed when it removed the degrade debuff"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.EXOTIC_AMBER), "Exotic Buffs",
				"Some exotic potions and scrolls have received more minor buffs, and not total redesigns:\n\n" +
				"_- Potions of Storm Clouds, Shrouding Fog, and Corrosion_ initial gas AOE up to 3x3 from 1x1\n" +
				"_- Potion of Shrouding Fog_ now only blocks enemy vision\n" +
				"_- Potion of Corrosion_ starting damage increased by 1\n" +
				"_- Potion of Magical Sight_ vision range up to 12 from 8\n" +
				"_- Potion of Cleansing_ now applies debuff immunity for 5 turns\n\n" +
				"_- Scroll of Foresight_ now increases detection range to 8 (from 2), but lasts 250 turns (from 600)\n" +
				"_- Scroll of Prismatic Image_ hp +2 and damage +20%"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_TOOLKIT), "Artifact Buffs",
				"The _Alchemist's Toolkit_ has received some minor changes to go along with the energy system adjustments:\n" +
				"_-_ Toolkit indirectly buffed by energy now being more valuable\n" +
				"_-_ Energy required to level up toolkit halved, kit can now be levelled anywhere\n" +
				"_-_ Toolkit warmup is now based on time, and gets faster as it levels up\n" +
				"_-_ Toolkit can now be used when enemies are near\n\n" +
				"The _Horn of Plenty_ is getting a change to increase its flexibility, and to make it better synergize with food eating talents:\n" +
				"_-_ The horn now has a 'snack' option that always consumes 1 charge\n" +
				"_-_ To counterbalance this, the total number of charges and charge speed have been halved, but each charge is worth twice as much as before.\n\n" +
				"I'm giving a mild buff to the _Dried Rose_ to fix an odd inconsistency where it was better to kill the ghost off than let them heal:\n" +
				"_-_ Ghost HP regen doubled, to match the roses recharge speed (500 turns to full HP)"));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.WARRIOR, 0, 90, 12, 15), HeroSubClass.BERSERKER.title(),
				"The berserker is getting a small QOL buff to make it easier to hold onto rage in combat:\n\n" +
				"_-_ Rage now starts expiring after not taking damage for 2 turns, instead of immediately."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null), "Talent and Ability Buffs",
				"Talent and ability balance is becoming more stable now, but I've still got a few buffs to hand out, some are quite significant:\n\n" +
				"_- Wild Magic_ Charge cost reduced to 25, from 35.\n" +
				"_- Spirit Hawk_ Duration up to 100 turns, from 60.\n\n" +
				"_- Empowering Scrolls_ now lasts for 2 wand zaps, up from 1.\n" +
				"_- Light Cloak_ now grants 16.6% charge speed per rank, up from 13.3%\n" +
				"_- Shrug it Off_ now caps damage taken at 20% at +4, up from 25%."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new MagesStaff(),
				"The reduction to the Mage's starting melee damage in v1.0.0 had a good effect on his early game winrate, but it's still notably higher than other heroes. So, I'm nudging his early melee power down one more time:\n\n" +
				"_- Mage's Staff_ base damage reduced to 1-6 from 1-7."));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.ROGUE, 0, 90, 12, 15), HeroSubClass.ASSASSIN.title(),
				"The Assassin is doing very well right now, especially after the power boost he can receive from smoke bomb or death mark. I'm scaling back his core power a little to try and reign him in a bit:\n\n" +
				"_-_ Preparation bonus damage at power level 1/2/3/4 reduced to 10/20/35/50%, from 15/30/45/60%"));

		changes.addButton( new ChangeButton(new TalentIcon(Talent.DOUBLE_JUMP.icon()), Talent.DOUBLE_JUMP.title(),
				"Just one talent/ability nerf this time! I'm scaling double jump back a bit to put it more in line with the other heroic leap talents:\n\n" +
				"_-_ Charge cost reduction now caps at 50%, down from 60%\n" +
				"_-_ The warrior must now jump again within 3 turns, down from 5\n\n" +
				"I'll likely making more balance tweaks (including nerfs) to abilities and talents in the future, but at the moment double jump is the only major standout."));

	}

	public static void add_v1_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v1.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 17th, 2021\n" +
				"_-_ 71 days after Shattered v0.9.3\n" +
				"_-_ 316 days after Shattered v0.9.0\n" +
				"_-_ A bit more than 7 years after v0.1.0!\n" +
				"\n" +
				"_That's right, we've hit v1.0.0!_ This update was previously called v0.9.4 while in beta.\n\n" +
				"Shattered will also now use the _major.minor.patch_ version naming scheme moving forward. So, the next patch will be v1.0.1, and the next update will be v1.1.0. _This change does not affect my plans for future updates!_\n\n" +
				"Expect more dev commentary here in the future."));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY_PORT), "iOS Release!",
				"_Shattered Pixel Dungeon is now available on the iOS App Store!_\n\n" +
				"After years of requests, Shattered is finally available on Apple devices! The iOS version of the game will release in lockstep with the Android version moving forward, with some small variance due to different update approval processes.\n\n" +
				"Note that the iOS version costs $5, but comes with some supporter features built-in. I have no plans to make any changes to the monetization of the Android version."));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "new music!",
				"_The game's music tracks has been remastered!_\n\n" +
				"The new music is composed by Kristjan Harristo, check the about scene for more details on them. Currently we have only replaced the existing tracks, but we are working on tracks for each of the dungeons regions as well!\n\n" +
				"The new in-game track in particular is also an experiment in variable music looping. The track has an intro and a main segment and can play the main segment once or twice before looping back to the intro. This makes the track notably less repetative, and we intend to use similar techniques in other tracks."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LIQUID_METAL), "new alchemy recipes!",
				"Two new alchemy recipes have been added! They're focused on helping you recycle thrown weapons and wands that you don't want to use.\n\n" +
				"_Liquid metal_ lets you sacrifice thrown weapons to repair other ones.\n\n" +
				"_Arcane resin_ lets you sacrifice a wand to upgrade other low level wands.\n\n" +
				"A new page has been added to the alchemy guide for these recipes, and it's now possible to find later guidebook pages in the prison."));

		changes.addButton(new ChangeButton(new Image(new Image(Assets.Environment.TERRAIN_FEATURES, 64, 64, 16, 16)), "new traps",
				"Two new traps have been added! They are both less common traps that have a higher potential to be helpful.\n\n" +
				"_Geyser traps_ convert surrounding terrain to water and throw back anything near them.\n\n" +
				"_Gateway traps_ are a special teleportation trap which never expire, and always teleport to the same location.\n\n" +
				"All teleportation traps now also affect characters and items next to them."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY), "new player experience improvements",
				"_The adventurer's guidebook is now the Tome of Dungeon Mastery!_\n\n" +
				"This is partly as a reference to the tome of mastery, which I removed in the previous update, and partly because the game's tutorial functionality has been improved.\n\n" +
				"Guidebook pages are now a bit shorter and more plentiful, and some of them are now given to the player right at the start of the game. These automatic pages are suggested to the player to read at crucial moments. This way the guidebook does a better job of highlighting info right when it's needed."));

		changes.addButton(new ChangeButton(new Image(new SpectralNecromancerSprite()), "spectral necromancers",
				"A new rare variant has been added for necromancers: _Spectral Necromancers!_\n\n" +
				"These necromancers don't care for skeletons, and prefer to summon a bunch of wraiths instead! Dealing with their horde might be tricky, but you'll be rewarded with a scroll of remove curse."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "new ankh mechanics",
				"_Regular Ankhs_ have been totally redesigned, and now give the player a chance to save all of their equipment! Be careful though, you'll have to fight your way back to your lost gear.\n\n" +
				"_Blessed Ankhs_ have received comparatively minor changes. In addition to the resurrection effect, these ankhs now also give the player 3 turns of invulnerability. This should help give players a moment to heal up after being revived."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_FEAR), "runestones",
				"All Scrolls now produce 2 runestones, instead of some scrolls producing 3. The stones that used to be given in higher quantities have received buffs in compensation:\n\n" +
				"_- Stone of Intuition_ can now be used a second time if the guess was correct.\n" +
				"_- Stone of Flock_ AOE up to 5x5 from 3x3, sheep duration increased slightly.\n" +
				"_- Stone of Deepened Sleep_ is now stone of deep sleep, instantly puts one enemy into magical sleep.\n" +
				"_- Stone of Clairvoyance_ AOE up to 20x20, from 12x12.\n" +
				"_- Stone of Aggression_ duration against enemies up 5, now works on bosses, and always forces attacking.\n" +
				"_- Stone of Affection_ is now stone of fear, it fears one target for 20 turns."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Various tech and stability improvements.\n" +
				"_-_ Increased the minimum supported Android version to 4.0, from 2.3.\n" +
				"_-_ Game versions that use github for update checking can now opt-in to beta updates within the game.\n\n" +

				"_-_ Item renaming functionality has been moved to within the item info window.\n" +
				"_-_ Various minor UI improvements to the intro, welcome and about scenes.\n" +
				"_-_ Adjusted settings windows, removed some unnecessary elements.\n" +
				"_-_ Added info buttons to the scroll of enchantment window\n"+
				"_-_ Armor with the warrior's seal on it now states max shielding.\n" +
				"_-_ Bonus strength is now shown separately from base strength.\n\n" +

				"_-_ Improved the exit visuals on floor 10.\n" +
				"_-_ Becoming magic immune now also cleanses existing magical buffs and debuffs.\n" +
				"_-_ Traps that spawn visible or that never deactivate can no longer appear in enclosed spaces"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor visual and text errors\n" +
				"_-_ damage warn triggering when hero gains HP from being hit\n" +
				"_-_ various rare bugs involving pitfall traps\n" +
				"_-_ disarming traps opening chests\n\n" +

				"_-_ various minor errors with electricity effects\n" +
				"_-_ soul mark not working properly on low HP enemies with shielding\n" +
				"_-_ various rare errors with shadows buff\n" +
				"_-_ errors with time freeze and inter-floor teleportation mechanics\n" +
				"_-_ rooted characters not being immune to knockback effects\n" +
				"_-_ time stasis sometimes not preventing harmful effects in its last turn.\n\n" +

				"_-_ wands losing max charge on save/load in rare cases\n" +
				"_-_ magical infusion clearing curses\n" +
				"_-_ dewdrops stacking on each other in rare cases\n" +
				"_-_ exploding skeletons not being blocked by transfusion shield in rare cases\n" +
				"_-_ rare incorrect interactions between swiftthistle and golden lotus\n" +
				"_-_ Rings not being renamable if they weren't IDed"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ statues not becoming aggressive when debuffed\n" +
				"_-_ swapping places with allies reducing momentum\n" +
				"_-_ DK minions dropping imp quest tokens\n" +
				"_-_ giant succubi teleporting into enclosed spaces\n" +
				"_-_ spectral blades being blocked by allies\n" +
				"_-_ Spirit Hawk and Shadow Clone being corruptible\n" +
				"_-_ Rogue's body replacement ally being vulnerable to various AI-related debuffs\n" +
				"_-_ some ranged enemies becoming frozen if they were attacked from out of their vision\n\n" +

				"_-_ gladiator combos dealing much more damage than intended in certain cases\n" +
				"_-_ magical charge and scroll empower interacting incorrectly\n" +
				"_-_ magical sight not working with farsight talent\n" +
				"_-_ perfect copy talent giving very slightly more HP than intended\n" +
				"_-_ wild magic using cursed wands as if they're normal"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null), "Armor Ability Buffs pt.1",
				"Based on balance data and feedback, I'm making a bunch of buffs and adjustments to armor abilities and their related talents!\n\n" +
				"_- Endure_ bonus damage conversion rate up to 1/3 from 1/4.\n\n" +
				"_- Striking Wave_ effectiveness increased by 20%.\n" +
				"_- Shock Force_ now actually adds 20% damage per level as stated. Previously it only added 15%.\n\n" +
				"_- Wild Magic_ now boosts wand levels, instead of overriding them.\n" +
				"_- Conserved Magic_ now has a chance to give each wand a 3rd shot.\n" +
				"_- Conserved Magic_ charge cost reduction down to 33/55/70/80% from 44/69/82/90%.\n\n" +
				"_- Elemental Blast_ base damage increased to 15-25 from 10-20.\n" +
				"_- Elemental Power_ now boosts power by 20% per level, up from 15%.\n\n" +
				"_- Remote Beacon_ range per level increased to 4, from 3."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null), "Armor Ability Buffs pt.2",
				"_- Shadow Clone_ now follows the hero at 2x speed.\n" +
				"_- Shadow Blade_ damage per level increased to 7.5% from 6.25%.\n" +
				"_- Cloned Armor_ armor per level increased to 15% from 12.5%.\n\n" +
				"_- Spirit Hawk_ evasion, accuracy, and duration increased by 20%.\n" +
				"_- Swift Spirit_ now gives 2/3/4/5 dodges, up from 1/2/3/4.\n" +
				"_- Go for the Eyes_ now gives 2/4/6/8 turns of blind, up from 2/3/4/5.\n\n" +
				"_- Spirit Blades_ effectiveness increased by 20%."));

		changes.addButton( new ChangeButton(new WoollyBomb(),
				"As stones of flock were buffed, I thought it was only fair to give woolly bombs some compensation buffs as well:\n\n" +
				"_-_ AOE size up to 9x9 from 5x5\n" +
				"_-_ Sheep duration up to 12-16 from 8-16"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new MagesStaff(),
				"The Mage continues to do too well in the early game since the talent changes in v0.9.1. Rather than weakening his talents and other magical abilities more, I've decided to make him more reliant on them instead by reducing his melee damage.\n\n" +
				"_- Mage's Staff_ base damage reduced to 1-7 from 1-8."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null), "Armor Ability Nerfs",
				"I focused mostly on buffs this update, but a few abilities and talents do need to be scaled back a little:\n\n" +
				"_- Double Jump_ charge cost reduction down to 20/36/50/60%, from 24/42/56/67%.\n\n" +
				"_- Telefrag_ self damage increased to a flat 5 per level.\n\n" +
				"_- Smoke Bomb_ max range reduced to 6 tiles from 8.\n" +
				"_- Body Replacement_ armor reduced to 1-3 per level, from 1-5.\n" +
				"_- Hasty Retreat_ turns of haste/invis reduced to 1/2/3/4 from 2/3/4/5\n" +
				"_- Shadow Step_ charge cost reduction down to 20/36/50/60%, from 24/42/56/67%.\n\n" +
				"_- Double Mark_ charge cost reduction down to 30/50/65/75%, from 33/55/70/80%.\n\n" +
				"_- 13th armor ability_ now only lasts for 6 turns, but also no longer prevents EXP or item drops. I'm trying to retain the ability's core theme while making it a bit less effective at totally removing enemies.\n" +
				"_- resistance talent_ damage reduction reduced to 10/19/27/35%, from 10/20/30/40%."));

	}

}
