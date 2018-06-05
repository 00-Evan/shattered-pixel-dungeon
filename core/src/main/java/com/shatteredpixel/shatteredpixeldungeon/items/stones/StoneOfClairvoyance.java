/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.stones;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Point;

public class StoneOfClairvoyance extends Runestone {
	
	private static final int DIST = 8;
	
	{
		//TODO
		image = ItemSpriteSheet.STONE_TIWAZ;
	}
	
	@Override
	protected void activate(int cell) {
		boolean[] FOV = new boolean[Dungeon.level.length()];
		Point c = Dungeon.level.cellToPoint(cell);
		ShadowCaster.castShadow(c.x, c.y, FOV, DIST);
		
		int sX = Math.max(0, c.x - DIST);
		int eX = Math.min(Dungeon.level.width()-1, c.x + DIST);
		
		int sY = Math.max(0, c.y - DIST);
		int eY = Math.min(Dungeon.level.height()-1, c.y + DIST);
		
		for (int y = sY; y <= eY; y++){
			int curr = y*Dungeon.level.width() + sX;
			for ( int x = sX; x <= eX; x++){
				
				curUser.sprite.parent.addToBack( new CheckedCell( curr ) );
				
				if (FOV[curr] && Dungeon.level.secret[curr]){
					Dungeon.level.discover( curr );
					
					if (Dungeon.level.heroFOV[curr]) {
						GameScene.discoverTile( curr, Dungeon.level.map[curr] );
						ScrollOfMagicMapping.discover( curr );
						
					}
				}
				curr++;
			}
		}
	}
	
}
