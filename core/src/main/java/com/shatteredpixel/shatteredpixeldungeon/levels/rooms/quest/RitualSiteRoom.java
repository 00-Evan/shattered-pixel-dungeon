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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.Arrays;

public class RitualSiteRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 10);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 10);
	}

	public void paint( Level level ) {

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		boolean[] validTopRowCells = new boolean[10];
		Arrays.fill(validTopRowCells, true);
		//set wall tiles to false
		validTopRowCells[0] = validTopRowCells[3] = validTopRowCells[6] = validTopRowCells[9] = false;
		for (Door d : connected.values()){
			if (d.y == top){
				validTopRowCells[d.x-left] = false;
			} else if (d.y <= top+2){
				if (d.x == left)    validTopRowCells[1] = false;
				else                validTopRowCells[8] = false;
			}
		}

		Point topRow = new Point(left, top);
		int cageRow = Random.Int(2);

		for (int i = left; i < right; i+=3){
			topRow.x = i;
			Painter.drawInside(level, this, topRow, 2, Terrain.WALL);

			//rare case of two doors (one side one top), just continue
			if (!validTopRowCells[topRow.x+1 - left] && !validTopRowCells[topRow.x+2 - left]){
				continue;
			}

			if (!validTopRowCells[topRow.x+2 - left]){
				topRow.x += 1;
			} else if (!validTopRowCells[topRow.x+1 - left]){
				topRow.x += 2;
			} else {
				topRow.x += Random.IntRange(1, 2);
			}

			validTopRowCells[topRow.x-left] = false;
			if (cageRow == 0){
				Painter.drawInside(level, this, topRow, 2, Terrain.REGION_DECO);
			} else {
				Table table = new Table();
				Painter.drawInside(level, this, topRow, 2, Terrain.CUSTOM_DECO);
				table.pos(topRow.x, topRow.y + 1);
				level.customTerrain.add(table);
			}
			cageRow--;
		}

		//if there are any spare valid top row cells, stick a cage in one of them
		int tries = 100;
		do {
			int i = Random.IntRange(1, 9);
			if (validTopRowCells[i]){
				topRow.x = left+i;
				Painter.drawInside(level, this, topRow, 1, Terrain.REGION_DECO);
				tries = 0;
			}
		} while (tries-- > 0);

		RitualMarker vis = new RitualMarker();
		Point c = center();
		c.y++;
		vis.pos(c.x - 2, c.y - 2);

		level.customTiles.add(vis);
		
		Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.CUSTOM_DECO_EMPTY);

		level.addItemToSpawn(new CeremonialCandle());
		level.addItemToSpawn(new CeremonialCandle());
		level.addItemToSpawn(new CeremonialCandle());
		level.addItemToSpawn(new CeremonialCandle());

		CeremonialCandle.ritualPos = c.x + (level.width() * c.y);
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return super.canPlaceItem(p, l) && l.distance(CeremonialCandle.ritualPos, l.pointToCell(p)) >= 2;
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return super.canPlaceCharacter(p, l) && l.distance(CeremonialCandle.ritualPos, l.pointToCell(p)) >= 2;
	}

	@Override
	public boolean canConnect(Point p) {
		//wall is placed here
		if (p.y == top && (p.x == left+3 || p.x == left + 6)){
			return false;
		}
		return super.canConnect(p);
	}

	public static class RitualMarker extends CustomTilemap {
		
		{
			texture = Assets.Environment.PRISON_QUEST;
			
			tileW = tileH = 5;
		}
		
		final int TEX_WIDTH = 256;

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(mapSimpleImage(0, 2, TEX_WIDTH), 5);
			return v;
		}

		@Override
		public Image image(int tileX, int tileY) {
			if (tileX == 0 || tileY == 0 || tileX == tileW-1 || tileY == tileH-1){
				return null;
			}
			return super.image(tileX, tileY);
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(this, "desc");
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			if (tileW == 3){
				tileW = tileH = 5;
				tileX -= 1;
				tileY -= 1;
			}
		}
	}

	public static class Table extends CustomTilemap {

		{
			texture = Assets.Environment.PRISON_QUEST;

			tileW = 1;
			tileH = 2;
		}

		final int TEX_WIDTH = 256;

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(mapSimpleImage(0, 0, TEX_WIDTH), 1);
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(this, "desc");
		}

	}

}
