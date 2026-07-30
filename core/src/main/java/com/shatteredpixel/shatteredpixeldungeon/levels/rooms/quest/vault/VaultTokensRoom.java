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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultMirror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultTokenDoor;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultTokensRoom extends VaultLongRoom {

	@Override
	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);

		Point c = center();

		if (wide()){

			int leftDoor = 0, rightDoor = 0;
			for (Door d : connected.values()){
				if (d.x < c.x){
					leftDoor = 1;
					Painter.fill(level, left+1, top+1, (width()-2)/2, height()-2, Terrain.EMPTY);
				} else {
					rightDoor = 1;
					Painter.fill(level, c.x+1, top+1, (width()-2)/2, height()-2, Terrain.EMPTY);
				}
			}

			Painter.fillDiamond(level, left+3, top+1, 9, 9, Terrain.WALL);
			Painter.fillDiamond(level, left+9, top+1, 9, 9, Terrain.WALL);
			Painter.fillDiamond(level, left+5, top+1, 9, 9, Terrain.EMPTY_SP);
			Painter.fillDiamond(level, left+7, top+1, 9, 9, Terrain.EMPTY_SP);
			Painter.fill(level, left+4-leftDoor, c.y, 13+leftDoor+rightDoor, 1, Terrain.EMPTY);
			Painter.fill(level, left+4, c.y, 13, 1, Terrain.EMPTY_SP);
		} else {

			int topdoor = 0, bottomDoor = 0;
			for (Door d : connected.values()){
				if (d.y < c.y){
					topdoor = 1;
					Painter.fill(level, left+1, top+1, width()-2, (height()-2)/2, Terrain.EMPTY);
				} else {
					bottomDoor = 1;
					Painter.fill(level, left+1, c.y+1, width()-2, (height()-2)/2, Terrain.EMPTY);
				}
			}

			Painter.fillDiamond(level, left+1, top+3, 9, 9, Terrain.WALL);
			Painter.fillDiamond(level, left+1, top+9, 9, 9, Terrain.WALL);
			Painter.fillDiamond(level, left+1, top+5, 9, 9, Terrain.EMPTY_SP);
			Painter.fillDiamond(level, left+1, top+7, 9, 9, Terrain.EMPTY_SP);
			Painter.fill(level, c.x, top+4-topdoor, 1, 13+topdoor+bottomDoor, Terrain.EMPTY);
			Painter.fill(level, c.x, top+4, 1, 13, Terrain.EMPTY_SP);
		}

		Painter.fillDiamond(level, c.x-3, c.y-3, 7, 7, Terrain.WALL);
		Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.EMPTY_SP);

		Painter.fill(level, c.x-2, c.y, 5, 1, Terrain.EMPTY_SP);
		Painter.fill(level, c.x, c.y-1, 1, 5, Terrain.EMPTY_SP);

		Painter.set(level, c.x-1, c.y-1, Terrain.REGION_DECO_ALT);
		Painter.set(level, c.x+1, c.y-1, Terrain.REGION_DECO_ALT);

		Carpet carpet = new Carpet();
		carpet.setRect(c.x-1, c.y-1, 3, 3);
		level.customTiles.add(carpet);

		Painter.set(level, c.x, c.y+3, Terrain.DOOR);
		VaultTokenDoor door = new VaultTokenDoor();
		door.pos = c.x + (c.y+3)*level.width();
		level.mobs.add(door);

		VaultMirror mirror = new VaultMirror();
		mirror.createReward(Dungeon.hero.heroClass);
		mirror.pos = c.x + (c.y-1)*level.width();
		level.mobs.add(mirror);

		if (Random.Int(2) == 0) {
			level.drop(((VaultLevel) level).createEquipment(3), c.x - 2 + c.y * level.width());
			level.drop(((VaultLevel) level).createConsumabe(3), c.x + 2 + c.y * level.width());
		} else {
			level.drop(((VaultLevel) level).createEquipment(3), c.x + 2 + c.y * level.width());
			level.drop(((VaultLevel) level).createConsumabe(3), c.x - 2 + c.y * level.width());
		}

		Mob enemy;
		ArrayList<Class<?extends Mob>> toReturn = new ArrayList<>();
		do {
			enemy = level.createMob();
			if (Char.hasProp(enemy, Char.Property.LARGE)){
				toReturn.add(enemy.getClass());
			}
		} while (Char.hasProp(enemy, Char.Property.LARGE));
		int[] wanderPositions;
		if (Random.Int(2) == 0) {
			wanderPositions = new int[]{
					level.pointToCell(new Point(c.x-4, c.y)),
					level.pointToCell(new Point(c.x, c.y-4)),
					level.pointToCell(new Point(c.x+4, c.y)),
					level.pointToCell(new Point(c.x, c.y+4))
			};
		} else {
			wanderPositions = new int[]{
					level.pointToCell(new Point(c.x-4, c.y)),
					level.pointToCell(new Point(c.x, c.y-4)),
					level.pointToCell(new Point(c.x+4, c.y)),
					level.pointToCell(new Point(c.x, c.y+4))
			};
		}
		int idx = Random.Int(4);
		enemy.pos = wanderPositions[idx];
		enemy.setupStealthGameplayWanderPositions(wanderPositions, idx);
		enemy.state = enemy.WANDERING;
		level.mobs.add(enemy);

		for (Class<?extends Mob> cls : toReturn){
			((VaultLevel) level).returnMob(cls);
		}

		//TODO okay lots of token stuff to do here!

	}

	@Override
	public boolean canConnect(Point p) {
		if (!super.canConnect(p)){
			return false;
		}
		if (wide()){
			return p.x < left+5 || p.x > right-5;
		} else {
			return p.y < top+5 || p.y > bottom-5;
		}
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return super.canPlaceItem(p, l) && l.map[l.pointToCell(p)] != Terrain.EMPTY_SP;
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		//no random placement in inner area
		return super.canPlaceCharacter(p, l) && l.map[l.pointToCell(p)] != Terrain.EMPTY_SP;
	}
}
