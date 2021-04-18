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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class SmokeBomb extends ArmorAbility {

	@Override
	protected String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target != null) {

			PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid,null), 8);

			if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
					!Dungeon.level.heroFOV[target] ||
					Actor.findChar( target ) != null) {

				GLog.w( Messages.get(this, "fov") );
				return;
			}

			armor.charge -= 35;
			Item.updateQuickslot();

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
					Buff.prolong( mob, Blindness.class, Blindness.DURATION/2f );
					if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
					mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
				}
			}
			Buff.affect(hero, Invisibility.class, Invisibility.DURATION/2f);

			CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
			ScrollOfTeleportation.appear( hero, target );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );
			Dungeon.level.occupyCell( hero );
			Dungeon.observe();
			GameScene.updateFog();

			hero.spendAndNext( Actor.TICK );
		}
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.SMOKE_BOMB_1, Talent.SMOKE_BOMB_2, Talent.SMOKE_BOMB_3, Talent.SMOKE_BOMB_4};
	}
}
