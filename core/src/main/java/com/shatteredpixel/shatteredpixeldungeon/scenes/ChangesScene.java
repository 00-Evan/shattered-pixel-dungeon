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
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.DewVial;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
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
		
		ChangeInfo changes = new ChangeInfo("v0.6.2", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.6.2e", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (caused by 0.6.2):\n" +
				"_-_ 'Exit Game' button corrupting saves on some devices"
				));
		
		changes = new ChangeInfo("v0.6.2c & v0.6.2d", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (caused by 0.6.2):\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Mimics causing crashes in certain cases\n" +
				"_-_ Game frequently hanging on some devices\n\n" +
				"Fixed (Existed prior to 0.6.2):\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Music volume being ignored in certain cases\n" +
				"_-_ Layout issues with enemy description windows\n" +
				"_-_ An exploit which allowed players to quit without saving by using splitscreen mode."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"_-_ Various Translation Updates"));
		
		changes = new ChangeInfo("v0.6.2b", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(new CloakOfShadows(),
				"Increased the base charge speed of the Cloak of Shadows by 20%, charge speed at higher levels unchanged."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Increased the brightness of the game's fog of war across all brightness settings.\n\n" +
				"_-_ Added more detailed information to historical updates in the changes list.\n\n" +
				"_-_ Wands that fire magical bolts now push on their detonation area, opening doors and trampling grass.\n\n" +
				"_-_ Improved the visuals of alchemy pots."));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (caused by 0.6.2):\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Distortion traps not clearing journal entries\n\n" +
				"Fixed (Existed prior to 0.6.2):\n" +
				"_-_ Various crash bugs\n" +
				"_-_ Game failing to save in rare cases\n" +
				"_-_ Mimics spawning over pits in rare cases\n" +
				"_-_ Game music not correctly pausing on android 2.2 and 2.3\n" +
				"_-_ Items being removed from quickslots when containers are bought"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"_-_ Fixed various text errors with venom\n" +
				"\n" +
				"_-_ Various Translation Updates\n\n" +
				"_-_ New Language: _Czech_"));
		
		
		changes = new ChangeInfo("v0.6.2a", false, "");
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
				"_-_ Game music now mutes itself during phone calls on android 6.0+"));
		
		changes.addButton( new ChangeButton(new Image(Assets.SPINNER, 144, 0, 16, 16), Messages.get(this, "bugfixes"),
				"Fixed (caused by 0.6.2):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Herbal healing and armor not greying out their icons correctly\n\n" +
				"Fixed (Existed prior to 0.6.2):\n" +
				"_-_ Various rare crash bugs\n" +
				"_-_ Gladiator being able to combo non-visible enemies"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(this, "language"),
				"In English:\n" +
				"_-_ Fixed missing text when dying to venom\n" +
				"\n" +
				"_-_ Translation Updates"));
		
		changes = new ChangeInfo(Messages.get(this, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 24th, 2017\n" +
				"_-_ 70 days after Shattered v0.6.1\n" +
				"\n" +
				"Commentary will be added here when this update is older."));
		
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
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 15th, 2017\n" +
				"_-_ 72 days after Shattered v0.6.0\n" +
				"\n" +
				"Commentary will be added here when this update is older."));
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
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 4th, 2017\n" +
				"_-_ 116 days after Shattered v0.5.0\n" +
				"\n" +
				"Commentary will be added here when this update is older."));
		
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
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 8th, 2017\n" +
				"_-_ 233 days after Shattered v0.4.0\n" +
				"_-_ 115 days after Shattered v0.4.3\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
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
		
		changes = new ChangeInfo( "v0.4.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.4.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 16th, 2016\n" +
				"_-_ 37 days after Shattered v0.4.2\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Added rankings and hall of heroes sync via Google Play Games, for the Google Play version of Shattered.\n\n" +
				"_-_ Added Power Saver mode in settings\n" +
				"_-_ Download size reduced by ~25%\n" +
				"_-_ Game now supports small screen devices\n" +
				"_-_ Performance improvements\n" +
				"_-_ Improved variety of level visuals"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.FLAIL, null), "Balance Changes",
				"_-_ Flail max damage increased by ~15%\n" +
				"_-_ Wand of Frost damage reduction increased from 5% per turn of chill to 7.5%\n" +
				"_-_ Ring of Furor speed bonus reduced by ~15% for slow weapons, ~0% for fast weapons\n" +
				"_-_ Reduced sacrificial curse bleed by ~33%\n" +
				"_-_ Reworked glyph of brimstone, now grants shielding instead of healing\n" +
				"_-_ Reworked glyph of stone, now reduces speed in doorways\n" +
				"_-_ Thrown potions now trigger traps and plants"));
		
		changes = new ChangeInfo("v0.4.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 9th, 2016\n" +
				"_-_ 46 days after Shattered v0.4.1\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Technical Improvements",
				"_-_ Many general performance improvements\n" +
				"_-_ Game now uses 2 CPU cores, up from 1\n" +
				"_-_ Reduced hitching on many devices\n" +
				"_-_ Framerate improvements for older devices\n" +
				"_-_ Game size reduced by ~10%"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Glaive()), "Balance Changes",
				"_-_ Spear and Glaive damage reduced\n" +
				"_-_ Runic blade damage reduced\n" +
				"_-_ Grim enchant now procs more often\n" +
				"_-_ Glyph of stone adds more weight\n" +
				"_-_ Glyph of potential procs less often\n" +
				"_-_ Wand of Fireblast less dangerous to caster\n" +
				"_-_ Wand of Pris. Light reveal area reduced\n" +
				"_-_ Ring of Wealth slightly more effective\n" +
				"_-_ Ring of Sharpshooting gives more accuracy"));
		
		changes = new ChangeInfo("v0.4.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released July 25th, 2016\n" +
				"_-_ 35 days after Shattered v0.4.0\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new PlateArmor()), "Item Changes pt.1",
				"Armor and Enemy Balance Changes:\n" +
				"_-_ Armor now has a min damage block value\n" +
				"_-_ Armor gains more blocking from upgrades\n" +
				"_-_ Prison+ enemy damage increased\n" +
				"_-_ Evil Eyes reworked\n" +
				"_-_ Brimstone glyph healing reduced\n" +
				"\n" +
				"Class Balance Changes:\n" +
				"_-_ Mage's Staff melee damage increased\n" +
				"_-_ Mage's Staff can now preserve one upgrade\n" +
				"_-_ Cloak of Shadows buffed at lower levels\n" +
				"_-_ Some Battlemage effects changed\n" +
				"\n" +
				"Wand Balance Changes:\n" +
				"_-_ All wands damage adjusted/increased\n" +
				"_-_ Upgraded wands appear slightly less often\n" +
				"_-_ Wand of Lightning bonus damage reduced\n" +
				"_-_ Wand of Fireblast uses fewer charges\n" +
				"_-_ Wand of Venom damage increases over time\n" +
				"_-_ Wand of Prismatic Light bonus damage reduced\n" +
				"_-_ Corrupted enemies live longer & no longer attack eachother\n" +
				"_-_ Wands in the holster now charge faster"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new RunicBlade()), "Item Changes pt.2",
				"Ring Balance Changes:\n" +
				"_-_ Ring of Force weaker at 18+ strength, stronger otherwise\n" +
				"_-_ Ring of Tenacity reduces more damage\n" +
				"_-_ Ring of Wealth secret rewards adjusted\n" +
				"_-_ Ring of Evasion now works consistently\n" +
				"\n" +
				"Artifact Balance Changes:\n" +
				"_-_ Dried Rose charges faster, ghost HP up\n" +
				"_-_ Horn of Plenty now charges on exp gain\n" +
				"_-_ Master Thieves Armband levels faster\n" +
				"_-_ Sandals of Nature level faster\n" +
				"_-_ Hourglass uses fewer charges at a time\n" +
				"_-_ Horn of Plenty adjusted, now stronger\n" +
				"\n" +
				"Weapon Balance Changes:\n" +
				"_-_ Lucky enchant deals max dmg more often\n" +
				"_-_ Dazzling enchant now cripples & blinds\n"+
				"_-_ Flail now can't surprise attack, damage increased\n" +
				"_-_ Extra reach weapons no longer penetrate\n" +
				"_-_ Runic blade damage decreased"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Added a new journal button with key display\n" +
				"_-_ Keys now exist in the journal, not inventory\n" +
				"_-_ Improved donator menu button visuals\n" +
				"_-_ Increased the efficiency of data storage\n" +
				"_-_ Chasms now deal less damage, but bleed\n" +
				"_-_ Many shop prices adjusted\n" +
				"_-_ Pirahna rooms no longer give cursed gear"));
		
		changes = new ChangeInfo("v0.4.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released June 20th, 2016\n" +
				"_-_ 391 days after Shattered v0.3.0\n" +
				"_-_ 50 days after Shattered v0.3.5\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Longsword()), "Equipment Overhaul!",
				"_-_ 13 new weapons, 12 rebalanced weapons\n" +
				"\n" +
				"Equipment Balance:\n" +
				"_-_ Tier 2-4 weapons do more base damage\n" +
				"_-_ All weapons gain more dmg from upgrades\n" +
				"_-_ Upgrades now remove enchants less often\n" +
				"_-_ Upgrades reduce str requirements less\n" +
				"_-_ All armors require 1 more str\n" +
				"_-_ Encumbered characters can't sneak attack\n" +
				"\n" +
				"Droprate Changes:\n" +
				"_-_ Powerful equipment less common early\n" +
				"_-_ +3 and +2 equipment less common\n" +
				"_-_ Equipment curses more common\n" +
				"_-_ Tier 1 equipment no longer drops\n" +
				"_-_ Arcane styli slightly more common\n" +
				"_-_ Better item drops on floors 22-24"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Stylus()), "Curse, Enchant, & Glyph Overhaul!",
				"_-_ 3 new enchants, 10 rebalanced enchants\n" +
				"_-_ 8 new glyphs, 5 rebalanced glyphs\n" +
				"_-_ 12 new curse effects\n" +
				"\n" +
				"Equipment Curses:\n" +
				"_-_ Curses now give negative effects\n" +
				"_-_ Curses no longer give negative levels\n" +
				"_-_ Upgrades now weaken curses\n" +
				"_-_ Remove curse scrolls now affect 1 item"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Class Balance:\n" +
				"_-_ Huntress now starts with knuckleduster\n" +
				"_-_ Assassin sneak bonus damage reduced\n" +
				"_-_ Fixed a bug where berserker was immune when enraged\n" +
				"_-_ Gladiator's clobber now inflicts vertigo, not stun\n" +
				"\n" +
				"Enemy Balance:\n" +
				"_-_ Tengu damage increased\n" +
				"_-_ Prison Guard health and DR increased\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Scrolls of upgrade no longer burn\n" +
				"_-_ Potions of strength no longer freeze\n" +
				"_-_ Translation updates\n" +
				"_-_ Various bugfixes"));
		
		//**********************
		//       v0.3.X
		//**********************
		
		changes = new ChangeInfo( "v0.3.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.3.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 1st, 2016\n" +
				"_-_ 81 days after Shattered v0.3.4\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 15, 12, 15), "Warrior Rework!",
				"Warrior Rework:\n" +
				"_-_ Starting STR down to 10, from 11\n" +
				"_-_ Short sword dmg down to 1-10, from 1-12\n" +
				"_-_ Short sword can no longer be reforged\n" +
				"_-_ Now IDs potions of health, not STR\n" +
				"_-_ Now starts with a unique seal for armor\n" +
				"_-_ Seal grants shielding ontop of health\n" +
				"_-_ Seal allows for one upgrade transfer"));
		
		changes.addButton( new ChangeButton(new Image(Assets.WARRIOR, 0, 90, 12, 15), "Warrior Subclass Rework!",
				"Berserker Rework:\n" +
				"_-_ Bonus damage now scales with lost HP, instead of a flat 50% at 50% hp\n" +
				"_-_ Berserker can now endure through death for a short time, with caveats\n" +
				"\n" +
				"Gladiator Rework:\n" +
				"_-_ Combo no longer grants bonus damage\n" +
				"_-_ Combo is now easier to stack\n" +
				"_-_ Combo now unlocks special finisher moves"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Balance Tweaks:\n" +
				"_-_ Spears can now reach enemies 1 tile away\n" +
				"_-_ Wand of Blast Wave now pushes bosses less\n" +
				"\n" +
				"Misc:\n" +
				"_-_ Can now examine multiple things in one tile\n" +
				"_-_ Pixelated font now available for cyrillic languages\n" +
				"_-_ Added Hungarian language"));
		
		changes = new ChangeInfo("v0.3.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 10th, 2016\n" +
				"_-_ 54 days after Shattered v0.3.3\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "Translations!",
				"Shattered Pixel Dungeon now supports multiple languages, thanks to a new community translation project!\n\n" +
				"The Following languages are supported at release:\n" +
				"_-_ English\n" +
				"_-_ Russian\n" +
				"_-_ Korean\n" +
				"_-_ Chinese\n" +
				"_-_ Portugese\n" +
				"_-_ German\n" +
				"_-_ French\n" +
				"_-_ Italian\n" +
				"_-_ Polish\n" +
				"_-_ Spanish"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Completely redesigned the text rendering system to support none-english characters\n\n" +
				"New text system renders using either the default system font, or the original pixelated game font. None-latin languages must use system font.\n\n" +
				"Balance Changes:\n" +
				"_-_ Hunger now builds ~10% slower\n" +
				"_-_ Sad Ghost no longer gives tier 1 loot\n" +
				"_-_ Sad Ghost gives tier 4/5 loot less often\n" +
				"_-_ Burning now deals less damage at low HP\n" +
				"_-_ Weakness no longer discharges wands\n" +
				"_-_ Rockfall traps rebalanced"));
		
		changes = new ChangeInfo("v0.3.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released December 18th, 2015\n" +
				"_-_ 44 days after Shattered v0.3.2\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), "Google Play Games",
				"Added support for Google Play Games in the Google Play version:\n\n" +
				"- Badges can now sync across devices\n" +
				"- Five Google Play Achievements added\n" +
				"- Rankings sync will come in future\n\n" +
				"Shattered remains a 100% offline game if Google Play Games is not enabled"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Gameplay Changes:\n" +
				"- Tengu's maze is now different each time\n" +
				"- Items no longer auto-pickup when enemies are near\n" +
				"\n" +
				"Fixes:\n" +
				"- Fixed several bugs with prison enemies\n" +
				"- Fixed some landscape window size issues\n" +
				"- Fixed other minor bugs\n" +
				"\n" +
				"Misc:\n" +
				"- Added support for reverse landscape\n" +
				"- Added a small holiday treat ;)\n" +
				"- Thieves now disappear when they get away"));
		
		changes = new ChangeInfo("v0.3.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 4th, 2015\n" +
				"_-_ 79 days after Shattered v0.3.1\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TENGU, 0, 0, 14, 16), "Prison Rework",
				"_-_ Tengu boss fight completely redone\n" +
				"_-_ Corpse dust quest overhauled\n" +
				"_-_ Rotberry quest overhauled\n" +
				"_-_ NEW elemental embers quest\n" +
				"_-_ NEW prison mob: insane prison guards!\n" +
				"_-_ Thieves can escape with a stolen item\n" +
				"_-_ Gnoll shaman attack speed increased"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY, null), "Balance Changes",
				"_-_ Mastery Book now always at floor 10, even when unlocked\n" +
				"_-_ Hunger damage now increases with hero level, starts lower\n" +
				"\n" +
				"Sewers rebalance: \n" +
				"_-_ Marsupial rat dmg and evasion reduced\n" +
				"_-_ Gnoll scout accuracy reduced\n" +
				"_-_ Sewer crabs less likely to spawn on floor 3, grant more exp\n" +
				"_-_ Fly swarms rebalanced, moved to the sewers\n" +
				"_-_ Great Crab HP reduced \n" +
				"_-_ Goo fight rebalanced \n" +
				" \n" +
				"Base Class Changes: \n" +
				"_-_ Mage's staff base damage increased \n" +
				"_-_ Huntress now starts with 20 hp \n" +
				"_-_ Huntress no longer heals more from dew \n" +
				"_-_ Rogue's cloak of shadows now drains less while invisible\n" +
				" \n" +
				"Subclass Changes: \n" +
				"_-_ Berserker now starts raging at 50% hp (up from 40%) \n" +
				"_-_ Warden now heals 2 extra HP from dew \n" +
				"_-_ Warlock completely overhauled"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Visual improvements from 1.9.1 source\n" +
				"_-_ Improved golden UI for donators\n" +
				"_-_ Fixed 'white line' graphical artifacts\n" +
				"_-_ Floor locking now pauses all passive effects, not just hunger\n" +
				"_-_ Cursed chains now only cripple, do not root\n" +
				"_-_ Warping trap rebalanced, much less harsh\n" +
				"_-_ Various other bugfixes"));
		
		changes = new ChangeInfo("v0.3.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 17th, 2015\n" +
				"_-_ 83 days after Shattered v0.3.0\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 112, 96, 16, 16), "Trap Overhaul",
				"_-_ Over 20 new traps + tweaks to existing ones\n" +
				"_-_ Trap visuals overhauled\n" +
				"_-_ Traps now get trickier deeper in the dungeon\n" +
				"_-_ Trap room reworked to make use of new traps"));
		
		changes.addButton( new ChangeButton(new Image(Assets.MENU, 15, 0, 16, 15), "Interface Improvements",
				"_-_ Adjusted display scaling\n" +
				"_-_ Search and Examine merged into one button (double tap to search)\n" +
				"_-_ New max of 4 Quickslots!\n" +
				"_-_ Multiple toolbar modes for large display and landscape users\n" +
				"_-_ Ability to flip toolbar and indicators (left-handed mode)\n" +
				"_-_ Better settings menu\n" +
				"_-_ Graphics settings are now accessible ingame\n" +
				"_-_ More consistent text rendering\n" +
				"_-_ Recent changes can now be viewed from the title screen\n" +
				"_-_ Added a health bar for bosses"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"Balance changes:\n" +
				"_-_ Ethereal chains now gain less charge the more charges they have\n" +
				"_-_ Staff of regrowth grants more herbal healing\n" +
				"_-_ Monks now disarm less randomly, but not less frequently\n" +
				"_-_ Invisibility potion effect now lasts for 20 turns, up from 15\n\n" +
				"QOL improvements:\n" +
				"_-_ Quickslots now autotarget enemies\n" +
				"_-_ Resting now works while hungry & at max HP\n" +
				"_-_ Dew drops no longer collect when at full health with no dew vial\n" +
				"_-_ Items now stay visible in the fog of war\n" +
				"_-_ Added a visual hint to weak floor rooms\n" +
				"_-_ Many bugfixes"));
		
		changes = new ChangeInfo("v0.3.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released May 26th, 2015\n" +
				"_-_ 253 days after Shattered v0.2.0\n" +
				"_-_ 92 days after Shattered v0.2.4\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Image(Assets.MAGE, 0, 15, 12, 15), "Mage Rework!",
				"_-_ No longer starts with knuckledusters or a wand\n" +
				"_-_ Can no longer equip wands\n" +
				"_-_ Now starts with a unique mages staff, empowered with magic missile to start.\n\n" +
				"_-_ Battlemage reworked, staff now deals bonus effects when used as a melee weapon.\n\n" +
				"_-_ Warlock reworked, gains more health and fullness from gaining exp, but food no longer restores hunger."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_DISINTEGRATION, null), "Wand Rework!",
				"Removed Wands:\n" +
				"Flock, Blink, Teleportation, Avalanche\n" +
				"\n" +
				"Reworked Wands:\n" +
				"Magic Missile, Lightning, Disintegration,\n" +
				"Fireblast (was Firebolt), Venom (was Poison),\n" +
				"Frost (was Slowing), Corruption (was Amok),\n" +
				"Blast Wave (was Telekinesis), Regrowth\n" +
				"\n" +
				"New Wands:\n" +
				"Prismatic Light, Transfusion\n" +
				"\n" +
				"_-_ Wand types are now known by default.\n" +
				"_-_ Wands now each have unique sprites.\n" +
				"_-_ Wands now cap at 10 charges instead of 9\n" +
				"_-_ Wands now recharge faster the more charges are missing.\n" +
				"_-_ Self-targeting with wands is no longer possible.\n" +
				"_-_ Wand recharge effects now give charge over time.\n" +
				"_-_ Wands can now be cursed!"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"New Artifacts:\n" +
				"_-_ Ethereal Chains (replaces wand of blink)\n" +
				"_-_ Lloyd's Beacon (replaces wand of teleportation)\n" +
				"\n" +
				"Misc. Balance changes:\n" +
				"_-_ Blessed Ankhs now revive at 1/4hp, but also grant initiative.\n" +
				"_-_ Alchemist's Toolkit removed (will be reworked)\n" +
				"_-_ Chalice of blood nerfed, now regens less hp at high levels.\n" +
				"_-_ Cape of Thorns buffed, now absorbs all damage, but only deflects adjacent attacks.\n" +
				"_-_ Sandals of nature adjusted, now give more seeds, less dew.\n" +
				"_-_ Hunger no longer increases while fighting bosses.\n" +
				"_-_ Floor 1 now spawns 10 rats, exactly enough to level up.\n" +
				"_-_ Scrolls of recharging and mirror image now more common.\n" +
				"_-_ Mimics are now less common, stronger, & give better loot.\n" +
				"\n" +
				"UI tweaks:\n" +
				"_-_ New app icon!\n" +
				"_-_ Shading added to main game interface\n" +
				"_-_ Buffs now have descriptions, tap their icons!\n" +
				"_-_ Visual indicator added for surprising enemies"));
		
		//**********************
		//       v0.2.X
		//**********************
		
		changes = new ChangeInfo( "v0.2.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.2.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released February 23rd, 2015\n" +
				"_-_ 48 days after Shattered v0.2.3\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Honeypot()), "Pixel Dungeon v1.7.5",
				"v1.7.3 - v1.7.5 Source Implemented, with exceptions:\n" +
				"_-_ Degredation not implemented.\n\n" +
				"_-_ Badge syncing not implemented.\n\n" +
				"_-_ Scroll of Weapon Upgrade renamed to Magical Infusion, works on armor.\n\n" +
				"_-_ Scroll of Enchantment not implemented, Arcane stylus has not been removed.\n\n" +
				"_-_ Honey pots now shatter in a new item: shattered honeypot. A bee will defend its shattered pot to the death against anything that gets near.\n\n" +
				"_-_ Bombs have been reworked/nerfed: they explode after a delay, no longer stun, deal more damage at the center of the blast, affect the world (destroy items, blow up other bombs)."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BANDOLIER, null), "New Content",
				"_-_ The huntress has been buffed: starts with Potion of Mind Vision identified, now benefits from strength on melee attacks, and has a chance to reclaim a single used ranged weapon from each defeated enemy.\n\n" +
				"_-_ A new container: The Potion Bandolier! Potions can now shatter from frost, but the bandolier can protect them.\n\n" +
				"_-_ Shops now stock a much greater variety of items, some item prices have been rebalanced.\n\n" +
				"_-_ Added Merchant's Beacon.\n\n" +
				"_-_ Added initials for IDed scrolls/potions."));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Going down stairs no longer increases hunger, going up still does.\n\n" +
				"_-_ Many, many bugfixes.\n" +
				"_-_ Some UI improvements.\n" +
				"_-_ Ingame audio quality improved.\n" +
				"_-_ Unstable spellbook buffed.\n" +
				"_-_ Psionic blasts deal less self-damage.\n" +
				"_-_ Potions of liquid flame affect a 3x3 grid."));
		
		changes = new ChangeInfo("v0.2.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released January 6th, 2015\n" +
				"_-_ 64 days after Shattered v0.2.2\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new TimekeepersHourglass()), "Artifact Changes",
				"Added 4 new artifacts:\n" +
				"_-_ Alchemist's Toolkit\n" +
				"_-_ Unstable Spellbook\n" +
				"_-_ Timekeeper's Hourglass\n" +
				"_-_ Dried Rose\n\n" +
				"_-_ Artifacts are now unique over each run\n" +
				"_-_ Artifacts can now be cursed!\n" +
				"_-_ Cloak of Shadows is now exclusive to the rogue\n" +
				"_-_ Smaller Balance Changes and QOL improvements to almost every artifact"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_CRIMSON, null), "Balance Changes",
				"_-_ Health potion farming has been nerfed from all sources\n" +
				"_-_ Freerunner now moves at very high speeds when invisible\n" +
				"_-_ Ring of Force buffed significantly\n" +
				"_-_ Ring of Evasion reworked again\n" +
				"_-_ Improved the effects of some blandfruit types\n" +
				"_-_ Using throwing weapons now cancels stealth"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"_-_ Implement a donation system in the Google Play version of Shattered\n\n" +
				"_-_ Significantly increased the stability of the save system\n\n" +
				"_-_ Increased the number of visible rankings to 11 from 6\n\n" +
				"_-_ Improved how the player is interrupted by hermful events"));
		
		changes = new ChangeInfo("v0.2.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released November 3rd, 2014\n" +
				"_-_ 21 days after Shattered v0.2.1\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.WEIGHT, null), "Pixel Dungeon v1.7.2",
				"Implemented directly from v1.7.2:\n" +
				"_-_ Synchronous Movement\n" +
				"_-_ Challenges\n" +
				"_-_ UI improvements\n" +
				"_-_ Vertigo debuff\n\n" +
				"Implement with changes:\n" +
				"_-_ Weightstone: Increases either speed or damage, at the cost of the other, instead of increasing either speed or accuracy.\n\n" +
				"Not Implemented:\n" +
				"_-_ Key ring and unstackable keys\n" +
				"_-_ Blindweed has not been removed"));
		
		changes.addButton( new ChangeButton(new Image(Assets.TERRAIN_FEATURES, 144, 112, 16, 16), "New Plants",
				"Added two new plants:\n" +
				"_-_ Stormvine, which brews into levitation\n" +
				"_-_ Dreamfoil, which brews into purity\n\n" +
				"_-_ Potion of levitation can now be thrown to make a cloud of confusion gas\n\n" +
				"_-_ Removed gas collision logic, gasses can now stack without limitation."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.REMAINS, null), "Heroes Remains",
				"Heroes remains have been significantly adjusted to prevent strategies that exploit them, but also to increase their average loot.\n\n" +
				"Remains have additional limitations:\n" +
				"_-_ Heros will no longer drop remains if they have obtained the amulet of yendor, or die 5 or more floors above the deepest floor they have reached\n" +
				"_-_ Class exclusive items can no longer appear in remains\n" +
				"_-_ Items found in remains are now more harshly level-capped\n" +
				"_-_ Remains are not dropped, and cannot be found, when challenges are enabled.\n\n" +
				"However:\n" +
				"_-_ Remains can now contain useful items from the inventory, not just equipped items.\n" +
				"_-_ Remains are now less likely to contain gold."));
		
		changes = new ChangeInfo("v0.2.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released October 13th, 2014\n" +
				"_-_ 28 days after Shattered v0.2.0\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Image(Assets.GHOST, 0, 0, 14, 15), "New Sewer Quests",
				"_-_ Removed the dried rose quest (the rose will return...)\n\n" +
				"_-_ Tweaked the mechanics of the fetid rat quest\n\n" +
				"_-_ Added a gnoll trickster quest\n\n" +
				"_-_ Added a great crab quest"));
		
		changes.addButton( new ChangeButton(new Image(Assets.GOO, 43, 3, 14, 11), "Goo Changes",
				"Goo's animations have been overhauled, including a particle effect for the area of its pumped up attack.\n\n" +
				"Goo's arena has been updated to give more room to maneuver, and to be more variable."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUIDE_PAGE, null), "Story & Signpost Changes",
				"Most text in the sewers has been overhauled, including descriptions, quest dialogues, signposts, and story scrolls"));
		
		changes = new ChangeInfo("v0.2.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released September 15th, 2014\n" +
				"_-_ 31 days after Shattered v0.1.1"));
		
		changes.addButton( new ChangeButton(new ItemSprite(new HornOfPlenty()), "Artifacts!",
				"Added artifacts to the game!\n\n" +
				"Artifacts are unique items which offer new gameplay opportunities and grow stronger through unique means.\n\n" +
				"Removed 7 Rings... And Replaced them with 7 Artifacts!\n" +
				"_-_ Ring of Shadows becomes Cloak of Shadows\n" +
				"_-_ Ring of Satiety becomes Horn of Plenty\n" +
				"_-_ Ring of Mending becomes Chalice of Blood\n" +
				"_-_ Ring of Thorns becomes Cape of Thorns\n" +
				"_-_ Ring of Haggler becomes Master Thieves' Armband\n" +
				"_-_ Ring of Naturalism becomes Sandals of Nature"));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null), "New Rings!",
				"To replace the lost rings, 6 new rings have been added:\n" +
				"_-_ Ring of Force\n" +
				"_-_ Ring of Furor\n" +
				"_-_ Ring of Might\n" +
				"_-_ Ring of Tenacity\n" +
				"_-_ Ring of Sharpshooting\n" +
				"_-_ Ring of Wealth\n\n" +
				"The 4 remaining rings have also been tweaked or reworked entirely:\n" +
				"_-_ Ring of Accuracy\n" +
				"_-_ Ring of Elements\n" +
				"_-_ Ring of Evasion\n" +
				"_-_ Ring of Haste"));
		
		changes.addButton( new ChangeButton(Icons.get(Icons.PREFS), Messages.get(this, "misc"),
				"-Nerfed farming health potions from fly swarms.\n\n" +
				"-Buffed crazed bandit and his drops.\n\n" +
				"-Made Blandfruit more common.\n\n" +
				"-Nerfed Assassin bonus damage slightly, to balance with him having an artifact now.\n\n" +
				"-Added a welcome page!"));
		
		changes = new ChangeInfo(" ", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		//**********************
		//       v0.1.X
		//**********************
		
		changes = new ChangeInfo( "v0.1.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		infos.add(changes);
		
		changes = new ChangeInfo("v0.1.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 15th, 2014\n" +
				"_-_ 10 days after Shattered v0.1.0\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new Blandfruit(),
				"Players who chance upon gardens or who get lucky while trampling grass may come across a new plant: the _Blandfruit._\n\n" +
				"As the name implies, the fruit from this plant is pretty unexceptional, and will barely do anything for you on its own. Perhaps there is some way to prepare the fruit with another ingredient..."));
		
		changes.addButton( new ChangeButton(new ItemSprite(new Ankh()), "Revival Item Changes",
				"When the Dew Vial was initially added to Pixel Dungeon, its essentially free revive made ankhs pretty useless by comparison. " +
				"To fix this, both items have been adjusted to combine to create a more powerful revive.\n\n" +
				"Dew Vial nerfed:\n" +
				"_-_ Still grants a full heal at full charge, but grants less healing at partial charge.\n" +
				"_-_ No longer revives the player if they die.\n\n" +
				"Ankh buffed:\n" +
				"_-_ Can now be blessed with a full dew vial, to gain the vial's old revive effect."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_BERKANAN, null), "Misc Item Changes",
				"Sungrass buffed:\n" +
				"_-_ Heal scaling now scales with max hp.\n\n" +
				"Scroll of Psionic Blast rebalanced:\n" +
				"_-_ Now deals less self damage, and damage is more consistent.\n" +
				"_-_ Duration of self stun/blind effect increased.\n\n" +
				"Scroll of lullaby reworked:\n" +
				"_-_ No longer instantly sleeps enemies, now afflicts them with drowsy, which turns into magical sleep.\n" +
				"_-_ Magically slept enemies will only wake up when attacked.\n" +
				"_-_ Hero is also affected, and will be healed by magical sleep."));
		
		changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		infos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "Developer Commentary",
				"_-_ Released August 5th, 2014\n" +
				"_-_ 69 days after Pixel Dungeon v1.7.1\n" +
				"_-_ 9 days after v1.7.1 source release\n" +
				"\n" +
				"More dev commentary will be added here soon."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_EARTHROOT, null), "Seed Changes",
				"_-_ Blindweed buffed, now cripples as well as blinds.\n\n" +
				"_-_ Sungrass nerfed, heal scales up over time, total heal reduced by combat.\n\n" +
				"_-_ Earthroot nerfed, damage absorb down to 50% from 100%, total shield unchanged.\n\n" +
				"_-_ Icecap buffed, freeze effect is now much stronger in water."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_SILVER, null), "Potion Changes",
				"_-_ Potion of Purity buffed, immunity duration increased to 10 turns from 5, clear effect radius increased.\n\n" +
				"_-_ Potion of Frost buffed, freeze effect is now much stronger in water."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_BERKANAN, null), "Scroll Changes",
				"_-_ Scroll of Psionic blast reworked, now rarer and much stronger, but deals damage to the hero.\n\n" +
				"_-_ Scroll of Challenge renamed to Scroll of Rage, now amoks nearby enemies."));

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
