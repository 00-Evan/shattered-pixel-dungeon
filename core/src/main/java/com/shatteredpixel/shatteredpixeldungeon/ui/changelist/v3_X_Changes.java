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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BlessSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v3_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		//add_Coming_Soon(changeInfos);
		add_v3_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Overview",
				"..."));

	}

	public static void add_v3_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v3.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("ALPHA-1.0", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Alpha Info",
				"The Alpha for v3.0 is now winding down! I expect to release v3.0-BETA-1.0 in another few days! Please let me know if you encounter any issues so I can get them fixed before then.\n\n" +
				"I am considering doing further alphas as v3.0 continues development after the holidays, giving you guys early peeks at the Paladin and the other two armor abilities. Said alphas will last for a day or two."));

		changes.addButton( new ChangeButton(new HeroIcon(new AscendedForm()), "Ascended Form",
				"_Ascended Form and all of its talents have been added to the alpha!_\n\n" +
				"The Cleric projects holy energy from themselves, assuming an extended radiant form for 10 turns. While in this form the Cleric can cast new spells, gains 2 attack range, 30 shielding, and any additional spells they cast will grant 10 shielding per holy tome charge used.\\n\\nThe shielding from ascended form and its spells does not decay normally, but will immediately fade once ascended form ends. The ability can be re-used to refresh ascended form's duration."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"The Cleric can now be played past floor 20, allowing runs with them to be completed!\n\n" +
				"**Bless** has been scaled back when used on allies, now grants 6/10 turns of bless and 10/15 healing, down from 10/15 and 15/25.\n\n" +
				"Now that other characters trigger **illuminate** (bugfix from ALPHA-0.6), it creates an unfortunate anti-synergy with priest's illuminate bonus. To fix this, other characters now deal +10 bonus magic damage when triggering illuminate with Priest. To prevent this stacking with **Searing Light**, that talent now only triggers from the Cleric's attacks."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by ALPHA:**\n" +
				"**-** Minor visual errors in spell window\n" +
				"**-** Minor textual errors"));

		changes = new ChangeInfo("ALPHA-0.7", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Quick extra release to fix some bugs =S\n\n" +
				"Fixed the following bugs:\n" +
				"**Caused by ALPHA:**\n" +
				"**-** Crashes caused by hallowed ground\n" +
				"**-** Holy Intuition being referred to as divine intuition in some cases\n" +
				"**-** Text display errors with recall inscription buff"));

		changes = new ChangeInfo("ALPHA-0.6", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new HeroIcon(HeroSubClass.PRIEST), "Priest Spells",
				"I've added two new Priest spells! These are both focused more on utility and synergy.\n\n" +
				"**Hallowed Ground** creates an area of terrain that's cumbersome to enemies and beneficial to the Priest and their allies. Allies are healed, the Priest get shielding, enemies get briefly rooted and slowed, and grass grows randomly.\n\n" +
				"**Mnemonic Prayer** extends the duration of buffs on allies, or debuffs on enemies. It also re-applies illuminated if the target was illuminated previously.\n\n" +
				"I've also lessened the blur on the Cleric's splash art again, it's almost full-detail!\n\n" +
				"With these additions the base Cleric and Priest are now finished! I'm going to implement an armor ability over the next few days and then we'll be ready for beta!"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**-** Retroactively renamed the alphas to \"ALPHA-0.X\"\n\n" +
				"I've made a few changes to talent/spell names and icons for better consistency:\n" +
				"**-** Detect Magic renamed to Holy Intuition\n" +
				"**-** Recall Glyph renamed to Recall Inscription\n" +
				"**-** Adjusted icons for Cleric food talents\n" +
				"**-** Only talents that grant new spells use the 'yellow + blue' color scheme\n" +
				"**-** Adjusted icons for Sucker Punch and Followup Strike"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Caused by ALPHA:**\n" +
				"**-** Holy Intuition (Detect Curse) not working on wands\n" +
				"**-** Allies not properly triggering illuminated from guiding light\n" +
				"**-** Shield of Light duration stacking with multiple casts instead of resetting\n\n" +
				"**Existed Prior to ALPHA:**\n" +
				"**-** Various minor textual errors"));

		changes = new ChangeInfo("ALPHA-0.2 - 0.5", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new HeroIcon(BlessSpell.INSTANCE), "Spell Additions & Changes",
				"I've added a new talent/spell in this alpha release!:\n\n" +
				"**Bless** is a T2 spell/talent that applies the bless buff to the Cleric or another character, and applies either healing or shielding, depending on the target. I decided on another cheaper spell for T2 since recall glyph is being made more expensive.\n\n" +
				"Expect more talents/spells for the Priest very soon, possibly tomorrow!\n\n" +
				"I'm also making a few balance changes to existing spells based on early feedback:\n" +
				"**- Recall Glyph** charge cost increased by 1 across the board.\n" +
				"**- Divine Sense** duration down to 30 from 50.\n" +
				"**- Divine Sense** charge cost up to 2 from 1, as was already stated in the UI."));

		changes.addButton( new ChangeButton(new HolyTome(),
				"Thanks a bunch for your early feedback!\n\n" +
				"Aside from bug reports, the most consistent bit of feedback I've heard is that the holy tome's charging mechanic doesn't feel great. I originally concepted it as charging based on exp gain to accommodate some of my early spell designs that involved self-healing. However, once I got into more specific designs I ended up not making these sorts of spells anyway, as while healing is obviously part of the divine caster archetype, it’s also really powerful and boring.\n\n" +
				"So at this point it’s become pretty clear to me that the existing tome recharge mechanic is a holdover from earlier design iterations, and I’m trying out scrapping it entirely. The tome now recharges based on time, roughly 33% slower than the Rogue’s Cloak of Shadows, and we’ll refine things from there. This should be a buff to recharging speed overall, although it may not feel that way after floor 10 as I’m also fixing a bug that caused Holy Lance to not consume charges."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Existed Prior to ALPHA:**\n" +
				"**-** Holiday pasty names not appearing in catalogs\n" +
				"**-** Duration of greater haste not being preserved on save/load\n" +
				"**-** Various minor textual errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		/*changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released ..., ...\n" +
				"_-_ ... days after Shattered v2.5.0\n\n" +
				"Dev commentary will be added here in the future."));*/

		/*changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Alpha Info",
				"Hey Alpha testers! This alpha is in a less complete state than normal, here's info about what's currently incomplete game-content wise:\n" +
				"**-** The Cleric is missing one T2 Talent\n" +
				"**-** The Priest is missing two of their T3 talents\n" +
				"**-** The Paladin is not implemented\n" +
				"**-** No armor abilities are implemented, and so I've capped Cleric runs at floor 20 for now."));*/

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 1), "The Cleric!",
				"**Shattered Pixel Dungeon has another new hero, making for six total!!**\n" +
				"\n" +
				"The Cleric is an entire new hero class **with a variety of unique spells that they learn via talents!** They can be unlocked by removing the curse on any piece of equipment, but existing players who already have a win will automatically unlock them!\n" +
				"\n" +
				"Much like how the Duelist can use a bunch of weapon abilities, I want the Cleric to be an ability-centric hero focused on Magic. Unlike the Duelist, these abilities are tied into the hero and their talents, instead of equipment items.\n" +
				"\n" +
				"**Note that while the base Cleric is playable, they are not yet fully implemented!** Some visuals are placeholders and not all talents are functional."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 4), "Cleric Subclasses",
				"**The Cleric has two subclasses, each with their own emphasis!**\n" +
				"\n" +
				"**The Priest** is focused on ranged spell combat, effects that synergize with magical items like wands and artifacts.\n" +
				"\n" +
				"**The Paladin has not been implemented yet, look out for them soon!**"));

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
				"\n" +
				"**Misc.:**\n" +
				"**-** Various minor textual errors\n" +
				"**-** Various rare crash errors"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SOMETHING), "Nothing yet!",
				"While there are no other balance changes in v3.0 currently, I do plan to go over some balance numbers and make adjustments here later in the beta."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

	}

}
