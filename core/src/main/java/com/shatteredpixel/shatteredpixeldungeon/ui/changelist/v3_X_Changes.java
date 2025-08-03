/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollExileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v3_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v3_2_Changes(changeInfos);
		add_v3_1_Changes(changeInfos);
		add_v3_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Overview and ETA",
				"The next major Shattered update will v3.3 (I may change the name to v4.0 but it's unlikely). This update will include an overhaul to the Ambitious Imp quest in the metropolis!\n" +
				"\n" +
				"The blacksmith quest overhaul ended up taking quite a bit of time, and so I expect v3.3 will as well. I will write some blog posts as I make progress though, so hopefully you'll hear from me as things start to take shape in October or November.\n" +
				"\n" +
				"**Please keep in mind that while I always try to keep to the ETAs I provide, they are just estimates. If you don't hear from me by the ETA, it means I'm still busy with the update!**"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHANGES), "Patches & Old Android and Java",
				"There will be a higher than normal number of patches to v3.2. Firstly to address balance concerns arising from all the item changes (rather than waiting and doing them next update), and then to make various game library updates and technical changes.\n" +
				"\n" +
				"As mentioned previously, one of the consequences of these library updates and technical changes will be removing support for Android 4.0-4.4, and Java 8-10. Dropping support for these is unfortunately necessary in order to meet an impending requirement by Google."));

		changes.addButton( new ChangeButton(new Image(new ImpSprite()), "City Quest Overhaul!",
				"The major content improvements coming in v3.3 will be an overhaul to the Ambitious Imp's quest in the Metropolis. This quest is one of the last major leftovers from the original Pixel Dungeon and badly needs some modernization.\n" +
				"\n" +
				"Just like the Blacksmith quest overhaul, this new quest will send you to a new sub-region of the dungeon with some unique gameplay. Rather than digging, you'll be attempting to infiltrate a dwarven vault full of danger and treasure!"));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Other Changes",
				"I will want to keep v3.3 mainly focused on the new quest, but there will be room for smaller changes too! Mainly this will be balance changes, but there might be room for a few smaller design improvements or additions too.\n" +
				"\n" +
				"Also, I can't make any promises yet, but I am hoping to move forward with more visual improvements, including in-game spritework improvements, whose development has largely been on hiatus since last year."));
	}

	public static void add_v3_2_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v3.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Dev Commentary",
				"**-** Released August 4th, 2025\n" +
				"**-** 63 days after Shattered v3.1.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JAVELIN), "Thrown Weapon Sets",
				"**Thrown weapons have been majorly overhauled to be more worthwhile to upgrade!**\n" +
				"\n" +
				"**-** Thrown weapons now spawn in sets of three, sets do not mix.\n" +
				"**-** Thrown weapon base durability increased to 3x5/8/12, from 2x5/10/15.\n" +
				"**-** Sets are upgraded as a unit (all 3), and upgrading fully repairs the set.\n" +
				"**-** Upgrades now boost durability by 1.5x, down from 3x.\n" +
				"**-** Thrown weapon default damage scaling per upgrade reduced to 1-tier, down from 2-tier.\n" +
				"**-** Sets can be enchanted, cursed, augmented, unidentified, etc.\n" +
				"**-** Sets can spawn with natural upgrades, enchants, or curses.\n" +
				"**-** A few special rooms now have a chance to spawn higher value thrown weapon sets.\n" +
				"\n" +
				"Note that darts are not affected by these changes, they effectively all belong to the same set and still cannot be upgraded."));

		changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), "Hit and Miss Icons",
				"**Pretty much every effect that changes accuracy or evasion now has an icon that shows up when that effect is the reason an attack hit or missed!**\n" +
				"\n" +
				"This is an extension of the green bow icon that was used to make the Ferret Tuft's evasion boost more visible in v3.1. It should make it much easier to tell how much of a difference various buffs/debuffs are making to hit chance.\n" +
				"\n" +
				"There are 12 different hit icons and 11 miss icons in total."));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "Text Banners",
				"**New visuals have been added for the 'boss slain' and 'game over' text banners!**\n" +
				"\n" +
				"They should now match the newer text visuals in the title screen. This also includes some detail adjustments such as centering the sword in 'boss slain' vertically instead of horizontally."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LIQUID_METAL), "Liquid Metal",
				"Liquid metal has also been changed to go along with the general changes to thrown weapons:\n" +
				"\n" +
				"**-** Recipe adjusted, now takes one known uncursed thrown weapon set and always costs 3 energy.\n" +
				"**-** Liquid metal can now replace missing/broken thrown weapons from a set (up to the usual cap of 3)\n" +
				"**-** Liquid metal scaling per upgrade down to 1.33x from 2x.\n" +
				"\n" +
				"The upgrade scaling change works out to be a slight nerf, as thrown weapon durability scaling per upgrade was also reduced to 1.5x from 3x. However, I expect durability and repairing to be much more of a factor now, as previously upgraded thrown weapons would reach infinite durability very quickly."));

		changes.addButton(new ChangeButton(new TalentIcon(Talent.SURVIVALISTS_INTUITION), "Survivalist's Intuition",
				"Now that thrown weapons can be identified, there is design space for the Huntress to have a non-generic identification talent:\n" +
				"\n" +
				"**- Survivalist's Intuition** now lets the Huntress ID thrown weapons at 3x speed at +1 or on-use at +2. Previously it increased ID speed of all items by 1.75x at +1 or 2.5x at +2."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**Highlights:**\n" +
				"**-** Blacksmith's smith option now offers a thrown weapon in addition to 2 melee weapons and an armor\n" +
				"**-** Thrown weapons (including the bow) now always have a throw delay of 1 turn if they aren't aimed at a target\n" +
				"**-** Ascension challenge now always notifies the player the first time the amulet's curse is weakened\n" +
				"**-** Characters that are immune to the wand of lightning no longer reduce its overall damage if it arcs though them\n" +
				"**-** Chaotic Censer no longer aims at passive enemies\n" +
				"**-** Added a new language: Swedish!\n" +
				"\n" +
				"**Thrown Weapons:**\n" +
				"**-** Augmentation now affects thrown weapon durability based on how it affects attack speed\n" +
				"**-** Projecting enchant can now stack on thrown weapons, giving more reach\n" +
				"**-** Explosive curse now consumes uses on thrown weapons when it triggers\n" +
				"**-** Friendly curse now sets weapon damage to 0 if charmed by that enemy",

				"**Misc:**\n" +
				"**-** Improved most tutorial/guidebook text to be more concise\n" +
				"**-** Stone of Intuition guess window now shows which item you are guessing for\n" +
				"**-** Blacksmith room can no longer spawn next to the depth exit room\n" +
				"**-** Rats that are made friendly by the 19th armor ability no longer attack enemies\n" +
				"**-** Wands now only need to be known uncursed to be used in making resin\n" +
				"**-** Made tweaks to tunnel and maze rooms to reduce the chance of them generating certain shapes\n" +
				"**-** Made piranha description text more helpful\n" +
				"**-** Updated code libraries on iOS (Android and Desktop lib updates will come in a patch later)\n" +
				"**-** Added dev commentary for v2.2.0\n"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Gameplay:**\n" +
				"**-** Cases where sacrificial fire would not apply just after a character steps into it\n" +
				"**-** Rogue's inscribed stealth talent not properly triggering from some unstable spell effects\n" +
				"**-** Rare cases where hero could not regen more than 1hp/turn when they should\n" +
				"**-** Distortion traps always generating mimics when choosing to generate an uncommon enemy\n" +
				"**-** Cleric's body form enchantment/glyph effect also applying to some enemies\n" +
				"\n" +
				"**Misc:**\n" +
				"**-** Cases where status text would stack on itself incorrectly\n" +
				"**-** Language selection menu using the wrong name for Indonesian\n" +
				"**-** Minor visual and textual bugs"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new TalentIcon(Talent.PROJECTILE_MOMENTUM), "Thrown Weapon Talent Buffs",
				"Some talents that interact with thrown weapons are getting buffs as part of the thrown weapon changes:\n" +
				"\n" +
				"**- Projectile Momentum** accuracy boost up substantially, to +50/100/150% at +1/2/3, from +20/40/60% at +1/2/3.\n" +
				"\n" +
				"**- Shared Enchantments** no longer prevents multiple enchantment triggers. Both enchants can now trigger if the thrown weapon and the Huntress' bow are both enchanted."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "Cleric & Priest Buffs",
				"Firstly, a quick buff to the Cleric's worst performing T2 spell:\n" +
				"**- Divine Sense** duration up to 50 turns from 30.\n" +
				"\n" +
				"I'm also making some changes to improve the Priest's synergy with their various spell effects. Hopefully this will help the subclass compete a bit more with Paladin, who already gets great synergy thanks to all spells extending holy weapon and ward:\n" +
				"**- Guiding Light** free use cooldown down to 50 turns from 100.\n" +
				"**- Illuminate** is now inflicted on directly targeted enemies by all spells."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "Warrior & Gladiator Buffs",
				"Firstly, I'm making a change to the Warrior in general, to improve tactical flexibility and to make the Hold Fast talent more interesting:\n" +
				"**- Hold Fast** now slows the decay of combo and shielding buffs by 33/67/100% at +1/2/3. This includes the broken seal shield and Berserker's enrage shield!\n" +
				"\n" +
				"Next, the Gladiator was weakened a bit after v3.1's Warrior changes, so I'm giving him some more combo flexibility to compensate:\n" +
				"**- Combo** now lasts for 15 turns after killing an enemy.\n" +
				"**- Cleave** combo duration boost increased to 30/45/60 turns at +1/2/3, from 15/30/45 turns."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 6), "Battlemage Buffs/Changes",
				"Some Battlemage staff on-hit effects have been changed to be more interesting and more powerful. Most notably this removes three effects that were previously just copies of enchantments:\n" +
				"\n" +
				"**- Staff of Fireblast** effect reworked, now has a chance to explode away flames, dealing damage to enemies.\n" +
				"**- Staff of Lightning** effect reworked, now has a chance to charge the Mage, granting lightning immunity and extra arcing reach.\n" +
				"**- Staff of Blast Wave** effect reworked, now consumes paralysis to deal big bonus damage.\n" +
				"**- Staff of Warding** mildly changed, now heals level 2 and 3 wards in addition to sentries."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TOMAHAWK), "Thrown Weapon Nerfs",
				"Various thrown weapons are getting targeted nerfs, mainly focused around compensating for lower durability weapons now effectively having +50% base uses, or certain weapons now being much more spammable.\n" +
				"\n" +
				"**- Shuriken** instant-throw condition changed to a simple 20 turn cooldown\n" +
				"**- Kunai** base damage down to 6-12 from 6-15\n" +
				"**- Bolas** damage scaling down to 1-2 from 1-3\n" +
				"**- Heavy Boomerang** durability down to 5 from 8\n" +
				"**- Tomahawk** damage scaling down to 1-3 from 1-4\n" +
				"**- Tomahawk** bleed % down to 33% from 60%, but it is now a separate roll that ignores enemy armor\n" +
				"**- Force Cube** base damage down to 10-20 from 10-25"));

		changes.addButton(new ChangeButton(new TalentIcon(Talent.SHARED_UPGRADES), "Thrown Weapon Talent Nerfs",
				"Some talents are also getting nerfed as part of the thrown weapon changes:\n" +
				"\n" +
				"**- Projectile Momentum** damage boost down to +10/20/30% at +1/2/3, from +15/30/45% at +1/2/3\n" +
				"\n" +
				"**- Durable Projectiles** durability boost down to +33%/+50% at +1/+2, from +50%/+75% at +1/+2\n" +
				"**- Shared Upgrades** slightly reworked, now grants a flat +16.67% damage boost and +1 duration per thrown weapon level, but caps at +33/67/100% damage and +2/4/6 duration at talent level 1/2/3."));

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "Berserker Nerfs",
				"The Berserker is doing quite well after v3.1, given that the new shield naturally synergizes with him. I'm scaling back the base subclass power a bit to compensate:\n" +
				"\n" +
				"**-** Rate of rage gain and loss reduced by 25%\n" +
				"**-** Base enrage shield reduced to 8+2*lvl from 10+2*lvl (the armor's level)"));

	}

	public static void add_v3_1_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v3.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Dev Commentary",
				"**-** Released June 2nd, 2025\n" +
				"**-** 95 days after v3.0.0\n" +
				"\n" +
				"Dev commentary will be added here in the future"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SEAL), "Warrior Mini Rework",
				"**The Warrior has had a mini overhaul to his broken seal ability, and new visuals for his splash art and broken seal!**\n" +
				"\n" +
				"The seal's shielding now triggers in bursts with a cooldown, when the Warrior is below 50% HP. It should feel more impactful and interactive while still being easy to use for new players.\n" +
				"\n" +
				"The Warrior's new splash art is once again done by Aleksandar Komitov. The original Warrior splash was the first one Aleks did, and so this remake is the most significant of the hero splash art changes. Aleks was keen to make corrections to armor design and proportions to bring the splash up to his current standards.",

				"Here are the detailed changes to the broken seal shielding mechanics:\n" +
				"**-** Broken Seal shield no longer passively builds, it now triggers all at once just before the Warrior gets damaged to 50% health or lower.\n" +
				"**-** Max shield is now based on armor tier, scaling from 5-13 (max of 15 with iron will talent).\n" +
				"**-** This shielding does not decay, but ends shortly after combat\n" +
				"**-** This shielding has a 150 turn cooldown, unused shield refunds up to 50% cooldown.\n" +
				"**-** The broken seal can now be applied to known-uncursed armor.\n" +
				"**-** When swapping armor, the Warrior now gets a prompt to swap his seal too.",

				"Various other Warrior mechanics have been adjusted as well:\n" +
				"**- Hearty Meal** HP threshold increased to 33% from 30%, healing increased to 4/6 at +1/+2 from 3/5 at +1/+2.\n" +
				"**- Provoked Anger** talent now triggers when any shield buff breaks, and grants +3/+5 bonus damage, up from +2/+3.\n" +
				"**- Iron Will** talent unchanged, still grants 1 or 2 max shield.\n" +
				"**- Liquid Willpower** talent now grants regular barrier equal to 6.5%/10% of max HP, instead of recharging 50%/75% of max seal shield.\n" +
				"**- Lethal Defence** talent now reduces seal cooldown, instead of recharging seal shield.\n" +
				"**- Gladiator** will retain any active shielding from his broken seal as long as he has combo.\n" +
				"**- Berserker** enrage shield is now its own separate shielding buff, and has its own scaling separate from the seal's max shield."));

		changes.addButton( new ChangeButton(Icons.STAIRS.get(), "New Rooms and Terrain Types",
				"This update includes an **expansion to the dungeon's standard rooms!**\n" +
				"\n" +
				"**- New decorative terrain** has been added to each region, largely inspired by details from the region splash arts. \n" +
				"**- 5 new standard rooms** have been added that use these new terrain objects, one per region.\n" +
				"**- 8 existing standard rooms** have been modified to use the new terrain objects.\n" +
				"**- 10 new entrance/exit variants** of standard rooms have been added as well. two per region.\n" +
				"**- Boss Arenas** also use these new terrain types in a few places\n" +
				"**- Plain empty rooms** no longer spawn normally."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FERRET_TUFT), "Ferret Tuft",
				"**A new trinket has been added to the game!**\n" +
				"\n" +
				"The **Ferret Tuft** is a simpler evasion-boosting trinket with more of a cute aesthetic. It's a little reference to a favourite lime ferret.\n" +
				"\n" +
				"Choosing and upgrading the trinket will cause all characters to gain evasion, including enemies! That might sound like a mixed bag, but keep in mind that there are lots of ways to counter enemy evasion."));

		changes.addButton( new ChangeButton(BadgeBanner.image( Badges.Badge.TAKING_THE_MICK.image ), "New Badges",
				"v3.1 also includes **four new badges**, each themed around a specific challenge:\n" +
				"\n" +
				"**- Safety Hazard** is a gold-tier badge that requires using terrain against enemies\n" +
				"**- So Many Colors** is a platinum-tier badge that requires having a bunch of buffs/debuffs at once\n" +
				"**- Pacifist Ascent** is a diamond-tier badge that require surviving an ascension without any enemy kills.\n" +
				"**- Taking the Mick** is a diamond-tier badge that requires defeating the final boss with a VERY high level pickaxe\n" +
				"\n" +
				"I've also reduced the difficulty of the **Big Game Hunter** badge. It now requires discovering 10 types of rare enemies, down from all of them."));

		changes.addButton( new ChangeButton(new Image(new GnollExileSprite()), "New Rare Enemies",
				"**Two new rare enemies** have been added to the sewers:\n" +
				"\n" +
				"**Gnoll Exiles** are exceptionally strong, but also wary of combat. They won't attack unprovoked, so you can just let them pass, but maybe you'll be interested in the loot they carry...\n" +
				"\n" +
				"**Hermit Crabs** are sturdy but slow crabs that use a broken barrel for extra support. They're a bit tough to fight, but much easier to run away from, and have a good chance to drop some armor for you."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.RANKINGS), "Exploration and Quest Score",
				"I've made adjustments to how score is calculated to balance the difficulty of the various score categories a little better:\n" +
				"\n" +
				"**Exploration Score** is no longer all-or-nothing for each floor. It is now based on how many rooms were not fully explored. For each floor, score is reduced to 50%/20%/0% for 1/2/3+ missed rooms. The criteria for whether something is 'fully explored' is unchanged.\n" +
				"\n" +
				"**Quest Score** can now be reduced in a similar manner to boss score. Make sure to position well to avoid penalties! This reduction includes telegraphed attacks/effects (e.g. crystal spire, gnoll geomancer), and regular attacks that you should be able to always avoid (e.g. rot lashers, fetid rat's ooze). Cases where attacks are sometimes unavoidable (e.g. corpse dust wraiths, gnoll trickster) have some leeway before penalties apply."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**Highlights:**\n" +
				"**-** Various small color tweaks to health bars, inventory buttons, and flare vfx to improve the experience for colorblind players\n" +
				"**-** Custom notes for items can now be created or edited from the item's info window\n" +
				"**-** Hero renaming can now be done in the hero info window while a run is in progress\n" +
				"**-** All potions now refresh the duration of their specific effects. Previously the duration of some potion effects could stack on themselves\n" +
				"**-** Added discovery hints for all catalog entries\n" +
				"**-** Added landmark entries for lost backpack and beacon of returning",

				"**Items:**\n" +
				"**-** Slightly reduced telekinetic grab's sale/energy value, so that it can't be used to effectively turn high-tier thrown weapons into energy\n" +
				"**-** Unidentified wands can now be imbued in the mage's staff\n" +
				"**-** Partially IDed items can now be used with the dried rose\n" +
				"\n" +
				"**Characters:**\n" +
				"**-** Improved Tengu's AI, he can now switch targets if he is unable to attack the hero\n" +
				"**-** Defeating Dwarf King now cleanses the player of the degraded debuff",

				"**Effects:**\n" +
				"**-** Smaller shorter-term shielding buffs are now consumed before larger longer-term ones\n" +
				"**-** The gravity chaos cursed wand effect now has its own debuff icon\n" +
				"**-** When using metamorph, food talents that grant the same type of recharging can now stack\n" +
				"**-** Improved VFX for activating or deactivating brawler's stance\n" +
				"**-** Guiding Light's spell icon is now brightened when it is free to cast\n" +
				"\n" +
				"**Misc:**\n" +
				"**-** The inventory button gold indicator on mobile now shows when buying items\n" +
				"**-** Rooms with a chasm in the center must now be at least 3x3, up from 2x2\n" +
				"**-** Increased the minimum supported iOS version to 12, from 11\n" +
				"**-** Moved the notification position to the top-left on the Steam version. It should no longer obscure UI elements",

				"**v3.1.1:**\n" +
				"**-** Switched Shattered's rendering backend on iOS to use Metal (new) instead of OpenGL (legacy). This should result in better performance and higher frame rates on supported devices.\n" +
				"\n" +
				"**-** Enemies and items can no longer spawn on top of plants generated by garden rooms\n" +
				"\n" +
				"**-** Added a new language: Traditional Chinese!\n" +
				"**-** Renamed Chinese language to Simplified Chinese\n" +
				"**-** Updated translations and translator credits\n" +
				"\n" +
				"**-** Increased the HP threshold for status pane blinking red to 33% from 30%, for consistency with hearty meal change."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Highlights:**\n" +
				"**-** Thrown weapons gaining or losing more accuracy than intended based on enemy adjacency\n" +
				"**-** Shocking enchantment triggering its damage twice in many cases since v3.0\n" +
				"**-** Runs in progress older than 2 months showing 'NO TEXT FOUND'\n" +
				"**-** Allies not waking from magical sleep after it has healed them\n" +
				"**-** Desktop versions downloaded via github not properly notifying when updates are available\n" +
				"\n" +
				"**Items:**\n" +
				"**-** Players being able to take smith rewards multiple times in specific cases\n" +
				"**-** Stone glyph not considering some evasion/accuracy buffs\n" +
				"**-** Brimstone glyph not properly scaling past +50% glyph power\n" +
				"**-** Boomerangs disappearing if the game was closed during their circle back animation\n" +
				"**-** Crystal path rooms sometimes sorting items incorrectly when player had exotic crystals\n" +
				"**-** Cursed wand of warding not using autotargeting",

				"**Effects:**\n" +
				"**-** Hallowed ground producing furrowed grass more often than intended in some cases\n" +
				"**-** Ascended form ending early if its shielding was reduced to 0\n" +
				"**-** Metamorphed cleanse clearing lost inventory debuff\n" +
				"**-** Metamorphed aggressive barrier using incorrect logic\n" +
				"**-** Lay on Hands sometimes applying 0 barrier to allies\n" +
				"**-** Death via the Cleric's life link not being recorded in rankings\n" +
				"**-** Targeting traps having very slightly less range than intended\n" +
				"**-** Duelist being able to stack invisibility using the sneak weapon ability\n" +
				"**-** Metamorphed holy intuition deleting armor in rare cases",

				"**Characters:**\n" +
				"**-** Swapping places with allies not being counted as movement for shuriken's instant attack\n" +
				"**-** Exploits where sheep could last forever if game was frequently saved/loaded\n" +
				"**-** Mirror images not benefitting from body form or holy weapon if the Cleric was unarmed\n" +
				"**-** Light Ally and Shadow Clone having very slightly more accuracy/evasion than the hero at base\n" +
				"\n" +
				"**Misc:**\n" +
				"**-** Shattered locking to 60fps on 120hz iOS devices\n" +
				"**-** Custom notes not properly applying to specific rings, wands, & trinkets\n" +
				"**-** Paralysis vfx on enemies sometimes cancelling animations as it ends\n" +
				"**-** Rare cases of wonky display scaling on Linux systems\n" +
				"**-** Various minor visual & textual errors\n" +
				"**-** Various rare crash errors",

				"**v3.1.1:**\n" +
				"**-** Thrown weapons sticking to broken crystal guardians\n" +
				"**-** Ebony mimics always appearing hidden after ankh revive\n" +
				"**-** Various issues with how the game handles animation logic at very low framerates\n" +
				"**-** Haptics not working properly on more modern iOS devices\n" +
				"**-** Website links not working on modern iOS versions\n" +
				"**-** Indonesian language not working on desktop platforms"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight(CharSprite.POSITIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "Cleric Buffs",
				"Overall the Cleric is doing well following their initial release. Their winrate is low currently, but they are also very popular so I expect some of that is people still figuring them out. For the moment I'm focusing on multiple targeted buffs to specific Cleric mechanics that are weaker vs. others.\n" +
				"\n" +
				"**Base class:**\n" +
				"**- Guiding light** base damage up to 2-8 from 2-6\n" +
				"**- Holy Weapon & Ward** are now cast instantly\n" +
				"**- Shield of Light** duration up to 5 from 4\n" +
				"**- Divine Sense** is now cast instantly\n" +
				"\n" +
				"**Paladin:**\n" +
				"**- Lay on Hands** healing up to 15/20/25 from 10/15/20\n" +
				"**- Aura of Protection** dmg resist up to 20%/30%/40% from 15%/23%/30%",

				"**Priest:**\n" +
				"**- Illuminate** bonus damage up to 5+lvl from lvl\n" +
				"**- Radiance** now triggers and applies illuminate\n" +
				"**- Holy Lance** cooldown down to 30 from 50\n" +
				"**- Hallowed Ground** heal up to 15 from 10\n" +
				"**- Hallowed Ground** root up to 2 turns from 1\n" +
				"**- Mnemonic Prayer** is now cast instantly\n" +
				"\n" +
				"**Ascended Form:**\n" +
				"**- Divine Intervention** duration extension up to 3/4/5/6 from 1/2/3/4\n" +
				"**- Judgement** extra dmg increased to +33% per spell from 5-10 per spell\n" +
				"\n" +
				"**Power of Many:**\n" +
				"**- Life Link** duration up to 10/13/17/20 from 6/8/10/12"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight(CharSprite.NEGATIVE);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "Cleric Nerfs",
				"A few Cleric mechanics are standouts in terms of power though, so I am scaling some of those back:\n" +
				"\n" +
				"**- Searing Light** dmg down to +3/+5 from +4/+6\n" +
				"**- Enlightening Meal** charge gain down to +0.67/+1 from +1/+1.5\n" +
				"**- Cleanse** is no longer cast instantly\n" +
				"**- Hallowed Ground** barrier now caps at 30\n" +
				"**- Flash** starting charge cost up to 2 from 1\n" +
				"**- Stasis** charge cost up to 2 from 1, but duration +50%"));

	}

	public static void add_v3_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v3.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 27th, 2025\n" +
				"_-_ 197 days after Shattered v2.5.0\n" +
				"_-_ 548 days after Shattered v2.0.0\n\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 1), "The Cleric!",
				"**Shattered Pixel Dungeon has another new hero, making for six total!!**\n" +
				"\n" +
				"The Cleric is an entire new hero class **with a variety of unique spells that they learn via talents!** They can be unlocked by removing the curse on any piece of equipment, but existing players who already have a win will automatically unlock them!\n" +
				"\n" +
				"Much like how the Duelist can use a bunch of weapon abilities, I want the Cleric to be an ability-centric hero focused on Magic. Unlike the Duelist, these abilities are tied into the hero and their talents, instead of equipment items."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 4), "Cleric Subclasses",
				"**The Cleric has two subclasses, each with their own emphasis!**\n" +
				"\n" +
				"**The Priest** is focused on ranged spell combat, effects that synergize with magical items like wands and artifacts.\n" +
				"\n" +
				"**The Paladin** is focused on melee spell combat and defensive power. Their effects most strongly synergize with weapons and armor."));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.CLERIC, 6), "Cleric Armor Abilities",
				"**The Cleric has three lategame armor abilities as well!**\n" +
				"\n" +
				"**Ascended Form** grants the Cleric access to new spells, extra attack ranged, and shielding whenever they cast spells.\n" +
				"\n" +
				"**Trinity** lets the Cleric access the effects of items they've previously identified in their run without the items themselves.\n" +
				"\n" +
				"**Power of Many** empowers or creates an ally for the Cleric, and grants them new spells to cast with that ally.\n"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight(CharSprite.WARNING);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.DISPLAY.get(), "Visual & Interface Changes",
				"**Shattered's title graphic has been totally redrawn!** The new title graphic, by Aleksandar Komitov, improves on text quality and style while trying to stay true to the original title. There is now a landscape and portrait variant of this title as well.\n" +
				"\n" +
				"The game's **Hero Splash Arts** have been improved as well! After so many years, Aleksandar has revised some of the game's hero splash arts to bring them up to his current standards:\n" +
				"**-** The Huntress and Rogue have recieved major changes\n" +
				"**-** The Mage has recieved moderate changes\n" +
				"**-** The Duelist has received only tiny tweaks to some face details.\n" +
				"**-** The Warrior's splash remains unchanged for the moment, but improvements to it are coming soon as well!\n" +
				"\n" +
				"The **Games in Progress screen** has been expanded too, with up to 6 runs at once, info about recency, and sorting options."));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"**Highlights:**\n" +
				"**-** Turned off auto-targeting in cases where it was almost always wrong (Bless spell, Wand of Warding, etc.)\n" +
				"**-** Enemies are now more willing to switch targets if their target is retreating and they are being attacked by something else\n" +
				"**-** Enemies are now more consistently drawn to the hero's position if they are attacked from out of visible range\n" +
				"**-** Internal adjustments to hunger and regeneration effects, they should now be more responsive when hunger or regen rate changes.\n" +
				"**-** Fixed Reclaim trap spell exploits, and allowed it to be dropped while charged\n" +
				"**-** Traps triggered by time freeze ending now always resolve after other effects/actions (e.g. teleportation, item pickup)\n" +
				"**-** Improved visual contrast for symbols on runestones",

				"**Characters:**\n" +
				"**-** Crazed bandits now have their own description\n" +
				"\n" +
				"**Effects:**\n" +
				"**-** Blast Wave no longer knocks back characters if they are killed over a pit\n" +
				"**-** Cloak of Shadows (and new Holy Tome) can no longer be transmuted.\n" +
				"\n" +
				"**Misc.:**\n" +
				"**-** Adjusted icons for Sucker Punch and Followup Strike\n" +
				"**-** Camera no longer re-centers on hero when adding custom notes\n" +
				"**-** Camera panning to enemies now respects the 'camera follow intensity' setting.\n" +
				"**-** Improved the game's monochrome Android icon\n" +
				"**-** Added developer commentary for v2.0.0\n" +
				"**-** Improved text clarity in a few cases\n" +
				"**-** Updated internal code libraries",

				"**v3.0.1:**\n" +
				"**-** Salt Cube's regen reduction now automatically disables itself during boss fights, the trinket doesn't need to be dropped.\n" +
				"**-** Waterskin drinking now more intelligently handles the extra healing from vial of blood, no longer consuming excess dew.\n" +
				"**-** Cursed wand pitfall effect no longer drops items if triggered via wondrous resin\n" +
				"**-** Feint armor ability no longer autotargets\n" +
				"**-** Removed support for runs in progress from prior to v2.3.2 (Jan. 2024)\n" +
				"**-** Removed internal code for old blacksmith quest from prior to v2.2 (Oct. 2023)\n" +
				"\n" +
				"**v3.0.2:**\n" +
				"**-** Music muting while game is in background on desktop now also applies to blacksmith's hammering sfx\n" +
				"**-** Improved number rounding logic when damage hero takes is affected by several modifiers at once\n" +
				"**-** Updated translations and translator credits"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed the following bugs:\n" +
				"**Highlights:**\n" +
				"**-** Various issues with system gestures registering as game inputs on Android and iOS\n" +
				"**-** Various cases where closing the game inside of the alchemy scene could result in lost progress\n" +
				"**-** Rare cases where multiple ascension wins could be recorded for one run\n" +
				"**-** Runestones working while hero is magic immune\n" +
				"**-** Unstable spell never triggering scroll effects that could apply either in or out of combat\n" +
				"**-** Exotic crystals trinket not applying to monster drops in many cases",

				"**Items:**\n" +
				"**-** Rare cases where bomb AOE could be influenced by nearby potions shattering\n" +
				"**-** Exploits involving juggling salt cube to get slower hunger with full regen\n" +
				"**-** Cursed armband not updating player gold display on desktop after stealing gold\n" +
				"**-** Very specific cases where disarming traps could make items unattainable\n" +
				"**-** Icecap incorrectly being blocked by high grass\n" +
				"**-** Stone of detect magic incorrectly being usable on thrown weapons\n" +
				"**-** Artifact uniqueness being affected by runs from prior game versions\n" +
				"**-** Crashes when aqua brew is dropped down chasms\n" +
				"**-** Rare errors when cancelling scroll of enchantment on armor\n" +
				"**-** Multiplicity glyph not working correctly with ghouls in some cases\n" +
				"**-** Corrosion gas from chaotic censer always starting at 1 damage",

				"**Effects:**\n" +
				"**-** Monk abilities which ignore armor using the incorrect damage icon\n" +
				"**-** Damage type not showing when hitting downed crystal guardians\n" +
				"**-** Very rare cases where spawned high grass could interfere with the floor 20 shop\n" +
				"**-** Certain effects not having on-death or rankings messages\n" +
				"**-** Specific cases where cursed wand effects would forget they were spawned by wondrous resin\n" +
				"**-** Duration of greater haste not being preserved on save/load\n" +
				"**-** Disarming traps not teleporting the hero's weapon in some cases where they should\n" +
				"**-** Cursed wand effects potentially applying levitation to immovable characters\n" +
				"**-** Geomancer rockfall attack being cleared on save/load\n" +
				"**-** Duelist's block ability not working properly with save/load",

				"**Misc.:**\n" +
				"**-** Rat King's description sometimes being incorrect in journal\n" +
				"**-** Pacifist badge unlocking when it shouldn't in rare cases\n" +
				"**-** Retreating characters failing to retreat through crowded area in some cases\n" +
				"**-** Various minor UI errors when holding down inventory buttons just before moving\n" +
				"**-** Rare cases where game win scene wouldn't trigger immediately\n" +
				"**-** Ripper demons sometimes losing their target early\n" +
				"**-** Various minor textual errors\n" +
				"**-** Various rare crash errors",

				"**v3.0.1:**\n" +
				"**-** Exploit involving unidentified crossbow and curse infusion\n" +
				"**-** Exploit involving placing wards on a necromancer's summoning position\n" +
				"**-** Shocking enchantment missing valid targets in some cases\n" +
				"**-** Rare cases where bees would refuse to attack near enemies vs. far ones\n" +
				"**-** quick-using an item from a bag not using that item's autotarget properties\n" +
				"**-** Alchemy guide items being greyed out in main menu\n" +
				"**-** Various rare crash bugs\n" +
				"**-** Various minor visual & textual errors",

				"**v3.0.2:**\n" +
				"**-** Game incorrectly thinking it is on a phone screen when played on Steam Deck\n" +
				"**-** Water of Awareness buff persisting between floors if the hero is very fast\n" +
				"**-** Various errors when saving/loading custom controller bindings\n" +
				"**-** Visual errors when Tengu's sprite animations are frozen (e.g. via paralysis)\n" +
				"**-** Characters still appearing as visible if knocked out of hero FOV in specific cases\n" +
				"**-** Talisman of Foresight's scry ability not detecting hiding mimics"));

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
				"**- Mossy Clump** now generates grassy floors 1/3 of the time and water floors 2/3 of the time, instead of 1/2 each. This will usually result in one fewer grassy floor over a whole run.\n" +
				"**- Mossy Clump** upgrade energy cost reduced to 10/15/20 from 20/25/30. This is now the same as other 'higher cost' trinkets."));

		changes.addButton(new ChangeButton(new TalentIcon(Talent.SHARED_UPGRADES), "Shared Upgrades Nerf",
				"Thrown weapons aren't an especially popular category of item to upgrade, and I would like to make more extensive changes to them in the future, but for the moment I'm making a targeted adjustment to the Shared Upgrades talent. The bonus damage it provided wasn't tied to the tier of thrown weapon used, which made T2 thrown weapons disproportionally powerful for the Sniper.\n" +
				"\n" +
				"**- Shared Upgrades** now grants 2.5%/5%/7.5% bonus damage per upgrade per tier, instead of a flat 10%/20%/30% bonus damage per upgrade. Functionally, this means -50% bonus dmg for T2 thrown weapons, -25% for T3, no changes for T4, and +25% for T5."));

	}

}
