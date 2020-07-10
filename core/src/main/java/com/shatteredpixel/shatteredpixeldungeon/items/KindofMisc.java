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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;


public abstract class KindofMisc extends EquipableItem {

	private static final float TIME_TO_EQUIP = 1f;

	@Override
	public boolean doEquip(final Hero hero) {

		boolean equipFull = false;
		if ( this instanceof Artifact
				&& hero.belongings.artifact != null
				&& hero.belongings.misc != null){
			equipFull = true;
		} else if (this instanceof Ring
				&& hero.belongings.misc != null
				&& hero.belongings.ring != null){
			equipFull = true;
		}

		if (equipFull) {

			final KindofMisc m1;
			final KindofMisc m2;
			if (this instanceof Artifact){
				m1 = hero.belongings.artifact;
				m2 = hero.belongings.misc;
			} else {
				m1 = hero.belongings.misc;
				m2 = hero.belongings.ring;
			}

			GameScene.show(
					new WndOptions(Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							Messages.titleCase(m1.toString()),
							Messages.titleCase(m2.toString())) {

						@Override
						protected void onSelect(int index) {

							KindofMisc equipped = (index == 0 ? m1 : m2);
							int slot = Dungeon.quickslot.getSlot(KindofMisc.this);
							detach(hero.belongings.backpack);
							if (equipped.doUnequip(hero, true, false)) {
								doEquip(hero);
							} else {
								collect();
							}
							if (slot != -1) Dungeon.quickslot.setSlot(slot, KindofMisc.this);
						}
					});

			return false;

		} else {

			if (this instanceof Artifact){
				if (hero.belongings.artifact == null)   hero.belongings.artifact = (Artifact) this;
				else                                    hero.belongings.misc = (Artifact) this;
			} else if (this instanceof Ring){
				if (hero.belongings.ring == null)   hero.belongings.ring = (Ring) this;
				else                                hero.belongings.misc = (Ring) this;
			}

			detach( hero.belongings.backpack );

			activate( hero );

			cursedKnown = true;
			if (cursed) {
				equipCursed( hero );
				GLog.n( Messages.get(this, "equip_cursed", this) );
			}

			hero.spendAndNext( TIME_TO_EQUIP );
			return true;

		}

	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){

			if (hero.belongings.artifact == this) {
				hero.belongings.artifact = null;
			} else if (hero.belongings.misc == this) {
				hero.belongings.misc = null;
			} else if (hero.belongings.ring == this){
				hero.belongings.ring = null;
			}

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.artifact == this
				|| hero.belongings.misc == this
				|| hero.belongings.ring == this;
	}

}
