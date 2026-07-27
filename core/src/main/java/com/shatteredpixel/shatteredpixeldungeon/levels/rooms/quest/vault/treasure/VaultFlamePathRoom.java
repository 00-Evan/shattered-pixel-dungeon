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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure;

import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class VaultFlamePathRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );

		Point c = center();

		Rect leftSide = new Rect(left+1, top+1, left+3, bottom-1);
		Rect topSide = new Rect(left+1, top+1, right-1, top+3);
		Rect rightSide = new Rect(right-3, top+1, right-1, bottom-1);
		Rect bottomSide = new Rect(left+1, bottom-3, right-1, bottom-1);

		Rect treasure = null;
		if (entrance.x == left){
			if (entrance.y < c.y){
				fillGroup(level, bottomSide, RIGHT);
				fillGroup(level, rightSide, UP);
				fillGroup(level, topSide, LEFT);
				Painter.set(level, (left+1) + level.width()*(top+6), Terrain.EMPTY_SP);
			} else {
				fillGroup(level, topSide, RIGHT);
				fillGroup(level, rightSide, DOWN);
				fillGroup(level, bottomSide, LEFT);
				Painter.set(level, (left+1) + level.width()*(top+4), Terrain.EMPTY_SP);
			}
			treasure = new Rect(left+1, top+5, left+5, top+5);
		} else if (entrance.y == top){
			if (entrance.x < c.x){
				fillGroup(level, rightSide, DOWN);
				fillGroup(level, bottomSide, LEFT);
				fillGroup(level, leftSide, UP);
				Painter.set(level, (left+6) + level.width()*(top+1), Terrain.EMPTY_SP);
			} else {
				fillGroup(level, leftSide, DOWN);
				fillGroup(level, bottomSide, RIGHT);
				fillGroup(level, rightSide, UP);
				Painter.set(level, (left+4) + level.width()*(top+1), Terrain.EMPTY_SP);
			}
			treasure = new Rect(left+5, top+1, left+5, top+5);
		} else if (entrance.x == right){
			if (entrance.y < c.y){
				fillGroup(level, bottomSide, LEFT);
				fillGroup(level, leftSide, UP);
				fillGroup(level, topSide, RIGHT);
				Painter.set(level, (right-1) + level.width()*(top+6), Terrain.EMPTY_SP);
			} else {
				fillGroup(level, topSide, LEFT);
				fillGroup(level, leftSide, DOWN);
				fillGroup(level, bottomSide, RIGHT);
				Painter.set(level, (right-1) + level.width()*(top+4), Terrain.EMPTY_SP);
			}
			treasure = new Rect(right-5, top+5, right-1, top+5);
		} else if (entrance.y == bottom){
			if (entrance.x < c.x){
				fillGroup(level, rightSide, UP);
				fillGroup(level, topSide, LEFT);
				fillGroup(level, leftSide, DOWN);
				Painter.set(level, (left+6) + level.width()*(bottom-1), Terrain.EMPTY_SP);
			} else {
				fillGroup(level, leftSide, UP);
				fillGroup(level, topSide, RIGHT);
				fillGroup(level, rightSide, DOWN);
				Painter.set(level, (left+4) + level.width()*(bottom-1), Terrain.EMPTY_SP);
			}
			treasure = new Rect(left+5, bottom-5, left+5, bottom-1);
		}

		Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.EMPTY_SP);
		int treasurePos = level.pointToCell(Random.element(treasure.getPoints()));
		Item treasureItem = ((VaultLevel)level).createEquipment(1);
		level.drop(treasureItem, treasurePos).type = Heap.Type.CHEST;

		treasureItem = ((VaultLevel) level).findT2SolveItem();
		if (treasureItem == null) {
			treasureItem = ((VaultLevel) level).createConsumabe(1);
		}
		do {
			treasurePos = level.pointToCell(Random.element(treasure.getPoints()));
		} while (level.heaps.get(treasurePos) != null);
		level.drop(treasureItem, treasurePos);

		do {
			treasurePos = level.pointToCell(Random.element(treasure.getPoints()));
		} while (level.heaps.get(treasurePos) != null);
		level.drop(new DwarfToken(), treasurePos);

	}

	//direction the flames are moving in
	final int LEFT = 0;
	final int UP = 1;
	final int RIGHT = 2;
	final int DOWN = 3;

	public void fillGroup(Level level, Rect space, int dir){
		ArrayList<Integer> priorOffsets = new ArrayList<>();
		int ofs;
		int delay;
		if (dir == LEFT){
			for (int y = space.top; y <= space.bottom; y++){
				do {
					ofs = Random.Int(5);
				} while (priorOffsets.contains(ofs));
				delay = 0;
				for (int x = space.right; x >= space.left; x--){
					VaultLevel.VaultFlameTrap.setupTrap(level, x + level.width()*y, delay + ofs, 5, 2);
					delay++;
				}
				priorOffsets.add(ofs);
			}
		} else if (dir == RIGHT){
			for (int y = space.top; y <= space.bottom; y++){
				do {
					ofs = Random.Int(5);
				} while (priorOffsets.contains(ofs));
				delay = 0;
				for (int x = space.left; x <= space.right; x++){
					VaultLevel.VaultFlameTrap.setupTrap(level, x + level.width()*y, delay + ofs, 5, 2);
					delay++;
				}
				priorOffsets.add(ofs);
			}
		} else if (dir == UP){
			for (int x = space.left; x <= space.right; x++){
				do {
					ofs = Random.Int(5);
				} while (priorOffsets.contains(ofs));
				delay = 0;
				for (int y = space.bottom; y >= space.top; y--){
					VaultLevel.VaultFlameTrap.setupTrap(level, x + level.width()*y, delay + ofs, 5, 2);
					delay++;
				}
				priorOffsets.add(ofs);
			}
		} else if (dir == DOWN){
			for (int x = space.left; x <= space.right; x++){
				do {
					ofs = Random.Int(5);
				} while (priorOffsets.contains(ofs));
				delay = 0;
				for (int y = space.top; y <= space.bottom; y++){
					VaultLevel.VaultFlameTrap.setupTrap(level, x + level.width()*y, delay + ofs, 5, 2);
					delay++;
				}
				priorOffsets.add(ofs);
			}
		}
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) > 1 && Math.abs(c.y - p.y) > 1) && super.canConnect(p);
	}
}
