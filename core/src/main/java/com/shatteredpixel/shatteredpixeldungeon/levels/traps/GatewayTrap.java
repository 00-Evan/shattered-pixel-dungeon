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

package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GatewayTrap extends Trap {

	{
		color = TEAL;
		shape = CROSSHAIR;

		disarmedByActivation = false;
		avoidsHallways = true;
	}

	private int telePos = -1;

	@Override
	public void activate() {

		if (telePos == -1){
			for (int i : PathFinder.NEIGHBOURS9){
				Char ch = Actor.findChar(pos + i);
				if (ch != null){
					if (ScrollOfTeleportation.teleportChar(ch)) {
						if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
							((Mob) ch).state = ((Mob) ch).WANDERING;
						}
						telePos = ch.pos;
						break;
					}
				}
				Heap heap = Dungeon.level.heaps.get(pos + i);
				if (heap != null && heap.type == Heap.Type.HEAP){
					int cell = Dungeon.level.randomRespawnCell( null );

					Item item = heap.pickUp();

					if (cell != -1) {
						Dungeon.level.drop( item, cell );
						telePos = cell;
						break;
					}
				}
			}
		}

		if (telePos != -1){

			ArrayList<Integer> telePositions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[telePos+i]
						&& Actor.findChar( telePos+i ) == null){
					telePositions.add(telePos+i);
				}
			}
			Random.shuffle(telePositions);

			if (Dungeon.level.passable[telePos]
					&& Actor.findChar( telePos ) == null){
				telePositions.add(0, telePos);
			}

			ArrayList<Integer> largeCharPositions = new ArrayList<>();
			for (int pos : telePositions){
				if (Dungeon.level.openSpace[pos]){
					largeCharPositions.add(pos);
				}
			}

			for (int i : PathFinder.NEIGHBOURS9){

				Char ch = Actor.findChar(pos + i);
				if (ch != null && !Char.hasProp(ch, Char.Property.IMMOVABLE)){
					int newPos = -1;
					if (Char.hasProp(ch, Char.Property.LARGE)){
						if (!largeCharPositions.isEmpty()){
							newPos = largeCharPositions.get(0);
						}
					} else {
						if (!telePositions.isEmpty()) {
							newPos = telePositions.get(0);
						}
					}

					if (newPos != -1){
						telePositions.remove((Integer)newPos);
						largeCharPositions.remove((Integer)newPos);

						if (ScrollOfTeleportation.teleportToLocation(ch, newPos)){
							if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
								((Mob) ch).state = ((Mob) ch).WANDERING;
							}
						}
					}
				}

				Heap heap = Dungeon.level.heaps.get(pos + i);
				if (heap != null && heap.type == Heap.Type.HEAP){
					Item item = heap.pickUp();
					Dungeon.level.drop( item, telePos );
					if (item instanceof Honeypot.ShatteredPot){
						((Honeypot.ShatteredPot)item).movePot(pos, telePos);
					}
					Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
					CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
				}
			}
		}

	}

	private static final String TELE_POS = "tele_pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TELE_POS, telePos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		telePos = bundle.getInt(TELE_POS);
	}
}
