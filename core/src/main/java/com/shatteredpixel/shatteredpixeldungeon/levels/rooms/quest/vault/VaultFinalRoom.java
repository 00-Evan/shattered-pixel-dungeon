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

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ImpStatue;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class VaultFinalRoom extends SpecialRoom {

	@Override
	public int minWidth() {
		return 21;
	}

	@Override
	public int maxWidth() {
		return 21;
	}

	@Override
	public int minHeight() {
		return 21;
	}

	@Override
	public int maxHeight(){
		return 21;
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public boolean canConnect(Point p) {
		return (Math.abs(p.x - center().x) <= 5 || Math.abs(p.y - center().y) <= 5);
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fillEllipse( level, this, 5, Terrain.EMPTY_SP );

		Point c = center();

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );
		Room entry = new EmptyRoom();
		Room treasure = new EmptyRoom();;
		if (entrance.x == left) {
			entry.set(left + 1, top + 5, left + 3, bottom - 5);
			Painter.set(level, left+4, c.y, Terrain.DOOR);
			treasure.set(right - 3,  top + 5, right - 1, bottom - 5);
			Painter.set(level, right-4, c.y, Terrain.LOCKED_DOOR);
		} else if (entrance.x == right){
			treasure.set(left + 1, top + 5, left + 3, bottom - 5);
			Painter.set(level, left+4, c.y, Terrain.LOCKED_DOOR);
			entry.set(right - 3,  top + 5, right - 1, bottom - 5);
			Painter.set(level, right-4, c.y, Terrain.DOOR);
		} else if (entrance.y == top) {
			entry.set(left + 5, top + 1, right-5, top + 3);
			Painter.set(level, c.x, top+4, Terrain.DOOR);
			treasure.set(left + 5, bottom - 3, right-5, bottom - 1);
			Painter.set(level, c.x, bottom-4, Terrain.LOCKED_DOOR);
		} else {
			treasure.set(left + 5, top + 1, right-5, top + 3);
			Painter.set(level, c.x, top+4, Terrain.LOCKED_DOOR);
			entry.set(left + 5, bottom - 3, right-5, bottom - 1);
			Painter.set(level, c.x, bottom-4, Terrain.DOOR);
		}
		Painter.fill(level, entry, Terrain.CUSTOM_DECO_EMPTY);

		Carpet carpet = new Carpet();
		carpet.setRect(entry.left, entry.top, entry.width(), entry.height());
		level.customTiles.add(carpet);

		Painter.fill(level, treasure, Terrain.EMPTY_SP);

		level.drop(new ImpStatue(), level.pointToCell(treasure.random(1)));

		//These items are meant to be taken out with you and so use levelgen logic
		Artifact artif = Generator.randomArtifact();
		if (artif != null){
			artif.identify();
			artif.transferUpgrade(5);
			level.drop(artif, level.pointToCell(treasure.random(1)));
		}
		//TODO more options

	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return false;
	}


}
