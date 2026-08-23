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

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;

public class VaultAlternatingFireRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		Point c = center();
		Painter.set(level, c, Terrain.PEDESTAL);

		Item i = ((VaultLevel)level).createEquipment(0);
		if (i != null){
			level.drop( i, level.pointToCell(c) );
		}

		int cell;
		boolean alternate = false;
		for (int x = left+1; x <= right-1; x++){

			for (int y = top+1; y <= bottom-1; y++){
				cell = x + y*level.width();

				if (level.map[cell] != Terrain.PEDESTAL) {
					VaultLevel.VaultFlameTrap.setupTrap(level, cell, alternate ? 1 : 0, 2, 1);
				}
				alternate = !alternate;
			}
		}

	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return super.canPlaceItem(p, l) && p == center();
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return false;
	}

}
