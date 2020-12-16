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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_9_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_9_0_Changes(changeInfos);
	}

	public static void add_v0_9_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.9.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("v0.9.1a", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ The shadows buff now has a green icon, instead of the regular invisibility blue.\n\n" +
				"_-_ Slightly adjusted the Huntress' splash art to improve details on her face."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by v0.9.1):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Errors with autotargeting and the wand of disintegration\n" +
				"_-_ Fissure rooms in the caves rarely failing to make bridges\n" +
				"_-_ Enemies not changing targets when their current target cannot be reached\n\n" +
				"Fixed (existed prior to v0.9.1):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Incorrect badges rarely showing in rankings\n" +
				"_-_ Hourglass spawning sand when unidentified or cursed\n" +
				"_-_ Hero having a smaller tap region than other characters\n" +
				"_-_ Questgivers rarely not being added to the journal"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released December 11th, 2020\n" +
				"_-_ 68 days after Shattered v0.9.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "Tier Two Talents!",
				"_A second tier of talents has been added, including twenty new talents spanning levels 7-12!_\n\n" +
				"The second talent tier is similar to the first, but talent powers are much less focused on the early game. Expect effects that are useful all game long.\n\n" +
				"Look forward to tier 3 of the talent system coming in v0.9.2, which will span levels 13-20."));

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Levelgen Improvements!",
				"_The game's level generation system has received a number of improvements!:_\n\n" +
				"_-_ A new region specific room has been added to each dungeon region, 5 in total.\n" +
				"_-_ Three new level feelings have been added: large, secrets, and traps.\n" +
				"_-_ The level layout system now creates much more varied layouts based on more shapes.\n" +
				"_-_ Adjacent rooms can now be merged in many more cases.\n" +
				"_-_ Several existing room types have received small tweaks and improvements."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.HUNTRESS, 0, 15, 12, 15), "Huntress and Rogue Adjustments",
				"The _Huntress and Rogue_ have both received some adjustments to their innate powers:\n\n" +
				"_-_ Bonus thrown weapon durability is no longer an innate huntress power, it is now a talent.\n" +
				"_-_ Short-range mind vision is no longer an innate huntress power, it is now a talent.\n\n" +
				"_-_ The Rogue no longer causes the game to spawn slightly more secret rooms. Instead secret room generation has been slightly increased for all heroes, and the Rogue has talents that help him find these secrets."));

		changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "Tier One Talent Changes",
				"Several _tier one talents_ have been changed based on feedback and gameplay data:\n\n" +
				"_- Test Subject_ now triggers on identifying any item, but the healing it grants has been halved.\n\n" +
				"_- Energizing Meal_ is now a T2 talent. In T1 it has been replaced by _Empowering Meal_, which grants bonus damage on wand zaps.\n" +
				"_- Tested Hypothesis_ now triggers on identifying any item. It now also grants a small amount of recharging, instead of shielding.\n" +
				"_- Energizing Upgrade_ is now a T2 talent. In T1 it has been replaced by _Backup Barrier_, which gives a small shield when the Mage's staff runs out of charge.\n\n" +
				"_- Rationed Meal_ has been removed. It is replaced by _Cached Rations_, which allows the Rogue to find bonus food.\n" +
				"_- Mending Shadow_ has been reworked. It is now _Protective Shadows_, which grants the Rogue shielding.\n\n" +
				"_- Invigorating Meal_ is now a T2 talent. In T1 it has been replaced by _Nature's Bounty_, which allows the Huntress to find berries in grass."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Statue grid rooms now appear in the Dwarven City, rather than the Prison.\n" +
				"_-_ Pillar rooms now appear in the Prison, rather than the Dwarven City.\n\n" +
				"_-_ The Sad Ghost's reward UI has been improved.\n\n" +
				"_-_ Added a debuff indicating when enemies have thrown weapons attached to them.\n\n" +
				"_-_ Sniper's mark, charm, and terror all now cancel if their subject is dead.\n\n" +
				"_-_ Stones of intuition can now be used on rings.\n\n" +
				"_-_ Barriers now decay more slowly when under 20 shielding.\n\n" +
				"_-_ Elixir of honeyed healing now triggers on-eat talents and satiates 150 turns of hunger, up from 90.\n\n" +
				"_-_ The Mage's staff is no longer automatically set to max charges when imbuing a new wand."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various minor audiovisual errors\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Curse infusion not awarding item level badge\n" +
				"_-_ Various errors with DM-300\n" +
				"_-_ Various minor errors relating to time freeze\n" +
				"_-_ Dwarf King and some Yog fists rarely taking negative damage\n" +
				"_-_ Talisman gaining charge while cursed\n" +
				"_-_ Large characters entering tunnels when vertigoed\n" +
				"_-_ Assassin blink ignoring hero being rooted\n" +
				"_-_ Hero being able to be both well-fed and hungry\n" +
				"_-_ Rare AI issues when paths are blocked\n" +
				"_-_ Various rare bugs with corruption\n" +
				"_-_ Antimagic not applying to wards or magical sleep debuff\n" +
				"_-_ Artifacts rarely losing levels when transmuted\n" +
				"_-_ High grass appearing on top of plants in rare cases\n" +
				"_-_ Characters rarely appearing inside doors or the hero\n" +
				"_-_ Talent points being spendable when the hero is dead\n" +
				"_-_ Warlocks not having capped health potions drops"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.MAGE, 0, 90, 12, 15), HeroSubClass.BATTLEMAGE.title(),
				"I'm making a variety of adjustments to the _Battlemage_ to make him a more compelling choice vs. the Warlock, and to improve some wands that don't work as well for him as the Warlock:\n\n" +
				"_-_ Staff charge granted on-hit increased to 0.5 from 0.33\n\n" +
				"_- Staff of Magic Missile_ on-hit now gives all wands 0.5 charge, up from 0.33\n\n" +
				"_- Staff of Transfusion_ on-hit now triggers when enemy is charmed\n" +
				"_- Staff of Transfusion_ on-hit now grants a shield in addition to a free shot on allies\n\n" +
				"_- Staff of Frost_ on-hit now has a chance to trigger at lower amounts of chill, still guaranteed at 10+ turns.\n\n" +
				"_- Staff of Living Earth_ on-hit now grants 33% of damage as armor, up from 25%.\n\n" +
				"_- Staff of Regrowth_ on-hit now triggers if the hero or enemy are standing in grass.\n" +
				"_- Staff of Regrowth_ on-hit now grants herbal healing, instead of spawning grass."));

		changes.addButton(new ChangeButton(new WandOfTransfusion(),
				"The _Wand of Transfusion_ is currently in an odd place, where it is not very useful as a general wand, but is GREAT when heavily upgraded by the warlock. I'm making a few adjustments so that its power is less polarized, and to make it hopefully more useful when combined with allies:\n\n" +
				"_-_ Starting charges increased to 2, from 1\n" +
				"_-_ Shield per-hit adjusted to 5+lvl from 5+2*lvl\n" +
				"_-_ Self-damage reduced to 5% of max HP, from 10% max HP\n" +
				"_-_ Damage from allies no longer cancels charm effect"));

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_TOPAZ, null), Messages.get(RingOfEnergy.class, "name"),
				"The _Ring of Energy_ is doing better after being buffed to apply to artifacts, but there is still room to make its effect stronger without making it overpowered:\n\n" +
				"_-_ Bonus artifact charging increased from 10% to 15%"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( new Image(Assets.Interfaces.TALENT_ICONS, 0, 0, 16, 16), Talent.HEARTY_MEAL.title(),
				"_Hearty Meal_ is currently the strongest tier one talent in the game, so I'm deepening the missing health requirement slightly to make its power a bit harder to access:\n\n" +
				"_-_ Now grants 2/3 healing when hero is below 50% health, down from 3/5\n" +
				"_-_ The full 3/5 heal is still available if the hero is below 25% health"));

		changes.addButton( new ChangeButton( new Image(new SpinnerSprite()), Messages.get(Spinner.class, "name"),
				"I'm pulling down the damage of _Cave Spinners_ slightly to address player frustration:\n\n" +
				"_-_ Melee damage down to 10-20 from 10-25"));

		changes = new ChangeInfo("v0.9.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

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

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"I'm making some adjustments to sewers loot to give players a bit more control of what gear they get, and to reduce the chance of spawning high tier gear that the player may never get to use:\n" +
				"_-_ Chance for regular gear drops in the sewers to be T4/T5 reduced by 50%.\n" +
				"_-_ Players can now see what type of weapon/armor the sad ghost has before selecting it.\n\n" +
				"_-_ Statues are now killed if a disarming trap triggers under them.\n" +
				"_-_ Weak shields no longer override stronger ones.\n" +
				"_-_ Long pressing item slots in the alchemy screen now shows the item's description.\n\n" +
				"_-_ Updated translations and translator credits."));

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

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (existed prior to v0.9.0):\n" +
				"_-_ Tengu's abilities being reset by saving/loading\n" +
				"_-_ Various cases where game win badges would not appear\n" +
				"_-_ Force cubes trigger traps before being placed to the floor\n" +
				"_-_ Beacon of returning rarely teleporting the player into walls\n" +
				"_-_ Player being forced to swap equipped misc items when they shouldn't in some cases\n" +
				"_-_ Enemies rarely not appearing paralyzed when they are\n" +
				"_-_ Great crab sometimes dropping 3 meat instead of 2\n" +
				"_-_ Cleave being reset when a kill corrupts the enemy\n" +
				"_-_ Sleeping VFX persisting in cases where it shouldn't"));

		changes.addButton(new ChangeButton(new WandOfFrost(),
				"This is actually a bugfix, but I'm listing it separately for clarity. In v0.8.2 the _wand of frost_ is listed as losing 6.67% damage per turn of chill, to a max of -50%. This is not what was implemented however, and the wand instead lost 3.33% per turn to a max of -30%. This has now been corrected and the wand performs as listed in the 0.8.2 changelog."));

	}
}
