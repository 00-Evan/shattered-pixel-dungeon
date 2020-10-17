/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_9_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_9_0_Changes(changeInfos);
	}

	public static void add_v0_9_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.9.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("v0.9.0b", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "Talent Balance Changes",
				"I'm making some numbers changes to improve some of the lowest winrate talents. Expect more thorough changes, and a new tier of talents, when I release v0.9.1!\n\n" +
				"_- Survivalist's Intuition_ ID speed boost at +1/+2 increased to 1.75x/2.5x, from 1.5x/2x\n" +
				"_- Scholar's Intuition_ ID speed boost at +1 increased to 3x from 2x\n\n" +
				"_- Energizing Meal_ recharging turns at +1/+2 increased to 5/8 from 4/6\n\n" +
				"_- Sucker Punch_ damage boost at +1/+2 increased to 1-2/2 from 1/1-2\n" +
				"_- Followup Strike_ damage boost +1/+2 increased to 2/3 from 1-2/2-3"));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "Hostile Champions Tweaks",
				"Based on feedback, I'm adjusting the hostile champions challenge to be a bit harsher overall, while also reduce the chance of some of the worst-case scenarios:\n" +
				"_-_ Champion spawn chance increased to 1/10 from 1/15\n" +
				"_-_ Champions per floor are now capped, to prevent unlucky cases with huge champion numbers. This cap gets higher as depth increases.\n" +
				"_-_ Growing champion base power reduced by 20%, power scaling reduced by 50%.\n" +
				"_-_ Champion enemies now include their champion buff description in their regular description.\n\n" +
				"I've also fixed two bugs that affected hostile champions specifically:\n" +
				"_-_ Fixed pathing issues with giant champions.\n" +
				"_-_ Fixed projecting elementals spamming their ranged attacks."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"I'm making some adjustments to sewers loot to give players a bit more control of what gear they get, and to reduce the chance of spawning high tier gear that the player may never get to use:\n" +
				"_-_ Chance for regular gear drops in the sewers to be T4/T5 reduced by 50%.\n" +
				"_-_ Players can now see what type of weapon/armor the sad ghost has before selecting it.\n\n" +
				"_-_ Statues are now killed if a disarming trap triggers under them.\n" +
				"_-_ Weak shields no longer override stronger ones.\n" +
				"_-_ Long pressing item slots in the alchemy screen now shows the item's description.\n\n" +
				"_-_ Updated translations and translator credits."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by v0.9.0):\n" +
				"_-_ \"+1 Talent Point\" appearing when no new points are given in some cases.\n\n" +
				"Fixed (existed prior to v0.9.0):\n" +
				"_-_ Tengu's abilities being reset by saving/loading\n" +
				"_-_ Various cases where game win badges would not appear\n" +
				"_-_ Force cubes trigger traps before being placed to the floor\n" +
				"_-_ Beacon of returning rarely teleporting the player into walls\n" +
				"_-_ Player being forced to swap equipped misc items when they shouldn't in some cases\n" +
				"_-_ Enemies rarely not appearing paralyzed when they are"));

		changes = new ChangeInfo("v0.9.0a", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Updated Translations"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by v0.9.0):\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Cases where warrior's shield would not decrease when it should\n" +
				"_-_ Incorrect interactions between scrolls and time freeze\n" +
				"_-_ Unlocked badges rarely showing as locked\n" +
				"_-_ Burning spider webs destroying some tile types that they shouldn't\n\n" +
				"Fixed (existed prior to v0.9.0):\n" +
				"_-_ Great crab sometimes dropping 3 meat instead of 2\n" +
				"_-_ Cleave being reset when a kill corrupts the enemy\n" +
				"_-_ Sleeping VFX persisting in cases where it shouldn't"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 5th, 2020\n" +
				"_-_ 61 days after Shattered v0.8.2\n" +
				"_-_ 173 days after Shattered v0.8.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "Hero Talents!",
				"_A new gameplay system has been added!_\n\n" +
				"As you play the game and level up, you now unlock points to spend on hero talents. These talents have a range of effects, from stat adjustments to triggered effects to full on abilities!\n\n" +
				"Talents are split into tiers, and to start only the first tier is available, which covers levels 1 to 6. More talents will be added soon!\n\n" +
				"Previously I would have spent many more months on v0.9.0 and released the entire system in one update, but I'm changing up my strategy and splitting these big updates into smaller parts."));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "Hostile Champions!",
				"A new challenge has been added: _Hostile Champions!_\n\n" +
				"Unlike most other challenges, this one focuses on amping up the difficulty of the dungeon, rather than removing tools from the player. Give it a try if you've unlocked challenges, and you might find some particularly nasty enemies in the dungeon!"));

		changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), "Badge Visuals",
				"The badges screen now shows which badges are locked, rather than just using a generic 'locked badge' visual.\n\n" +
				"Badges now have different border colors based on their difficulty (bronze, silver, gold, platinum, diamond), and are ordered based on these colors."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.WARRIOR, 0, 15, 12, 15), "Hero balance adjustments",
				"The _Warrior, Mage, and Huntress_ are getting some balance tweaks to offset their new talents:\n\n" +
				"The _Warrior and Mage_ no longer have innate on-eat effects, these are replaced by their food-based talents.\n\n" +
				"The _Warrior's_ base max shielding has been reduced from 2 to 1, to compensate for his shielding talent.\n\n" +
				"The _Mage_ no longer has an innate wand identification effect, this is replaced by his wand identification talent.\n\n" +
				"The _Huntress'_ studded gloves have had their base damage reduced to 1-5 from 1-6, to compensate for her damage-dealing talent."));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_OFF), "Existing Challenges",
				"Some existing challenges have been tweaked to reduce the number of items that they remove from the game:\n\n" +
				"_On Diet_ no longer restricts food, but instead causes all food to be 1/3 as effective at satiating hunger.\n\n" +
				"_Faith Is My Armor_ no longer restricts the hero to cloth armor, but instead heavily reduces the blocking power of all armor above cloth.\n\n" +
				"_Pharmacophobia_ no longer removes health potions, but instead makes them poisonous to the player."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Unidentified scrolls can now be used to make runestones! You won't know what stones you'll get until you brew the scroll, but the scroll will be retroactively identified.\n\n" +
				"_-_ Reduced hero unlock requirements.\n\n" +
				"_-_ Added HP numbers to the player's health bar.\n" +
				"_-_ Tweaked some interface visuals to be more rounded.\n\n" +
				"_-_ Spider webs are now flammable, and can be shot through by fireblast.\n" +
				"_-_ The reclaim trap spell can no longer be dropped when a trap is stored in it. This prevents an exploit.\n" +
				"_-_ Items gained from secret mazes are now known to be uncursed."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various visual errors\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various rare game freeze bugs\n" +
				"_-_ Back button closing the game in hero select\n" +
				"_-_ Issues with touch input on Android 11 when gestures are enabled\n" +
				"_-_ Crystal mimics escaping when they are still visible\n" +
				"_-_ Shadows buff being cancelled by enemies seen via mind vision\n" +
				"_-_ Aqua blast occasionally not stunning\n" +
				"_-_ Errors with turn spending when talisman is used\n" +
				"_-_ Newborn elemental not dropping its quest item for overlevelled heroes\n" +
				"_-_ Spinners shooting webs though walls\n" +
				"_-_ Elastic enchantment closing doors when used with spirit bow\n" +
				"_-_ Shopkeepers buying items worth 0 gold\n" +
				"_-_ Duplicate artifacts in rare cases\n" +
				"_-_ Custom names not applying to Mage's staff\n" +
				"_-_ Ring of might not reducing max HP when degraded\n" +
				"_-_ Rare bugs involving ripper demon leaping\n" +
				"_-_ Hero unable to cleanse fire with chill when immune to it, and vice-versa\n" +
				"_-_ DM-201's attacking while stunned"));

		changes.addButton(new ChangeButton(new WandOfFrost(),
				"This is actually a bugfix, but I'm listing it separately for clarity. In v0.8.2 the _wand of frost_ is listed as losing 6.67% damage per turn of chill, to a max of -50%. This is not what was implemented however, and the wand instead lost 3.33% per turn to a max of -30%. This has now been corrected and the wand performs as listed in the 0.8.2 changelog."));

	}
}
