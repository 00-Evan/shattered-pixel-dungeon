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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGuardSprite;
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

import java.util.ArrayList;

public class v2_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v2_5_Changes(changeInfos);
		add_v2_4_Changes(changeInfos);
		add_v2_3_Changes(changeInfos);
		add_v2_2_Changes(changeInfos);
		add_v2_1_Changes(changeInfos);
		add_v2_0_Changes(changeInfos);
	}

	public static void add_v2_5_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.5", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 11th, 2024\n" +
				"_-_ 119 days after v2.4.0\n" +
				"\n" +
				"Dev commentary will be added here in the future"));

		changes.addButton( new ChangeButton(Icons.JOURNAL.get(), "Journal Overhaul!",
				"_The game's Journal interface has been completely overhauled!_\n" +
				"\n" +
				"The Notes section has an entirely new grid-based UI with new icons, and _support for custom notes!_ Custom notes feature user-enterable text and can be tied to a floor, specific item, item type, or just be plain text.\n" +
				"\n" +
				"The Catalogs section has also been moved to a grid UI, and has been massively expanded with _almost 500 entries!_ The catalogs now contain almost every item in the game, and a bestiary featuring almost every character, plant, and trap! There are also _9 new badges_ which can be unlocked by filling the catalogs out!\n" +
				"\n" +
				"You can also now _view badges directly from the journal_, both the ones for your current run and overall badges.\n" +
				"\n" +
				"The guidebook tabs are unchanged, and the lore tab has been merged into the catalogs."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SALT_CUBE), "New Trinkets!",
				"_Four new trinkets have been added!_ Bringing the total to 15 trinkets.\n" +
				"\n" +
				"The _Salt Cube_ extends the duration that food keeps you full, but also reduces HP regeneration.\n" +
				"\n" +
				"The _Vial of Blood_ increases the healing granted by major healing sources, but also slows that healing down.\n" +
				"\n" +
				"The _Shard of Oblivion_ increases the amount of loot you'll find from enemies when you are wearing unidentified equipment.\n" +
				"\n" +
				"The _Chaotic Censer_ randomly spreads gasses nearby, that are harmful to you and to enemies.\n" +
				"\n" +
				"There is also now a fourth 'random' option when selecting trinkets, if you don't want any of the first three."));

		changes.addButton( new ChangeButton(Icons.DISPLAY_LAND.get(), "Region Splash Arts!",
				"_New splash arts have been added to the game's loading screens!_\n" +
				"\n" +
				"There's one for each of the five regions, once again made by Aleksandar Komitov!\n" +
				"\n" +
				"Loading times have not been increased, but the first loading screen of each region now pauses to display that region's story text. This gives an opportunity to appreciate the art without making players wait longer on every loading screen. The game will even start panning the loading screen art during this pause for mobile portrait users!"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_TIWAZ), "Interface Improvements",
				"In addition to the journal overhaul, there are several smaller UI changes and additions in this update too:\n" +
				"\n" +
				"_Scrolls of Upgrade_ now have a preview window that appears when they are used, that summarizes how the stats of an item will change when upgraded.\n" +
				"\n" +
				"The main menu _Badges Screen_ has been replaced with a new journal screen, containing the catalogs and guidebooks as well as unlocked badges.\n" +
				"\n" +
				"The _Alchemy Screen_ has been adjusted, primarily for desktop users. The guidebook button is now more visible, and shows the guidebook to the side when used on desktop.\n" +
				"\n" +
				"The game now displays a little congratulations message after your first win, and clarity on what winning the game unlocks has been improved."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE), "Cursed Wand Expansion",
				"Cursed wand effects have been massively expanded! There are now 16 new effects bringing the total number of effects up to 32. These include simple common effects like shooting bubbles and new super rare effects like a giant supernova explosion!\n" +
				"\n" +
				"A few existing cursed effects have also been changed:\n" +
				"_-_ Reduced healing from the uncommon health transfer effect by 50%\n" +
				"_-_ The uncommon 'shock and recharge' effect is now a lightning bolt, does more damage to a target and grants less wand recharge\n" +
				"_-_ The rare inter-floor teleport effect now caps at sending the player back 10 floors"));

		changes.addButton( new ChangeButton(Icons.BACKPACK_LRG.get(), "Inventory Management Improvements",
				"I've made various smaller changes to make inventory management a bit easier:\n" +
				"\n" +
				"_-_ The potion bandolier can now store the waterskin, and the scroll holder can now store arcane styli\n" +
				"_-_ Converting a potion or scroll into alchemical energy now also identifies it\n" +
				"_-_ Reduced the chance for multiple equipment drops from slimes, skeletons, guards, DM-200s, and golems\n" +
				"_-_ Increased the base drop rate of equipment from DM-200s and golems to compensate\n" +
				"_-_ Stones of Intuition now always get 2 uses each, even if the first guess is incorrect\n" +
				"_-_ Dark dungeon levels no longer contain a torch, but also reduce vision by 3/8, down from 4/8"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB), "Bombs and Identification",
				"I've fixed a long-standing exploit where players could use explosions to determine if equipment was upgraded. Explosions will now avoid destroying any equipment, previously they would destroy un-upgraded equipment only.\n" +
				"\n" +
				"As compensation to this change, I'm changing up a runestone, and buffing bombs and explosion effects:\n" +
				"\n" +
				"Firstly _Stones of Detect Magic_ are a new item that replace stones of disarming. Each stone can be used on a piece of equipment to identify whether it is cursed, and whether it is enchanted/upgraded. This should give a few more options when it comes to IDing equipment.",

				"Secondly, bombs and explosion effects are buffed across the board:\n" +
				"_-_ Explosion max damage +50% (this increases average damage by 33%)\n" +
				"_-_ Bombs no longer bounce when thrown directly at characters\n" +
				"_-_ Damage falloff removed from ALL explosion and bomb types\n" +
				"_-_ Alchemy bombs now all explode in a minimum AOE of 5x5 (up from 3x3)\n" +
				"_- Flashbangs_ redesigned, they are now smoke bombs, and spread shrouding fog in a 5x5 AOE in addition to dealing damage.\n" +
				"_- Shocks Bombs_ redesigned, they are now flashbangs, and deal +25% electricity damage and stun for 10 turns within their 5x5 AOE.\n" +
				"_- Regrowth Bomb_ AOE size up to 7x7 from 5x5, now always generate at least 3 plants, up from 2."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ Enemies are now less willing to follow the hero's movement path when approaching from a distance\n" +
				"_-_ Improved the quality of loot from tormented spirits\n" +
				"_-_ Traps which choose targets now have a max range of 8 tiles (or 6 on dark floors)\n" +
				"_-_ Burn and Ooze now end the moment water is entered, but still always do at least 1 turn of damage\n" +
				"_-_ Improved visibility of the ambitious imp\n" +
				"_-_ Trinkets can now be energized to get 5 energy back\n" +
				"_-_ Rings transmuted from artifacts can now be +1 or +2 if the artifact was +5 or +10\n" +
				"_-_ Vampiric no longer triggers on NPCs or allies\n" +
				"_-_ Blooming now produces furrowed grass if regen effects are disabled during boss fights",

				"_Characters:_\n" +
				"_-_ Tengu no longer avoids ground-based effects as if he were flying\n" +
				"_-_ Flying characters now visually fall into pits when they die\n" +
				"_-_ Flying characters now only wake sleeping enemies they are next to\n" +
				"_-_ Wraiths spawned by spectral necromancers are now more powerful\n" +
				"_-_ Shadow clone now inherits silent steps from the Rogue\n" +
				"_-_ DM-300 no longer spews gas at inorganic allies\n" +
				"_-_ DM-300 can no longer use an ability immediately after exiting supercharge state\n" +
				"_-_ Necromancers now interrupt the hero if they start summoning within vision\n" +
				"\n" +
				"_Effects:_\n" +
				"_-_ Drowsy and Sniper's Mark now state remaining duration in their descriptions\n" +
				"_-_ Wells of health and awareness descriptions are now more accurate\n" +
				"_-_ Thorns glyph no longer rebounds damage to allies",

				"_Items:_\n" +
				"_-_ Rapier's lunge ability no longer triggers weapon ability use effects when it is aimed at empty space\n" +
				"_-_ Warrior's broken seal now includes glyph info in its description\n" +
				"_-_ Added a cancel confirmation window to scrolls of enchantment\n" +
				"_-_ Armor now always takes 1 turn to equip, instead of 2 turns multiplied by movement speed\n" +
				"_-_ Minor visual changes to hourglass stasis effect\n" +
				"_-_ Styli can now be used on known uncursed armor\n" +
				"_-_ Potion of Storm Clouds no longer triggers traps when thrown",

				"_Misc:_\n" +
				"_-_ The game now remembers which monitor it was set to fullscreen on in multi-monitor setups\n" +
				"_-_ The journal window and hero info window can now be closed via key binding\n" +
				"_-_ Single wraiths can now spawn adjacent to their spawning cell if it is blocked\n" +
				"_-_ Added a little game log text when shopkeepers flee\n" +
				"_-_ Reduced the maximum size of some trap-filled rooms\n" +
				"_-_ Toxic gas rooms can no longer contain extra traps\n" +
				"_-_ Updated internal code libraries\n" +
				"_-_ Made slight tweaks to tutorial functionality\n" +
				"_-_ Added a new buff icon for wand-based buffs\n" +
				"_-_ ShatteredPD is now categorized as a game on Android devices\n" +
				"_-_ Updated translations and translator credits\n" +
				"_-_ Added dev commentary for v1.4.0"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Hall of Heroes pages 5-10 not syncing via Google Play Games on Google Play version\n" +
				"_-_ Incorrect behaviour when game is force-closed from trinket selection window\n" +
				"_-_ Scroll of remove curse not being consumed when freeing tormented spirits\n" +
				"_-_ Sleeping enemies not being alerted by allies in many cases\n" +
				"_-_ Shopkeepers often failing to clear gasses from their shops\n" +
				"_-_ Ripper Demons refusing to leap onto enemies above chasms\n" +
				"_-_ Cases where a locked laboratory room could contain the solution potion to a hazard room that contains its key",

				"_Characters:_\n" +
				"_-_ Final boss not immediately advancing to its final phase in some cases\n" +
				"_-_ Sad Ghost rarely spawning inside of walls\n" +
				"_-_ Ally position swapping working when allies are paralyzed\n" +
				"_-_ DM-300's exposed wire shielding mechanic and the vertigo debuff interacting incorrectly\n" +
				"_-_ Enemies not properly prioritizing targets based on distance\n" +
				"_-_ Very specific cases where the hero would refuse to walk onto visible traps\n" +
				"_-_ Rare cases where the rot heart could be boxed in and not spawn gas\n" +
				"_-_ Statue enemies reverting to being passive when ascending\n" +
				"_-_ Wards not being targetable if they were in solid terrain\n" +
				"_-_ Necromancers not being able to summon their minions in unlocked doors\n" +
				"_-_ Cases where the gnoll geomancer could be awoken via debuff\n" +
				"_-_ Spawned mimics (e.g. via cursed wand) not scaling with ascension challenge",

				"_Effects:_\n" +
				"_-_ Warrior being able to gain extra upgrades on his armor via hero armor transfer effect\n" +
				"_-_ Earthroot and living rock armor not applying to bone explosion damage\n" +
				"_-_ Precise Assault not working with unarmed melee attacks\n" +
				"_-_ Deferred damage sometimes being delayed on save/load\n" +
				"_-_ Various situational errors when enemies are transmogrified over chasms\n" +
				"_-_ Challenge Arena effect briefly persisting between floors\n" +
				"_-_ Magically slept enemies waking up when terror expires on them\n" +
				"_-_ Corpse Dust sometimes spawning many wraiths in quick succession after spawning none\n" +
				"_-_ Assassinate/execute effects not working on enraged brutes\n" +
				"_-_ Terror not affecting brutes in specific cases\n" +
				"_-_ Debuffs disappearing from DM-300's pylons on save/load\n" +
				"_-_ Duelist's Spike ability incorrectly triggering on-kill effects in specific circumstances",
				
				"_Items:_\n" +
				"_-_ Trinkets becoming unidentified when transmuted\n" +
				"_-_ Wondrous resin applying its effect to chaos elementals\n" +
				"_-_ Exotic crystals and Parchment Scrap trinkets affecting levelgen in some cases\n" +
				"_-_ Auto-aim not working correctly with cursed wands\n" +
				"_-_ Aqua brew always knocking hero up and left when thrown on self\n" +
				"_-_ Bomb fuses not being visually cleared when boss levels are reset by unblessed ankhs\n" +
				"_-_ Helpful darts dealing damage to allies in rare cases\n" +
				"_-_ Specific cases where beacon of returning could place the hero inside of closed dooors\n" +
				"_-_ Tipped dart cleaning window showing 'clean all' and 'clean one' even with just 1 dart\n" +
				"_-_ Specific cases where one scroll of transmutation couldn't be used on another\n" +
				"_-_ Death via a reclaimed trap not counting as dying to your own magic item",

				"_Misc:_\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor visual and textual errors\n" +
				"_-_ Rare cases of levelgen hanging\n" +
				"_-_ Game log spam during the tutorial in specific cases\n" +
				"_-_ Surface victory badges not being added to the rankings screen\n" +
				"_-_ Very rare cases of players getting outside of boss arenas\n" +
				"_-_ Direction compass not pointing to exit after defeating Tengu\n" +
				"_-_ Split alchemy guide not updating with inventory state in various cases\n" +
				"_-_ Settings defaulting to audio tab and not languages tab when user is using a language with an incomplete translation\n" +
				"_-_ Magic well note entries not being cleared if there are two wells in a level"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CLOVER), "Trinket Buffs",
				"I'm handing out buffs to several trinkets that are underperforming:\n" +
				"\n" +
				"_- 13 Leaf Clover_ upgrade cost up, but it now only affect the hero's damage rolls. It no longer applies to armor or enemy attacks. This should preserve the intended chaos of the effect without making enemies way more dangerous.\n" +
				"_- Dimensional Sundial_ upgrade cost up, but it now slightly reduces enemy spawn rates during daytime. 'nighttime' has also been adjusted to be 8pm-8am, from 9pm-7am.\n" +
				"_- Wondrous Resin_'s bonus cursed wand effects are now always neutral or positive.\n" +
				"_- Mimic Tooth_ now boosts loot from all mimics."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 1), "Duelist Buffs",
				"The Duelist is doing much better balance-wise since v2.4.0, but there are various specific abilities that are getting boosts this update:\n" +
				"\n" +
				"Weapon Abilities:\n" +
				"_- Lunge, Cleave, Spike, Retribution, and Brawler's Stance_ abilities all now deal more bonus damage\n" +
				"_- Crossbow's Charge Shot_ ability can now also cause a melee attack to knock back, or an untipped dart attack to deal bonus damage. Tipped dart benefits unchanged.\n" +
				"\n" +
				"Talents:\n" +
				"_- Liquid Evasion_ talent evasion at +1 reduced to 3x from 4x, but the talent now also grants bonus accuracy on the next attack. \n" +
				"_- Lethal Haste_ talent now grants instant movement, instead of haste.\n" +
				"_- Swift Equip_ talent's second use at +2 no longer has a 5 turn timer.\n" +
				"_- Deadly Followup_ talent damage per level up to +10% from +8%.\n" +
				"_- Precise Assault_ accuracy bonus increased to 2x/5x/inf., up from 2x/4x/8x.\n" +
				"_- Expose Weakness_ talent now applies weakness as well as vulnerable."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 4), "Monk Buffs",
				"v2.4.0's Duelist buffs were mainly focused on weapon abilities, which mainly benefit the Champion, so it makes sense that the Monk has fallen a little behind. I'm giving out several buffs and mechanics changes to boost the Monk back up, which should improve every ability she has, except meditate.\n" +
				"\n" +
				"_-_ Removed ability cooldowns entirely, except for flurry which has a 1 turn cooldown\n" +
				"_- Flurry_ damage increased by 50%\n" +
				"_- Focus_ now works on magic attack and has infinite duration\n" +
				"_- Dash_ range increased by 33%\n" +
				"_- Dragon Kick_ damage doubled\n" +
				"\n" +
				"_- Unencumbered Spirit_ energy gain changed to 50%/75%/100% from 40%/80%/120%\n" +
				"_- Combined Energy_ is now more permissive and reduces charge use by 1, instead of 50%"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.UNSTABLE_SPELL), "Other Buffs",
				"_- Wand Preservation_ talent now always succeeds, no longer grants arcane resin on failure, and is limited to 1 use at +1, and 1 use per level at +2. \n" +
				"_- Rogue's foresight_ talent trigger chance increased to 75% at +1 and 100% at +2, up from 60% and 90%.\n" +
				"_- Hold Fast_ talent now grants 1-2 armor per level, up from 0-2.\n" +
				"\n" +
				"_- Ring of Energy_ charge rate boost up to 17.5% per level, from 15%.\n" +
				"_- Unstable Brew_ now guarantees an appropriate potion effect when thrown or drank, instead of making it more likely.\n" +
				"_- Unstable Spell_ now guarantees an appropriate scroll effect based on nearby enemies, instead of making it more likely.\n"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MOSSY_CLUMP), "Mossy Clump & Grassy Floors",
				"The Mossy Clump is still far and away the strongest trinket, despite several nerfs since adding it. At this point it's clear the value of that trinket is more reflective of how powerful grassy floors are, rather than the trinket itself.\n" +
				"\n" +
				"So, I'm nerfing the drop-rate of dew from grassy floors by 50%, meaning that they will generate 2x as much dew as a regular floor on average, down from 4x. Seed drops from these floor are unaffected.\n" +
				"\n" +
				"This reduces the bonus dew granted by the mossy clump by 67%, which should hopefully bring it more in-line with other trinket."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_AGGRESSION), "Stone of Aggression",
				"_Stones of Aggression_ were meant to have some use during boss fights, but currently they're too strong in that case, especially with many challenges enabled:\n" +
				"\n" +
				"_-_ Bosses now take 1/2 damage from their minions when affected by aggression, the final boss specifically takes 1/4 damage.\n" +
				"_-_ Aggression duration vs. regular enemies up to 20 turns, from 5."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WHIP), "Other Item Nerfs",
				"_- Whip's Lash Ability_ bonus damage reduced to 0%, from +20%. Ability still guarantees a hit on every target.\n" +
				"\n" +
				"_- Ring of Haste_ bonus speed per level reduced to 17.5% from 20%.\n" +
				"\n" +
				"_Elixir of Featherfall_ adjusted:\n" +
				"_-_ Output quantity reduced to 1 from 2\n" +
				"_-_ Recipe energy cost reduced to 10 from 16\n" +
				"_-_ Falling into a chasm now reduces effect duration by 10 turns instead of ending it. Total duration unchanged at 50 turns."));

	}

	public static void add_v2_4_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.4", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 15th, 2024\n" +
				"_-_ 118 days after Shattered v2.3.0\n\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RAT_SKULL), "Trinkets!",
				"_A new category of item has been added: Trinkets!_\n" +
				"\n" +
				"Trinkets are a new item type produced and upgraded via alchemical energy, and are more about tweaking gameplay variables than giving direct power or utility.\n" +
				"\n" +
				"Look out for a trinket catalyst in the early stages of the game, which you can use at the first alchemy pot to produce one of three trinket options. There are _11 trinkets in total_."));

		changes.addButton( new ChangeButton(Icons.get(Icons.STAIRS), "New Rooms",
				"_New standard rooms have been added to the various regions of the game!_\n" +
				"\n" +
				"This includes one totally new room per region, and two new variants of entrance/exit rooms per region. Rooms that are mostly empty rectangles are now much less common, and there's a bunch of variety for which rooms can have entrance/exit stairs in them.\n" +
				"\n" +
				"Entrance rooms are now also capable of merging with other rooms in a dungeon floor, but there are some guarantees so that enemies won't ever be near you right after descending to a new floor."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PUMPKIN_PIE), "More Holiday Items",
				"_I've added more holiday items for the rest of 2024!_\n" +
				"\n" +
				"Expect to see some new temporary holiday overrides for cornish pasties for:\n" +
				"_-_ Pride in late June\n" +
				"_-_ Shattered's Birthday in early August\n" +
				"_-_ Pixel Dungeon's Birthday in early December\n" +
				"_-_ New Years in late December and early January\n" +
				"\n" +
				"This is in addition to the usual pumpkin pie around Halloween and candy cane around the Winter Holidays.\n" +
				"\n" +
				"...I may have also added a tiny little surprise for Rat King's birthday, but that won't appear until 2025."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "Duelist Weapon Abilities",
				"I'm making some overarching changes to the Duelist's weapon abilities, to make them stronger and feel more impactful:\n" +
				"_-_ Weapon ability charge speed down by 33%\n" +
				"_-_ Weapon ability charge cap reduced to 2-8, from 3-10\n" +
				"_-_ Weapon abilities buffed across the board\n" +
				"_-_ Some talents adjusted to account for weapon charges being less frequent\n" +
				"_-_ Weapon abilities now directly state their damage ranges\n" +
				"Check the buffs and talent changes section for specific weapon ability and talent changes.\n" +
				"\n" +
				"There are also some changes to the Champion subclass to go along with this:\n" +
				"_-_ Champion's two weapons now share a charge count, but Champion gets boosted max charges and charge speed.\n" +
				"_-_ Champion's secondary charge talent has been replaced with a new talent that encourages varied ability use."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ENERGY), "Alchemy Changes",
				"I've made a bunch of changes to the alchemy system to streamline things and complement the addition of trinkets:\n" +
				"\n" +
				"_-_ Catalysts have been removed entirely, recipes that required one now simply cost 8 or 9 more energy.\n" +
				"_-_ A new unstable brew and spell have been added, which give random potion/scroll effects.\n" +
				"_-_ Aqua Blast and Featherfall are now a brew and elixir, otherwise unchanged.\n" +
				"_-_ High value potions/scrolls now grant a little more energy if they are energized.\n" +
				"_-_ Alchemy pots now always spawn on the 3rd or 4th floor in each region.\n" +
				"_-_ Various UI improvements have been made to the alchemy screen.\n" +
				"\n" +
				"Various specific alchemy items have also received changes to their energy cost, output quantity, or mechanics. Check the buffs and nerfs section for more details on those."));

		changes.addButton( new ChangeButton(Icons.get(Icons.TALENT), "Talent Changes",
				"Duelist talents have been changed to account for adjustments to weapon charge mechanics:\n" +
				"_- Aggressive Barrier_ shielding increased to 3/5 from 3, threshold changed to 50% from 40%/60%.\n" +
				"_- Focused Meal_ charge boost reduced to 0.67/1 from 1/1.5.\n" +
				"_- Weapon Recharging_ boost reduced to every 15/10 turns from every 10/6 turns.\n" +
				"_- Counter Ability_ now grants up to 1.5 charges, instead of reducing cost.\n" +
				"\n" +
				"I've made some changes meant to improve some of the least popular T1 talents:\n" +
				"_- Cached Rations_ now awards a smaller number of unique 'supply rations'. These rations grant a little healing and cloak charge.\n" +
				"_- Test Subject and Tested Hypothesis_ have been replaced with two new talents that grant small combat bonuses.\n" +
				"\n" +
				"Plus one change to an unnecessarily complex T1 talent:\n" +
				"_- Hearty Meal_ has been simplified, now just has one threshold at 30% HP"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ Potion and Scroll talents can now trigger from elixirs, brews, and spells\n" +
				"_-_ Ankh resurrection window now warns if two items aren't selected\n" +
				"_-_ Trying to attack an enemy that has charmed you now shows a warning\n" +
				"_-_ Enemies seen via mind vision can now be auto-targeted, but the game won't auto-aim as aggressively for performance reasons\n" +
				"\n" +
				"_Heroes:_\n" +
				"_-_ Gladiator and Monk now include brief ability descriptions in their subclass descriptions\n" +
				"_-_ Ability descriptions for Gladiator and Monk now change if their abilities are empowered",

				"_Items:_\n" +
				"_-_ Thrown weapons now state when they break in the game log\n" +
				"_-_ Tipped darts now last forever when reaching 100 uses, like other thrown weapons\n" +
				"_-_ Dried rose now includes the ghost's strength in its description\n" +
				"_-_ Plant effects (e.g. fadeleaf) now trigger before traps when time freeze ends\n" +
				"\n" +
				"_Misc:_\n" +
				"_-_ Updated various code dependencies\n" +
				"_-_ Slight optimizations to memory use\n" +
				"_-_ Improved the error message on Android when native code is missing\n" +
				"_-_ Removed the power saver setting on Android 4.4+ devices, if the user hadn't already enabled it.\n" +
				"(Power saver was always meant for very old Android devices. It gives no real benefit for more modern ones.)"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Ruins rooms in the last region using incorrect wall shapes since v2.2.0\n" +
				"_-_ Rare cases where some victory badges would not save if game was immediately closed\n" +
				"_-_ Rare cases where game actors could continue to process for a moment when hero falls into a chasm\n" +
				"_-_ Various cases where characters would not play death animations if they died while paralyzed\n" +
				"_-_ Dwarf King now only clears minions that are still allied with him when transitioning phases\n" +
				"\n" +
				"_Heroes:_\n" +
				"_-_ Duelist's swift equip not working during time freeze\n" +
				"_-_ Monk's flurry of blows not using projecting enchantment when empowered\n" +
				"_-_ Various battlemage on-hit effects not showing as magical damage\n" +
				"_-_ Empowered strike talent not working with blastwave\n" +
				"_-_ Challenge ability incorrectly working on neutral mobs\n" +
				"_-_ Seer shot talent not working in blacksmith quest area",

				"_Enemies:_\n" +
				"_-_ Newborn elemental boss rarely firing its attack through walls\n" +
				"_-_ Rare cases where the final boss could command minions to attack themselves\n" +
				"_-_ Gnoll geomancer and sappers potentially dropping boulders next to entrance\n" +
				"_-_ Gnoll sappers granting armor to corrupted gnoll guards\n" +
				"_-_ Necromancer skeletons not following necromancer's aggro in some cases\n" +
				"_-_ Spectral necromancers now only kill wraiths they are aligned with when they die\n" +
				"_-_ Teleportation effects not accounting for large characters in specific cases\n" +
				"_-_ DM-300 not becoming supercharged if exactly damaged to the supercharge threshold\n" +
				"_-_ Specific cases where damaging immune enemies would count towards regen time during boss fights",

				"_Items pt.1:_\n" +
				"_-_ Armband not working on hiding mimics\n" +
				"_-_ Chilling enchant reducing chill duration in rare cases\n" +
				"_-_ Rare cases where lucky enchant wouldn't trigger\n" +
				"_-_ Runestones affecting terrain when thrown at a character\n" +
				"_-_ Thrown weapons sticking to downed ghouls in some cases\n" +
				"_-_ Camouflage glyph not working if hero uses ethereal chains to move into grass\n" +
				"_-_ Sandals of nature incorrectly interacting with artifact charging\n" +
				"_-_ Various specific errors with artifact charge boosting\n" +
				"_-_ Cursed wands can no longer turn important NPCs into sheep\n" +
				"_-_ King's crown not preserving armor hardening",
				
				"_Items pt.2:_\n" +
				"_-_ Brimstone glyph not benefiting from glyph power boosts past +50%\n" +
				"_-_ Errors when leaving/entering blacksmith's area while a boomerang was circling back\n" +
				"_-_ Transfusion not benefiting from wand damage bonuses\n" +
				"_-_ Dwarf King's crown automatically IDing armor\n" +
				"_-_ Armband allowing more than one steal in specific cases\n" +
				"_-_ Swiftness glyph ignoring nearby enemies in specific cases\n" +
				"_-_ Swiftness glyph turning off near neutral characters\n" +
				"_-_ Stone of fear applying to hero and allies\n" +
				"_-_ Spike ability overriding elastic enchant in some cases\n" +
				"_-_ Geyser traps and aqua brew not extinguishing flames",

				"_Misc:_\n" +
				"_-_ Various minor visual and textual errors\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Specific cases where unbreakable traps could spawn in halls in the caves\n" +
				"_-_ Music not properly pausing in background on desktop in some cases\n" +
				"_-_ Various rare errors when game launches in fullscreen\n" +
				"_-_ 'taste vengeance' badge not being earnable in a run after unlocking it\n" +
				"_-_ Ascension hero speed debuff triggering at 7+ amulet curse stacks instead of 6+\n" +
				"_-_ View distance becoming 0 during final boss in specific cases"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RAPIER), "Weapon Ability Buffs",
				"_- Lunge_ damage up\n" +
				"_- Cleave_ damage up, is now instant if it kills, but no longer chains\n" +
				"_- Heavy Blow_ damage up, non-surprise penalty changed to no bonus damage\n" +
				"_- Sneak_ charge cost down to 1 from 2, invis duration now scales\n" +
				"_- Combo Strike_ damage up, combo duration now resets on hit (like gladiator)\n" +
				"_- Spike_ damage up\n" +
				"_- Defensive Stance_ charge cost down to 1 from 2, duration now scales\n" +
				"_- Harvest_ charge cost down to 1 from 2, bleed amount up, is now constant, and deals regular damage to bleed-immune foes\n" +
				"_- Sword Dance_ charge cost down to 1 from 2, duration now scales, ACC boost up to 50%\n" +
				"_- Block_ duration now scales, now keeps blocking until you attack\n" +
				"_- Lash_ now deals bonus damage and is guaranteed to hit all targets\n" +
				"_- Spin_ charge cost down to 1 from 2, damage up\n" +
				"_- Runic Strike_ enchant boost now scales\n" +
				"_- Charge Shot_ AOE up to 7x7 from 5x5, dart durability boost now scales\n" +
				"_- Retribution_ is now instant if it kills\n" +
				"_- Brawler's Stance_ now deals bonus damage, reduces recharge speed instead of costing charge per hit"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCHEMIZE), "Alchemy Buffs",
				"Firstly, many items have received simple energy cost reductions. Note that all energy costs are after accounting for catalyst removal\n" +
				"_- Caustic Brew_ cost down to 1 from 2\n" +
				"_- Blizzard Brew_ cost down to 8 from 11\n" +
				"_- Shocking Brew_ cost down to 10 from 14\n" +
				"_- Aqua Brew_ cost down to 8 from 11\n" +
				"_- Elixir of Dragon's Blood_ cost down to 10 from 14\n" +
				"_- Telekinetic Grab_ cost down to 10 from 11\n" +
				"_- Beacon of Returning_ cost down to 12 from 15\n" +
				"_- Magical Infusion_ cost down to 12 from 13\n" +
				"_- Recycle_ cost down to 12 from 17",

				"Some alchemy produce has also received bigger buffs:\n" +
				"_- Elixir of Toxic Essence_ cost down to 8 from 10, spreads gas quicker, and gives 5 turns of lingering gas immunity\n" +
				"_- Elixir of Icy Touch_ cost down to 6 from 14 and now applies 3 chill each hit, up from 2\n" +
				"_- Wild Energy_ now requires a regular scroll of recharging, instead of mystical energy\n" +
				"_- Reclaim Trap_ cost up to 8 from 6, but output quantity increased to 5 from 4\n" +
				"_- Alchemize_ has a new recipe, it is now much cheaper to make\n" +
				"_- Summon Elemental_ cost down to 10 from 15 and buffing the spell now persists after use"));

		changes.addButton( new ChangeButton(new Image(new GhostSprite()), "Ghost and Blacksmith Enchantments",
				"I've given a slight boost to the rewards of the Ghost quest and Blacksmith's smith reward, aimed at making enchantments/glyphs more likely. this should make these rewards a bit more interesting more often.\n" +
				"\n" +
				"_-_ Ghost reward enchant rate up to 20% from 10%\n" +
				"_-_ Blacksmith smith reward enchant rate up to 30% from 0%"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_MIGHT), "Alchemy Nerfs",
				"_- Elixir of Might_ cost up to 16 from 14 (after account for catalyst removal)\n" +
				"_- Phase Shift_ cost down to 10 from 13, but output quantity down to 6 from 8"));

	}

	public static void add_v2_3_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.3", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released January 18th, 2024\n" +
				"_-_ 92 days after Shattered v2.2.0\n\n" +
				"v2.3.0 was originally going to contain two new variants for the caves quest, but after a longer than expected dev cycle plus the holidays, I opted to release v2.3.0 with just one more variant and move on to other content for a while, instead of spending even more time on the caves quest.\n\n" +
				"More Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new Image(new GnollGuardSprite()), "Gnoll Caves Quest",
				"_A second variant has been added to the caves quest!_\n" +
				"\n" +
				"This variant features _gnolls and earth-moving magic._ Expect to spend a bit more time digging, as this environment features collapsed walls, boulders, and angry gnolls wielding spears and earth-moving devices. Their magic is quite chaotic, so be ready to dodge and possibly get the gnolls caught in their own attacks.\n" +
				"\n" +
				"The boss for this variant is the _gnoll geomancer,_ an experienced gnoll with access to lots of mobility and earth-moving power."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS), "New Remains Items",
				"_Heroes remains now contain a new unique item that varies based on the class of the hero that died._\n" +
				"\n" +
				"These items are single use consumables that provide a small benefit that's themed after the hero who died. There are also two new badges relating to these items.\n" +
				"\n" +
				"Remains now always contain a signature remains item in addition to an extra item from the previous run, if one was chosen. This replaces the previous behaviour, where remains would contain 50 or 10 gold if no eligible item could be chosen.\n" +
				"\n" +
				"Additionally, remains which contain a stackable item from the previous run now cap the quantity of that item at 3."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CANDY_CANE), "New Holiday Items",
				"Shattered has had little holiday food items that temporarily replace cornish pasties ever since 2016, but only for Halloween and the Winter Holidays. Throughout 2024 you'll see a bunch of new items for more holidays through the year, which all have different tiny bonus effects when eaten.\n" +
				"\n" +
				"For now I have implemented items for Lunar New Years and Easter, with more on the way in future updates.\n" +
				"\n" +
				"I've also shortened the duration for Halloween and the Winter Holidays after 2023, so as not to make the game overly festive in the later months of the year, and nerfed the healing effect on Halloween pumpkin pies."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), "Floating Text Icons",
				"The little bits of floating text that appear above characters to indicate damage, positive effects, and currency gain now have tiny icons to indicate the type!\n" +
				"\n" +
				"For damage, it is now shown whether the damage is physical, magical, or if it comes from a particular debuff or DOT effect. There are separate physical damage icons depending on whether armor reduces the incoming damage. Armor never reduces magical or DOT damage. Damage text is also now always red,  warnings and negative effects are always orange.\n" +
				"\n" +
				"For positive effects, icons are now shown for healing, shielding, and exp gain. Loads of healing or shielding effects which previously didn't show floating text now do as well."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_Highlights:_\n" +
				"_-_ Improved the sprites for Armored Brutes and DM-201s\n" +
				"_-_ The troll blacksmith no longer works on cursed items\n" +
				"_-_ Reduced the chance for sleeping enemies to clump together in caves quest levels\n" +
				"_-_ Random scroll and potion drops are now more consistent throughout a run\n" +
				"\n" +
				"_Enemies:_\n" +
				"_-_ DM-300's rockfall attack now uses positional danger indicators\n" +
				"_-_ Improved visual clarity of sparks in the DM-300 fight\n" +
				"_-_ Removed unnecessary game log entries when DM-300 uses abilities\n" +
				"_-_ Phantom piranhas now die on land if there is no water to teleport to",

				"_Items:_\n" +
				"_-_ Added a warning when trying to steal from shops with less than 100% success chance\n" +
				"_-_ Curse infusion now preserves an existing curse on items that don't have the curse infusion bonus yet\n" +
				"_-_ long pressing on the ghost equip window now shows the stats of equipped items\n" +
				"\n" +
				"_Allies:_\n" +
				"_-_ Ghosts and Rogue's shadow clone are now considered inorganic (immune to bleed, toxic, poison)\n" +
				"_-_ Corrupted allies no longer attack passive enemies\n" +
				"_-_ Spirit hawk now interrupts the hero when it expires",

				"_Misc:_\n" +
				"_-_ Added a bit of clarity text to some parts of the blacksmith quest/rewards\n" +
				"_-_ Surface scene now shows night later in the evening as well as after midnight\n" +
				"_-_ Did a consistency pass on heal over time effects interrupting the hero resting\n" +
				"_-_ Long-press to assign quickslot now works in the full UI inventory pane, just like the mobile inventory window\n" +
				"_-_ Added support for themed icons on Android 13+\n" +
				"_-_ Removed support for saves prior to v1.4.3\n" +
				"_-_ Added developer commentary for v1.2.0",

				"_v2.3.1_\n" +
				"I've updated to the latest version of Shattered's game library (libGDX), which has a few benefits:\n" +
				"_-_ Improved vibration on modern iOS devices\n" +
				"_-_ Improved changing audio device behavior\n" +
				"_-_ Misc. stability & compatibility improvements\n" +
				"\n" +
				"_-_ Magical fire is now cleared by frost next to it, in addition to on top of it\n" +
				"_-_ Tengu's fire wall attack now ignites items\n" +
				"_-_ Improved music transitions in main menu when game was just won\n" +
				"_-_ Added support for controller vibration\n" +
				"_-_ Added a vibration toggle in the settings\n" +
				"_-_ Updated translators and translator credits\n" +
				"_-_ Increased the minimum supported iOS version to 11, from 9\n" +
				"\n" +
				"_v2.3.2_\n" +
				"_-_ Slightly improved the layout of shop rooms when many items are present\n" +
				"_-_ Updated a link in the game's credits"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"_Highlights:_\n" +
				"_-_ Enemies continuing to fight each other after amok expires in many cases\n" +
				"_-_ Some inter-level teleportation effects working inside caves quest level\n" +
				"_-_ Transmutation being usable on the pickaxe during the caves quest\n" +
				"_-_ Unintended changes to reforge functionality when both items are the same level\n" +
				"_-_ Rounding errors causing tipped darts to last longer than intended in some cases\n" +
				"\n" +
				"_Quests:_\n" +
				"_-_ Crystal spire being considered a mini boss, not a full boss\n" +
				"_-_ Crystal spire attacks ignoring damage-resisting effects\n" +
				"_-_ Cases where remains would fail to appear in the new mining level\n" +
				"_-_ Blacksmith landmark entry not clearing when you have spent all favor\n" +
				"_-_ Rare cases where hero could appear to be on top of crystal spire\n" +
				"_-_ Corpse dust quest tracking all wraiths instead of just the ones it spawned\n" +
				"_-_ Some cases where new rot garden room could spawn much smaller than intended",

				"_Enemies:_\n" +
				"_-_ Final boss fight not properly interacting with the into darkness challenge\n" +
				"_-_ Monk ability use disqualifying for dwarf king's 'no weapons' badge when a weapon was equipped\n" +
				"_-_ Tengu behaving slightly incorrectly when taking massive damage\n" +
				"_-_ Mimics not dropping their loot if corrupted while hiding\n" +
				"_-_ Rare cases where DM-300 finale music would play before the fight\n" +
				"_-_ Rare errors in DM-201 target selection",

				"_Items:_\n" +
				"_-_ Rotberry seed being deleted in rare cases\n" +
				"_-_ Rare cases where the game would freeze after reviving via unblessed ankh\n" +
				"_-_ Some bombs and explosion-spawning effects incorrectly dealing magic damage\n" +
				"_-_ Foresight effects not triggering after level transition\n" +
				"_-_ Projecting missile weapons not working on enemies inside solid terrain\n" +
				"_-_ Cursed wand of warding having different targeting properties than other wands\n" +
				"_-_ Thrown potions not clearing fire/ooze if they shattered out of view\n" +
				"_-_ Retribution and psionic blast not applying to all visible characters in very rare cases\n" +
				"_-_ Degrade debuff not applying to thrown weapons\n" +
				"_-_ Cloak of shadows not losing charge if it is dispelled as it is activated\n" +
				"_-_ Items being assignable to non-visible quickslots in specific cases\n" +
				"_-_ Rare quickslot errors when bags which already contain items are collected",

				"_Hero & Allies:_\n" +
				"_-_ Thrown potions not triggering Liquid Agility talent\n" +
				"_-_ Sneak ability working while Duelist is rooted\n" +
				"_-_ Damage from Body Slam talent ignoring armor\n" +
				"_-_ Lunge ability incorrectly interacting with movespeed in some cases\n" +
				"_-_ Cases where prismatic images could keep appearing and then disappearing\n" +
				"_-_ Hero not being able to self-trample plants when standing on stairs\n" +
				"_-_ Berserker being able to rage without his seal equipped in some cases\n" +
				"_-_ Allies rarely spawning on hazards after ankh revive\n" +
				"_-_ Ally warp working on corrupted DM-201s\n" +
				"_-_ Duelist's lunge ability not correctly applying range boosts in rare cases\n" +
				"\n" +
				"_Misc.:_\n" +
				"_-_ Various rare crash and freeze bugs\n" +
				"_-_ Various minor visual and textual errors\n" +
				"_-_ Tutorial becoming stuck in rare cases\n" +
				"_-_ Beta updates setting not working as intended\n" +
				"_-_ Music fading not working in rare cases\n" +
				"_-_ Scrolling pane in journal window freezing in rare cases",

				"_v2.3.1:_\n" +
				"_-_ Game unintentionally spamming new Google Play players with Play Games login requests\n" +
				"_-_ Events which interrupt the hero not interrupting resting\n" +
				"_-_ Rare cases where hero could lose a turn when moving between depths\n" +
				"_-_ Transmutation removing items from quickslots in rare cases\n" +
				"_-_ Incorrect death messages when player is killed by wards\n" +
				"_-_ Amoked allies not being affected by aggression debuff\n" +
				"\n" +
				"_v2.3.2:_\n" +
				"_-_ Various minor textual errors\n" +
				"_-_ Very specific cases where levelgen could vary based on whether the player has a timekeeper's hourglass or not\n" +
				"_-_ Hero leap vfx not causing the camera to follow them\n" +
				"_-_ Cases where grass and embers wouldn't appear on top of specific ground visuals"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.TALENT.get(), "Hero Abilities",
				"No nerfs this time, and just a couple targeted buffs to hero abilities/talents.\n" +
				"\n" +
				"_- Rogue's Foresight_ talent trigger chance increased to 60% at +1 and 90% at +2, up from 50% at +1 and 75% at +2.\n" +
				"\n" +
				"_- Elemental Strike_ ability base range increased to 4 from 3."));

	}

	public static void add_v2_2_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 18th, 2023\n" +
				"_-_ 138 days after Shattered v2.1.0\n\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE), "New Blacksmith Quest!",
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

		changes.addButton( new ChangeButton(Icons.CHALLENGE_COLOR.get(), "Hostile Champions",
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
				"_-_ Crashes caused by text input windows for controller users",

				"_v2.2.1:_\n" +
				"_-_ System gestures in iOS sometimes registering as taps within the game\n" +
				"_-_ Endure ability not working properly when used twice quickly\n" +
				"_-_ Melee damage of Mage's Staff benefiting from talents that boost wand levels\n" +
				"_-_ Various blink effects allowing movement over magical fire\n" +
				"_-_ Some game actions being possible while meditating\n" +
				"_-_ Various minor visual and textual errors\n" +
				"\n" +
				"_v2.2.2 (iOS only):_\n" +
				"_-_ Gesture fix from v2.2.1 resulting in input delays\n" +
				"_-_ Rare crash errors caused by audio loading"));

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
				"Dev commentary will be added here in the next major update."));

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
				"v2.0.0 introduced Shattered Pixel Dungeon's first ever new hero: the Duelist!\n" +
				"\n" +
				"The Duelist was the first of two new heroes that were focused around active abilities, as opposed to focusing on equipment power (Warrior and Mage), or environment interaction (Rogue and Huntress). I see v2.0 as the beginning of another new era in Shattered's development, as after focusing on new platforms during most of v1.X, game updates instead started focusing on big content expansions.\n" +
				"\n" +
				"Unfortunately the Duelist ended up releasing quite weak. I overestimated how well an ability focused hero would perform versus the existing heroes. I erred much more on the side of making the next hero stronger, and so far that seems to be working out quite well."));

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
