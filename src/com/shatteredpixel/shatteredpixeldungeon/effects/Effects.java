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
package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.noosa.Image;
import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class Effects {

	public enum  Type {
		RIPPLE,
		LIGHTNING,
		WOUND,
		EXCLAMATION,
		CHAIN,
		DEATH_RAY,
		LIGHT_RAY,
		HEALTH_RAY
	};
	
	public static Image get( Type type ) {
		Image icon = new Image( Assets.EFFECTS );
		switch (type) {
			case RIPPLE:
				icon.frame(icon.texture.uvRect(0, 0, 16, 16));
				break;
			case LIGHTNING:
				icon.frame(icon.texture.uvRect(16, 0, 32, 8));
				break;
			case WOUND:
				icon.frame(icon.texture.uvRect(16, 8, 32, 16));
				break;
			case EXCLAMATION:
				icon.frame(icon.texture.uvRect(0, 16, 6, 25));
				break;
			case CHAIN:
				icon.frame(icon.texture.uvRect(6, 16, 11, 22));
				break;
			case DEATH_RAY:
				icon.frame(icon.texture.uvRect(16, 16, 32, 24));
				break;
			case LIGHT_RAY:
				icon.frame(icon.texture.uvRect(16, 23, 32, 31));
				break;
			case HEALTH_RAY:
				icon.frame(icon.texture.uvRect(16, 30, 32, 38));
				break;
		}
		return icon;
	}
}
