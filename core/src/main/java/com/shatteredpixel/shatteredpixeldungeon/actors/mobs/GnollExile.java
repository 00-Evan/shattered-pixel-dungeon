/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollExileSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;

public class GnollExile extends Gnoll {

	{
		spriteClass = GnollExileSprite.class;

		PASSIVE = new Passive();
		state = PASSIVE;

		loot = Gold.class;
		lootChance = 1f;
	}

	//TODO higher stats, more detailed behaviour, better loot

	//"the spear-wielding gnoll pauses and eyes you suspiciously, but doesn't move to attack"
	//"After a moment the gnoll nods its head toward you and begins to move on"

	// offer 1 ration to get all of its drops

	//has 1 extra reach from their spear
	@Override
	protected boolean canAttack( Char enemy ) {
		if (Dungeon.level.adjacent( pos, enemy.pos )){
			return true;
		}

		if (Dungeon.level.distance( pos, enemy.pos ) <= 2){
			boolean[] passable = BArray.not(Dungeon.level.solid, null);

			//our own tile is always passable
			passable[pos] = true;

			PathFinder.buildDistanceMap(enemy.pos, passable, 2);

			if (PathFinder.distance[pos] <= 2){
				return true;
			}
		}

		return super.canAttack(enemy);
	}

	@Override
	public String description() {
		String desc = super.description();
		if (state == PASSIVE){
			desc += "\n\n" + Messages.get(this, "desc_passive");
		} else {
			desc += "\n\n" + Messages.get(this, "desc_aggro");
		}
		return desc;
	}

	//while passive gnoll exiles wander around
	private class Passive extends Mob.Wandering {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && justAlerted) {

				//TODO pause if hero first enters fov?

				return noticeEnemy();

			} else {

				return continueWandering();

			}
		}
	}
}
