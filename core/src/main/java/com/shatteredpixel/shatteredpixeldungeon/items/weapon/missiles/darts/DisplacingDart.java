/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplacingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.DISPLACING_DART;
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {

		//attempts to teleport the enemy to a position 8-10 cells away from the hero
		//prioritizes the closest visible cell to the defender, or closest non-visible if no visible are present
		//grants vision on the defender if teleport goes to non-visible
		if (!defender.properties().contains(Char.Property.IMMOVABLE)){
			
			ArrayList<Integer> visiblePositions = new ArrayList<>();
			ArrayList<Integer> nonVisiblePositions = new ArrayList<>();

			PathFinder.buildDistanceMap(attacker.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

			for (int pos = 0; pos < Dungeon.level.length(); pos++){
				if (Dungeon.level.passable[pos]
						&& PathFinder.distance[pos] >= 8
						&& PathFinder.distance[pos] <= 10
						&& (!Char.hasProp(defender, Char.Property.LARGE) || Dungeon.level.openSpace[pos])
						&& Actor.findChar(pos) == null){

					if (Dungeon.level.heroFOV[pos]){
						visiblePositions.add(pos);
					} else {
						nonVisiblePositions.add(pos);
					}

				}
			}

			int chosenPos = -1;

			if (!visiblePositions.isEmpty()) {
				for (int pos : visiblePositions) {
					if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
							> Dungeon.level.trueDistance(defender.pos, pos)){
						chosenPos = pos;
					}
				}
			} else {
				for (int pos : nonVisiblePositions) {
					if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
							> Dungeon.level.trueDistance(defender.pos, pos)){
						chosenPos = pos;
					}
				}
			}
			
			if (chosenPos != -1){
				ScrollOfTeleportation.appear( defender, chosenPos );
				if (!Dungeon.level.heroFOV[chosenPos]){
					Buff.affect(attacker, TalismanOfForesight.CharAwareness.class, 5f).charID = defender.id();
				}
				Dungeon.level.occupyCell(defender );
			}
		
		}
		
		return super.proc(attacker, defender, damage);
	}
}
