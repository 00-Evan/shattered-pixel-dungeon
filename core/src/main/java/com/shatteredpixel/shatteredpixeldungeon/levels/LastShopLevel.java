/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LegacyBuilder;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.Random;

public class LastShopLevel extends RegularLevel {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}
	
	@Override
	protected Builder builder() {
		feeling = Feeling.CHASM;
		return new LegacyBuilder(LegacyBuilder.Type.LAST_SHOP,
				width, height, minRoomSize, maxRoomSize);
	}
	
	@Override
	protected void decorate() {
		
		for (int i=0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				
				map[i] = Terrain.EMPTY_DECO;
				
			} else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
				
			} else if (map[i] == Terrain.SECRET_DOOR) {
				
				map[i] = Terrain.DOOR;
				
			}
		}
		
		if (Imp.Quest.isCompleted()) {
			placeSign();
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
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		return pointToCell( roomEntrance.random() );
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	protected boolean[] water() {
		return Patch.generate( width, height, 0.10f, 4, true );
	}

	@Override
	protected boolean[] grass() {
		return Patch.generate( width, height, 0.10f, 3, true );
	}
	
	protected int nTraps() {
		return 0;
	}

	@Override
	public Group addVisuals( ) {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}
}
