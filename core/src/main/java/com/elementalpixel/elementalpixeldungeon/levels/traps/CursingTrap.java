/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.elementalpixel.elementalpixeldungeon.levels.traps;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.effects.CellEmitter;
import com.elementalpixel.elementalpixeldungeon.effects.particles.ShadowParticle;
import com.elementalpixel.elementalpixeldungeon.items.EquipableItem;
import com.elementalpixel.elementalpixeldungeon.items.Heap;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.KindOfWeapon;
import com.elementalpixel.elementalpixeldungeon.items.armor.Armor;
import com.elementalpixel.elementalpixeldungeon.items.weapon.Weapon;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.MagesStaff;
import com.elementalpixel.elementalpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;
import java.util.Collections;

public class CursingTrap extends Trap {

	{
		color = VIOLET;
		shape = WAVES;
	}

	@Override
	public void activate() {
		if (Dungeon.level.heroFOV[ pos ]) {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
			Sample.INSTANCE.play(Assets.Sounds.CURSED);
		}

		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null){
			for (Item item : heap.items){
				if (item.isUpgradable() && !(item instanceof MissileWeapon))
					curse(item);
			}
		}

		if (Dungeon.hero.pos == pos && !Dungeon.hero.flying){
			curse(Dungeon.hero);
		}
	}

	public static void curse(Hero hero){
		//items the trap wants to curse because it will create a more negative effect
		ArrayList<Item> priorityCurse = new ArrayList<>();
		//items the trap can curse if nothing else is available.
		ArrayList<Item> canCurse = new ArrayList<>();

		KindOfWeapon weapon = hero.belongings.weapon;
		if (weapon instanceof Weapon && !(weapon instanceof MagesStaff)){
			if (((Weapon) weapon).enchantment == null)
				priorityCurse.add(weapon);
			else
				canCurse.add(weapon);
		}

		Armor armor = hero.belongings.armor;
		if (armor != null){
			if (armor.glyph == null)
				priorityCurse.add(armor);
			else
				canCurse.add(armor);
		}

		Collections.shuffle(priorityCurse);
		Collections.shuffle(canCurse);

		if (!priorityCurse.isEmpty()){
			curse(priorityCurse.remove(0));
		} else if (!canCurse.isEmpty()){
			curse(canCurse.remove(0));
		}

		EquipableItem.equipCursed(hero);
		GLog.n( Messages.get(CursingTrap.class, "curse") );
	}

	private static void curse(Item item){
		item.cursed = item.cursedKnown = true;

		if (item instanceof Weapon){
			Weapon w = (Weapon) item;
			if (w.enchantment == null){
				w.enchant(Weapon.Enchantment.randomCurse());
			}
		}
		if (item instanceof Armor){
			Armor a = (Armor) item;
			if (a.glyph == null){
				a.inscribe(Armor.Glyph.randomCurse());
			}
		}
	}
}
