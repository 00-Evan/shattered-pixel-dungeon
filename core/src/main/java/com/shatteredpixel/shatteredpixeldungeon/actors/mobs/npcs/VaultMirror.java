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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VaultMirrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class VaultMirror extends NPC {

	{
		spriteClass = VaultMirrorSprite.class;
	}

	public Item reward = null;

	public void createReward(HeroClass cls){
		switch (cls) {
			case WARRIOR:
				reward = new BrokenSeal().upgrade().identify(false);
				((BrokenSeal)reward).setGlyph(Armor.Glyph.random());
				break;
			case MAGE:
				reward = new MagesStaff().upgrade(3).identify(false);
				((MagesStaff)reward).enchant();
				break;
			case ROGUE:
				reward = new CloakOfShadows().upgrade(8).identify(false);
				((CloakOfShadows) reward).directCharge(8);
				break;
			case HUNTRESS:
				reward = new SpiritBow().identify(false);
				((SpiritBow)reward).enchant();
				break;
			case DUELIST:
				reward = new MirrorSword().upgrade(3).identify(false);
				((MeleeWeapon)reward).enchant();
				break;
			case CLERIC:
				reward = new HolyTome().upgrade(8).identify(false);
				((HolyTome) reward).directCharge(8);
				break;
		}
	}

	@Override
	public boolean interact(Char c) {
		if (c instanceof Hero) {
			ShatteredPixelDungeon.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					if (reward != null) {

						String sceneText = Messages.get(VaultMirror.class, "approach") + "\n\n";
						switch (((Hero) c).heroClass){
							case WARRIOR:
								sceneText += Messages.get(VaultMirror.class, "scene_warrior");
								break;
							case MAGE:
								sceneText += Messages.get(VaultMirror.class, "scene_mage");
								break;
							case ROGUE:
								sceneText += Messages.get(VaultMirror.class, "scene_rogue");
								break;
							case HUNTRESS:
								sceneText += Messages.get(VaultMirror.class, "scene_huntress");
								break;
							case DUELIST:
								sceneText += Messages.get(VaultMirror.class, "scene_duelist");
								break;
							case CLERIC:
								sceneText += Messages.get(VaultMirror.class, "scene_cleric");
								break;
						}
						sceneText += "\n\n" + Messages.get(VaultMirror.class, "scene_final");

						GameScene.show(new WndOptions(sprite(),
								Messages.titleCase(name()),
								sceneText,
								Messages.get(VaultMirror.class, "take")) {
							@Override
							protected void onSelect(int index) {
								super.onSelect(index);
								if (index == 0) {
									GameScene.show(new WndTitledMessage(sprite(), Messages.titleCase(name()), Messages.get(VaultMirror.class, "scene_take")));
									if (!reward.doPickUp((Hero) c)) {
										Dungeon.level.drop(reward, c.pos).sprite.drop();
									}
									Imp.Quest.mirrorUsed = true;
									reward = null;
								}
							}
						});
					} else {
						GameScene.show(new WndTitledMessage(sprite(), Messages.titleCase(name()), Messages.get(VaultMirror.class, "scene_nothing")));
					}
				}
			});
		}
		return false;
	}

	private static final String REWARD = "reward";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (reward != null) {
			bundle.put(REWARD, reward);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(REWARD)){
			reward = (Item) bundle.get(REWARD);
		}
	}

	public static class MirrorSword extends Greatsword {

		{
			//cannot be taken out of the vault
			unique = true;
		}

	}

}
