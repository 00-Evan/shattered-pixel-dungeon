/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.effects;

import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.Assets;

public class BannerSprites {

	public enum  Type {
		PIXEL_DUNGEON,
		BOSS_SLAIN,
		GAME_OVER
	};
	
	public static Image get( Type type ) {
		Image icon = new Image( Assets.BANNERS );
		switch (type) {
		case PIXEL_DUNGEON:
			icon.frame( icon.texture.uvRect( 0, 0, 128, 70 ) );
			break;
		case BOSS_SLAIN:
			icon.frame( icon.texture.uvRect( 0, 70, 128, 105 ) );
			break;
		case GAME_OVER:
			icon.frame( icon.texture.uvRect( 0, 105, 128, 140 ) );
			break;
		}
		return icon;
	}
}
