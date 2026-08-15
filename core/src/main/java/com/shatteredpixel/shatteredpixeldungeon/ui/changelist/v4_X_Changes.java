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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class v4_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){

		if (DeviceCompat.isiOS() && DeviceCompat.getPlatformVersion() <= 12) {
			//only iOS 12 for the moment
			add_Coming_Soon(changeInfos);
		}
		add_v4_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		/*changes.addButton( new ChangeButton( ChangeIcons.V074_SHPX, "Overview and ETA",
				"..."));*/

		if (DeviceCompat.isiOS() && DeviceCompat.getPlatformVersion() <= 12) {
			changes.addButton(new ChangeButton(Icons.WARNING.get(), "iOS 12 end of support",
					"Unfortunately, due to an impending requirement by Apple, v4.0.0 and any immediate patches will be the last versions of Shattered compatible with iOS 12.\n" +
					"\n" +
					"iOS 13 and 14 support will also have to be dropped around this time next year due to the same requirements."));
		}
	}

	public static void add_v4_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v4.0-BETA", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V074_SHPX, "Beta To-Do",
				"Greetings Beta Testers!\n" +
				"\n" +
				"v4.0 is releasing in a pretty much content-complete state, but there are still a few things to finish up before launch:\n" +
				"\n" +
				"**-** Some new visuals in the quest area are still a WIP, most notably the visuals for the boss fight.\n" +
				"**-** There are finished new visuals for the Newborn Elemental quest and both caves quests that I have yet to implement.\n" +
				"**-** I would like to make some tweaks to new visuals based on feedback, so please let me know what tou think! In particular I'm aware there's been a lot of feedback on scrolls and runestones.\n" +
				"**-** There will of course be bugs to fix and balance tweaks to make, especially with so much more content.\n" +
				"\n" +
				"I expect this beta to last 2-3 weeks."));

		changes.addButton( new ChangeButton( ChangeIcons.V33_IMP, "New City Quest",
				"**The Ambitious Imp's quest has been totally redone, into easily the largest quest yet!**\n" +
				"\n" +
				"The Imp now tasks you with raiding an ancient dwarven vault, filled with hazards and loot! Be careful though, as you won't be able to take your gear with you! The quest includes:\n" +
				"\n" +
				"**-** A massive sub-region the size of ~3 dungeon floors, featuring over 20 new room types!\n" +
				"**-** Three new static hazards that are integrated into 10 of the new rooms!\n" +
				"**-** New variants of enemies ranging from the prisons to the city!\n" +
				"**-** An entire second progression experience, with easy low tier loot and well-defended high tier loot!\n" +
				"**-** A new boss that's specially designed to test the skills of vault plunderers!\n" +
				"\n" +
				"Of course, adventurers need a suitable reward ;). If you're successful in the vault, the Imp will let you take **a single item out with you!** Many items in the vault are at a similar power level to his classic reward of a highly upgraded ring, but you'll get a much bigger variety of choice. He'll also continue to set up shop later as usual."));

		changes.addButton( new ChangeButton( ChangeIcons.V40_CITY_CARPET, "New Environment Visuals!",
				"**After almost two years since I initially teased it, the first wave of visual improvements to Shattered's in-game pixel art are finally here!**\n" +
				"\n" +
				"Lots of dungeon environments have been improved or given new details by **Aleksandar Komitov!** This time around there's one important universal change, and then a focus on the city region and quest areas:\n" +
				"\n" +
				"**-** There is now shadowing where walls meet floor.\n" +
				"**-** The City now has a new kind of special floor tile.\n" +
				"**-** Carpets are now a custom visual that can be applied on top of regular tiles anywhere! They are currently only used in the dwarven city.\n" +
				"**-** Enhanced visuals at the city entrance and exit.\n" +
				"**-** Overhauled the Blacksmith's room.\n" +
				"**-** Overhauled Mass Grave room from the prison quest.\n" +
				"**-** Improvements to sewer barrels, barricades, and city flaming pedestals.\n" +
				"\n" +
				"I expect to continue releasing waves of new art and tweaks each major update for the forseeable future, **please let me know what you think!**"));

		changes.addButton( new ChangeButton( ChangeIcons.V40_POTION_CRIMSON, "New Consumable Item Sprites!",
				"**After almost two years since I initially teased it, the first wave of visual improvements to Shattered's in-game pixel art are finally here!**\n" +
				"\n" +
				"Most of Shattered's consumable items have been given overhauled sprites by **PumpkinVolt**! This time around we focused on consumable items, and some items directly related to those consumables. The following sprites have been redone:\n" +
				"\n" +
				"**-** Seeds, darts, potions, & elixirs/brews\n" +
				"**-** Runestones, scrolls, & spell items\n" +
				"**-** Dewdrops & petrified seed (related to seeds)\n" +
				"**-** Liquid metal & blood vial (related to potions)\n" +
				"**-** Energy crystals & exotic crystals (related to spells)\n" +
				"**-** Dwarf Tokens & other new quest items\n" +
				"\n" +
				"I expect to continue releasing waves of new art and tweaks each major update for the forseeable future, **please let me know what you think!**"));

		changes.addButton( new ChangeButton( ChangeIcons.V40_GREATSWORD_CRYSTAL, "New Enchantments!",
				"**Four new enchantments and two new curses** have been added for weapons! Long-time players will recognize several of these as reworked enchantments that were previously removed years ago.\n" +
				"\n" +
				"**- Venemous** is a returning common enchant that can apply stacking poison on a delay.\n" +
				"**- Vorpal** is a returning uncommon enchant that can convert damage into non-stacking bleed.\n" +
				"**- Eldritch** is a returning uncommon enchant that can apply fear to all nearby enemies except your primary target.\n" +
				"**- Crystal** is a new rare enchant that boosts damage, but requires you to work around limited durability.\n" +
				"**- Pressurized** is a new curse that can create a geyser of water, pushing both you and enemies away.\n" +
				"**- Wondrous** is a new curse that can trigger random cursed wand effects."));

		changes.addButton( new ChangeButton( ChangeIcons.V13_BUFF_AGGRESSION, "Swarm Intelligence Overhaul",
				"The swarm intelligence challenge is getting adjustments to improve counterplay and consistency.\n" +
				"\n" +
				"Prior to v4.0 swarm triggered with a static 8 tiles of range only when an enemy initially noticed you. Experienced players found a bunch of ways to avoid triggering the effect entirely though.\n" +
				"\n" +
				"The challenge is being adjusted to now trigger constantly whenever an enemy sees you, but with a range of only 2 tiles that increases up to 12 over time. Breaking sight in any way, even for a moment, resets this range. This way tricksy gameplay is still rewarded, but in a way that's more visible and easy to access.\n" +
				"\n" +
				"A visual buff has also been added that shows you the current alert range."));

		changes.addButton( new ChangeButton( ChangeIcons.V40_VAULTSKELE_INVESTIGATING, "New Enemy AI",
				"As part of the new vault quest, I've added a separate style of enemy behaviour that's meant to work with stealth gameplay! Enemies using this AI mode have these differences:\n" +
				"\n" +
				"**-** Nearby enemy movement can be seen through walls.\n" +
				"**- Sleeping** enemies have reduced detection range.\n" +
				"**- Wandering** enemies patrol pre-set routes and have a sharply reduced detection range behind themselves if they are moving.\n" +
				"**- Investigating** is a new AI state that exists between wandering/sleeping and attacking. Investigating enemies will move toward you, but don't attack until they detect you again. Investigating enemies can lose sight of the player easily, so ducking behind doors or corners is very effective.\n" +
				"**-** If an enemy is attacked, they skip investigating and immediately start retaliating, just like normal.\n" +
				"\n" +
				"Currently this new AI mode is only used in the new vault quest, all other enemies in the dungeon should be unchanged."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V075_LONGSWORD_CORRUPTING, "Existing Enchant Changes",
				"With new enchantments being added, I've also taken the oppourtunity to improve on some quirks relating to existing enchants:\n" +
				"\n" +
				"**- Kinetic** conserved damage is no longer lost (but still isn't increased) if the hit from kinetic is cancelled due to the enemy first dieing to another on-hit effect (e.g. smite)\n" +
				"**- Corrupting** can now apply if ANY part of the corrupting hit kills (e.g. smite), not just the hit from the corrupting weapon itself.\n" +
				"**- Grim** now triggers more consistently if extra damage (e.g. smite, holy weapon) applies before the hit from the grim weapon itself."));

		changes.addButton( new ChangeButton( ChangeIcons.V40_CHANGES, "Changes Screen Adjustments",
				"With another major release coming to Shattered, I've decided to look back and make a few adjustments to spruce up the changes screen.\n" +
				"\n" +
				"**-** Changes screen is now split into post-release (v1.0+) and pre-release (v0.9.3-), this gives enough space for tabs for each major version (with v0.5 and older still grouped together)\n" +
				"**-** Change screen icons now use historical visuals that are accurate to the update being covered. I felt this change was especially important now that we're starting a big in-game art expansion.\n" +
				"**-** Added change history for the original Pixel Dungeon at the end of the list. This includes original comments made by Watabou for each update!"));

		changes.addButton( new ChangeButton( ChangeIcons.V081_MISC, Messages.get(ChangesScene.class, "misc"),
				"**Highlights:**\n" +
				"**-** Health bars now visually darken to show the total amount of incoming damage over time\n" +
				"**-** Targeted cell VFX for incoming enemy attacks now persist until the attack occurs\n" +
				"**-** Added better support for 3rd party Android appstores that handle updates themselves\n" +
				"**-** Various internal code improvements to music playback, should reduce stuttering/glitching\n" +
				"**-** Made UI/UX improvements to stone of augmentation\n" +
				"\n" +
				"**Other:**\n" +
				"**-** Updated credits based on new artist contributions\n" +
				"**-** Alchemy and well water VFX are now visible in the fog of war\n" +
				"**-** Items are now pushed out of doors that become locked by bosses\n" +
				"**-** Dropped support for savegames prior to v3.1.1 \n" +
				"**-** Significant performance improvements to blue 'checked cell' visuals and red targeted cell indicators.\n" +
				"**-** Updated various code dependancies"));

		changes.addButton( new ChangeButton( ChangeIcons.V061_BUGFIX, Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Highlights:**\n" +
				"**-** Cases where necromancer skeleton visuals could become desynced from their actual location\n" +
				"**-** Specific cases where generating extra artifacts (e.g. via wealth farming) could affect ring generation in later depths.\n" +
				"**-** Blood vial causing healing effects to stack when they shouldn't\n" +
				"\n" +
				"**Effects:**\n" +
				"**-** Damage from Radiance's illuminate proc not counting as magical\n" +
				"**-** Battlemage's wand of lightning effect lasting for less time than intended\n" +
				"**-** Champion's second weapon not being counted as equipped by some effects\n" +
				"**-** Partial damage buildup from Corruption being lost on save/load\n" +
				"**-** Telefrag ability not interacting properly with some on-kill effects\n" +
				"**-** Spectral walls from skeleton key using the wrong vfx on boss floors"
				,
				"**Items:**\n" +
				"**-** Helpful darts triggering on-hit fx on allies since v3.3.5\n" +
				"**-** Weapon state becoming bugged if the rapier's lunge attack failed to find a target\n" +
				"**-** VFX from flow and swiftness glyph being visible in the fog of war\n" +
				"**-** Specific cases where enchantment changes could be reverted on thrown weapons\n" +
				"**-** Wild magic zapping with more wands than intended in some cases\n" +
				"**-** Value of elixir of honeyed healing not scaling properly with quantity\n" +
				"**-** Skeleton key not pushing enemies behind doors it locked in some cases\n" +
				"**-** Lost backpack item being usable to manipulate item stacks in shops\n" +
				"**-** Potions counting as being used when they splash harmlessly"
				,
				"**Enemies:**\n" +
				"**-** Specific cases where electricity VFX would not appear in the DM-300 fight\n" +
				"**-** Necromancers being able to visually push their own skeleton when teleporting it\n" +
				"**-** Specific cases where crystal guardians could get infinite haste\n" +
				"**-** Necromancers being a bit too eager to teleport their skeletons in some cases\n" +
				"**-** Various cases where chasms could cause enemy deaths to trigger twice\n" +
				"**-** Assassination and combined lethality not working on armored brutes\n" +
				"**-** Death by shocking elemental damage not counting as death by enemy magic\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Large bosses appearing outside the bounds of desktop boss HP bar\n" +
				"**-** Incorrect text relating to potions of healing or waterskin in hero epitaphs\n" +
				"**-** Various rare and minor bugs in level generation\n" +
				"**-** Various minor visual and textual errors\n" +
				"**-** Various rare crash bugs"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V14_SANDALS, "Item Buffs",
				"While v4.0 is mainly focused on new content, there are a few balance changes I'm making as well. Several items are getting some boosts:\n" +
				"\n" +
				"**- Force Cube** base damage up to 10-30 from 10-25\n" +
				"\n" +
				"**- Footwear of Nature** charge speed scaling with level increased, up to +50% at +10\n" +
				"**- Dried Rose** ghost HP up to 40+10*lvl, from 20+8*lvl, and ghost damage now scales with strength, just like the hero\n" +
				"**- Master Thieves' Armband** charge speed now scales with level, up to +50% at +10\n" +
				"\n" +
				"**- Exotic Crystals** conversion chance up to 20% per level, from 12.5%\n" +
				"**- Chaotic Censer** is now much more likely to spew gasses that actually harm the enemy"));

		changes.addButton( new ChangeButton( ChangeIcons.V40_COMBO_STRIKE, "Talent/Ability Buffs",
				"A couple of abilities/talents are also getting buffs:\n" +
				"\n" +
				"**- Combo Strike** Duelist ability now grants 15 turns of duration on-kill, just like Gladiator's combo\n" +
				"**- Feint** armor ability base cost decreased to 35 from 50 (cost swapped with challenge)"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V40_STONE_AGGRESSION, "Item Nerfs",
				"A few items were also showing as quite strong and are getting scaled back a bit.\n" +
				"\n" +
				"Most notably, I'm returning runestones of aggression back to their pre-v1.0 behaviour vs. bosses. In retrospect, while I do want them to be usable to redirect aggression during boss fights, letting them apply directly to bosses was a mistake. You can still use aggro stones on boss minions to take aggression off yourself during boss fights, and everthing ganging up on one minion is a more suitable scaled-back version of the scroll of rage effect.\n" +
				"\n" +
				"**- Stone of Aggression** can no longer be directly applied to bosses\n" +
				"**- Ring of Haste** speed boost per level down to +15%, from +17.5%\n" +
				"**- Spyglass** downside made more signiciant, bonus item opacity down to 10% from 15%"));

		changes.addButton( new ChangeButton( ChangeIcons.V40_BARKSKIN, "Talent/Ability Nerfs",
				"A few targeted nerfs/adjustments to hero abilities or talents. Some of these are general and some are aimed at preventing cheese in the early stages of the new quest:\n" +
				"\n" +
				"These changes are general (although barkskin was also far too strong in the vault):\n" +
				"**- Barkskin** talent no longer triggers from furrowed grass, but does now trigger from plants\n" +
				"**- Barkskin** talent amount per level reduced to 33% of hero level, from 50%\n" +
				"**- Challenge** armor ability base cost increased to 50 from 35 (cost swapped with feint)\n" +
				"\n" +
				"These adjustments are aimed at the new vault area specifically:\n" +
				"**- Hold Fast** talent now requires the Warrior's seal, or has a cap based on worn armor if used by other heroes\n" +
				"**- Monk** no longer retains energy over revives or entry into the vault"));

	}

}
