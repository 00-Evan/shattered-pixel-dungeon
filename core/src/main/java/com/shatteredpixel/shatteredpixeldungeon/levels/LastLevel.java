/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class LastLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = 8;
	}

	private int pedestal;

	@Override
	protected void setupSize() {
		if (width == 0 || height == 0) {
			width = 16;
			height = 64;
		}
		length = width * height;
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}

	@Override
	public void create() {
		super.create();
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}

	@Override
	protected boolean build() {

		Arrays.fill( map, Terrain.CHASM );

		int mid = width/2;

		Painter.fill( this, 0, height-1, width, 1, Terrain.WALL );
		Painter.fill( this, mid - 1, 10, 3, (height-11), Terrain.EMPTY);
		Painter.fill( this, mid - 2, height - 3, 5, 1, Terrain.EMPTY);
		Painter.fill( this, mid - 3, height - 2, 7, 1, Terrain.EMPTY);

		Painter.fill( this, mid - 2, 9, 5, 7, Terrain.EMPTY);
		Painter.fill( this, mid - 3, 10, 7, 5, Terrain.EMPTY);

		entrance = (height-2) * width() + mid;
		map[entrance] = Terrain.ENTRANCE;

		pedestal = 12*(width()) + mid;
		map[pedestal] = Terrain.PEDESTAL;
		map[pedestal-1-width()] = map[pedestal+1-width()] = map[pedestal-1+width()] = map[pedestal+1+width()] = Terrain.STATUE_SP;

		exit = pedestal;

		int pos = pedestal;

		map[pos-width()] = map[pos-1] = map[pos+1] = map[pos-2] = map[pos+2] = Terrain.WATER;
		pos+=width();
		map[pos] = map[pos-2] = map[pos+2] = map[pos-3] = map[pos+3] = Terrain.WATER;
		pos+=width();
		map[pos-3] = map[pos-2] = map[pos-1] = map[pos] = map[pos+1] = map[pos+2] = map[pos+3] = Terrain.WATER;
		pos+=width();
		map[pos-2] = map[pos+2] = Terrain.WATER;


		feeling = Feeling.NONE;
		viewDistance = 8;

		return true;
	}

	@Override
	protected void decorate() {
		for (int i=0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}
	}

	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		drop( new Amulet(), pedestal );
	}

	@Override
	public int randomRespawnCell() {
		int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
	}
}
