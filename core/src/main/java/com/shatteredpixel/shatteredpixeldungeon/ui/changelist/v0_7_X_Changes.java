/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gauntlet;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tomahawk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PrismaticSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_7_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_7_4_Changes(changeInfos);
		add_v0_7_3_Changes(changeInfos);
		add_v0_7_2_Changes(changeInfos);
		add_v0_7_1_Changes(changeInfos);
		add_v0_7_0_Changes(changeInfos);
	}

	public static void add_v0_7_4_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.7.4", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released July 18th, 2019\n" +
				"_-_ 56 days after Shattered v0.7.3" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new WandOfWarding(),
				"This brand new wand spawns autonomous wards which attack enemies. Wards can be upgraded by being zapped again, and eventually form up into sentry turrets.\n\n" +
				"The Wand of Warding does very consistent damage, but requires some setup first."));

		changes.addButton( new ChangeButton(new WandOfLivingEarth(),
				"This new wand has a lower damage output, but grants significant defensive power. The rocks the wand shoots at enemies reform around the hero and absorb damage. If enough rock is built, it will form up into a rock guardian which fights with the player.\n\n" +
				"The Wand of Living Earth is lacking in offensive output, but does a great job of pulling focus and damage away from the player."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_BERKANAN), "Ally AI improvements",
				"Allies which follow the player are now considered to be 'intelligent', and have the following improved behaviours:\n" +
				"_-_ Intelligent allies will not attack enemies which are asleep, or which haven't noticed the player yet.\n" +
				"_-_ Intelligent allies will follow the hero through stairs so long as they are near to them.\n\n" +
				"Lastly, the hero can now swap places with any ally, even unintelligent ones."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Overhauled main menu interface to allow for more expandability.\n\n" +
				"_-_ Ring of elements and antimagic effects now apply to damage from wands.\n\n" +
				"_-_ Added a little surprise if you reach the surface with an upgraded ally item.\n\n" +
				"_-_ The great crab can now only block one enemy at a time.\n\n" +
				"_-_ Shattered Pixel Dungeon now requires Android 2.3+ to run, up from Android 2.2+.\n\n" +
				"_-_ Google Play Games and sharing gameplay data now requires android 4.1+, up from 4.0+."));

		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Shattered pots being lost if the player has a full inventory\n" +
				"_-_ Doors incorrectly closing when swapping places with an ally\n" +
				"_-_ Various rare bugs with heavy boomerangs\n" +
				"_-_ Various minor text errors"));

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"Updated Translations"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new DriedRose(),
				"The Dried Rose's ghost hero has received some buffs and adjustments to go along with other ally improvements:\n\n" +
				"_-_ The ghost hero can now be given instructions by using the rose after summoning them, and tapping on a location.\n\n" +
				"_-_ Ghost HP scaling increased to 8 per petal, from 4.\n" +
				"_-_ Ghost evasion reduced to 1x hero evasion from 2x.\n" +
				"_-_ Ghost now heals over time while they are summoned."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.NOISEMAKER, null), "Enhanced Bomb Buffs",
				"Many enchant bombs are performing poorly compared to some of the more popular ones, such as holy bombs and boss bombs. While I am toning down the strongest bombs a bit, I'm also making some pretty significant buffs to weaker bombs:\n\n" +
				"_-_ Frost bomb cost down to 2 from 3, now instantly freezes enemies caught in the blast in addition to chilling.\n" +
				"_-_ Woolly bomb cost down to 2 from 3, now does regular bomb damage in addition to spawning sheep.\n" +
				"_-_ Noisemaker now explodes when an enemy is attracted to its location.\n" +
				"_-_ Flashbang cost increased to 6 from 5, now deals regular bomb damage and debuffs in a smaller AOE.\n" +
				"_-_ Shock bomb cost increased to 6 from 5, now stuns/damages immediately instead of over time with electricity.\n" +
				"_-_ Regrowth bomb cost increased to 8 from 6, now heals significantly more and spawns more plants."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.LONGSWORD, new ItemSprite.Glowing(0xFF4400)), "Enchant/Glyph Buffs",
				"Continuing from the changes in 0.7.3, I'm still watching enchantment balance and making buffs where there's room to do so:\n\n" +
				"_-_ Blazing Enchantment bonus damage increased to 2/3 of burning damage, from 1-3.\n" +
				"_-_ Shocking Enchantment damage increased to 40% from 33%.\n" +
				"_-_ Blooming Enchantment chance for a second tile of grass increased to 10% per level, from 5%.\n" +
				"_-_ Lucky Enchantment proc chance scaling with levels increased by ~2x.\n" +
				"_-_ Corrupting Enchantment base proc chance increased to 15% from 10%, scaling reduced to compensate.\n\n" +
				"_-_ Glyph of Flow now grants a flat 2x speed boost in water, up from 1.5x + 0.1x per level."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WILD_ENERGY, null), "Misc Item Buffs",
				"_-_ Wild energy now gives 4 turns of charging instantly, and 8 turns over time. Up from 10 turns over time.\n\n" +
				"_-_ Stone of Clairvoyance radius increased to 12 from 8. This increases the area by ~2.25x.\n\n" +
				"_-_ Allies are now healed by magical sleep, just like the hero."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_MAIL, new ItemSprite.Glowing(0x88EEFF)), "Glyph Nerfs",
				"_-_ Glyph of Thorns bleed amount reduced to 4+lvl from 4+2*lvl, proc rate increased.\n\n" +
				"_-_ Glyph of Antimagic base damage reduction reduced to 0-4 from 2-4.\n\n" +
				"_-_ Glyph of Brimstone shield generation removed. The glyph now only protects the user from fire and does not also grant shielding when the user is aflame."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARCANE_BOMB, null), "Enhanced Bomb Nerfs",
				"_-_ Holy Bomb no longer blinds characters caught in the blast, recipe cost up to 8 from 6.\n\n" +
				"_-_ Arcane Bomb damage now falls off based on distance. Reduced to 100%/83%/67% from all 100%.\n\n" +
				"_-_ Shrapnel Bomb damage now slightly falls off based on distance. Damage is reduced by 5% per tile of distance."));

	}
	
	public static void add_v0_7_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.7.3", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 23rd, 2019\n" +
				"_-_ 66 days after Shattered v0.7.2" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.LONGSWORD, new ItemSprite.Glowing(0xFFFF00)), "Enchantment Changes",
				"Several changes have been made to enchantments, based on feedback from 0.7.2:\n\n" +
				"_-_ Precise and swift enchantments have been removed.\n\n" +
				"_-_ Lucky and blooming are now uncommon enchants, instead of rare and common.\n\n" +
				"_-_ Kinetic is a new common enchantment! This enchantment preserves excess damage when an enemy is killed and applies it to your next hit.\n\n" +
				"_-_ Corrupting is a new rare enchantment! When killing an enemy, there is a chance you will corrupt it instead."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.KUNAI, null), "New Thrown Weapons",
				"Four new thrown weapons have been added!\n\n" +
				"_-_ Throwing clubs are a tier-2 weapon with extra durability\n\n" +
				"_-_ Kunai are a tier-3 weapon with bonus damage on sneak attacks\n\n" +
				"_-_ Heavy boomerangs are a tier-4 weapon which returns after being thrown\n\n" +
				"_-_ Force cubes are a tier-5 weapon which damage enemies in a 3x3 area"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_ARCANE, null), "New Boss Recipes",
				"Two new recipes have been added, one which uses goo blobs and another which uses metal shards.\n\n" +
				"_-_ Elixir of arcane armor requires a goo blob and a potion of earthen armor. It grants a long-lasting resistance to magic.\n\n" +
				"_-_ Wild energy requires a metal shard and a scroll of mystical energy. It grants large amounts of recharging, but with some unpredictable effects attached!"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new Dart(),
				"Dart tipping has been removed from the alchemy system. Darts can instead be tipped right from the inventory.\n\n" +
				"Tipped darts have had their shop price reduced by 33%, and can now be cleaned if you don't wish to use the effect.\n\n" +
				"The alchemy guide has been adjusted due to the removal of dart tipping from alchemy. It now has 9 pages (down from 10), and the order of pages have been adjusted to put some simpler recipes earlier."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Shattered honeypots are now stackable, and can be sold for a small amount of gold.\n\n" +
				"_-_ The changes list has been split into three separate groups, so that the game's entire change history isn't loaded all at once.\n\n" +
				"_-_ Tengu now throws his shurikens one at a time, just like other ranged enemies. The speed of the shurikens has been increased to compensate, so that the player doesn't need to keep waiting while Tengu's attacks are in flight.\n\n" +
				"_-_ After the tengu boss battle, any extra items now drop in tengu's cell, instead of a random prison cell.\n\n" +
				"_-_ The hero will no longer step onto visible traps if that trap wasn't discovered when movement started.\n\n" +
				"_-_ When the mage's staff is cursed, the wand within the staff will now also be cursed.\n\n" +
				"_-_ Scrolls of transmutation can now be used on thrown weapons.\n\n" +
				"_-_ Improved the coloration of crystal keys. They should now be more distinct from iron keys."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Prismatic images causing errors when falling into pits\n" +
				"_-_ Secret rooms never spawning in the earlier parts of a region\n" +
				"_-_ Curse of multiplicity not working correctly on boss floors\n" +
				"_-_ Curse of multiplicity closing doors when it shouldn't\n" +
				"_-_ Ring of wealth rarely generating items which are blocked by challenges\n" +
				"_-_ Windows rarely appearing in places they shouldn't\n" +
				"_-_ Odd behaviour when the player is killed by electricity or a grim weapon\n" +
				"_-_ Explosions destroying armor with the warrior's seal on it\n" +
				"_-_ Various minor visual bugs\n" +
				"_-_ Various rare crash bugs"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"Updated Translations"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton( new Image(Assets.WARRIOR, 0, 90, 12, 15), "Berserker & Gladiator",
				"Because of nerfs I have made to the scaling of the warrior's shield regen, I have some power budget to give to his subclasses!\n\n" +
				"Berserker rate of rage loss decreased by 50%. It should now be easier to hold onto rage at higher health, but being injured will still help to retain it longer.\n\n" +
				"Gladiator is now significantly more flexible:\n" +
				"_-_ Using items no longer resets combo\n" +
				"_-_ Throwing weapons now increment combo\n" +
				"_-_ Slam ability now deals damage based on armor, instead of simply increasing damage."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CURSE_INFUSE, null), "Boss Recipe Buffs",
				"All recipes made with ingredients dropped by bosses have been buffed (except bombs):\n\n" +
				"_-_ Caustic brew now affects a 7x7 area, up from 5x5. Energy cost of caustic brew reduced to 4 from 8.\n\n" +
				"_-_ Elixir of aquatic rejuvenation now heals faster, and does not waste healing if the hero is not in water. Total amount of healing reduced to compensate.\n\n" +
				"_-_ Curse Infusion now grants a single upgrade to wands/weapons/armor in addition to cursing. This upgrade is lost if the item is uncursed.\n\n" +
				"_-_ Reclaim trap no longer grants recharging, now stores the trap instead. The trap can then be triggered anywhere the player likes."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_EMERALD, null), "Other Item Buffs",
				"_-_ Ring of elements now grants 20% resistance per level, up from 16%. However, ring of elements also no longer applies to melee attacks from magic-wielding enemies.\n\n" +
				"_-_ Throwing stone base damage increased to 2-5 from 1-5\n" +
				"_-_ Throwing stone durability increased to 5 from 3\n\n" +
				"_-_ Throwing hammer base damage increased to 10-20 from 8-20"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_SCALE, new ItemSprite.Glowing( 0x663300 )), "Enchant/Glyph Buffs",
				"_-_ Vampiric now has a chance to heal for large amounts, instead of always healing for small amounts.\n\n" +
				"_-_ Entanglement no longer roots, now only applies herbal armor buff. Amount of herbal armor granted reduced to compensate.\n\n" +
				"_-_ Affection charm duration up to 8-12 from 4-12. This means an affection proc now guarantees a free hit.\n\n" +
				"_-_ Potential no longer grants small amounts of partial charge on every hit, now has a chance to grant one full charge instead. Overall amount of charge given increased by ~20%."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new Tomahawk(),
				"The Tomahawk has been adjusted to make its damage more upfront, but also to reduce its extreme damage scaling with upgrades.\n\n" +
				"_-_ Tomahawk damage scaling increased to 2-4 per level, up from 2-2\n" +
				"_-_ Tomahawk bleed damage now starts at 60% of damage, down from 100%"));
		
		changes.addButton( new ChangeButton( new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Nerfs",
				"Warrior shielding regeneration scaling reduced. It is now a flat 1 shield every 30 turns. This is a very slight buff to the earlygame, and a significant nerf to the lategame.\n\n" +
				"I made this change as too much of the warrior's power was put into his base class, and into a passive ability that players tend to ignore. By removing this power, I can put more power into the warrior's subclasses, which should make the warrior feel more fun and interesting without significantly nerfing him overall."));
		
		changes.addButton( new ChangeButton( new Image(Assets.TERRAIN_FEATURES, 16, 0, 16, 16), "Trap Adjustments!",
				"Several traps have been slightly adjusted due to reclaim trap's new functionality:\n\n" +
				"_-_ Disintegration trap no longer deals damage based on target HP\n" +
				"_-_ Flock trap duration no longer scales with depth\n" +
				"_-_ Bosses now resist grim traps, Yog is immune\n" +
				"_-_ Pitfall traps do not work on boss floors\n" +
				"_-_ Reduced poison dart trap damage scaling\n" +
				"_-_ Rockfall traps trigger in a 5x5 AOE when cast from reclaim trap\n" +
				"_-_ Bosses will resist weakening traps"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_PLATE, new ItemSprite.Glowing( 0x660022 )), "Enchant/Glyph Nerfs",
				"_-_ Chilling now only stacks the chilled debuff up to 6 turns.\n\n" +
				"_-_ Thorns now bleeds enemies for a set amount based on armor level, instead of scaling with damage dealt.\n\n" +
				"_-_ Antimagic no longer affects the melee attacks of magic wielding enemies.\n" +
				"_-_ Antimagic no longer bases its blocking power on armor directly, now uses its own calculation which scales on level. This is a slight boost for lower tier armors and a nerf for higher tier ones."));
	}
	
	public static void add_v0_7_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.7.2", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released Mar 18th, 2019\n" +
				"_-_ 90 days after Shattered v0.7.1\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CATALYST, null), "Catalysts!",
				"Added two new recipes: _Alchemical Catalysts_ and _Arcane Catalysts._\n\n" +
				"These catalysts are made with any potion/scroll, and a seed/runestone. They replace many specific items for higher-cost recipes. Alchemy should be much more flexible now!\n\n" +
				"Additional Alchemy Changes:\n\n" +
				"When a recipe asks for any item of a certain type that item no longer has to be identified.\n\n" +
				"Alchemy guidebook pages now spawn more slowly at earlier stages of the game, and significantly faster at later stages of the game."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.LONGSWORD, new ItemSprite.Glowing(0x0000FF)), "Enchantment Overhaul!",
				"Enchantments have been significantly rebalanced to be less about direct damage and more about utility and situational power. Their design should now be more similar to glyphs.\n\n" +
				"Buffed Enchants: Chilling, Lucky.\n\n" +
				"Nerfed Enchants: Blazing, Shocking, Grim, Vampiric\n\n" +
				"Removed Enchants: Vorpal, Venomous, Dazzling, Eldritch, and Stunning.\n\n" +
				"New Enchants: Blocking, Blooming, Elastic (formerly a curse), Precise, and Swift.\n\n" +
				"New Curse: Polarized.\n\n" +
				"Some battlemage effects have been adjusted to accommodate these new enchantments. Most of these are very minor, except staff of regrowth, which now procs blooming."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_INFUSE, null), "Enchantment Adjustments",
				"_-_ Significantly adjusted when enchants/glyphs are lost when items are upgraded. Items are now always safe up to +4, then have a growing chance until +8 where enchantment loss is guaranteed.\n\n" +
				"_-_ Upgrades now have a set 33% chance to cleanse curses, instead of a chance which scales with level.\n\n" +
				"Magical Infusion spell adjusted:\n" +
				"_-_ Recipe changed to: upgrade + catalyst + 4 energy.\n" +
				"_-_ No longer applies an enchant/glyph, instead is guaranteed to preserve one while upgrading."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BREW_INFERNAL, null), "Combination Items",
				"The following combination items are no longer craftable, and are effectively removed from the game:\n" +
				"_-_ Wicked Brew\n" +
				"_-_ Frigid Brew\n" +
				"_-_ FrostFire Brew\n" +
				"_-_ Elixir of Restoration\n" +
				"_-_ Elixir of Vitality\n\n" +
				"These items offered no unique gameplay and existed purely to give a few cheap recipes. Thanks to catalysts filling that role, they no longer have a reason to exist. FrostFire Brew in particular may return in some form."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ The Identification system has been adjusted to require EXP gain in addition to item uses. " +
				"This change prevents exploits where an item could be used in unintended ways to rapidly ID it. " +
				"Items should ID at about the same rate if exp is gained while using them.\n\n" +
				"_-_ Increased the max level to gain exp from gnoll brutes and cave spinners by 1.\n\n" +
				"_-_ Sniper's mark now lasts for 2 turns, up from 1. This should make it easier to use with slow weapons, or while slowed.\n\n" +
				"Elixir of Might reworked:\n" +
				"_-_ Recipe changed to: strength + catalyst + 5 energy\n" +
				"_-_ Health boost now scales up with level, but fades after the hero gains a few levels\n\n" +
				"_-_ Meat Pie recipe cost reduced from 9 to 6, total healing reduced from 45 to 25\n\n" +
				"_-_ Added a privacy policy link to the Google Play edition of Shattered."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor visual bugs\n" +
				"_-_ Grim enchant activating when an enemy is already dead\n" +
				"_-_ Burning destroying scrolls when the hero is immune to it\n" +
				"_-_ Chasms killing enemies which are already dead in some cases\n" +
				"_-_ Thieves not correctly interacting with quickslotted items\n" +
				"_-_ Screen orientation not always being set when game starts\n" +
				"_-_ Flying characters pushing the ground after teleporting\n" +
				"_-_ Bombs rarely damaging tengu multiple times\n" +
				"_-_ Thrown weapons instantly breaking in rare cases\n" +
				"_-_ Dwarf King summoning skeletons while frozen\n" +
				"_-_ Incorrect behaviour when wands recharge very quickly\n" +
				"_-_ Thieves rarely escaping when they are close\n" +
				"_-_ Beacon of returning losing set location when scroll holder is picked up\n" +
				"_-_ Recycle not giving an item if inventory is full\n" +
				"_-_ Rare cases where the game wouldn't save during alchemy"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"Updated Translations\n\n" +
				"Updated Translator Credits"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Major internal improvements to service integrations for Google Play version of the game:\n" +
				"_-_ 'Share Gameplay Data' now uses Google Firebase Analytics instead of older Google Analytics. Data collected is unchanged.\n" +
				"_-_ Many internal improvements to Google Play Games sync and Google Payment integration.\n" +
				"_-_ Item renaming donation perk now applies to wands.\n\n" +
				"_-_ Added support for adaptive icons in Android 8.0+.\n" +
				"_-_ Improved how the game handles orientation changes and window resizing.\n" +
				"_-_ Shocking enchantment no longer visually arcs lightning to the hero."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (existed before 0.7.2):\n" +
				"_-_ Cloak of Shadows very rarely consuming more charges than it should\n" +
				"_-_ Assassin's blink not working on enemies standing on traps\n" +
				"_-_ Glyph of stone blocking an incorrect amount of damage (too low) in some cases\n" +
				"_-_ Hourglass not updating charges correctly in some cases\n" +
				"_-_ Blandfruit bush rarely appearing in 'on diet' challenge\n" +
				"_-_ Strength from ring of might not appearing in rankings\n" +
				"_-_ Multiplicity curse spawning rats on floor 5\n" +
				"_-_ Dried rose rarely being usable before completing ghost quest\n" +
				"_-_ Corrupted thieves being able to steal from the hero\n" +
				"_-_ Rare crashes involving rankings windows\n" +
				"_-_ Crashes and other odd behaviour when a berserking hero is affected by shielding buffs\n" +
				"_-_ Tengu spawning on top of other characters\n" +
				"_-_ Cloak of shadows only being usable from quickslots if it has 1 charge"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new WandOfTransfusion(),
				"Wand of Transfusion changed significantly when used on enemies:\n" +
				"_-_ No longer self-harms, now grants a mild self-shield instead\n" +
				"_-_ Charm duration no longer scales with level, damage to undead enemies reduced"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AMETHYST, null), Messages.get(RingOfWealth.class, "name"),
				"Ring of Wealth significantly buffed:\n" +
				"_-_ Special item drops now happen ~50% more often\n" +
				"_-_ The ring of wealth now awards a greater variety of items from special drops\n" +
				"_-_ special wealth drops have a 1/10 chance to award a high value item\n" +
				"_-_ Wraiths and minion enemies no longer have a chance to generate wealth items"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SPEAR, new ItemSprite.Glowing(0x00FFFF)), "Buffed Enchants",
				"_-_ Chilling now stacks with itself over multiple procs\n\n" +
				"_-_ Lucky buffed/reworked. No longer affects damage, now generates bonus items when enemies are killed with a lucky weapon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_SWIFTTHISTLE, null), "Item Balance Adjustments",
				"Several seeds and stones have been buffed:\n" +
				"_-_ Player can now move without cancelling swiftthistle's effect\n" +
				"_-_ Duration of poison from sorrowmoss increased by ~33%\n" +
				"_-_ Increased the strength of warden's earthroot effect\n" +
				"_-_ Stone of clairvoyance no longer disarms traps, now goes through walls instead\n" +
				"_-_ Stone of detect curse is reworked, now stone of disarming. Disarms up to 9 traps where it is thrown.\n" +
				"_-_ Stone of aggression now forces enemies to attack a target. Duration is longer if thrown at allies.\n\n" +
				"_-_ Scroll of teleportation now teleports the player to the entrance of secret/special rooms instead of into them\n\n" +
				"_-_ Blessed ankhs now cure the same debuffs as a potions of healing\n\n" +
				"Fire and toxic gas have been adjusted to deal damage based on dungeon depth, and not target max health. " +
				"This means more damage versus regular enemies, and less versus bosses. " +
				"Several bosses have lost their resistances to these effects as a result of this change."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.DIRK, new ItemSprite.Glowing(0xFF4400)), "Nerfed Enchants",
				"_-_ Blazing no longer deals direct damage, now instead is more likely to set enemies on fire.\n\n" +
				"_-_ Shocking no longer deals damage to enemy being attacked, deals more damage to surrounding enemies.\n\n" +
				"_-_ Vampiric now grants less health when hero is at higher HP.\n\n" +
				"_-_ Grim is now more likely to 'finish off' an enemy, but is less likely to activate at higher enemy health."));
	}
	
	public static void add_v0_7_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.7.1", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released Dec 18th, 2018\n" +
				"_-_ 61 days after Shattered v0.7.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton( new Image(Assets.HUNTRESS, 0, 15, 12, 15), "Huntress Reworked!",
				"The Huntress has received a class overhaul!\n\n" +
				"Her boomerang has been replaced with a bow. The bow has infinite uses, like the boomerang, but cannot be upgraded directly, instead it will grow stronger as the huntress levels up.\n\n" +
				"Her knuckledusters have been replaced with studded gloves. This change is purely cosmetic.\n\n" +
				"Those with runs in progress will have their boomerang turn into a bow, and will regain most of the scrolls of upgrade spent on the boomerang.\n\n" +
				"The huntress can now also move through grass without trampling it (she 'furrows' it instead)."));
		
		changes.addButton( new ChangeButton( new Image(Assets.HUNTRESS, 0, 90, 12, 15), "Huntress Subclasses Reworked!",
				"Huntress subclasses have also received overhauls:\n\n" +
				"The Sniper can now see 50% further, penetrates armor with ranged attacks, and can perform a special attack with her bow.\n\n" +
				"The Warden can now see through grass and gains a variety of bonuses to plant interaction."));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.TRIDENT, null), "Thrown Weapon Improvements",
				"Thrown weapons now show their tier, ranging from 1-5 like with melee weapons.\n\n" +
				"All Heroes now benefit from excess strength on thrown weapons.\n\n" +
				"Thrown weapons now get +50% accuracy when used at range.\n\n" +
				"Thrown weapons can now be upgraded!\n" +
				"_-_ Upgrades work on a single thrown weapon\n" +
				"_-_ Increases damage based on tier\n" +
				"_-_ Gives 3x durability each upgrade\n" +
				"_-_ Weapons with 100+ uses now last forever\n" +
				"_-_ Darts are not upgradeable, but tipped darts can get extra durability\n\n" +
				"Ring of sharpshooting has been slightly adjusted to tie into this new upgrade system."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.UNLOCK_MAGE.image), "Hero Class changes",
				"All heroes except the warrior now need to be unlocked via new badges. The requirements are quite simple, with the goal of giving new players some early goals. Players who have already unlocked characters will not need to re-unlock them.\n\n" +
				"To help accelerate item identification for alchemy, all heroes now start with 3 identified items: The scroll of identify, a potion, and another scroll."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Added a partial turn indicator to the game interface, which occupies the same spot as the busy icon. This should make it much easier to plan actions that take more or less than 1 turn.\n\n" +
				"Rings now have better descriptions for their stats! All rings now show exactly how they affect you in a similar way to how other equipment gives direct stats.\n\n" +
				"Precise descriptions have been added for weapons which block damage.\n\n" +
				"Added item stats to the item catalog.\n\n" +
				"Dropping an item now takes 1 turn, up from 0.5 turns."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ various crash bugs\n" +
				"_-_ various minor visual bugs\n" +
				"_-_ recycle being able to produce health potions with pharmacophobia enabled\n" +
				"_-_ magical porter soft-locking the game in rare cases\n" +
				"_-_ mystical energy recharging some artifacts incorrectly\n" +
				"_-_ health potion limits not applying to prison guards\n" +
				"_-_ traps with ground-based effects affecting flying characters\n" +
				"_-_ odd behaviour when transmuting certain items\n" +
				"_-_ keys rarely spawning without chests\n" +
				"_-_ fireblast rarely damaging things it shouldn't\n" +
				"_-_ dew drops from dew catchers landing on stairs\n" +
				"_-_ ankh revive window rarely closing when it shouldn't\n" +
				"_-_ flock and summoning traps creating harsh sound effects\n" +
				"_-_ thrown weapons being lost when used on sheep\n" +
				"_-_ various specific errors when actions took more than 1 turn\n" +
				"_-_ various freeze bugs caused by Tengu"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"Updated translations\n\n" +
				"Updated translator credits"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton( new Image(Assets.ROGUE, 0, 15, 12, 15), "Hero Buffs",
				"_-_ Rogue's cloak of shadows base charge speed increased by ~11%, scaling reduced to compensate.\n\n" +
				"_-_ Warlock's soul mark base chance increased to 15% from 10%, scaling reduced to compensate.\n\n" +
				"_-_ Warlock's soul mark hunger restoration increased by 100%, health restoration increased by 33%."));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_TOPAZ, null), "Various Item Buffs",
				"_-_ Ring of energy simplified/buffed. Now grants a flat +25% charge speed per level, instead of +1 effective missing charge per level\n\n" +
				"_-_ Ring of elements power increased to 16% from 12.5%\n\n" +
				"_-_ Ring of wealth 'luck' bonus increased to 20% from 15%\n\n" +
				"_-_ Bolas base damage increased to 6-9 from 4-6\n\n" +
				"_-_ Wand of regrowth now spawns furrowed grass when it begins to run out of energy due to excessive use, instead of short grass.\n\n" +
				"Wand of fireblast buffed:\n" +
				"_-_ shot distance at 3 charges reduced by 1\n" +
				"_-_ damage at 1 charge reduced slightly\n" +
				"_-_ damage at 2/3 charges increased by ~15%"));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.ARMOR_LEATHER, new ItemSprite.Glowing(0x222222)), "Other Buffs",
				"_-_ vorpal enchant bleed reduced by 20%\n\n" +
				"_-_ glyph of potential wand charge bonus increased by 20%\n\n" +
				"_-_ glyph of stone evasion conversion efficiency increased to 75% from 60%"));
		
		changes.addButton( new ChangeButton(new Image(Assets.KING, 1, 0, 14, 16), "Dwarf King",
				"While I would like to make more extensive changes to Dwarf King in the future, I've made a couple smaller tweaks for now to make him harder to cheese:\n\n" +
				"_-_ Dwarf King is now able to summon skeletons even if he cannot see the hero\n" +
				"_-_ Dwarf King is now resistant to fire and toxic gas"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton( new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Nerfs",
				"_-_ Warrior's shielding regen scaling reduced by ~15%. This is primarily a lategame nerf."));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_RUBY, null), "Ring Nerfs",
				"Ring of furor has been nerfed/simplified:\n" +
				"_-_ Now provides a flat +10.5% attack speed per level, instead of speed which scales based on how slow the weapon is.\n\n" +
				"This means the ring is effectively nerfed for slow weapons and regular weapons, and slightly buffed for fast weapons. A +6 ring grants almost exactly doubled attack speed.\n\n\n" +
				"The ring of force's equipped weapon bonus was always meant as a small boost so it wasn't useless if the player already had a better weapon. It wasn't intended to be used to both replace melee and then boost thrown weapons.\n" +
				"_-_ The ring of force no longer gives bonus damage to thrown weapons."));
		
		changes.addButton( new ChangeButton( new Gauntlet(),
				"As furor now works much better with fast weapons, I've taken the opportunity to very slightly nerf sai and gauntlets\n\n" +
				"_-_ Sai blocking down to 0-2 from 0-3\n" +
				"_-_ Gauntlet blocking down to 0-4 from 0-5"));
		
		changes.addButton( new ChangeButton( new Shuriken(),
				"Shuriken have been adjusted due to the new upgrade system:\n\n" +
				"_-_ Base damage increased to 4-8 from 4-6\n" +
				"_-_ Durability reduced to 5 from 10"));
	}
	
	public static void add_v0_7_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.7.0", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released Oct 18th, 2018\n" +
				"_-_ 501 days after Shattered v0.6.0\n" +
				"_-_ 168 days after Shattered v0.6.5\n" +
				"\n" +
				"Dev commentary will be added here in the future."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TILES_SEWERS, 48, 96, 16, 16 ), "Alchemy Overhaul!",
				"The game's alchemy system has been entirely overhauled!\n\n" +
				"Alchemy is now a full consumable crafting system which lets you create all kinds of new items.\n\n" +
				"There is also a new resource: alchemical energy. Every alchemy pot has some energy within it. Some recipes require this energy, so make sure to use it wisely!\n\n" +
				"All of this is explained in a new guidebook specifically for alchemy. Pages of it can be found in alchemy rooms. Existing players will be given some pages automatically to get started."));
		
		changes.addButton( new ChangeButton(new AlchemistsToolkit(),
				"The Alchemist's Toolkit returns!\n\n" +
				"The toolkit can be found like any other artifact, and acts as a sort of horn of plenty for the new alchemical energy resource."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 32, 112, 16, 16), "New Consumables",
				"Added a new scroll, potion, and plant!\n\n" +
				"_-_ Scroll of transmutation is a rare scroll which allows the user to change an item into another one of the same type. Note that it cannot be used to make scrolls of magical infusion.\n\n" +
				"_-_ Potion of haste is an uncommon potion which grants a temporary burst of speed.\n\n" +
				"_-_ Swiftthistle is the plant counterpart to potions of haste. Both the plant and tipped dart give various speed or time-based buffs."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_BLINK, null), "Runestones",
				"Added 10 new runestones, and runestone crafting!\n\n" +
				"Two or three runestones can be crafted by using a scroll with an alchemy pot.\n\n" +
				"Runestones give various effects that are similar in theme to their scroll counterpart.\n\n" +
				"Runestones also naturally appear in alchemy rooms, and a new special room type."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_AMBER, null), "Exotic Potions",
				"Added 12 new potions which can be created through alchemy!\n\n" +
				"Mix a potion and any two seeds to create an exotic potion with unique effects.\n\n" +
				"Exotic Potions are only available through alchemy, or by transmuting a regular potion."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_ISAZ, null), "Exotic Scrolls",
				"Added 12 new scrolls which can be created through alchemy!\n\n" +
				"Mix a scroll and any two runestones to create an exotic scroll with unique effects.\n\n" +
				"Exotic Scrolls are only available through alchemy, or by transmuting a regular scroll."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RETURN_BEACON, null), "New Recipes!",
				"Added ~40 other items which can be created through alchemy!\n\n" +
				"Most of these recipes require alchemical energy, and information about them can be found within alchemy guidebook pages in the prison and deeper in the dungeon.\n\n" +
				"All of these items are only available through alchemy."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ARMBAND, null), "Spawn Rate Changes",
				"_-_ Master Thieves' Armband is now a regularly dropping artifact.\n" +
				"_-_ Thieves now rarely drop a random ring or artifact instead of the armband.\n\n" +
				"_-_ Blandfruit seeds and wells of transmutation have been removed.\n" +
				"_-_ Potion of Might and Scroll of Magical infusion are now produced through alchemy.\n" +
				"_-_ Transmuting potions/scrolls now gives their exotic variant, and vice-versa.\n\n" +
				"_-_ One runestone of enchantment and one runestone of intution are guaranteed per run.\n" +
				"_-_ Potion and scroll drops are now slightly more varied.\n" +
				"_-_ Reduced the droprate of bombs.\n\n" +
				"_-_ Adjusted enchant/glyph probabilities slightly. rare ones should be slightly more common.\n\n" +
				"_-_ There is now a guaranteed alchemy room every chapter."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_BEACON, null), "Boss reward changes",
				"Boss rewards have been significantly adjusted:\n\n" +
				"_-_ Lloyd's beacon and Cape of Thorns no longer drop, they are effectively removed from the game.\n\n" +
				"_-_ Goo and DM-300 now drop unique alchemy ingredients instead.\n\n" +
				"_-_ Lloyd's beacon has been replaced by alchemy recipes, Cape of Thorns will likely return in some form in the future."));
		
		changes.addButton( new ChangeButton(new Blandfruit(),
				"Blandfruit has been changed to be more consistent with potions.\n\n" +
				"All blandfruit types now exactly mimic their potion counterparts, there are now no blandfruit-exclusive effects.\n\n" +
				"When a thrown blandfruit shatters, it will now leave behind blandfruit chunks, which can be eaten. This allows offensive blandfruits to be used without losing their food value.\n\n" +
				"The previous unique mechanics of earthfruit, sorrowfruit, and firefruit have been recycled into the new alchemy system."));
		
		changes.addButton( new ChangeButton(new UnstableSpellbook(),
				"The unstable spellbook has received a mini-rework to go along with the new exotic scrolls.\n\n" +
				"_-_ Previous enhanced scroll mechanic removed.\n\n" +
				"_-_ Feeding a scroll to the spellbook now allows you to use either that scroll, or its exotic equivalent.\n\n" +
				"_-_ Using the exotic variant of a scroll costs 2 charges instead of 1.\n\n" +
				"_-_ Charge speed at low levels increased. Max charges increased to 8 from 6."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Potions which should be thrown can now be thrown from the quickslot, if they are IDed.\n" +
				"_-_ Thrown items and wand zaps now go through tall grass.\n" +
				"_-_ Expanded what items bags can carry. Most alchemy produce can fit in a bag, magical holster now holds bombs.\n\n" +
				"_-_ Caustic ooze now lasts a max of 20 turns.\n" +
				"_-_ Bleeding damage is now more consistent.\n\n" +
				"_-_ Adjusted the text for breaking paralysis.\n"+
				"_-_ Adjusted various potion/plant/seed sprites.\n" +
				"_-_ Healing now has an icon and description.\n" +
				"_-_ Improved the layering system for raised terrain like grass.\n" +
				"_-_ Added an ingame version indicator.\n" +
				"_-_ Added a new indicator for when an item is not identified, but known to be uncursed.\n\n" +
				"_-_ Improved payment & sync functions on Google Play version.\n\n" +
				"_-_ Adjusted bone pile functionality to make it more clear that a spawning wraith means an item is cursed."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various rare crash and freeze bugs\n" +
				"_-_ Various audio and visual bugs\n" +
				"_-_ Sad Ghost attacking nonexistent enemies\n" +
				"_-_ Various rare cases where item windows could stack\n" +
				"_-_ Cases where projectiles would disappear\n" +
				"_-_ Multiplicity curse duplicating projectiles\n" +
				"_-_ Lucky enchant not correctly scaling with upgrades\n" +
				"_-_ Various effects incorrectly working on dead characters\n" +
				"_-_ Wands never appearing in heroes remains\n" +
				"_-_ Remains rarely appearing inside bookcases on floor 20\n" +
				"_-_ Wand of corruption doing nothing to corrupted enemies\n" +
				"_-_ Augmented weapons rarely having inconsistent speed\n" +
				"_-_ Scroll of upgrade revealing curses on unidentified items\n" +
				"_-_ Item curses rarely not being revealed when they should be\n" +
				"_-_ Assassin buffs not being cleared when they should in some cases\n" +
				"_-_ Rooting not working correctly on retreating enemies\n" +
				"_-_ Searching spending hunger in a locked level\n" +
				"_-_ 'Faith is my armor' deleting class armors\n" +
				"_-_ Various cases where the player can be ontop of enemies"));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various screen layout issues in power saver mode\n" +
				"_-_ Crashes when tengu is healed above 1/2 health\n" +
				"_-_ Bolas incorrectly requiring 15 strength\n" +
				"_-_ Non-heroes being able to use reach weapons through walls\n" +
				"_-_ Antimagic glyph applying to more effects when used by the sad ghost\n" +
				"_-_ Some items not being known as uncursed when sold from shops\n" +
				"_-_ Obfuscation glyph not improving every upgrade\n" +
				"_-_ Magical sleep rarely cancelling paralysis\n" +
				"_-_ Exploits where bone piles could be used to check if an item was cursed"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"Updated Translations\n\nUpdated translator credits\n\nAdded new language: Basque!"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new WandOfTransfusion(),
				"Wand of transfusion has been rebalanced, with an emphasis on making it much more useful in conjunction with weaker allies:\n\n" +
				"_-_ Using the wand still costs 10% max hp\n\n" +
				"_-_ Ally healing adjusted to 10% of user max HP + a flat 3 per level, from 30% + 3%/lvl missing hp\n\n" +
				"_-_ Ally healing can now overheal up to whatever the max healing per shot is\n\n" +
				"_-_ Undead damage is is now the same as ally healing, from 30% + 5%/lvl max hp\n\n" +
				"_-_ Charming is now more powerful at higher wand levels\n\n" +
				"_-_ All other transfusion functionality has been removed"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_KAUNAN, null), new ScrollOfTeleportation().trueName(),
				"The scroll of teleportation has been buffed. It now prioritizes sending the user to rooms they have not seen yet, and can teleport to secret rooms."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_ODAL, null), new ScrollOfMirrorImage().trueName(),
				"Scroll of mirror image has been adjusted to have more interactions with other items, but to also be less powerful at base:\n\n" +
				"_-_ Scroll now spawns 2 images, down from 3\n\n" +
				"_-_ Mirror images now attack with the hero's weapon, at 50% damage\n\n" +
				"_-_ Images no longer fade after a successful attack, instead they pull enemy aggro\n\n" +
				"_-_ Images start out invisible, have 1 hp, no blocking power, but do inherit some of the hero's evasion."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_NAUDIZ, null), new ScrollOfTerror().trueName(),
				"Terror now has it's duration reduced by 5 whenever damage is taken, rather than being removed entirely. Scroll of terror duration has been increased to 20 from 10.\n\n" +
				"Charm now has it's duration reduced by 5 whenever damage is taken, rather than not losing any duration. Succubi have been given a life-drain ability in compensation, and various charming effects have had their durations adjusted."));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new WandOfRegrowth(),
				"Wand of regrowth will now cease producing plants if it is overused. Charges spent before it begins degrading will increase if the wand is upgraded. At +12 the wand will function infinitely.\n\n" +
				"This change is made to combat farming with low-levelled wands of regrowth. Especially with the alchemy changes this would be far too powerful. Infinite farming is still possible, but requires upgrades."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GYFU, null), new ScrollOfRetribution().trueName(),
				"The scroll of psionic blast is now known as the scroll of retribution:\n" +
				"_-_ removed damage and stun penalty, now self-weakens instead\n" +
				"_-_ now blinds enemies as well as the player\n" +
				"_-_ damage dealt now scales with missing player HP. At very low HP scroll is still an instakill on most enemies\n\n" +
				"Scroll of psionic blast still exists however. It is now an exotic scroll!"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), new PotionOfHealing().trueName(),
				"_-_ Speed of healing effects (e.g. potion of healing) have been reduced slightly. Overall heal amounts unchanged."));
		
		changes.addButton( new ChangeButton(new Honeypot(),
				"Bees were never intended to be used as a boss-killing tool by stacking many of them onto one area. This use has now been restricted:\n" +
				"_-_ Bees are now hostile to eachother\n\n" +
				"Note that the new alchemy system may have a recipe which helps calm angry bees down..."));
	}
}
