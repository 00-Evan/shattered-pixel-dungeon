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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class EscapeCrystal extends Item {

	{
		image = ItemSpriteSheet.ESCAPE;

		unique = true;

		defaultAction = AC_USE;
	}

	public static final String AC_USE = "USE";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals( AC_USE )) {

			if (Dungeon.level instanceof VaultLevel){

				//TODO actually do score calculation:
				//1000 points for 'exploring' (maybe just seeing rooms)
				//1000 points for collecting tokens and/or using them.
				//2000 points for defeating boss (maybe broken up a bit based on boss progression?)
				int score = 901;

				if (score == 0){
					GameScene.show(new WndTitledMessage(new ImpSprite(),
							Messages.titleCase(Messages.get(Imp.class, "name")),
							Messages.get(EscapeCrystal.class, "leaving_start")));
				} else {
					String message;
					if (score < 500)        message = Messages.get(EscapeCrystal.class, "leaving_early");
					else if (score < 900)   message = Messages.get(EscapeCrystal.class, "leaving_partly_explored");
					else if (score < 1500)  message = Messages.get(EscapeCrystal.class, "leaving_fully_explored");
					else if (score < 2500)  message = Messages.get(EscapeCrystal.class, "leaving_beat_miniboss");
					else                    message = Messages.get(EscapeCrystal.class, "leaving_victory");

					GameScene.show(new WndOptions(new ImpSprite(),
							Messages.titleCase(Messages.get(Imp.class, "name")),
							message,
							Messages.get(EscapeCrystal.class, "leaving_yes"),
							Messages.get(EscapeCrystal.class, "leaving_no")) {
						@Override
						protected void onSelect(int index) {
							if (index == 0) {
								if (score >= 500) {
									GameScene.selectItem(new WndBag.ItemSelector(){

										@Override
										public String textPrompt() {
											return "Select an Item";
										}

										@Override
										public boolean itemSelectable(Item item) {
											if (item instanceof EscapeCrystal){
												return false;
											}
											if (score < 900){
												return !(item instanceof EquipableItem || item instanceof Wand);
											} else if (score < 2500){
												//TODO enchants/curses
												return item.level() == 0;
											} else {
												return !item.unique;
											}
										}

										@Override
										public void onSelect(Item item) {
											if (item != null){

												leaveVault(item);
											}
										}
									});
								} else {
									leaveVault(null);
								}
							}
						}
					});
				}

			} else {
				if (storedItems == null || !storedItems.contains(BELONGINGS)){
					GameScene.show(new WndError(Messages.get(EscapeCrystal.class, "error_start") + "\n\n" +
							Messages.get(EscapeCrystal.class, "error_no_items")));
					detachAll(hero.belongings.backpack);
				} else {
					GameScene.show(new WndError(Messages.get(EscapeCrystal.class, "error_start") + "\n\n" +
							Messages.get(EscapeCrystal.class, "error_with_items")));
				}

			}

		}

	}

	private void leaveVault( Item preserve ){
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

		//remove all buffs/debuffs which don't persist over revives
		for (Buff b : Dungeon.hero.buffs()) {
			if (!b.revivePersists) {
				b.detach();
			}
		}

		restoreHeroBelongings(Dungeon.hero, preserve);
		Dungeon.hero.updateHT(false);
		detachAll(Dungeon.hero.belongings.backpack);
		Imp.Quest.complete();

		Level.beforeTransition();
		InterlevelScene.curTransition = new LevelTransition(Dungeon.level,
				Dungeon.hero.pos,
				LevelTransition.Type.BRANCH_ENTRANCE,
				Dungeon.depth,
				0,
				LevelTransition.Type.BRANCH_EXIT);
		InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
		Game.switchScene(InterlevelScene.class);
	}

	public static String BELONGINGS = "belongings";
	public static String QUICKSLOTS = "quickslots";
	public static String GOLD       = "gold";
	public static String ENERGY     = "energy";

	public void storeHeroBelongings( Hero hero ){
		storedItems = new Bundle();

		Bundle belongings = new Bundle();
		hero.belongings.storeInBundle(belongings);
		storedItems.put(BELONGINGS, belongings);

		Bundle quickslots = new Bundle();
		Dungeon.quickslot.storePlaceholders(quickslots);
		storedItems.put(QUICKSLOTS, quickslots);

		storedItems.put(GOLD, Dungeon.gold);
		storedItems.put(ENERGY, Dungeon.energy);

		Dungeon.quickslot.reset();
		QuickSlotButton.reset();
		Dungeon.gold = Dungeon.energy = 0;
		hero.belongings.clear();
	}

	public void restoreHeroBelongings( Hero hero, Item preserve ){
		hero.belongings.clear();

		Dungeon.quickslot.reset();
		Dungeon.quickslot.restorePlaceholders(storedItems.getBundle(QUICKSLOTS));
		QuickSlotButton.reset();

		Dungeon.hero.belongings.restoreFromBundle(storedItems.getBundle(BELONGINGS));

		Dungeon.gold = storedItems.getInt(GOLD);
		Dungeon.energy = storedItems.getInt(ENERGY);

		if (preserve != null){
			if (!preserve.collect()) {
				//if hero's inventory is full, the Imp drops the reward
				Imp.Quest.reward = preserve;
			}
		}

		storedItems = null;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public Bundle storedItems;

	public static String STORED_ITEMS = "stored_items";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STORED_ITEMS, storedItems);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		storedItems = bundle.getBundle(STORED_ITEMS);
	}
}
