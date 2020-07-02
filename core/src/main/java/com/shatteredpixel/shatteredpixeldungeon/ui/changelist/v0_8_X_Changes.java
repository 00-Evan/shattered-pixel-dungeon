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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM100Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class v0_8_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_8_1_Changes(changeInfos);
		add_v0_8_0_Changes(changeInfos);
	}

	public static void add_v0_8_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.8.1", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo( "", false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo( "v0.8.1a", false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "Sound Effect Adjustments",
				"_-_ The volume of debuffing, item collection, and dewdrop collection sounds has been adjusted.\n\n" +
				"_-_ The grass trampling sound no longer layers ontop of the regular grass stepping sound.\n\n" +
				"_-_ Adjusted the hitsounds for arrows and darts.\n" +
				"_-_ Added pitch variance to the attack sounds for the bow and crossbow.\n\n" +
				"_-_ Adjusted the warning sounds that play when the hero is injured.\n\n" +
				"_-_ Adjusted the surprise attack sound to play better on phone speakers."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_- The Wand of Frost_ can now stack chill debuff, instead of just refreshing it. This should make the battlemage's freeze affect more accessible after the nerf, but multiple charges may be needed.\n\n" +
				"_-_ Updated translations."));

		changes.addButton( new ChangeButton(new Image( Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by v0.8.1):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Warlocks not playing their debuff sound if the hero is already debuffed\n" +
				"_-_ Various small logic errors with ring of wealth\n" +
				"_-_ Wand of warding incorrectly refusing to place a new ward with 1 energy left\n\n" +
				"Fixed (existed prior to v0.8.1):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Various minor visual errors\n" +
				"_-_ Corpse dust wraiths rarely spawning inside doors"));

		changes = new ChangeInfo( Messages.get( ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 27th, 2020\n" +
				"_-_ 72 days after Shattered v0.8.0\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "New Hero Select!",
				"The game's hero select screen has been completely overhauled, and how shows off the heroes in more detail than ever before.\n\n" +
				"The new hero select features a minimal UI that retains all the old functionality while trying to keep out of the way as much as possible. The centerpiece of hero select is now the heroes themselves, who are depicted with _new detailed splash arts_ by Aleksandar Komitov! _Make sure check out their credits listing in the new about page!_"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "Sound Effects",
				"_15+ new sound effects_ have been added to the game, courtesy of Charlie! _Make sure check out their credits listing in the new about page!_\n\n" +
				"These sounds cover movement, combat, and a bunch of miscellanious situations:\n" +
				"_-_ Grass now crunches underfoot (extra loud if it's tall grass), and solid floors now have a more solid sound.\n" +
				"_-_ The default hitsound has been remastered, and weapons can now produce slashing, stabbing, or crushing sounds.\n" +
				"_-_ Bows, crossbows, and projectile wands have new shooting/hitting sounds.\n" +
				"_-_ A new heavy impact sound plays when you land a surprise attack, and a new blocking sound when damage is negated by parrying or shields.\n" +
				"_-_ Some misc sounds have also been added for: gas spewing, chains being thrown, magical effects charging up, and the player being hit to low health.\n\n" +
				"I've also remastered the title and ending music tracks to improve their quality and volume."));

		changes.addButton( new ChangeButton(new Image(Assets.Interfaces.BUFFS_LARGE, 0, 0, 16, 16), "Item and Buff Icons",
				"_Buff icons now have a new fading behaviour_ that much more accurately communicates how much of their duration is left. Several duplicated buff icons have also been recolored so they are distinct.\n\n" +
				"_Item icons have been added to rings!_ To accommodate this, item icons now appear in the top-right of an item's inventory slot. Several existing item icons have also been improved."));

		changes.addButton( new ChangeButton(new TalismanOfForesight(),
				"The _Talisman of Foresight_ has received a rework to make its active ability more useful and interactive!\n\n" +
				"A few changes have been made to passive trap detection:\n" +
				"_-_ Talisman no longer gains EXP from the hero finding secrets\n" +
				"_-_ 'uneasy' status no longer lingers when traps aren't in range\n" +
				"_-_ Passive charge speed at max level reduced by 33%\n\n" +
				"The talisman's scry active ability has been redesigned:\n" +
				"_-_ Scry now projects outward in a cone, in a direction and distance of the player's choice.\n" +
				"_-_ Everything within scry's range is revealed, including the map, secrets, and enemies/items.\n" +
				"_-_ The player gets temporary mind vision on revealed enemies/items.\n" +
				"_-_ The talisman gains exp based on what it reveals. Secrets are worth the most EXP.\n" +
				"_-_ As the talisman levels, max scry distance and mind vision duration increase."));

		changes.addButton( new ChangeButton(new WandOfRegrowth(),
				"The _Wand of Regrowth_ has been reworked, with the goal of making it useful for more than just farming:\n\n" +
				"_-_ Initial charges increased to 2 from 1.\n" +
				"_-_ Regrowth now attacks in the same pattern as fireblast, no longer consumes all charges at once.\n" +
				"_-_ Rebalanced rooting time, AOE size, farming cap, grass and plant spawning frequency, and upgrade scaling.\n" +
				"_-_ Added a new unique plant that is only summoned when spending 3 charges of regrowth.\n\n" +
				"_Starflower_ is no longer a special regrowth plant, so it's getting a few changes:\n" +
				"_-_ Starflower no longer has a 1/5 chance to drop its own seed.\n" +
				"_-_ Starflower is now 2.5x as rare as other seeds, down from 10x."));

		changes.addButton( new ChangeButton(new WandOfMagicMissile(),
				"The _Wand of Magic Missile_ has received a new ability that lets it better synergize with other wands!\n\n" +
				"After zapping with an upgraded magic missile wand, the hero becomes 'magically charged' for a few turns. When the hero zaps with another lower-level wand, the charge is consumed and that wand will act as if it is the same level as the wand of magic missile!\n\n" +
				"My hope with this change is to make magic missile more worthy of upgrades if the player is going for a sort of 'wandslinger' build, while still maintaining the wands weakness when used solo.\n\n" +
				"I intend to closely watch how this change plays out, and make adjustments if it ends up messing with the Mage too much. As a start to this, the _Battlemage_ on-hit effect for magic missile has been toned down, to prevent a magic missile staff plus a single low-level wand being stronger than just imbuing that wand."));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POUCH), "Item Droprate and Inventory Managment",
				"I'm making several changes primarily targeted at reducing inventory clutter. These shouldn't have a significant impact on game balance, but should reduce the likelihood of the player's inventory being clogged with low-value items.\n\n" +
				"_-_ Bags now appear inside of themselves, rather than taking up main inventory space.\n" +
				"_-_ Consumable drops are now slightly rarer, but the game is now much more likely to give a variety of consumables over the course of a run.\n" +
				"_-_ Equipment drops are now slightly rarer, but are also higher in quality on average. In particular enemy equipment drops can now be above +0.\n" +
				"_-_ Item drops have been added to DM-200s, Ghouls, and Golems."));

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.TENGU, 0, 0, 14, 16), "Boss Changes",
				"I'm making some design and balance changes to bosses based on analytics data and feedback:\n\n" +
				"_Tengu_'s second phase has been removed due to its overall negative reception. In exchange, the traps in Tengu's first phase are now more maze-like. Tengu now also receives an accuracy penalty when attacking at melee range.\n\n" +
				"_DM-300_'s boss fight now starts after the player explores its arena, the supercharge sparks now move more quickly, and several dialogue lines have been tweaked. This will hopefully make it easier for the player to understand what to do when DM-300 becomes supercharged.\n\n" +
				"_Yog-Dzewa_ is receiving some smaller balance tweaks:\n" +
				"_-_ Yog laser damage down to 20-30 from 20-35\n" +
				"_-_ Bright/Dark fist ranged damage down to 10-20 from 12-24\n" +
				"_-_ Burning fist now evaporates slightly more water tiles"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "Interface Adjustments",
				"Aside from the new hero select, several other interfaces have been adjusted as well:\n" +
				"_-_ The about scene has been expanded with more credits listings and links.\n" +
				"_-_ Small improvements to icon style and button layout on the title screen.\n" +
				"_-_ The layout of the language selection window has been improved.\n" +
				"_-_ The settings window now uses icons for its tabs, instead of text.\n" +
				"_-_ Adjusted the layout of buying and selling windows slightly."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ The game's startup time has been substantially improved on devices with slower storage.\n" +
				"_-_ Volume sliders are now more sensetive at the lower range.\n" +
				"_-_ A keybinding has been added for resting.\n\n" +
				"_-_ Passive enemies are no longer automatically surprise attacked.\n" +
				"_-_ Several buff durations have been slightly tweaked to be more consistent.\n" +
				"_-_ The footwear of nature now only lets you select seeds that you haven't fed to it already.\n\n" +
				"_-_ Cell checking visual effects have been improved.\n\n" +
				"_-_ Updated translations."));

		changes.addButton( new ChangeButton(new Image( Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various visual/textual errors\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Levitation applying ground pressing effects before actually ending\n" +
				"_-_ Ascend/descend working while rooted\n" +
				"_-_ Scroll of remove curse not being usable while degraded\n" +
				"_-_ Stone glyph not clamping negative accuracy or evasion\n" +
				"_-_ Incorrect interactions between corrupting and on-kill effects\n" +
				"_-_ Unblessed ankh revives waking up mimics\n" +
				"_-_ Rogue armor's blink not going over terrain in some cases\n" +
				"_-_ DM-300 being able to drill out of its arena in rare cases\n" +
				"_-_ Dark & bright fists rarely teleporting to unreachable places\n" +
				"_-_ Summoning traps failing to summon anything in some cases\n" +
				"_-_ Debuffs being incorrectly persisted in rankings\n" +
				"_-_ Tengu cleansing doomed debuff between first and second phase\n" +
				"_-_ Gasses being examinable when not visible\n" +
				"_-_ Fireblast applying debuffs to dead enemies\n" +
				"_-_ Update checker using data on metered networks"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AMETHYST), "Ring of Wealth and Lucky Enchant",
				"The _Ring of Wealth_ has been substantially buffed to make it more worth investing upgrades in:\n" +
				"_-_ Regular drop chance boost up to 25% from 20%.\n" +
				"_-_ Special ring of wealth drops now increase in value, not frequency, as the ring levels.\n" +
				"_-_ Removed guaranteed +0 weapons/armor from special ring of wealth drops.\n" +
				"_-_ Added new vfx to special ring of wealth drops to better indicate how rare a drop was.\n\n" +
				"The _Lucky_ Enchantment is also being buffed:\n" +
				"_-_ Lucky now gives low and medium value drops from ring of wealth's special reward pool."));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.ROGUE, 0, 90, 12, 15), "Assassin and Sniper",
				"The _Assassin_ is having a little trouble now that more enemies have tools to counter raw damage, so he's getting a boost:\n" +
				"_-_ Preparation now gives a +15% damage bonus at 1/3/6/11 turns, up from a +10% damage bonus at 1/3/6/11/16 turns.\n" +
				"_-_ Preparation now executes low health enemies, rather than dealing more damage.\n" +
				"_-_ Preparation can now execute bosses if they are very weak.\n\n" +
				"The _Sniper_ is also having trouble after 0.8.0, so she's getting a boost as well:\n" +
				"_-_ Sniper's mark now lasts 4 turns, up from 2.\n" +
				"_-_ Sniper shot damage scaling with distance increased, now caps at 3x damage, up from 2.5x."));

		changes.addButton( new ChangeButton(new WandOfWarding(),
				"The _Wand of Warding_ has received a variety of changes focused around making it more powerful and easier to use:\n" +
				"_-_ Wards/sentries are now inorganic\n" +
				"_-_ Ward/sentry energy cost and warding energy capacity reduced by 1\n" +
				"_-_ Ward/sentry vision range increased by 1\n" +
				"_-_ Ward/sentry attack speed standardized to 0.5x/1x\n" +
				"_-_ Greater wards now get 5 zaps, up from 4\n" +
				"_-_ Lesser sentries now start with 4 less HP for each zap they used as a ward\n" +
				"_-_ Upgrading a sentry now always grants it the largest possible health boost\n" +
				"_-_ Sentry healing up to 8/10/15 from 6/8/12\n\n" +
				"_-_ Ward sprites now visually darken as they use up charges\n" +
				"_-_ Sentries now show how much they are being healed\n" +
				"_-_ Wards can now be adjacent to eachother\n" +
				"_-_ Wards can now be summoned out of FOV if nothing is in the way\n" +
				"_-_ Wards now spawn adjacent to an enemy/wall if shot at one"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_DISINTEGRATION), "Various Item Buffs",
				"I'm buffing items that are performing poorly after v0.8.0:\n\n" +
				"Several wands need a boost as raw damage is now less effective:\n" +
				"_- Disintegration_ base range up to 6 from 4.\n" +
				"_- Lightning_ no longer harms allies, self-damage down to 67% from 100%.\n" +
				"_- Frost_ chill damage reduction down to 5% per turn from 10%.\n" +
				"_- Fireblast_ now spreads fire in a wider cone shape.\n" +
				"_- Potion of Dragon's Breath_ uses this wider cone too.\n\n" +
				"With more ranged enemies, knockback needs more utility:\n" +
				"_- Knockback effects_ now close doors if the knockback target was inside one.\n\n" +
				"Various other items/enchants are also being buffed:\n" +
				"_- Horn of Plenty_ charge rate increased by 25%\n" +
				"_- Shocking_ no longer harms allies\n" +
				"_- Camouflage_ now applies regular invisibility\n" +
				"_- Flow_ speed multiplier increased to 2 + 0.25xlvl from 2.\n" +
				"_- Corrupting_ proc rate scaling increased by ~20%"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_CORRUPTION), "Various Item Nerfs",
				"I'm nerfing items that are overperforming due to v0.8.0:\n\n" +
				"The _Wand of Corruption_ is once again overpowered, so I'm trying out making it more dependant on enemy health:\n" +
				"_-_ Enemies now have 5x resistance at full HP, up from 3x.\n\n" +
				"As _Wand of Frost_ was buffed, I'm scaling back the battlemage's frost ability a bit:\n" +
				"_-_ Battlemage frost on-hit now only freezes at 10+ turns of chill, rather than at 2-10 turns.\n\n" +
				"Magic defense items are being scaled back due to the increased number of magical enemies:\n" +
				"_- Ring of Elements_ resist per level down to 17.5% from 20%\n" +
				"_- Antimagic_ defense reduced by ~20%\n\n" +
				"A few other items/enchants are being adjusted as well:\n" +
				"_- Ethereal chains_ levelling speed reduced\n" +
				"_- Timekeeper's Hourglass_ sand cost doubled\n" +
				"_- Chilling_ and _Shocking_ proc rate reduced\n" +
				"_- Swiftness_ now requires 2 tiles of space, up from 1\n" +
				"_- Thorns_ proc rate scaling reduced by ~50%"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE), "Various Weapon Nerfs",
				"A few weapons are also being toned down:\n\n" +
				"As warlocks no longer reduce hero strength, the greataxe is now much easier to use and is too strong as a result:\n" +
				"_- Greataxe_ base dmg reduced to 5-45 from 5-50\n\n" +
				"Defense is now much more useful overall, so several defense-granting weapons are performing better than intended:\n" +
				"_- Greatshield_ base block down to 0-6 from 0-10\n" +
				"_- Gauntlet_ blocking reduced to 0 from 0-4\n" +
				"_- Roundshield_ base dmg down to 3-10 from 3-12\n" +
				"_- Roundshield_ base block down to 0-4 from 0-5\n" +
				"_- Sai_ blocking reduced to 0 from 0-2"));

	}
	
	public static void add_v0_8_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.8.0", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo( Messages.get( ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released April 16th, 2020\n" +
				"_-_ 546 days after Shattered v0.7.0\n" +
				"_-_ 197 days after Shattered v0.7.5\n" +
				"\n" +
				"Dev commentary will be added here in the future."));

		changes.addButton( new ChangeButton(new Image(new DM100Sprite()), "Misc Enemies",
				"_DM-100s_ have been added to the prison to replace gnoll shamans. They behave very similarly to prison shamans, but have a few stat tweaks:\n" +
				"_-_ Health increased to 20 from 18.\n" +
				"_-_ Removed bonus damage vs enemies in water.\n" +
				"_-_ The Max level for hero to earn exp from them is reduced to 13 from 14.\n\n" +
				"_Mimics_ have been reworked! I don't want to fully spoil the surprise, but they are now deadlier but offer more counterplay. Keep an eye on chests before you open them...\n\n" +
				"_Golden and Crystal Mimics_ have also been added! Golden mimics have higher stats but a better reward, and crystal mimics will try to run away with their loot! Neither require a key to open.\n\n"+
				"_Armored Statues_ have been added as a rare variant to regular statues. These statues have much higher defense and HP, but will give you both a weapon and armor if you kill them."));

		changes.addButton( new ChangeButton(new Image(new ShamanSprite.Purple()), "Caves",
				"Vision range in the caves is now the same as other regions.\n\n" +
				"_Bats_ have had their damage reduced by 13%, and heal for less when they attack. This should make them less oppressive in the early caves.\n\n" +
				"_Gnoll Brutes_ now deal ~8% less damage, and rage at 0 hp instead of 1/4 HP. This rage grants them shielding that slowly fades over time. When the shield runs out, they die.\n\n" +
				"_Gnoll shamans_ have been completely reworked for the caves. They are more powerful and inflict different debuffs depending on the color of their mask.\n\n" +
				"_Spinners_ are now 20% more evasive, 10% more accurate, and now spit their webs toward the hero from a distance. Their webs block projectiles, but can be cleared.\n\n" +
				"_DM-200s_ are a new enemy in the later parts of the caves. They are too big to move into tunnels and doors, but make up for it with high stats and a toxic gas attack."));

		Image i = new Image(new DM300Sprite());
		i.scale.set(PixelScene.align(0.74f));
		changes.addButton( new ChangeButton(i, Messages.get(NewDM300.class, "name"),
				"The DM-300 fight has been reworked! DM-300 now has redesigned abilities, a new boss arena, and multiple phases!\n\n" +
				"As a part of this rework, DM-300's direct stats have been adjusted:\n" +
				"_-_ Health increased to 300 from 200\n" +
				"_-_ Evasion reduced by ~17%\n" +
				"_-_ Damage reduced by ~11%\n" +
				"_-_ Accuracy reduced by ~28%"));

		changes.addButton( new ChangeButton(new Image(new ElementalSprite.Frost()), "Dwarven City",
				"_Dwarven Ghouls_ are a new enemy which replace elementals as the most simple opponent in the city. They are fairly weak on their own, but always travel in groups.\n\n" +
				"_Elementals_ now have ~10% less HP, multiple different types, and will occasionally fire debuffing ranged attacks. The debuffs they inflict, and their resistances, vary by the elemental type.\n\n" +
				"_Monks_ no longer disarm, but instead build focus which allows them to always dodge one physical attack. Monks start combat with full focus and build it faster when on the move.\n\n" +
				"_Warlocks_ now deal ~21% less damage in melee, but have a nasty new debuff. They inflict 'degraded', which temporarily weakens heavily upgraded gear.\n\n" +
				"_Golems_ have been entirely reworked and are now large enemies like DM-200s. Unlike DM-200s they have no ranged attacks, but instead use dwarven teleportation magic to compensate for their low mobility."));

		changes.addButton( new ChangeButton(new Image(new KingSprite()), Messages.get(DwarfKing.class, "name"),
				"The Dwarf King fight has received a full rework! His fight still heavily features minion summoning, but now also features new abilities, a new arena, and multiple phases!\n\n" +
				"As a part of this fight rework DK's stats have been adjusted:\n" +
				"_-_ Damage reduced by ~40%\n" +
				"_-_ Accuracy reduced by ~18%\n" +
				"_-_ Armor reduced by ~30%\n" +
				"_-_ Evasion reduced by ~12%"));

		changes.addButton( new ChangeButton(new Image(new SpawnerSprite()), "Demon Halls",
				"Floor 21 is now a regular demon halls floor\n\n" +
				"_Ripper Demons_ are a new basic enemy. They have high damage attacks and a leap ability, but have weaker defensive stats. Instead of spawning normally, they are created by the next enemy...\n\n" +
				"_Demon Spawners_ exist in special rooms within the demon halls, and create a steady stream of ripper demons as long as they are alive. Spawners do not return once killed, and award a bunch of EXP and a guaranteed potion of healing.\n\n" +
				"Minor adjustments have been made to existing demon halls enemies:\n" +
				"_-_ Spawn rates reduced slightly, to account for ripper demons.\n" +
				"_-_ Succubi now drop scrolls which are more likely to be rare.\n" +
				"_-_ Evil Eyes now always drop dew, a seed, or a runestone.\n" +
				"_-_ Scorpios now drop potions which are more likely to be rare. They cannot drop healing.\n" +
				"_-_ Acidic scorpios now inflict ooze and don't reflect damage."));

		i = new Image(new YogSprite());
		i.scale.set(0.8f);
		changes.addButton( new ChangeButton(i, "Yog-Dzewa",
				"The Yog-Dzewa fight has been reworked! The key ingredients (eye, fists, and larva) are still present, but the specific stats, mechanics, and pacing of the fight have been almost totally redone.\n\n" +
				"Most notably, the fight is now much more evenly paced. Rather than summoning the same two fists every time at the start of the fight, Yog now summons fists steadily as the fight progresses. The fists also won't be the same each time, which gives the fight some variance.\n\n" +
				"To discourage skipping the demon halls, the fight is also affected by how many demon spawners were left alive on the previous floors. Expect the fight to be much harder if demon spawners are left alive!"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Image(Assets.Environment.TERRAIN_FEATURES, 64, 96, 16, 16), "Trap Adjustments",
				"I've re-evaluated several lategame traps, and trap spawning frequency. My hope with these changes is to make traps less oppressive in the lategame, while still keeping their general effect wherever possible.\n\n" +
				"Traps now show up ~10% more in the sewers, scaling down to ~25% less in the demon halls.\n\n" +
				"_-_ Worn dart traps now deal more damage\n" +
				"_-_ Ooze traps now affect a 3x3 area\n" +
				"_-_ Explosive traps no longer appear as regular random traps, but still show up in special rooms\n" +
				"_-_ Cursing traps now only curse a single worn weapon or armor\n" +
				"_-_ Disintegration traps no longer affect items in the player's inventory\n" +
				"_-_ Disarming traps are now more common, but cannot teleport your weapon too far away\n" +
				"_-_ Pitfall traps are now more common, affect a 3x3 area, and give you one turn to react\n" +
				"_-_ Distortion traps are now more common, can appear in the city, and have a reworked effect. They now act as more chaotic summoning traps, instead of resetting the current floor."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_WARRIOR), "Weapon and Armor Changes",
				"While I intend to make larger changes in a future update, I've decided to do some targeted improvements to class armors now to make the abilities more usable:\n" +
				"_-_ Class armor now has a charge percentage which builds at a rate of 50% per level, each ability uses 35% charge.\n" +
				"_-_ Stun from heroic leap increased to 5 turns from 3.\n" +
				"_-_ Molten earth now roots for 5 turns, up from 3, and deals a burst of immediate damage.\n" +
				"_-_ Smoke bomb now stealths the rogue for 10 turns and blinds for 5 turns, up from 2, but only blinds enemies adjacent to the rogue's old location.\n\n" +
				"Weapon/armor spawns have been adjusted, primarily to remove rare low tier item drops in the lategame:\n" +
				"_-_ Tier 2 items no longer spawn in caves+\n" +
				"_-_ Tier 3 items no longer spawn in demon halls\n" +
				"_-_ Tier 4 items are slightly more common in caves, slightly less common in city\n" +
				"_-_ Tier 5 items are notably more common in city, slightly more common in demon halls"));

		changes.addButton( new ChangeButton(new Image(new StatueSprite()), "AI Tweaks",
				"Improvements have been made to hero, ally, and enemy AI:\n\n" +
				"_-_ Wandering characters are now less likely to repeatedly block eachother in hallways.\n\n" +
				"_-_ Characters can now approach their enemy even if the path to them is blocked.\n\n" +
				"_-_ Characters are now more willing to switch targets if the path to their current target is blocked.\n\n" +
				"_-_ Fleeing enemies are now more willing to run around the hero to retreat.\n\n" +
				"_-_ The hero is now interrupted if their path becomes obstructed by an ally."));

		if (DeviceCompat.isDesktop()) {
			changes.addButton( new ChangeButton( Icons.get( Icons.LIBGDX ), "LibGDX Desktop Functionality",
					"The desktop version of the game is now running through the same codebase as the Android version!\n\n" +
					"Moving forward the desktop version of the game will be at feature parity with the Android version, which includes translations, access to betas, and release timings!\n\n" +
					"Currently all features from the old desktop codebase have been re-implemented in this version:\n" +
					"_-_ Keyboard support, including movement and keybindings\n" +
					"_-_ Mouse support, including scroll to zoom\n" +
					"_-_ Full window management support, including fullscreen\n" +
					"_-_ Update notifier (also now appears for android users)\n\n" +
					"The following additional features have also been added:\n" +
					"_-_ Support for all languages, including asian scripts\n" +
					"_-_ Support for smooth font (droid sans) on all languages\n" +
					"_-_ A crash popup, which includes debug info" ) );
		} else {
			changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.SCROLL_TIWAZ, null), "Update Notification",
					"A little blinking button will now appear in the bottom-left of the title screen if an update is available for the game. \n\n" +
					"The Google Play version of the game uses Google's in-app updates library, and will seamlessly update the game through Google Play if the user accepts the prompt.\n\n" +
					"Other versions of the game will grab update info from Github, and will direct the player to the latest releases page to download the update.\n\n" +
					"Note that on Android, both updaters require Android 5.0+ to work correctly. If your device isn't compatible the notification simply won't appear."));
		}

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ The game now flashes red when the hero is badly hurt.\n" +
				"\n" +
				"_-_ Imp now assigns monks or golems based on depth.\n" +
				"_-_ Number of monks/golems required reduced to 5/4 from 8/6.\n" +
				"\n" +
				"_-_ Torch light duration reduced to 250 turns from 300.\n" +
				"_-_ Each demon halls floor now contains 2 torches, up from 1.\n" +
				"\n" +
				"_-_ The velvet pouch now stores goo blobs and metal shards.\n" +
				"_-_ Items will now go into the main inventory if a bag is full.\n" +
				"\n" +
				"_-_ Shops can now stock more exotic weapons.\n" +
				"_-_ Improved logic for which bag a shop stocks.\n" +
				"\n" +
				"_-_ Upgrading a stack of missile weapons now resets the durability of the stack\n" +
				"_-_ Brewing a potion with 3 of the same seed now identifies it.\n" +
				"_-_ Skeletal remains now always spawn a wraith if a cursed item spawned within them."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Dried rose desc now includes ghost's weapon and armor.\n" +
				"_-_ Wand of Transfusion desc now includes damage/healing numbers.\n" +
				"_-_ Beneficial darts now mention that they don't harm allies.\n" +
				"\n" +
				"_-_ Thrown potions of purity now cleanse Tengu's smoke bomb and fire wave effects.\n" +
				"_-_ Wand of blast wave and force cube AOEs no longer trigger Tengu's traps.\n" +
				"\n" +
				"_-_ Ankhs are now blessed when thrown into a well of healing.\n" +
				"_-_ Items are now uncursed when thrown into a well of healing.\n" +
				"\n" +
				"_-_ Improved the story text for the caves, city, and halls.\n" +
				"_-_ Added some hint text to snakes for new players.\n" +
				"\n" +
				"_-_ Improved vfx for falling enemies and items.\n" +
				"_-_ The settings menu and game options have been streamlined slightly.\n" +
				"\n" +
				"_-_ Updated translations and translator credits"));

		changes.addButton( new ChangeButton(new Image( Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Rare freeze bugs when enemies make ranged attacks\n" +
				"_-_ Loading screens very rarely freezing\n" +
				"_-_ Various bugs in the new Tengu fight\n" +
				"_-_ Unstable spellbook not casting spells if the game was closed while using it\n" +
				"_-_ Shurikens sometimes not attacking instantly even after the hero moved\n" +
				"_-_ Items spawning on the same tile as an enemy spawn\n" +
				"_-_ Unidentified alchemists toolkit being usable in alchemy\n" +
				"_-_ Storm clouds spreading faster than intended\n" +
				"_-_ Chests opening when they are teleported\n" +
				"_-_ Teleportation rarely placing the hero onto hidden traps\n" +
				"_-_ Viscocity glyph not considering all armor sources when applying deferred damage\n" +
				"_-_ Wand of warding bolts not pushing on terrain\n" +
				"_-_ Wand of prismatic light rarely revealing areas it shouldn't\n" +
				"_-_ Upgraded wands not spawning fully charged"));

		changes.addButton( new ChangeButton(new Image( Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed:\n" +
				"_-_ Invisibility not applying if the hero is also shadowmelded\n" +
				"_-_ Landscape/portrait modes working incorrectly in rare cases\n" +
				"_-_ Inventory incorrectly being seen as full when upgrading with the blacksmith\n" +
				"_-_ Odd interactions between the blacksmith and curse infusion\n" +
				"_-_ Force cubes not pressing on every tile in their AOE\n" +
				"_-_ Force cubes affecting an AOE when thrown onto chasms\n" +
				"_-_ Rankings rarely not being recorded if hero is killed by a statue\n" +
				"_-_ Caustic slimes attacking slower than intended\n" +
				"_-_ Newborn elementals disappearing when unblessed ankhs are used\n" +
				"_-_ Hero being able to swap places with allies when movement is restricted\n" +
				"_-_ Some allies being corruptable when they shouldn't be\n" +
				"_-_ Piranhas playing no death animation if they die on land\n" +
				"_-_ Many rare crash bugs\n" +
				"_-_ Many minor audio and visual bugs"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new SpiritBow(),
				"Sniper shot is currently performing quite poorly versus the sniper's other abilities, so I'm giving it a significant buff:\n\n" +
				"_-_ Base sniper shot damage increased by 20%\n" +
				"_-_ Sniper shot damage scaling with distance increased. At high distance sniper shot will deal up to 40% more total damage."));

		changes.addButton( new ChangeButton(new WandOfCorruption(),
				"This is actually a bugfix, but is so significant that I'm listing it as a buff for clarity:\n\n" +
				"_-_ Fixed Wand of corruption acting as if it were one level weaker than it was in some cases.\n\n" +
				"_-_ Doubled corruption resistance reduction from debuffs, as it was 50% weaker than intended. It is now as strong as listed in 0.7.5 changelog (50% for major debuffs, 25% for minor)\n\n" +
				"Additionally, corruption is getting access to two of the new debuffs added in 0.8.0: _Hex,_ and _Weakness._"));

		changes.addButton( new ChangeButton(new Image(Assets.Interfaces.BUFFS_LARGE, 80, 32, 16, 16), "Bless changes",
				"Accuracy and evasion bonuses from blessed buff increased to 25% from 20%." ));

		changes.addButton( new ChangeButton(new StoneOfAugmentation(),
				"The Evasion armor augment has been underperforming verses the defence augment, so I'm adjusting the stat tradeoff they provide:\n" +
				"_-_ Evasion now grants 33% more evasion\n" +
				"_-_ Defense now costs 33% more evasion"));

		changes.addButton( new ChangeButton(new Image(new NecromancerSprite()), "Necromancers",
				"To better balance overall prison difficulty, I've strengthened necromancers slightly:\n" +
				"_-_ Necromancer evasion increased by ~18%"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new StoneOfAugmentation(),
				"The Defense armor augment has been overperforming verses the evasion augment, so I'm adjusting the stat tradeoff they provide:\n" +
				"_-_ Defense now costs 33% more evasion\n" +
				"_-_ Evasion now grants 33% more evasion"));

		changes.addButton( new ChangeButton(new Image(new GuardSprite()), "Enemy Nerfs",
				"To better balance overall prison difficulty, I've weakened prison guards slightly:\n" +
				"_-_ Prison guard accuracy reduced by ~15%\n" +
				"_-_ Prison guard armor reduced by ~12%\n\n" +
				"Some unnecessary enemy debuff resistances have been removed:\n" +
				"_-_ Several enemies are no longer immune to terror or amok\n" +
				"_-_ Evil eyes and wraiths no longer resist grim\n" +
				"_-_ Golems and succubi are no longer immune to sleep\n" +
				"_-_ Bees are no longer immune to poison"));

	}
	
}
