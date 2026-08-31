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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.VaultBeacon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.AmbitiousImpRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultEntranceRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL);
		Painter.fill( level, this, 2, Terrain.EMPTY );

		Point c = center();

		Painter.set(level, c.x-3, c.y-3, Terrain.WALL);
		Painter.set(level, c.x+3, c.y-3, Terrain.WALL);
		Painter.set(level, c.x-3, c.y+3, Terrain.WALL);
		Painter.set(level, c.x+3, c.y+3, Terrain.WALL);

		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 3, Terrain.EMPTY);
		}

		Painter.set(level, c.x-2, c.y-2, Terrain.REGION_DECO);
		Painter.set(level, c.x+2, c.y-2, Terrain.REGION_DECO);
		Painter.set(level, c.x-2, c.y+2, Terrain.REGION_DECO);
		Painter.set(level, c.x+2, c.y+2, Terrain.REGION_DECO);

		Painter.fill( level, left+2, top+4, 7, 3, Terrain.CUSTOM_DECO_EMPTY);
		Painter.fill( level, left+4, top+2, 3, 7, Terrain.CUSTOM_DECO_EMPTY);

		Carpet carpet = new Carpet();
		carpet.setRect(left+2, top+4, 7, 3);
		level.customTiles.add(carpet);

		carpet = new Carpet();
		carpet.setRect(left+4, top+2, 3, 7);
		level.customTiles.add(carpet);

		QuestEntranceInternal vis = new QuestEntranceInternal();
		vis.pos(c.x - 1, c.y - 1);
		level.customTiles.add(vis);

		AmbitiousImpRoom.WallBanners vis2 = new AmbitiousImpRoom.WallBanners();
		vis2.pos(left+2, top+1);
		level.customTerrain.add(vis2);

		ArrayList<Point> pedestalCandidates = new ArrayList<>();
		pedestalCandidates.add(new Point(left+2, c.y));
		pedestalCandidates.add(new Point(right-2, c.y));
		pedestalCandidates.add(new Point(c.x, top+2));
		pedestalCandidates.add(new Point(c.x, bottom-2));

		Point furthest = null;
		float furthestDist = 0;
		for (Point p : pedestalCandidates){
			float dist = 0;
			for (Door d : connected.values()){
				dist += Point.distance(p, d);
			}
			if (furthest == null || dist > furthestDist){
				furthest = p;
				furthestDist = dist;
			}
		}

		//disabled pedestals for now, they don't look great with the new carpet
		//Painter.set(level, furthest, Terrain.PEDESTAL);

		int ofs = 0;
		if (furthest.x == c.x){
			//Painter.fill(level, furthest.x-1, furthest.y, 3, 1, Terrain.PEDESTAL);
			ofs = 1;
		} else {
			//Painter.fill(level, furthest.x, furthest.y-1, 1, 3, Terrain.PEDESTAL);
			ofs = level.width();
		}

		boolean addTorch = Dungeon.isChallenged(Challenges.DARKNESS);
		switch (Random.Int(3)){
			case 0:
				if (addTorch) level.drop(new Torch(), level.pointToCell(furthest)-ofs);
				level.drop(new VaultBeacon(), level.pointToCell(furthest));
				level.drop(new VaultBeacon(), level.pointToCell(furthest)+ofs);
				break;
			case 1:
				level.drop(new VaultBeacon(), level.pointToCell(furthest)-ofs);
				if (addTorch) level.drop(new Torch(), level.pointToCell(furthest));
				level.drop(new VaultBeacon(), level.pointToCell(furthest)+ofs);
				break;
			case 2:
				level.drop(new VaultBeacon(), level.pointToCell(furthest)-ofs);
				level.drop(new VaultBeacon(), level.pointToCell(furthest));
				if (addTorch)level.drop(new Torch(), level.pointToCell(furthest)+ofs);
				break;
		}

		int entrance;
		do {
			entrance = level.pointToCell(center());
		} while (level.findMob(entrance) != null);

		level.transitions.add(new LevelTransition(level,
				entrance,
				LevelTransition.Type.BRANCH_ENTRANCE,
				Dungeon.depth,
				0,
				LevelTransition.Type.BRANCH_EXIT));
	}

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public boolean canConnect(Point p) {
		return (p.x > left+1 && p.x < right-1) || (p.y > top+1 && p.y < bottom-1);
	}

	public static class QuestEntranceInternal extends CustomTilemap {

		{
			texture = Assets.Environment.CITY_QUEST;

			tileW = tileH = 3;
		}

		final int TEX_WIDTH = 256;

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(mapSimpleImage(8, 1, TEX_WIDTH), 3);
			return v;
		}

		//TODO final visuals and text for this

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
