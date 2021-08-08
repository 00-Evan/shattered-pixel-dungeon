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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Image;

public enum Icons {
	
	//button icons
	CHECKED,
	UNCHECKED,
	INFO,
	CHALLENGE_OFF,
	CHALLENGE_ON,
	PREFS,
	LANGS,
	EXIT,
	CLOSE,
	ARROW,
	DISPLAY,
	DATA,
	AUDIO,
	TALENT,
	MAGNIFY,
	
	//ingame UI icons
	SKULL,
	BUSY,
	COMPASS,
	SLEEP,
	ALERT,
	LOST,
	TARGET,
	BACKPACK,
	SEED_POUCH,
	SCROLL_HOLDER,
	POTION_BANDOLIER,
	WAND_HOLSTER,
	
	//hero & rankings icons
	DEPTH,
	WARRIOR,
	MAGE,
	ROGUE,
	HUNTRESS,
	
	//main menu icons
	ENTER,
	GOLD,
	RANKINGS,
	BADGES,
	NEWS,
	CHANGES,
	SHPX,
	
	//misc icons
	LIBGDX,
	WATA,
	WARNING,

	//credits icons:
	//16x16
	ALEKS,
	//32x32
	CHARLIE,
	KRISTJAN,
	CUBE_CODE,
	PURIGRO,
	ARCNOR;

	public Image get() {
		return get( this );
	}
	
