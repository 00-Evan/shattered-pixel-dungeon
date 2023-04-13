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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
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

public class v2_X_Changes {

	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_Coming_Soon(changeInfos);
		add_v2_0_Changes(changeInfos);
	}

	public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
		changes.hardlight(0xCCCCCC);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Overview",
				"My current plan is for the next major update to be v2.1.0, which will mostly be focused on responding to any balance or general gameplay issues that appear as dust settles from the Duelist's release.\n\n" +
				"I don't yet have a specific timeline for v2.1.0, but I think it's a safe bet that you'll hear from me about it sometime in late April or May. Please keep in mind that while I always try to keep to the ETAs I provide, they are just estimates. If you don't hear from me by the ETA, it means I'm still busy with the update!"));

		changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "Hero Changes",
				"The largest changes in v2.1.0 will be focused around the game's heroes, with a big emphasis on the Duelist. I'm generally happy with how she performed during the beta, but such a large addition to the game is definitely going to need some refinement over time. I may also make some smaller changes to the other heroes as well, depending on how things shake out. "));

		changes.addButton( new ChangeButton(Icons.get(Icons.TALENT), "Side Content",
				"v2.1.0 will also include some amount of side content, but it will probably be fairly small. I plan to add a couple new exotic enemies, and there might be room for one or two other little things as well."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Smaller Fixes",
				"There will also be more miscellaneous changes and bugfixes! Despite all of the fixes I've made in v2.0.0, my list of things to do has actually grown! I'd like to spend a bit of time keeping that list in check."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE), "Blacksmith Quest?",
				"And no I haven't forgotten about the blacksmith's quest! I will likely do some concepting or internal technical work for it during v2.1.0's development, but the quest rework itself is more likely to be part of v2.2.0."));

	}

	public static void add_v2_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v2.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes = new ChangeInfo("v2.0.2", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Overhauled visuals for the action indicator. It now has different background colors for different actions, and supports a smaller secondary icon or text. Subclass abilities all make use of this new functionality, and some info has been moved from buff icons to this new indicator.\n\n" +
				"_-_ The game's pixel font now supports Vietnamese!\n\n" +
				"_-_ Rankings now attempt to show some basic information if loading full game data fails.\n\n" +
				"_-_ The changes scene now shows a warning if the user is not viewing it in English.\n\n" +
				"_-_ Liquid metal value increased to 1 from 0.5."));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (Caused by v2.0):\n" +
				"_-_ Various minor visual errors\n" +
				"_-_ Flail spin ability triggering talents when already at full spin power\n" +
				"_-_ Lucky elemental strike working on ally characters\n" +
				"_-_ Resistance from empowered meditate lasting 1 turn longer than intended\n" +
				"_-_ Threshold for Monastic Vigor being lower than intended (95/75/55 instead of 100/80/60)\n" +
				"_-_ New quickslot swap behaviour sometimes placing items without a quick-use action into quickslots\n" +
				"_-_ Levitation persisting for 1 extra turn after the hero falls into a pit\n" +
				"_-_ Lunge ability still allowing the Duelist to attack if it placed her onto a teleportation trap\n" +
				"_-_ Spirit bow arrows triggering the deadly followup talent",
				"Fixed (existed prior to v2.0):\n" +
				"_-_ Various bugs with controller input which occurred when two or more inputs were entered at once.\n" +
				"_-_ Hero being able to leave floor 5 after it locks in rare cases\n" +
				"_-_ Various minor rounding errors in alchemy produce values\n" +
				"_-_ Spirit bow encumbrance calculations always using a strength requirement of 10\n" +
				"_-_ Downed ghoul visuals sometimes staying visible in the fog of war"));

		changes = new ChangeInfo("v2.0.1", false, null);
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 6), "Duelist Buffs",
				"I expect to make some more thorough changes in v2.1, after gameplay data becomes more reliable, but for now I'm making some early buffs to the Duelist to improve on a bunch of her talents and weapon abilities that are not performing as well as others. This should hopefully make her gameplay feel more consistent.\n\n" +
				"_-_ Weapon ability recharge speed increased by 12.5% at no missing charges, down to being unchanged when at 0/10\n" +
				"\n" +
				"_- Strengthening Meal_ bonus damage up to 3 from 2\n" +
				"_- Swift Equip_ cooldown down to 20 turns from 30\n" +
				"_- Unencumbered Spirit_ energy boost up to 33/67/100/150%, from 25/50/100/150%\n" +
				"_- Monastic Vigor_ threshold changed to 100/80/60%, from 100/85/70%\n" +
				"\n" +
				"_- Sneak_ now grants 8/6/4 turns of stealth, up from 6/5/4\n" +
				"_- Spike_ now deals +40/30% bonus damage, up from +15/10%\n" +
				"_- Heavy Blow_ now deals +65/60/55/50% damage, up from +50/45/40/35%\n" +
				"_- Combo Strike_ now deals +40/35/30% damage per stack, up from +30/25/20%\n" +
				"_- Spin_ now deals +33% damage per stack, up from +20%"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Quickslot contents are now automatically swapped if a newly equipped item that is not quickslotted replaces an item that was quickslotted. This should make weapon swapping gameplay smoother.\n" +
				"\n" +
				"_-_ The changes screen now supports more text for a single entry. On mobile UI the changes window can now have multiple tabs, on full UI the changes pane on the right is now scrollable.\n" +
				"\n" +
				"_-_ Updated translations\n" +
				"_-_ Attacking an enemy now properly sets them as the auto-targeting target in all cases\n" +
				"_-_ Levitation now prevents damage from floor electricity during the DM-300 fight\n" +
				"_-_ Removed support for save data prior to v1.2.3"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (Caused by v2.0):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Duelist not being automatically unlocked for existing players with a win\n" +
				"_-_ Blooming elemental strike placing tall grass on plants\n" +
				"_-_ Various minor textual errors\n" +
				"_-_ Enemies frozen by Challenge ability still being fearable\n" +
				"_-_ Charged shot ability rarely messing with the game's UI when its animation ends\n" +
				"_-_ Combined energy talent rarely boosting energy above 100%\n" +
				"_-_ Brawler's stance not preventing damage blocking from equipped weapon\n" +
				"_-_ Duelist Unlock badge not registering on Google Play Games\n" +
				"_-_ Champion weapon swapping not triggering an attack indicator update",
				"Fixed (existed prior to v2.0):\n" +
				"_-_ Various UI bugs caused by pressing multiple buttons simultaneously\n" +
				"_-_ Noisemakers being visually defusable after trigger but not exploding\n" +
				"_-_ Noisemakers being collectable in some cases after triggering\n" +
				"_-_ Damage/Stun from blastwave knockback applying to downed ghouls\n" +
				"_-_ Even more cases of particle effects sometimes failing to appear\n" +
				"_-_ Projecting champions with ranged attacks refusing to melee from a distance in some cases\n" +
				"_-_ Life Link sometimes persisting for longer than intended during Dwarf King fight" +
				"_-_ Various rare UI bug"));

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
				"_-_ Added a new Language: _Vietnamese!_ Currently only system font is supported for Vietnamese.\n" +
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
				"_-_ Updated various code libraries\n"));

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
				"_-_ No error message being given when the mage uses elemental blast without a staff"));

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
