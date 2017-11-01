/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.DewVial;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

	private final ArrayList<ChangeInfo> infos = new ArrayList<>();

	@Override
	public void create() {
		super.create();

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedText title = PixelScene.renderText( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.x = (w - title.width()) / 2 ;
		title.y = 4;
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 16;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.y + title.height();
		align( panel );
		add( panel );

		ScrollPane list = new ScrollPane( new Component() ){

			@Override
			public void onClick(float x, float y) {
				for (ChangeInfo info : infos){
					if (info.onClick( x, y )){
						return;
					}
				}
			}

		};
		add( list );
		
		//**********************
		//       v0.6.2
 		//**********************
		
		ChangeInfo changes = new ChangeInfo("v0.6.2a", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.IRON_KEY, null), "New Key Display",
				"The key display has been overhauled!\n\n" +
				"_-_ Each key type now has its own icon, instead of all special keys being shown as golden.\n\n" +
				"_-_ Can now display up to 6 keys, up from 3. After 3 keys the key icons will become smaller.\n\n" +
				"_-_ Button background now dims as keys are collected, for added visual clarity."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Improved the formatting of older updates in the changes list. More information will be added to fill these out in future updates.\n\n" +
				"_-_ Slightly reduced the chance for items to appear in locked chests.\n\n" +
				"_-_ Game music now mutes itself during phone calls"));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (caused by 0.6.2):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Herbal healing and armor not greying out their icons correctly\n\n" +
				"Fixed (Existed prior to 0.6.2):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Gladiator being able to combo non-visible enemies"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"In English:\n" +
				"_-_ Fixed missing text when dieing to venom\n" +
				"\n" +
				"_-_ Translation Updates"));
		
		changes = new ChangeInfo("v0.6.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		infos.add(changes);
		
		changes.addButton( new ChangeButton( Icons.get(Icons.DEPTH), "Dungeon Secrets!",
				"The secrets of the dungeon have been totally redesigned!\n\n" +
				"_-_ Regular rooms can no longer be totally hidden\n\n" +
				"_-_ 12 new secret rooms added, which are always hidden\n\n" +
				"_-_ Hidden doors are now harder to find automatically\n\n" +
				"_-_ Searching now consumes 6 turns of hunger, up from 2.\n\n" +
				"This is a big adjustment to how secrets work in the dungeon. The goal is to make secrets more interesting, harder to find, and also more optional."));
		
		changes.addButton( new ChangeButton( new Image(Assets.ROGUE, 0, 15, 12, 15), "Rogue Rework!",
				"The rogue has been reworked! His abilities have received a number of changes to make his strengths more pronounced and focused.\n\n" +
				"These abilities have been _removed:_\n" +
				"_-_ Gains evasion from excess strength on armor\n" +
				"_-_ Gains hunger 20% more slowly\n" +
				"_-_ Identifies rings by wearing them\n" +
				"_-_ Has an increased chance to detect secrets\n\n" +
				"These abilities have been _added:_\n" +
				"_-_ Searches in a wider radius than other heroes\n" +
				"_-_ Is able to find more secrets in the dungeon\n\n" +
				"Make sure to check out the Cloak of Shadows and Dagger changes as well."));
		
		changes.addButton( new ChangeButton( new Image(Assets.ROGUE, 0, 90, 12, 15), "Rogue Subclasses Rework!",
				"Both of the rogue's subclasses has been reworked, with an emphasis on more powerful abilities that need more interaction from the player.\n\n" +
				"_The Assassin:_\n" +
				"_-_ No longer gains a free +25% damage on surprise attacks\n" +
				"_-_ Now prepares for a deadly strike while invisible, the longer he waits the more powerful the strike becomes.\n" +
				"_-_ Charged attacks have special effects, such as blinking to the target and dealing bonus damage to weakened enemies.\n\n" +
				"_The Freerunner:_\n" +
				"_-_ No longer gains movement speed when not hungry or encumbered\n" +
				"_-_ Now gains 'momentum' as he runs. Momentum increases evasion and movement speed as it builds.\n" +
				"_-_ Momentum is rapidly lost when standing still.\n" +
				"_-_ Evasion gained from momentum scales with excess strength on armor."));
		
		changes.addButton( new ChangeButton( new Image(Assets.TERRAIN_FEATURES, 16, 0, 16, 16), "Trap Overhaul!",
				"Most of the game's traps have received changes, some have been overhauled entirely!\n\n" +
				"_-_ Removed Spear and Paralytic Gas Traps\n" +
				"_-_ Lightning Trap is now Shocking and Storm traps\n" +
				"_-_ Elemental Traps now all create fields of their element\n" +
				"_-_ Worn and Poison Trap are now Worn and Poison Dart Trap\n" +
				"_-_ Dart traps, Rockfall Trap, and Disintegration Trap are now always visible, but attack at a range\n" +
				"_-_ Warping Trap reworked, no longer sends to previous floors\n" +
				"_-_ Gripping and Flashing Traps now never deactivate, but are less harmful\n\n" +
				"_-_ Tengu now uses Gripping Traps\n\n" +
				"_-_ Significantly reduced instances of items appearing ontop of item-destroying traps"));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.LOCKED_CHEST, null), "Chest Adjustments",
				"_-_ Crystal chests are now opened by crystal keys.\n\n" +
				"_-_ Golden chests now sometimes appear in the dungeon, containing more valuable items."));
		
		
		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new WandOfCorruption(),
				"The Wand of Corruption has been reworked!\n\n" +
				"_-_ Corruption resistance is now based on enemy exp values, not max HP. Low HP and debuffs still reduce enemy corruption resistance.\n\n" +
				"_-_ Wand now only spends 1 charge per shot, and inflicts debuffs on enemies if it fails to corrupt. Debuffs become stronger the closer the wand got to corrupting.\n\n" +
				"_-_ Corrupted enemies are now considered by the game to be ally characters.\n\n" +
				"_-_ Corrupted enemies award exp immediately as they are corrupted.\n\n" +
				"These changes are aimed at making the wand more powerful, and also less of an all-in wand. Wand of Corruption is now useful even if it doesn't corrupt an enemy."));
		
		changes.addButton( new ChangeButton( new Image(Assets.STATUE, 0, 0, 12, 15), "AI and Enemy Changes",
				"_-_ Characters now have an internal alignment and choose enemies based on that. Friendly characters should now never attack eachother.\n\n" +
				"_-_ Injured characters will now always have a persistent health bar, even if they aren't being targeted.\n\n" +
				"_-_ Improved enemy emote visuals, they now appear more frequently and there is now one for losing a target.\n\n" +
				"_-_ Enemies now always lose their target after being teleported."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Buff icons can now be tinted, several buffs take advantage of this to better display their state.\n\n" +
				"_-_ Added a new interface for alchemy. This replaces throwing items into the pot directly.\n\n" +
				"_-_ Reduced the spawn rate of dark floors to 1.5x, from 2x.\n\n" +
				"_-_ Crystal chest rooms will now always have a different item type in each chest.\n\n" +
				"_-_ Burning and freezing now destroy held items in a much less random manner.\n\n" +
				"_-_ Various internal code improvements.\n" +
				"_-_ Zooming is now less stiff at low resolutions.\n" +
				"_-_ Improved VFX when items are picked up."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Various exploits players could use to determine map shape\n" +
				"_-_ Artifacts being removed from the quickslot when being equipped in some cases\n" +
				"_-_ Swapping misc items not working correctly with a full inventory\n" +
				"_-_ Non-hostile characters reducing the number of spawned enemies in some cases\n" +
				"_-_ Bugged interaction between poison and venom\n" +
				"_-_ Enemies sometimes not waking from sleep\n" +
				"_-_ Swarms not duplicating over hazards\n" +
				"_-_ Black bars on certain modern phones\n" +
				"_-_ Action button not persisting between floors\n" +
				"_-_ Various bugs with multiplicity curse\n" +
				"_-_ Various minor visual bugs\n" +
				"_-_ Blandfruit rarely becoming a potion\n" +
				"_-_ Planted seeds not updating terrain correctly\n" +
				"_-_ Enemies rarely spawning ontop of exit stairs\n" +
				"_-_ Evil Eyes sometimes skipping beam chargeup\n" +
				"_-_ Warrior's seal being disabled by armor kit" ));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"In English:\n" +
				"_-_ Improved some common game log entries\n" +
				"_-_ Fixed a typo when enemies die out of view\n" +
				"_-_ Fixed a typo in the ghost hero's introduction\n" +
				"_-_ Fixed a typo in dirk description\n" +
						"\n" +
				"_-_ Translation Updates\n\n" +
				"_-_ Added new language: _Turkish_"));
		
		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new CloakOfShadows(),
				"As part of the rogue rework, the cloak of shadows has been significantly buffed:\n\n" +
				"_-_ Max charges have been halved, however each charge is roughly twice as useful.\n" +
				"_-_ As there are half as many charges total, charge rate is effectively increased.\n" +
				"_-_ Cooldown mechanic removed, cloak can now be used multiple times in a row.\n" +
				"_-_ Cloak levelling progression changed, it is now much more dependant on hero level\n\n" +
				"These changes should let the rogue go invisible more often, and with more flexibility."));
		
		changes.addButton( new ChangeButton(new Dagger(),
				"As part of the rogue rework, sneak attack weapons have been buffed:\n\n" +
				"_-_ Dagger sneak attack minimum damage increased to 75% from 50%.\n" +
				"_-_ Dirk sneak attack minimum damage increased to 67% from 50%\n" +
				"_-_ Assassin's blade sneak attack minimum damage unchanged at 50%\n\n" +
				"This change should hopefully give the rogue some needed earlygame help, and give him a more clear choice as to what item he should upgrade, if no items were found in the dungeon."));
		
		changes.addButton( new ChangeButton(new Flail(),
				"The flail's downsides were too harsh, so one of them has been changed both to make its weaknesses more centralized and to hopefully increase its power.\n\n" +
				"_-_ Flail no longer attacks at 0.8x speed, instead it has 20% reduced accuracy."));
		
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.POTION_GOLDEN, null), "Potion Adjustments",
				"Potion of Purification buffed:\n" +
				"_-_ Drinking effect now lasts for 20 turns, up from 15.\n" +
				"_-_ Drinking now provides immunity to all area-bound effects, not just gasses.\n\n" +
				"Potion of Frost buffed:\n" +
				"_-_ Now creates a freezing field which lasts for several turns."));
		
		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Berserker",
				"The Berserker's survivability and power have been reduced to help bring him into line with the other subclasses:\n\n" +
				"_-_ Bonus damage from low health reduced significantly when below 50% HP. 2x damage while berserking is unchanged.\n\n" +
				"_-_ Turns of exhaustion after berserking increased to 60 from 40. Damage reduction from exhaustion stays higher for longer."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS, null), "Heroes Remains",
				"_-_ Remains can no longer contain progression items, such as potions of strength or scrolls of upgrade.\n\n" +
				"_-_ All upgradeable items dropped by remains are now capped at +3 (+0 for artifacts)\n\n" +
				"The intention for remains is so a previously failed run can give a nice surprise and tiny boost to the next one, but these items are both too strong and too easy to abuse.\n\n" +
				"In compensation, it is now much less likely to receive gold from remains, unless that character died with very few items."));
		
		//**********************
		//       v0.6.1
		//**********************
		
		changes = new ChangeInfo("v0.6.1", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);

		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		infos.add(changes);

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null), "Journal Additions",
				"_-_ Overhauled the Journal window with loads of new functionality\n\n" +
				"_-_ Added a completely overhauled tutorial experience, which replaces the existing signpost system.\n\n" +
				"_-_ Massively expanded the items catalog, now contains every identifiable item in the game."));
		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.ALL_ITEMS_IDENTIFIED.image), "Badge Changes",
				"_-_ Added new badges for identifying all weapons, armor, wands, and artifacts.\n\n" +
				"_-_ All identification-based badges are now tied to the new item list system, and progress for them will persist between runs.\n\n" +
				"_-_ Removed the Night Hunter badge\n\n" +
				"_-_ The 'Many Deaths' badge now covers all death related badges, previously it was not covering 2 of them.\n\n" +
				"_-_ Reduced the numbers of games needed for the 'games played' badges from 10/100/500/2000 to 10/50/250/1000\n\n" +
				"_-_ Blank badges shown in the badges menu are now accurate to how many badges you have left to unlock."));
		changes.addButton( new ChangeButton( Icons.get(Icons.DEPTH), "Dungeon Changes",
				"_-_ Added 5 new regional rooms\n" +
				"_-_ Added two new uncommon room types\n" +
				"_-_ Added a new type of tunnel room\n\n" +
				"_-_ Level layouts now more likely to be dense and interconnected.\n\n" +
				"_-_ Tunnels will now appear more consistently.\n\n" +
				"_-_ Ascending stairs, descending stairs, and mining no longer increase hunger."));
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_TOPAZ, null), new RingOfEnergy().trueName(),
				"_-_ Added the ring of energy."));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CHEST, null), "Sprites",
				"New sprites for the following:\n" +
						"_-_ Chests & Mimics\n" +
						"_-_ Darts\n" +
						"_-_ Javelins\n" +
						"_-_ Tomahawks"));

		changes = new ChangeInfo(Messages.get(this, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		infos.add(changes);

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null), "Ring Mechanics Changes",
				"Rings now handle upgrades and curses more similarly to other items:\n\n" +
				"_-_ Rings are now found at +0, down from +1, but are more powerful to compensate.\n\n" +
				"_-_ Curses no longer affect ring upgrades, it is now impossible to find negatively upgraded rings.\n\n" +
				"_-_ Cursed rings are now always harmful regardless of their level, until the curse is cleansed.\n\n" +
				"_-_ Scrolls of upgrade have a chance to remove curses on a ring, scrolls of remove curse will always remove the curse."));
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_AMETHYST, null), new RingOfWealth().trueName(),
				"The ring of wealth is getting a change in emphasis, moving away from affecting items generally, and instead affecting item drops more strongly.\n\n" +
				"_-_ No longer grants any benefit to item spawns when levels are generated.\n\n" +
				"_-_ Now has a chance to generate extra loot when defeating enemies.\n\n" +
				"I'm planning to make further tweaks to this item in future updates."));
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), new PotionOfHealing().trueName(),
				"Health Potions are getting a changeup to make hoarding and chugging them less effective, and to encourage a bit more strategy than to just drink them on the verge of death.\n\n" +
				"_-_ Health potions now heal in a burst that fades over time, rather than instantly.\n\n" +
				"_-_ Health potions now heal more than max HP at low levels, and slightly less than max HP at high levels.\n\n" +
				"Make sure to read the dew vial changes as well."));
		changes.addButton( new ChangeButton( new DewVial(),
				"The dew vial (and dew) are having their healing abilities enhanced to improve the availability of healing in the sewers, and to help offset the health potion changes.\n\n" +
				"_-_ Dew drops now heal 5% of max HP\n\n" +
				"_-_ Dew vial now always spawns on floor 1\n\n" +
				"_-_ The dew vial is now full at 20 drops, drinking heals 5% per drop and is instantaneous.\n\n" +
				"_-_ Dew will always be collected into an available vial, even if the hero is below full HP.\n\n" +
				"_-_ When drinking from the vial, the hero will now only drink as many drops as they need to reach full HP."));
		changes.addButton( new ChangeButton( new Image(Assets.STATUE, 0, 0, 12, 15), "AI Changes",
				"_-_ Improvements to pathfinding. Characters are now more prone to take efficient paths to their targets, and will prefer to wait instead of taking a very inefficient path.\n\n" +
				"_-_ Characters will now more consistently decide who to attack based on distance and who they are being attacked by."));
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed:\n" +
				"_-_ Issues with Android 7.0+ multi-window\n" +
				"_-_ Rare stability issues on certain devices\n" +
				"_-_ Numerous rare crash and freeze bugs\n" +
				"_-_ Chasm death not showing in rankings\n" +
				"_-_ Resting icon sometimes not appearing\n" +
				"_-_ Various minor graphical bugs\n" +
				"_-_ The ambitious imp rarely blocking paths\n" +
				"_-_ Berserk prematurely ending after loading\n" +
				"_-_ Mind vision not updating while waiting\n" +
				"_-_ Troll blacksmith destroying broken seal\n" +
				"_-_ Wands always being uncursed by upgrades\n" +
				"_-_ Various bugs with Evil Eyes\n" +
				"_-_ Thieves being able to escape while visible\n" +
				"_-_ Enemies not visually dying in rare cases\n" +
				"_-_ Visuals flickering while zooming on low resolution devices.\n" +
				"_-_ Various minor bugs with the buff indicator\n" +
				"_-_ Sleep-immune enemies being affected by magical sleep\n" +
				"_-_ Sad Ghost being affected by corruption\n" +
				"_-_ Switching places with the Sad Ghost over chasms causing the hero to fall"));
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Completely overhauled the changes scene (which you're currently reading!)\n" +
				"_-_ Item and enemy spawn RNG is now more consistent. Should prevent things like finding 4 crabs on floor 3.\n" +
				"_-_ The compass marker now points toward entrances after the amulet has been acquired.\n" +
				"_-_ Improved quickslot behaviour when items are removed by monks or thieves.\n" +
				"_-_ Allies are now better able to follow you through bosses.\n" +
				"_-_ Lloyd's Beacon's return function is no longer blocked by none-hostile creatures.\n" +
				"_-_ Performance tweaks on devices with 2+ cpu cores\n" +
				"_-_ Stepping on plants now interrupts the hero\n" +
				"_-_ Improved potion and scroll inventory icons\n" +
				"_-_ Increased landscape width of some windows\n" +
				"_-_ Un-IDed artifacts no longer display charge"));
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"Fixed in English:\n" +
				"_-_ Missing capitalization in Prison Guard text\n" +
				"_-_ Typo when trying a locked chest with no key\n" +
				"_-_ Missing period in alarm trap description\n\n" +
				"_-_ Added new Language: _Catalan_\n\n" +
				"_-_ Various translation updates"));

		changes = new ChangeInfo(Messages.get(this, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		infos.add(changes);

		changes.addButton( new ChangeButton( new UnstableSpellbook(),
				"The Unstable spellbook wasn't really worth upgrading, so it's getting some new effects to make it worth investing in!\n\n" +
				"_-_ Infusing a scroll into the unstable spellbook will now grant a unique empowered effect whenever that scroll's spell is cast from the book.\n\n" +
				"To compensate, charge mechanics have been adjusted:\n\n" +
				"_-_ Max charges reduced from 3-8 to 2-6\n\n" +
				"_-_ Recharge speed has been reduced slightly" ));
		changes.addButton( new ChangeButton( new DriedRose().upgrade(10),
				"The ghost hero summoned by the rose is now much more capable and is also much less temporary:\n\n" +
				"_-_ Ghost can now be equipped with a weapon and armor and uses them just like the hero.\n" +
				"_-_ Ghost no longer takes damage over time as long as the rose remains equipped.\n" +
				"_-_ Ghost health increased by 10\n" +
				"_-_ Ghost now has a persistent HP bar\n" +
				"_-_ Ghost can now follow you between floors\n\n" +
				"However:\n\n" +
				"_-_ Ghost no longer gains damage and defense from rose levels, instead gains strength to use better equipment.\n" +
				"_-_ Rose no longer recharges while ghost is summoned\n" +
				"_-_ Rose takes 25% longer to fully charge" ));
		changes.addButton( new ChangeButton( Icons.get(Icons.BACKPACK), "Inventory",
				"_-_ Inventory space increased from 19 slots to 20 slots.\n\n" +
				"_-_ Gold indicator has been moved to the top-right of the inventory window to make room for the extra slot." ));

		changes = new ChangeInfo(Messages.get(this, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		infos.add(changes);

		changes.addButton( new ChangeButton( new HornOfPlenty(),
				"The Horn of Plenty was providing a bit too much value in the earlygame, especially without upgrades:\n\n" +
				"_-_ Charge Speed reduced, primarily at lower levels:\n-20% at +0\n-7.5% at +10\n\n" +
				"_-_ Upgrade rate adjusted, Food now contributes towards upgrades exactly in line with how much hunger it restores. This means smaller food items will contribute more, larger ones will contribute less. Rations still grant exactly 1 upgrade each."));
		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.RING_GARNET, null), new RingOfMight().trueName(),
				"The Ring of Might's strength bonus is already extremely valuable, having it also provide an excellent health boost was simply too much:\n\n" +
				"_-_ Health granted reduced from +5 per upgrade to +3.5% of max hp per upgrade.\n\n" +
				"This is a massive reduction to its earlygame health boosting power, however as the player levels up this will improve. By hero level 26 it will be as strong as before this change."));
		changes.addButton( new ChangeButton( new EtherealChains(),
				"The ability for Ethereal Chains to pull you literally anywhere limits design possibilities for future updates, so I've added a limitation.\n\n" +
				"_-_ Ethereal chains now cannot reach locations the player cannot reach by walking or flying. e.g. the chains can no longer reach into a locked room.\n\n" +
				"_-_ Ethereal chains can now reach through walls on boss floors, but the above limitation still applies."));
		
		//**********************
		//       v0.6.0
		//**********************
		
		changes = new ChangeInfo("v0.6.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton( Icons.get(Icons.DEPTH), "Levelgen Overhaul!",
				"Level creation algorithm overhauled!\n\n" +
				"_-_ Levels are now much less box-shaped\n" +
				"_-_ Sewers are now smaller, caves+ are now larger\n" +
				"_-_ Some rooms can now be much larger than before\n" +
				"_-_ Added 8 new standard room types, and loads of new standard room layouts\n\n" +
				"_-_ Reduced number of traps in later chapters"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Torch()), "Light Source Buffs",
				"_-_ Light sources now grant significantly more vision\n" +
				"_-_ Light from torches now lasts 20% longer\n" +
				"_-_ Slightly increased visibility on floor 22+\n" +
				"_-_ Floor 21 shop now sells 3 torches, up from 2"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Food()), "Food Buffs",
				"_-_ Meat and small rations are 50% more filling\n" +
				"_-_ Pasties and blandfruit are 12.5% more filling"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Greataxe()), "Tier-5 Weapon Buffs",
				"_-_ Greataxe base damage increased by ~22%\n" +
				"_-_ Greatshield base damage increased by ~17%"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new StoneOfEnchantment()), "Enchant and Glyph Balance Changes",
				"_-_ Vampiric enchant lifesteal reduced by 20%\n\n" +
				"Lucky enchant rebalanced:\n" +
				"_-_ now deals 2x/0x damage, instead of min/max\n" +
				"_-_ base chance to deal 2x increased by ~10%\n\n" +
				"Glyph of Viscosity rebalanced:\n" +
				"_-_ proc chance reduced by ~25% \n" +
				"_-_ damage over time reverted from 15% to 10%\n\n" +
				"_-_ Glyph of Entanglement root time reduced by 40%\n\n" +
				"Glyph of Potential rebalanced:\n" +
				"_-_ self-damage no longer scales with max hp\n" +
				"_-_ grants more charge at higher levels"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Visiting floor 21 before completing the imp quest no longer prevents his shop from spawning\n\n" +
				"_-_ Floor 2 entry doors are now only hidden for new players\n\n" +
				"_-_ Falling damage tweaked to be less random\n\n" +
				"_-_ Resume indicator now appears in more cases"));
		
		//**********************
		//       v0.5.0
		//**********************
		
		changes = new ChangeInfo("v0.5.0", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton( Icons.get(Icons.DEPTH), "New Dungeon Visual Style!",
				"_-_ Walls and some terrain now have depth\n" +
				"_-_ Characters & items are raised & cast shadows\n" +
				"_-_ Added a visible tile grid in the settings menu"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Quarterstaff()), "Equipment Balance Changes",
				"_-_ Quarterstaff armor bonus increased from 2 to 3\n\n" +
				"_-_ Wand of Frost damage against chilled enemies reduced from -7.5% per turn of chill to -10%\n\n" +
				"_-_ Wand of Transfusion self-damage reduced from 15% max hp to 10% max hp per zap\n\n" +
				"_-_ Dried Rose charges 20% faster and the ghost hero is stronger, especially at low levels"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Stylus()), "Glyph Balance Changes",
				"_-_ Glyph of Entanglement activates less often but grants significantly more herbal armor\n\n" +
				"_-_ Glyph of Stone armor bonus reduced from 2+level to 0+level\n\n" +
				"_-_ Glyph of Antimagic magical damage resist reduced from 50% of armor to 33% of armor\n\n" +
				"_-_ Glyph of Viscosity damage rate increased from 10% of deferred damage to 15%"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"_-_ Added new Language: Esperanto\n" +
				"_-_ Added new Language: Indonesian\n"));
		
		//**********************
		//       v0.4.X
		//**********************
		
		changes = new ChangeInfo( "v0.4.X", true, "More Info will be added here soon.");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.4.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 16th, 2016\n" +
				"_-_ 37 days after Shattered v0.4.2"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Update Changes",
				"Various utility features and improvements"));
		
		changes = new ChangeInfo("v0.4.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 9th, 2016\n" +
				"_-_ 46 days after Shattered v0.4.1"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Update Changes",
				"Performance and game engine improvements"));
		
		changes = new ChangeInfo("v0.4.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released July 25th, 2016\n" +
				"_-_ 35 days after Shattered v0.4.0"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new PlateArmor()), "Update Changes",
				"Balance adjustments to enemies & armor"));
		
		changes = new ChangeInfo("v0.4.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 20th, 2016\n" +
				"_-_ 391 days after Shattered v0.3.0\n" +
				"_-_ 50 days after Shattered v0.3.5"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Longsword()), "Update Changes",
				"Reworked equips, enchants & curses"));
		
		//**********************
		//       v0.3.X
		//**********************
		
		changes = new ChangeInfo( "v0.3.X", true, "More Info will be added here soon.");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.3.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 1st, 2016\n" +
				"_-_ 81 days after Shattered v0.3.4"));
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "Update Changes",
				"Reworked Warrior & subclasses"));
		
		changes = new ChangeInfo("v0.3.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 10th, 2016\n" +
				"_-_ 54 days after Shattered v0.3.3"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "Update Changes",
				"Multiple language support"));
		
		changes = new ChangeInfo("v0.3.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released December 18th, 2015\n" +
				"_-_ 44 days after Shattered v0.3.2"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Update Changes",
				"Support for Google Play Games"));
		
		changes = new ChangeInfo("v0.3.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 4th, 2015\n" +
				"_-_ 79 days after Shattered v0.3.1"));
		
		changes.addButton( new ChangeButton(new Image(Assets.TENGU, 0, 0, 14, 16), "Update Changes",
				"Prison Rework & Balance Changes"));
		
		changes = new ChangeInfo("v0.3.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 17th, 2015\n" +
				"_-_ 83 days after Shattered v0.3.0"));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 16, 0, 16, 16), "Update Changes",
				"Traps reworked & UI upgrades"));
		
		changes = new ChangeInfo("v0.3.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 26th, 2015\n" +
				"_-_ 253 days after Shattered v0.2.0\n" +
				"_-_ 92 days after Shattered v0.2.4"));
		
		changes.addButton( new ChangeButton(new Image(Assets.MAGE, 0, 15, 12, 15), "Update Changes",
				"Wands & Mage completely reworked"));
		
		//**********************
		//       v0.2.X
		//**********************
		
		changes = new ChangeInfo( "v0.2.X", true, "More Info will be added here soon.");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.2.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 23rd, 2015\n" +
				"_-_ 48 days after Shattered v0.2.3"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Update Changes",
				"Small improvements and tweaks"));
		
		changes = new ChangeInfo("v0.2.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released January 6th, 2015\n" +
				"_-_ 64 days after Shattered v0.2.2"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new TimekeepersHourglass()), "Update Changes",
				"Artifact additions & improvements"));
		
		changes = new ChangeInfo("v0.2.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 3rd, 2014\n" +
				"_-_ 21 days after Shattered v0.2.1"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Update Changes",
				"Small improvements and tweaks"));
		
		changes = new ChangeInfo("v0.2.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 13th, 2014\n" +
				"_-_ 28 days after Shattered v0.2.0"));
		
		changes.addButton( new ChangeButton(new Image(Assets.RAT, 0, 30, 16, 15), "Update Changes",
				"Sewer improvements\n\n" +
					"More Info to be added here soon."));
		
		changes = new ChangeInfo("v0.2.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 15th, 2014\n" +
				"_-_ 31 days after Shattered v0.1.1"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new HornOfPlenty()), "Update Changes",
				"Added artifacts, reworked rings"));
		
		changes = new ChangeInfo(" ", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		//**********************
		//       v0.1.X
		//**********************
		
		changes = new ChangeInfo( "v0.1.X", true, "More Info will be added here soon.");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.1.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 15th, 2014\n" +
				"_-_ 10 days after Shattered v0.1.0"));
		
		changes.addButton( new ChangeButton(new Blandfruit(),
				"Added blandfruit"));
		
		changes.addButton( new ChangeButton(new DewVial(),
				"Reworked dew vial and ankh"));
		
		//changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_BERKANAN, null), "Item Changes",
		//		""));
		
		changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 5th, 2014\n" +
				"_-_ 69 days after Pixel Dungeon v1.7.1\n" +
				"_-_ 9 days after v1.7.1 source release"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_EARTHROOT, null), "Seed Changes",
				"_-_ Blindweed buffed, now cripples as well as blinds\n\n" +
				"_-_ Sungrass nerfed, heal scales up over time, total heal reduced by combat\n\n" +
				"_-_ Earthroot nerfed, damage absorb down to 50% from 100%, total shield unchanged\n\n" +
				"_-_ Icecap buffed, freeze effect is now much stronger in water"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_SILVER, null), "Potion Changes",
				"_-_ Potion of Purity buffed, immunity duration increased to 10 turns from 5, clear effect radius increased\n\n" +
				"_-_ Potion of Frost buffed, freeze effect is now much stronger in water."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_BERKANAN, null), "Scroll Changes",
				"_-_ Scroll of Psionic blast reworked, now rarer and much stronger, but deals damage to the hero\n\n" +
				"_-_ Scroll of Challenge renamed to Scroll of Rage, now amoks nearby enemies"));

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second =false;
		for (ChangeInfo info : infos){
			if (info.major) {
				posY = nextPosY;
				second = false;
				info.setRect(0, posY, panel.innerWidth(), 0);
				content.add(info);
				posY = nextPosY = info.bottom();
			} else {
				if (!second){
					second = true;
					info.setRect(0, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = info.bottom();
				} else {
					second = false;
					info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = Math.max(info.bottom(), nextPosY);
					posY = nextPosY;
				}
			}
		}


		content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}

	private static class ChangeInfo extends Component {

		protected ColorBlock line;

		private RenderedText title;
		private boolean major;

		private RenderedTextMultiline text;

		private ArrayList<ChangeButton> buttons = new ArrayList<>();

		public ChangeInfo( String title, boolean majorTitle, String text){
			super();
			
			if (majorTitle){
				this.title = PixelScene.renderText( title, 9 );
				line = new ColorBlock( 1, 1, 0xFF222222);
				add(line);
			} else {
				this.title = PixelScene.renderText( title, 6 );
				line = new ColorBlock( 1, 1, 0xFF333333);
				add(line);
			}
			major = majorTitle;

			add(this.title);

			if (text != null && !text.equals("")){
				this.text = PixelScene.renderMultiline(text, 6);
				add(this.text);
			}

		}

		public void hardlight( int color ){
			title.hardlight( color );
		}

		public void addButton( ChangeButton button ){
			buttons.add(button);
			add(button);

			button.setSize(16, 16);
			layout();
		}

		public boolean onClick( float x, float y ){
			for( ChangeButton button : buttons){
				if (button.inside(x, y)){
					button.onClick();
					return true;
				}
			}
			return false;
		}

		@Override
		protected void layout() {
			float posY = this.y + 2;
			if (major) posY += 2;

			title.x = x + (width - title.width()) / 2f;
			title.y = posY;
			PixelScene.align( title );
			posY += title.baseLine() + 2;

			if (text != null) {
				text.maxWidth((int) width());
				text.setPos(x, posY);
				posY += text.height();
			}

			float posX = x;
			float tallest = 0;
			for (ChangeButton change : buttons){

				if (posX + change.width() >= right()){
					posX = x;
					posY += tallest;
					tallest = 0;
				}

				//centers
				if (posX == x){
					float offset = width;
					for (ChangeButton b : buttons){
						offset -= b.width();
						if (offset <= 0){
							offset += b.width();
							break;
						}
					}
					posX += offset / 2f;
				}

				change.setPos(posX, posY);
				posX += change.width();
				if (tallest < change.height()){
					tallest = change.height();
				}
			}
			posY += tallest + 2;

			height = posY - this.y;
			
			if (major) {
				line.size(width(), 1);
				line.x = x;
				line.y = y+2;
			} else if (x == 0){
				line.size(1, height());
				line.x = width;
				line.y = y;
			} else {
				line.size(1, height());
				line.x = x;
				line.y = y;
			}
		}
	}

	//not actually a button, but functions as one.
	private static class ChangeButton extends Component {

		protected Image icon;
		protected String title;
		protected String message;

		public ChangeButton( Image icon, String title, String message){
			super();
			
			this.icon = icon;
			add(this.icon);

			this.title = Messages.titleCase(title);
			this.message = message;

			layout();
		}

		public ChangeButton( Item item, String message ){
			this( new ItemSprite(item), item.name(), message);
		}

		protected void onClick() {
			ShatteredPixelDungeon.scene().add(new ChangesWindow(new Image(icon), title, message));
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + (width - icon.width) / 2f;
			icon.y = y + (height - icon.height) / 2f;
			PixelScene.align(icon);
		}
	}
	
	private static class ChangesWindow extends WndTitledMessage {
	
		public ChangesWindow( Image icon, String title, String message ) {
			super( icon, title, message);
			
			add( new TouchArea( chrome ) {
				@Override
				protected void onClick( Touchscreen.Touch touch ) {
					hide();
				}
			} );
			
		}
		
	}
}
