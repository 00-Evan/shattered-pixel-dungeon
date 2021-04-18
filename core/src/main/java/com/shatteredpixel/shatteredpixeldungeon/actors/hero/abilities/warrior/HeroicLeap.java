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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class HeroicLeap extends ArmorAbility {

	private static int LEAP_TIME	= 1;
	private static int SHOCK_TIME	= 5;

	@Override
	protected String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public void activate( ClassArmor armor, Hero hero, Integer target ) {
		if (target != null && target != hero.pos) {

			Ballistica route = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);
			int cell = route.collisionPos;

			//can't occupy the same cell as another char, so move back one.
			if (Actor.findChar( cell ) != null && cell != hero.pos)
				cell = route.path.get(route.dist-1);

			armor.charge -= 35;
			armor.updateQuickslot();

			final int dest = cell;
			hero.busy();
			hero.sprite.jump(hero.pos, cell, new Callback() {
				@Override
				public void call() {
					hero.move(dest);
					Dungeon.level.occupyCell(hero);
					Dungeon.observe();
					GameScene.updateFog();

					for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
						Char mob = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
						if (mob != null && mob != hero && mob.alignment != Char.Alignment.ALLY) {
							Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
						}
					}

					CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
					Camera.main.shake(2, 0.5f);

					Invisibility.dispel();
					hero.spendAndNext(LEAP_TIME);
				}
			});
		}
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.HEROIC_LEAP_1, Talent.HEROIC_LEAP_2, Talent.HEROIC_LEAP_3, Talent.HEROIC_LEAP_4};
	}
}
