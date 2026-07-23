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
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v4_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		//add_Coming_Soon(changeInfos);
		add_v4_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V074_SHPX, "Overview and ETA",
				"..."));
	}

	public static void add_v4_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v4.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton( ChangeIcons.V074_SHPX, "Dev Commentary",
				"TODO"));

		changes.addButton( new ChangeButton( ChangeIcons.V33_IMP, "New City Quest",
				"TODO"));

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
				"**-** Overhauled Mass Grave and Ritual rooms from the prison quest.\n" +
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
				"**-** Dwarf Tokens & other new quest items\n" +
				"\n" +
				"I expect to continue releasing waves of new art and tweaks each major update for the forseeable future, **please let me know what you think!**"));

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
				"Prior to v4.0 swarm triggered with a static 8 tiles of range only when an enemy initially notices you. Experienced players have found a bunch of ways to avoid triggering the effect entirely though.\n" +
				"\n" +
				"The challenge is being adjusted to now trigger constantly whenever an enemy sees you, but with a range of only 2 tiles that increases up to 12 over time. Breaking sight in any way, even for a moment, resets this range. A visual buff is also added that shows you the current alert range. This way tricksy gameplay is still rewarded, but in a way that's more visible and easy to access."));

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
				"**-** Added change history for the original Pixel Dungeon at the end of the list. This includes original comments made by Watabou for each update.!"));

		changes.addButton( new ChangeButton( ChangeIcons.V081_MISC, Messages.get(ChangesScene.class, "misc"),
				"..."));

		changes.addButton( new ChangeButton( ChangeIcons.V061_BUGFIX, Messages.get(ChangesScene.class, "bugfixes"),
				"..."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		//

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		// stone of aggro again?

	}

}
