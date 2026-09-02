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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultTokenDoor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultBossElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultFinalRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

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
		ArrayList<String> actions = new ArrayList<>(); //no drop or throw
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals( AC_USE )) {

			if (Dungeon.level instanceof VaultLevel){

				//pre-v4.0.0 saves still in the vault tester area
				if (Imp.Quest.isOld()){
					leaveVault(null, 0);
					return;
				}

				int score = 0;

				//firstly, score is always a full 4k if the hero had the statue
				if (hero.belongings.getItem(ImpStatue.class) != null){
					score = 4000;
				} else {
					//otherwise there is partial score, to a max of 3k:

					//1,000 for exploring up to 80% of the level
					score += (int) (1000 * Dungeon.level.levelExplorePercent(Dungeon.depth));

					//1,000 for collecting tokens (100 each), plus a 250 bonus for opening the door
					boolean doorOpened = true;
					for (Char ch : Dungeon.level.mobs){
						if (ch instanceof VaultTokenDoor){
							doorOpened = false;
							break;
						}
					}
					if (doorOpened){
						score += 1250; //1000 for tokens, 250 for door
					} else {
						Item tokens = hero.belongings.getItem(DwarfToken.class);
						if (tokens != null){
							score += Math.min(1000, 100*tokens.quantity());
						}
					}

					//up to 750 for damaging/killing the boss elemental
					VaultFinalRoom r = (VaultFinalRoom) ((VaultLevel) Dungeon.level).room(VaultFinalRoom.class);
					if (r.elementalWasSummoned()){
						boolean elementalFound = false;
						for (Char ch : Dungeon.level.mobs){
							if (ch instanceof VaultBossElemental){
								elementalFound = true;
								score += (int) (750 * (ch.HP/(float)ch.HT));
								break;
							}
						}
						if (!elementalFound){
							//some poor sucker is absolutely going to kill the boss, not take the statue,
							// and then be forced to leave by a golem or something
							score += 750;
						}
					}

					//finally, score is rounded down to the nearest 50 points
					score = (score/50)*50;

				}

				if (score < 50){
					GameScene.show(new WndTitledMessage(new ImpSprite(),
							Messages.titleCase(Messages.get(Imp.class, "name")),
							Messages.get(EscapeCrystal.class, "leaving_start")));
				} else {
					String message;
					if (score < 500)        message = Messages.get(EscapeCrystal.class, "leaving_early");
					else if (score <= 1000) message = Messages.get(EscapeCrystal.class, "leaving_partly_explored");
					else if (score <= 2000) message = Messages.get(EscapeCrystal.class, "leaving_fully_explored");
					else if (score < 4000)  message = Messages.get(EscapeCrystal.class, "leaving_partial_victory");
					else                    message = Messages.get(EscapeCrystal.class, "leaving_victory");

					int finalScore = score;
					GameScene.show(new WndOptions(new ImpSprite(),
							Messages.titleCase(Messages.get(Imp.class, "name")),
							message,
							Messages.get(EscapeCrystal.class, "leaving_yes"),
							Messages.get(EscapeCrystal.class, "leaving_no")) {
						@Override
						protected void onSelect(int index) {
							if (index == 0) {
								if (finalScore >= 500) {
									GameScene.selectItem(new WndBag.ItemSelector(){

										@Override
										public String textPrompt() {
											return Messages.get(EscapeCrystal.class, "prompt");
										}

										@Override
										public boolean itemSelectable(Item item) {
											if (item instanceof EscapeCrystal){
												return false;
											}
											//lowest reward, just a consumable
											if (finalScore <= 1000){
												return !item.unique && !(item instanceof EquipableItem || item instanceof Wand);
											//mid rewards, item at a max of +0 or +1
											} else if (finalScore < 4000){
												int maxLevel = finalScore > 2000 ? 1 : 0;
												if (item instanceof MagesStaff){
													return ((MagesStaff) item).wandClass() != null
															&& item.level() <= maxLevel+1; //+1 to account for staff's level
												} else if (item instanceof Armor && ((Armor) item).checkSeal() != null){
													return item.level() <= maxLevel+1; //+1 to account for seal's level
												} else {
													return item.level() <= maxLevel && !item.unique;
												}
											} else {
												if (item instanceof MagesStaff){
													return ((MagesStaff) item).wandClass() != null;
												}
												return !item.unique;
											}
										}

										@Override
										public void onSelect(Item item) {
											if (item != null){

												String desc = Messages.get(EscapeCrystal.class, "leaving_item");

												if (item instanceof Armor && ((Armor) item).checkSeal() != null){
													desc += "\n\n" + Messages.get(EscapeCrystal.class, "leaving_seal");
												} else if (item instanceof MagesStaff){
													desc += "\n\n" + Messages.get(EscapeCrystal.class, "leaving_staff");
												//can only take 1 of a consumable item
												} if (item.quantity() > 0 && !(item instanceof EquipableItem)){
													item = item.duplicate().quantity(1);
												}

												Item finalItem = item;
												GameScene.show(new WndOptions(
														new ItemSprite(finalItem),
														Messages.titleCase(finalItem.title()),
														desc,
														Messages.get(EscapeCrystal.class, "leaving_yes"),
														Messages.get(EscapeCrystal.class, "leaving_no")){
													@Override
													protected void onSelect(int index) {
														if (index == 0){
															leaveVault(finalItem, finalScore);
														}
														super.onSelect(index);
													}
												});

											}
										}
									});
								} else {
									leaveVault(null, finalScore);
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

	private void leaveVault( Item preserve, int score ){
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

		Dungeon.hero.live(); //clears all non-persist buffs, resets hunger/regen
		Dungeon.hero.HP = Dungeon.hero.HT; //full heal

		//logic for removing Warrior's Seal or Mage's staff
		if (preserve instanceof Armor && ((Armor) preserve).checkSeal() != null){
			BrokenSeal seal = ((Armor) preserve).checkSeal();
			Armor.Glyph attached = seal.getGlyph();
			((Armor) preserve).detachSeal();
			//glyph always gets preserved
			if (((Armor) preserve).glyph == null && attached != null){
				((Armor) preserve).inscribe(attached);
			}
		} else if (preserve instanceof MagesStaff){
			Wand w = Reflection.newInstance(((MagesStaff) preserve).wandClass());
			w.identify(false);
			w.upgrade(preserve.level()-1);
			preserve = w;
		}

		restoreHeroBelongings(Dungeon.hero, preserve);
		Dungeon.hero.updateHT(false);
		detachAll(Dungeon.hero.belongings.backpack);
		if (!Imp.Quest.isOld()) Imp.Quest.complete(score);

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
		//we detach the item being preserved first, to cancel any equip-based buffs (e.g. wand charging)
		if (preserve != null) {
			preserve.detachAll(hero.belongings.backpack);
		}

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
