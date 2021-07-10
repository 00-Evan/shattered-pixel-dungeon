/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.levels.rooms.secret;

import com.saqfish.spdnet.items.scrolls.Scroll;
import com.saqfish.spdnet.items.scrolls.ScrollOfIdentify;
import com.saqfish.spdnet.items.scrolls.ScrollOfLullaby;
import com.saqfish.spdnet.items.scrolls.ScrollOfMagicMapping;
import com.saqfish.spdnet.items.scrolls.ScrollOfMirrorImage;
import com.saqfish.spdnet.items.scrolls.ScrollOfRage;
import com.saqfish.spdnet.items.scrolls.ScrollOfRecharging;
import com.saqfish.spdnet.items.scrolls.ScrollOfRemoveCurse;
import com.saqfish.spdnet.items.scrolls.ScrollOfRetribution;
import com.saqfish.spdnet.items.scrolls.ScrollOfTeleportation;
import com.saqfish.spdnet.items.scrolls.ScrollOfTerror;
import com.saqfish.spdnet.items.scrolls.ScrollOfTransmutation;
import com.saqfish.spdnet.levels.Level;
import com.saqfish.spdnet.levels.Terrain;
import com.saqfish.spdnet.levels.painters.Painter;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class SecretLibraryRoom extends SecretRoom {
	
	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}
	
	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}
	
	private static HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();
	static{
		scrollChances.put( ScrollOfIdentify.class,      1f );
		scrollChances.put( ScrollOfRemoveCurse.class,   2f );
		scrollChances.put( ScrollOfMirrorImage.class,   3f );
		scrollChances.put( ScrollOfRecharging.class,    3f );
		scrollChances.put( ScrollOfTeleportation.class, 3f );
		scrollChances.put( ScrollOfLullaby.class,       4f );
		scrollChances.put( ScrollOfMagicMapping.class,  4f );
		scrollChances.put( ScrollOfRage.class,          4f );
		scrollChances.put( ScrollOfRetribution.class,   4f );
		scrollChances.put( ScrollOfTerror.class,        4f );
		scrollChances.put( ScrollOfTransmutation.class, 6f );
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.BOOKSHELF );
		
		Painter.fillEllipse(level, this, 2, Terrain.EMPTY_SP);
		
		Door entrance = entrance();
		if (entrance.x == left || entrance.x == right){
			Painter.drawInside(level, this, entrance, (width() - 3) / 2, Terrain.EMPTY_SP);
		} else {
			Painter.drawInside(level, this, entrance, (height() - 3) / 2, Terrain.EMPTY_SP);
		}
		entrance.set( Door.Type.HIDDEN );
		
		int n = Random.IntRange( 2, 3 );
		HashMap<Class<? extends Scroll>, Float> chances = new HashMap<>(scrollChances);
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);
			
			Class<?extends Scroll> scrollCls = Random.chances(chances);
			chances.put(scrollCls, 0f);
			level.drop( Reflection.newInstance(scrollCls), pos );
		}
	}
	
}
