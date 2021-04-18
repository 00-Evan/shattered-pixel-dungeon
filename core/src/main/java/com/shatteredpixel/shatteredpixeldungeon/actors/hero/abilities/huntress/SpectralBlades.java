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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class SpectralBlades extends ArmorAbility {

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		armor.charge -= 35;
		Item.updateQuickslot();

		Item proto = new Shuriken();

		final HashMap<Callback, Mob> targets = new HashMap<>();

		for (Mob mob : Dungeon.level.mobs) {
			if (Dungeon.level.distance(hero.pos, mob.pos) <= 12
					&& Dungeon.level.heroFOV[mob.pos]
					&& mob.alignment != Char.Alignment.ALLY) {

				Callback callback = new Callback() {
					@Override
					public void call() {
						hero.attack( targets.get( this ) );
						targets.remove( this );
						if (targets.isEmpty()) {
							Invisibility.dispel();
							hero.spendAndNext( hero.attackDelay() );
						}
					}
				};

				((MissileSprite)hero.sprite.parent.recycle( MissileSprite.class )).
						reset( hero.sprite, mob.pos, proto, callback );

				targets.put( callback, mob );
			}
		}

		if (targets.size() == 0) {
			GLog.w( Messages.get(this, "no_enemies") );
			return;
		}

		hero.sprite.zap( hero.pos );
		hero.busy();
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.SPECTRAL_BLADES_1, Talent.SPECTRAL_BLADES_2, Talent.SPECTRAL_BLADES_3, Talent.SPECTRAL_BLADES_4};
	}
}