	public static Image get( Icons type ) {
		Image icon = new Image( Assets.Interfaces.ICONS );
		switch (type) {
		case CHECKED:
			icon.frame( icon.texture.uvRect( 0, 0, 12, 12 ) );
			break;
		case UNCHECKED:
			icon.frame( icon.texture.uvRect( 16, 0, 28, 12 ) );
			break;
		case INFO:
			icon.frame( icon.texture.uvRect( 32, 0, 46, 14 ) );
			break;
		case CHALLENGE_ON:
			icon.frame( icon.texture.uvRect( 48, 0, 62, 12 ) );
			break;
		case CHALLENGE_OFF:
			icon.frame( icon.texture.uvRect( 64, 0, 78, 12 ) );
			break;
		case PREFS:
			icon.frame( icon.texture.uvRect( 80, 0, 94, 14 ) );
			break;
		case LANGS:
			icon.frame( icon.texture.uvRect( 96, 0, 110, 11 ) );
			break;
		case EXIT:
			icon.frame( icon.texture.uvRect( 112, 0, 127, 11 ) );
			break;
		case CLOSE:
			icon.frame( icon.texture.uvRect( 0, 16, 11, 27 ) );
			break;
		case ARROW:
			icon.frame( icon.texture.uvRect( 16, 16, 27, 27 ) );
			break;
		case DISPLAY:
			icon.frame( icon.texture.uvRect( 32, 16, 45, 32 ) );
			break;
		case DATA:
			icon.frame( icon.texture.uvRect( 48, 16, 64, 31 ) );
			break;
		case AUDIO:
			icon.frame( icon.texture.uvRect( 64, 16, 78, 30 ) );
			break;
		case TALENT:
			icon.frame( icon.texture.uvRect( 80, 16, 93, 29 ) );
			break;
		case MAGNIFY:
			icon.frame( icon.texture.uvRect( 96, 16, 110, 30 ) );
			break;
		case SKULL:
			icon.frame( icon.texture.uvRect( 0, 32, 8, 40 ) );
			break;
		case BUSY:
			icon.frame( icon.texture.uvRect( 8, 32, 16, 40 ) );
			break;
		case COMPASS:
			icon.frame( icon.texture.uvRect( 0, 40, 7, 45 ) );
			break;
		case SLEEP:
			icon.frame( icon.texture.uvRect( 16, 32, 25, 40 ) );
			break;
		case ALERT:
			icon.frame( icon.texture.uvRect( 16, 40, 24, 48 ) );
			break;
		case LOST:
			icon.frame( icon.texture.uvRect( 24, 40, 32, 48 ) );
			break;
		case TARGET:
			icon.frame( icon.texture.uvRect( 32, 32, 48, 48 ) );
			break;
		case BACKPACK:
			icon.frame( icon.texture.uvRect( 48, 32, 58, 42 ) );
			break;
		case SCROLL_HOLDER:
			icon.frame( icon.texture.uvRect( 58, 32, 68, 42 ) );
			break;
		case SEED_POUCH:
			icon.frame( icon.texture.uvRect( 68, 32, 78, 42 ) );
			break;
		case WAND_HOLSTER:
			icon.frame( icon.texture.uvRect( 78, 32, 88, 42 ) );
			break;
		case POTION_BANDOLIER:
			icon.frame( icon.texture.uvRect( 88, 32, 98, 42 ) );
			break;
			
		case DEPTH:
			icon.frame( icon.texture.uvRect( 0, 48, 13, 64 ) );
			break;
		case WARRIOR:
			icon.frame( icon.texture.uvRect( 16, 48, 25, 63 ) );
			break;
		case MAGE:
			icon.frame( icon.texture.uvRect( 32, 48, 47, 62 ) );
			break;
		case ROGUE:
			icon.frame( icon.texture.uvRect( 48, 48, 57, 63 ) );
			break;
		case HUNTRESS:
			icon.frame( icon.texture.uvRect( 64, 48, 80, 64 ) );
			break;
		
		case ENTER:
			icon.frame( icon.texture.uvRect( 0, 64, 16, 80 ) );
			break;
		case RANKINGS:
			icon.frame( icon.texture.uvRect( 17, 64, 34, 80 ) );
			break;
		case BADGES:
			icon.frame( icon.texture.uvRect( 34, 64, 50, 80 ) );
			break;
		case NEWS:
			icon.frame( icon.texture.uvRect( 51, 64, 67, 79 ) );
			break;
		case CHANGES:
			icon.frame( icon.texture.uvRect( 68, 64, 83, 79 ) );
			break;
		case SHPX:
			icon.frame( icon.texture.uvRect( 85, 64, 101, 80 ) );
			break;
		case GOLD:
			icon.frame( icon.texture.uvRect( 102, 64, 119, 80 ) );
			break;
		
		case LIBGDX:
			icon.frame( icon.texture.uvRect( 0, 81, 16, 94 ) );
			break;
		case WATA:
			icon.frame( icon.texture.uvRect( 17, 81, 34, 93 ) );
			break;
		case WARNING:
			icon.frame( icon.texture.uvRect( 34, 81, 48, 95 ) );
			break;

		case ALEKS:
			icon.frame( icon.texture.uvRect( 48, 80, 64, 96 ) );
			break;
		//32*32 icons are scaled down to match game's size
		case CHARLIE:
			icon.frame( icon.texture.uvRect( 0, 96, 32, 128 ) );
			icon.scale.set(PixelScene.align(0.49f));
			break;
		case KRISTJAN:
			icon.frame( icon.texture.uvRect( 32, 96, 64, 128 ) );
			icon.scale.set(PixelScene.align(0.49f));
			break;
		case ARCNOR:
			icon.frame( icon.texture.uvRect( 64, 96, 96, 128 ) );
			icon.scale.set(PixelScene.align(0.49f));
			break;
		case PURIGRO:
			icon.frame( icon.texture.uvRect( 96, 96, 128, 128 ) );
			icon.scale.set(PixelScene.align(0.49f));
			break;
		case CUBE_CODE:
			icon.frame( icon.texture.uvRect( 101, 32, 128, 62 ) );
			icon.scale.set(PixelScene.align(0.49f));
			break;

		}
		return icon;
	}
	
	public static Image get( HeroClass cl ) {
		switch (cl) {
		case WARRIOR:
			return get( WARRIOR );
		case MAGE:
			return get( MAGE );
		case ROGUE:
			return get( ROGUE );
		case HUNTRESS:
			return get( HUNTRESS );
		default:
			return null;
		}
	}
}
