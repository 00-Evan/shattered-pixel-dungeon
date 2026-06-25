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

package com.shatteredpixel.shatteredpixeldungeon.tiles.custom;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

//TODO currently carpets only have implemented visuals for the dwarven city,
// and also only support being rectangular in shape
public class Carpet extends CustomTilemap {

	{
		texture = Assets.Environment.CARPET;
	}

	protected SparseArray<Integer> tileOverrides = new SparseArray<>();

	public static final int SKIP = -1;
	//first 80 tiles are regular carpet and stiching, so we start customs at 80
	public static final int CITY_STATUE = 80;
	public static final int CITY_PEDESTAL = 81;
	public static final int CITY_ENTRANCE = 82;
	public static final int CITY_STATUE_TR = 83;
	public static final int CITY_STATUE_BR = 84;
	public static final int CITY_STATUE_TL = 85;
	public static final int CITY_STATUE_BL = 86;

	//specify a tile (in carpet coordinates) to override. 0,0 is top-left
	public void overrideTile(int x, int y, int override){
		tileOverrides.put(x + tileW*y, override);
	}

	//specify a tile (int level coords) to override
	public void overrideTile(Level level, int x, int y, int override){
		x -= tileX;
		y -= tileY;
		tileOverrides.put(x + tileW*y, override);
	}

	//specify a tile (in level map index) to override
	public void overrideTile(int tile, Level level, int override){
		int x = tile % level.width() - tileX;
		int y = tile / level.width() - tileY;
		tileOverrides.put(x + tileW*y, override);
	}

	@Override
	public Tilemap create() {
		Tilemap v = super.create();
		int[] data = new int[tileW*tileH];
		int regionOfs = 16 * (int)(Dungeon.depth/5);
		int i = 0;
		for (int y = 0; y < tileH; y++){
			for (int x = 0; x < tileW; x++){
				if (tileOverrides.containsKey(i)){
					data[i] = tileOverrides.get(i);
				} else {
					data[i] = regionOfs;
					if (y == 0) data[i] += 1;
					if (x == tileW - 1) data[i] += 2;
					if (y == tileH - 1) data[i] += 4;
					if (x == 0) data[i] += 8;
				}
				i++;
			}
		}
		v.map( data, tileW );
		return v;
	}

	//for now we assume that overrides should give the text/desc of the base tile

	@Override
	public String name(int tileX, int tileY) {
		int cell = tileX + tileW*tileY;
		if (tileOverrides.containsKey(cell)){
			return null;
		}
		return Messages.get(this, "name");
	}

	@Override
	public String desc(int tileX, int tileY) {
		int cell = tileX + tileW*tileY;
		if (tileOverrides.containsKey(cell)){
			return null;
		}
		return Messages.get(this, "desc");
	}

	public static final String OVERRIDES_KEYS = "overrides_keys";
	public static final String OVERRIDES_VALUES = "overrides_values";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		int[] keys = tileOverrides.keyArray();
		int[] values = new int[keys.length];
		for (int i = 0; i < keys.length; i++){
			values[i] = tileOverrides.get(keys[i]);
		}
		bundle.put(OVERRIDES_KEYS, keys);
		bundle.put(OVERRIDES_VALUES, values);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		tileOverrides.clear();
		int[] keys = bundle.getIntArray(OVERRIDES_KEYS);
		int[] values = bundle.getIntArray(OVERRIDES_VALUES);
		for (int i = 0; i < keys.length; i++){
			tileOverrides.put(keys[i], values[i]);
		}
	}
}
