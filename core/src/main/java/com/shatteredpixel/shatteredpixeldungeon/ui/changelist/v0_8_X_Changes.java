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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
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
		add_v0_8_0_Changes(changeInfos);
	}
	
	public static void add_v0_8_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.8.0", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo( "0.8.0b", false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"_-_ Yog-Dzewa's fists are now considered bosses, not minibosses.\n\n" +
				"_-_ Updated translations."));

		changes.addButton( new ChangeButton(new Image( Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by 0.8.0):\n" +
				"_-_ Various rare freezes when descending\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Yog-Dzewa fight glitching in rare cases where the eye and last fist are killed at the same time\n" +
				"_-_ Bright/Dark fist very rarely teleporting into enclosed spaces"));

		changes = new ChangeInfo( "0.8.0a", false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		Image i = new Image(new YogSprite());
		i.scale.set(0.8f);
		changes.addButton( new ChangeButton(i, "Enemy and Boss adjustments",
				"Based on player feedback and gameplay data, I'm making the following tweaks to enemy/boss balance. These changes mainly make particular enemies/bosses a bit easier:\n\n" +
				"_-_ Turns taken for DM-300 to dig through rock increased to 3 from 2.5\n" +
				"_-_ DM-300 pylons now resist damage above 15, up from 10\n\n" +
				"_-_ Ripper Demon leap ability now has a short cooldown\n" +
				"_-_ Ripper Demon damage reduced by ~8%\n" +
				"_-_ Ripper Demon leap now deals large bleed damage less often\n\n" +
				"_-_ Yog's laser damage reduced by ~8%\n" +
				"_-_ Bright fist blind duration reduced by 40%\n" +
				"_-_ Dark fist ranged damage reduced by 20%\n" +
				"_-_ Soiled fist now spreads slightly more furrowed grass\n" +
				"_-_ Soiled fist now resists burning\n" +
				"_-_ Rusted fist melee damage increased by 10%"));

		changes.addButton( new ChangeButton(new SpiritBow(),
				"Sniper shot is currently performing quite poorly versus the sniper's other abilities, so I'm giving it a significant buff:\n\n" +
				"_-_ Base sniper shot damage increased by 20%\n" +
				"_-_ Sniper shot damage scaling with distance increased. At high distance sniper shot will deal up to 40% more total damage."));

		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"Following feedback I've tweaked the sensitivity of the new 'red flash' effect. It should now trigger in far fewer cases where the hero isn't taking serious damage, but should still trigger when the hero is at risk of death.\n\n" +
				"I've tweaked the behaviour of ranged enemies slightly, which should eliminate cases where they don't follow the hero through a door despite seeing them go through it.\n\n" +
				"_-_ Increased the size of the descending region at the end of Dwarf King's level.\n\n" +
				"_-_ Decreased the minimum window size for desktop users.\n\n" +
				"_-_ Updated translations."));

		changes.addButton( new ChangeButton(new Image( Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"Fixed (caused by 0.8.0):\n" +
				"_-_ Various errors/crashed caused by new imp shop\n" +
				"_-_ Several cases where water could spread to tiles that it shouldn't spread to.\n" +
				"_-_ Water of health being used up on items that cannot be cursed\n" +
				"_-_ Golems being able to teleport immovable characters\n" +
				"_-_ Armored statues appearing in faith is my armor challenge\n" +
				"_-_ Yog-dzewa being able to totally blind the player if into darkness is enabled\n" +
				"_-_ Large enemies being able to move into enclosed spaces when vertigoed\n" +
				"_-_ Infinite loop bug when attempting to teleport DM-300\n" +
				"_-_ Various rare crash bugs"));

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

		i = new Image(new DM300Sprite());
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

		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 64, 96, 16, 16), "Trap Adjustments",
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

		changes.addButton( new ChangeButton(new Image( Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
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

		changes.addButton( new ChangeButton(new Image( Assets.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
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
		
		changes.addButton( new ChangeButton(new WandOfCorruption(),
				"This is actually a bugfix, but is so significant that I'm listing it as a buff for clarity:\n\n" +
				"_-_ Fixed Wand of corruption acting as if it were one level weaker than it was in some cases.\n\n" +
				"_-_ Doubled corruption resistance reduction from debuffs, as it was 50% weaker than intended. It is now as strong as listed in 0.7.5 changelog (50% for major debuffs, 25% for minor)\n\n" +
				"Additionally, corruption is getting access to two of the new debuffs added in 0.8.0: _Hex,_ and _Weakness._"));

		changes.addButton( new ChangeButton(new Image(Assets.BUFFS_LARGE, 80, 32, 16, 16), "Bless changes",
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
